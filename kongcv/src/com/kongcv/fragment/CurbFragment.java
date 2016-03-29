package com.kongcv.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
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
	private AMapListView lv;
	private List<HireStartEntity> mList;
	private List<HireEndEntity> list;
	private List<ResultEntity> resultBean;
	private List<UserList> userBeans;
	private ZyCurbAdapter zydapter;
	private CzCommityAdapter czdapter;
	// private ProgressDialog pro = null;
	public int TAG = 1;
	HireStartEntity startTime;
	HireEndEntity endTime;
	ResultEntity result;
	UserList us;
	private ACacheUtils mCache;
	private Handler mHandler;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				zydapter = new ZyCurbAdapter(getActivity(), mList, list,
						resultBean);
				lv.setAdapter(zydapter);
				// pro.dismiss();
				// mAdapter.notifyDataSetChanged();
				lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if(resultBean!=null && resultBean.size()>position){
							int state = resultBean.get(position).getTrade_state();
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
				// pro.dismiss();
				// mAdapter.notifyDataSetChanged();
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
		View view = inflater.inflate(R.layout.curbfragment, container, false);
		lv = (AMapListView) view.findViewById(R.id.lv);
		// pro = ProgressDialog.show(getActivity(), "", "正在获取获取数据，请稍候");
		mCache = ACacheUtils.get(getActivity());
		mList = new ArrayList<HireStartEntity>();
		list = new ArrayList<HireEndEntity>();
		resultBean = new ArrayList<ResultEntity>();
		userBeans = new ArrayList<UserList>();
		startTime = new HireStartEntity();
		endTime = new HireEndEntity();
		result = new ResultEntity();
		us = new UserList();
		initView();
		getData1();
		return view;

	}

	private void initView() {
		mHandler = new Handler();
		lv.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		lv.setAMapListViewListener(this);
	}

	/**
	 * 初始化数据和下拉刷新数据
	 */
	private int skip=0;
	public void getData1() {
		
		if (((MineOrdermanagerActivity) getActivity()).TYPEORDER == 0) {
			initData1(skip, 10);
		} else {
			initData2(skip, 10);
		}
	}
	class ReadType extends PreReadTask<Integer, Void, Void> {
		@Override
		protected Void doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			doInitData1(params[0]);
			return null;
		}
	}
	private void doInitData1(Integer integer) {
		// TODO Auto-generated method stub
		try {
			JSONObject obj = new JSONObject();
		    obj.put("user_id", mCache.getAsString("user_id"));
	//		obj.put("user_id", "567a43d134f81a1d87870d62");
			// 判断是租用订单还是出租订单
			obj.put("role", "customer");
			// 3代表所有数据
			obj.put("trade_state", 3);
			obj.put("skip", integer);
			obj.put("limit", 10);
			obj.put("mode", "curb");
			String doHttpsPost = PostCLientUtils.doHttpsPost(
					Information.KONGCV_GET_TRADE_LIST,
					JsonStrUtils.JsonStr(obj));
			Log.i("doHttpsPostOrderqqqqqqqq", doHttpsPost);
			JSONObject object = new JSONObject(doHttpsPost);
			JSONArray array = object.getJSONArray("result");
			for (int i = 0; i < array.length(); i++) {
				// 开始时间
				JSONObject ob = array.getJSONObject(i);
				if (ob.has("hire_start")) {
					Log.v("ssss", ob.has("hire_start") + "");
					String start = GTMDateUtil.GTMToLocal(
							ob.getJSONObject("hire_start").getString(
									"iso"), true);
					startTime.setIso(start);
					mList.add(startTime);
				} else {
					startTime.setIso("");
					mList.add(startTime);
				}
				// 结束时间
				if (ob.has("hire_end")) {
					String end = GTMDateUtil.GTMToLocal(
							ob.getJSONObject("hire_end").getString(
									"iso"), true);
					endTime.setIso(end);
					list.add(endTime);// 结束时间
				} else {
					Log.v("ssss", ob.has("hire_start") + "");
					endTime.setIso("");
					list.add(endTime);
				}
				// 价钱
				double price =array.getJSONObject(i).getDouble("price");
				
				// 订单号
				String objectId = array.getJSONObject(i).getString(
						"objectId");
				// 租用人的地址
				String park_curb = array.getJSONObject(i).getString(
						"park_curb");
				JSONObject objStr = new JSONObject(park_curb);
				String address = objStr.getString("address");
				// 租用方式
				String hire_method = array.getJSONObject(i).getString(
						"hire_method");
				JSONObject objStrs = new JSONObject(hire_method);
				String method = objStrs.getString("method");
				// 订单状态
				int trade_state = array.getJSONObject(i).getInt(
						"trade_state");
				result.setPrice(price);// 价钱
				result.setObjectId(objectId);// 订单号
				result.setTrade_state(trade_state);// 订单状态
				result.setPark_curb(address); // 租用人的地址
				result.setMethod(method);// 租用方式
				resultBean.add(result);
			}
			Message msg = Message.obtain();
			msg.what = 0;
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 加载更多的数据
	 */
	public void getData2() {
		int skip = resultBean.size();
		int limit = 10;
		if (((MineOrdermanagerActivity) getActivity()).TYPEORDER == 0) {
			initData1(skip, limit);
		} else {
			initData2(skip, limit);
		}
	}

	/**
	 * 接口获取数据，默认是租用下道路的数据
	 */
	public void initData1(final int skip, final int limit) {
		mList.clear();
		list.clear();
		resultBean.clear();
		if (zydapter != null) {
			zydapter.notifyDataSetChanged();
		}
		ReadType readType=new ReadType();
		readType.execute(skip);
	}
	
	/**
	 * 出租下道路的数据
	 */
	public void initData2(final int skip, final int limit) {
		mList.clear();
		list.clear();
		resultBean.clear();
		userBeans.clear();
		if (czdapter != null) {
			czdapter.notifyDataSetChanged();
		}
		ReadType readType=new ReadType();
		readType.execute(skip);
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject obj = new JSONObject();
				try {
				// obj.put("user_id", mCache.getAsString("user_id"));
					obj.put("user_id", "567a43d134f81a1d87870d62");
					obj.put("role", "hirer");
					// 需要判断订单类型
					obj.put("trade_state", 3);
					obj.put("skip", skip);
					obj.put("limit", limit);
					obj.put("mode", "curb");
					Log.i("objorder", obj.toString());
					String doHttpsPost = PostCLientUtils.doHttpsPost(
							Information.KONGCV_GET_TRADE_LIST,
							JsonStrUtils.JsonStr(obj));
					Log.i("doHttpsPostOrder", doHttpsPost);

					JSONObject object = new JSONObject(doHttpsPost);
					JSONArray array = object.getJSONArray("result");
					for (int i = 0; i < array.length(); i++) {
						JSONObject ob = array.getJSONObject(i);
						if (ob.has("hire_start")) {
							Log.v("ssss", ob.has("hire_start") + "");
							String start = GTMDateUtil.GTMToLocal(
									ob.getJSONObject("hire_start").getString(
											"iso"), true);
							startTime.setIso(start);
							mList.add(startTime);
						} else {
							Log.e("第条开始时间："+i, ob+"");
							startTime.setIso("");
							mList.add(startTime);
						}
						if (ob.has("hire_end")) {
							String end = GTMDateUtil.GTMToLocal(
									ob.getJSONObject("hire_end").getString(
											"iso"), true);
							endTime.setIso(end);
							list.add(endTime);// 结束时间
						} else {
							Log.e("第条结束时间："+i, ob+"");
							endTime.setIso("");
							list.add(endTime);
						}
						// 价钱
				//		int price = array.getJSONObject(i).getInt("price");
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
							String url = objStr.getJSONObject("image")
									.getString("url");
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
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();*/
	}

	@Override
	public void onRefresh() {
		mHandler.postAtTime(new Runnable() {
			@Override
			public void run() {
				getData1();
				onLoad();
			}
		}, 2000);

	}

	@Override
	public void onLoadMore() {
		mHandler.postAtTime(new Runnable() {
			@Override
			public void run() {
				getData2();
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
