package com.changxiao.example.timeline.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.changxiao.example.timeline.ItemType;
import com.changxiao.example.timeline.R;
import com.changxiao.example.timeline.model.TimeLineModel;
import com.changxiao.example.timeline.widget.TimeLineMarker;

import java.util.List;

/**
 * Created by Chang.Xiao on 2016/3/19 22:23.
 *
 * 方法执行顺序getItemViewType-->onCreateViewHolder加载item布局-->自定义TimeLineMarker的构造方法-->TimeLineViewHolder-->onBindViewHolder
 *
 * @version 1.0
 */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {

    private List<TimeLineModel> mDatas;
    private LayoutInflater mInflater;
    private int[] mColors;

    public TimeLineAdapter(Context context, List<TimeLineModel> mDatas) {
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        mColors = new int[] { R.color.colorMarker1, R.color.colorMarker2, R.color.colorMarker3,
                R.color.colorMarker4, R.color.colorMarker5, R.color.colorMarker6,
                R.color.colorMarker7, R.color.colorMarker8, R.color.colorMarker9,
                R.color.colorMarker10  };
    }

    /**
     * 得到View的类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        int size = mDatas.size() - 1;
        if (size == 0) {
            return ItemType.ATOM.getValue();
        } else if (position == 0) {
            return ItemType.START.getValue();
        } else if (position == size) {
            return ItemType.END.getValue();
        } else {
            return ItemType.NORMAL.getValue();
        }
    }

    /**
     * 创建、渲染视图并初始化(每个item執行一次)
     * @param parent
     * @param viewType   ListView的每一个条目布局唯一，然而Recycler的每一个条目可以有不同布局
     * @return
     */
    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.time_line_item, null);
        return new TimeLineViewHolder(view, viewType);
    }

    /**
     * 给渲染的视图绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {
        TimeLineModel timeLineModel = mDatas.get(position);
        holder.setData(timeLineModel);
    }

    /**
     * 得到条目的数量
     * @return
     */
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    // 这个ViewHolder相当于ListView的ViewHolder
    class TimeLineViewHolder extends RecyclerView.ViewHolder {

        private TextView name;

        // 这个ViewHolder相当于ListView的ViewHolder
        public TimeLineViewHolder(View itemView, int type) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_time_line_txt);
            // 设置时间轴的效果
            TimeLineMarker timeLineMarker = (TimeLineMarker) itemView.findViewById(R.id.item_time_line_mark);
            if (type == ItemType.ATOM.getValue()) {
                timeLineMarker.setBeginLine(null);
                timeLineMarker.setEndLine(null);
            } else if (type == ItemType.START.getValue()) {
                timeLineMarker.setBeginLine(null);
            } else if (type == ItemType.END.getValue()) {
                timeLineMarker.setEndLine(null);
            }

            // 动态设置marker的颜色(drawable的shap)
            GradientDrawable myGrad = (GradientDrawable) timeLineMarker.getMarkerDrawable();
            int ram = (int) (Math.random() * 10);
            myGrad.setColor(itemView.getContext().getResources().getColor(mColors[ram]));
        }

        public void setData(TimeLineModel timeLineModel) {
            name.setText("名字：" + timeLineModel.getName() + "  年龄：" + timeLineModel.getAge());
        }
    }
}
