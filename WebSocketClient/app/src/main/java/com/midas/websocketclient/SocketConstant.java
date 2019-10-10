package com.midas.websocketclient;

/**
 * client的链接状态
 * <p/>
 * Created by Chang.Xiao on 2016/5/12.
 *
 * @version 1.0
 */
public class SocketConstant {

    /**
     * 链接打开filter
     */
    public static final String ON_OPEN = "android.intent.action.on_open";

    /**
     * 链接关闭filter
     */
    public static final String ON_CLOSE = "android.intent.action.on_close";

    /**
     * 链接发生错误
     */
    public static final String ON_ERROR = "android.intent.action.on_error";

    /**
     * 接收到消息
     */
    public static final String ON_RECEIVE = "android.intent.action.on_receive";
}
