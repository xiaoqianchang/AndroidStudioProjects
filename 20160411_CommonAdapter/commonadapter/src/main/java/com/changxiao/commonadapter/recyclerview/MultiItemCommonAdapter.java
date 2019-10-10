package com.changxiao.commonadapter.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import com.changxiao.commonadapter.ViewHolder;

import java.util.List;

/**
 * 对多种ItemViewType支持的适配器
 *
 * Created by Chang.Xiao on 2016/4/12 15:41.
 *
 * @version 1.0
 */
public abstract class MultiItemCommonAdapter<T> extends CommonAdapter<T> {

    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

    /**
     * 构建CommonAdapter
     *
     * @param context  上下文
     * @param datas 数据
     * @param multiItemTypeSupport
     */
    public MultiItemCommonAdapter(Context context, List<T> datas,
                                  MultiItemTypeSupport<T> multiItemTypeSupport) {
        super(context, -1, datas);
        this.mMultiItemTypeSupport = multiItemTypeSupport;
        if (mMultiItemTypeSupport == null) {
            throw new IllegalArgumentException("the mMultiItemTypeSupport can not be null.");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mMultiItemTypeSupport != null) {
            return mMultiItemTypeSupport.getItemViewType(position, mDatas.get(position));
        }
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mMultiItemTypeSupport != null) {
            int layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
            ViewHolder holder = ViewHolder.get(mContext, null, parent, layoutId, -1);
            setListener(parent, holder, viewType);
            return holder;
        }
        return super.onCreateViewHolder(parent, viewType);
    }
}
