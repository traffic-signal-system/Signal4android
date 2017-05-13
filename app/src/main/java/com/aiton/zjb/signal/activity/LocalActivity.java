package com.aiton.zjb.signal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.aiton.administrator.shane_library.shane.utils.IPUtil;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.adapter.IpAdapter;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.constant.GbtDefine;
import com.aiton.zjb.signal.util.ByteUtils;
import com.aiton.zjb.signal.util.UdpClientSocket;

import java.util.ArrayList;

public class LocalActivity extends ZjbBaseActivity implements View.OnClickListener {

    private String INSTANCESTATE = "LocalActivity";
    private ArrayList<String> mIpList = new ArrayList<>();
    private GridView mGv_tsc;
    private IpAdapter mIpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
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
        mGv_tsc = (GridView) findViewById(R.id.gv_tsc);
    }

    @Override
    protected void initViews() {
        mIpAdapter = new IpAdapter(LocalActivity.this, mIpList);
        mGv_tsc.setAdapter(mIpAdapter);
    }

    @Override
    protected void setListeners() {
        findViewById(R.id.button_broadcast).setOnClickListener(this);
        mGv_tsc.setOnItemClickListener(new ItemClickListener());
        findViewById(R.id.textViewAddIP).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new BrocastRunnable()).start();
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putStringArrayList(INSTANCESTATE+"mIpList",mIpList);
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        mIpList = savedInstanceState.getStringArrayList(INSTANCESTATE + "mIpList");
//        super.onRestoreInstanceState(savedInstanceState);
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_broadcast:
                new Thread(new BrocastRunnable()).start();
                break;
            case R.id.textViewAddIP:
                View inflate = getLayoutInflater().inflate(R.layout.edit_dialog_view, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(LocalActivity.this)
                        .setView(inflate)
                        .create();
                final EditText editTextIP =  (EditText) inflate.findViewById(R.id.editTextIP);
                inflate.findViewById(R.id.textViewComfirmIP).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String s = editTextIP.getText().toString();
                        if (IPUtil.isIP(s)){
                            mIpList.add(s);
                            mIpAdapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        }else{
                            editTextIP.setError("请输入正确的IP");
                        }
                    }
                });
                alertDialog.show();
                break;
        }
    }

    class BrocastRunnable implements Runnable {

        @Override
        public void run() {
            try {
                UdpClientSocket udpClientSocket = new UdpClientSocket();
                String ipBrocast = "224.0.0.1";
                String s = "123456";
                byte[] bytes1 = s.getBytes();
                udpClientSocket.send(ipBrocast, GbtDefine.BROADCAST_PORT, bytes1);
                udpClientSocket.setSoTimeout(1000);
                mIpList.clear();
                while (true) {
                    byte[] bytes = udpClientSocket.receiveByte(ipBrocast, GbtDefine.BROADCAST_PORT);
                    String IP = ByteUtils.bytesUInt(bytes[0]) + "." + ByteUtils.bytesUInt(bytes[1]) + "." + ByteUtils.bytesUInt(bytes[2]) + "." + ByteUtils.bytesUInt(bytes[3]);
                    Log.i("LocalActivity", "LocalActivity--run--IP" + IP);
                    mIpList.add(IP);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIpAdapter.notifyDataSetChanged();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
    class ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //显示所选Item的ItemText
            Intent intent = new Intent();
            intent.putExtra(Constant.IntentKey.IP, mIpList.get(position));
            intent.setClass(LocalActivity.this, PhaseActivity.class);
            startActivityTo(intent);
        }
    }
}
