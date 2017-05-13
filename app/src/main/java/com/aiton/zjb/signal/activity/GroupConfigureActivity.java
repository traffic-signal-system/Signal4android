package com.aiton.zjb.signal.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.fragment.JieDuanFragment;
import com.aiton.zjb.signal.fragment.PeiShiFragment;
import com.aiton.zjb.signal.fragment.TimeBaseFragment;

public class GroupConfigureActivity extends FragmentActivity {
    private String[] tabsItem = new String[]{
            "时基",
            "配时方案",
            "阶段配时",
    };
    private int[] imgResID = new int[]{
            R.mipmap.tsc_page_basetime,
            R.mipmap.tsc_page_stage,
            R.mipmap.tsc_page_detector,
    };
    private Class[] fragment = new Class[]{
            TimeBaseFragment.class,
            PeiShiFragment.class,
            JieDuanFragment.class,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_configure);
        setRequestedOrientation(Constant.HengShuPing);
        FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtab);
        for (int i = 0; i < tabsItem.length; i++) {
            View inflate = getLayoutInflater().inflate(R.layout.tabs_item, null);
            TextView tabs_text = (TextView) inflate.findViewById(R.id.tabs_text);
            ImageView imgRes = (ImageView) inflate.findViewById(R.id.imgRes);
            imgRes.setImageResource(imgResID[i]);
            tabs_text.setText(tabsItem[i]);
            tabHost.addTab(tabHost.newTabSpec(tabsItem[i]).setIndicator(inflate), fragment[i], null);
        }
    }
}
