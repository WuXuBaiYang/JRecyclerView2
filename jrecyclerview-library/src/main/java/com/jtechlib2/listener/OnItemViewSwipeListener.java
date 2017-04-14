package com.jtechlib2.listener;

import android.support.v7.widget.RecyclerView;

/**
 * item的滑动监听
 * Created by wuxubaiyang on 16/4/1.
 */
public interface OnItemViewSwipeListener {
    void onItemViewSwipe(RecyclerView.ViewHolder viewHolder, int direction);
}