package com.kongcv.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

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
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.ImageRun.GetImage;
import com.kongcv.global.CardTypeBean;
import com.kongcv.global.Information;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.Data;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;

/**
 * 银行卡页面
 * 
 * @author kcw
 * 
 */

public class MineWalletCreditActivity extends Activity implements
		OnClickListener {
	private ImageView iv_back;// 返回键
	private ImageView iv_jiahao;// 添加银行卡
	private TextView tv_bank;
	private TextView tv_card;// 银行卡号
	private ImageView iv_bg_yuan;// 银行卡图片
	private RelativeLayout rl_bankcard;
	private ACacheUtils mCache;
	private List<CardTypeBean> list;
	private CardTypeBean cardBean;
	private String banks;
	private String cards;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:
				rl_bankcard.setVisibility(View.VISIBLE);
				iv_jiahao.setVisibility(View.INVISIBLE);
				list = (List<CardTypeBean>) msg.obj;
				tv_bank.setText(list.get(0).getBank());
				String card = list.get(0).getCard();
				String subCard = card.substring(card.length() - 4,
						card.length());
				tv_card.setText(subCard);
				iv_bg_yuan.setImageBitmap(list.get(0).getBitmap());
				Data.putData("choiceBankImg", list.get(0).getBitmap());

				Data.putData("hostName", list.get(0).getName());
				Data.putData("choiceBankImg", list.get(0).getBitmap());
				// 银行卡类型
				Data.putData("bank", list.get(0).getBank());
				// 银行卡卡号
				Data.putData("hostCode", list.get(0).getCard());
				break;
			case 1:
				rl_bankcard.setVisibility(View.INVISIBLE);
				iv_jiahao.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_credit);
		MyApplication.getInstance().addActivity(this);
		mCache = ACacheUtils.get(this);
		initView();
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_jiahao = (ImageView) findViewById(R.id.iv_jiahao);
		iv_back.setOnClickListener(this);
		iv_jiahao.setOnClickListener(this);
		tv_bank = (TextView) findViewById(R.id.tv_bank);
		tv_card = (TextView) findViewById(R.id.tv_card);
		iv_bg_yuan = (ImageView) findViewById(R.id.iv_bg_yuan);
		rl_bankcard = (RelativeLayout) findViewById(R.id.rl_bankcard);
		rl_bankcard.setOnClickListener(this);

		if ((MineCreditAddActivity.TAG).equals("old")) {
			rl_bankcard.setVisibility(View.VISIBLE);
			iv_jiahao.setVisibility(View.INVISIBLE);
			if (Data.getData("bank") != null) {
				tv_bank.setText(Data.getData("bank").toString());
				mCache.put("bank", Data.getData("bank").toString());

			}
			if (Data.getData("hostCode") != null) {
				String s = Data.getData("hostCode").toString();
				String ss = s.substring(s.length() - 4, s.length());
				tv_card.setText(ss);
				mCache.put("card", Data.getData("hostCode").toString());
			}
			if (Data.getData("choiceBankImg") != null) {
				iv_bg_yuan.setImageBitmap((Bitmap) Data
						.getData("choiceBankImg"));
			}
		} else {
			initData();
		}

	}

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
					if (!result.equals("[]")) {
						JSONArray arry = new JSONArray(result);
						list = new ArrayList<CardTypeBean>();
						cardBean = new CardTypeBean();
						for (int i = 0; i < arry.length(); i++) {
							String bank_card = arry.getJSONObject(i).getString(
									"bank_card");
							String passwd = arry.getJSONObject(i).getString(
									"passwd");
							Data.putData("pwd", passwd);
							mCache.put("pwd", passwd);
							JSONArray arrays = new JSONArray(bank_card);
							for (int ii = 0; ii < arry.length(); ii++) {
								String name = arrays.getJSONObject(ii)
										.getString("name");
								String card = arrays.getJSONObject(ii)
										.getString("card");
								String bank = arrays.getJSONObject(ii)
										.getString("bank");
								String bank_icon_url = arrays.getJSONObject(ii)
										.getString("bank_icon_url");
								Bitmap bitmap = GetImage
										.getImage(bank_icon_url);
								Data.putData("banUrl", bank_icon_url);
								mCache.put("bank", bank);
								mCache.put("card", card);
								cardBean.setBitmap(bitmap);
								cardBean.setName(name);
								cardBean.setBank(bank);
								cardBean.setCard(card);
								cardBean.setUrl(bank_icon_url);
								list.add(cardBean);

								Log.e("bank", bank + "----" + card);

							}
						}
						banks = list.get(0).getBank();
						cards = list.get(0).getCard();
						Log.e("card", banks + "----" + cards);
						Message msg = Message.obtain();
						msg.obj = list;
						msg.what = 0;
						handler.sendMessage(msg);
					} else {
						Message msg = Message.obtain();
						msg.what = 1;
						handler.sendMessage(msg);
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
		case R.id.iv_jiahao:
			Intent intentAdd = new Intent(this, MineCreditAddActivity.class);
			startActivity(intentAdd);
			finish();

			break;
		case R.id.rl_bankcard:
			Intent intentModify = new Intent(this,
					MineCreditModifyActivity.class);
			startActivity(intentModify);
			finish();

			break;
		default:
			break;
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
