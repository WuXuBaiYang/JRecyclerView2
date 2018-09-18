package com.jtechlib2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jtechlib2.R;
import com.jtechlib2.view.RecyclerHolder;


/**
 * 默认的加载更多的足部
 * Created by wuxubaiyang on 2016/3/7.
 */
public class SimpleLoadFooterAdapter extends LoadFooterAdapter {
    private Context context;

    public SimpleLoadFooterAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getFooterView(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.load_footer, parent, false);
        view.setTag("footer");
        return view;
    }

    @Override
    public void loadFailState(RecyclerHolder recyclerHolder) {
        //设置加载失败的文本
        recyclerHolder.setText(R.id.textview_load_footer, context.getResources().getString(R.string.load_more_state_fail));
        //隐藏progress
        recyclerHolder.hideViewInvisible(R.id.progressbar_load_footer);
    }

    @Override
    public void loadingState(RecyclerHolder recyclerHolder) {
        //清空文本
        recyclerHolder.setText(R.id.textview_load_footer, "");
        //显示porgress
        recyclerHolder.showView(R.id.progressbar_load_footer);
    }

    @Override
    public void noMoreDataState(RecyclerHolder recyclerHolder) {
        //设置无更多数据文本
        recyclerHolder.setText(R.id.textview_load_footer, context.getResources().getString(R.string.load_more_state_nomoredata));
        //隐藏progress
        recyclerHolder.hideViewInvisible(R.id.progressbar_load_footer);
    }

    @Override
    public void normalState(RecyclerHolder recyclerHolder) {
        //设置常态文本
        recyclerHolder.setText(R.id.textview_load_footer, "");
        //隐藏progress
        recyclerHolder.hideViewInvisible(R.id.progressbar_load_footer);
    }
}