package com.kongcv.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.ImageRun.GetImage;
import com.kongcv.global.Information;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
/**
 * 修改手机号成功的页面
 * @author kcw
 */
public class MineChangeSuccess extends Activity {
	private TextView tv_phonenumber;
	private ACacheUtils mCache;
	private Button bt_success;
	private String newPhone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changephonesuccess);
	    MineSheZhiActivity.TAG="second";
	    mCache=ACacheUtils.get(this);
	    MyApplication.getInstance().addActivity(this);
		initView();
	}

	private void initView() {
		tv_phonenumber = (TextView) findViewById(R.id.tv_phonenumber);
		bt_success = (Button) findViewById(R.id.bt_success);
		newPhone = mCache.getAsString("number");
		tv_phonenumber.setText(newPhone);
		bt_success.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MineChangeSuccess.this,MineSheZhiActivity.class);
				mCache.remove("USER");
				mCache.put("USER", newPhone);
				startActivity(i);
				finish();

			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}
}
