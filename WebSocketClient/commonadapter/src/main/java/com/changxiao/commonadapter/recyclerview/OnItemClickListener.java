package com.changxiao.commonadapter.recyclerview;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Chang.Xiao on 2016/4/12 18:22.
 *
 * @version 1.0
 */
public interface OnItemClickListener<T> {

    void onItemClick(ViewGroup parent, View view, T t, int position);

    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}
