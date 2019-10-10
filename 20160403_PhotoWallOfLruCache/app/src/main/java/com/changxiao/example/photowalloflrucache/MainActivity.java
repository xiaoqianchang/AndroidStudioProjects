package com.changxiao.example.photowalloflrucache;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.changxiao.example.photowalloflrucache.adapter.PhotoWallAdapter;
import com.changxiao.example.photowalloflrucache.data.Images;

/**
 * Android照片墙应用实现，再多的图片也不怕崩溃(本demo中只用了LruCache(内存)缓存)
 *
 * 它的设计思路其实也非常简单，用一个GridView控件当作“墙”，然后随着GridView的滚动将一张张照片贴在“墙”上，
 * 这些照片可以是手机本地中存储的，也可以是从网上下载的。制作类似于这种的功能的应用，有一个非常重要的问题需
 * 要考虑，就是图片资源何时应该释放。因为随着GridView的滚动，加载的图片可能会越来越多，如果没有一种合理的
 * 机制对图片进行释放，那么当图片达到一定上限时，程序就必然会崩溃。
 * 今天我们照片墙应用的实现，重点也是放在了如何防止由于图片过多导致程序崩溃上面。主要的核心算法使用了Android
 * 中提供的LruCache类，这个类是3.1版本中提供的，如果你是在更早的Android版本中开发，则需要导入
 * android-support-v4的jar包。
 *
 * 步骤：
 * 1.第一个要考虑的问题就是，我们从哪儿去收集这么多的图片呢？这里我从谷歌官方提供的Demo里将图片源取了出来，
 * 我们就从这些网址中下载图片
 * 
 * Created by Chang.Xiao on 2016/4/3 19:19.
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 用于展示照片墙的GridView
     */
    private GridView mPhotoWall;

    /**
     * GridView的适配器
     */
    private PhotoWallAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotoWall = (GridView) findViewById(R.id.photo_wall);
        adapter = new PhotoWallAdapter(this, 0, Images.imageThumbUrls, mPhotoWall);
        mPhotoWall.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出程序时结束所有的下载任务
        adapter.cancelAllTasks();
    }
}
