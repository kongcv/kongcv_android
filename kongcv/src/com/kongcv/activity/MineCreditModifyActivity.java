package com.kongcv.activity;

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
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 修改银行卡页面
 * 
 * @author kcw
 */
public class MineCreditModifyActivity extends Activity implements
		OnClickListener {
	private ImageView iv_back;// 返回键
	private EditText et_credithost;// 持卡人姓名
	private EditText et_creditnumber;// 持卡人卡号
	/**
	 * 持卡人姓名
	 */
	private String hostName;
	/**
	 * 持卡人卡号(新输入的)
	 */
	private String newHostCode;
	private TextView tv_credittype;
	private EditText et_creditinput, et_creditagaininput, et_creditmodifyinput;
	private Button bt_submit;
	private RelativeLayout rl_types;
	private ImageView iv_yuan;
	/**
	 * 输入的密码
	 */
	private String password;
	/**
	 * 修改密码
	 */
	private String modifyPassword;
	/**
	 * 确认修改密码
	 */
	private String againmodifypwd;
	/**
	 * MD5加密码
	 */
	private String pwds;
	private ACacheUtils mCache;
	private LinearLayout modify_lin;
	/**
	 * 银行卡类型
	 */
	private String NewType;
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
							ToastUtil.show(MineCreditModifyActivity.this,
									"修改成功");
							Intent i=new Intent(MineCreditModifyActivity.this,MineWalletCreditActivity.class);
							MineCreditAddActivity.TAG = "old";
							startActivity(i);
					
							finish();
						} else {
							ToastUtil.show(MineCreditModifyActivity.this,
									"信息修改失败！");
						}
					} else {
						ToastUtil.show(MineCreditModifyActivity.this, "修改失败！");
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
		setContentView(R.layout.mine_modifycredit);
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
		et_creditmodifyinput = (EditText) findViewById(R.id.et_creditmodifyinput);
		// 显示出用户添加银行卡的一些信息
		et_credithost.setText(Data.getData("hostName").toString());
		et_creditnumber.setText(Data.getData("hostCode").toString());
		tv_credittype.setText(Data.getData("bank").toString());
		modify_lin=(LinearLayout) findViewById(R.id.modify_lin);
		iv_yuan.setImageBitmap((Bitmap) Data.getData("choiceBankImg"));
		modify_lin.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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
	private void addCreditSubmit() {

		hostName = et_credithost.getText().toString().trim();

		newHostCode = et_creditnumber.getText().toString().trim();

		NewType = tv_credittype.getText().toString().trim();

		password = et_creditinput.getText().toString().trim();
		modifyPassword = et_creditagaininput.getText().toString().trim();
		againmodifypwd = et_creditmodifyinput.getText().toString().trim();

		Log.i("密码sss", password);
		if (TextUtils.isEmpty(password)) {
			ToastUtil.show(this, "密码不能为空");
			return;
		}
		if (!(Data.getData("pwd")).equals(MD5Utils.md5(password))) {
			ToastUtil.show(this, "您输入的密码有误");
			return;
		}
		if (TextUtils.isEmpty(modifyPassword)) {
			ToastUtil.show(this, "请输入修改密码");
			return;
		}
		if (!(modifyPassword.equals(againmodifypwd))) {
			ToastUtil.show(this, "两次输入密码不一致");
			return;
		} else {
			pwds = MD5Utils.md5(modifyPassword);
			Log.i("密码", pwds);
			// 将MD5码放入Data中以便后面修改
			Data.putData("pwd", pwds);
			mCache.put("pwd", pwds);
		}
		
		/**
		 * 接口请求数据
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject obj = new JSONObject();
				try {
					obj.put("user_id", mCache.getAsString("user_id"));
					JSONObject bank_card = new JSONObject();
					bank_card.put("bank", NewType);
					bank_card.put("card", newHostCode);
					bank_card.put("name", hostName);
					bank_card.put("bank_icon_url", Data.getData("banUrl"));
					// 银行卡类型
					Data.putData("bank", NewType);
					
					obj.put("bank_card", bank_card);
					obj.put("passwd", pwds);
					if (!newHostCode.equals(Data.getData("hostCode").toString())) {
						// 银行卡卡号
						Data.putData("hostCode", newHostCode);
						MineCreditAddActivity.TAG = "card";
						obj.put("action", "card");
						Log.e("sssss+++++++",MineCreditAddActivity.TAG );
					} else if (!(password.equals(modifyPassword))) {
						MineCreditAddActivity.TAG = "passwd";
						obj.put("action", "passwd");
						Log.e("sssss+++++++VVVVVVVVVVVV",MineCreditAddActivity.TAG );
					}
					Log.i("objobjobjobjobj", obj.toString());
					String doHttpsPost = PostCLientUtils.doHttpsPost(
							Information.KONGCV_PUT_PURSE,
							JsonStrUtils.JsonStr(obj));
					Log.i("doHttpsPost怎么", doHttpsPost);
					JSONObject objs = new JSONObject(doHttpsPost);
					Message m = Message.obtain();
					m.what = 1;
					m.obj = objs;
					handler.sendMessage(m);
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
