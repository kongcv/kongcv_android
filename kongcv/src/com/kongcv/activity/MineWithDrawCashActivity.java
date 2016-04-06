package com.kongcv.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.global.Information;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.Data;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.MD5Utils;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 提现页面
 * 
 * @author kcw
 * 
 */
public class MineWithDrawCashActivity extends Activity implements
		OnClickListener {
	private ImageView iv_back;// 返回键
	private EditText et_checkmoney;// 提取金额
	private EditText et_checkpwd;// 提取密码
	private String password;
	private String money;
	private Button bt_commitdraw;// 确认提现
	private ACacheUtils mCache;
	private Double age0;
	private Button bt_checkHistory;// 提现记录
	private String pwds;
	private TextView tv_card;
	private LinearLayout withdraw_lin;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				MineWalletActivity.COUNT = "second";
				ToastUtil.show(MineWithDrawCashActivity.this, "提现成功");
				break;
			case 1:
				ToastUtil.show(MineWithDrawCashActivity.this, "提现失败");
				break;
			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_withdrawcash);
		mCache = ACacheUtils.get(this);
		MyApplication.getInstance().addActivity(this);
		initView();
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		bt_checkHistory = (Button) findViewById(R.id.bt_checkHistory);
		iv_back.setOnClickListener(this);
		et_checkmoney = (EditText) findViewById(R.id.et_checkmoney);
		et_checkpwd = (EditText) findViewById(R.id.et_checkpwd);
		bt_commitdraw = (Button) findViewById(R.id.bt_commitdraw);
		bt_commitdraw.setOnClickListener(this);
		bt_checkHistory.setOnClickListener(this);
		tv_card = (TextView) findViewById(R.id.tv_card);
		withdraw_lin = (LinearLayout) findViewById(R.id.withdraw_lin);
		withdraw_lin.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), 0);
			}
		});
		if (mCache.getAsString("bank") != null
				&& mCache.getAsString("card") != null) {
			String card = mCache.getAsString("card");
			String subCard = card.substring(card.length() - 4, card.length());
			tv_card.setText(mCache.getAsString("bank") + "(" + subCard + ")");
			tv_card.setTextColor(Color.parseColor("#1967C1"));

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.bt_commitdraw:// 确认提现按钮
			checkMoney();
			break;
		case R.id.bt_checkHistory:// 提现记录
			Intent i = new Intent(this, MineCheckHistory.class);
			startActivity(i);

			break;
		default:
			break;
		}

	}

	private void checkMoney() {
		money = et_checkmoney.getText().toString();
		if ("".equals(money)) {
			ToastUtil.show(getApplicationContext(), "请输入提现金额");
			return;
		} else if (money.equals("0")) {
			ToastUtil.show(getApplicationContext(), "提现金额不对哦!");
			return;
		} else {
			age0 = Double.valueOf(money);

		}
		checkPwd();

	}

	/**
	 * 校验密码
	 */
	protected void checkPwd() {
		password = et_checkpwd.getText().toString().trim();
		String pwd = mCache.getAsString("pwd");
		pwds = MD5Utils.md5(password);
		Log.i("密码ssssss", password);
		if (TextUtils.isEmpty(password)) {
			ToastUtil.show(getApplicationContext(), "请输入提现密码");
			return;
		} else if (!pwds.equals(pwd)) {
			ToastUtil.show(getApplicationContext(), "提现密码输入错误");
			return;

		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					JSONObject obj = new JSONObject();
					try {
						obj.put("user_id", mCache.getAsString("user_id"));
						obj.put("passwd", pwds);
						Log.v("objKKKKK", obj.toString());

						String doHttpsPost = PostCLientUtils.doHttpsPost(
								Information.KONGCV_VERIFY_PURSE_PASSWD,
								JsonStrUtils.JsonStr(obj));

						Log.i("hhjjlslsjdjl", doHttpsPost);

						JSONObject objs = new JSONObject(doHttpsPost);
						Log.i("hhjjlslsjdjl", objs + "");
						if (objs != null) {
							String str = objs.getString("result");
							if (str != null) {
								JSONObject objStr = new JSONObject(str);
								String state = objStr.getString("state");

								if (state.equals("ok")) {
									checkMoneys();

								} else {
									Looper.prepare();
									ToastUtil.show(
											MineWithDrawCashActivity.this,
											"密码输入错误");
									Looper.loop();
								}

							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	/**
	 * 走提现接口
	 */
	protected void checkMoneys() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				JSONObject obj = new JSONObject();
				try {
					obj.put("user_id", mCache.getAsString("user_id"));
					obj.put("money", age0);
					Data.putData("tixianmoney", money);
					if (Double.valueOf(Data.getData("newMoney").toString()) < age0) {
						Looper.prepare();
						ToastUtil.show(MineWithDrawCashActivity.this,
								"抱歉，余额不足哦");
						Looper.loop();
						return;
					}
					String doHttpsPost = PostCLientUtils.doHttpsPost(
							Information.KONGCV_INSERT_WITHDRAW_DEPOSIT,
							JsonStrUtils.JsonStr(obj));

					JSONObject objs = new JSONObject(doHttpsPost);
					Log.i("hhjjlslsjdjl", objs + "");
					if (objs != null) {
						String str = objs.getString("result");
						JSONObject objStr = new JSONObject(str);
						String state = objStr.getString("state");
						Message msg = handler.obtainMessage();
						if (state.equals("ok")) {

							msg.obj = state;
							msg.what = 0;

						} else {
							msg.what = 1;
						}
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

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