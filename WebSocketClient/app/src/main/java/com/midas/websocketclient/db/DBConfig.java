package com.midas.websocketclient.db;

import com.midas.websocketclient.bean.ZRUser;
import com.midas.websocketclient.bean.ZRMessage;

/**
 * 数据库的配置
 * <p/>
 * Created by Chang.Xiao on 2016/5/17.
 *
 * @version 1.0
 */
public class DBConfig {

    /**
     * 数据库的信息
     */
    public static final String DB_NAME = "MIDAS.DB";
    public static  int DB_VERSION = 1;

    /**
     * 联系人表
     */
    public static final String CONTACT_TABLE_CREATESTR = "CREATE TABLE IF NOT EXISTS "
            + ZRUser.TABLE_NAME + " ("
            + ZRUser._ID + "  TEXT primary key, "
            + ZRUser.NAME + " TEXT, "
            + ZRUser.NICKNAME + " TEXT, "
            + ZRUser.AVATAR + " TEXT, "
            + ZRUser.SIGNATURE + " TEXT ";

    /**
     * 消息表
     */
    public static final String MESSAGE_TABLE_CREATESTR = "CREATE TABLE IF NOT EXISTS "
            + ZRMessage.TABLE_NAME + " ("
            + ZRMessage._ID + " TEXT, "
            + ZRMessage.FROM + " TEXT, "
            + ZRMessage.TO + " TEXT, "
            + ZRMessage.DATE + " TEXT, "
            + ZRMessage.CONTENT + " TEXT, ";
}
