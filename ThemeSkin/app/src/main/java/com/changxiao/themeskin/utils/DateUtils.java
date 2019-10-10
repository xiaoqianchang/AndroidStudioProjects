package com.changxiao.themeskin.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Date utils used for application.
 * <p>
 * Created by Chang.Xiao on 2016/7/1.
 *
 * @version 1.0
 */
public class DateUtils {

    /**
     * yyyyMMddHHmmss
     */
    public static final String TIME_FORMAT = "yyyyMMddHHmmss";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String TIME_FORMAT2 = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd
     */
    public static final String TIME_FORMAT3 = "yyyy-MM-dd";
    /**
     * yyyyMMdd
     */
    public static final String TIME_FORMAT4 = "yyyyMMdd";
    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String TIME_FORMAT5 = "yyyy-MM-dd HH:mm";
    /**
     * yyyyMM
     */
    public static final String TIME_FORMAT6 = "yyyyMM";
    /**
     * MM-dd
     */
    public static final String TIME_FORMAT7 = "MM-dd";
    /**
     * HH:mm
     */
    public static final String TIME_FORMAT8 = "HH:mm";
    /**
     * HH:mm:ss
     */
    public static final String TIME_FORMAT9 = "HH:mm:ss";
    /**
     * MM-dd HH:mm
     */
    public static final String TIME_FORMAT10 = "MM-dd HH:mm";
    /**
     * yy-MM-dd HH:mm
     */
    public static final String TIME_FORMAT11 = "yy-MM-dd HH:mm";
    /**
     * mm:ss
     */
    public static final String TIME_FORMAT12 = "mm:ss";
    /**
     * MM-dd HH:mm
     */
    public static final String TIME_FORMAT13 = "MM月dd日 HH:mm";

    /**
     * Returns the current time in milliseconds since January 1, 1970 00:00:00.0 UTC.
     *
     * @return
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * Returns the current time in milliseconds since January 1, 1970 00:00:00.0 UTC by DateFormat.
     *
     * @param format
     * @return
     */
    public static String getCurrentTimeStr(String format) {
        return formatTime(System.currentTimeMillis(), format);
    }

    /**
     * 格式化时间
     *
     * @param time          毫秒
     * @param format        format
     * @return
     */
    public static String formatTime(long time, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * transferStringDateToLong
     * default formatDate {yyyy-MM-dd hh:mm:ss}
     *
     * @param date {2016-06-15 08:05:18}
     * @return
     */
    public static long transferStringDateToLong(String date) {
        try {
            if (TextUtils.isEmpty(date))
                return 0l;
            return transferStringDateToLong(TIME_FORMAT2, date);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0l;
        }
    }

    /**
     * transferStringDateToLong
     *
     * @param formatDate{yyyy-MM-dd hh:mm:ss}
     * @param date      {2016-06-15 08:05:18}
     * @return
     */
    public static long transferStringDateToLong(String formatDate, String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
            Date dt = sdf.parse(date);
            return dt.getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
            return 0l;
        }
    }

    /**
     * Get timemillis by date string
     *
     * @param dataStr
     * @param format
     * @return
     */
    public static long getTimeInMillisByString(String dataStr, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        try {
            Date date = dateFormat.parse(dataStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get format date
     *
     * @param srcFormat         source format
     * @param destFormat        dest format
     * @param dateString        date string
     * @return
     */
    public static String getFormatDate(String srcFormat, String destFormat,
                                       String dateString) {
        SimpleDateFormat src = new SimpleDateFormat(srcFormat, Locale.CHINA);
        SimpleDateFormat dest = new SimpleDateFormat(destFormat, Locale.CHINA);
        try {
            return dest.format(src.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * yyyy-mm-dd hh:mm to yyyy年mm月dd日 hh:mm
     *
     * @param date
     * @return
     */
    public final static String formatDate(String date) {
        try {
            String yy = date.split("-")[0];
            String mm = date.split("-")[1];
            String temp = date.split("-")[2];
            String dd = temp.substring(0, 2);
            String hr = temp.substring(3, 8);
            return yy + "年" + mm + "月" + dd + "日 " + hr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 计算时间差
     *
     * @param time1
     * @param time2
     * @return
     */
    public static long getDiffTime(long time1, long time2) {
        long delta = time1 - time2;
        if (delta < 0) {
            delta = -delta;
        }
        return delta;
    }

    /**
     * 计算时差
     *
     * @param starttime
     * @param endtime
     * @return
     */
    public static long calTimeDur(String starttime, String endtime) {
        long l = 0l;
        try {
            SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT2);
            Date now = df.parse(starttime);
            Date date = df.parse(endtime);
            l = now.getTime() - date.getTime();
            return l;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
