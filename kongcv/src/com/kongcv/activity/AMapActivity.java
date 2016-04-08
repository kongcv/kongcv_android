package com.kongcv.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import android.animation.LayoutTransition;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLongClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.google.gson.Gson;
import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.global.Bean;
import com.kongcv.global.CurbAndObjectId;
import com.kongcv.global.Information;
import com.kongcv.global.Location_info;
import com.kongcv.global.SearchBean;
import com.kongcv.utils.Data;
import com.kongcv.utils.ToastUtil;
import com.kongcv.view.AMapListView;
import com.kongcv.view.AMapListView.AMapListViewListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 点击跳转到地图导航 显示info信息页面
 * 
 * @author kcw001
 */
public class AMapActivity extends FragmentActivity implements
		OnMapClickListener, AMapListViewListener, OnItemClickListener,
		OnMapLongClickListener, OnCameraChangeListener, OnMarkerClickListener,
		InfoWindowAdapter, OnItemSelectedListener, OnInfoWindowClickListener,
		OnKeyListener, OnClickListener {

	private AMap aMap;
	private MapView mapView;
	private AMapListView mListView;
	private SimpleAdapter mAdapter1;
	private ArrayList<HashMap<String, Object>> dlist;

	private Marker locationMarker; // 选择的点
	private LatLonPoint lp;
	private int currentPage = 0;// 当前页面，从0开始计数
	private PoiSearch.Query query;// Poi查询条件类
	private PoiSearch poiSearch;

	private PoiOverlay poiOverlay;// poi图层
	private List<PoiItem> poiItems;// poi数据
	private Marker detailMarker;// 显示Marker的详情
	private PoiResult poiResult; // poi返回的结果
	private ArrayList addresslist, latLngList, restList, priceList;
	private String temp;
	private Bean beanList;
	private int skip = 0;
	private int numCheck = -1;
	private final OkHttpClient client = new OkHttpClient();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.amap_activity);
		MyApplication.getInstance().addActivity(this);
		getInfo();
		initHigh();// 获取屏幕宽高
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		loading();
		init();
	}
	
	private int getHire_type() {
		Bundle bundle = getIntent().getExtras();
		if (bundle.getInt("hire_type") == 2) {
			return bundle.getInt("hire_type");
		} else {
			return -1;
		}
	}
	private String getAddress() {
		Bundle bundle = getIntent().getExtras();
		String address = (String) bundle.get("strAddress");
		Log.d("strAddress", address+"<>");
		return address;
	}
	private void getInfo() {
		searchBean = (SearchBean) getIntent().getSerializableExtra("bean");
		if (searchBean != null) {
			Gson gson = new Gson();
			numCheck = getHire_type();
			if (numCheck == 2) {
				searchBean.setHire_field(null);
				searchBean.setHire_method_id(null);
			}else{
				searchBean.setHire_field(getField());
				searchBean.setHire_method_id(getCarObjectId());
			}
			String json = gson.toJson(searchBean);
			// TODO Auto-generated method stub
			doSearch(json);
		}
	}
	private void doSearch(String objFrom) {
		// TODO Auto-generated method stub
		okhttp3.Request request = new okhttp3.Request.Builder()
				.url(Information.KONGCV_LOCATION_SEARCH)
				.headers(Information.getHeaders())
				.post(RequestBody.create(Information.MEDIA_TYPE_MARKDOWN,
						objFrom)).build();
		client.newCall(request).enqueue(new okhttp3.Callback() {
			@Override
			public void onResponse(Call arg0, okhttp3.Response response)
					throws IOException {
				// TODO Auto-generated method stub
				if (response.isSuccessful()) {
					Message msg = mHandler.obtainMessage();
					msg.what = 0;
					msg.obj = response.body().string();
					mHandler.sendMessage(msg);
				}
			}
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				Log.e("KONGCV_LOCATION_SEARCH", arg1.toString());
			}
		});
	}
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				try {
					String Str = (String) msg.obj;
					Log.d("刷新>>>", Str+"<>");
					if(Str!=null){
						JSONObject object = new JSONObject(Str);
						if (object.getJSONArray("result") != null
								&& object.getJSONArray("result").length() > 0) {
							refresh(Str);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
				
			}
		}
	};
	
	private void refresh(String readFrom) {
		try {
			JSONObject jsonObject = new JSONObject(readFrom);
			JSONArray jsonArray = jsonObject.getJSONArray("result");
			if (jsonArray != null && jsonArray.length() > 0) {
				addresslist = new  ArrayList<String>();
				List<String> objList = new ArrayList<String>(); // 车位的objectId
				List<LatLng> latLngList = new ArrayList<LatLng>();// 经纬度集合
				restList = new ArrayList<Integer>();
				priceList=new ArrayList<String>();
				List<String> parkList = new ArrayList<String>();
				Log.d("jsonArray.length()", jsonArray.length()+"<>");
				Log.d("jsonArray.length()", jsonArray.length()+"<>");
				Log.d("jsonArray.length()", jsonArray.length()+"<>");
				
				for (int index = 0; index < jsonArray.length(); index++) { // 每次加载5条
					String address = jsonArray.getJSONObject(index).getString(
							"address");// 地址信息
					addresslist.add(address);// 地址集合
					JSONArray arrayPrice = jsonArray.getJSONObject(index).getJSONArray("hire_price");
					JSONArray arrayMethod = jsonArray.getJSONObject(index).getJSONArray("hire_method");
					// 经纬度
					JSONObject object = jsonArray.getJSONObject(index)
							.getJSONObject("location");
					double latitude = object.getDouble("latitude");
					double longitude = object.getDouble("longitude");
					LatLng latLng = new LatLng(latitude, longitude);
					latLngList.add(latLng);
					if (numCheck != 2) {
						for(int i=0;i<arrayMethod.length();i++){
							if(arrayMethod.getJSONObject(i).getString("objectId").equals(getCarObjectId())){
								String string = arrayPrice.get(i).toString();
								priceList.add(string);
							}
						}
					}
					int park_space = jsonArray.getJSONObject(index).getInt(
							"park_space");
					restList.add(park_space);

					String objectId = jsonArray.getJSONObject(index).getString(
							"objectId");
					objList.add(objectId);
				}
				bean = new Bean();
				bean.setPark_id(parkList);
				bean.setAddressList(addresslist);
				bean.setLatLngList(latLngList);
				bean.setObjList(objList);
				bean.setRestList(restList);
				bean.setPriceList(priceList);
				if (dlist != null) {
					dlist.clear();
					mAdapter1.notifyDataSetChanged();
				}
				if(bean!=null){
					priceList = (ArrayList) bean.getPriceList();
					addresslist = (ArrayList) bean.getAddressList();
				}
				if(getData()!=null){
					mAdapter1 = new SimpleAdapter(AMapActivity.this, getData(),
							R.layout.amap_listview_item, new String[] {
									"tv_address", "tv_detail", "tv_rest",
									"tv_price" }, new int[] {
									R.id.amap_tv_address, R.id.amap_tv_detail,
									R.id.amap_tv_rest, R.id.amap_tv_price });
					mListView.setAdapter(mAdapter1);
					mAdapter1.notifyDataSetChanged();
				}
			} else {
				ToastUtil.show(getBaseContext(), "没有数据！");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void loading() {
		mypDialog = new ProgressDialog(this, R.style.MyDialogStyle);
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mypDialog.setMessage("loading...");
		mypDialog.setIndeterminate(false);
		mypDialog.setCancelable(true);
		mypDialog.show();
	}
	private void initView() {
		// TODO Auto-generated method stub
		/** 下拉刷新，上拉加载 */
		mListView = (AMapListView) findViewById(R.id.add_xListView);// 这个listview是在这个layout里面
		mListView.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据

		mAdapter1 = new SimpleAdapter(AMapActivity.this, getData(),
				R.layout.amap_listview_item, new String[] { "tv_address",
						"tv_detail", "tv_rest", "tv_price" }, new int[] {
						R.id.amap_tv_address, R.id.amap_tv_detail,
						R.id.amap_tv_rest, R.id.amap_tv_price });
		mListView.setAdapter(mAdapter1);
		
		mListView.setAMapListViewListener(this);
		mListView.setOnItemClickListener(this);
		setAItemNum = (ImageView) findViewById(R.id.iv_map_flexible);
		setAItemNum.setOnClickListener(this);
		rangePrice = (TextView) findViewById(R.id.tv_rangeandprice);
		rangePrice.setOnClickListener(this);
	}
	
	private int displayHeight;// 屏幕高度
	private int displayWidth;// 屏幕宽度
	private void initHigh() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		displayWidth = displayMetrics.widthPixels;
		displayHeight = displayMetrics.heightPixels;
	}
	
	private ImageView setAItemNum;
	private void initMapHeight() {// 初始化地图宽高
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, (int) (displayHeight * 0.5f + 0.5f));
		mapView.setLayoutParams(params);
	}
	private void setMapHeight() {// 初始化地图宽高
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, (int) (displayHeight * 0.8f + 0.5f));
		mapView.setLayoutParams(params);
	}
	private void setItemShow() {// 初始化地图宽高
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, (int) (displayHeight * 0.2f + 0.5f));
		mListView.setLayoutParams(params);
	}
	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
			initMapHeight();
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (SearchActivity.latLng == null) {
						initMarkers();
						initView();
					} else if (Data.getData("LatLng") != null) {
						LatLng latLng = (LatLng) Data.getData("LatLng");
						aMap.moveCamera(CameraUpdateFactory
								.changeLatLng(SearchActivity.latLng));
						locationMarker = aMap.addMarker(new MarkerOptions()
								.anchor(0.5f, 1)
								.icon(BitmapDescriptorFactory
										.fromResource(R.drawable.start))
								.position(
										new LatLng(latLng.latitude,
												latLng.longitude))
								.title(""));
						SearchActivity.latLng = null;
						mypDialog.dismiss();
					} else {
						aMap.moveCamera(CameraUpdateFactory
								.changeLatLng(SearchActivity.latLng));
						locationMarker = aMap.addMarker(new MarkerOptions()
								.anchor(0.5f, 1)
								.icon(BitmapDescriptorFactory
										.fromResource(R.drawable.start))
								.position(
										new LatLng(
												SearchActivity.latLng.latitude,
												SearchActivity.latLng.longitude))
								.title(""));
						SearchActivity.latLng = null;
						mypDialog.dismiss();
					}
				}
			}, 3000);
			setUpMap();// 设置侦听
		}
	}
	private void initMarkers() {
		beanList=new Bean();
		beanList = bean;
		if (beanList != null) {
			addresslist = (ArrayList) beanList.getAddressList();
			latLngList = (ArrayList) beanList.getLatLngList();
			restList = (ArrayList) beanList.getRestList();
			priceList = (ArrayList) beanList.getPriceList();
			aMap.moveCamera(CameraUpdateFactory
					.changeLatLng((LatLng) latLngList.get(0)));
			for (int i = 0; i < latLngList.size(); i++) {
				locationMarker = aMap.addMarker(new MarkerOptions()
						.anchor(0.5f, 1)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.start))
						.position((LatLng) latLngList.get(i)).title(""));
			}
			mypDialog.dismiss();
		} else {
			if (searchBean != null) {
				Location_info location_info = searchBean.getLocation_info();

				aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(
						location_info.getLatitude(), location_info
								.getLongitude())));
				locationMarker = aMap.addMarker(new MarkerOptions()
						.anchor(0.5f, 1)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.start))
						.position(
								new LatLng(location_info.getLatitude(),
										location_info.getLongitude()))
						.title(""));
				mypDialog.dismiss();
			}
		}
	}

	// 传递参数
	private ArrayList<HashMap<String, Object>> getData() {
		if (addresslist != null) {
			dlist = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < addresslist.size(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String a[] = ((String) addresslist.get(i)).split("&");
				int park_space = (Integer) restList.get(i);
				map.put("tv_address", a[0]);
				map.put("tv_detail", a[1]);
				if (park_space == 0) {
					map.put("tv_rest", "已租");
				} else {
					map.put("tv_rest", "未租");
				}
				if (priceList.size() > 0)
				map.put("tv_price", "¥ " + priceList.get(i));
				dlist.add(map);
			}
		}
		return dlist;
	}

	/**
	 * amap添加一些事件监听器
	 */
	private void setUpMap() {
		aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
		aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器
		aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
		JPushInterface.onResume(this);
		MobclickAgent.onResume(this);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
		mapView.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);

	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 对单击地图事件回调 处理marker点击事件
	 */
	@Override
	public void onMapClick(LatLng latng) {
		locationMarker.showInfoWindow();
		animalDone();
	}
	private void animalDone() {
		// TODO Auto-generated method stub
		LayoutTransition transition = new LayoutTransition();  
	    transition.setAnimator(LayoutTransition.CHANGE_APPEARING,  
	            transition.getAnimator(LayoutTransition.CHANGE_APPEARING));  
	    transition.setAnimator(LayoutTransition.APPEARING,  
	            null);  
	    transition.setAnimator(LayoutTransition.DISAPPEARING,  
	            null);  
	    transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,  
	            null);  
	    mapView.setLayoutTransition(transition); 
	}
	
	/**
	 * 对长按地图事件回调
	 */
	@Override
	public void onMapLongClick(LatLng point) {
	}

	/**
	 * 对正在移动地图事件回调
	 */
	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
	}

	/**
	 * 对移动地图结束事件回调
	 */
	@Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {
	}

	/** 停止刷新， */
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			public void run() {
				doRefresh();
				onLoad();
			}
			private void doRefresh() {
				// TODO Auto-generated method stub
				if (searchBean != null) {
					Gson gson = new Gson();
					numCheck = getHire_type();
					if (numCheck == 2) {
						searchBean.setHire_field(null);
						searchBean.setHire_method_id(null);
					}else{
						searchBean.setHire_field(getField());
						searchBean.setHire_method_id(getCarObjectId());
					}
					skip =0;
					searchBean.setSkip(skip);
					searchBean.setLimit(10);
					String json = gson.toJson(searchBean);
					Log.v("json", json);
					doSearch(json);
				//	mHandler.sendEmptyMessageDelayed(1, 1000);
				}
			}
		}, 1500);
	}

	// 加载更多
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			public void run() {
				doLoadMore();
				onLoad();
			}

			private void doLoadMore() {
				// TODO Auto-generated method stub
				if (searchBean != null) {
					Gson gson = new Gson();
					numCheck = getHire_type();
					if (numCheck == 2) {
						searchBean.setHire_field(null);
						searchBean.setHire_method_id(null);
					}else{
						searchBean.setHire_field(getField());
						searchBean.setHire_method_id(getCarObjectId());
					}
					skip++;
					skip = skip * 10;
					searchBean.setSkip(skip);
					searchBean.setLimit(10);
					String json = gson.toJson(searchBean);
					Log.d("第一次>>>", json);
					doSearch(json);
					mHandler.sendEmptyMessageDelayed(2, 1000);
				}
			}
		}, 1500);
	}

	//private double price;
	private String price;
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (position != 0) {
			if (SearchActivity.latLng == null) {
				ArrayList data = (ArrayList) beanList.getObjList();
				final String object = (String) data.get(position - 1);
				if (numCheck != 2) {
					price = priceList.get(position - 1).toString();
				}
				view.postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Intent intent = new Intent(AMapActivity.this,
								DetailsActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("bean", searchBean);
						bundle.putString("objectId", getCarObjectId());// 车位id
						bundle.putString("mode", getMode());
						bundle.putString("getField", getField());
						
						bundle.putString("park_id", object);
						String a[] = price.split("/");
						bundle.putDouble("price", Double.parseDouble(a[0]));
						Log.d("a[]", a[0]);
						intent.putExtras(bundle);
						CurbAndObjectId bean = new CurbAndObjectId();
						bean.setMode(getMode());
						bean.setObjectId(getCarObjectId());
						bean.setField(getField());
						Data.putData("CurbAndObjectId", bean);// 车位类型id
						startActivity(intent);
					}
				}, 1000);
			}
		}
	}
	/**
	 * 获取field
	 */
	private String getField() {
		Bundle bundle = getIntent().getExtras();
		String field = (String) bundle.get("getField");
		return field;
	}
	/**
	 * 获取到field id
	 * @return
	 */
	private String getCarObjectId() {
		Bundle bundle = getIntent().getExtras();
		String object = (String) bundle.get("objectId");
		return object;
	}

	/**
	 * 获取mode
	 */
	private String getMode() {
		Bundle bundle = getIntent().getExtras();
		String object = (String) bundle.get("mode");
		return object;
	}

	/**
	 * onMarkerClick
	 */
	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		if (poiOverlay != null && poiItems != null && poiItems.size() > 0) {
			detailMarker = marker;
			doSearchPoiDetail(poiItems.get(poiOverlay.getPoiIndex(marker))
					.getPoiId());
		}
		return false;
	}

	/**
	 * 查单个poi详情
	 */
	public void doSearchPoiDetail(String poiId) {
		if (poiSearch != null && poiId != null) {
			poiSearch.searchPOIDetailAsyn(poiId);
		}
	}

	/**
	 * info
	 */
	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}
	private boolean flag = true;// 页面布局的切换
	private boolean rangeAndPrice = true;
	private ProgressDialog mypDialog;
	private TextView rangePrice, tvRest, tvPrice;
	private Bean bean;
	private SearchBean searchBean;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_map_flexible:
			if (flag) {
				setAItemNum.setImageResource(R.drawable.item_show);
				setMapHeight();
				setItemShow();
				ToastUtil.fixListViewHeight(mListView, 2);
				flag = !flag;
			} else {
				setAItemNum.setImageResource(R.drawable.item_show_sh);
				ToastUtil.fixListViewHeight(mListView, -1);
				initMapHeight();
				flag = !flag;
			}
			break;
		case R.id.tv_rangeandprice:
			skip=0;
			rangePrice.setText("按距离");
			if (rangeAndPrice) {
				rangePrice.setText("按价格");
				if (searchBean != null) {
					if (dlist != null) {
						dlist.clear();
						mAdapter1.notifyDataSetChanged();
					}
					Gson gson = new Gson();
					numCheck = getHire_type();
					if (numCheck == 2) {
						searchBean.setHire_field(null);
						searchBean.setHire_method_id(null);
					}else{
						searchBean.setHire_field(getField());
						searchBean.setHire_method_id(getCarObjectId());
					}
					searchBean.setSort("price_asc");
					searchBean.setSkip(skip);
					String json = gson.toJson(searchBean);
					doSearch(json);
				} else {
					ToastUtil.show(getApplicationContext(), "没有数据啦~~");
				}
				rangeAndPrice = false;
			} else {
				Log.d("skip=="+skip, ">>>>>>>>>>>>");
				rangePrice.setText("按距离");
				if (searchBean != null) {
					if (dlist != null) {
						dlist.clear();
						mAdapter1.notifyDataSetChanged();
					}
					Gson gson = new Gson();
					numCheck = getHire_type();
					if (numCheck == 2) {
						searchBean.setHire_field(null);
						searchBean.setHire_method_id(null);
					}else{
						searchBean.setHire_field(getField());
						searchBean.setHire_method_id(getCarObjectId());
					}
					searchBean.setSort(null);
					searchBean.setSkip(skip);
					String json = gson.toJson(searchBean);
					doSearch(json);
				} else {
					ToastUtil.show(getApplicationContext(), "没有数据啦~~");
				}
				rangeAndPrice = true;
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		locationMarker.hideInfoWindow();
		lp = new LatLonPoint(locationMarker.getPosition().latitude,
				locationMarker.getPosition().longitude);
		locationMarker.destroy();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, HomeActivity.class);
			intent.putExtra("back", 0);
			startActivity(intent);
			finish();
		}
		return false;
	}

}
