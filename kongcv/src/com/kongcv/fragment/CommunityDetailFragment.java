package com.kongcv.fragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.kongcv.R;
import com.kongcv.UI.AsyncImageLoader.PreReadTask;
import com.kongcv.activity.LogInActivity;
import com.kongcv.activity.PayActivity;
import com.kongcv.activity.SearchActivity;
import com.kongcv.adapter.DetailFragmentAdapter;
import com.kongcv.adapter.DetailFragmentAdapter.ViewHolder;
import com.kongcv.adapter.PhoneNumberAdapter;
import com.kongcv.calendar.PickDialog;
import com.kongcv.calendar.PickDialogListener;
import com.kongcv.global.DetailBean;
import com.kongcv.global.DetailBean.ResultEntity;
import com.kongcv.global.DetailBean.ResultEntity.LocationEntity;
import com.kongcv.global.Information;
import com.kongcv.global.JpushBean;
import com.kongcv.global.PayOneBean;
import com.kongcv.global.UserBean;
import com.kongcv.global.ZyCommityAdapterBean;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.Data;
import com.kongcv.utils.DateUtils;
import com.kongcv.utils.GTMDateUtil;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.StringUtils;
import com.kongcv.utils.ToastUtil;

/**
 * 详情fragment 走JPush通知之类的
 */
