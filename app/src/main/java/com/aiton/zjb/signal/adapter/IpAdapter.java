package com.aiton.zjb.signal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aiton.zjb.signal.R;

import java.util.ArrayList;

public class IpAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> mIpList;
    public IpAdapter(Context context,ArrayList<String> mIpList){
        this.context= context;
        this.mIpList=mIpList;
    }

    @Override
    public int getCount() {
        return mIpList.size();
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
        LayoutInflater inflater = LayoutInflater.from(context);
        View inflate = inflater.inflate(R.layout.lv_tsc01, null);
        TextView itemIPAddress = (TextView) inflate.findViewById(R.id.itemIPAddress);
        itemIPAddress.setText(mIpList.get(position));
        return inflate;
    }
}