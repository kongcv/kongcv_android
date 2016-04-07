package com.kongcv.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.global.AboutUsBean;
import com.kongcv.global.Information;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 关于我们页面
 * 
 * @author kcw
 */
public class MineAboutus extends Activity {
	private ImageView iv_back;
	private TextView tv_weibo, tv_qq, tv_weixin, tv_email, tv_e;
	private String s_wangzhan, n_wangzhan, s_youxiang, n_youxiang, s_qq, n_qq,
			s_weixin, n_weixin, s_weibo, n_weibo;
	private List<AboutUsBean> mList;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			List<AboutUsBean> mLists = (List<AboutUsBean>) msg.obj;
			for (int i = 0; i < mLists.size(); i++) {
				String s = mLists.get(0).getInfo();
				s_wangzhan = s.substring(s.indexOf("//") + 2, s.length());
				n_wangzhan = mLists.get(0).getName();
				s_youxiang = mLists.get(1).getInfo();
				n_youxiang = mLists.get(1).getName();
				s_qq = mLists.get(2).getInfo();
				n_qq = mLists.get(2).getName();
				s_weixin = mLists.get(3).getInfo();
				n_weixin = mLists.get(3).getName();
				s_weibo = mLists.get(4).getInfo();
				n_weibo = mLists.get(4).getName();
			}
			tv_e.setText(n_wangzhan + ":" + s_wangzhan);
			tv_email.setText(n_youxiang + ":" + s_youxiang);
			tv_qq.setText(n_qq + ":" + s_qq);
			tv_weixin.setText(n_weixin + ":" + s_weixin);
			tv_weibo.setText(n_weibo + ":" + s_weibo);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_aboutus);
		MyApplication.getInstance().addActivity(this);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		initView();
		initData();
	}

	private void initView() {
		tv_weibo = (TextView) findViewById(R.id.tv_weibo);
		tv_qq = (TextView) findViewById(R.id.tv_qq);
		tv_weixin = (TextView) findViewById(R.id.tv_weixin);
		tv_email = (TextView) findViewById(R.id.tv_email);
		tv_e = (TextView) findViewById(R.id.tv_e);
	}

	public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType
			.parse("application/json;charset=utf-8");
	private final OkHttpClient client = new OkHttpClient();
	
	private void initData() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("{}", null);
			okhttp3.Request request = new okhttp3.Request.Builder()
			.url(Information.KONGCV_GET_COMPANY_INFO)
			.headers(Information.getHeaders())
			.post(RequestBody.create(MEDIA_TYPE_MARKDOWN,
					JsonStrUtils.JsonStr(obj))).build();
			client.newCall(request).enqueue(new Callback() {

				@Override
				public void onResponse(Call arg0, okhttp3.Response arg1)
						throws IOException {
					if (arg1.isSuccessful()) {
						doMRun(arg1.body().string());
					}
				}

				@Override
				public void onFailure(Call arg0, IOException arg1) {

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void doMRun(String str){
		try {
			JSONObject Resultobj = new JSONObject(str);
			String result = Resultobj.getString("result");
			JSONArray array = new JSONArray(result);
			Log.i("result", result);
			mList = new ArrayList<AboutUsBean>();
			AboutUsBean as;
			for (int i = 0; i < array.length(); i++) {
				as = new AboutUsBean();
				String info = array.getJSONObject(i).getString("info");
				String name = array.getJSONObject(i).getString("name");
				as.setInfo(info);
				as.setName(name);
				mList.add(as);

			}
			Message msg = Message.obtain();
			msg.obj = mList;
			msg.what = 0;
			handler.sendMessage(msg);

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
