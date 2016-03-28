package com.kongcv.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;

import cn.jpush.android.api.JPushInterface;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.kongcv.R;
import com.kongcv.TTSController;
import com.kongcv.utils.ToastUtil;

/**
 * 导航页面 设置路径 导航以及语音播报
 */
public class NaviStartActivity extends Activity implements 
		AMapLocationListener, OnMapClickListener,OnMapLoadedListener {
	
	private boolean mIsGetGPS = false;// 记录GPS定位是否成功
	// 记录起点、终点位置
	private NaviLatLng mStartPoint = new NaviLatLng();
	private NaviLatLng mEndPoint = new NaviLatLng();
	// 记录起点、终点在地图上添加的Marker
	private Marker mStartMarker;
	private Marker mEndMarker;
	private Marker mGPSMarker;
	// 驾车路径规划起点，途经点，终点的list
	private List<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
	private List<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
	private ProgressDialog mGPSProgressDialog;// GPS过程显示状态
	private ProgressDialog mProgressDialog;// 路径规划过程显示状态
	// 定位
	private LocationManagerProxy mLocationManger;
	private MapView mMapView;// 地图控件

	//地图导航资源
	private AMap mAmap;
	private AMapNavi mAmapNavi;
	private RouteOverLay mRouteOverLay;
	private boolean mIsMapLoaded = false;//地图加载
	
	// 记录地图点击事件相应情况，根据选择不同，地图响应不同
	private int mMapClickMode = MAP_CLICK_NO;
	private static final int MAP_CLICK_NO = 0;// 地图不接受点击事件
	private static final int MAP_CLICK_START = 1;// 地图点击设置起点
	private static final int MAP_CLICK_END = 2;// 地图点击设置终点

	private final static int GPSNO = 0;// 使用我的位置进行计算、GPS定位还未成功状态

	private int mStartPointMethod = BY_MY_POSITION;
	private static final int BY_MY_POSITION = 0;// 以我的位置作为起点
	private static final int BY_MAP_POSITION = 1;// 以地图点选的点为起点
	
	private final static int CALCULATEERROR = 1;// 启动路径计算失败状态
	private final static int CALCULATESUCCESS = 2;// 启动路径计算成功状态
	
	private int mNaviMethod;
	private static final int NAVI_METHOD = 0;// 执行模拟导航操作
	private static final int ROUTE_METHOD = 1;// 执行计算线路操作
	
	// 记录导航种类，用于记录当前选择是驾车还是步行
	private int mTravelMethod = DRIVER_NAVI_METHOD;
	private static final int DRIVER_NAVI_METHOD = 0;// 驾车导航
	private static final int WALK_NAVI_METHOD = 1;// 步行导航
	
	// 导航监听
	private AMapNaviListener mAmapNaviListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.startnavis_activity);
		
		initView(savedInstanceState);
		initLocation();
		initMapAndNavi();
	}
	/**
	 * 初始化map
	 */
	private void initView(Bundle savedInstanceState) {
		mMapView = (MapView) findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		mAmap = mMapView.getMap();
		// 设置地图点击 加载事件侦听
		mAmap.setOnMapClickListener(this);
		mAmap.setOnMapLoadedListener(this);
		
		TTSController ttsManager = TTSController.getInstance(this);// 初始化语音模块
		ttsManager.init();
		AMapNavi.getInstance(this).setAMapNaviListener(ttsManager);// 设置语音模块播报
	}
	/**
	 * 初始化定位功能
	 */
	private void initLocation() {
		// TODO Auto-generated method stub
		mLocationManger = LocationManagerProxy.getInstance(this);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次
		mLocationManger.requestLocationData(LocationProviderProxy.AMapNetwork,
				60 * 1000, 15, this);
		mLocationManger.setGpsEnable(false);
	}
	/**
	 * 初始化导航和定位功能
	 */
	private void initMapAndNavi() {
		// 初始语音播报资源
		setVolumeControlStream(AudioManager.STREAM_MUSIC);// 设置声音控制
		// 语音播报开始

		mAmapNavi = AMapNavi.getInstance(this);// 初始化导航引擎

		// 初始化Marker添加到地图
		mStartMarker = mAmap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(), R.drawable.start))));
		mEndMarker = mAmap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(), R.drawable.end))));
		mGPSMarker = mAmap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.location_marker))));

	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	/**
	 * 当GPS位置有更新时的回调函数
	 */
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
	}

	/**
	 * 定位成功的回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null && location.getAMapException().getErrorCode() == 0) {
			mIsGetGPS = true;
			mStartPoint = new NaviLatLng(location.getLatitude(),
					location.getLongitude());

			mGPSMarker.setPosition(new LatLng(mStartPoint.getLatitude(),
					mStartPoint.getLongitude()));
			mStartPoints.clear();
			mStartPoints.add(mStartPoint);
			dissmissGPSProgressDialog();
			
			double latitude = getIntent().getDoubleExtra("latitude", 0);
			double longitude = getIntent().getDoubleExtra("longitude", 0);
			NaviLatLng naviLatLng = new NaviLatLng(latitude,
					longitude);
			mEndPoints.add(naviLatLng);
			initNavi();
		} else {
			ToastUtil.show(NaviStartActivity.this, "定位出现异常");
		}
	}


	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	/**
	 * 点击map地图图标的操作显示
	 */
	//private int markerCounts=0;
	@Override
	public void onMapClick(LatLng latLng) {
		
		MarkerOptions markerOptions = new MarkerOptions();
	    // 设置Marker的图标样式
	    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.end));
	    // 设置Marker点击之后显示的标题
	    //markerOptions.title("第" + (markerCounts + 1) + "个Marker");
	    // 设置Marker的坐标，为我们点击地图的经纬度坐标
	    markerOptions.position(latLng);
	    // 设置Marker的可见性
	    markerOptions.visible(true);
	    // 设置Marker是否可以被拖拽，这里先设置为false，之后会演示Marker的拖拽功能
	    markerOptions.draggable(false);
	    // 将Marker添加到地图上去
	    mAmap.addMarker(markerOptions);
	    // Marker的计数器自增
	    //markerCounts++;
	  
	//    LatLng position = markerOptions.getPosition();
	}

	/**
	 * 自己写的方法 默认加载
	 */
	private void initNavi(){
		int driveMode = AMapNavi.DrivingDefault;//驾车默认导航
		mAmapNavi.calculateDriveRoute(mStartPoints, mEndPoints,
				null, driveMode);
		mEndPoints.clear();
		mRouteOverLay = new RouteOverLay(mAmap, null);
		AMapNaviPath naviPath = mAmapNavi.getNaviPath();
		
		// 获取路径规划线路，显示到地图上
		mRouteOverLay.setRouteInfo(naviPath);
		mRouteOverLay.addToMap();
		if (mIsMapLoaded) {
			mRouteOverLay.zoomToSpan();
		}
		showGPSProgressDialog();
		
		
		mNaviMethod=NAVI_METHOD;
	}

	
	/**
	 * 导航回调函数
	 */
	private AMapNaviListener getAMapNaviListener() {
		if (mAmapNaviListener == null) {

			mAmapNaviListener = new AMapNaviListener() {
				/**
				 * 当前方路况光柱信息有更新时回调函数
				 */
				@Override
				public void onTrafficStatusUpdate() {
					// TODO Auto-generated method stub

				}

				/**
				 * 启动导航后的回调函数
				 */
				@Override
				public void onStartNavi(int arg0) {
					// TODO Auto-generated method stub

				}

				/**
				 * 步行或驾车导航时，出现偏航后需要重新计算路径的回调函数
				 */
				@Override
				public void onReCalculateRouteForYaw() {
					// TODO Auto-generated method stub

				}

				/**
				 * 驾车导航时，如果前方遇到拥堵时需要重新计算路径的回调
				 */
				@Override
				public void onReCalculateRouteForTrafficJam() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onLocationChange(AMapNaviLocation location) {

				}

				/**
				 * 导航创建成功时的回调函数
				 */
				@Override
				public void onInitNaviSuccess() {
					// TODO Auto-generated method stub

				}

				/**
				 * 导航创建失败时的回调函数
				 */
				@Override
				public void onInitNaviFailure() {
					// TODO Auto-generated method stub

				}

				/**
				 * 导航播报信息的回调函数
				 */
				@Override
				public void onGetNavigationText(int arg0, String arg1) {
					// TODO Auto-generated method stub

				}

				/**
				 * 模拟导航停止后的回调函数
				 */
				@Override
				public void onEndEmulatorNavi() {
					// TODO Auto-generated method stub

				}
				
				/**
				 * 路径规划成功后的回调函数
				 */
				@Override
				public void onCalculateRouteSuccess() {
					dissmissProgressDialog();
					dissmissProgressDialog();
					switch (mNaviMethod) {
					case ROUTE_METHOD://在这里跳到显示路线显示页面
						
						//initNavi();
						
						break;
					case NAVI_METHOD://在这里跳导航页面  ?? 能够进入导航 但是再次进入导航数据不发变化  
						dissmissGPSProgressDialog();
						//handler.postDelayed(this, 15000);
						
						handler.postAtTime(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Intent intent=new Intent(NaviStartActivity.this, NaviCustomActivity.class);
								startActivity(intent);
								finish();
							}
						}, 3000);
						
						break;
					}
				}

				/**
				 * 路径规划失败后的回调函数
				 */
				@Override
				public void onCalculateRouteFailure(int arg0) {
					dissmissProgressDialog();
					ToastUtil.show(NaviStartActivity.this,"路径规划出错");
				}

				/**
				 * 到达某个途经点的回调函数
				 */
				@Override
				public void onArrivedWayPoint(int arg0) {
					// TODO Auto-generated method stub

				}

				/**
				 * 到达目的地后的回调函数
				 */
				@Override
				public void onArriveDestination() {
					// TODO Auto-generated method stub

				}

				/**
				 * 用户手机GPS设置是否开启的回调函数
				 */
				@Override
				public void onGpsOpenStatus(boolean arg0) {
					// TODO Auto-generated method stub

				}

				/**
				 * 下面两个方法废弃
				 */
				@Override
				public void onNaviInfoUpdated(AMapNaviInfo arg0) {
				}

				@Override
				public void onNaviInfoUpdate(NaviInfo arg0) {
				}
			};
		}
		return mAmapNaviListener;
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	/**
	 * 返回键处理事件 返回回到AMap页面
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent = new Intent(NaviStartActivity.this,
					AMapActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	// ------------------------------------------------
	@Override
	public void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
		mMapView.onResume();
		initNavi();
		// 以上两句必须重写
		// 以下两句逻辑是为了保证进入首页开启定位和加入导航回调
		AMapNavi.getInstance(this).setAMapNaviListener(getAMapNaviListener());
		
		TTSController.getInstance(this).startSpeaking();
	}
	@Override
	public void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
		mMapView.onPause();
		// 以上两句必须重写
		// 下边逻辑是移除监听
		AMapNavi.getInstance(this)
				.removeAMapNaviListener(getAMapNaviListener());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		
		// 这是最后退出页，所以销毁导航和播报资源
		AMapNavi.getInstance(this).destroy();// 销毁导航
		TTSController.getInstance(this).stopSpeaking();
		TTSController.getInstance(this).destroy();

	}
	
	/**
	 * 显示GPS进度框
	 */
	private void showGPSProgressDialog() {
		if (mGPSProgressDialog == null)
			mGPSProgressDialog = new ProgressDialog(this);
		mGPSProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mGPSProgressDialog.setIndeterminate(false);
		mGPSProgressDialog.setCancelable(true);
		mGPSProgressDialog.setMessage("定位中...");
		mGPSProgressDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissGPSProgressDialog() {
		if (mGPSProgressDialog != null) {
			mGPSProgressDialog.dismiss();
		}
	}
	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (mProgressDialog == null)
			mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setMessage("线路规划中");
		mProgressDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	
	/**
	 * 导航模块
	 */
	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		mIsMapLoaded = true;
		if (mRouteOverLay != null) {
			mRouteOverLay.zoomToSpan();
		}
	}
	
}



















