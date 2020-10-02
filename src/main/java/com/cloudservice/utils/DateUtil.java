package com.cloudservice.utils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具
 * <p>java.util.Date 操作工具</p>
 *
 * @author	陈晨(17061934)
 * @date	2013.08.02
 * @see		SimpleDateFormat
 * @see		Calendar
 * @see		Date
 * @since	1.0
 */
public final class DateUtil {
    // 日志
    private static final Logger LOG = LoggerFactory.getLogger(DateUtil.class);

	/*
	 * 毫秒
	 */
    /** 1 hour milliseconds */
    public static final long HOUR_TIME = 60 * 60 * 1000L;
    /** 1 day milliseconds*/
    public static final long DAY_TIME = 24 * HOUR_TIME;
    /** 1 week milliseconds */
    public static final long WEEK_TIME = 7 * DAY_TIME;
    /** 1 month milliseconds, not entirely accurate */
    public static final long MONTH_TIME = 30 * DAY_TIME;
    /** 1 year milliseconds, not entirely accurate */
    public static final long YEAR_TIME = 365 * DAY_TIME;

    /*
     * 基础日期格式
     */
    public static final String YEAR = "yyyy";
    public static final String YEAR_2 = "yy";
    public static final String MONTH = "MM";
    public static final String DAY = "dd";
    public static final String HOUR = "HH";
    public static final String HOUR_12 = "hh";
    public static final String MINUTE = "mm";
    public static final String SECOND = "ss";
    public static final String MILLISECOND = "SSS";
    /*
     * 基础日期正则
     */
    public static final String REGEX_YEAR = "[0-9]{4}";
    public static final String REGEX_MONTH = "((0[1-9])|(1[0-2]))";
    public static final String REGEX_DAY = "((0[1-9])|([12][0-9])|(3[01]))";
    public static final String REGEX_HOUR = "([01][0-9]|(2[0-3]))";
    public static final String REGEX_HOUR_12 = "((0[0-9])|(1[01]))";
    public static final String REGEX_MINUTE = "[0-5][0-9]";
    public static final String REGEX_SECOND = "[0-5][0-9]";
    public static final String REGEX_MILLISECOND = "[0-9]{3}";

    /*
     * 常规日期格式
     */
    public static final String TIME = HOUR + ":" + MINUTE + ":" + SECOND;
    public static final String TIME_ALL = TIME + "." + MILLISECOND;
    public static final String DATE = YEAR + "-" + MONTH + "-" + DAY;
    public static final String DATE_LONG = DATE + " " + TIME;
    public static final String DATE_ALL = DATE + " " + TIME_ALL;

    /*
     * 常规"."日期格式
     */
    public static final String DATE_POINT = YEAR + "." + MONTH + "." + DAY;
    public static final String DATE_LONG_POINT = DATE + " " + TIME;
    public static final String DATE_ALL_POINT = DATE + " " + TIME_ALL;

    /*
     * 中国日期格式
     */
    public static final String TIME_CHINA = HOUR + "时" + MINUTE + "分" + SECOND + "秒";
    public static final String TIME_ALL_CHINA = TIME_CHINA + MILLISECOND + "毫秒";
    public static final String DATE_CHINA = YEAR + "年" + MONTH + "月" + DAY + "日";
    public static final String DATE_LONG_CHINA = DATE_CHINA + " " + TIME_CHINA;
    public static final String DATE_ALL_CHINA = DATE_CHINA + " " + TIME_ALL_CHINA;

    /*
     * 流水日期格式
     */
    public static final String TIME_SERIAL = HOUR + MINUTE + SECOND;
    public static final String TIME_ALL_SERIAL = TIME_SERIAL + MILLISECOND;
    public static final String DATE_SERIAL = YEAR + MONTH + DAY;
    public static final String DATE_LONG_SERIAL = DATE_SERIAL + TIME_SERIAL;
    public static final String DATE_ALL_SERIAL = DATE_SERIAL + TIME_ALL_SERIAL;

    private DateUtil() {}

    /**
     * <p>获取当前日期</p>
     *
     * <pre>
     * DateUtil.now()	=	2017-07-12 18:26:35.123
     * </pre>
     *
     * @author	陈晨(17061934)
     * @return
     */
    public static Date now(Long... time) {
        if (ArrayUtils.isEmpty(time)) {
            return new Date(System.currentTimeMillis());
        }
        if (time[0] == null) {
            return null;
        }
        return new Date(time[0]);
    }

