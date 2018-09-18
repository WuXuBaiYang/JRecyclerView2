package com.jtechlib2.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;

import com.jtechlib2.adapter.LoadFooterAdapter;
import com.jtechlib2.adapter.LoadMoreAdapter;
import com.jtechlib2.adapter.SimpleLoadFooterAdapter;
import com.jtechlib2.listener.ItemTouchCallback;
import com.jtechlib2.listener.OnItemViewMoveListener;
import com.jtechlib2.listener.OnItemViewSwipeListener;
import com.jtechlib2.listener.OnLoadListener;

/**
 * jrecyclerview
 *
 * @author wuxubaiyang
 */
public class JRecyclerView extends RecyclerView {
    /**
     * 加载状态-正常状态
     */
    public static final int LOAD_STATE_NORMAL = 0x9527;
    /**
     * 加载状态-加载中
     */
    public static final int LOAD_STATE_LOADING = 0x9528;
    /**
     * 加载状态-加载失败
     */
    public static final int LOAD_STATE_FAIL = 0x9529;
    /**
     * 加载状态-无更多数据
     */
    public static final int LOAD_STATE_NOMORE = 0x9530;
    /**
     * 加载状态标志位
     */
    private int loadState = LOAD_STATE_NORMAL;
    /**
     * layoutmanager状态-线性布局
     */
    public static final int LAYOUT_STATE_LINEAR = 0x9531;
    /**
     * layoutmanager状态-表格布局
     */
    public static final int LAYOUT_STATE_GRID = 0x9532;
    /**
     * layoutmanager状态-瀑布流布局
     */
    public static final int LAYOUT_STATE_STAGGERED = 0x9533;
    /**
     * layoutmanager标志位
     */
    private int layoutState = LAYOUT_STATE_LINEAR;
    /**
     * 是否显示足部（加载更多）
     */
    private boolean loadMore = false;
    /**
     * 外部包裹的适配器
     */
    private LoadMoreAdapter loadMoreAdapter;
    /**
     * item的触摸事件回调实现
     */
    private ItemTouchCallback itemTouchCallback;
    /**
     * item的触摸事件处理方法
     */
    private ItemTouchHelper itemTouchHelper;
    /**
     * 加载更多监听
     */
    private OnLoadListener onLoadListener;

    /**
     * 主构造
     *
     * @param context context
     */
    public JRecyclerView(Context context) {
        this(context, null);
    }

    /**
     * 主构造
     *
     * @param context context
     * @param attrs   attrs
     */
    public JRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 主构造
     *
     * @param context context
     * @param attrs   attrs
     * @param arg2    styleid
     */
    public JRecyclerView(Context context, AttributeSet attrs, int arg2) {
        super(context, attrs, arg2);
        //设置item的触摸回调(处理滑动删除以及拖动换位)
        itemTouchCallback = new ItemTouchCallback();
        itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(this);
    }

    /**
     * 开始拖动换位
     *
     * @param viewHolder holder
     */
    public void startDrag(ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    /**
     * 开始滑动删除
     *
     * @param viewHolder holder
     */
    public void startSwipe(ViewHolder viewHolder) {
        itemTouchHelper.startSwipe(viewHolder);
    }

    /**
     * 设置可以上下拖动
     *
     * @param longPressDragEnabled   长按拖动是否可用
     * @param onItemViewMoveListener 拖动监听
     */
    public void setMoveUpDown(boolean longPressDragEnabled, OnItemViewMoveListener onItemViewMoveListener) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        setMove(longPressDragEnabled, dragFlags, onItemViewMoveListener);
    }

    /**
     * 设置可以左右拖动
     *
     * @param longPressDragEnabled   长按拖动是否可用
     * @param onItemViewMoveListener 拖动监听
     */
    public void setMoveLeftRight(boolean longPressDragEnabled, OnItemViewMoveListener onItemViewMoveListener) {
        int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        setMove(longPressDragEnabled, dragFlags, onItemViewMoveListener);
    }

