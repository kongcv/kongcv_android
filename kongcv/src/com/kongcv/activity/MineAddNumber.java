package com.kongcv.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 添加车牌卡
 * @author kcw
 */
public class MineAddNumber extends Activity implements OnClickListener {
	private Button bt_cancel;
	private Button bt_save;
	private TextView tv_card;
	private String card;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_carnumber);
		MyApplication.getInstance().addActivity(this);
		initView();
	}

	/**
	 * 保存数据
	 */
	private void saveData() {
		card = tv_card.getText().toString().trim();
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject jso = new JSONObject();
				try {
					jso.put("mobilePhoneNumber", "");
					jso.put("user_name", "");
					jso.put("device_token", "");
					jso.put("device_type", "");
					jso.put("license_plate", card);
					String doHttpsPost = PostCLientUtils.doHttpsPost2(
							com.kongcv.global.Information.KONGCV_PUT_USERINFO,
							JsonStrUtils.JsonStr(jso),"fkaoere1uvy0altxujrym67uz");

					JSONObject objs = new JSONObject(doHttpsPost);
					Log.i("hhjjlslsjdjl", objs + "");
					if (objs != null) {
						String str = objs.getString("result");

						if (str != null) {
							JSONObject objStr = new JSONObject(str);
							String state = objStr.getString("state");

							if (state.equals("ok")) {
								Looper.prepare();
								com.kongcv.utils.ToastUtil.show(
										MineAddNumber.this, "保存成功");
								Looper.loop();

							} else {
								Looper.prepare();
								ToastUtil.show(MineAddNumber.this, "保存失败");
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

	private void initView() {
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_save = (Button) findViewById(R.id.bt_save);
		tv_card = (TextView) findViewById(R.id.tv_card);
		bt_cancel.setOnClickListener(this);
		bt_save.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_cancel:
			finish();
			break;
		case R.id.bt_save:
			saveData();
			break;

		default:
			break;
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
