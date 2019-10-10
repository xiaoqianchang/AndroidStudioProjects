package com.changxiao.themeskin.utils;

/**
 * Global configuration for application.
 *
 * @author gufei
 * @version 1.0
 * @createDate 2015-10-19
 * @lastUpdate 2015-10-19
 */
public class AppConfig {

    // 服务器数据地址
    public static final String SERVER_URL = "http://gank.io/api/";

    public static String WORK_FOLDER = FileUtils.getWorkFolder();

    public static String APP_IMG_DIR = FileUtils.getImageFolder("image/");

    public static String APP_DOWNLOAD_DIR = WORK_FOLDER + "download/";

    public static String APP_LOG_DIR = WORK_FOLDER + "mdsjrLog/%s/";

    public static String APP_CRASH_LOG_DIR = APP_LOG_DIR + "crash/";

    public static final boolean CRASH_LOG_TO_FILE = true;

    public static final boolean LOG_TO_FILE = true;

    public static final boolean DEBUG = true;

    public static final String SP_DATA = "sp_data";

}
