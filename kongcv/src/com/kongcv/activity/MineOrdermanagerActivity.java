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
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.adapter.OrederTabAdapter;
import com.kongcv.fragment.CommityFragment;
import com.kongcv.fragment.CurbFragment;

/**
 * 订单管理页面
 * 
 * @author kcw
 * 
 */
public class MineOrdermanagerActivity extends FragmentActivity implements
		OnClickListener {
	private RadioGroup mRadioGroup;
	private RadioButton mRadioButton1, mRadioButton2;
	private RadioButton btn_one, btn_two;
	private RadioGroup rad;
	private ImageView iv_line;// 指导线
	private int screenWidth;// 屏幕的宽度
	private ViewPager pager;
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	private OrederTabAdapter mAdapter;
	private ImageView iv_back;
	private CommityFragment comf;
	private CurbFragment cubf;
	private int currentItem;
	/**
	 * 0代表是租用订单，1代表是出租订单
	 */
	public int TYPEORDER = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_ordermanager);
		MyApplication.getInstance().addActivity(this);
		initView();
		initData();
	}

	private void initView() {
		mRadioGroup = (RadioGroup) findViewById(R.id.radio);
		mRadioButton1 = (RadioButton) findViewById(R.id.btn1);
		mRadioButton2 = (RadioButton) findViewById(R.id.btn2);
		rad = (RadioGroup) findViewById(R.id.rad);
		btn_one = (RadioButton) findViewById(R.id.btn_one);
		btn_two = (RadioButton) findViewById(R.id.btn_two);
		iv_line = (ImageView) findViewById(R.id.iv_line);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		// 获取屏幕的宽度
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(outMetrics);
		screenWidth = outMetrics.widthPixels;
		// 设置iv_line宽度
		// 获取控件的(注意：一定要用父控件的LayoutParams写LinearLayout.LayoutParams)
		LinearLayout.LayoutParams lp = (LayoutParams) iv_line.getLayoutParams();// 获取控件的布局参数对象
		lp.width = screenWidth / 2;
		iv_line.setLayoutParams(lp); // 设置该控件的layoutParams参数
		comf = new CommityFragment();
		cubf = new CurbFragment();
		mFragments.add(cubf);
		mFragments.add(comf);

		pager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new OrederTabAdapter(getSupportFragmentManager(),
				mFragments, currentItem);
		pager.setAdapter(mAdapter);
		mRadioButton1.setOnClickListener(this);
		mRadioButton2.setOnClickListener(this);
		btn_one.setOnClickListener(this);
		btn_two.setOnClickListener(this);

	}

	private void initData() {
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					btn_two.setChecked(true);// 道路
					cubf.getData1();
					break;
				case 1:
					btn_one.setChecked(true);
					comf.getData1();
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.btn1:
			TYPEORDER = 1;
			comf.getData1();
			pager.setCurrentItem(0);// 出租订单
			break;
		case R.id.btn2:
			TYPEORDER = 0;
			comf.getData1();
			pager.setCurrentItem(0);// 选择某一页,租用订单
			break;
		case R.id.btn_one:// 社区按钮
			comf.getData1();
			pager.setCurrentItem(0);
			break;
		case R.id.btn_two:// 道路按钮
			cubf.getData1();
			pager.setCurrentItem(1);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}
	
}
