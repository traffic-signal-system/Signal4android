package com.aiton.zjb.signal.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseFragment;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.constant.GbtDefine;
import com.aiton.zjb.signal.model.ExtReportState;
import com.aiton.zjb.signal.model.NoAction;
import com.aiton.zjb.signal.model.NodeListEntity;
import com.aiton.zjb.signal.model.PushInfo;
import com.aiton.zjb.signal.model.UserInfo;
import com.aiton.zjb.signal.util.ByteUtils;
import com.aiton.zjb.signal.util.ExtReportUtils;
import com.aiton.zjb.signal.util.UdpClientSocket;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManaulFragment extends ZjbBaseFragment implements View.OnClickListener {

    private View mInflate;
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
    private int[] RadioID = new int[]{
            R.id.rb_munual,
            R.id.rb_self
    };
    private int[] controlID = new int[]{
            R.id.btnNextStep,
            R.id.btnNextPhase,
            R.id.btnNextDirec,
            R.id.btnNorth,
            R.id.btnEast,
            R.id.btnSouth,
            R.id.btnWest
    };
    private ImageView[] lightImg = new ImageView[32];
    private RadioButton[] contorlRadio = new RadioButton[2];
    private Button[] contorlBtn = new Button[7];
    private TextView mTv_countdown;
    private String mIp;
    private NodeListEntity mTscNode;
    private Thread thread;
    private boolean mSelfControl;
    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;
    private NodeListEntity mNodeListEntity;
    private boolean isOnline = false;
    private BufferedReader in = null;
    private String content = "";
    private PrintWriter out = null;
    private Handler heartHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            heartBeat();
        }
    };
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constant.BROADCASTRECEIVER.SIGNAL_STATU:
//                    String s = intent.getStringExtra(Constant.IntentKey.SIGNAL_STATUE);
//                    Log.i("ManaulFragment", "ManaulFragment--onReceive--状态上报过来的广播" + s);
//                    final ExtReportState extReportState = GsonUtils.parseJSON(s, ExtReportState.class);
//                    if (extReportState != null) {
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (TextUtils.equals(extReportState.getControlModel(), "手动")) {
//                                    mSelfControl = true;
//                                } else {
//                                    mSelfControl = false;
//                                }
//                                setView(extReportState);
//                            }
//                        }).start();
//                    }
                    break;
            }
        }
    };

    public ManaulFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mInflate == null) {
            if (Constant.HengShuPing != getResources().getConfiguration().orientation) {
                mInflate = inflater.inflate(R.layout.fragment_manaulland, container, false);
            } else {
                mInflate = inflater.inflate(R.layout.fragment_manaul, container, false);
            }
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
        mTscNode = new NodeListEntity(1, 1, "", "随便", mIp, GbtDefine.GBT_PORT, "1.0", 1, 1, 0, 0, 0);
    }

    @Override
    protected void findID() {
        for (int i = 0; i < lightID.length; i++) {
            lightImg[i] = (ImageView) mInflate.findViewById(lightID[i]);
        }
        mTv_countdown = (TextView) mInflate.findViewById(R.id.tv_countdown);
        contorlRadio[0] = (RadioButton) mInflate.findViewById(RadioID[0]);
        contorlRadio[1] = (RadioButton) mInflate.findViewById(RadioID[1]);
        for (int i = 0; i < 7; i++) {
            contorlBtn[i] = (Button) mInflate.findViewById(controlID[i]);
        }
    }

    @Override
    protected void initViews() {
        if (!isOnline) {
            thread = new Thread(new GetManaulRunnable());
            thread.start();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket socket = new Socket(Constant.IP, Constant.PORT);
//                    OutputStream outputStream = socket.getOutputStream();
//                    outputStream.write(("{\"userId\":\""+mUserInfo.getObject().getId()+"\"}").getBytes("utf-8"));
                        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                                socket.getOutputStream(), "utf-8")), true);
                        in = new BufferedReader(new InputStreamReader(socket
                                .getInputStream()));
                        out.println("{\"userId\":\"" + mUserInfo.getObject().getId() + "\"}");
                        int heart = 0;
                        while (true) {
                            if (!socket.isClosed()) {
                                if (socket.isConnected()) {
                                    if (!socket.isInputShutdown()) {
                                        if ((content = in.readLine()) != null) {
                                            try {
                                                PushInfo pushInfo = GsonUtils.parseJSON(content, PushInfo.class);
                                                String title = pushInfo.getTitle();
                                                if (TextUtils.equals(title, "extReportState")) {
                                                    final ExtReportState extReportState = GsonUtils.parseJSON(pushInfo.getContext(), ExtReportState.class);
                                                    if (extReportState != null) {
                                                        heart++;
                                                        if (heart > 30) {
                                                            Log.e("PoliceLineActivity", "PoliceLineActivity--run--" + content);
                                                            heartHandler.sendEmptyMessage(0);
                                                            Log.e("PoliceLineActivity", "PoliceLineActivity--run--来了这里");
                                                            heart = 0;
                                                        }
                                                        if (TextUtils.equals(extReportState.getControlModel(), "手动")) {
                                                            mSelfControl = true;
                                                        } else {
                                                            mSelfControl = false;
                                                        }
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                setView(extReportState);
                                                            }
                                                        }).start();
                                                    }
                                                }
                                            } catch (Exception e) {
                                                Log.e("PoliceLineActivity", "PoliceLineActivity--run--数据异常");
                                            }
                                        } else {

                                        }
                                    }
                                }
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            openOnlineStatue();
        }
    }

    @Override
    protected void setListeners() {
        contorlRadio[0].setOnClickListener(this);
        contorlRadio[1].setOnClickListener(this);
        for (int i = 0; i < contorlBtn.length; i++) {
            contorlBtn[i].setOnClickListener(this);
        }
    }

    private void heartBeat() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String url = Constant.Url.HEART_BEAT;
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("PushIntentServer", "PushIntentServer--onSuccess--心跳返回" + s);
                try {

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("PushIntentServer", "PushIntentServer--onFailure--失败");
            }
        });
    }

    private void openOnlineStatue() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", mNodeListEntity.getId());
        String url = Constant.Url.OPEN_SIGNAL_STATUE;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MyNodeOnItemClic", "MyNodeOnItemClickListener--onSuccess--开启信号机状态上报" + s);
                try {

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BROADCASTRECEIVER.SIGNAL_STATU);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    /**
     * 获取信号机状态上报
     */
    class GetManaulRunnable implements Runnable {

        @Override
        public void run() {
            try {
                UdpClientSocket ucs = new UdpClientSocket();
                ucs.send(mTscNode.getIpAddress(), mTscNode.getPort(), GbtDefine.REPORT_TSC_STATUS);
                while (true) {
                    try {
                        byte[] bytes = ucs.receiveByte(mTscNode.getIpAddress(), mTscNode.getPort());
                        mSelfControl = ExtReportUtils.isShouDongControl(bytes[3]);
                        String s1 = ByteUtils.bytesToHexString(bytes);
                        Log.i("run ", "run  状态上报" + s1);
                        ExtReportState ers = ExtReportUtils.byte2ReportState(bytes);
                        setView(ers);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception exce) {
                exce.printStackTrace();
            }
        }
    }

    private void setView(final ExtReportState ers) {
        final List<Integer> listChannelGreen = ers.getListChannelGreenStatus();
        final List<Integer> listChannelRed = ers.getListChannelRedStatus();
        final List<Integer> listChannelYellow = ers.getListChannelYellowStatus();
        final int leftTime = ers.getStageTotalTime() - ers.getStageRunTime();
        if (listChannelYellow.contains(0)) {
            if (leftTime == 4 || leftTime == 3 || leftTime == 2 || leftTime == 1) {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTv_countdown.setText((leftTime) + "");
                            if (mSelfControl) {
                                contorlRadio[0].setChecked(true);
                            } else {
                                contorlRadio[1].setChecked(true);
                            }
                            for (int i = 0; i < lightImg.length; i++) {
                                setLightColor(i, listChannelGreen, R.mipmap.offone);
                                setLightColor(i, listChannelRed, R.mipmap.redone);
                                setLightColor(i, listChannelYellow, R.mipmap.yellowone);
                            }
                        }
                    });
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refeashView(listChannelGreen, listChannelRed, listChannelYellow, leftTime);
            } else {
                refeashView(listChannelGreen, listChannelRed, listChannelYellow, leftTime);
            }
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTv_countdown.setText((leftTime) + "");
                    if (mSelfControl) {
                        contorlRadio[0].setChecked(true);
                    } else {
                        contorlRadio[1].setChecked(true);
                    }
                    for (int i = 0; i < lightImg.length; i++) {
                        setLightColor(i, listChannelGreen, R.mipmap.greenone);
                        setLightColor(i, listChannelRed, R.mipmap.redone);
                        setLightColor(i, listChannelYellow, R.mipmap.offone);
                    }
                }
            });
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            refeashView(listChannelGreen, listChannelRed, listChannelYellow, leftTime);
        }
    }

    private void refeashView(final List<Integer> listChannelGreen, final List<Integer> listChannelRed, final List<Integer> listChannelYellow, final int leftTime) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTv_countdown.setText((leftTime) + "");
                    if (mSelfControl) {
                        contorlRadio[0].setChecked(true);
                    } else {
                        contorlRadio[1].setChecked(true);
                    }
                    for (int i = 0; i < lightImg.length; i++) {
                        setLightColor(i, listChannelGreen, R.mipmap.greenone);
                        setLightColor(i, listChannelRed, R.mipmap.redone);
                        setLightColor(i, listChannelYellow, R.mipmap.yellowone);
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    private void setLightColor(int i, List<Integer> listChannelGreen, int img) {
        if (listChannelGreen.get(i) == 1) {
            lightImg[i].setImageResource(img);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == RadioID[0]) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            AlertDialog alertDialog = builder.setMessage("是否切换到手动控制")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (isOnline) {
                                showLoadingDialog("正在切换到手动");
                                changeControlTypeOnline(1);
                            } else {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            UdpClientSocket udpClientSocket = new UdpClientSocket();
                                            udpClientSocket.send(mIp, GbtDefine.GBT_PORT, GbtDefine.CTRL_MUNUAL);
                                            byte[] receiveByte = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                                            Log.e("ManaulFragment", "ManaulFragment--run--手控返回值" + ByteUtils.bytesToHexString(receiveByte));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                            contorlRadio[0].setChecked(true);
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create();
            alertDialog.show();
        } else if (id == RadioID[1]) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            AlertDialog alertDialog = builder.setMessage("是否切换到自动控制")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (isOnline) {
                                showLoadingDialog("正在切换到自动");
                                changeControlTypeOnline(2);
                            } else {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            UdpClientSocket udpClientSocket = new UdpClientSocket();
                                            udpClientSocket.send(mIp, GbtDefine.GBT_PORT, GbtDefine.CTRL_SELF);
                                            byte[] bytes = udpClientSocket.receiveByte(mIp, GbtDefine.GBT_PORT);
                                            Log.e("ManaulFragment", "ManaulFragment--run--自动控制返回值" + ByteUtils.bytesToHexString(bytes));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                            contorlRadio[1].setChecked(true);
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create();
            alertDialog.show();
        } else if (id == controlID[0]) {
            if (isOnline) {
                if (mSelfControl) {
                    showLoadingDialog("正在切换下一步");
                    nextControl(Constant.Url.NEXT_STEP);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("请先切换到手动控制")
                            .setPositiveButton("确认", null)
                            .create()
                            .show();
                }
            } else {
                contorl(GbtDefine.CTRL_NEXTSTEP);
            }
        } else if (id == controlID[1]) {
            if (isOnline) {
                if (mSelfControl) {
                    showLoadingDialog("正在切换下相位");
                    nextControl(Constant.Url.NEXT_PHASE);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("请先切换到手动控制")
                            .setPositiveButton("确认", null)
                            .create()
                            .show();
                }
            } else {
                contorl(GbtDefine.CTRL_NEXTPHASE);
            }
        } else if (id == controlID[2]) {
            if (isOnline) {
                if (mSelfControl) {
                    showLoadingDialog("正在切换下方向");
                    nextControl(Constant.Url.NEXT_DIRECT);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("请先切换到手动控制")
                            .setPositiveButton("确认", null)
                            .create()
                            .show();
                }
            } else {
                contorl(GbtDefine.CTRL_NEXTDIREC);
            }
        } else if (id == controlID[3]) {
            if (isOnline) {
                if (mSelfControl) {
                    showLoadingDialog("正在切换北方向");
                    changeDirect(1);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("请先切换到手动控制")
                            .setPositiveButton("确认", null)
                            .create()
                            .show();
                }
            } else {
                contorl(GbtDefine.CTRL_NORTH);
            }
        } else if (id == controlID[4]) {
            if (isOnline) {
                if (mSelfControl) {
                    showLoadingDialog("正在切换东方向");
                    changeDirect(2);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("请先切换到手动控制")
                            .setPositiveButton("确认", null)
                            .create()
                            .show();
                }
            } else {
                contorl(GbtDefine.CTRL_EAST);
            }
        } else if (id == controlID[5]) {
            if (isOnline) {
                if (mSelfControl) {
                    showLoadingDialog("正在切换南方向");
                    changeDirect(3);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("请先切换到手动控制")
                            .setPositiveButton("确认", null)
                            .create()
                            .show();
                }
            } else {
                contorl(GbtDefine.CTRL_SOUTH);
            }
        } else if (id == controlID[6]) {
            if (isOnline) {
                if (mSelfControl) {
                    showLoadingDialog("正在切换西方向");
                    changeDirect(4);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("请先切换到手动控制")
                            .setPositiveButton("确认", null)
                            .create()
                            .show();
                }
            } else {
                contorl(GbtDefine.CTRL_WEST);
            }
        }
    }

    private void nextControl(String controlUrl) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", mNodeListEntity.getId());
        String url = controlUrl;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                cancelLoadingDialog();
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (!noAction.isSuccess()) {
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

    private void changeDirect(int direct) {
        if (mSelfControl) {
            //direct 1北   2 东  3南  4西
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("userId", mUserInfo.getObject().getId());
            params.put("deviceId", mDevicesID);
            params.put("nodeId", mNodeListEntity.getId());
            params.put("direct", direct);
            String url = Constant.Url.CHANGE_DIRECT;
            asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    Log.e("ManaulFragment", "ManaulFragment--onSuccess--切换方向返回值" + s);
                    cancelLoadingDialog();
                    try {
                        NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                        if (!noAction.isSuccess()) {
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
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("请先切换到手动控制")
                    .setPositiveButton("确认", null)
                    .create()
                    .show();
        }
    }

    private void changeControlTypeOnline(int type) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", mNodeListEntity.getId());
        params.put("type", type);
        String url = Constant.Url.CHANGE_CONTROL_TYPE;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("ManaulFragment", "ManaulFragment--onSuccess--切换控制状态返回值" + s);
                cancelLoadingDialog();
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (!noAction.isSuccess()) {
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

    private void contorl(final byte[] bytes) {
        if (mSelfControl) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        UdpClientSocket udpClientSocket = new UdpClientSocket();
                        udpClientSocket.send(mIp, GbtDefine.GBT_PORT, bytes);
                        String s1 = ByteUtils.bytesToHexString(bytes);
                        Log.e("run ", "run 操作返回值" + s1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("请先切换到手动控制")
                    .setPositiveButton("确认", null)
                    .create()
                    .show();
        }
    }
}
