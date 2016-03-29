package com.kongcv.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
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
import com.kongcv.adapter.ZyCommityAdapter;
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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * 社区的订单(通过判断是租用的，还是出租的)
 * @author kcw
 */
public class CommityFragment extends Fragment implements AMapListViewListener {
	
	private AMapListView lv;
	private List<HireStartEntity> mList;
	private List<HireEndEntity> list;
	private List<ResultEntity> resultBean;
	private List<UserList> userBeans;
	private ZyCommityAdapter zydapter;
	private CzCommityAdapter czdapter;
	HireStartEntity startTime;
	HireEndEntity endTime;
	ResultEntity result;
	UserList us;
	private ACacheUtils mCache;
	private Handler mHandler;
	private List<String> fieldList;
	private List<String> parkList;
	
	// private ProgressDialog pro = null;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				final FieldAndPark fieldAndPark=(FieldAndPark) msg.obj;
				zydapter = new ZyCommityAdapter(getActivity(), mList, list,resultBean);
				lv.setAdapter(zydapter);
				// pro.dismiss();
				// mAdapter.notifyDataSetChanged();
				lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
				      	int state = resultBean.get(position-1).getTrade_state();
				      	String field = fieldAndPark.fieldList.get(position-1);
				      	String park_id = fieldAndPark.parkList.get(position-1);
						if (0==state) {
							Intent i = new Intent(getActivity(),DetailsActivity.class);
							// 传递数据
							i.putExtra("mode", "community");
							i.putExtra("trade_state", state);
							i.putExtra("park_id", park_id);
							i.putExtra("getField", field);
							startActivity(i);
						}else if(1==state){
							Intent i = new Intent(getActivity(),DetailsActivity.class);
							// 传递数据
							i.putExtra("mode", "community");
							i.putExtra("trade_state", state);
							i.putExtra("park_id", park_id);
							i.putExtra("getField", field);
							startActivity(i);
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
						int state = resultBean.get(position-1).getTrade_state();
						if (0==state) {
							Intent i = new Intent(getActivity(),
									DetailsActivity.class);
							// 传递数据
							i.putExtra("mode", "community");
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
		View view = inflater
				.inflate(R.layout.commityfragment, container, false);
		lv =  (AMapListView) view.findViewById(R.id.lv);
		// pro = ProgressDialog.show(getActivity(), "", "正在获取获取数据，请稍候");
		mCache = ACacheUtils.get(getActivity());
		init();
		return view;
	}
	
	private void init() {
		// TODO Auto-generated method stub
		/**
		 * 废代码
		 */
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
	private int limit=10;
	public void getData1() {
		if (((MineOrdermanagerActivity) getActivity()).TYPEORDER == 0) {
			initData1(skip, limit);
		} else {
			initData2(skip, limit);
		}
	}
	
	/**
	 * 加载更多的数据
	 */
	public void getData2() {
		if (((MineOrdermanagerActivity) getActivity()).TYPEORDER == 0) {
			initData1(skip, limit);
		} else {
			initData2(skip, limit);
		}
	}
	
	
	/**
	 * 接口获取数据,获取的是租用下社区的订单
	 */
	public void initData1(int skipNum,int limitNum) {
		postHttp(data1(skipNum, limitNum),skipNum);
	}
	/**
	 * 参数
	 * @param skipNum
	 * @param limitNum
	 * @return
	 */
	private String data1(int skipNum, final int limitNum){
		String jsonStr=null;
		try {
			if (zydapter != null) {
				mList.clear();
				list.clear();
				resultBean.clear();
				zydapter.notifyDataSetChanged();
			}
			JSONObject params=new JSONObject();
//			params.put("user_id", mCache.getAsString("user_id"));
			params.put("user_id", "567a43d134f81a1d87870d62");
			params.put("role", "customer");	
			params.put("trade_state", 3);	
			params.put("skip", skipNum);	
			params.put("limit", limitNum); 
			params.put("mode", "community");
			jsonStr = JsonStrUtils.JsonStr(params);
		//	doInfo(jsonStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonStr;
	}
	private final OkHttpClient client = new OkHttpClient();
	private void postHttp(String json, final int skipNum){
		Log.d("postHttp传递的参数是>>>", json);
		Log.d("postHttp传递的参数是>>>", json);
		Log.d("postHttp传递的参数是>>>", json);
		Log.d("postHttp传递的参数是>>>", json);
		Log.d("postHttp传递的参数是>>>", json);
		okhttp3.Request request=new okhttp3.Request.Builder()
		  .url(Information.KONGCV_GET_TRADE_LIST)
		  .headers(Information.getHeaders())
	      .post(RequestBody.create(Information.MEDIA_TYPE_MARKDOWN, json))
	      .build();
		client.newCall(request).enqueue(new okhttp3.Callback() {
	
			@Override
			public void onResponse(Call arg0, okhttp3.Response response) throws IOException {
				// TODO Auto-generated method stub
				if(response.isSuccessful()){
					doResponse(response.body().string(),skipNum);
				}
			}
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				Log.e("KONGCV_GET_TRADE_LIST", arg1.toString());
			}
		});
	}
	/**
	 * 接口获取数据,获取的是出租下社区的订单
	 */
	public void initData2(int skip,int limit) {
		if (czdapter != null) {
			mList.clear();
			list.clear();
			resultBean.clear();
			userBeans.clear();
			czdapter.notifyDataSetChanged();
		}
		doHtttpRequest(data2(skip,limit));
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject obj = new JSONObject();
				try {
			//	    obj.put("user_id", mCache.getAsString("user_id"));
					obj.put("user_id", mCache.getAsString("user_id"));
			//		obj.put("user_id", "567a43d134f81a1d87870d62");
					obj.put("role", "hirer");
					// 需要判断订单类型
					obj.put("trade_state", 3);
					obj.put("skip", skip);
					obj.put("limit", limit);
					obj.put("mode", "community");
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
							String start = GTMDateUtil.GTMToLocal(
									ob.getJSONObject("hire_start").getString(
											"iso"), true);
							startTime.setIso(start);
							mList.add(startTime);
						} else {
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
							endTime.setIso("");
							list.add(endTime);
						}
						// 价格
						int price = array.getJSONObject(i).getInt("price");
						// 订单号
						String objectId = array.getJSONObject(i).getString(
								"objectId");
						// 租用方式
						String hire_method = array.getJSONObject(i).getString(
								"hire_method");
						JSONObject objStrs = new JSONObject(hire_method);
						String method = objStrs.getString("method");

						if(array.getJSONObject(i).has("user")){
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
								String url = objStr.getJSONObject("image").getString("url");
								Bitmap bitMap = GetImage.getImage(url);
								us.setBitMap(bitMap);
							} else {
								us.setBitMap(null);
							}
							// 电话
							String mobilePhoneNumber = objStr
									.getString("mobilePhoneNumber");
							Log.v("mobilePhoneNumber", mobilePhoneNumber);
							us.setMobilePhoneNumber(mobilePhoneNumber);// 电话
						}
						// 订单状态
						int trade_state = array.getJSONObject(i).getInt(
								"trade_state");
						result.setPrice(price);// 价钱
						result.setObjectId(objectId);// 订单号
						result.setTrade_state(trade_state);// 订单状态
						result.setMethod(method);// 租用方式
						
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
	private void doHtttpRequest(String data2) {
		// TODO Auto-generated method stub
		Log.d("doHtttpRequest data2>>>><<<<",data2+":data2");
		Log.d("doHtttpRequest data2>>>><<<<",data2+":data2");
		Log.d("doHtttpRequest data2>>>><<<<",data2+":data2");
		Log.d("doHtttpRequest data2>>>><<<<",data2+":data2");
		
		okhttp3.Request request=new okhttp3.Request.Builder()
		  .url(Information.KONGCV_GET_TRADE_LIST)
		  .headers(Information.getHeaders())
	      .post(RequestBody.create(Information.MEDIA_TYPE_MARKDOWN, data2))
	      .build();
		client.newCall(request).enqueue(new okhttp3.Callback() {
	
			@Override
			public void onResponse(Call arg0, okhttp3.Response response) throws IOException {
				// TODO Auto-generated method stub
				if(response.isSuccessful()){
					
				}
			}
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				Log.e("KONGCV_GET_TRADE_LIST", arg1.toString());
			}
		});
	}

	private String data2(int skip2, int limit2) {
		// TODO Auto-generated method stub
		String data2=null;
		try {
			JSONObject obj=new JSONObject();
			//	obj.put("user_id", mCache.getAsString("user_id"));
			obj.put("user_id", "567a43d134f81a1d87870d62");
			obj.put("role", "hirer");
			// 需要判断订单类型
			obj.put("trade_state", 3);
			obj.put("skip", skip);
			obj.put("limit", limit);
			obj.put("mode", "community");
			
			data2=JsonStrUtils.JsonStr(obj);
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    }
		return data2;
	}

	@Override
	public void onRefresh() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getData1();
				onLoad();
			}
		});
		
	}
	@Override
	public void onLoadMore() {
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getData2();
				onLoad();
			}
		});
	}
		/**
		 * 停止刷新
		 */
		private void onLoad() {
			lv.stopRefresh();
			lv.stopLoadMore();
			lv.setRefreshTime("刚刚");
		}
		class FieldAndPark {
			List<String> fieldList;
			List<String> parkList;
			public FieldAndPark(List<String> field,List<String> park){
				this.fieldList=field;
				this.parkList=park;
			}
		}
		private void doResponse(String string,int skipNum) {
			// TODO Auto-generated method stub
			try {
				JSONObject object = new JSONObject(string);
				JSONArray array = object.getJSONArray("result");
				String address=null;
				String method=null;
				fieldList=new ArrayList<String>();
				parkList=new ArrayList<String>();
				for (int i = 0; i < array.length(); i++) {
					// 开始时间
					JSONObject ob = array.getJSONObject(i);
					if (ob.has("hire_start")) {
						String start = GTMDateUtil.GTMToLocal(
								array.getJSONObject(i)
										.getJSONObject("hire_start")
										.getString("iso"), true);
						startTime.setIso(start);
						mList.add(startTime);
					} else {
						Log.e("第条开始时间："+i, ob+"");
						startTime.setIso("0000-00-00 00:00:00");
						mList.add(startTime);
					}
					// 结束时间
					if (ob.has("hire_end")) {
						String end = GTMDateUtil.GTMToLocal(array
								.getJSONObject(i).getJSONObject("hire_end")
								.getString("iso"), true);
						endTime.setIso(end);
						list.add(endTime);
					} else {
						Log.e("第条结束时间："+i, ob+"");
						endTime.setIso("0000-00-00 00:00:00");
						list.add(endTime);
					}
					// 价钱
					double price = array.getJSONObject(i).getDouble("price");
					// 订单号
					String objectId = array.getJSONObject(i).getJSONObject("user").getString(
							"objectId");
					// 租用人的地址
					if(array.getJSONObject(i).has("park_community")){
						String park_community = array.getJSONObject(i)
								.getString("park_community");//
						JSONObject objStr = new JSONObject(park_community);
						address = objStr.getString("address");
						
						String parkId = objStr.getString(
								"objectId");
						parkList.add(parkId);
					}
					// 租用方式
					/*if(!array.getJSONObject(i).has("hire_method")){
						Log.e("第几条没有hire_method字段", "i=i+_"+i);
					}else{
						String hire_method = array.getJSONObject(i).getString(
								"hire_method");
						JSONObject objStrs = new JSONObject(hire_method);
						method= objStrs.getString("method");
						result.setMethod(method);//租用方式
						
						String field = objStrs.getString("field");
						fieldList.add(field);
					}*/
					if(array.getJSONObject(i).has("hire_method")){
						String hire_method = array.getJSONObject(i).getString(
								"hire_method");
						JSONObject objStrs = new JSONObject(hire_method);
						method= objStrs.getString("method");
						result.setMethod(method);//租用方式
						
						String field = objStrs.getString("field");
						fieldList.add(field);
					}
					// 订单状态
					int trade_state = array.getJSONObject(i).getInt(
							"trade_state");
					result.setPrice(price);// 价钱
					
					result.setObjectId(objectId);// 订单号
					result.setTrade_state(trade_state);// 订单状态
					if(address!=null)
					result.setPark_community(address); // 租用人的地址
					resultBean.add(result);
				}
				FieldAndPark fieldAndPark=new FieldAndPark(fieldList, parkList);
				Message msg = Message.obtain();
				msg.what = 0;
				msg.obj=fieldAndPark;
				handler.sendMessage(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
}



























