package com.kongcv.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import cn.jpush.android.api.JPushInterface;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.model.LatLng;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.UI.AsyncImageLoader.PreReadTask;
import com.kongcv.global.CheckUpdate;
import com.kongcv.global.Information;
import com.kongcv.global.UpdateService;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.Data;
import com.kongcv.utils.HttpUtils;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.NormalPostRequest;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.ToastUtil;

public class LogoActivity extends Activity implements AMapLocationListener{
	private ACacheUtils mCache;
	private SharedPreferences preferences;
	private Animation animation;
	private static int TIME = 5000; 
	private View view;
	// 定位
	private LocationManagerProxy mLocationManger;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.activity_logo, null);
		setContentView(view);
		isNetworkConnected();
	}
	private void isNetworkConnected() {
		MyApplication.getInstance().addActivity(this);
		mCache = ACacheUtils.get(getApplicationContext());
		if(HttpUtils.isNetworkConnected(getApplicationContext())) {
			initLocation();//初始化定位
			getData();// 预加载
			preferences = getSharedPreferences("count", MODE_WORLD_READABLE);
			int count = preferences.getInt("count", 0);
			if (0 == count) {
				//版本更新提示
				readServiceCode();
				Editor editor = preferences.edit();
				editor.putInt("count", ++count);
				editor.commit();
			} else {
				handler.sendEmptyMessage(0);
			}
			into();
		}else {
			 //提示对话框
	        AlertDialog.Builder builder=new Builder(this);
	        builder.setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?").setPositiveButton("设置", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                Intent intent=null;
	                //判断手机系统的版本  即API大于10 就是3.0或以上版本 
	                if(android.os.Build.VERSION.SDK_INT>10){
	                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
	                }else{
	                    intent = new Intent();
	                    ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
	                    intent.setComponent(component);
	                    intent.setAction("android.intent.action.VIEW");
	                }
	               startActivity(intent);
	            }
	        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                finish();
	            }
	        }).show();
	    }
	}
	CheckUpdate fromJson;
	private void readServiceCode() {
		// TODO Auto-generated method stub
			Map<String, String> params = new HashMap<String, String>();  
			params.put("app_type", "user");  
			RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
			Request<JSONObject> request = new NormalPostRequest(Information.KONGCV_GET_ANDROID_VERSION,
			    new Response.Listener<JSONObject>() {
			        @Override
			        public void onResponse(JSONObject response) {
			            Gson gson=new Gson();
			            fromJson = gson.fromJson(response.toString(), CheckUpdate.class);
			            mHandler.sendEmptyMessageDelayed(1, 100);
			        }
			    }, new Response.ErrorListener() {
			        @Override
			        public void onErrorResponse(VolleyError error) {
			            Log.e("TAG", error.getMessage(), error);
			            
			        }
			    }, params);
			requestQueue.add(request);
	}
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				BuilderReadInfo(fromJson);
				break;

			default:
				break;
			}
		}
	};
	
	/**
	 * 提示更新操作
	 */
	private int currentVersionCode;
	private void BuilderReadInfo(final CheckUpdate fromJson){
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo=packageManager.getPackageInfo(LogoActivity.this.getPackageName(), 0);
			String appVersion = packageInfo.versionName; // 版本名
			currentVersionCode = packageInfo.versionCode; // 版本号
			System.out.println(currentVersionCode + " " + appVersion);
			System.out.println(currentVersionCode + " " + appVersion);
			if(fromJson.getResult().get(0).getVersion_Num()>currentVersionCode){
				if(fromJson.getResult().get(0).getMust()==0){
					Builder builder = new AlertDialog.Builder(LogoActivity.this,AlertDialog.THEME_HOLO_DARK);
					builder.setTitle(fromJson.getResult().get(0).getApk().getName());
					builder.setMessage("发现新版本:"+fromJson.getResult().get(0).getVersion()+",当前版本："+appVersion
							+"\n"+"是否更新?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ToastUtil.show(getBaseContext(), "确定更新！");
							Intent intent = new Intent(LogoActivity.this, UpdateService.class);
							intent.putExtra("UpdateApk", fromJson.getResult().get(0).getApk().getUrl());
							intent.putExtra("apkName", fromJson.getResult().get(0).getApk().getName());
							intent.putExtra("activityCode", 0);
							startService(intent);
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ToastUtil.show(getBaseContext(), "取消更新！");
						}
					}).show();
				}else{
					//强制刷新 直接下载
					Intent intent = new Intent(LogoActivity.this, UpdateService.class);
					intent.putExtra("UpdateApk", fromJson.getResult().get(0).getApk().getUrl());
					startService(intent);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 加载获取id和url
	 */
	private void getData() {
		if (mCache.getAsString("ReadImgTask") == null
				&& mCache.getAsString("ReadBtnTask") == null) {
			ReadImgTask task = new ReadImgTask();
			task.execute();
			ReadBtnTask task2 = new ReadBtnTask();
			task2.execute();
		} else {
			List<String> doReadImg = doReadImg(mCache
					.getAsString("ReadImgTask"));
			Data.putData("url", doReadImg);//传递跑马灯图片url
			List<ModeAndObjId> doReadBtn = doReadBtn(mCache
					.getAsString("ReadBtnTask"));
			Data.putData("objectId", doReadBtn);
		}
	}
	private List<String> doReadImg(String str) {
		try {
			List<String> list = new ArrayList<String>();
			JSONObject demoJson = new JSONObject(str);
			JSONArray numberList = demoJson.getJSONArray("result");
			for (int i = 0; i < numberList.length(); i++) {
				//之前类型的图片
			    JSONObject object = numberList.getJSONObject(i).getJSONObject(
						"picture");
				String url = object.getString("url");
				list.add(url);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 预加道路和社区
	 */
	class ReadBtnTask extends PreReadTask<String, Void, Void> {
	//	private List<String> list;
		private List<ModeAndObjId> list;
		@Override
		protected Void doInBackground(String... arg0) {
			try {
				JSONObject obj = new JSONObject();
				obj.put("{}", null);
				String jsonStr = PostCLientUtils.doHttpsPost(
						Information.KONGCV_GET_PARK_TYPE,
						JsonStrUtils.JsonStr(obj));
				// 缓存
				mCache.put("ReadBtnTask", jsonStr,ACacheUtils.TIME_DAY);
				list = doReadBtn(jsonStr);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Data.putData("objectId", list);
			return null;
		}
	}
	private List<ModeAndObjId> doReadBtn(String jsonStr) {	
		ModeAndObjId objId=null;
		List<ModeAndObjId> list=null;
		try {
			list=new ArrayList<ModeAndObjId>();
			JSONObject demoJson = new JSONObject(jsonStr);
			JSONArray numberList = demoJson.getJSONArray("result");
			for (int i = 0; i < numberList.length(); i++) {
				String name = numberList.getJSONObject(i).getString("name");
				JSONObject object = numberList.getJSONObject(i).getJSONObject(
						"picture_small");
				String __type = object.getString("__type");
				String id = object.getString("id");
				String url = object.getString("url");
				// 使用另一种方式传递数据
				String objectId = numberList.getJSONObject(i).getString(
						"objectId");
				objId=new ModeAndObjId(name, objectId,url);
				list.add(objId);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public class ModeAndObjId implements Serializable{
		public String mode;
		public String url;
		public String objList;
		public ModeAndObjId(String mode,String objList,String url){
			this.mode=mode;
			this.objList=objList;
			this.url=url;
		}
	}
	
	
	/**
	 * 预加载跑马灯图片
	 */
	class ReadImgTask extends PreReadTask<String, Void, Void> {

		private List<String> list;
		@Override
		protected Void doInBackground(String... arg0) {

			try {
				JSONObject obj = new JSONObject();
				obj.put("{}", null);
				String jsonString = PostCLientUtils.doHttpsPost(
						Information.KONGCV_GET_ADVERTISE,
						JsonStrUtils.JsonStr(obj));
				mCache.put("ReadImgTask", jsonString,ACacheUtils.TIME_DAY);
				Log.e("得到轮播图", jsonString);
				list = doReadImg(jsonString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(list!=null){
				Data.putData("url", list);
			}
			return null;
		}
	}
	private void initLocation() {
		// TODO Auto-generated method stub
		mLocationManger = LocationManagerProxy
				.getInstance(getApplicationContext());
		mLocationManger.requestLocationData(LocationProviderProxy.AMapNetwork,
				60 * 1000, 15, this);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				stopLocation();
			}
		}, 15000);//15秒 未定位成功
	}
	private void enterHome() {
		// TODO Auto-generated method stub
		handler.postAtTime(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent=new Intent(LogoActivity.this,SplashActivity.class);
				startActivity(intent);
				finish();
			}
		}, 3000);
	}
	private Handler handler=new Handler(){
		@Override
	    public void handleMessage(Message msg) {
	        switch (msg.what) {
	        case 0:
	        	enterHome();
	            break;
	        }
	    }
	};
	
	private void into() {
			// 设置动画效果是alpha，在anim目录下的alpha.xml文件中定义动画效果
			animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
			// 给view设置动画效果
			view.startAnimation(animation);
			animation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation arg0) {
				}
				@Override
				public void onAnimationRepeat(Animation arg0) {
				}
				@Override
				public void onAnimationEnd(Animation arg0) {
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							Intent intent = new Intent(LogoActivity.this,WelcomeActivity.class);
							startActivity(intent);
							// 设置Activity的切换效果
							overridePendingTransition(R.anim.in_from_right,
									R.anim.out_to_left);
							LogoActivity.this.finish();
						}
					}, TIME);
				}
			});
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
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
	private LatLng latLng;
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
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
			String wk=amapLocation.getCity();
			wk=wk.substring(0,wk.length()-1);
			Data.putData("address", address);//城市
			Data.putData("wk", wk);//地址
			latLng = new LatLng(Latitude, Longitude);
	//		Data.putData("LatLng", latLng);
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
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
	public void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}
}


























