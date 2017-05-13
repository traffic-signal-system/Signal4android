package com.aiton.zjb.signal.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aiton.zjb.signal.R;
import com.aiton.zjb.signal.base.ZjbBaseFragment;
import com.aiton.zjb.signal.constant.Constant;
import com.aiton.zjb.signal.fragment.timebasefragment.DayBaseTimeFragment;
import com.aiton.zjb.signal.fragment.timebasefragment.MonthBaseTimeFragment;
import com.aiton.zjb.signal.fragment.timebasefragment.TimeIntervalFragment;
import com.aiton.zjb.signal.fragment.timebasefragment.WeekBaseTimeFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeBaseFragment extends ZjbBaseFragment implements View.OnClickListener {


    private View mInflate;
    private ViewPager mViewPager;
    private MonthBaseTimeFragment mFragment0201;
    private WeekBaseTimeFragment mFragment0202;
    private DayBaseTimeFragment mFragment0203;
    private TimeIntervalFragment mFragment0204;
    private Button[] btnTabs = new Button[4];
    private int[] btnTabsID = new int[]{
            R.id.button,
            R.id.button2,
            R.id.button3,
            R.id.button4
    };
    public String mIp;

    public TimeBaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mInflate==null){
            mInflate = inflater.inflate(R.layout.fragment_fragment02, container, false);
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
    }

    @Override
    protected void initSP() {

    }

    @Override
    protected void initData() {
        initFragment();
    }

    @Override
    protected void findID() {
        for (int i = 0; i < btnTabs.length; i++) {
            btnTabs[i] = (Button) mInflate.findViewById(btnTabsID[i]);
        }
        mViewPager = (ViewPager) mInflate.findViewById(R.id.viewpagerShiJI);
    }

    @Override
    protected void initViews() {
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(new MyViewPagerAdapter(getFragmentManager()));
        selectBtn(0);
    }

    @Override
    protected void setListeners() {
        for (int i = 0; i < btnTabs.length; i++) {
            btnTabs[i].setOnClickListener(this);
        }
        mViewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            selectBtn(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return mFragment0201;
            } else if (position == 1) {
                return mFragment0202;
            } else if (position == 2) {
                return mFragment0203;
            } else if (position == 3) {
                return mFragment0204;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    private void selectBtn(int m) {
        btnTabs[m%4].setBackgroundResource(R.color.white);
        btnTabs[(m+1)%4].setBackgroundResource(R.color.btn02);
        btnTabs[(m+2)%4].setBackgroundResource(R.color.btn02);
        btnTabs[(m+3)%4].setBackgroundResource(R.color.btn02);
    }

    private void initFragment() {
        mFragment0201 = new MonthBaseTimeFragment();
        mFragment0202 = new WeekBaseTimeFragment();
        mFragment0203 = new DayBaseTimeFragment();
        mFragment0204 = new TimeIntervalFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.button2:
                mViewPager.setCurrentItem(1);

                break;
            case R.id.button3:
                mViewPager.setCurrentItem(2);

                break;
            case R.id.button4:
                mViewPager.setCurrentItem(3);

                break;
        }
    }
}
