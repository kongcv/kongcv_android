package com.kongcv.calendar;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.SlidingDrawer.OnDrawerScrollListener;
import android.widget.TextView;
import android.widget.Toast;

import com.kongcv.R;
import com.kongcv.adapter.CustomViewPagerAdapter;
import com.kongcv.calendar.CalendarView.CallBack;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.Data;
import com.kongcv.utils.DateUtils;
import com.kongcv.utils.ToastUtil;

public class PickDialog extends Dialog implements OnClickListener,CallBack{
	
	private Context context;
	private PickDialogListener pickDialogListener;
	private LinearLayout dialog_data;
	
	private CalendarViewBuilder builder = new CalendarViewBuilder();//创建view的辅助类
	
	private TextView monthCalendarView;
	private TextView weekCalendarView;
	
	private TextView mSubscibeCircleView;
	private TextView mNowCircleView;
	private TextView mAddCircleView;
	private LinearLayout layout;
	//年月周
	private TextView showYearView;
	private TextView showMonthView;
	private TextView showWeekView;
	private ViewPager viewPager;
	private CalendarView[] views;
	private LayoutInflater inflater;
	
	private View mContentPager;
	private SlidingDrawer mSlidingDrawer;
	private CustomDate mClickDate;
	public interface MyDateListener {
		//回传activity参数
		public void refreshPriorityUI(String date);
	}
	
	public PickDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	public PickDialog(Context context,PickDialogListener pickDialogListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.pickDialogListener=pickDialogListener;
	}
	public PickDialog(Context context, int theme, PickDialogListener pickDialogListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.pickDialogListener=pickDialogListener;
	}
	
	//如何调用接口里面的
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mCache = ACacheUtils.get(context);
		
		inflater = LayoutInflater.from(context);
		layout = (LinearLayout) inflater.inflate(
				R.layout.dialog_data, null);
		
		this.setCanceledOnTouchOutside(true);
		this.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		init();
		
