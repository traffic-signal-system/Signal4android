package com.aiton.zjb.signal.fragment.timebasefragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseFragment;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.constant.GbtDefine;
import com.aiton.zjb.signal.model.GroupListEntity;
import com.aiton.zjb.signal.model.NoAction;
import com.aiton.zjb.signal.model.NodeListEntity;
import com.aiton.zjb.signal.model.ShiJiInfo;
import com.aiton.zjb.signal.model.UserInfo;
import com.aiton.zjb.signal.util.ByteUtils;
import com.aiton.zjb.signal.util.UdpClientSocket;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 时段信息分析
 * 848E0010
 * 3001010800000100
 * 0001021200000100
 * 0001000000000000
 * 00010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000010000000000000001000000000000000100000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000020000000000000002000000000000000200000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000030000000000000003000000000000000300000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000040000000000000004000000000000000400000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000050000000000000005000000000000000500000000000000060000000000000006000000000000000600000000000000060000000000000006000000000000000600000000000000060000000000000006000000000000000600000000000000060000000000
 * 848E0010
 * 3001010800000100
 * 30时段表号48行 01时段事件号 01开始执行的时刻整点数 08开始执行的整分数 00控制方式 00配时方案号 01辅助功能输出 00特殊功能输出
 * 0001021200000100
 * 0001030A00000100
 * <p/>
 * 84消息类型 8E对象标识（取值1~N） 00 10时段16个
 */
public class MonthBaseTimeFragment extends ZjbBaseFragment implements View.OnClickListener {

    private View mInflate;
    private String mIp;
    private TextView mText_baseTimeIndex;
    private TextView mText_shiDuan;
    private String LISTDIALOGISSHOW = "listDialogIsShow";
    private String WhatIsShow = "";
    private AlertDialog mListDialog;
    private List<Integer> shiJiList = new ArrayList<>();
    private List<Integer> shiDuanList = new ArrayList<>();
    private List<ShiJiInfo> shiJiInfoList = new ArrayList<>();
    private int[] checkBoxID = new int[]{
            R.id.checkBox,
            R.id.checkBox2,
            R.id.checkBox3,
            R.id.checkBox4,
            R.id.checkBox5,
            R.id.checkBox6,
            R.id.checkBox7,
            R.id.checkBox8,
            R.id.checkBox9,
            R.id.checkBox10,
            R.id.checkBox11,
            R.id.checkBox12
    };
    private CheckBox[] mCheckBoxes = new CheckBox[12];
    private View mLinear_peizhi;
    private View mRela_peizhi;
    private int currentIndex = 31;
    private int currentShiDuan = 1;
    private ArrayList<byte[]> baseTimeInfo = new ArrayList<>();
    private ArrayList<byte[]> baseTimeInfoMonth = new ArrayList<>();
    private ArrayList<byte[]> baseTimeInfoWeek = new ArrayList<>();
    private ArrayList<byte[]> baseTimeInfoDay = new ArrayList<>();
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
    private List<ArrayList<byte[]>> shiduanByteListList = new ArrayList<>();
    private int mTimeBaseNum;
    private boolean mHasShiJiInfo;//当前页面是否是已经配置过
    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;
    private NodeListEntity mNodeListEntity;
    private boolean isOnline = false;
    private GroupListEntity mGroupListEntity;
    private boolean isGroupConfigure = false;

