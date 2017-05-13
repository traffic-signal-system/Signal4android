package com.aiton.zjb.signal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.model.GroupListEntity;
import com.aiton.zjb.signal.model.GroupNodesList;
import com.aiton.zjb.signal.model.NodeListEntity;
import com.aiton.zjb.signal.model.UserInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class GroupDetailActivity extends ZjbBaseActivity implements View.OnClickListener {

    private TextView mTextViewGroupID;
    private TextView mTextViewGroupName;
    private GroupListEntity mGroupListEntity;
    private TextView textViewEnable;
    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;
    private ListView mListViewGroupNode;
    private List<NodeListEntity> mNodeListEntities = new ArrayList<>();
    private MyAdapter mAdapter;
    private TextView mTextViewNone;
    private Button mButtonGreen;
    private Button mButtonConfigure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        init();
    }

    @Override
    protected void initIntent() {
        Intent intent = getIntent();
        mGroupListEntity = (GroupListEntity) intent.getSerializableExtra(Constant.IntentKey.GROUP);
    }

    @Override
    protected void initSP() {
        mACache = ACache.get(GroupDetailActivity.this, Constant.ACACHE.USER);
        mUserInfo = (UserInfo) mACache.getAsObject(Constant.ACACHE.USERINFO);
        mDevicesID = mACache.getAsString(Constant.ACACHE.DEVICES_ID);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findID() {
        mTextViewGroupID = (TextView) findViewById(R.id.textViewGroupID);
        mTextViewGroupName = (TextView) findViewById(R.id.textViewGroupName);
        textViewEnable = (TextView) findViewById(R.id.textViewEnable);
        mListViewGroupNode = (ListView) findViewById(R.id.listViewGroupNode);
        mTextViewNone = (TextView) findViewById(R.id.textViewNone);
        mButtonConfigure = (Button) findViewById(R.id.buttonConfigure);
        mButtonGreen = (Button) findViewById(R.id.buttonGreen);
    }

    @Override
    protected void initViews() {
        mTextViewNone.setVisibility(View.GONE);
        mTextViewGroupID.setText(mGroupListEntity.getId() + "");
        mTextViewGroupName.setText(mGroupListEntity.getGroupName());
        if (mGroupListEntity.getGroupEnable() == 1) {
            textViewEnable.setText("可用");
        } else {
            textViewEnable.setText("不可用");
        }
        if (mGroupListEntity.getId() == -1) {
            mButtonConfigure.setVisibility(View.GONE);
            mButtonGreen.setVisibility(View.GONE);
        }
        mAdapter = new MyAdapter();
        mListViewGroupNode.setAdapter(mAdapter);
        showLoadingDialog("获取节点");
        getGroupNode();
    }

    @Override
    protected void setListeners() {
        mButtonConfigure.setOnClickListener(this);
        findViewById(R.id.imageViewBack).setOnClickListener(this);
        mButtonGreen.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.buttonGreen:
                if (mNodeListEntities.size()>=2){
                    if (isOffLine()) {
                        showTipDialog("节点状态有误，不能配置");
                    } else {
                        intent.setClass(this, GroupGreenActivity.class);
                        intent.putExtra(Constant.IntentKey.GROUP_NODE, new GroupNodesList(true, "", 1, mNodeListEntities));
                        intent.putExtra(Constant.IntentKey.GROUP,mGroupListEntity);
                        startActivityTo(intent);
                    }
                }else {
                    showTipDialog("节点数必须大于1才能进行绿波配置");
                }
                break;
            case R.id.buttonConfigure:
                if (isOffLine()) {
                    showTipDialog("节点状态有误，不能配置");
                } else {
                    intent.setClass(GroupDetailActivity.this, GroupConfigureActivity.class);
                    intent.putExtra(Constant.IntentKey.GROUP, mGroupListEntity);
                    startActivityTo(intent);
                }
                break;
            case R.id.imageViewBack:
                finishTo();
                break;
        }
    }

    private boolean isOffLine() {
        for (int i = 0; i < mNodeListEntities.size(); i++) {
            if (mNodeListEntities.get(i).getProtocolType() == 3) {
                return true;
            }
        }
        return false;
    }

    private void getGroupNode() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("groupId", mGroupListEntity.getId());
        String url = Constant.Url.GET_NODES_BY_GROUPID;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                cancelLoadingDialog();
                String s = new String(responseBody);
                try {
                    Log.e("GroupDetailActivity", "GroupDetailActivity--onSuccess--" + s);
                    GroupNodesList groupNodesList = GsonUtils.parseJSON(s, GroupNodesList.class);
                    mNodeListEntities.clear();
                    if (groupNodesList.isSuccess()) {
                        Log.e("GroupDetailActivity", "GroupDetailActivity--onSuccess--11111");
                        List<NodeListEntity> object = groupNodesList.getObject();
                        Log.e("GroupDetailActivity", "GroupDetailActivity--onSuccess--22222" + object.size());
                        mNodeListEntities.addAll(object);
                        Log.e("GroupDetailActivity", "GroupDetailActivity--onSuccess--长度" + mNodeListEntities.size());
                        if (mNodeListEntities.size() == 0) {
                            mTextViewNone.setVisibility(View.VISIBLE);
                        } else {
                            mTextViewNone.setVisibility(View.GONE);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        if (groupNodesList.getMessageCode() == 3) {
                            reLogin();
                        }
                    }
                    Toast.makeText(GroupDetailActivity.this, groupNodesList.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(GroupDetailActivity.this, "获取节点异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNodeListEntities.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = getLayoutInflater().inflate(R.layout.group_node_item, null);
            TextView textViewNodeName = (TextView) inflate.findViewById(R.id.textViewNodeName);
            TextView textViewNodeIP = (TextView) inflate.findViewById(R.id.textViewNodeIP);
            ImageView imageViewStatue = (ImageView) inflate.findViewById(R.id.imageViewStatue);
            textViewNodeName.setText(mNodeListEntities.get(position).getDeviceName());
            textViewNodeIP.setText(mNodeListEntities.get(position).getIpAddress() + "");
            if (mNodeListEntities.get(position).getProtocolType() != 3) {
                imageViewStatue.setImageResource(R.mipmap.online);
            } else {
                imageViewStatue.setImageResource(R.mipmap.offline);
            }
            return inflate;
        }
    }
}
