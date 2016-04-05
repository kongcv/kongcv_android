package com.kongcv.fragment;

import java.util.ArrayList;

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

import com.google.gson.Gson;
import com.kongcv.R;
import com.kongcv.activity.DetailsActivity;
import com.kongcv.activity.TestActivity;
import com.kongcv.adapter.InfoNotifyAdapter;
import com.kongcv.global.InfoBean;
import com.kongcv.global.Information;
import com.kongcv.global.JpushBean;
import com.kongcv.global.JpushBeanAndInfoBean;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.GTMDateUtil;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.ToastUtil;
import com.kongcv.view.AMapListView;
import com.kongcv.view.AMapListView.AMapListViewListener;

public class MineSendFragment extends Fragment implements AMapListViewListener {
	private AMapListView lv;
	private ArrayList<InfoBean> mList;
	private InfoNotifyAdapter infoAdapter;
	private ACacheUtils mCache;
	
	private View view;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			JpushBeanAndInfoBean jpushBeanAndInfoBean=(JpushBeanAndInfoBean) msg.obj;
			final ArrayList<InfoBean> mLists=jpushBeanAndInfoBean.infoList;
			final JpushBean jpushBean=jpushBeanAndInfoBean.jpushBean;
			
			infoAdapter = new InfoNotifyAdapter(getActivity(), mLists);
			lv.setAdapter(infoAdapter);
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String s = mLists.get(position-1).getState();
					String m = mLists.get(position-1).getMode();
					String p = mLists.get(position-1).getPark_id();
					String f = mLists.get(position - 1).getHire_method_field();
					if("未处理".equals(s)){
						Intent i = new Intent(getActivity(),
								DetailsActivity.class);
						i.putExtra("mode", m);
						i.putExtra("park_id", p);
						i.putExtra("getField", f);
						Gson gson=new Gson();
						String json = gson.toJson(jpushBean);
						i.putExtra("MineSendFragment", json);
						startActivity(i);//未处理 跳转到详情页
					}else if("已接受".equals(s)) {//跳转支付页面
						Intent i = new Intent(getActivity(),
								TestActivity.class);
						Gson gson=new Gson();
						String json = gson.toJson(jpushBean);
						i.putExtra("MineSendFragment", json);
						startActivity(i);
					}else {
						ToastUtil.show(getActivity(), "拒绝了 不做任何处理！");
					}
				}
			});
			onLoad();
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.minereceivefragment, container,
				false);
		mCache = ACacheUtils.get(getActivity());
		initView();
		mList = new ArrayList<InfoBean>();
		getData1();
		return view;
	}
	private void initView() {
		lv = (AMapListView) view.findViewById(R.id.lv);
		lv.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		lv.setAMapListViewListener(this);
	}

	/**
	 * 初始化数据和下拉刷新数据
	 */
	private void getData1() {
		int skip = 0;
		int limit = mList.size() == 0 ? 10 : mList.size();
		mRun(skip, limit);
	}

	/**
	 * 加载更多的数据
	 */
	private void getData2() {
		int skip = mList.size();
		int limit = 10;
		mRun(skip, limit);

	}

	/**
	 * 接口获取我收到的信息
	 */
	protected void mRun(final int skip, final int limit) {
		
		new Thread(new Runnable() {
			private String status;
			@Override
			public void run() {
				JSONObject obj = new JSONObject();
				try {
					obj.put("mobilePhoneNumber", mCache.getAsString("USER"));
					obj.put("action", "send");
					obj.put("skip", skip);// 跳过几条数据
					obj.put("limit", limit);// 限制返回几条数据
					obj.put("mode", "community");
					String doHttpsPost = PostCLientUtils.doHttpsPost(
							Information.KONGCV_GET_PUSHMESSAGE_LIST,
							JsonStrUtils.JsonStr(obj));
					Log.i("我发送的。。", doHttpsPost);
					JSONObject object = new JSONObject(doHttpsPost);
					JSONArray array = object.getJSONArray("result");
					InfoBean bean;
					JpushBean jpushBean = null;
					if(mList!=null){
						mList.clear();
					}
					for (int i = 0; i < array.length(); i++) {
						bean = new InfoBean();
						jpushBean=new JpushBean();
						int state = array.getJSONObject(i)
								.getInt("state");
						String hire_method_id = array.getJSONObject(i)
								.getJSONObject("extras").getString("hire_method_id");
						String park_id = array.getJSONObject(i)
								.getJSONObject("extras").getString("park_id");
						String address = array.getJSONObject(i)
								.getJSONObject("extras").getString("address");
						String own_device_token = array.getJSONObject(i)
								.getJSONObject("extras").getString("own_device_token");
						String own_mobile = array.getJSONObject(i)
								.getJSONObject("extras").getString("own_mobile");
						String hire_end = array.getJSONObject(i)
								.getJSONObject("extras").getString("hire_end");
						String mode = array.getJSONObject(i)
								.getJSONObject("extras").getString("mode");
						String push_type = array.getJSONObject(i)
								.getJSONObject("extras").getString("push_type");
						String hire_method_field = array.getJSONObject(i).getJSONObject("extras")
								.getString("hire_method_field");
						String hire_start = array.getJSONObject(i)
								.getJSONObject("extras").getString("hire_start");
						String own_device_type = array.getJSONObject(i)
								.getJSONObject("extras").getString("own_device_type");
						int price = array.getJSONObject(i)
								.getJSONObject("extras").getInt("price");
						String createdAt = array.getJSONObject(i).getString("createdAt");
						if (state==0) {
							status = "未处理";
						} else if (state==1) {
							status = "已接受";
						} else {
							status = "拒绝";
						}
						jpushBean.setHire_method_id(hire_method_id);
						jpushBean.setPark_id(park_id);
						jpushBean.setAddress(address);
						jpushBean.setOwn_device_token(own_device_token);
						jpushBean.setOwn_mobile(own_mobile);
						jpushBean.setHire_end(hire_end);
						jpushBean.setMode(mode);
						jpushBean.setPush_type(push_type);
						jpushBean.setHire_method_field(hire_method_field);
						jpushBean.setHire_start(hire_start);
						jpushBean.setOwn_device_type(own_device_type);
						jpushBean.setPrice(price);
						
						bean.setState(status);
						bean.setHire_method_id(hire_method_id);
						bean.setPark_id(park_id);
						bean.setAddress(address);
						bean.setHire_start(GTMDateUtil.GTMToLocal(createdAt, true));
						bean.setMode(mode);
						bean.setHire_method_field(hire_method_field);
						
						mList.add(bean);
					}
					JpushBeanAndInfoBean jpushBeanAndInfoBean=new JpushBeanAndInfoBean(jpushBean, mList);
					Message msg = mHandler.obtainMessage();
					msg.what = 0;
					msg.obj = jpushBeanAndInfoBean;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	// 刷新
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				getData1();
				onLoad();
			}
		}, 2000);

	}

	// 加载更多
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
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