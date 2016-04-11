package com.kongcv.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.kongcv.adapter.PhoneNumberAdapter;
import com.kongcv.calendar.PickDialog;
import com.kongcv.calendar.PickDialogListener;
import com.kongcv.global.CurbInfoBean;
import com.kongcv.global.CurbInfoBean.ResultEntity;
import com.kongcv.global.CurbInfoBean.ResultEntity.LocationEntity;
import com.kongcv.global.CurbJpushBean;
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
import com.kongcv.utils.ToastUtil;

public class CurbDetailFragment extends Fragment implements OnClickListener,
		OnItemClickListener {

	private View view;
	private ACacheUtils mCache;
	private TextView tvAddress, tvAddressDetail, curbStart, curbEnd,
			curbDescribe, tvCurbMoney, tv_reserveOrpay;
	private ListView curbInfoListView;
	private Button btnInform, btnPayTo;
	private PickDialog dialog;
	private ArrayList<Map<String, Object>> dataList;
	public final String KEY[] = new String[] { "ivDaoH", "txtFind", "ivCome" };
	private List<String> hire_method, hire_price;
	private int index = -1;
	private String field, mode, park_id, stringExtra;
	private DetailFragmentAdapter adapter;
	private List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
	private String start, end;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
			return view;
		}
		view = inflater.inflate(R.layout.activity_curb, null);
		initView();
		getModeAndParkId();
		getDeviceToken();
		return view;
	}

	/**
	 * 一开始获取到mode和park_id
	 */
	private String strExtra, CurbMineReceiver;
	private ZyCommityAdapterBean mCommBean = null;

	private void getModeAndParkId() {
		// TODO Auto-generated method stub
		Bundle arguments = getArguments();
		mode = arguments.getString("mode");
		park_id = arguments.getString("park_id");
		field = arguments.getString("field");
		mineSendFragment = arguments.getString("MineSendFragment");
		strExtra = arguments.getString("stringExtra");
		CurbMineReceiver = arguments.getString("CurbMineReceiver");// 我收到的
		mCommBean = (ZyCommityAdapterBean) arguments
				.getSerializable("mCommBean");// 订单管理 跳转过来的

		if (strExtra != null) {
			visiblePhone2(strExtra);
		}
		if (mCommBean != null) {
			if (mCommBean.getTrade_state() == 1) {
				btnInform.setVisibility(View.GONE);
				btnPayTo.setVisibility(View.GONE);
				visiblePhone(mCommBean);
				tv_reserveOrpay.setText("交易完成");
			} else {
				if (mCommBean.getHandsel_state() == 0) {
					LastTradeId = mCommBean.getObjectId();
					btnInform.setVisibility(View.GONE);
					btnPayTo.setVisibility(View.VISIBLE);
					btnPayTo.setOnClickListener(this);
					tv_reserveOrpay.setText("定金");
				} else if (mCommBean.getHandsel_state() == 1) {
					LastTradeId = mCommBean.getObjectId();
					btnInform.setVisibility(View.GONE);
					btnPayTo.setVisibility(View.VISIBLE);
					btnPayTo.setOnClickListener(this);
					tv_reserveOrpay.setText("差额");
				}
			}
		}
		Intent intent = getActivity().getIntent();
		stringExtra = intent.getStringExtra("own_device_token");
		Log.d("stringExtra", stringExtra+"<>");
		Log.d("mineSendFragment", mineSendFragment+"<>");
		Log.d("mCommBean", mCommBean+"<>");
		Log.d("strExtra", strExtra+"<>");
		
		ReadInfo task = new ReadInfo();
		task.execute();
	}

	/**
	 * 出租人接收设置电话号码求租人可见
	 */
	private List<String> phoneList;
	private PhoneNumberAdapter phoneNumberAdapter;

	private void visiblePhone(ZyCommityAdapterBean mCommBean) {
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

	private void visiblePhone2(String stringExtra) {
		Gson gson = new Gson();
		JpushBean fromJson = gson.fromJson(stringExtra, JpushBean.class);
		String own_mobile = fromJson.getReq_mobile();
		mListPhone.setVisibility(View.VISIBLE);
		phoneList = new ArrayList<String>();
		phoneList.add(own_mobile);
		if (phoneList != null && phoneList.size() > 0) {
			phoneNumberAdapter = new PhoneNumberAdapter(getActivity(),
					phoneList);
			mListPhone.setAdapter(phoneNumberAdapter);
			phoneNumberAdapter.notifyDataSetChanged();
			ToastUtil.fixListViewHeight(mListPhone, -1);
		}
	}

	/**
	 * 打开jpush
	 */
	private String LastTradeId = null;

	private void getDeviceToken() {
		// TODO Auto-generated method stub
		if (stringExtra != null) {
			Log.v("道边打开通知栏获取的消息回调！", stringExtra);
			Gson gson = new Gson();
			CurbJpushBean fromJson = gson.fromJson(stringExtra,
					CurbJpushBean.class);
			if (stringExtra != null
					&& "verify_accept".equals(fromJson.getPush_type())) {// 接收
				btnInform.setVisibility(View.GONE);
				btnPayTo.setVisibility(View.VISIBLE);
				btnPayTo.setOnClickListener(this);
			} else if (stringExtra != null
					&& "verify_reject".equals(fromJson.getPush_type())) {// 拒绝
				btnInform.setText("此车位已出租");
				// btnInform.setOnClickListener(null);
			} else if (stringExtra != null
					&& "trade_charge".equals(fromJson.getPush_type())) {
				if (fromJson.getPrice() != 0) {
					LastTradeId = fromJson.getTrade_id();
					btnInform.setVisibility(View.GONE);
					btnPayTo.setVisibility(View.VISIBLE);
					btnPayTo.setOnClickListener(this);
				} else {
					btnInform.setVisibility(View.GONE);
					btnPayTo.setVisibility(View.GONE);
					btnPayTo.setOnClickListener(null);
				}
			}
		}
	}

	class ReadInfo extends PreReadTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... arg0) {
			Details();
			return null;
		}
	}

	private void Details() {
		try {
			JSONObject obj = new JSONObject();
			obj.put("park_id", park_id);
			obj.put("mode", mode); // 值需要传递
			String doHttpsPost = PostCLientUtils
					.doHttpsPost(Information.KONGCV_GET_PARK_INFO,
							JsonStrUtils.JsonStr(obj));
			Log.v("道边详情页获取到详细数据", doHttpsPost); // 获取到详细数据
			if (doHttpsPost.equals("{}")) {
				Looper.prepare();
				ToastUtil.show(getActivity(), "没有数据！请重新搜索！");
				Looper.loop();
				// 回转到SearchActivity
				Intent intent = new Intent(getActivity(), SearchActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
			Gson gson = new Gson();
			CurbInfoBean fromJson = gson.fromJson(doHttpsPost,
					CurbInfoBean.class);
			ResultEntity result = fromJson.getResult();
			rate = result.getRate();
			Message msg = mHandler.obtainMessage();
			msg.what = 0;
			msg.obj = result;
			mHandler.sendMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ResultEntity result;
	private String user;
	private PayOneBean payBean;
	public static LocationEntity location;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				try {
					result = (ResultEntity) msg.obj;
					if (result.getPark_space() == 0) {
						btnInform.setText("此车位已出租");
						btnInform.setOnClickListener(null);
					}
					location = result.getLocation();
					if (result.getPark_description() != null)
						curbDescribe.setText(result.getPark_description());
					
					if (strExtra != null) {
						Gson gson = new Gson();
						CurbJpushBean fromJson = gson.fromJson(strExtra,
								CurbJpushBean.class);
						curbStart.setText(fromJson.getHire_start());
						curbEnd.setText(fromJson.getHire_end());
					}
					if (stringExtra != null) {
						Gson gson = new Gson();
						CurbJpushBean fromJson = gson.fromJson(stringExtra,
								CurbJpushBean.class);
						curbStart.setText(fromJson.getHire_start());
						curbEnd.setText(fromJson.getHire_end());
					}
					if (mineSendFragment != null) {
						Gson gson = new Gson();
						CurbJpushBean fromJson = gson.fromJson(
								mineSendFragment, CurbJpushBean.class);
						curbStart.setText(fromJson.getHire_start());
						curbEnd.setText(fromJson.getHire_end());
					}
					payBean = new PayOneBean();
					List<String> hire_time = result.getHire_time();
					hire_price = result.getHire_price();
					hire_method = new ArrayList<String>();
					String hire_methodStr = result.getHire_method();
					String address = result.getAddress();
					String s = new String(address);
					String a[] = s.split("&");
					tvAddress.setText(a[0]);
					tvAddressDetail.setText(a[1]);
					setAdapter(hire_time, hire_price, hire_methodStr);
					user = result.getUser();
					Data.putData("ResultEntityUser", user);
					/**
					 * 租用的起始时间 需要判断当前模式是记时小时 还是其他
					 */
					payBean.setUser_id(mCache.getAsString("user_id"));
					payBean.setPark_id(park_id);
					payBean.setCurb_rate(rate);
					payBean.setMode(mode);

					Gson gson = new Gson();
					UserBean userBean = gson.fromJson(user, UserBean.class);
					payBean.setHirer_id(userBean.getObjectId());
					payBean.setLicense_plate(userBean.getLicense_plate());
					Data.putData("CurbPayOneBean", payBean);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 1:
				try {
					// 前往支付
					String bill_id = (String) msg.obj;
					JSONObject bean = new JSONObject(user);
					mobilePhoneNumber = bean.getString("mobilePhoneNumber");
					Bundle bundle = new Bundle();
					bundle.putString("trade_id", bill_id);
					bundle.putString("price", tvCurbMoney.getText().toString());
					bundle.putString("phoneNumber", mobilePhoneNumber);
					if (stringExtra != null) {
						bundle.putString("stringExtra", stringExtra);
					} else if (mineSendFragment != null) {
						bundle.putString("stringExtra", mineSendFragment);
					}
					if (mCommBean != null) {
						bundle.putSerializable("mCommBean", mCommBean);
					}
					if (rate != 0) {
						bundle.putDouble("rate", rate);
					}
					Intent intent = new Intent(getActivity(), PayActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2:
				try {
					String doHttpsPost = (String) msg.obj;
					JSONObject obj2 = new JSONObject(doHttpsPost);
					String str = obj2.getString("result");
					JSONObject objStr = new JSONObject(str);
					String state = objStr.getString("state");
					if ("ok".equals(state)) {
						ToastUtil.show(getActivity(), "订单成功确认!");
					} else {
						ToastUtil.show(getActivity(), "订单确认失败，请尝试重新操作!");
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
	private boolean toastOrNo = false;
	private double rate = 0;
	private String mineSendFragment;
	private String mobilePhoneNumber;
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
					if (mCommBean != null) {
						if (mCommBean.getTrade_state() == 0) {
							if (mCommBean.getHandsel_state() == 0) {
								if (method.equals("计时/小时")) {
									if (hire_price.get(i).indexOf("/") != -1) {
										String[] split = hire_price.get(i)
												.split("/");
										double p = Double.parseDouble(split[0]) / 4.00;
										java.text.DecimalFormat df = new java.text.DecimalFormat(
												"#.00");
										tvCurbMoney.setText(p < 1 ? "0"
												+ df.format(p) : df.format(p));
										toastOrNo = true;
										tv_reserveOrpay.setText("定金");
									} else {
										double p = Double
												.parseDouble(hire_price.get(i)) / 4.00;
										java.text.DecimalFormat df = new java.text.DecimalFormat(
												"#.00");
										tvCurbMoney.setText(p < 1 ? "0"
												+ df.format(p) : df.format(p));
										toastOrNo = true;
									}
								}
							} else {
								tvCurbMoney
										.setText((mCommBean.getPrice() - mCommBean
												.getMoney()) + "");
							}
						} else {
							tvCurbMoney.setText(mCommBean.getPrice() + "");
						}
					} else {
						if (method.equals("计时/小时")) {
							if (hire_price.get(i).indexOf("/") != -1) {
								String[] split = hire_price.get(i).split("/");
								double p = Double.parseDouble(split[0]) / 4.00;
								java.text.DecimalFormat df = new java.text.DecimalFormat(
										"#.00");
								tvCurbMoney.setText(p < 1 ? "0" + df.format(p)
										: df.format(p));
								toastOrNo = true;
								tv_reserveOrpay.setText("定金");
							} else {
								double p = Double
										.parseDouble(hire_price.get(i)) / 4.00;
								java.text.DecimalFormat df = new java.text.DecimalFormat(
										"#.00");
								tvCurbMoney.setText(p < 1 ? "0" + df.format(p)
										: df.format(p));
								toastOrNo = true;
							}
						}
					}
				}
				if (stringExtra != null) {
					Gson gson = new Gson();
					JpushBean jpushBean = gson.fromJson(stringExtra,
							JpushBean.class);
					if (jpushBean.getHire_method_field().equals(initAdapter)) {
						index = i;
						tvCurbMoney.setText(jpushBean.getPrice() + "");
					}
				}
				if (strExtra != null) {
					Gson gson = new Gson();
					JpushBean jpushBean = gson.fromJson(strExtra,
							JpushBean.class);
					if (jpushBean.getHire_method_field().equals(initAdapter)) {
						index = i;
						tvCurbMoney.setText(jpushBean.getPrice() + "");
					}
				}
				if (mineSendFragment != null) {
					Gson gson = new Gson();
					JpushBean jpushBean = gson.fromJson(mineSendFragment,
							JpushBean.class);
					if (jpushBean.getHire_method_field().equals(initAdapter)) {
						index = i;
						tvCurbMoney.setText(jpushBean.getPrice() + "");
					}
				}
			}
			if (mCommBean == null) {
				if (CurbMineReceiver != null) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(KEY[0], hire_name.get(index));// 地点
					map.put(KEY[1], hire_time.get(index).equals("0") ? ""
							: hire_time.get(index));// 事件a
					map.put(KEY[2], hire_price.get(index));// 价格
					dataList.add(map);
				} else {
					if (strExtra != null) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put(KEY[0], hire_name.get(index));// 地点
						map.put(KEY[1], hire_time.get(index).equals("0") ? ""
								: hire_time.get(index));// 事件a
						map.put(KEY[2], hire_price.get(index));// 价格
						dataList.add(map);
					} else {
						if (mineSendFragment != null) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put(KEY[0], hire_name.get(index));// 地点
							map.put(KEY[1],
									hire_time.get(index).equals("0") ? ""
											: hire_time.get(index));// 事件a
							map.put(KEY[2], hire_price.get(index));// 价格
							dataList.add(map);
						} else {
							for (int i = 0; i < hire_price.size(); i++) {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put(KEY[0], hire_name.get(i));// 地点
								map.put(KEY[1],
										hire_time.get(i).equals("0") ? ""
												: hire_time.get(i));// 事件a
								map.put(KEY[2], hire_price.get(i));// 价格
								dataList.add(map);
							}
						}
					}
				}
			} else {
				curbStart.setText(mCommBean.getHire_start());
				curbEnd.setText(mCommBean.getHire_end());

				Map<String, Object> map = new HashMap<String, Object>();
				map.put(KEY[0], hire_name.get(index));// 地点
				map.put(KEY[1], hire_time.get(index).equals("0") ? ""
						: hire_time.get(index));// 事件a
				map.put(KEY[2], hire_price.get(index));// 价格
				dataList.add(map);
			}
			if (dataList != null && dataList.size() > 0) {
				adapter = new DetailFragmentAdapter(getActivity());
				adapter.setDataSource(dataList);
				curbInfoListView.setAdapter(adapter);
				maps = dataList;
				ToastUtil.fixListViewHeight(curbInfoListView, -1);
				curbInfoListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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

	private ListView mListPhone;

	private void initView() {
		tvAddress = (TextView) view.findViewById(R.id.tv_curb_address);
		tvAddressDetail = (TextView) view.findViewById(R.id.tv_curb_add_detail);
		curbStart = (TextView) view.findViewById(R.id.curb_rent_start);
		curbEnd = (TextView) view.findViewById(R.id.curb_rent_end);
		curbDescribe = (TextView) view.findViewById(R.id.tv_curb_describe);
		tvCurbMoney = (TextView) view.findViewById(R.id.tv_curb_money);// 价格 随变化
		mListPhone = (ListView) view.findViewById(R.id.detail_lv_phone);
		tv_reserveOrpay = (TextView) view.findViewById(R.id.tv_reserveOrpay);

		curbStart.setOnClickListener(this);
		curbEnd.setOnClickListener(this);

		btnInform = (Button) view.findViewById(R.id.curb_inform);
		btnPayTo = (Button) view.findViewById(R.id.curb_pay);
		btnInform.setOnClickListener(this);

		curbInfoListView = (ListView) view
				.findViewById(R.id.curb_listview_Info);
		curbInfoListView.setOnItemClickListener(this);
		mCache = ACacheUtils.get(getActivity());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.curb_rent_start:// 起始日历
			dialog = new PickDialog(getActivity(), new PickDialogListener() {
				@Override
				public void refreshPriorityUI(String str) {
					// TODO Auto-generated method stub
					curbStart.setText(str);
					start = str;
					end = curbEnd.getText().toString();
					if (!start.equals(" 年  月  日") && !end.equals(" 年  月  日")
							&& index != -1) {
						String days = DateUtils.getDays(start, end, true);// 天数
						String text = (String) dataList.get(index).get(KEY[2]);
						if (text != null && text.indexOf("/") != -1) {
							String a[] = text.split("/");
							double price = Double.parseDouble(a[0]);
							int dayNum = Integer.parseInt(days) + 1;
							if (a[1].equals("小时")) {
								double p = price / 8.00;
								java.text.DecimalFormat df = new java.text.DecimalFormat(
										"#.00");
								tvCurbMoney.setText(p < 1 ? "0" + df.format(p)
										: df.format(p));
							} else if (a[1].equals("天")) {
								tvCurbMoney.setText(price * dayNum + "");
							} else if (a[1].equals("月")) {
								double p = dayNum / 30.0 * price;// 这样为保持2位
								java.text.DecimalFormat df = new java.text.DecimalFormat(
										"#.00");
								tvCurbMoney.setText(p < 1 ? "0" + df.format(p)
										: df.format(p));
							}
						} else {
							double price = Integer.parseInt(text);
							text = (String) dataList.get(index).get(KEY[0]);
							if (text.indexOf("/") != -1) {
								String[] split = text.split("/");
								int dayNum = Integer.parseInt(days) + 1;
								if (split[1].equals("小时")) {
									double p = price / 8.00;
									java.text.DecimalFormat df = new java.text.DecimalFormat(
											"#.00");
									tvCurbMoney.setText(p < 1 ? "0"
											+ df.format(p) : df.format(p));
								} else if (split[1].equals("天")) {
									tvCurbMoney.setText(price * dayNum + "");
								} else if (split[1].equals("月")) {
									double p = dayNum / 30.0 * price;// 这样为保持2位
									java.text.DecimalFormat df = new java.text.DecimalFormat(
											"#.00");
									tvCurbMoney.setText(p < 1 ? "0"
											+ df.format(p) : df.format(p));
								}
							}
						}
					}
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.show();
			break;
		case R.id.curb_rent_end:// 结束日历
			dialog = new PickDialog(getActivity(), new PickDialogListener() {
				@Override
				public void refreshPriorityUI(String str) {
					// TODO Auto-generated method stub
					end = str;
					curbEnd.setText(str);
					start = curbStart.getText().toString();
					if (!start.equals(" 年  月  日") && !end.equals(" 年  月  日")
							&& index != -1) {
						String days = DateUtils.getDays(start, end, true);// 天数
						String text = (String) dataList.get(index).get(KEY[2]);
						if (text != null && text.indexOf("/") != -1) {
							String a[] = text.split("/");
							double price = Double.parseDouble(a[0]);
							int dayNum = Integer.parseInt(days) + 1;
							if (a[1].equals("小时")) {
								double p = price / 8.00;
								java.text.DecimalFormat df = new java.text.DecimalFormat(
										"#.00");
								tvCurbMoney.setText(p < 1 ? "0" + df.format(p)
										: df.format(p));
							} else if (a[1].equals("天")) {
								tvCurbMoney.setText(price * dayNum + "");
							} else if (a[1].equals("月")) {
								double p = dayNum / 30.0 * price;// 这样为保持2位
								java.text.DecimalFormat df = new java.text.DecimalFormat(
										"#.00");
								tvCurbMoney.setText(p < 1 ? "0" + df.format(p)
										: df.format(p));
							}
						} else {
							double price = Double.parseDouble(text);
							text = (String) dataList.get(index).get(KEY[0]);
							if (text.indexOf("/") != -1) {
								String[] split = text.split("/");
								int dayNum = Integer.parseInt(days) + 1;
								if (split[1].equals("小时")) {
									double p = price / 8.00;
									java.text.DecimalFormat df = new java.text.DecimalFormat(
											"#.00");
									tvCurbMoney.setText(p < 1 ? "0"
											+ df.format(p) : df.format(p));
								} else if (split[1].equals("天")) {
									tvCurbMoney.setText(price * dayNum + "");
								} else if (split[1].equals("月")) {
									double p = dayNum / 30.0 * price;// 这样为保持2位
									java.text.DecimalFormat df = new java.text.DecimalFormat(
											"#.00");
									tvCurbMoney.setText(p < 1 ? "0"
											+ df.format(p) : df.format(p));
								}
							}
						}
					}
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.show();
			break;
		case R.id.curb_inform:// 发送jPush
			if (mCache.getAsString("USER") != null
					&& mCache.getAsString("sessionToken") != null) {
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
		case R.id.curb_pay:// 支付
			// 获取订单号 接着跳转到支付页面
			try {
				if (LastTradeId != null) {
					Message msg = mHandler.obtainMessage();
					msg.what = 1;
					msg.obj = LastTradeId;
					mHandler.sendMessage(msg);
				} else {
					if (payBean != null) {
						Gson gson = new Gson();
						JpushBean fromJson = null;
						if (stringExtra != null) {
							fromJson = gson.fromJson(stringExtra,
									JpushBean.class);
						} else if (mineSendFragment != null) {
							fromJson = gson.fromJson(mineSendFragment,
									JpushBean.class);
						}
						if (fromJson != null) {
							String fromJsonStart = fromJson.getHire_start();
							String fromJsonEnd = fromJson.getHire_end();
							if (fromJsonStart != null && fromJsonEnd != null
									&& !fromJsonStart.isEmpty()
									&& !fromJsonEnd.isEmpty()) {
								String days = DateUtils.getDays(
										fromJson.getHire_start(),
										fromJson.getHire_end(), true);
								int dayInt = 0;
								if (days != null && !days.isEmpty()) {
									dayInt = Integer.parseInt(days) + 1;
								}
								if (dayInt > 0) {
									payBean.setExtra_flag("1");
								} else {
									payBean.setExtra_flag("0");
								}
							} else {
								payBean.setExtra_flag("0");
							}
						} else {
							String start = curbStart.getText().toString();
							String end = curbEnd.getText().toString();
							if (!start.equals(" 年  月  日")
									&& !end.equals(" 年  月  日")) {
								String days = DateUtils.getDays(start, end,
										true);
								int dayInt = Integer.parseInt(days) + 1;
								if (dayInt > 0) {
									payBean.setExtra_flag("1");
								} else {
									payBean.setExtra_flag("0");
								}
							} else {
								payBean.setExtra_flag("0");
							}

						}
						hire_method_id = fromJson.getHire_method_id();
						String hire_method = result.getHire_method();
						JSONArray array;
						array = new JSONArray(hire_method);
						String string = null;
						for (int i = 0; i < array.length(); i++) {
							if (array.getJSONObject(i).getString("objectId")
									.equals(hire_method_id)) {
								string = result.getHire_price().get(i);
								payBean.setUnit_price(string);//
							}
						}
						String a[] = string.split("/");
						if (a[1].equals("小时")) {
							payBean.setHire_start("");
							payBean.setHire_end("");
						} else {
							payBean.setHire_start(fromJson.getHire_start());
							payBean.setHire_end(fromJson.getHire_end());
						}
						payBean.setHire_method_id(hire_method_id);
						payBean.setPrice(fromJson.getPrice());
						payBean.setCurb_rate(rate);
						String json = gson.toJson(payBean);
						doPay(json);
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

	/**
	 * 同意之后下订单支付
	 * 
	 * @param object
	 */
	private void doPay(String object) {
		// TODO Auto-generated method stub
		/*
		 * MyThread myThread = new MyThread(object); myThread.start();
		 */
		okhttp3.Request request = new okhttp3.Request.Builder()
				.url(Information.KONGCV_INSERT_TRADEDATA)
				.headers(Information.getHeaders())
				.post(RequestBody.create(Information.MEDIA_TYPE_MARKDOWN,
						object)).build();

		client.newCall(request).enqueue(new okhttp3.Callback() {
			@Override
			public void onResponse(Call arg0, okhttp3.Response response)
					throws IOException {
				// TODO Auto-generated method stub
				if (response.isSuccessful()) {
					try {
						JSONObject obj = new JSONObject(response.body()
								.string());
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
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				Log.e("KONGCV_INSERT_TRADEDATA", arg1.toString());
			}
		});

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
			String doHttpsPost = PostCLientUtils.doHttpsPost(
					Information.KONGCV_INSERT_TRADEDATA, str);
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

	/**
	 * 发送Jpush通知
	 */
	private OkHttpClient client = new OkHttpClient();

	private void sendInform() {
		// TODO Auto-generated method stub
		if(JPushInformBefore()!=null){
			Log.d("JPushInformBefore()", JPushInformBefore()+"<>");
			okhttp3.Request request = new okhttp3.Request.Builder()
					.url(Information.KONGCV_JPUSH_MESSAGE_P2P)
					.headers(Information.getHeaders())
					.post(RequestBody.create(Information.MEDIA_TYPE_MARKDOWN,
							JPushInformBefore())).build();

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
		}else{
			ToastUtil.show(getActivity(), "请求参数有错误");
		}
	}

	private String hire_method_id;

	/**
	 * 发送jpush请求参数
	 */
	private String JPushInformBefore() {
		String jsonStr = null;
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
				
				String data = (String) Data.getData("ResultEntityUser");
				JSONObject user = new JSONObject(data);
				mobilePhoneNumber = user.getString("mobilePhoneNumber");
				String device_token = user.getString("device_token");
				JSONObject obj = new JSONObject();
				obj.put("mobilePhoneNumber", mobilePhoneNumber);// 对方手机号
				obj.put("push_type", "verify_request");// 租用请求
				obj.put("device_token", device_token);
				obj.put("device_type", "android");
				String asString = mCache.getAsString("user_id");
				obj.put("user_id", asString);
				JSONObject extras = new JSONObject();
				extras.put("park_id", park_id);// 得到车位的objectId
				extras.put("mode", mode);
				extras.put("hire_method_id", hire_method_id);
				extras.put("hire_method_field", field);
				extras.put("address", tvAddress.getText().toString()
						+ tvAddressDetail.getText().toString());// 地址
				if (!curbStart.getText().toString().equals(" 年  月  日")
						&& !curbEnd.getText().toString().equals(" 年  月  日")) {
					extras.put("hire_start", curbStart.getText().toString()
							+ " 00:00:00");// 出租截止时间和日期
					extras.put("hire_end", curbEnd.getText().toString()
							+ " 23:59:59");
				} else {
					extras.put("hire_start", "");// 出租截止时间和日期
					extras.put("hire_end", "");
				}
				// extras.put("own_device_token",
				// mCache.getAsString("RegistrationID"));
				extras.put("own_device_token",
						JPushInterface.getRegistrationID(getActivity()));
				extras.put("own_device_type", "android");
				extras.put("own_mobile", mCache.getAsString("USER"));
				extras.put("push_type", "verify_request");
				extras.put("price",
						Double.valueOf(tvCurbMoney.getText().toString()));// 价格需要计算
																			// 等会传递
				obj.put("extras", extras);
				Log.v("发送jPushJPUSH!!", JsonStrUtils.JsonStr(obj));
				jsonStr = JsonStrUtils.JsonStr(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return jsonStr;
	}

	/**
	 * 点击选择
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.e("onItemCick", field + ":field");
		start = curbStart.getText().toString();
		end = curbEnd.getText().toString();
		if (!start.equals(" 年  月  日") && !end.equals(" 年  月  日")) {
			adapter.setP(position);
			adapter.notifyDataSetChanged();
			String string = (String) dataList.get(position).get(KEY[2]);
			String days = DateUtils.getDays(start, end, true);// 天数
			hire_method_id = hireMethodId.get(position);
			field = hireFieldList.get(position);
			if (dataList != null && dataList.size() > 0) {
				if (string != null && string.indexOf("/") != -1) {
					String a[] = string.split("/");
					double price = Double.parseDouble(a[0]);
					int dayNum = Integer.parseInt(days) + 1;
					if (a[1].equals("小时")) {
						double p = price / 4.00;
						java.text.DecimalFormat df = new java.text.DecimalFormat(
								"#.00");
						tvCurbMoney.setText(p < 1 ? "0" + df.format(p) : df
								.format(p));
					} else if (a[1].equals("天")) {
						tvCurbMoney.setText(price * dayNum + "");
					} else if (a[1].equals("月")) {
						double p = dayNum / 30.0 * price;// 这样为保持2位
						java.text.DecimalFormat df = new java.text.DecimalFormat(
								"#.00");
						tvCurbMoney.setText(p < 1 ? "0" + df.format(p) : df
								.format(p));
					}
				} else {
					double price = Double.parseDouble(string);
					string = (String) dataList.get(position).get(KEY[0]);
					if (string.indexOf("/") != -1) {
						String[] split = string.split("/");
						int dayNum = Integer.parseInt(days) + 1;
						if (split[1].equals("小时")) {
							double p = price / 4.00;
							java.text.DecimalFormat df = new java.text.DecimalFormat(
									"#.00");
							tvCurbMoney.setText(p < 1 ? "0" + df.format(p) : df
									.format(p));
						} else if (split[1].equals("天")) {
							tvCurbMoney.setText(price * dayNum + "");
						} else if (split[1].equals("月")) {
							double p = dayNum / 30.0 * price;// 这样为保持2位
							java.text.DecimalFormat df = new java.text.DecimalFormat(
									"#.00");
							tvCurbMoney.setText(p < 1 ? "0" + df.format(p) : df
									.format(p));
						}
					}
				}
			}
		} else {
			ToastUtil.show(getActivity(), "请先选择时间！");
			return;
		}

	}

}
