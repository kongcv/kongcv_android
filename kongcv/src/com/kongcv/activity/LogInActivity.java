package com.kongcv.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.kongcv.utils.StringUtils;
import com.kongcv.utils.ToastUtil;

/**
 * 空车位登录页面
 */
public class LogInActivity extends Activity implements OnClickListener {

	private EditText phone;
	private EditText cord;
	private TextView now;
	private Button getCord;
	private Button saveCord;

	private static String iPhone;
	private String iCord;
	private String mState;
	private String mCode;
	private String mMsg;
	private Button button;
	private LinearLayout log;
	private Context context;
	private ACacheUtils mCache;
	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		MyApplication.getInstance().addActivity(this);
		mCache = ACacheUtils.get(this);
		initView();
	}
	private void initView() {
		phone = (EditText) findViewById(R.id.phone);
		cord = (EditText) findViewById(R.id.cord);
		now = (TextView) findViewById(R.id.now);
		getCord = (Button) findViewById(R.id.getcord);
		saveCord = (Button) findViewById(R.id.savecord);
		log = (LinearLayout) findViewById(R.id.log);
		log.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			}
		});
		getCord.setOnClickListener(this);
		saveCord.setOnClickListener(this);
		String textStr1 = "<font color=\"#858585\">注册代表您同意空车位</font>";
		String textStr2 = "<font color=\"#94cAE4\">网络使用协议</font>";
		String textStr3 = "<font color=\"#858585\">和</font>";
		String textStr4 = "<font color=\"#94cAE4\">隐私条款</font>";
		now.setText(Html.fromHtml(textStr1 + textStr2 + textStr3 + textStr4));
	}
	/**
	 * 验证码倒计时
	 */
	private CountDownTimer timer = new CountDownTimer(60000, 1000) {  
        @Override  
        public void onTick(long millisUntilFinished) {  
        	getCord.setText((millisUntilFinished / 1000) + "秒后可重发");  
        	getCord.setEnabled(false);
        }  
        @Override  
        public void onFinish() {  
        	getCord.setText("获取验证码"); 
        	getCord.setEnabled(true);
        }  
    }; 
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.getcord: // 获取验证码。
			iPhone=phone.getText().toString();
			if (StringUtils.isMobileNo(iPhone)) {
				getCode();
				timer.start();
			}else {
				ToastUtil.show(getApplicationContext(), "手机号码格式不正确");
			}
			break;
		case R.id.savecord:// 提交验证码和手机号码 这里 有两个版本 递推 和 用户
			getLogIn();
			break;
		default:
			break;
		}
	}
	private void getCode() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				doCode();
			}
		}).start();
	}
	private void getLogIn() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				doLogIn();
			}

		}).start();
	}

	private void doCode() {
		// TODO Auto-generated method stub
		try {
				iPhone = phone.getText().toString();
				JSONObject obj = new JSONObject();
				obj.put("mobilePhoneNumber", iPhone);
				PostCLientUtils.doHttpsPost(Information.KONGCV_GET_SMSCODE,
						JsonStrUtils.JsonStr(obj));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 登录
	 */
	private void doLogIn() {
		// TODO Auto-generated method stub
		try {
			iCord = cord.getText().toString();
			iPhone = phone.getText().toString();
			if (StringUtils.isMobileNo(iPhone) && StringUtils.isCodeNo(iCord)) {
				JSONObject obj = new JSONObject();
				obj.put("mobilePhoneNumber", iPhone);
				obj.put("smsCode", iCord);
				String doHttpsPost = PostCLientUtils.doHttpsPost(
						Information.KONGCV_SIGNUP, JsonStrUtils.JsonStr(obj));
				JSONObject obj2=new JSONObject(doHttpsPost);
				String str = obj2.getString("result");
				JSONObject objStr=new JSONObject(str);
				String state = objStr.getString("state");
				if("ok".equals(state)){
					String sessionToken = objStr.getString("sessionToken");
					String user_id = objStr.getString("user_id");
					mCache.put("USER", iPhone);//手机号码
					mCache.put("user_id", user_id);
					mCache.put("sessionToken", sessionToken);
					mCache.put("RegistrationID", JPushInterface.getRegistrationID(getApplicationContext()));
		//			传递数据 走上传的接口
					mHandler.sendEmptyMessage(0);
				}else {
					Looper.prepare();
					ToastUtil.show(getBaseContext(), "注册失败！请60秒后重新注册！");
					Looper.loop();
				}
			} else {
				Looper.prepare();
				ToastUtil.show(getBaseContext(), "验证码输入错误！");
				Looper.loop();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private Handler mHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				updateUser();
				break;
			default:
				break;
			}
		}
	};
	
	private void updateUser() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					JSONObject obj=new JSONObject();
					obj.put("mobilePhoneNumber", mCache.getAsString("USER"));
					obj.put("user_name", mCache.getAsString("USER"));
					obj.put("device_token", mCache.getAsString("RegistrationID"));
					obj.put("device_type", "android");
					obj.put("license_plate", "");
					obj.put("version", AndroidUtil.getVersion(getApplicationContext()));
					String str = PostCLientUtils.doHttpsPost2(Information.KONGCV_PUT_USERINFO, 
							JsonStrUtils.JsonStr(obj), mCache.getAsString("sessionToken"));
					Log.v("发布登录成功", str);
					JSONObject obj2=new JSONObject(str);
					String result = obj2.getString("result");
					JSONObject objStr=new JSONObject(result);
					String state = objStr.getString("state");
					if("ok".equals(state)){
						finish();
					}else {
						Looper.prepare();
						ToastUtil.show(getBaseContext(), "发布失败请尝试重新发布！");
						Looper.loop();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
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
