package com.jtechlib2.listener;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.jtechlib2.adapter.LoadMoreAdapter;
import com.jtechlib2.adapter.RecyclerSwipeAdapter;
import com.jtechlib2.view.RecyclerHolder;

/**
 * 自定义滑动删除以及拖动换位
 */
public class ItemTouchCallback extends ItemTouchHelper.Callback {
    private boolean isItemViewSwipeEnabled = false;
    private boolean isLongPressDragEnabled = false;
    private int dragFlags, swipeFlags;

    private LoadMoreAdapter loadMoreAdapter;
    private RecyclerSwipeAdapter recyclerSwipeAdapter;

    private OnItemViewMoveListener onItemViewMoveListener;
    private OnItemViewSwipeListener onItemViewSwipeListener;

    public void setLoadMoreAdapter(LoadMoreAdapter loadMoreAdapter) {
        this.loadMoreAdapter = loadMoreAdapter;
        //如果用户传入的适配器是recyclerswipeadapter，则获取
        if (loadMoreAdapter.getOriginAdapter() instanceof RecyclerSwipeAdapter) {
            this.recyclerSwipeAdapter = (RecyclerSwipeAdapter) loadMoreAdapter.getOriginAdapter();
        }
    }

    public void setDragFlags(int dragFlags) {
        this.dragFlags = dragFlags;
    }

    public void setSwipeFlags(int swipeFlags) {
        this.swipeFlags = swipeFlags;
    }

    public void setItemViewSwipeEnabled(boolean itemViewSwipeEnabled) {
        isItemViewSwipeEnabled = itemViewSwipeEnabled;
    }

    public void setLongPressDragEnabled(boolean longPressDragEnabled) {
        isLongPressDragEnabled = longPressDragEnabled;
    }

    public void setOnItemViewMoveListener(OnItemViewMoveListener onItemViewMoveListener) {
        this.onItemViewMoveListener = onItemViewMoveListener;
    }

    public void setOnItemViewSwipeListener(OnItemViewSwipeListener onItemViewSwipeListener) {
        this.onItemViewSwipeListener = onItemViewSwipeListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (null != loadMoreAdapter && loadMoreAdapter.canPress(viewHolder.getAdapterPosition())) {
            return makeMovementFlags(dragFlags, swipeFlags);
        }
        return 0;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (null != onItemViewMoveListener && null != loadMoreAdapter && loadMoreAdapter.canPress(target.getAdapterPosition())) {
            return onItemViewMoveListener.onItemViewMove(recyclerView, viewHolder, target);
        }
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (null != onItemViewSwipeListener) {
            onItemViewSwipeListener.onItemViewSwipe(viewHolder, direction);
        }
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isItemViewSwipeEnabled;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isLongPressDragEnabled;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (null == recyclerSwipeAdapter) {
            super.clearView(recyclerView, viewHolder);
            return;
        }
        //强转viewholder
        RecyclerHolder recyclerHolder = (RecyclerHolder) viewHolder;
        //调用清除方法
        getDefaultUIUtil().clearView(checkSwipeViewNotNull(recyclerSwipeAdapter.getSwipeView(recyclerHolder)));
        //重置方法
        recyclerSwipeAdapter.clearView(recyclerHolder);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (null == recyclerSwipeAdapter) {
            super.onSelectedChanged(viewHolder, actionState);
            return;
        }
        if (null != viewHolder) {
            //设置选择方法
            getDefaultUIUtil().onSelected(checkSwipeViewNotNull(recyclerSwipeAdapter.getSwipeView((RecyclerHolder) viewHolder)));
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (null == recyclerSwipeAdapter) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }
        //强转recyclerholder
        RecyclerHolder recyclerHolder = (RecyclerHolder) viewHolder;
        //绘制滑动视图
        getDefaultUIUtil().onDraw(c, recyclerView, checkSwipeViewNotNull(recyclerSwipeAdapter.getSwipeView(recyclerHolder)), dX, dY, actionState, isCurrentlyActive);
        //调用绘制监听
        int direction = 0;
        if (dX > 0) {
            direction = ItemTouchHelper.END;
            recyclerSwipeAdapter.onSwipeEnd(recyclerHolder, dX);
        } else if (dX < 0) {
            direction = ItemTouchHelper.START;
            recyclerSwipeAdapter.onSwipeStart(recyclerHolder, dX);
        }
        recyclerSwipeAdapter.onSwipe(recyclerHolder, direction, dX);
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (null == recyclerSwipeAdapter) {
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }
        //上层绘制
        getDefaultUIUtil().onDrawOver(c, recyclerView, checkSwipeViewNotNull(recyclerSwipeAdapter.getSwipeView((RecyclerHolder) viewHolder)), dX, dY, actionState, isCurrentlyActive);
    }

    /**
     * 检查滑动视图是否为空
     *
     * @param swipeView
     * @return
     */
    private View checkSwipeViewNotNull(View swipeView) {
        if (null == swipeView) {
            throw new NullPointerException("swipeView could not be null,bro!");
        } else {
            return swipeView;
        }
    }
}