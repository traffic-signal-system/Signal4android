package com.aiton.administrator.shane_library.shane.utils;

/**
 * Created by Administrator on 2016/11/16.
 */
public class StringUtil {
    /** 将int数组转换成字符串
     * @param in
     * @return
     */
    public static String arrayToString(int[] in)
    {
        if (in == null)
            return "null";

        if (in.length == 0)
            return "no element";

        int length = in.length;
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++)
        {
            buf.append(in[i]);
        }

        return buf.toString();
    }
}
