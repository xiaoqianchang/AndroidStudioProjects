package com.changxiao.themeskin.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 添加加载更多功能
 * <p>
 * Created by Chang.Xiao on 2016/7/26.
 *
 * @version 1.0
 */
public class XCRecyclerView extends RecyclerView {

    private boolean isScrollingToBottom = true;
    private FloatingActionButton floatingActionButton;
    private LoadMoreListener listener;

    public XCRecyclerView(Context context) {
        super(context);
    }

    public XCRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public XCRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void applyFloatingActionButton(FloatingActionButton floatingActionButton) {
        this.floatingActionButton = floatingActionButton;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        isScrollingToBottom = dy > 0;
        if (floatingActionButton != null) {
            if (isScrollingToBottom) {
                if (floatingActionButton.isShown())
                    floatingActionButton.hide();
            } else {
                if (!floatingActionButton.isShown())
                    floatingActionButton.show();
            }
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
            int totalItemCount = layoutManager.getItemCount();
            if (lastVisibleItem == (totalItemCount - 1) && isScrollingToBottom) {
                if (listener != null)
                    listener.loadMore();
            }
        }
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener){
        this.listener = loadMoreListener;
    }

    public interface LoadMoreListener {
        void loadMore();
    }
}
