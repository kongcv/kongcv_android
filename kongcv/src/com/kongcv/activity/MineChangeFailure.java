package com.kongcv.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
/**
 * 更改手机号失败的页面
 * @author kcw
 */
public class MineChangeFailure extends Activity implements OnClickListener{
	private Button bt_newbind;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changephonefailure);
		MyApplication.getInstance().addActivity(this);
		initeView();
	}
	private void initeView() {
		bt_newbind=(Button) findViewById(R.id.bt_newbind);
		bt_newbind.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		Intent i=new Intent(this,MineChangePhoneNumber.class);
		startActivity(i);
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
