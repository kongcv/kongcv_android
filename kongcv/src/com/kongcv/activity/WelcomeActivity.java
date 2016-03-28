package com.kongcv.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.adapter.BasePagerAdapter;

public class WelcomeActivity extends Activity implements OnPageChangeListener
		,OnClickListener{

	private int[] images;
	private Context context;
	private ViewPager viewPager;
	private Button startButton;
	private ArrayList<View> views;
	private ImageView[] indicators = null;
	private LinearLayout indicatorLayout;
	private PagerAdapter pagerAdapter;
	private Handler mHandler=new Handler(){};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		MyApplication.getInstance().addActivity(this);
		context=this;
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		indicatorLayout = (LinearLayout) findViewById(R.id.indicator);
		images = new int[] { R.drawable.welcome_01, R.drawable.welcome_02,
				R.drawable.welcome_03,R.drawable.welcome_04 };
		startButton = (Button) findViewById(R.id.start_Button);
		startButton.setOnClickListener(this);
		// 实例化视图控件
		viewPager = (ViewPager) findViewById(R.id.viewpage);
		indicators=new ImageView[images.length];
		views=new ArrayList<View>();
		for (int i = 0; i < indicators.length; i++) {
			// 循环加入图片
			ImageView imageView = new ImageView(context);
			imageView.setBackgroundResource(images[i]);
			views.add(imageView);
			//加入小圆点
			indicators[i] = new ImageView(context);
			indicators[i].setBackgroundResource(R.drawable.indicators_default);
			if (i == 0) {
				indicators[i].setBackgroundResource(R.drawable.indicators_now);
			}
			indicatorLayout.addView(indicators[i]);
		}
		pagerAdapter = new BasePagerAdapter(views);
		viewPager.setAdapter(pagerAdapter); // 设置适配器
		viewPager.setOnPageChangeListener(this);
	}
	
	
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		// 显示最后一个图片时显示按钮
				if (arg0 == indicators.length - 1) {
					startButton.setVisibility(View.VISIBLE);
				} else {
					startButton.setVisibility(View.INVISIBLE);
				}
				// 更改指示器图片
				for (int i = 0; i < indicators.length; i++) {
					indicators[arg0].setBackgroundResource(R.drawable.indicators_now);
					if (arg0 != i) {
						indicators[i]
								.setBackgroundResource(R.drawable.indicators_default);
					}
				}
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	public void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.start_Button) {
			startActivity(new Intent(WelcomeActivity.this, SplashActivity.class));
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			this.finish();
		}
	}
	
	
	
	
	
	
	
	
	
	
}
