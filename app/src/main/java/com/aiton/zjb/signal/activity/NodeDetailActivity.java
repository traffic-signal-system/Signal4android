package com.aiton.zjb.signal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.widget.ListView4ScrollView;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.model.AreaListInfo;
import com.aiton.zjb.signal.model.GetUserInfo;
import com.aiton.zjb.signal.model.GroupListEntity;
import com.aiton.zjb.signal.model.GroupListInfo;
import com.aiton.zjb.signal.model.NodeAndGroupAndAreaInfo;
import com.aiton.zjb.signal.model.NodeListEntity;
import com.aiton.zjb.signal.model.UserInfo;
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

public class NodeDetailActivity extends ZjbBaseActivity implements View.OnClickListener {

    private TextView mTextViewManager;
    private TextView mTextViewGroup;
    private TextView mTextViewGroupSer;
    private TextView mTextViewDeviceName;
    private TextView mTextViewdeviceId;
    private TextView mTextViewPort;
    private TextView mTextViewVersion;
    private TextView mTextViewLinkType;
    private TextView mTextViewLocation;
    private List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> mAreaList = new ArrayList<>();
    private List<GroupListEntity> mAllGroupList = new ArrayList<>();
    private Map<Integer, String> areaNameMap = new HashMap<>();
    private Map<Integer, GroupListEntity> groupNameMap = new HashMap<>();
    private NodeListEntity mNode;
    private List<Integer> mGroupID = new ArrayList<>();
    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;
    private List<GetUserInfo.ObjectEntity> mUserInfoList = new ArrayList<>();
    private int managerID = 0;
    private Integer groupIDint = -2;
    private LatLng mLatLng;
    private ImageView mImageViewStatue;
    private int mProtocolType;
    private ImageView imageView_location;
    private ListView4ScrollView mListViewVideo;
    private MyAdapter mAdapter;
    private List<VideoListInfo.ObjectEntity> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_detail);
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
    }

    @Override
    protected void initSP() {
        mACache = ACache.get(NodeDetailActivity.this, Constant.ACACHE.USER);
        mUserInfo = (UserInfo) mACache.getAsObject(Constant.ACACHE.USERINFO);
        mDevicesID = mACache.getAsString(Constant.ACACHE.DEVICES_ID);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findID() {
        mTextViewManager = (TextView) findViewById(R.id.textViewManager);
        mTextViewGroup = (TextView) findViewById(R.id.textViewGroup);
        mTextViewGroupSer = (TextView) findViewById(R.id.textViewGroupSer);
        mTextViewDeviceName = (TextView) findViewById(R.id.textViewDeviceName);
        mTextViewdeviceId = (TextView) findViewById(R.id.textViewdeviceId);
        mTextViewPort = (TextView) findViewById(R.id.textViewPort);
        mTextViewVersion = (TextView) findViewById(R.id.textViewVersion);
        mTextViewLinkType = (TextView) findViewById(R.id.textViewLinkType);
        mTextViewLocation = (TextView) findViewById(R.id.textViewLocation);
        mImageViewStatue = (ImageView) findViewById(R.id.imageViewStatue);
        imageView_location = (ImageView) findViewById(R.id.imageView_location);
        mListViewVideo = (ListView4ScrollView) findViewById(R.id.listViewVideo);
    }

    @Override
    protected void initViews() {
        mAdapter = new MyAdapter();
        mListViewVideo.setAdapter(mAdapter);
        showLoadingDialog("");
        getVideoList();
    }

    @Override
    protected void setListeners() {
        findViewById(R.id.imageViewBack).setOnClickListener(this);
        findViewById(R.id.buttonSee).setOnClickListener(this);
        findViewById(R.id.rela_location).setOnClickListener(this);
    }

    private void getVideoList() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId",mUserInfo.getObject().getId());
        params.put("deviceId",mDevicesID);
        params.put("nodeId",mNode.getId());
        String url = Constant.Url.GET_VIDEOS_BY_NODEID;
        asyncHttpClient.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("NodeDetailActivity", "NodeDetailActivity--onSuccess--获取的摄像头列表"+s);
                VideoListInfo videoListInfo = GsonUtils.parseJSON(s, VideoListInfo.class);
                try {
                    if (videoListInfo.isSuccess()){
                        List<VideoListInfo.ObjectEntity> object = videoListInfo.getObject();
                        videoList.addAll(object);
                        getUsers();
                    }else {
                        if (videoListInfo.getMessageCode()==3){
                            reLogin();
                        }
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
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
                        managerID = mNode.getMaintainAppUserId();
                        String phone = "";
                        for (int i = 0; i < mUserInfoList.size(); i++) {
                            if (mUserInfoList.get(i).getId() == managerID) {
                                phone = mUserInfoList.get(i).getPhone();
                                break;
                            }
                        }
                        groupIDint = mNode.getId();
                        mLatLng = new LatLng(mNode.getLatitude(), mNode.getLongitude());
                        mTextViewManager.setText("ID  " + managerID + "," + "电话  " + phone);
                        mTextViewGroup.setText(groupIDint + "  群：" + groupNameMap.get(mNode.getGroupId()).getGroupName() + "  域：" + areaNameMap.get(groupNameMap.get(mNode.getGroupId()).getAreaId()));
                        mTextViewDeviceName.setText(mNode.getDeviceName());
                        mTextViewdeviceId.setText(mNode.getIpAddress());
                        mTextViewPort.setText(mNode.getPort() + "");
                        mTextViewVersion.setText(mNode.getDeviceVersion());
                        mTextViewLocation.setText(mLatLng.latitude + "," + mLatLng.longitude);
                        mProtocolType = mNode.getProtocolType();
                        if (mProtocolType != 3) {
                            mImageViewStatue.setImageResource(R.mipmap.online);
                        } else {
                            mImageViewStatue.setImageResource(R.mipmap.offline);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        if (getUserInfo.getMessageCode() == 3) {
                            reLogin();
                        } else {
                            Toast.makeText(NodeDetailActivity.this, getUserInfo.getMessage(), Toast.LENGTH_SHORT).show();
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
            case R.id.rela_location:
                intent.setClass(NodeDetailActivity.this,MainActivity.class);
                intent.putExtra(Constant.IntentKey.NODE,mNode);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityTo(intent);
                break;
            case R.id.imageViewBack:
                setResult(Constant.REQUEST_RETURN_CODE.NODE_DETAIL);
                finishTo();
                break;
            case R.id.buttonSee:
                if (mProtocolType==1){
                    intent.setClass(NodeDetailActivity.this, PhaseActivity.class);
                    intent.putExtra(Constant.IntentKey.NODE, mNode);
                    startActivityTo(intent);
                }else{
                    showTipDialog("信号机离线");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Constant.REQUEST_RETURN_CODE.NODE_DETAIL);
        finishTo();
    }

     class MyAdapter extends BaseAdapter {

             @Override
             public int getCount() {
                 return videoList.size();
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
                 View inflate = getLayoutInflater().inflate(R.layout.video_item, null);
                 TextView textViewVideoName= (TextView) inflate.findViewById(R.id.textViewVideoName);
                 TextView textViewVideoIP = (TextView) inflate.findViewById(R.id.textViewVideoIP);
                 ImageView imageViewVideoDelete = (ImageView) inflate.findViewById(R.id.imageViewVideoDelete);
                 imageViewVideoDelete.setVisibility(View.GONE);
                 textViewVideoName.setText(videoList.get(position).getServername());
                 textViewVideoIP.setText(videoList.get(position).getServerip());
                 return inflate;
             }
         }
}
