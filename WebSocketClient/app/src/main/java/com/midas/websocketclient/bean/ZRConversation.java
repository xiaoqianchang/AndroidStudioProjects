package com.midas.websocketclient.bean;

import java.io.Serializable;

/**
 * 最近会话
 * <p/>
 * Created by Chang.Xiao on 2016/5/20.
 *
 * @version 1.0
 */
public class ZRConversation implements Serializable {

    private String userid;
    private String userName;
    private String userHeadImg;
    private String content;
    private String messageCount;
    private String messageTime;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(String messageCount) {
        this.messageCount = messageCount;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}
