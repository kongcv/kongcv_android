package com.kongcv.activity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.kongcv.R;
import com.kongcv.global.CardTypeBean;
import com.kongcv.global.Information;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.Data;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 钱包页面
 * 
 * @author kcw
 * 
 */
public class MineWalletActivity extends Activity implements OnClickListener {
	private ImageView iv_back;// 返回键
	private RelativeLayout rl_wallet_check;// 账单
	private RelativeLayout rl_minecredit;// 银行卡
	private RelativeLayout rl_minecash;// 提现
	private TextView tv_money;
	private List<CardTypeBean> list;
	private CardTypeBean carben;
	/**
	 * first是没有提现过，second是提现过
	 */
	public static String COUNT = "first";
	private ACacheUtils mCache;
	private	String newMoney;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String newMon= (String) msg.obj;
			    tv_money.setText(newMon + "元"); 
				break;
			case 1:
				tv_money.setText("0.00元");
				break;
			default:
				break;
			}
			

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_wallet);
		mCache = ACacheUtils.get(this);
		initView();
		initData();
	}

	private void initView() {
		rl_minecash = (RelativeLayout) findViewById(R.id.rl_minecash);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		rl_wallet_check = (RelativeLayout) findViewById(R.id.rl_wallet_check);
		rl_minecredit = (RelativeLayout) findViewById(R.id.rl_minecredit);
		iv_back.setOnClickListener(this);
		rl_minecash.setOnClickListener(this);
		rl_wallet_check.setOnClickListener(this);
		rl_minecredit.setOnClickListener(this);
		tv_money = (TextView) findViewById(R.id.tv_money);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_wallet_check:// 账单
			Intent intentCheck = new Intent(this, MineWalletCheckActivity.class);
			startActivity(intentCheck);
			break;
		case R.id.rl_minecredit:// 银行卡
			Intent intentCredit = new Intent(this,
					MineWalletCreditActivity.class);
			startActivity(intentCredit);
			break;
		case R.id.rl_minecash:// 提现
			Intent intentCash = new Intent(this, MineWithDrawCashActivity.class);
			startActivityForResult(intentCash, 0);
			break;
		case R.id.iv_back:// 返回键
			finish();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 0:
			if (data != null) {
			String tixian=data.getStringExtra("tixian");
			double ss=Double.parseDouble(newMoney)-Double.valueOf(tixian);
	    	 //直接截取，不会四舍五入
	         BigDecimal bg = new BigDecimal(ss);  
	         String qian=bg.setScale(2, RoundingMode.DOWN).toString();
			  tv_money.setText(qian+ "元");
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 接口获取钱包数据
	 */
	private void initData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject obj = new JSONObject();
				try {
					obj.put("user_id", mCache.getAsString("user_id"));
					String doHttpsPost = PostCLientUtils.doHttpsPost(
							Information.KONGCV_GET_PURSE,
							JsonStrUtils.JsonStr(obj));
					Log.i("doHttpsPost结果是", doHttpsPost);
					JSONObject Resultobj = new JSONObject(doHttpsPost);
					String result = Resultobj.getString("result");
					Log.i("result结果是", result);
					Message msg=handler.obtainMessage();
					if(!result.equals("[]")) {
						JSONArray arry = new JSONArray(result);
						list = new ArrayList<CardTypeBean>();
						carben = new CardTypeBean();
						for (int i = 0; i < arry.length(); i++) {
							String mon = arry.getJSONObject(i).getString(
									"money");
							carben.setMoney(mon);
						}
						 list.add(carben);
				    	 String money = list.get(0).getMoney();
				    	 double num=Double.parseDouble(money);
				    	 //直接截取，不会四舍五入
				         BigDecimal bg = new BigDecimal(num);  
				         newMoney=bg.setScale(2, RoundingMode.DOWN).toString();
						/**
						 * 保存账户余额里的钱
						 */
						Data.putData("newMoney", newMoney);
						msg.what = 0;
						msg.obj=newMoney;
					}else{
						msg.what = 1;
					}
					handler.sendMessage(msg);
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
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}
}
