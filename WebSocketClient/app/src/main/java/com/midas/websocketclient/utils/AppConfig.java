package com.midas.websocketclient.utils;

/**
 *
 * 
 * Created by Chang.Xiao on 2016/7/21.
 * @version 1.0
 */
public class AppConfig {

    // 服务器数据地址
    public static final String SERVER_URL = "http://172.16.101.91:9000/";

    public static final String SERVER_URL_INIT_1 = "http://f.midasjr.com";
    public static final String SERVER_URL_INIT_2 = "http://b.midasjr.com";
    public static final String SERVER_URL_INIT_3 = "http://1.202.201.170";

    public static final String INIT_M_PATH = "/html/init/a/";
    public static final String INIT_L_PATH = "/initinfo.shtml";

//    public static final String PATH = "qtest";
    public static final String PATH = "91";
    public static String initinfo1 = SERVER_URL_INIT_1 + INIT_M_PATH + PATH + INIT_L_PATH;
    public static String initinfo2 = SERVER_URL_INIT_2 + INIT_M_PATH + PATH + INIT_L_PATH;
    public static String initinfo3 = SERVER_URL_INIT_3 + INIT_M_PATH + PATH + INIT_L_PATH;

}
