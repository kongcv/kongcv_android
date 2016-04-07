package com.kongcv.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import cn.jpush.android.api.JPushInterface;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.ImageRun.GetImage;
import com.kongcv.UI.AsyncImageLoader.PreReadTask;
import com.kongcv.adapter.CreditTypeAdapter;
import com.kongcv.global.CardTypeBean;
import com.kongcv.global.Information;
import com.kongcv.utils.BitmapCache;
import com.umeng.analytics.MobclickAgent;
/**
 * 银行类型页面
 * @author kcw
 */
public class MineCreditType extends Activity {
	private ListView lv_type;
	private List<CardTypeBean> cList;
	private ImageView iv_back;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			lv_type.setAdapter(new CreditTypeAdapter(MineCreditType.this, cList,imageLoader));
			lv_type.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent,
						View view, int position, long id) {
					String choiceBankType = cList.get(position)
							.getBank();
					Bitmap choiceBankImg = cList.get(position)
							.getBitmap();
					String url=cList.get(position).getUrl();
					Intent i = new Intent();
					i.putExtra("choiceBankType", choiceBankType);
					i.putExtra("choiceBankImg", choiceBankImg);
					i.putExtra("bankUrl", url);
					setResult(0, i);
					finish();
				}
			});

		};
	};

	private ImageLoader imageLoader;
	private RequestQueue mQueue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_credit_type_list);
		mQueue = Volley.newRequestQueue(this);
		imageLoader = new ImageLoader(mQueue, new BitmapCache());
		
		initView();
		MyApplication.getInstance().addActivity(this);
		init();
	}
	private void init() {
		ReadType readType=new ReadType();
		readType.execute();
	}
	class ReadType extends PreReadTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			initData();
			return null;
		}
	}
	/**
	 * 接口获取银行数据
	 */
	public static final MediaType MEDIA_TYPE_MARKDOWN
	  = MediaType.parse("application/json;charset=utf-8");
	private final OkHttpClient client = new OkHttpClient();
	private void initData() {
		Request request=new Request.Builder()
		  .url(Information.KONGCV_GET_BANK)
		  .headers(Information.getHeaders())
	      .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, "{}"))
	      .build();
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				if(response.isSuccessful()){
					OkHttpReadInfo(response.body().string());
				}
			}
			@Override
			public void onFailure(Call arg0, IOException arg1) {
		
			}
		});
		
	}
	private void OkHttpReadInfo(String doHttpsPost) {
		JSONObject Resultobj;
		try {
			Resultobj = new JSONObject(doHttpsPost);
			String result = Resultobj.getString("result");
			JSONArray arry = new JSONArray(result);
			cList = new ArrayList<CardTypeBean>();
			CardTypeBean bean;
			for (int i = 0; i < arry.length(); i++) {
				bean = new CardTypeBean();
				String bank = arry.getJSONObject(i).getString("bank");
				String url = arry.getJSONObject(i)
						.getJSONObject("picture").getString("url");
				Bitmap bitmap = GetImage.getImage(url);
				bean.setBank(bank);
				bean.setBitmap(bitmap);
				bean.setUrl(url);
				cList.add(bean);
			}
			Message msg = Message.obtain();
			msg.what = 1;
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void initView() {
		lv_type = (ListView) findViewById(R.id.lv_type);
		iv_back=(ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

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
