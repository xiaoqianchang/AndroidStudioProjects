package com.changxiao.commonadapter.recyclerview.support;

/**
 * 1.header所对应的布局文件
 * 2.显示header的title对应的TextView
 * 3.显示的title是什么（一般肯定根据Bean生成）
 * <p/>
 * Created by Chang.Xiao on 2016/4/12 15:54.
 *
 * @version 1.0
 */
public interface SectionSupport<T> {

    /**
     * Header布局的layoutId
     *
     * @return
     */
    int getSectionHeaderLayoutId();

    /**
     * Header布局中title的viewId
     *
     * @return
     */
    int getSectionTitleTextViewId();

    /**
     * Header布局中title的内容
     *
     * @param t 实体Bean
     * @return title显示内容
     */
    String getTitle(T t);
}
