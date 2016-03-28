package com.kongcv.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.fragment.DetailsFragment;
import com.kongcv.global.JpushBean;
import com.kongcv.utils.AndroidUtil;
import com.kongcv.utils.Data;

/**
 * 测试textactivity
 * @author kcw001
 * 需求：点击进入到详情页
 */
public class TestActivity extends FragmentActivity {

	private FragmentManager fm = getSupportFragmentManager();
	private FragmentTransaction fragmentTransaction = getSupportFragmentManager()
			.beginTransaction();
	private String mode;
	private String park_id;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.details_activity);
		MyApplication.getInstance().addActivity(this);
		init();
	}
	private void init(){
		try {
			Intent intent=getIntent();
			String own_device_token = intent.getStringExtra("own_device_token");
			if(own_device_token!=null){
				Log.d("own_device_token用户点击打开了通知", own_device_token);
		    	Log.d("own_device_token用户点击打开了通知", own_device_token);
		    	/**
		    	 * 这一块需要处理：
		    	 */
				Data.putData("own_device_token", own_device_token);
				JSONObject object=new JSONObject(own_device_token);
				Gson gson=new Gson();
				JpushBean fromJson = gson.fromJson(own_device_token, JpushBean.class);
				String hire_method_field = fromJson.getHire_method_field();
				mode=fromJson.getMode();
				park_id = fromJson.getPark_id();
				/*mode=object.getString("mode");
				park_id=object.getString("park_id");*/
				
				if(AndroidUtil.isAppAlive(getApplicationContext(), "com.kongcv")){
					DetailsFragment fragment=new DetailsFragment();
					Bundle args=new Bundle();
					args.putString("mode", mode);
					args.putString("park_id",park_id);
					args.putString("field",hire_method_field);
					
					fragment.setArguments(args);
					fragmentTransaction.replace(R.id.fragmentmain, fragment);
					fragmentTransaction.commit();
				}else{
					Intent launchIntent =getApplicationContext().getPackageManager().
							getLaunchIntentForPackage("com.kongcv");
					launchIntent.setFlags(
		                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					getApplicationContext().startActivity(launchIntent);
				}
			}else {
				String stringExtra = intent.getStringExtra("MineSendFragment");
				Log.v("testActivity", stringExtra);
				Gson gson=new Gson();
				JpushBean fromJson = gson.fromJson(stringExtra, JpushBean.class);
				mode=fromJson.getMode();
				Log.v("testActivity mode", mode);
				park_id=fromJson.getPark_id();
				Log.v("testActivity park_id", park_id);
				DetailsFragment fragment=new DetailsFragment();
				Bundle args=new Bundle();
				args.putString("mode", mode);
				args.putString("park_id",park_id);
				args.putString("field", fromJson.getHire_method_field());
				args.putString("MineSendFragment", stringExtra);
				fragment.setArguments(args);
				fragmentTransaction.replace(R.id.fragmentmain, fragment);
				fragmentTransaction.commit();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
