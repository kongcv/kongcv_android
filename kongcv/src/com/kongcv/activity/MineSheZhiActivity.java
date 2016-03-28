package com.kongcv.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.utils.ACacheUtils;

/**
 * 设置页面
 * 
 * @author kcw
 * 
 */
public class MineSheZhiActivity extends Activity implements OnClickListener {
	private RelativeLayout rl_infofeedback;
	private RelativeLayout rl_changephonenumber;
	private RelativeLayout rl_aboutus;
	private TextView tv_phonenumber;
	private ImageView iv_back;
	private Button bt_quit;
	public static String TAG = "first";
	private ACacheUtils mCache;
	private RelativeLayout rl_addnumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_shezhi);
		mCache=ACacheUtils.get(this);
		MyApplication.getInstance().addActivity(this);
		initView();
	}

	private void initView() {
		rl_addnumber = (RelativeLayout) findViewById(R.id.rl_addnumber);
		rl_infofeedback = (RelativeLayout) findViewById(R.id.rl_infofeedback);
		rl_changephonenumber = (RelativeLayout) findViewById(R.id.rl_changephonenumber);
		rl_aboutus = (RelativeLayout) findViewById(R.id.rl_aboutus);
		rl_infofeedback.setOnClickListener(this);
		rl_addnumber.setOnClickListener(this);
		rl_changephonenumber.setOnClickListener(this);
		rl_aboutus.setOnClickListener(this);
		tv_phonenumber = (TextView) findViewById(R.id.tv_phonenumber);
		tv_phonenumber.setText(mCache.getAsString("USER"));
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		bt_quit = (Button) findViewById(R.id.bt_quit);
		bt_quit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_infofeedback:
			Intent intentinfo = new Intent(this, MineInfofeedBack.class);
            startActivity(intentinfo);

			break;
		case R.id.rl_addnumber:
			Intent intentaddnumber = new Intent(this, MineAddNumber.class);
			startActivity(intentaddnumber);
			break;

		case R.id.rl_changephonenumber:
			Intent intentchangephonenumber = new Intent(this,
					MineChangePhoneNumber.class);
			startActivity(intentchangephonenumber);
			finish();
			break;

		case R.id.rl_aboutus:
			Intent intentaboutus = new Intent(this, MineAboutus.class);
			startActivity(intentaboutus);
			
			break;
		case R.id.iv_back:
			finish();
			break;
		case R.id.bt_quit:
			quit();
			break;
		default:
			break;
		}

	}

	private void quit() {
		new AlertDialog.Builder(MineSheZhiActivity.this).setTitle("确定要退出系统吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MyApplication.getInstance().exit();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
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

}