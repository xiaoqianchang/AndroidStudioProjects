package com.changxiao.example.demo_recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chang.Xiao on 2016/3/17 15:01.
 *
 * @version 1.0
 */
public class StaggerAdapter extends MyAdapter {

    private List<Integer> mHeights;

    public StaggerAdapter(Context context, List<String> mDatas) {
        super(context, mDatas);
        mHeights = new ArrayList<Integer>();
        for (int i = 0; i < mDatas.size(); i++) {
            mHeights.add((int) (100 + Math.random() * 300));
        }
    }

    /**
     * 绑定数据到条目中
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // 改变每一个item的高度
        ViewGroup.LayoutParams params = holder.tv_num.getLayoutParams();
        params.height = mHeights.get(position);
        holder.tv_num.setLayoutParams(params);

        holder.tv_num.setText(mDatas.get(position));

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {
            holder.tv_num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.tv_num, pos);
                }
            });
            holder.tv_num.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.tv_num, pos);
                    return false;
                }
            });
        }

    }

}
