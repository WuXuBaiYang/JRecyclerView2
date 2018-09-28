package com.jtechlib2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jtechlib2.view.RecyclerHolder;


/**
 * 通用适配器
 * Created by jianghan on 2016/9/9.
 */
public abstract class RecyclerAdapter<D> extends BaseJAdapter<RecyclerHolder, D> {

    public RecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerHolder createHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new RecyclerHolder(createView(inflater, parent, viewType));
    }

    protected abstract View createView(LayoutInflater layoutInflater, ViewGroup viewGroup, int viewType);

    @Override
    public void convert(RecyclerHolder holder, int viewType, int position) {
        convert(holder, getItem(position), position);
    }

    protected abstract void convert(RecyclerHolder holder, D model, int position);
}