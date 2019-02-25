package com.jtechlib2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jtechlib2.listener.RecyclerDataObserver;
import com.jtechlib2.view.JRecyclerView;
import com.jtechlib2.view.RecyclerHolder;

/**
 * 自定义适配器，包裹用户设置的适配器，实现添加足部（加载更多）功能
 *
 * @author JTech
 */
public class LoadMoreAdapter extends RecyclerView.Adapter {
    private static final int ITEM_FOOTER = 0x9527;
    private LoadFooterAdapter loadFooterAdapter;
    private RecyclerHolder recyclerHolder;
    private RecyclerView.Adapter originAdapter;
    private Context context;
    private boolean loadMore;
    private int layoutState;

    public LoadMoreAdapter(Context context, RecyclerView.Adapter originAdapter, LoadFooterAdapter loadFooterAdapter) {
        this.loadFooterAdapter = loadFooterAdapter;
        this.context = context;
        this.originAdapter = originAdapter;
        //注册适配器的数据观察着
        originAdapter.registerAdapterDataObserver(new RecyclerDataObserver(this));
    }

    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
    }

    public void setLayoutState(int layoutState) {
        this.layoutState = layoutState;
    }

    public RecyclerView.Adapter getOriginAdapter() {
        return originAdapter;
    }

    /**
     * 是否可以触摸，防止上啦加载，footer影响touch
     *
     * @param position 位置
     * @return 是否可以press
     */
    public boolean canPress(int position) {
        return !(loadMore && position == getItemCount() - 1);
    }

    @Override
    public int getItemCount() {
        return originAdapter.getItemCount() + (loadMore ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (loadMore && position == getItemCount() - 1) {
            return ITEM_FOOTER;
        }
        return originAdapter.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_FOOTER) {
            if (null == recyclerHolder) {
                //实例化足部视图的viewholder
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                recyclerHolder = new RecyclerHolder(loadFooterAdapter.getFooterView(layoutInflater, parent));
                //重置状态
                modifyState(JRecyclerView.LOAD_STATE_NORMAL);
            }
            return recyclerHolder;
        }
        return originAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        //处理footer
        if (ITEM_FOOTER == getItemViewType(position)) {
            if (layoutState == JRecyclerView.LAYOUT_STATE_STAGGERED) {
                resetStaggLayoutFooterSize(holder);
            }
        } else {
            //调用适配器的bindview方法
            originAdapter.onBindViewHolder(holder, position);
        }
    }

    /**
     * 修改加载状态
     *
     * @param loadState 加载状态
     */
    public void modifyState(int loadState) {
        if (null != recyclerHolder) {
            switch (loadState) {
                case JRecyclerView.LOAD_STATE_FAIL:// 加载失败
                    loadFooterAdapter.loadFailState(recyclerHolder);
                    break;
                case JRecyclerView.LOAD_STATE_LOADING:// 加载中
                    loadFooterAdapter.loadingState(recyclerHolder);
                    break;
                case JRecyclerView.LOAD_STATE_NOMORE:// 无更多数据
                    loadFooterAdapter.noMoreDataState(recyclerHolder);
                    break;
                case JRecyclerView.LOAD_STATE_NORMAL:// 正常状态
                    loadFooterAdapter.normalState(recyclerHolder);
                    break;
            }
        }
    }

    /**
     * 重置表格布局的足部size
     *
     * @param holder 足部的holder
     */
    private void resetStaggLayoutFooterSize(RecyclerView.ViewHolder holder) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView
                .getLayoutParams();
        layoutParams.setFullSpan(true);
        holder.itemView.setLayoutParams(layoutParams);
    }
}