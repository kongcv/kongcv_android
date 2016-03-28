package com.kongcv.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.adapter.SearchAdapter;
import com.kongcv.fragment.PublishFragment;
import com.kongcv.global.LocationInfo;
import com.kongcv.utils.Data;
import com.kongcv.utils.ToastUtil;
import com.kongcv.view.AMapListView;
import com.kongcv.view.AMapListView.AMapListViewListener;

public class SearchPublishActivity extends Activity implements
		OnItemClickListener, AMapListViewListener, OnPoiSearchListener,
		TextWatcher, OnGeocodeSearchListener, OnClickListener {

	private int mCount;
	private AMapListView lv;
	private Drawable mDelete;
	private EditText txtFind;
	private int currentPage = 0;// 当前页面，从0开始计数
	private Inputtips inputTips;
	private SearchAdapter adapter;
	private PoiSearch poiSearch;// POI搜索
	private String keyWord = "";// 要输入的poi搜索关键字
	ArrayList<Map<String, Object>> mList;
	private static GeocodeSearch geocoderSearch;// 经纬度搜索
	private ImageView ivBack, ivDelete, ivSearch;
	private PoiSearch.Query query;// Poi查询条件类
	private PoiResult poiResult; // poi返回的结果
	// key和id
	public static final String KEY[] = new String[] { "ivDaoH", "txtFind",
			"ivCome" };
	
	private String city=null;
	private String a[]=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		MyApplication.getInstance().addActivity(this);
		initView();
	}

	/**
	 * 搜索
	 */
	private void doSearchQuery(String str) {
		currentPage = 0;
		query = new PoiSearch.Query(str, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setPageSize(20);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页

		poiSearch = new PoiSearch(this, query);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}

	/**
	 * 下一页
	 */
	public void nextPage() {
		if (query != null && poiSearch != null && poiResult != null) {
			if (poiResult.getPageCount() - 1 > currentPage) {
				currentPage++;
				// query = new PoiSearch.Query(txtFind.getText().toString(), "",
				// (String) Data.getData("wk"));//
				// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
				// query.setPageSize(20);// 设置每页最多返回多少条poiitem
				query.setPageNum(currentPage);// 设置查后一页
				poiSearch.setOnPoiSearchListener(this);
				poiSearch.searchPOIAsyn();
			} else {
				ToastUtil.show(SearchPublishActivity.this, R.string.no_result);
			}
		}
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		// TODO Auto-generated method stub
		lv = (AMapListView) findViewById(R.id.amaplist);
		lv.setOnItemClickListener(this);
		lv.setAMapListViewListener(this);
		lv.setPullLoadEnable(true);

		txtFind = (EditText) findViewById(R.id.txtfind);
		ivDelete = (ImageView) findViewById(R.id.iv_delete);
		ivSearch = (ImageView) findViewById(R.id.iv_search);
		ivBack = (ImageView) findViewById(R.id.iv_back);

		txtFind.addTextChangedListener(this);
		ivDelete.setOnClickListener(this);
		ivSearch.setOnClickListener(this);
		ivBack.setOnClickListener(this);

		mList = new ArrayList<Map<String, Object>>();
		mCount = mList.size();
		// 在初始化方法中设置图片的颜色
		ivDelete.setImageResource(R.drawable.delete_item);
		mDelete = this.getResources().getDrawable(
				R.drawable.delete_item_unselector);
		// 初始化中设置GeocodeSearch侦听 请求获取经纬度
		geocoderSearch = new GeocodeSearch(SearchPublishActivity.this);
		geocoderSearch.setOnGeocodeSearchListener(this);
	}

	/**
	 * 获取地理坐标
	 */
	private LocationInfo info=null;
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		// TODO Auto-generated method stub
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				try {
					GeocodeAddress address = result.getGeocodeAddressList()
							.get(0);
					LatLonPoint latLonPoint = address.getLatLonPoint();
					double latitude = latLonPoint.getLatitude();
					double longitude = latLonPoint.getLongitude();
					Data.putData("city", address.getCity());
				
					info=new LocationInfo();
					info.set_type("GeoPoint");
					info.setLatitude(latitude);
					info.setLongitude(longitude);
					
					/*Data.putData("location_info", info);
					Log.e("location_info对象", info + "");*/

					ToastUtil.show(getApplicationContext(), latitude+":"+longitude+"!");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			ToastUtil.show(getApplicationContext(), "高德地图获取地理坐标出错！");
		}
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	/**
	 * 文字输入提示
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		String newText = s.toString().trim();
		if (newText != null) {
			adapter = new SearchAdapter(SearchPublishActivity.this);
			mList.clear();
			adapter.setDataSource(mList);
			lv.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		inputTips = new Inputtips(SearchPublishActivity.this,
				new InputtipsListener() {

					@Override
					public void onGetInputtips(List<Tip> tipList, int rCode) {
						Log.e("onGetInputtips", tipList+"");
						if (rCode == 0) {// 正确返回
							for (int i = 0; i < tipList.size(); i++) {
								Log.e("tipList", tipList.get(i)+"");
								Map<String, Object> map = new HashMap<String, Object>();
								map.put(KEY[0], R.drawable.iv_daoh);// 加入图片
								map.put(KEY[1], tipList.get(i).getName());// 文字
								map.put(KEY[2], R.drawable.iv_come);
								String city=getCity(tipList.get(i).getDistrict());
								map.put(KEY[3], city);
								mList.add(map);
							}
							adapter = new SearchAdapter(
									SearchPublishActivity.this);
							adapter.setDataSource(mList);
							lv.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}
					}

					private String getCity(String district) {
						//广西壮族自治区玉林市玉州区
						if(district.indexOf("市")>=0 ){
							 a = district.split("市"); 
							 city=a[0];
							if(city.indexOf("省")>=0){
								a = city.split("省"); 
								city=a[1];
							}else if(city.indexOf("区")>=0){
								a = city.split("区"); 
								city=a[1];
							}
						}
						return city;
					}
				});
		try {
			inputTips.requestInputtips(newText, "");// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号
		} catch (AMapException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		final String str = (String) mList.get(position-1).get(SearchActivity.KEY[1]);
		final String strCity = (String) mList.get(position-1).get(SearchActivity.KEY[3]);
		String strSearch = getStrSearch(str);
		getLatlon(strSearch,strCity);
		
		view.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SearchPublishActivity.this,
						PublishFragment.class);
				/*intent.putExtra("addressName", str);
				intent.putExtra("longitude", (Double) Data.getData("longitude"));
				intent.putExtra("latitude", (Double) Data.getData("latitude"));*/
				intent.putExtra("addressName", str);
				intent.putExtra("tv_City", strCity);
				intent.putExtra("info", info);
				setResult(RESULT_OK, intent);
				finish();
			}

		}, 1000);
	}
	private String getStrSearch(String str){
		 String param=null;
		 if(str.indexOf("(")!=-1){
			 String string=str.substring(0, str.indexOf("("));
			 param=string+" "+str.substring(str.indexOf("(")+1,str.indexOf(")"));
		 }else{
			 param=str;
		 }
		return param;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_search:
			mList.clear();
			searchButton();
			break;
		case R.id.iv_delete:// 删除时清空数据
			txtFind.setText("");
			adapter = new SearchAdapter(SearchPublishActivity.this);
			mList.clear();
			adapter.setDataSource(mList);
			lv.setAdapter(adapter);
			adapter.notifyDataSetChanged();

			break;
		case R.id.iv_back:// 点击回退到 fragment
			this.finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 点击搜索按钮
	 */
	public void searchButton() {
		keyWord = ToastUtil.checkEditText(txtFind);
		if ("".equals(keyWord)) {
			ToastUtil.show(this, "请输入搜索关键字");
			return;
		} else {
			// doSearch();
			String newText = txtFind.getText().toString();
			doSearchQuery(newText);
		}
	}

	/**
	 * 搜索
	 */
	/*private void doSearch() {
		final String str = (String) mList.get(0).get(SearchActivity.KEY[1]);
		// Log.v("天安门广场吃泡面", str);
		getLatlon(str);

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				*//**
				 * 向主activity携带的地址信息
				 *//*
				Intent intent = new Intent(SearchPublishActivity.this,
						PublishFragment.class);
				intent.putExtra("addressName", str);
				intent.putExtra("longitude", (Double) Data.getData("longitude"));
				intent.putExtra("latitude", (Double) Data.getData("latitude"));
				setResult(RESULT_OK, intent);
				finish();
			}
		}, 200);
	}*/

	private Handler mHandler = new Handler() {
	};

	/**
	 * 响应地理编码
	 */
	public static void getLatlon(String name,String city) {
		GeocodeQuery query = new GeocodeQuery(name, city);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}

	/** 停止刷新， */
	private void onLoad() {
		lv.stopRefresh();
		lv.stopLoadMore();
		lv.setRefreshTime("刚刚");
	}

	// 刷新
	public void onRefresh() {
		Log.e("onRefresh", "正在刷新");
		Log.e("onRefresh", "正在刷新");
		Log.e("onRefresh", "正在刷新");
		mHandler.postDelayed(new Runnable() {
			public void run() {
				nextPage();
				onLoad();
			}
		}, 2000);
	}

	// 加载更多
	public void onLoadMore() {
		Log.e("onLoadMore", "正在刷新");
		Log.e("onLoadMore", "正在刷新");
		Log.e("onLoadMore", "正在刷新");
		mHandler.postDelayed(new Runnable() {

			public void run() {
				nextPage();
				onLoad();
			}
		}, 2000);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		// TODO Auto-generated method stub
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
					poiResult = result;
					List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					if (poiItems != null && poiItems.size() > 0) {
						// mList.clear();
						Log.v("onPoiSearched中  before for", mList + "");

						ArrayList<PoiItem> pois = result.getPois();
						for (int i = 0; i < pois.size(); i++) {
							// address.add(result.getPois().get(i).toString());
							Log.e("result", result.getPois().get(i).toString());

							Map<String, Object> map = new HashMap<String, Object>();
							map.put(KEY[0], R.drawable.iv_daoh);// 加入图片
							map.put(KEY[1], result.getPois().get(i).toString());// 文字
							map.put(KEY[2], R.drawable.iv_come);
							mList.add(map);
						}
						Log.v("onPoiSearched中  after for", mList + "");
						adapter = new SearchAdapter(SearchPublishActivity.this);
						adapter.setDataSource(mList);
						lv.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}
				}
			}
		} else {
			ToastUtil.show(SearchPublishActivity.this, R.string.no_result);
		}
	}

}
