package com.aiton.zjb.signal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.constant.GbtDefine;
import com.aiton.zjb.signal.fragment.ConflictFragment;
import com.aiton.zjb.signal.fragment.JieDuanFragment;
import com.aiton.zjb.signal.fragment.ManaulFragment;
import com.aiton.zjb.signal.fragment.PeiShiFragment;
import com.aiton.zjb.signal.fragment.TimeBaseFragment;
import com.aiton.zjb.signal.fragment.XiangWeiFragment;
import com.aiton.zjb.signal.model.NodeListEntity;
import com.aiton.zjb.signal.model.UserInfo;
import com.aiton.zjb.signal.util.ByteUtils;
import com.aiton.zjb.signal.util.UdpClientSocket;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class PhaseActivity extends FragmentActivity {

    private String[] tabsItem = new String[]{
            "手控",
            "时基",
            "配时方案",
            "阶段配时",
            "相位",
            "冲突",
//            "灯检"
    };
    private int[] imgResID = new int[]{
            R.mipmap.tsc_page_manual,
            R.mipmap.tsc_page_basetime,
            R.mipmap.tsc_page_stage,
            R.mipmap.tsc_page_detector,
            R.mipmap.tsc_page_phase,
            R.mipmap.tsc_page_collision,
//            R.mipmap.tsc
    };
    private Class[] fragment = new Class[]{
            ManaulFragment.class,
            TimeBaseFragment.class,
            PeiShiFragment.class,
            JieDuanFragment.class,
            XiangWeiFragment.class,
            ConflictFragment.class,
//            Fragment07.class
    };
    private String mIp;
    private NodeListEntity mNodeListEntity;
    private boolean isOnline = false;
    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phase);
        setRequestedOrientation(Constant.HengShuPing);
        FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtab);
        for (int i = 0; i < tabsItem.length; i++) {
            View inflate = getLayoutInflater().inflate(R.layout.tabs_item, null);
            TextView tabs_text = (TextView) inflate.findViewById(R.id.tabs_text);
            ImageView imgRes = (ImageView) inflate.findViewById(R.id.imgRes);
            imgRes.setImageResource(imgResID[i]);
            tabs_text.setText(tabsItem[i]);
            tabHost.addTab(tabHost.newTabSpec(tabsItem[i]).setIndicator(inflate), fragment[i], null);
        }

        Intent intent = getIntent();
        mIp = intent.getStringExtra(Constant.IntentKey.IP);
        mACache = ACache.get(PhaseActivity.this, Constant.ACACHE.USER);
        mUserInfo = (UserInfo) mACache.getAsObject(Constant.ACACHE.USERINFO);
        mDevicesID = mACache.getAsString(Constant.ACACHE.DEVICES_ID);
        mNodeListEntity = (NodeListEntity) intent.getSerializableExtra(Constant.IntentKey.NODE);
        if (mNodeListEntity != null) {
            isOnline = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (isOnline){
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("userId",mUserInfo.getObject().getId());
            params.put("nodeId",mNodeListEntity.getId());
            String url = Constant.Url.CLOSE_SIGNAL_STATUE;
            asyncHttpClient.post(url,params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    Log.e("PhaseActivity", "PhaseActivity--onSuccess--关闭信号机状态上报"+s);
                    try {

                    }catch (Exception e){

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        UdpClientSocket udpClientSocket = new UdpClientSocket();
                        udpClientSocket.send(mIp, GbtDefine.GBT_PORT, GbtDefine.REPORT_TSC_STATUS_CANCEL);
                        byte[] bytes = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                        String s1 = ByteUtils.bytesToHexString(bytes);
                        Log.e("run ", "run " + s1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        super.onDestroy();
    }
}
