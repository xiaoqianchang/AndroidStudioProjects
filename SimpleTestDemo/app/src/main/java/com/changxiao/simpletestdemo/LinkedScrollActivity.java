package com.changxiao.simpletestdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changxiao.simpletestdemo.linkedscroll.Channel;
import com.changxiao.simpletestdemo.linkedscroll.ChannelAdapter;
import com.changxiao.simpletestdemo.linkedscroll.ChannelCategory;
import com.changxiao.simpletestdemo.linkedscroll.FileUtil;
import com.changxiao.simpletestdemo.linkedscroll.OneKeyListen;
import com.changxiao.simpletestdemo.linkedscroll.PinnedHeaderListView;
import com.changxiao.simpletestdemo.linkedscroll.TestSectionedAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LinkedScrollActivity extends AppCompatActivity {

    private TestSectionedAdapter sectionedAdapter;

    private ChannelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linked_scroll);
        PinnedHeaderListView listView = (PinnedHeaderListView) findViewById(R.id.pinnedListView);
        LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        sectionedAdapter = new TestSectionedAdapter();
        adapter = new ChannelAdapter();
        getData();
        listView.setAdapter(sectionedAdapter);

    }

    private void getData() {
        String s = FileUtil.readAssetFileData(this, "channels.json");
        OneKeyListen oneKeyListen = new Gson().fromJson(s, OneKeyListen.class);
        List<ChannelCategory> classInfos = oneKeyListen.getClassInfos();
//        sectionedAdapter.setChannelCategory(classInfos);
        adapter.setChannelCategory(classInfos);
    }
}
