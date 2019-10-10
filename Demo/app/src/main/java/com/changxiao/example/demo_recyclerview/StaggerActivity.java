package com.changxiao.example.demo_recyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 导入RecyclerView支持库
 *
 * 5.0 废弃了ListView HttpClient
 *
 * Created by Chang.Xiao on 2016/3/17 14:09.
 * @version 1.0
 */
public class StaggerActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stagger);

        // 1.拿到RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // 2. 初始化数据
        initData();
        // 3. 适配器:ListView BaseAdapter-ViewHolder
        mAdapter = new StaggerAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        // 4. 设置一个布局管理器
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));

        // 设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setmOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(StaggerActivity.this, position + " click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(StaggerActivity.this, position + " long click", Toast.LENGTH_SHORT).show();
                mAdapter.removeData(position);
            }
        });

    }

    /**
     * 初始化数据
     */
    private void initData() {
        mDatas = new ArrayList<String>();
        for(int i = 'A'; i < 'z'; i++) {
//            mDatas.add("" + (char) i); // 用双引号转为字符串效率低
            mDatas.add(String.valueOf((char) i)); // 专业的做法
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            /*case R.id.listView:
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            case R.id.gridView:
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
                break;
            case R.id.horizontalGridView:
                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL));
                break;
            case R.id.staggerGridView: // 瀑布流
//                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));

                break;*/
            case R.id.add:
                mAdapter.addData(1);
                break;
            case R.id.remove:
                mAdapter.removeData(1);
                break;
        }

        return true; // 拦截点击menu事件
    }

}
