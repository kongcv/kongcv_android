package com.kongcv.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

import cn.jpush.android.api.JPushInterface;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviInfo;
import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.TTSController;
import com.kongcv.utils.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * 处理语音导航播报模块
 */
public class NaviCustomActivity extends Activity implements
		AMapNaviViewListener {
	// 导航监听
	private AMapNaviListener mAmapNaviListener;
	private AMapNaviView mAmapAMapNaviView;
	// 导航界面风格
	private int mThemeStle;
	// 导航可以设置的参数
	private boolean mDayNightFlag = Utils.DAY_MODE;// 默认为白天模式
	private boolean mDeviationFlag = Utils.YES_MODE;// 默认进行偏航重算
	private boolean mJamFlag = Utils.YES_MODE;// 默认进行拥堵重算
	private boolean mTrafficFlag = Utils.OPEN_MODE;// 默认进行交通播报
	private boolean mCameraFlag = Utils.OPEN_MODE;// 默认进行摄像头播报
	private boolean mScreenFlag = Utils.YES_MODE;// 默认是屏幕常亮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navis_activity);

		// 语音播报开始
		TTSController.getInstance(this).startSpeaking();
		// 实时导航方式进行导航
		AMapNavi.getInstance(this).startNavi(AMapNavi.GPSNaviMode);
		initView(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
	}

	private void initView(Bundle savedInstanceState) {
		mAmapAMapNaviView = (AMapNaviView) findViewById(R.id.customnavimap);
		mAmapAMapNaviView.onCreate(savedInstanceState);
		mAmapAMapNaviView.setAMapNaviViewListener(this);
		setAmapNaviViewOptions();
	}

	/**
	 * 设置导航参数播报
	 */

	private void setAmapNaviViewOptions() {
		// TODO Auto-generated method stub
		if (mAmapAMapNaviView == null) {
			return;
		}
		AMapNaviViewOptions viewOptions = new AMapNaviViewOptions();
		viewOptions.setSettingMenuEnabled(true);// 设置导航setting可用
		viewOptions.setNaviNight(mDayNightFlag);// 设置导航是否为黑夜模式
		viewOptions.setReCalculateRouteForYaw(mDeviationFlag);// 设置导偏航是否重算
		viewOptions.setReCalculateRouteForTrafficJam(mJamFlag);// 设置交通拥挤是否重算
		viewOptions.setTrafficInfoUpdateEnabled(mTrafficFlag);// 设置是否更新路况
		viewOptions.setCameraInfoUpdateEnabled(mCameraFlag);// 设置摄像头播报
		viewOptions.setScreenAlwaysBright(mScreenFlag);// 设置屏幕常亮情况
		viewOptions.setNaviViewTopic(mThemeStle);// 设置导航界面主题样式

		mAmapAMapNaviView.setViewOptions(viewOptions);
	}

	private AMapNaviListener getAMapNaviListener() {
		if (mAmapNaviListener == null) {
			mAmapNaviListener = new AMapNaviListener() {

				public void onTrafficStatusUpdate() {
					// TODO Auto-generated method stub

				}

				public void onStartNavi(int arg0) {
					// TODO Auto-generated method stub

				}

				public void onReCalculateRouteForYaw() {
				}

				public void onReCalculateRouteForTrafficJam() {

				}

				public void onLocationChange(AMapNaviLocation location) {

				}

				public void onInitNaviSuccess() {
					// TODO Auto-generated method stub

				}

				public void onInitNaviFailure() {
					// TODO Auto-generated method stub

				}

				public void onGetNavigationText(int arg0, String arg1) {
					// TODO Auto-generated method stub

				}

				public void onEndEmulatorNavi() {
					// TODO Auto-generated method stub

				}

				public void onCalculateRouteSuccess() {
				}

				
				
				public void onCalculateRouteFailure(int arg0) {
				}

				public void onArrivedWayPoint(int arg0) {
					// TODO Auto-generated method stub
				}

				public void onArriveDestination() {
					// TODO Auto-generated method stub
				}

				public void onGpsOpenStatus(boolean arg0) {
					// TODO Auto-generated method stub
				}

				public void onNaviInfoUpdated(AMapNaviInfo arg0) {
					// TODO Auto-generated method stub
				}

				public void onNaviInfoUpdate(NaviInfo arg0) {
					// TODO Auto-generated method stub
				}
			};
		}
		return mAmapNaviListener;
	}

	/**
	 * TTS语音播报导航
	 */
	@Override
	public void onLockMap(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onNaviBackClick() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onNaviCancel() {
		// 导航取消操作
		finish();
	}

	@Override
	public void onNaviMapMode(int arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onNaviSetting() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onNaviTurnClick() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onNextRoadClick() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScanViewButtonClick() {
		// TODO Auto-generated method stub
	}

	// ------------------------------生命周期方法---------------------------
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mAmapAMapNaviView.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setAmapNaviViewOptions();
		AMapNavi.getInstance(this).setAMapNaviListener(getAMapNaviListener());
		mAmapAMapNaviView.onResume();
		JPushInterface.onResume(this);
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		JPushInterface.onPause(this);
		mAmapAMapNaviView.onPause();
		super.onPause();
		AMapNavi.getInstance(this)
				.removeAMapNaviListener(getAMapNaviListener());
		MobclickAgent.onPause(this);
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		mAmapAMapNaviView.onDestroy();
	 	//页面结束时，停止语音播报
		TTSController.getInstance(this).stopSpeaking();
	}
}