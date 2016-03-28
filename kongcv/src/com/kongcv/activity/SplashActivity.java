package com.kongcv.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import cn.jpush.android.api.JPushInterface;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.global.CheckUpdate;
import com.kongcv.global.Information;
import com.kongcv.global.UpdateService;
import com.kongcv.utils.NormalPostRequest;
import com.kongcv.utils.ToastUtil;

/**
 * 闪屏页面
 */
public class SplashActivity extends Activity {

	private RelativeLayout rlRoot;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		MyApplication.getInstance().addActivity(this);
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		// 渐变动画
		AlphaAnimation anim = new AlphaAnimation(0.3f, 1);
		anim.setDuration(Information.time);// 动画执行时间
		rlRoot.startAnimation(anim);// 开启动画
		readServiceCode();
	}
	/**
	 * 检查服务器和本地版本
	 */
	CheckUpdate fromJson;
	private void readServiceCode(){
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
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				enterHome();
				break;
			case 1:
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
			PackageInfo packageInfo=packageManager.getPackageInfo(SplashActivity.this.getPackageName(), 0);
			String appVersion = packageInfo.versionName; // 版本名
			currentVersionCode = packageInfo.versionCode; // 版本号
			if(fromJson.getResult().get(0).getVersion_Num()>currentVersionCode){
				if(fromJson.getResult().get(0).getMust()==0){
					Builder builder = new AlertDialog.Builder(SplashActivity.this,AlertDialog.THEME_HOLO_DARK);
					builder.setTitle(fromJson.getResult().get(0).getApk().getName());
					builder.setMessage("发现新版本:"+fromJson.getResult().get(0).getVersion()+",当前版本："+appVersion
							+"\n"+"是否更新?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ToastUtil.show(getBaseContext(), "确定更新！");
							Intent intent = new Intent(SplashActivity.this, UpdateService.class);
							intent.putExtra("UpdateApk", fromJson.getResult().get(0).getApk().getUrl());
							intent.putExtra("apkName", fromJson.getResult().get(0).getApk().getName());
							intent.putExtra("activityCode", 0);
							startService(intent);
							mHandler.sendEmptyMessage(0);
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ToastUtil.show(getBaseContext(), "取消更新！");
							dialog.dismiss();
							mHandler.sendEmptyMessage(0);
						}
					}).create().show();
				}else{
					//强制刷新 直接下载
					Intent intent = new Intent(SplashActivity.this, UpdateService.class);
					intent.putExtra("UpdateApk", fromJson.getResult().get(0).getApk().getUrl());
					startService(intent);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 跳到主页面
	 */
	protected void enterHome() {
		
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}
}



	
