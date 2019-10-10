package com.changxiao.example.timeline.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.changxiao.example.timeline.R;

/**
 * Created by Chang.Xiao on 2016/3/19 21:54.
 *
 * @version 1.0
 */
public class TimeLineMarker extends View {

    private int mLineSize;
    private int mMarkerSize;
    private Drawable mBeginLine;
    private Drawable mEndLine;
    private Drawable mMarkerDrawable;
    Rect bounds;

    public TimeLineMarker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // 获取自定义属性
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TimeLineMarker);
        mMarkerSize = a.getDimensionPixelSize(R.styleable.TimeLineMarker_markerSize, 25);
        mLineSize = a.getDimensionPixelSize(R.styleable.TimeLineMarker_lineSize, 15);
        mBeginLine = a.getDrawable(R.styleable.TimeLineMarker_beginLine);
        mEndLine = a.getDrawable(R.styleable.TimeLineMarker_endLine);
        mMarkerDrawable = a.getDrawable(R.styleable.TimeLineMarker_marker);
        a.recycle(); // TypedArray回收
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量本view的宽高以及里面控件的宽高
        int w = mMarkerSize + getPaddingLeft() + getPaddingRight();
        int h = mMarkerSize + getPaddingTop() + getPaddingBottom();

        /**由于受父容器的影响，真实的宽高不一定是这么高
         * wideMeasureSpec:
         * 1) size;
         * 2)mode:
         * MeasureSpec.AT_MOST, match_parent
         * MeasureSpec.EXACTLY, 确切的值
         * MeasureSpec.UNSPECIFIED warp_content
         */
        // 通过系统一个方法做决策，决定最终view的宽高
        int widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
        int heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);
        // 设置最终的测量结果
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 当view显示的时候回调
        // 定位这几个drawable的坐标，然后绘制
        initDrawable();
    }

    private void initDrawable() {
        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();

        int width = getWidth();
        int height = getHeight();

        // 容器宽高
        int cWidth = width - pLeft - pRight;
        int cHeight = height - pTop - pBottom;

        if (mMarkerDrawable != null) {
            /**
             * 画的marker的实际宽高不一定就是你的属性值，肯呢个跟容器的大小有关
             */
            int markerSize = Math.min(mMarkerSize, Math.min(cWidth, cHeight));

            mMarkerDrawable.setBounds(pLeft, pTop, pLeft + markerSize, pTop + markerSize);
            bounds = mMarkerDrawable.getBounds();
        }
        int lineLeft = bounds.centerX() - (mLineSize>>1); // mLineSize>>1(二进制位移操作)相当于mLineSize/2
        if (mBeginLine != null) {
            mBeginLine.setBounds(lineLeft, 0, lineLeft + mLineSize, bounds.top);
        }
        if (mEndLine != null) {
            mEndLine.setBounds(lineLeft, bounds.bottom, lineLeft + mLineSize, height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBeginLine != null) {
            mBeginLine.draw(canvas);
        }
        if (mEndLine != null) {
            mEndLine.draw(canvas);
        }
        if (mMarkerDrawable != null) {
            mMarkerDrawable.draw(canvas);
        }
    }

    public void setLineSize(int lineSize) {
        this.mLineSize = lineSize;
    }

    public void setMarkerSize(int markerSize) {
        this.mMarkerSize = markerSize;
    }

    public void setBeginLine(Drawable beginLine) {
        this.mBeginLine = beginLine;
        initDrawable();
        invalidate();
    }

    public void setEndLine(Drawable endLine) {
        this.mEndLine = endLine;
        initDrawable();
        invalidate();
    }

    public void setMarkerDrawable(Drawable markerDrawable) {
        this.mMarkerDrawable = markerDrawable;
        initDrawable();
        invalidate();
    }

    public Drawable getMarkerDrawable() {
        return mMarkerDrawable;
    }

}
