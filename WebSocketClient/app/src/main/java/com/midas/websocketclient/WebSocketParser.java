package com.midas.websocketclient;

/**
 * socket返回消息json解析类对象
 * <p/>
 * Created by Chang.Xiao on 2016/5/12.
 *
 * @version 1.0
 */
public class WebSocketParser {

    private static WebSocketParser mParser;

    private WebSocketParser() {
    }

    public static WebSocketParser getInstance() {
        if (mParser == null) {
            synchronized (WebSocketParser.class) {
                if (mParser == null) {
                    mParser = new WebSocketParser();
                }
            }
            return mParser;
        }

        return mParser;
    }
}
