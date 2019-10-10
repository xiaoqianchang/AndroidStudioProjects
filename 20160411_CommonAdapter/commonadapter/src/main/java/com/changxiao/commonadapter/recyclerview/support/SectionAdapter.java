package com.changxiao.commonadapter.recyclerview.support;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.changxiao.commonadapter.ViewHolder;
import com.changxiao.commonadapter.recyclerview.MultiItemCommonAdapter;
import com.changxiao.commonadapter.recyclerview.MultiItemTypeSupport;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 我们的分类header是多种ItemViewType的一种，那么直接继承MultiItemCommonAdapter实现
 *
 * Created by Chang.Xiao on 2016/4/12 15:59.
 *
 * @version 1.0
 */
public abstract class SectionAdapter<T> extends MultiItemCommonAdapter<T> {

    private SectionSupport<T> mSectionSupport;
    private static final int TYPE_SECTION = 0;
    // head title集合
    private LinkedHashMap<String, Integer> mSections;

    private MultiItemTypeSupport<T> headerItemTypeSupport = new MultiItemTypeSupport<T>() {
        // 根据位置判断，如果当前是header所在位置，返回header类型常量；否则返回1.
        @Override
        public int getItemViewType(int position, T t) {
            int positionVal = getIndexForPosition(position);
            return mSections.values().contains(position) ? TYPE_SECTION :
                    mMultiItemTypeSupport.getItemViewType(positionVal, t);
        }

        // 如果type是header类型，则返回mSectionSupport.sectionHeaderLayoutId()；否则则返回mLayout.
        @Override
        public int getLayoutId(int itemType) {
            if (itemType == TYPE_SECTION) {
                return mSectionSupport.getSectionHeaderLayoutId();
            } else {
                return mMultiItemTypeSupport.getLayoutId(itemType);
            }
        }
    };

    @Override
    public int getItemViewType(int position) {
        return mMultiItemTypeSupport.getItemViewType(position, null);
    }

    final RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            findSections();
        }
    };

    public SectionAdapter(Context context, int layoutId, List<T> datas, SectionSupport<T> sectionSupport) {
        super(context, datas, null);
        mLayoutId = layoutId;
        mMultiItemTypeSupport = headerItemTypeSupport;
        mSectionSupport = sectionSupport;
        mSections = new LinkedHashMap<String, Integer>();
        findSections();
        registerAdapterDataObserver(observer);
    }

    @Override
    protected boolean isEnabled(int viewType) {
        if (viewType == TYPE_SECTION) {
            return false;
        }
        return super.isEnabled(viewType);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        unregisterAdapterDataObserver(observer);
    }

    public void findSections() {
        int n = mDatas.size();
        int nSections = 0;
        mSections.clear();
        for (int i = 0; i < n; i++) {
            String sectionName = mSectionSupport.getTitle(mDatas.get(i));
            if (!mSections.containsKey(sectionName)) {
                mSections.put(sectionName, i + nSections);
                nSections++;
            }
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + mSections.size();
    }

    public int getIndexForPosition(int position) {
        int nSection = 0;
        Set<Map.Entry<String, Integer>> entrySet = mSections.entrySet();
        for (Map.Entry<String, Integer> entry : entrySet) {
            if (entry.getValue() < position) {
                nSection++;
            }
        }
        return position - nSection;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        position = getIndexForPosition(position);
        if (holder.getItemViewType() == TYPE_SECTION) {
            holder.setText(mSectionSupport.getSectionTitleTextViewId(), mSectionSupport.getTitle(mDatas.get(position)));
            return;
        }
        super.onBindViewHolder(holder, position);
    }
}
