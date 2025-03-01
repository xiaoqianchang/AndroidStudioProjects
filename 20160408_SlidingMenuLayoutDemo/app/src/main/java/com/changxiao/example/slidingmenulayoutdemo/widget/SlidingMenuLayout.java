package com.changxiao.example.slidingmenulayoutdemo.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

/**
 * 注意：
 * 1.使用SlidingLayout这个布局的前提条件，必须为这个布局提供两个子元素，第一个元素会作为左边布局偏移出屏幕，
 * 第二个元素会作为右边布局显示在屏幕上。
 * 2.setScrollEvent方法也就是提供了一个注册接口，touch事件将会注册到传入的View上，这个方法接收一个View
 * 作为参数，然后为这个View绑定了一个touch事件。
 * 3.isBindBasicLayout。这个方法就是判断了一下注册touch事件的View是不是四个基本布局之一，如果是就返回true，
 * 否则返回false。这个方法在整个SlidingLayout中起着非常重要的作用，主要用于控制onTouch事件是返回true还是
 * false，这将影响到布局当中的View的功能是否可用。
 * <p/>
 * 再总结一下吧，向Activity中加入滑动菜单功能只需要两步：
 * 1. 在Acitivty的layout中引入我们自定义的布局，并且给这个布局要加入两个直接子元素。
 * 2. 在Activity中通过setScrollEvent方法，给一个View注册touch事件。
 * <p/>
 * Created by Chang.Xiao on 2016/4/8 18:24.
 *
 * @version 1.0
 */
public class SlidingMenuLayout extends LinearLayout implements View.OnTouchListener {

    /**
     * 滚动显示和隐藏左侧布局时，手指滑动需要达到的速度。
     */
    private static final int SNAP_VELOCITY = 200;

    /**
     * 屏幕宽度值。
     */
    private int screenWidth;

    /**
     * menu最多可以滑动到的左边界。值由menu布局的宽度来定，marginLeft到达此值之后，不能再减少(左移)。
     */
    private int leftEdge;

    /**
     * 左侧布局最多可以滑动到的右边缘。值恒为0，即marginLeft到达0之后，不能增加。
     */
    private int rightEdge;

    /**
     * 左侧布局完全显示时，留给右侧布局的宽度值。
     */
    private int leftLayoutPadding = 200;

    /**
     * 记录手指按下时的横坐标。
     */
    private float xDown;

    /**
     * 记录手指移动时的横坐标。
     */
    private float xMove;

    /**
     * 记录手指抬起时的横坐标。
     */
    private float xUp;

    /**
     * 左侧布局当前是显示还是隐藏。只有完全显示或隐藏时才会更改此值，滑动过程中此值无效。
     */
    private boolean isLeftLayoutVisible;

    /**
     * 左侧布局对象。
     */
    private View leftLayout;

    /**
     * 右侧布局对象。
     */
    private View rightLayout;

    /**
     * 用于监听侧滑事件的View。
     */
    private View mBindView;

    /**
     * 左侧布局的参数，通过此参数来重新确定左侧布局的宽度，以及更改leftMargin的值。
     */
    private MarginLayoutParams leftLayoutParams;

    /**
     * 右侧布局的参数，通过此参数来重新确定右侧布局的宽度。
     */
    private MarginLayoutParams rightLayoutParams;

    /**
     * 用于计算手指滑动的速度。
     */
    private VelocityTracker mVelocityTracker;

