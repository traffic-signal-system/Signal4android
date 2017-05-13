package com.aiton.zjb.signal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.model.NodeAndGroupAndAreaInfo;
import com.aiton.zjb.signal.model.NodeListEntity;
import com.aiton.zjb.signal.model.UserInfo;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ChooseAddActivity extends ZjbBaseActivity implements View.OnClickListener {

    MapView mMapView = null;
    private AMap AMap;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    private String mCityCode;
    private String mCity;
    private String mAmapLocationPoiName;
    private String mAddress;
    private ACache mACache;
    private List<NodeListEntity> mNodeList = new ArrayList<>();
    private ArrayList<Marker> mMarkerArrayList;
    private String mDevicesID;
    private UserInfo mUserInfo;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    double latitude = amapLocation.getLatitude();//获取纬度
                    double longitude = amapLocation.getLongitude();//获取经度
                    mCityCode = amapLocation.getCityCode();
                    mCity = amapLocation.getCity();
                    //设置上车地址
                    mAmapLocationPoiName = amapLocation.getPoiName();
                    mAddress = amapLocation.getAddress();
                    mPosition = new LatLng(latitude, longitude);
                    mTextView_location.setText(mPosition.latitude+","+mPosition.longitude);
                    moveCenterMarker(mPosition);
                    getNodeListAsyncHttp();
                    Log.e("onLocationChanged ", "onLocationChanged 定位坐标" + mPosition.toString());
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }

    };
    private LatLng mPosition;
    private TextView mTextView_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_add);
        init();
        initMap(savedInstanceState);
    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void initSP() {
        mACache = ACache.get(ChooseAddActivity.this, Constant.ACACHE.USER);
        mUserInfo = (UserInfo) mACache.getAsObject(Constant.ACACHE.USERINFO);
        mDevicesID = mACache.getAsString(Constant.ACACHE.DEVICES_ID);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findID() {
        mMapView = (MapView) findViewById(R.id.map);
        mTextView_location = (TextView) findViewById(R.id.textView_location);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {
        findViewById(R.id.imageView_back).setOnClickListener(this);
        findViewById(R.id.textView_complete).setOnClickListener(this);
    }

    private void initMap(Bundle savedInstanceState) {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        AMap = mMapView.getMap();
        //设置默认定位按钮是否显示
        UiSettings uiSettings = AMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        AMap.setMyLocationEnabled(true);
        AMap.moveCamera(CameraUpdateFactory.zoomBy(6));
        AMap.setTrafficEnabled(true);
        AMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                mPosition = AMap.getCameraPosition().target;
                Log.e("onTouch ", "onTouch 中心点坐标"+ mPosition.toString());
                mTextView_location.setText(mPosition.latitude+","+mPosition.longitude);
            }
        });
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    private void moveCenterMarker(LatLng latLng) {
        AMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
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
                    NodeAndGroupAndAreaInfo nodeAndGroupAndAreaInfo = GsonUtils.parseJSON(s, NodeAndGroupAndAreaInfo.class);
                    if (nodeAndGroupAndAreaInfo.isSuccess()) {
                        for (int i = 0; i < nodeAndGroupAndAreaInfo.getObject().getAreaList().size(); i++) {
                            NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity areaListEntity = nodeAndGroupAndAreaInfo.getObject().getAreaList().get(i);
                            for (int j = 0; j < areaListEntity.getGroupList().size(); j++) {
                                List<NodeListEntity> nodeList = areaListEntity.getGroupList().get(j).getNodeList();
                                mNodeList.addAll(nodeList);
                            }
                        }
                        initMarker();
                    } else {
                        if (nodeAndGroupAndAreaInfo.getMessageCode()==3){
                            reLogin();
                        }else{
                            Toast.makeText(ChooseAddActivity.this, nodeAndGroupAndAreaInfo.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void initMarker() {
        ArrayList<MarkerOptions> arrayMarkrOption = new ArrayList<>();
        arrayMarkrOption.clear();
        for (int i = 0; i < mNodeList.size(); i++) {
            NodeListEntity nodeListEntity = mNodeList.get(i);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title("" + nodeListEntity.getId());
            if (nodeListEntity.getLinkType() == 1) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.tsc_online));
            } else {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.tsc));
            }
            markerOptions.position(new LatLng(nodeListEntity.getLatitude(), nodeListEntity.getLongitude()));
            arrayMarkrOption.add(markerOptions);
        }
        mMarkerArrayList = AMap.addMarkers(arrayMarkrOption, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    protected void onStop() {
        mLocationClient.stopLocation();//停止定位
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mLocationClient.onDestroy();//销毁定位客户端。
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.textView_complete:
                intent.putExtra(Constant.IntentKey.LOCATIONLAT,mPosition.latitude);
                intent.putExtra(Constant.IntentKey.LOCATIONLng,mPosition.longitude);
                setResult(Constant.REQUEST_RETURN_CODE.CHOOSEADD,intent);
                finishTo();
                break;
            case R.id.imageView_back:
                finishTo();
                break;
        }
    }
}
