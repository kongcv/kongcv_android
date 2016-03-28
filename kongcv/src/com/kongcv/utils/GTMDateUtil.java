package com.kongcv.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;

public class GTMDateUtil {
	/**
	 * GTM转本地时间
	 * @param GTMDate
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String GTMToLocal(String GTMDate,boolean flag) {
		int tIndex = GTMDate.indexOf("T");
		String dateTemp = GTMDate.substring(0, tIndex);
		String timeTemp = GTMDate.substring(tIndex + 1, GTMDate.length() - 6);
		String convertString = dateTemp + " " + timeTemp;

		SimpleDateFormat format;
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		Date result_date;
		long result_time = 0;

		if (null == GTMDate) {
			return GTMDate;
		} else {
			try {
				format.setTimeZone(TimeZone.getTimeZone("GMT00:00"));
				result_date = format.parse(convertString);
				result_time = result_date.getTime();
				format.setTimeZone(TimeZone.getDefault());

				SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
				String parse = format2.format(format2.parse(format
						.format(result_time)));
			//	Log.v("转换时间2为", parse);//yyyy-MM-dd
				
				Date d = format2.parse(parse);
				SimpleDateFormat sf1 = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
				parse = sf1.format(d);
		    // 	Log.v("转化结果是：：：", parse);
				if(flag){
					return format.format(result_time);//  yyyy-MM-dd HH:mm:ss格式的
				}else {
					return parse;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return GTMDate;
	}

	/***
	 * 转成格林威治时间 感觉用不到
	 */
	public static String LocalToGTM(String LocalDate) {
		SimpleDateFormat format;
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		Date result_date;
		long result_time = 0;
		if (null == LocalDate) {
			return LocalDate;
		} else {
			try {
				format.setTimeZone(TimeZone.getDefault());
				result_date = format.parse(LocalDate);
				result_time = result_date.getTime();
				format.setTimeZone(TimeZone.getTimeZone("GMT00:00"));
				return format.format(result_time);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return LocalDate;
	}

}
