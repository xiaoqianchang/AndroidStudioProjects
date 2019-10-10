package com.changxiao.example.timeline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.changxiao.example.timeline.adapter.TimeLineAdapter;
import com.changxiao.example.timeline.model.TimeLineModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecycler;
    private TimeLineAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecycler = (RecyclerView) findViewById(R.id.time_line_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TimeLineAdapter(this, getDatas());
        mRecycler.setAdapter(mAdapter);

    }

    private List<TimeLineModel> getDatas() {
        List<TimeLineModel> datas = new ArrayList<TimeLineModel>();
        for (int i = 0; i < 30; i++) {
            datas.add(new TimeLineModel("changxiao" + i, 20 + i));
        }
        return datas;
    }
}
