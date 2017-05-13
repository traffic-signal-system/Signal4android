package com.aiton.administrator.shane_library.shane.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

public class NetChecker {

	private Context context;

	private static NetChecker instance;

	public static NetChecker getInstance(Context context) {
		if (instance == null) {
			instance = new NetChecker(context);
		}

		return instance;
	}

	public NetChecker(Context context) {
		this.context = context;
	}

	public boolean checkNetwork() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo == null) {
			return false;
		} else {
			return activeNetInfo.isAvailable();
		}
	}

	/**
	 * 检测有网络连接，但是不通的情况
	 * 
	 * @return
	 * 
	 */
	public boolean isAvailableNetwork() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo == null) {
			return false;
		} else {
			if (activeNetInfo.isConnected()) {
				return (activeNetInfo.isAvailable());
			} else {
				return false;
			}
		}
	}

	/**
	 * 检测wifi是否可用
	 * 
	 * @return
	 * 
	 */
	public boolean isWifiAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (activeNetInfo == null) {
			return false;
		} else {
			if (activeNetInfo.isConnected()) {
				return activeNetInfo.isAvailable();
			} else {
				return false;
			}
		}
	}

	/**
	 * 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
	 * @return
	 */
	public boolean isNetworkOnline() {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process ipProcess = runtime.exec("ping -c 1 114.114.114.114");
			int exitValue = ipProcess.waitFor();
			return (exitValue == 0);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
}
