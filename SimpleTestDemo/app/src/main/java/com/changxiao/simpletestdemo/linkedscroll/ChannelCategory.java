package com.changxiao.simpletestdemo.linkedscroll;

import java.util.List;

/**
 * $desc$
 * <p>
 * Created by Chang.Xiao on 2017/12/4.
 *
 * @version 1.0
 */

public class ChannelCategory {

    private String className; // 分类名
    private long classId; // 分类ID
    private List<Channel> channelInfos;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public List<Channel> getChannelInfos() {
        return channelInfos;
    }

    public void setChannelInfos(List<Channel> channelInfos) {
        this.channelInfos = channelInfos;
    }
}