public class CommunityDetailFragment extends Fragment implements
		OnClickListener, OnItemClickListener, OnKeyListener {

	private View view;
	private Button mInform, mPay;
	private ACacheUtils mCache;
	private ListView mListView;
	private TextView txt_start, txt_end, mTxtAddress, txt_CarArea,
			txt_CarDescribe, txt_CarHigh, txt_CarNum, txt_CarRight,
			txt_CarAddress, txt_CarCard, detail_txt_yu, time_pay_start,
			time_pay_end, tv_reserveOrpay, tv_not_rent, tv_pay_number;
	boolean isChecked;
	private DetailFragmentAdapter adapter;
	private boolean flag = true;
	public static final String KEY[] = new String[] { "ivDaoH", "txtFind",
			"ivCome" };
	ArrayList<Map<String, Object>> dataList;
	private Button consent, refuse;
	private String stringExtra;
	private PickDialog dialog;
	private PhoneNumberAdapter phoneNumberAdapter;
	private List<String> phoneList;
	private ListView mListPhone;
	private double money = 0;
	private View rl_time_pay_start;
	private View rl_time_pay_end;
	private int index = -1;
	private String mode;
	private String park_id, field, CurbMineReceiver;
	private double price;
	private String LastTradeId = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
			return view;
		}
		view = inflater.inflate(R.layout.activity_detail, null);
		mCache = ACacheUtils.get(getActivity());
		init();
		getModeAndParkId();
		getDeviceToken();
		return view;
	}

	/**
	 * 一开始获取到mode和park_id
	 */
	private ZyCommityAdapterBean mCommBean = null;

	private void getModeAndParkId() {
		Bundle arguments = getArguments();
		mode = arguments.getString("mode");
		park_id = arguments.getString("park_id");
		price = arguments.getDouble("price");

		field = arguments.getString("field");
		mineSendFragment = arguments.getString("MineSendFragment");
		strExtra = arguments.getString("stringExtra");
		CurbMineReceiver = arguments.getString("CurbMineReceiver");// 我收到的
		mCommBean = (ZyCommityAdapterBean) arguments
				.getSerializable("mCommBean");// 订单管理 跳撞过来的
		if (mCommBean != null) {
			if (mCommBean.getTrade_state() == 1) {
				mInform.setVisibility(View.GONE);
				mPay.setVisibility(View.GONE);
				visiblePhone2(mCommBean);
				tv_reserveOrpay.setText("交易完成");
			} else {
				LastTradeId = mCommBean.getObjectId();
				mInform.setVisibility(View.GONE);
				mPay.setVisibility(View.VISIBLE);
				mPay.setOnClickListener(this);
				tv_reserveOrpay.setText("金额");
				visiblePhone2(mCommBean);
			}
		}
		if (park_id == null && price != 0) {
			park_id = (String) Data.getData("PARK_ID");
		}
		if (CurbMineReceiver != null) {
			consent.setVisibility(View.VISIBLE);
			refuse.setVisibility(View.VISIBLE);
			consent.setOnClickListener(this);
			refuse.setOnClickListener(this);

			mInform.setVisibility(View.GONE);
			mPay.setVisibility(View.GONE);
		}
		Intent intent = getActivity().getIntent();
		stringExtra = intent.getStringExtra("own_device_token");
		ReadInfo task = new ReadInfo();
		task.execute();
	}

	private void visiblePhone2(ZyCommityAdapterBean mCommBean2) {
		// TODO Auto-generated method stub
		mListPhone.setVisibility(View.VISIBLE);
		phoneList = new ArrayList<String>();
		phoneList.add(mCommBean.getMobilePhoneNumber());
		if (phoneList != null && phoneList.size() > 0) {
			phoneNumberAdapter = new PhoneNumberAdapter(getActivity(),
					phoneList);
			mListPhone.setAdapter(phoneNumberAdapter);
			phoneNumberAdapter.notifyDataSetChanged();
			ToastUtil.fixListViewHeight(mListPhone, -1);
		}
	}

	/**
	 * 打开jpush消息通知调到页面并返回携带的数据
	 */
	private void getDeviceToken() {
		if (stringExtra != null) {
			Log.v("打开通知栏获取的消息回调！", stringExtra);
			Gson gson = new Gson();
			JpushBean fromJson = gson.fromJson(stringExtra, JpushBean.class);
			if (stringExtra != null
					&& "verify_request".equals(fromJson.getPush_type())) {
				consent.setVisibility(View.VISIBLE);// 出租人设置接收拒绝按钮可见
				refuse.setVisibility(View.VISIBLE);
				rl_time_pay_start.setVisibility(View.GONE);
				rl_time_pay_end.setVisibility(View.GONE);
				mInform.setVisibility(View.GONE);// 求租人 订单确认不可见
				mPay.setVisibility(View.GONE);// 求租人 支付按钮可见
			} else if (stringExtra != null
					&& "verify_accept".equals(fromJson.getPush_type())) {
				rl_time_pay_start.setVisibility(View.VISIBLE);
				rl_time_pay_end.setVisibility(View.VISIBLE);
				refuse.setVisibility(View.GONE);// 出租人接收或拒绝按钮不可见
				consent.setVisibility(View.GONE);// 出租人接收或拒绝按钮不可见
				mInform.setVisibility(View.GONE);// 求租人 订单确认不可见
				mPay.setVisibility(View.VISIBLE);// 求租人 支付按钮可见
				visiblePhone(stringExtra);
			} else if (stringExtra != null
					&& "verify_reject".equals(fromJson.getPush_type())) {
				mInform.setVisibility(View.GONE);
				mPay.setVisibility(View.GONE);
				mPay.setOnClickListener(null);
			}
		}
	}

	/**
	 * 出租人接收设置电话号码求租人可见
	 */
	private void visiblePhone(String stringExtra) {
		Gson gson = new Gson();
		JpushBean fromJson = gson.fromJson(stringExtra, JpushBean.class);
		String own_mobile = fromJson.getOwn_mobile();
		mListPhone.setVisibility(View.VISIBLE);
		phoneList = new ArrayList<String>();
		phoneList.add(own_mobile);
		if (phoneList != null && phoneList.size() > 0) {
			phoneNumberAdapter = new PhoneNumberAdapter(getActivity(),
					phoneList);
			mListPhone.setAdapter(phoneNumberAdapter);
			phoneNumberAdapter.notifyDataSetChanged();
			// ToastUtil.fixListViewHeight(mListPhone);
		}
	}

	class ReadInfo extends PreReadTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			Details();
			return null;
		}
	}

	private void init() {
		mListPhone = (ListView) view.findViewById(R.id.detail_lv_phone);
		detail_txt_yu = (TextView) view.findViewById(R.id.detail_txt_yu);
		mTxtAddress = (TextView) view.findViewById(R.id.txt_detail);
		txt_start = (TextView) view.findViewById(R.id.txt_start);// 开始时间和结束时间
		txt_end = (TextView) view.findViewById(R.id.txt_end);
		txt_CarArea = (TextView) view.findViewById(R.id.tv_car_area);
		txt_CarHigh = (TextView) view.findViewById(R.id.tv_car_high);
		txt_CarNum = (TextView) view.findViewById(R.id.tv_car_num);
		txt_CarRight = (TextView) view.findViewById(R.id.tv_car_right);
		txt_CarAddress = (TextView) view.findViewById(R.id.tv_car_address);
		txt_CarCard = (TextView) view.findViewById(R.id.tv_access_card);// 门禁卡
		txt_CarDescribe = (TextView) view.findViewById(R.id.tv_car_describe);
		tv_not_rent = (TextView) view.findViewById(R.id.tv_not_rent);
		tv_reserveOrpay = (TextView) view.findViewById(R.id.tv_reserveOrpay);// 需要更改的文字提示
		tv_pay_number = (TextView) view.findViewById(R.id.tv_pay_number);// 显示计算好的价钱

		mInform = (Button) view.findViewById(R.id.inform);
		mPay = (Button) view.findViewById(R.id.detail_pay_btn);
		mInform.setOnClickListener(this);
		mPay.setOnClickListener(this);

		mListView = (ListView) view.findViewById(R.id.detail_listview);
		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 设置位单选
		mListView.setOnItemClickListener(this);

		consent = (Button) view.findViewById(R.id.detail_button_consent);
		refuse = (Button) view.findViewById(R.id.detail_button_refuse);
		consent.setOnClickListener(this);
		refuse.setOnClickListener(this);

		time_pay_start = (TextView) view.findViewById(R.id.time_pay_start);
		time_pay_end = (TextView) view.findViewById(R.id.time_pay_end);
		time_pay_start.setOnClickListener(this);
		time_pay_end.setOnClickListener(this);
		rl_time_pay_start = view.findViewById(R.id.rl_time_pay_start);
		rl_time_pay_end = view.findViewById(R.id.rl_time_pay_end);
	}

	private List<String> hire_price;
	private String user;
	private ResultEntity result;
	private PayOneBean payOneBean;
	private UserBean userBean;
	private String jpushStr;
	private Gson gson;
	public static LocationEntity location;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				String response = (String) msg.obj;
				gson = new Gson();
				DetailBean bean = gson.fromJson(response, DetailBean.class);
				result = bean.getResult();
				payOneBean = new PayOneBean();
				location = result.getLocation();
				String address = result.getAddress();
				String s = new String(address);
				String a[] = s.split("&");
				mTxtAddress.setText(a[0]);
				detail_txt_yu.setText(a[1]);
				/*if (stringExtra == null) {
					if (GTMDateUtil.GTMToLocal(result.getHire_start().getIso(),
							false) != null
							&& GTMDateUtil.GTMToLocal(result.getHire_end()
									.getIso(), false) != null) {
						txt_start.setText(GTMDateUtil.GTMToLocal(result
								.getHire_start().getIso(), false));
						txt_end.setText(GTMDateUtil.GTMToLocal(result
								.getHire_end().getIso(), false));
					} else {
						txt_start.setText(" 年  月  日");
						txt_end.setText(" 年  月  日");
					}
				} else {
					JpushBean fromJson = gson.fromJson(stringExtra,
							JpushBean.class);
					time_pay_start.setText(fromJson.getHire_start());
					time_pay_end.setText(fromJson.getHire_end());
					txt_start.setText(GTMDateUtil.GTMToLocal(result
							.getHire_start().getIso(), false));
					txt_end.setText(GTMDateUtil.GTMToLocal(result
							.getHire_end().getIso(), false));
				}*/
				
				txt_start.setText(GTMDateUtil.GTMToLocal(result
						.getHire_start().getIso(), false));
				txt_end.setText(GTMDateUtil.GTMToLocal(result
						.getHire_end().getIso(), false));
				if(mCommBean!=null){
					Log.d("发送的jpush消息为0吗?", stringExtra+"<><>");
					JpushBean fromJson = gson.fromJson(stringExtra,
							JpushBean.class);
					time_pay_start.setVisibility(View.VISIBLE);
					time_pay_end.setVisibility(View.VISIBLE);
					time_pay_start.setText(fromJson.getHire_start());
					time_pay_end.setText(fromJson.getHire_end());
				}
				if (stringExtra != null) {
					Log.d("发送的jpush消息为0吗?", stringExtra+"<><>");
					JpushBean fromJson = gson.fromJson(stringExtra,
							JpushBean.class);
					time_pay_start.setVisibility(View.VISIBLE);
					time_pay_end.setVisibility(View.VISIBLE);
					time_pay_start.setText(fromJson.getHire_start());
					time_pay_end.setText(fromJson.getHire_end());
				}
				
				// 不可出租日
				List<String> no_hire = result.getNo_hire();
				tv_not_rent.setText(StringUtils.listToString(no_hire));

				if (!result.getPark_description().equals("")) {
					txt_CarDescribe.setText(result.getPark_description());
				}
				txt_CarArea.setText(result.getPark_area() + "㎡");// 车位面积
				txt_CarHigh.setText(result.getPark_height() + "m");
				txt_CarNum.setText(result.getTail_num());
				if (result.getStruct() == 0) {
					txt_CarAddress.setText("地上");
				} else {
					txt_CarAddress.setText("地下");
				}
				if (!result.getGate_card().equals("")) {
					txt_CarCard.setText(result.getGate_card());
				}
				if (result.isNormal() == true) {
					txt_CarRight.setText("是");
				} else {
					txt_CarRight.setText("否");
				}
				if (result.getPark_space() == 0) {
					mInform.setText("此车位已出租");
					mInform.setOnClickListener(null);
				}
				List<String> hire_time = result.getHire_time();
				hire_price = result.getHire_price();
				String hire_method = result.getHire_method();
				Data.putData("hire_method", result);
				setAdapter(hire_time, hire_price, hire_method);
				user = result.getUser();
				Log.e("user", "user");
				gson = new Gson();
				userBean = gson.fromJson(user, UserBean.class);
				/**
				 * 租用时间 租用时间这个 需要处理一部分
				 */
			/*	payOneBean.setHire_start(GTMDateUtil.GTMToLocal(result
						.getHire_start().getIso(), true));
				payOneBean.setHire_end(GTMDateUtil.GTMToLocal(result
						.getHire_end().getIso(), true));*/
				
				payOneBean.setUser_id(mCache.getAsString("user_id"));
				if (user.contains("license_plate")) {
					payOneBean.setLicense_plate(userBean.getLicense_plate());// 车牌号
				}
				payOneBean.setHirer_id(userBean.getObjectId());
				payOneBean.setPark_id(park_id);
				payOneBean.setMode(mode);// 社区道路这个值需要传递
				/*String days = DateUtils.getDays(payOneBean.getHire_start(),
						payOneBean.getHire_end(), true);*/
				String days = DateUtils.getDays(GTMDateUtil.GTMToLocal(result
						.getHire_start().getIso(), true),
						GTMDateUtil.GTMToLocal(result
								.getHire_end().getIso(), true), true);
				
				int dayInt = Integer.parseInt(days) + 1;
				if (dayInt > 0) {
					payOneBean.setExtra_flag("1");
				} else {
					payOneBean.setExtra_flag("0");
				}
				if (result.getProperty() != null) {
					payOneBean.setProperty_id(result.getProperty()
							.getObjectId());
				}
				if (mineSendFragment != null) {
					rl_time_pay_start.setVisibility(View.VISIBLE);
					rl_time_pay_end.setVisibility(View.VISIBLE);
					refuse.setVisibility(View.GONE);// 出租人接收或拒绝按钮不可见
					consent.setVisibility(View.GONE);// 出租人接收或拒绝按钮不可见
					mInform.setVisibility(View.GONE);// 求租人 订单确认不可见
					mPay.setVisibility(View.VISIBLE);// 求租人 支付按钮可见
					JpushBean fromJson = gson.fromJson(mineSendFragment,
							JpushBean.class);
					String hire_start = fromJson.getHire_start();
					String hire_end = fromJson.getHire_end();
					time_pay_start.setText(hire_start);
					time_pay_end.setText(hire_end);
					tv_pay_number.setText(fromJson.getPrice() + "");
					visiblePhone(stringExtra);
				}
				if (strExtra != null) {
					JpushBean fromJson = gson.fromJson(strExtra,
							JpushBean.class);
					String hire_start = fromJson.getHire_start();
					String hire_end = fromJson.getHire_end();
					time_pay_start.setText(hire_start);
					time_pay_end.setText(hire_end);
					tv_pay_number.setText(fromJson.getPrice() + "");
				}
				if (CurbMineReceiver != null) {
					JpushBean fromJson = gson.fromJson(CurbMineReceiver,
							JpushBean.class);
					String hire_start = fromJson.getHire_start();
					String hire_end = fromJson.getHire_end();
					time_pay_start.setText(hire_start);
					time_pay_end.setText(hire_end);
					tv_pay_number.setText(fromJson.getPrice() + "");
				}
				break;
			case 1:
				String trade_id = (String) msg.obj;
				Bundle bundle = new Bundle();
				if (user != null) {
					Gson ugson = new Gson();
					UserBean fromJson = ugson.fromJson(user, UserBean.class);
					String phoneNumber = fromJson.getMobilePhoneNumber();
					bundle.putString("phoneNumber", phoneNumber);
				}
				bundle.putString("trade_id", trade_id);// 订单号
				bundle.putString("price", tv_pay_number.getText().toString());
				if (stringExtra != null) {
					bundle.putString("stringExtra", stringExtra);
				} else if (mineSendFragment != null) {
					bundle.putString("stringExtra", mineSendFragment);
				} else if (CurbMineReceiver != null) {
					bundle.putString("stringExtra", CurbMineReceiver);
				}
				if (mCommBean != null) {
					bundle.putSerializable("mCommBean", mCommBean);
				}
				Intent intent = new Intent(getActivity(), PayActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case 2:
				jpushStr = (String) msg.obj;
				try {
					JSONObject obj2 = new JSONObject(jpushStr);
					String str = obj2.getString("result");
					JSONObject objStr = new JSONObject(str);
					String state = objStr.getString("state");
					if ("ok".equals(state)) {
						ToastUtil.show(getActivity(), "JPUSH!发送消息成功!");
					} else {
						ToastUtil.show(getActivity(), "订单确认失败!");
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
	private ArrayList<String> hireMethodId;
	private ArrayList<String> hireFieldList;

	private void setAdapter(List<String> hire_time, List<String> hire_price,
			String hire_method) {
		// TODO Auto-generated method stub
		try {
			hireMethodId = new ArrayList<String>();
			hireFieldList = new ArrayList<String>();
			dataList = new ArrayList<Map<String, Object>>();
			List<String> hire_name = new ArrayList<String>();
			JSONArray array = new JSONArray(hire_method);

			for (int i = 0; i < array.length(); i++) {
				String method = array.getJSONObject(i).getString("method");
				hire_name.add(method);
				String initAdapter = array.getJSONObject(i).getString("field");
				hireMethodId.add(array.getJSONObject(i).getString("objectId"));
				hireFieldList.add(initAdapter);
				if (initAdapter.equals(field)) {
					index = i;
					hire_method_id = array.getJSONObject(i).getString(
							"objectId");
					payOneBean.setHire_method_id(hire_method_id);
					if (mCommBean == null) {
						if (method.equals("计时/小时")) {
							if (hire_price.get(i).indexOf("/") != -1) {
								String[] split = hire_price.get(i).split("/");
								double p = Integer.parseInt(split[0]) / 4.00;
								java.text.DecimalFormat df = new java.text.DecimalFormat(
										"#.00");
								tv_pay_number.setText(df.format(p).toString());
							} else {
								double p = Integer.parseInt(hire_price.get(i)) / 4.00;
								java.text.DecimalFormat df = new java.text.DecimalFormat(
										"#.00");
								tv_pay_number.setText(df.format(p).toString());
							}
						}
					} else {
						tv_pay_number.setText(mCommBean.getPrice() + "");
					}
				}
				if (stringExtra != null) {
					Gson gson = new Gson();
					JpushBean jpushBean = gson.fromJson(stringExtra,
							JpushBean.class);
					if (jpushBean.getHire_method_field().equals(initAdapter)) {
						index = i;
						tv_pay_number.setText(jpushBean.getPrice() + "");
					}
				}
			}
			if (mCommBean == null) {
				for (int i = 0; i < hire_price.size(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(KEY[0], hire_name.get(i));// 地点
					map.put(KEY[1], hire_time.get(i).equals("0") ? ""
							: hire_time.get(i));// 事件a
					map.put(KEY[2], hire_price.get(i));// 价格
					dataList.add(map);
				}
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(KEY[0], hire_name.get(index));// 地点
				map.put(KEY[1], hire_time.get(index).equals("0") ? ""
						: hire_time.get(index));// 事件a
				map.put(KEY[2], hire_price.get(index));// 价格
				dataList.add(map);
			}
			if (dataList != null) {
				adapter = new DetailFragmentAdapter(getActivity());
				adapter.setDataSource(dataList);
				mListView.setAdapter(adapter);
				maps = dataList;
				ToastUtil.fixListViewHeight(mListView, -1);
				mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				if (index != -1) {
					adapter.setP(index);
				}
				adapter.notifyDataSetChanged();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.inform:// 点击发送订单
			if (mineSendFragment == null || strExtra == null
					|| CurbMineReceiver == null) {
				start = time_pay_start.getText().toString();
				end = time_pay_end.getText().toString();
				if (start.equals(" 年  月  日") || end.equals(" 年  月  日")) {
					ToastUtil.show(getActivity(), "请先选择出租日期！");
					return;
				}
			}
			if (mCache.getAsString("USER") != null
					&& mCache.getAsString("sessionToken") != null) {
				payOneBean.setPark_id(park_id);
				sendInform();
			} else {
				ToastUtil.show(getActivity(), "请先登录！");
				mHandler.postAtTime(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getActivity(),
								LogInActivity.class);
						startActivity(intent);
					}
				}, 2000);
			}
			break;
		case R.id.detail_button_consent:// 接收
			Gson gson = new Gson();
			JpushBean jpushBean = null;
			if (stringExtra != null) {
				jpushBean = gson.fromJson(stringExtra, JpushBean.class);
			}
			if (CurbMineReceiver != null) {
				jpushBean = gson.fromJson(CurbMineReceiver, JpushBean.class);
			}
			MyPushMsgState pushMsgState = new MyPushMsgState(
					jpushBean.getMessage_id(), 1);// objectId
			pushMsgState.start();
			consentJPush();
			break;
		case R.id.detail_button_refuse://
			Gson gson2 = new Gson();
			JpushBean jpushBean2 = null;
			if (stringExtra != null) {
				jpushBean2 = gson2.fromJson(stringExtra, JpushBean.class);
			}
			if (CurbMineReceiver != null) {
				jpushBean2 = gson2.fromJson(CurbMineReceiver, JpushBean.class);
			}
			MyPushMsgState pushMsgState2 = new MyPushMsgState(
					jpushBean2.getMessage_id(), 2);// objectId
			pushMsgState2.start();
			refuseJPush();
		case R.id.detail_pay_btn:
			if (LastTradeId == null) {
				try {
					Gson gson3 = new Gson();
					PayOneBean data = payOneBean;
					if (data != null) {
						JpushBean jpushBean3 = null;
						if (stringExtra != null) {
							jpushBean3 = gson3.fromJson(stringExtra,
									JpushBean.class);
						} else {
							jpushBean3 = gson3.fromJson(mineSendFragment,
									JpushBean.class);
						}
						String hire_method_id = jpushBean3.getHire_method_id();
						String hire_method = result.getHire_method();
						JSONArray array = new JSONArray(hire_method);
						for (int i = 0; i < array.length(); i++) {
							if (array.getJSONObject(i).getString("objectId")
									.equals(hire_method_id)) {
								String string = result.getHire_price().get(i);
								data.setUnit_price(string);
							}
						}
						/*payOneBean.setHire_start(GTMDateUtil.GTMToLocal(result
								.getHire_start().getIso(), true));
						payOneBean.setHire_end(GTMDateUtil.GTMToLocal(result
								.getHire_end().getIso(), true));*/
						data.setHire_start(jpushBean3.getHire_start()+" 00:00:00");
						data.setHire_end(jpushBean3.getHire_end()+" 00:00:00");
						
						data.setPrice(jpushBean3.getPrice());
						String json = gson3.toJson(data);
						doPay(json);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Message msg = mHandler.obtainMessage();
				msg.what = 1;
				msg.obj = LastTradeId;
				mHandler.sendMessage(msg);
			}
			break;
		case R.id.time_pay_start:
			dialog = new PickDialog(getActivity(), new PickDialogListener() {
				@Override
				public void refreshPriorityUI(String str) {
					// TODO Auto-generated method stub
					start = str;
					time_pay_start.setText(str);
					end = time_pay_end.getText().toString();
					if (!start.equals(" 年  月  日") && !end.equals(" 年  月  日")
							&& index != -1) {
						notRest = tv_not_rent.getText().toString();
						String days = DateUtils.getDays(start, end, true);
						String[] notRestArr = notRest.split(",");
						days = getDays(start, end, notRestArr, days);// 时间天
						String text = (String) dataList.get(index).get(
								CommunityDetailFragment.KEY[2]);
						String a[] = text.split("/");
						double price = Double.parseDouble(a[0]);
						int dayNum = Integer.parseInt(days);
						if (a[1].equals("小时")) {
							double p = price / 4.00;
							java.text.DecimalFormat df = new java.text.DecimalFormat(
									"#.00");
							tv_pay_number.setText(df.format(p).toString());
						} else if (a[1].equals("天")) {
							tv_pay_number.setText(price * dayNum + "");
						} else if (a[1].equals("月")) {
							double monthPrice = dayNum / 30.0 * price;// 这样为保持2位
							java.text.DecimalFormat df = new java.text.DecimalFormat(
									"#.00");
							String s = df.format(monthPrice);
							tv_pay_number.setText(s);
						}
					}
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.show();
			break;
		case R.id.time_pay_end:
			dialog = new PickDialog(getActivity(), new PickDialogListener() {
				@Override
				public void refreshPriorityUI(String str) {
					// TODO Auto-generated method stub
					end = str;
					time_pay_end.setText(str);
					start = time_pay_start.getText().toString();
					if (!start.equals(" 年  月  日") && !end.equals(" 年  月  日")
							&& index != -1) {
						notRest = tv_not_rent.getText().toString();
						String days = DateUtils.getDays(start, end, true);
						String[] notRestArr = notRest.split(",");
						days = getDays(start, end, notRestArr, days);// 时间天
						String text = (String) dataList.get(index).get(
								CommunityDetailFragment.KEY[2]);
						String a[] = text.split("/");
						double price = Double.parseDouble(a[0]);
						int dayNum = Integer.parseInt(days);
						if (a[1].equals("小时")) {
							double p = price / 4.00;
							java.text.DecimalFormat df = new java.text.DecimalFormat(
									"#.00");
							tv_pay_number.setText(df.format(p).toString());
						} else if (a[1].equals("天")) {
							tv_pay_number.setText(price * dayNum + "");
						} else if (a[1].equals("月")) {
							double monthPrice = dayNum / 30.0 * price;// 这样为保持2位
							java.text.DecimalFormat df = new java.text.DecimalFormat(
									"#.00");
							String s = df.format(monthPrice);
							tv_pay_number.setText(s);
						}
					}
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.show();
			break;
		default:
			break;
		}
	}

	/**
	 * 接受Jpush通知和拒绝jpush消息推送 都是发布租用信息人处理
	 */
	private void refuseJPush() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				refuse();
			}
		}).start();
	}

	private void refuse() {
		// TODO Auto-generated method stub
		try {
			UserBean fromJson = userBean;
			Gson gson = new Gson();
			JpushBean jpushBean = null;
			if (stringExtra != null) {
				jpushBean = gson.fromJson(stringExtra, JpushBean.class);
			}
			if (CurbMineReceiver != null) {
				jpushBean = gson.fromJson(CurbMineReceiver, JpushBean.class);
			}

			JSONObject obj = new JSONObject();
			obj.put("mobilePhoneNumber", jpushBean.getOwn_mobile());// 对方手机号
			obj.put("push_type", "verify_reject");// 租用请求
			obj.put("device_token", jpushBean.getOwn_device_token());
			obj.put("device_type", jpushBean.getOwn_device_type());

			String asString = mCache.getAsString("user_id");
			Log.v("asString", asString);
			obj.put("user_id", asString);// 自己的id

			JSONObject extras = new JSONObject();
			extras.put("park_id", jpushBean.getPark_id());// 得到车位的objectId
			extras.put("mode", jpushBean.getMode());
			extras.put("hire_method_id", jpushBean.getHire_method_id());
			extras.put("address", jpushBean.getAddress());
			extras.put("hire_start", jpushBean.getHire_start()); // 事件和日期
			extras.put("hire_end", jpushBean.getHire_end());
			extras.put("own_device_token",
					JPushInterface.getRegistrationID(getActivity()));

			extras.put("own_device_type", fromJson.getDevice_type());
			extras.put("own_mobile", mCache.getAsString("USER"));// 自己的手机号
			extras.put("push_type", "verify_reject");
			extras.put("price", jpushBean.getPrice()); // 价格 需要自己计算
			extras.put("hire_method_field", jpushBean.getHire_method_field());
			obj.put("extras", extras);

			String doHttpsPost = PostCLientUtils.doHttpsPost(
					Information.KONGCV_JPUSH_MESSAGE_P2P,
					JsonStrUtils.JsonStr(obj));// 发送Jpush通知

			JSONObject obj2 = new JSONObject(doHttpsPost);
			String str = obj2.getString("result");
			JSONObject objStr = new JSONObject(str);
			String state = objStr.getString("state");
			if ("ok".equals(state)) {
				Looper.prepare();
				ToastUtil.show(getActivity(), "已成功拒绝对方请求！");
				Looper.loop();
			} else {
				Looper.prepare();
				ToastUtil.show(getActivity(), "拒绝失败,请重新拒绝对方请求！");
				Looper.loop();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void consentJPush() {
		// TODO Auto-generated method stub
		try {
			UserBean fromJson = userBean;
			Gson gson = new Gson();
			JpushBean jpushBean = null;
			if (stringExtra != null) {
				jpushBean = gson.fromJson(stringExtra, JpushBean.class);
			}
			if (CurbMineReceiver != null) {
				jpushBean = gson.fromJson(CurbMineReceiver, JpushBean.class);
			}
			JSONObject obj = new JSONObject();
			obj.put("mobilePhoneNumber", jpushBean.getOwn_mobile());
			obj.put("push_type", "verify_accept");// 租用请求
			obj.put("device_token", jpushBean.getOwn_device_token());
			obj.put("device_type", jpushBean.getOwn_device_type());
			String asString = mCache.getAsString("user_id");
			obj.put("user_id", asString);
			JSONObject extras = new JSONObject();
			extras.put("park_id", jpushBean.getPark_id());// 得到车位的objectId
			extras.put("mode", jpushBean.getMode());
			extras.put("hire_method_id", jpushBean.getHire_method_id());
			extras.put("address", jpushBean.getAddress());
			extras.put("hire_start", jpushBean.getHire_start()); // 事件和日期
			extras.put("hire_end", jpushBean.getHire_end());
			// extras.put("own_device_token",
			// mCache.getAsString("RegistrationID"));
			extras.put("own_device_token",
					JPushInterface.getRegistrationID(getActivity()));
			extras.put("own_device_type", fromJson.getDevice_type());
			extras.put("own_mobile", mCache.getAsString("USER"));// 自己的手机号
			extras.put("push_type", "verify_accept");
			extras.put("price", jpushBean.getPrice());
			extras.put("hire_method_field", jpushBean.getHire_method_field());
			obj.put("extras", extras);
			Log.d("发送jpush时间>>>", JsonStrUtils.JsonStr(obj));
			Log.d("发送jpush时间>>>", JsonStrUtils.JsonStr(obj));
			okhttp3.Request request = new okhttp3.Request.Builder()
					.url(Information.KONGCV_JPUSH_MESSAGE_P2P)
					.headers(Information.getHeaders())
					.post(RequestBody.create(Information.MEDIA_TYPE_MARKDOWN,
							JsonStrUtils.JsonStr(obj))).build();
			client.newCall(request).enqueue(new okhttp3.Callback() {
				@Override
				public void onResponse(Call arg0, okhttp3.Response response)
						throws IOException {
					// TODO Auto-generated method stub
					if (response.isSuccessful()) {
						try {
							JSONObject obj2 = new JSONObject(response.body()
									.string());
							String str = obj2.getString("result");
							JSONObject objStr = new JSONObject(str);
							String state = objStr.getString("state");
							if ("ok".equals(state)) {
								Looper.prepare();
								ToastUtil.show(getActivity(), "已同意对方的支付请求！");
								Looper.loop();
							} else {
								Looper.prepare();
								ToastUtil.show(getActivity(), "接收对方请求失败！");
								Looper.loop();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					Log.e("KONGCV_JPUSH_MESSAGE_P2P", arg1.toString());
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class MyPushMsgState extends Thread {
		private String object;
		private int state;

		public MyPushMsgState(String object, int state) {
			this.object = object;
			this.state = state;
		}

		@Override
		public void run() {
			changeState(object, state);
		}
	}

	/**
	 * 改变消息推送状态
	 */
	private void changeState(String message_id, int s) {
		// TODO Auto-generated method stub
		try {
			JSONObject object = new JSONObject();
			object.put("message_id", message_id);
			object.put("state", s);
			String doHttpsPost = PostCLientUtils.doHttpsPost(
					Information.KONGCV_CHANGE_PUSHMESSAGE_STATE,
					JsonStrUtils.JsonStr(object));
			JSONObject obj = new JSONObject(doHttpsPost);
			String str = obj.getString("result");
			JSONObject objStr = new JSONObject(str);
			String state = objStr.getString("state");
			if ("ok".equals(state)) {
				Log.v("改变消息推送状态接口", "改变消息推送状态成功！");
			} else {
				Log.v("改变消息推送状态接口", "改变消息推送状态失败！");//
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 同意之后下订单支付
	 * 
	 * @param object
	 */
	private void doPay(String object) {
		// TODO Auto-generated method stub
		Log.e("同意之后下订单支付", object + ":");
		MyThread myThread = new MyThread(object);
		myThread.start();
	}

	public class MyThread extends Thread {
		private String object;

		public MyThread(String object) {
			this.object = object;
		}

		@Override
		public void run() {
			pay(object);
		}
	}

	/**
	 * 支付
	 */
	private void pay(String str) {
		// TODO Auto-generated method stub
		try {
			Log.v("pay订单！ doHttpsPost", "获取的str" + str);
			String doHttpsPost = PostCLientUtils.doHttpsPost(
					Information.KONGCV_INSERT_TRADEDATA, str);
			Log.v("pay订单！ doHttpsPost", doHttpsPost);
			JSONObject obj = new JSONObject(doHttpsPost);
			String result = obj.getString("result");
			JSONObject object = new JSONObject(result);
			String state = object.getString("state");
			if ("ok".equals(state)) {
				String trade_id = object.getString("trade_id");
				Message msg = mHandler.obtainMessage();
				msg.what = 1;
				msg.obj = trade_id;
				mHandler.sendMessage(msg);
			} else {
				Looper.prepare();
				ToastUtil.show(getActivity(), "获取订单信息失败，请重新操作！");
				Looper.loop();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private final OkHttpClient client = new OkHttpClient();

	private void sendInform() {
		// TODO Auto-generated method stub
		try {
			Gson gson = new Gson();
			JpushBean fromJson = null;
			if (mineSendFragment != null) {
				fromJson = gson.fromJson(mineSendFragment, JpushBean.class);
				mode = fromJson.getMode();
				hire_method_id = fromJson.getHire_method_id();
				field = fromJson.getHire_method_field();
			} else if (strExtra != null) {
				fromJson = gson.fromJson(strExtra, JpushBean.class);
				mode = fromJson.getMode();
				hire_method_id = fromJson.getHire_method_id();
				field = fromJson.getHire_method_field();
			} else if (CurbMineReceiver != null) {
				fromJson = gson.fromJson(CurbMineReceiver, JpushBean.class);
				mode = fromJson.getMode();
				hire_method_id = fromJson.getHire_method_id();
				field = fromJson.getHire_method_field();
			}
			UserBean data = userBean;
			JSONObject obj = new JSONObject();
			obj.put("mobilePhoneNumber", data.getMobilePhoneNumber());// 对方手机号
			obj.put("push_type", "verify_request");// 租用请求
			obj.put("device_token", data.getDevice_token());
			obj.put("device_type", data.getDevice_type());

			String asString = mCache.getAsString("user_id");
			Log.v("asString", asString);
			obj.put("user_id", asString);// 自己的id
			JSONObject extras = new JSONObject();
			if (mineSendFragment == null && strExtra == null
					&& CurbMineReceiver == null) {
				extras.put("park_id", park_id);
			} else {
				extras.put("park_id", fromJson.getPark_id());
			}
			extras.put("mode", mode);//
			extras.put("hire_method_id", hire_method_id);
			extras.put("hire_method_field", field);
			extras.put("address", mTxtAddress.getText().toString()
					+ detail_txt_yu.getText().toString());
			extras.put("hire_start", time_pay_start.getText().toString()); // 事件和日期
			extras.put("hire_end", time_pay_end.getText().toString());
			// mCache.put("RegistrationID",
			// JPushInterface.getRegistrationID(getActivity()));
			// extras.put("own_device_token",
			// mCache.getAsString("RegistrationID"));
			extras.put("own_device_token",
					JPushInterface.getRegistrationID(getActivity()));
			extras.put("own_device_type", "android");
			extras.put("own_mobile", mCache.getAsString("USER"));

			extras.put("push_type", "verify_request");
			extras.put("price",
					Double.valueOf(tv_pay_number.getText().toString()));
			obj.put("extras", extras);
			Log.v("发送jPushJPUSH!!", JsonStrUtils.JsonStr(obj));
			okhttp3.Request request = new okhttp3.Request.Builder()
					.url(Information.KONGCV_JPUSH_MESSAGE_P2P)
					.headers(Information.getHeaders())
					.post(RequestBody.create(Information.MEDIA_TYPE_MARKDOWN,
							JsonStrUtils.JsonStr(obj))).build();
			client.newCall(request).enqueue(new okhttp3.Callback() {

				@Override
				public void onResponse(Call arg0, okhttp3.Response response)
						throws IOException {
					// TODO Auto-generated method stub
					if (response.isSuccessful()) {
						Message msg = mHandler.obtainMessage();
						msg.what = 2;
						msg.obj = response.body().string();
						mHandler.sendMessage(msg);
					}
				}

				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					Log.e("KONGCV_JPUSH_MESSAGE_P2P", arg1.toString());
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 获取详情页面信息并解析
	 */
	private void Details() {
		FormBody body = new FormBody.Builder().add("park_id", park_id)
				.add("mode", mode).build();
		okhttp3.Request request = new okhttp3.Request.Builder()
				.url(Information.KONGCV_GET_PARK_INFO)
				.headers(Information.getHeaders()).post(body).build();
		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call arg0, okhttp3.Response response)
					throws IOException {
				// TODO Auto-generated method stub
				if (response.isSuccessful()) {
					try {
						String string = response.body().string();
						JSONObject object = new JSONObject(string);
						if (string.equals("{}")
								&& object.getJSONObject("result") == null) {
							Looper.prepare();
							ToastUtil.show(getActivity(), "没有数据!请重新搜索!");
							Looper.loop();
							Intent intent = new Intent(getActivity(),
									SearchActivity.class);
							startActivity(intent);
							getActivity().finish();
						} else {
							Message msg = mHandler.obtainMessage();
							msg.what = 0;
							msg.obj = string;
							mHandler.sendMessage(msg);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				Log.e("KONGCV_GET_PARK_INFO", arg1.toString());
			}
		});
	}

	private List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
	private String notRest;
	private String strPrice;
	private String start;
	private String end;
	private String mineSendFragment;
	private String strExtra;
	private String hire_method_id;

	@Override
	public void onItemClick(AdapterView<?> parent, View parentView,
			int position, long id) {
		start = time_pay_start.getText().toString();
		end = time_pay_end.getText().toString();
		if (start.equals(" 年  月  日") || end.equals(" 年  月  日")) {
			ToastUtil.show(getActivity(), "请先选择出租日期！");
			return;
		}
		ViewHolder holder = (ViewHolder) parentView.getTag();
		notRest = tv_not_rent.getText().toString();
		String days = DateUtils.getDays(start, end, true);
		String[] notRestArr = notRest.split(",");
		days = getDays(start, end, notRestArr, days);// 时间天
		String text = new String(holder.tvPrice.getText().toString());
		String a[] = text.split("/");
		strPrice = hire_price.get(position);
		String ap[] = strPrice.split("/");
		double price = Double.parseDouble(ap[0]);
		Log.v("days", days);
		int dayNum = Integer.parseInt(days);

		adapter.setP(position);
		adapter.notifyDataSetChanged();
		if (hire_price != null) {
			if (a[1].equals("小时")) {
				double p = price / 4.00;
				java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
				dayNum = (int) Math.ceil(p);
				tv_pay_number.setText(dayNum + "");
			} else if (a[1].equals("天")) {
				tv_pay_number.setText(price * dayNum + "");
			} else if (a[1].equals("月")) {
				double monthPrice = dayNum / 30.0 * price;// 这样为保持2位
				java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
				String s = df.format(monthPrice);
				monthPrice = Double.valueOf(s);// double
				price = (int) Math.ceil(monthPrice);
				tv_pay_number.setText(price + "");
			}
		}
		hire_method_id = hireMethodId.get(position);
		field = hireFieldList.get(position);
	}

	/**
	 * 计算时间差
	 */
	private String getDays(String start, String end, String[] notRestArr,
			String days) {
		int day = 0;
		int parseInt = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = (Date) sdf.parse(start);
			Date endDate = (Date) sdf.parse(end);
			List<java.util.Date> lists = DateUtils
					.dateSplit(startDate, endDate);
			if (notRestArr != null) {
				for (int i = 0; i < notRestArr.length; i++) {
					if (!lists.isEmpty()) {
						for (Date d : lists) {
							if (notRestArr[i].equals(DateUtils.getWeekday(sdf
									.format(d)))) {
								day++;
							}
						}
					}
				}
				parseInt = Integer.parseInt(days) + 1;
				parseInt = parseInt - day;
			} else {
				parseInt = Integer.parseInt(days) + 1;
			}
			days = parseInt + "";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return days;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			getActivity().finish();
		}
		return false;
	}
}