    /**
     * 设置可以自由拖动
     *
     * @param longPressDragEnabled   长按拖动是否可用
     * @param onItemViewMoveListener 拖动监听
     */
    public void setMoveFree(boolean longPressDragEnabled, OnItemViewMoveListener onItemViewMoveListener) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        setMove(longPressDragEnabled, dragFlags, onItemViewMoveListener);
    }

    /**
     * 设置移动属性
     *
     * @param longPressDragEnabled   长按拖动是否可用
     * @param dragFlags              拖动标记
     * @param onItemViewMoveListener 拖动监听
     */
    public void setMove(boolean longPressDragEnabled, int dragFlags, OnItemViewMoveListener onItemViewMoveListener) {
        //设置长按拖动是否可用
        setLongPressDragEnabled(longPressDragEnabled);
        //设置拖动标记为左右
        setDragFlags(dragFlags);
        //设置监听
        setOnItemViewMoveListener(onItemViewMoveListener);
    }

    /**
     * 设置滑动删除
     *
     * @param swipeEnabled            滑动删除是否可用
     * @param onItemViewSwipeListener 滑动删除监听
     */
    public void setSwipeStart(boolean swipeEnabled, OnItemViewSwipeListener onItemViewSwipeListener) {
        int swipeFlags = ItemTouchHelper.START;
        setSwipe(swipeEnabled, swipeFlags, onItemViewSwipeListener);
    }

    /**
     * 设置滑动删除
     *
     * @param swipeEnabled            滑动删除是否可用
     * @param onItemViewSwipeListener 滑动删除监听
     */
    public void setSwipeEnd(boolean swipeEnabled, OnItemViewSwipeListener onItemViewSwipeListener) {
        int swipeFlags = ItemTouchHelper.END;
        setSwipe(swipeEnabled, swipeFlags, onItemViewSwipeListener);
    }

    /**
     * 自由滑动
     *
     * @param swipeEnabled            是否开启滑动
     * @param onItemViewSwipeListener 滑动监听
     */
    public void setSwipeFree(boolean swipeEnabled, OnItemViewSwipeListener onItemViewSwipeListener) {
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        setSwipe(swipeEnabled, swipeFlags, onItemViewSwipeListener);
    }

    /**
     * 设置滑动删除
     *
     * @param swipeEnabled            滑动删除是否可用
     * @param swipeFlags              滑动删除的标记
     * @param onItemViewSwipeListener 滑动删除监听
     */
    public void setSwipe(boolean swipeEnabled, int swipeFlags, OnItemViewSwipeListener onItemViewSwipeListener) {
        //设置滑动是否可用
        setItemViewSwipeEnabled(swipeEnabled);
        //设置滑动方向
        setSwipeFlags(swipeFlags);
        //设置监听
        setOnItemViewSwipeListener(onItemViewSwipeListener);
    }

    /**
     * 设置拖动换位标记
     *
     * @param dragFlags 拖动标记，多个由"|"符号分割
     */
    public void setDragFlags(int dragFlags) {
        itemTouchCallback.setDragFlags(dragFlags);
    }

    /**
     * 设置滑动删除标记
     *
     * @param swipeFlags 滑动标记，多个由"|"符号分割
     */
    public void setSwipeFlags(int swipeFlags) {
        itemTouchCallback.setSwipeFlags(swipeFlags);
    }

    /**
     * 设置item是否可以滑动删除
     *
     * @param itemViewSwipeEnabled 是否可以滑动删除
     */
    public void setItemViewSwipeEnabled(boolean itemViewSwipeEnabled) {
        itemTouchCallback.setItemViewSwipeEnabled(itemViewSwipeEnabled);
    }

    /**
     * 设置item是否可以长点击拖动换位
     *
     * @param longPressDragEnabled 是否可以长点击拖动
     */
    public void setLongPressDragEnabled(boolean longPressDragEnabled) {
        itemTouchCallback.setLongPressDragEnabled(longPressDragEnabled);
    }

    /**
     * 设置item拖动换位的监听
     *
     * @param onItemViewMoveListener 拖动换位监听
     */
    public void setOnItemViewMoveListener(OnItemViewMoveListener onItemViewMoveListener) {
        itemTouchCallback.setOnItemViewMoveListener(onItemViewMoveListener);
    }

    /**
     * 设置item滑动删除监听
     *
     * @param onItemViewSwipeListener 滑动删除监听
     */
    public void setOnItemViewSwipeListener(OnItemViewSwipeListener onItemViewSwipeListener) {
        itemTouchCallback.setOnItemViewSwipeListener(onItemViewSwipeListener);
    }

    /**
     * 设置加载状态
     *
     * @param loadState 加载状态
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        if (null != loadMoreAdapter) {
            loadMoreAdapter.modifyState(loadState);
        }
    }

    /**
     * 设置状态为正在加载
     */
    public void setLoadingState() {
        setLoadState(LOAD_STATE_LOADING);
    }

    /**
     * 设置状态为加载失败
     */
    public void setLoadFailState() {
        setLoadState(LOAD_STATE_FAIL);
    }

    /**
     * 设置状态为无更多数据
     */
    public void setLoadFinishState() {
        setLoadState(LOAD_STATE_NOMORE);
    }

    /**
     * 设置状态为加载完成
     */
    public void setLoadCompleteState() {
        setLoadState(LOAD_STATE_NORMAL);
    }

    /**
     * 是否开启加载更多功能
     *
     * @param loadMore 是否加载更多
     */
    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
        if (null != loadMoreAdapter) {
            loadMoreAdapter.setLoadMore(loadMore);
        }
    }

    /**
     * 是否正在加载中
     *
     * @return 是否正在加载
     */
    public boolean isLoading() {
        return this.loadState == LOAD_STATE_LOADING;
    }

    /**
     * 是否加载更多
     *
     * @return 是否加载更多
     */
    public boolean isLoadMore() {
        return loadMore;
    }

    /**
     * 设置加载更多监听
     *
     * @param onLoadListener 加载更多监听
     */
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    /**
     * 设置适配器
     *
     * @param adapter 适配器
     */
    @Override
    public void setAdapter(Adapter adapter) {
        setAdapter(adapter, new SimpleLoadFooterAdapter(getContext()));
    }

    /**
     * 设置适配器以及足部适配
     *
     * @param adapter        适配器
     * @param loadMoreFooter 足部适配
     */
    public void setAdapter(Adapter adapter, LoadFooterAdapter loadMoreFooter) {
        loadMoreAdapter = new LoadMoreAdapter(getContext(), adapter, loadMoreFooter);
        loadMoreAdapter.setLoadMore(loadMore);
        loadMoreAdapter.setLayoutState(layoutState);
        //设置touch
        itemTouchCallback.setLoadMoreAdapter(loadMoreAdapter);
        super.setAdapter(loadMoreAdapter);
    }

    /**
     * 设置layoutmanager
     *
     * @param layout layoutmanager
     */
    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (layout instanceof GridLayoutManager) {
            layoutState = LAYOUT_STATE_GRID;
            //重置足部所占格数
            resetGridLayoutFooterSize();
        } else if (layout instanceof LinearLayoutManager) {
            layoutState = LAYOUT_STATE_LINEAR;
        } else if (layout instanceof StaggeredGridLayoutManager) {
            layoutState = LAYOUT_STATE_STAGGERED;
        }
        if (null != loadMoreAdapter) {
            loadMoreAdapter.setLayoutState(layoutState);
        }
    }

    /**
     * 滚动
     *
     * @param dx X轴滚动距离
     * @param dy Y轴滚动距离
     */
    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        boolean scrollUp = dy > 0;
        boolean scrollLeft = dx > 0;
        //重置加载失败的状态
        if ((!scrollUp || !scrollLeft) && loadState == LOAD_STATE_FAIL) {
            loadState = LOAD_STATE_NORMAL;
            loadMoreAdapter.modifyState(loadState);
        }
        //滑动到底部，并且满足其他条件，则发起加载更多请求
        if (computeVerticalScrollExtent() + computeVerticalScrollOffset() >= computeVerticalScrollRange()
                && loadMore && (scrollUp || scrollLeft) && loadState == LOAD_STATE_NORMAL
                && null != loadMoreAdapter && null != onLoadListener) {
            onLoadListener.loadMore();
            setLoadState(LOAD_STATE_LOADING);
        }
    }

    /**
     * 重置交错布局的足部size
     */
    private void resetGridLayoutFooterSize() {
        final GridLayoutManager gridLayoutManager = ((GridLayoutManager) getLayoutManager());
        final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (loadMore && position == loadMoreAdapter.getItemCount() - 1) {
                    return gridLayoutManager.getSpanCount();
                } else if (null != spanSizeLookup) {
                    return spanSizeLookup.getSpanSize(position);
                } else {
                    return 1;
                }
            }
        });
    }
}