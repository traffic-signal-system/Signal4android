package com.aiton.zjb.signal.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.model.UserInfo;
import com.aiton.zjb.signal.util.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class ChooseActivity extends ZjbBaseActivity implements View.OnClickListener {
    Banner banner;
    //如果你需要考虑更好的体验，可以这么操作


    private Handler handler  ;
    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }
    private ACache mACache;
    private int mHengShuPing;
    private UserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // PushManager.getInstance().initialize(ChooseActivity.this, PushServer.class);
       // PushManager.getInstance().registerPushIntentService(ChooseActivity.this, PushIntentServer.class);
        setContentView(R.layout.welcome);




        handler =  new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what==1){
                    setContentView(R.layout.activity_choose);
                    init();
                    bannerInit();
                }
            }
        };

        new Thread(new Runnable(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(4000) ;
                } catch (Exception e) {
                    // TODO: handle exception
                }
                handler.sendEmptyMessage(1) ;
            }

        }).start() ;
    }

    private void bannerInit() {
        //Fresco.initialize(this);
        if (banner == null) {

            banner = (Banner) findViewById(R.id.banner);
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());
            //设置图片集合
            List<String> images = new ArrayList<String>();
            images.add("http://www.aiton.com.cn/banner/d1.jpg");
            images.add("http://www.aiton.com.cn/banner/d2.jpg");
            images.add("http://www.aiton.com.cn/banner/d3.jpg");
            images.add("http://www.aiton.com.cn/banner/d4.jpg");
            banner.setImages(images);
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }
    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void initSP() {
        mACache = ACache.get(ChooseActivity.this, "HengShuPing");
        if (mACache != null) {
            String hengShuPing = mACache.getAsString("HengShuPing");
            if (hengShuPing!=null) {
                mHengShuPing = Integer.parseInt(hengShuPing);
                Constant.HengShuPing = mHengShuPing;
            }
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findID() {
        findViewById(R.id.button_internet).setOnClickListener(this);
        findViewById(R.id.button_local).setOnClickListener(this);
        findViewById(R.id.button_HengShuPing).setOnClickListener(this);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        ACache aCache = ACache.get(ChooseActivity.this, Constant.ACACHE.USER);
        mUserInfo = (UserInfo) aCache.getAsObject(Constant.ACACHE.USERINFO);
        //开始轮播
        if(banner !=null)
            banner.startAutoPlay();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.button_internet:
                if (mUserInfo == null) {
                    intent.setClass(ChooseActivity.this, LoginActivity.class);
                    startActivityTo(intent);
                } else {
                    intent.setClass(ChooseActivity.this, MainActivity.class);
                    startActivityTo(intent);
                }
                break;
            case R.id.button_local:
                intent.setClass(ChooseActivity.this, LocalActivity.class);
                startActivityTo(intent);
                break;
            case R.id.button_HengShuPing:
                if (getResources().getConfiguration().orientation == 1) {
                    Constant.HengShuPing = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                } else {
                    Constant.HengShuPing = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                }
                mACache.put("HengShuPing", Constant.HengShuPing+"");
                setRequestedOrientation(Constant.HengShuPing);
                break;
        }
    }
}
