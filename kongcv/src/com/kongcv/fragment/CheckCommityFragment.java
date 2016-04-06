package com.kongcv.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kongcv.R;
import com.kongcv.activity.MineWalletCheckActivity;
import com.kongcv.adapter.ZdAdapter;
import com.kongcv.global.CheckBean;
import com.kongcv.global.Information;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.GTMDateUtil;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.ToastUtil;
import com.kongcv.view.AMapListView;
import com.kongcv.view.AMapListView.AMapListViewListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 社区下的账单
 * 
 * @author kcw
 * 
 */
public class CheckCommityFragment extends Fragment implements
		AMapListViewListener {
	private AMapListView lv;
	private List<CheckBean> checkList;
	private ACacheUtils mCache;
	private String times;
	private int type;
	private ZdAdapter zdAdapter;
	private MineWalletCheckActivity checkActivity;
	private int skip = 0;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				checkList = (List<CheckBean>) msg.obj;
				zdAdapter = new ZdAdapter(checkActivity, checkList);
				lv.setAdapter(zdAdapter);
				break;
			case 1:
				ToastUtil.show(checkActivity, "抱歉，没有更多数据哦！");
				break;
			default:
				break;
			}

		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.checkcommity, null);
		checkActivity = (MineWalletCheckActivity) getActivity();
		lv = (AMapListView) view.findViewById(R.id.lv);
		mCache = ACacheUtils.get(getActivity());
		Bundle bundle = getArguments();
		type = bundle.getInt("type");
		times = bundle.getString("time");
		initView();
		getData(0, 10);
		return view;
	}

	private void initView() {
		lv.setPullLoadEnable(true);
		lv.setAMapListViewListener(this);
	}

	/**
	 * 网络请求数据
	 */
	public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType
			.parse("application/json;charset=utf-8");
	private final OkHttpClient client = new OkHttpClient();

	private void getData(final int skip, final int limit) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("user_id", mCache.getAsString("user_id"));
			obj.put("query_month", times);
			obj.put("skip", skip * 10);
			obj.put("limit", limit);
			if (type == 0) {
				obj.put("role", "customer");
			} else {
				obj.put("role", "hirer");
			}
			obj.put("mode", "community");
			obj.put("pay_state", 0);

			okhttp3.Request request = new okhttp3.Request.Builder()
					.url(Information.KONGCV_GET_TRADE_DATE_LIST)
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

	private void doMRun(String str) {
		Message msg = handler.obtainMessage();
		try {
			JSONObject object = new JSONObject(str);
			JSONArray array;
			array = object.getJSONArray("result");
			if (array != null && array.length() > 0) {
				checkList = new ArrayList<CheckBean>();
				CheckBean cb;
				for (int i = 0; i < array.length(); i++) {
					cb = new CheckBean();
					String money = array.getJSONObject(i).getString("money");

					if (array.getJSONObject(i).has("park_community")) {
						String json = array.getJSONObject(i).getString(
								"park_community");
						JSONObject objStr = new JSONObject(json);
						String address = objStr.getString("address");
						cb.setAddress(address);
					} else {
						cb.setAddress("");
					}
					String createdAt = GTMDateUtil.GTMToLocal(array
							.getJSONObject(i).getString("createdAt"), true);
					String year = createdAt
							.substring(0, createdAt.indexOf(" "));
					String time = createdAt.substring(
							createdAt.indexOf(" ") + 1,
							createdAt.indexOf(":", createdAt.indexOf(":") + 1));

					cb.setMoney(money);
					cb.setTime(time);
					cb.setYear(year);
					checkList.add(cb);
				}
				msg.what = 0;
				msg.obj = checkList;
			} else {
				msg.what = 1;
			}
			handler.sendMessage(msg);

		} catch (JSONException e) {
			e.printStackTrace();
		}

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
	

}