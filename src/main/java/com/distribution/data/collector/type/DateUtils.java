package com.distribution.data.collector.type;

import com.datastax.driver.core.utils.UUIDs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class DateUtils {

	private static SimpleDateFormat dateformater;
    private static SimpleDateFormat timeformater;
    private static SimpleDateFormat dateTimeformater;
    private static long DAY_IN_MILLISECOND = 1000 * 3600 * 24;

	private DateUtils() {
    }

	/**
	 * 根据yyyy-M-dd字符串解析成相应的日期
	 * @param strDate yyyy-M-dd格式的日期
	 * @return 转换后的日期
	 */
    public static LocalDate parseDate(String strDate){
        return LocalDate.parse(strDate, getDateFormater());
    }

    public static LocalDateTime parseDate(String strDate, String pattern){
        return LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 根据HH:mm字符串解析成相应的时间格式
     * @param strTime HH:mm格式的时间格式
     * @return 转换后的时间
     */
    public final static LocalTime parseTime(String strTime){
		return LocalTime.parse(strTime, getTimeFormater());
    }

	/**
	 * 根据yyyy-M-dd hh:mm字符串解析成相应的日期时间
	 * @param strDateTime 以“yyy-M-dd hh:mm”为格式的时间字符串
	 * @return 转换后的日期
	 */
    public static LocalDateTime parseDateTime(String strDateTime){
        return LocalDateTime.parse(strDateTime, getDateTimeFormater());
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }
    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    public static void dateToLocalTime() {
        Date date = new Date();
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalTime localTime = localDateTime.toLocalTime();
    }

    public static Date localDateTimeToUdate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date localDateToUdate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date localTimeToUdate(LocalDate localDate, LocalTime localTime) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

	/**
	 * 得到日期的起始日期，例如2004-1-1 15:12，转换后为 2004-1-1 00:00
	 * @param date需要转换的日期
	 * @return转换后的日期
	 */
    public static Date getTodayStart(Date date){
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
        gc.set(GregorianCalendar.MINUTE, 0);
        gc.set(GregorianCalendar.SECOND, 0);
        gc.set(GregorianCalendar.MILLISECOND, 0);
        return gc.getTime();
    }

    public static Date get96Start(Date date){
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(date);
    	gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
    	gc.set(GregorianCalendar.MINUTE, 0);
    	gc.set(GregorianCalendar.SECOND, 0);
    	gc.set(GregorianCalendar.MILLISECOND, 0);
    	return gc.getTime();
    }
    /**
	 * 得到日期的起始日期，例如2004-1-1 15:12，转换后为 2004-1-1 00:00
	 * @param date需要转换的日期
	 * @return转换后的日期
	 */
    public static LocalDateTime getTodayStart(LocalDateTime localDateTime){
    	return localDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }
    /**
     * 得到日期的结束日期，例如2004-1-1 15:12，转换后为2004-1-2 00:00，注意为第二天的0点整
     * @param date所要转换的日期
     * @return 转换后的日期，为第二天的零点整
     */
    public static LocalDateTime getTodayEnd(LocalDateTime localDateTime){
    	return localDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0).plusDays(1);
    }

	/**
	 * 得到日期的结束日期，例如2004-1-1 15:12，转换后为2004-1-2 00:00，注意为第二天的0点整
	 * @param date所要转换的日期
	 * @return 转换后的日期，为第二天的零点整
	 */
    public static Date getTodayEnd(Date date){
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dateDayAdd(date, 1));

        gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
        gc.set(GregorianCalendar.MINUTE, 0);
        gc.set(GregorianCalendar.SECOND, 0);
        gc.set(GregorianCalendar.MILLISECOND, 0);
        return gc.getTime();
    }

    public static Date get96End(Date date){
    	GregorianCalendar gc = new GregorianCalendar();
    	 gc.setTime(date);
    	gc.set(GregorianCalendar.HOUR_OF_DAY, 23);
    	gc.set(GregorianCalendar.MINUTE, 45);
    	gc.set(GregorianCalendar.SECOND, 0);
    	gc.set(GregorianCalendar.MILLISECOND, 0);
    	return gc.getTime();
    }

	/**
	 * 得到日起所在月份的开始日期（第一天的开始日期），例如2004-1-15 15:10，转换后为2004-1-1 00:00
	 * @param month 需要转换的日期
	 * @return 转换后的日期
	 */
    public static Date getMonthBegin(Date month){
        GregorianCalendar ca = new GregorianCalendar();
        ca.setTime(month);
        int year = ca.get(GregorianCalendar.YEAR);
        int mon = ca.get(GregorianCalendar.MONTH);
        GregorianCalendar gCal = new GregorianCalendar(year, mon, 1);
        return gCal.getTime();
    }

    /**
     * 根据年、月返回由年、月构成的日期的月份开始日期
     * @param year所在的年
     * @param month 所在的月份，从1月到12月
     * @return 返回转换后的日期
     */
    public static Date getMonthBegin(int year, int month){
    	if (month <=0 || month > 12 ){
    		throw new IllegalArgumentException("month must from 1 to 12");
    	}

		GregorianCalendar gCal = new GregorianCalendar(year, month - 1, 1);
		return gCal.getTime();
    }

	/**
	 * 根据日起所在的月份，得到这个月的最后一个时刻日期，为下个月的第一天零点整
	 * @param month 需要转换的日期
	 * @return 转换后的日期
	 */
    public static Date getMonthEnd(Date month){
        GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(month);
        int lastDay = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		cal.set(GregorianCalendar.DAY_OF_MONTH, lastDay);
        return getTodayEnd(cal.getTime());
    }

    /**
     * 根据日期所在的星期，得到这个星期的开始日期，注意，每周从星期日开始计算
     * @param date 需要转换的日期
     * @return 转换后的日期
     */
    public static Date getWeekBegin(Date date){
		GregorianCalendar gCal = new GregorianCalendar();
		gCal.setTime(date);
		int startDay = gCal.getActualMinimum(GregorianCalendar.DAY_OF_WEEK);
		gCal.set(GregorianCalendar.DAY_OF_WEEK, startDay);

    	return gCal.getTime();
    }

    /**
     * 根据日起所在的星期，得到这个星期的最后结束日期，为下周开始第一天的零点整‘
     * @param date 需要转换的日期
     * @return 转换后的日期
     */
	public static Date getWeekEnd(Date date){
		GregorianCalendar gCal = new GregorianCalendar();
		gCal.setTime(date);
		int lastDay = gCal.getActualMaximum(GregorianCalendar.DAY_OF_WEEK);
		gCal.set(GregorianCalendar.DAY_OF_WEEK, lastDay);

		return getTodayEnd(gCal.getTime());
	}

	/**
	 * 根据年、月返回由年、月构成的日期的月份结束日期，为下一个月第一天零点整
     * @param year所在的年
     * @param month 所在的月份，从1月到12月
	 * @return 转换的日期
	 */
	public static Date getMonthEnd(int year, int month){
		Date start = getMonthBegin(year, month);
		return getMonthEnd(start);
	}

	/**
	 * 根据日期所在的年份，得到当年的开始日期，为每年的1月1日零点整
	 * @param date 需要转换的日期
	 * @return 转换后的日期
	 */
	public final static Date getYearBegin(Date date){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(GregorianCalendar.DAY_OF_YEAR, 1);
		return getTodayStart(cal.getTime());
	}

	/**
	 * 根据日期所在的年份，得到当年的结束日期，为来年的1月1日零点整
	 * @param date 需要转换的日期
	 * @return 转换后的日期
	 */
	public final static Date getYearEnd(Date date){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		int lastday = cal.getActualMaximum(GregorianCalendar.DAY_OF_YEAR);
		cal.set(GregorianCalendar.DAY_OF_YEAR, lastday);
//		System.out.println(cal.getTime());
		return getTodayEnd(cal.getTime());
	}

	/**
	 * 转换日期为 yyyy-M-dd格式的字符串
	 * @param date 需要转换的日期
	 * @return 转换后的日期字符串
	 */
    public static String formatDate(LocalDate date){
        return getDateFormater().format(date);
    }

    /**
     * 根据指定的转化模式，转换日期成字符串
     * @param date 需要转换的日期
     * @param pattern 转换模式，请参考javadoc中的DateFormater部分
     * @return 转换后的字符串
     */
	public final static String formatDate(Date date, String pattern){
		SimpleDateFormat formater = new SimpleDateFormat(pattern);
		return formater.format(date);
	}

	/**
	 * 根据指定的转化模式，转换日期成字符串
	 * @param date 需要转换的日期
	 * @param pattern 转换模式，请参考javadoc中的DateFormater部分
	 * @return 转换后的字符串
	 */
	public final static String formatDate(LocalDateTime date, String pattern){
		return DateTimeFormatter.ofPattern(pattern).format(date);
	}

	/**
	 * 转换指定日期成时间格式hh:mm格式的字符串
	 * @param date 指定的时间
	 * @return 转换后的字符产
	 */
    public static String formatTime(LocalTime date){
        return getTimeFormater().format(date);
    }

	/**
	 * 转换指定日期成yyyy-M-dd hh:mm格式的字符串
	 * @param date 需要转换的日期
	 * @return 转换后的字符串
	 */
    public static String formatDateTime(LocalDateTime date){
        return getDateTimeFormater().format(date);
    }

	/**
	 * 在指定日期的基础上，增加或是减少月份信息，如1月31日，增加一个月后，则为2月28日（非闰年）
	 * @param date 指定的日期
	 * @param months 增加或是减少的月份数，正数为增加，负数为减少
	 * @return 增加或是减少后的日期
	 */
    public final static Date dateMonthAdd(Date date, int months){
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        int m = cal.get(Calendar.MONTH) + months;
        if (m < 0){
            m += -12;
        }
        cal.roll(Calendar.YEAR, m / 12);
        cal.roll(Calendar.MONTH, months);
        return cal.getTime();
    }

	/**
	 * 在指定的日期基础上，增加或是减少天数
	 * @param date 指定的日期
	 * @param days 需要增加或是减少的天数，正数为增加，负数为减少
	 * @return 增加或是减少后的日期
	 */
    public final static Date dateDayAdd(Date date, int days){
        long now = date.getTime() + days * DAY_IN_MILLISECOND;
        return (new Date(now));
    }

    /**
     * 在指定的日期基础上，增加或是减少年数
     * @param date 指定的日期
     * @param age需要增加或是减少的年数，正数为增加，负数为减少
     * @return 增加或是减少后的日期
     */
    public final static Date dateYearAdd(Date date, int year){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.roll(GregorianCalendar.YEAR, year);
		return cal.getTime();
    }

    /**
     * 得到日期的年数
     * @param date 指定的日期
     * @return 返回指定日期的年数
     */
    public final static int getDateYear(Date date){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(GregorianCalendar.YEAR);
    }

    /**
     * 得到指定日期的月份数
     * @param date 指定的日期
     * @return 返回指定日期的月份数
     */
	public final static int getDateMonth(Date date){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(GregorianCalendar.MONDAY);
	}

	/**
	 * 得到制定日期在当前天数，例如2004-5-20日，返回141
	 * @param date 指定的日期
	 * @return 返回的天数
	 */
	public final static int getDateYearDay(Date date){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(GregorianCalendar.DAY_OF_YEAR);
	}

	public static int daysBetween(Date st, Date ed){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			st = sdf.parse(sdf.format(st));
			ed = sdf.parse(sdf.format(ed));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(st);
		long time1 = cal.getTimeInMillis();
		cal.setTime(ed);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 得到制定日期在当前月中的天数，例如2004-5-20日，返回20
	 * @param date 指定的日期
	 * @return 返回天数
	 */
	public final static int getDateMonthDay(Date date){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(GregorianCalendar.DAY_OF_MONTH);
	}

    /**
     * 得到定制日期的年份
     * @param date
     * @return
     */
	public final static int getYear(Date date){
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(GregorianCalendar.YEAR);
    }

    /**
     * 得到制定日期在当在一年中的月份数，从1开始
     * @param date
     * @return
     */
	public final static int getMonth(Date date){
	    GregorianCalendar cal = new GregorianCalendar();
	    cal.setTime(date);
	    return cal.get(GregorianCalendar.MONTH) + 1;
	}

	/**
	 * 得到制定日期在当在一月中的号数，从1开始
	 * @param date
	 * @return
	 */
	public final static int getDay(Date date){
	    GregorianCalendar cal = new GregorianCalendar();
	    cal.setTime(date);
	    return cal.get(GregorianCalendar.DAY_OF_MONTH);
	}

	public final static int getSecond(Date date){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(GregorianCalendar.SECOND);
	}

	/**
	 * 得到制定日期在当前星期中的天数，例如2004-5-20日，返回5，
	 * 每周以周日为开始按1计算，所以星期四为5
	 * @param date 指定的日期
	 * @return 返回天数
	 */
	public final static int getDateWeekDay(Date date){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(GregorianCalendar.DAY_OF_WEEK) - 1;
	}

//    public final static int getWeek(String ymd) {
//        java.util.Date date = DateUtils.parseDate(ymd, "yyyyMMdd");
//        GregorianCalendar gCal = new GregorianCalendar();
//        gCal.setTime(date);
//        return ((gCal.get(Calendar.DAY_OF_WEEK) - 1) + 7) % 7;
//    }
//
//    /**
//     * 得到星期数
//     * @param date
//     * @return
//     */
//    public static int getWeek(java.util.Date date) {
//        GregorianCalendar gCal = new GregorianCalendar();
//        gCal.setTime(date);
//        return ((gCal.get(Calendar.DAY_OF_WEEK) - 1) + 7) % 7;
//    }

    private static DateTimeFormatter getDateFormater(){
        return DateTimeFormatter.ofPattern("yyyyMMdd");
    }

    private static DateTimeFormatter getTimeFormater(){
        return DateTimeFormatter.ofPattern("HH:mm");
    }

    private static DateTimeFormatter getDateTimeFormater(){
        return DateTimeFormatter.ofPattern("yyyyMMdd HH:mm");
    }

    public static void main(String[] args){
        LocalDateTime dt = LocalDateTime.now();
        System.out.println(UUIDs.startOf(DateUtils.getTodayStart(dt).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        System.out.println(UUIDs.endOf(DateUtils.getTodayEnd(dt).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
    }
}
