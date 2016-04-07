package com.kongcv.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.kongcv.activity.DetailsActivity;
import com.kongcv.activity.MineOrdermanagerActivity;
import com.kongcv.adapter.CzCommityAdapter;
import com.kongcv.adapter.ZyCommityAdapter;
import com.kongcv.global.Information;
import com.kongcv.global.ZyCommityAdapterBean;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.GTMDateUtil;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.view.AMapListView;
import com.kongcv.view.AMapListView.AMapListViewListener;

/**
 * 社区的订单(通过判断是租用的，还是出租的)
 * @author kcw
 */
public class CommityFragment extends Fragment implements AMapListViewListener {

	private ZyCommityAdapter zydapter;
	private CzCommityAdapter czdapter;
	private AMapListView lv;
	private ACacheUtils mCache;
	private String[] str = new String[] { "customer", "hirer" };
	private int skip = 0, limit = 10, trade_state;
	private double price;
	private List<ZyCommityAdapterBean> beansList;
	private String method, username, start, end, objectId, hire_method, user,
			url, mobilePhoneNumber, parkId, field, address,device_type,device_token;
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				beansList = (ArrayList<ZyCommityAdapterBean>) msg.obj;
				if(beansList!=null && beansList.size()>0){
					zydapter = new ZyCommityAdapter(getActivity(), beansList);
					lv.setAdapter(zydapter);
					lv.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							if(beansList!=null && beansList.size()>0 && position>=1){
								trade_state = beansList.get(position - 1)
										.getTrade_state();
								field = beansList.get(position - 1).getField();
								parkId = beansList.get(position - 1).getParkId();
								mCommBean=beansList.get(position-1);
								mCommBean.setMode("community");
								Intent i = new Intent(getActivity(),
										DetailsActivity.class);
								// 传递数据
								i.putExtra("mode", "community");
								i.putExtra("trade_state", trade_state);
								i.putExtra("park_id", parkId);
								i.putExtra("getField", field);
								i.putExtra("mCommBean", mCommBean);
								startActivity(i);
							}
						}
					});
				}
				break;
			case 1:
				beansList = (List<ZyCommityAdapterBean>) msg.obj;
				if(beansList!=null && beansList.size()>0){
					czdapter = new CzCommityAdapter(getActivity(), beansList);
					lv.setAdapter(czdapter);
					lv.setOnItemClickListener(null);
				}
				break;
			default:
				break;
			}
		};
	};

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.commityfragment, container, false);
		initView();
		refresh();
		return view;
	}

	private void initView() {
		lv = (AMapListView) view.findViewById(R.id.lv);
		mCache = ACacheUtils.get(getActivity());
		lv.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		lv.setAMapListViewListener(this);
	}

	/**
	 * 初始化数据和下拉刷新数据
	 */
	public void refresh() {
		if (MineOrdermanagerActivity.TYPEORDER == 0) {
			postHttp(info(str[0]), 0);
		} else {
			postHttp(info(str[1]), 1);
		}
	}

	/**
	 * 参数
	 * 
	 * @param skipNum
	 * @param limitNum
	 * @return
	 */
	private String info(String role) {
		String jsonStr = null;
		try {
			JSONObject params = new JSONObject();
			params.put("user_id", mCache.getAsString("user_id"));
			params.put("role", role);
			params.put("trade_state", 3);
			params.put("skip", skip * 10);
			params.put("limit", limit);
			params.put("mode", "community");
			jsonStr = JsonStrUtils.JsonStr(params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonStr;
	}

	private final OkHttpClient client = new OkHttpClient();
	private void postHttp(String json, final int i) {
		okhttp3.Request request = new okhttp3.Request.Builder()
				.url(Information.KONGCV_GET_TRADE_LIST)
				.headers(Information.getHeaders())
				.post(RequestBody.create(Information.MEDIA_TYPE_MARKDOWN, json))
				.build();
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
				Log.e("KONGCV_GET_TRADE_LIST", arg1.toString());
			}
		});
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				skip = 0;
				refresh();
				onLoad();
			}
		}, 2000);

	}
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
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

	private void doResponse2(String string) {
		// TODO Auto-generated method stub
		try {
			JSONObject object = new JSONObject(string);
			JSONArray array = object.getJSONArray("result");
			if (array != null && array.length() > 0) {
				beansList = new ArrayList<ZyCommityAdapterBean>();
				ZyCommityAdapterBean mCommBean = null;
				for (int i = 0; i < array.length(); i++) {
					mCommBean = new ZyCommityAdapterBean();
					JSONObject ob = array.getJSONObject(i);
					if (ob.has("hire_start")) {
						start = GTMDateUtil
								.GTMToLocal(ob.getJSONObject("hire_start")
										.getString("iso"), true);
						mCommBean.setHire_start(start);
					}
					if (ob.has("hire_end")) {
						end = GTMDateUtil.GTMToLocal(
								ob.getJSONObject("hire_end").getString("iso"),
								true);
						mCommBean.setHire_end(end);
					}
					price = array.getJSONObject(i).getDouble("price");
					// 订单号
					objectId = array.getJSONObject(i).getString("objectId");
					// 租用方式
					if (array.getJSONObject(i).has("hire_method")) {
						hire_method = array.getJSONObject(i).getString(
								"hire_method");
						JSONObject objStrs = new JSONObject(hire_method);
						method = objStrs.getString("method");
						mCommBean.setMethod(method);
					}
					if (array.getJSONObject(i).has("user")) {
						// 用户名
						user = array.getJSONObject(i).getString("user");
						JSONObject objStr = new JSONObject(user);
						if (objStr.has("username")) {
							username = objStr.getString("username");
							mCommBean.setUsername(username);
						}
						// 头像
						if (objStr.has("image")) {
							url = objStr.getJSONObject("image")
									.getString("url");
							Bitmap httpBitmap = GetImage.getHttpBitmap(url);
							mCommBean.setBitmap(httpBitmap);
							mCommBean.setImage(url);
						}
						// 电话
						mobilePhoneNumber = objStr
								.getString("mobilePhoneNumber");
						mCommBean.setMobilePhoneNumber(mobilePhoneNumber);
					}
					// 订单状态
					trade_state = array.getJSONObject(i).getInt("trade_state");
					mCommBean.setPrice(price);
					mCommBean.setObjectId(objectId);
					mCommBean.setTrade_state(trade_state);
					mCommBean.setMode("community");
					beansList.add(mCommBean);
				}
				Message msg = Message.obtain();
				msg.what = 1;
				msg.obj = beansList;
				mHandler.sendMessage(msg);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private double money;
	private int handsel_state;
	private ZyCommityAdapterBean mCommBean = null;
	private void doResponse(String string) {
		// TODO Auto-generated method stub
		try {
			JSONObject object = new JSONObject(string);
			JSONArray array = object.getJSONArray("result");
			if (array != null && array.length() > 0) {
				beansList = new ArrayList<ZyCommityAdapterBean>();
				for (int i = 0; i < array.length(); i++) {
					mCommBean = new ZyCommityAdapterBean();
					// 开始时间
					JSONObject ob = array.getJSONObject(i);
					money = ob.getDouble("money");
					handsel_state=ob.getInt("handsel_state");
					mCommBean.setMoney(money);
					mCommBean.setHandsel_state(handsel_state);
					if (ob.has("hire_start")) {
						start = GTMDateUtil.GTMToLocal(array.getJSONObject(i)
								.getJSONObject("hire_start").getString("iso"),
								true);
						mCommBean.setHire_start(start);// "0000-00-00 00:00:00"
					}
					// 结束时间
					if (ob.has("hire_end")) {
						end = GTMDateUtil.GTMToLocal(array.getJSONObject(i)
								.getJSONObject("hire_end").getString("iso"),
								true);
						mCommBean.setHire_end(end);
					}
					// 价钱
					price = array.getJSONObject(i).getDouble("price");
					// 订单号
					objectId = array.getJSONObject(i).getJSONObject("user")
							.getString("objectId");
					// 租用人的地址
					if (array.getJSONObject(i).has("park_community")) {
						String park_community = array.getJSONObject(i)
								.getString("park_community");//
						JSONObject objStr = new JSONObject(park_community);
						address = objStr.getString("address");
						parkId = objStr.getString("objectId");
						mCommBean.setParkId(parkId);
					}
					if (array.getJSONObject(i).has("hire_method")) {
						hire_method = array.getJSONObject(i).getString(
								"hire_method");
						JSONObject objStrs = new JSONObject(hire_method);
						method = objStrs.getString("method");
						field = objStrs.getString("field");
						mCommBean.setMethod(method);
						mCommBean.setField(field);
					}
					if(array.getJSONObject(i).has("hirer")){
						String hirer=array.getJSONObject(i).getString("hirer");
						JSONObject hirerObj=new JSONObject(hirer);
						device_type=hirerObj.getString("device_type");
						device_token=hirerObj.getString("device_token");
						mobilePhoneNumber=hirerObj.getString("mobilePhoneNumber");
						mCommBean.setDevice_type(device_type);
						mCommBean.setDevice_token(device_token);
						mCommBean.setMobilePhoneNumber(mobilePhoneNumber);
					}
					// 订单状态
					trade_state = array.getJSONObject(i).getInt("trade_state");
					if (address != null)
					mCommBean.setAddress(address);
					mCommBean.setPrice(price);
					mCommBean.setObjectId(objectId);
					mCommBean.setTrade_state(trade_state);
					mCommBean.setMode("community");
					
					beansList.add(mCommBean);
				}
				Message msg = Message.obtain();
				msg.what = 0;
				msg.obj = beansList;
				mHandler.sendMessage(msg);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}
