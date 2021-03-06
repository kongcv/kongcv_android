package com.kongcv.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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
import com.kongcv.global.ZyCommityAdapterBean;
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

	private AMapListView lv;
	private ACacheUtils mCache;
	private ZyCurbAdapter zydapter;
	private CzCommityAdapter czdapter;
	private View view;
	private double price,money;
	private int trade_state,check_state;
	private List<ZyCommityAdapterBean> beansList;
	private List<ZyCommityAdapterBean> beansList2;
	private String[] str = new String[] { "customer", "hirer" };
	private String method, username, start, end, objectId, hire_method, user,device_type,device_token,
			url, mobilePhoneNumber, park_curb, address,park_id,field;
	private int limit = 10,handsel_state;
	
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				beansList = (List<ZyCommityAdapterBean>) msg.obj;
				if (beansList != null && beansList.size() > 0) {
					zydapter = new ZyCurbAdapter(getActivity(), beansList);
					lv.setAdapter(zydapter);
					zydapter.notifyDataSetChanged();
					lv.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							if (beansList != null
									&& beansList.size() >= position) {
								trade_state = beansList.get(position-1)
										.getTrade_state();
								park_id=beansList.get(position-1).getParkId();
								field=beansList.get(position-1).getField();
								mCommBean=beansList.get(position-1);
								mCommBean.setMode("curb");
								Intent i = new Intent(getActivity(),
										DetailsActivity.class);
								// 传递数据
								i.putExtra("mode", "curb");
								i.putExtra("trade_state", trade_state);
								i.putExtra("park_id", park_id);
								i.putExtra("getField", field);
								i.putExtra("mCommBean", mCommBean);
								startActivity(i);
							}
						}
					});
				}
				break;
			case 1:
				beansList2 = (ArrayList<ZyCommityAdapterBean>) msg.obj;
				if (beansList2 != null && beansList2.size() > 0) {
					czdapter = new CzCommityAdapter(getActivity(), beansList2);
					lv.setAdapter(czdapter);
					czdapter.notifyDataSetChanged();
					lv.setOnItemClickListener(null);
				}
				break;
			case 2:
				beansList.clear();
				zydapter.notifyDataSetChanged();
				lv.setOnItemClickListener(null);
				break;
			case 3:
				beansList2.clear();
				czdapter.notifyDataSetChanged();
				lv.setOnItemClickListener(null);
				break;
			default:
				break;
			}
		};
	};
	MineOrdermanagerActivity activity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.curbfragment, container, false);
		activity=(MineOrdermanagerActivity) getActivity();
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
		ReadType readType = new ReadType();
		readType.execute();
	}
	/**
	 * 网络请求
	 */
	class ReadType extends PreReadTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (MineOrdermanagerActivity.TYPEORDER == 0) {
				getCurbOrCommInfo(breakJsonStr(str[0]), 0);
			} else {
				getCurbOrCommInfo(breakJsonStr(str[1]), 1);
			}
			return null;
		}
	}
	private void getCurbOrCommInfo(String jsonStr, final int i) {
		// TODO Auto-generated method stub
		try {
			String doHttpsPost = PostCLientUtils.doHttpsPost(Information.KONGCV_GET_TRADE_LIST, jsonStr);
			if (i == 0) {
				doResponse(doHttpsPost);
			} else {
				doResponse2(doHttpsPost);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*okhttp3.Request request = new okhttp3.Request.Builder()
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
				Log.e("KONGCV_GET_TRADE_LIST", arg1.toString());
			}
		});*/
	}
	private ZyCommityAdapterBean mCommBean = null;
	private void doResponse(String string) {
		// TODO Auto-generated method stub
		try {
			Log.d("doResponse", string+"");
			JSONObject object = new JSONObject(string);
			JSONArray array = object.getJSONArray("result");
			if (array != null && array.length() > 0) {
				beansList = new ArrayList<ZyCommityAdapterBean>();
				for (int i = 0; i < array.length(); i++) {
					mCommBean = new ZyCommityAdapterBean();
					// 开始时间
					JSONObject ob = array.getJSONObject(i);
					if (ob.has("hire_start")) {
						start = GTMDateUtil
								.GTMToLocal(ob.getJSONObject("hire_start")
										.getString("iso"), true);
						mCommBean.setHire_start(start);
					}
					// 结束时间
					if (ob.has("hire_end")) {
						end = GTMDateUtil.GTMToLocal(
								ob.getJSONObject("hire_end").getString("iso"),
								true);
						mCommBean.setHire_end(end);
					}
					handsel_state=array.getJSONObject(i).getInt("handsel_state");//0没有定金1支付差额"trade_state": 1,
					// 价钱
					money = array.getJSONObject(i).getDouble("money");
					check_state = array.getJSONObject(i).getInt("check_state");
					price = array.getJSONObject(i).getDouble("price");
					// 订单号
					objectId = array.getJSONObject(i).getString("objectId");
					// 租用人的地址
					park_curb = array.getJSONObject(i).getString("park_curb");
					JSONObject objStr = new JSONObject(park_curb);
					address = objStr.getString("address");
					park_id = objStr.getString("objectId");
					// 租用方式
					hire_method = array.getJSONObject(i).getString(
							"hire_method");
					JSONObject objStrs = new JSONObject(hire_method);
					method = objStrs.getString("method");
					field = objStrs.getString("field");
					mCommBean.setField(field);
					// 订单状态
					trade_state = array.getJSONObject(i).getInt("trade_state");
					String hirer = array.getJSONObject(i).getString("hirer");
					JSONObject hirerObj=new JSONObject(hirer);
					mobilePhoneNumber = hirerObj.getString("mobilePhoneNumber");
					device_type = hirerObj.getString("device_type");
					device_token = hirerObj.getString("device_token");
					mCommBean.setDevice_token(device_token);
					mCommBean.setDevice_type(device_type);
					
					mCommBean.setMoney(money);
					mCommBean.setCheck_state(check_state);
					mCommBean.setPrice(price);
					mCommBean.setObjectId(objectId);
					mCommBean.setPark_curb(park_curb);
					mCommBean.setAddress(address);
					mCommBean.setMethod(method);
					mCommBean.setTrade_state(trade_state);
					mCommBean.setParkId(park_id);
					mCommBean.setHandsel_state(handsel_state);
					mCommBean.setMobilePhoneNumber(mobilePhoneNumber);
					mCommBean.setMode("curb");
					beansList.add(mCommBean);
				}
				Message msg = mHandler.obtainMessage();
				msg.what = 0;
				msg.obj = beansList;
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
				beansList2 = new ArrayList<ZyCommityAdapterBean>();
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
						field = objStrs.getString("field");
						mCommBean.setMethod(method);
						mCommBean.setField(field);
					}
					// 用户名
					user = array.getJSONObject(i).getString("user");
					JSONObject objStr = new JSONObject(user);
					if (objStr.has("username")) {
						username = objStr.getString("username");// 用户名
						mCommBean.setUsername(username);
					}
					// 头像
					if (objStr.has("image")) {
						url = objStr.getJSONObject("image").getString("url");
						Bitmap httpBitmap = GetImage.getHttpBitmap(url);
						// Bitmap bitMap = GetImage.getImage(url);
						mCommBean.setBitmap(httpBitmap);
						mCommBean.setImage(url);
					}
					
					// 电话
					mobilePhoneNumber = objStr.getString("mobilePhoneNumber");
					// 订单状态
					trade_state = array.getJSONObject(i).getInt("trade_state");
					mCommBean.setMobilePhoneNumber(mobilePhoneNumber);
					mCommBean.setPrice(price);
					mCommBean.setObjectId(objectId);
					mCommBean.setTrade_state(trade_state);
					mCommBean.setMode("curb");
					beansList2.add(mCommBean);
				}
				Message msg = mHandler.obtainMessage();
				msg.what = 1;
				msg.obj = beansList2;
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
			obj.put("role", role);
			// 3代表所有数据
			obj.put("trade_state", 3);
			obj.put("skip", skip * 10);
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

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
