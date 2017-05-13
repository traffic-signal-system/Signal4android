package com.aiton.zjb.signal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.model.AreaListInfo;
import com.aiton.zjb.signal.model.GroupListEntity;
import com.aiton.zjb.signal.model.GroupListInfo;
import com.aiton.zjb.signal.model.NoAction;
import com.aiton.zjb.signal.model.NodeAndGroupAndAreaInfo;
import com.aiton.zjb.signal.model.NodeListEntity;
import com.aiton.zjb.signal.model.UserInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class NodeActivity extends ZjbBaseActivity implements View.OnClickListener {

    private ListView mListView_jieDian;
    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;
    private List<NodeListEntity> mNodeList = new ArrayList<>();
    private List<NodeListEntity> mAllNodeList = new ArrayList<>();
    private List<NodeListEntity> mAreaNodeList = new ArrayList<>();
    private MyAdapter mAdapter;
    private TextView mTvAddJieDian;
    private ArrayList<Integer> GroupID = new ArrayList<>();
    private DrawerLayout drawer_layout;
    private LinearLayout list_right_drawer;
    private TextView mTextViewArea;
    private TextView mTextViewGroup;
    private ListView mListViewAreaGroup;
    private MyArearGroupAdapter mMyArearGroupAdapter;
    private String whatIsShow = "";
    private final String areaStr = "域";
    private final String groupStr = "群";
    private List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> mAreaList = new ArrayList<>();
    private List<GroupListEntity> mGroupList = new ArrayList<>();
    private List<GroupListEntity> mAllGroupList = new ArrayList<>();
//    private List<GroupListEntity> mAReaGroupList = new ArrayList<>();
    private NodeAndGroupAndAreaInfo.ObjectEntity mNodeAndGroupAndAreaInfoObject;
    private boolean isDetialBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jie_dian);
        init();
    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void initSP() {
        mACache = ACache.get(NodeActivity.this, Constant.ACACHE.USER);
        mUserInfo = (UserInfo) mACache.getAsObject(Constant.ACACHE.USERINFO);
        mDevicesID = mACache.getAsString(Constant.ACACHE.DEVICES_ID);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findID() {
        mListView_jieDian = (ListView) findViewById(R.id.listView_jieDian);
        mTvAddJieDian = (TextView) findViewById(R.id.tvAddJieDian);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        list_right_drawer = (LinearLayout) findViewById(R.id.list_right_drawer);
        mTextViewArea = (TextView) findViewById(R.id.textViewArea);
        mTextViewGroup = (TextView) findViewById(R.id.textViewGroup);
        mListViewAreaGroup = (ListView) findViewById(R.id.listViewAreaGroup);
    }

    @Override
    protected void initViews() {
        mAdapter = new MyAdapter();
        mListView_jieDian.setAdapter(mAdapter);
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mMyArearGroupAdapter = new MyArearGroupAdapter();
        mListViewAreaGroup.setAdapter(mMyArearGroupAdapter);
    }

    @Override
    protected void setListeners() {
        mTvAddJieDian.setOnClickListener(this);
        mTextViewArea.setOnClickListener(this);
        mTextViewGroup.setOnClickListener(this);
        findViewById(R.id.imageViewBack).setOnClickListener(this);
        findViewById(R.id.textViewAll).setOnClickListener(this);
        drawer_layout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        mListViewAreaGroup.setOnItemClickListener(new MyAreaGroupOnItemClick());
        mListView_jieDian.setOnItemClickListener(new MyNodeOnItemClickListener());
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.textViewAll:
                if (TextUtils.equals(whatIsShow, areaStr)) {
                    mTextViewArea.setText("域：所有");
                    mTextViewGroup.setText("群：所有");
                    mGroupList.clear();
                    mNodeList.clear();
                    mNodeList.addAll(mAllNodeList);
                    mGroupList.addAll(mAllGroupList);
                } else if (TextUtils.equals(whatIsShow, groupStr)) {
                    mTextViewGroup.setText("群：所有");
                    mNodeList.clear();
                    mNodeList.addAll(mAreaNodeList);
                }
                mAdapter.notifyDataSetChanged();
                drawer_layout.closeDrawer(list_right_drawer);
                break;
            case R.id.textViewGroup:
//                if (!TextUtils.equals(mTextViewArea.getText().toString().trim(),"域：所有")) {
                whatIsShow = groupStr;
                mMyArearGroupAdapter.notifyDataSetChanged();
                drawer_layout.openDrawer(list_right_drawer);
//                } else {
//                Toast.makeText(NodeActivity.this, "请先选择域", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.textViewArea:
                whatIsShow = areaStr;
                mMyArearGroupAdapter.notifyDataSetChanged();
                drawer_layout.openDrawer(list_right_drawer);
                break;
            case R.id.imageViewBack:
                finishTo();
                break;
            case R.id.tvAddJieDian:
                intent.setClass(NodeActivity.this, AddNodeActivity.class);
                intent.putExtra(Constant.IntentKey.GROUP_LIST, new GroupListInfo(mAllGroupList));
                intent.putExtra(Constant.IntentKey.Area_LIST, new AreaListInfo(mAreaList));
                startActivityTo(intent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isDetialBack) {
            showLoadingDialog("");
            getNodeListAsyncHttp();
        }
        isDetialBack = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_RETURN_CODE.NODE_DETAIL:
                isDetialBack = true;
                break;
        }
    }

    /**
     * 获取节点树
     */
    private void getNodeListAsyncHttp() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String url = Constant.Url.GETNODEANDGROUPANDAREA;
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MainActivity", "MainActivity--onSuccess--获取节点树" + s);
                try {
                    mAllNodeList.clear();
                    mAllGroupList.clear();
                    GroupID.clear();
                    mAreaList.clear();
                    mNodeList.clear();
                    mGroupList.clear();
                    NodeAndGroupAndAreaInfo nodeAndGroupAndAreaInfo = GsonUtils.parseJSON(s, NodeAndGroupAndAreaInfo.class);
                    if (nodeAndGroupAndAreaInfo.isSuccess()) {
                        mNodeAndGroupAndAreaInfoObject = nodeAndGroupAndAreaInfo.getObject();
                        mAreaList.addAll(mNodeAndGroupAndAreaInfoObject.getAreaList());
                        for (int i = 0; i < mAreaList.size(); i++) {
                            NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity areaListEntity = mAreaList.get(i);
                            List<GroupListEntity> groupList = areaListEntity.getGroupList();
                            mAllGroupList.addAll(groupList);
                            mGroupList.addAll(groupList);
                            for (int j = 0; j < areaListEntity.getGroupList().size(); j++) {
                                List<NodeListEntity> nodeList = groupList.get(j).getNodeList();
                                GroupID.add(groupList.get(j).getId());
                                mAllNodeList.addAll(nodeList);
                                mNodeList.addAll(nodeList);
                            }
                        }
                        mTextViewArea.setText("域：所有");
                        mTextViewGroup.setText("群：所有");
                        mAdapter.notifyDataSetChanged();
                    } else {
                        if (nodeAndGroupAndAreaInfo.getMessageCode() == 3) {
                            reLogin();
                        } else {
                            Toast.makeText(NodeActivity.this, nodeAndGroupAndAreaInfo.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    cancelLoadingDialog();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(NodeActivity.this, "获取节点列表失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyNodeOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            intent.setClass(NodeActivity.this, NodeDetailActivity.class);
            intent.putExtra(Constant.IntentKey.NODE, mNodeList.get(i));
            intent.putExtra(Constant.IntentKey.GROUP_LIST, new GroupListInfo(mAllGroupList));
            intent.putExtra(Constant.IntentKey.Area_LIST, new AreaListInfo(mAreaList));
            startActivityForResultTo(intent, Constant.REQUEST_RETURN_CODE.NODE_DETAIL);
        }
    }

    class MyAreaGroupOnItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mNodeList.clear();
            if (TextUtils.equals(areaStr, whatIsShow)) {
                mTextViewArea.setText("域：" + mAreaList.get(i).getName());
                mGroupList.clear();
                mAreaNodeList.clear();
                List<GroupListEntity> groupList = mAreaList.get(i).getGroupList();
                mGroupList.addAll(groupList);
                for (int j = 0; j < groupList.size(); j++) {
                    mAreaNodeList.addAll(groupList.get(j).getNodeList());
                    mNodeList.addAll(groupList.get(j).getNodeList());
                }
            } else if (TextUtils.equals(groupStr, whatIsShow)) {
                GroupListEntity groupListEntity = mGroupList.get(i);
                mTextViewGroup.setText("群：" + groupListEntity.getGroupName());
                mNodeList.addAll(groupListEntity.getNodeList());
            }
            mAdapter.notifyDataSetChanged();
            drawer_layout.closeDrawer(list_right_drawer);
        }
    }

    class MyArearGroupAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            switch (whatIsShow) {
                case areaStr:
                    return mAreaList.size();
                case groupStr:
                    return mGroupList.size();
                default:
                    return 0;
            }
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View inflate = getLayoutInflater().inflate(R.layout.areagroup_itemview, null);
            TextView textViewAreaGroup = (TextView) inflate.findViewById(R.id.textViewAreaGroup);
            if (TextUtils.equals(areaStr, whatIsShow)) {
                textViewAreaGroup.setText(mAreaList.get(position).getName());
            } else if (TextUtils.equals(groupStr, whatIsShow)) {
                textViewAreaGroup.setText(mGroupList.get(position).getGroupName());
            }
            return inflate;
        }
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNodeList.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View inflate = getLayoutInflater().inflate(R.layout.jiedian_listitem_view, null);
            TextView textViewJieDianName = (TextView) inflate.findViewById(R.id.textViewJieDianName);
            TextView textViewJieDianIP = (TextView) inflate.findViewById(R.id.textViewJieDianIP);
            ImageView imageViewStatue = (ImageView) inflate.findViewById(R.id.imageViewStatue);
            inflate.findViewById(R.id.imageViewEdit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(NodeActivity.this, AddNodeActivity.class);
                    intent.putExtra(Constant.IntentKey.NODE, mNodeList.get(position));
                    intent.putExtra(Constant.IntentKey.GROUP_LIST, new GroupListInfo(mAllGroupList));
                    intent.putExtra(Constant.IntentKey.Area_LIST, new AreaListInfo(mAreaList));
                    startActivityTo(intent);
                }
            });
            inflate.findViewById(R.id.imageViewDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteNode(position);
                }
            });
            textViewJieDianName.setText(mNodeList.get(position).getDeviceName());
            textViewJieDianIP.setText(mNodeList.get(position).getIpAddress());
            if (mNodeList.get(position).getProtocolType() != 3) {
                imageViewStatue.setImageResource(R.mipmap.online);
            } else {
                imageViewStatue.setImageResource(R.mipmap.offline);
            }
            return inflate;
        }
    }

    /**
     * 删除节点
     *
     * @param position
     */
    private void deleteNode(final int position) {
        new AlertDialog.Builder(NodeActivity.this)
                .setMessage("是否删除信号机")
                .setNegativeButton("否", null)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showLoadingDialog("删除节点");
                        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        params.put("userId", mUserInfo.getObject().getId());
                        params.put("deviceId", mDevicesID);
                        params.put("id", mNodeList.get(position).getId());
                        String url = Constant.Url.DELETE_NODE;
                        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String s = new String(responseBody);
                                Log.e("MyAdapter", "MyAdapter--onSuccess--删除节点返回值" + s);
                                try {
                                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                                    Toast.makeText(NodeActivity.this, noAction.getMessage(), Toast.LENGTH_SHORT).show();
                                    if (noAction.isSuccess()) {
                                        getNodeListAsyncHttp();
                                    } else {
                                        if (noAction.getMessageCode() == 3) {
                                            reLogin();
                                        }
                                    }
                                } catch (Exception e) {

                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });
                    }
                })
                .create()
                .show();
    }
}
