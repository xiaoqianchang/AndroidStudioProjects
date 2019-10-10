package com.changxiao.example.demo_recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Chang.Xiao on 2016/3/17 15:01.
 *
 * @version 1.0
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    protected List<String> mDatas;
    protected LayoutInflater inflater;
    protected OnItemClickListener mOnItemClickListener;

    /**
     * 点击事件监听
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public MyAdapter(Context context, List<String> mDatas) {
        this.mDatas = mDatas;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * 创建条目缓存视图
     *
     * ListView getView+ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.main_item, parent, false);
        return new MyViewHolder(itemView);
    }

    /**
     * 绑定数据到条目中
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
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

    /**
     * 数据的长度
     * @return
     */
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    // 这个ViewHolder相当于ListView的ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_num;

        // 这个ViewHolder相当于ListView的ViewHolder
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);
        }
    }

    /**
     * 添加Item
     * @param position
     */
    public void addData(int position) {
        mDatas.add(position, "Insert One");
        notifyItemInserted(position);
    }

    /**
     * 删除Item
     * @param position
     */
    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

}
