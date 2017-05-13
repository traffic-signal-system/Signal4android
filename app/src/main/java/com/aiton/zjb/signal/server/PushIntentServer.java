package com.aiton.zjb.signal.server;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.model.PushInfo;
import com.aiton.zjb.signal.model.UserInfo;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/12/29.
 */
public class PushIntentServer extends GTIntentService {
    /**
     * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
     * onReceiveMessageData 处理透传消息<br>
     * onReceiveClientId 接收 cid <br>
     * onReceiveOnlineState cid 离线上线通知 <br>
     * onReceiveCommandResult 各种事件处理回执 <br>
     */

    private static int heartCount = 0;
    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;

    @Override
    public void onReceiveServicePid(Context context, int i) {
    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        Log.i(TAG, "onReceiveClientId -> " + "clientid = " + s);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        String s = new String(gtTransmitMessage.getPayload());
        PushInfo pushInfo = GsonUtils.parseJSON(s, PushInfo.class);
        String title = pushInfo.getTitle();
        if (TextUtils.equals(title,"extReportState")) {
            //信号机状态上报
            Intent intent = new Intent();
            intent.setAction(Constant.BROADCASTRECEIVER.SIGNAL_STATU);
            intent.putExtra(Constant.IntentKey.SIGNAL_STATUE,pushInfo.getContext());
            context.sendBroadcast(intent);
            mACache = ACache.get(context, Constant.ACACHE.USER);
            mUserInfo = (UserInfo) mACache.getAsObject(Constant.ACACHE.USERINFO);
            mDevicesID = mACache.getAsString(Constant.ACACHE.DEVICES_ID);
            heartCount++;
            if (heartCount==30){
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                String url = Constant.Url.HEART_BEAT;
                params.put("userId",mUserInfo.getObject().getId());
                params.put("deviceId",mDevicesID);
                asyncHttpClient.post(url,params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String s = new String(responseBody);
                        Log.i("PushIntentServer", "PushIntentServer--onSuccess--心跳返回"+s  );
                        try {

                        }catch (Exception e){

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e("PushIntentServer", "PushIntentServer--onFailure--失败");
                    }
                });
                heartCount=0;
            }
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        Log.e("PushIntentServer", "PushIntentServer--onReceiveCommandResult--"+gtCmdMessage.getAction());
    }


}
