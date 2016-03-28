package com.kongcv.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.kongcv.calendar.CustomDate;

public class DateUtils {

	public static String[] weekName = { "周日", "周一", "周二", "周三", "周四", "周五",
			"周六" };

	public static int getMonthDays(int year, int month) {
		if (month > 12) {
			month = 1;
			year += 1;
		} else if (month < 1) {
			month = 12;
			year -= 1;
		}
		int[] arr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int days = 0;

		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			arr[1] = 29; // 闰年2月29天
		}

		try {
			days = arr[month - 1];
		} catch (Exception e) {
			e.getStackTrace();
		}

		return days;
	}

	public static int getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static int getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	public static int getCurrentMonthDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	public static int getWeekDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}

	public static int getHour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}

	public static CustomDate getNextSunday() {

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 7 - getWeekDay() + 1);
		CustomDate date = new CustomDate(c.get(Calendar.YEAR),
				c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
		return date;
	}

	public static int[] getWeekSunday(int year, int month, int day, int pervious) {
		int[] time = new int[3];
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.add(Calendar.DAY_OF_MONTH, pervious);
		time[0] = c.get(Calendar.YEAR);
		time[1] = c.get(Calendar.MONTH) + 1;
		time[2] = c.get(Calendar.DAY_OF_MONTH);
		return time;

	}

	public static int getWeekDayFromDate(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateFromString(year, month));
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week_index < 0) {
			week_index = 0;
		}
		return week_index;
	}

	@SuppressLint("SimpleDateFormat")
	public static Date getDateFromString(int year, int month) {
		String dateString = year + "-" + (month > 9 ? month : ("0" + month))
				+ "-01";
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return date;
	}

	public static boolean isToday(CustomDate date) {
		return (date.year == DateUtils.getYear()
				&& date.month == DateUtils.getMonth() && date.day == DateUtils
					.getCurrentMonthDay());
	}

	public static boolean isCurrentMonth(CustomDate date) {
		return (date.year == DateUtils.getYear() && date.month == DateUtils
				.getMonth());
	}

	/**
	 * 返回当天日期 String 格式： "yyyy-MM-dd" "yyyy-MM-dd HH:mm:ss:SS"等
	 */
	public static String getNowData(Date data, String dateformat) {
		SimpleDateFormat format = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
		String now = format.format(data);
		return now;
	}

	/**
	 * 传入字符串变成
	 * @param point1
	 *            起始日期
	 * @param point2
	 *            终止日期
	 * @return
	 */
	public static String getDays(String point1, String point2, boolean flag) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		try {
			java.util.Date date = myFormatter.parse(point1);
			java.util.Date mydate = myFormatter.parse(point2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
			if (flag)
				day = Math.abs(day);
		} catch (Exception e) {
			return "";
		}
		return day + "";
	}

	public static Calendar getDate(int day, int month, int year) {
		Calendar calDate = Calendar.getInstance();
		calDate.clear();
		calDate.set(Calendar.YEAR, year);
		calDate.set(Calendar.MONTH, month);
		calDate.set(Calendar.DAY_OF_MONTH, day);
		return calDate;
	}

	/**
	 * 传入Date对象计算相隔天数
	 */
	public static int getDays(Calendar fromCalendar, Calendar toCalendar) {
		return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime()
				.getTime()) / (1000 * 60 * 60 * 24));
	}

	public Calendar getDate2(Date date, int day, int month, int year) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);
		calDate.set(Calendar.YEAR, year);
		calDate.set(Calendar.MONTH, month);
		calDate.set(Calendar.DAY_OF_MONTH, day);
		return calDate;
	}

	/**
	 * datepickerdialog 遍历隐藏 天数 如果想隐藏年，把getChildAt(2)改为getChildAt(0)...
	 * http://blog.csdn.net/lzt623459815/article/details/8479991
	 */
	public static DatePicker findDatePicker(ViewGroup group) {
		if (group != null) {
			for (int i = 0, j = group.getChildCount(); i < j; i++) {
				View child = group.getChildAt(i);
				if (child instanceof DatePicker) {
					return (DatePicker) child;
				} else if (child instanceof ViewGroup) {
					DatePicker result = findDatePicker((ViewGroup) child);
					if (result != null)
						return result;
				}
			}
		}
		return null;
	}

	/**
	 * 判断指定日期是周几
	 */
	public static String getWeekday(String date) {// 必须yyyy-MM-dd
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdw = new SimpleDateFormat("E");
		Date d = null;
		try {
			d = sd.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdw.format(d);
	}
	
	/**
	 * 遍历两个起始时间的每一个日期
	 */
	public static List<Date> dateSplit(Date startDate, Date endDate)
	        throws Exception {
	    if (!startDate.before(endDate))
	        throw new Exception("开始时间应该在结束时间之后");
	    Long spi = endDate.getTime() - startDate.getTime();
	    Long step = spi / (24 * 60 * 60 * 1000);// 相隔天数

	    List<Date> dateList = new ArrayList<Date>();
	    dateList.add(endDate);
	    for (int i = 1; i <= step; i++) {
	        dateList.add(new Date(dateList.get(i - 1).getTime()
	                - (24 * 60 * 60 * 1000)));// 比上一天减一
	    }
	    return dateList;
	}
}
