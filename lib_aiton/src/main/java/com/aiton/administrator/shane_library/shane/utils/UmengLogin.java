package com.aiton.administrator.shane_library.shane.utils;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by zjb on 2016/7/7.
 */
public class UmengLogin {
    public static void UmengLoginID(String phone){
        MobclickAgent.onProfileSignIn(phone);
    }
}
