package com.aiton.administrator.shane_library.shane.refreshview.callback;

/**
 * 提供自定义headerview的接口
 * 
 * @author huxq17@163.com
 * 
 */
public interface IHeaderCallBack {
	/**
	 * 正常状态
	 */
	void onStateNormal();

	/**
	 * 准备刷新
	 */
	void onStateReady();

	/**
	 * 正在刷新
	 */
	void onStateRefreshing();

	/**
	 * 刷新结束
	 */
	void onStateFinish();

	/**
	 * 获取headerview显示的高度与headerview高度的比例
	 * 
	 * @param offset
	 *            移动距离和headerview高度的比例，范围是0~1，0：headerview完全没显示 1：headerview完全显示
	 * @param offsetY
	 *            headerview移动的距离
	 */
	void onHeaderMove(double offset, int offsetY, int deltaY);

	/**
	 * 设置显示上一次刷新的时间
	 * 
	 * @param lastRefreshTime
	 *            上一次刷新的时间
	 */
	void setRefreshTime(long lastRefreshTime);

	/**
	 * 隐藏footerview
	 */
	void hide();

	/**
	 * 显示footerview
	 */
	void show();

	/**
	 * 获得headerview的高度,如果不想headerview全部被隐藏，就可以只返回一部分的高度
	 * 
	 * @return
	 */
	int getHeaderHeight();
}