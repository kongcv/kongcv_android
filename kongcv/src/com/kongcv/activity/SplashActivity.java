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
import com.kongcv.UI.AsyncImageLoader.PreReadTask;
import com.kongcv.activity.LogoActivity.ModeAndObjId;
import com.kongcv.activity.LogoActivity.ReadBtnTask;
import com.kongcv.global.CheckUpdate;
import com.kongcv.global.Information;
import com.kongcv.global.UpdateService;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.Data;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.NormalPostRequest;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 闪屏页面
 */
public class SplashActivity extends Activity {

	private RelativeLayout rlRoot;
	private ACacheUtils mCache;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		mCache=ACacheUtils.get(getApplicationContext());
		MyApplication.getInstance().addActivity(this);
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		// 渐变动画
		AlphaAnimation anim = new AlphaAnimation(0.3f, 1);
		anim.setDuration(Information.time);// 动画执行时间
		rlRoot.startAnimation(anim);// 开启动画
		
		getGridImageView();
	}
	/**
	 * 加载数据
	 */
	private void getGridImageView(){
		if (mCache.getAsString("ReadImgTask") == null&& mCache.getAsString("ReadBtnTask") == null) {
			ReadBtnTask task2 = new ReadBtnTask();
			task2.execute();
		}else{
			readServiceCode();
		}
	}
	class ReadBtnTask extends PreReadTask<String, Void, Void> {
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
				e.printStackTrace();
			}
			Data.putData("objectIddoReadBtn", list);
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
	private Builder builder;
	private void BuilderReadInfo(final CheckUpdate fromJson){
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo=packageManager.getPackageInfo(SplashActivity.this.getPackageName(), 0);
			String appVersion = packageInfo.versionName; // 版本名
			currentVersionCode = packageInfo.versionCode; // 版本号
			if(fromJson.getResult().get(0).getVersion_Num()>currentVersionCode){
				if(fromJson.getResult().get(0).getMust()==0){
					builder = new AlertDialog.Builder(SplashActivity.this,AlertDialog.THEME_HOLO_DARK);
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
							mHandler.sendEmptyMessageDelayed(0,1500);
						}
					});
					 builder.setCancelable(false);
					 builder.show();
				}else{
					//强制刷新 直接下载
					Intent intent = new Intent(SplashActivity.this, UpdateService.class);
					intent.putExtra("UpdateApk", fromJson.getResult().get(0).getApk().getUrl());
					startService(intent);
				}
			}else if(fromJson.getResult().get(0).getVersion_Num()==currentVersionCode){
				mHandler.sendEmptyMessageDelayed(0, 1500);
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
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
		MobclickAgent.onPause(this);
	}
}



	
