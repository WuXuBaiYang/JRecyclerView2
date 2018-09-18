package com.jtechlib2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.jtechlib2.listener.OnItemTouchToMove;
import com.jtechlib2.view.RecyclerHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * JRecyclerView适配器
 *
 * @param <VH> 继承自recyclerholder
 * @param <D>  数据类型
 */
public abstract class BaseJAdapter<VH extends RecyclerHolder, D> extends RecyclerView.Adapter<VH> {
    /**
     * context对象
     */
    private Context context;
    /**
     * 数据集合
     */
    private List<D> realDatas;
    /**
     * 起始页码
     */
    private int startPage = 1;
    /**
     * 单页数据量
     */
    private int displayNumber = 20;
    /**
     * 当前页码
     */
    private int currentPage = startPage;
    /**
     * item触摸后可拖动监听
     */
    private OnItemTouchToMove onItemTouchToMove;

    /**
     * 主构造
     *
     * @param context context
     */
    public BaseJAdapter(Context context) {
        this.context = context;
        realDatas = new ArrayList<>();
    }

    /**
     * 得到上下文对象
     *
     * @return context
     */
    public Context getContext() {
        return context;
    }

    public void setOnItemTouchToMove(OnItemTouchToMove onItemTouchToMove) {
        this.onItemTouchToMove = onItemTouchToMove;
    }

    /**
     * 添加一个视图的监听为触摸可拖动换位
     *
     * @param holder 视图持有类
     * @param viewId 视图的id
     */
    public void addItemTouchToMove(VH holder, int viewId) {
        addItemTouchToMove(holder, holder.getView(viewId));
    }

    /**
     * 添加一个视图的监听为触摸可拖动换位
     *
     * @param holder 视图持有类
     * @param view   视图对象
     */
    public void addItemTouchToMove(final VH holder, View view) {
        if (null != view) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (null != onItemTouchToMove) {
                        onItemTouchToMove.itemTouchToMove(holder);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 获取item对象
     *
     * @param position 位置
     * @return item数据对象
     */
    public D getItem(int position) {
        if (null != realDatas && realDatas.size() > position) {
            return realDatas.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (null != realDatas) {
            return realDatas.size();
        }
        return 0;
    }

    /**
     * 根据标记获取页码（刷新或者加载更多）
     *
     * @param loadMore 是否为加载更多
     * @return 页码
     */
    public int getPage(boolean loadMore) {
        if (loadMore) {
            return nextPage();
        } else {
            return startPage;
        }
    }

    /**
     * 根据标记设置页码
     *
     * @param loadMore 是否为加载更多
     * @return 页码
     */
    public int setpage(boolean loadMore) {
        if (loadMore) {
            return pagePlus();
        } else {
            return resetPage();
        }
    }

    /**
     * 获取起始页码
     *
     * @return 起始页码
     */
    public int getStartPage() {
        return startPage;
    }

    /**
     * 得到下一页的页码
     *
     * @return 下一页页码
     */
    public int nextPage() {
        return currentPage + 1;
    }

    /**
     * 页码增加
     *
     * @return 增加后的页码
     */
    public int pagePlus() {
        return currentPage++;
    }

    /**
     * 将页码重置
     *
     * @return 重置后的页码
     */
    public int resetPage() {
        this.currentPage = startPage;
        return currentPage;
    }

    public int getDisplayNumber() {
        return displayNumber;
    }

    /**
     * 设置起始页码
     *
     * @param startPage 起始页码
     */
    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    /**
     * 设置单页数据量
     *
     * @param displayNumber 单页数据量
     */
    public void setDisplayNumber(int displayNumber) {
        this.displayNumber = displayNumber;
    }

    /**
     * 获取真实数据
     *
     * @return 真实数据集合
     */
    public List<D> getRealDatas() {
        return realDatas;
    }

    /**
     * 获取真实数据的长度
     *
     * @return 真实数据长度
     */
    public int getRealDatasSize() {
        return null != getRealDatas() ? getRealDatas().size() : 0;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return createHolder(LayoutInflater.from(context), parent, viewType);
    }

    /**
     * 创建item的根视图
     *
     * @param inflater LayoutInflater
     * @param parent   父视图
     * @param viewType 视图类型
     * @return viewholder
     */
    public abstract VH createHolder(LayoutInflater inflater, ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        convert(holder, getItemViewType(position), position);
    }

    /**
     * Recycler适配器填充方法
     *
     * @param position 位置
     * @param viewType 视图类型
     * @param holder   viewholder
     */
    public abstract void convert(VH holder, int viewType, int position);

    /**
     * 设置数据
     *
     * @param datas 数据
     */
    public void setDatas(Collection<D> datas) {
        setDatas(datas, false);
    }

    /**
     * 设置数据
     *
     * @param datas    数据
     * @param loadMore 是否为加载更多
     */
    public void setDatas(Collection<D> datas, boolean loadMore) {
        if (loadMore) {
            addDatas(datas);
        } else {
            notifyItemRangeRemoved(0, realDatas.size());
            realDatas = new ArrayList<>(datas);
            notifyItemRangeChanged(0, realDatas.size());
        }
        setpage(loadMore);
    }

    /**
     * 添加数据集合
     *
     * @param datas 数据
     */
    public void addDatas(Collection<D> datas) {
        addDatas(getRealDatasSize(), datas);
    }

    /**
     * 添加数据集合
     *
     * @param index 位置
     * @param datas 数据
     */
    public void addDatas(int index, Collection<D> datas) {
        if (null != datas && null != realDatas) {
            realDatas.addAll(index, datas);
            notifyItemRangeInserted(index, datas.size());
        }
    }

    /**
     * 添加单条数据
     *
     * @param data 数据
     */
    public void addData(D data) {
        if (null != data && null != realDatas) {
            addData(getItemCount(), data);
        }
    }

    /**
     * 添加数据到指定位置
     *
     * @param index 指定位置
     * @param data  数据
     */
    public void addData(int index, D data) {
        if (null != data && null != realDatas) {
            realDatas.add(index, data);
            notifyItemInserted(index);
        }
    }

    /**
     * 移除数据
     *
     * @param position 数据位置
     */
    public void removeData(int position) {
        if (null != realDatas && realDatas.size() > position) {
            realDatas.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 移动数据
     *
     * @param fromPosition 原始位置
     * @param toPosition   目标位置
     */
    public void moveData(int fromPosition, int toPosition) {
        Collections.swap(realDatas, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * 更新某一项数据
     *
     * @param position 下标
     * @param data     数据对象
     */
    public void updataItem(int position, D data) {
        if (null != data && null != realDatas && realDatas.size() > position) {
            realDatas.set(position, data);
            notifyItemChanged(position);
        }
    }
}