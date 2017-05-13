package com.aiton.zjb.signal.base;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.aiton.administrator.shane_library.shane.app.SysApplication;
import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.zjb.signal.activity.LoginActivity;
import com.aiton.zjb.signal.constant.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class ZjbBaseFragment extends Fragment {
    private ProgressDialog mProgressDialog;
    private boolean isFinish = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(Constant.HengShuPing);
        super.onCreate(savedInstanceState);
    }
    public void init() {
        //添加当前界面到容器中
        initSP();
        initIntent();
        initData();
        findID();
        initViews();
        setListeners();
    }
    protected abstract void initIntent();

    protected abstract void initSP();

    protected abstract void initData();

    protected abstract void findID();

    protected abstract void initViews();

    protected abstract void setListeners();

    public void startActivityTo(Intent intent) {
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(com.aiton.administrator.shane_library.R.anim.my_scale_action, com.aiton.administrator.shane_library.R.anim.my_alpha_action);//放大淡出效果
    }

    public void startActivityForResultTo(Intent intent, int result) {
        getActivity().startActivityForResult(intent, result);
        getActivity().overridePendingTransition(com.aiton.administrator.shane_library.R.anim.my_scale_action, com.aiton.administrator.shane_library.R.anim.my_alpha_action);//放大淡出效果
    }

    public void finishTo() {
        getActivity().finish();
        getActivity().overridePendingTransition(com.aiton.administrator.shane_library.R.anim.slide_up_in, com.aiton.administrator.shane_library.R.anim.slide_down_out);
    }

    public void reLogin() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("你的账号在其他设备登录，请重新登陆")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ACache aCache = ACache.get(getActivity(), Constant.ACACHE.USER);
                        aCache.clear();
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), LoginActivity.class);
                        startActivityTo(intent);
                        SysApplication.getInstance().exitExceptThisActivity();
                    }
                })
                .create();
        alertDialog.show();
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    dialog.dismiss();
                    ACache aCache = ACache.get(getActivity(), Constant.ACACHE.USER);
                    aCache.clear();
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LoginActivity.class);
                    startActivityTo(intent);
                    SysApplication.getInstance().exitExceptThisActivity();
                }
                return false;
            }
        });
    }

    public void showLoadingDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
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

    public void cancelLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && !isFinish) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * 单个按钮无操作
     * @param msg
     */
    public void showTipDialog(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg)
                .setPositiveButton("是", null)
                .create()
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFinish = true;
    }
}