    /**
     * <p>通过基础日期格式获取具体日期值</p>
     *
     * <pre>
     * DateUtil.baseValue(2017-07-12, "yyyy")	=	2017
     * DateUtil.baseValue(2017-07-12, "MM") 	=	7
     * DateUtil.baseValue(2017-07-12, "dd") 	=	12
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @param baseFormat	基础日期格式
     * @return
     */
    public static int baseValue(Object date, String baseFormat) {
        if (!checkBaseDateFormat(baseFormat)) {
            return 0;
        }
        return Integer.parseInt(format(date, baseFormat));
    }

    /**
     * <p>获取日期年份</p>
     *
     * <pre>
     * DateUtil.year(2017-07-12 12:30:30.512)	=	2017
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static int year(Object date) {
        return baseValue(date, YEAR);
    }

    /**
     * <p>获取日期月份</p>
     *
     * <pre>
     * DateUtil.month(2017-07-12 12:30:30.512)	=	7
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static int month(Object date) {
        return baseValue(date, MONTH);
    }

    /**
     * <p>获取日期天</p>
     *
     * <pre>
     * DateUtil.day(2017-07-12 12:30:30.512)	=	12
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static int day(Object date) {
        return baseValue(date, DAY);
    }

    /**
     * <p>获取日期小时</p>
     *
     * <pre>
     * DateUtil.hour(2017-07-12 12:30:30.512)	=	12
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static int hour(Object date) {
        return baseValue(date, HOUR);
    }

    /**
     * <p>获取日期小时(12小时制)</p>
     *
     * <pre>
     * DateUtil.hour12(2017-07-12 00:30:30.512)	=	12
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static int hour12(Object date) {
        return baseValue(date, HOUR_12);
    }

    /**
     * <p>获取日期分钟</p>
     *
     * <pre>
     * DateUtil.minute(2017-07-12 12:30:30.512)	=	30
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static int minute(Object date) {
        return baseValue(date, MINUTE);
    }

    /**
     * <p>获取日期秒</p>
     *
     * <pre>
     * DateUtil.second(2017-07-12 12:30:30.512)	=	30
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static int second(Object date) {
        return baseValue(date, SECOND);
    }

    /**
     * <p>获取日期毫秒</p>
     *
     * <pre>
     * DateUtil.milliSecond(2017-07-12 12:30:30.512)	=	512
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static int milliSecond(Object date) {
        return baseValue(date, MILLISECOND);
    }

    /**
     * @description 获取今天是周几
     * <p>
     *     0 - 6
     *     周日 ~ 周六
     * </p>
     *
     * <pre>
     * 〈举例说明〉
     * </pre>
     *
     * @auther  陈晨(96421)
     * @date    2020/10/1 22:52
     * @param   date
     */
    public static int week(Object... date) {
        Calendar calendar = Calendar.getInstance();
        if (ArrayUtils.isNotEmpty(date)) {
            calendar.setTime((Date) date[0]);
        }
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * <p>获取日期为当年的第几天</p>
     *
     * <pre>
     * DateUtil.day4Year(2017-07-12 12:30:30.512)	=	N
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static int getDays4Year(Object date) {
        if (!isDate(date)) {
            return 0;
        }
        // 获取当年年份
        int year = year(date);
        // 获取当年第一天时间
        Date firstDay = parse(year + "-01-01 00:00:00.000", DATE_ALL);
        if (firstDay == null) {
            return 0;
        }
        // 计算时间与当年第一天相差的毫秒
        long dayTime = ((Date) date).getTime() - firstDay.getTime();
        // 计算天数
        return ((int) (dayTime / DAY_TIME)) + 1;
    }

    /**
     * <p>判断日期是否为闰年</p>
     *
     * <pre>
     * DateUtil.isLeap(2017-07-12 12:30:30.512)		=	false
     * DateUtil.isLeap(2000-07-12 12:30:30.512)		=	true
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static boolean isLeap(Object date) {
        int year = year(date);
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            return true;
        }
        return false;
    }

    /**
     * <p>获取月天数</p>
     *
     * <pre>
     * DateUtil.monthDay(2017-07-12 12:30:30.512)	=	31
     * DateUtil.monthDay(2017-04-12 12:30:30.512)	=	30
     * DateUtil.monthDay(2017-02-12 12:30:30.512)	=	28
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static int getMonthDays(Object date) {
        if (!isDate(date)) {
            return 0;
        }
        int month = month(date);
        switch (month) {
            case 2:
                return isLeap(date) ? 29 : 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }
    }

    /**
     * <p>日期增加基础值</p>
     *
     * <pre>
     * DateUtil.add(2017-07-12 12:30:30.512, "yyyy", 1)		=	2018-07-12 12:30:30.512
     * DateUtil.add(2017-07-12 12:30:30.512, "MM", 1)		=	2017-08-12 12:30:30.512
     * DateUtil.add(2017-07-12 12:30:30.512, "dd", 1)		=	2017-07-13 12:30:30.512
     * DateUtil.add(2017-07-12 12:30:30.512, "HH", 1)		=	2017-07-12 13:30:30.512
     * DateUtil.add(2017-07-12 12:30:30.512, "hh", 1)		=	2017-07-12 01:30:30.512
     * DateUtil.add(2017-07-12 12:30:30.512, "mm", 1)		=	2017-07-12 12:31:30.512
     * DateUtil.add(2017-07-12 12:30:30.512, "ss", 1)		=	2017-07-12 12:30:31.512
     * DateUtil.add(2017-07-12 12:30:30.512, "SSS", 1)		=	2017-07-12 12:30:30.513
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static Date add(Object date, String format, int count) {
        if (!isDate(date) || !checkBaseDateFormat(format)) {
            return null;
        }

        // 设置Calendar field
        int field;
        if (YEAR.equals(format)) {
            field = Calendar.YEAR;
        }
        else if (MONTH.equals(format)) {
            field = Calendar.MONTH;
        }
        else if (DAY.equals(format)) {
            field = Calendar.DATE;
        }
        else if (HOUR.equals(format) || HOUR_12.equals(format)) {
            field = Calendar.HOUR;
        }
        else if (MINUTE.equals(format)) {
            field = Calendar.MINUTE;
        }
        else if (SECOND.equals(format)) {
            field = Calendar.SECOND;
        }
        else if (MILLISECOND.equals(format)) {
            field = Calendar.MILLISECOND;
        }
        else {
            return null;
        }

        // 增加时间
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date) date);
        cal.add(field, count);
        return cal.getTime();
    }

    /**
     * <p>日期增加年份</p>
     *
     * <pre>
     * DateUtil.addYear(2017-07-12 12:30:30.512, 1)		=	2018-07-12 12:30:30.512
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static Date addYear(Object date, int year) {
        return add(date, YEAR, year);
    }

    /**
     * <p>日期增加月份</p>
     *
     * <pre>
     * DateUtil.addMonth(2017-07-12 12:30:30.512, 1)	=	2017-08-12 12:30:30.512
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static Date addMonth(Object date, int month) {
        return add(date, MONTH, month);
    }

    /**
     * <p>日期增加天数</p>
     *
     * <pre>
     * DateUtil.addDay(2017-07-12 12:30:30.512, 1)		=	2017-07-13 12:30:30.512
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static Date addDay(Object date, int day) {
        return add(date, DAY, day);
    }

    /**
     * <p>日期增加小时</p>
     *
     * <pre>
     * DateUtil.addHour(2017-07-12 12:30:30.512, 1)		=	2017-07-12 13:30:30.512
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static Date addHour(Object date, int hour) {
        return add(date, HOUR, hour);
    }

    /**
     * <p>日期增加分钟</p>
     *
     * <pre>
     * DateUtil.addMinute(2017-07-12 12:30:30.512, 1)	=	2017-07-12 12:31:30.512
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static Date addMinute(Object date, int minute) {
        return add(date, MINUTE, minute);
    }

    /**
     * <p>日期增加秒</p>
     *
     * <pre>
     * DateUtil.addSecond(2017-07-12 12:30:30.512, 1)	=	2017-07-12 12:30:31.512
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static Date addSecond(Object date, int second) {
        return add(date, SECOND, second);
    }

    /**
     * <p>日期增加毫秒</p>
     *
     * <pre>
     * DateUtil.addMillisecond(2017-07-12 12:30:30.512, 1)	=	2017-07-12 12:30:30.513
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date			java.util.Date
     * @return
     */
    public static Date addMillisecond(Object date, int millisecond) {
        return add(date, MILLISECOND, millisecond);
    }

    /**
     * <p>获取当前系统时区</p>
     *
     * <pre>
     * DateUtil.timeZone()	=	8
     * </pre>
     *
     * @author	陈晨(17061934)
     * @return	-12 - 12
     */
    public static int timeZone() {
        Date init = new Date(0);
        int year = year(init);
        return hour(init) * (year < 1970 ? -1 : 1);
    }

    /**
     * <p>在当前时区基础上调整时区</p>
     *
     * <pre>
     * DateUtil.setTimeZone(2017-07-12 12:00:00, -8)	=	2017-07-12 04:00:00
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date		java.util.Date
     * @param timeZone	-12 - 12
     * @return
     */
    public static Date setTimeZone(Object date, int timeZone) {
        if (!isDate(date)) {
            return null;
        }

        // 时区区间判断
        if (timeZone > 12 || timeZone < -12) {
            throw new IllegalArgumentException("Time Zone Error, -12 <= hour <= 12");
        }
        Date localDate = new Date(((Date) date).getTime() + timeZone * HOUR_TIME);
        return localDate;
    }

    /**
     * <p>获取0点日期</p>
     *
     * <pre>
     * DateUtil.modDay(2017-07-12 12:00:00)	=	2017-07-12 00:00:00
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date		java.util.Date
     * @return
     */
    public static Date modDay(Object date) {
        if (!isDate(date)) {
            return null;
        }

        // 时区
        long timeZone = timeZone() * HOUR_TIME;
        // 天毫秒取整
        long dayTime = ((((Date) date).getTime() + timeZone) / DAY_TIME) * DAY_TIME;
        return new Date(dayTime - timeZone);
    }

    /**
     * <p>格式化日期, 格式化失败抛出异常</p>
     *
     * <pre>
     * DateUtil.format(2017-07-12, "yyyy-MM-dd")	=	"2017-07-12"
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date		java.util.Date
     * @param format	日期格式字符串
     * @return
     */
    public static String format(Object date, String format) {
        if (!isDate(date) || !checkDateFormat(format)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (IllegalArgumentException e) {
            LOG.warn("date[" + date
                    + "], format[" + format
                    + "], format exception,e={}",e);
        }
        return null;
    }

    public static String formatDateTime(Object date) {
        return format(date, DATE_LONG);
    }

    public static String formatDate(Object date) {
        return format(date, DATE);
    }

    public static String formatTime(Object date) {
        return format(date, TIME);
    }

    /**
     * <p>格式化日期, 格式化失败返回空串</p>
     *
     * <pre>
     * DateUtil.format(2017-07-12, "yyyy-MM-dd")	=	"2017-07-12"
     * DateUtil.format(null, "yyyy-MM-dd")			=	""
     * DateUtil.format(2017-07-12, "123abc")		=	""
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date		java.util.Date
     * @param format	日期格式字符串
     * @return
     */
    public static String formatNaN(Object date, String format) {
        String formatDate = format(date, format);
        return formatDate == null ? "" : formatDate;
    }

    /**
     * <p>将日期字符串转换为java.util.Date日期, 转换失败则抛出异常</p>
     *
     * <pre>
     * DateUtil.format("2017-07-12", "yyyy-MM-dd")	=	2017-07-12 00:00:00
     * DateUtil.format("123abc", "yyyy-MM-dd")		=	null
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param dateStr	日期字符串
     * @param format	日期格式字符串
     * @return
     */
    public static Date parse(String dateStr, String format) {
        if (StringUtils.isBlank(dateStr) || !checkDateFormat(format)) {
            return null;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            LOG.warn("dateStr[" + dateStr
                    + "], format[" + format
                    + "], parse exception");
        }
        return null;
    }

    public static Date parseDatetime(String dateStr) {
        return parse(dateStr, DATE_LONG);
    }

    /**
     * <p>将日期格式转换为对应的正则表达式</p>
     *
     * <pre>
     * DateUtil.format2Regex("yyyy-MM-dd")	=	"[0-9]{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([12][0-9])|(3[01]))";
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param format	日期格式字符串
     * @return
     */
    public static String format2Regex(String format) {
        if (!checkDateFormat(format)) {
            return null;
        }

        // 替换正则特殊字符
        String regex = format.replaceAll("\\\\", "\\\\\\\\")
                .replaceAll("\\^", "\\\\\\^")
                .replaceAll("\\$", "\\\\\\$")
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\*", "\\\\*")
                .replaceAll("\\+", "\\\\+")
                .replaceAll("\\?", "\\\\?")
                .replaceAll("\\|", "\\\\|")
                .replaceAll("\\{", "\\\\{")
                .replaceAll("\\}", "\\\\}")
                .replaceAll("\\[", "\\\\[")
                .replaceAll("\\]", "\\\\]")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)")
                ;
        LOG.debug("ReplaceRegexChar: " + regex);

        // 替换日期正则
        regex = regex.replaceAll(YEAR, REGEX_YEAR)
                .replaceAll(MONTH, REGEX_MONTH)
                .replaceAll(DAY, REGEX_DAY)
                .replaceAll(HOUR, REGEX_HOUR)
                .replaceAll(HOUR_12, REGEX_HOUR_12)
                .replaceAll(MINUTE, REGEX_MINUTE)
                .replaceAll(SECOND, REGEX_SECOND)
                .replaceAll(MILLISECOND, REGEX_MILLISECOND)
        ;
        LOG.debug("RegexChar: " + regex);
        return regex;
    }

    /**
     * 校验日期字符串格式
     * <pre>
     * DateUtil.format("2017-07-12", "yyyy-MM-dd")	true
     * DateUtil.format("123abc", "yyyy-MM-dd")		false
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param dateStr	日期字符串
     * @param format	日期格式字符串
     * @return
     */
    public static boolean matches(String dateStr, String format) {
        if (StringUtils.isBlank(dateStr) || !checkDateFormat(format)) {
            return false;
        }
        String regex = format2Regex(format);
        return dateStr.matches(regex);
    }

    /**
     * 检查日期类型
     * <p>检查日期是否为空, 且为java.util.Date类型</p>
     *
     * <pre>
     * DateUtil.checkDate(null)		false
     * DateUtil.checkDate(Date)		true
     * DateUtil.checkDate(Other)	false
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param date
     */
    public static boolean isDate(Object date) {
        return date != null && date instanceof Date;
    }

    /**
     * 检查日期格式
     * <p>格式字符串包含任何基础日期格式, 则校验通过</p>
     *
     * <pre>
     * DateUtil.checkDateFormat(null)			false
     * DateUtil.checkDateFormat("") 			false
     * DateUtil.checkDateFormat("  ")			false
     * DateUtil.checkDateFormat("123abc")		false
     * DateUtil.checkDateFormat("yyyy") 		true
     * DateUtil.checkDateFormat("123yyyyabc")	true
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param format
     */
    public static boolean checkDateFormat(String format) {
        if (StringUtils.isBlank(format)) {
            return false;
        }
        boolean isFormat = format.contains(YEAR);
        isFormat |= format.contains(MONTH);
        isFormat |= format.contains(DAY);
        isFormat |= format.contains(HOUR);
        isFormat |= format.contains(HOUR_12);
        isFormat |= format.contains(MINUTE);
        isFormat |= format.contains(SECOND);
        isFormat |= format.contains(MILLISECOND);
        return isFormat;
    }

    /**
     * 检查基础日期格式
     * <p>格式字符串必须为基础日期格式, 否则抛出异常</p>
     *
     * <pre>
     * DateUtil.checkDateFormat(null)			false
     * DateUtil.checkDateFormat("") 			false
     * DateUtil.checkDateFormat("  ")			false
     * DateUtil.checkDateFormat("123abc")		false
     * DateUtil.checkDateFormat("yyyy") 		true
     * DateUtil.checkDateFormat("123yyyyabc")	false
     * </pre>
     *
     * @author	陈晨(17061934)
     * @param format
     */
    public static boolean checkBaseDateFormat(String format) {
        if (StringUtils.isBlank(format)) {
            return false;
        }
        boolean isFormat = "".equals(format.replaceFirst(YEAR, ""));
        isFormat |= "".equals(format.replaceFirst(MONTH, ""));
        isFormat |= "".equals(format.replaceFirst(DAY, ""));
        isFormat |= "".equals(format.replaceFirst(HOUR, ""));
        isFormat |= "".equals(format.replaceFirst(HOUR_12, ""));
        isFormat |= "".equals(format.replaceFirst(MINUTE, ""));
        isFormat |= "".equals(format.replaceFirst(SECOND, ""));
        isFormat |= "".equals(format.replaceFirst(MILLISECOND, ""));
        return isFormat;
    }

}


