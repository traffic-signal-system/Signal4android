package com.aiton.administrator.shane_library.shane.utils;

import android.content.Context;
import android.text.TextUtils;

import com.aiton.administrator.shane_library.shane.model.UsedPersonID;
import com.android.volley.VolleyError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zjb on 2016/4/6.
 */
public class IsMobileNOorPassword {
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		联通：130、131、132、152、155、156、185、186
		电信：133、153、180、189、（1349卫通）
		总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		*/
        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 密码必须大于6位，且由字母及数字组合
     * @param password
     * @return
     */
    public static boolean isPassword(String password){
        String pass = "^(?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9]{7,}$";
        if (TextUtils.isEmpty(password)) return false;
        else return password.matches(pass);
    }
    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
    /**
     * 验证合法字符
     *
     * @return
     */
    public static boolean checkCharacter(String character) {
        boolean flag = false;
        try {
            String check = "[a-zA-Z0-9_]{4,16}";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(character);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
    public static boolean isCarID(String carId){
        String telRegex = "[\\u4e00-\\u9fa5|WJ]{1}[A-Z0-9]{6}";
        if (TextUtils.isEmpty(carId)) return false;
        else return carId.matches(telRegex);
    }
    /**
     * 验证姓名
     */
    public static boolean checkChineseName(String name){
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]*");
            Matcher matcher = p.matcher(name);
            flag = matcher.matches();
            if (name.length() < 2 || name.length() > 15) {
                flag = false;
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
    public static boolean checkPersonID(Context context,String personID){
        final boolean[] flag = {false};
        try {
            String yanzhengdizhi = "http://apis.juhe.cn/idcard/index?key=86c3cf00ca77641e108196c609ca3c08&cardno=" + personID;
            HTTPUtils.get(context, yanzhengdizhi, new VolleyListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }

                @Override
                public void onResponse(String s) {
                    UsedPersonID usedPersonID = GsonUtils.parseJSON(s, UsedPersonID.class);
                    int error_code = usedPersonID.getError_code();
                    //成功
                    flag[0] = 0 == error_code;
                }
            });

        } catch (Exception e) {
            flag[0] = false;
        }
        return flag[0];
    }
}
