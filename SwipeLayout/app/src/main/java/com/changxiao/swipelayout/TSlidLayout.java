package com.changxiao.swipelayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * 此视图可以单独作为一个ListView的item
 * 可以侧滑拉出菜单的自定义View，如ListView侧滑删除效果
 * 菜单item一起滑动
 * <p/>
 * Created by Chang.Xiao on 2016/5/18.
 *
 * @version 1.0
 */
public class TSlidLayout extends ViewGroup {

    /** 用于滑动的类*/
    private Scroller mScroller;
    /** 用来跟踪触摸速度的类*/
    private VelocityTracker mVelocityTracker;
    /** 最小滑动的速度*/
    private static final int SNAP_VELOCITY = 300;
    /**最小滑动距离，超过了，才认为开始滑动  */
    private int mTouchSlop = 0 ;
    /**上次触摸的X坐标*/
    private float mLastX = -1;
    /**上次触摸的Y坐标*/
    private float mLastY = -1;
    private Context mContext;
    /**菜单与item视图*/
    private View menu,mContentView;
    /**viewgroup的宽高*/
    private int maxWidth,maxHeight;
    /**滑出菜单是否可见*/
    private boolean isMenuVisible=false;
    /**滑出菜单的宽度*/
    private int slidMenuWidth=0;
    /**是否正在左右滑动*/
    private boolean isSliding=false;
    /**为每个item记录位置，判断点击的是哪个item*/
    public int pos=-1;


