package com.kongcv.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.ToastUtil;

/**
 * 更改手机号
 * @author kcw
 */
public class MineChangePhoneNumber extends Activity implements OnClickListener {
	private EditText et_phonenumber;
	private Button next;
	private String number;
	private ACacheUtils mCache;
	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changephonenumber);
		mCache = ACacheUtils.get(this);
		MyApplication.getInstance().addActivity(this);
		initView();
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		et_phonenumber = (EditText) findViewById(R.id.et_phonenumber);
		next = (Button) findViewById(R.id.next);
		next.setOnClickListener(this);
		et_phonenumber.setHint("已绑定手机号"+mCache.getAsString("USER"));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.next:
			getPhone();
		default:
			break;
		}

	}

	private void getPhone() {
		//获取刚输入的手机号
		number = et_phonenumber.getText().toString();
		if (TextUtils.isEmpty(number)) {
			ToastUtil.show(getApplicationContext(), "请输入需要绑定的手机号");
		} else {
			Intent i = new Intent(this, MineChangePhoneNext.class);
			//将新输入的手机号放入缓存
			mCache.put("number", number);
			startActivity(i);
			finish();
		}
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
