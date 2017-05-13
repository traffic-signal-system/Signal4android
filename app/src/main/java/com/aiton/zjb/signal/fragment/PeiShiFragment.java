package com.aiton.zjb.signal.fragment;


import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;
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
public class PeiShiFragment extends ZjbBaseFragment implements View.OnClickListener {

    private View mInflate;
    private String mIp;
    private List<byte[]> peiShiByteArrList = new ArrayList<>();
    private ListView mListView_fangAn;
    private MyAdapter mAdapter;
    private View mLinearChoosePeiShi;
    private Button mButtonAddPeiShi;
    private TextView mTextViewFangAnHao;
    private TextView mTextViewPeiShiHao;
    private TextView mTextViewZhouQi;
    private TextView mTextViewXieTiaoXiangWei;
    private TextView mTextViewXiangWeiCha;
    private String WhatIsShow;
    private String WhatSeekBarIsShow;
    private AlertDialog mListDialog;
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
    private NodeListEntity mNodeListEntity;
    private boolean isOnline = false;
    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;
    private GroupListEntity mGroupListEntity;
    private boolean isGroupConfigure = false;

    public PeiShiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_fragment05, container, false);
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
        mListView_fangAn = (ListView) mInflate.findViewById(R.id.listView_FangAn);
        mLinearChoosePeiShi = mInflate.findViewById(R.id.linearChoosePeiShi);
        mButtonAddPeiShi = (Button) mInflate.findViewById(R.id.buttonAddPeiShi);
        mTextViewFangAnHao = (TextView) mInflate.findViewById(R.id.textViewFangAnHao);
        mTextViewPeiShiHao = (TextView) mInflate.findViewById(R.id.textViewPeiShiHao);
        mTextViewZhouQi = (TextView) mInflate.findViewById(R.id.textViewZhouQi);
        mTextViewXieTiaoXiangWei = (TextView) mInflate.findViewById(R.id.textViewXieTiaoXiangWei);
        mTextViewXiangWeiCha = (TextView) mInflate.findViewById(R.id.textViewXiangWeiCha);
    }

    @Override
    protected void initViews() {
        mAdapter = new MyAdapter();
        mListView_fangAn.setAdapter(mAdapter);
        mLinearChoosePeiShi.setVisibility(View.GONE);
    }

    @Override
    protected void setListeners() {
        mButtonAddPeiShi.setOnClickListener(this);
        mTextViewFangAnHao.setOnClickListener(this);
        mTextViewPeiShiHao.setOnClickListener(this);
        mTextViewZhouQi.setOnClickListener(this);
        mTextViewXieTiaoXiangWei.setOnClickListener(this);
        mTextViewXiangWeiCha.setOnClickListener(this);
        mInflate.findViewById(R.id.imageView_cancleAdd).setOnClickListener(this);
        mListView_fangAn.setOnItemClickListener(new MyItemClickListener());
        mListView_fangAn.setOnItemLongClickListener(new MyItemLongClickListener());
    }

    class MyItemLongClickListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("是否删除该配时号")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            if (isOnline||isGroupConfigure) {
                                deleteTimePattern(i);
                            } else {
                                new Thread(new SetPeiShiEmptyRunnable(i)).start();
                            }
                        }
                    })
                    .setNegativeButton("否", null)
                    .create()
                    .show();
            return false;
        }
    }

    private void deleteTimePattern(int index) {
        //84消息类型 C0配时表标识 00 05对象数 01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
        //01方案号 78周期时长 1E相位差 0A协调相位 01对应的阶段配时表号
        //81C10005                       01781E0A01 02781E0A02 03781E0A03 0F781E070F 10781E0A10
        //84C00005                       01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
        String setPeiShi = "81C000";
        int fangAnHao = ByteUtils.bytesUInt(peiShiByteArrList.get(index)[0]);
        int duiXiangShu = peiShiByteArrList.size();
        String duiXiang = "";
        for (int i = 0; i < peiShiByteArrList.size(); i++) {
            byte[] bytes1 = peiShiByteArrList.get(i);
            if (ByteUtils.bytesUInt(bytes1[0]) == fangAnHao) {
                duiXiang = duiXiang + "";
            } else {
                duiXiang = duiXiang + ByteUtils.bytesToHexString(bytes1);
            }
        }
        duiXiangShu = duiXiangShu - 1;
        setPeiShi = setPeiShi + ByteUtils.bytesToHexString(new byte[]{(byte) duiXiangShu}) + duiXiang;
        timePatternAsyn(setPeiShi);
    }

    /**
     * 删除配时方案
     */
    class SetPeiShiEmptyRunnable implements Runnable {

        private int index;

        public SetPeiShiEmptyRunnable(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            try {
                UdpClientSocket udpClientSocket = new UdpClientSocket();
                //84消息类型 C0配时表标识 00 05对象数 01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
                //01方案号 78周期时长 1E相位差 0A协调相位 01对应的阶段配时表号
                //81C10005                       01781E0A01 02781E0A02 03781E0A03 0F781E070F 10781E0A10
                //84C00005                       01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
                String setPeiShi = "81C000";
                int fangAnHao = ByteUtils.bytesUInt(peiShiByteArrList.get(index)[0]);
                int duiXiangShu = peiShiByteArrList.size();
                String duiXiang = "";
                for (int i = 0; i < peiShiByteArrList.size(); i++) {
                    byte[] bytes1 = peiShiByteArrList.get(i);
                    if (ByteUtils.bytesUInt(bytes1[0]) == fangAnHao) {
                        duiXiang = duiXiang + "";
                    } else {
                        duiXiang = duiXiang + ByteUtils.bytesToHexString(bytes1);
                    }
                }
                duiXiangShu = duiXiangShu - 1;
                setPeiShi = setPeiShi + ByteUtils.bytesToHexString(new byte[]{(byte) duiXiangShu}) + duiXiang;
                udpClientSocket.send(mIp, GbtDefine.GBT_PORT, ByteUtils.hexStringToByte(setPeiShi));
                byte[] bytes1 = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                setData(bytes1);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideAddPeiShi();
                    }
                });
                new Thread(new PeiShiRunnable()).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddPeiShi:
                String string = mButtonAddPeiShi.getText().toString();
                if (TextUtils.equals("添加配时方案", string)) {
                    mButtonAddPeiShi.setText("保存到信号机");
                    mLinearChoosePeiShi.setVisibility(View.VISIBLE);
                } else {
                    String reason = getReason();
                    if (TextUtils.equals(reason, "")) {
                        if (isGroupConfigure){
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("是否更新到信号机，该操作会导致群绿波失效")
                                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (isOnline||isGroupConfigure) {
                                                setTimePattern();
                                            } else {
                                                new Thread(new SetPeiShiRunnable()).start();
                                            }
                                        }
                                    })
                                    .setNegativeButton("否", null)
                                    .create()
                                    .show();
                        }else {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("是否更新到信号机")
                                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (isOnline||isGroupConfigure) {
                                                setTimePattern();
                                            } else {
                                                new Thread(new SetPeiShiRunnable()).start();
                                            }
                                        }
                                    })
                                    .setNegativeButton("否", null)
                                    .create()
                                    .show();
                        }
                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setMessage(reason)
                                .setPositiveButton("是", null)
                                .create()
                                .show();
                    }
                }
                break;
            case R.id.imageView_cancleAdd:
                hideAddPeiShi();
                break;
            case R.id.textViewFangAnHao:
                List<Integer> list = new ArrayList<>();
                List<Integer> list1 = new ArrayList<>();
                for (int i = 0; i < peiShiByteArrList.size(); i++) {
                    list.add(ByteUtils.bytesUInt(peiShiByteArrList.get(i)[0]));
                }
                for (int i = 0; i < 16; i++) {
                    if (!list.contains(i + 1)) {
                        list1.add(i + 1);
                    }
                }
                WhatIsShow = "方案号";
                showListDialog(list1);
                break;
            case R.id.textViewPeiShiHao:
                if (jieDuanHaoUsed.size()>0){
                    WhatIsShow = "配时号";
                    showListDialog(jieDuanHaoUsed);
                }else{
                    showTipDialog("暂无可用阶段配时号");
                }
                break;
            case R.id.textViewZhouQi:
                WhatSeekBarIsShow = "周期";
                showSeekBarDialog(20, 255, Integer.parseInt(mTextViewZhouQi.getText().toString().trim()));
                break;
            case R.id.textViewXieTiaoXiangWei:
                WhatSeekBarIsShow = "协调相位";
                showSeekBarDialog(1, 16, Integer.parseInt(mTextViewXieTiaoXiangWei.getText().toString().trim()));
                break;
            case R.id.textViewXiangWeiCha:
                WhatSeekBarIsShow = "相位差";
                showSeekBarDialog(0, 255, Integer.parseInt(mTextViewXiangWeiCha.getText().toString().trim()));
                break;
        }
    }

    private void setTimePattern() {
        //84消息类型 C0配时表标识 00 05对象数 01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
        //01方案号 78周期时长 1E相位差 0A协调相位 01对应的阶段配时表号
        //81C10005                       01781E0A01 02781E0A02 03781E0A03 0F781E070F 10781E0A10
        //84C00005                       01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
        String setPeiShi = "81C000";
        int fangAnHao = Integer.parseInt(mTextViewFangAnHao.getText().toString());
        int PeiShiHao = Integer.parseInt(mTextViewPeiShiHao.getText().toString());
        int ZhouQi = Integer.parseInt(mTextViewZhouQi.getText().toString());
        int XieTiaoXiangWei = Integer.parseInt(mTextViewXieTiaoXiangWei.getText().toString());
        int XiangWeiCha = Integer.parseInt(mTextViewXiangWeiCha.getText().toString());
        Log.e("PeiShiFragment", "PeiShiFragment--setTimePattern--00000");
        int duiXiangShu = peiShiByteArrList.size();
        String duiXiang = "";
        Log.e("PeiShiFragment", "PeiShiFragment--setTimePattern--111111");
        boolean isAdd = true;
        byte[] bytes = new byte[]{(byte) fangAnHao, (byte) ZhouQi, (byte) XiangWeiCha, (byte) XieTiaoXiangWei, (byte) PeiShiHao};
        for (int i = 0; i < peiShiByteArrList.size(); i++) {
            byte[] bytes1 = peiShiByteArrList.get(i);
            if (ByteUtils.bytesUInt(bytes1[0]) == fangAnHao) {
                duiXiang = duiXiang + ByteUtils.bytesToHexString(bytes);
                isAdd = false;
            } else {
                duiXiang = duiXiang + ByteUtils.bytesToHexString(bytes1);
            }
        }
        if (isAdd) {
            duiXiangShu = duiXiangShu + 1;
            duiXiang = duiXiang + ByteUtils.bytesToHexString(bytes);
        }
        Log.e("PeiShiFragment", "PeiShiFragment--setTimePattern--2222");
        setPeiShi = setPeiShi + ByteUtils.bytesToHexString(new byte[]{(byte) duiXiangShu}) + duiXiang;
        timePatternAsyn(setPeiShi);
    }

    private void timePatternAsyn(String setPeiShi) {
        Log.e("PeiShiFragment", "PeiShiFragment--timePatternAsyn--");
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("byteString", setPeiShi);
        String url = "";
        if (isOnline) {
            params.put("nodeId", mNodeListEntity.getId());
            url = Constant.Url.SET_TIME_PATTERN;
        }
        if (isGroupConfigure){
            params.put("groupId", mGroupListEntity.getId());
            url = Constant.Url.SET_GROUP_TIME_PATTERN;
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
                            hideAddPeiShi();
                            getTimePattern();
                        }else{
                            String object = (String) noAction.getObject();
                            byte[] bytes = ByteUtils.hexStringToByte(object);
                            if (ByteUtils.bytesUInt(bytes[0]) == 133 && ByteUtils.bytesUInt(bytes[1]) == 192) {
                                Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                                hideAddPeiShi();
                                getTimePattern();
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

    private String getReason() {
        if (TextUtils.equals(mTextViewFangAnHao.getText().toString(), "0")) {
            return "请选择方案号";
        } else if (TextUtils.equals(mTextViewPeiShiHao.getText().toString(), "0")) {
            return "请选择配时号";
        }
        return "";
    }

    private void showSeekBarDialog(final int min, final int max, int currentIndex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.seekbar_dialog, null);
        final AlertDialog alertDialog = builder.setView(inflate)
                .create();
        alertDialog.show();
        final TextView tv_ChooseXiangWeiCha = (TextView) inflate.findViewById(R.id.tv_ChooseXiangWeiCha);
        tv_ChooseXiangWeiCha.setText(WhatSeekBarIsShow + "：" + min);
        final DiscreteSeekBar seekBar = (DiscreteSeekBar) inflate.findViewById(R.id.seekBar);
        seekBar.setMin(min);
        seekBar.setMax(max);
        seekBar.setProgress(currentIndex);
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
                if (TextUtils.equals(WhatSeekBarIsShow, "周期")) {
                    mTextViewZhouQi.setText(seekBar.getProgress() + "");
                } else if (TextUtils.equals(WhatSeekBarIsShow, "协调相位")) {
                    mTextViewXieTiaoXiangWei.setText(seekBar.getProgress() + "");
                } else if (TextUtils.equals(WhatSeekBarIsShow, "相位差")) {
                    mTextViewXiangWeiCha.setText(seekBar.getProgress() + "");
                }
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

    private void hideAddPeiShi() {
        mLinearChoosePeiShi.setVisibility(View.GONE);
        mButtonAddPeiShi.setText("添加配时方案");
        mTextViewFangAnHao.setText("0");
        mTextViewPeiShiHao.setText("0");
        mTextViewZhouQi.setText("20");
        mTextViewXieTiaoXiangWei.setText("1");
        mTextViewXiangWeiCha.setText("1");
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
            if (list.size() > 7) {
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

    public class MyItemClick implements AdapterView.OnItemClickListener {

        private List<Integer> list;

        public MyItemClick(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Integer integer = list.get(i);
            mListDialog.dismiss();
            if (TextUtils.equals("方案号", WhatIsShow)) {
                mTextViewFangAnHao.setText(integer + "");
            } else if (TextUtils.equals("配时号", WhatIsShow)) {
                mTextViewPeiShiHao.setText(integer + "");
            }
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
            TextView textListItem = (TextView) mDialog_list_itemView.findViewById(R.id.textListItem);
            textListItem.setText(shiji.get(position) + "");
            return mDialog_list_itemView;
        }
    }

    class MyItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mButtonAddPeiShi.setText("保存到信号机");
            mLinearChoosePeiShi.setVisibility(View.VISIBLE);
            byte[] bytes = peiShiByteArrList.get(i);
            mTextViewFangAnHao.setText(ByteUtils.bytesUInt(bytes[0]) + "");
            mTextViewPeiShiHao.setText(ByteUtils.bytesUInt(bytes[4]) + "");
            mTextViewZhouQi.setText(ByteUtils.bytesUInt(bytes[1]) + "");
            mTextViewXieTiaoXiangWei.setText(ByteUtils.bytesUInt(bytes[3]) + "");
            mTextViewXiangWeiCha.setText(ByteUtils.bytesUInt(bytes[2]) + "");
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return peiShiByteArrList.size();
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
            View inflate = getLayoutInflater(getArguments()).inflate(R.layout.fangan_listitem, null);
            TextView textView_FangAnHao = (TextView) inflate.findViewById(R.id.textView_FangAnHao);
            TextView textView_PeiShiHao = (TextView) inflate.findViewById(R.id.textView_PeiShiHao);
            TextView textView_ZhouQi = (TextView) inflate.findViewById(R.id.textView_ZhouQi);
            TextView textView_XieTiaoXiangWei = (TextView) inflate.findViewById(R.id.textView_XieTiaoXiangWei);
            TextView textView_XiangWeiCha = (TextView) inflate.findViewById(R.id.textView_XiangWeiCha);
            //01方案号 78周期时长 1E相位差 0A协调相位 01对应的阶段配时表号
            if (peiShiByteArrList.size() > 0) {
                byte[] bytes = peiShiByteArrList.get(position);
                textView_FangAnHao.setText(ByteUtils.bytesUInt(bytes[0]) + "");
                textView_PeiShiHao.setText(ByteUtils.bytesUInt(bytes[4]) + "");
                textView_ZhouQi.setText(ByteUtils.bytesUInt(bytes[1]) + "");
                textView_XieTiaoXiangWei.setText(ByteUtils.bytesUInt(bytes[3]) + "");
                textView_XiangWeiCha.setText(ByteUtils.bytesUInt(bytes[2]) + "");
            }
            return inflate;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isOnline || isGroupConfigure) {
            getTimePattern();
            getStagePattern();
        } else {
            new Thread(new PeiShiRunnable()).start();
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
                Log.e("PeiShiFragment", "PeiShiFragment--onSuccess--群时段返回值" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        String object = (String) noAction.getObject();
                        Log.e("PeiShiFragment", "PeiShiFragment--onSuccess--群时段" + object);
                        LogUtil.LogShitou("时段",object);
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
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "获取阶段配时表失败", Toast.LENGTH_SHORT).show();
            }
        });
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
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--配时表返回值" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        String object = (String) noAction.getObject();
                        Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--配时表" + object);
                        final byte[] bytes = ByteUtils.hexStringToByte(object);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                timePattren(bytes);
                            }
                        }).start();
                    } else {
                        if (noAction.getMessageCode() == 3) {
                            reLogin();
                        }else{
                            //初始化数据
                            String receive = "84C0000";
                            Log.e("JieDuanFragment", "JieDuanFragment--onSuccess--阶段配时长度"+receive.length());
                            timePattren(ByteUtils.hexStringToByte(receive));
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "获取配时方案表失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class SetPeiShiRunnable implements Runnable {

        @Override
        public void run() {
            try {
                UdpClientSocket udpClientSocket = new UdpClientSocket();
                //84消息类型 C0配时表标识 00 05对象数 01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
                //01方案号 78周期时长 1E相位差 0A协调相位 01对应的阶段配时表号
                //81C10005                       01781E0A01 02781E0A02 03781E0A03 0F781E070F 10781E0A10
                //84C00005                       01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
                String setPeiShi = "81C000";
                int fangAnHao = Integer.parseInt(mTextViewFangAnHao.getText().toString());
                int PeiShiHao = Integer.parseInt(mTextViewPeiShiHao.getText().toString());
                int ZhouQi = Integer.parseInt(mTextViewZhouQi.getText().toString());
                int XieTiaoXiangWei = Integer.parseInt(mTextViewXieTiaoXiangWei.getText().toString());
                int XiangWeiCha = Integer.parseInt(mTextViewXiangWeiCha.getText().toString());
                int duiXiangShu = peiShiByteArrList.size();
                String duiXiang = "";
                boolean isAdd = true;
                byte[] bytes = new byte[]{(byte) fangAnHao, (byte) ZhouQi, (byte) XiangWeiCha, (byte) XieTiaoXiangWei, (byte) PeiShiHao};
                for (int i = 0; i < peiShiByteArrList.size(); i++) {
                    byte[] bytes1 = peiShiByteArrList.get(i);
                    if (ByteUtils.bytesUInt(bytes1[0]) == fangAnHao) {
                        duiXiang = duiXiang + ByteUtils.bytesToHexString(bytes);
                        isAdd = false;
                    } else {
                        duiXiang = duiXiang + ByteUtils.bytesToHexString(bytes1);
                    }
                }
                if (isAdd) {
                    duiXiangShu = duiXiangShu + 1;
                    duiXiang = duiXiang + ByteUtils.bytesToHexString(bytes);
                }
                setPeiShi = setPeiShi + ByteUtils.bytesToHexString(new byte[]{(byte) duiXiangShu}) + duiXiang;
                udpClientSocket.send(mIp, GbtDefine.GBT_PORT, ByteUtils.hexStringToByte(setPeiShi));
                byte[] bytes1 = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                setData(bytes1);
                new Thread(new PeiShiRunnable()).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class JieDuanRunNable implements Runnable {

        @Override
        public void run() {
            try {
                UdpClientSocket udpClientSocket = new UdpClientSocket();
                byte[] bytessend = new byte[]{(byte) 0x80, (byte) 0xC1, (byte) 0x00};
                udpClientSocket.send(mIp, GbtDefine.GBT_PORT, bytessend);
                byte[] bytes = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
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
                stagePattern(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void stagePattern(byte[] bytes) {
        if ((ByteUtils.bytesUInt(bytes[0]) == 132 && ByteUtils.bytesUInt(bytes[1]) == 193)||(ByteUtils.bytesUInt(bytes[0]) == 129 && ByteUtils.bytesUInt(bytes[1]) == 193)) {
            //84C1001010
            // 01阶段方案号 01阶段号（配时号） 0000 8西（1000西人行） 0南 0东 3北（0011北左北直） 0F绿灯（15秒） 03黄灯（3秒） 00红灯 01阶段感应参数
            // 0102000000380F030001
            // 0103000003800F030001
            // 0104000038000F030001
            // 01000000000000000000
            // 01000000000000000000010000000000000000000100
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
        }
    }

    class PeiShiRunnable implements Runnable {

        @Override
        public void run() {
            try {
                UdpClientSocket mUdpClientSocket = new UdpClientSocket();
                mUdpClientSocket.send(mIp, GbtDefine.GBT_PORT, GbtDefine.GET_PATTERN);
                byte[] bytes = mUdpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                Log.e("PeiShiRunnable", "PeiShiRunnable--run--配时方案"+ByteUtils.bytesToHexString(bytes));
                timePattren(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void timePattren(byte[] bytes) {
        if ((ByteUtils.bytesUInt(bytes[0]) == 132 && ByteUtils.bytesUInt(bytes[1]) == 192)||(ByteUtils.bytesUInt(bytes[0]) == 129 && ByteUtils.bytesUInt(bytes[1]) == 192)) {
            //84消息类型 C0配时表标识 00 05对象数 01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
            //01方案号 78周期时长 1E相位差 0A协调相位 01对应的阶段配时表号
            //84C00005                       01781E0A02 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
            peiShiByteArrList.clear();
            int peiShiNum = ByteUtils.bytesUInt(bytes[3]);
            for (int i = 0; i < peiShiNum; i++) {
                peiShiByteArrList.add(new byte[]{bytes[4 + i * 5], bytes[5 + i * 5], bytes[6 + i * 5], bytes[7 + i * 5], bytes[8 + i * 5]});
                Log.e("PeiShiFragment", "PeiShiFragment--timePattren--"+ByteUtils.bytesToHexString(new byte[]{bytes[4 + i * 5], bytes[5 + i * 5], bytes[6 + i * 5], bytes[7 + i * 5], bytes[8 + i * 5]}));
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void setData(byte[] bytes) {
        if (ByteUtils.bytesUInt(bytes[0]) == 133 && ByteUtils.bytesUInt(bytes[1]) == 192) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideAddPeiShi();
                    Toast.makeText(getActivity(), "设置配时方案成功", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
