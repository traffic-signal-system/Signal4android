package com.aiton.zjb.signal.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseFragment;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.model.DengBanInfo;
import com.aiton.zjb.signal.model.XiangWeiInfo;
import com.aiton.zjb.signal.util.ByteUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class XiangWeiViewFragment extends ZjbBaseFragment implements View.OnClickListener {


    private View mInflate;
    private int[] tongDaoTvID = new int[]{
            R.id.textViewTongDao01,
            R.id.textViewTongDao02,
            R.id.textViewTongDao03,
            R.id.textViewTongDao04
    };

    private int[] xiangWeiTvID = new int[]{
            R.id.textViewXiangWei01,
            R.id.textViewXiangWei02,
            R.id.textViewXiangWei03,
            R.id.textViewXiangWei04
    };

    private int[] cheLiuTvID = new int[]{
            R.id.textViewCheLiu01,
            R.id.textViewCheLiu02,
            R.id.textViewCheLiu03,
            R.id.textViewCheLiu04
    };
    private TextView[] tongDaoTv = new TextView[4];
    private TextView[] xiangWeiTv = new TextView[4];
    private TextView[] cheLiuTv = new TextView[4];
    private TextView mTextView_dengQuBan;
    private DengBanInfo dengBanInfo;
    private Map<Integer, Integer> xiangWeiCheLiuMap = new HashMap<>();
    private Map<Integer, String> cheLiuInfoMap = new HashMap<>();
    private List<byte[]> tongDaoPeiZhiByteArrList = new ArrayList<>();
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constant.BROADCASTRECEIVER.TONGDAO:
                    XiangWeiInfo xiangWeiInfo = (XiangWeiInfo) intent.getSerializableExtra(Constant.IntentKey.XIANG_WEI_VIEW);
                    tongDaoPeiZhiByteArrList.clear();
                    tongDaoPeiZhiByteArrList.addAll(xiangWeiInfo.getTongDaoPeiZhiByteArrList());
                    xiangWeiCheLiuMap.clear();
                    xiangWeiCheLiuMap.putAll(xiangWeiInfo.getXiangWeiCheLiuMap());
                    initViews();
                    break;
            }
        }
    };

    public XiangWeiViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_xiang_wei_view, container, false);
            init();
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) mInflate.getParent();
        if (parent != null) {
            parent.removeView(mInflate);
        }
        return mInflate;
    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initData() {
        //方向
        cheLiuInfoMap.put(1, "北左");
        cheLiuInfoMap.put(2, "北直");
        cheLiuInfoMap.put(4, "北右");
        cheLiuInfoMap.put(8, "北人行");
        cheLiuInfoMap.put(65, "东左");
        cheLiuInfoMap.put(66, "东直");
        cheLiuInfoMap.put(68, "东右");
        cheLiuInfoMap.put(72, "东人行");
        cheLiuInfoMap.put(129, "南左");
        cheLiuInfoMap.put(130, "南直");
        cheLiuInfoMap.put(132, "南右");
        cheLiuInfoMap.put(136, "南人行");
        cheLiuInfoMap.put(193, "西左");
        cheLiuInfoMap.put(194, "西直");
        cheLiuInfoMap.put(196, "西右");
        cheLiuInfoMap.put(200, "西人行");
        cheLiuInfoMap.put(24, "北二次过街");
        cheLiuInfoMap.put(88, "东二次过街");
        cheLiuInfoMap.put(152, "南二次过街");
        cheLiuInfoMap.put(216, "西二次过街");
        cheLiuInfoMap.put(0, "北调头");
        cheLiuInfoMap.put(64, "东调头");
        cheLiuInfoMap.put(128, "南调头");
        cheLiuInfoMap.put(192, "西调头");
        cheLiuInfoMap.put(7, "北特殊");
        cheLiuInfoMap.put(71, "东特殊");
        cheLiuInfoMap.put(135, "南特殊");
        cheLiuInfoMap.put(199, "西特殊");
        cheLiuInfoMap.put(5, "北其他");
        cheLiuInfoMap.put(69, "东其他");
        cheLiuInfoMap.put(133, "南其他");
        cheLiuInfoMap.put(197, "西其他");
    }

    @Override
    protected void findID() {
        mTextView_dengQuBan = (TextView) mInflate.findViewById(R.id.textView_DengQuBan);
        for (int i = 0; i < tongDaoTvID.length; i++) {
            tongDaoTv[i] = (TextView) mInflate.findViewById(tongDaoTvID[i]);
            xiangWeiTv[i] = (TextView) mInflate.findViewById(xiangWeiTvID[i]);
            cheLiuTv[i] = (TextView) mInflate.findViewById(cheLiuTvID[i]);
        }
    }

    @Override
    protected void initViews() {
        mTextView_dengQuBan.setText(this.dengBanInfo.getDengBan());
        for (int i = 0; i < tongDaoTvID.length; i++) {
            tongDaoTv[i].setText("通道" + (this.dengBanInfo.getTongDaoIndex() + i));
            int xiangwei = ByteUtils.bytesUInt(tongDaoPeiZhiByteArrList.get(this.dengBanInfo.getTongDaoIndex() + i - 1)[1]);
            if (xiangwei == 0) {
                xiangWeiTv[i].setText("");
                cheLiuTv[i].setText("");
            } else {
                xiangWeiTv[i].setText("P" + xiangwei);
                cheLiuTv[i].setText(cheLiuInfoMap.get(xiangWeiCheLiuMap.get(xiangwei)));
            }
        }
    }

    @Override
    protected void setListeners() {
        mInflate.findViewById(R.id.linear_tongDao01).setOnClickListener(this);
        mInflate.findViewById(R.id.linear_tongDao02).setOnClickListener(this);
        mInflate.findViewById(R.id.linear_tongDao03).setOnClickListener(this);
        mInflate.findViewById(R.id.linear_tongDao04).setOnClickListener(this);
    }

    public void setView(DengBanInfo dengBanInfo, List<byte[]> tongDaoPeiZhiByteArrList, Map<Integer, Integer> xiangWeiCheLiuMap) {
        this.dengBanInfo = dengBanInfo;
        this.tongDaoPeiZhiByteArrList = tongDaoPeiZhiByteArrList;
        this.xiangWeiCheLiuMap = xiangWeiCheLiuMap;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.linear_tongDao01:
                intent.setAction(Constant.BROADCASTRECEIVER.XIANGWEI);
                intent.putExtra(Constant.IntentKey.TONG_DAO_HAO, dengBanInfo.getTongDaoIndex());
                getActivity().sendBroadcast(intent);
                break;
            case R.id.linear_tongDao02:
                intent.setAction(Constant.BROADCASTRECEIVER.XIANGWEI);
                intent.putExtra(Constant.IntentKey.TONG_DAO_HAO, dengBanInfo.getTongDaoIndex() + 1);
                getActivity().sendBroadcast(intent);
                break;
            case R.id.linear_tongDao03:
                intent.setAction(Constant.BROADCASTRECEIVER.XIANGWEI);
                intent.putExtra(Constant.IntentKey.TONG_DAO_HAO, dengBanInfo.getTongDaoIndex() + 2);
                getActivity().sendBroadcast(intent);
                break;
            case R.id.linear_tongDao04:
                intent.setAction(Constant.BROADCASTRECEIVER.XIANGWEI);
                intent.putExtra(Constant.IntentKey.TONG_DAO_HAO, dengBanInfo.getTongDaoIndex() + 3);
                getActivity().sendBroadcast(intent);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BROADCASTRECEIVER.TONGDAO);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(receiver);
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
