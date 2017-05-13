package com.aiton.zjb.signal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiton.administrator.shane_library.shane.utils.ACache;
import com.aiton.administrator.shane_library.shane.utils.GsonUtils;
import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.model.AreaListInfo;
import com.aiton.zjb.signal.model.GroupListEntity;
import com.aiton.zjb.signal.model.NoAction;
import com.aiton.zjb.signal.model.NodeAndGroupAndAreaInfo;
import com.aiton.zjb.signal.model.UserInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class AddGroupActivity extends ZjbBaseActivity implements View.OnClickListener {

    private List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> mAllAreaList;
    private AlertDialog mListDialog;
    private TextView mTextViewAreaCode;
    private int mIdArea = -2;
    private String mGroupName = "";
    private EditText mEditTextGroupName;
    private CheckBox mCheckBoxEnable;
    private ACache mACache;
    private UserInfo mUserInfo;
    private String mDevicesID;
    private GroupListEntity mGroupListEntity;
    private boolean isEdit = false;
    private TextView mTextViewTitle;
    private Map<Integer, String> areaInfoMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        init();
    }

    @Override
    protected void initIntent() {
        Intent intent = getIntent();
        AreaListInfo areaListInfo = (AreaListInfo) intent.getSerializableExtra(Constant.IntentKey.Area_LIST);
        mGroupListEntity = (GroupListEntity) intent.getSerializableExtra(Constant.IntentKey.GROUP_INFO);
        if (mGroupListEntity != null) {
            isEdit = true;
        }
        mAllAreaList = areaListInfo.getAreaList();
        for (int i = 0; i < mAllAreaList.size(); i++) {
            areaInfoMap.put(mAllAreaList.get(i).getId(), mAllAreaList.get(i).getName());
        }
    }

    @Override
    protected void initSP() {
        mACache = ACache.get(AddGroupActivity.this, Constant.ACACHE.USER);
        mUserInfo = (UserInfo) mACache.getAsObject(Constant.ACACHE.USERINFO);
        mDevicesID = mACache.getAsString(Constant.ACACHE.DEVICES_ID);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findID() {
        mTextViewAreaCode = (TextView) findViewById(R.id.textViewAreaCode);
        mEditTextGroupName = (EditText) findViewById(R.id.editTextGroupName);
        mCheckBoxEnable = (CheckBox) findViewById(R.id.checkBoxEnable);
        mTextViewTitle = (TextView) findViewById(R.id.textViewTitle);
    }

    @Override
    protected void initViews() {
        if (isEdit) {
            mTextViewTitle.setText("修改群");
            mIdArea = mGroupListEntity.getAreaId();
            mTextViewAreaCode.setText(mIdArea + "  " + areaInfoMap.get(mIdArea));
            mEditTextGroupName.setText(mGroupListEntity.getGroupName());
            if (mGroupListEntity.getGroupEnable()==1){
                mCheckBoxEnable.setChecked(true);
            }else{
                mCheckBoxEnable.setChecked(false);
            }
        }
    }

    @Override
    protected void setListeners() {
        findViewById(R.id.imageViewBack).setOnClickListener(this);
        findViewById(R.id.rela_AreaCode).setOnClickListener(this);
        findViewById(R.id.textViewCommit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                finishTo();
                break;
            case R.id.rela_AreaCode:
                showListDialog(mAllAreaList);
                break;
            case R.id.textViewCommit:
                if (mIdArea != -2) {
                    if (!TextUtils.equals(mEditTextGroupName.getText().toString().trim(), "")) {
                        showLoadingDialog("保存群");
                        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        params.put("userId", mUserInfo.getObject().getId());
                        params.put("deviceId", mDevicesID);
                        params.put("areaId", mIdArea);
                        params.put("groupName", mEditTextGroupName.getText().toString().trim());
                        if (mCheckBoxEnable.isChecked()) {
                            params.put("groupEnable", 1);
                        } else {
                            params.put("groupEnable", 0);
                        }
                        String url = Constant.Url.ADD_GROUP;
                        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                cancelLoadingDialog();
                                String s = new String(responseBody);
                                Log.e("AddGroupActivity", "AddGroupActivity--onSuccess--添加群返回值");
                                NoAction noAction = GsonUtils.parseJSON(s, NoAction.class);
                                try {
                                    if (noAction.isSuccess()) {
                                        finishTo();
                                    } else {
                                        if (noAction.getMessageCode() == 3) {
                                            reLogin();
                                        }
                                    }
                                    Toast.makeText(AddGroupActivity.this, noAction.getMessage(), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {

                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                cancelLoadingDialog();
                                Toast.makeText(AddGroupActivity.this, "连接错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        showTipDialog("群名称不能为空");
                    }
                } else {
                    showTipDialog("请选择域编号");
                }
                break;
        }
    }

    /**
     * 列表dialog
     *
     * @param list
     */
    private void showListDialog(List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View list_dialog_view = getLayoutInflater().inflate(R.layout.list_dialog, null);
        ListView dialogListView = (ListView) list_dialog_view.findViewById(R.id.listView);
        dialogListView.setAdapter(new MyDialogAdapter(list));
        dialogListView.setOnItemClickListener(new MyItemClick(list));
        mListDialog = builder.setView(list_dialog_view)
                .create();
        mListDialog.show();
        int orientation = getResources().getConfiguration().orientation;
        Window dialogWindow = mListDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getResources().getDisplayMetrics(); // 获取屏幕宽、高用
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

    class MyDialogAdapter extends BaseAdapter {

        private List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> shiji;

        public MyDialogAdapter(List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> shiji) {
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
            View mDialog_list_itemView = null;
            mDialog_list_itemView = getLayoutInflater().inflate(R.layout.manager_item_view, null);
            TextView textView_managerID = (TextView) mDialog_list_itemView.findViewById(R.id.textView_managerID);
            TextView textView_ManagerPhone = (TextView) mDialog_list_itemView.findViewById(R.id.textView_ManagerPhone);
            textView_managerID.setText(mAllAreaList.get(position).getId() + "");
            textView_ManagerPhone.setText(mAllAreaList.get(position).getName());
            return mDialog_list_itemView;
        }
    }

    public class MyItemClick implements AdapterView.OnItemClickListener {

        private List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> list;

        public MyItemClick(List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> list) {
            this.list = list;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mListDialog.dismiss();
            mIdArea = mAllAreaList.get(i).getId();
            mGroupName = mAllAreaList.get(i).getName();
            mTextViewAreaCode.setText(mIdArea + "  " + mGroupName);
        }
    }
}
