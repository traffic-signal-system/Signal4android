package com.aiton.zjb.signal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
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
import com.aiton.zjb.signal.model.NoAction;
import com.aiton.zjb.signal.model.NodeAndGroupAndAreaInfo;
import com.aiton.zjb.signal.model.UserInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class GroupActivity extends ZjbBaseActivity implements View.OnClickListener {

    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;
    private ListView mListViewGroup;
    private ListView mListViewAreaGroup;
    private List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> areaList = new ArrayList<>();
    private List<GroupListEntity> allGroupList = new ArrayList<>();
    private List<GroupListEntity> groupList = new ArrayList<>();
    private TextView mTextViewArea;
    private DrawerLayout drawer_layout;
    private LinearLayout list_right_drawer;
    private MyAreaAdapter mMyAreaAdapter;
    private MyAdapter mMyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manager);
        init();
    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void initSP() {
        mACache = ACache.get(GroupActivity.this, Constant.ACACHE.USER);
        mUserInfo = (UserInfo) mACache.getAsObject(Constant.ACACHE.USERINFO);
        mDevicesID = mACache.getAsString(Constant.ACACHE.DEVICES_ID);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findID() {
        mListViewGroup = (ListView) findViewById(R.id.listViewGroup);
        mListViewAreaGroup = (ListView) findViewById(R.id.listViewAreaGroup);
        mTextViewArea = (TextView) findViewById(R.id.textViewArea);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        list_right_drawer = (LinearLayout) findViewById(R.id.list_right_drawer);
    }

    @Override
    protected void initViews() {
        mMyAdapter = new MyAdapter();
        mListViewGroup.setAdapter(mMyAdapter);
        mMyAreaAdapter = new MyAreaAdapter();
        mListViewAreaGroup.setAdapter(mMyAreaAdapter);
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    protected void setListeners() {
        findViewById(R.id.imageViewBack).setOnClickListener(this);
        findViewById(R.id.textViewAll).setOnClickListener(this);
        findViewById(R.id.textViewAddGroup).setOnClickListener(this);
        mTextViewArea.setOnClickListener(this);
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
        mListViewAreaGroup.setOnItemClickListener(new MyAreaItemClick());
        mListViewGroup.setOnItemClickListener(new MyGroupItemClick());
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLoadingDialog("获取数据");
        getGroupAndArea();
    }

    private void getGroupAndArea() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        String url = Constant.Url.GET_GROUP_AND_AREA;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.i("GroupActivity", "GroupActivity--onSuccess--"+s);
                try {
                    NodeAndGroupAndAreaInfo nodeAndGroupAndAreaInfo = GsonUtils.parseJSON(s, NodeAndGroupAndAreaInfo.class);
                    if (nodeAndGroupAndAreaInfo.isSuccess()) {
                        cancelLoadingDialog();
                        mTextViewArea.setText("域：所有");
                        areaList.clear();
                        allGroupList.clear();
                        groupList.clear();
                        List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> areaListEntityList = nodeAndGroupAndAreaInfo.getObject().getAreaList();
                        areaList.addAll(areaListEntityList);
                        for (int i = 0; i < areaListEntityList.size(); i++) {
                            allGroupList.addAll(areaListEntityList.get(i).getGroupList());
                            groupList.addAll(areaListEntityList.get(i).getGroupList());
                        }
                        mMyAdapter.notifyDataSetChanged();
                        mMyAreaAdapter.notifyDataSetChanged();
                    } else {
                        if (nodeAndGroupAndAreaInfo.getMessageCode() == 3) {
                            reLogin();
                        }
                    }
                    Toast.makeText(GroupActivity.this, nodeAndGroupAndAreaInfo.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void deleteGroup(int position) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("id", groupList.get(position).getId());
        String url = Constant.Url.DELETE_GROUP;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MyArearGroupAdapter", "MyArearGroupAdapter--onSuccess--删除群反沪指" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    Toast.makeText(GroupActivity.this, noAction.getMessage(), Toast.LENGTH_SHORT).show();
                    if (noAction.isSuccess()) {
                        drawer_layout.closeDrawer(list_right_drawer);
                        getGroupAndArea();
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

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.textViewAddGroup:
                intent.setClass(GroupActivity.this, AddGroupActivity.class);
                intent.putExtra(Constant.IntentKey.Area_LIST, new AreaListInfo(areaList));
                startActivityTo(intent);
                break;
            case R.id.textViewAll:
                mTextViewArea.setText("域：所有");
                groupList.clear();
                groupList.addAll(allGroupList);
                mMyAdapter.notifyDataSetChanged();
                drawer_layout.closeDrawer(list_right_drawer);
                break;
            case R.id.imageViewBack:
                finishTo();
                break;
            case R.id.textViewArea:
                drawer_layout.openDrawer(list_right_drawer);
                break;
        }
    }

    class MyGroupItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            intent.setClass(GroupActivity.this, GroupDetailActivity.class);
            intent.putExtra(Constant.IntentKey.GROUP, groupList.get(i));
            startActivityTo(intent);
        }
    }

    class MyAreaItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mTextViewArea.setText("域：" + areaList.get(i).getName());
            groupList.clear();
            groupList.addAll(areaList.get(i).getGroupList());
            mMyAdapter.notifyDataSetChanged();
            drawer_layout.closeDrawer(list_right_drawer);
        }
    }

    class MyAreaAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return areaList.size();
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
            View inflate = getLayoutInflater().inflate(R.layout.area_item_view, null);
            TextView textViewArea = (TextView) inflate.findViewById(R.id.textViewArea);
            textViewArea.setText(areaList.get(position).getName());
            return inflate;
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return groupList.size();
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
            View inflate = getLayoutInflater().inflate(R.layout.group_item_view, null);
            TextView textViewGroupID = (TextView) inflate.findViewById(R.id.textViewGroupID);
            TextView textViewGroupName = (TextView) inflate.findViewById(R.id.textViewGroupName);
            textViewGroupID.setText(groupList.get(position).getId() + "");
            textViewGroupName.setText(groupList.get(position).getGroupName());
            ImageView imageViewEdit = (ImageView) inflate.findViewById(R.id.imageViewEdit);
            ImageView imageViewDelete = (ImageView) inflate.findViewById(R.id.imageViewDelete);
            if (groupList.get(position).getId()==-1){
                imageViewEdit.setVisibility(View.INVISIBLE);
                imageViewDelete.setVisibility(View.INVISIBLE);
            }
            imageViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.IntentKey.Area_LIST, new AreaListInfo(areaList));
                    intent.putExtra(Constant.IntentKey.GROUP_INFO, groupList.get(position));
                    intent.setClass(GroupActivity.this, AddGroupActivity.class);
                    startActivityTo(intent);
                }
            });
            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(GroupActivity.this)
                            .setMessage("是否删除该群")
                            .setNegativeButton("否", null)
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    showLoadingDialog("删除群");
                                    deleteGroup(position);
                                }
                            })
                            .create()
                            .show();
                }
            });
            return inflate;
        }
    }
}
