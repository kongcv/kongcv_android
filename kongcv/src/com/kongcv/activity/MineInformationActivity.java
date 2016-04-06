package com.kongcv.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.adapter.TabAdapter;
import com.kongcv.fragment.MineReceiveFragment;
import com.kongcv.fragment.MineSendFragment;
import com.kongcv.utils.ACacheUtils;
import com.umeng.analytics.MobclickAgent;
/*
 * 消息通知页面
 */
public class MineInformationActivity extends FragmentActivity {
	
	private RadioGroup mRadioGroup;
	private RadioButton mRadioButton1, mRadioButton2;
	private ImageView iv_line;// 指导线
	private int screenWidth;// 屏幕的宽度
	private ViewPager pager;
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	private TabAdapter mAdapter;
	private ACacheUtils mCache;
	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_infonotiy);
		mCache = ACacheUtils.get(this);
		MyApplication.getInstance().addActivity(this);
		initView();
		initData();
	}
	private void initView() {
		mRadioGroup = (RadioGroup) findViewById(R.id.radio);
		mRadioButton1 = (RadioButton) findViewById(R.id.btn1);
		mRadioButton2 = (RadioButton) findViewById(R.id.btn2);
		iv_line = (ImageView) findViewById(R.id.iv_line);
		// 获取屏幕的宽度
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(outMetrics);
		screenWidth = outMetrics.widthPixels;

		// 设置iv_line宽度
		// 获取控件的(注意：一定要用父控件的LayoutParams写LinearLayout.LayoutParams)
		LinearLayout.LayoutParams lp = (LayoutParams) iv_line.getLayoutParams();// 获取控件的布局参数对象
		lp.width = screenWidth / 4;
		iv_line.setLayoutParams(lp); // 设置该控件的layoutParams参数
		iv_line.setX(lp.width / 2);
		mFragments.add(new MineReceiveFragment());
		mFragments.add(new MineSendFragment());

		pager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new TabAdapter(getSupportFragmentManager(), mFragments);
		pager.setAdapter(mAdapter);
		iv_back=(ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initData() {
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				/**
				 * 接受页
				 */
				case R.id.btn1:
					pager.setCurrentItem(0);// 选择某一页
					break;
				/**
				 * 发送页
				 */
				case R.id.btn2:
					pager.setCurrentItem(1);
					break;
				default:
					break;

				}
			}
		});
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					mRadioButton1.setChecked(true);
					break;
				case 1:
					mRadioButton2.setChecked(true);
					break;

				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				LinearLayout.LayoutParams lp = (LayoutParams) iv_line
						.getLayoutParams();
				// 获取组件距离左侧组件的距离
				lp.leftMargin = (int) ((positionOffset + position)
						* screenWidth / 2);
				iv_line.setLayoutParams(lp);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
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
