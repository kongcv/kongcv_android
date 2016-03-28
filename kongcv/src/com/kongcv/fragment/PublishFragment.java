package com.kongcv.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kongcv.R;
import com.kongcv.activity.HomeActivity;
import com.kongcv.activity.LogInActivity;
import com.kongcv.activity.SearchPublishActivity;
import com.kongcv.adapter.PublishTypeAdapter;
import com.kongcv.adapter.PublishTypeAdapter.UpdateList;
import com.kongcv.calendar.PickDialog;
import com.kongcv.calendar.PickDialogListener;
import com.kongcv.calendar.TypeDialog;
import com.kongcv.calendar.TypeDialog.LeaveMyDialogListener;
import com.kongcv.global.CarBean;
import com.kongcv.global.Community;
import com.kongcv.global.Information;
import com.kongcv.global.LocationInfo;
import com.kongcv.global.MineCarmanagerBean;
import com.kongcv.global.PublishBean;
import com.kongcv.global.RegistionBean;
import com.kongcv.global.SimpleSpinnerOption;
import com.kongcv.global.TypeBean;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.Data;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.ToastUtil;
import com.kongcv.view.MultiSpinner;
import com.kongcv.view.MySwitch;
import com.kongcv.view.MySwitch.OnCheckedChangeListener;

/**
 * 发布页面
 * 
 * @author kcw001
 */
public class PublishFragment extends Fragment implements OnClickListener {

	private static final String TAG = "PublishFragment";
	private HomeActivity homeActivity;
	private ScrollView mScrollView;
	private LinearLayout mLayout;
	private ImageView ivPublish, ivType;
	private TextView mDay, mTime, tvTorB, mTxtStart, mTxtEnd, mAddress,
			tv_City;
	private PickDialog dialog;
	private MultiSpinner mTvNoOr;
	private int num = 0;
	private AlertDialog dialog3;
	private AlertDialog.Builder builder;
	private Spinner spinner;
	private TypeDialog dialog2;
	private ACacheUtils mCache;
	private PublishBean bean;
	private String address;
	private String park_descriptions;
	private int park_area;
	private int park_height;
	private String park_details;
	private List<String> hire_method_id_list;
	private String firstAddress;
	private String tail_num;
	private String startTime;
	private String endTime;
	private int park_struct;
	private CarBean car;
	private boolean normal;
	private String gate_cards;
	private List<String>hire_price_list;
	private List<String> hire_time_list;
	/**
	 * 发布
	 */
	private EditText park_detail, park_description, tv_car_num, tv_car_area,
			tv_car_high, gate_card;
	ArrayList<TypeBean> items;
	public static ArrayList<TypeBean> mydialog;
	private ArrayList<TypeBean> mydialog2=new ArrayList<TypeBean>();
	
