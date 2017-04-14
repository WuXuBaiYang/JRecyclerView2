package com.jtechlib2.listener;

import android.support.v7.widget.RecyclerView;

/**
 * 加载更多的适配器数据观察者
 * Created by wuxubaiyang on 2016/3/7.
 */
public class RecyclerDataObserver extends RecyclerView.AdapterDataObserver {
    private RecyclerView.Adapter adapter;

    public RecyclerDataObserver(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        adapter.notifyItemRangeChanged(positionStart, itemCount, payload);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        adapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        adapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        adapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        adapter.notifyItemRangeRemoved(positionStart, itemCount);
    }
}