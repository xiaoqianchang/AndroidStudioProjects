package com.changxiao.commonadapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.changxiao.commonadapter.ViewHolder;

import java.util.List;

/**
 * 单种ViewItemType的通用Adapter
 * <p/>
 * Created by Chang.Xiao on 2016/4/11 22:17.
 *
 * @version 1.0
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    /**
     * item点击事件
     */
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 构建CommonAdapter
     *
     * @param context  上下文
     * @param layoutId item的布局文件layoutId
     * @param datas    数据
     */
    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mLayoutId = layoutId;
        this.mDatas = datas;
    }

    /**
     * 得到View的类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * 创建、渲染视图并初始化(每个item執行一次)
     *
     * @param parent
     * @param viewType ListView的每一个条目布局唯一，然而Recycler的每一个条目可以有不同布局
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // onCreateViewHolder时，通过layoutId即可利用我们的通用的ViewHolder生成实例。
        ViewHolder viewHolder = ViewHolder.get(mContext, null, parent, mLayoutId, -1);
        setListener(parent, viewHolder, viewType);
        return viewHolder;
    }

    protected int getPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition();
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType))
            return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = getPosition(viewHolder);
                    mOnItemClickListener.onItemClick(parent, v, mDatas.get(position), position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = getPosition(viewHolder);
                    return mOnItemClickListener.onItemLongClick(parent, v, mDatas.get(position), position);
                }
                return false;
            }
        });
    }

    /**
     * 给渲染的视图绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.updatePosition(position);
        /*
        onBindViewHolder这里主要用于数据、事件绑定，我们这里直接抽象出去，让用户去操作。
        可以看到我们修改了下参数，用户可以拿到当前Item所需要的对象和viewHolder去操作。
         */
        convert(holder, mDatas.get(position));
    }

    protected abstract void convert(ViewHolder holder, T t);

    /**
     * 得到条目的数量
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}
