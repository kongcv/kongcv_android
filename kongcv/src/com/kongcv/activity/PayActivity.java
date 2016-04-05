package com.kongcv.activity;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.UI.AsyncImageLoader.PreReadTask;
import com.kongcv.global.Information;
import com.kongcv.global.JpushBean;
import com.kongcv.global.PayBean;
import com.kongcv.global.Pay_info;
import com.kongcv.global.ZyCommityAdapterBean;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
import com.pingplusplus.android.PaymentActivity;
import com.pingplusplus.android.PingppLog;
import com.umeng.analytics.MobclickAgent;

/**
 * 支付activity
 * @author kcw001
 */
public class PayActivity extends Activity implements OnClickListener {

	private ImageView alipay,weChat,mBreak;
	private static final int REQUEST_CODE_PAYMENT = 1;
	
	private static final String CHANNEL_UPACP = "wx";
	private static final String CHANNEL_ALIPAY = "alipay";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_pay);
		MyApplication.getInstance().addActivity(this);
		mCache = ACacheUtils.get(getApplicationContext());
		initView();
	}
	
	private void getBill_id(String params) {
		// TODO Auto-generated method stub
		ReadInfo info=new ReadInfo();
		info.execute(params);
	}
	class ReadInfo extends PreReadTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			getBillId(params[0]);
			return null;
		}
	}
	private ZyCommityAdapterBean mCommBean = null;
	private void getBillId(String params) {
		// TODO Auto-generated method stub
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
			mCommBean=(ZyCommityAdapterBean) extras.getSerializable("mCommBean");
			String trade_id = extras.getString("trade_id");
			String price =extras.getString("price");
			String phoneNumber = extras.getString("phoneNumber");
			
			if(mCommBean==null){
				String stringExtra = extras.getString("stringExtra");
				Gson gson=new Gson();
				Pay_info info=new Pay_info();
				if(stringExtra!=null && phoneNumber!=null){
					JpushBean jpushBean = gson.fromJson(stringExtra, JpushBean.class);
					info.setMode(jpushBean.getMode());
					info.setOpen_id("123456");
					info.setDevice_type(jpushBean.getOwn_device_type());
					if(jpushBean.getMode().equals("curb")){
						//hour_meter
						if(jpushBean.getHire_method_field().equals("hour_meter") && jpushBean.getPush_type().equals("verify_accept")){
							info.setPay_type("handsel");
						}else{
							info.setPay_type("balance");
						}
					}else{
						info.setPay_type("money");
					}
					info.setPhone(phoneNumber);
					info.setSubject(jpushBean.getAddress());
					info.setDevice_token(jpushBean.getOwn_device_token());
				}
				if(price!=null)
				payTo(trade_id,price,params,info);
			}else {
				Pay_info info=new Pay_info();
				info.setMode(mCommBean.getMode());
				info.setOpen_id("123456");
				info.setDevice_type(mCommBean.getDevice_type());//
				if(mCommBean.getMode().equals("curb")){
					if(mCommBean.getField().equals("hour_meter") && mCommBean.getHandsel_state()==0){
						info.setPay_type("handsel");
					//	price=mCommBean.getMoney()+"";
					}else{
						info.setPay_type("balance");
						price=(mCommBean.getPrice()-mCommBean.getMoney())+"";
					}
				}else{
					info.setPay_type("money");
					price=mCommBean.getMoney()+"";
				}
				info.setPhone(phoneNumber);
				info.setSubject(mCommBean.getAddress());
				info.setDevice_token(mCommBean.getDevice_token());
				if(price!=null)
				payTo(trade_id,price,params,info);
			}
		}
	}
	/**
	 * 获取订单号并支付
	 */
	public String NetType(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			String typeName = info.getTypeName().toLowerCase(); 
			typeName = info.getExtraInfo().toLowerCase();
			Log.v("支付页面", typeName);
			return typeName;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		// TODO Auto-generated method stub
		alipay = (ImageView) findViewById(R.id.iv_alipay);
		weChat = (ImageView) findViewById(R.id.iv_wechat);
		mBreak = (ImageView) findViewById(R.id.iv_pay_break);
		
		alipay.setOnClickListener(this);
		weChat.setOnClickListener(this);
		mBreak.setOnClickListener(this);
		payNumber = (TextView) findViewById(R.id.tv_dialog_pay_num);
		
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
			String string = extras.getString("price");
			Log.v("pay price", string);
			payNumber.setText("¥"+string);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_alipay:
			getBill_id(CHANNEL_ALIPAY);
			break;
		case R.id.iv_wechat:
			getBill_id(CHANNEL_UPACP);
			break;
		case R.id.iv_pay_break:// 回退
			finish();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 插入交易数据
	 * @param trade_id
	 */
	private void payTo(String trade_id,String price,String params,Pay_info info) {
		// TODO Auto-generated method stub
		try {
			JSONObject object = new JSONObject();
			object.put("trade_id", trade_id);
			Log.d("插入交易数据>>><<<", JsonStrUtils.JsonStr(object));
			Log.d("插入交易数据>>><<<", JsonStrUtils.JsonStr(object));
			String doHttpsPost = PostCLientUtils.doHttpsPost(
					Information.KONGCV_INSERT_TRADE_BILLDATA,
					JsonStrUtils.JsonStr(object));
			JSONObject object2 = new JSONObject(doHttpsPost);
			String string = object2.getString("result");
			JSONObject object3 = new JSONObject(string);
			String state = object3.getString("state");
			if (state.equals("ok")) {
				String bill_id = object3.getString("bill_id");
				if(params.equals(CHANNEL_UPACP)){
					new PaymentTask().execute(new PaymentRequest(CHANNEL_UPACP,
							Double.parseDouble(price), bill_id,info));
				}else if(params.equals(CHANNEL_ALIPAY)){
					new PaymentTask().execute(new PaymentRequest(CHANNEL_ALIPAY,
							Double.parseDouble(price), bill_id,info));
				}
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	private boolean flag=false;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				break;

			default:
				break;
			}
		}
	};
	private TextView payNumber;
	private ACacheUtils mCache;

	class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {
		@Override
		protected void onPreExecute() {
			// 按键点击之后的禁用，防止重复点击
			weChat.setOnClickListener(null);
			alipay.setOnClickListener(null);
		}
		@Override
		protected String doInBackground(PaymentRequest... pr) {
			PaymentRequest paymentRequest = pr[0];
			String data = null;
			Pay_info pay_info = paymentRequest.pay_info;
			Log.v("支付", pay_info.toString());
			String mode = pay_info.getMode();
			String device_type = pay_info.getDevice_type();
			String phone = pay_info.getPhone();
			String money=pay_info.getPay_type();//全额 定金 差额
			String registionId=pay_info.getDevice_token();
			String jStr = "{'cp':"+0+",'pt':'"+money+"','md':'" + mode + "','tk':'"+
					registionId+ "','tp':'" + device_type + "','mb':'" + phone + "'}";
			Log.v("单引号", jStr);
			PayBean payBean = new PayBean();
			payBean.setOrder_no(paymentRequest.bill_id);
			payBean.setChannel(paymentRequest.channel);
			payBean.setAmount(paymentRequest.amount);
			
			payBean.setOpen_id(pay_info.getOpen_id());
			payBean.setSubject(pay_info.getSubject());// 车位地址 
			payBean.setPay_info(jStr);

			Gson gson = new Gson();
			final String json = gson.toJson(payBean);
			Log.v("上传服务器的json数据是：", json);
			Log.v("支付反馈：", json);
			try {
				 data =
				 PostCLientUtils.doHttpsPost4(Information.PINGPP_PAY, json,mCache.getAsString("sessionToken"));
				 Log.v("支doHttpsPost反馈：", data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return data;
		}
		
		public String getDomain(String curl) {
			URL url = null;
			String q = "";
			try {
				url = new URL(curl);
				q = url.getHost();
			} catch (MalformedURLException e) {

			}
			url = null;
			return q;
		}

		public String GetInetAddress(String host) {
			String IPAddress = "";
			InetAddress ReturnStr1 = null;
			try {
				ReturnStr1 = java.net.InetAddress.getByName(host);
				IPAddress = ReturnStr1.getHostAddress();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return IPAddress;
			}
			return IPAddress;
		}
		/**
		 * 获得服务端的charge，调用ping++ sdk。
		 */
		@Override
		protected void onPostExecute(String data) {
			if (null == data) {
		// showMsg("请求出错", "请检查URL", "URL无法获取charge");
			Log.v("请求出错请检查URLURL无法获取charge", "URL无法获取charge");
				return;
			}
			Log.d("charge", data);// 获取到charge字段
			PingppLog.DEBUG = true;
			Intent intent = new Intent();
			String packageName = getPackageName();
			ComponentName componentName = new ComponentName(packageName,
					packageName + ".wxapi.WXPayEntryActivity");
			intent.setComponent(componentName);
			intent.putExtra(PaymentActivity.EXTRA_CHARGE, data);
			startActivityForResult(intent, REQUEST_CODE_PAYMENT);
		}
	}
	/**
	 * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。 最终支付成功根据异步通知为准
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 支付页面返回处理
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				String result = data.getExtras().getString("pay_result");
				String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
				String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
				showMsg(result, errorMsg, extraMsg);
			}
		}
	}
	
	public void showMsg(String title, String msg1, String msg2) {
		String str = title;
		if (null != msg1 && msg1.length() != 0) {
			str += "\n" + msg1;
		}
		if (null != msg2 && msg2.length() != 0) {
			str += "\n" + msg2;
		}
		AlertDialog.Builder builder = new Builder(PayActivity.this);
		builder.setMessage(str);
		builder.setTitle("提示");
		builder.setPositiveButton("OK", null);
		builder.create().show();
	}

	class PaymentRequest {
		String channel;
		double amount;
		String bill_id;
		Pay_info pay_info;
		
		public PaymentRequest(String channel, double amount, String bill_id,Pay_info info) {
			this.channel = channel;
			this.amount = amount;
			this.bill_id = bill_id;
			this.pay_info=info;
		}
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
}






