	PublishTypeAdapter adapter;
	List<String> hire_method_id = new ArrayList<String>();
	List<String> hire_price = new ArrayList<String>();
	List<String> hire_time = new ArrayList<String>();
	List<String> hire_field = new ArrayList<String>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mScrollView = (ScrollView) inflater.inflate(
				R.layout.publish_community_activity, container, false);
		homeActivity = (HomeActivity) getActivity();
//		mFragmentManager = getActivity().getSupportFragmentManager();
		mCache = ACacheUtils.get(getActivity());
		initData();
		if (HomeActivity.CWGL == 2) {
			visibleData();
		}
		return mScrollView;
	}

	/**
	 * 图片点击事件
	 */
	private void initData() {
		bean = new PublishBean();
		tvTorB = (TextView) mScrollView.findViewById(R.id.tv_topOrbutm);
		ivPublish = (ImageView) mScrollView.findViewById(R.id.iv_publish);
		ivType = (ImageView) mScrollView.findViewById(R.id.iv_type);
		mTvNoOr = (MultiSpinner) mScrollView.findViewById(R.id.tv_NoOr);

		textstart = (TextView) mScrollView.findViewById(R.id.publish_tv_timerstart);// 出租时间
		textend = (TextView) mScrollView.findViewById(R.id.tv_timerend);// 截至日期
		textstart.setOnClickListener(this);
		textend.setOnClickListener(this);
		
		view = mScrollView.findViewById(R.id.rl_publish_header);

		park_detail = (EditText) mScrollView.findViewById(R.id.park_detail);
		park_description = (EditText) mScrollView
				.findViewById(R.id.park_description);
		tv_car_num = (EditText) mScrollView.findViewById(R.id.tv_car_num);
		// 城市注释掉了
		tv_City = (TextView) mScrollView.findViewById(R.id.tv_City);
		tv_car_area = (EditText) mScrollView.findViewById(R.id.tv_car_area);
		tv_car_high = (EditText) mScrollView.findViewById(R.id.tv_car_high);
		gate_card = (EditText) mScrollView.findViewById(R.id.gate_card);

		mySwitch = (MySwitch) mScrollView.findViewById(R.id.my_switch);
		mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(MySwitch view, boolean isChecked) {
				if (isChecked) {
					bean.setNormal(true);
				} else {
					bean.setNormal(false);
				}
			}
		});
		mySwitch.setChecked(false);
		
		view.setOnClickListener(this);
		tvTorB.setOnClickListener(this);
		ivType.setOnClickListener(this);
		ivPublish.setOnClickListener(this);

		mAddress = (TextView) mScrollView.findViewById(R.id.tv_getSearch);
		mAddress.setText((CharSequence) Data.getData("address"));
		mListView = (ListView) mScrollView.findViewById(R.id.listview);
		tv_City.setText((CharSequence) Data.getData("wk"));
		initDay();
	}

	/**
	 * 从车位管理跳转到此，显示相应的数据
	 */
	private int visibleInit=0;
	private void visibleData() {
		mydialog= new ArrayList<TypeBean>();
		visibleInit=1;
		Bundle extras = getArguments();
		if(extras!=null){
			MineCarmanagerBean carmanagerBean = (MineCarmanagerBean) extras.getSerializable("MineCarmanagerBean");
			if(carmanagerBean!=null){
				address=carmanagerBean.getAddress();
				firstAddress = address.substring(0, address.indexOf("&"));
				park_details = address.substring(address.indexOf("&") + 1,
						address.length());
				park_detail.setText(park_details);
				mAddress.setText(firstAddress);
				tv_car_area.setText(carmanagerBean.getPark_area()+"");
				tv_car_high.setText(carmanagerBean.getPark_height()+"");
				mTvNoOr.setText(carmanagerBean.getNo_hire());
				gate_card.setText(carmanagerBean.getGate_card());
				park_description.setText(carmanagerBean.getPark_description());
				tv_car_num.setText(carmanagerBean.getTail_num());
				
				textstart.setText(carmanagerBean.getHire_start());
				textend.setText(carmanagerBean.getHire_end());
				
				Community community = (Community) Data.getData("community");
				List<String> objectId = community.getObjectId();//类型id
				List<String> method = community.getMethod();//类型名称'
				List<String> hireField = community.getHire_field();
		//		ArrayList<TypeBean> item=new ArrayList<TypeBean>();
				List<String> hireMethodList = carmanagerBean.getHire_method_id();
				List<String> hirePriceList = carmanagerBean.getHire_price();
				List<String> hireTimeList = carmanagerBean.getHire_time();
				typeBeansList = new ArrayList<TypeBean>();
				for(int i=0;i<hireMethodList.size();i++){
					TypeBean typeBean=new TypeBean();
					
					for(int ii=0;ii<objectId.size();ii++){
						if(objectId.get(ii).equals(hireMethodList.get(i))){
							typeBean.setDate(hireTimeList.get(i).toString()==null?"0":hireTimeList.get(i).toString());
							typeBean.setPrice(hirePriceList.get(i).toString());
							typeBean.setMethod(method.get(ii).toString());
							
							hire_time.add(hireTimeList.get(i).toString()==null?"0":
								hireTimeList.get(i).toString());
							hire_field.add(hireField.get(ii).toString());
							hire_method_id.add(hireMethodList.get(i).toString());
							hire_price.add(hirePriceList.get(i).toString());
						}
					}
					mydialog.add(typeBean);
				}
				typeBeansList=mydialog;
				
				LocationInfo info=new LocationInfo();
				info.set_type("GeoPoint");
				info.setLatitude(carmanagerBean.getLatitude());
				info.setLongitude(carmanagerBean.getLongitude());
				bean.setLocationInfo(info);
				
				
				bean.setHireEnd(carmanagerBean.getHire_end());
				bean.setHireStart(carmanagerBean.getHire_start());
				normal = carmanagerBean.isNormal();
				if(normal){
					mySwitch.setChecked(!normal);
				}else{
					mySwitch.setChecked(normal);
				}
				adapter=new PublishTypeAdapter(homeActivity, mydialog);
				mListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				ToastUtil.fixListViewHeight(mListView, -1);
				if (0 == carmanagerBean.getPark_struct()) {
					tvTorB.setText("地上");
				} else {
					tvTorB.setText("地下");
				}
				bean.setAddress(carmanagerBean.getAddress());
				bean.setCity(carmanagerBean.getCity());
				bean.setGateCard(carmanagerBean.getGate_card());
				bean.setNormal(normal);
			}
		}
	}

	public static String[] days = new String[] { "一", "二", "三", "四", "五", "六",
			"日" };

	private void initDay() {
		// TODO Auto-generated method stub
		mTvNoOr.setTitle("周数选择");
		ArrayList multiSpinnerList = new ArrayList();
		for (int i = 0; i < 7; i++) {
			SimpleSpinnerOption option = new SimpleSpinnerOption();
			option.setName(days[i]);
			option.setValue(i + 1);
			multiSpinnerList.add(option);
		}
		mTvNoOr.setDataList(multiSpinnerList);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.publish_tv_timerstart:// 点击出现日历弹框
			dialog = new PickDialog(homeActivity, new PickDialogListener() {
				@Override
				public void refreshPriorityUI(String str) {
					// TODO Auto-generated method stub
					textstart.setText(str);
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.show();
			break;
		case R.id.tv_timerend:
			dialog = new PickDialog(homeActivity, new PickDialogListener() {
				@Override
				public void refreshPriorityUI(String str) {
					// TODO Auto-generated method stub
					textend.setText(str);
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.show();
			break;
		case R.id.tv_topOrbutm:// 车位类型 车上车下
			topOrbutm();
			break;
		case R.id.iv_type:
			typeCar();
			break;
		case R.id.iv_publish: // 将订单消息数据 提交到后台
			if (ToastUtil.isLogIn(mCache.getAsString("USER"))) {
				if (mydialog != null) {//
					postData(mydialog);
				} else {
					ToastUtil.show(getActivity(), "请点击选择类型！");
				}
			} else {
				Intent intent = new Intent(getActivity(), LogInActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.rl_publish_header:
			// 点击到搜索页面
			Intent intent = new Intent(homeActivity,
					SearchPublishActivity.class);
			startActivityForResult(intent, 0);
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (data != null) {// 获取子activity put的数据
				String str = data.getStringExtra("addressName");
				String tvCity = data.getStringExtra("tv_City");

				mAddress.setText(str);// 设置文字
				/*LocationInfo info = (LocationInfo) Data
						.getData("location_info");*/
				LocationInfo info=(LocationInfo) data.getSerializableExtra("info");
				bean.setLocationInfo(info);
				bean.setCity(tvCity);
				tv_City.setText(tvCity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ---------------------------------必要的重写方法--------------------------------------------------
	@Override
	public void onResume() {
		super.onResume();
//		HomeActivity.curFragmentTag = getString(R.string.mpublic_fg);
	}

	private int indexOf=0;
	private int iiii=0;
	private void typeCar() {
		if(mydialog==null){
			mydialog=new  ArrayList<TypeBean>();
		}
		
		dialog2 = new TypeDialog(homeActivity, R.style.CustomDialog,
				new LeaveMyDialogListener() {
					@Override
					public void refreshUI(ArrayList<TypeBean> item) {
						// TODO Auto-generated method stub
						for(int i=0;i<item.size();i++){
							System.out.println("item>>>>>>>>>>>>>>>>>>>>>>>>>>"+item.get(i).getDate());
						}
						if (null == items) {
							if(visibleInit==0){
  								items = item;
							}else{
								items=typeBeansList;
								for(int i=0;i<item.size();i++){
									if(hire_field.contains(item.get(i).getField())){
										indexOf = hire_field.indexOf(item.get(i).getField());
										hire_time.set(indexOf, item.get(i).getDate()==null?"0":item.get(i).getDate());
										hire_price.set(indexOf, item.get(i).getPrice());
										
										items.get(i).setDate(item.get(i).getDate()==null?"0":item.get(i).getDate());
										items.get(i).setPrice(item.get(i).getPrice());
										items.get(i).setField(hire_field.get(i).toString());
										items.get(i).setObjectId(hire_method_id.get(i).toString());
									}else{
										Log.e("for if", "false");
										items.addAll(item);
									}
								}
							}
						} else {
							Log.e("items!=null", "items!=null");
							if(visibleInit==0){
								for(int i=0;i<item.size();i++){
									if(hire_field.contains(item.get(i).getField())){
										indexOf = hire_field.indexOf(item.get(i).getField());
										hire_time.set(indexOf, item.get(i).getDate()==null?"0":item.get(i).getDate());
										hire_price.set(indexOf, item.get(i).getPrice());
										
										items.get(i).setDate(item.get(i).getDate()==null?"0":item.get(i).getDate());
										items.get(i).setPrice(item.get(i).getPrice());
										items.get(i).setField(hire_field.get(i).toString());
										items.get(i).setObjectId(hire_method_id.get(i).toString());
									}else{
										items.addAll(item);
									}
								}
							}else{
								for(int i=0;i<item.size();i++){
									/*if(hire_field.contains(item.get(i).getField())){
										indexOf = hire_field.indexOf(item.get(i).getField());
										hire_time.set(indexOf, item.get(i).getDate()==null?"0":item.get(i).getDate());
										hire_price.set(indexOf, item.get(i).getPrice());
										items.get(i).setDate(item.get(i).getDate()==null?"0":item.get(i).getDate());
										items.get(i).setPrice(item.get(i).getPrice());
									}else{
										for(int index=0;index<items.size();i++){
											TypeBean typeBean=new TypeBean();
											typeBean.setDate(item.get(i).getDate()==null?"0":item.get(i).getDate());
											typeBean.setField(item.get(index).getField());
											typeBean.setObjectId(item.get(index).getObjectId());
											typeBean.setMethod(item.get(index).getMethod());
											items.add(typeBean);
										}
									}*/
									TypeBean typeBean=new TypeBean();
									typeBean.setDate(item.get(i).getDate());
									typeBean.setField(item.get(i).getField());
									typeBean.setObjectId(item.get(i).getObjectId());
									typeBean.setMethod(item.get(i).getMethod());
									items.add(typeBean);
								}
							}
						}
						mydialog=items;
						
						adapter = new PublishTypeAdapter(homeActivity,
								items, new UpdateList() {
									@Override
									public void deteleList(
											ArrayList<TypeBean> arrayList) {
										// TODO Auto-generated method stub
										items = arrayList;
									}
								});
						mListView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						ToastUtil.fixListViewHeight(mListView, -1);
					}
				});
		dialog2.setContentView(R.layout.publish_type_dialog);
		dialog2.show();
	}
	
	private TextView textstart;
	private TextView textend;
	private View view;
	private ListView mListView;
	private MySwitch mySwitch;
	private ArrayList<TypeBean> typeBeansList;
	
	private void topOrbutm() {
		builder = new Builder(getActivity());
		builder.setTitle("选择车位类型");
		final String[] items = new String[] { "地上", "地下" };// 默认状态是地上

		builder.setSingleChoiceItems(items, num,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (1 == which) {
							num = 1;
						} else {
							num = 0;
						}
					}
				});
		// 确定框
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (1 == num) {
					tvTorB.setText(items[num]);
					bean.setPark_struct(1);
				} else {
					tvTorB.setText(items[num]);
					bean.setPark_struct(0);
				}
			}
		});
		dialog3 = builder.create();
		dialog3.show();
	}

	/**
	 * 发布页面的数据
	 */
	private void postData(ArrayList<TypeBean> arrList) {
		try {
			if(arrList!=null){
				if(visibleInit==0){
					for (int i = 0; i < arrList.size(); i++) {
						if(!hire_price.contains(arrList.get(i).getPrice()) && !hire_method_id.contains(arrList.get(i).getObjectId())){
							hire_price.add(arrList.get(i).getPrice());
						}else{
							hire_price.set(hire_method_id.indexOf(arrList.get(i).getObjectId()),
									arrList.get(i).getPrice());
						}
						
						if(!hire_method_id.contains(arrList.get(i).getObjectId())){
							hire_method_id.add(arrList.get(i).getObjectId());
						}
						/*if (arrList.get(i).getDate() != null && !hire_time.contains(arrList.get(i).getDate())) {
							hire_time.add(i,arrList.get(i).getDate()==null?"0":arrList.get(i).getDate());
						}*/
						hire_time.add(arrList.get(i).getDate());
						if(!hire_field.contains(arrList.get(i).getField())){
							hire_field.add(arrList.get(i).getField());
						}
					}
				}
				bean.setHire_field(hire_field);
				bean.setHirePrice(hire_price);
				bean.setHireTime(hire_time);
				bean.setHireMethodId(hire_method_id);
			}
			String str = mTvNoOr.getText().toString();
			String[] strArray = str.split(",");
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < strArray.length; i++) {
				list.add(strArray[i]);
			}

			bean.setUserId(mCache.getAsString("user_id"));
			bean.setAddress(mAddress.getText().toString());
			
			bean.setParkDetail(park_detail.getText().toString().trim());
			bean.setParkDescription(park_description.getText().toString()
					.trim());
			bean.setHireStart(textstart.getText().toString() + " 00:00:00");
			bean.setHireEnd(textend.getText().toString() + " 00:00:00");
			bean.setNoHire(list);
			bean.setTailNum(tv_car_num.getText().toString().trim());
			// bean.setCity(tv_City.getText().toString());// 自动定位获取得到
			Log.e("hire_method_id",hire_method_id+"::");
			Log.e("hire_field",hire_field+"::");
			Log.e("hire_price",hire_price+"::");
			Log.e("hire_field",hire_field+"::");
			
			
			Log.e("getHireMethodId",bean.getHireMethodId()+"::");
			Log.e("getHire_field",bean.getHire_field()+"::");
			Log.e("getHirePrice",bean.getHirePrice()+"::");
			Log.e("getHireTime",bean.getHireTime()+"::");
			
			Double area = Double.parseDouble(tv_car_area.getText().toString());
			bean.setParkArea(area);
			bean.setParkHeight(Double.parseDouble(tv_car_high.getText()
					.toString()));
			bean.setGateCard(gate_card.getText().toString());
			bean.setMode("community");
			bean.setPersonal(1);
			
			bean.setHire_field(hire_field);
			bean.setHirePrice(hire_price);
			bean.setHireTime(hire_time);
			bean.setHireMethodId(hire_method_id);
			
			Gson gson = new Gson();
			String json = gson.toJson(bean);
			Log.e("发布的数据是：", bean.toString());
			Log.e("发布的数据是：", json);
			
			MyThread thread = new MyThread(json);
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class MyThread extends Thread {
		private String gStr;

		public MyThread(String jStr) {
			this.gStr = jStr;
		}

		@Override
		public void run() {
			doPost(gStr);
		}
	}

	private void doPost(String gStr) {
		// TODO Auto-generated method stub
		try {
			String doHttpsPost = PostCLientUtils.doHttpsPost(
					Information.KONGCV_INSERT_PARKDATA, gStr);
			Log.e("doHttpsPost", doHttpsPost);
			JSONObject object = new JSONObject(doHttpsPost);
			String result = object.getString("result");
			JSONObject object2 = new JSONObject(result);
			String state = object2.getString("state");

			if ("ok".equals(state)) {
				Looper.prepare();
				ToastUtil.show(getActivity(), "发布成功！");
				Looper.loop();
				doRegistionId();// 发布数据之后 更新代码
			} else {
				Looper.prepare();
				ToastUtil.show(getActivity(), "信息不全！");
				Looper.loop();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doRegistionId() {
		// TODO Auto-generated method stub
		try {
			RegistionBean registionBean = new RegistionBean();
			registionBean.setMobilePhoneNumber(mCache.getAsString("USER"));
			registionBean.setUser_name(mCache.getAsString("USER"));
			registionBean.setDevice_token(mCache.getAsString("registrationID"));
			registionBean.setDevice_type("android");
			registionBean.setLicense_plate(tv_car_num.getText().toString());
			Gson gson = new Gson();
			String jsonStr = gson.toJson(registionBean);

			String doHttpsPost = PostCLientUtils.doHttpsPost(
					Information.KONGCV_PUT_USERINFO, jsonStr);
			Log.v("发布页面 doRegistionId", doHttpsPost);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
