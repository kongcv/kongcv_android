package com.kongcv.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
/**
 * 道路下的账单
 * @author kcw
 *
 */
public class CheckCurbFragment extends Fragment implements AMapListViewListener {
	private AMapListView lv;
	private List<CheckBean> checkList;
	private ACacheUtils mCache;
	private String times;
	private int type;
	private ZdAdapter zdAdapter;
	private MineWalletCheckActivity checkActivity;
	private int skip=0;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				checkList = (List<CheckBean>) msg.obj;

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
		View view = inflater.inflate(R.layout.checkcurb, null);
		checkActivity = (MineWalletCheckActivity) getActivity();
		checkList = new ArrayList<CheckBean>();	
		zdAdapter = new ZdAdapter(checkActivity, checkList);
		lv = (AMapListView) view.findViewById(R.id.lv);
		mCache = ACacheUtils.get(getActivity());
		Bundle bundle = getArguments();
		type = bundle.getInt("type");
		times = bundle.getString("time");
		initView();
		checkList.clear();
		zdAdapter.notifyDataSetChanged();
		getData(0,10);
		return view;
	}

	private void initView() {
		lv.setPullLoadEnable(true);
		lv.setAMapListViewListener(this);
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
	private void getData(final int skip, final int limit) {
		new Thread(new Runnable() {
			@Override
			public void run() {
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
					obj.put("mode", "curb");
					obj.put("pay_state", 0);
					Log.i("obj", obj + "");
					String doHttpsPost = PostCLientUtils.doHttpsPost(
							Information.KONGCV_GET_TRADE_DATE_LIST,
							JsonStrUtils.JsonStr(obj));
					Log.i("doHttpsPostbbbbbbbbbbbbb", doHttpsPost);
					JSONObject object = new JSONObject(doHttpsPost);
					JSONArray array = object.getJSONArray("result");
					Log.e("array", array + "");
					Message msg = handler.obtainMessage();
					if (array != null && array.length()>0){
					
						CheckBean cb;
					
			
					for (int i = 0; i < array.length(); i++) {
						cb = new CheckBean();
						String money = array.getJSONObject(i)
								.getString("money");
						String json = array.getJSONObject(i).getString(
								"park_curb");
						JSONObject objStr = new JSONObject(json);
						String address = objStr.getString("address");
						String createdAt = GTMDateUtil.GTMToLocal(array
								.getJSONObject(i).getString("createdAt"),
								true);
						String year = createdAt.substring(0,
								createdAt.indexOf(" "));
						String time = createdAt.substring(
								createdAt.indexOf(" ") + 1,
								createdAt.indexOf(":",
										createdAt.indexOf(":") + 1));
						cb.setAddress(address);
						cb.setMoney(money);
						cb.setTime(time);
						cb.setYear(year);
						checkList.add(cb);
					}
					msg.what = 0;
					msg.obj = checkList;
					}else{
						msg.what=1;
					}
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();

	}

}