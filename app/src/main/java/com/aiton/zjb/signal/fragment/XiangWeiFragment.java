package com.aiton.zjb.signal.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseFragment;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.constant.GbtDefine;
import com.aiton.zjb.signal.model.DengBanInfo;
import com.aiton.zjb.signal.model.NoAction;
import com.aiton.zjb.signal.model.NodeListEntity;
import com.aiton.zjb.signal.model.UserInfo;
import com.aiton.zjb.signal.model.XiangWeiInfo;
import com.aiton.zjb.signal.util.ByteUtils;
import com.aiton.zjb.signal.util.UdpClientSocket;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class XiangWeiFragment extends ZjbBaseFragment implements View.OnClickListener {


    private View mInflate;
    private String mIp;
    private ViewPager mViewpager;
    private List<DengBanInfo> mDengBanInfoList = new ArrayList<>();
    private List<byte[]> tongDaoInfoByteArrList = new ArrayList<>();
    private List<byte[]> tongDaoPeiZhiByteArrList = new ArrayList<>();
    private Map<Integer, String> cheLiuInfoMap = new HashMap<>();
    private String WhatSeekBarIsShow;
    private String WhatListIsShow;
    private int currentIndex = 1;
    private TextView mTextViewTongDaoHao;
    private TextView mTextViewZiDongShan;
    private TextView mTextViewType;
    private TextView mTextViewGuanLianXiangWei;
    private boolean isDialogChoose = false;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constant.BROADCASTRECEIVER.XIANGWEI:
                    int intExtra = intent.getIntExtra(Constant.IntentKey.TONG_DAO_HAO, 1);
                    selectTongDao(intExtra);
                    break;
            }
        }
    };
    private int mCurrentGuanLianXiangWei;
    private int mCurrentZiDongShan;
    private Map<Integer, String> ziDongShanStrMap = new HashMap<>();
    private AlertDialog mListDialog;
    private AlertDialog mCheckListDialog;
    private Map<Integer, String> leiXingMap = new HashMap<>();
    private Map<Integer, String> XiangWeiTypeMap = new HashMap<>();
    private Map<Integer, String> XiangWeiXuanXiangMap = new HashMap<>();
    private Map<Integer, Integer> xiangWeiCheLiuMap = new HashMap<>();
    private Map<Integer, Integer> CheLiuXiangWeiMap = new HashMap<>();
    private Map<Integer, Integer> cheDaoNumMap = new HashMap<>();
    private int mCurrentType;
    private RadioButton mRadioButtonTongDaoPeiZhi;
    private RadioButton radioButtonPuTongXiangWei;
    private RadioButton radioButtonFangXiangPeiZhi;
    private View mView_tongDaoPeiZhi;
    private View mView_puTongXiangWei;
    private View mView_fangXiangPeiZhi;
    private List<byte[]> XiangWeibyteArrList = new ArrayList<>();
    private TextView mTextViewTongDaoHaoXiangWei;
    private TextView mTextViewTypeXiangWei;
    private TextView mTextViewGongNengXiangWei;
    private TextView mTextViewMinGreenXiangWei;
    private TextView mTextViewMaxGreenXiangWei1;
    private TextView mTextViewMaxGreenXiangWei2;
    private TextView mTextViewDelayGreenXiangWei;
    private TextView mTextViewShanGreenXiangWei;
    private TextView mTextViewCheckXiangWei;
    private TextView mTextViewFangXiang;
    private TextView mTextViewCheDao;
    private TextView mTvGuanLianXiangWeiFangXiang;
    private int mCurrentXiangWeiType;
    private int mCurrentXiangWeiGongNeng;
    private int mCurrentMinGreen;
    private int mCurrentMaxGreen1;
    private int mCurrentMaxGreen2;
    private int mCurrentDelayGreen;
    private int mCurrentShanGreen;
    private int mCurrentCheDaoNum=1;
    private int mCurrentFangXiang;
    private int mCurrentSaveFangXiang;
    private int mCurrentSaveGaunLian;
    private int mCurrentXiangWeiHao;
    private Map<Integer, Boolean> checkEdXiangWeiMap = new HashMap<>();
    private Map<Integer, Boolean> checkEdSaveXiangWeiMap = new HashMap<>();
    private MyCheckDialogAdapter mMyCheckDialogAdapter;
    private CheckBox mCheckBoxAll;
    private boolean isCheckAll = true;
    private CheckBox mCheckBoxIsMoreSet;
    private CheckBox mCheckBoxSetFangXiang;
    private NodeListEntity mNodeListEntity;
    private boolean isOnline =false;
    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;

    public XiangWeiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_fragment03, container, false);
            init();
        }
        //?????????rootView?????????????????????????????????parent??? ?????????parent?????????parent?????????????????????????????????rootview?????????parent????????????
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
        //????????????
        mDengBanInfoList.add(new DengBanInfo("????????????", 1));
        mDengBanInfoList.add(new DengBanInfo("????????????", 5));
        mDengBanInfoList.add(new DengBanInfo("????????????", 9));
        mDengBanInfoList.add(new DengBanInfo("????????????", 13));
        mDengBanInfoList.add(new DengBanInfo("?????????????????????", 17));
        mDengBanInfoList.add(new DengBanInfo("???????????????", 21));
        mDengBanInfoList.add(new DengBanInfo("???????????????", 25));
        mDengBanInfoList.add(new DengBanInfo("???????????????", 29));
        //?????????
        ziDongShanStrMap.put(0, "?????????");
        ziDongShanStrMap.put(2, "?????????");
        ziDongShanStrMap.put(10, "????????????");
        ziDongShanStrMap.put(4, "?????????");
        ziDongShanStrMap.put(12, "????????????");
        //??????
        leiXingMap.put(1, "??????");
        leiXingMap.put(2, "?????????");
        leiXingMap.put(3, "??????");
        //????????????
        XiangWeiTypeMap.put(0, "??????");//00000000
        XiangWeiTypeMap.put(16, "????????????");//00010000
        XiangWeiTypeMap.put(32, "????????????");//00100000
        XiangWeiTypeMap.put(64, "????????????");//01000000
        XiangWeiTypeMap.put(128, "????????????");//10000000
        //??????????????????
        XiangWeiXuanXiangMap.put(0, "??????");//00000000
        XiangWeiXuanXiangMap.put(1, "????????????");//00000001
        XiangWeiXuanXiangMap.put(2, "??????????????????");//00000010
        XiangWeiXuanXiangMap.put(4, "??????????????????");//00000100
        XiangWeiXuanXiangMap.put(8, "????????????");//00001000
        XiangWeiXuanXiangMap.put(16, "????????????");//00010000
        //??????
        cheLiuInfoMap.put(1, "??????");
        cheLiuInfoMap.put(2, "??????");
        cheLiuInfoMap.put(4, "??????");
        cheLiuInfoMap.put(8, "?????????");
        cheLiuInfoMap.put(65, "??????");
        cheLiuInfoMap.put(66, "??????");
        cheLiuInfoMap.put(68, "??????");
        cheLiuInfoMap.put(72, "?????????");
        cheLiuInfoMap.put(129, "??????");
        cheLiuInfoMap.put(130, "??????");
        cheLiuInfoMap.put(132, "??????");
        cheLiuInfoMap.put(136, "?????????");
        cheLiuInfoMap.put(193, "??????");
        cheLiuInfoMap.put(194, "??????");
        cheLiuInfoMap.put(196, "??????");
        cheLiuInfoMap.put(200, "?????????");
        cheLiuInfoMap.put(24, "???????????????");
        cheLiuInfoMap.put(88, "???????????????");
        cheLiuInfoMap.put(152, "???????????????");
        cheLiuInfoMap.put(216, "???????????????");
        cheLiuInfoMap.put(0, "?????????");
        cheLiuInfoMap.put(64, "?????????");
        cheLiuInfoMap.put(128, "?????????");
        cheLiuInfoMap.put(192, "?????????");
        cheLiuInfoMap.put(7, "?????????");
        cheLiuInfoMap.put(71, "?????????");
        cheLiuInfoMap.put(135, "?????????");
        cheLiuInfoMap.put(199, "?????????");
        cheLiuInfoMap.put(5, "?????????");
        cheLiuInfoMap.put(69, "?????????");
        cheLiuInfoMap.put(133, "?????????");
        cheLiuInfoMap.put(197, "?????????");
        //????????????????????????
        for (int i = 0; i < 32; i++) {
            checkEdSaveXiangWeiMap.put(i + 1, false);
        }
    }

    @Override
    protected void findID() {
        mViewpager = (ViewPager) mInflate.findViewById(R.id.viewpagerXiangWei);
        mTextViewTongDaoHao = (TextView) mInflate.findViewById(R.id.textViewTongDaoHao);
        mTextViewZiDongShan = (TextView) mInflate.findViewById(R.id.textViewZiDongShan);
        mTextViewType = (TextView) mInflate.findViewById(R.id.textViewType);
        mTextViewGuanLianXiangWei = (TextView) mInflate.findViewById(R.id.textViewGuanLianXiangWei);
        mRadioButtonTongDaoPeiZhi = (RadioButton) mInflate.findViewById(R.id.radioButtonTongDaoPeiZhi);
        radioButtonPuTongXiangWei = (RadioButton) mInflate.findViewById(R.id.radioButtonPuTongXiangWei);
        radioButtonFangXiangPeiZhi = (RadioButton) mInflate.findViewById(R.id.radioButtonFangXiangPeiZhi);
        mView_tongDaoPeiZhi = mInflate.findViewById(R.id.view_tongDaoPeiZhi);
        mView_puTongXiangWei = mInflate.findViewById(R.id.view_puTongXiangWei);
        mView_fangXiangPeiZhi = mInflate.findViewById(R.id.view_fangXiangPeiZhi);
        mTextViewTongDaoHaoXiangWei = (TextView) mInflate.findViewById(R.id.textViewTongDaoHaoXiangWei);
        mTextViewTypeXiangWei = (TextView) mInflate.findViewById(R.id.textViewTypeXiangWei);
        mTextViewGongNengXiangWei = (TextView) mInflate.findViewById(R.id.textViewGongNengXiangWei);
        mTextViewMinGreenXiangWei = (TextView) mInflate.findViewById(R.id.textViewMinGreenXiangWei);
        mTextViewMaxGreenXiangWei1 = (TextView) mInflate.findViewById(R.id.textViewMaxGreenXiangWei1);
        mTextViewMaxGreenXiangWei2 = (TextView) mInflate.findViewById(R.id.textViewMaxGreenXiangWei2);
        mTextViewDelayGreenXiangWei = (TextView) mInflate.findViewById(R.id.textViewDelayGreenXiangWei);
        mTextViewShanGreenXiangWei = (TextView) mInflate.findViewById(R.id.textViewShanGreenXiangWei);
        mTextViewCheckXiangWei = (TextView) mInflate.findViewById(R.id.textViewCheckXiangWei);
        mTextViewFangXiang = (TextView) mInflate.findViewById(R.id.textViewFangXiang);
        mTextViewCheDao = (TextView) mInflate.findViewById(R.id.textViewCheDao);
        mTvGuanLianXiangWeiFangXiang = (TextView) mInflate.findViewById(R.id.tvGuanLianXiangWeiFangXiang);
        mCheckBoxIsMoreSet = (CheckBox) mInflate.findViewById(R.id.checkBoxIsMoreSet);
        mCheckBoxSetFangXiang = (CheckBox) mInflate.findViewById(R.id.checkBoxSetFangXiang);
    }

    @Override
    protected void initViews() {
        mViewpager.setOffscreenPageLimit(2);
    }

    @Override
    protected void setListeners() {
        mTextViewTongDaoHao.setOnClickListener(this);
        mTextViewZiDongShan.setOnClickListener(this);
        mTextViewType.setOnClickListener(this);
        mTextViewGuanLianXiangWei.setOnClickListener(this);
        mTextViewFangXiang.setOnClickListener(this);
        mTextViewCheDao.setOnClickListener(this);
        mTvGuanLianXiangWeiFangXiang.setOnClickListener(this);
        mTextViewTongDaoHaoXiangWei.setOnClickListener(this);
        mTextViewTypeXiangWei.setOnClickListener(this);
        mTextViewGongNengXiangWei.setOnClickListener(this);
        mTextViewMinGreenXiangWei.setOnClickListener(this);
        mTextViewMaxGreenXiangWei1.setOnClickListener(this);
        mTextViewMaxGreenXiangWei2.setOnClickListener(this);
        mTextViewDelayGreenXiangWei.setOnClickListener(this);
        mTextViewShanGreenXiangWei.setOnClickListener(this);
        mTextViewCheckXiangWei.setOnClickListener(this);
        mViewpager.addOnPageChangeListener(new MyPageChangListener());
        mRadioButtonTongDaoPeiZhi.setOnCheckedChangeListener(new MyCheckedChangeListener());
        radioButtonPuTongXiangWei.setOnCheckedChangeListener(new MyCheckedChangeListener());
        radioButtonFangXiangPeiZhi.setOnCheckedChangeListener(new MyCheckedChangeListener());
        mInflate.findViewById(R.id.button_saveTongDao).setOnClickListener(this);
        mInflate.findViewById(R.id.buttonSaveXiangWei).setOnClickListener(this);
        mInflate.findViewById(R.id.buttonSaveFangXiang).setOnClickListener(this);
        mInflate.findViewById(R.id.imageViewLeft).setOnClickListener(this);
        mInflate.findViewById(R.id.imageViewRight).setOnClickListener(this);
    }

    class MyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                switch (compoundButton.getId()) {
                    case R.id.radioButtonTongDaoPeiZhi:
                        mView_tongDaoPeiZhi.setVisibility(View.VISIBLE);
                        mView_puTongXiangWei.setVisibility(View.GONE);
                        mView_fangXiangPeiZhi.setVisibility(View.GONE);
                        break;
                    case R.id.radioButtonPuTongXiangWei:
                        mView_tongDaoPeiZhi.setVisibility(View.GONE);
                        mView_puTongXiangWei.setVisibility(View.VISIBLE);
                        mView_fangXiangPeiZhi.setVisibility(View.GONE);
                        break;
                    case R.id.radioButtonFangXiangPeiZhi:
                        mView_tongDaoPeiZhi.setVisibility(View.GONE);
                        mView_puTongXiangWei.setVisibility(View.GONE);
                        mView_fangXiangPeiZhi.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    class MyPageChangListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (!isDialogChoose) {
                if (position == 0) {
                    currentIndex = 1;
                } else if (position == 1) {
                    currentIndex = 5;
                } else if (position == 2) {
                    currentIndex = 9;
                } else if (position == 3) {
                    currentIndex = 13;
                } else if (position == 4) {
                    currentIndex = 17;
                } else if (position == 5) {
                    currentIndex = 21;
                } else if (position == 6) {
                    currentIndex = 25;
                } else if (position == 7) {
                    currentIndex = 29;
                }
                selectTongDao(currentIndex);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewLeft:
                int currentItem = mViewpager.getCurrentItem();
                if (currentItem>0){
                    mViewpager.setCurrentItem(currentItem-1);
                }
                break;
            case R.id.imageViewRight:
                int currentItem1 = mViewpager.getCurrentItem();
                if (currentItem1<7){
                    mViewpager.setCurrentItem(currentItem1+1);
                }
                break;
            case R.id.buttonSaveFangXiang:
                if (mCheckBoxSetFangXiang.isChecked()) {
                    if (TextUtils.equals("??????????????????", mTextViewFangXiang.getText().toString())) {
                        showMsgDialog("???????????????");
                    } else if (TextUtils.equals("?????????????????????", mTextViewCheDao.getText().toString())) {
                        showMsgDialog("??????????????????");
                    } else {
                        Log.e("XiangWeiFragment", "XiangWeiFragment--onClick--" + mCurrentSaveFangXiang);
                        Log.e("XiangWeiFragment", "XiangWeiFragment--onClick--" + CheLiuXiangWeiMap.get(mCurrentSaveFangXiang));
                        Log.e("XiangWeiFragment", "XiangWeiFragment--onClick--" + mCurrentSaveGaunLian);
                        if (CheLiuXiangWeiMap.get(mCurrentSaveFangXiang) == mCurrentSaveGaunLian||mCurrentSaveGaunLian==0) {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("??????????????????")
                                    .setPositiveButton("???", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (isOnline){
                                                setDirect();
                                            }else{
                                                new Thread(new SetFangXiangRunnable()).start();
                                            }
                                        }
                                    })
                                    .setNegativeButton("???", null)
                                    .create()
                                    .show();
                        } else {
                            if (xiangWeiCheLiuMap.get(mCurrentSaveGaunLian)==null){
                                if (CheLiuXiangWeiMap.get(mCurrentSaveFangXiang) == 0){
                                    new AlertDialog.Builder(getActivity())
                                            .setMessage("??????????????????")
                                            .setPositiveButton("???", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    if (isOnline){
                                                        setDirect();
                                                    }else{
                                                        new Thread(new SetFangXiangRunnable()).start();
                                                    }
                                                }
                                            })
                                            .setNegativeButton("???", null)
                                            .create()
                                            .show();
                                }else{
                                    int xiangwei = CheLiuXiangWeiMap.get(mCurrentSaveFangXiang);
                                    String cheliu = cheLiuInfoMap.get(mCurrentSaveFangXiang);
                                    showMsgDialog(cheliu +"?????????->>P" + xiangwei + "\n????????????"+cheliu+"???P"+xiangwei+"?????????\nP"+xiangwei+"???????????????null???????????????");
                                }
                            }else{
                                String fangxiang = cheLiuInfoMap.get(xiangWeiCheLiuMap.get(mCurrentSaveGaunLian));
                                showMsgDialog("P"+mCurrentSaveGaunLian+"???????????????->>" + fangxiang + "\n????????????" + fangxiang + "???P"+mCurrentSaveGaunLian+"?????????\nP" + mCurrentSaveGaunLian + "???????????????null???????????????");
                            }
                        }
                    }
                } else {
                    showMsgDialog("???????????????????????????");
                }
                break;
            case R.id.buttonSaveXiangWei:
                String send = "81950020";
                String duiXiang = ByteUtils.bytesToHexString(new byte[]{0x00, 0x00, (byte) mCurrentMinGreen, (byte) mCurrentDelayGreen, (byte) mCurrentMaxGreen1, (byte) mCurrentMaxGreen2, 0x07, (byte) mCurrentShanGreen, (byte) mCurrentXiangWeiType, (byte) mCurrentXiangWeiGongNeng, 0x00});
                String xiangweihao = "";
                Log.e("SetXiangWeiRunnable", "SetXiangWeiRunnable--run--????????????????????????" + duiXiang);
                if (mCheckBoxIsMoreSet.isChecked()) {
                    if (TextUtils.equals("?????????", mTextViewCheckXiangWei.getText().toString())) {
                        showTipDialog("??????????????????????????????????????????????????????");
                        break;
                    } else {
                        for (int i = 0; i < XiangWeibyteArrList.size(); i++) {
                            if (checkEdSaveXiangWeiMap.get(i + 1)) {
                                send = send + ByteUtils.bytesToHexString(new byte[]{(byte) (i + 1)}) + duiXiang;
                                xiangweihao = xiangweihao + (i + 1);
                            } else {
                                send = send + ByteUtils.bytesToHexString(XiangWeibyteArrList.get(i));
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < XiangWeibyteArrList.size(); i++) {
                        if (mCurrentXiangWeiHao == (i + 1)) {
                            send = send + ByteUtils.bytesToHexString(new byte[]{(byte) (i + 1)}) + duiXiang;
                        } else {
                            send = send + ByteUtils.bytesToHexString(XiangWeibyteArrList.get(i));
                        }
                    }
                }
                Log.e("XiangWeiFragment", "XiangWeiFragment--onClick--???????????????" + xiangweihao);
                Log.e("SetXiangWeiRunnable", "SetXiangWeiRunnable--run--????????????????????????" + send);
                final String finalSend = send;
                new AlertDialog.Builder(getActivity())
                        .setMessage("??????????????????")
                        .setPositiveButton("???", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (isOnline){
                                    setPhase(finalSend);
                                }else{
                                    new Thread(new SetXiangWeiRunnable(finalSend)).start();
                                }
                            }
                        })
                        .setNegativeButton("???", null)
                        .create()
                        .show();
                break;
            case R.id.textViewCheckXiangWei:
                List<Integer> checkList = new ArrayList<>();
                for (int i = 0; i < 32; i++) {
                    checkList.add(i + 1);
                }
                checkEdXiangWeiMap.clear();
                checkEdXiangWeiMap.putAll(checkEdSaveXiangWeiMap);
                showCheckListDialog(checkList);
                break;
            case R.id.textViewShanGreenXiangWei:
                WhatSeekBarIsShow = "????????????";
                showSeekBarDialog(1, 255, mCurrentShanGreen);
                break;
            case R.id.textViewDelayGreenXiangWei:
                WhatSeekBarIsShow = "????????????????????????";
                showSeekBarDialog(1, 255, mCurrentDelayGreen);
                break;
            case R.id.textViewMinGreenXiangWei:
                WhatSeekBarIsShow = "??????????????????";
                showSeekBarDialog(1, 255, mCurrentMinGreen);
                break;
            case R.id.textViewMaxGreenXiangWei1:
                WhatSeekBarIsShow = "??????????????????1";
                showSeekBarDialog(1, 255, mCurrentMaxGreen1);
                break;
            case R.id.textViewMaxGreenXiangWei2:
                WhatSeekBarIsShow = "??????????????????2";
                showSeekBarDialog(1, 255, mCurrentMaxGreen2);
                break;
            case R.id.textViewGongNengXiangWei:
                WhatListIsShow = "??????????????????";
                List<Integer> listXiangWeiXuanXiang = new ArrayList<>();
                listXiangWeiXuanXiang.add(0);
                listXiangWeiXuanXiang.add(1);
                listXiangWeiXuanXiang.add(2);
                listXiangWeiXuanXiang.add(4);
                listXiangWeiXuanXiang.add(8);
                listXiangWeiXuanXiang.add(16);
                showListDialog(listXiangWeiXuanXiang);
                break;
            case R.id.textViewTypeXiangWei:
                WhatListIsShow = "????????????";
                List<Integer> listXiangWeiType = new ArrayList<>();
                listXiangWeiType.add(0);
                listXiangWeiType.add(16);
                listXiangWeiType.add(32);
                listXiangWeiType.add(64);
                listXiangWeiType.add(128);
                showListDialog(listXiangWeiType);
                break;
            case R.id.textViewTongDaoHaoXiangWei:
                WhatSeekBarIsShow = "?????????";
                showSeekBarDialog(1, 32, currentIndex);
                break;
            case R.id.button_saveTongDao:
                new AlertDialog.Builder(getActivity())
                        .setMessage("??????????????????")
                        .setPositiveButton("???", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (isOnline){
                                    setChannel();
                                }else {
                                    new Thread(new SetTongDaoRunnable()).start();
                                }
                            }
                        })
                        .setNegativeButton("???", null)
                        .create()
                        .show();
                break;
            case R.id.textViewTongDaoHao:
                WhatSeekBarIsShow = "?????????";
                showSeekBarDialog(1, 32, currentIndex);
                break;
            case R.id.textViewZiDongShan:
                WhatListIsShow = "?????????";
                List<Integer> list = new ArrayList<>();
                list.add(0);
                list.add(2);
                list.add(10);
                list.add(4);
                list.add(12);
                showListDialog(list);
                break;
            case R.id.textViewType:
                WhatListIsShow = "??????";
                List<Integer> list1 = new ArrayList<>();
                list1.add(1);
                list1.add(2);
                list1.add(3);
                showListDialog(list1);
                break;
            case R.id.textViewGuanLianXiangWei:
                WhatSeekBarIsShow = "????????????";
                showSeekBarDialog(0, 32, mCurrentGuanLianXiangWei);
                break;
            case R.id.textViewFangXiang:
                WhatListIsShow = "??????";
                List<Integer> FangXiangList = new ArrayList<>();
                FangXiangList.add(1);
                FangXiangList.add(2);
                FangXiangList.add(4);
                FangXiangList.add(8);
                FangXiangList.add(65);
                FangXiangList.add(66);
                FangXiangList.add(68);
                FangXiangList.add(72);
                FangXiangList.add(129);
                FangXiangList.add(130);
                FangXiangList.add(132);
                FangXiangList.add(136);
                FangXiangList.add(193);
                FangXiangList.add(194);
                FangXiangList.add(196);
                FangXiangList.add(200);
                FangXiangList.add(24);
                FangXiangList.add(88);
                FangXiangList.add(152);
                FangXiangList.add(216);
                FangXiangList.add(0);
                FangXiangList.add(64);
                FangXiangList.add(128);
                FangXiangList.add(192);
                FangXiangList.add(7);
                FangXiangList.add(71);
                FangXiangList.add(135);
                FangXiangList.add(199);
                FangXiangList.add(5);
                FangXiangList.add(69);
                FangXiangList.add(133);
                FangXiangList.add(197);
                showListDialog(FangXiangList);
                break;
            case R.id.textViewCheDao:
                WhatSeekBarIsShow = "?????????";
                showSeekBarDialog(1, 6, mCurrentCheDaoNum);
                break;
            case R.id.tvGuanLianXiangWeiFangXiang:
                WhatSeekBarIsShow = "??????????????????";
                showSeekBarDialog(0, 32, mCurrentSaveGaunLian);
                break;
        }
    }

    private void setChannel() {
        String sendStr = "81B00020";
        String duiXiangStr = ByteUtils.bytesToHexString(new byte[]{(byte) currentIndex, (byte) mCurrentGuanLianXiangWei, (byte) mCurrentZiDongShan, (byte) mCurrentType});
        Log.e("SetTongDaoRunnable", "SetTongDaoRunnable--run--??????????????????" + duiXiangStr);
        for (int i = 0; i < tongDaoPeiZhiByteArrList.size(); i++) {
            byte[] bytes = tongDaoPeiZhiByteArrList.get(i);
            if (ByteUtils.bytesUInt(bytes[0]) == currentIndex) {
                sendStr = sendStr + duiXiangStr;
            } else {
                sendStr = sendStr + ByteUtils.bytesToHexString(bytes);
            }
        }
        Log.e("SetTongDaoRunnable", "SetTongDaoRunnable--run--?????????????????????" + sendStr);
        //81B00020
        // 01010002
        // 02020002
        // 030300020404000305050002060600020707000208080003090900020A0A00020B0B00020C0C00030D0D00020E0E00020F0F0002101000031111000312120003131300031414000315150002161600021717000218180002191900021A1A00021B1B00021C1C00021D1D00021E1E00021F1F000220200002
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", mNodeListEntity.getId());
        params.put("byteString", sendStr);
        String url = Constant.Url.SET_CHANNEL;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--?????????????????????" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        String object = (String) noAction.getObject();
                        byte[] bytes = ByteUtils.hexStringToByte(object);
                        if (ByteUtils.bytesUInt(bytes[0]) == 133 && ByteUtils.bytesUInt(bytes[1]) == 176) {
                            Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), noAction.getMessage(), Toast.LENGTH_SHORT).show();
                        if (noAction.getMessageCode() == 3) {
                            reLogin();
                        }
                    }
                    getTongDao(true);
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void setPhase(String finalSend) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", mNodeListEntity.getId());
        params.put("byteString", finalSend);
        String url = Constant.Url.SET_PHASE;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--?????????????????????" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        String object = (String) noAction.getObject();
                        byte[] bytes = ByteUtils.hexStringToByte(object);
                        if (ByteUtils.bytesUInt(bytes[0]) == 133 && ByteUtils.bytesUInt(bytes[1]) == 149) {
                            Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), noAction.getMessage(), Toast.LENGTH_SHORT).show();
                        if (noAction.getMessageCode() == 3) {
                            reLogin();
                        }
                    }
                    getTongDao(true);
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void setDirect() {
        String send = "81FA0020";
        String duixiang = ByteUtils.bytesToHexString(new byte[]{(byte) mCurrentSaveFangXiang, (byte) mCurrentSaveGaunLian, 0x00, (byte) mCurrentCheDaoNum});
        Log.e("SetFangXiangRunnable", "SetFangXiangRunnable--run--?????????????????????" + duixiang);
        for (int i = 0; i < tongDaoInfoByteArrList.size(); i++) {
            byte[] bytes = tongDaoInfoByteArrList.get(i);
            if (mCurrentSaveFangXiang == ByteUtils.bytesUInt(bytes[0])) {
                send = send + duixiang;
            } else {
                send = send + ByteUtils.bytesToHexString(bytes);
            }
        }
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", mNodeListEntity.getId());
        params.put("byteString", send);
        String url = Constant.Url.SET_DIRECT;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--?????????????????????" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        String object = (String) noAction.getObject();
                        byte[] bytes = ByteUtils.hexStringToByte(object);
                        if (ByteUtils.bytesUInt(bytes[0]) == 133 && ByteUtils.bytesUInt(bytes[1]) == 250) {
                            Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), noAction.getMessage(), Toast.LENGTH_SHORT).show();
                        if (noAction.getMessageCode() == 3) {
                            reLogin();
                        }
                    }
                    getTongDao(true);
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void showMsgDialog(String fangxiang) {
        new AlertDialog.Builder(getActivity())
                .setMessage(fangxiang)
                .setPositiveButton("???", null)
                .create()
                .show();
    }

    private void showCheckListDialog(List<Integer> list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View list_dialog_view = getActivity().getLayoutInflater().inflate(R.layout.check_list_dialog, null);
        ListView dialogListView = (ListView) list_dialog_view.findViewById(R.id.listView_check);
        mMyCheckDialogAdapter = new MyCheckDialogAdapter(list);
        dialogListView.setAdapter(mMyCheckDialogAdapter);
        list_dialog_view.findViewById(R.id.comfirm_Check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEdSaveXiangWeiMap.clear();
                checkEdSaveXiangWeiMap.putAll(checkEdXiangWeiMap);
                String s = "";
                for (int i = 0; i < checkEdSaveXiangWeiMap.size(); i++) {
                    if (checkEdSaveXiangWeiMap.get(i + 1)) {
                        s = s + "," + (i + 1);
                    }
                }
                if (TextUtils.equals(s, "")) {
                    s = "?????????";
                }
                mTextViewCheckXiangWei.setText(s);
                mCheckListDialog.dismiss();
            }
        });
        mCheckBoxAll = (CheckBox) list_dialog_view.findViewById(R.id.checkBoxAll);
        mCheckBoxAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isCheckAll) {
                    if (b) {
                        for (int i = 0; i < checkEdXiangWeiMap.size(); i++) {
                            checkEdXiangWeiMap.put(i + 1, true);
                        }
                    } else {
                        for (int i = 0; i < checkEdXiangWeiMap.size(); i++) {
                            checkEdXiangWeiMap.put(i + 1, false);
                        }
                    }
                    mMyCheckDialogAdapter.notifyDataSetChanged();
                }
            }
        });
        mCheckListDialog = builder.setView(list_dialog_view)
                .create();
        mCheckListDialog.show();
        int orientation = getActivity().getResources().getConfiguration().orientation;
        Window dialogWindow = mCheckListDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getActivity().getResources().getDisplayMetrics(); // ????????????????????????
        if (orientation == 1) {
            lp.width = (int) (d.widthPixels * 0.8); // ????????????????????????0.6
            if (list.size() > 6) {
                lp.height = (int) (d.heightPixels * 0.5);
            }
        } else {
            lp.width = (int) (d.widthPixels * 0.5); // ????????????????????????0.6
            if (list.size() >= 5) {
                lp.height = (int) (d.heightPixels * 0.8);
            }
        }
        dialogWindow.setAttributes(lp);
    }

    /**
     * ????????????dialog
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
        DisplayMetrics d = getActivity().getResources().getDisplayMetrics(); // ????????????????????????
        if (orientation == 1) {
            lp.width = (int) (d.widthPixels * 0.8); // ????????????????????????0.6
            if (list.size() > 6) {
                lp.height = (int) (d.heightPixels * 0.5);
            }
        } else {
            lp.width = (int) (d.widthPixels * 0.5); // ????????????????????????0.6
            if (list.size() >= 5) {
                lp.height = (int) (d.heightPixels * 0.8);
            }
        }
        dialogWindow.setAttributes(lp);
    }

    public class MyItemClick implements AdapterView.OnItemClickListener {

        private List<Integer> list;

        public MyItemClick(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Integer integer = list.get(i);
            if (TextUtils.equals(WhatListIsShow, "?????????")) {
                mCurrentZiDongShan = integer;
                mTextViewZiDongShan.setText(WhatListIsShow + "???" + ziDongShanStrMap.get(integer));
            } else if (TextUtils.equals(WhatListIsShow, "??????")) {
                mCurrentType = integer;
                mTextViewType.setText(WhatListIsShow + "???" + leiXingMap.get(integer));
            } else if (TextUtils.equals(WhatListIsShow, "????????????")) {
                mCurrentXiangWeiType = integer;
                mTextViewTypeXiangWei.setText(WhatListIsShow + "???" + XiangWeiTypeMap.get(integer));
            } else if (TextUtils.equals(WhatListIsShow, "??????????????????")) {
                mCurrentXiangWeiType = integer;
                mTextViewGongNengXiangWei.setText(WhatListIsShow + "???" + XiangWeiXuanXiangMap.get(integer));
            } else if (TextUtils.equals(WhatListIsShow, "??????")) {
                mCurrentSaveFangXiang = integer;
                if (!mCheckBoxSetFangXiang.isChecked()) {
                    mCurrentSaveGaunLian = CheLiuXiangWeiMap.get(mCurrentSaveFangXiang);
                    if (mCurrentSaveGaunLian == 0) {
                        mTvGuanLianXiangWeiFangXiang.setText("???????????????null");
                    } else {
                        mTvGuanLianXiangWeiFangXiang.setText("???????????????P" + mCurrentSaveGaunLian);
                    }
                    mCurrentCheDaoNum = cheDaoNumMap.get(mCurrentSaveGaunLian);
                    mTextViewCheDao.setText("????????????" + mCurrentCheDaoNum);
                }
                mTextViewFangXiang.setText(WhatListIsShow + "???" + cheLiuInfoMap.get(integer));
            }
            mListDialog.dismiss();
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
            if (TextUtils.equals(WhatListIsShow, "?????????")) {
                textListItem.setText(ziDongShanStrMap.get(shiji.get(position)));
            } else if (TextUtils.equals(WhatListIsShow, "??????")) {
                textListItem.setText(leiXingMap.get(shiji.get(position)));
            } else if (TextUtils.equals(WhatListIsShow, "????????????")) {
                textListItem.setText(XiangWeiTypeMap.get(shiji.get(position)));
            } else if (TextUtils.equals(WhatListIsShow, "??????????????????")) {
                textListItem.setText(XiangWeiXuanXiangMap.get(shiji.get(position)));
            } else if (TextUtils.equals(WhatListIsShow, "??????")) {
                textListItem.setText(cheLiuInfoMap.get(shiji.get(position)));
            }
            return mDialog_list_itemView;
        }
    }

    class MyCheckDialogAdapter extends BaseAdapter {

        private List<Integer> shiji;

        public MyCheckDialogAdapter(List<Integer> shiji) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View mDialog_list_itemView = getLayoutInflater(getArguments()).inflate(R.layout.view_duo_xuan_list_item, null);
            final CheckBox checkBox_XiangWei = (CheckBox) mDialog_list_itemView.findViewById(R.id.checkBox_XiangWei);
            checkBox_XiangWei.setChecked(checkEdXiangWeiMap.get(position + 1));
            checkBox_XiangWei.setText(shiji.get(position) + "");
            checkBox_XiangWei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        checkEdXiangWeiMap.put(position + 1, true);
                    } else {
                        checkEdXiangWeiMap.put(position + 1, false);
                        isCheckAll = false;
                        mCheckBoxAll.setChecked(false);
                        isCheckAll = true;
                    }
                }
            });
            return mDialog_list_itemView;
        }
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            XiangWeiViewFragment xiangWeiViewFragment = new XiangWeiViewFragment();
            xiangWeiViewFragment.setView(mDengBanInfoList.get(position), tongDaoPeiZhiByteArrList, xiangWeiCheLiuMap);
            return xiangWeiViewFragment;
        }

        @Override
        public int getCount() {
            return mDengBanInfoList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BROADCASTRECEIVER.XIANGWEI);
        getActivity().registerReceiver(receiver, filter);
        if (isOnline){
            getTongDao(false);
        }else {
            new Thread(new TongDaoRunnable(false)).start();
        }
    }

    private void getTongDao(final boolean isSave) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", mNodeListEntity.getId());
        String url = Constant.Url.GET_CHANNEL;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--?????????????????????" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        String object = (String) noAction.getObject();
                        Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--?????????" + object);
                        final byte[] bytes = ByteUtils.hexStringToByte(object);
                        if (ByteUtils.bytesUInt(bytes[0]) == 132 && ByteUtils.bytesUInt(bytes[1]) == 176) {
                            tongDaoPeiZhiByteArrList.clear();
                            for (int i = 0; i < 32; i++) {
                                tongDaoPeiZhiByteArrList.add(new byte[]{bytes[4 + i * 4], bytes[5 + i * 4], bytes[6 + i * 4], bytes[7 + i * 4]});
                            }
                            getDirect(isSave);
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
                Toast.makeText(getActivity(), "?????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDirect(final boolean isSave) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", mNodeListEntity.getId());
        String url = Constant.Url.GET_DIRECT;
        Log.e("XiangWeiFragment", "XiangWeiFragment--getDirect--????????????");
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--?????????????????????" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        String object = (String) noAction.getObject();
                        Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--?????????" + object);
                        final byte[] ReceiveBytes = ByteUtils.hexStringToByte(object);
                        if (ByteUtils.bytesUInt(ReceiveBytes[0]) == 132 && ByteUtils.bytesUInt(ReceiveBytes[1]) == 250) {
                            tongDaoInfoByteArrList.clear();
                            for (int i = 0; i < 32; i++) {
                                tongDaoInfoByteArrList.add(new byte[]{ReceiveBytes[4 + 4 * i], ReceiveBytes[5 + 4 * i], ReceiveBytes[6 + 4 * i], ReceiveBytes[7 + 4 * i]});
                            }
                            xiangWeiCheLiuMap.clear();
                            CheLiuXiangWeiMap.clear();
                            cheDaoNumMap.clear();
                            for (int i = 0; i < tongDaoInfoByteArrList.size(); i++) {
                                byte[] bytes = tongDaoInfoByteArrList.get(i);
                                xiangWeiCheLiuMap.put(ByteUtils.bytesUInt(bytes[1]), ByteUtils.bytesUInt(bytes[0]));
                                CheLiuXiangWeiMap.put(ByteUtils.bytesUInt(bytes[0]), ByteUtils.bytesUInt(bytes[1]));
                                cheDaoNumMap.put(ByteUtils.bytesUInt(bytes[1]), ByteUtils.bytesUInt(bytes[3]));
                            }
                            getPhase(isSave);
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
                Toast.makeText(getActivity(), "?????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPhase(final boolean isSave) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", mNodeListEntity.getId());
        String url = Constant.Url.GET_PHASE;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--?????????????????????" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        String object = (String) noAction.getObject();
                        Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--?????????" + object);
                        final byte[] bytes = ByteUtils.hexStringToByte(object);
                        if (ByteUtils.bytesUInt(bytes[0]) == 132 && ByteUtils.bytesUInt(bytes[1]) == 149) {
                            XiangWeibyteArrList.clear();
                            for (int i = 0; i < 32; i++) {
                                XiangWeibyteArrList.add(new byte[]{bytes[4 + 12 * i], bytes[5 + 12 * i], bytes[6 + 12 * i], bytes[7 + 12 * i], bytes[8 + 12 * i], bytes[9 + 12 * i], bytes[10 + 12 * i], bytes[11 + 12 * i], bytes[12 + 12 * i], bytes[13 + 12 * i], bytes[14 + 12 * i], bytes[15 + 12 * i]});
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!isSave) {
                                        mViewpager.setAdapter(new MyViewPagerAdapter(getFragmentManager()));
                                    } else {
                                        Intent intent = new Intent();
                                        intent.putExtra(Constant.IntentKey.XIANG_WEI_VIEW, new XiangWeiInfo(tongDaoPeiZhiByteArrList, xiangWeiCheLiuMap));
                                        intent.setAction(Constant.BROADCASTRECEIVER.TONGDAO);
                                        getActivity().sendBroadcast(intent);
                                    }
                                    setViewpagerPage();
                                    selectTongDao(currentIndex);
                                }
                            });
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
                Toast.makeText(getActivity(), "?????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(receiver);
    }


    private void setGaunLianXiangWei() {
        if (mCurrentGuanLianXiangWei == 0) {
            mTextViewGuanLianXiangWei.setText("???????????????null");
        } else {
            mTextViewGuanLianXiangWei.setText("???????????????P" + mCurrentGuanLianXiangWei);
        }
    }

    private void showSeekBarDialog(final int min, final int max, final int currentValue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.seekbar_dialog, null);
        final AlertDialog alertDialog = builder.setView(inflate)
                .create();
        alertDialog.show();
        final TextView tv_ChooseXiangWeiCha = (TextView) inflate.findViewById(R.id.tv_ChooseXiangWeiCha);
        if (TextUtils.equals(WhatSeekBarIsShow, "????????????")) {
            if (currentValue == 0) {
                tv_ChooseXiangWeiCha.setText(WhatSeekBarIsShow + "???null");
            } else {
                tv_ChooseXiangWeiCha.setText(WhatSeekBarIsShow + "???P" + currentValue);
            }
        } else if (TextUtils.equals(WhatSeekBarIsShow, "??????????????????")) {
            if (currentValue == 0) {
                tv_ChooseXiangWeiCha.setText("????????????" + "???null");
            } else {
                tv_ChooseXiangWeiCha.setText("????????????" + "???P" + currentValue);
            }
        } else {
            tv_ChooseXiangWeiCha.setText(WhatSeekBarIsShow + "???" + currentValue);
        }
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
                if (TextUtils.equals(WhatSeekBarIsShow, "?????????")) {
                    selectTongDao(progress);
                    isDialogChoose = true;
                    setViewpagerPage();
                    isDialogChoose = false;
                } else if (TextUtils.equals(WhatSeekBarIsShow, "????????????")) {
                    mCurrentGuanLianXiangWei = progress;
                    setGaunLianXiangWei();
                } else if (TextUtils.equals(WhatSeekBarIsShow, "??????????????????")) {
                    mCurrentSaveGaunLian = progress;
                    if (!mCheckBoxSetFangXiang.isChecked()) {
                        setFangXiangView(mCurrentSaveGaunLian);
                    } else {
                        if (mCurrentSaveGaunLian == 0) {
                            mTvGuanLianXiangWeiFangXiang.setText("???????????????null");
                        } else {
                            mTvGuanLianXiangWeiFangXiang.setText("???????????????P" + mCurrentSaveGaunLian);
                        }
                    }
                } else if (TextUtils.equals(WhatSeekBarIsShow, "?????????")) {
                    mCurrentXiangWeiHao = progress;
                    setXiangWeiView(progress);
                } else if (TextUtils.equals(WhatSeekBarIsShow, "??????????????????")) {
                    mCurrentMinGreen = progress;
                    mTextViewMinGreenXiangWei.setText(WhatSeekBarIsShow + "???" + mCurrentMinGreen);
                } else if (TextUtils.equals(WhatSeekBarIsShow, "??????????????????1")) {
                    mCurrentMaxGreen1 = progress;
                    mTextViewMaxGreenXiangWei1.setText(WhatSeekBarIsShow + "???" + mCurrentMaxGreen1);
                } else if (TextUtils.equals(WhatSeekBarIsShow, "??????????????????2")) {
                    mCurrentMaxGreen2 = progress;
                    mTextViewMaxGreenXiangWei2.setText(WhatSeekBarIsShow + "???" + mCurrentMaxGreen2);
                } else if (TextUtils.equals(WhatSeekBarIsShow, "????????????????????????")) {
                    mCurrentDelayGreen = progress;
                    mTextViewDelayGreenXiangWei.setText(WhatSeekBarIsShow + "???" + mCurrentDelayGreen);
                } else if (TextUtils.equals(WhatSeekBarIsShow, "????????????")) {
                    mCurrentShanGreen = progress;
                    mTextViewShanGreenXiangWei.setText(WhatSeekBarIsShow + "???" + mCurrentShanGreen);
                } else if (TextUtils.equals(WhatSeekBarIsShow, "?????????")) {
                    mCurrentCheDaoNum = progress;
                    mTextViewCheDao.setText(WhatSeekBarIsShow + "???" + mCurrentCheDaoNum);
                }
                alertDialog.dismiss();
            }
        });
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                if (WhatSeekBarIsShow.equals("????????????")) {
                    if (value == 0) {
                        tv_ChooseXiangWeiCha.setText(WhatSeekBarIsShow + "???NULL");
                    } else {
                        tv_ChooseXiangWeiCha.setText(WhatSeekBarIsShow + "P???" + value);
                    }
                } else if (WhatSeekBarIsShow.equals("??????????????????")) {
                    if (value == 0) {
                        tv_ChooseXiangWeiCha.setText("????????????" + "???NULL");
                    } else {
                        tv_ChooseXiangWeiCha.setText("????????????" + "???P" + value);
                    }
                } else if (TextUtils.equals(WhatSeekBarIsShow, "?????????")
                        || TextUtils.equals(WhatSeekBarIsShow, "?????????")
                        || TextUtils.equals(WhatSeekBarIsShow, "??????????????????")
                        || TextUtils.equals(WhatSeekBarIsShow, "??????????????????1")
                        || TextUtils.equals(WhatSeekBarIsShow, "??????????????????2")
                        || TextUtils.equals(WhatSeekBarIsShow, "????????????????????????")
                        || TextUtils.equals(WhatSeekBarIsShow, "????????????")
                        || TextUtils.equals(WhatSeekBarIsShow, "?????????")
                        ) {
                    tv_ChooseXiangWeiCha.setText(WhatSeekBarIsShow + "???" + value);
                }
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
    }

    private void setFangXiangView(int index) {
        if (index == 0) {
            mTvGuanLianXiangWeiFangXiang.setText("???????????????null");
            mTextViewFangXiang.setText("??????????????????");
            mTextViewCheDao.setText("?????????????????????");
        } else {
            mTvGuanLianXiangWeiFangXiang.setText("???????????????P" + index);
            if (xiangWeiCheLiuMap.get(index) == null) {
                mTextViewFangXiang.setText("??????????????????");
            } else {
                mCurrentFangXiang = xiangWeiCheLiuMap.get(index);
                mCurrentSaveFangXiang = mCurrentFangXiang;
                mTextViewFangXiang.setText("?????????" + cheLiuInfoMap.get(mCurrentFangXiang));
            }
            if (cheDaoNumMap.get(index) == null) {
                mTextViewCheDao.setText("?????????????????????");
            } else {
                mCurrentCheDaoNum = cheDaoNumMap.get(index);
                mTextViewCheDao.setText("????????????" + mCurrentCheDaoNum);
            }
        }
    }

    public void selectTongDao(int index) {
        currentIndex = index;
        //????????????
        byte[] bytes = tongDaoPeiZhiByteArrList.get(currentIndex - 1);
        mCurrentGuanLianXiangWei = ByteUtils.bytesUInt(bytes[1]);
        mCurrentSaveGaunLian = mCurrentGuanLianXiangWei;
        mCurrentXiangWeiHao = mCurrentGuanLianXiangWei;
        mCurrentZiDongShan = ByteUtils.bytesUInt(bytes[2]);
        mCurrentType = ByteUtils.bytesUInt(bytes[3]);
        //??????view
        mTextViewTongDaoHao.setText("????????????" + currentIndex);
        setGaunLianXiangWei();
        mTextViewZiDongShan.setText("????????????" + ziDongShanStrMap.get(mCurrentZiDongShan));
        mTextViewType.setText("?????????" + leiXingMap.get(mCurrentType));
        setXiangWeiView(mCurrentGuanLianXiangWei);

        //??????view

        setFangXiangView(mCurrentGuanLianXiangWei);
    }

    private void setXiangWeiView(int guanLianXiangWei) {
        //????????????
        if (guanLianXiangWei == 0) {
            guanLianXiangWei = 1;
        }
        byte[] bytes1 = XiangWeibyteArrList.get(guanLianXiangWei - 1);
        mCurrentXiangWeiType = ByteUtils.bytesUInt(bytes1[9]);
        mCurrentXiangWeiGongNeng = ByteUtils.bytesUInt(bytes1[10]);
        mCurrentMinGreen = ByteUtils.bytesUInt(bytes1[3]);
        mCurrentMaxGreen1 = ByteUtils.bytesUInt(bytes1[5]);
        mCurrentMaxGreen2 = ByteUtils.bytesUInt(bytes1[6]);
        mCurrentDelayGreen = ByteUtils.bytesUInt(bytes1[4]);
        mCurrentShanGreen = ByteUtils.bytesUInt(bytes1[8]);
        //??????view
        mTextViewTongDaoHaoXiangWei.setText("????????????" + guanLianXiangWei);
        mTextViewTypeXiangWei.setText("???????????????" + XiangWeiTypeMap.get(mCurrentXiangWeiType));
        mTextViewGongNengXiangWei.setText("?????????????????????" + XiangWeiXuanXiangMap.get(mCurrentXiangWeiGongNeng));
        mTextViewMinGreenXiangWei.setText("?????????????????????" + mCurrentMinGreen);
        mTextViewMaxGreenXiangWei1.setText("??????????????????1???" + mCurrentMaxGreen1);
        mTextViewMaxGreenXiangWei2.setText("??????????????????2???" + mCurrentMaxGreen2);
        mTextViewDelayGreenXiangWei.setText("???????????????????????????" + mCurrentDelayGreen);
        mTextViewShanGreenXiangWei.setText("???????????????" + mCurrentShanGreen);
    }

    private void setViewpagerPage() {
        if (currentIndex >= 1 && currentIndex <= 4) {
            mViewpager.setCurrentItem(0);
        } else if (currentIndex >= 5 && currentIndex <= 8) {
            mViewpager.setCurrentItem(1);
        } else if (currentIndex >= 9 && currentIndex <= 12) {
            mViewpager.setCurrentItem(2);
        } else if (currentIndex >= 13 && currentIndex <= 16) {
            mViewpager.setCurrentItem(3);
        } else if (currentIndex >= 17 && currentIndex <= 20) {
            mViewpager.setCurrentItem(4);
        } else if (currentIndex >= 21 && currentIndex <= 24) {
            mViewpager.setCurrentItem(5);
        } else if (currentIndex >= 25 && currentIndex <= 28) {
            mViewpager.setCurrentItem(6);
        } else if (currentIndex >= 29 && currentIndex <= 32) {
            mViewpager.setCurrentItem(7);
        }
    }

    class SetFangXiangRunnable implements Runnable {

        @Override
        public void run() {
            try {
                //84FA0020
                // 00150001
                // 01010003
                // 02000001
                // 04000001
                // 051D0001
                // 07190001
                // 08040001
                // 18110001
                // 40160001
                // 41050001
                // 42060001
                // 44070001
                // 451E0001
                // 471A0001
                // 48080001
                // 58120001
                // 80170001
                // 81090001
                // 820A0001
                // 840B0001
                // 851F0001
                // 871B0001
                // 880C0001
                // 98130001
                // C0180001
                // C10D0001
                // C20E0001
                // C40F0001
                // C5200001
                // C71C0001
                // C8100001
                // D8140001
                //84FA0020
                // 00150001010100010202000104030001051D000107190001080400011811000140160001410500014206000144070001451E0001471A000148080001581200018017000181090001820A0001840B0001851F0001871B0001880C000198130001C0180001C10D0001C20E0001C40F0001C5200001C71C0001C8100001D8140001
                //81FA0020
                // 00150001010100040202000104030001051D000107190001080400011811000140160001410500014206000144070001451E0001471A000148080001581200018017000181090001820A0001840B0001851F0001871B0001880C000198130001C0180001C10D0001C20E0001C40F0001C5200001C71C0001C8100001D8140001
                UdpClientSocket udpClientSocket = new UdpClientSocket();
                String send = "81FA0020";
                String duixiang = ByteUtils.bytesToHexString(new byte[]{(byte) mCurrentSaveFangXiang, (byte) mCurrentSaveGaunLian, 0x00, (byte) mCurrentCheDaoNum});
                Log.e("SetFangXiangRunnable", "SetFangXiangRunnable--run--?????????????????????" + duixiang);
                for (int i = 0; i < tongDaoInfoByteArrList.size(); i++) {
                    byte[] bytes = tongDaoInfoByteArrList.get(i);
                    if (mCurrentSaveFangXiang == ByteUtils.bytesUInt(bytes[0])) {
                        send = send + duixiang;
                    } else {
                        send = send + ByteUtils.bytesToHexString(bytes);
                    }
                }
                Log.e("SetFangXiangRunnable", "SetFangXiangRunnable--run--???????????????????????????" + send);
                udpClientSocket.send(mIp, GbtDefine.GBT_PORT, ByteUtils.hexStringToByte(send));
                byte[] receiveByte = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                new Thread(new TongDaoRunnable(true)).start();
                if (ByteUtils.bytesUInt(receiveByte[0]) == 133 && ByteUtils.bytesUInt(receiveByte[1]) == 250) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class SetXiangWeiRunnable implements Runnable {
        private String send;

        public SetXiangWeiRunnable(String send) {
            this.send = send;
        }

        @Override
        public void run() {
            try {
                UdpClientSocket udpClientSocket = new UdpClientSocket();
                udpClientSocket.send(mIp, GbtDefine.GBT_PORT, ByteUtils.hexStringToByte(send));
                byte[] receiveByte = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                new Thread(new TongDaoRunnable(true)).start();
                if (ByteUtils.bytesUInt(receiveByte[0]) == 133 && ByteUtils.bytesUInt(receiveByte[1]) == 149) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class SetTongDaoRunnable implements Runnable {

        @Override
        public void run() {
            try {
                //84B00020
                // 01????????? 01???????????? 00????????????          02??????????????????
                //                  00?????????            01??????
                //                  02?????????            02?????????
                //                  0A????????????           03??????
                //                  04?????????            04????????????(?????????)
                //                  0C????????????
                // 02020002
                // 03030002
                // 04040003
                // 05050002
                // 06060002
                UdpClientSocket udpClientSocket = new UdpClientSocket();
                String sendStr = "81B00020";
                String duiXiangStr = ByteUtils.bytesToHexString(new byte[]{(byte) currentIndex, (byte) mCurrentGuanLianXiangWei, (byte) mCurrentZiDongShan, (byte) mCurrentType});
                Log.e("SetTongDaoRunnable", "SetTongDaoRunnable--run--??????????????????" + duiXiangStr);
                for (int i = 0; i < tongDaoPeiZhiByteArrList.size(); i++) {
                    byte[] bytes = tongDaoPeiZhiByteArrList.get(i);
                    if (ByteUtils.bytesUInt(bytes[0]) == currentIndex) {
                        sendStr = sendStr + duiXiangStr;
                    } else {
                        sendStr = sendStr + ByteUtils.bytesToHexString(bytes);
                    }
                }
                Log.e("SetTongDaoRunnable", "SetTongDaoRunnable--run--?????????????????????" + sendStr);
                //81B00020
                // 01010002
                // 02020002
                // 030300020404000305050002060600020707000208080003090900020A0A00020B0B00020C0C00030D0D00020E0E00020F0F0002101000031111000312120003131300031414000315150002161600021717000218180002191900021A1A00021B1B00021C1C00021D1D00021E1E00021F1F000220200002
                udpClientSocket.send(mIp, GbtDefine.GBT_PORT, ByteUtils.hexStringToByte(sendStr));
                byte[] receiveByte = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                Log.e("SetTongDaoRunnable", "SetTongDaoRunnable--run--?????????????????????" + ByteUtils.bytesToHexString(receiveByte));
                new Thread(new TongDaoRunnable(true)).start();
                if (ByteUtils.bytesUInt(receiveByte[0]) == 133 && ByteUtils.bytesUInt(receiveByte[1]) == 176) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class TongDaoRunnable implements Runnable {
        private boolean isSave;

        public TongDaoRunnable(boolean isSave) {
            this.isSave = isSave;
        }

        @Override
        public void run() {
            try {
                UdpClientSocket udpClientSocket = new UdpClientSocket();
                byte[] sendByte = {(byte) 0x80, (byte) 0xB0, 0x00};
                udpClientSocket.send(mIp, GbtDefine.GBT_PORT, sendByte);
                byte[] bytes = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                //84B00020
                // 01????????? 01???????????? 00????????????          02??????????????????
                //                  00?????????            01??????
                //                  02?????????            02?????????
                //                  0A????????????           03??????
                //                  04?????????            04????????????(?????????)
                //                  0C????????????
                // 02020002
                // 03030002
                // 04040003
                // 05050002
                // 06060002
                // 0707000208080003090900020A0A00020B0B00020C0C00030D0D00020E0E00020F0F0002101000031111000312120003131300031414000315150002161600021717000218180002191900021A1A00021B1B00021C1C00021D1D00021E1E00021F1F000220200002
                Log.e("TongDaoRunnable", "TongDaoRunnable--run--?????????" + ByteUtils.bytesToHexString(bytes));
                tongDaoPeiZhiByteArrList.clear();
                if (ByteUtils.bytesUInt(bytes[0]) == 132 && ByteUtils.bytesUInt(bytes[1]) == 176) {
                    for (int i = 0; i < 32; i++) {
                        tongDaoPeiZhiByteArrList.add(new byte[]{bytes[4 + i * 4], bytes[5 + i * 4], bytes[6 + i * 4], bytes[7 + i * 4]});
                    }
                    new Thread(new FangXiangRunnable(isSave)).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class FangXiangRunnable implements Runnable {
        private boolean isSave;

        public FangXiangRunnable(boolean isSave) {
            this.isSave = isSave;
        }

        @Override
        public void run() {
            try {
                UdpClientSocket udpClientSocket = new UdpClientSocket();
                byte[] sendBytes = {(byte) 0x80, (byte) 0xfa, 0x00};
                udpClientSocket.send(mIp, GbtDefine.GBT_PORT, sendBytes);
                byte[] ReceiveBytes = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                Log.e("FangXiang", "FangXiang--run--?????????" + ByteUtils.bytesToHexString(ReceiveBytes));
                if (ByteUtils.bytesUInt(ReceiveBytes[0]) == 132 && ByteUtils.bytesUInt(ReceiveBytes[1]) == 250) {
                    tongDaoInfoByteArrList.clear();
                    for (int i = 0; i < 32; i++) {
                        tongDaoInfoByteArrList.add(new byte[]{ReceiveBytes[4 + 4 * i], ReceiveBytes[5 + 4 * i], ReceiveBytes[6 + 4 * i], ReceiveBytes[7 + 4 * i]});
                    }
                    xiangWeiCheLiuMap.clear();
                    CheLiuXiangWeiMap.clear();
                    cheDaoNumMap.clear();
                    for (int i = 0; i < tongDaoInfoByteArrList.size(); i++) {
                        byte[] bytes = tongDaoInfoByteArrList.get(i);
                        xiangWeiCheLiuMap.put(ByteUtils.bytesUInt(bytes[1]), ByteUtils.bytesUInt(bytes[0]));
                        CheLiuXiangWeiMap.put(ByteUtils.bytesUInt(bytes[0]), ByteUtils.bytesUInt(bytes[1]));
                        cheDaoNumMap.put(ByteUtils.bytesUInt(bytes[1]), ByteUtils.bytesUInt(bytes[3]));
                    }
                    new Thread(new XiangWeiRunnable(isSave)).start();
                }
                //?????????1-6
                //84FA0020
                // 01010001??????
                // 02020001??????
                // 04030001??????
                // 08040001?????????
                // 41050001??????
                // 42060001??????
                // 44070001??????
                // 48080001?????????
                // 81090001??????
                // 820A0001??????
                // 840B0001??????
                // 880C0001?????????
                // C10D0001??????
                // C20E0001??????
                // C40F0001??????
                // C8100001?????????
                // 18110001???????????????
                // 58120001???????????????
                // 98130001???????????????
                // D8140001???????????????
                // 00150001?????????
                // 40160001?????????
                // 80170001?????????
                // C0180001?????????
                // 07190001?????????
                // 471A0001?????????
                // 871B0001?????????
                // C71C0001?????????
                // 051D0001?????????
                // 451E0001?????????
                // 851F0001?????????
                // C5200001?????????
                //84FA0020
                // 00150001
                // 01010003
                // 02000001
                // 04000001
                // 051D0001
                // 07190001
                // 08040001
                // 18110001
                // 40160001
                // 41050001
                // 42060001
                // 44070001
                // 451E0001
                // 471A0001
                // 48080001
                // 58120001
                // 80170001
                // 81090001
                // 820A0001
                // 840B0001
                // 851F0001
                // 871B0001
                // 880C0001
                // 98130001
                // C0180001
                // C10D0001
                // C20E0001
                // C40F0001
                // C5200001
                // C71C0001
                // C8100001
                // D8140001
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class XiangWeiRunnable implements Runnable {
        private boolean isSave;

        public XiangWeiRunnable(boolean isSave) {
            this.isSave = isSave;
        }

        @Override
        public void run() {
            try {
                //809300??????84930020
                //809400??????84940004
                //809500??????
                //84950020
                // 01????????? 00??????????????????????????? 00?????????????????? 0F?????????????????????1~255??? 08???????????????????????????1~255??? 28??????????????????1(1~255) 3C??????????????????2(1~255) 07?????????????????????????????? 02????????????(1~255) 20????????????(00100000)???????????? 01?????????????????? 00????????????
                // 02      00               00           0F                   08                      28                  3C                   07                 02               80     (10000000)????????????  01          00
                // 0300000F08283C0702200100                                                                                                                                               (01000000)????????????
                // 0400000F08283C0702200200                                                                                                                                               (00010000)????????????
                // 0500000F08283C0702200100
                // 0600000F08283C0702200100
                // 0700000F08283C0702200100
                // 0800000F08283C0702200200
                // 0900000F08283C0702200100
                // 0A00000F08283C0702200100
                // 0B00000F08283C0702200100
                // 0C00000F08283C0702200200
                // 0D00000F08283C0702200100
                // 0E00000F08283C0702200100
                // 0F00000F08283C0702200100
                // 1000000F08283C0702200200
                // 1100000F08283C0702200200
                // 1200000F08283C0702200200
                // 1300000F08283C0702200200
                // 1400000F08283C0702200200
                // 1500000F08283C0702200100
                // 1600000F08283C0702200100
                // 1700000F08283C0702200100
                // 1800000F08283C0702200100
                // 1900000F08283C0702200100
                // 1A00000F08283C0702200100
                // 1B00000F08283C0702200100
                // 1C00000F08283C0702200100
                // 1D00000F08283C0702200100
                // 1E00000F08283C0702200100
                // 1F00000F08283C0702200100
                // 2000000F08283C0702200100
                UdpClientSocket udpClientSocket = new UdpClientSocket();
                byte[] byteXiangWei = new byte[]{(byte) 0x80, (byte) 0x95, 0x00};
                udpClientSocket.send(mIp, GbtDefine.GBT_PORT, byteXiangWei);
                byte[] bytes = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                Log.e("XiangWeiRunnable", "XiangWeiRunnable--run--???????????????" + ByteUtils.bytesToHexString(bytes));
                if (ByteUtils.bytesUInt(bytes[0]) == 132 && ByteUtils.bytesUInt(bytes[1]) == 149) {
                    XiangWeibyteArrList.clear();
                    for (int i = 0; i < 32; i++) {
                        XiangWeibyteArrList.add(new byte[]{bytes[4 + 12 * i], bytes[5 + 12 * i], bytes[6 + 12 * i], bytes[7 + 12 * i], bytes[8 + 12 * i], bytes[9 + 12 * i], bytes[10 + 12 * i], bytes[11 + 12 * i], bytes[12 + 12 * i], bytes[13 + 12 * i], bytes[14 + 12 * i], bytes[15 + 12 * i]});
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isSave) {
                                mViewpager.setAdapter(new MyViewPagerAdapter(getFragmentManager()));
                            } else {
                                Intent intent = new Intent();
                                intent.putExtra(Constant.IntentKey.XIANG_WEI_VIEW, new XiangWeiInfo(tongDaoPeiZhiByteArrList, xiangWeiCheLiuMap));
                                intent.setAction(Constant.BROADCASTRECEIVER.TONGDAO);
                                getActivity().sendBroadcast(intent);
                            }
                            setViewpagerPage();
                            selectTongDao(currentIndex);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //land
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //port
        }
    }
}