    public TSlidLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public TSlidLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    @SuppressLint("NewApi")
    public TSlidLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        init(context);
    }

    private void init(Context context) {
        this.mContext=context;
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        slidMenuWidth=mContext.getResources().getDimensionPixelSize(R.dimen.slidemenu_right_width);
    }

    /*@Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        menu = getChildAt(1);
        mContentView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 调用ListView的item点击事件
                ((ListView)getParent()).performItemClick(v, pos, pos);
            }
        });
    }*/

    /**
     * 添加视图和滑出菜单
     * @param content
     */
    public void addItemAndMenu(View content,View menu){
        //具体宽高在addView的时候设置，里面的控件充满行高
        addView(content,new LayoutParams(-1, -1));
        addView(menu,new LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.slidemenu_right_width),-1));
        this.menu=menu;
        this.mContentView=content;
        content.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 调用ListView的item点击事件
                ((ListView)getParent()).performItemClick(v, pos, pos);
            }
        });
    }

    /**
     * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置ViewGroup自己的宽和高
     * Exactly：width代表的是精确的尺寸
     AT_MOST：width代表的是最大可获得的空间
     MATCH_PARENT(FILL_PARENT)对应于EXACTLY，WRAP_CONTENT对应于AT_MOST
     其他情况(有具体值的)也对应于EXACTLY
     */
    @SuppressLint("NewApi")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        slidMenuWidth = menu.getMeasuredWidth();
        //当我们需要重写onMeasure时，记得要调用setMeasuredDimension来设置自身的mMeasuredWidth和mMeasuredHeight，否则，就会抛出异常
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        //      final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //      final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //
        //        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        //        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);

        /**
         * 根据childView计算的出的宽和高，计算容器的宽和高，主要用于容器是warp_content时
         */
        for (int i = 0,count = getChildCount(); i < count; i++) {
            View childView = getChildAt(i);
            //获取每个子view的自己高度宽度，取最大的就是viewGroup的大小
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            maxWidth = Math.max(maxWidth,childView.getMeasuredWidth());
            maxHeight = Math.max(maxHeight,childView.getMeasuredHeight());
        }
        //为ViewGroup设置宽高
        setMeasuredDimension(maxWidth,maxHeight);

        // 计算出所有的childView的宽和高---可用
        //        measureChildren(widthMeasureSpec, heightMeasureSpec);

        /**
         * 设置所有的childView的宽和高，此处如果不设置，会造成多个子view的情况下，有的子view设置成match_parent但是不能充满父控件的问题
         */
        //首先判断params.width的值是多少，有三种情况。
        //如果是大于零的话，及传递的就是一个具体的值，那么，构造MeasupreSpec的时候可以直接用EXACTLY。
        //如果为-1的话，就是MatchParent的情况，那么，获得父View的宽度，再用EXACTLY来构造MeasureSpec。
        //如果为-2的话，就是wrapContent的情况，那么，构造MeasureSpec的话直接用一个负数就可以了。
        for (int i = 0,count = getChildCount(); i < count; i++) {
            View childView = getChildAt(i);
            int widthSpec = 0;
            int heightSpec = 0;
            LayoutParams params = (LayoutParams) childView.getLayoutParams();
            if(params.width > 0){
                widthSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY);
            }else if (params.width == -1) {
                widthSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
            } else if (params.width == -2) {
                widthSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST);
            }

            if(params.height > 0){
                heightSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
            }else if (params.height == -1) {
                heightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
            } else if (params.height == -2) {
                heightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
            }
            childView.measure(widthSpec, heightSpec);
        }
    }
    /*
     * 首先执行onMeasure，然后就会执行onLayout
     * 为子View指定位置：相对父控件的位置！！！！！！
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        //此处left,top相对父视图为0，0
        mContentView.layout(0, 0, right, maxHeight);
        menu.layout(mContentView.getMeasuredWidth(), 0, mContentView.getMeasuredWidth()+menu.getMeasuredWidth(), maxHeight);

    }

    /**
     * 注：Scroller中：正值代表向左移动，负值代表向右移动
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (mVelocityTracker == null) {
            // 使用obtain方法得到VelocityTracker的一个对象
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        mVelocityTracker.computeCurrentVelocity(1000);
        // 获得当前的速度
        int velocityX = (int) mVelocityTracker.getXVelocity();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX=ev.getRawX();
                mLastY=ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算当前的速度
                if(Math.abs(velocityX)>SNAP_VELOCITY||(Math.abs(mLastX-ev.getRawX())>mTouchSlop)){
                    isSliding=true;

                    int deltaX = (int) (mLastX - ev.getRawX());
                    mLastX = ev.getRawX();
                    if(getScrollX()>=0&&getScrollX()<=slidMenuWidth){
                        if(isMenuVisible){
                            if(getScrollX()+deltaX<=0){
                                deltaX=-getScrollX();
                            }
                            if((getScrollX()+deltaX)>=slidMenuWidth){
                                //此时菜单可见，不能左滑，只能右滑隐藏菜单
                                deltaX=0;
                            }
                        }else{
                            if((getScrollX()+deltaX)>=slidMenuWidth){
                                deltaX=slidMenuWidth-getScrollX();
                            }
                            if(getScrollX()+deltaX<=0){
                                //菜单不可见，此时不能右滑，只能左滑显示菜单
                                deltaX=0;
                            }
                        }
                        scrollBy(deltaX,0);
                    }
                }
                break;
            default:
                isSliding=false;
                //速度加滑动距离满足一个即自动显示或隐藏
                int delta=0;
                if(isMenuVisible){
                    //右菜单可见时
                    if(velocityX >= SNAP_VELOCITY||(slidMenuWidth-getScrollX())>=slidMenuWidth/3){
                        //自动隐藏
                        delta=-getScrollX();
                        mScroller.startScroll(getScrollX(), 0,delta, 0);
                        invalidate();
                    }
                    if((velocityX>0&&velocityX < SNAP_VELOCITY)||(slidMenuWidth-getScrollX())<slidMenuWidth/3){
                        //自动显示
                        delta=slidMenuWidth-getScrollX();
                        mScroller.startScroll(getScrollX(), 0,delta, 0);
                        invalidate();
                    }
                }else{
                    //右菜单不可见时
                    if(velocityX <= -SNAP_VELOCITY||getScrollX()>=slidMenuWidth/3){
                        //滑动速度超过或者滑动距离超过一半时，松手自动显示
                        delta=slidMenuWidth-getScrollX();
                        mScroller.startScroll(getScrollX(), 0,delta,0);
                        invalidate();
                    }
                    if((velocityX<0&&velocityX > -SNAP_VELOCITY)||getScrollX()<slidMenuWidth/3){
                        //自动隐藏
                        delta=-getScrollX();
                        mScroller.startScroll(getScrollX(), 0,delta, 0);
                        //startScroll只是设置滑动的初始化参数，一定要调用下面这句，才能真正开始滑动
                        invalidate();
                    }
                }
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if(isSliding){
            //如果正在滑动（非点击），则拦截此事件，不传递给子View
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * ViewGroup在分发绘制自己的孩子的时候，会对其子View调用computeScroll()方法
     */
    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }else{//滑动结束后，改变菜单状态
            changeMenuVisibleState();
        }
    }

    /**
     * 在手动或者自动滚动完成后，改变菜单可见状态
     */
    private void changeMenuVisibleState(){
        //必须在滚动完成后，判断菜单是否可见，否则会出现判断错误的情况
        if(getScrollX()==slidMenuWidth){
            isMenuVisible=true;
        }else{
            isMenuVisible=false;
        }
    }

    /***
     * 当菜单可见时，隐藏它
     */
    public void hideMenuWithAnimation(){
        mScroller.startScroll(getScrollX(), 0,-getScrollX(), 0);
        invalidate();
        isMenuVisible=false;
    }

    /***
     * 删除的时候，瞬间隐藏所有菜单
     */
    public void hideMenu() {
        //此函数参数意义：首先瞬间移动到startX，然后在规定的时间内缓慢平移指定距离（dy：非坐标）
        //一般startX用getScrollX()代表从当前位置开始平移
        mScroller.startScroll(0, 0, 0, 0, 0);
        invalidate();
        isMenuVisible=false;
    }

    public boolean getIsMenuVisible(){
        return isMenuVisible;
    }
}
