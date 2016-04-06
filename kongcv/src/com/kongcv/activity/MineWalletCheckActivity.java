package com.kongcv.activity;

import java.util.Calendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.fragment.CheckCommityFragment;
import com.kongcv.fragment.CheckCurbFragment;
import com.kongcv.utils.DateUtils;
import com.kongcv.view.MyDatePickerDialog;
import com.umeng.analytics.MobclickAgent;
/*
 * 账单页面
 */
public class MineWalletCheckActivity extends FragmentActivity implements
		OnClickListener {
	private ImageView iv_back;
	private ImageView iv_youjia;
	private ImageView iv_xiala;
	private TextView rent;// 租用
	private TextView hire;// 出租
	private RadioGroup mRadioGroup;
	private RadioButton mRadioButton1, mRadioButton2;
	private FragmentManager fragmentManager = getSupportFragmentManager();
	private FragmentTransaction mFragmentTransaction;
	private CheckCommityFragment mCommityFragment;
	private CheckCurbFragment mCurbFragment;
	private static int CHECKTYPE = 0;// 0代表租用账单，1代表出租订单
	private Bundle bundle = new Bundle();
	private TextView tvYear,tvMonth;
	private DatePicker dp ;
	private String time;
	private Calendar cal ; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_check);
		MyApplication.getInstance().addActivity(this);
		initView();
		//TODO一点开就请求租用下的个人的数据
		initData1();
	    

	}
	private void initView() {
		cal = Calendar.getInstance(); 
		tvYear = (TextView) findViewById(R.id.tv_year);
		tvMonth = (TextView) findViewById(R.id.tv_month);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		iv_xiala = (ImageView) findViewById(R.id.iv_xiala);
		iv_youjia = (ImageView) findViewById(R.id.iv_youjia);
		rent = (TextView) findViewById(R.id.rent);
		hire = (TextView) findViewById(R.id.hire);
		rent.setOnClickListener(this);
		hire.setOnClickListener(this);
		mRadioGroup = (RadioGroup) findViewById(R.id.radio);
		mRadioButton1 = (RadioButton) findViewById(R.id.btn1);
		mRadioButton2 = (RadioButton) findViewById(R.id.btn2);
		mRadioButton1.setOnClickListener(this);
		mRadioButton2.setOnClickListener(this);
		tvYear.setText(cal.get(Calendar.YEAR)+"");
		tvMonth.setText(cal.get(Calendar.MONTH)+1+"");
	    time=tvYear.getText().toString()+"-"+tvMonth.getText().toString()+"-"+"1"+" "+"00:00:00";
		tvYear.setOnClickListener(this);
		tvMonth.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_year:
			dateMyPicker();
			break;
		case R.id.tv_month:
			dateMyPicker();
			break;	
		case R.id.iv_back:
			finish();
			break;
		case R.id.hire:// 点击出租时图标发生变化
			iv_xiala.setImageResource(R.drawable.icon_youjiantou);
			iv_youjia.setImageResource(R.drawable.icon_xiajiantou);
			mRadioButton1.performClick();
			CHECKTYPE = 1;
			initData1();
			//mRadioButton1.performClick();
			break;
		case R.id.rent:// 点击租用时图标发生变化
			iv_youjia.setImageResource(R.drawable.icon_youjiantou);
			iv_xiala.setImageResource(R.drawable.icon_xiajiantou);
			mRadioButton1.performClick();
		    CHECKTYPE = 0;
			initData1();
			break;
		case R.id.btn1:// 社区按钮(个人)
			mRadioButton2.setBackgroundColor(Color.TRANSPARENT);
			mRadioButton1.setBackgroundResource(R.drawable.bg_shequ);
			initData1();
			break;
		case R.id.btn2:// 道路按钮(商业)
			mRadioButton1.setBackgroundColor(Color.TRANSPARENT);
			mRadioButton2.setBackgroundResource(R.drawable.bg_daolu);
			initData2();
			break;
		default:
			break;
		}
	}
	/**
	 * 自定义datePicker
	 */
	private void dateMyPicker(){
		
		MyDatePickerDialog myDialog=new MyDatePickerDialog(this, R.style.MyDialog,new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				tvYear.setText(year+"");
				tvMonth.setText(monthOfYear+1+"");
				String years=tvYear.getText().toString();
				String month=tvMonth.getText().toString(); 
			    time=years+"-"+month+"-"+"1"+" "+"00:00:00";
			}
		},
		cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		myDialog.show();
		dp = DateUtils.findDatePicker((ViewGroup) myDialog.getWindow().getDecorView());  
		if(dp!=null){
			 ((ViewGroup)((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE); 
		}
		
	}
	/**
	 * 请求数据
	 */
	private void initData1() {
		mFragmentTransaction = fragmentManager.beginTransaction();
		mCommityFragment = new CheckCommityFragment();
		bundle.putInt("type", CHECKTYPE);
		bundle.putString("time", time);
		mCommityFragment.setArguments(bundle);
		mFragmentTransaction.replace(R.id.content, mCommityFragment);
		mFragmentTransaction.commit();

	}
	/**
	 * 请求数据
	 */
	private void initData2() {
		mFragmentTransaction = fragmentManager.beginTransaction();
		mCurbFragment = new CheckCurbFragment();
		bundle.putInt("type", CHECKTYPE);
		bundle.putString("time", time);
		mCurbFragment.setArguments(bundle);
		mFragmentTransaction.replace(R.id.content, mCurbFragment);
		mFragmentTransaction.commit();

	}
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
