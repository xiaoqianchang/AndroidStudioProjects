package com.changxiao.swipelayout;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TListView lv;
    private List<String> str=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for(int i=0;i<20;i++){
            str.add(i+"个");
        }
        lv=(TListView) findViewById(R.id.lv);
        lv.setAdapter(new MyAdapter());
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, id+"aaaaaaaaaaaaaaaaa"+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return str.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return str.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            HolderView holder = null;
            if (convertView == null) {
                holder = new HolderView();
                convertView=View.inflate(MainActivity.this,R.layout.slidmenuitem, null);
                holder.tsl=(TSlidLayout) convertView.findViewById(R.id.tsl);
                holder.content=View.inflate(MainActivity.this,R.layout.slid_layout_menu3, null);
                holder.menu=View.inflate(MainActivity.this,R.layout.slidmenuright, null);
                holder.tsl.addItemAndMenu(holder.content,holder.menu);
                convertView.setTag(holder);
            }else {
                holder = (HolderView) convertView.getTag();
            }

            final int pos=position;
            //必须设置此句，否则不知道点击的是哪个item中的menu
            holder.tsl.pos=position;
            holder.menu.findViewById(R.id.menu_delete).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    str.remove(pos);
                    notifyDataSetChanged();
                    lv.hideAllMenuView();

                }
            });
            holder.menu.findViewById(R.id.menu_hello).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(MainActivity.this, "hello"+pos, Toast.LENGTH_SHORT).show();
                }
            });
            ((TextView)holder.content.findViewById(R.id.tv_top)).setText(str.get(position));
            return convertView;
        }

    }
    static class HolderView {
        public TSlidLayout tsl;
        public View content;
        public View menu;
    }
}
