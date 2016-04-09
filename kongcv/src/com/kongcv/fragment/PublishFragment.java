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
import android.widget.ListView;
import android.widget.ScrollView;
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

	private HomeActivity homeActivity;
	private ScrollView mScrollView;
	private ImageView ivPublish, ivType;
	private TextView tvTorB, mAddress, tv_City;
	private PickDialog dialog;
	private MultiSpinner mTvNoOr;
	private int num = 0;
	private AlertDialog dialog3;
	private AlertDialog.Builder builder;
	private TypeDialog dialog2;
	private ACacheUtils mCache;
	private PublishBean bean;
	private String address;
	private boolean normal;
	private String park_details;
	private String firstAddress;
	/**
	 * 发布
	 */
	private EditText park_detail, park_description, tv_car_num, tv_car_area,
			tv_car_high, gate_card;
	
	PublishTypeAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mScrollView = (ScrollView) inflater.inflate(
				R.layout.publish_community_activity, container, false);
		homeActivity = (HomeActivity) getActivity();
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

		textstart = (TextView) mScrollView
				.findViewById(R.id.publish_tv_timerstart);// 出租时间
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
		mListView = (ListView) mScrollView.findViewById(R.id.listview);
		initDay();
	}

	/**
	 * 从车位管理跳转到此，显示相应的数据
	 */
	private TypeBean typeBean;
	private void visibleData() {
		Bundle extras = getArguments();
		if (extras != null) {
			MineCarmanagerBean carmanagerBean = (MineCarmanagerBean) extras
					.getSerializable("MineCarmanagerBean");
			if (carmanagerBean != null) {
				address = carmanagerBean.getAddress();
				firstAddress = address.substring(0, address.indexOf("&"));
				park_details = address.substring(address.indexOf("&") + 1,
						address.length());
				park_detail.setText(park_details);
				mAddress.setText(firstAddress);
				tv_car_area.setText(carmanagerBean.getPark_area() + "");
				tv_car_high.setText(carmanagerBean.getPark_height() + "");
				mTvNoOr.setText(carmanagerBean.getNo_hire());
				gate_card.setText(carmanagerBean.getGate_card());
				park_description.setText(carmanagerBean.getPark_description());
				tv_car_num.setText(carmanagerBean.getTail_num());

				textstart.setText(carmanagerBean.getHire_start());
				textend.setText(carmanagerBean.getHire_end());

				Community community = (Community) Data.getData("community");
				List<String> objectId = community.getObjectId();// 类型id
				List<String> method = community.getMethod();// 类型名称'
				List<String> hireField = community.getHire_field();
				List<String> hireMethodList = carmanagerBean
						.getHire_method_id();
				List<String> hirePriceList = carmanagerBean.getHire_price();
				List<String> hireTimeList = carmanagerBean.getHire_time();
				
				Log.d("hireMethodList i==", hireMethodList+"<>");
				Log.d("hirePriceList i==", hirePriceList+"<>");
				Log.d("hireTimeList i==", hireTimeList+"<>");
				Log.d("objectId i==", objectId+"<>");
				Log.d("method i==", method+"<>");
				Log.d("hireField i==", hireField+"<>");
				for(int i=0;i<hireMethodList.size();i++){
					typeBean = new TypeBean();
					for(int ii=0;ii<objectId.size();ii++){
						if(objectId.get(ii).equals(hireMethodList.get(i))){
							typeBean.setField(hireField.get(ii));
							typeBean.setDate(hireTimeList.get(i));
							typeBean.setObjectId(hireMethodList.get(i));
							typeBean.setPrice(hirePriceList.get(i));
							typeBean.setMethod(method.get(ii));
						}
					}
					if(typeBean.getMethod()!=null)
					items.add(typeBean);
				}
				adapter = new PublishTypeAdapter(homeActivity, items,
						new UpdateList() {
							@Override
							public void deteleList(
									ArrayList<TypeBean> arrayList) {
								// TODO Auto-generated method stub
								items = arrayList;//删除之后的
							}
						});
				mListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				ToastUtil.fixListViewHeight(mListView, -1);
				/*for (int i = 0; i < hireMethodList.size(); i++) {
					typeBean = new TypeBean();
					for (int ii = 0; ii < objectId.size(); ii++) {
						if (objectId.get(ii).equals(hireMethodList.get(i))) {
							typeBean.setDate(hireTimeList.get(i).toString() == null ? ""
									: hireTimeList.get(i).toString());
							typeBean.setPrice(hirePriceList.get(i).toString());
							typeBean.setMethod(method.get(ii).toString());
						}
					}
					items.add(typeBean);
				}
				adapter = new PublishTypeAdapter(homeActivity, items,
						new UpdateList() {
							@Override
							public void deteleList(
									ArrayList<TypeBean> arrayList) {
								// TODO Auto-generated method stub
								items = arrayList;//删除之后的
							}
						});
				mListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				ToastUtil.fixListViewHeight(mListView, -1);*/
				LocationInfo info = new LocationInfo();
				info.set_type("GeoPoint");
				info.setLatitude(carmanagerBean.getLatitude());
				info.setLongitude(carmanagerBean.getLongitude());
				bean.setLocationInfo(info);

				bean.setHireEnd(carmanagerBean.getHire_end());
				bean.setHireStart(carmanagerBean.getHire_start());
				normal = carmanagerBean.isNormal();
				if (normal) {
					mySwitch.setChecked(!normal);
				} else {
					mySwitch.setChecked(normal);
				}
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
				postData();
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
				LocationInfo info = (LocationInfo) data
						.getSerializableExtra("info");
				bean.setLocationInfo(info);
				bean.setCity(tvCity);
				tv_City.setText(tvCity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	// ---------------------------------必要的重写方法--------------------------------------------------
	private ArrayList<TypeBean> items=new ArrayList<TypeBean>();
	private void typeCar() {
		dialog2 = new TypeDialog(homeActivity, R.style.CustomDialog,
				new LeaveMyDialogListener() {
					@Override
					public void refreshDate(TypeBean item) {
						// TODO Auto-generated method stub
						items.add(item);
						adapter = new PublishTypeAdapter(homeActivity, items,
								new UpdateList() {
									@Override
									public void deteleList(
											ArrayList<TypeBean> arrayList) {
										// TODO Auto-generated method stub
										items = arrayList;//删除之后的
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
	private List<String> hire_method_id,hire_price,hire_time,hire_field;
	private void postData() {
		try {
			if(items!=null && items.size()>0){
				hire_method_id = new ArrayList<String>();
				hire_price = new ArrayList<String>();
				hire_time = new ArrayList<String>();
				hire_field = new ArrayList<String>();
				for(int index=0;index<items.size();index++){
					hire_field.add(items.get(index).getField()==null?"0":items.get(index).getField());
					hire_price.add(items.get(index).getPrice()==null?"0":items.get(index).getPrice());
					hire_time.add(items.get(index).getDate()==null?"0":items.get(index).getDate());
					hire_method_id.add(items.get(index).getObjectId());
				}
				if(bean.getHire_field()!=null && bean.getHireTime()!=null 
						&& bean.getHireMethodId()!=null && bean.getHirePrice()!=null){
					bean.setHire_field(null);
					bean.setHirePrice(null);
					bean.setHireTime(null);
					bean.setHireMethodId(null);
				}
				bean.setHire_field(hire_field);
				bean.setHirePrice(hire_price);
				bean.setHireTime(hire_time);
				bean.setHireMethodId(hire_method_id);
			}else {
				bean.setHire_field(null);
				bean.setHirePrice(null);
				bean.setHireTime(null);
				bean.setHireMethodId(null);
				ToastUtil.show(getActivity(), "出租类型为空!");
				return;
			}
			if(mAddress.getText().toString().equals("")){
				ToastUtil.show(getActivity(), "请先点击搜索地址!");
				return;
			}
			if(park_detail.getText().toString().equals("")){
				ToastUtil.show(getActivity(), "补充详细地址不能为空!");
				return;
			}
			if(!textstart.getText().toString().equals("起租日期 ")){
				Log.d("起租日期", textstart.getText().toString());
				bean.setHireStart(textstart.getText().toString() + " 00:00:00");
			}else{
				ToastUtil.show(getActivity(), "起租日期不能为空!");
				return;
			}
			if(!textend.getText().toString().equals("截止日期 ")){
				Log.d("截止日期", textend.getText().toString());
				bean.setHireEnd(textend.getText().toString() + " 00:00:00");
			}else{
				ToastUtil.show(getActivity(), "截止日期不能为空!");
				return;
			}
			String str = mTvNoOr.getText().toString();
			if(!str.equals("请选择周数")){
				String[] strArray = str.split(",");
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < strArray.length; i++) {
					list.add(strArray[i]);
				}
				bean.setNoHire(list);
			}else{
				bean.setNoHire(null);
			}
			bean.setUserId(mCache.getAsString("user_id"));
			bean.setAddress(mAddress.getText().toString());
			bean.setParkDetail(park_detail.getText().toString().trim());
			bean.setParkDescription(park_description.getText().toString()
					.trim());
			bean.setTailNum(tv_car_num.getText().toString().trim());
		    bean.setCity(tv_City.getText().toString());// 自动定位获取得到
			if(tv_car_area.getText().toString()!=null && !tv_car_area.getText().toString().equals("")){
				Double area = Double.parseDouble(tv_car_area.getText().toString());
				bean.setParkArea(area);
			}
			if(tv_car_high.getText().toString()!=null && !tv_car_high.getText().toString().equals("")){
				bean.setParkHeight(Double.parseDouble(tv_car_high.getText().toString()));
			}
			bean.setGateCard(gate_card.getText().toString());
			bean.setMode("community");
			bean.setPersonal(1);
			bean.setHire_field(hire_field);
			bean.setHirePrice(hire_price);
			bean.setHireTime(hire_time);
			bean.setHireMethodId(hire_method_id);

			Gson gson = new Gson();
			String json = gson.toJson(bean);
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
//{"result":"{\"state\":\"error\", \"code\":10, \"error\":\"出租信息-车位有相同记录,请重新编辑位置信息\"}"}
			if ("ok".equals(state)) {
				Looper.prepare();
				ToastUtil.show(getActivity(), "发布成功！");
				Looper.loop();
				doRegistionId();// 发布数据之后 更新代码
			} else {
				String error = object2.getString("error");
				Looper.prepare();
				ToastUtil.show(getActivity(), error);
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
