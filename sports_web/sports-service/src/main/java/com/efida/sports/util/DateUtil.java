package com.efida.sports.util;

/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 常用日期函数
 * @author zhiyang
 * @version $Id: DateUtil.java, v 0.1 2016年5月19日 下午7:29:13 zhiyang Exp $
 */
public class DateUtil {

    /**logger*/
    private static Logger      logger                 = LoggerFactory.getLogger(DateUtil.class);

    /** yyyyMMdd */
    public final static String SHORT_FORMAT           = "yyyyMMdd";

    /** yyyyMMddHHmmss */
    public final static String LONG_FORMAT            = "yyyyMMddHHmmss";

    /** yyyy-MM-dd */
    public final static String WEB_FORMAT             = "yyyy-MM-dd";

    /** HHmmss */
    public final static String TIME_FORMAT            = "HHmmss";

    /** yyyyMM */
    public final static String MONTH_FORMAT           = "yyyyMM";

    /** yyyy年MM月dd日 */
    public final static String CHINA_FORMAT           = "yyyy年MM月dd日";

    /** yyyy-MM-dd HH:mm:ss */
    public final static String LONG_WEB_FORMAT        = "yyyy-MM-dd HH:mm:ss";

    /** yyyy-MM-dd HH:mm */
    public final static String LONG_WEB_FORMAT_NO_SEC = "yyyy-MM-dd HH:mm";

