package com.aiton.zjb.signal.fragment.timebasefragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.aiton.zjb.signal.model.TeShuShiJiInfo;
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
 * A simple {@link Fragment} subclass.
 */
public class DayBaseTimeFragment extends ZjbBaseFragment implements View.OnClickListener {

    private View mInflate;
    private String mIp;
    private TextView mText_baseTimeIndex;
    private TextView mText_shiDuan;
    private String LISTDIALOGISSHOW = "listDialogIsShow";
    private String WhatIsShow = "";
    private AlertDialog mListDialog;
    private List<Integer> shiJiList = new ArrayList<>();
    private List<Integer> shiDuanList = new ArrayList<>();
    private List<TeShuShiJiInfo> shiJiInfoList = new ArrayList<>();
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
            R.id.checkBox12,
            R.id.checkBox13,
            R.id.checkBox14,
            R.id.checkBox15,
            R.id.checkBox16,
            R.id.checkBox17,
            R.id.checkBox18,
            R.id.checkBox19,
            R.id.checkBox20,
            R.id.checkBox21,
            R.id.checkBox22,
            R.id.checkBox23,
            R.id.checkBox24,
            R.id.checkBox25,
            R.id.checkBox26,
            R.id.checkBox27,
            R.id.checkBox28,
            R.id.checkBox29,
            R.id.checkBox30,
            R.id.checkBox31,
            R.id.checkBox32,
            R.id.checkBox33,
            R.id.checkBox34,
            R.id.checkBox35,
            R.id.checkBox36,
            R.id.checkBox37,
            R.id.checkBox38,
            R.id.checkBox39,
            R.id.checkBox40,
            R.id.checkBox41,
            R.id.checkBox42,
            R.id.checkBox43,
    };
    private CheckBox[] mCheckBoxes = new CheckBox[43];
    private View mLinear_peizhi;
    private View mRela_peizhi;
    private int currentIndex = 1;
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
    private CheckBox mCheckBox_quanxuan;
    private NodeListEntity mNodeListEntity;
    private boolean isOnline = false;
    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;
    private GroupListEntity mGroupListEntity;
    private boolean isGroupConfigure = false;

    public DayBaseTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_day_base_time, container, false);
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
        for (int i = 0; i < 20; i++) {
            shiJiList.add(1 + i);
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
        mCheckBox_quanxuan = (CheckBox) mInflate.findViewById(R.id.checkBox_quanxuan);
        mCheckBox_quanxuan.setOnCheckedChangeListener(new MyCheckedChangeListener());
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

    private void timeBaseData(byte[] bytes) {
        String s = ByteUtils.bytesToHexString(bytes);
        Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--run--时基表返回" + s);
        //84 8D 00 06 011FFEFEFFFFFFFE01 020202FE0000020201 030000FE0000000001 151FFE02FFFFFFFE01 160000000000000001 1F0002FEFFFFFFFE01
        //84消息类型 8D对象表示（时基表） 00 06对象数 011FFEFEFFFFFFFE01对象 020202FE0000020201 030000FE0000000001 151FFE02FFFFFFFE01 160000000000000001 1F0002FEFFFFFFFE01
        if ((ByteUtils.bytesUInt(bytes[1]) == 141 && ByteUtils.bytesUInt(bytes[0]) == 132) || (ByteUtils.bytesUInt(bytes[1]) == 141 && ByteUtils.bytesUInt(bytes[0]) == 129)) {
            //01调度计划号 1FFE调度月 FE调度周 FFFFFFFE调度日 01时段表号
            //1F 0002 FE FFFFFFFE 01 一月     0000 0000 0000 0010
            //1F 0082 FE FFFFFFFE 01 一月 七月 0000 0000 1000 0010
            //01 040A FE 0000000E 01
            baseTimeInfo.clear();
            baseTimeInfoDay.clear();
            baseTimeInfoWeek.clear();
            baseTimeInfoDay.clear();
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
            initUI();
        }
    }

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

    public void initUI() {
        shiJiInfoList.clear();
        for (int i = 0; i < baseTimeInfoDay.size(); i++) {
            byte[] bytes = baseTimeInfoDay.get(i);
            //字节数组转二进制字符串
            String s = ByteUtils.getBinaryStrFromByteArr(new byte[]{bytes[1], bytes[2]});
            String s1 = ByteUtils.getBinaryStrFromByteArr(new byte[]{bytes[4], bytes[5], bytes[6], bytes[7]});
            //二进制字符串转成1,0数组
            int[] ints = ByteUtils.byteArrToIntArr(s);
            int[] ints1 = ByteUtils.byteArrToIntArr(s1);
            //翻转
            ArrayUtils.reverse(ints);
            ArrayUtils.reverse(ints1);
            int diaoDuHao = ByteUtils.bytesUInt(bytes[8]);
            shiJiInfoList.add(new TeShuShiJiInfo(ByteUtils.bytesUInt(bytes[0]), ints1, ints, diaoDuHao));
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkShiJiInfo(currentIndex);
            }
        });
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
                int[] MonthInfoArr = shiJiInfoList.get(i).getMonthInfoArr();
                for (int j = 1; j < 13; j++) {
                    if (MonthInfoArr[j] == 1) {
                        mCheckBoxes[j - 1].setChecked(true);
                    }
                }
                int[] dayInfoArr = shiJiInfoList.get(i).getDayInfoArr();
                for (int j = 0; j < dayInfoArr.length; j++) {
                    if (dayInfoArr[j] == 1) {
                        mCheckBoxes[12 + j - 1].setChecked(true);
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
                boolean checkBoxIsCheckEd = checkBoxIsCheckEd();
                boolean checkBoxDayIsCheckEd = checkBoxDayIsCheckEd();
                if (checkBoxIsCheckEd) {
                    if (checkBoxDayIsCheckEd) {
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
                                        new Thread(new setDayBaseTimeRunnable()).start();
                                    }
                                }
                            })
                                    .setNegativeButton("否", null)
                                    .create()
                                    .show();
                        }
                    } else {
                        showTipDialog("请配置日期");
                    }
                } else {
                    showTipDialog("请配置月份");
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
        //84 8D 00 06 011FFEFEFFFFFFFE01 020202FE0000020201 030000FE0000000001 151FFE02FFFFFFFE01 160000000000000001 1F0002FEFFFFFFFE01
        //84消息类型 8D对象表示（时基表） 00 06对象数 011FFEFEFFFFFFFE01对象 020202FE0000020201 030000FE0000000001 151FFE02FFFFFFFE01 160000000000000001 1F0002FEFFFFFFFE01
        //01调度计划号 1FFE调度月 FE调度周 FFFFFFFE调度日 01时段表号
        //1F 0002 FE FFFFFFFE 01 一月     0000 0000 0000 0010
        //1F 0082 FE FFFFFFFE 01 一月 七月 0000 0000 1000 0010
        String baseTimeStr = "";
        byte[] monthCheckByteArr = getMonthCheckByteArr();
        byte[] dayCheckByteArr = getDayCheckByteArr();
        byte[] bytes3 = ByteUtils.byteMerger(monthCheckByteArr, dayCheckByteArr);
        byte[] bytes1 = ByteUtils.byteMerger(new byte[]{(byte) currentIndex}, bytes3);
        byte[] bytesShiJiInfo = ByteUtils.byteMerger(bytes1, new byte[]{(byte) currentShiDuan});
        if (mHasShiJiInfo) {
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
                        if (noAction.getObject() == null) {
                            Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                            getTimeBase();
                        } else {
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
     * 将月份的选择情况转化为符合要求的byte[]
     *
     * @return
     */
    private byte[] getMonthCheckByteArr() {
        String strMonthCheck = "";
        String strMonthCheck01 = "0";
        String strMonthCheck02 = "";
        for (int i = 0; i < mCheckBoxes.length - 31; i++) {
            if (i >= 0 || i < 7) {
                if (mCheckBoxes[i].isChecked()) {
                    strMonthCheck01 = strMonthCheck01 + 1;
                } else {
                    strMonthCheck01 = strMonthCheck01 + 0;
                }
            } else {
                if (mCheckBoxes[i].isChecked()) {
                    strMonthCheck02 = strMonthCheck01 + 1;
                } else {
                    strMonthCheck02 = strMonthCheck01 + 0;
                }
            }

        }
        //翻转字符串
        strMonthCheck01 = new StringBuilder(strMonthCheck01).reverse().toString();
        strMonthCheck02 = new StringBuilder(strMonthCheck02 + "000").reverse().toString();
        strMonthCheck = strMonthCheck02 + strMonthCheck01;
        Log.e("DayBaseTimeFragment", "DayBaseTimeFragment--getMonthCheckByteArr--日时基月拼接" + strMonthCheck);
        byte b1 = ByteUtils.bit2byte(strMonthCheck.substring(0, 8));
        byte b2 = ByteUtils.bit2byte(strMonthCheck.substring(8, 16));
        byte[] bytesMonthCheck = new byte[]{b1, b2};
        return bytesMonthCheck;
    }

    /**
     * 将日期的选择情况转化为符合要求的byte[]
     *
     * @return
     */
    private byte[] getDayCheckByteArr() {
        String strMonthCheck = "";
        String strMonthCheck01 = "0";
        String strMonthCheck02 = "";
        String strMonthCheck03 = "";
        String strMonthCheck04 = "";
        for (int i = 0; i < mCheckBoxes.length - 12; i++) {
            if (i >= 0 && i < 7) {
                if (mCheckBoxes[i + 12].isChecked()) {
                    strMonthCheck01 = strMonthCheck01 + 1;
                } else {
                    strMonthCheck01 = strMonthCheck01 + 0;
                }
            } else if (i >= 7 && i < 15) {
                if (mCheckBoxes[i + 12].isChecked()) {
                    strMonthCheck02 = strMonthCheck02 + 1;
                } else {
                    strMonthCheck02 = strMonthCheck02 + 0;
                }
            } else if (i >= 15 && i < 23) {
                if (mCheckBoxes[i + 12].isChecked()) {
                    strMonthCheck03 = strMonthCheck03 + 1;
                } else {
                    strMonthCheck03 = strMonthCheck03 + 0;
                }
            } else if (i >= 23 && i < 31) {
                if (mCheckBoxes[i + 12].isChecked()) {
                    strMonthCheck04 = strMonthCheck04 + 1;
                } else {
                    strMonthCheck04 = strMonthCheck04 + 0;
                }
            }
        }
        //翻转字符串
        strMonthCheck01 = new StringBuilder(strMonthCheck01).reverse().toString();
        strMonthCheck02 = new StringBuilder(strMonthCheck02).reverse().toString();
        strMonthCheck03 = new StringBuilder(strMonthCheck03).reverse().toString();
        strMonthCheck04 = new StringBuilder(strMonthCheck04).reverse().toString();
        strMonthCheck = strMonthCheck04 + strMonthCheck03 + strMonthCheck02 + strMonthCheck01;
        Log.e("DayBaseTimeFragment", "DayBaseTimeFragment--getDayCheckByteArr--日时基日期拼接" + strMonthCheck);
        byte b1 = ByteUtils.bit2byte(strMonthCheck.substring(0, 8));
        byte b2 = ByteUtils.bit2byte(strMonthCheck.substring(8, 16));
        byte b3 = ByteUtils.bit2byte(strMonthCheck.substring(16, 24));
        byte b4 = ByteUtils.bit2byte(strMonthCheck.substring(24, 32));
        byte[] bytesMonthCheck = new byte[]{(byte) 0xFE, b1, b2, b3, b4};
        Log.e("DayBaseTimeFragment", "DayBaseTimeFragment--getDayCheckByteArr--日时基拼接转换" + ByteUtils.bytesToHexString(bytesMonthCheck));
        return bytesMonthCheck;
    }

    /**
     * 检查月份是否有选择
     *
     * @return
     */
    private boolean checkBoxIsCheckEd() {
        for (int i = 0; i < mCheckBoxes.length - 31; i++) {
            if (mCheckBoxes[i].isChecked()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查日期是否有选择
     *
     * @return
     */
    private boolean checkBoxDayIsCheckEd() {
        for (int i = 0; i < mCheckBoxes.length - 12; i++) {
            if (mCheckBoxes[i + 12].isChecked()) {
                return true;
            }
        }
        return false;
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
     * 列表dialog适配器
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
     * 手控自控监听
     */
    class MyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            for (int i = 0; i < mCheckBoxes.length - 12; i++) {
                mCheckBoxes[i + 12].setChecked(b);
            }
        }
    }

    /**
     * 设置特殊时基
     */
    class setDayBaseTimeRunnable implements Runnable {

        @Override
        public void run() {
            try {
                //84 8D 00 06 011FFEFEFFFFFFFE01 020202FE0000020201 030000FE0000000001 151FFE02FFFFFFFE01 160000000000000001 1F0002FEFFFFFFFE01
                //84消息类型 8D对象表示（时基表） 00 06对象数 011FFEFEFFFFFFFE01对象 020202FE0000020201 030000FE0000000001 151FFE02FFFFFFFE01 160000000000000001 1F0002FEFFFFFFFE01
                //01调度计划号 1FFE调度月 FE调度周 FFFFFFFE调度日 01时段表号
                //1F 0002 FE FFFFFFFE 01 一月     0000 0000 0000 0010
                //1F 0082 FE FFFFFFFE 01 一月 七月 0000 0000 1000 0010
                byte[] monthCheckByteArr = getMonthCheckByteArr();
                byte[] dayCheckByteArr = getDayCheckByteArr();
                byte[] bytes3 = ByteUtils.byteMerger(monthCheckByteArr, dayCheckByteArr);
                byte[] bytes1 = ByteUtils.byteMerger(new byte[]{(byte) currentIndex}, bytes3);
                byte[] bytesShiJiInfo = ByteUtils.byteMerger(bytes1, new byte[]{(byte) currentShiDuan});
                if (mHasShiJiInfo) {
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
                    UdpClientSocket udpClientSocket = new UdpClientSocket();
                    udpClientSocket.send(mIp, GbtDefine.GBT_PORT, ByteUtils.byteMerger(GbtDefine.SET_PLAN_RESPONSE, sendSetShiJi));
                    byte[] bytes = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                    Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--run--配置时基返回值" + ByteUtils.bytesToHexString(bytes));
                    //818D0006011FFEFEFEFFFFFF01020202FE0202000001030000FE0000000001151FFE02FEFFFFFF011600000000000000011F0082FEFEFFFFFF01
                    setData(bytes);
                } else {
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
                    setData(bytes);
                }
                //重新更新时基显示
                new Thread(new ShiJiRunnable()).start();
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
                UdpClientSocket mUdpClientSocket = new UdpClientSocket();
                mUdpClientSocket.send(mIp, GbtDefine.GBT_PORT, GbtDefine.GET_PLAN);
                byte[] bytes = mUdpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                timeBaseData(bytes);
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
                scheduleData(bytes);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--run--异常");
            }
        }
    }

    /**
     * 列表dialog itemClick
     */
    class MyItemClick implements AdapterView.OnItemClickListener {

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
}
