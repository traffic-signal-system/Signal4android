package com.aiton.zjb.signal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.aiton.administrator.shane_library.shane.utils.IPUtil;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.model.VideoListInfo;

public class AddVideoActivity extends ZjbBaseActivity implements View.OnClickListener {

    private EditText mEditTextName;
    private EditText mEditTextIP;
    private EditText mEditTextPort;
    private EditText mEditTextUserName;
    private EditText mEditTextPassword;
    private EditText mEditTextNote;
    private VideoListInfo.ObjectEntity mVideoInfo;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        init();
    }

    @Override
    protected void initIntent() {
        Intent intent = getIntent();
        mVideoInfo = (VideoListInfo.ObjectEntity) intent.getSerializableExtra(Constant.IntentKey.VIDEO_INFO);
        if (mVideoInfo!=null){
            isEdit=true;
        }
    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findID() {
        mEditTextName = (EditText) findViewById(R.id.editTextName);
        mEditTextIP = (EditText) findViewById(R.id.editTextIP);
        mEditTextPort = (EditText) findViewById(R.id.editTextPort);
        mEditTextUserName = (EditText) findViewById(R.id.editTextUserName);
        mEditTextPassword = (EditText) findViewById(R.id.editTextPassword);
        mEditTextNote = (EditText) findViewById(R.id.editTextNote);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {
        findViewById(R.id.textViewComplete).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.imageViewBack:
                finishTo();
                break;
            case R.id.textViewComplete:
                if (whatFuck()){
                    String name = mEditTextName.getText().toString().trim();
                    String ip = mEditTextIP.getText().toString().trim();
                    String port = mEditTextPort.getText().toString().trim();
                    String userName = mEditTextUserName.getText().toString().trim();
                    String password = mEditTextPassword.getText().toString().trim();
                    String note = mEditTextNote.getText().toString().trim();
                    if (isEdit){
                        intent.putExtra(Constant.IntentKey.VIDEO_INFO,new VideoListInfo.ObjectEntity(mVideoInfo.getId(),mVideoInfo.getNodeId(),name,ip,port,userName,password,note));
                        setResult(Constant.REQUEST_RETURN_CODE.EDIT_VIDEO_INFO,intent);
                    }else {
                        intent.putExtra(Constant.IntentKey.VIDEO_NAME, name);
                        intent.putExtra(Constant.IntentKey.VIDEO_IP, ip);
                        intent.putExtra(Constant.IntentKey.VIDEO_PORT, port);
                        intent.putExtra(Constant.IntentKey.VIDEO_USER_NAEM, userName);
                        intent.putExtra(Constant.IntentKey.VIDEO_USER_PASSWORD, password);
                        intent.putExtra(Constant.IntentKey.VIDEO_NOTE, note);
                        setResult(Constant.REQUEST_RETURN_CODE.VIDEO_INFO,intent);
                        finishTo();
                    }
                }
                break;
        }
    }

    private boolean whatFuck() {
        if (TextUtils.equals(mEditTextName.getText().toString().trim(), "")) {
            mEditTextName.setError("设备名不能为空");
            return false;
        } else if ((TextUtils.equals(mEditTextIP.getText().toString().trim(), ""))||!IPUtil.isIP(mEditTextIP.getText().toString().trim())) {
            mEditTextIP.setError("请输入正确的IP");
            return false;
        } else if (TextUtils.equals(mEditTextPort.getText().toString().trim(), "")) {
            mEditTextPort.setError("设备端口不能为空");
            return false;
        } else if (TextUtils.equals(mEditTextUserName.getText().toString().trim(), "")) {
            mEditTextUserName.setError("用户名不能为空");
            return false;
        } else if (TextUtils.equals(mEditTextPassword.getText().toString().trim(), "")) {
            mEditTextPassword.setError("密码不能为空");
            return false;
        }
        return true;
    }

}
