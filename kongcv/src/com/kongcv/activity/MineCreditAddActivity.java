package com.kongcv.activity;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.global.Information;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.Data;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.MD5Utils;
import com.kongcv.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 添加银行卡页面
 */
public class MineCreditAddActivity extends Activity implements OnClickListener {
	private ImageView iv_back;// 返回键
	private EditText et_credithost;// 持卡人姓名
	private EditText et_creditnumber;// 持卡人卡号
	/**
	 * 持卡人姓名
	 */
	private String hostName;
	/**
	 * 持卡人卡号
	 */
	private String hostCode;
	private TextView tv_credittype;
	private EditText et_creditinput, et_creditagaininput;
	private Button bt_submit;
	private RelativeLayout rl_types;
	private ImageView iv_yuan;
	/**
	 * 输入的密码
	 */
	private String password;
	/**
	 * 确认密码
	 */
	private String againpassword;
	/**
	 * MD5加密码
	 */
	private String pwds;
	/**
	 * 银行卡类型
	 */
	private String type;
	/**
	 * new代表新创建，card代表更新卡号，passwd代表更新密码
	 */
	public static String TAG = "new";
	private ACacheUtils mCache;
	private LinearLayout add_lin;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			JSONObject objs = (JSONObject) msg.obj;
			try {
				if (objs != null) {
					String str = objs.getString("result");
					if (str != null) {
						JSONObject objStr = new JSONObject(str);
						String state = objStr.getString("state");
						if (state.equals("ok")) {
							ToastUtil.show(MineCreditAddActivity.this, "提交成功");
							Intent i = new Intent(MineCreditAddActivity.this,
									MineWalletCreditActivity.class);
							TAG = "old";
							startActivity(i);
							finish();
						} else {
							ToastUtil.show(MineCreditAddActivity.this, "你已提交！");
						}
					} else {
						ToastUtil.show(MineCreditAddActivity.this, "提交失败！");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_addcredit);
		mCache = ACacheUtils.get(this);
		 MyApplication.getInstance().addActivity(this);
		initView();
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		et_credithost = (EditText) findViewById(R.id.et_credithost);
		et_creditnumber = (EditText) findViewById(R.id.et_creditnumber);
		et_creditinput = (EditText) findViewById(R.id.et_creditinput);// 输入密码
		et_creditagaininput = (EditText) findViewById(R.id.et_creditagaininput);// 确认密码
		tv_credittype = (TextView) findViewById(R.id.tv_credittype);// 银行卡类型
		bt_submit = (Button) findViewById(R.id.bt_submit);// 提交按钮
		iv_back.setOnClickListener(this);
		bt_submit.setOnClickListener(this);
		rl_types = (RelativeLayout) findViewById(R.id.rl_types);
		rl_types.setOnClickListener(this);
		iv_yuan = (ImageView) findViewById(R.id.iv_yuan);
		tv_credittype = (TextView) findViewById(R.id.tv_credittype);
		add_lin = (LinearLayout) findViewById(R.id.add_lin);
		add_lin.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	             return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.bt_submit:
			addCreditSubmit();
			break;
		case R.id.rl_types:
			choiceCardType();
			break;
		default:
			break;
		}
	}

	/**
	 * 找回银行卡
	 */
	private void choiceCardType() {
		Intent intent = new Intent(this, MineCreditType.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * 获取银行卡图片和类型
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 0:
			if (data != null) {
				String choiceBankType = data.getStringExtra("choiceBankType");
				Bitmap choiceBankImg = data.getParcelableExtra("choiceBankImg");
				String banUrl = data.getStringExtra("bankUrl");
				tv_credittype.setText(choiceBankType);
				iv_yuan.setImageBitmap(choiceBankImg);
				// 存入银行卡图片
				Data.putData("choiceBankImg", choiceBankImg);
				Data.putData("banUrl", banUrl);
			}
			break;
		default:
			break;
		}

	}

	/*
	 * 提交添加的银行卡
	 */
	private final OkHttpClient client = new OkHttpClient();

	private void addCreditSubmit() {
		hostName = et_credithost.getText().toString().trim();
		hostCode = et_creditnumber.getText().toString().trim();
		type = tv_credittype.getText().toString().trim();
		if (TextUtils.isEmpty(type)) {
			ToastUtil.show(this, "请您选择要添加的银行卡");
			return;
		}
		if (TextUtils.isEmpty(hostName)) {
			ToastUtil.show(this, "请输入正确的姓名");
			return;
		}
		if (TextUtils.isEmpty(hostCode)) {
			ToastUtil.show(this, "请输入正确的银行卡号");
			return;
		}
		password = et_creditinput.getText().toString().trim();
		againpassword = et_creditagaininput.getText().toString().trim();
		if (TextUtils.isEmpty(password)) {
			ToastUtil.show(this, "密码不能为空");
			return;
		}
		if (TextUtils.isEmpty(againpassword)) {
			ToastUtil.show(this, "请再次输入密码");
			return;
		}
		if (!(password.equals(againpassword))) {
			ToastUtil.show(this, "两次输入密码不一致");
			return;
		} else {
			pwds = MD5Utils.md5(password);
			// 将MD5码放入Data中以便后面修改
			Data.putData("pwd", pwds);

		}
		try {
			JSONObject obj = new JSONObject();
			obj.put("user_id", mCache.getAsString("user_id"));
			JSONObject bank_card = new JSONObject();
			bank_card.put("bank", type);
			bank_card.put("card", hostCode);
			bank_card.put("name", hostName);
			bank_card.put("bank_icon_url", Data.getData("banUrl"));
			Data.putData("hostName", hostName);
			// 银行卡类型
			Data.putData("bank", type);
			// 银行卡卡号
			Data.putData("hostCode", hostCode);
			obj.put("bank_card", bank_card);
			obj.put("passwd", pwds);
			obj.put("action", "new");
			okhttp3.Request request = new okhttp3.Request.Builder()
					.url(Information.KONGCV_PUT_PURSE)
					.headers(Information.getHeaders())
					.post(RequestBody.create(Information.MEDIA_TYPE_MARKDOWN,
							JsonStrUtils.JsonStr(obj))).build();

			client.newCall(request).enqueue(new okhttp3.Callback() {

				@Override
				public void onResponse(Call arg0, okhttp3.Response response)
						throws IOException {
					if (response.isSuccessful()) {
						JSONObject objs;
						try {
							objs = new JSONObject(response.toString());
							Message m = Message.obtain();
							m.what = 1;
							m.obj = objs;
							handler.sendMessage(m);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				@Override
				public void onFailure(Call arg0, IOException arg1) {
					Log.e("MEDIA_TYPE_MARKDOWN", arg1.toString());
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
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
