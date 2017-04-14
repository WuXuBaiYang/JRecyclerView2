package com.jtechlib2.listener;

import com.jtechlib2.view.RecyclerHolder;

/**
 * 设置item触摸后可拖动item换位
 */
public interface OnItemTouchToMove {
    <VH extends RecyclerHolder> void itemTouchToMove(VH holder);
}