package com.kongcv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.fragment.DetailsFragment;
/**
 * 详情activity 用于切换fragment 画面
 */
public class DetailsActivity extends FragmentActivity{
	
	private FragmentManager fm = getSupportFragmentManager();
	private FragmentTransaction fragmentTransaction = getSupportFragmentManager()
			.beginTransaction();
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.details_activity);
		MyApplication.getInstance().addActivity(this);
		getModeandParkId();
	}
	
	private void getModeandParkId(){
		Intent intent = getIntent();
		String mode = intent.getStringExtra("mode");
		String park_id = intent.getStringExtra("park_id");
		double price=intent.getDoubleExtra("price", 0);
		
		String field = intent.getStringExtra("getField");
		String CurbMineReceiver = intent.getStringExtra("CurbMineReceiver");
		if(mode!=null && park_id!=null){
			DetailsFragment fragment=new DetailsFragment();
			Bundle args=new Bundle();
			String stringExtra = intent.getStringExtra("MineSendFragment");
			if(stringExtra!=null){
				args.putString("stringExtra", stringExtra);
			}
			if(CurbMineReceiver!=null){
				args.putString("CurbMineReceiver", CurbMineReceiver);
			}
			args.putString("mode", mode);
			args.putString("park_id",park_id);
			args.putDouble("price", price);
			args.putString("field", field);
			fragment.setArguments(args);
			fragmentTransaction.replace(R.id.fragmentmain, fragment);
			fragmentTransaction.commit();
		}else {
			fragmentTransaction.replace(R.id.fragmentmain, new DetailsFragment());
			fragmentTransaction.commit();
		}
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

















