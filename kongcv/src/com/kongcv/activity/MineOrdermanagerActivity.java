package com.kongcv.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.umeng.analytics.MobclickAgent;

/**
 * 订单管理页面
 * @author kcw
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
	/**
	 * 0代表是租用订单，1代表是出租订单
	 */
	public static int TYPEORDER;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_ordermanager);
		initView();
		initData();
	}
	private void initView() {
		MyApplication.getInstance().addActivity(this);
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
		LinearLayout.LayoutParams lp = (LayoutParams) iv_line.getLayoutParams();// 获取控件的布局参数对象
		lp.width = screenWidth / 2;
		iv_line.setLayoutParams(lp); // 设置该控件的layoutParams参数
		comf = new CommityFragment();
		cubf = new CurbFragment();
		mFragments.add(cubf);
		mFragments.add(comf);
		pager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new OrederTabAdapter(getSupportFragmentManager(),
				mFragments);
		pager.setAdapter(mAdapter);
		mRadioButton1.setOnClickListener(this);
		mRadioButton2.setOnClickListener(this);
		btn_one.setOnClickListener(this);
		btn_two.setOnClickListener(this);
		TYPEORDER=0;
	}
	private void initData() {
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					cubf.refresh();
					btn_one.setChecked(true);
					break;
				case 1:
					comf.refresh();
					btn_two.setChecked(true);// 个人
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
			TYPEORDER = 0;
			cubf.refresh();
			pager.setCurrentItem(0);
			loading();
			break;
		case R.id.btn2: 
			TYPEORDER = 1;
			cubf.refresh();
			pager.setCurrentItem(0);
			loading();
			break;
		case R.id.btn_one:// 商业
			cubf.refresh();
			pager.setCurrentItem(0);
			loading();
			break;
		case R.id.btn_two:// 个人
			comf.refresh();
			pager.setCurrentItem(1);
			loading();
			break;
		default:
			break;
		}
	}
	private ProgressDialog mypDialog;
	private void loading() {
		mypDialog = new ProgressDialog(this, R.style.MyDialogStyle);
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mypDialog.setMessage("loading...");
		mypDialog.setIndeterminate(false);
		mypDialog.setCancelable(true);
		mypDialog.show();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				mypDialog.dismiss();
			}
		}, 1000);
	}
	private Handler mHandler=new Handler(){};
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
