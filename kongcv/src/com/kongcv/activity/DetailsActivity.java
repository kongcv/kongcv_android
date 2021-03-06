package com.kongcv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.fragment.DetailsFragment;
import com.kongcv.global.SearchBean;
import com.kongcv.global.ZyCommityAdapterBean;
import com.umeng.analytics.MobclickAgent;
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
		int trade_state = intent.getIntExtra("trade_state", -1);
		String field = intent.getStringExtra("getField");
		String CurbMineReceiver = intent.getStringExtra("CurbMineReceiver");
		ZyCommityAdapterBean mCommBean=(ZyCommityAdapterBean) intent.getSerializableExtra("mCommBean");
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
			if(mCommBean!=null){
				args.putSerializable("mCommBean", mCommBean);
			}
			if(mode!=null)
			args.putString("mode", mode);
			if(park_id!=null)
			args.putString("park_id",park_id);
			if(price!=0)
			args.putDouble("price", price);
			if(field!=null)
			args.putString("field", field);
			if(trade_state!=-1)
			args.putInt("trade_state", trade_state);	
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
		MobclickAgent.onResume(this);
	}
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	/*
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent=new Intent(this,AMapActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("bean", getBean());
		bundle.putString("objectId", getCarObjectId());// 车位id
		bundle.putString("mode", getMode());
		bundle.putString("getField", getField());
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	private SearchBean getBean(){
		SearchBean searchBean = (SearchBean) getIntent().getSerializableExtra("bean");
		return searchBean;
	}
	private String getMode() {
		Bundle bundle = getIntent().getExtras();
		String object = (String) bundle.get("mode");
		return object;
	}
	private String getField() {
		Bundle bundle = getIntent().getExtras();
		String field = (String) bundle.get("getField");
		return field;
	}
	private String getCarObjectId() {
		Bundle bundle = getIntent().getExtras();
		String object = (String) bundle.get("objectId");
		return object;
	}*/
}

















