package com.aiton.zjb.signal.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.gsm.SmsManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.app.SysApplication;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.NetChecker;
import com.aiton.zjb.signal.activity.LoginActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.loopj.android.http.AsyncHttpClient;
import com.umeng.analytics.MobclickAgent;


public abstract class ZjbBaseActivity extends Activity {

    //    private ProgressDialog mProgressDialog;
    public static String MYBROADCAST = "MyBroadcast";

    private BroadcastReceiver reciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
        }
    };
    private ProgressDialog mProgressDialog;
    private boolean isFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void init() {
        //添加当前界面到容器中
        SysApplication.getInstance().addActivity(this);
        initSP();
        setRequestedOrientation(Constant.HengShuPing);
        initIntent();
        initData();
        findID();
        initViews();
        setListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ZjbBaseActivity.MYBROADCAST);
        registerReceiver(reciver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计
        MobclickAgent.onPageStart(getRunningActivityName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getRunningActivityName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        isFinish = true;
        unregisterReceiver(reciver);
        super.onDestroy();
    }

    private String getRunningActivityName() {
        String contextString = this.toString();
        try {
            return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
        } catch (Exception e) {
        }
        return "";
    }

    public void showLoadingDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        cancelLoadingDialog();
                        finishTo();
                    }
                    return false;
                }
            });
        }
    }

    public void showLoadingDialog(final AsyncHttpClient asyncHttpClient, final Context context, String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        asyncHttpClient.cancelRequests(context, false);
                        mProgressDialog.dismiss();
                    }
                    return false;
                }
            });
        }
    }

    public void cancelLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && !isFinish) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void dialogFinish(String msg) {
        if (!isFinish) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(msg)
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finishTo();
                        }
                    })
                    .create();
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        dialog.dismiss();
                        finishTo();
                    }
                    return false;
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        } else {
            Toast.makeText(this, "网络超时", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 单个按钮无操作
     *
     * @param msg
     */
    public void showTipDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ZjbBaseActivity.this);
        builder.setMessage(msg)
                .setPositiveButton("是", null)
                .create()
                .show();
    }

    public void reLogin() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("你的账号在其他设备登录，请重新登陆")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ACache aCache = ACache.get(ZjbBaseActivity.this, Constant.ACACHE.USER);
                        aCache.clear();
                        Intent intent = new Intent();
                        intent.setClass(ZjbBaseActivity.this, LoginActivity.class);
                        startActivityTo(intent);
                    }
                })
                .create();
        alertDialog.show();
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    dialog.dismiss();
                    ACache aCache = ACache.get(ZjbBaseActivity.this, Constant.ACACHE.USER);
                    aCache.clear();
                    Intent intent = new Intent();
                    intent.setClass(ZjbBaseActivity.this, LoginActivity.class);
                    startActivityTo(intent);
                }
                return false;
            }
        });
    }

    protected abstract void initIntent();

    protected abstract void initSP();

    protected abstract void initData();

    protected abstract void findID();

    protected abstract void initViews();

    protected abstract void setListeners();

    /**
     * 判断网络是否连接
     *
     * @return true or false
     */
    protected boolean isNetAvailable() {
        NetChecker nc = NetChecker.getInstance(this.getApplicationContext());
        if (nc.isNetworkOnline()) {
            return true;
        } else {
            return false;
        }
    }

    protected void sendSms(String mobile, String msg) {
        SmsManager smsManager = SmsManager.getDefault();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        smsManager.sendTextMessage(mobile, null, msg, null, null);
    }

    public void startActivityTo(Intent intent) {
        startActivity(intent);
        overridePendingTransition(com.aiton.administrator.shane_library.R.anim.my_scale_action, com.aiton.administrator.shane_library.R.anim.my_alpha_action);//放大淡出效果
    }

    public void startActivityForResultTo(Intent intent, int result) {
        startActivityForResult(intent, result);
        overridePendingTransition(com.aiton.administrator.shane_library.R.anim.my_scale_action, com.aiton.administrator.shane_library.R.anim.my_alpha_action);//放大淡出效果
    }

    public void finishTo() {
        finish();
        overridePendingTransition(com.aiton.administrator.shane_library.R.anim.slide_up_in, com.aiton.administrator.shane_library.R.anim.slide_down_out);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(com.aiton.administrator.shane_library.R.anim.slide_up_in, com.aiton.administrator.shane_library.R.anim.slide_down_out);
    }
}
