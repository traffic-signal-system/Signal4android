package com.aiton.zjb.signal.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.administrator.shane_library.shane.widget.HorizontialListView;
import com.aiton.administrator.shane_library.shane.widget.UISwitchButton;
import com.aiton.zjb.signal.PlaySurfaceView;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.model.ExtReportState;
import com.aiton.zjb.signal.model.GroupNodesList;
import com.aiton.zjb.signal.model.NoAction;
import com.aiton.zjb.signal.model.NodeListEntity;
import com.aiton.zjb.signal.model.PushInfo;
import com.aiton.zjb.signal.model.UserInfo;
import com.aiton.zjb.signal.model.VideoListInfo;
import com.aiton.zjb.signal.util.CrashUtil;
import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.RealPlayCallBack;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.MediaPlayer.PlayM4.Player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PoliceLineActivity extends ZjbBaseActivity implements SurfaceHolder.Callback, View.OnClickListener {

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
    private GroupNodesList mGroupNodesList;
    private List<NodeListEntity> mNodeListEntities;
    private HorizontialListView mListViewPoliceNode;
    private MyAdapter mAdapter;
    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;
    private boolean mSelfControl;
    private int currentNodeID = 0;
    private BufferedReader in = null;
    private String content = "";
    private PrintWriter out = null;

    private int m_iLogID = -1; // return by NET_DVR_Login_v30
    private NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = null;
    String TAG = "MainActivity";
    private int m_iStartChan = 0; // start channel no
    private int m_iChanNum = 0; // channel number
    private boolean m_bNeedDecode = true;
    private boolean m_bMultiPlay = false;
    private static PlaySurfaceView[] playView = new PlaySurfaceView[4];
    private int m_iPlayID = -1; // return by NET_DVR_RealPlay_V30
    private int m_iPlaybackID = -1; // return by NET_DVR_PlayBackByTime
    private int m_iPort = -1; // play port
    private SurfaceView m_osurfaceView = null;
    private boolean m_bStopPlayback = false;
    private boolean isSetView = false;
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
    private TextView mTv_countdown;
    private UISwitchButton mSwb_manul;
    private TextView mTextViewStatue;
    private TextView mTextViewOpen;
    private int[] buttonID = new int[]{
            R.id.buttonNorth,
            R.id.buttonEast,
            R.id.buttonSouth,
            R.id.buttonWest,
            R.id.buttonNextStep,
            R.id.buttonNextPhase,
            R.id.buttonNextDrict,
    };
    private Button[] buttonArr = new Button[7];
    private Socket mSocket;
    private String mStrIP;
    private String mStrUser;
    private String mStrPsd;
    private int mNPort;
    private TextView mTextViewNoVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashUtil crashUtil = CrashUtil.getInstance();
        crashUtil.init(this);
        setContentView(R.layout.activity_police_line);
        if (!initeSdk()) {
            this.finish();
            return;
        }
        init();
    }

    @Override
    protected void initIntent() {
        Intent intent = getIntent();
        mGroupNodesList = (GroupNodesList) intent.getSerializableExtra(Constant.IntentKey.GROUP_NODE);
        mNodeListEntities = mGroupNodesList.getObject();
    }

    @Override
    protected void initSP() {
        mACache = ACache.get(PoliceLineActivity.this, Constant.ACACHE.USER);
        mUserInfo = (UserInfo) mACache.getAsObject(Constant.ACACHE.USERINFO);
        mDevicesID = mACache.getAsString(Constant.ACACHE.DEVICES_ID);
    }

    @Override
    protected void initData() {
        showLoadingDialog("获取数据");
    }

    @Override
    protected void findID() {
        mListViewPoliceNode = (HorizontialListView) findViewById(R.id.HorizontialListView);
        for (int i = 0; i < lightID.length; i++) {
            lightImg[i] = (ImageView) findViewById(lightID[i]);
        }
        mTv_countdown = (TextView) findViewById(R.id.tv_countdown);
        m_osurfaceView = (SurfaceView) findViewById(R.id.Sur_Player);
        m_osurfaceView.getHolder().addCallback(this);
        mSwb_manul = (UISwitchButton) findViewById(R.id.swb_manul);
        mTextViewStatue = (TextView) findViewById(R.id.textViewStatue);
        mTextViewOpen = (TextView) findViewById(R.id.textViewOpen);
        for (int i = 0; i < buttonID.length; i++) {
            buttonArr[i]= (Button) findViewById(buttonID[i]);
        }
        mTextViewNoVideo = (TextView) findViewById(R.id.textViewNoVideo);
    }

    @Override
    protected void initViews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(Constant.IP, Constant.PORT);
