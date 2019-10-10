package com.changxiao.viewdraglayout;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        getDatas();
        listView.setAdapter(new MyAdapter());
    }

    public List<String> getDatas() {
        datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add(String.valueOf(i));
        }
        return datas;
    }

    class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item, null);
//            TextView textView = (TextView) convertView.findViewById(R.id.text);
//            textView.setText(datas.get(position));
            return convertView;
        }
    }
}
