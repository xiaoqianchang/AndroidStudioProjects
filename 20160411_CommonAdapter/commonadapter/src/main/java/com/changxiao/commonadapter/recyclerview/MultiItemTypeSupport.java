package com.changxiao.commonadapter.recyclerview;

/**
 * Created by Chang.Xiao on 2016/4/12 15:37.
 *
 * @version 1.0
 */
public interface MultiItemTypeSupport<T> {

    /**
     * 指明不同的Bean返回什么itemViewType
     *
     * @param position
     * @param t
     * @return
     */
    int getItemViewType(int position, T t);

    /**
     * 不同的itemView所对应的layoutId
     *
     * @param itemType
     * @return
     */
    int getLayoutId(int itemType);
}
