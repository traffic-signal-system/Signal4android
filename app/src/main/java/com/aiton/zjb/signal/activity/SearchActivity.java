package com.aiton.zjb.signal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseActivity;
import com.aiton.zjb.signal.constant.Constant;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchActivity extends ZjbBaseActivity {

    private ListView mListViewAddSearch;
    private EditText mEditTextSearch;
    private List<Tip> listSearch = new ArrayList<>();
    private MyAdapter mAdapter;
    private String mCityCode;
    private Inputtips mInputtips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    @Override
    protected void initIntent() {
        Intent intent = getIntent();
        mCityCode = intent.getStringExtra(Constant.IntentKey.CITY_CODE);
    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findID() {
        mListViewAddSearch = (ListView) findViewById(R.id.listViewAddSearch);
        mEditTextSearch = (EditText) findViewById(R.id.editTextSearch);
    }

    @Override
    protected void initViews() {
        mAdapter = new MyAdapter();
        mListViewAddSearch.setAdapter(mAdapter);
    }

    @Override
    protected void setListeners() {
        mEditTextSearch.addTextChangedListener(new MyTextWatcher());
        mInputtips = new Inputtips(SearchActivity.this, new Inputtips.InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> list, int i) {
                listSearch.addAll(list);

//                for (int j = 0; j < listSearch.size(); j++) {
//                    Tip tip = listSearch.get(j);
//                    if (TextUtils.isEmpty(tip.getAdcode()) || TextUtils.isEmpty(tip.getAddress()) || TextUtils.isEmpty(tip.getDistrict()) ) {
//                        Log.e("ChosseSite", "onGetInputtips: --->>" + tip.getName());
//
//                        listSearch.remove(j);
//                    }
//                }
                Iterator<Tip> iterator = listSearch.iterator();
                while(iterator.hasNext()){
                    Tip tip = iterator.next();
                    if (TextUtils.isEmpty(tip.getAdcode()) || TextUtils.isEmpty(tip.getAddress()) || TextUtils.isEmpty(tip.getDistrict()) ) {
                        Log.e("ChosseSite", "onGetInputtips: --->>" + tip.getName());

                        iterator.remove();
                    }

                }
                mAdapter.notifyDataSetChanged();
                for (int j = 0; j < list.size(); j++) {
                    Log.e("onGetInputtips", "提示返回结果" + list.get(j).getName());
                    Log.e("onGetInputtips ", "onGetInputtips getDistrict" + list.get(j).getDistrict());
                    Log.e("onGetInputtips ", "onGetInputtips getAdcode" + list.get(j).getAdcode());
                    Log.e("onGetInputtips ", "onGetInputtips getAddress" + list.get(j).getAddress());
                }
            }
        });
        mListViewAddSearch.setOnItemClickListener(new MyItemClickListener());
    }

    class MyItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Tip tip = listSearch.get(i);
            Intent intent = new Intent();
            intent.putExtra(Constant.IntentKey.SEARCH_ADD,tip);
            setResult(Constant.REQUEST_RETURN_CODE.RESULT_ADD,intent);
            finishTo();
        }
    }

    class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.e("onTextChanged", "" + s);
            try {
                listSearch.clear();
                mAdapter.notifyDataSetChanged();
                if (!"".equals(s)) {
                    mInputtips.requestInputtips(s + "", "" + mCityCode);
                }
            } catch (AMapException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listSearch.size();
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
            View inflate = getLayoutInflater().inflate(R.layout.search_add_view, null);
            TextView textView_poi = (TextView) inflate.findViewById(R.id.textView_poi);
            TextView textView_detail = (TextView) inflate.findViewById(R.id.textView_detail);
            textView_poi.setText(listSearch.get(position).getName());
            textView_detail.setText(listSearch.get(position).getDistrict());
            return inflate;
        }
    }
}
