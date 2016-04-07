package com.kongcv.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.jpush.android.api.JPushInterface;

import com.amap.api.maps.model.LatLng;
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
import com.kongcv.global.Location_info;
import com.kongcv.global.SearchBean;
import com.kongcv.utils.Data;
import com.kongcv.utils.ToastUtil;
import com.kongcv.view.AMapListView;
import com.kongcv.view.AMapListView.AMapListViewListener;
import com.umeng.analytics.MobclickAgent;

public class SearchActivity extends FragmentActivity implements
		AMapListViewListener, OnItemClickListener, TextWatcher,
		OnGeocodeSearchListener, OnPoiSearchListener, OnKeyListener,
		OnEditorActionListener, OnClickListener {
	private int currentPage = 0;// 当前页面，从0开始计数
	private PoiSearch.Query query;// Poi查询条件类
	private PoiSearch poiSearch;// POI搜索

	private AMapListView lv;
	private EditText txtFind;
	private ImageView ivDelete;
	private TextView ivSearch;

	private String city = null;
	private String a[] = null;

	public static final String KEY[] = new String[] { "ivDaoH", "txtFind",
			"ivCome", "ivCity" };
	private static GeocodeSearch geocoderSearch;// 经纬度搜索
	private SearchAdapter adapter;
	private PoiResult poiResult; // poi返回的结果
	ArrayList<Map<String, Object>> mList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		MyApplication.getInstance().addActivity(this);
		init();
	}

	/**
	 * 获取field
	 */
	private String getField() {
		Bundle bundle = getIntent().getExtras();
		String field = (String) bundle.get("hire_field");
		return field;
	}

	/**
	 * 获取到hire_method_id
	 * 
	 * @return
	 */
	private String getData() {
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
	 * 初始化
	 */
	private void init() {
		lv = (AMapListView) findViewById(R.id.amaplist);
		lv.setOnItemClickListener(this);
		lv.setAMapListViewListener(this);
		lv.setPullLoadEnable(true);
		lv.setPullRefreshEnable(false);
		txtFind = (EditText) findViewById(R.id.txtfind);
		txtFind.setHint("不输入即搜索周边车位");
		ivDelete = (ImageView) findViewById(R.id.iv_delete);
		ivSearch = (TextView) findViewById(R.id.iv_search);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		txtFind.addTextChangedListener(this);
		ivDelete.setOnClickListener(this);
		ivSearch.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		txtFind.setOnKeyListener(this);
		txtFind.setOnEditorActionListener(this);
		mList = new ArrayList<Map<String, Object>>();
		
		// 初始化中设置GeocodeSearch侦听 请求获取经纬度
		geocoderSearch = new GeocodeSearch(SearchActivity.this);
		geocoderSearch.setOnGeocodeSearchListener(this);
	}

	/**
	 * 响应地理编码
	 */
	public static void getLatlon(String name, String city) {
		GeocodeQuery query = new GeocodeQuery(name, city);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}

	/**
	 * 下一页
	 */
	public void nextPage() {
		if (query != null && poiSearch != null && poiResult != null) {
			if (poiResult.getPageCount() - 1 > currentPage) {
				currentPage++;
				query.setPageNum(currentPage);// 设置查后一页
				poiSearch.setOnPoiSearchListener(this);
				poiSearch.searchPOIAsyn();
			} else {
				ToastUtil.show(SearchActivity.this, R.string.no_result);
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String newText = s.toString().trim();
		if (newText.length() > 0) {
			ivDelete.setImageResource(R.drawable.delete_item_unselector);
		} else if (newText.length() <= 0) {
			ivDelete.setImageResource(R.drawable.delete_item);
		}
		if (newText != null) {
			adapter = new SearchAdapter(SearchActivity.this);
			mList.clear();
			adapter.setDataSource(mList);
			lv.setAdapter(adapter);
		}
		inputTips = new Inputtips(SearchActivity.this, new InputtipsListener() {
			@Override
			public void onGetInputtips(List<Tip> tipList, int rCode) {
				if (rCode == 0) {// 正确返回
					mList = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < tipList.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put(KEY[0], R.drawable.iv_daoh);// 加入图片
						map.put(KEY[1], tipList.get(i).getName());// 文字
						map.put(KEY[2], R.drawable.iv_come);
						String city = getCity(tipList.get(i).getDistrict());
						map.put(KEY[3], city);
						mList.add(map);
					}
					adapter = new SearchAdapter(SearchActivity.this);
					adapter.setDataSource(mList);
					lv.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
			}
		});
		try {
			inputTips.requestInputtips(newText, "");// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号
		} catch (AMapException e) {
			e.printStackTrace();
		}
	}
	private String getCity(String district) {
		// 广西壮族自治区玉林市玉州区
		if (district.indexOf("市") >= 0) {
			a = district.split("市");
			city = a[0];
			if (city.indexOf("省") >= 0) {
				a = city.split("省");
				city = a[1];
			} else if (city.indexOf("区") >= 0) {
				a = city.split("区");
				city = a[1];
			}
		}
		return city;
	}
	/**
	 * Button点击事件回调方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			if (!TextUtils.isEmpty(txtFind.getText())) {
				searchButton();
			} else {
				mList.clear();
				LatLng data = (LatLng) Data.getData("LatLng");
				if(data!=null){
					Location_info info = new Location_info();
					info.setLatitude(data.latitude);
					info.setLongitude(data.longitude);
					Bundle bundle = new Bundle();
					bean = new SearchBean();
					if(Data.getData("wk")!=null)
					bean.setAddress(Data.getData("wk").toString());
					bean.setHire_field(getField());
					bean.setHire_method_id(getData());
					bean.setLimit(10);
					bean.setSkip(0);
					bean.setLocation_info(info);
					bean.setMode(getMode());
					bundle.putString("mode", getMode());// 道路社区
					bundle.putString("objectId", getData());// 车位id
					bundle.putString("getField", getField());//
					bundle.putSerializable("bean", bean);
					if (getHire_type() == 2) {
						bundle.putInt("hire_type", getHire_type());
					}
					Intent intent = new Intent(SearchActivity.this,
							AMapActivity.class);
					intent.putExtras(bundle);
					hintKbTwo();
					startActivity(intent);
				}
			}
			break;
		case R.id.iv_delete:// 删除时清空数据
			txtFind.setText("");
			adapter = new SearchAdapter(SearchActivity.this);
			mList.clear();
			adapter.setDataSource(mList);
			lv.setAdapter(adapter);
			break;
		case R.id.iv_back:// 点击回退到 fragment //隐藏软键盘
			hintKbTwo();
			this.finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 关闭软键盘
	 */
	private void hintKbTwo() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive() && getCurrentFocus() != null) {
			if (getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	/**
	 * 点击搜索按钮
	 */
	public void searchButton() {
		String newText = txtFind.getText().toString();
		doSearchQuery(newText);
	}

	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery(String str) {
		currentPage = 0;
		query = new PoiSearch.Query(str, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setPageSize(20);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页

		poiSearch = new PoiSearch(this, query);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}

	/**
	 * 获取地理坐标
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		// TODO Auto-generated method stub
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				LatLonPoint latLonPoint = address.getLatLonPoint();
				if (latLonPoint != null) {
					try {
						double latitude = latLonPoint.getLatitude();
						double longitude = latLonPoint.getLongitude();
						Location_info info = new Location_info();
						info.setLatitude(latitude);
						info.setLongitude(longitude);
						bean = new SearchBean();
						bean.setAddress(address.getFormatAddress());
						bean.setHire_field(getField());
						bean.setHire_method_id(getData());
						bean.setLimit(10);
						bean.setSkip(0);
						bean.setLocation_info(info);
						bean.setMode(getMode());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				ToastUtil.show(SearchActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil.show(SearchActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(SearchActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(SearchActivity.this, getString(R.string.error_other)
					+ rCode);
		}
	}

	public static String s = "";
	public static LatLng latLng;
	private Handler mHandler = new Handler() {
	};
	private Inputtips inputTips;
	private ImageView ivBack;
	private SearchBean bean;

	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {

	}

	/**
	 * 设置点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (position != 0) {
			String str = (String) mList.get(position - 1).get(
					SearchActivity.KEY[1]);
			String strCity = (String) mList.get(position - 1).get(
					SearchActivity.KEY[3]);
			String strSearch = getStrSearch(str);
			getLatlon(strSearch, strCity);
			view.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (latLng != null) {
						try {
							double latitude = latLng.latitude;
							double longitude = latLng.longitude;
							// TODO Auto-generated method stub
							Bundle bundle = new Bundle();
							bundle.putDouble("latitude", latitude);
							bundle.putDouble("longitude", longitude);
							Intent intent = new Intent(SearchActivity.this,
									AMapActivity.class);
							intent.putExtras(bundle);
							hintKbTwo();
							startActivity(intent);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						Bundle bundle = new Bundle();
						bundle.putString("mode", getMode());// 道路社区
						bundle.putString("objectId", getData());// 车位id
						bundle.putString("getField", getField());//
						bundle.putSerializable("bean", bean);
						if (getHire_type() == 2) {
							bundle.putInt("hire_type", getHire_type());
						}
						Intent intent = new Intent(SearchActivity.this,
								AMapActivity.class);
						intent.putExtras(bundle);
						hintKbTwo();
						startActivity(intent);
					}
				}
			}, 1000);
		}
	}
	private String getStrSearch(String str) {
		String param = null;
		if (str.indexOf("(") != -1) {
			String string = str.substring(0, str.indexOf("("));
			param = string + " "
					+ str.substring(str.indexOf("(") + 1, str.indexOf(")"));
		} else {
			param = str;
		}
		return param;
	}

	/**
	 * 判断
	 */
	private int getHire_type() {
		Bundle bundle = getIntent().getExtras();
		if (bundle.getInt("hire_type") == 2) {
			return bundle.getInt("hire_type");
		} else {
			return -1;
		}
	}
	/**
	 * 回车键监听 回车之后走搜索 这一步没必要 现在都是下一步了
	 */
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// 在这里编写自己想要实现的功能
		if (actionId == EditorInfo.IME_ACTION_SEND) {
		}
		return false;
	}

	/**
	 * 删除具体处理
	 */
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DEL) {
			if (txtFind == null) {
				adapter = new SearchAdapter(SearchActivity.this);
				mList.clear();
				adapter.setDataSource(mList);
				lv.setAdapter(adapter);
			}
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
		MobclickAgent.onPause(this);
	}
	/**
	 * poi搜索
	 */
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
					mList.clear();
					if (poiItems != null && poiItems.size() > 0) {
						ArrayList<PoiItem> pois = result.getPois();
						for (int i = 0; i < pois.size(); i++) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put(KEY[0], R.drawable.iv_daoh);// 加入图片
							map.put(KEY[1], result.getPois().get(i).toString());// 文字
							map.put(KEY[2], R.drawable.iv_come);
							map.put(KEY[3], getCity(result.getPois().get(i).getCityName()));
							mList.add(map);
						}
						adapter = new SearchAdapter(SearchActivity.this);
						adapter.setDataSource(mList);
						lv.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}
				}
			}
		} else {
			ToastUtil.show(SearchActivity.this, R.string.no_result);
		}
	}
	/** 停止刷新， */
	private void onLoad() {
		lv.stopLoadMore();
		lv.setRefreshTime("刚刚");
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			public void run() {
				nextPage();
				onLoad();
			}
		}, 2000);
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		searchButton();
		onLoad();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
