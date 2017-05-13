package com.aiton.administrator.shane_library.shane.refreshview.callback;


import com.aiton.administrator.shane_library.shane.refreshview.XRefreshView;

public interface IFooterCallBack {
    /**
     * 当不是到达底部自动加载更多的时候，需要自己写点击事件
     *
     * @param xRefreshViewListener
     */
    void callWhenNotAutoLoadMore(XRefreshView.XRefreshViewListener xRefreshViewListener);

    /**
     * 正常状态，例如需要点击footerview才能加载更多，主要是到达底部不自动加载更多时会被调用
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
     * 已无更多数据
     */
    void onStateComplete();


    /**
     * 设置显示或者隐藏footerview
     * @param show
     */
    void show(boolean show);

    /**
     * footerview是否显示中
     *
     * @return
     */
    boolean isShowing();

    /**
     * 获得footerview的高度
     *
     * @return
     */
    int getFooterHeight();
}
