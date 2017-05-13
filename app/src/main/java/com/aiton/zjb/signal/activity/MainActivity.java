package com.aiton.zjb.signal.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.utils.VersionUtils;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.TTSController;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.model.AreaListInfo;
import com.aiton.zjb.signal.model.CheckLiveInfo;
import com.aiton.zjb.signal.model.GroupListEntity;
import com.aiton.zjb.signal.model.GroupListInfo;
import com.aiton.zjb.signal.model.GroupNodesList;
import com.aiton.zjb.signal.model.NodeAndGroupAndAreaInfo;
import com.aiton.zjb.signal.model.NodeListEntity;
import com.aiton.zjb.signal.model.UserInfo;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviStaticInfo;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.help.Tip;
import com.autonavi.tbt.NaviStaticInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends ZjbBaseActivity implements View.OnClickListener, LocationSource, AMapLocationListener, AMapNaviListener {

    private DrawerLayout drawer_layout;
    private ImageView mImg_mine;
    private LinearLayout list_left_drawer;
    MapView mMapView = null;
    private AMap AMap;
    private LatLng mLocationLatLng;
    private float zoom = 16f;
    private UserInfo mUserInfo;
    private Button mButton_exit;
    private ACache mACache;
    private List<NodeListEntity> mNodeList = new ArrayList<>();
    private List<NodeListEntity> searchNodeList = new ArrayList<>();
    private List<NodeListEntity> policeNodeList = new ArrayList<>();
    private ArrayList<Marker> mMarkerArrayList;
    private String mDevicesID;
    private ImageView mImageView_shensuo;
    private View mLinear_tuceng;
    private boolean isZhanKai = false;
    private TextView mTextView_tuCeng;
    private Map<Integer, NodeListEntity> nodeMap = new HashMap<>();
    private List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> mAreaList = new ArrayList<>();
    private List<GroupListEntity> mAllGroupList = new ArrayList<>();
    private NodeListEntity mNode;
    private boolean isFromNodeDtail = false;
    private ListView mListViewSearch;
    private EditText mEditTextSearch;
    private MySearchAdapter mMySearchAdapter;
    private Map<Integer, String> areaNameMap = new HashMap<>();
    private Map<Integer, GroupListEntity> groupNameMap = new HashMap<>();
    private View mMain_menu;
    private View mMain_police;
    private boolean isHideInfoWindows = false;
    private Map<Integer, Boolean> markerSelectMap = new HashMap<>();
    private List<LatLng> policeLatLngs = new ArrayList<>();
    private View mMain_route_plan;
    private String mCityCode;
    private TextView mTextViewStart;
    private TextView mTextViewEnd;
    private Tip mStartTip;
    private Tip mEndTip;
    // 起点终点列表
    private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
    private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
    private TTSController ttsManager;
    private AMapNavi aMapNavi;
    private RouteOverLay mRouteOverLay;
    private RadioGroup mRouteMode;
    private RadioButton mRadioButtonTime;
    private boolean isRoute = false;
    private TextView mTextViewPoliceComfrim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Constant.HengShuPing != getResources().getConfiguration().orientation) {
            setContentView(R.layout.activity_main_land);
        } else {
            setContentView(R.layout.activity_main);
        }
        init();
        initMap(savedInstanceState);
    }

    @Override
    protected void initIntent() {
        Intent intent = getIntent();
        mNode = (NodeListEntity) intent.getSerializableExtra(Constant.IntentKey.NODE);
        if (mNode != null) {
            isFromNodeDtail = true;
        }
    }

    @Override
    protected void initSP() {
        mACache = ACache.get(MainActivity.this, Constant.ACACHE.USER);
        mUserInfo = (UserInfo) mACache.getAsObject(Constant.ACACHE.USERINFO);
        mDevicesID = mACache.getAsString(Constant.ACACHE.DEVICES_ID);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findID() {
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        list_left_drawer = (LinearLayout) findViewById(R.id.list_left_drawer);
        mImg_mine = (ImageView) findViewById(R.id.imageView_mine);
        mMapView = (MapView) findViewById(R.id.map);
        mButton_exit = (Button) findViewById(R.id.button_exit);
        mImageView_shensuo = (ImageView) findViewById(R.id.imageView_shensuo);
        mLinear_tuceng = findViewById(R.id.linear_tuceng);
        mTextView_tuCeng = (TextView) findViewById(R.id.textView_TuCeng);
        mListViewSearch = (ListView) findViewById(R.id.listViewSearch);
        mEditTextSearch = (EditText) findViewById(R.id.editTextSearch);
        mMain_menu = findViewById(R.id.main_menu);
        mMain_police = findViewById(R.id.main_police);
        mMain_route_plan = findViewById(R.id.main_route_plan);
        mTextViewStart = (TextView) findViewById(R.id.textViewStart);
        mTextViewEnd = (TextView) findViewById(R.id.textViewEnd);
        mRouteMode = (RadioGroup) findViewById(R.id.routeMode);
        mRadioButtonTime = (RadioButton) findViewById(R.id.radioButtonTime);
        mTextViewPoliceComfrim = (TextView) findViewById(R.id.textViewPoliceComfrim);
    }

    @Override
    protected void initViews() {
        mLinear_tuceng.setVisibility(View.INVISIBLE);
        mTextView_tuCeng.setFocusable(true);
        mTextView_tuCeng.setFocusableInTouchMode(true);
        mTextView_tuCeng.requestFocus();
        mMySearchAdapter = new MySearchAdapter();
        mListViewSearch.setAdapter(mMySearchAdapter);
    }

    @Override
    protected void setListeners() {
        mImg_mine.setOnClickListener(this);
        mButton_exit.setOnClickListener(this);
        mImageView_shensuo.setOnClickListener(this);
        findViewById(R.id.imageView_reLocation).setOnClickListener(this);
        findViewById(R.id.rela_jieDianManger).setOnClickListener(this);
        findViewById(R.id.imageSearch).setOnClickListener(this);
        findViewById(R.id.rela_groupManager).setOnClickListener(this);
        findViewById(R.id.rela_AreaManager).setOnClickListener(this);
        findViewById(R.id.rela_offLineMap).setOnClickListener(this);
        mEditTextSearch.addTextChangedListener(new MyTextWatcher());
        mListViewSearch.setOnItemClickListener(new MySearchItemClickListener());
        findViewById(R.id.textViewGreenWave).setOnClickListener(this);
        findViewById(R.id.textView_police).setOnClickListener(this);
        findViewById(R.id.textViewSave).setOnClickListener(this);
        findViewById(R.id.textViewPoliceExit).setOnClickListener(this);
        mTextViewPoliceComfrim.setOnClickListener(this);
        findViewById(R.id.textViewBestRoute).setOnClickListener(this);
        findViewById(R.id.imageViewCancle).setOnClickListener(this);
        findViewById(R.id.relaStart).setOnClickListener(this);
        findViewById(R.id.relaEnd).setOnClickListener(this);
        mRouteMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                switch (checkedRadioButtonId) {
                    case R.id.radioButtonTime:
                        Log.e("MainActivity", "MainActivity--onCheckedChanged--时间最短");
                        calculateDriveRoute(PathPlanningStrategy.DRIVING_DEFAULT);
                        break;
                    case R.id.radioButtonDistance:
                        Log.e("MainActivity", "MainActivity--onCheckedChanged--距离最短");
                        calculateDriveRoute(PathPlanningStrategy.DRIVING_SHORTEST_DISTANCE);
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initMap(Bundle savedInstanceState) {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        AMap = mMapView.getMap();

        ttsManager = TTSController.getInstance(this);
        ttsManager.init();
        aMapNavi = AMapNavi.getInstance(this);
        aMapNavi.addAMapNaviListener(this);
        aMapNavi.addAMapNaviListener(ttsManager);
        aMapNavi.setEmulatorNaviSpeed(150);//设置模拟导航的速度
        mRouteOverLay = new RouteOverLay(AMap, null, getApplicationContext());

        //设置默认定位按钮是否显示
        UiSettings uiSettings = AMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        AMap.setTrafficEnabled(true);
        // 设置定位监听
        AMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        AMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        AMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        AMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                if (isHideInfoWindows) {
                    return null;
                }
                View inflate = getLayoutInflater().inflate(R.layout.node_marker, null);
                TextView textViewDeviceName = (TextView) inflate.findViewById(R.id.textViewDeviceName);
                TextView textViewDeviceIP = (TextView) inflate.findViewById(R.id.textViewDeviceIP);
                Button imageViewDetail = (Button) inflate.findViewById(R.id.imageViewDetail);
                final int title = Integer.parseInt(marker.getTitle());
                textViewDeviceName.setText("设备名称：" + nodeMap.get(title).getDeviceName());
                textViewDeviceIP.setText("设备ID：" + nodeMap.get(title).getId());
                if (nodeMap.get(title).getProtocolType() == 1) {
                    imageViewDetail.setBackgroundResource(R.mipmap.online);
                } else {
                    imageViewDetail.setBackgroundResource(R.mipmap.offline);
                }
                imageViewDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, NodeDetailActivity.class);
                        intent.putExtra(Constant.IntentKey.NODE, nodeMap.get(title));
                        intent.putExtra(Constant.IntentKey.GROUP_LIST, new GroupListInfo(mAllGroupList));
                        intent.putExtra(Constant.IntentKey.Area_LIST, new AreaListInfo(mAreaList));
                        startActivityForResultTo(intent, Constant.REQUEST_RETURN_CODE.NODE_DETAIL);
                    }
                });
                return inflate;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
        AMap.setOnMarkerClickListener(new MyMarkerClickListener());
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onArriveDestination(NaviStaticInfo naviStaticInfo) {

    }

    @Override
    public void onArriveDestination(AMapNaviStaticInfo aMapNaviStaticInfo) {

    }

    @Override
    public void onCalculateRouteSuccess() {
        AMapNaviPath naviPath = aMapNavi.getNaviPath();
        if (naviPath == null) {
            return;
        }
        // 获取路径规划线路，显示到地图上
        mRouteOverLay.setAMapNaviPath(naviPath);
        mRouteOverLay.addToMap();
    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    class MyMarkerClickListener implements com.amap.api.maps.AMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            int title = Integer.parseInt(marker.getTitle());
            if (isHideInfoWindows) {
                NodeListEntity nodeListEntity = nodeMap.get(title);
                if (nodeListEntity.getProtocolType() != 3) {
                    if (markerSelectMap.get(title)) {
                        markerSelectMap.put(title, false);
                        LatLng latLng = new LatLng(nodeListEntity.getLatitude(), nodeListEntity.getLongitude());
                        for (int i = 0; i < policeNodeList.size(); i++) {
                            if (latLng.latitude == policeNodeList.get(i).getLatitude() && latLng.longitude == policeNodeList.get(i).getLongitude()) {
                                policeNodeList.remove(i);
                                policeLatLngs.remove(i);
                                break;
                            }
                        }
                        drawPoliceLine();
                    } else {
                        policeNodeList.add(nodeListEntity);
                        policeLatLngs.add(new LatLng(nodeListEntity.getLatitude(), nodeListEntity.getLongitude()));
                        markerSelectMap.put(title, true);
                        drawPoliceLine();
                    }
                } else {
                    showTipDialog("信号机不在线");
                }
            }
            return isHideInfoWindows;
        }
    }

    private void drawPoliceLine() {
        initMarker();
        if (policeLatLngs.size() > 1) {
            PolylineOptions polygonOptions = new PolylineOptions();
            AMap.addPolyline(polygonOptions.addAll(policeLatLngs)
                    .width(10)
                    .color(Color.argb(255, 255, 1, 1)));
        }
    }

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
                    mNodeList.clear();
                    mAllGroupList.clear();
                    mAreaList.clear();
                    markerSelectMap.clear();
                    NodeAndGroupAndAreaInfo nodeAndGroupAndAreaInfo = GsonUtils.parseJSON(s, NodeAndGroupAndAreaInfo.class);
                    if (nodeAndGroupAndAreaInfo.isSuccess()) {
                        mAreaList.addAll(nodeAndGroupAndAreaInfo.getObject().getAreaList());
                        for (int i = 0; i < nodeAndGroupAndAreaInfo.getObject().getAreaList().size(); i++) {
                            NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity areaListEntity = nodeAndGroupAndAreaInfo.getObject().getAreaList().get(i);
                            List<GroupListEntity> groupList = areaListEntity.getGroupList();
                            mAllGroupList.addAll(groupList);
                            for (int j = 0; j < areaListEntity.getGroupList().size(); j++) {
                                List<NodeListEntity> nodeList = areaListEntity.getGroupList().get(j).getNodeList();
                                mNodeList.addAll(nodeList);
                            }
                        }
                        Log.e("MainActivity", "MainActivity--onSuccess--节点数量" + mNodeList.size());
                        areaNameMap.clear();
                        for (int i = 0; i < mAreaList.size(); i++) {
                            areaNameMap.put(mAreaList.get(i).getId(), mAreaList.get(i).getName());
                        }
                        groupNameMap.clear();
                        for (int i = 0; i < mAllGroupList.size(); i++) {
                            groupNameMap.put(mAllGroupList.get(i).getId(), mAllGroupList.get(i));
                        }
                        clearMarkerSelectMap();
                        initMarker();
                    } else {
                        if (nodeAndGroupAndAreaInfo.getMessageCode() == 3) {
                            reLogin();
                        } else {
                            Toast.makeText(MainActivity.this, nodeAndGroupAndAreaInfo.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    cancelLoadingDialog();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "获取节点树异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void moveCenterMarker(LatLng latLng) {
        AMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

    private void initMarker() {
        ArrayList<MarkerOptions> arrayMarkrOption = new ArrayList<>();
        arrayMarkrOption.clear();
        nodeMap.clear();
        AMap.clear(true);
        for (int i = 0; i < mNodeList.size(); i++) {
            NodeListEntity nodeListEntity = mNodeList.get(i);
            nodeMap.put(nodeListEntity.getId(), nodeListEntity);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title("" + nodeListEntity.getId());
            if (nodeListEntity.getProtocolType() != 3) {
                if (markerSelectMap.get(nodeListEntity.getId())) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.tsc_police));
                } else {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.tsc_online));
                }
            } else {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.tsc));
            }
            markerOptions.position(new LatLng(nodeListEntity.getLatitude(), nodeListEntity.getLongitude()));
            arrayMarkrOption.add(markerOptions);
        }
        mMarkerArrayList = AMap.addMarkers(arrayMarkrOption, false);
        if (isFromNodeDtail) {
            for (int j = 0; j < mMarkerArrayList.size(); j++) {
                Marker marker = mMarkerArrayList.get(j);
                if (marker.getTitle().equals(mNode.getId() + "")) {
                    marker.showInfoWindow();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        final Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.textViewSave:
                mTextViewPoliceComfrim.setText("进入紧急救援");
                startPoliceLine();
                break;
            case R.id.relaStart:
                intent.setClass(this, SearchActivity.class);
                intent.putExtra(Constant.IntentKey.CITY_CODE, mCityCode);
                startActivityForResultTo(intent, Constant.REQUEST_RETURN_CODE.START_ADD);
                break;
            case R.id.relaEnd:
                intent.setClass(this, SearchActivity.class);
                intent.putExtra(Constant.IntentKey.CITY_CODE, mCityCode);
                startActivityForResultTo(intent, Constant.REQUEST_RETURN_CODE.END_ADD);
                break;
            case R.id.imageViewCancle:
                routeExit();
                break;
            case R.id.textViewBestRoute:
                isRoute = true;
                mMain_route_plan.setVisibility(View.VISIBLE);
                mRouteMode.setVisibility(View.GONE);
                mMain_menu.setVisibility(View.GONE);
                break;
            case R.id.textViewPoliceComfrim:
                if (policeNodeList.size() > 1) {
                    intent.putExtra(Constant.IntentKey.GROUP_NODE, new GroupNodesList(true, "", 1, policeNodeList));
                    intent.setClass(MainActivity.this, PoliceLineActivity.class);
                    startActivityTo(intent);
                } else {
                    showTipDialog("必须选两个或以上节点");
                }
                break;
            case R.id.textViewPoliceExit:
                policeExit();
                break;
            case R.id.textView_police:
                mTextViewPoliceComfrim.setText("进入警卫任务");
                startPoliceLine();
                break;
            case R.id.textViewGreenWave:
                intent.setClass(MainActivity.this, GroupActivity.class);
                startActivityTo(intent);
                break;
            case R.id.rela_offLineMap:
                intent.setClass(MainActivity.this, OffLineMapActivity.class);
                startActivityTo(intent);
                break;
            case R.id.rela_AreaManager:
                intent.setClass(MainActivity.this, AreaActivity.class);
                startActivityTo(intent);
                break;
            case R.id.rela_groupManager:
                intent.setClass(MainActivity.this, GroupActivity.class);
                startActivityTo(intent);
                break;
            case R.id.imageSearch:

                break;
            case R.id.rela_jieDianManger:
                intent.setClass(MainActivity.this, NodeActivity.class);
                startActivityTo(intent);
                break;
            case R.id.imageView_shensuo:
                isZhanKai = !isZhanKai;
                if (isZhanKai) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fromtoptobuttomslow);
                    mLinear_tuceng.startAnimation(animation);
                    mLinear_tuceng.setVisibility(View.VISIBLE);
                    Animation animation01 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.roat_anim);
                    animation.setFillAfter(true);
                    if (animation != null) {
                        mImageView_shensuo.startAnimation(animation01);
                    }
                    mImageView_shensuo.setImageResource(R.mipmap.shousuo);
                } else {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.frombuttomtotopslow);
                    mLinear_tuceng.startAnimation(animation);
                    mLinear_tuceng.setVisibility(View.INVISIBLE);
                    Animation animation01 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.roat_anim);
                    animation.setFillAfter(true);
                    if (animation != null) {
                        mImageView_shensuo.startAnimation(animation01);
                    }
                    mImageView_shensuo.setImageResource(R.mipmap.zhankai);
                }
                break;
            case R.id.imageView_mine:
                drawer_layout.openDrawer(list_left_drawer);
                break;
            case R.id.imageView_reLocation:
                CameraPosition cameraPosition = AMap.getCameraPosition();
                zoom = cameraPosition.zoom;
                isFromNodeDtail = false;
                showLoadingDialog("重新定位");
                mLocationClient.startLocation();//启动定位
                break;
            case R.id.button_exit:
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("确定要退出登录吗？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mACache.clear();
                                finishTo();
                                intent.setClass(MainActivity.this, LoginActivity.class);
                                startActivityTo(intent);
                            }
                        })
                        .setNegativeButton("否", null)
                        .create()
                        .show();
                break;
        }
    }

    private void startPoliceLine() {
        isHideInfoWindows = true;
        for (int i = 0; i < mMarkerArrayList.size(); i++) {
            mMarkerArrayList.get(i).setInfoWindowEnable(false);
            mMarkerArrayList.get(i).hideInfoWindow();
        }
        clearMarkerSelectMap();
        //清空警卫路线节点坐标
        policeNodeList.clear();
        policeLatLngs.clear();
        showTipDialog("请依次选择节点，点击选中或取消，选完后点击完成，进入操作界面");
        mMain_menu.setVisibility(View.GONE);
        mMain_police.setVisibility(View.VISIBLE);
    }

    private void routeExit() {
        isRoute = false;
        mMain_route_plan.setVisibility(View.GONE);
        mMain_menu.setVisibility(View.VISIBLE);
        mRouteOverLay.removeFromMap();
    }

    private void policeExit() {
        isHideInfoWindows = false;
        for (int i = 0; i < mMarkerArrayList.size(); i++) {
            mMarkerArrayList.get(i).setInfoWindowEnable(true);
        }
        clearMarkerSelectMap();
        initMarker();
        mMain_menu.setVisibility(View.VISIBLE);
        mMain_police.setVisibility(View.GONE);
    }

    /**
     * 计算驾车路线
     */
    private void calculateDriveRoute(int mode) {
        if (mStartTip != null && mEndTip != null) {
            mRouteMode.setVisibility(View.VISIBLE);
            mStartPoints.clear();
            mEndPoints.clear();
            mStartPoints.add(new NaviLatLng(mStartTip.getPoint().getLatitude(), mStartTip.getPoint().getLongitude()));
            mEndPoints.add(new NaviLatLng(mEndTip.getPoint().getLatitude(), mEndTip.getPoint().getLongitude()));
            moveCenterMarker(new LatLng(mStartTip.getPoint().getLatitude(), mStartTip.getPoint().getLongitude()));
            boolean isSuccess = aMapNavi.calculateDriveRoute(mStartPoints,
                    mEndPoints, null, mode);
            if (!isSuccess) {
                Toast.makeText(MainActivity.this, "路线计算失败,检查参数情况", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_RETURN_CODE.START_ADD:
                if (resultCode == Constant.REQUEST_RETURN_CODE.RESULT_ADD) {
                    mStartTip = data.getParcelableExtra(Constant.IntentKey.SEARCH_ADD);
                    mTextViewStart.setText(mStartTip.getName());
                    if (mRadioButtonTime.isChecked()) {
                        calculateDriveRoute(PathPlanningStrategy.DRIVING_DEFAULT);
                    } else {
                        mRadioButtonTime.setChecked(true);
                    }
                }
                break;
            case Constant.REQUEST_RETURN_CODE.END_ADD:
                if (resultCode == Constant.REQUEST_RETURN_CODE.RESULT_ADD) {
                    mEndTip = data.getParcelableExtra(Constant.IntentKey.SEARCH_ADD);
                    mTextViewEnd.setText(mEndTip.getName());
                    calculateDriveRoute(PathPlanningStrategy.DRIVING_DEFAULT);
                    if (mRadioButtonTime.isChecked()) {
                        calculateDriveRoute(PathPlanningStrategy.DRIVING_DEFAULT);
                    } else {
                        mRadioButtonTime.setChecked(true);
                    }
                }
                break;
        }
    }

    private void clearMarkerSelectMap() {
        for (int i = 0; i < mNodeList.size(); i++) {
            NodeListEntity nodeListEntity = mNodeList.get(i);
            markerSelectMap.put(nodeListEntity.getId(), false);
        }
    }

    /**
     * 检查版本是否可用
     */
    private void checkLive() {
        final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        showLoadingDialog("获取数据");
        RequestParams params = new RequestParams();
        String url = Constant.Url.CHECK_LIVE;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.i("LoginActivity", "LoginActivity--onSuccess--" + s);
                try {
                    CheckLiveInfo checkLiveInfo = GsonUtils.parseJSON(s, CheckLiveInfo.class);
                    if (checkLiveInfo.isSuccess()) {
                        if (VersionUtils.getCurrVersion(MainActivity.this) >= checkLiveInfo.getObject().getAndroidVersionForMaintainApp()) {
                            getNodeListAsyncHttp();
                        } else {
                            dialogFinish("当前版本不可用");
                        }
                    } else {
                        dialogFinish(checkLiveInfo.getMessage());
                    }
                } catch (Exception e) {
                    dialogFinish("系统出错");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("LoginActivity", "LoginActivity--onFailure--检查版本连接错误");
                cancelLoadingDialog();
                dialogFinish("网络出错");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
        }
        aMapNavi.destroy();
        ttsManager.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                cancelLoadingDialog();
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                //取出经纬度
                mLocationLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                mCityCode = aMapLocation.getCityCode();
                if (!isFromNodeDtail) {
                    AMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocationLatLng, zoom));
                } else {
                    AMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mNode.getLatitude(), mNode.getLongitude()), zoom));
                }
                checkLive();
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    OnLocationChangedListener mListener;
    AMapLocationClient mLocationClient;
    AMapLocationClientOption mLocationOption;

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置是否只定位一次,默认为false
            mLocationOption.setOnceLocation(true);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            showLoadingDialog("正在定位");
            mLocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    class MySearchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return searchNodeList.size();
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
            View inflate = getLayoutInflater().inflate(R.layout.search_item, null);
            TextView textViewSearchNode = (TextView) inflate.findViewById(R.id.textViewSearchNode);
            TextView textViewSearchGroup = (TextView) inflate.findViewById(R.id.textViewSearchGroup);
            TextView textViewSearchArea = (TextView) inflate.findViewById(R.id.textViewSearchArea);
            textViewSearchNode.setText(searchNodeList.get(position).getDeviceName());
            textViewSearchGroup.setText("所属群：" + groupNameMap.get(searchNodeList.get(position).getGroupId()).getGroupName());
            textViewSearchArea.setText("所属域：" + areaNameMap.get(groupNameMap.get(searchNodeList.get(position).getGroupId()).getAreaId()));
            return inflate;
        }
    }

    class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                mListViewSearch.setVisibility(View.GONE);
            } else {
                searchNodeList.clear();
                for (int i = 0; i < mNodeList.size(); i++) {
                    NodeListEntity nodeListEntity = mNodeList.get(i);
                    if (nodeListEntity.getDeviceName().contains(s)) {
                        searchNodeList.add(nodeListEntity);
                    }
                }
                if (searchNodeList.size() > 0) {
                    mListViewSearch.setVisibility(View.VISIBLE);
                } else {
                    mEditTextSearch.setError("没有找到");
                }
                mMySearchAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class MySearchItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mEditTextSearch.setText("");
            mListViewSearch.setVisibility(View.GONE);
            NodeListEntity nodeListEntity = searchNodeList.get(i);
            AMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(nodeListEntity.getLatitude(), nodeListEntity.getLongitude()), zoom));
            for (int j = 0; j < mMarkerArrayList.size(); j++) {
                Marker marker = mMarkerArrayList.get(j);
                if (marker.getTitle().equals(nodeListEntity.getId() + "")) {
                    marker.showInfoWindow();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(list_left_drawer)) {
            drawer_layout.closeDrawer(list_left_drawer);
        } else if (isHideInfoWindows) {
            policeExit();
        } else if (isRoute) {
            routeExit();
        } else {
            finishTo();
        }
    }
}
