package com.changxiao.example.slidingmenulayoutdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.changxiao.example.slidingmenulayoutdemo.widget.SlidingMenuLayout;

/**
 * 原理：
 * 说是滑动菜单的框架，其实说白了也很简单，就是我们自定义一个布局，在这个自定义布局中实现好滑动菜单的功能，
 * 然后只要在Activity的布局文件里面引入我们自定义的布局，这个Activity就拥有了滑动菜单的功能了。
 * 原理讲完了，是不是很简单？下面我们来动手实现吧。
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 侧滑布局对象，用于通过手指滑动将左侧的菜单布局进行显示或隐藏。
     */
    private SlidingMenuLayout slidingMenuLayout;

    /**
     * menu按钮，点击按钮展示左侧布局，再点击一次隐藏左侧布局。
     */
    private Button menuButton;

    /**
     * 放在content布局中的ListView。
     */
    private ListView contentListView;

    /**
     * 作用于contentListView的适配器。
     */
    private ArrayAdapter<String> contentListAdapter;

    private String[] contentItems = {"Content Item 1", "Content Item 2", "Content Item 3", "Content Item 4", "Content Item 5", "Content Item 6", "Content Item 7", "Content Item 8", "Content Item 9", "Content Item 10", "Content Item 11", "Content Item 12", "Content Item 13", "Content Item 14", "Content Item 15", "Content Item 16"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slidingMenuLayout = (SlidingMenuLayout) findViewById(R.id.slidingLayout);
        menuButton = (Button) findViewById(R.id.menuButton);
        contentListView = (ListView) findViewById(R.id.contentList);
        contentListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contentItems);
        contentListView.setAdapter(contentListAdapter);
        // 将监听滑动事件绑定在contentListView上
        slidingMenuLayout.setScrollView(contentListView);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 实现点击一下menu展示左侧布局，再点击一下隐藏左侧布局的功能
                if (slidingMenuLayout.isLeftLayoutVisible()) {
                    slidingMenuLayout.scrollToRightLayout();
                } else {
                    slidingMenuLayout.scrollToLeftLayout();
                }
            }
        });
    }
}
