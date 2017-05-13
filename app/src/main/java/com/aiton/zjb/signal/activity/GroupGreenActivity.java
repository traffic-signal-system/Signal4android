package com.aiton.zjb.signal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.model.GroupListEntity;
import com.aiton.zjb.signal.model.GroupNodesList;
import com.aiton.zjb.signal.model.NoAction;
import com.aiton.zjb.signal.model.NodeListEntity;
import com.aiton.zjb.signal.model.UserInfo;
import com.aiton.zjb.signal.util.ByteUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class GroupGreenActivity extends ZjbBaseActivity implements View.OnClickListener, View.OnLongClickListener {

    private ListView mListViewGroupGreen;
    private GroupNodesList mGroupNodesList;
    private List<NodeListEntity> mNodeListEntities;
    private int[] mIntsPhaseDiff;
    private TextView mTextViewCycle;
    private int cilcle = 0;
    private ImageView mImageViewDel;
    private ImageView mImageViewAdd;
    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;
    private int mGroupID;
    private int asynCount = 0;
    private int asynSetCount = 0;
    private MyAdapter mAdapter;
    private List<byte[]> peiShiByteArrList = new ArrayList<>();
    boolean set = false;
    private GroupListEntity mGroupListEntity;
    private TextView mTextViewGroupName;
    private TextView mTextViewGroupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_green);
        init();
    }

    @Override
    protected void initIntent() {
        Intent intent = getIntent();
        mGroupNodesList = (GroupNodesList) intent.getSerializableExtra(Constant.IntentKey.GROUP_NODE);
        mGroupListEntity = (GroupListEntity) intent.getSerializableExtra(Constant.IntentKey.GROUP);
        mGroupID = mGroupListEntity.getId();
    }

    @Override
    protected void initSP() {
        mACache = ACache.get(this, Constant.ACACHE.USER);
        mUserInfo = (UserInfo) mACache.getAsObject(Constant.ACACHE.USERINFO);
        mDevicesID = mACache.getAsString(Constant.ACACHE.DEVICES_ID);
    }

    @Override
    protected void initData() {
        mNodeListEntities = mGroupNodesList.getObject();
        mIntsPhaseDiff = new int[mNodeListEntities.size()];
    }

    @Override
    protected void findID() {
        mListViewGroupGreen = (ListView) findViewById(R.id.listViewGroupGreen);
        mTextViewCycle = (TextView) findViewById(R.id.textViewCycle);
        mImageViewDel = (ImageView) findViewById(R.id.imageViewDel);
        mImageViewAdd = (ImageView) findViewById(R.id.imageViewAdd);
        mTextViewGroupName = (TextView) findViewById(R.id.textViewGroupName);
        mTextViewGroupID = (TextView) findViewById(R.id.textViewGroupID);
    }

    @Override
    protected void initViews() {
        mAdapter = new MyAdapter();
        mListViewGroupGreen.setAdapter(mAdapter);
        mTextViewCycle.setText(cilcle + "");
        mTextViewGroupName.setText("群名称：" + mGroupListEntity.getGroupName());
        mTextViewGroupID.setText("群ID：" + mGroupListEntity.getId() + "");
    }

    @Override
    protected void setListeners() {
        findViewById(R.id.imageViewBack).setOnClickListener(this);
        findViewById(R.id.textViewComfrim).setOnClickListener(this);
        mImageViewDel.setOnClickListener(this);
        mImageViewAdd.setOnClickListener(this);
        mImageViewDel.setOnLongClickListener(this);
        mImageViewAdd.setOnLongClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        asynCount = 0;
        set = false;
        showLoadingDialog("加载数据");
        getData();
    }

    private void getData() {
        getGroupTimePattern();
        for (int i = 0; i < mNodeListEntities.size(); i++) {
            mIntsPhaseDiff[i] = 0;
            getTimePattern(i);
        }
    }

    private void getGroupTimePattern() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        String url = Constant.Url.GET_GROUP_TIME_PATTERN;
        params.put("groupId", mGroupID);
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
                        if (ByteUtils.bytesUInt(bytes[0]) != 134) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    timeGroupPattren(bytes);
                                }
                            }).start();
                            refreash(set);
                        }
                    } else {
                        if (noAction.getMessageCode() == 3) {
                            reLogin();
                        } else {
//                            //初始化数据
//                            String receive = "84C0000";
//                            Log.e("JieDuanFragment", "JieDuanFragment--onSuccess--阶段配时长度" + receive.length());
//                            timeGroupPattren(ByteUtils.hexStringToByte(receive));
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(GroupGreenActivity.this, "获取配时方案表失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreash(boolean set) {
        asynCount++;
        if (asynCount > mNodeListEntities.size()) {
            mAdapter.notifyDataSetChanged();
            cancelLoadingDialog();
            if (set) {
                showTipDialog("设置成功");
            }
        }
    }

    private void getTimePattern(final int position) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("nodeId", mNodeListEntities.get(position).getId());
        String url = Constant.Url.GET_TIME_PATTERN;
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
                        if (ByteUtils.bytesUInt(bytes[0]) != 134) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    timePattren(bytes, position);
                                }
                            }).start();
                            refreash(set);
                        }
                    } else {
                        if (noAction.getMessageCode() == 3) {
                            reLogin();
                        } else {
//                            //初始化数据
//                            String receive = "84C0000";
//                            Log.e("JieDuanFragment", "JieDuanFragment--onSuccess--阶段配时长度" + receive.length());
//                            timePattren(ByteUtils.hexStringToByte(receive), position);
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(GroupGreenActivity.this, "获取配时方案表失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void timePattren(byte[] bytes, int position) {
        if ((ByteUtils.bytesUInt(bytes[0]) == 132 && ByteUtils.bytesUInt(bytes[1]) == 192) || (ByteUtils.bytesUInt(bytes[0]) == 129 && ByteUtils.bytesUInt(bytes[1]) == 192)) {
            //84消息类型 C0配时表标识 00 05对象数 01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
            //01方案号 78周期时长 1E相位差 0A协调相位 01对应的阶段配时表号
            //84C00005                       01781E0A02 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
            if (bytes.length > 5) {
                mIntsPhaseDiff[position] = ByteUtils.bytesUInt(bytes[6]);
            }
        }
    }

    private void timeGroupPattren(byte[] bytes) {
        if ((ByteUtils.bytesUInt(bytes[0]) == 132 && ByteUtils.bytesUInt(bytes[1]) == 192) || (ByteUtils.bytesUInt(bytes[0]) == 129 && ByteUtils.bytesUInt(bytes[1]) == 192)) {
            //84消息类型 C0配时表标识 00 05对象数 01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
            //01方案号 78周期时长 1E相位差 0A协调相位 01对应的阶段配时表号
            //84C00005
            peiShiByteArrList.clear();
            int peiShiNum = ByteUtils.bytesUInt(bytes[3]);
            for (int i = 0; i < peiShiNum; i++) {
                peiShiByteArrList.add(new byte[]{bytes[4 + i * 5], bytes[5 + i * 5], bytes[6 + i * 5], bytes[7 + i * 5], bytes[8 + i * 5]});
            }
            if (bytes.length > 5) {
                cilcle = ByteUtils.bytesUInt(bytes[5]);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextViewCycle.setText(cilcle + "");
                    }
                });
            }
        }
    }

    private void setTimePattern(int position) {
        //84消息类型 C0配时表标识 00 05对象数 01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
        //01方案号 78周期时长 1E相位差 0A协调相位 01对应的阶段配时表号
        //81C10005                       01781E0A01 02781E0A02 03781E0A03 0F781E070F 10781E0A10
        //84C00005                       01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
        //84C00005                       01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
        //81C00005                       918C1E0A01 028C1E0A02 038C1E0A03 0F8C1E0A0F 108C1E0A10
        String setPeiShi = "81C000" + ByteUtils.bytesToHexString(new byte[]{(byte) peiShiByteArrList.size()});
        for (int i = 0; i < peiShiByteArrList.size(); i++) {
            byte[] bytes1 = peiShiByteArrList.get(i);
            bytes1[1] = (byte) cilcle;
            if (position == 0) {
                bytes1[2] = (byte) 0x00;
            } else {
                bytes1[2] = (byte) mIntsPhaseDiff[position];
            }
            setPeiShi = setPeiShi + ByteUtils.bytesToHexString(bytes1);
        }
        Log.e("GroupGreenActivity", "GroupGreenActivity--setTimePattern--" + setPeiShi);
        timePatternAsyn(setPeiShi, position);
    }

    private void timePatternAsyn(String setPeiShi, int position) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("byteString", setPeiShi);
        String url = Constant.Url.SET_TIME_PATTERN;
        params.put("nodeId", mNodeListEntities.get(position).getId());
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--设置时基返回值" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        if (noAction.getObject() == null) {
                            asynSetCount++;
                            if (asynSetCount > mNodeListEntities.size()) {
                                getData();
                            }
                        } else {
                            String object = (String) noAction.getObject();
                            byte[] bytes = ByteUtils.hexStringToByte(object);
                            if (ByteUtils.bytesUInt(bytes[0]) == 133 && ByteUtils.bytesUInt(bytes[1]) == 192) {
                                asynSetCount++;
                                if (asynSetCount > mNodeListEntities.size()) {
                                    getData();
                                }
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

    private void setGroupTimePattern() {
        //84消息类型 C0配时表标识 00 05对象数 01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
        //01方案号 78周期时长 1E相位差 0A协调相位 01对应的阶段配时表号
        //81C10005                       01781E0A01 02781E0A02 03781E0A03 0F781E070F 10781E0A10
        //84C00005                       01781E0A01 02781E0A02 03781E0A03 0F781E0A0F 10781E0A10
        String setPeiShi = "81C000" + ByteUtils.bytesToHexString(new byte[]{(byte) peiShiByteArrList.size()});
        for (int i = 0; i < peiShiByteArrList.size(); i++) {
            byte[] bytes1 = peiShiByteArrList.get(i);
            Log.e("GroupGreenActivity", "GroupGreenActivity--setGroupTimePattern--bytes1[1]111--" + ByteUtils.bytesToHexString(bytes1));
            bytes1[1] = (byte) cilcle;
            Log.e("GroupGreenActivity", "GroupGreenActivity--setGroupTimePattern--bytes1[1]222--" + ByteUtils.bytesToHexString(bytes1));
            setPeiShi = setPeiShi + ByteUtils.bytesToHexString(bytes1);
        }
        Log.e("GroupGreenActivity", "GroupGreenActivity--setGroupTimePattern--" + setPeiShi);
        timeGroupPatternAsyn(setPeiShi);
        //84C0000501781E0A0102781E0A0203781E0A030F781E0A0F10781E0A10
        //81C00005918C1E0A01028C1E0A02038C1E0A030F8C1E0A0F108C1E0A10
    }

    private void timeGroupPatternAsyn(String setPeiShi) {
        Log.e("PeiShiFragment", "PeiShiFragment--timePatternAsyn--");
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("byteString", setPeiShi);
        String url = Constant.Url.SET_GROUP_CYCLE;
        params.put("groupId", mGroupID);
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MonthBaseTimeFragment", "MonthBaseTimeFragment--onSuccess--设置时基返回值" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    if (noAction.isSuccess()) {
                        if (noAction.getObject() == null) {
                            asynSetCount++;
                            if (asynSetCount > mNodeListEntities.size()) {
                                getData();
                            }
                        } else {
                            String object = (String) noAction.getObject();
                            byte[] bytes = ByteUtils.hexStringToByte(object);
                            if (ByteUtils.bytesUInt(bytes[0]) == 133 && ByteUtils.bytesUInt(bytes[1]) == 192 || ByteUtils.bytesUInt(bytes[0]) == 129 && ByteUtils.bytesUInt(bytes[1]) == 192) {
                                asynSetCount++;
                                if (asynSetCount > mNodeListEntities.size()) {
                                    getData();
                                }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewComfrim:
                new AlertDialog.Builder(this)
                        .setMessage("确认配置绿波？")
                        .setNegativeButton("否", null)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                showLoadingDialog("加载数据");
                                set = true;
                                asynCount = 0;
                                asynSetCount = 0;
                                setGroupTimePattern();
                                for (int i = 0; i < mNodeListEntities.size(); i++) {
                                    setTimePattern(i);
                                }
                            }
                        })
                        .create()
                        .show();
                break;
            case R.id.imageViewAdd:
                if (cilcle < 255) {
                    cilcle++;
                    mTextViewCycle.setText(cilcle + "");
                }
                break;
            case R.id.imageViewDel:
                if (cilcle > 0) {
                    cilcle--;
                    mTextViewCycle.setText(cilcle + "");
                }
                break;
            case R.id.imageViewBack:
                finishTo();
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewAdd:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mImageViewAdd.isPressed()) {
                            try {
                                if (cilcle < 255) {
                                    Thread.sleep(100);
                                    cilcle++;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mTextViewCycle.setText(cilcle + "");
                                        }
                                    });
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                break;
            case R.id.imageViewDel:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mImageViewDel.isPressed()) {
                            try {
                                if (cilcle > 0) {
                                    Thread.sleep(100);
                                    cilcle--;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mTextViewCycle.setText(cilcle + "");
                                        }
                                    });
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                break;
        }
        return false;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View inflate = getLayoutInflater().inflate(R.layout.group_green_item_view, null);
            TextView textViewNodeID = (TextView) inflate.findViewById(R.id.textViewNodeID);
            TextView textViewNodeName = (TextView) inflate.findViewById(R.id.textViewNodeName);
            TextView textViewNodeIP = (TextView) inflate.findViewById(R.id.textViewNodeIP);
            final TextView textViewPhaseDiff = (TextView) inflate.findViewById(R.id.textViewPhaseDiff);
            final ImageView imageViewDel = (ImageView) inflate.findViewById(R.id.imageViewDel);
            final ImageView imageViewAdd = (ImageView) inflate.findViewById(R.id.imageViewAdd);
            imageViewDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mIntsPhaseDiff[position] > 0) {
                        mIntsPhaseDiff[position]--;
                        textViewPhaseDiff.setText(mIntsPhaseDiff[position] + "");
                    }
                }
            });
            imageViewAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mIntsPhaseDiff[position] < 255) {
                        mIntsPhaseDiff[position]++;
                        textViewPhaseDiff.setText(mIntsPhaseDiff[position] + "");
                    }
                }
            });
            imageViewDel.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (imageViewDel.isPressed()) {
                                try {
                                    if (mIntsPhaseDiff[position] > 0) {
                                        Thread.sleep(100);
                                        mIntsPhaseDiff[position]--;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                textViewPhaseDiff.setText(mIntsPhaseDiff[position] + "");
                                            }
                                        });
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    return false;
                }
            });
            imageViewAdd.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (imageViewAdd.isPressed()) {
                                try {
                                    if (mIntsPhaseDiff[position] < 255) {
                                        Thread.sleep(100);
                                        mIntsPhaseDiff[position]++;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                textViewPhaseDiff.setText(mIntsPhaseDiff[position] + "");
                                            }
                                        });
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    return false;
                }
            });
            if (position == 0) {
                imageViewAdd.setVisibility(View.INVISIBLE);
                imageViewDel.setVisibility(View.INVISIBLE);
                textViewPhaseDiff.setVisibility(View.INVISIBLE);
            }
            textViewPhaseDiff.setText(mIntsPhaseDiff[position] + "");
            textViewNodeID.setText("节点ID：" + mNodeListEntities.get(position).getId() + "");
            textViewNodeName.setText("节点名称：" + mNodeListEntities.get(position).getDeviceName());
            textViewNodeIP.setText("节点IP：" + mNodeListEntities.get(position).getIpAddress());
            return inflate;
        }
    }
}