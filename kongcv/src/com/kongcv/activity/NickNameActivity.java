package com.kongcv.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.global.Information;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.AndroidUtil;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.ToastUtil;

public class NickNameActivity extends Activity implements OnClickListener{

	private TextView nickSave;
	private ACacheUtils mCache;
	private EditText tvNickName;
	private String newNickName;
	private LinearLayout nick_lin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nickname);
		MyApplication.getInstance().addActivity(this);
		mCache = ACacheUtils.get(getApplicationContext());
		initView();
	}
	/**
	 * 初始化view
	 */
	private void initView() {
		tvNickName = (EditText) findViewById(R.id.et_nickname);
		ImageView back = (ImageView) findViewById(R.id.nickname_back);
		back.setOnClickListener(this);
		nickSave = (TextView) findViewById(R.id.tv_nickname_save);
		nickSave.setOnClickListener(this);
		nick_lin = (LinearLayout) findViewById(R.id.nick_lin);
		nick_lin.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), 0);
			}
		});
	}
	private void hintKeyBoard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive() && getCurrentFocus() != null) {
			if (getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	private Handler mHandler=new Handler(){};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nickname_back:
			this.finish();
			break;
		case R.id.tv_nickname_save:
			Log.e("User", mCache.getAsString("USER"));
			if (mCache.getAsString("USER") != null&& mCache.getAsString("sessionToken") != null) {
				upload();
				hintKeyBoard();
			}else{
				ToastUtil.show(getApplicationContext(), "请先登录！");
				mHandler.postAtTime(new Runnable() {
					@Override
					public void run() {
						Intent intent=new Intent(NickNameActivity.this,LogInActivity.class);
						startActivity(intent);
					}
				}, 2000);
				
			}
			break;

		default:
			break;
		}

	}
	
	private void upload() {
		newNickName=tvNickName.getText().toString().trim();
	    mCache.put("user_name", newNickName);
		new Thread(new Runnable() {
			@Override
			public void run() {
					try {
						JSONObject obj=new JSONObject();
						obj.put("user_name", newNickName);
						obj.put("version",AndroidUtil.getVersion(getApplicationContext()));
						String str = PostCLientUtils.doHttpsPost2(Information.KONGCV_PUT_USERINFO, 
								JsonStrUtils.JsonStr(obj), mCache.getAsString("sessionToken"));
						Log.v("发布登录成功", str);
						//{"result":"{\"state\":\"ok\", \"code\":1, \"msg\":\"成功\"}"}
						JSONObject obj2=new JSONObject(str);
						String result = obj2.getString("result");
						JSONObject objStr=new JSONObject(result);
						String state = objStr.getString("state");
						if("ok".equals(state)){
							// 保存成功，1、提交到服务器↑↑↑↑↑↑； 2、返回，并更新
							 Intent data=new Intent();
							 data.putExtra("user_name",newNickName);
							 setResult(0, data);
							 finish();
						}else {
							Looper.prepare();
							ToastUtil.show(getBaseContext(), "修改昵称失败！请重新操作");
							Looper.loop();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}).start();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);
	}
	@Override
	public void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}
}
