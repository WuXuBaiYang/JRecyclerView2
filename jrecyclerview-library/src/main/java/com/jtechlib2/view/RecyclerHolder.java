package com.jtechlib2.view;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用viewholder
 */
public class RecyclerHolder extends RecyclerView.ViewHolder {
    private final static int MAX_VIEW_INSTANCE = 20;

    private final SparseArray<View> mViews = new SparseArray<>(MAX_VIEW_INSTANCE);

    public RecyclerHolder(View itemView) {
        super(itemView);
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId 资源id
     * @param <T>    泛型
     * @return 返回实例化后的对象
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 获取imageview
     *
     * @param viewId 资源id
     * @param <T>    泛型
     * @return 返回实例化后的对象
     */
    public <T extends ImageView> T getImageView(int viewId) {
        return getView(viewId);
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId 资源id
     * @param text 文本
     * @return holder对象
     */
    public RecyclerHolder setText(int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId 资源id
     * @param drawableId 图片id
     * @return holder对象
     */
    public RecyclerHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId 资源id
     * @param bm 图片
     * @return holder对象
     */
    public RecyclerHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 设置视图为不可见
     *
     * @param viewId 资源id
     * @return holder对象
     */
    public RecyclerHolder hideViewGone(int viewId) {
        getView(viewId).setVisibility(View.GONE);
        return this;
    }

    /**
     * 设置视图为不可见（invisible）
     *
     * @param viewId 资源id
     * @return holder对象
     */
    public RecyclerHolder hideViewInvisible(int viewId) {
        getView(viewId).setVisibility(View.INVISIBLE);
        return this;
    }

    /**
     * 显示视图
     *
     * @param viewId 资源id
     * @return holder对象
     */
    public RecyclerHolder showView(int viewId) {
        getView(viewId).setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置视图是否显示
     *
     * @param viewId 资源id
     * @param isVisible 是否显示
     * @return holder对象
     */
    public RecyclerHolder setViewVisible(int viewId, boolean isVisible) {
        if (isVisible) {
            showView(viewId);
        } else {
            hideViewGone(viewId);
        }
        return this;
    }

    /**
     * 设置视图是否显示
     *
     * @param viewId 资源id
     * @param isVisible 是否显示
     * @return holder对象
     */
    public RecyclerHolder setViewVisibleInvisible(int viewId, boolean isVisible) {
        if (isVisible) {
            showView(viewId);
        } else {
            hideViewInvisible(viewId);
        }
        return this;
    }

    /**
     * 设置点击事件
     *
     * @param viewId 资源id
     * @param onClickListener 点击监听
     * @return holder对象
     */
    public RecyclerHolder setClickListener(int viewId, View.OnClickListener onClickListener) {
        getView(viewId).setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 设置长点击事件
     *
     * @param viewId 资源id
     * @param onLongClickListener 长点击事件
     * @return holder对象
     */
    public RecyclerHolder setLongClickListener(int viewId, View.OnLongClickListener onLongClickListener) {
        getView(viewId).setOnLongClickListener(onLongClickListener);
        return this;
    }

    /**
     * 设置是否可用
     *
     * @param viewId 资源id
     * @param enabled 是否可用
     * @return holder对象
     */
    public RecyclerHolder setEnabled(int viewId, boolean enabled) {
        getView(viewId).setEnabled(enabled);
        return this;
    }

    /**
     * 设置选择状态
     *
     * @param viewId 资源id
     * @param selected 是否选择
     * @return holder对象
     */
    public RecyclerHolder setSelected(int viewId, boolean selected) {
        getView(viewId).setSelected(selected);
        return this;
    }

    /**
     * 设置背景图片
     *
     * @param viewId 视图id
     * @param resId 资源id
     * @return holder对象
     */
    public RecyclerHolder setBackgroundResource(int viewId, int resId) {
        getView(viewId).setBackgroundResource(resId);
        return this;
    }
}