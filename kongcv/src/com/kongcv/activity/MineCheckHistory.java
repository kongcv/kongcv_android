package com.kongcv.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.adapter.CheckAdapter;
import com.kongcv.global.Information;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.GTMDateUtil;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.ToastUtil;
import com.kongcv.view.AMapListView;
import com.kongcv.view.AMapListView.AMapListViewListener;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * 提现记录页面
 * 
 * @author kcw
 */
public class MineCheckHistory extends Activity implements AMapListViewListener {
	private AMapListView lv;
	private ImageView iv_back;
	private List<String> mList;
	private List<String> timeList;
	private CheckAdapter mAdapter;
	private ACacheUtils mCache;
	private int skip = 0;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				mAdapter = new CheckAdapter(MineCheckHistory.this, mList,
						timeList);

				lv.setAdapter(mAdapter);
				break;
			case 1:
				ToastUtil.show(MineCheckHistory.this, "抱歉，没有更多数据哦！");
				break;
			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.minecheckhistory);
		mCache = ACacheUtils.get(this);
		MyApplication.getInstance().addActivity(this);
		initView();
		getData(0, 10);

	}

	

	/**
	 * 接口获取数据
	 */
	protected void getData(final int skip, final int limit) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject obj = new JSONObject();
				try {
					obj.put("user_id", mCache.getAsString("user_id"));
					obj.put("skip", skip * 10);
					obj.put("limit", limit);
					String doHttpsPost = PostCLientUtils.doHttpsPost(
							Information.KONGCV_GET_WITHDRAW_DEPOSIT,
							JsonStrUtils.JsonStr(obj));
					JSONObject object = new JSONObject(doHttpsPost);
					JSONArray array = object.getJSONArray("result");
					Message msg = handler.obtainMessage();
					if (array != null && array.length()>0) {
						mList = new ArrayList<String>();
						timeList = new ArrayList<String>();
						for (int i = 0; i < array.length(); i++) {
							String money = array.getJSONObject(i).getString(
									"money");
							String times = GTMDateUtil.GTMToLocal(array
									.getJSONObject(i).getString("createdAt"),
									true);
							String time = times.substring(0, times.indexOf(" "));

							mList.add(money);
							timeList.add(time);
						}
						msg.what = 0;
					} else {
						msg.what = 1;
					}
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		lv = (AMapListView) findViewById(R.id.lv);
		lv.setPullLoadEnable(true);
		lv.setAMapListViewListener(this);
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	/**
	 * 刷新
	 */
	@Override
	public void onRefresh() {
		handler.postAtTime(new Runnable() {
			@Override
			public void run() {
				getData(0, 10);
				onLoad();
			}
		}, 2000);

	}

	/**
	 * 加载更多
	 */
	@Override
	public void onLoadMore() {
		handler.postAtTime(new Runnable() {
			public void run() {
				skip++;
				getData(skip, 10);
				onLoad();
			}
		}, 2000);
	}

	/**
	 * 停止刷新
	 */
	private void onLoad() {
		lv.stopRefresh();
		lv.stopLoadMore();
		lv.setRefreshTime("刚刚");
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
