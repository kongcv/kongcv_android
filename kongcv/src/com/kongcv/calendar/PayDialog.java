package com.kongcv.calendar;

import java.util.ArrayList;
import java.util.List;

import com.kongcv.R;
import com.kongcv.utils.ToastUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * 支付页面 
 */
public class PayDialog extends Dialog implements
		android.view.View.OnClickListener {

	private Context context;
	private MyPayDialogListener listener;

	private List<String> list = new ArrayList<String>();
	private ImageView alipay;
	private ImageView weChat;
	private ImageView mBreak;

	public interface MyPayDialogListener {// 定义接口处理点击事件
		public void onClick(View view);
	}

	public PayDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public PayDialog(Context context, int theme, MyPayDialogListener listener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		alipay = (ImageView) findViewById(R.id.iv_alipay);
		weChat = (ImageView) findViewById(R.id.iv_wechat);
		mBreak = (ImageView) findViewById(R.id.iv_pay_break);
		alipay.setOnClickListener(this);
		weChat.setOnClickListener(this);
		mBreak.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_alipay:

			ToastUtil.show(context, "微信支付可点击！");
			break;
		case R.id.iv_wechat:

			ToastUtil.show(context, "支付宝支付可点击！");
			break;
		case R.id.iv_pay_break://点击dialog消失
			
			ToastUtil.show(context, "点击回退");
			dismiss();
			break;
		default:
			break;
		}
	}

}
