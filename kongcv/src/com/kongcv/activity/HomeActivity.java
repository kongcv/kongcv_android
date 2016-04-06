package com.kongcv.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.model.LatLng;
import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.fragment.CarwFragment;
import com.kongcv.fragment.MineFragment;
import com.kongcv.fragment.PublishFragment;
import com.kongcv.global.FragOperManager;
import com.kongcv.global.Information;
import com.kongcv.global.MineCarmanagerBean;
import com.kongcv.jPush.HttpHelper;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.Data;
import com.kongcv.utils.ExampleUtil;
import com.kongcv.utils.Logger;

/**
 * 空车位页面对所有fragment进行管理
 */
public class HomeActivity extends FragmentActivity implements
		RadioGroup.OnCheckedChangeListener, AMapLocationListener {

	public static boolean isForeground = false;// 极光推送的标记
	/**
	 * 车位管理
	 */
	public static int CWGL = 1;
	// 定位
	private LocationManagerProxy mLocationManger;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initLocation();
		registerMessageReceiver();
		newThreadToReset();
		init();
	}

	private void initLocation() {
		mLocationManger = LocationManagerProxy
				.getInstance(getApplicationContext());
		mLocationManger.requestLocationData(LocationProviderProxy.AMapNetwork,
				60 * 1000, 15, this);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				stopLocation();
			}
		}, 15000);// 15秒 未定位成功
	}

	private Handler mHandler = new Handler() {
	};


	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	private void getDisplay() {
		// 方法1 Android获得屏幕的宽和高
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = screenWidth = display.getWidth();
		int screenHeight = screenHeight = display.getHeight();
		// 方法2
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		TextView tv = (TextView) this.findViewById(R.id.tv);
		float width = dm.widthPixels * dm.density;
		float height = dm.heightPixels * dm.density;
		Log.e("获取到设备的分辨率是：：：", dm.toString());
		Log.e("获取到设备的分辨率是：：：", "Y=" + screenWidth + ";X=" + screenHeight);
		System.out
				.println(("First method:" + dm.toString() + "\n"
						+ "Second method:" + "Y=" + screenWidth + ";X=" + screenHeight));
		System.out
				.println(("First method:" + dm.toString() + "\n"
						+ "Second method:" + "Y=" + screenWidth + ";X=" + screenHeight));
		System.out
				.println(("First method:" + dm.toString() + "\n"
						+ "Second method:" + "Y=" + screenWidth + ";X=" + screenHeight));
		System.out
				.println(("First method:" + dm.toString() + "\n"
						+ "Second method:" + "Y=" + screenWidth + ";X=" + screenHeight));
	}

