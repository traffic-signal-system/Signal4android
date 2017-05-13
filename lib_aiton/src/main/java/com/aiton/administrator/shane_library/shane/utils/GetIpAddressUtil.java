package com.aiton.administrator.shane_library.shane.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by Administrator on 2016/3/22.
 */
public class GetIpAddressUtil
{

    /**
     * 调用android  的API： NetworkInterface. getHardwareAddress ()
     */
    public static String getPhoneIp()
    {
        try
        {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); )
            {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); )
                {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address)
                    {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e)
        {
        }
        return null;
    }

//    public static String getLocalIpAddress()
//    {
//        try {
//            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
//            {
//                NetworkInterface intf = en.nextElement();
//                for(Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
//                {
//                    InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress()&&!inetAddress.isLinkLocalAddress())
//                    {
//                        return inetAddress.getHostAddress().toString();
//                    }
//                }
//            }
//        } catch (SocketException ex)
//        {
//            Log.e("WifiPreference IpAddress", ex.toString());
//        }
//        return null;
//    }


    /**
     * 设备开通Wifi连接，获取到网卡的MAC地址和IP地址(但是不开通wifi，这种方法获取不到Mac地址，这种方法也是网络上使用的最多的方法)
     */
    public  static String getIp(Context context)
    {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (wifiManager.isWifiEnabled())
        {
//          wifiManager.setWifiEnabled(true);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            return intToIp(ipAddress);
        }
        return null;
    }

    public static String intToIp(int ip)
    {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
    }

}
