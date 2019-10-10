package com.changxiao.framework.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import com.changxiao.framework.constants.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 创建人 alvin
 * @author 修改人 Alvin
 * @author 修改说明
 * @version 1.0
 * @功能 print logcat
 * @date 创建日期 2013-4-17
 * @date 修改日期 2013-4-17
 */
public class Logger {

    public final static String JSON_ERROR = "解析json异常";
    /**
     * upload log
     */
    public static String CACHE_DIR = "";
    public static String LOG_UPLOAD_FILE = "upload_log_file";
    public static boolean UPLOADING = false;
    public static String ERROR_LOG_PATH = Environment.getExternalStorageDirectory() + "/ting/errorLog/infor.log";
    static Map<String, String> map = new HashMap<String, String>();
    private static long nowTime = System.currentTimeMillis();
    private static int LOG_LEVEL = 0;

	static {
		map.clear();
        String sdStatus = null;
        try {
            sdStatus = Environment.getExternalStorageState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sdStatus.equals(Environment.MEDIA_MOUNTED)) {

            String pathname = Environment.getExternalStorageDirectory()
                    + "/ting/config.ini";
            if (!TextUtils.isEmpty(pathname)) {
                File file = new File(pathname);
                if (file.exists()) {
                    FileReader reader = null;
                    BufferedReader br = null;
                    try {
                        reader = new FileReader(pathname);
                        br = new BufferedReader(reader);
                        String str = null;
                        while ((str = br.readLine()) != null) {
                            if (!TextUtils.isEmpty(str)) {
                                String[] strs = str.split("=");
                                if (strs != null && strs.length == 2) {
                                    map.put(strs[0], strs[1]);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        }
    }

    public static void log(Object showMessage) {

        if (Constants.isDebug) {// 防止上线之后出现打印语句
            Log.i("ting", showMessage + "");
        }

    }

    public static void logListToSD(List list, String message) {

        if (list == null || list.size() == 0)
            return;

        logToSd("start-log-list:" + message + ":size:" + list.size());
        for (Object object : list) {
            if (object != null)
                logToSd(object.toString());
        }
        logToSd("end-log-list" + message);
    }

    public static void logToSd(String showMessage, File file) {
        if (file == null) {
            return;
        }
        PrintWriter printWriter = null;

        try {
            printWriter = new PrintWriter(
                    new FileWriter(file, true));
            printWriter.println(showMessage);
        } catch (Throwable tr2) {
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    public static void logToSd(String showMessage) {
        if (checkIfLog(showMessage) || Constants.isDebug) {// 防止上线之后出现打印语句
            File savePathFile = getLogFilePath();

            if (savePathFile == null) {
                return;
            }
            logToSd(showMessage, savePathFile);
        }
    }

    public static String getCacheDir(Context context) {
        CACHE_DIR = context.getCacheDir().getPath();
        return CACHE_DIR;
    }

    public static void logToFile(String message) {
        if (!Constants.isLogToFile
                || TextUtils.isEmpty(message)
                || TextUtils.isEmpty(CACHE_DIR)
                || UPLOADING)
            return;
        File file = new File(CACHE_DIR, LOG_UPLOAD_FILE);
        logToSd(message, file);
    }
	
    public static void init(Context appCtx) {
        if (appCtx != null && appCtx.getExternalFilesDir("") != null) {
            ERROR_LOG_PATH = appCtx.getExternalFilesDir("")+"/errorLog/infor.log";
        }
    }

    public static File getLogFilePath() {
        String sdStatus;
        try {
            sdStatus = Environment.getExternalStorageState();
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }

		if (!Environment.MEDIA_MOUNTED.equals(sdStatus)) {
			return null;
		}

		String pathName = Environment.getExternalStorageDirectory() +"/ting/errorLog/infor.log";

		File file = new File(pathName);
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public static boolean checkIfLog(String logstr) {

		for(Map.Entry<String, String> entry : map.entrySet()) {
			if(logstr.contains(entry.getKey())) {
				if(entry.getValue().equals("true")) {
					return true;
				}
			}
		}
		return false;
	}

    public static void throwRuntimeException(Object exceptionMessage) {
        if (Constants.isDebug) {
            throw new RuntimeException("出现异常：" + exceptionMessage);
        }
    }

    public static void logFuncRunTimeReset(String message) {
        log(message);
        nowTime = System.currentTimeMillis();
    }

    public static void logFuncRunTime(String message) {
        log("time " + message + ":" + (System.currentTimeMillis() - nowTime));
        nowTime = System.currentTimeMillis();
    }

    public static String getLineInfo() {
        if (Constants.isDebug) {
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            return "@" + ste.getFileName() + ": Line " + ste.getLineNumber();
        } else {
            return "";
        }
    }

    public static void setDebugLevel(int level) {
        LOG_LEVEL = level;
    }

    public static void log(String tag, String message, boolean show) {
        if (null != message && isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);
        }
    }

    public static void v(String tag, String msg) {
        if (null != msg && isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (null != msg && isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (null != msg && isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (null != msg && isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (null != msg && isLoggable(tag, Log.INFO)) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (null != msg && isLoggable(tag, Log.INFO)) {
            Log.i(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (null != msg && isLoggable(tag, Log.WARN)) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (null != msg && isLoggable(tag, Log.WARN)) {
            Log.w(tag, msg, tr);
        }
    }

    public static void w(String tag, Throwable tr) {
        if (null != tr && isLoggable(tag, Log.WARN)) {
            Log.w(tag, tr);
        }
    }

    public static void e(Exception e) {
        e(JSON_ERROR, JSON_ERROR + e.getMessage() + Logger.getLineInfo());
    }

    public static void e(String tag, String msg) {
        if (null != msg && isLoggable(tag, Log.ERROR)) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (null != msg && isLoggable(tag, Log.ERROR)) {
            Log.e(tag, msg, tr);
        }
    }

    public static boolean isLoggable(String tag, int level) {

        if (!Constants.isDebug)
            return false;
        return level >= LOG_LEVEL;
    }

}
