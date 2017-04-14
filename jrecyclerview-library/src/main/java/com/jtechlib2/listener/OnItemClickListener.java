package com.jtechlib2.listener;

import android.view.View;

import com.jtechlib2.view.RecyclerHolder;

/**
 * item点击事件回调
 * Created by wuxubaiyang on 2016/3/7.
 */
public interface OnItemClickListener {
    void onItemClick(RecyclerHolder holder, View view, int position);
}