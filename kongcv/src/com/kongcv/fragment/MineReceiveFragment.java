package com.kongcv.fragment;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

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
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.kongcv.R;
import com.kongcv.UI.AsyncImageLoader.PreReadTask;
import com.kongcv.activity.DetailsActivity;
import com.kongcv.activity.MineInformationActivity;
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

public class MineReceiveFragment extends Fragment implements
		AMapListViewListener {
	private AMapListView lv;
	private View view;
	private ArrayList<InfoBean> mList;
	private ArrayList<JpushBean> jpushBeans;
	private InfoNotifyAdapter infoAdapter;
	private ACacheUtils mCache;
	private String status;
	private MineInformationActivity infoActivity;
	Gson gson = new Gson();
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			JpushBeanAndInfoBean jpushBeanAndInfoBean = (JpushBeanAndInfoBean) msg.obj;
			final ArrayList<InfoBean> mLists = jpushBeanAndInfoBean.infoList;
			final ArrayList<JpushBean> mListJpush = jpushBeanAndInfoBean.jpushBean;
			infoAdapter = new InfoNotifyAdapter(getActivity(), mLists);
			lv.setAdapter(infoAdapter);
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if (position != 0) {
						String s = mLists.get(position - 1).getState();
						String m = mLists.get(position - 1).getMode();
						String p = mLists.get(position - 1).getPark_id();
						String f = mLists.get(position - 1)
								.getHire_method_field();
						// 根据状态判断是否跳转
						if ("未处理".equals(s)) {// 状态
							Intent i = new Intent(getActivity(),
									DetailsActivity.class);
							i.putExtra("mode", m);
							i.putExtra("park_id", p);
							i.putExtra("getField", f);
							String json = gson.toJson(mListJpush
									.get(position - 1));
							i.putExtra("CurbMineReceiver", json);
							startActivity(i);
						}
					}
				}
			});
			onLoad();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.minereceivefragment, container, false);
		infoActivity = (MineInformationActivity) getActivity();
		mCache = ACacheUtils.get(getActivity());
		initView();
		refresh();
		return view;
	}

	private void initView() {
		lv = (AMapListView) view.findViewById(R.id.lv);
		lv.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		lv.setAMapListViewListener(this);
	}

	/**
	 * 网络请求数据
	 */
	public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType
			.parse("application/json;charset=utf-8");
	private final OkHttpClient client = new OkHttpClient();

	protected void mRun(final int skip, final int limit) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("mobilePhoneNumber", mCache.getAsString("USER"));
			obj.put("action", "recv");
			obj.put("skip", skip * 10);// 跳过几条数据
			obj.put("limit", limit);// 限制返回几条数据
			obj.put("mode", "community");
			okhttp3.Request request = new okhttp3.Request.Builder()
					.url(Information.KONGCV_GET_PUSHMESSAGE_LIST)
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
		try {
			Log.d("str我 的>>>", str+"<>");
			Message msg = mHandler.obtainMessage();
			JSONObject object = new JSONObject(str);
			JSONArray array = object.getJSONArray("result");
			if (array != null && array.length() > 0) {
				InfoBean bean;
				JpushBean jpushBean = null;
				if (mList != null) {
					mList.clear();
				}
				mList = new ArrayList<InfoBean>();
				jpushBeans = new ArrayList<JpushBean>();
				for (int i = 0; i < array.length(); i++) {
					bean = new InfoBean();
					jpushBean = new JpushBean();
					int state = array.getJSONObject(i).getInt("state");
					String hire_method_id = array.getJSONObject(i)
							.getJSONObject("extras")
							.getString("hire_method_id");

					String message_id = array.getJSONObject(i)
							.getJSONObject("user").getString("objectId");
					String park_id = array.getJSONObject(i)
							.getJSONObject("extras").getString("park_id");
					String address = array.getJSONObject(i)
							.getJSONObject("extras").getString("address");
					String own_device_token = array.getJSONObject(i)
							.getJSONObject("extras")
							.getString("own_device_token");
					String own_mobile = array.getJSONObject(i)
							.getJSONObject("extras").getString("own_mobile");
					String hire_end = array.getJSONObject(i)
							.getJSONObject("extras").getString("hire_end");
					String mode = array.getJSONObject(i)
							.getJSONObject("extras").getString("mode");
					String push_type = array.getJSONObject(i)
							.getJSONObject("extras").getString("push_type");
					String hire_method_field = array.getJSONObject(i)
							.getJSONObject("extras")
							.getString("hire_method_field");
					String hire_start = array.getJSONObject(i)
							.getJSONObject("extras").getString("hire_start");
					String own_device_type = array.getJSONObject(i)
							.getJSONObject("extras")
							.getString("own_device_type");
					int price = array.getJSONObject(i).getJSONObject("extras")
							.getInt("price");
					String createdAt = array.getJSONObject(i).getString(
							"createdAt");
					if (state == 0) {
						status = "未处理";
					} else if (state == 1) {
						status = "已接受";
					} else {
						status = "已拒绝";
					}
					jpushBean.setMessage_id(message_id);
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

					bean.setAddress(address);
					bean.setHire_start(GTMDateUtil.GTMToLocal(createdAt, true));
					bean.setMode(mode);
					bean.setState(status);
					bean.setPark_id(park_id);
					bean.setHire_method_field(hire_method_field);
					mList.add(bean);
					jpushBeans.add(jpushBean);
				}
				for(int i=0;i<mList.size();i++){
					Log.d("i=="+i, mList.get(i).toString());
				}
				JpushBeanAndInfoBean jpushBeanAndInfoBean = new JpushBeanAndInfoBean(
						jpushBeans, mList);
				msg.what = 0;
				msg.obj = jpushBeanAndInfoBean;
				mHandler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		mHandler.postAtTime(new Runnable() {
			@Override
			public void run() {
				refresh();
				onLoad();
			}
		}, 1500);
	}

	private int skip = 0;

	private void refresh() {
		mRun(0, 10);
	}

	@Override
	public void onLoadMore() {
		mHandler.postAtTime(new Runnable() {
			@Override
			public void run() {
				skip++;
				Loading();
				onLoad();
			}

			private void Loading() {
				mRun(skip, 10);
			}
		}, 1500);
	}

	private void onLoad() {
		lv.stopRefresh();
		lv.stopLoadMore();
		lv.setRefreshTime("刚刚");
	}

}