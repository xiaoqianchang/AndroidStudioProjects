package com.changxiao.library;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * 网页可以处理:
 * 点击相应控件:拨打电话、发送短信、发送邮件、上传图片、播放视频
 * 进度条、返回网页上一层、显示网页标题
 * Thanks to: https://github.com/youlookwhat/WebViewStudy
 * contact me: http://www.jianshu.com/users/e43c6e979831/latest_articles
 */
public class WebViewActivity extends AppCompatActivity {

    private static final String URL = "url";

    private ProgressBar mProgressBar;
    private WebView mWebView;
    // 全屏时视频加载view
    private FrameLayout mVideoFullView;
    private Toolbar mToolbar;
    // 进度条是否加载到90%
    public boolean isProgress90;
    // 网页是否加载完成
    private boolean isPageFinished;
    // title
    private String mTitle;
    // 链接
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getIntentData();
        initView();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (null != intent) {
            mUrl = intent.getStringExtra(URL);
        }
    }

    private void initView() {

    }
}
