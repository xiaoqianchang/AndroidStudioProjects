package com.midas.websocketclient.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;

/**
 * $desc$
 * <p/>
 * Created by Chang.Xiao on 2016/5/17.
 *
 * @version 1.0
 */
public class ZRListView extends ListView {

    /**上次触摸的X坐标*/
    private float mLastX = -1;
    private int mLastPointToPosition=-1;
    /**最小滑动距离，超过了，才认为开始滑动  */
    private int mTouchSlop = 0 ;

    public ZRListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public ZRListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public ZRListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        init(context);
    }

    @SuppressLint("NewApi")
    public ZRListView(Context context, AttributeSet attrs, int defStyleAttr,
                      int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // TODO Auto-generated constructor stub
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX=ev.getRawX();
                //在此处改变mLastPointToPosition，否则return时未能改变
                int posTemp=mLastPointToPosition;
                mLastPointToPosition=pointToPosition((int)ev.getX(), (int)ev.getY());
                //此处注意：在GridView和ListView中，getChildAt ( int position ) 方法中position指的是当前可见区域的第几个元素，而不是整个listview中的位置
                for(int i=0;i<=getLastVisiblePosition()-getFirstVisiblePosition();i++){
                    if(getChildAt(i)!=null){
                        ZRSwipeLayout tsl=(ZRSwipeLayout)getChildAt(i);
                        //当有菜单出现时，只能点击菜单，点击其他任何地方均收起菜单
                        if(tsl.getIsMenuVisible()){
                            if(posTemp!=(pointToPosition((int)ev.getX(), (int)ev.getY()))){
                                //说明点击的不是有菜单的那个item
                                tsl.hideMenuWithAnimation();
                                //拦截此事件，不再向下传递
                                return false;
                            }else{
                                //如果点击的不是菜单，则隐藏菜单，否则传递给子View
                                if(inRangeOfView(tsl.getChildAt(0), ev)){
                                    tsl.hideMenuWithAnimation();
                                    //拦截此事件，不再向下传递，包括自身的事件传递

                                    return false;
                                }
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if((Math.abs(mLastX-ev.getRawX())>mTouchSlop)){
                    //只要水平方向有滑动，就不进行垂直滑动（请求不允许拦截子View触摸事件，即交给子View处理）
                    //此时不会调用本身的onTouchEvent
                    requestDisallowInterceptTouchEvent(true);
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 判断是否点击在view的内部
     * @param view
     * @param ev
     * @return
     *            true 点击在view的内部
     *            false 点击在view的外部
     */
    private boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        //此处需要取在屏幕上的坐标，并且只需判断x坐标，因为listview中点击的item已经确定
        view.getLocationOnScreen(location);
        int x = location[0];
        if (ev.getX() < x || ev.getX() > (x + view.getWidth())) {
            return false;
        }
        return true;
    }

    public void hideAllMenuView(){
        for(int i=0;i<=getLastVisiblePosition()-getFirstVisiblePosition();i++){
            if(getChildAt(i)!=null){
                ZRSwipeLayout tsl=(ZRSwipeLayout)getChildAt(i);
                //当有菜单出现时，只能点击菜单，点击其他任何地方均收起菜单
                if(tsl.getIsMenuVisible()){
                    tsl.hideMenu();
                }
            }
        }
    }
}
