package com.kongcv.activity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.UI.AsyncImageLoader.PreReadTask;
import com.kongcv.adapter.CarManagerAdapter;
import com.kongcv.global.MineCarmanagerBean;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.Data;
import com.kongcv.utils.GTMDateUtil;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.view.AMapListView;
import com.kongcv.view.AMapListView.AMapListViewListener;

/*
 * 车位管理页面
 */
public class MineCarmanagerActivity extends Activity implements
		OnItemClickListener, AMapListViewListener {

	private AMapListView listView;
	private CarManagerAdapter mAdapter;
	private ImageView iv_back;
	private int park_area, park_struct;
	private String hire_method_id, tail_num, gate_card, park_description,
			hire_start, hire_end, no_hire;
	private boolean normal;
	private int park_height;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				updateInfo = (ArrayList<MineCarmanagerBean>) msg.obj;
				for(int i=0;i<updateInfo.size();i++){
					Log.d("i=="+i, updateInfo.get(i).toString()+"<>");
				}
				mAdapter = new CarManagerAdapter(MineCarmanagerActivity.this,
						updateInfo);
				listView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};
	private ACacheUtils mCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_carmanager);
		MyApplication.getInstance().addActivity(this);
		mCache = ACacheUtils.get(getApplicationContext());
		initView();
		getInfoData();
	}

	/**
	 * 初始化数据及刷新请求数据
	 */
	private int skip = 0;
	private final OkHttpClient client = new OkHttpClient();

	private void getInfoData() {
		ReadType type = new ReadType();
		type.execute(skip);
	}

	/**
	 * 网络获取数据
	 */
	class ReadType extends PreReadTask<Integer, Void, Void> {
		@Override
		protected Void doInBackground(Integer... params) {
			getData(params[0] * 10);
			return null;
		}
	}

	private ArrayList<MineCarmanagerBean> updateInfo;
	private void getData(Integer integer) {
		JSONObject jso = new JSONObject();
		try {
			jso.put("user_id", mCache.getAsString("user_id"));
			jso.put("skip", skip * 10);
			jso.put("limit", 10);
			jso.put("mode", "community");
			jso.put("action", "userid");
			String doHttpsPost = PostCLientUtils.doHttpsPost(
					com.kongcv.global.Information.KONGCV_GET_PARK_LIST,
					JsonStrUtils.JsonStr(jso));
			JSONObject object = new JSONObject(doHttpsPost);
			JSONArray jsonArray = object.getJSONArray("result");
			Message msg = handler.obtainMessage();
			if (jsonArray != null && jsonArray.length() > 0) {
				updateInfo = updateInfo(doHttpsPost);
				msg.what = 1;
				msg.obj = updateInfo;
				handler.sendMessage(msg);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		listView = (AMapListView) this.findViewById(R.id.iv_carmanager);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listView.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		listView.setAMapListViewListener(this);
		listView.setOnItemClickListener(this);
	}

	/**
	 * 刷新
	 */
	@Override
	public void onRefresh() {
		handler.postAtTime(new Runnable() {
			@Override
			public void run() {
				skip = 0;
				getInfoData();
				onLoad();
			}
		}, 2000);
	}

	/**
	 * 加载更多
	 */
	@Override
	public void onLoadMore() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				skip++;
				getInfoData();
				onLoad();
			}
		}, 2000);
	}

	/**
	 * 停止刷新
	 */
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("刚刚");
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}

	private String city = null;

	private ArrayList<MineCarmanagerBean> updateInfo(String doHttpsPost) {
		ArrayList<MineCarmanagerBean> items=null;
		try {
			JSONObject Resultobj = new JSONObject(doHttpsPost);
			String result = Resultobj.getString("result");
			JSONArray array = new JSONArray(result);

			items= new ArrayList<MineCarmanagerBean>();
			MineCarmanagerBean carmanagerBean;
			List<String> price_list = new ArrayList<String>();
			List<String> hireTimeList = new ArrayList<String>();
			List<String> hirePriceList = new ArrayList<String>();
			List<String> hireMethodIdList = new ArrayList<String>();
			String noHrieString = null;
			for (int i = 0; i < array.length(); i++) {
				carmanagerBean = new MineCarmanagerBean();
				String address = array.getJSONObject(i).getString("address");
				int park_hide = array.getJSONObject(i).getInt("park_hide");
				if (array.getJSONObject(i).has("city")) {
					city = array.getJSONObject(i).getString("city");
				}
				double latitude = array.getJSONObject(i)
						.getJSONObject("location").getDouble("latitude");
				double longitude = array.getJSONObject(i)
						.getJSONObject("location").getDouble("longitude");
				// 出租方式id
				JSONObject obj = array.getJSONObject(i);
				if (obj.has("park_area")) {
					// 车位面积
					park_area = obj.getInt("park_area");
				} else {
					park_area = -1;
				}
				// 车位限高
				if (obj.has("park_height")) {
					park_height = obj.getInt("park_height");
				} else {
					park_height = -1;
				}
				// 地上，地下
				if (obj.has("park_struct")) {
					park_struct = obj.getInt("park_struct");
				}
				// 是否正规车位
				if (obj.has("normal")) {
					normal = obj.getBoolean("normal");
				}
				// 限行尾号
				if (obj.has("tail_num")) {
					tail_num = obj.getString("tail_num");
				}
				// 门禁卡
				if (obj.has("gate_card")) {
					gate_card = obj.getString("gate_card");
				}
				// 车位描述
				if (obj.has("park_description")) {
					park_description = obj.getString("park_description");
				}
				if (obj.has("hire_start")) {
					hire_start = GTMDateUtil.GTMToLocal(
							obj.getJSONObject("hire_start").getString("iso"),
							true);
					if (hire_start.indexOf(" ") != -1) {
						String[] str = hire_start.split(" ");
						hire_start = str[0];
					}
				}
				if (obj.has("hire_end")) {
					
					hire_end = GTMDateUtil.GTMToLocal(
							obj.getJSONObject("hire_end").getString("iso"),
							true);
					if (hire_end.indexOf(" ") != -1) {
						String[] str = hire_end.split(" ");
						hire_end = str[0];
					}
				}
				// 非出租日
				if (obj.has("no_hire")) {
					no_hire = obj.getString("no_hire");
					if(no_hire!=null){
						JSONArray jsonArray = new JSONArray(no_hire);
						List<String> list = new ArrayList<String>();
						for (int is = 0; is < jsonArray.length(); is++) {
							String jsonObject = jsonArray.getString(is);
							list.add(jsonObject);
						}
						StringBuilder builder = new StringBuilder();
						boolean flag = false;
						for (String string : list) {
							if (flag) {
								builder.append(",");
							} else {
								flag = true;
							}
							builder.append(string);
							noHrieString = builder.toString();
						}
					}
				}
				// 出租价格
				String hire_price = array.getJSONObject(i).getString(
						"hire_price");
				JSONArray jsonArrays = new JSONArray(hire_price);
				for (int x = 0; x < jsonArrays.length(); x++) {
					String jsonObjects = jsonArrays.getString(x);
					price_list.add(jsonObjects);
					hirePriceList.add(jsonObjects);
				}
				// 出租时间
				String hire_time = array.getJSONObject(i)
						.getString("hire_time");
				JSONArray json = new JSONArray(hire_time);
				List<String> hire_time_list = new ArrayList<String>();
				for (int x = 0; x < json.length(); x++) {
					String jsonObjects = json.getString(x);
					hire_time_list.add(jsonObjects);
					hireTimeList.add(jsonObjects);
				}
				String objectId = array.getJSONObject(i).getString("objectId");
				String hire_method = array.getJSONObject(i).getString(
						"hire_method");
				JSONArray arrays = new JSONArray(hire_method);
				for (int y = 0; y < arrays.length(); y++) {
					hire_method_id = arrays.getJSONObject(y).getString(
							"objectId");
					hireMethodIdList.add(hire_method_id);
				}
				carmanagerBean.setAddress(address);
				carmanagerBean.setPark_hide(park_hide);
				carmanagerBean.setCity(city);
				carmanagerBean.setLatitude(latitude);
				carmanagerBean.setLongitude(longitude);
				carmanagerBean.setHire_method_id(hireMethodIdList);
				carmanagerBean.setPark_area(park_area);
				carmanagerBean.setPark_height(park_hide);
				carmanagerBean.setPark_struct(park_struct);
				carmanagerBean.setNormal(normal);
				carmanagerBean.setTail_num(tail_num == null ? "" : tail_num);
				carmanagerBean.setGate_card(gate_card == null ? "" : gate_card);
				carmanagerBean
						.setPark_description(park_description == null ? ""
								: park_description);
				carmanagerBean.setHire_start(hire_start == null ? ""
						: hire_start);
				carmanagerBean.setHire_end(hire_end == null ? "" : hire_end);//
				carmanagerBean.setNo_hire(noHrieString);
				carmanagerBean.setHire_price(hirePriceList);
				carmanagerBean.setHire_time(hireTimeList);
				carmanagerBean.setObjectId(objectId);
				items.add(carmanagerBean);
			}
			return items;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(position!=0){
			Intent data = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("MineCarmanagerBean",
					updateInfo.get(position - 1));
			data.putExtra("bundle", bundle);
			Data.putData("MineCarmanagerBean", updateInfo.get(position - 1));
		//	setResult(HomeActivity.CWGL, data);
			setResult(1, data);
			finish();
		}
	}
}