    /**
     * 判定是否为空或者null
     * 
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {

        if (str == null || str.length() < 1) {
            return true;
        }

        return false;
    }

    /**
     * 判定字符串是否为null
     * 
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {

        return str == null;
    }

    /**
     * 日期对象解析成日期字符串基础方法，可以据此封装出多种便捷的方法直接使用
     * 
     * @param date 待格式化的日期对象
     * @param format 输出的格式
     * @return 格式化的字符串
     */
    public static String format(Date date, String format) {
        if (date == null || format.length() < 1) {
            return "";
        }

        return new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE).format(date);
    }

    /**
     * 格式化当前时间
     * 
     * @param format 输出的格式
     * @return
     */
    public static String formatCurrent(String format) {
        if (isBlank(format)) {
            return "";
        }

        return format(new Date(), format);
    }

    /**
     * 日期字符串解析成日期对象基础方法，可以在此封装出多种便捷的方法直接使用
     * 
     * @param dateStr 日期字符串
     * @param format 输入的格式
     * @return 日期对象
     * @throws ParseException 
     */
    public static Date parseWithNoThrow(String dateStr, String format) {
        if (isBlank(format)) {
            return null;
        }

        if (dateStr == null || dateStr.length() < format.length()) {
            return null;
        }

        try {
            return new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE).parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parse(String dateStr, String format) throws ParseException {
        if (isBlank(format)) {
            throw new ParseException("format can not be null.", 0);
        }

        if (dateStr == null || dateStr.length() < format.length()) {
            throw new ParseException("date string's length is too small.", 0);
        }

        return new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE).parse(dateStr);
    }

    /**
     * 日期字符串格式化基础方法，可以在此封装出多种便捷的方法直接使用
     * 
     * @param dateStr 日期字符串
     * @param formatIn 输入的日期字符串的格式
     * @param formatOut 输出日期字符串的格式
     * @return 已经格式化的字符串
     * @throws ParseException
     */
    public static String format(String dateStr, String formatIn,
                                String formatOut) throws ParseException {

        Date date = parse(dateStr, formatIn);
        return format(date, formatOut);
    }

    /**
     * 把日期对象按照<code>yyyyMMdd</code>格式解析成字符串
     * 
     * @param date 待格式化的日期对象 
     * @return 格式化的字符串
     */
    public static String formatShort(Date date) {
        return format(date, SHORT_FORMAT);
    }

    /**
     * 把日期对象按照<code>yyyyMMddHHmmss</code>格式解析成字符串
     * 
     * @param date 待格式化的日期对象 
     * @return 格式化的字符串
     */
    public static String formatLong(Date date) {
        return format(date, LONG_FORMAT);
    }

    /**
     * 把日期字符串按照<code>yyyyMMdd</code>格式，进行格式化
     * 
     * @param dateStr 待格式化的日期字符串
     * @param formatIn 输入的日期字符串的格式 
     * @return 格式化的字符串
     */
    public static String formatShort(String dateStr, String formatIn) throws ParseException {
        return format(dateStr, formatIn, SHORT_FORMAT);
    }

    /**
     * 把日期对象按照<code>yyyy-MM-dd</code>格式解析成字符串
     * 
     * @param date 待格式化的日期对象 
     * @return 格式化的字符串
     */
    public static String formatWeb(Date date) {
        return format(date, WEB_FORMAT);
    }

    /**
     * 把日期字符串按照<code>yyyy-MM-dd</code>格式，进行格式化
     * 
     * @param dateStr 待格式化的日期字符串
     * @param formatIn 输入的日期字符串的格式 
     * @return 格式化的字符串
     * @throws ParseException 
     */
    public static String formatWeb(String dateStr, String formatIn) throws ParseException {
        return format(dateStr, formatIn, WEB_FORMAT);
    }

    /**
     * 把日期对象按照<code>yyyyMM</code>格式解析成字符串
     * 
     * @param date 待格式化的日期对象 
     * @return 格式化的字符串
     */
    public static String formatMonth(Date date) {

        return format(date, MONTH_FORMAT);
    }

    /**
     * 把日期对象按照<code>HHmmss</code>格式解析成字符串
     * 
     * @param date 待格式化的日期对象 
     * @return 格式化的字符串
     */
    public static String formatTime(Date date) {
        return format(date, TIME_FORMAT);
    }

    /**
     * 将字符串转换为data
     * @param sDate yyyyMMddHHmmss
     * @return
     */
    public static Date parseDateLongFormat(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat(LONG_FORMAT);
        Date d = null;

        if ((sDate != null) && (sDate.length() == LONG_FORMAT.length())) {
            try {
                d = dateFormat.parse(sDate);
            } catch (ParseException ex) {
                return null;
            }
        }

        return d;
    }

    /**
     * 在日志字符串上进行秒的增加操作
     * @param dateStr    日期字符串
     * @param format     格式
     * @param second     秒
     * @return
     */
    public static String add(String dateStr, String format, int second) {
        Date time = null;
        try {
            time = parse(dateStr, format);
        } catch (ParseException e) {
            // 什么也不做
            logger.warn("DateUtil解析错误" + e);
        }

        if (null == time) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.add(Calendar.SECOND, second);

        return format(cal.getTime(), format);
    }

    /**
     * 计算时间差(s)
     * 
     * @param timeStr1
     * @param timeStr2
     * @param format
     * @return
     */
    public static int sub(String timeStr1, String timeStr2, String format) {
        Date time1 = null, time2 = null;
        try {
            time1 = parse(timeStr1, format);
            time2 = parse(timeStr2, format);
        } catch (ParseException e) {
            // 什么也不做
            logger.warn("DateUtil解析错误" + e);
        }
        if (time1 == null && time2 == null) {
            return 0;
        }
        Calendar c1 = Calendar.getInstance(), c2 = Calendar.getInstance();
        c1.setTime(time1);
        c2.setTime(time2);
        long gap = (c1.getTimeInMillis() - c2.getTimeInMillis()) / 1000;
        return (int) gap;
    }

    /**
     * 两个日期相差多少秒 
     *  
     * @param date1 
     * @param date2 
     * @return 
     */
    public static int getTimeDelta(Date date1, Date date2) {
        long timeDelta = (date1.getTime() - date2.getTime()) / 1000;//单位是秒  
        int secondsDelta = timeDelta > 0 ? (int) timeDelta : (int) Math.abs(timeDelta);
        return secondsDelta;
    }

    /**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */
    public static long daysBetween(Date smdate, Date bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return between_days;
        } catch (Exception e) {
            return 0;
        }

    }

    public static Date getYesterdayStartTime() {
        return getStartTime(new Date(System.currentTimeMillis() - 86400 * 1000));
    }

    public static Date getYesterdayEndTime() {
        return getEndTime(new Date(System.currentTimeMillis() - 86400 * 1000));
    }

    //获取前一天结束时间
    public static Date getEndTime(Date date) {
        Calendar todayEnd = Calendar.getInstance();
        if (date != null) {
            todayEnd.setTime(date);
        }
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    public static Date getNextDay(Date date) {
        Calendar calender = Calendar.getInstance();
        if (calender != null) {
            calender.setTime(date);
        }
        calender.add(Calendar.DATE, 1);
        return calender.getTime();
    }

    //开始时间
    public static Date getStartTime(Date date) {
        Calendar todayStart = Calendar.getInstance();
        if (date != null) {
            todayStart.setTime(date);
        }
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    /**
     * 日期对象解析成日期字符串基础方法，可以据此封装出多种便捷的方法直接使用
     * 
     * @param date 待格式化的日期对象
     * @param format 输出的格式
     * @return 格式化的字符串
     */
    public static String format(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE).format(date);
    }

    public static String formatDay(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(date);
    }

    /** 
    *字符串的日期格式的计算 
    */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static String timeDifference(Date startTime, Date endTime) {
        String diffStr = "";
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        //获取分钟数
        long sec = (endTime.getTime() - startTime.getTime());
        long day = sec / nd;
        if (day != 0) {
            diffStr += day + "天";
        }
        // 计算差多少小时
        long hour = sec % nd / nh;
        if (hour != 0) {
            diffStr += hour + "小时";
        }
        // 计算差多少分钟
        long min = sec % nd % nh / nm;
        if (min != 0) {
            diffStr += min + "分钟";
        }
        return diffStr;
    }

    public static Date formatZHTime(String datdString) {
        if ("".equals(datdString) || null == datdString) {
            return null;
        }
        datdString = datdString.replace("GMT", "").replaceAll("\\(.*\\)", "");
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss z",
            Locale.ENGLISH);
        Date dateTrans = null;
        try {
            dateTrans = format.parse(datdString);
        } catch (ParseException e) {
        }
        return dateTrans;
    }

    /**
     * 日期比较(DATE1 大于DATE2 返回true)
     * 
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static Boolean compareDate(Date DATE1, Date DATE2) {
        boolean result = false;
        if (DATE1 == null || DATE1 == null) {
            return result;
        }
        if (DATE1.getTime() > DATE2.getTime()) {
            result = true;
        }
        return result;
    }

    public static Date strToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date strToDate1(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDayAgoTime(int days) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.DATE, todayStart.get(Calendar.DATE) - days);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

}
