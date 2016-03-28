package com.kongcv.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.global.Information;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.AndroidUtil;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.StringUtils;
import com.kongcv.utils.ToastUtil;
/**
 * 更改手机号的下一步页面
 * @author kcw
 */
public class MineChangePhoneNext extends Activity implements OnClickListener {
	private Button bt_next;
	private String newNumber, newCode;
	private EditText et_newcode;
	private ACacheUtils mCache;
	private ImageView iv_back;
	

	private Handler hadler = new Handler() {
		public void handleMessage(Message msg) {
			JSONObject obs = (JSONObject) msg.obj;
			try {
				if (obs != null) {
					String str = obs.getString("result");
					if (str != null) {
						JSONObject objStr = new JSONObject(str);
						String state = objStr.getString("state");
						if (state.equals("ok")) {
							Intent i = new Intent(MineChangePhoneNext.this,
									MineChangeSuccess.class);
							startActivity(i);
							finish();
						} else {
							Intent is = new Intent(MineChangePhoneNext.this,
									MineChangeFailure.class);
							startActivity(is);
							finish();
						}
					}else{
						Intent is = new Intent(MineChangePhoneNext.this,
								MineChangeFailure.class);
						startActivity(is);
						finish();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changephonenext);
		mCache = ACacheUtils.get(this);
		MyApplication.getInstance().addActivity(this);
		initView();

	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		bt_next = (Button) findViewById(R.id.bt_next);
		bt_next.setOnClickListener(this);
		et_newcode = (EditText) findViewById(R.id.et_newcode);
		iv_back.setOnClickListener(this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject obj = new JSONObject();
			    //获取需要绑定的新手机号
				newNumber = mCache.getAsString("number");
				try {
					if (StringUtils.isMobileNo(newNumber)) {
						obj.put("mobilePhoneNumber", newNumber);
						obj.put("version",  AndroidUtil.getVersion(getApplicationContext()));
						Log.i("newNumberPPPPPPP", newNumber);
						String doHttpsPost = PostCLientUtils.doHttpsPost2(
								Information.KONGCV_PUT_USERINFO,
								JsonStrUtils.JsonStr(obj),mCache.getAsString("sessionToken"));
					} else {
						Looper.prepare();
						ToastUtil.show(getApplicationContext(), "手机号码格式不正确");
						Looper.loop();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.bt_next://点击下一步，获取验证码
			getCode();
			break;
		default:
			break;
		}
	}
/**
 * 接口获取验证码
 */
	private void getCode() {
		newCode = et_newcode.getText().toString();
		if (TextUtils.isEmpty(newCode)) {
			ToastUtil.show(getApplicationContext(), "验证码不能为空");
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						JSONObject obj = new JSONObject();
						obj.put("smsCode", newCode);
						Log.i("hhjjlslsjdjlssssss", JsonStrUtils.JsonStr(obj));
						String doHttpsPost = PostCLientUtils.doHttpsPost2(
								Information.KONGCV_VERIFY_MOBILE,
								JsonStrUtils.JsonStr(obj),mCache.getAsString("sessionToken"));
						Log.v("doHttpsPostPhone", doHttpsPost);
						JSONObject objs = new JSONObject(doHttpsPost);
						Message  msg= Message.obtain();
						msg.obj=objs;
						msg.what=0;
						hadler.handleMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			
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
