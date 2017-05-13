package com.aiton.zjb.signal.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.aiton.zjb.signal.model.UserInfo;
import com.aiton.zjb.signal.util.ByteUtils;
import com.aiton.zjb.signal.util.UdpClientSocket;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class JieDuanFragment extends ZjbBaseFragment implements View.OnClickListener {


    private View mInflate;
    private String mIp;
    private List<List<byte[]>> jieDuanByteArrList = new ArrayList<>();
    private List<byte[]> jieDuanList01 = new ArrayList<>();
    private List<byte[]> jieDuanList02 = new ArrayList<>();
    private List<byte[]> jieDuanList03 = new ArrayList<>();
    private List<byte[]> jieDuanList04 = new ArrayList<>();
    private List<byte[]> jieDuanList05 = new ArrayList<>();
    private List<byte[]> jieDuanList06 = new ArrayList<>();
    private List<byte[]> jieDuanList07 = new ArrayList<>();
    private List<byte[]> jieDuanList08 = new ArrayList<>();
    private List<byte[]> jieDuanList09 = new ArrayList<>();
    private List<byte[]> jieDuanList10 = new ArrayList<>();
    private List<byte[]> jieDuanList11 = new ArrayList<>();
    private List<byte[]> jieDuanList12 = new ArrayList<>();
    private List<byte[]> jieDuanList13 = new ArrayList<>();
    private List<byte[]> jieDuanList14 = new ArrayList<>();
    private List<byte[]> jieDuanList15 = new ArrayList<>();
    private List<byte[]> jieDuanList16 = new ArrayList<>();
    private List<Integer> jieDuanHaoUsed = new ArrayList<>();
    private int[] lightID = new int[]{
            R.id.img_North00,
            R.id.img_North01,
            R.id.img_North02,
            R.id.img_North04,
            R.id.img_East00,
            R.id.img_East01,
            R.id.img_East02,
            R.id.img_East04,
            R.id.img_South00,
            R.id.img_South01,
            R.id.img_South02,
            R.id.img_South04,
            R.id.img_West00,
            R.id.img_West01,
            R.id.img_West02,
            R.id.img_West04,

            R.id.img_North05,
            R.id.img_East05,
            R.id.img_South05,
            R.id.img_West05,

            R.id.img_North06,
            R.id.img_East06,
            R.id.img_South06,
            R.id.img_West06,

            R.id.img_North03,
            R.id.img_East03,
            R.id.img_South03,
            R.id.img_West03,

            R.id.img_North07,
            R.id.img_East07,
            R.id.img_South07,
            R.id.img_West07
    };
    private ImageView[] lightImg = new ImageView[32];
    private TextView mTv_countdown;
    private int currentIndex = 1;
    private int CurrentLuKou = 1;
    private TextView mTextViewGreen;
    private TextView mTextViewYellow;
    private TextView mTextViewRed;
    private DiscreteSeekBar mSeekBarGreen;
    private DiscreteSeekBar mSeekBarYellow;
    private DiscreteSeekBar mSeekBarRed;
    private TextView mTvJieDuanPeiShiHao;
    private TextView mTvLuKouJiZhunTu;
    private String WhatSeekBarIsShow;
    private int luKouJiZhunTuLength;
    private int[] mCurrentLightInts;
    private CheckBox mCheckBoxGanYing;
    private NodeListEntity mNodeListEntity;
    private boolean isOnline = false;
    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;
    private GroupListEntity mGroupListEntity;
    private boolean isGroupConfigure = false;

    public JieDuanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_fragment06, container, false);
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
        jieDuanByteArrList.add(jieDuanList01);
        jieDuanByteArrList.add(jieDuanList02);
        jieDuanByteArrList.add(jieDuanList03);
        jieDuanByteArrList.add(jieDuanList04);
        jieDuanByteArrList.add(jieDuanList05);
        jieDuanByteArrList.add(jieDuanList06);
        jieDuanByteArrList.add(jieDuanList07);
        jieDuanByteArrList.add(jieDuanList08);
        jieDuanByteArrList.add(jieDuanList09);
        jieDuanByteArrList.add(jieDuanList10);
        jieDuanByteArrList.add(jieDuanList11);
        jieDuanByteArrList.add(jieDuanList12);
        jieDuanByteArrList.add(jieDuanList13);
        jieDuanByteArrList.add(jieDuanList14);
        jieDuanByteArrList.add(jieDuanList15);
        jieDuanByteArrList.add(jieDuanList16);
    }

    @Override
    protected void findID() {
        for (int i = 0; i < lightID.length; i++) {
            lightImg[i] = (ImageView) mInflate.findViewById(lightID[i]);
        }
        mTv_countdown = (TextView) mInflate.findViewById(R.id.tv_countdown);
        mTextViewGreen = (TextView) mInflate.findViewById(R.id.textViewGreen);
        mTextViewYellow = (TextView) mInflate.findViewById(R.id.textViewYellow);
        mTextViewRed = (TextView) mInflate.findViewById(R.id.textViewRed);
        mSeekBarGreen = (DiscreteSeekBar) mInflate.findViewById(R.id.seekBarGreen);
        mSeekBarYellow = (DiscreteSeekBar) mInflate.findViewById(R.id.seekBarYellow);
        mSeekBarRed = (DiscreteSeekBar) mInflate.findViewById(R.id.seekBarRed);
        mTvJieDuanPeiShiHao = (TextView) mInflate.findViewById(R.id.tvJieDuanPeiShiHao);
        mTvLuKouJiZhunTu = (TextView) mInflate.findViewById(R.id.tvLuKouJiZhunTu);
        mCheckBoxGanYing = (CheckBox) mInflate.findViewById(R.id.checkBoxGanYing);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {
        mTvJieDuanPeiShiHao.setOnClickListener(this);
        mTvLuKouJiZhunTu.setOnClickListener(this);
        mSeekBarGreen.setOnProgressChangeListener(new MyProgressChangeListener());
        mSeekBarYellow.setOnProgressChangeListener(new MyProgressChangeListener());
        mSeekBarRed.setOnProgressChangeListener(new MyProgressChangeListener());
        mInflate.findViewById(R.id.northRenXing).setOnClickListener(this);
        mInflate.findViewById(R.id.northRenXing2).setOnClickListener(this);
        mInflate.findViewById(R.id.eastRenXing).setOnClickListener(this);
        mInflate.findViewById(R.id.eastRenXing2).setOnClickListener(this);
        mInflate.findViewById(R.id.southRenXing).setOnClickListener(this);
        mInflate.findViewById(R.id.southRenXing2).setOnClickListener(this);
        mInflate.findViewById(R.id.westRenXing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changLight(15);
            }
        });
        mInflate.findViewById(R.id.westRenXing2).setOnClickListener(this);
        lightImg[20].setOnClickListener(this);
        lightImg[21].setOnClickListener(this);
        lightImg[22].setOnClickListener(this);
        lightImg[23].setOnClickListener(this);
        lightImg[28].setOnClickListener(this);
        lightImg[29].setOnClickListener(this);
        lightImg[30].setOnClickListener(this);
        lightImg[31].setOnClickListener(this);
        mInflate.findViewById(R.id.relaNorth).setOnClickListener(this);
        mInflate.findViewById(R.id.relaEast).setOnClickListener(this);
        mInflate.findViewById(R.id.relaSouth).setOnClickListener(this);
        mInflate.findViewById(R.id.relaWest).setOnClickListener(this);
        mInflate.findViewById(R.id.buttonComfirm).setOnClickListener(this);
        mInflate.findViewById(R.id.imageViewGreenAdd).setOnClickListener(this);
        mInflate.findViewById(R.id.imageViewGreenDelete).setOnClickListener(this);
        mInflate.findViewById(R.id.imageViewYellowAdd).setOnClickListener(this);
        mInflate.findViewById(R.id.imageViewYellowDelete).setOnClickListener(this);
        mInflate.findViewById(R.id.imageViewRedAdd).setOnClickListener(this);
        mInflate.findViewById(R.id.imageViewRedDelete).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isOnline || isGroupConfigure) {
            showLoadingDialog("获取数据");
            getStagePattern();
        } else {
            new Thread(new JieDuanRunNable()).start();
        }
    }

    private void getStagePattern() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        String url = "";
        if (isOnline) {
            params.put("nodeId", mNodeListEntity.getId());
            url = Constant.Url.GET_STAGE_PATTERN;
        }
        if (isGroupConfigure) {
            params.put("groupId", mGroupListEntity.getId());
            url = Constant.Url.GET_GROUP_STAGE_PATTERN_BY_GROUPID;
        }
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                cancelLoadingDialog();
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        String object = (String) noAction.getObject();
                        final byte[] bytes = ByteUtils.hexStringToByte(object);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                stagePattern(bytes);
                            }
                        }).start();
                    } else {
                        if (noAction.getMessageCode() == 3) {
                            reLogin();
                        }else{
                            //初始化数据
                            String receive = "84C1001010" +
                                    "01000000000000000000" +
                                    "01000000000000000000" +
                                    "01000000000000000000" +
                                    "01000000000000000000" +
                                    "01000000000000000000" +
                                    "01000000000000000000" +
                                    "01000000000000000000" +
                                    "01000000000000000000" +
                                    "01000000000000000000" +
                                    "01000000000000000000" +
                                    "01000000000000000000" +
                                    "01000000000000000000" +
                                    "01000000000000000000" +
                                    "01000000000000000000" +
                                    "01000000000000000000" +
                                    "01000000000000000000" +
                                    "02000000000000000000" +
                                    "02000000000000000000" +
                                    "02000000000000000000" +
                                    "02000000000000000000" +
                                    "02000000000000000000" +
                                    "02000000000000000000" +
                                    "02000000000000000000" +
                                    "02000000000000000000" +
                                    "02000000000000000000" +
                                    "02000000000000000000020000000000000000000200000000000000000002000000000000000000020000000000000000000200000000000000000002000000000000000000" +
                                    "03000000000000000000" +
                                    "03000000000000000000" +
                                    "03000000000000000000" +
                                    "03000000000000000000" +
                                    "03000000000000000000" +
                                    "03000000000000000000030000000000000000000300000000000000000003000000000000000000030000000000000000000300000000000000000003000000000000000000030000000000000000000300000000000000000003000000000000000000030000000000000000000400000000000000000004000000000000000000040000000000000000000400000000000000000004000000000000000000040000000000000000000400000000000000000004000000000000000000040000000000000000000400000000000000000004000000000000000000040000000000000000000400000000000000000004000000000000000000040000000000000000000400000000000000000005000000000000000000050000000000000000000500000000000000000005000000000000000000050000000000000000000500000000000000000005000000000000000000050000000000000000000500000000000000000005000000000000000000050000000000000000000500000000000000000005000000000000000000050000000000000000000500000000000000000005000000000000000000060000000000000000000600000000000000000006000000000000000000060000000000000000000600000000000000000006000000000000000000060000000000000000000600000000000000000006000000000000000000060000000000000000000600000000000000000006000000000000000000060000000000000000000600000000000000000006000000000000000000060000000000000000000700000000000000000007000000000000000000070000000000000000000700000000"
                                    +"000000000007000000000000000000070000000000000000000700000000000000000007000000000000000000070000000000000000000700000000000000000007000000000000000000070000000000000000000700000000000000000007000000000000000000070000000000000000000700000000000000000008000000000000000000080000000000000000000800000000000000000008000000000000000000080000000000000000000800000000000000000008000000000000000000080000000000000000000800000000000000000008000000000000000000080000000000000000000800000000000000000008000000000000000000080000000000000000000800000000000000000008000000000000000000090000000000000000000900000000000000000009000000000000000000090000000000000000000900000000000000000009000000000000000000090000000000000000000900000000000000000009000000000000000000090000000000000000000900000000000000000009000000000000000000090000000000000000000900000000000000000009000000000000000000090000000000000000000A0000000000000000000A0000000000000000000A0000000000000000000A0000000000000000000A0000000000000000000A0000000000000000000A0000000000000000000A0000000000000000000A0000000000000000000A0000000000000000000A0000000000000000000A0000000000000000000A0000000000000000000A0000000000000000000A0000000000000000000A0000000000000000000B0000000000000000000B0000000000000000000B0000000000000000000B0000000000000000000B0000000000000000000B0000000000000000000B0000000000000000000B0000000000000000000B0000000000000000000B0000000000000000000B0000000000000000000B0000000000000000000B0000000000000000000B0000000000000000000B0000000000000000000B0000000000000000000C0000000000000000000C0000000000000000000C0000000000000000000C0000000000000000000C0000000000000000000C0000000000000000000C0000000000000000000C0000000000000000000C0000000000000000000C0000000000000000000C0000000000000000000C0000000000000000000C0000000000000000000C0000000000000000000C0000000000000000000C0000000000000000000D0000000000000000000D0000000000000000000D0000000000000000000D0000000000000000000D0000000000000000000D0000000000000000000D0000000000000000000D00000000"
                                    +"00000000000D0000000000000000000D0000000000000000000D0000000000000000000D0000000000000000000D0000000000000000000D0000000000000000000D0000000000000000000D0000000000000000000E0000000000000000000E0000000000000000000E0000000000000000000E0000000000000000000E0000000000000000000E0000000000000000000E0000000000000000000E0000000000000000000E0000000000000000000E0000000000000000000E0000000000000000000E0000000000000000000E0000000000000000000E0000000000000000000E0000000000000000000E000000000000000000" +
                                    "0F000000000000000000" +
                                    "0F000000000000000000" +
                                    "0F000000000000000000" +
                                    "0F000000000000000000" +
                                    "0F000000000000000000" +
                                    "0F000000000000000000" +
                                    "0F000000000000000000" +
                                    "0F000000000000000000" +
                                    "0F000000000000000000" +
                                    "0F0000000000000000000F0000000000000000000F0000000000000000000F0000000000000000000F0000000000000000000F000000000000000000" +
                                    "0F000000000000000000" +
                                    "10000000000000000000" +
                                    "10000000000000000000" +
                                    "10000000000000000000" +
                                    "10000000000000000000" +
                                    "100000000000000000001000000000000000000010000000000000000000100000000000000000001000000000000000000010000000000000000000100000000000000000001000000000000000000010000000000000000000100000000000000000001000000000000000000010000000000000000000";
                            Log.e("JieDuanFragment", "JieDuanFragment--onSuccess--阶段配时长度"+receive.length());
                            stagePattern(ByteUtils.hexStringToByte(receive));
                        }
                    }
                    Toast.makeText(getActivity(),noAction.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    Toast.makeText(getActivity(), "获取阶段配时表失败", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        });
    }

    private void stagePattern(byte[] bytes) {
        if ((ByteUtils.bytesUInt(bytes[0]) == 132 && ByteUtils.bytesUInt(bytes[1]) == 193)||(ByteUtils.bytesUInt(bytes[0]) == 129 && ByteUtils.bytesUInt(bytes[1]) == 193)) {
            //84C1001010
            // 01阶段方案号 01阶段号（配时号） 0000 8西（1000西人行） 0南 0东 3北（0011北左北直） 0F绿灯（15秒） 03黄灯（3秒） 00红灯 01阶段感应参数
            // 0102000000380F030001
            // 0103000003800F030001
            // 0104000038000F030001
            // 01000000000000000000
            // 0100000000000000000001000000000000000000
            for (int i = 0; i < 16; i++) {
                List<byte[]> bytes12 = jieDuanByteArrList.get(i);
                bytes12.clear();
                for (int j = 0; j < 16; j++) {
                    bytes12.add(new byte[]{bytes[5 + 16 * 10 * i + 10 * j], bytes[6 + 16 * 10 * i + 10 * j], bytes[7 + 16 * 10 * i + 10 * j], bytes[8 + 16 * 10 * i + 10 * j], bytes[9 + 16 * 10 * i + 10 * j], bytes[10 + 16 * 10 * i + 10 * j], bytes[11 + 16 * 10 * i + 10 * j], bytes[12 + 16 * 10 * i + 10 * j], bytes[13 + 16 * 10 * i + 10 * j], bytes[14 + 16 * 10 * i + 10 * j]});
                }
            }
            jieDuanHaoUsed.clear();
            for (int i = 0; i < jieDuanByteArrList.size(); i++) {
                List<byte[]> bytes12 = jieDuanByteArrList.get(i);
                for (int j = 0; j < bytes12.size(); j++) {
                    if (ByteUtils.bytesUInt(bytes12.get(j)[1]) != 0) {
                        jieDuanHaoUsed.add(i + 1);
                        break;
                    }
                }
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreashUI();
                }
            });
        }
    }

    class MyProgressChangeListener implements DiscreteSeekBar.OnProgressChangeListener {

        @Override
        public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
            switch (seekBar.getId()) {
                case R.id.seekBarGreen:
                    mTextViewGreen.setText("绿：" + value);
                    break;
                case R.id.seekBarYellow:
                    mTextViewYellow.setText("黄：" + value);
                    break;
                case R.id.seekBarRed:
                    mTextViewRed.setText("红：" + value);
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewGreenAdd:
                int progress = mSeekBarGreen.getProgress();
                if (progress < 255) {
                    mSeekBarGreen.setProgress(progress + 1);
                }
                break;
            case R.id.imageViewGreenDelete:
                int progress1 = mSeekBarGreen.getProgress();
                if (progress1 > 0) {
                    mSeekBarGreen.setProgress(progress1 - 1);
                }
                break;
            case R.id.imageViewYellowAdd:
                int progress2 = mSeekBarYellow.getProgress();
                if (progress2 < 255) {
                    mSeekBarYellow.setProgress(progress2 + 1);
                }
                break;
            case R.id.imageViewYellowDelete:
                int progress3 = mSeekBarYellow.getProgress();
                if (progress3 > 0) {
                    mSeekBarYellow.setProgress(progress3 - 1);
                }
                break;
            case R.id.imageViewRedAdd:
                int progress4 = mSeekBarRed.getProgress();
                if (progress4 < 255) {
                    mSeekBarRed.setProgress(progress4 + 1);
                }
                break;
            case R.id.imageViewRedDelete:
                int progress5 = mSeekBarRed.getProgress();
                if (progress5 > 0) {
                    mSeekBarRed.setProgress(progress5 - 1);
                }
                break;
            case R.id.tvJieDuanPeiShiHao:
                WhatSeekBarIsShow = "阶段配时号";
                showSeekBarDialog(1, 16, currentIndex);
                break;
            case R.id.tvLuKouJiZhunTu:
                WhatSeekBarIsShow = "路口基准图";
                if (luKouJiZhunTuLength < 16) {
                    showSeekBarDialog(1, luKouJiZhunTuLength + 1, CurrentLuKou);
                } else {
                    showSeekBarDialog(1, luKouJiZhunTuLength, CurrentLuKou);
                }
                break;
            case R.id.northRenXing:
                changLight(3);
                break;
            case R.id.eastRenXing:
                changLight(7);
                break;
            case R.id.southRenXing:
                changLight(11);
                break;
            case R.id.northRenXing2:
                changLight(16);
                break;
            case R.id.eastRenXing2:
                changLight(17);
                break;
            case R.id.southRenXing2:
                changLight(18);
                break;
            case R.id.westRenXing2:
                changLight(19);
                break;
            case R.id.img_North06:
                changLight(20);
                break;
            case R.id.img_North07:
                changLight(28);
                break;
            case R.id.img_East06:
                changLight(21);
                break;
            case R.id.img_East07:
                changLight(29);
                break;
            case R.id.img_South06:
                changLight(22);
                break;
            case R.id.img_South07:
                changLight(30);
                break;
            case R.id.img_West06:
                changLight(23);
                break;
            case R.id.img_West07:
                changLight(31);
                break;
            case R.id.relaNorth:
                showChangeLightDialog("北", 1);
                break;
            case R.id.relaEast:
                showChangeLightDialog("东", 2);
                break;
            case R.id.relaSouth:
                showChangeLightDialog("南", 3);
                break;
            case R.id.relaWest:
                showChangeLightDialog("西", 4);
                break;
            case R.id.buttonComfirm:
                new AlertDialog.Builder(getActivity())
                        .setMessage("是否更新到信号机")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (isOnline||isGroupConfigure) {
                                    setStagePattern();
                                } else {
                                    new Thread(new SetJieDuanRunnable()).start();
                                }
                            }
                        })
                        .setNegativeButton("否", null)
                        .create()
                        .show();
                break;
        }
    }

    private void setStagePattern() {
        showLoadingDialog("保存中");
        String send = "81C1001010";
        StringBuffer duiXiang = new StringBuffer();
        for (int i = 0; i < mCurrentLightInts.length; i++) {
            duiXiang = duiXiang.append(mCurrentLightInts[i]);
        }
        String duiXiangStr = duiXiang.toString();
        Log.e("SetJieDuanRunnable", "SetJieDuanRunnable--run--对象" + duiXiangStr);
        String s = new StringBuilder(duiXiangStr).reverse().toString();
        byte b1 = ByteUtils.bit2byte(s.substring(0, 8));
        byte b2 = ByteUtils.bit2byte(s.substring(8, 16));
        byte b3 = ByteUtils.bit2byte(s.substring(16, 24));
        byte b4 = ByteUtils.bit2byte(s.substring(24, 32));
        String duiXiangHex = ByteUtils.bytesToHexString(new byte[]{b1, b2, b3, b4}) + ByteUtils.bytesToHexString(new byte[]{(byte) mSeekBarGreen.getProgress(), (byte) mSeekBarYellow.getProgress(), (byte) mSeekBarRed.getProgress()});
        if (mCheckBoxGanYing.isChecked()) {
            duiXiangHex = duiXiangHex + "00";
        } else {
            duiXiangHex = duiXiangHex + "01";
        }
        duiXiangHex = ByteUtils.bytesToHexString(new byte[]{(byte) currentIndex, (byte) CurrentLuKou}) + duiXiangHex;
        Log.e("SetJieDuanRunnable", "SetJieDuanRunnable--run--阶段对象16进制" + duiXiangHex);
        for (int i = 0; i < jieDuanByteArrList.size(); i++) {
            List<byte[]> bytes = jieDuanByteArrList.get(i);
            for (int j = 0; j < bytes.size(); j++) {
                if (i == (currentIndex - 1) && j == (CurrentLuKou - 1)) {
                    send = send + duiXiangHex;
                } else {
                    send = send + ByteUtils.bytesToHexString(bytes.get(j));
                }
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("是",null);
        final AlertDialog alertDialog = builder.create();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("byteString", send);
        String url ="";
        if (isOnline){
            params.put("nodeId", mNodeListEntity.getId());
            url = Constant.Url.SET_STAGE_PATTERN;
        }
        if (isGroupConfigure){
            params.put("groupId", mGroupListEntity.getId());
            url = Constant.Url.SET_GROUP_STAGE_PATTERN;
        }
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--设置时基返回值" + s);
                try {
                    cancelLoadingDialog();
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        if (noAction.getObject()==null){
                            showLoadingDialog("");
                            getStagePattern();
                        }else{
                            String object = (String) noAction.getObject();
                            byte[] bytes = ByteUtils.hexStringToByte(object);
                            if (ByteUtils.bytesUInt(bytes[0]) == 133 && ByteUtils.bytesUInt(bytes[1]) == 193) {
                                Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                                showLoadingDialog("");
                                getStagePattern();
                            } else {
                                Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (noAction.getMessageCode() == 3) {
                            reLogin();
                        }else{
                            alertDialog.setMessage(noAction.getMessage());
                            alertDialog.show();
                        }
                    }
                    Toast.makeText(getActivity(), noAction.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    class SetJieDuanRunnable implements Runnable {

        @Override
        public void run() {
            try {
                //84C1001010
                // 01阶段方案号 01阶段号（配时号） 0000 8西（1000西人行） 0南 0东 3北（0011北左北直） 0F绿灯（15秒） 03黄灯（3秒） 00红灯 01阶段感应参数
                // 0102000000380F030001
                // 0103000003800F030001
                // 0104000038000F030001
                // 01000000000000000000
                UdpClientSocket udpClientSocket = new UdpClientSocket();
                String send = "81C1001010";
                StringBuffer duiXiang = new StringBuffer();
                for (int i = 0; i < mCurrentLightInts.length; i++) {
                    duiXiang = duiXiang.append(mCurrentLightInts[i]);
                }
                String duiXiangStr = duiXiang.toString();
                Log.e("SetJieDuanRunnable", "SetJieDuanRunnable--run--对象" + duiXiangStr);
                String s = new StringBuilder(duiXiangStr).reverse().toString();
                byte b1 = ByteUtils.bit2byte(s.substring(0, 8));
                byte b2 = ByteUtils.bit2byte(s.substring(8, 16));
                byte b3 = ByteUtils.bit2byte(s.substring(16, 24));
                byte b4 = ByteUtils.bit2byte(s.substring(24, 32));
                String duiXiangHex = ByteUtils.bytesToHexString(new byte[]{b1, b2, b3, b4}) + ByteUtils.bytesToHexString(new byte[]{(byte) mSeekBarGreen.getProgress(), (byte) mSeekBarYellow.getProgress(), (byte) mSeekBarRed.getProgress()});
                if (mCheckBoxGanYing.isChecked()) {
                    duiXiangHex = duiXiangHex + "00";
                } else {
                    duiXiangHex = duiXiangHex + "01";
                }
                duiXiangHex = ByteUtils.bytesToHexString(new byte[]{(byte) currentIndex, (byte) CurrentLuKou}) + duiXiangHex;
                Log.e("SetJieDuanRunnable", "SetJieDuanRunnable--run--阶段对象16进制" + duiXiangHex);
                for (int i = 0; i < jieDuanByteArrList.size(); i++) {
                    List<byte[]> bytes = jieDuanByteArrList.get(i);
                    for (int j = 0; j < bytes.size(); j++) {
                        if (i == (currentIndex - 1) && j == (CurrentLuKou - 1)) {
                            send = send + duiXiangHex;
                        } else {
                            send = send + ByteUtils.bytesToHexString(bytes.get(j));
                        }
                    }
                }
                LogUtil.LogShitou("阶段发送值", send);
                Log.e("SetJieDuanRunnable", "SetJieDuanRunnable--run--阶段发送值长度" + send.length());
                udpClientSocket.send(mIp, GbtDefine.GBT_PORT, ByteUtils.hexStringToByte(send));
                byte[] bytes = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                Log.e("SetJieDuanRunnable", "SetJieDuanRunnable--run--阶段配置返回" + ByteUtils.bytesToHexString(bytes));
                setData(bytes);
                new Thread(new JieDuanRunNable()).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showChangeLightDialog(String drec, final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.change_light_dialog, null);
        TextView textViewZuo = (TextView) inflate.findViewById(R.id.textViewZuo);
        TextView textViewZhi = (TextView) inflate.findViewById(R.id.textViewZhi);
        TextView textViewYou = (TextView) inflate.findViewById(R.id.textViewYou);
        TextView textViewTeShu = (TextView) inflate.findViewById(R.id.textViewTeShu);
        final ImageView imageViewZuo = (ImageView) inflate.findViewById(R.id.imageViewZuo);
        final ImageView imageViewZhi = (ImageView) inflate.findViewById(R.id.imageViewZhi);
        final ImageView imageViewYou = (ImageView) inflate.findViewById(R.id.imageViewYou);
        final ImageView imageViewTeShu = (ImageView) inflate.findViewById(R.id.imageViewTeShu);
        final AlertDialog alertDialog = builder
                .setView(inflate)
                .create();
        final int[] ints = new int[4];
        textViewZuo.setText(drec + "左");
        textViewZhi.setText(drec + "直");
        textViewYou.setText(drec + "右");
        textViewTeShu.setText(drec + "特殊");
        setDialogLight(mCurrentLightInts[(i - 1) * 4], imageViewZuo, ints, 0);
        setDialogLight(mCurrentLightInts[(i - 1) * 4 + 1], imageViewZhi, ints, 1);
        setDialogLight(mCurrentLightInts[(i - 1) * 4 + 2], imageViewYou, ints, 2);
        setDialogLight(mCurrentLightInts[4 * 6 + i - 1], imageViewTeShu, ints, 3);
        changeDialogLight(imageViewZuo, ints, 0);
        changeDialogLight(imageViewZhi, ints, 1);
        changeDialogLight(imageViewYou, ints, 2);
        changeDialogLight(imageViewTeShu, ints, 3);
        inflate.findViewById(R.id.buttonConfrim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentLightInts[(i - 1) * 4] = ints[0];
                mCurrentLightInts[(i - 1) * 4 + 1] = ints[1];
                mCurrentLightInts[(i - 1) * 4 + 2] = ints[2];
                mCurrentLightInts[4 * 6 + i - 1] = ints[3];
                setLight((i - 1) * 4);
                setLight((i - 1) * 4 + 1);
                setLight((i - 1) * 4 + 2);
                setLight(4 * 6 + i - 1);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void changeDialogLight(final ImageView imageViewZuo, final int[] ints, final int which) {
        imageViewZuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ints[which] == 1) {
                    ints[which] = 0;
                    imageViewZuo.setImageResource(R.mipmap.redone);
                } else {
                    ints[which] = 1;
                    imageViewZuo.setImageResource(R.mipmap.greenone);
                }
            }
        });
    }

    private void setDialogLight(int currentLightInt, ImageView imageViewZuo, int[] ints, int which) {
        if (currentLightInt == 1) {
            ints[which] = 1;
            imageViewZuo.setImageResource(R.mipmap.greenone);
        } else {
            ints[which] = 0;
            imageViewZuo.setImageResource(R.mipmap.redone);
        }
    }

    private void changLight(int i) {
        if (mCurrentLightInts[i] == 1) {
            mCurrentLightInts[i] = 0;
        } else {
            mCurrentLightInts[i] = 1;
        }
        setLight(i);
    }

    private void showSeekBarDialog(final int min, final int max, int currentValue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.seekbar_dialog, null);
        final AlertDialog alertDialog = builder.setView(inflate)
                .create();
        alertDialog.show();
        final TextView tv_ChooseXiangWeiCha = (TextView) inflate.findViewById(R.id.tv_ChooseXiangWeiCha);
        tv_ChooseXiangWeiCha.setText(WhatSeekBarIsShow + "：" + currentValue);
        final DiscreteSeekBar seekBar = (DiscreteSeekBar) inflate.findViewById(R.id.seekBar);
        seekBar.setMin(min);
        seekBar.setMax(max);
        seekBar.setProgress(currentValue);
        inflate.findViewById(R.id.imageViewdel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = seekBar.getProgress();
                if (progress > min) {
                    seekBar.setProgress(progress - 1);
                }
            }
        });
        inflate.findViewById(R.id.imageViewadd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = seekBar.getProgress();
                if (progress < max) {
                    seekBar.setProgress(progress + 1);
                }
            }
        });
        inflate.findViewById(R.id.comfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = seekBar.getProgress();
                if (TextUtils.equals(WhatSeekBarIsShow, "阶段配时号")) {
                    currentIndex = progress;
                    CurrentLuKou = 1;
                } else if (TextUtils.equals(WhatSeekBarIsShow, "路口基准图")) {
                    CurrentLuKou = progress;
                }
                refreashUI();
                alertDialog.dismiss();
            }
        });
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                tv_ChooseXiangWeiCha.setText(WhatSeekBarIsShow + "：" + value);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
    }

    class JieDuanRunNable implements Runnable {

        @Override
        public void run() {
            try {
                UdpClientSocket udpClientSocket = new UdpClientSocket();
                byte[] bytesSend = new byte[]{(byte) 0x80, (byte) 0xC1, (byte) 0x00};
                udpClientSocket.send(mIp, GbtDefine.GBT_PORT, bytesSend);
                byte[] bytes = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                String s = ByteUtils.bytesToHexString(bytes);
                LogUtil.LogShitou("阶段返回值", s);
                //84C1001010
                // 0101000080030F030001
                // 0102000000380F030001
                // 0103000003800F0300010104000038000F030001010000000000000000000100000000000000000001000000000000000000010000000000000000000100000000000000000001000000000000000000010000000000000000000100000000000000000001000000000000000000010000000000000000000100000000000000000001000000000000000000
                // 02010000010114030001
                // 02020000828214030001
                // 02030000101014030001
                // 02040000282814030001
                // 020000000000000000000200000000000000000002000000000000000000020000000000000000000200000000000000000002000000000000000000020000000000000000000200000000000000000002000000000000000000020000000000000000000200000000000000000002000000000000000000
                // 03010000202014030001
                // 0302000030001403000103030000000114030001030000000000000000000300000000000000000003000000000000000000030000000000000000000300000000000000000003000000000000000000030000000000000000000300000000000000000003000000000000000000030000000000000000000300000000000000000003000000000000000000030000000000000000000400000000000000000004000000000000000000040000000000000000000400000000000000000004000000000000000000040000000000000000000400000000000000000004000000000000000000040000000000000000000400000000000000000004000000000000000000040000000000000000000400000000000000000004000000000000000000040000000000000000000400000000000000000005000000000000000000050000000000000000000500000000000000000005000000000000000000050000000000000000000500000000000000000005000000000000000000050000000000000000000500000000000000000005000000000000000000050000000000000000000500000000000000000005000000000000000000050000000000000000000500000000000000000005000000000000000000060000000000000000000600000000000000000006000000000000000000060000000000000000000600000000000000000006000000000000000000060000000000000000000600000000000000000006000000000000000000060000000000000000000600000000000000000006000000000000000000060000000000000000000600000000000000000006000000000000000000060000000000000000000700000000000000000007000000000000000000070000000000000000000700000000
                Log.e("JieDuanRunNable", "JieDuanRunNable--run--阶段返回值长度" + s.length());
                stagePattern(bytes);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("JieDuanRunNable", "JieDuanRunNable--run--异常");
            }
        }
    }

    private void setData(byte[] bytes) {
        if (ByteUtils.bytesUInt(bytes[0]) == 133 && ByteUtils.bytesUInt(bytes[1]) == 193) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void refreashUI() {
        // 01阶段方案号 01阶段号（配时号） 0000 8西（1000西人行） 0南 0东 3北（0011北左北直） 0F绿灯（15秒） 03黄灯（3秒） 00红灯 01阶段感应参数
        List<byte[]> bytesArr = jieDuanByteArrList.get(currentIndex - 1);
        byte[] bytes1 = bytesArr.get(CurrentLuKou - 1);
        String binaryStrFromByteArr = ByteUtils.getBinaryStrFromByteArr(new byte[]{bytes1[2], bytes1[3], bytes1[4], bytes1[5]});
        String light2Str = new StringBuilder(binaryStrFromByteArr).reverse().toString();
        mCurrentLightInts = ByteUtils.byteArrToIntArr(light2Str);
        for (int i = 0; i < mCurrentLightInts.length; i++) {
            setLight(i);
        }
        mTvJieDuanPeiShiHao.setText("阶段配时号：" + currentIndex);
        mTvLuKouJiZhunTu.setText("路口基准图：" + CurrentLuKou);
        mTv_countdown.setText(CurrentLuKou + "");
        mTextViewGreen.setText("绿：" + ByteUtils.bytesUInt(bytes1[6]) + "");
        mTextViewYellow.setText("黄：" + ByteUtils.bytesUInt(bytes1[7]) + "");
        mTextViewRed.setText("红：" + ByteUtils.bytesUInt(bytes1[8]) + "");
        mSeekBarGreen.setProgress(ByteUtils.bytesUInt(bytes1[6]));
        mSeekBarYellow.setProgress(ByteUtils.bytesUInt(bytes1[7]));
        mSeekBarRed.setProgress(ByteUtils.bytesUInt(bytes1[8]));
        luKouJiZhunTuLength = 0;
        for (int i = 0; i < bytesArr.size(); i++) {
            if (bytesArr.get(i)[1] != 0) {
                luKouJiZhunTuLength++;
            } else {
                break;
            }
        }
    }

    private void setLight(int i) {
        if (mCurrentLightInts[i] == 1) {
            setLightNext(i, R.mipmap.greenone);
        } else {
            setLightNext(i, R.mipmap.redone);
        }
    }

    private void setLightNext(int i, int lightColor) {
        lightImg[i].setImageResource(lightColor);
//        if (i == 3) {
//            lightImg[16].setImageResource(lightColor);
//        }
//        if (i == 7) {
//            lightImg[20].setImageResource(lightColor);
//        }
//        if (i == 11) {
//            lightImg[24].setImageResource(lightColor);
//        }
//        if (i == 15) {
//            lightImg[28].setImageResource(lightColor);
//        }
    }
}
