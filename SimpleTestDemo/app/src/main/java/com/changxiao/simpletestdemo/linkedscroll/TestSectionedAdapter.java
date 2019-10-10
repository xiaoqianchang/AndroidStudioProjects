package com.changxiao.simpletestdemo.linkedscroll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changxiao.simpletestdemo.R;

import java.util.List;

public class TestSectionedAdapter extends SectionedBaseAdapter {

    private List<ChannelCategory> channelCategoryList;

    @Override
    public Object getItem(int section, int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int section, int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getSectionCount() {
        return channelCategoryList.size();
    }

    @Override
    public int getCountForSection(int section) {
        return channelCategoryList.get(section).getChannelInfos().size();
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.list_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        ((TextView) layout.findViewById(R.id.textItem)).setText("Section " + section + " Item " + position);
        ((TextView) layout.findViewById(R.id.textItem)).setText(channelCategoryList.get(section).getChannelInfos().get(position).channelName);
        return layout;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
//        ((TextView) layout.findViewById(R.id.textItem)).setText("Header for section " + section);
        ((TextView) layout.findViewById(R.id.textItem)).setText(channelCategoryList.get(section).getClassName());
        return layout;
    }

    public void setChannelCategory(List<ChannelCategory> classInfos) {
        channelCategoryList = classInfos;
    }
}
