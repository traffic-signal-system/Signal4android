package com.aiton.zjb.signal.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiton.administrator.shane_library.shane.utils.FileUitls;
import com.aiton.administrator.shane_library.shane.widget.FlikerProgressBar;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;

public class OffLineMapActivity extends ZjbBaseActivity implements OfflineMapManager.OfflineMapDownloadListener, View.OnClickListener {

    private OfflineMapManager mAmapManager;
    private TextView mTextViewSize;
    private FlikerProgressBar roundProgressbar;
    private Button mButtonStatue;
    private ImageView mImageViewDeleteXiamen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_line_map);
        init();
    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findID() {
        mTextViewSize = (TextView) findViewById(R.id.textViewSize);
        //构造OfflineMapManager对象
        mAmapManager = new OfflineMapManager(OffLineMapActivity.this, this);
        roundProgressbar = (FlikerProgressBar) findViewById(R.id.round_flikerbar);
        mButtonStatue = (Button) findViewById(R.id.buttonStatue);
        mImageViewDeleteXiamen = (ImageView) findViewById(R.id.imageViewDeleteXiamen);
    }

    @Override
    protected void initViews() {
        OfflineMapCity Xiamen = mAmapManager.getItemByCityName("厦门市");
        try {
            mAmapManager.updateOfflineCityByName("厦门市");
        } catch (AMapException e) {
            e.printStackTrace();
        }
        Log.e("OffLineMapActivity", "OffLineMapActivity--initViews--"+Xiamen.getcompleteCode());
        Log.e("OffLineMapActivity", "OffLineMapActivity--initViews--"+Xiamen.getState());
        Log.e("OffLineMapActivity", "OffLineMapActivity--initViews--"+Xiamen.getUrl());
        Log.e("OffLineMapActivity", "OffLineMapActivity--initViews--"+Xiamen.getVersion());
        mTextViewSize.setText(FileUitls.FormetFileSize(Xiamen.getSize()));
        mImageViewDeleteXiamen.setVisibility(View.GONE);
    }

    @Override
    protected void setListeners() {
        roundProgressbar.setOnClickListener(this);
        findViewById(R.id.imageViewBack).setOnClickListener(this);
        mButtonStatue.setOnClickListener(this);
        mImageViewDeleteXiamen.setOnClickListener(this);
    }

    @Override
    public void onDownload(int i, int i1, String s) {
        Log.e("OffLineMapActivity", "OffLineMapActivity--onDownload--" + i);
        Log.e("OffLineMapActivity", "OffLineMapActivity--onDownload--" + i1);
        Log.e("OffLineMapActivity", "OffLineMapActivity--onDownload--" + s);
        if (i1>roundProgressbar.getProgress()){
            roundProgressbar.setProgress(i1);
        }
        if (i1==100){
            Log.e("OffLineMapActivity", "OffLineMapActivity--onDownload--完成");
            roundProgressbar.setVisibility(View.GONE);
            mImageViewDeleteXiamen.setVisibility(View.VISIBLE);
            mButtonStatue.setText("已下载");
            roundProgressbar.finishLoad();
        }
    }

    @Override
    public void onCheckUpdate(boolean b, String s) {
        Log.e("OffLineMapActivity", "OffLineMapActivity--onCheckUpdate--"+b);
        Log.e("OffLineMapActivity", "OffLineMapActivity--onCheckUpdate--"+s);
        if (s.equals("厦门市")){
            if (b){
                mButtonStatue.setText("下载");
                mImageViewDeleteXiamen.setVisibility(View.GONE);
            }else{
                mButtonStatue.setText("已下载");
                mImageViewDeleteXiamen.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRemove(boolean b, String s, String s1) {
        if (s.equals("厦门市")){
            if (b){
                mButtonStatue.setText("下载");
                mImageViewDeleteXiamen.setVisibility(View.GONE);
            }
        }
        Log.e("OffLineMapActivity", "OffLineMapActivity--onRemove--"+b);
        Log.e("OffLineMapActivity", "OffLineMapActivity--onRemove--"+s);
        Log.e("OffLineMapActivity", "OffLineMapActivity--onRemove--"+s1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewDeleteXiamen:
                new AlertDialog.Builder(OffLineMapActivity.this)
                        .setMessage("是否删除离线地图")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAmapManager.remove("厦门市");
                            }
                        })
                        .setNegativeButton("否",null)
                        .create()
                        .show();
                break;
            case R.id.imageViewBack:
                finishTo();
                break;
            case R.id.buttonStatue:
                if (TextUtils.equals(mButtonStatue.getText().toString(),"已下载")){

                }else if (TextUtils.equals(mButtonStatue.getText().toString(),"下载")){
                    try {
                        if (roundProgressbar.isFinish()){
                            roundProgressbar.reset();
                        }
                        mButtonStatue.setText("下载中");
                        mAmapManager.downloadByCityName("厦门市");
                        roundProgressbar.setVisibility(View.VISIBLE);
                    } catch (AMapException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}