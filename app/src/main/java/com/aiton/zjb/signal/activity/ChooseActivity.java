package com.aiton.zjb.signal.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.model.UserInfo;
import com.aiton.zjb.signal.server.PushIntentServer;
import com.aiton.zjb.signal.server.PushServer;
import com.igexin.sdk.PushManager;

public class ChooseActivity extends ZjbBaseActivity implements View.OnClickListener {

    private ACache mACache;
    private int mHengShuPing;
    private UserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushManager.getInstance().initialize(ChooseActivity.this, PushServer.class);
        PushManager.getInstance().registerPushIntentService(ChooseActivity.this, PushIntentServer.class);
        setContentView(R.layout.activity_choose);
        init();
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
