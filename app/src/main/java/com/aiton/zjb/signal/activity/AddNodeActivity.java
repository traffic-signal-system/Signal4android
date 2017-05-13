package com.aiton.zjb.signal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.utils.IPUtil;
import com.aiton.administrator.shane_library.shane.widget.ListView4ScrollView;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.model.AddNodeBack;
import com.aiton.zjb.signal.model.AreaListInfo;
import com.aiton.zjb.signal.model.GetUserInfo;
import com.aiton.zjb.signal.model.GroupListEntity;
import com.aiton.zjb.signal.model.GroupListInfo;
import com.aiton.zjb.signal.model.NoAction;
import com.aiton.zjb.signal.model.NodeAndGroupAndAreaInfo;
import com.aiton.zjb.signal.model.NodeListEntity;
import com.aiton.zjb.signal.model.UserInfo;
import com.aiton.zjb.signal.model.VideoInfo;
import com.aiton.zjb.signal.model.VideoListInfo;
import com.amap.api.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


public class AddNodeActivity extends ZjbBaseActivity implements View.OnClickListener {

    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;
    private AlertDialog mListDialog;
    private List<GetUserInfo.ObjectEntity> mUserInfoList = new ArrayList<>();
    private String mWhatISShow;
    private TextView mTextViewManager;
    private String manager = "管理人员";
    private List<Integer> mGroupID = new ArrayList<>();
    private String groupID = "群编号";
    private TextView mTextViewGroupID;
    private LatLng mLatLng;
    private TextView mTextViewLocation;
    private int managerID = 0;
    private String managerPhone = "";
    private Integer groupIDint = -2;
    private EditText mEditTextDeviceName;
    private EditText mEditTextdeviceIP;
    private EditText mEditTextPort;
    private EditText mEditTextVerson;
    private TextView mTextViewTitle;
    private NodeListEntity mNode;
    private boolean isEdit = false;
    private List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> mAreaList = new ArrayList<>();
    private List<GroupListEntity> mAllGroupList = new ArrayList<>();
    private Map<Integer, String> areaNameMap = new HashMap<>();
    private Map<Integer, GroupListEntity> groupNameMap = new HashMap<>();
    private ListView4ScrollView mListViewVideo;
    private List<VideoInfo> mVideoInfoList = new ArrayList<>();
    private MyAdapter mAdapter;
    private int mAddVideoCount;
    private AddNodeBack mAddNodeBack;
    private List<VideoListInfo.ObjectEntity> videoListInfoObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jie_dian);
        init();
    }

    @Override
    protected void initIntent() {
        Intent intent = getIntent();
        AreaListInfo AreaListInfo = (AreaListInfo) intent.getSerializableExtra(Constant.IntentKey.Area_LIST);
        mAreaList.clear();
        mAreaList.addAll(AreaListInfo.getAreaList());
        GroupListInfo GroupListInfo = (GroupListInfo) intent.getSerializableExtra(Constant.IntentKey.GROUP_LIST);
        mAllGroupList.clear();
        mAllGroupList.addAll(GroupListInfo.getAllGroupList());
        mGroupID.clear();
        for (int i = 0; i < mAllGroupList.size(); i++) {
            mGroupID.add(mAllGroupList.get(i).getId());
        }
        areaNameMap.clear();
        for (int i = 0; i < mAreaList.size(); i++) {
            areaNameMap.put(mAreaList.get(i).getId(), mAreaList.get(i).getName());
        }
        groupNameMap.clear();
        for (int i = 0; i < mAllGroupList.size(); i++) {
            groupNameMap.put(mAllGroupList.get(i).getId(), mAllGroupList.get(i));
        }
        mNode = (NodeListEntity) intent.getSerializableExtra(Constant.IntentKey.NODE);
        if (mNode != null) {
            isEdit = true;
        } else {
            isEdit = false;
        }
    }

    @Override
    protected void initSP() {
        mACache = ACache.get(AddNodeActivity.this, Constant.ACACHE.USER);
        mUserInfo = (UserInfo) mACache.getAsObject(Constant.ACACHE.USERINFO);
        mDevicesID = mACache.getAsString(Constant.ACACHE.DEVICES_ID);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findID() {
        mTextViewManager = (TextView) findViewById(R.id.textViewManager);
        mTextViewGroupID = (TextView) findViewById(R.id.textViewGroupID);
        mEditTextDeviceName = (EditText) findViewById(R.id.editTextDeviceName);
        mEditTextdeviceIP = (EditText) findViewById(R.id.editTextdeviceIP);
        mEditTextPort = (EditText) findViewById(R.id.editTextPort);
        mEditTextVerson = (EditText) findViewById(R.id.editTextVerson);
        mTextViewTitle = (TextView) findViewById(R.id.textViewTitle);
        mTextViewLocation = (TextView) findViewById(R.id.textViewLocation);
        mListViewVideo = (ListView4ScrollView) findViewById(R.id.listViewVideo);
    }

    @Override
    protected void initViews() {
        mAdapter = new MyAdapter();
        mListViewVideo.setAdapter(mAdapter);
        mListViewVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isEdit){
                    Intent intent = new Intent();
                    intent.putExtra(Constant.IntentKey.VIDEO_INFO, videoListInfoObject.get(i));
                    intent.setClass(AddNodeActivity.this, AddVideoActivity.class);
                    startActivityForResultTo(intent, Constant.REQUEST_RETURN_CODE.EDIT_VIDEO_INFO);
                }
            }
        });
        showLoadingDialog("");
        if (isEdit) {
            getVideoList();
        } else {
            getUsers();
        }
    }

    @Override
    protected void setListeners() {
        findViewById(R.id.rela_manager).setOnClickListener(this);
        findViewById(R.id.rela_GroupID).setOnClickListener(this);
        findViewById(R.id.rela_sequence).setOnClickListener(this);
        findViewById(R.id.rela_linkType).setOnClickListener(this);
        findViewById(R.id.rela_chooseAdd).setOnClickListener(this);
        findViewById(R.id.textView_complete).setOnClickListener(this);
        findViewById(R.id.imageViewBack).setOnClickListener(this);
        findViewById(R.id.add_Video).setOnClickListener(this);
    }

    private void getVideoList() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", mNode.getId());
        String url = Constant.Url.GET_VIDEOS_BY_NODEID;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("NodeDetailActivity", "NodeDetailActivity--onSuccess--获取的摄像头列表" + s);
                VideoListInfo videoListInfo = GsonUtils.parseJSON(s, VideoListInfo.class);
                mVideoInfoList.clear();
                try {
                    if (videoListInfo.isSuccess()) {
                        videoListInfoObject = videoListInfo.getObject();
                        for (int i = 0; i < videoListInfoObject.size(); i++) {
                            VideoListInfo.ObjectEntity objectEntity = videoListInfoObject.get(i);
                            mVideoInfoList.add(new VideoInfo(objectEntity.getServername(), objectEntity.getServerip(), objectEntity.getServerport(), objectEntity.getUsername(), objectEntity.getPassword(), objectEntity.getRebarks()));
                        }
                        mAdapter.notifyDataSetChanged();
                        getUsers();
                    } else {
                        if (videoListInfo.getMessageCode() == 3) {
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

    /**
     * 列表dialog
     *
     * @param list
     */
    private void showListDialog(List<Integer> list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View list_dialog_view = getLayoutInflater().inflate(R.layout.list_dialog, null);
        ListView dialogListView = (ListView) list_dialog_view.findViewById(R.id.listView);
        dialogListView.setAdapter(new MyDialogAdapter(list));
        dialogListView.setOnItemClickListener(new MyItemClick(list));
        mListDialog = builder.setView(list_dialog_view)
                .create();
        mListDialog.show();
        int orientation = getResources().getConfiguration().orientation;
        Window dialogWindow = mListDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        if (orientation == 1) {
            lp.width = (int) (d.widthPixels * 0.8); // 宽度设置为屏幕的0.6
            if (list.size() > 7) {
                lp.height = (int) (d.heightPixels * 0.5);
            }
        } else {
            lp.width = (int) (d.widthPixels * 0.5); // 宽度设置为屏幕的0.6
            if (list.size() >= 5) {
                lp.height = (int) (d.heightPixels * 0.8);
            }
        }
        dialogWindow.setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_RETURN_CODE.CHOOSEADD && resultCode == Constant.REQUEST_RETURN_CODE.CHOOSEADD) {
            double locationLat = (double) data.getSerializableExtra(Constant.IntentKey.LOCATIONLAT);
            double locationLng = (double) data.getSerializableExtra(Constant.IntentKey.LOCATIONLng);
            mLatLng = new LatLng(locationLat, locationLng);
            mTextViewLocation.setText(locationLat + "," + locationLng);
        } else if (requestCode == Constant.REQUEST_RETURN_CODE.VIDEO_INFO && resultCode == Constant.REQUEST_RETURN_CODE.VIDEO_INFO) {
            String videoName = data.getStringExtra(Constant.IntentKey.VIDEO_NAME);
            String videoIP = data.getStringExtra(Constant.IntentKey.VIDEO_IP);
            String videoPort = data.getStringExtra(Constant.IntentKey.VIDEO_PORT);
            String videoUserName = data.getStringExtra(Constant.IntentKey.VIDEO_USER_NAEM);
            String videoUserPassword = data.getStringExtra(Constant.IntentKey.VIDEO_USER_PASSWORD);
            String videoNote = data.getStringExtra(Constant.IntentKey.VIDEO_NOTE);
            mVideoInfoList.add(new VideoInfo(videoName, videoIP, videoPort, videoUserName, videoUserPassword, videoNote));
            mAdapter.notifyDataSetChanged();
            if (isEdit) {
                showLoadingDialog("添加摄像头");
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("userId", mUserInfo.getObject().getId());
                params.put("deviceId", mDevicesID);
                params.put("nodeId", mNode.getId());
                params.put("servername", videoName);
                params.put("serverip", videoIP);
                params.put("serverport", videoPort);
                params.put("username", videoUserName);
                params.put("password", videoUserPassword);
                params.put("rebarks", videoNote);
                String url = Constant.Url.ADD_VIDEO;
                asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String s = new String(responseBody);
                        Log.e("AddNodeActivity", "AddNodeActivity--onSuccess--添加摄像头返回值" + s);
                        try {
                            NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                            if (noAction.isSuccess()) {
                                getVideoList();
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
        }else if (requestCode ==Constant.REQUEST_RETURN_CODE.EDIT_VIDEO_INFO&&resultCode==Constant.REQUEST_RETURN_CODE.EDIT_VIDEO_INFO){
            VideoListInfo.ObjectEntity videoInfo = (VideoListInfo.ObjectEntity) data.getSerializableExtra(Constant.IntentKey.VIDEO_INFO);
            showLoadingDialog("修改摄像头");
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("userId", videoInfo.getId());
            params.put("deviceId", mDevicesID);
            params.put("nodeId", videoInfo.getNodeId());
            params.put("servername", videoInfo.getServername());
            params.put("serverip", videoInfo.getServerip());
            params.put("serverport", videoInfo.getServerport());
            params.put("username", videoInfo.getUsername());
            params.put("password", videoInfo.getPassword());
            params.put("rebarks", videoInfo.getRebarks());
            String url = Constant.Url.ADD_VIDEO;
            asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    Log.e("AddNodeActivity", "AddNodeActivity--onSuccess--添加摄像头返回值" + s);
                    try {
                        NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                        if (noAction.isSuccess()) {
                            getVideoList();
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
    }

    private void getUsers() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String url = Constant.Url.GET_USERS;
        params.put("userId", mUserInfo.getObject().getId() + "");
        params.put("deviceId", mDevicesID);
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                try {
                    GetUserInfo getUserInfo = GsonUtils.parseJSON(s, GetUserInfo.class);
                    if (getUserInfo.isSuccess()) {
                        mUserInfoList.clear();
                        mUserInfoList.addAll(getUserInfo.getObject());
                        if (isEdit) {
                            mTextViewTitle.setText("修改节点");
                            managerID = mNode.getMaintainAppUserId();
                            String phone = "";
                            for (int i = 0; i < mUserInfoList.size(); i++) {
                                if (mUserInfoList.get(i).getId() == managerID) {
                                    phone = mUserInfoList.get(i).getPhone();
                                    break;
                                }
                            }
                            groupIDint = mNode.getGroupId();
                            mLatLng = new LatLng(mNode.getLatitude(), mNode.getLongitude());
                            mTextViewManager.setText("ID  " + managerID + "," + "电话  " + phone);
                            mTextViewGroupID.setText(groupIDint + "  群：" + groupNameMap.get(mNode.getGroupId()).getGroupName() + "  域：" + areaNameMap.get(groupNameMap.get(mNode.getGroupId()).getAreaId()));
                            mEditTextDeviceName.setText(mNode.getDeviceName());
                            mEditTextdeviceIP.setText(mNode.getIpAddress());
                            mEditTextPort.setText(mNode.getPort() + "");
                            mEditTextVerson.setText(mNode.getDeviceVersion());
                            mTextViewLocation.setText(mLatLng.latitude + "," + mLatLng.longitude);
                        }
                    } else {
                        if (getUserInfo.getMessageCode() == 3) {
                            reLogin();
                        } else {
                            Toast.makeText(AddNodeActivity.this, getUserInfo.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    cancelLoadingDialog();
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
            case R.id.add_Video:
                intent.setClass(AddNodeActivity.this, AddVideoActivity.class);
                startActivityForResultTo(intent, Constant.REQUEST_RETURN_CODE.VIDEO_INFO);
                break;
            case R.id.imageViewBack:
                finishTo();
                break;
            case R.id.textView_complete:
                String s = checkInfo();
                if (TextUtils.equals(s, "信息完整")) {
                    showLoadingDialog("");
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("userId", mUserInfo.getObject().getId() + "");
                    params.put("deviceId", mDevicesID);
                    params.put("groupId", groupIDint);
                    params.put("groupSequence", "");
                    params.put("deviceName", mEditTextDeviceName.getText().toString().trim());
                    params.put("ipAddress", mEditTextdeviceIP.getText().toString().trim());
                    params.put("port", mEditTextPort.getText().toString().trim());
                    params.put("deviceVersion", mEditTextVerson.getText().toString().trim());
                    params.put("linkType", 1);
                    params.put("protocolType", "");
                    params.put("longitude", mLatLng.longitude + "");
                    params.put("latitude", mLatLng.latitude + "");
                    params.put("maintainAppUserId", managerID);
                    String url;
                    if (isEdit) {
                        params.put("id", mNode.getId());
                        url = Constant.Url.EDIT_NODE;
                    } else {
                        url = Constant.Url.ADD_NODE;
                    }
                    asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            cancelLoadingDialog();
                            String s = new String(responseBody);
                            Log.e("AddNodeActivity", "AddNodeActivity--onSuccess--添加节点返回值" + s);
                            try {
                                mAddNodeBack = GsonUtils.parseJSON(s, AddNodeBack.class);
                                Toast.makeText(AddNodeActivity.this, mAddNodeBack.getMessage(), Toast.LENGTH_SHORT).show();
                                if (mAddNodeBack.isSuccess()) {
                                    if (!isEdit) {
                                        if (mVideoInfoList.size() > 0) {
                                            mAddVideoCount = 0;
                                            for (int i = 0; i < mVideoInfoList.size(); i++) {
                                                addVideo(i);
                                            }
                                        } else {
                                            finishTo();
                                        }
                                    }
                                } else {
                                    if (mAddNodeBack.getMessageCode() == 3) {
                                        reLogin();
                                    }
                                }
                            } catch (Exception e) {
                                Toast.makeText(AddNodeActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(AddNodeActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                            cancelLoadingDialog();
                        }
                    });
                } else {
                    Toast.makeText(AddNodeActivity.this, s, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rela_chooseAdd:
                intent.setClass(AddNodeActivity.this, ChooseAddActivity.class);
                startActivityForResultTo(intent, Constant.REQUEST_RETURN_CODE.CHOOSEADD);
                break;
            case R.id.rela_linkType:
                Toast.makeText(AddNodeActivity.this, "连接类型暂时传空", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rela_sequence:
                Toast.makeText(AddNodeActivity.this, "群序列暂时传空", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rela_GroupID:
                mWhatISShow = groupID;
                showListDialog(mGroupID);
                break;
            case R.id.rela_manager:
                mWhatISShow = manager;
                List<Integer> listManager = new ArrayList<>();
                for (int i = 0; i < mUserInfoList.size(); i++) {
                    listManager.add(i);
                }
                showListDialog(listManager);
                break;
        }
    }

    private void addVideo(int index) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", mAddNodeBack.getObject().getId());
        VideoInfo videoInfo = mVideoInfoList.get(index);
        params.put("servername", videoInfo.getServername());
        params.put("serverip", videoInfo.getServerip());
        params.put("serverport", videoInfo.getServerport());
        params.put("username", videoInfo.getUsername());
        params.put("password", videoInfo.getPassword());
        params.put("rebarks", videoInfo.getRebarks());
        String url = Constant.Url.ADD_VIDEO;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("AddNodeActivity", "AddNodeActivity--onSuccess--添加摄像头返回值" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        mAddVideoCount++;
                        if (mAddVideoCount == mVideoInfoList.size()) {
                            finishTo();
                        }
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
        finishTo();
    }

    private String checkInfo() {
        if (managerID == 0 && managerPhone.equals("")) {
            return "请选择管理人员";
        } else if (groupIDint == -2) {
            return "请选择群编号";
        } else if (TextUtils.equals("", mEditTextDeviceName.getText().toString().trim())) {
            return "请输入设备名称";
        } else if (TextUtils.equals("", mEditTextdeviceIP.getText().toString().trim()) || !IPUtil.isIP(mEditTextdeviceIP.getText().toString().trim())) {
            return "请输入正确的IP";
        } else if (TextUtils.equals("", mEditTextPort.getText().toString().trim())) {
            return "请输入端口号";
        } else if (TextUtils.equals("", mEditTextVerson.getText().toString().trim())) {
            return "请输入版本号";
        } else if (TextUtils.equals("", mTextViewLocation.getText().toString().trim())) {
            return "请选择地址";
        }
        return "信息完整";
    }

    public class MyItemClick implements AdapterView.OnItemClickListener {

        private List<Integer> list;

        public MyItemClick(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Integer integer = list.get(i);
            mListDialog.dismiss();
            if (TextUtils.equals(manager, mWhatISShow)) {
                managerID = mUserInfoList.get(i).getId();
                managerPhone = mUserInfoList.get(i).getPhone();
                mTextViewManager.setText("ID  " + managerID + "," + "电话  " + managerPhone);
            } else if (TextUtils.equals(groupID, mWhatISShow)) {
                groupIDint = integer;
                mTextViewGroupID.setText(groupIDint + "  群：" + mAllGroupList.get(i).getGroupName() + "  域：" + areaNameMap.get(mAllGroupList.get(i).getAreaId()));
            }

        }
    }

    class MyDialogAdapter extends BaseAdapter {

        private List<Integer> shiji;

        public MyDialogAdapter(List<Integer> shiji) {
            this.shiji = shiji;
        }

        @Override
        public int getCount() {
            return shiji.size();
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
            View mDialog_list_itemView = null;
            if (TextUtils.equals(mWhatISShow, manager)) {
                mDialog_list_itemView = getLayoutInflater().inflate(R.layout.manager_item_view, null);
                TextView textView_managerID = (TextView) mDialog_list_itemView.findViewById(R.id.textView_managerID);
                TextView textView_ManagerPhone = (TextView) mDialog_list_itemView.findViewById(R.id.textView_ManagerPhone);
                textView_managerID.setText(mUserInfoList.get(position).getId() + "");
                textView_ManagerPhone.setText(mUserInfoList.get(position).getPhone());
            } else if (TextUtils.equals(mWhatISShow, groupID)) {
                mDialog_list_itemView = getLayoutInflater().inflate(R.layout.group_info_view, null);
                TextView textView_GroupID = (TextView) mDialog_list_itemView.findViewById(R.id.textView_GroupID);
                TextView textView_groupName = (TextView) mDialog_list_itemView.findViewById(R.id.textView_groupName);
                TextView textViewAreaName = (TextView) mDialog_list_itemView.findViewById(R.id.textViewAreaName);
                textView_GroupID.setText(mGroupID.get(position) + "");
                textView_groupName.setText(mAllGroupList.get(position).getGroupName());
                textViewAreaName.setText(areaNameMap.get(mAllGroupList.get(position).getAreaId()));
            } else {
                mDialog_list_itemView = getLayoutInflater().inflate(R.layout.dialog_list_item, null);
                TextView textListItem = (TextView) mDialog_list_itemView.findViewById(R.id.textListItem);
                textListItem.setText(shiji.get(position) + "");
            }
            return mDialog_list_itemView;
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mVideoInfoList.size();
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
            View inflate = getLayoutInflater().inflate(R.layout.video_item, null);
            TextView textViewVideoName = (TextView) inflate.findViewById(R.id.textViewVideoName);
            TextView textViewVideoIP = (TextView) inflate.findViewById(R.id.textViewVideoIP);
            inflate.findViewById(R.id.imageViewVideoDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(AddNodeActivity.this)
                            .setMessage("是否删除该摄像头")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mVideoInfoList.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                    if (isEdit) {
                                        showLoadingDialog("删除摄像头");
                                        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                        RequestParams params = new RequestParams();
                                        params.put("userId", mUserInfo.getObject().getId());
                                        params.put("deviceId", mDevicesID);
                                        params.put("id", videoListInfoObject.get(position).getId());
                                        String url = Constant.Url.DELETE_VIDEO;
                                        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                String s = new String(responseBody);
                                                Log.e("MyAdapter", "MyAdapter--onSuccess--删除节点返回值"+s);
                                                NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                                                try {
                                                    if (noAction.isSuccess()) {
                                                        getVideoList();
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
                                }
                            })
                            .setNegativeButton("否", null)
                            .create()
                            .show();
                }
            });
            textViewVideoName.setText(mVideoInfoList.get(position).getServername());
            textViewVideoIP.setText(mVideoInfoList.get(position).getServerip());
            return inflate;
        }
    }
}
