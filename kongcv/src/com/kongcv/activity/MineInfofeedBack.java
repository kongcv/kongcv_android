package com.kongcv.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
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
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.ToastUtil;
/**
 * 信息反馈
 * @author kcw
 */
public class MineInfofeedBack extends Activity implements OnClickListener{
	
	private Button bt_send;
	private EditText et_infomationfeed;
	private String info;
	private ACacheUtils mCache;
	private ImageView iv_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_feedback);
		mCache= ACacheUtils.get(this);
		MyApplication.getInstance().addActivity(this);
		initView();
	}


	private void initView() {
		bt_send=(Button) findViewById(R.id.bt_send);
		bt_send.setOnClickListener(this);
		et_infomationfeed=(EditText) findViewById(R.id.et_infomationfeed);
		iv_back=(ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_send:
			send();
			
			break;
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
		
	}

	private void send() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
		        info=et_infomationfeed.getText().toString().trim();
				JSONObject obj = new JSONObject();
				try {
					obj.put("user_id", "566e5cf900b0bf377b2e77a2");
					obj.put("feedback", info);
					Log.i("TTTYHGHH++++++",
							"user_id:" + mCache.getAsString("user_id")
									+ "+money:" + info);

					String doHttpsPost = PostCLientUtils.doHttpsPost(
							Information.KONGCV_INSERT_FEEDBACK,
							JsonStrUtils.JsonStr(obj));
					
					JSONObject objs = new JSONObject(doHttpsPost);
					Log.i("信息反馈", objs + "");
					if (objs != null) {
						String str = objs.getString("result");
						
						if (str != null) {
							JSONObject objStr = new JSONObject(str);
							String state = objStr.getString("state");

							if (state.equals("ok")) {
								Looper.prepare();
								com.kongcv.utils.ToastUtil.show(
										MineInfofeedBack.this, "发送成功");
								Looper.loop();
								
							} else {
								Looper.prepare();
								ToastUtil.show(MineInfofeedBack.this,
										"发送失败");
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