    /**
     * 重写SlidingLayout的构造函数，其中获取了屏幕的宽度。
     *
     * @param context
     * @param attrs
     */
    public SlidingMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
    }

    /**
     * 绑定监听侧滑事件的View，即在绑定的View进行滑动才可以显示和隐藏左侧布局。
     *
     * @param bindView 需要绑定的View对象。
     */
    public void setScrollView(View bindView) {
        mBindView = bindView;
        mBindView.setOnTouchListener(this);
    }

    /**
     * 将屏幕滚动到左侧布局界面，滚动速度设定为30.
     */
    public void scrollToLeftLayout() {
        new ScrollTask().execute(30);
    }

    /**
     * 将屏幕滚动到右侧布局界面，滚动速度设定为-30.
     */
    public void scrollToRightLayout() {
        new ScrollTask().execute(-30);
    }

    /**
     * 左侧布局是否完全显示出来，或完全隐藏，滑动过程中此值无效。
     *
     * @return 左侧布局完全显示返回true，完全隐藏返回false。
     */
    public boolean isLeftLayoutVisible() {
        return isLeftLayoutVisible;
    }

    /**
     * 在onLayout中重新设定左侧布局和右侧布局的参数。(初始化)
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            // 获取左侧布局对象
            leftLayout = getChildAt(0);
            leftLayoutParams = (MarginLayoutParams) leftLayout.getLayoutParams();
            // 重置左侧布局对象的宽度为屏幕宽度减去leftLayoutPadding
            leftLayoutParams.width = screenWidth - leftLayoutPadding;
            // 设置最左边距为负的左侧布局的宽度
            leftEdge = -leftLayoutParams.width;
            leftLayoutParams.leftMargin = leftEdge;
            leftLayout.setLayoutParams(leftLayoutParams);
            // 获取右侧布局对象
            rightLayout = getChildAt(1);
            rightLayoutParams = (MarginLayoutParams) rightLayout.getLayoutParams();
            rightLayoutParams.width = screenWidth;
            rightLayout.setLayoutParams(rightLayoutParams);
        }
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时，记录按下时的横坐标
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                // 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整左侧布局的leftMargin值，从而显示和隐藏左侧布局
                xMove = event.getRawX();
                int distance = (int) (xMove - xDown);
                if (isLeftLayoutVisible) {
                    leftLayoutParams.leftMargin = distance;
                } else {
                    leftLayoutParams.leftMargin = leftEdge + distance;
                }
                if (leftLayoutParams.leftMargin < leftEdge) {
                    leftLayoutParams.leftMargin = leftEdge;
                } else if (leftLayoutParams.leftMargin > rightEdge) {
                    leftLayoutParams.leftMargin = rightEdge;
                }
                leftLayout.setLayoutParams(leftLayoutParams);
                break;
            case MotionEvent.ACTION_UP:
                // 手指抬起时，进行判断当前手势的意图，从而决定是滚动到左侧布局，还是滚动到右侧布局
                xUp = event.getRawX();
                if (wantToShowLeftLayout()) {
                    if (shouldScrollToLeftLayout()) {
                        scrollToLeftLayout();
                    } else {
                        scrollToRightLayout();
                    }
                } else if (wantToShowRightLayout()) {
                    if (shouldScrollToContent()) {
                        scrollToRightLayout();
                    } else {
                        scrollToLeftLayout();
                    }
                }
                recycleVelocotyTracker();
                break;
        }
        return isBindBasicLayout();
    }

    /**
     * 判断当前手势的意图是不是想显示右侧布局。如果手指移动的距离是负数，且当前左侧布局是可见的，则认为当前手势是想要显示右侧布局。
     *
     * @return 当前手势想显示右侧布局返回true，否则返回false。
     */
    private boolean wantToShowRightLayout() {
        return xUp - xDown < 0 && isLeftLayoutVisible;
    }

    /**
     * 判断当前手势的意图是不是想显示左侧布局。如果手指移动的距离是正数，且当前左侧布局是不可见的，则认为当前手势是想要显示左侧布局。
     *
     * @return 当前手势想显示左侧布局返回true，否则返回false。
     */
    private boolean wantToShowLeftLayout() {
        return xUp - xDown > 0 && !isLeftLayoutVisible;
    }

    /**
     * 判断是否应该滚动将左侧布局展示出来。如果手指移动距离大于屏幕的1/2，或者手指移动速度大于SNAP_VELOCITY，
     * 就认为应该滚动将左侧布局展示出来。
     *
     * @return 如果应该滚动将左侧布局展示出来返回true，否则返回false。
     */
    private boolean shouldScrollToLeftLayout() {
        return xUp - xDown > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
    }

    /**
     * 判断是否应该滚动将右侧布局展示出来。如果手指移动距离加上leftLayoutPadding大于屏幕的1/2，
     * 或者手指移动速度大于SNAP_VELOCITY， 就认为应该滚动将右侧布局展示出来。
     *
     * @return 如果应该滚动将右侧布局展示出来返回true，否则返回false。
     */
    private boolean shouldScrollToContent() {
        return xDown - xUp + leftLayoutPadding > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
    }

    /**
     * 判断绑定滑动事件的View是不是一个基础layout，不支持自定义layout，只支持四种基本layout,
     * AbsoluteLayout已被弃用。
     *
     * @return 如果绑定滑动事件的View是LinearLayout, RelativeLayout, FrameLayout,
     * TableLayout之一就返回true，否则返回false。
     */
    private boolean isBindBasicLayout() {
        if (mBindView == null) {
            return false;
        }
        String viewName = mBindView.getClass().getName();
        return viewName.equals(LinearLayout.class.getName()) || viewName.equals(RelativeLayout.class.getName()) || viewName.equals(FrameLayout.class.getName()) || viewName.equals(TableLayout.class.getName());
    }

    /**
     * 创建VelocityTracker对象，并将触摸事件加入到VelocityTracker当中。
     *
     * @param event 右侧布局监听控件的滑动事件
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 获取手指在右侧布局的监听View上的滑动速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocotyTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            ;
            mVelocityTracker = null;
        }
    }

    class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... speed) {
            int leftMargin = leftLayoutParams.leftMargin;
            while (true) {
                leftMargin = leftMargin + speed[0];
                if (leftMargin > rightEdge) {
                    leftMargin = rightEdge;
                    break;
                }
                if (leftMargin < leftEdge) {
                    leftMargin = leftEdge;
                    break;
                }
                publishProgress(leftMargin);
                // 为了要有滚动效果产生，每次循环使线程睡眠20毫秒，这样肉眼才能够看到滚动动画。
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (speed[0] > 0) {
                isLeftLayoutVisible = true;
            } else {
                isLeftLayoutVisible = false;
            }
            return leftMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... leftMargin) {
            leftLayoutParams.leftMargin = leftMargin[0];
            leftLayout.setLayoutParams(leftLayoutParams);
        }

        @Override
        protected void onPostExecute(Integer leftMargin) {
            leftLayoutParams.leftMargin = leftMargin;
            leftLayout.setLayoutParams(leftLayoutParams);
        }
    }

}
