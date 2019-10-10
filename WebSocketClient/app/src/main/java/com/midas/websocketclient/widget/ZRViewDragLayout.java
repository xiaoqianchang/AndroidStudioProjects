package com.midas.websocketclient.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.midas.websocketclient.R;

/**
 *
 *
 * Created by Chang.Xiao on 2016/5/17.
 * @version 1.0
 */
public class ZRViewDragLayout extends LinearLayout {

    private ViewDragHelper mViewDragHelper;
    /**
     * 手势事件类
     */
    private GestureDetectorCompat mGestureDetectorCompat;
    private View contentView;
    private View actionView;
    /**
     * 左滑最大距离(为actionView的宽度)
     */
    private int dragDistance;
    private final double AUTO_OPEN_SPEED_LIMIT = 800.0;
    /**
     * 滑动距离
     */
    private int draggedX;
//    private ViewDragListener mViewDragListener;

    public ZRViewDragLayout(Context context) {
        this(context, null);
        init();
    }

    public ZRViewDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
        init();
    }

    public ZRViewDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 创建ViewDragHelper的实例，第一个参数是ViewGroup，传自己，
        // 第二个参数就是滑动灵敏度的意思,可以随意设置，第三个是回调
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
        mGestureDetectorCompat = new GestureDetectorCompat(getContext(), new XScrollDetector());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        actionView = getChildAt(1);
        actionView.setVisibility(GONE);

        // 点击内容item
        /*contentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "aaaaa", Toast.LENGTH_SHORT).show();
//                if (mOnItemClickListener != null) {
//                    mOnItemClickListener.onItemClick(v);
//                }
            }
        });*/
        // 点击删除
        actionView.findViewById(R.id.menu_delete).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "sssssss", Toast.LENGTH_SHORT).show();
//                if (mOnDeleteClickListener != null) {
//                    mOnDeleteClickListener.onDeleteClick(v);
//                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        dragDistance = actionView.getMeasuredWidth();
    }

    /**
     * DragHelper回调
     */
    private class DragHelperCallback extends ViewDragHelper.Callback {

        /**
         * 根据返回结果决定当前child是否可以拖拽
         * @param child child 当前被拖拽的view
         * @param pointerId pointerId 区分多点触摸的id
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == contentView || child == actionView;
        }

        /**
         * 位置改变时回调，常用于滑动时更改scale进行缩放等效果
         * @param changedView
         * @param left
         * @param top
         * @param dx 横向滑动的加速度
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            draggedX = left;
            if (changedView == contentView) {
                actionView.offsetLeftAndRight(dx);
            } else {
                contentView.offsetLeftAndRight(dx);
            }
            if (actionView.getVisibility() == View.GONE) {
                actionView.setVisibility(View.VISIBLE);
            }
            invalidate();
        }

        /**
         *
         * @param child
         * @param left 代表你将要移动到的位置的坐标,返回值就是最终确定的移动的位置,
         *              判断如果这个坐标在layout之内,那我们就返回这个坐标值，
         *              不能让他超出这个范围, 除此之外就是如果你的layout设置了padding的话，
         *              让子view的活动范围在padding之内的
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == contentView) {
                final int leftBound = getPaddingLeft();
                final int minLeftBound = -leftBound - dragDistance;
                final int newLeft = Math.min(Math.max(minLeftBound, left), 0);
                return newLeft;
            } else {
                final int minLeftBound = getPaddingLeft() + contentView.getMeasuredWidth() - dragDistance;
                final int maxLeftBound = getPaddingLeft() + contentView.getMeasuredWidth() + getPaddingRight();
                final int newLeft = Math.min(Math.max(left, minLeftBound), maxLeftBound);
                return newLeft;
            }
        }

        /**
         * 返回拖拽的范围，不对拖拽进行真正的限制，仅仅决定了动画执行速度
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return dragDistance;
        }

        /**
         * 拖动结束后调用
         * @param releasedChild
         * @param xvel 水平方向的速度  向右为正
         * @param yvel 竖直方向的速度  向下为正
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            boolean settleToOpen = false;
            if (xvel > AUTO_OPEN_SPEED_LIMIT) {
                settleToOpen = false;
            } else if (xvel < -AUTO_OPEN_SPEED_LIMIT) {
                settleToOpen = true;
            } else if (draggedX <= -dragDistance / 2) {
                settleToOpen = true;
            } else if (draggedX > -dragDistance / 2) {
                settleToOpen = false;
            }

            final int settleDestX = settleToOpen ? -dragDistance : 0;
            mViewDragHelper.smoothSlideViewTo(contentView, settleDestX, 0);
            ViewCompat.postInvalidateOnAnimation(ZRViewDragLayout.this);
        }
    }

    /**
     * 事件拦截下来,相当于把自定义控件的事件交给ViewDragHelper去处理
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mViewDragHelper.cancel();
            return false;
        }
        if(mViewDragHelper.shouldInterceptTouchEvent(ev) && mGestureDetectorCompat.onTouchEvent(ev)) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**上次触摸的X坐标*/
    private float mLastX = -1;
    /**上次触摸的Y坐标*/
    private float mLastY = -1;
    /**是否正在左右滑动*/
    private boolean isSliding=false;
    /**
     * 事件监听，交给ViewDragHelper处理
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX=ev.getRawX();
                mLastY=ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                isSliding=true;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        mViewDragHelper.processTouchEvent(ev);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 手势监听回调,SimpleOnGestureListener为了不用重写那么多监听的帮助类
     */
    private class XScrollDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // 判断是否是滑动的x距离>y距离
            return Math.abs(distanceY) <= Math.abs(distanceX);
//            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(@Nullable OnItemClickListener l) {
        if (!isClickable()) {
            setClickable(true);
        }
        this.mOnItemClickListener = l;
    }
    public interface OnItemClickListener {

        /**
         * Called when a content item has been clicked.
         * @param view
         */
        void onItemClick(View view);
    }

    private OnDeleteClickListener mOnDeleteClickListener;
    public void setOnDeleteClickListener(@Nullable OnDeleteClickListener listener) {
        this.mOnDeleteClickListener = listener;
    }
    public interface OnDeleteClickListener {

        /**
         * Called when a delete has been clicked.
         * @param view
         */
        void onDeleteClick(View view);
    }

}