    public MonthBaseTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_month_base_time, container, false);
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
        for (int i = 0; i < 10; i++) {
            shiJiList.add(31 + i);
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
    }

    @Override
    protected void findID() {
        mText_baseTimeIndex = (TextView) mInflate.findViewById(R.id.text_baseTimeIndex);
        mText_shiDuan = (TextView) mInflate.findViewById(R.id.text_ShiDuan);
        for (int i = 0; i < checkBoxID.length; i++) {
            mCheckBoxes[i] = (CheckBox) mInflate.findViewById(checkBoxID[i]);
        }
        mLinear_peizhi = mInflate.findViewById(R.id.linear_peizhi);
        mRela_peizhi = mInflate.findViewById(R.id.rela_peizhi);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {
        mText_baseTimeIndex.setOnClickListener(this);
        mText_shiDuan.setOnClickListener(this);
        mInflate.findViewById(R.id.button_peizhi).setOnClickListener(this);
        mInflate.findViewById(R.id.button_saveBaseTime).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isOnline || isGroupConfigure) {
            getTimeBase();
            getSchedule();
        } else {
            new Thread(new ShiJiRunnable()).start();
            new Thread(new ShiDuanRunnable()).start();
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

    private void getTimeBase() {
        Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onStart--获取时基");
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        String url = "";
        if (isOnline) {
            params.put("nodeId", mNodeListEntity.getId());
            url = Constant.Url.GET_TIME_BASE;
        }
        if (isGroupConfigure) {
            params.put("groupId", mGroupListEntity.getId());
            url = Constant.Url.GET_GROUP_TIME_BASE;
        }
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--在线时基返回值" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        String object = (String) noAction.getObject();
                        Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--时基值" + object);
                        final byte[] bytes = ByteUtils.hexStringToByte(object);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                timeBaseData(bytes);
                            }
                        }).start();
                    } else {
                        if (noAction.getMessageCode() == 3) {
                            reLogin();
                        } else {
                            //初始化数据
                            String receive = "848D0000";
                            timeBaseData(ByteUtils.hexStringToByte(receive));
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "获取时基表失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 设置月时基后提示
     *
     * @param bytes
     */
    private void setData(final byte[] bytes) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ByteUtils.bytesUInt(bytes[1]) == 141 && ByteUtils.bytesUInt(bytes[0]) == 133) {
                    Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 将能用的时段号取出来
     */
    private void isShiDuanExist() {
        shiDuanList.clear();
        for (int i = 0; i < shiduanByteListList.size(); i++) {
            ArrayList<byte[]> bytesArrList = shiduanByteListList.get(i);
            for (int j = 0; j < bytesArrList.size(); j++) {
                String s = ByteUtils.bytesToHexString(bytesArrList.get(j));
                if (ByteUtils.bytesUInt(bytesArrList.get(j)[2]) != 0) {
                    shiDuanList.add(i + 1);
                    break;
                }
            }
        }
    }

    /**
     * 检查是否含有时基信息而显示对应的界面
     *
     * @param index
     */
    public void checkShiJiInfo(int index) {
        mHasShiJiInfo = isHasShiJiInfo(index);
        if (mHasShiJiInfo) {
            mLinear_peizhi.setVisibility(View.VISIBLE);
            mRela_peizhi.setVisibility(View.GONE);
        } else {
            mLinear_peizhi.setVisibility(View.GONE);
            mRela_peizhi.setVisibility(View.VISIBLE);
            mText_shiDuan.setText("时段编号：");
            currentShiDuan = 0;
        }
    }

    /**
     * 判断该时基是否有有效信息
     *
     * @param index
     * @return
     */
    public boolean isHasShiJiInfo(int index) {
        for (int i = 0; i < shiJiInfoList.size(); i++) {
            if (shiJiInfoList.get(i).getIndex() == index) {
                int[] shiJiInfoArr = shiJiInfoList.get(i).getShiJiInfoArr();
                for (int j = 1; j < 13; j++) {
                    if (shiJiInfoArr[j] == 1) {
                        mCheckBoxes[j - 1].setChecked(true);
                    }
                }
                currentShiDuan = shiJiInfoList.get(i).getDiaoDuHao();
                mText_shiDuan.setText("时段编号：" + currentShiDuan);
                return true;
            }
        }
        return false;
    }

    public byte[] getByteArr(int num, int index, byte[] theBytes) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = theBytes[4 + num * 8 + index * 48 * 8 + i];
        }
        return bytes;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_saveBaseTime:
                if (currentShiDuan == 0) {
                    showTipDialog("请先选择时段");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("是否更新到信号机");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (isOnline || isGroupConfigure) {
                                setBaseTime();
                            } else {
                                new Thread(new SetShiJiRunnable()).start();
                            }
                        }
                    })
                            .setNegativeButton("否", null)
                            .create()
                            .show();
                }
                break;
            case R.id.button_peizhi:
                mLinear_peizhi.setVisibility(View.VISIBLE);
                mRela_peizhi.setVisibility(View.GONE);
                for (int i = 0; i < checkBoxID.length; i++) {
                    mCheckBoxes[i].setChecked(false);
                }
                break;
            case R.id.text_baseTimeIndex:
                WhatIsShow = "shijiShow";
                showListDialog(shiJiList);
                break;
            case R.id.text_ShiDuan:
                WhatIsShow = "shiduanShow";
                showListDialog(shiDuanList);
                break;
        }
    }

    private void setBaseTime() {
        byte[] monthCheckByteArr = getMonthCheckByteArr();
        String baseTimeStr = "";
        if (mHasShiJiInfo) {
            byte[] bytes1 = ByteUtils.byteMerger(new byte[]{(byte) currentIndex}, monthCheckByteArr);
            byte[] bytes2 = {(byte) 0xFE, (byte) 0xFF, (byte) 0xFF, (byte) 0XFF, (byte) 0xFE, (byte) currentShiDuan};
            byte[] bytesShiJiInfo = ByteUtils.byteMerger(bytes1, bytes2);
            Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--run--合成的时基对象" + ByteUtils.bytesToHexString(bytesShiJiInfo));
            //848D0003
            // 011FFEFEFFFFFFFE01
            // 151FFE24FFFFFFFE01
            // 1F0646FEFFFFFFFE01
            byte[] baseInfoListInfo = new byte[mTimeBaseNum * 9];
            for (int i = 0; i < baseTimeInfo.size(); i++) {
                if (ByteUtils.bytesUInt(baseTimeInfo.get(i)[0]) == currentIndex) {
                    System.arraycopy(bytesShiJiInfo, 0, baseInfoListInfo, i * 9, 9);
                } else {
                    System.arraycopy(baseTimeInfo.get(i), 0, baseInfoListInfo, i * 9, 9);
                }
            }
            byte[] timeBaseNumByteArr = {(byte) mTimeBaseNum};
            byte[] sendSetShiJi = ByteUtils.byteMerger(timeBaseNumByteArr, baseInfoListInfo);
            byte[] bytes = ByteUtils.byteMerger(GbtDefine.SET_PLAN_RESPONSE, sendSetShiJi);
            baseTimeStr = ByteUtils.bytesToHexString(bytes);
        } else {
            byte[] bytes1 = ByteUtils.byteMerger(new byte[]{(byte) currentIndex}, monthCheckByteArr);
            byte[] bytes2 = {(byte) 0xFE, (byte) 0xFF, (byte) 0xFF, (byte) 0XFF, (byte) 0xFE, (byte) currentShiDuan};
            byte[] bytesShiJiInfo = ByteUtils.byteMerger(bytes1, bytes2);
            byte[] baseInfoListInfo = new byte[(mTimeBaseNum + 1) * 9];
            for (int i = 0; i < baseTimeInfo.size(); i++) {
                System.arraycopy(baseTimeInfo.get(i), 0, baseInfoListInfo, i * 9, 9);
            }
            System.arraycopy(bytesShiJiInfo, 0, baseInfoListInfo, mTimeBaseNum * 9, 9);
            byte[] timeBaseNumByteArr = {(byte) (mTimeBaseNum + 1)};
            byte[] sendSetShiJi = ByteUtils.byteMerger(timeBaseNumByteArr, baseInfoListInfo);
            byte[] bytes = ByteUtils.byteMerger(GbtDefine.SET_PLAN_RESPONSE, sendSetShiJi);
            baseTimeStr = ByteUtils.bytesToHexString(bytes);
        }
        Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--setBaseTime--发送时基设置" + baseTimeStr);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("byteString", baseTimeStr);
        String url = "";
        if (isOnline) {
            params.put("nodeId", mNodeListEntity.getId());
            url = Constant.Url.SET_TIMEBASE;
        }
        if (isGroupConfigure) {
            params.put("groupId", mGroupListEntity.getId());
            url = Constant.Url.SET_GROUP_TIME_BASE;
        }
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--设置时基返回值" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        if (noAction.getObject()==null){
                            Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                        }else {
                            String object = (String) noAction.getObject();
                            byte[] bytes = ByteUtils.hexStringToByte(object);
                            if (ByteUtils.bytesUInt(bytes[1]) == 141 && ByteUtils.bytesUInt(bytes[0]) == 133) {
                                Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                                getTimeBase();
                            } else {
                                Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (noAction.getMessageCode() == 3) {
                            reLogin();
                        }else {
                            showTipDialog(noAction.getMessage());
                        }
                    }
                    getTimeBase();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    /**
     * 将月份的选择情况转化为符合要求的byte[]
     *
     * @return
     */
    private byte[] getMonthCheckByteArr() {
        String strMonthCheck = "";
        String strMonthCheck01 = "0";
        String strMonthCheck02 = "";
        for (int i = 0; i < mCheckBoxes.length; i++) {
            if (i >= 0 && i < 7) {
                if (mCheckBoxes[i].isChecked()) {
                    strMonthCheck01 = strMonthCheck01 + 1;
                } else {
                    strMonthCheck01 = strMonthCheck01 + 0;
                }
            } else {
                if (mCheckBoxes[i].isChecked()) {
                    strMonthCheck02 = strMonthCheck02 + 1;
                } else {
                    strMonthCheck02 = strMonthCheck02 + 0;
                }
            }

        }
        //翻转字符串
        strMonthCheck01 = new StringBuilder(strMonthCheck01).reverse().toString();
        strMonthCheck02 = new StringBuilder(strMonthCheck02 + "000").reverse().toString();
        strMonthCheck = strMonthCheck02 + strMonthCheck01;
        Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--getMonthCheckByteArr--月时基二进制字符" + strMonthCheck);
        Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--getMonthCheckByteArr--二进制字符" + strMonthCheck01);
        Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--getMonthCheckByteArr--二进制字符" + strMonthCheck02);
        byte b1 = ByteUtils.bit2byte(strMonthCheck.substring(0, 8));
        byte b2 = ByteUtils.bit2byte(strMonthCheck.substring(8, 16));
        byte[] bytesMonthCheck = new byte[]{b1, b2};
        return bytesMonthCheck;
    }

    /**
     * 列表dialog
     *
     * @param list
     */
    private void showListDialog(List<Integer> list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View list_dialog_view = getActivity().getLayoutInflater().inflate(R.layout.list_dialog, null);
        ListView dialogListView = (ListView) list_dialog_view.findViewById(R.id.listView);
        dialogListView.setAdapter(new MyAdapter(list));
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

    /**
     * 列表list itemclick
     */
    public class MyItemClick implements AdapterView.OnItemClickListener {

        private List<Integer> list;

        public MyItemClick(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (TextUtils.equals(WhatIsShow, "shijiShow")) {
                mText_baseTimeIndex.setText("时基编号：" + list.get(i));
                currentIndex = list.get(i);
                for (int k = 0; k < mCheckBoxes.length; k++) {
                    mCheckBoxes[k].setChecked(false);
                }
                checkShiJiInfo(currentIndex);
            } else {
                currentShiDuan = list.get(i);
                mText_shiDuan.setText("时段编号：" + list.get(i));
            }
            mListDialog.dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //判断是否有弹窗
        if (mListDialog != null) {
            if (mListDialog.isShowing()) {
                outState.putBoolean(LISTDIALOGISSHOW, true);
                outState.putString("whatIsShow", WhatIsShow);
                mListDialog.dismiss();
            } else {
                outState.putBoolean(LISTDIALOGISSHOW, false);
            }
        }
        //保存当前时基页数
        outState.putInt("currentIndex", currentIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //对弹窗的处理
            boolean aBoolean = savedInstanceState.getBoolean(LISTDIALOGISSHOW);
            if (aBoolean) {
                String whatIsShow = savedInstanceState.getString("whatIsShow");
                if (TextUtils.equals(whatIsShow, "shijiShow")) {
                    WhatIsShow = "shijiShow";
                    showListDialog(shiJiList);
                } else {
                    WhatIsShow = "shiduanShow";
                    showListDialog(shiDuanList);
                }
            }
            //取出当前时基页数
            currentIndex = savedInstanceState.getInt("currentIndex");
            mText_baseTimeIndex.setText("时基编号：" + currentIndex);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    /**
     * 列表dialog adapter
     */
    class MyAdapter extends BaseAdapter {

        private List<Integer> shiji;

        public MyAdapter(List<Integer> shiji) {
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
            View linear_dialog = mDialog_list_itemView.findViewById(R.id.linear_dialog);
            if (TextUtils.equals(WhatIsShow, "shijiShow")) {
                for (int i = 0; i < shiJiInfoList.size(); i++) {
                    if (shiJiInfoList.get(i).getIndex() == shiji.get(position)) {
                        linear_dialog.setBackgroundColor(Color.RED);
                    }
                }
            }
            textListItem.setText(shiji.get(position) + "");
            return mDialog_list_itemView;
        }
    }

    /**
     * 设置时基
     */
    class SetShiJiRunnable implements Runnable {

        @Override
        public void run() {
            try {
                //84 8D 00 06 011FFEFEFFFFFFFE01 020202FE0000020201 030000FE0000000001 151FFE02FFFFFFFE01 160000000000000001 1F0002FEFFFFFFFE01
                //84消息类型 8D对象表示（时基表） 00 06对象数 011FFEFEFFFFFFFE01对象 020202FE0000020201 030000FE0000000001 151FFE02FFFFFFFE01 160000000000000001 1F0002FEFFFFFFFE01
                //01调度计划号 1FFE调度月 FE调度周 FFFFFFFE调度日 01时段表号
                //1F 0002 FE FFFFFFFE 01 一月     0000 0000 0000 0010
                //1F 0082 FE FFFFFFFE 01 一月 七月 0000 0000 1000 0010
                byte[] monthCheckByteArr = getMonthCheckByteArr();

                if (mHasShiJiInfo) {
                    byte[] bytes1 = ByteUtils.byteMerger(new byte[]{(byte) currentIndex}, monthCheckByteArr);
                    byte[] bytes2 = {(byte) 0xFE, (byte) 0xFF, (byte) 0xFF, (byte) 0XFF, (byte) 0xFE, (byte) currentShiDuan};
                    byte[] bytesShiJiInfo = ByteUtils.byteMerger(bytes1, bytes2);
                    Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--run--合成的时基对象" + ByteUtils.bytesToHexString(bytesShiJiInfo));
                    //848D0003
                    // 011FFEFEFFFFFFFE01
                    // 151FFE24FFFFFFFE01
                    // 1F0646FEFFFFFFFE01
                    byte[] baseInfoListInfo = new byte[mTimeBaseNum * 9];
                    for (int i = 0; i < baseTimeInfo.size(); i++) {
                        if (ByteUtils.bytesUInt(baseTimeInfo.get(i)[0]) == currentIndex) {
                            System.arraycopy(bytesShiJiInfo, 0, baseInfoListInfo, i * 9, 9);
                        } else {
                            System.arraycopy(baseTimeInfo.get(i), 0, baseInfoListInfo, i * 9, 9);
                        }
                    }
                    byte[] timeBaseNumByteArr = {(byte) mTimeBaseNum};
                    byte[] sendSetShiJi = ByteUtils.byteMerger(timeBaseNumByteArr, baseInfoListInfo);
                    Log.e("SetShiJiRunnable", "SetShiJiRunnable--run--设置时基" + ByteUtils.bytesToHexString(sendSetShiJi));
                    UdpClientSocket udpClientSocket = new UdpClientSocket();
                    udpClientSocket.send(mIp, GbtDefine.GBT_PORT, ByteUtils.byteMerger(GbtDefine.SET_PLAN_RESPONSE, sendSetShiJi));
                    byte[] bytes = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                    Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--run--配置时基返回值" + ByteUtils.bytesToHexString(bytes));
                    //重新更新时基显示
                    new Thread(new ShiJiRunnable()).start();
                    setData(bytes);
                } else {
                    byte[] bytes1 = ByteUtils.byteMerger(new byte[]{(byte) currentIndex}, monthCheckByteArr);
                    byte[] bytes2 = {(byte) 0xFE, (byte) 0xFF, (byte) 0xFF, (byte) 0XFF, (byte) 0xFE, (byte) currentShiDuan};
                    byte[] bytesShiJiInfo = ByteUtils.byteMerger(bytes1, bytes2);
                    byte[] baseInfoListInfo = new byte[(mTimeBaseNum + 1) * 9];
                    for (int i = 0; i < baseTimeInfo.size(); i++) {
                        System.arraycopy(baseTimeInfo.get(i), 0, baseInfoListInfo, i * 9, 9);
                    }
                    System.arraycopy(bytesShiJiInfo, 0, baseInfoListInfo, mTimeBaseNum * 9, 9);
                    byte[] timeBaseNumByteArr = {(byte) (mTimeBaseNum + 1)};
                    byte[] sendSetShiJi = ByteUtils.byteMerger(timeBaseNumByteArr, baseInfoListInfo);
                    UdpClientSocket udpClientSocket = new UdpClientSocket();
                    udpClientSocket.send(mIp, GbtDefine.GBT_PORT, ByteUtils.byteMerger(GbtDefine.SET_PLAN_RESPONSE, sendSetShiJi));
                    byte[] bytes = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                    Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--run--配置新增时基返回值" + ByteUtils.bytesToHexString(bytes));
                    //重新更新时基显示
                    new Thread(new ShiJiRunnable()).start();
                    setData(bytes);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--run--抛异常了");
            }
        }
    }

    /**
     * 获取时基表
     */
    class ShiJiRunnable implements Runnable {

        @Override
        public void run() {
            try {
                UdpClientSocket mUdpClientSocket01 = new UdpClientSocket();
                mUdpClientSocket01.send(mIp, GbtDefine.GBT_PORT, GbtDefine.GET_PLAN);
                byte[] bytes = mUdpClientSocket01.receiveByte(mIp, GbtDefine.GBT_PORT);
                timeBaseData(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void timeBaseData(byte[] bytes) {
        String s = ByteUtils.bytesToHexString(bytes);
        Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--run--时基表返回" + s);
        //84 8D 00 06 011FFEFEFFFFFFFE01 020202FE0000020201 030000FE0000000001 151FFE02FFFFFFFE01 160000000000000001 1F0002FEFFFFFFFE01
        //84消息类型 8D对象表示（时基表） 00 06对象数 011FFEFEFFFFFFFE01对象 020202FE0000020201 030000FE0000000001 151FFE02FFFFFFFE01 160000000000000001 1F0002FEFFFFFFFE01
        if ((ByteUtils.bytesUInt(bytes[1]) == 141 && ByteUtils.bytesUInt(bytes[0]) == 132) || (ByteUtils.bytesUInt(bytes[1]) == 141 && ByteUtils.bytesUInt(bytes[0]) == 129)) {
            //01调度计划号 1FFE调度月 FE调度周 FFFFFFFE调度日 01时段表号
            //1F 0002 FE FFFFFFFE 01 一月     0000 0000 0000 0010
            //1F 0082 FE FFFFFFFE 01 一月 七月 0000 0000 1000 0010
            baseTimeInfo.clear();
            baseTimeInfoDay.clear();
            baseTimeInfoWeek.clear();
            baseTimeInfoMonth.clear();
            mTimeBaseNum = ByteUtils.bytesUInt(bytes[3]);
            for (int i = 0; i < mTimeBaseNum; i++) {
                baseTimeInfo.add(new byte[]{bytes[4 + i * 9], bytes[5 + i * 9], bytes[6 + i * 9], bytes[7 + i * 9], bytes[8 + i * 9], bytes[9 + i * 9], bytes[10 + i * 9], bytes[11 + i * 9], bytes[12 + i * 9]});
            }
            for (int i = 0; i < baseTimeInfo.size(); i++) {
                int baseTimeIndex = ByteUtils.bytesUInt(baseTimeInfo.get(i)[0]);
                if (baseTimeIndex >= 1 && baseTimeIndex <= 20) {
                    baseTimeInfoDay.add(baseTimeInfo.get(i));
                } else if (baseTimeIndex >= 21 && baseTimeIndex <= 30) {
                    baseTimeInfoWeek.add(baseTimeInfo.get(i));
                } else if (baseTimeIndex >= 31 && baseTimeIndex <= 40) {
                    baseTimeInfoMonth.add(baseTimeInfo.get(i));
                }
            }
            shiJiInfoList.clear();
            for (int i = 0; i < baseTimeInfoMonth.size(); i++) {
                byte[] bytes1 = baseTimeInfoMonth.get(i);
                String s1 = ByteUtils.getBinaryStrFromByteArr(new byte[]{bytes1[1], bytes1[2]});
                int[] ints = ByteUtils.byteArrToIntArr(s1);
                ArrayUtils.reverse(ints);
                int diaoDuHao = ByteUtils.bytesUInt(bytes1[8]);
                shiJiInfoList.add(new ShiJiInfo(ByteUtils.bytesUInt(bytes1[0]), ints, diaoDuHao));
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    checkShiJiInfo(currentIndex);
                }
            });
        }
    }

    /**
     * 获取时段表
     */
    class ShiDuanRunnable implements Runnable {

        @Override
        public void run() {
            try {
                UdpClientSocket mUdpClientSocket02 = new UdpClientSocket();
                mUdpClientSocket02.send(mIp, GbtDefine.GBT_PORT, GbtDefine.GET_SCHEDULE);
                byte[] bytes = mUdpClientSocket02.receiveByte(mIp, GbtDefine.GBT_PORT);
                scheduleData(bytes);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--run--异常");
            }
        }
    }

    private void scheduleData(byte[] bytes) {
        String s = ByteUtils.bytesToHexString(bytes);
        Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--run--时段表返回" + s);
        if ((ByteUtils.bytesUInt(bytes[1]) == 142 && ByteUtils.bytesUInt(bytes[0]) == 132) || (ByteUtils.bytesUInt(bytes[1]) == 142 && ByteUtils.bytesUInt(bytes[0]) == 129)) {
            for (int j = 0; j < shiduanByteListList.size(); j++) {
                shiduanByteListList.get(j).clear();
                for (int i = 0; i < 48; i++) {
                    shiduanByteListList.get(j).add(getByteArr(i, j, bytes));
                }
            }
            isShiDuanExist();
        }
    }
}