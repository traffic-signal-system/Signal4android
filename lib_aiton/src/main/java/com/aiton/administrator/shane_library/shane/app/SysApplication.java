package com.aiton.administrator.shane_library.shane.app;

import android.app.Activity;
import android.app.Application;

import com.aiton.administrator.shane_library.R;

import java.util.LinkedList;
import java.util.List;

/**
 * 一个类 用来结束所有后台activity
 *
 * @author Administrator
 */
public class SysApplication extends Application {
    //运用list来保存们每一个activity是关键
    private List<Activity> mList = new LinkedList<Activity>();

    /**
     * 单实例
     */
    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    private static SysApplication instance;

    //构造方法
    private SysApplication() {
    }

    //实例化一次
    public synchronized static SysApplication getInstance() {
        if (null == instance) {
            instance = new SysApplication();
        }
        return instance;
    }

    // add Activity  
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    //关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
                    activity.overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    /**
     * 保留当前界面，关闭其他界面
     */
    public void exitExceptThisActivity() {
        try {
            for (int i = 0; i < mList.size() - 1; i++) {
                mList.get(i).finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    //杀进程
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
 
