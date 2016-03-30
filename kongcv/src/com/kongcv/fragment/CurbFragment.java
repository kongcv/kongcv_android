package com.kongcv.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.kongcv.R;
import com.kongcv.ImageRun.GetImage;
import com.kongcv.UI.AsyncImageLoader.PreReadTask;
import com.kongcv.activity.DetailsActivity;
import com.kongcv.activity.MineOrdermanagerActivity;
import com.kongcv.adapter.CzCommityAdapter;
import com.kongcv.adapter.ZyCurbAdapter;
import com.kongcv.global.Information;
import com.kongcv.global.OrderCommityBean.ResultEntity;
import com.kongcv.global.OrderCommityBean.ResultEntity.HireEndEntity;
import com.kongcv.global.OrderCommityBean.ResultEntity.HireStartEntity;
import com.kongcv.global.OrderCommityBean.ResultEntity.UserList;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.GTMDateUtil;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.view.AMapListView;
import com.kongcv.view.AMapListView.AMapListViewListener;

/**
 * 道路的订单
 */
public class CurbFragment extends Fragment implements AMapListViewListener {

	
	HireStartEntity startTime;
	HireEndEntity endTime;
	ResultEntity result;
	UserList us;
	private View view;
	private int limit = 10;
	private AMapListView lv;
	private ACacheUtils mCache;
	private ZyCurbAdapter zydapter;
	private List<HireEndEntity> list;
	private List<UserList> userBeans;
	private CzCommityAdapter czdapter;
	private List<HireStartEntity> mList;
	private List<ResultEntity> resultBean;
	private String[] str = new String[] { "customer", "hirer" };
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				zydapter = new ZyCurbAdapter(getActivity(), mList, list,
						resultBean);
				lv.setAdapter(zydapter);
				lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (resultBean != null && resultBean.size() > position) {
							int state = resultBean.get(position)
									.getTrade_state();
							if (0 == state) {
								Intent i = new Intent(getActivity(),
										DetailsActivity.class);
								// 传递数据
								i.putExtra("mode", "cub");
								i.putExtra("trade_state", state);
								startActivity(i);
							}
						}
					}
				});
				break;
			case 1:
				czdapter = new CzCommityAdapter(getActivity(), mList, list,
						resultBean, userBeans);
				lv.setAdapter(czdapter);
				lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						int state = resultBean.get(position).getTrade_state();
						if (0 == state) {
							Intent i = new Intent(getActivity(),
									DetailsActivity.class);
							// 传递数据
							i.putExtra("mode", "curb");
							i.putExtra("trade_state", state);
							startActivity(i);
						}
					}
				});
				break;
			default:
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.curbfragment, container, false);
		initView();
		refresh();
		return view;
	}
	private void initView() {
		mCache = ACacheUtils.get(getActivity());
		lv = (AMapListView) view.findViewById(R.id.lv);
		lv.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		lv.setAMapListViewListener(this);
	}
	/**
	 * 初始化数据和下拉刷新数据
	 */
	private int skip = 0;
	public void refresh() {
		if (MineOrdermanagerActivity.TYPEORDER == 0) {
			getCurbOrCommInfo(breakJsonStr(str[0]), 0);
		} else {
			getCurbOrCommInfo(breakJsonStr(str[1]), 1);
		}
	}
	/**
	 * 网络请求
	 */
	private final OkHttpClient client = new OkHttpClient();
	private void getCurbOrCommInfo(String jsonStr, final int i) {
		// TODO Auto-generated method stub
		Log.d("i==" + i, jsonStr + ":");
		okhttp3.Request request = new okhttp3.Request.Builder()
				.url(Information.KONGCV_GET_TRADE_LIST)
				.headers(Information.getHeaders())
				.post(RequestBody.create(Information.MEDIA_TYPE_MARKDOWN,
						jsonStr)).build();
		client.newCall(request).enqueue(new okhttp3.Callback() {
			@Override
			public void onResponse(Call arg0, okhttp3.Response response)
					throws IOException {
				// TODO Auto-generated method stub
				if (response.isSuccessful()) {
					if (i == 0) {
						doResponse(response.body().string());
					} else {
						doResponse2(response.body().string());
					}
				}
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				Log.e("KONGCV_GET_HIRE_METHOD", arg1.toString());
			}
		});
	}

	private String mobilePhoneNumber;

	private void doResponse(String string) {
		// TODO Auto-generated method stub
		try {
			JSONObject object = new JSONObject(string);
			JSONArray array = object.getJSONArray("result");
			if (array != null && array.length() > 0) {
				mList = new ArrayList<HireStartEntity>();
				list = new ArrayList<HireEndEntity>();
				resultBean = new ArrayList<ResultEntity>();
				userBeans = new ArrayList<UserList>();
				startTime = new HireStartEntity();
				endTime = new HireEndEntity();
				result = new ResultEntity();
				us = new UserList();
				for (int i = 0; i < array.length(); i++) {
					JSONObject ob = array.getJSONObject(i);
					if (ob.has("hire_start")) {
						Log.v("ssss", ob.has("hire_start") + "");
						String start = GTMDateUtil
								.GTMToLocal(ob.getJSONObject("hire_start")
										.getString("iso"), true);
						startTime.setIso(start);
						mList.add(startTime);
					} else {
						startTime.setIso("");
						mList.add(startTime);
					}
					if (ob.has("hire_end")) {
						String end = GTMDateUtil.GTMToLocal(
								ob.getJSONObject("hire_end").getString("iso"),
								true);
						endTime.setIso(end);
						list.add(endTime);// 结束时间
					} else {
						Log.e("第条结束时间：" + i, ob + "");
						endTime.setIso("");
						list.add(endTime);
					}
					double price = array.getJSONObject(i).getDouble("price");
					// 订单号
					String objectId = array.getJSONObject(i).getString(
							"objectId");
					// 租用方式
					String hire_method = array.getJSONObject(i).getString(
							"hire_method");
					JSONObject objStrs = new JSONObject(hire_method);
					String method = objStrs.getString("method");
					// 用户名
					String user = array.getJSONObject(i).getString("user");
					JSONObject objStr = new JSONObject(user);
					if (objStr.has("username")) {
						String username = objStr.getString("username");
						us.setUsername(username);// 用户名
					} else {
						us.setUsername("");
					}
					// 头像
					if (objStr.has("image")) {
						String url = objStr.getJSONObject("image").getString(
								"url");
						Bitmap bitMap = GetImage.getImage(url);
						us.setBitMap(bitMap);
					} else {
						us.setBitMap(null);
					}
					if (objStr.has("mobilePhoneNumber")) {
						// 电话
						mobilePhoneNumber = objStr
								.getString("mobilePhoneNumber");
					}
					// 订单状态
					int trade_state = array.getJSONObject(i).getInt(
							"trade_state");
					result.setPrice(price);// 价钱
					result.setObjectId(objectId);// 订单号
					result.setTrade_state(trade_state);// 订单状态
					result.setMethod(method);// 租用方式
					us.setMobilePhoneNumber(mobilePhoneNumber);// 电话
					resultBean.add(result);
					userBeans.add(us);
				}
				Message msg = Message.obtain();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void doResponse2(String string) {
		// TODO Auto-generated method stub
		try {
			JSONObject object = new JSONObject(string);
			JSONArray array = object.getJSONArray("result");
			if (array != null && array.length() > 0) {
				us = new UserList();
				result = new ResultEntity();
				endTime = new HireEndEntity();
				startTime = new HireStartEntity();
				mList = new ArrayList<HireStartEntity>();
				list = new ArrayList<HireEndEntity>();
				resultBean = new ArrayList<ResultEntity>();
				userBeans = new ArrayList<UserList>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject ob = array.getJSONObject(i);
					if (ob.has("hire_start")) {
						Log.v("ssss", ob.has("hire_start") + "");
						String start = GTMDateUtil
								.GTMToLocal(ob.getJSONObject("hire_start")
										.getString("iso"), true);
						startTime.setIso(start);
						mList.add(startTime);
					} else {
						startTime.setIso("");
						mList.add(startTime);
					}
					if (ob.has("hire_end")) {
						String end = GTMDateUtil.GTMToLocal(
								ob.getJSONObject("hire_end").getString("iso"),
								true);
						endTime.setIso(end);
						list.add(endTime);// 结束时间
					} else {
						endTime.setIso("");
						list.add(endTime);
					}
					double price = array.getJSONObject(i).getDouble("price");
					// 订单号
					String objectId = array.getJSONObject(i).getString(
							"objectId");
					// 租用方式
					String hire_method = array.getJSONObject(i).getString(
							"hire_method");
					JSONObject objStrs = new JSONObject(hire_method);
					String method = objStrs.getString("method");
					// 用户名
					String user = array.getJSONObject(i).getString("user");
					JSONObject objStr = new JSONObject(user);
					if (objStr.has("username")) {
						String username = objStr.getString("username");
						us.setUsername(username);// 用户名
					} else {
						us.setUsername("");
					}
					// 头像
					if (objStr.has("image")) {
						String url = objStr.getJSONObject("image").getString(
								"url");
						Bitmap bitMap = GetImage.getImage(url);
						us.setBitMap(bitMap);
					} else {
						us.setBitMap(null);
					}
					// 电话
					String mobilePhoneNumber = objStr
							.getString("mobilePhoneNumber");
					// 订单状态
					int trade_state = array.getJSONObject(i).getInt(
							"trade_state");
					result.setPrice(price);// 价钱
					result.setObjectId(objectId);// 订单号
					result.setTrade_state(trade_state);// 订单状态
					result.setMethod(method);// 租用方式
					us.setMobilePhoneNumber(mobilePhoneNumber);// 电话
					resultBean.add(result);
					userBeans.add(us);
				}
				Message msg = Message.obtain();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 请求参数
	 */
	private String breakJsonStr(String role) {
		String jsonStr = null;
		try {
			JSONObject obj = new JSONObject();
			obj.put("user_id", mCache.getAsString("user_id"));
			// obj.put("user_id", "567a43d134f81a1d87870d62");
			obj.put("role", role);
			// 3代表所有数据
			obj.put("trade_state", 3);
			obj.put("skip", skip*10);
			obj.put("limit", limit);
			obj.put("mode", "curb");
			jsonStr = JsonStrUtils.JsonStr(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonStr;
	}

	@Override
	public void onRefresh() {
		mHandler.postAtTime(new Runnable() {
			@Override
			public void run() {
				skip = 0;
				refresh();
				onLoad();
			}
		}, 2000);
	}
	@Override
	public void onLoadMore() {
		mHandler.postAtTime(new Runnable() {
			@Override
			public void run() {
				skip++;
				refresh();
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
