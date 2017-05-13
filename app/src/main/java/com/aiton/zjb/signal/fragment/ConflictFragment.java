package com.aiton.zjb.signal.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseFragment;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.constant.GbtDefine;
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
public class ConflictFragment extends ZjbBaseFragment implements View.OnClickListener {
    private String mIp;

    private View mInflate;
    private TextView mTv_xiangWeiChongTuHao;
    private Button mButton_saveXiangWeiCongTu;
    private List<byte[]> xiangWeiChongTuBytearrList = new ArrayList<>();
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
    private int currentChongTu = 1;
    private int[] mCurrentLightInts;
    private DiscreteSeekBar mSeekBar;
    private ImageView mImageViewdel;
    private ImageView mImageViewadd;
    private NodeListEntity mNodeListEntity;
    private boolean isOnline = false;
    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;

    public ConflictFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_fragment04, container, false);
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

    }

    @Override
    protected void findID() {
        mTv_xiangWeiChongTuHao = (TextView) mInflate.findViewById(R.id.tv_XiangWeiChongTuHao);
        mButton_saveXiangWeiCongTu = (Button) mInflate.findViewById(R.id.button_SaveXiangWeiCongTu);
        for (int i = 0; i < lightID.length; i++) {
            lightImg[i] = (ImageView) mInflate.findViewById(lightID[i]);
        }
        mTv_countdown = (TextView) mInflate.findViewById(R.id.tv_countdown);
        mSeekBar = (DiscreteSeekBar) mInflate.findViewById(R.id.seekBar);
        mImageViewdel = (ImageView) mInflate.findViewById(R.id.imageViewdel);
        mImageViewadd = (ImageView) mInflate.findViewById(R.id.imageViewadd);
    }

    @Override
    protected void initViews() {
        mSeekBar.setMin(1);
        mSeekBar.setMax(32);
    }

    @Override
    protected void setListeners() {
//        mTv_xiangWeiChongTuHao.setOnClickListener(this);
        mButton_saveXiangWeiCongTu.setOnClickListener(this);
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
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isOnline) {
            getPhaseConflict();
        } else {
            new Thread(new ConflictRunnable()).start();
        }
    }

    private void getPhaseConflict() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", mNodeListEntity.getId());
        String url = Constant.Url.GET_PHASE_CONFLICT;
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
                        final byte[] receiveByte = ByteUtils.hexStringToByte(object);
                        if (ByteUtils.bytesUInt(receiveByte[0]) == 132 && ByteUtils.bytesUInt(receiveByte[1]) == 151) {
                            xiangWeiChongTuBytearrList.clear();
                            for (int i = 0; i < 32; i++) {
                                xiangWeiChongTuBytearrList.add(new byte[]{receiveByte[4 + i * 5], receiveByte[5 + i * 5], receiveByte[6 + i * 5], receiveByte[7 + i * 5], receiveByte[8 + i * 5]});
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setCurrentChongTu();
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
                Toast.makeText(getActivity(), "获取相位冲突表失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.tv_XiangWeiChongTuHao:
//                showSeekBarDialog(1, 32, currentChongTu);
//                break;
            case R.id.button_SaveXiangWeiCongTu:
                new AlertDialog.Builder(getActivity())
                        .setMessage("是否更新到信号机")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (isOnline) {
                                    setPhaseConflict();
                                } else {
                                    new Thread(new setConflictRunnable()).start();
                                }
                            }
                        })
                        .setNegativeButton("否", null)
                        .create()
                        .show();
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
        }
    }

    private void setPhaseConflict() {
        String str2 = "";
        for (int i = 0; i < mCurrentLightInts.length; i++) {
            str2 = str2 + mCurrentLightInts[i];
        }
        String str2re = new StringBuilder(str2).reverse().toString();
        Log.e("setConflictRunnable", "setConflictRunnable--run--合成2进制数组" + str2re);
        byte b1 = ByteUtils.bit2byte(str2re.substring(0, 8));
        byte b2 = ByteUtils.bit2byte(str2re.substring(8, 16));
        byte b3 = ByteUtils.bit2byte(str2re.substring(16, 24));
        byte b4 = ByteUtils.bit2byte(str2re.substring(24, 32));
        byte[] duiXiangBytes = {(byte) currentChongTu, b1, b2, b3, b4};
        Log.e("setConflictRunnable", "setConflictRunnable--run--合成对象" + ByteUtils.bytesToHexString(duiXiangBytes));
        String send = "81970020";
        for (int i = 0; i < xiangWeiChongTuBytearrList.size(); i++) {
            if (i == currentChongTu - 1) {
                send = send + ByteUtils.bytesToHexString(duiXiangBytes);
            } else {
                send = send + ByteUtils.bytesToHexString(xiangWeiChongTuBytearrList.get(i));
            }
        }
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", mNodeListEntity.getId());
        params.put("byteString", send);
        String url = Constant.Url.SET_PHASE_CONFLICT;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--设置相位冲突返回值" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        String object = (String) noAction.getObject();
                        byte[] bytes = ByteUtils.hexStringToByte(object);
                        getPhaseConflict();
                        if (ByteUtils.bytesUInt(bytes[0]) == 133 && ByteUtils.bytesUInt(bytes[1]) == 151) {
                            Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
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
        setDialogLight(imageViewZuo, ints, 0, (i - 1) * 4);
        setDialogLight(imageViewZhi, ints, 1, (i - 1) * 4 + 1);
        setDialogLight(imageViewYou, ints, 2, (i - 1) * 4 + 2);
        setDialogLight(imageViewTeShu, ints, 3, 4 * 6 + i - 1);
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
                lightImg[currentChongTu - 1].setImageResource(R.mipmap.greenone);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void setDialogLight(final ImageView imageView, final int[] ints, final int index, int select) {
        if (select == currentChongTu - 1) {
            imageView.setImageResource(R.mipmap.greenone);
            ints[index] = 0;
        } else {
            if (mCurrentLightInts[select] == 1) {
                ints[index] = 1;
                imageView.setImageResource(R.mipmap.redone);
            } else {
                ints[index] = 0;
                imageView.setImageResource(R.mipmap.offone);
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ints[index] == 1) {
                        ints[index] = 0;
                        imageView.setImageResource(R.mipmap.offone);
                    } else {
                        ints[index] = 1;
                        imageView.setImageResource(R.mipmap.redone);
                    }
                }
            });
        }
    }

    /**
     * 设置相位冲突线程
     */
    class setConflictRunnable implements Runnable {

        @Override
        public void run() {
            try {
                //84970020
                // 0100000000
                // 0200040000
                UdpClientSocket udpClientSocket = new UdpClientSocket();
                String str2 = "";
                for (int i = 0; i < mCurrentLightInts.length; i++) {
                    str2 = str2 + mCurrentLightInts[i];
                }
                String str2re = new StringBuilder(str2).reverse().toString();
                Log.e("setConflictRunnable", "setConflictRunnable--run--合成2进制数组" + str2re);
                byte b1 = ByteUtils.bit2byte(str2re.substring(0, 8));
                byte b2 = ByteUtils.bit2byte(str2re.substring(8, 16));
                byte b3 = ByteUtils.bit2byte(str2re.substring(16, 24));
                byte b4 = ByteUtils.bit2byte(str2re.substring(24, 32));
                byte[] duiXiangBytes = {(byte) currentChongTu, b1, b2, b3, b4};
                Log.e("setConflictRunnable", "setConflictRunnable--run--合成对象" + ByteUtils.bytesToHexString(duiXiangBytes));
                String send = "81970020";
                for (int i = 0; i < xiangWeiChongTuBytearrList.size(); i++) {
                    if (i == currentChongTu - 1) {
                        send = send + ByteUtils.bytesToHexString(duiXiangBytes);
                    } else {
                        send = send + ByteUtils.bytesToHexString(xiangWeiChongTuBytearrList.get(i));
                    }
                }
                Log.e("setConflictRunnable", "setConflictRunnable--run--发送相位冲突数据" + send);
                udpClientSocket.send(mIp, GbtDefine.GBT_PORT, ByteUtils.hexStringToByte(send));
                byte[] receiveByte = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                new Thread(new ConflictRunnable()).start();
                if (ByteUtils.bytesUInt(receiveByte[0]) == 133 && ByteUtils.bytesUInt(receiveByte[1]) == 151) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ConflictRunnable implements Runnable {

        @Override
        public void run() {
            try {
                UdpClientSocket udpClientSocket = new UdpClientSocket();
                byte[] send = {(byte) 0x80, (byte) 0x97, 0x00};
                udpClientSocket.send(mIp, GbtDefine.GBT_PORT, send);
                byte[] receiveByte = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                Log.e("ConflictRunnable", "ConflictRunnable--run--相位冲突表" + ByteUtils.bytesToHexString(receiveByte));
                //228返回
                //84970020
                // 0100000000
                // 0200040000
                // 0300000000
                // 0400000000
                // 0500000010
                // 0600000000
                // 0700000000
                // 0800000000
                // 0900000000
                // 0A00000000
                // 0B00000000
                // 0C00000000
                // 0D00000000
                // 0E00000000
                // 0F00000000
                // 1000000000
                // 1100000000
                // 1200000000
                // 1300000000
                // 1400000000
                // 1500000000
                // 1600000000
                // 1700000000
                // 1800000000
                // 1900000000
                // 1A00000000
                // 1B00000000
                // 1C00000000
                // 1D00000000
                // 1E00000000
                // 1F00000000
                // 2000000000
                if (ByteUtils.bytesUInt(receiveByte[0]) == 132 && ByteUtils.bytesUInt(receiveByte[1]) == 151) {
                    xiangWeiChongTuBytearrList.clear();
                    for (int i = 0; i < 32; i++) {
                        xiangWeiChongTuBytearrList.add(new byte[]{receiveByte[4 + i * 5], receiveByte[5 + i * 5], receiveByte[6 + i * 5], receiveByte[7 + i * 5], receiveByte[8 + i * 5]});
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setCurrentChongTu();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void changLight(int i) {
        if (mCurrentLightInts[i] == 1) {
            mCurrentLightInts[i] = 0;
        } else {
            mCurrentLightInts[i] = 1;
        }
        setLight(i);
        lightImg[currentChongTu - 1].setImageResource(R.mipmap.greenone);
    }

    private void setCurrentChongTu() {
        byte[] bytes = xiangWeiChongTuBytearrList.get(currentChongTu - 1);
        String binaryStrFromByteArr = ByteUtils.getBinaryStrFromByteArr(new byte[]{bytes[1], bytes[2], bytes[3], bytes[4]});
        String light2Str = new StringBuilder(binaryStrFromByteArr).reverse().toString();
        mCurrentLightInts = ByteUtils.byteArrToIntArr(light2Str);
        for (int i = 0; i < mCurrentLightInts.length; i++) {
            setLight(i);
        }
        lightImg[currentChongTu - 1].setImageResource(R.mipmap.greenone);
        mTv_xiangWeiChongTuHao.setText("相位号：" + currentChongTu);
        mTv_countdown.setText(currentChongTu + "");
        mSeekBar.setProgress(currentChongTu);
        mImageViewdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = mSeekBar.getProgress();
                if (progress > 1) {
                    mSeekBar.setProgress(progress - 1);
                }
            }
        });
        mImageViewadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = mSeekBar.getProgress();
                if (progress < 32) {
                    mSeekBar.setProgress(progress + 1);
                }
            }
        });
        mSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                currentChongTu = value;
                setCurrentChongTu();
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
    }

    private void setLight(int i) {
        if (mCurrentLightInts[i] == 1) {
            lightImg[i].setImageResource(R.mipmap.redone);
        } else {
            lightImg[i].setImageResource(R.mipmap.offone);
        }
    }

}
