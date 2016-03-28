package com.kongcv.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import cn.jpush.android.api.JPushInterface;

import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.JsonStrUtils;

public class WriteCommentActivity extends Activity implements OnClickListener,
		OnRatingBarChangeListener {

	private ImageView ivBack;
	private RatingBar ratingBar;
	private EditText editText;
	private Button button;
	private ACacheUtils mCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_comment);
		MyApplication.getInstance().addActivity(this);
		mCache = ACacheUtils.get(getApplicationContext());
		initView();
	}

	private void initView() {
		ivBack = (ImageView) findViewById(R.id.wr_iv_back);
		ratingBar = (RatingBar) findViewById(R.id.wr_ratingbar);
		editText = (EditText) findViewById(R.id.et_writer);
		button = (Button) findViewById(R.id.rl_tijiao);
		
		ivBack.setOnClickListener(this);
		ratingBar.setOnRatingBarChangeListener(this);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.wr_iv_back:
			finish();
			break;
		case R.id.rl_tijiao:
			doInfo();
			break;
		default:
			break;
		}
	}

	private void doInfo() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Info();
			}
		}).start();
	}

	private void Info() {
		// TODO Auto-generated method stub
		try {
			Intent i=getIntent();
			JSONObject obj = new JSONObject();
			obj.put("user_id", mCache.getAsString("user_id"));
			obj.put("comment", editText.getText().toString());//内容
			obj.put("park_id",i.getStringExtra("park_id"));
			obj.put("grade", ratingBar.getAlpha());
			obj.put("mode", i.getStringExtra("MODE"));
			Log.v("评论的数据是JsonStrUtils.JsonStr(obj)", JsonStrUtils.JsonStr(obj));
			
			
			/*String doHttpsPost = PostCLientUtils.doHttpsPost(
					Information.KONGCV_INSERT_COMMENT,
					JsonStrUtils.JsonStr(obj));
			Log.v("评论完了获取到的数据是doHttpsPost", doHttpsPost);
			JSONObject object=new JSONObject(doHttpsPost);
			String str = object.getString("result");
			JSONObject result= new JSONObject(str);
			String state = result.getString("state");
			if("ok".equals("state")){
				Looper.prepare();
				ToastUtil.show(getBaseContext(), "提交评论成功！");
				Looper.loop();
			}else {
				Looper.prepare();
				ToastUtil.show(getBaseContext(), "提交评论失败！");
				Looper.loop();
			}*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 设置滑动改变侦听
	 */
	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		// TODO Auto-generated method stub
		ratingBar.setAlpha(rating);
		/*Log.e("ratingBar setAlpha", ratingBar.getAlpha()+"::");
		ratingBar.setStepSize(rating);
		Log.e("ratingBar setStepSize", ratingBar.getStepSize()+"::");*/
		
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
