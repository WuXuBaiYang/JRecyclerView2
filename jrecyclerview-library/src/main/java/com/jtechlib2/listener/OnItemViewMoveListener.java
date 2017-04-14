package com.jtechlib2.listener;

import android.support.v7.widget.RecyclerView;

/**
 * item的拖动监听
 * Created by wuxubaiyang on 16/4/1.
 */
public interface OnItemViewMoveListener {
    boolean onItemViewMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target);
}