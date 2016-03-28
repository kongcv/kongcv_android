package com.kongcv.calendar;

import android.content.Context;

import com.kongcv.calendar.CalendarView.CallBack;


/**
 * calendview辅助类 在这里 完成回调今天等功能
 * @author kcw001
 *
 */
public class CalendarViewBuilder {

	private CalendarView[] calendarViews;
	
	/**
	 * create 多个calendarviews
	 * @param context
	 * @param count
	 * @param style
	 * @param callBack
	 * @return
	 */
	public  CalendarView[] createMassCalendarViews(Context context,int count,int style,CallBack callBack){
		calendarViews = new CalendarView[count];
		for(int i = 0; i < count;i++){
			calendarViews[i] = new CalendarView(context, style,callBack);
		}
		return calendarViews;
	}
	
	public  CalendarView[] createMassCalendarViews(Context context,int count,CallBack callBack){
		
		return createMassCalendarViews(context, count, CalendarView.MONTH_STYLE,callBack);
	}
	
	/**
	 * 切换CandlendarView的样式
	 * @param style
	 */
	public void swtichCalendarViewsStyle(int style){
		if(calendarViews != null)
		for(int i = 0 ;i < calendarViews.length;i++){
			calendarViews[i].switchStyle(style);
		}
	}
	
	/**
	 * CandlendarView回到当前日期
	 */
	
	public void backTodayCalendarViews(){
		if(calendarViews != null)
		for(int i = 0 ;i < calendarViews.length;i++){
			calendarViews[i].backToday();
		}
	}
}
