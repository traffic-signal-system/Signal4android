package com.aiton.zjb.signal.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.model.NoAction;
import com.aiton.zjb.signal.model.NodeAndGroupAndAreaInfo;
import com.aiton.zjb.signal.model.UserInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AreaActivity extends ZjbBaseActivity implements View.OnClickListener {

    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;
    private ListView mListViewArea;
    private List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> areaList = new ArrayList<>();
    private MyAdapter mMyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);
        init();
    }

    @Override
    protected void initIntent() {

    }

    @Override
    protected void initSP() {
        mACache = ACache.get(AreaActivity.this, Constant.ACACHE.USER);
        mUserInfo = (UserInfo) mACache.getAsObject(Constant.ACACHE.USERINFO);
        mDevicesID = mACache.getAsString(Constant.ACACHE.DEVICES_ID);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findID() {
        mListViewArea = (ListView) findViewById(R.id.listViewArea);
        mMyAdapter = new MyAdapter();
        mListViewArea.setAdapter(mMyAdapter);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {
        findViewById(R.id.textViewAddArea).setOnClickListener(this);
        findViewById(R.id.imageViewBack).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLoadingDialog("获取数据");
        getGroupAndArea();
    }

    private void getGroupAndArea() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        String url = Constant.Url.GET_GROUP_AND_AREA;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                try {
                    NodeAndGroupAndAreaInfo nodeAndGroupAndAreaInfo = GsonUtils.parseJSON(s, NodeAndGroupAndAreaInfo.class);
                    if (nodeAndGroupAndAreaInfo.isSuccess()) {
                        cancelLoadingDialog();
                        areaList.clear();
                        List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> areaListEntityList = nodeAndGroupAndAreaInfo.getObject().getAreaList();
                        areaList.addAll(areaListEntityList);
                        mMyAdapter.notifyDataSetChanged();
                    } else {
                        if (nodeAndGroupAndAreaInfo.getMessageCode() == 3) {
                            reLogin();
                        }
                    }
                    Toast.makeText(AreaActivity.this, nodeAndGroupAndAreaInfo.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void addArea(EditText editText_msg, final AlertDialog alertDialog) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("name", editText_msg.getText().toString().trim());
        String url = Constant.Url.ADD_AREA;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                try {
                    if (noAction.isSuccess()) {
                        getGroupAndArea();
                        alertDialog.dismiss();
                    } else {
                        if (noAction.getMessageCode() == 3) {
                            reLogin();
                        }
                    }
                    Toast.makeText(AreaActivity.this, noAction.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void editArea(final int position) {
        View inflate = getLayoutInflater().inflate(R.layout.edit_add_dialog_view, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(AreaActivity.this)
                .setView(inflate)
                .create();
        TextView textView_title = (TextView) inflate.findViewById(R.id.textView_title);
        final EditText editText_msg = (EditText) inflate.findViewById(R.id.editText_msg);
        textView_title.setText("修改域名称");
        editText_msg.setHint("请输入域名称");
        editText_msg.setText(areaList.get(position).getName());
        inflate.findViewById(R.id.textViewComfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.equals("", editText_msg.getText().toString().trim())) {
                    editText_msg.setError("域名称不能为空");
                } else {
                    showLoadingDialog("修改域名");
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("userId", mUserInfo.getObject().getId());
                    params.put("deviceId", mDevicesID);
                    params.put("id", areaList.get(position).getId());
                    params.put("name", editText_msg.getText().toString().trim());
                    String url = Constant.Url.EDIT_AREA;
                    asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String s = new String(responseBody);
                            Log.e("MyArearGroupAdapter", "MyArearGroupAdapter--onSuccess--修改域返回值" + s);
                            NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                            try {
                                if (noAction.isSuccess()) {
                                    getGroupAndArea();
                                    alertDialog.dismiss();
                                } else {
                                    if (noAction.getMessageCode() == 3) {
                                        reLogin();
                                    }
                                }
                                Toast.makeText(AreaActivity.this, noAction.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                }
            }
        });
        inflate.findViewById(R.id.textViewCancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void deleteArea(int position) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userId", mUserInfo.getObject().getId());
        params.put("deviceId", mDevicesID);
        params.put("id", areaList.get(position).getId());
        String url = Constant.Url.DELETE_AREA;
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.e("MyArearGroupAdapter", "MyArearGroupAdapter--onSuccess--删除域返回值" + s);
                try {
                    NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                    Toast.makeText(AreaActivity.this, noAction.getMessage(), Toast.LENGTH_SHORT).show();
                    if (noAction.isSuccess()) {
                        getGroupAndArea();
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
            case R.id.imageViewBack:
                finishTo();
                break;
            case R.id.textViewAddArea:
                View inflate = getLayoutInflater().inflate(R.layout.edit_add_dialog_view, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(AreaActivity.this)
                        .setView(inflate)
                        .create();
                TextView textView_title = (TextView) inflate.findViewById(R.id.textView_title);
                final EditText editText_msg = (EditText) inflate.findViewById(R.id.editText_msg);
                textView_title.setText("添加域");
                editText_msg.setHint("请输入域名称");
                inflate.findViewById(R.id.textViewComfirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.equals("", editText_msg.getText().toString().trim())) {
                            editText_msg.setError("域名称不能为空");
                        } else {
                            showLoadingDialog("添加域");
                            addArea(editText_msg, alertDialog);
                        }
                    }
                });
                inflate.findViewById(R.id.textViewCancle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                break;
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return areaList.size();
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
            View inflate = getLayoutInflater().inflate(R.layout.group_item_view, null);
            TextView textViewGroupID = (TextView) inflate.findViewById(R.id.textViewGroupID);
            TextView textViewGroupName = (TextView) inflate.findViewById(R.id.textViewGroupName);
            textViewGroupID.setText(areaList.get(position).getId() + "");
            textViewGroupName.setText(areaList.get(position).getName());
            inflate.findViewById(R.id.imageViewEdit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editArea(position);
                }
            });
            inflate.findViewById(R.id.imageViewDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(AreaActivity.this)
                            .setMessage("是否删除域")
                            .setNegativeButton("否", null)
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    showLoadingDialog("删除域");
                                    deleteArea(position);
                                }
                            })
                            .create()
                            .show();
                }
            });
            return inflate;
        }
    }
}