//                    OutputStream outputStream = socket.getOutputStream();
//                    outputStream.write(("{\"userId\":\""+mUserInfo.getObject().getId()+"\"}").getBytes("utf-8"));
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            mSocket.getOutputStream(), "utf-8")), true);
                    in = new BufferedReader(new InputStreamReader(mSocket
                            .getInputStream()));
                    out.println("{\"userId\":\"" + mUserInfo.getObject().getId() + "\"}");
                    int heart = 0;
                    while (true) {
                        if (!mSocket.isClosed()) {
                            if (mSocket.isConnected()) {
                                if (!mSocket.isInputShutdown()) {
                                    if ((content = in.readLine()) != null) {
                                        try {
                                            PushInfo pushInfo = GsonUtils.parseJSON(content, PushInfo.class);
                                            String title = pushInfo.getTitle();
                                            if (TextUtils.equals(title, "extReportState")) {
                                                final ExtReportState extReportState = GsonUtils.parseJSON(pushInfo.getContext(), ExtReportState.class);
                                                if (extReportState != null) {
                                                    heart++;
                                                    if (heart > 30) {
                                                        heartHandler.sendEmptyMessage(0);
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
        openOnlineStatue(0);
        mAdapter = new MyAdapter();
        mListViewPoliceNode.setAdapter(mAdapter);
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

    @Override
    protected void setListeners() {
        mListViewPoliceNode.setOnItemClickListener(new MyOnitemClickListener());
        mSwb_manul.setOnCheckedChangeListener(new MySwitchChangeListener());
        for (int i = 0; i < buttonArr.length; i++) {
            buttonArr[i].setOnClickListener(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BROADCASTRECEIVER.SIGNAL_STATU);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        showViedio();
        try {
            mSocket.close();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
        closeOnlineStatue(-1);
        unregisterReceiver(receiver);
    }

    private void changeControlTypeOnline(int type) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", currentNodeID);
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

    private void nextControl(String controlUrl) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", currentNodeID);
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
        //direct 1北   2 东  3南  4西
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", currentNodeID);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonNorth:
                showLoadingDialog("正在切换北方向");
                changeDirect(1);
                break;
            case R.id.buttonEast:
                showLoadingDialog("正在切换东方向");
                changeDirect(2);
                break;
            case R.id.buttonSouth:
                showLoadingDialog("正在切换南方向");
                changeDirect(3);
                break;
            case R.id.buttonWest:
                showLoadingDialog("正在切换西方向");
                changeDirect(4);
                break;
            case R.id.buttonNextStep:
                showLoadingDialog("正在切换下一步");
                nextControl(Constant.Url.NEXT_STEP);
                break;
            case R.id.buttonNextPhase:
                showLoadingDialog("正在切换下相位");
                nextControl(Constant.Url.NEXT_PHASE);
                break;
            case R.id.buttonNextDrict:
                showLoadingDialog("正在切换下方向");
                nextControl(Constant.Url.NEXT_DIRECT);
                break;
        }
    }

    class MySwitchChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (!isSetView) {
                if (!b) {
                    showLoadingDialog("正在切换到自动");
                    changeControlTypeOnline(2);
                    Log.e("MySwitchChangeListener", "MySwitchChangeListener--onCheckedChanged--手动");
                } else {
                    showLoadingDialog("正在切换到手动");
                    changeControlTypeOnline(1);
                    Log.e("MySwitchChangeListener", "MySwitchChangeListener--onCheckedChanged--自动");
                }
            }
        }
    }

    class MyOnitemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            showLoadingDialog("获取数据");
            openOnlineStatue(i);
        }
    }

    private void openOnlineStatue(final int index) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("lastNodeId", currentNodeID);
        currentNodeID = mNodeListEntities.get(index).getId();
        params.put("nodeId", currentNodeID);
        String url = Constant.Url.OPEN_SIGNAL_STATUE;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                cancelLoadingDialog();
                mAdapter.notifyDataSetChanged();
                Log.e("MyNodeOnItemClic", "MyNodeOnItemClickListener--onSuccess--开启信号机状态上报" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        getVideo();
                    } else {
                        showTipDialog(noAction.getMessage());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void getVideo() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", currentNodeID);
        String url = Constant.Url.GET_VIDEOS_BY_NODEID;
        asyncHttpClient.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("NodeDetailActivity", "NodeDetailActivity--onSuccess--获取的摄像头列表" + s);
                VideoListInfo videoListInfo = GsonUtils.parseJSON(s, VideoListInfo.class);
                try {
                    if (videoListInfo.isSuccess()) {
                        List<VideoListInfo.ObjectEntity> videoListInfoObject = videoListInfo.getObject();
                        showViedio();
                        if (videoListInfoObject.size()>0){
                            mTextViewNoVideo.setVisibility(View.GONE);
                            mStrUser = videoListInfoObject.get(0).getUsername();
                            mStrPsd = videoListInfoObject.get(0).getPassword();
                            mNPort = Integer.parseInt(videoListInfoObject.get(0).getServerport());
                            mStrIP = videoListInfoObject.get(0).getServerip();
                            login();
                        }else{
                            mTextViewNoVideo.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (videoListInfo.getMessageCode() == 3) {
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

    private void closeOnlineStatue(final int itemClick) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("nodeId", currentNodeID);
        String url = Constant.Url.CLOSE_SIGNAL_STATUE;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("PhaseActivity", "PhaseActivity--onSuccess--关闭信号机状态上报" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        if (itemClick >= 0) {
//                            openOnlineStatue(itemClick);
                        }
                    } else {
                        cancelLoadingDialog();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void setView(final ExtReportState ers) {
        final List<Integer> listChannelGreen = ers.getListChannelGreenStatus();
        final List<Integer> listChannelRed = ers.getListChannelRedStatus();
        final List<Integer> listChannelYellow = ers.getListChannelYellowStatus();
        final int leftTime = ers.getStageTotalTime() - ers.getStageRunTime();
        if (listChannelYellow.contains(0)) {
            if (leftTime == 4 || leftTime == 3 || leftTime == 2 || leftTime == 1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewviewview(leftTime);
                        for (int i = 0; i < lightImg.length; i++) {
                            setLightColor(i, listChannelGreen, R.mipmap.offone);
                            setLightColor(i, listChannelRed, R.mipmap.redone);
                            setLightColor(i, listChannelYellow, R.mipmap.yellowone);
                        }
                    }
                });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refeashView(listChannelGreen, listChannelRed, listChannelYellow, leftTime);
            } else {
                refeashView(listChannelGreen, listChannelRed, listChannelYellow, leftTime);
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    viewviewview(leftTime);
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

    private void viewviewview(int leftTime) {
        mTv_countdown.setText((leftTime) + "");
        isSetView = true;
        mSwb_manul.setChecked(mSelfControl);
        if (!mSelfControl) {
            mTextViewStatue.setText("状态：自动");
            mTextViewOpen.setText("开启手动");
            for (int i = 0; i < buttonArr.length; i++) {
                buttonArr[i].setEnabled(false);
            }
        } else {
            mTextViewStatue.setText("状态：手动");
            mTextViewOpen.setText("关闭手动");
            for (int i = 0; i < buttonArr.length; i++) {
                buttonArr[i].setEnabled(true);
            }
        }
        isSetView = false;
    }

    private void setLightColor(int i, List<Integer> listChannelGreen, int img) {
        if (listChannelGreen.get(i) == 1) {
            lightImg[i].setImageResource(img);
        }
    }

    private void refeashView(final List<Integer> listChannelGreen, final List<Integer> listChannelRed, final List<Integer> listChannelYellow, final int leftTime) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    viewviewview(leftTime);
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

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNodeListEntities.size();
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
            View inflate = getLayoutInflater().inflate(R.layout.police_nodelist_item, null);
            TextView textViewNode = (TextView) inflate.findViewById(R.id.textViewNode);
            NodeListEntity nodeListEntity = mNodeListEntities.get(position);
            if (nodeListEntity.getId() == currentNodeID) {
                textViewNode.setTextColor(Color.RED);
            }
            textViewNode.setText(nodeListEntity.getDeviceName());
            return inflate;
        }
    }

    /**
     * 视频
     */
    private boolean initeSdk() {
        // init net sdk
        if (!HCNetSDK.getInstance().NET_DVR_Init()) {
            Log.e(TAG, "HCNetSDK init is failed!");
            return false;
        }
        HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/",
                true);
        return true;
    }

    private void login() {
        try {
            if (m_iLogID < 0) {
                // login on the device
                m_iLogID = loginDevice();
                if (m_iLogID < 0) {
                    Log.e(TAG, "This device logins failed!");
                    return;
                } else {
                    Log.e("MainActivity", "MainActivity--login--" + "m_iLogID=" + m_iLogID);
                }
                // get instance of exception callback and set
                ExceptionCallBack oexceptionCbf = getExceptiongCbf();
                if (oexceptionCbf == null) {
                    Log.e(TAG, "ExceptionCallBack object is failed!");
                    return;
                }

                if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(
                        oexceptionCbf)) {
                    Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
                    return;
                }
                Log.e(TAG, "Login sucess ****************************1***************************");
                showViedio();
            } else {
                // whether we have logout
                if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID)) {
                    Log.e(TAG, " NET_DVR_Logout is failed!");
                    return;
                }
                m_iLogID = -1;
            }
        } catch (Exception err) {
            Log.e(TAG, "error: " + err.toString());
        }
    }

    private void showViedio() {
        try {
//                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            if (m_iLogID < 0) {
                Log.e(TAG, "please login on device first");
                return;
            }
            if (m_bNeedDecode) {
                if (m_iChanNum > 1)// preview more than a channel
                {
                    if (!m_bMultiPlay) {
                        startMultiPreview();
                        m_bMultiPlay = true;
                    } else {
                        stopMultiPreview();
                        m_bMultiPlay = false;
                        login();
                    }
                } else // preivew a channel
                {
                    if (m_iPlayID < 0) {
                        startSinglePreview();
                    } else {
                        stopSinglePreview();
                        login();
                    }
                }
            } else {

            }
        } catch (Exception err) {
            Log.e(TAG, "error: " + err.toString());
        }
    }

    private void stopSinglePreview() {
        if (m_iPlayID < 0) {
            Log.e(TAG, "m_iPlayID < 0");
            return;
        }

        // net sdk stop preview
        if (!HCNetSDK.getInstance().NET_DVR_StopRealPlay(m_iPlayID)) {
            Log.e(TAG, "StopRealPlay is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return;
        }

        m_iPlayID = -1;
        stopSinglePlayer();
    }

    private void stopSinglePlayer() {
        Player.getInstance().stopSound();
        // player stop play
        if (!Player.getInstance().stop(m_iPort)) {
            Log.e(TAG, "stop is failed!");
            return;
        }

        if (!Player.getInstance().closeStream(m_iPort)) {
            Log.e(TAG, "closeStream is failed!");
            return;
        }
        if (!Player.getInstance().freePort(m_iPort)) {
            Log.e(TAG, "freePort is failed!" + m_iPort);
            return;
        }
        m_iPort = -1;
    }

    private void startSinglePreview() {
        if (m_iPlaybackID >= 0) {
            Log.e(TAG, "Please stop palyback first");
            return;
        }
        RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();
        if (fRealDataCallBack == null) {
            Log.e(TAG, "fRealDataCallBack object is failed!");
            return;
        }
        Log.e(TAG, "m_iStartChan:" + m_iStartChan);

        NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();
        previewInfo.lChannel = m_iStartChan;
        previewInfo.dwStreamType = 0; // substream
        previewInfo.bBlocked = 1;
//         NET_DVR_CLIENTINFO struClienInfo = new NET_DVR_CLIENTINFO();
//         struClienInfo.lChannel = m_iStartChan;
//         struClienInfo.lLinkMode = 0;
        // HCNetSDK start preview
        m_iPlayID = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(m_iLogID,
                previewInfo, fRealDataCallBack);
//         m_iPlayID = HCNetSDK.getInstance().NET_DVR_RealPlay_V30(m_iLogID,
//         struClienInfo, fRealDataCallBack, false);
        if (m_iPlayID < 0) {
            Log.e(TAG, "NET_DVR_RealPlay is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return;
        }

        Log.e(TAG, "NetSdk Play sucess ***********************3***************************");
        Log.e("MainActivity", "MainActivity--startSinglePreview--显示");
    }

    private RealPlayCallBack getRealPlayerCbf() {
        RealPlayCallBack cbf = new RealPlayCallBack() {
            public void fRealDataCallBack(int iRealHandle, int iDataType,
                                          byte[] pDataBuffer, int iDataSize) {
                // player channel 1
                PoliceLineActivity.this.processRealData(1, iDataType, pDataBuffer,
                        iDataSize, Player.STREAM_REALTIME);
            }
        };
        return cbf;
    }

    public void processRealData(int iPlayViewNo, int iDataType,
                                byte[] pDataBuffer, int iDataSize, int iStreamMode) {
        if (!m_bNeedDecode) {
            // Log.i(TAG, "iPlayViewNo:" + iPlayViewNo + ",iDataType:" +
            // iDataType + ",iDataSize:" + iDataSize);
        } else {
            if (HCNetSDK.NET_DVR_SYSHEAD == iDataType) {
                if (m_iPort >= 0) {
                    return;
                }
                m_iPort = Player.getInstance().getPort();
                if (m_iPort == -1) {
                    Log.e(TAG, "getPort is failed with: " + Player.getInstance().getLastError(m_iPort));
                    return;
                }
                Log.e(TAG, "getPort succ with: " + m_iPort);
                if (iDataSize > 0) {
                    if (!Player.getInstance().setStreamOpenMode(m_iPort,
                            iStreamMode)) // set stream mode
                    {
                        Log.e(TAG, "setStreamOpenMode failed");
                        return;
                    }
                    if (!Player.getInstance().openStream(m_iPort, pDataBuffer,
                            iDataSize, 2 * 1024 * 1024)) // open stream
                    {
                        Log.e(TAG, "openStream failed");
                        return;
                    }
                    if (!Player.getInstance().play(m_iPort,
                            m_osurfaceView.getHolder())) {
                        Log.e(TAG, "play failed");
                        return;
                    }
                    if (!Player.getInstance().playSound(m_iPort)) {
                        Log.e(TAG, "playSound failed with error code:"
                                + Player.getInstance().getLastError(m_iPort));
                        return;
                    }
                }
            } else {
                if (!Player.getInstance().inputData(m_iPort, pDataBuffer,
                        iDataSize)) {
                    // Log.e(TAG, "inputData failed with: " +
                    // Player.getInstance().getLastError(m_iPort));
                    for (int i = 0; i < 4000 && m_iPlaybackID >= 0
                            && !m_bStopPlayback; i++) {
                        if (Player.getInstance().inputData(m_iPort,
                                pDataBuffer, iDataSize)) {
                            break;

                        }

                        if (i % 100 == 0) {
                            Log.e(TAG, "inputData failed with: "
                                    + Player.getInstance()
                                    .getLastError(m_iPort) + ", i:" + i);
                        }

                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();

                        }
                    }
                }

            }
        }

    }

    private void startMultiPreview() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int i = 0;
        for (i = 0; i < 4; i++) {
            if (playView[i] == null) {
                playView[i] = new PlaySurfaceView(this);
                playView[i].setParam(metric.widthPixels);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = playView[i].getCurHeight() - (i / 2)
                        * playView[i].getCurHeight();
                params.leftMargin = (i % 2) * playView[i].getCurWidth();
                params.gravity = Gravity.BOTTOM | Gravity.LEFT;
                addContentView(playView[i], params);
            }
            playView[i].startPreview(m_iLogID, m_iStartChan + i);
        }
        m_iPlayID = playView[0].m_iPreviewHandle;
    }

    private int loginDevice() {
        int iLogID = -1;

        iLogID = loginNormalDevice();

        // iLogID = JNATest.TEST_EzvizLogin();
        // iLogID = loginEzvizDevice();

        return iLogID;
    }

    private int loginNormalDevice() {
        // get instance
        m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
        if (null == m_oNetDvrDeviceInfoV30) {
            Log.e("MainActivity", "HKNetDvrDeviceInfoV30 new is failed!");
            return -1;
        }
        // call NET_DVR_Login_v30 to login on, port 8000 as default
        int iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(mStrIP, mNPort,
                mStrUser, mStrPsd, m_oNetDvrDeviceInfoV30);
        if (iLogID < 0) {
            Log.e(TAG, "NET_DVR_Login is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            Toast.makeText(PoliceLineActivity.this, "", Toast.LENGTH_SHORT).show();
            return -1;
        }
        if (m_oNetDvrDeviceInfoV30.byChanNum > 0) {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byChanNum;
        } else if (m_oNetDvrDeviceInfoV30.byIPChanNum > 0) {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartDChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byIPChanNum
                    + m_oNetDvrDeviceInfoV30.byHighDChanNum * 256;
        }
        Log.e(TAG, "NET_DVR_Login is Successful!");

        return iLogID;
    }

    private ExceptionCallBack getExceptiongCbf() {
        ExceptionCallBack oExceptionCbf = new ExceptionCallBack() {
            public void fExceptionCallBack(int iType, int iUserID, int iHandle) {
                Log.e("MainActivity", "MainActivity--fExceptionCallBack--" + "recv exception, type:" + iType);
            }
        };
        return oExceptionCbf;
    }

    private void stopMultiPreview() {
        int i = 0;
        for (i = 0; i < 4; i++) {
            playView[i].stopPreview();
        }
        m_iPlayID = -1;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        m_osurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        Log.e(TAG, "surface is created" + m_iPort);
        if (-1 == m_iPort) {
            return;
        }
        Surface surface = holder.getSurface();
        if (true == surface.isValid()) {
            if (false == Player.getInstance()
                    .setVideoWindow(m_iPort, 0, holder)) {
                Log.e(TAG, "Player setVideoWindow failed!");
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
