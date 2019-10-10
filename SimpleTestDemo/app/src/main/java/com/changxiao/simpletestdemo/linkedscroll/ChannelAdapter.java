package com.changxiao.simpletestdemo.linkedscroll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.changxiao.simpletestdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * $desc$
 * <p>
 * Created by Chang.Xiao on 2017/12/6.
 *
 * @version 1.0
 */

public class ChannelAdapter extends BaseAdapter implements SectionIndexer {

    private List<ChannelCategory> channelCategoryList;
    private List<Channel> channelList;

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.list_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        ((TextView) layout.findViewById(R.id.textItem)).setText(channelCategoryList.get(getSectionForPosition(position)).getChannelInfos().get(position).channelName);
        return layout;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    public void setChannelCategory(List<ChannelCategory> classInfos) {
        channelCategoryList = classInfos;
        channelList = getChannelList(classInfos);
    }

    public List<Channel> getChannelList(List<ChannelCategory> channelCategoryList) {
        List<Channel> channelList = new ArrayList<>();
//        if (null != channelCategoryList && channelCategoryList.size() > 0channelCategoryList) {
//            for (int i = 0; i < channelCategoryList.size(); i++) {
//                ChannelCategory category = channelCategoryList.get(i);
//                if (null != category) {
//                    List<Channel> channelInfos = category.getChannelInfos();
//                    channelList.addAll(channelInfos);
//                }
//            }
//        }
        return channelList;
    }
}
