package com.changxiao.simpletestdemo.linkedscroll;

import java.util.List;

/**
 * wiki: http://gitlab.ximalaya.com/geek/docs/blob/master/%E4%B8%80%E9%94%AE%E5%90%AC/%E6%8E%A5%E5%8F%A3%E5%AE%9A%E4%B9%89.md
 * <p>
 * Created by Chang.Xiao on 2017/12/4.
 *
 * @version 1.0
 */
public class OneKeyListen {

    private String slogan;// 口号
    private Channel lastVisitChannel;
    private String recSrc; // 算法跟踪字段
    private String recTrack; // 算法跟踪字段
    private List<ChannelCategory> classInfos;

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public Channel getLastVisitChannel() {
        return lastVisitChannel;
    }

    public void setLastVisitChannel(Channel lastVisitChannel) {
        this.lastVisitChannel = lastVisitChannel;
    }

    public String getRecSrc() {
        return recSrc;
    }

    public void setRecSrc(String recSrc) {
        this.recSrc = recSrc;
    }

    public String getRecTrack() {
        return recTrack;
    }

    public void setRecTrack(String recTrack) {
        this.recTrack = recTrack;
    }

    public List<ChannelCategory> getClassInfos() {
        return classInfos;
    }

    public void setClassInfos(List<ChannelCategory> classInfos) {
        this.classInfos = classInfos;
    }
}
