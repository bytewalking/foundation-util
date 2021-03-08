package com.evan.foundation.util;


import org.joda.time.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * @author evan
 * @since 2021-03-08 11:24
 */
public class DateUtils {


    public static final String LONG_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String SHORT_FORMAT = "yyyyMMdd";

    public static final String SIMPLE_FORMAT = "yyyy-MM-dd";

    public static final String YEAR_FORMAT = "yyyy";

    public static final String YEAR_MONTH_FORMAT = "yyyy-MM";

    public static final String MONTH_DAY_FORMAT = "MM-dd";

    public static final String YEAR_MONTH_CHINA_FORMAT = "yyyy年MM月";

    public static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";


    /**
     * 两个日期相差的年数
     *
     * @param start
     * @param end
     * @return
     */
    public static int diffYears(Date start, Date end) {
        return Years.yearsBetween(new DateTime(start.getTime()), new DateTime(end.getTime())).getYears();
    }


    /**
     * 两个日期相差的天数
     *
     * @param start
     * @param end
     * @return
     */
    public static int diffDays(Date start, Date end) {
        return Days.daysBetween(new DateTime(start.getTime()), new DateTime(end.getTime())).getDays();
    }

    /**
     * 两个日期相差的分钟数
     *
     * @param start
     * @param end
     * @return
     */
    public static int diffMinutes(Date start, Date end) {
        return Minutes.minutesBetween(new DateTime(start.getTime()), new DateTime(end.getTime())).getMinutes();
    }

    /**
     * 两个日期相差的秒数
     *
     * @param start
     * @param end
     * @return
     */
    public static int diffSeconds(Date start, Date end) {
        return Seconds.secondsBetween(new DateTime(start.getTime()), new DateTime(end.getTime())).getSeconds();
    }

    /**
     * 当前日期
     *
     * @return
     */
    public static Date nowDate() {
        return new Date();
    }


    /**
     * 日期转字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        return dateToString(date, pattern, TimeZone.getDefault());
    }

    /**
     * 日期转字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date, String pattern, TimeZone timeZone) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(date);
    }

    /**
     * 字符串转日期
     *
     * @param str
     * @param pattern
     * @return
     */
    public static Date stringToDate(String str, String pattern) {
        return stringToDate(str, pattern, TimeZone.getDefault());
    }