		this.setContentView(layout);
	}
	
	private void init(){
		viewPager = (ViewPager) layout.findViewById(R.id.viewpager);
		showMonthView = (TextView)layout.findViewById(R.id.show_month_view);
		showYearView = (TextView)layout.findViewById(R.id.show_year_view);
		showWeekView = (TextView)layout.findViewById(R.id.show_week_view);
		views = builder.createMassCalendarViews(getContext(), 5, this);
		monthCalendarView = (TextView) layout.findViewById(R.id.month_calendar_button);
		weekCalendarView = (TextView) layout.findViewById(R.id.week_calendar_button);
		mContentPager = layout.findViewById(R.id.contentPager);
		mSlidingDrawer = (SlidingDrawer)layout.findViewById(R.id.sildingDrawer);
		monthCalendarView.setOnClickListener(this);
		weekCalendarView.setOnClickListener(this);
		
		mSubscibeCircleView = (TextView)layout.findViewById(R.id.subscibe_circle_view);
		mNowCircleView = (TextView)layout.findViewById(R.id.now_circle_view);//点击今天回到今天
		mAddCircleView = (TextView)layout.findViewById(R.id.add_circle_view);
		
		mSubscibeCircleView.setOnClickListener(this);
		mNowCircleView.setOnClickListener(this);
		mAddCircleView.setOnClickListener(this);
		setViewPager();
		setOnDrawListener();
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.month_calendar_button://月份
			swtichBackgroundForButton(true);
			builder.swtichCalendarViewsStyle(CalendarView.MONTH_STYLE);
 			mSlidingDrawer.close();
			break;
		case R.id.week_calendar_button://周
			swtichBackgroundForButton(false);
			mSlidingDrawer.open();
			break;
		case R.id.now_circle_view://点击今天会到今天
			builder.backTodayCalendarViews();
			break;
		case R.id.add_circle_view://加号  点击进入编辑 
			
			break;
		case R.id.subscibe_circle_view://点击出租的话 在发布页面显示信息
			
			pickDialogListener.refreshPriorityUI((String) Data.getData("clickDate"));
			dismiss();
 		}
	}
	
	private void getDay(Boolean flag){
		swtichBackgroundForButton(false);
		mSlidingDrawer.open();
	}
	
	private void swtichBackgroundForButton(boolean isMonth){
 		if(isMonth){
 			monthCalendarView.setBackgroundResource(R.drawable.press_left_text_bg);
 			weekCalendarView.setBackgroundColor(Color.TRANSPARENT);
 		}else{
 			weekCalendarView.setBackgroundResource(R.drawable.press_right_text_bg);
 			monthCalendarView.setBackgroundColor(Color.TRANSPARENT);
 		}
 	}

	private void setViewPager() {
		CustomViewPagerAdapter<CalendarView> viewPagerAdapter = new CustomViewPagerAdapter<CalendarView>(views);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setCurrentItem(498); 
		viewPager.setOnPageChangeListener(new CalendarViewPagerLisenter(viewPagerAdapter));
	}
	
	@SuppressWarnings("deprecation")
	private void setOnDrawListener() {
		mSlidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {
				builder.swtichCalendarViewsStyle(CalendarView.WEEK_STYLE);
			}
		});
		
		mSlidingDrawer.setOnDrawerScrollListener(new OnDrawerScrollListener() {
			@Override
			public void onScrollStarted() {
				builder.swtichCalendarViewsStyle(CalendarView.MONTH_STYLE);
			}
			@Override
			public void onScrollEnded() {
			}
		});
	}

	

	//------------------------------------------------callback
	private int i=4;
	private ACacheUtils mCache;
	@Override
	public void clickDate(CustomDate date) {
		// TODO Auto-generated method stub
		mClickDate = date;
		i--;
		if(i==0){
			//点击事件和当前时间的比较
			int day = mClickDate.getDay();
			int month = mClickDate.getMonth()-1;
			int year = mClickDate.getYear();
			
			Calendar date2 = DateUtils.getDate(day, month, year);
			Date time = date2.getTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String clickDate = DateUtils.getNowData(time, "yyyy-MM-dd");
			Data.putData("clickDate", clickDate);
			
			Date data=new Date();
			String nowData = DateUtils.getNowData(data,"yyyy-MM-dd");
			//比较
			String days = DateUtils.getDays(clickDate, nowData,false);
			int num = Integer.parseInt(days);
		//	ToastUtil.show(context, "点击的时间是"+clickDate);
			
			/*	int day = mClickDate.getDay();
				int month = mClickDate.getMonth();
				int year = mClickDate.getYear();
				Calendar date2 = DateUtils.getDate(day, month, year);
				Date time = date2.getTime();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String nowData = DateUtils.getNowData(time, "yyyy-MM-dd");*/
//			if(num>=0){
				//可以获取天数显示：
				//Toast.makeText(getContext(), "点击的两个日期之间的间隔是"+num, 0).show();
		//		ToastUtil.show(context, "今天的时间是"+nowData);
//			}else{
				
//			}
			i=1;
		}
	}

	@Override
	public void onMesureCellHeight(int cellSpace) {
		// TODO Auto-generated method stub
		mSlidingDrawer.getLayoutParams().height = mContentPager.getHeight() - cellSpace;
	}

	@Override
	public void changeDate(CustomDate date) {
		// TODO Auto-generated method stub
		setShowDateViewText(date.year,date.month);
	}
	
	private void setShowDateViewText(int year, int month) {
		// TODO Auto-generated method stub
		 showYearView.setText(year+"");
		 showMonthView.setText(month+"月");
		 showWeekView.setText(DateUtils.weekName[DateUtils.getWeekDay()-1]);
	}

	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
	}
	
	
	/**
	 * 获取到日历天数  点击功能的实现
	 */
	
	@Override
	public void onClickOnDate() {
	}
	

}