//	private int i = 0;
	private MineCarmanagerBean bean;

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == HomeActivity.CWGL) {
			if (data != null) {
				HomeActivity.CWGL = 2;
				i = 1;
				bean = (MineCarmanagerBean) data.getBundleExtra("bundle").get(
						"MineCarmanagerBean");
				mPublishFragment = new PublishFragment();
				Bundle args = new Bundle();
				args.putSerializable("MineCarmanagerBean", bean);
				mPublishFragment.setArguments(args);
				rb1.setChecked(true);
			}
		}
	}*/

	// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
	private void init() {
		JPushInterface.init(getApplicationContext());
		mCache = ACacheUtils.get(this);
		MyApplication.getInstance().addActivity(this);

		mCarwFragment = new CarwFragment();
		mPublishFragment = new PublishFragment();
		mMineFragment = new MineFragment();

		manager = new FragOperManager(this, R.id.content);
		rdoBtn = (RadioGroup) findViewById(R.id.main_rg);
		rb1 = (RadioButton) findViewById(R.id.rb1);
		rb0 = (RadioButton) findViewById(R.id.rb0);
		rdoBtn.setOnCheckedChangeListener(this);
		
		Bundle bundle = getIntent().getBundleExtra("bundle");
		if (bundle == null) {
			manager.chAddFrag(mCarwFragment, "mCarwFragment", false);
		}else{
		//	HomeActivity.CWGL = 2;
		//	i = 1;
			bean = (MineCarmanagerBean) bundle.get(
					"MineCarmanagerBean");
			mPublishFragment = new PublishFragment();
			Bundle args = new Bundle();
			args.putSerializable("MineCarmanagerBean", bean);
			mPublishFragment.setArguments(args);
			rb1.setChecked(true);
		}
		getDisplay();
	}

	private void newThreadToReset() {
		new Thread() {
			public void run() {
				resetAliasAndTags();
			}
		}.start();
	}

	private void resetAliasAndTags() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("udid", Information.udid);
		String userInfo = null;
		try {
			userInfo = HttpHelper.post(Information.URL, params);
			Logger.e("userInfo获取到的值是 ", userInfo);
			String phoneId = (String) Data.getData("Config.udid");
			JPushInterface.setAliasAndTags(HomeActivity.this, phoneId, null);
			Log.e("手机：getRegistrationID",
					JPushInterface.getRegistrationID(getApplicationContext()));
			Log.e("手机：getRegistrationID",
					JPushInterface.getRegistrationID(getApplicationContext()));
			Log.e("手机：getRegistrationID",
					JPushInterface.getRegistrationID(getApplicationContext()));
		} catch (Exception e) {
			Logger.e("HomeActivity jpush resetAliasAndTags",
					"Call pushtalk api to get user info error", e);
			return;
		}
	}

	@Override
	protected void onResume() {
		initLocation();// 初始化定位
		isForeground = true;
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		isForeground = false;
		JPushInterface.onPause(this);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}

	// for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.kongcv.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	private ACacheUtils mCache;

	/**
	 * 注册广播服务
	 */
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	/**
	 * 接收广播显示在标题栏
	 */
	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!ExampleUtil.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
			}
		}
	}

	/**
	 * long类型时间格式
	 */
	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}

	@Override
	public void onBackPressed() {
		//if (i == 0) {
			Builder builder = new Builder(HomeActivity.this,
					AlertDialog.THEME_HOLO_DARK);
			builder.setMessage("确定要退出当前应用吗？")
					.setPositiveButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							})
					.setNegativeButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
									//MyApplication.getInstance().exit();
								}
							}).show();
		/*} else {
			Intent i = new Intent(this, MineCarmanagerActivity.class);
			startActivity(i);
			finish();
		}*/
	}

	/**
	 * 切换框架
	 */
	private FragOperManager manager;
	private RadioGroup rdoBtn;
	private CarwFragment mCarwFragment;
	private PublishFragment mPublishFragment;
	private MineFragment mMineFragment;
	private RadioButton rb1, rb0;

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb0:
			manager.chHideFrag(mMineFragment);
			manager.chHideFrag(mPublishFragment);
			manager.chAddFrag(mCarwFragment, "mCarwFragment", false);
			break;
		case R.id.rb1:
			manager.chHideFrag(mCarwFragment);
			manager.chHideFrag(mMineFragment);
			manager.chAddFrag(mPublishFragment, "mPublishFragment", false);
			break;
		case R.id.rb2:
			manager.chHideFrag(mCarwFragment);
			manager.chHideFrag(mPublishFragment);
			manager.chAddFrag(mMineFragment, "mMineFragment", false);
			break;
		default:
			break;
		}
	}

	/**
	 * 自动定位
	 */
	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null) {
			Double Latitude = amapLocation.getLatitude();
			Double Longitude = amapLocation.getLongitude();
			Bundle bundle = amapLocation.getExtras();
			String desc = "";
			if (null != bundle) {
				desc = bundle.getString("desc");
			}
			String address = amapLocation.getCity()
					+ amapLocation.getDistrict();
			String wk = amapLocation.getCity();
			wk = wk.substring(0, wk.length() - 1);
			Data.putData("address", address);// 城市
			Data.putData("wk", wk);// 地址
			LatLng latLng = new LatLng(Latitude, Longitude);
			Data.putData("LatLng", latLng);
		}
	}

	/**
	 * 销毁定位
	 */
	private void stopLocation() {
		if (mLocationManger != null) {
			mLocationManger.removeUpdates(this);
			mLocationManger.destory();
		}
		mLocationManger = null;
	}
}