    /**
     * 字符串转日期
     *
     * @param str
     * @param pattern
     * @return
     */
    public static Date stringToDate(String str, String pattern, TimeZone timeZone) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            simpleDateFormat.setTimeZone(timeZone);
            return simpleDateFormat.parse(str);
        } catch (Exception ex) {
            throw new RuntimeException("stringToDate error str " + str + " pattern " + pattern);
        }
    }


    public static String toString(Date date) {
        return dateToString(date, LONG_FORMAT);
    }

    public static String toShortString(Date date) {
        return dateToString(date, SHORT_FORMAT);
    }

    public static String toSimpleString(Date date) {
        return dateToString(date, SIMPLE_FORMAT);
    }

    /**
     * 指定日期新增几天
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
    }

    /**
     * 指定日期新增几年
     *
     * @param date
     * @param years
     * @return
     */
    public static Date addYears(Date date, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, years);
        return c.getTime();
    }


    /**
     * 指定日期新增几分钟
     *
     * @param date
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date date, int minutes) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, minutes);
        return c.getTime();
    }


    /**
     * 指定日期新增几秒
     *
     * @param date
     * @param seconds
     * @return
     */
    public static Date addSeconds(Date date, int seconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, seconds);
        return c.getTime();
    }


    /**
     * 指定日期新增几小时
     *
     * @param date
     * @param hours
     * @return
     */
    public static Date addHours(Date date, int hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, hours);
        return c.getTime();
    }

    /**
     * 精确到天
     *
     * @param date
     * @return
     */
    public static Date preciseDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 比较日期
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isBigger(Date date1, Date date2) {
        return date1.getTime() > date2.getTime();
    }


    /**
     * 获取星期
     *
     * @param date
     * @return
     */
    public static String getWeek(Date date) {
        return getWeek(date, TimeZone.getDefault());
    }


    /**
     * 获取周几
     *
     * @param dt
     * @param timeZone
     * @return
     */
    public static String getWeek(Date dt, TimeZone timeZone) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.setTimeZone(timeZone);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 获取月份
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        return getMonth(date, TimeZone.getDefault());
    }

    /**
     * 获取星期
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date, TimeZone timeZone) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setTimeZone(timeZone);
        int month = c.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 获取天
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        return getDay(date, TimeZone.getDefault());
    }

    /**
     * 获取天
     *
     * @param date
     * @return
     */
    public static int getDay(Date date, TimeZone timeZone) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(timeZone);
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        return day;
    }


    /**
     * 获小时
     *
     * @param date
     * @return
     */
    public static int getHour(Date date, TimeZone timeZone) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setTimeZone(timeZone);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获分
     *
     * @param date
     * @return
     */
    public static int getMinutes(Date date, TimeZone timeZone) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setTimeZone(timeZone);
        return c.get(Calendar.MINUTE);
    }

    /**
     * 校验字符串日期格式
     *
     * @param str
     * @param pattern
     * @return
     */
    public static boolean isValid(String str, String pattern) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            simpleDateFormat.parse(str);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * 将两个date格式化后比较
     *
     * @param date1  date1
     * @param date2  date2
     * @param format format
     * @return equals
     */
    public static boolean equals(Date date1, Date date2, String format) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.parse(simpleDateFormat.format(date1)).equals(simpleDateFormat.parse(simpleDateFormat.format(date2)));
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 本地时间转化为 utc 时间
     *
     * @param localDate
     * @return
     */
    public static Date getUtcTime(Date localDate) {

        long localTimeInMillis = localDate.getTime();

        /** long时间转换成Calendar */

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(localTimeInMillis);

        /** 取得时间偏移量 */

        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);

        /** 取得夏令时差 */

        int dstOffset = calendar.get(Calendar.DST_OFFSET);

        /** 从本地时间里扣除这些差量，即可以取得UTC时间*/

        calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        /** 取得的时间就是UTC标准时间 */
        Date utcDate = new Date(calendar.getTimeInMillis());
        return utcDate;
    }


    /**
     * utc 时间转换为本地时间
     *
     * @param utcDate
     * @return
     */
    public static Date toLocalDate(Date utcDate) {
        return stringToDate(dateToString(utcDate, LONG_FORMAT), LONG_FORMAT);
    }


    /**
     * 计算两个时间差额 格式化 天时分秒 （end-start）
     *
     * @param start
     * @param end
     * @return
     */
    public static String formatDiffTime(Date start, Date end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            return null;
        }
        long millisecond = end.getTime() - start.getTime();
        return formatMillisecond(millisecond);
    }

    /**
     * 计算毫秒差额 格式化 天时分秒
     *
     * @param millisecond
     * @return
     */
    public static String formatMillisecond(long millisecond) {
        if (millisecond < 0) {
            return null;
        }
        long day = millisecond / (24 * 60 * 60 * 1000);
        long hour = (millisecond / (60 * 60 * 1000) - day * 24);
        long min = ((millisecond / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (millisecond / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(day).append("天");
        }
        if (hour > 0) {
            sb.append(hour).append("小时");
        }
        if (min > 0) {
            sb.append(min).append("分");
        }
        if (s > 0) {
            sb.append(s).append("秒");
        }
        return sb.toString();
    }


    /**
     * 获取本月开始日期
     *
     * @return Date
     **/
    public static Date getMonthStart() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getMonthStart(String month) {
        try {
            DateFormat fmt = new SimpleDateFormat(YEAR_MONTH_FORMAT);
            Date date = fmt.parse(month + "-01");
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        } catch (ParseException ex) {
            throw new IllegalArgumentException("参数格式错误" + month);
        }
    }

    /**
     * 获取本月最后一天
     *
     * @return Date
     **/
    public static Date getMonthEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static Date getMonthEnd(String month) {
        try {
            DateFormat fmt = new SimpleDateFormat(YEAR_MONTH_FORMAT);
            Date date = fmt.parse(month + "-01");
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.MILLISECOND, 999);
            return cal.getTime();
        } catch (ParseException ex) {
            throw new IllegalArgumentException("参数格式错误" + month);
        }
    }

    public static Date toDate(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date toDate(LocalDate localDate) {
        return toDate(localDate.atStartOfDay());
    }
}


