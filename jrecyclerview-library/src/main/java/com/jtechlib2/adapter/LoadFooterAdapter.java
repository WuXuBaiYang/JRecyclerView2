package com.jtechlib2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jtechlib2.view.RecyclerHolder;

/**
 * 加载更多的足部适配器
 * Created by wuxubaiyang on 2016/3/7.
 */
public abstract class LoadFooterAdapter {

    /**
     * 获取足部视图
     *
     * @param layoutInflater inflater
     * @param parent         父视图
     * @return 足部视图
     */
    public abstract View getFooterView(LayoutInflater layoutInflater, ViewGroup parent);

    /**
     * 加载失败
     * @param recyclerHolder holder
     */
    public abstract void loadFailState(RecyclerHolder recyclerHolder);

    /**
     * 加载中
     * @param recyclerHolder holder
     */
    public abstract void loadingState(RecyclerHolder recyclerHolder);

    /**
     * 没有更多数据
     * @param recyclerHolder holder
     */
    public abstract void noMoreDataState(RecyclerHolder recyclerHolder);

    /**
     * 一般状态
     * @param recyclerHolder holder
     */
    public abstract void normalState(RecyclerHolder recyclerHolder);
}