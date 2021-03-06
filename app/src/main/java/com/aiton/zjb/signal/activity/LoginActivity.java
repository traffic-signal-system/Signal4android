package com.aiton.zjb.signal.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.utils.Installation;
import com.aiton.administrator.shane_library.shane.utils.IsMobileNOorPassword;
import com.aiton.administrator.shane_library.shane.utils.NetChecker;
import com.aiton.administrator.shane_library.shane.utils.VersionUtils;
import com.aiton.administrator.shane_library.shane.widget.AnimCheckBox;
import com.aiton.administrator.shane_library.shane.widget.SingleBtnDialog;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.model.CheckLiveInfo;
import com.aiton.zjb.signal.model.NoAction;
import com.aiton.zjb.signal.model.UserInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends ZjbBaseActivity implements View.OnClickListener {
    private LinearLayout mLinear_sms;
    private LinearLayout mLinear_password;
    private TextView mTextView_switch;
    private boolean isSmsLogin = true;
    private EditText mEditText_phone_sms;
    private EditText mEditText_sms;
    private String mPhone_sms;
    private int[] mI;
    private Runnable mR;
    private TextView mTextView_getSms;
    private EditText mEditText_phone;
    private EditText mEditText_password;
    private Button mBtn_login;
    private AnimCheckBox mCheckbox_accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Constant.HengShuPing != getResources().getConfiguration().orientation) {
            setContentView(R.layout.activity_login_land);
        } else {
            setContentView(R.layout.activity_login);
        }
        initPermission();
        if (!NetChecker.getInstance(LoginActivity.this).isNetworkOnline()) {
            SingleBtnDialog singleBtnDialog = new SingleBtnDialog(LoginActivity.this, "???????????????", "??????");
            singleBtnDialog.show();
            singleBtnDialog.setClicklistener(new SingleBtnDialog.ClickListenerInterface() {
                @Override
                public void doWhat() {
                    finishTo();
                }
            });
        }
        init();
    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void initData() {
        Log.e("LoginActivity", "LoginActivity--initData--");
        checkLive();
    }

    @Override
    protected void findID() {
        mLinear_sms = (LinearLayout) findViewById(R.id.linear_sms);
        mLinear_password = (LinearLayout) findViewById(R.id.linear_password);
        mTextView_switch = (TextView) findViewById(R.id.textView_switch);
        mEditText_phone_sms = (EditText) findViewById(R.id.editText_phone_sms);
        mEditText_sms = (EditText) findViewById(R.id.editText_sms);
        mTextView_getSms = (TextView) findViewById(R.id.textView_getSms);
        mEditText_phone = (EditText) findViewById(R.id.editText_phone);
        mEditText_password = (EditText) findViewById(R.id.editText_password);
        mBtn_login = (Button) findViewById(R.id.button_login);
        mCheckbox_accept = (AnimCheckBox) findViewById(R.id.checkbox_accept);
    }

    @Override
    protected void initViews() {
        mLinear_sms.setVisibility(View.VISIBLE);
        mLinear_password.setVisibility(View.GONE);
    }

    @Override
    protected void setListeners() {
        findViewById(R.id.tv_forget_password).setOnClickListener(this);
        mBtn_login.setOnClickListener(this);
        mTextView_getSms.setOnClickListener(this);
        mTextView_switch.setOnClickListener(this);
    }

    /**
     * ??????????????????6.0???????????????
     */
    private void initPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //??????WRITE_EXTERNAL_STORAGE??????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.PERMISSION.ACCESS_COARSE_LOCATION);//????????????code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //??????WRITE_EXTERNAL_STORAGE??????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constant.PERMISSION.ACCESS_FINE_LOCATION);//????????????code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //??????WRITE_EXTERNAL_STORAGE??????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constant.PERMISSION.WRITE_EXTERNAL_STORAGE);//????????????code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //??????WRITE_EXTERNAL_STORAGE??????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constant.PERMISSION.READ_EXTERNAL_STORAGE);//????????????code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //??????WRITE_EXTERNAL_STORAGE??????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    Constant.PERMISSION.READ_PHONE_STATE);//????????????code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                != PackageManager.PERMISSION_GRANTED) {
            //??????WRITE_EXTERNAL_STORAGE??????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},
                    Constant.PERMISSION.RECEIVE_BOOT_COMPLETED);//????????????code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {
            //??????WRITE_EXTERNAL_STORAGE??????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SETTINGS},
                    Constant.PERMISSION.RECEIVE_WRITE_SETTINGS);//????????????code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE)
                != PackageManager.PERMISSION_GRANTED) {
            //??????WRITE_EXTERNAL_STORAGE??????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.VIBRATE},
                    Constant.PERMISSION.RECEIVE_VIBRATE);//????????????code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.DISABLE_KEYGUARD)
                != PackageManager.PERMISSION_GRANTED) {
            //??????WRITE_EXTERNAL_STORAGE??????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.DISABLE_KEYGUARD},
                    Constant.PERMISSION.RECEIVE_DISABLE_KEYGUARD);//????????????code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            //??????WRITE_EXTERNAL_STORAGE??????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    Constant.PERMISSION.CALL_PHONE);//????????????code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW)
                != PackageManager.PERMISSION_GRANTED) {
            //??????CALL_PHONE??????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},
                    Constant.PERMISSION.SYSTEM_ALERT_WINDOW);//????????????code
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_forget_password://????????????
//                intent.setClass(this, ChangePasswordActivity.class);
//                intent.putExtra(Constant.INTENT_KEY.CHANGE_PASSWORD,Constant.MODE.LOGIN);
//                startActivityTo(intent);
                break;
            case R.id.textView_getSms://????????????
                if (ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.READ_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    //??????WRITE_EXTERNAL_STORAGE??????
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.READ_SMS},
                            Constant.PERMISSION.PERMISSION_READ_SMS);
                } else {
                    //???????????????????????????
                    if (IsMobileNOorPassword.isMobileNO(mEditText_phone_sms.getText().toString().trim())) {
                        sendSMS();
                    } else {
                        mEditText_phone_sms.setError("??????????????????????????????");
                    }
                }
                break;
            case R.id.textView_switch:
                isSmsLogin = !isSmsLogin;
                if (isSmsLogin) {
                    mLinear_sms.setVisibility(View.VISIBLE);
                    mLinear_password.setVisibility(View.GONE);
                    mTextView_switch.setText("??????????????????");
                } else {
                    mLinear_sms.setVisibility(View.GONE);
                    mLinear_password.setVisibility(View.VISIBLE);
                    mTextView_switch.setText("??????????????????");
                }
                break;
            case R.id.button_login://??????
                if (mCheckbox_accept.isChecked()) {
                    if (isSmsLogin) {
                        //????????????
                        if (!IsMobileNOorPassword.isMobileNO(mEditText_phone_sms.getText().toString().trim())) {
                            mEditText_phone_sms.setError("??????????????????????????????");
                        } else if (TextUtils.isEmpty(mEditText_sms.getText().toString().trim())) {
                            mEditText_sms.setError("?????????????????????");
                        } else {
                            final String id = Installation.id(LoginActivity.this);
                            Log.e("LoginActivity", "LoginActivity--onClick--?????????" + id);
                            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                            showLoadingDialog(asyncHttpClient, LoginActivity.this, "????????????");
                            RequestParams params = new RequestParams();
                            params.put("phone", mEditText_phone_sms.getText().toString().trim());
                            params.put("code", mEditText_sms.getText().toString().trim());
                            params.put("deviceId", id);
                           // Log.e("LoginActivity", "LoginActivity--onClick--??????CID"+PushManager.getInstance().getClientid(LoginActivity.this));
                           // params.put("channelId", PushManager.getInstance().getClientid(LoginActivity.this));
                            String url = Constant.Url.LOGIN;
                            asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    cancelLoadingDialog();
                                    String s = new String(responseBody);
                                    Log.e("LoginActivity", "LoginActivity--onSuccess--????????????" + s);
                                    try {
                                        UserInfo userInfo = GsonUtils.parseJSON(s, UserInfo.class);
                                        if (userInfo.isSuccess()) {
                                            ACache aCache = ACache.get(LoginActivity.this, Constant.ACACHE.USER);
                                            aCache.put(Constant.ACACHE.USERINFO, userInfo);
                                            aCache.put(Constant.ACACHE.DEVICES_ID, id);
                                            startToMainActivity();
                                        }
                                        toast(userInfo.getMessage());
                                    } catch (Exception e) {

                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    Toast.makeText(LoginActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        //????????????
                        if (!IsMobileNOorPassword.isMobileNO(mEditText_phone.getText().toString().trim())) {
                            mEditText_phone.setError("??????????????????????????????");
                        } else if (TextUtils.isEmpty(mEditText_phone.getText().toString().trim())) {
                            mEditText_phone.setError("??????????????????");
                        } else {
                            if (TextUtils.isEmpty(mEditText_password.getText().toString().trim())) {
                                mEditText_password.setError("??????????????????");
                            } else {
                                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                showLoadingDialog(asyncHttpClient, LoginActivity.this, "????????????");
                                RequestParams params = new RequestParams();
                                params.put("username", mEditText_phone.getText().toString().trim());
                                params.put("password", mEditText_password.getText().toString().trim());
                                String url = Constant.Url.LOGIN;
                                asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        cancelLoadingDialog();
                                        String s = new String(responseBody);
                                        Log.e("LoginActivity", "LoginActivity--onSuccess--????????????" + s);
                                        try {
                                            UserInfo UserInfo = GsonUtils.parseJSON(s, UserInfo.class);
                                            if (UserInfo.isSuccess()) {
                                                ACache aCache = ACache.get(LoginActivity.this, Constant.ACACHE.USER);
                                                aCache.put(Constant.ACACHE.USERINFO, UserInfo);
                                                startToMainActivity();
                                            } else {
                                                toast(UserInfo.getMessage());
                                            }
                                        } catch (Exception e) {

                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        Toast.makeText(LoginActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        if (!IsMobileNOorPassword.isPassword(mEditText_password.getText().toString().trim())) {
                            mEditText_password.setError("??????????????????6?????????????????????????????????");
                        }
                    }
                } else {
                    showTipDialog("?????????????????????????????????????????????????????????");
                }
                break;
        }
    }

    /**
     * ????????????????????????
     */
    private void checkLive() {
        Log.e("LoginActivity", "LoginActivity--checkLive--");
        final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        showLoadingDialog("");
        RequestParams params = new RequestParams();
        String url = Constant.Url.CHECK_LIVE;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("LoginActivity", "LoginActivity--onSuccess--" + s);
                try {
                    cancelLoadingDialog();
                    CheckLiveInfo checkLiveInfo = GsonUtils.parseJSON(s, CheckLiveInfo.class);
                    if (checkLiveInfo.isSuccess()) {
                        if (VersionUtils.getCurrVersion(LoginActivity.this)>=checkLiveInfo.getObject().getAndroidVersionForMaintainApp()) {

                        } else {
                            dialogFinish("?????????????????????");
                        }
                    } else {
                        dialogFinish(checkLiveInfo.getMessage());
                    }
                } catch (Exception e) {
                    dialogFinish("????????????");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("LoginActivity", "LoginActivity--onFailure--????????????????????????");
                cancelLoadingDialog();
                dialogFinish("????????????");
            }
        });
    }

    private void startToMainActivity() {
        finishTo();
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivityTo(intent);
    }

    private void sendSMS() {
        mPhone_sms = mEditText_phone_sms.getText().toString().trim();
        mTextView_getSms.removeCallbacks(mR);
        boolean mobileNO = IsMobileNOorPassword.isMobileNO(mPhone_sms);
        if (mobileNO) {
            mTextView_getSms.setEnabled(false);
            mI = new int[]{60};

            mR = new Runnable() {
                @Override
                public void run() {
                    mTextView_getSms.setText((mI[0]--) + "????????????");
                    if (mI[0] == 0) {
                        mTextView_getSms.setEnabled(true);
                        mTextView_getSms.setText("???????????????");
                        return;
                    } else {
                    }
                    mTextView_getSms.postDelayed(mR, 1000);
                }
            };
            mTextView_getSms.postDelayed(mR, 0);
            getSms();
        } else {
            Toast.makeText(LoginActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            mEditText_phone_sms.setError("???????????????????????????");
            mEditText_phone_sms.setText("");
        }
    }

    private void getSms() {
        String url = Constant.Url.GET_SMS;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("phone", mPhone_sms);

        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("LoginActivity", "LoginActivity--onSuccess--???????????????" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {

                    }
                    toast(noAction.getMessage());
                } catch (Exception e) {
                    toast("????????????");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                toast("???????????????");
            }
        });
    }

    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,ChooseActivity.class);
        startActivityTo(intent);
    }
}
