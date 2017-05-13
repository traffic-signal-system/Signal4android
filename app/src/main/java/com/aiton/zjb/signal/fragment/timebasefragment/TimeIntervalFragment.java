package com.aiton.zjb.signal.fragment.timebasefragment;


import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.utils.LogUtil;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseFragment;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.constant.GbtDefine;
import com.aiton.zjb.signal.model.GroupListEntity;
import com.aiton.zjb.signal.model.NoAction;
import com.aiton.zjb.signal.model.NodeListEntity;
import com.aiton.zjb.signal.model.ShiDuanInfo;
import com.aiton.zjb.signal.model.UserInfo;
import com.aiton.zjb.signal.util.ByteUtils;
import com.aiton.zjb.signal.util.UdpClientSocket;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeIntervalFragment extends ZjbBaseFragment implements View.OnClickListener {
    private View mInflate;
    private ListView mListViewShiDuan;
    private TextView mTv_chooseShiDuan;
    private AlertDialog mListDialog;
    private List<Integer> shiDuanList = new ArrayList<>();
    private List<Integer> shiDuanUsedList = new ArrayList<>();
    private String mIp;
    private List<ArrayList<byte[]>> shiduanByteListList = new ArrayList<>();
    private ArrayList<byte[]> shiDuanByteList01 = new ArrayList<>();
    private ArrayList<byte[]> shiDuanByteList02 = new ArrayList<>();
    private ArrayList<byte[]> shiDuanByteList03 = new ArrayList<>();
    private ArrayList<byte[]> shiDuanByteList04 = new ArrayList<>();
    private ArrayList<byte[]> shiDuanByteList05 = new ArrayList<>();
    private ArrayList<byte[]> shiDuanByteList06 = new ArrayList<>();
    private ArrayList<byte[]> shiDuanByteList07 = new ArrayList<>();
    private ArrayList<byte[]> shiDuanByteList08 = new ArrayList<>();
    private ArrayList<byte[]> shiDuanByteList09 = new ArrayList<>();
    private ArrayList<byte[]> shiDuanByteList010 = new ArrayList<>();
    private ArrayList<byte[]> shiDuanByteList011 = new ArrayList<>();
    private ArrayList<byte[]> shiDuanByteList012 = new ArrayList<>();
    private ArrayList<byte[]> shiDuanByteList013 = new ArrayList<>();
    private ArrayList<byte[]> shiDuanByteList014 = new ArrayList<>();
    private ArrayList<byte[]> shiDuanByteList015 = new ArrayList<>();
    private ArrayList<byte[]> shiDuanByteList016 = new ArrayList<>();
    private int currentIndex = 1;
    private List<ShiDuanInfo> mShiDuanInfoList = new ArrayList<>();
    private MyAdapter mMyAdapter;
    private LinearLayout mLinear_addShiDuan;
    private TextView mTv_shiDuanXuHao;
    private TextView mTv_controlType;
    private TextView mTv_shiJian;
    private TextView mTv_fangAn;
    private Button mButtonShiDuan;
    private String WhatIsShow = "";
    private List<byte[]> peiShiByteArrList = new ArrayList<>();
    private Map<Integer, String> controlTypeValue = new HashMap<>();
    private int mCurrentControlType;
    private NodeListEntity mNodeListEntity;
    private boolean isOnline = false;
    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;
    private GroupListEntity mGroupListEntity;
    private boolean isGroupConfigure = false;

    public TimeIntervalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_time_interval, container, false);
            init();
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) mInflate.getParent();
        if (parent != null) {
            parent.removeView(mInflate);
        }
        return mInflate;
    }

    @Override
    protected void initIntent() {
        Intent intent = getActivity().getIntent();
        mIp = intent.getStringExtra(Constant.IntentKey.IP);
        mNodeListEntity = (NodeListEntity) intent.getSerializableExtra(Constant.IntentKey.NODE);
        mGroupListEntity = (GroupListEntity) intent.getSerializableExtra(Constant.IntentKey.GROUP);
        if (mGroupListEntity != null) {
            isGroupConfigure = true;
        }
        if (mNodeListEntity != null) {
            isOnline = true;
        }
    }

    @Override
    protected void initSP() {
        mACache = ACache.get(getActivity(), Constant.ACACHE.USER);
        mUserInfo = (UserInfo) mACache.getAsObject(Constant.ACACHE.USERINFO);
        mDevicesID = mACache.getAsString(Constant.ACACHE.DEVICES_ID);
    }

    @Override
    protected void initData() {
        for (int i = 0; i < 16; i++) {
            shiDuanList.add(i + 1);
        }
        shiduanByteListList.add(shiDuanByteList01);
        shiduanByteListList.add(shiDuanByteList02);
        shiduanByteListList.add(shiDuanByteList03);
        shiduanByteListList.add(shiDuanByteList04);
        shiduanByteListList.add(shiDuanByteList05);
        shiduanByteListList.add(shiDuanByteList06);
        shiduanByteListList.add(shiDuanByteList07);
        shiduanByteListList.add(shiDuanByteList08);
        shiduanByteListList.add(shiDuanByteList09);
        shiduanByteListList.add(shiDuanByteList010);
        shiduanByteListList.add(shiDuanByteList011);
        shiduanByteListList.add(shiDuanByteList012);
        shiduanByteListList.add(shiDuanByteList013);
        shiduanByteListList.add(shiDuanByteList014);
        shiduanByteListList.add(shiDuanByteList015);
        shiduanByteListList.add(shiDuanByteList016);
        controlTypeValue.put(0, "自主控制");//00
        controlTypeValue.put(1, "关灯");//01
        controlTypeValue.put(2, "黄闪");//02
        controlTypeValue.put(3, "全红");//03
        controlTypeValue.put(7, "无电缆协调");//07
        controlTypeValue.put(5, "单点半感应");//05
        controlTypeValue.put(6, "单点全感应");//06
        controlTypeValue.put(11, "主从线控");//0B   1
        controlTypeValue.put(12, "系统联网");//0C   1
        controlTypeValue.put(13, "干预控制");//0D   1
        controlTypeValue.put(9, "动态预分析控制");//09
    }

    @Override
    protected void findID() {
        mListViewShiDuan = (ListView) mInflate.findViewById(R.id.listViewShiDuan);
        mTv_chooseShiDuan = (TextView) mInflate.findViewById(R.id.tvChooseShiDuan);
        mLinear_addShiDuan = (LinearLayout) mInflate.findViewById(R.id.linear_addShiDuan);
        mTv_shiDuanXuHao = (TextView) mInflate.findViewById(R.id.tv_ShiDuanXuHao);
        mTv_shiJian = (TextView) mInflate.findViewById(R.id.tv_ShiJian);
        mTv_controlType = (TextView) mInflate.findViewById(R.id.tv_ControlType);
        mTv_fangAn = (TextView) mInflate.findViewById(R.id.tv_FangAn);
        mButtonShiDuan = (Button) mInflate.findViewById(R.id.buttonShiDuan);
    }

    @Override
    protected void initViews() {
        hideSetShiDuan();
        mMyAdapter = new MyAdapter();
        mListViewShiDuan.setAdapter(mMyAdapter);
    }

    @Override
    protected void setListeners() {
        mTv_chooseShiDuan.setOnClickListener(this);
        mTv_shiDuanXuHao.setOnClickListener(this);
        mTv_shiJian.setOnClickListener(this);
        mTv_controlType.setOnClickListener(this);
        mTv_fangAn.setOnClickListener(this);
        mButtonShiDuan.setOnClickListener(this);
        mInflate.findViewById(R.id.imageView_cancleAdd).setOnClickListener(this);
        mListViewShiDuan.setOnItemClickListener(new MyOnitemClickListener());
        mListViewShiDuan.setOnItemLongClickListener(new MyLongItemClickListener());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvChooseShiDuan:
                WhatIsShow = "ShiDuan";
                showListDialog(shiDuanList);
                break;
            case R.id.buttonShiDuan:
                String s = mButtonShiDuan.getText().toString();
                if (TextUtils.equals(s, "添加时段序号")) {
                    mLinear_addShiDuan.setVisibility(View.VISIBLE);
                    mButtonShiDuan.setText(R.string.saveShIDuan);
                } else {
                    String reason = getReason();
                    if (TextUtils.equals("完成", reason)) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("是否更新到信号机")
                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (isOnline || isGroupConfigure) {
                                            setSchedule();
                                        } else {
                                            new Thread(new setShiDuanRunnable()).start();
                                        }
                                    }
                                })
                                .setNegativeButton("否", null)
                                .create()
                                .show();
                    } else {
                        Toast.makeText(getActivity(), reason, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.imageView_cancleAdd:
                hideSetShiDuan();
                break;
            case R.id.tv_ShiDuanXuHao:
                WhatIsShow = "ShiDuanXuHao";
                List<Integer> list = new ArrayList<>();
                List<Integer> list1 = new ArrayList<>();
                List<Integer> list4 = new ArrayList<>();
                for (int i = 0; i < 48; i++) {
                    list4.add(i + 1);
                }
                for (int i = 0; i < mShiDuanInfoList.size(); i++) {
                    list1.add(mShiDuanInfoList.get(i).getShiDuanNum());
                }
                for (int i = 0; i < list4.size(); i++) {
                    if (!list1.contains(list4.get(i))) {
                        list.add(list4.get(i));
                    }
                }
                showListDialog(list);
                break;
            case R.id.tv_ShiJian:
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        mTv_shiJian.setText(i + ":" + i1);
                    }
                }, 0, 0, true).show();
                break;
            case R.id.tv_ControlType:
                List<Integer> listcontrol = new ArrayList<>();
                listcontrol.add(0);
                listcontrol.add(1);
                listcontrol.add(2);
                listcontrol.add(3);
                listcontrol.add(7);
                listcontrol.add(5);
                listcontrol.add(6);
                listcontrol.add(11);
                listcontrol.add(12);
                listcontrol.add(13);
                listcontrol.add(9);
                showStringListDialog(listcontrol);
                break;
            case R.id.tv_FangAn:
                WhatIsShow = "FangAn";
                List<Integer> list2 = new ArrayList<>();
                for (int i = 0; i < peiShiByteArrList.size(); i++) {
                    list2.add(ByteUtils.bytesUInt(peiShiByteArrList.get(i)[0]));
                }
                showListDialog(list2);
                break;
        }
    }

    private void setSchedule() {
        //848E0010
        // 01 01时段序号 08正点数 00分数 00控制方式 01配时方案 00 00
        // 01 02        17     0E     03       03       00 00
        // 0100000000000000
        String[] shijian = mTv_shiJian.getText().toString().split(":");
        byte[] byteShiDuanInfo = new byte[8];
        int currentXuHao = Integer.parseInt(mTv_shiDuanXuHao.getText().toString());
        byte[] bytes2 = new byte[]{(byte) currentIndex, (byte) currentXuHao, (byte) Integer.parseInt(shijian[0]), (byte) Integer.parseInt(shijian[1]), (byte) mCurrentControlType, (byte) Integer.parseInt(mTv_fangAn.getText().toString()), 0x00, 0x00};
        System.arraycopy(bytes2, 0, byteShiDuanInfo, 0, bytes2.length);
        String s = ByteUtils.bytesToHexString(byteShiDuanInfo);
        Log.e("setShiDuanRunnable", "setShiDuanRunnable--run--时段拼接的对象" + s);
        String sendShiDuanHexStr = "818E001030";
        for (int i = 0; i < 16; i++) {
            ArrayList<byte[]> bytes = shiduanByteListList.get(i);
            for (int j = 0; j < bytes.size(); j++) {
                if (currentIndex == (i + 1) && currentXuHao == (j + 1)) {
                    sendShiDuanHexStr = sendShiDuanHexStr + s;
                } else {
                    sendShiDuanHexStr = sendShiDuanHexStr + ByteUtils.bytesToHexString(bytes.get(j));
                }
            }
        }
        scheduleAsyn(sendShiDuanHexStr);
    }

    private void scheduleAsyn(String sendShiDuanHexStr) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("byteString", sendShiDuanHexStr);
        String url = "";
        if (isOnline) {
            url = Constant.Url.SET_SCHEDULE;
            params.put("nodeId", mNodeListEntity.getId());
        }
        if (isGroupConfigure) {
            url = Constant.Url.SET_GROUP_SCHEDULE;
            params.put("groupId", mGroupListEntity.getId());
        }
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--设置时段返回值" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        if (noAction.getObject() == null) {
                            hideSetShiDuan();
                            getSchedule();
                        } else {
                            String object = (String) noAction.getObject();
                            byte[] bytes = ByteUtils.hexStringToByte(object);
                            if (ByteUtils.bytesUInt(bytes[0]) == 133 && ByteUtils.bytesUInt(bytes[1]) == 142) {
                                Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                                hideSetShiDuan();
                                getSchedule();
                            } else {
                                Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (noAction.getMessageCode() == 3) {
                            reLogin();
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    /**
     * 添加时段提示必选字段
     *
     * @return
     */
    private String getReason() {
        if (TextUtils.equals(mTv_shiDuanXuHao.getText().toString(), "0")) {
            return "请选择时段序号";
        } else if (TextUtils.equals(mTv_controlType.getText().toString(), "请选择")) {
            return "请选择控制方式";
        } else if (TextUtils.equals(mTv_fangAn.getText().toString(), "请选择")) {
            return "请选择配时方案";
        }
        return "完成";
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isOnline || isGroupConfigure) {
            getSchedule();
            getTimePattern();
        } else {
            new Thread(new ShiDuanRunnable()).start();
            new Thread(new PeiShiRunnable()).start();
        }
    }

    private void getTimePattern() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        String url = "";
        if (isOnline) {
            params.put("nodeId", mNodeListEntity.getId());
            url = Constant.Url.GET_TIME_PATTERN;
        }
        if (isGroupConfigure) {
            params.put("groupId", mGroupListEntity.getId());
            url = Constant.Url.GET_GROUP_TIME_PATTERN;
        }
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--在线时段返回值" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        String object = (String) noAction.getObject();
                        Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--时段值" + object);
                        final byte[] bytes = ByteUtils.hexStringToByte(object);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                timePattern(bytes);
                            }
                        }).start();
                    } else {
                        if (noAction.getMessageCode() == 3) {
                            reLogin();
                        } else {
                            //初始化数据
                            String receive = "84C0000";
                            Log.e("JieDuanFragment", "JieDuanFragment--onSuccess--阶段配时长度" + receive.length());
                            timePattern(ByteUtils.hexStringToByte(receive));
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "获取时段表失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void timePattern(byte[] bytes) {
        String s = ByteUtils.bytesToHexString(bytes);
        Log.e("PeiShiRunnable", "PeiShiRunnable--run--配时表返回" + s);
        if ((ByteUtils.bytesUInt(bytes[0]) == 132 && ByteUtils.bytesUInt(bytes[1]) == 192) || (ByteUtils.bytesUInt(bytes[0]) == 129 && ByteUtils.bytesUInt(bytes[1]) == 192)) {
            //84消息类型 C0配时表标识 00 05对象数 01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
            //01方案号 78周期时长 1E相位差 0A协调相位 01对应的阶段配时表号
            peiShiByteArrList.clear();
            int peiShiNum = ByteUtils.bytesUInt(bytes[3]);
            for (int i = 0; i < peiShiNum; i++) {
                peiShiByteArrList.add(new byte[]{bytes[4 + i * 5], bytes[5 + i * 5], bytes[6 + i * 5], bytes[7 + i * 5], bytes[8 + i * 5]});
            }
        }
    }

    private void getSchedule() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        String url = "";
        if (isOnline) {
            params.put("nodeId", mNodeListEntity.getId());
            url = Constant.Url.GET_SCHEDULE;
        }
        if (isGroupConfigure) {
            params.put("groupId", mGroupListEntity.getId());
            url = Constant.Url.GET_GROUP_SCHEDULE;
        }
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--在线时段返回值" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        String object = (String) noAction.getObject();
                        Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--时段值" + object);
                        final byte[] bytes = ByteUtils.hexStringToByte(object);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                scheduleData(bytes);
                            }
                        }).start();
                    } else {
                        if (noAction.getMessageCode() == 3) {
                            reLogin();
                        } else {
                            String receive = "848E001030" +
                                    "0100000000000000" +
                                    "0100000000000000" +
                                    "0100000000000000" +
                                    "0100000000000000" +
                                    "0100000000000000" +
                                    "0100000000000000" +
                                    "010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000" +
                                    "0200000000000000" +
                                    "020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000" +
                                    "0200000000000000" +
                                    "0200000000000000" +
                                    "0200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000" +
                                    "00000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000060000000000000006000000000000000600000000000000060000000000000006000000000000000600000000000000060000000000000006000000000000000600000000000000060000" +
                                    "00000000000600000000000000060000000000000006000000000000000600000000000000060000000000000006000000000000000600000000000000060000000000000006000000000000000600000000000000060000000000000006000000000000000600000000000000060000000000000006000000000000000600000000000000060000000000000006000000000000000600000000000000060000000000000006000000000000000600000000000000060000000000000006000000000000000600000000000000060000000000000006000000000000000600000000000000060000000000000006000000000000000600000000000000060000000000000006000000000000000600000000000000060000000000000006000000000000000600000000000000060000000000000007000000000000000700000000000000070000000000000007000000000000000700000000000000070000000000000007000000000000000700000000000000070000000000000007000000000000000700000000000000070000000000000007000000000000000700000000000000070000000000000007000000000000000700000000000000070000000000000007000000000000000700000000000000070000000000000007000000000000000700000000000000070000000000000007000000000000000700000000000000070000000000000007000000000000000700000000000000070000000000000007000000000000000700000000000000070000000000000007000000000000000700000000000000070000000000000007000000000000000700000000000000070000000000000007000000000000000700000000000000070000000000000007000000000000000700000000000000070000000000000007000000000000000700000000000000070000000000000008000000000000000800000000000000080000000000000008000000000000000800000000000000080000000000000008000000000000000800000000000000080000000000000008000000000000000800000000000000080000000000000008000000000000000800000000000000080000000000000008000000000000000800000000000000080000000000000008000000000000000800000000000000080000000000000008000000000000000800000000000000080000000000000008000000000000000800000000000000080000000000000008000000000000000800000000000000080000000000000008000000000000000800000000000000080000000000000008000000000000000800000000000000080000000000000008000000000000000800000000000000080000" +
                                    "00000000000800000000000000080000000000000008000000000000000800000000000000080000000000000008000000000000000800000000000000080000000000000008000000000000000900000000000000090000000000000009000000000000000900000000000000090000000000000009000000000000000900000000000000090000000000000009000000000000000900000000000000090000000000000009000000000000000900000000000000090000000000000009000000000000000900000000000000090000000000000009000000000000000900000000000000090000000000000009000000000000000900000000000000090000000000000009000000000000000900000000000000090000000000000009000000000000000900000000000000090000000000000009000000000000000900000000000000090000000000000009000000000000000900000000000000090000000000000009000000000000000900000000000000090000000000000009000000000000000900000000000000090000000000000009000000000000000900000000000000090000000000000009000000000000000900000000000000090000000000000009000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000A000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B0000" +
                                    "00000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000B000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000C000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000D000000000000000E0000" +
                                    "00000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000E000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F000000000000000F0000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000" +
                                    "0000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000";
                            scheduleData(ByteUtils.hexStringToByte(receive));
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "获取时段表失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scheduleData(byte[] bytes) {
        if ((ByteUtils.bytesUInt(bytes[1]) == 142 && ByteUtils.bytesUInt(bytes[0]) == 132) || (ByteUtils.bytesUInt(bytes[1]) == 142 && ByteUtils.bytesUInt(bytes[0]) == 129)) {
            //848E0010
            // 3001 01时段序号 08正点数 00分数 00控制方式 01配时方案 00
            // 0001 02        17     0E     03       03       00
            // 0001000000000000
            for (int j = 0; j < shiduanByteListList.size(); j++) {
                shiduanByteListList.get(j).clear();
                for (int i = 0; i < 48; i++) {
                    shiduanByteListList.get(j).add(getByteArr(i, j, bytes));
                }
            }
            initUI();
            isShiDuanExist();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMyAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * 设置信号机提示
     *
     * @param bytes
     */
    private void setData(final byte[] bytes) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ByteUtils.bytesUInt(bytes[0]) == 133 && ByteUtils.bytesUInt(bytes[1]) == 142) {
                    Toast.makeText(getActivity(), "更新信号机成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "更新信号机失败", Toast.LENGTH_SHORT).show();
                }
                hideSetShiDuan();
            }
        });
    }

    /**
     * 将能用的时段号取出来
     */
    private void isShiDuanExist() {
        shiDuanUsedList.clear();
        for (int i = 0; i < shiduanByteListList.size(); i++) {
            ArrayList<byte[]> bytesArrList = shiduanByteListList.get(i);
            for (int j = 0; j < bytesArrList.size(); j++) {
                if (ByteUtils.bytesUInt(bytesArrList.get(j)[2]) != 0) {
                    shiDuanUsedList.add(i + 1);
                    break;
                }
            }
        }
    }

    /**
     * 更新UI
     */
    private void initUI() {
        mShiDuanInfoList.clear();
        ArrayList<byte[]> shiDuanInfoBytes = shiduanByteListList.get(currentIndex - 1);
        for (int i = 0; i < shiDuanInfoBytes.size(); i++) {
            byte[] bytes = shiDuanInfoBytes.get(i);
            // 3001 01时段序号 08正点数 00分数 00控制方式 01配时方案 00
            // 0001 02        17     0E     03       03       00
            if (ByteUtils.bytesUInt(bytes[2]) != 0) {
                mShiDuanInfoList.add(new ShiDuanInfo(ByteUtils.bytesUInt(bytes[1]), ByteUtils.bytesUInt(bytes[2]), ByteUtils.bytesUInt(bytes[3]), ByteUtils.bytesUInt(bytes[4]), ByteUtils.bytesUInt(bytes[5])));
            }
        }
    }

    public byte[] getByteArr(int num, int index, byte[] theBytes) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = theBytes[5 + num * 8 + index * 48 * 8 + i];
        }
        return bytes;
    }

    /**
     * 时段信息list adapter
     */
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mShiDuanInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = getLayoutInflater(getArguments()).inflate(R.layout.shiduan_item, null);
            TextView tv_ShiDuanXuHao = (TextView) inflate.findViewById(R.id.tv_ShiDuanXuHao);
            TextView tv_ShiJian = (TextView) inflate.findViewById(R.id.tv_ShiJian);
            TextView tv_ControlType = (TextView) inflate.findViewById(R.id.tv_ControlType);
            TextView tv_FangAn = (TextView) inflate.findViewById(R.id.tv_FangAn);
            ShiDuanInfo shiDuanInfo = mShiDuanInfoList.get(position);
            tv_ShiDuanXuHao.setText(shiDuanInfo.getShiDuanNum() + "");
            tv_ShiJian.setText(shiDuanInfo.getShiDuanShi() + ":" + shiDuanInfo.getShiDuanFen());
            tv_ControlType.setText(controlTypeValue.get(shiDuanInfo.getControlType()));
            tv_FangAn.setText(shiDuanInfo.getPeiShiFangAn() + "");
            return inflate;
        }
    }

    /**
     * String列表dialog
     *
     * @param list
     */
    private void showStringListDialog(List<Integer> list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View list_dialog_view = getActivity().getLayoutInflater().inflate(R.layout.list_dialog, null);
        ListView dialogListView = (ListView) list_dialog_view.findViewById(R.id.listView);
        dialogListView.setAdapter(new MyStringDialogAdapter(list));
        dialogListView.setOnItemClickListener(new MyStringItemClick(list));
        mListDialog = builder.setView(list_dialog_view)
                .create();
        mListDialog.show();
        int orientation = getActivity().getResources().getConfiguration().orientation;
        Window dialogWindow = mListDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getActivity().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        if (orientation == 1) {
            lp.width = (int) (d.widthPixels * 0.8); // 宽度设置为屏幕的0.6
            if (list.size() > 5) {
                lp.height = (int) (d.heightPixels * 0.5);
            }
        } else {
            lp.width = (int) (d.widthPixels * 0.5); // 宽度设置为屏幕的0.6
            if (list.size() >= 5) {
                lp.height = (int) (d.heightPixels * 0.8);
            }
        }
        dialogWindow.setAttributes(lp);
    }

    /**
     * 数字列表dialog
     *
     * @param list
     */
    private void showListDialog(List<Integer> list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View list_dialog_view = getActivity().getLayoutInflater().inflate(R.layout.list_dialog, null);
        ListView dialogListView = (ListView) list_dialog_view.findViewById(R.id.listView);
        dialogListView.setAdapter(new MyDialogAdapter(list));
        dialogListView.setOnItemClickListener(new MyItemClick(list));
        mListDialog = builder.setView(list_dialog_view)
                .create();
        mListDialog.show();
        int orientation = getActivity().getResources().getConfiguration().orientation;
        Window dialogWindow = mListDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getActivity().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        if (orientation == 1) {
            lp.width = (int) (d.widthPixels * 0.8); // 宽度设置为屏幕的0.6
            if (list.size() > 5) {
                lp.height = (int) (d.heightPixels * 0.5);
            }
        } else {
            lp.width = (int) (d.widthPixels * 0.5); // 宽度设置为屏幕的0.6
            if (list.size() >= 5) {
                lp.height = (int) (d.heightPixels * 0.8);
            }
        }
        dialogWindow.setAttributes(lp);
    }

    public class MyStringItemClick implements AdapterView.OnItemClickListener {

        private List<Integer> list;

        public MyStringItemClick(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mCurrentControlType = list.get(i);
            mTv_controlType.setText(controlTypeValue.get(mCurrentControlType));
            mListDialog.dismiss();
        }
    }

    public class MyItemClick implements AdapterView.OnItemClickListener {

        private List<Integer> list;

        public MyItemClick(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Integer integer = list.get(i);
            if (TextUtils.equals("ShiDuan", WhatIsShow)) {
                mTv_chooseShiDuan.setText("时段编号：" + integer);
                currentIndex = integer;
                hideSetShiDuan();
                initUI();
                mMyAdapter.notifyDataSetChanged();
            } else if (TextUtils.equals("ShiDuanXuHao", WhatIsShow)) {
                mTv_shiDuanXuHao.setText("" + integer);
            } else if (TextUtils.equals("FangAn", WhatIsShow)) {
                mTv_fangAn.setText(integer + "");
            }
            mListDialog.dismiss();
        }
    }

    /**
     * 隐藏修改界面
     */
    private void hideSetShiDuan() {
        mLinear_addShiDuan.setVisibility(View.GONE);
        mButtonShiDuan.setText(R.string.addShIDuan);
        mTv_shiDuanXuHao.setText("0");
        mTv_shiJian.setText("0:0");
        mTv_controlType.setText("请选择");
        mTv_fangAn.setText("请选择");
    }

    class MyStringDialogAdapter extends BaseAdapter {

        private List<Integer> shiji;

        public MyStringDialogAdapter(List<Integer> shiji) {
            this.shiji = shiji;
        }

        @Override
        public int getCount() {
            return shiji.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View mDialog_list_itemView = getLayoutInflater(getArguments()).inflate(R.layout.dialog_list_item, null);
            TextView textListItem = (TextView) mDialog_list_itemView.findViewById(R.id.textListItem);
            textListItem.setText(controlTypeValue.get(shiji.get(position)));
            return mDialog_list_itemView;
        }
    }

    class MyDialogAdapter extends BaseAdapter {

        private List<Integer> shiji;

        public MyDialogAdapter(List<Integer> shiji) {
            this.shiji = shiji;
        }

        @Override
        public int getCount() {
            return shiji.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View mDialog_list_itemView = getLayoutInflater(getArguments()).inflate(R.layout.dialog_list_item, null);
            View linear_dialog = mDialog_list_itemView.findViewById(R.id.linear_dialog);
            TextView textListItem = (TextView) mDialog_list_itemView.findViewById(R.id.textListItem);
            textListItem.setText(shiji.get(position) + "");
            if (TextUtils.equals("ShiDuan", WhatIsShow)) {
                if (shiDuanUsedList.contains(shiji.get(position))) {
                    linear_dialog.setBackgroundColor(Color.RED);
                }
            }
            return mDialog_list_itemView;
        }
    }

    /**
     * 设置时段
     */
    class setShiDuanRunnable implements Runnable {

        @Override
        public void run() {
            try {
                //848E0010
                // 01 01时段序号 08正点数 00分数 00控制方式 01配时方案 00 00
                // 01 02        17     0E     03       03       00 00
                // 0100000000000000
                String[] shijian = mTv_shiJian.getText().toString().split(":");
                byte[] byteShiDuanInfo = new byte[8];
                int currentXuHao = Integer.parseInt(mTv_shiDuanXuHao.getText().toString());
                byte[] bytes2 = new byte[]{(byte) currentIndex, (byte) currentXuHao, (byte) Integer.parseInt(shijian[0]), (byte) Integer.parseInt(shijian[1]), (byte) mCurrentControlType, (byte) Integer.parseInt(mTv_fangAn.getText().toString()), 0x00, 0x00};
                System.arraycopy(bytes2, 0, byteShiDuanInfo, 0, bytes2.length);
                String s = ByteUtils.bytesToHexString(byteShiDuanInfo);
                Log.e("setShiDuanRunnable", "setShiDuanRunnable--run--时段拼接的对象" + s);
                String sendShiDuanHexStr = "818E001030";
                for (int i = 0; i < 16; i++) {
                    ArrayList<byte[]> bytes = shiduanByteListList.get(i);
                    for (int j = 0; j < bytes.size(); j++) {
                        if (currentIndex == (i + 1) && currentXuHao == (j + 1)) {
                            sendShiDuanHexStr = sendShiDuanHexStr + s;
                        } else {
                            sendShiDuanHexStr = sendShiDuanHexStr + ByteUtils.bytesToHexString(bytes.get(j));
                        }
                    }
                }
                byte[] bytesSendShiDuan = ByteUtils.hexStringToByte(sendShiDuanHexStr);
                UdpClientSocket mUdpClientSocket = new UdpClientSocket();
                mUdpClientSocket.send(mIp, GbtDefine.GBT_PORT, bytesSendShiDuan);
                byte[] bytes = mUdpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                String s1 = ByteUtils.bytesToHexString(bytes);
                Log.e("setShiDuanRunnable", "setShiDuanRunnable--run--设置时段返回值" + s1);
                setData(bytes);
                new Thread(new ShiDuanRunnable()).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 长按删除时段
     */
    class MyLongItemClickListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("是否删除该时段")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            if (isOnline || isGroupConfigure) {
                                deleteSchedule(mShiDuanInfoList.get(i).getShiDuanNum());
                            } else {
                                new Thread(new SetShiDuanEmpty(mShiDuanInfoList.get(i).getShiDuanNum())).start();
                            }
                        }
                    })
                    .setNegativeButton("否", null)
                    .create()
                    .show();
            return false;
        }
    }

    private void deleteSchedule(int xuHao) {
        String s = ByteUtils.bytesToHexString(new byte[]{(byte) currentIndex}) + "00000000000000";
        Log.e("setShiDuanRunnable", "setShiDuanRunnable--run--时段拼接的对象" + s);
        String sendShiDuanHexStr = "818E001030";
        for (int i = 0; i < 16; i++) {
            ArrayList<byte[]> bytes = shiduanByteListList.get(i);
            for (int j = 0; j < bytes.size(); j++) {
                if (currentIndex == (i + 1) && xuHao == (j + 1)) {
                    sendShiDuanHexStr = sendShiDuanHexStr + s;
                } else {
                    sendShiDuanHexStr = sendShiDuanHexStr + ByteUtils.bytesToHexString(bytes.get(j));
                }
            }
        }
        scheduleAsyn(sendShiDuanHexStr);
    }

    /**
     * 删除时段
     */
    class SetShiDuanEmpty implements Runnable {

        private int xuHao;

        public SetShiDuanEmpty(int xuHao) {
            this.xuHao = xuHao;
        }

        @Override
        public void run() {
            try {
                String s = ByteUtils.bytesToHexString(new byte[]{(byte) currentIndex}) + "00000000000000";
                Log.e("setShiDuanRunnable", "setShiDuanRunnable--run--时段拼接的对象" + s);
                String sendShiDuanHexStr = "818E001030";
                for (int i = 0; i < 16; i++) {
                    ArrayList<byte[]> bytes = shiduanByteListList.get(i);
                    for (int j = 0; j < bytes.size(); j++) {
                        if (currentIndex == (i + 1) && xuHao == (j + 1)) {
                            sendShiDuanHexStr = sendShiDuanHexStr + s;
                        } else {
                            sendShiDuanHexStr = sendShiDuanHexStr + ByteUtils.bytesToHexString(bytes.get(j));
                        }
                    }
                }
                byte[] bytesSendShiDuan = ByteUtils.hexStringToByte(sendShiDuanHexStr);
                UdpClientSocket mUdpClientSocket = new UdpClientSocket();
                mUdpClientSocket.send(mIp, GbtDefine.GBT_PORT, bytesSendShiDuan);
                byte[] bytes = mUdpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                String s1 = ByteUtils.bytesToHexString(bytes);
                Log.e("setShiDuanRunnable", "setShiDuanRunnable--run--设置空时段返回值" + s1);
                new Thread(new ShiDuanRunnable()).start();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideSetShiDuan();
                    }
                });
                setData(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 选择时段进行修改
     */
    class MyOnitemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ShiDuanInfo shiDuanInfo = mShiDuanInfoList.get(i);
            mLinear_addShiDuan.setVisibility(View.VISIBLE);
            mButtonShiDuan.setText(R.string.saveShIDuan);
            mTv_shiDuanXuHao.setText(shiDuanInfo.getShiDuanNum() + "");
            mTv_shiJian.setText(shiDuanInfo.getShiDuanShi() + ":" + shiDuanInfo.getShiDuanFen());
            mCurrentControlType = shiDuanInfo.getControlType();
            mTv_controlType.setText(controlTypeValue.get(mCurrentControlType));
            mTv_fangAn.setText(shiDuanInfo.getPeiShiFangAn() + "");
        }
    }

    /**
     * 获取配时表
     */
    class PeiShiRunnable implements Runnable {

        @Override
        public void run() {
            try {
                UdpClientSocket mUdpClientSocket = new UdpClientSocket();
                mUdpClientSocket.send(mIp, GbtDefine.GBT_PORT, GbtDefine.GET_PATTERN);
                byte[] bytes = mUdpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                timePattern(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取时段表
     */
    class ShiDuanRunnable implements Runnable {

        @Override
        public void run() {
            try {
                UdpClientSocket mUdpClientSocket = new UdpClientSocket();
                mUdpClientSocket.send(mIp, GbtDefine.GBT_PORT, GbtDefine.GET_SCHEDULE);
                byte[] bytes = mUdpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                LogUtil.LogShitou("时段", ByteUtils.bytesToHexString(bytes));
                Log.e("ShiDuanRunnable", "ShiDuanRunnable--run--时段表长度" + ByteUtils.bytesToHexString(bytes).length());
                scheduleData(bytes);
            } catch (Exception e) {
                Log.e("ShiDuanRunnable", "ShiDuanRunnable--run--时段表返回异常");
                e.printStackTrace();
            }
        }
    }
}