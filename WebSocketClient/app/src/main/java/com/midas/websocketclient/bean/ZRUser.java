package com.midas.websocketclient.bean;

import java.io.Serializable;

/**
 *
 * 
 * Created by Chang.Xiao on 2016/5/17.
 * @version 1.0
 */
public class ZRUser implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7981560250804078637l;

    public static final String TABLE_NAME = "contact";
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String NICKNAME = "nickname";
    public static final String AVATAR = "avatar";
    public static final String SIGNATURE = "signature";

    //数据库需要
    private String _id;
    private String name;
    private String nickName;
    private String avatar;
    private String signature; // 个性签名

    public ZRUser() {
        // TODO Auto-generated constructor stub
    }

    public ZRUser(String _id) {
        this._id = _id;
    }

    public ZRUser(String _id, String name, String nickName) {
        this._id = _id;
        this.name = name;
        this.nickName = nickName;
    }

    public ZRUser(String nickName, String avatar) {
        this.nickName = nickName;
        this.avatar = avatar;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
