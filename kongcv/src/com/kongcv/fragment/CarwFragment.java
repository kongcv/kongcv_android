package com.kongcv.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.kongcv.R;
import com.kongcv.ImageRun.Advertisements;
import com.kongcv.ImageRun.RequestManager;
import com.kongcv.UI.AsyncImageLoader.PreReadTask;
import com.kongcv.activity.HomeActivity;
import com.kongcv.activity.LogoActivity.ModeAndObjId;
import com.kongcv.activity.SearchActivity;
import com.kongcv.adapter.KCVGridAdapter;
import com.kongcv.global.Community;
import com.kongcv.global.Information;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.BitmapCache;
import com.kongcv.utils.Data;
import com.kongcv.view.KCVGridView;
/**
 * 空车位首页
 */
public class CarwFragment extends Fragment implements OnClickListener,
		OnItemClickListener {
	
	private static final String TAG = "CarwFragment";
	private HomeActivity homeActivity;
	private ImageView btn1, btn2;
	private RequestQueue mQueue;
	private ImageLoader imageLoader;
	private LinearLayout llAdvertiseBoard;// 获取图片线性布局
	private LayoutInflater inflater;
	private View kongcvLayout;
	private ACacheUtils mCache;
	private String mode0,mode1;
	private final OkHttpClient client = new OkHttpClient();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(savedInstanceState==null){
			kongcvLayout = inflater.inflate(R.layout.mkongcv_activity, container,
					false);
			homeActivity = (HomeActivity) getActivity();
			mCache = ACacheUtils.get(homeActivity);
			RequestManager.init(getActivity());
			inflater = LayoutInflater.from(getActivity());
			mQueue = Volley.newRequestQueue(homeActivity);
			imageLoader = new ImageLoader(mQueue, new BitmapCache());
			// 初始化view
			initView();
			// 初始化滑动button view
			initListView();
			// 获取到停车类型数据 及 是道路 还是社区
			btnImage();
			// 图片跑马灯
			ImageRun();
			getType();//异步加载处理数据
		}
		return kongcvLayout;
	}
	private List<ModeAndObjId> data=null;
	private void getType() {
		// TODO Auto-generated method stub
		if(data!=null){
			ReadType reading = new ReadType();
			reading.execute();
		}
	}
	class ReadType extends PreReadTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			ModeAndObjId modeAndObjId0 = data.get(0);
			ModeAndObjId modeAndObjId1 = data.get(1);
			GridViewOkHttp(modeAndObjId0, "0");
			GridViewOkHttp(modeAndObjId1, "1");
			return null;
		}
	}
	private KCVGridView gridView;
	private KCVGridAdapter gridAdapter;
	private void initListView() {
		gridView=(KCVGridView) kongcvLayout.findViewById(R.id.kcvgridview);
		gridView.setOnItemClickListener(this);
	}
	private void initView() {
		btn1 = (ImageView) kongcvLayout.findViewById(R.id.image1);
		btn2 = (ImageView) kongcvLayout.findViewById(R.id.image2);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
	}
	private Community CommunityCurb0,CommunityCurb1;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Community community;
			List<String> objectIdList,hire_fieldList,methodList,urlList;// objectId
			switch (msg.what) {
			case 0:
				community = (Community) msg.obj;
				List urlList0 = community.getUrl();
				gridAdapter=new KCVGridAdapter(homeActivity, urlList0, imageLoader);
				gridView.setAdapter(gridAdapter);
				gridAdapter.notifyDataSetChanged();
				if(community.getMode().equals("community")){
					Data.putData("community", community);
				}
				CommunityCurb0=community;
				break;
			case 1:
				community = (Community) msg.obj;
				if(community.getMode().equals("community")){
					Data.putData("community", community);
				}
				CommunityCurb1=community;
				break;
			case 2:
				List<String> url=(List<String>) msg.obj;
				getImage(url.get(0), btn1);
				getImage(url.get(1), btn2);
				break;
			default:
				break;
			}
		}
	};
	
	
	private void GridViewOkHttp(final ModeAndObjId andObjId,final String img){
		FormBody body = new FormBody.Builder()
	     .add("park_type_id", andObjId.objList)
	     .build();
		okhttp3.Request request=new okhttp3.Request.Builder()
		  .url(Information.KONGCV_GET_HIRE_METHOD)
		  .headers(Information.getHeaders())
	      .post(body)
	      .build();
		client.newCall(request).enqueue(new okhttp3.Callback() {
			@Override
			public void onResponse(Call arg0, okhttp3.Response response) throws IOException {
				// TODO Auto-generated method stub
				if(response.isSuccessful()){
					if ("0".equals(img)) {// 返回图片id要与之绑定 mode社区 道路
						mode0=andObjId.mode;
						getCacheThread(response.body().string(), img,mode0);
					} else if ("1".equals(img)) {
						mode1=andObjId.mode;
						getCacheThread(response.body().string(), img,mode1);
					}
				}
			}
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				Log.e("KONGCV_GET_HIRE_METHOD", arg1.toString());
			}
			});
	}
	/**
	 * 跑马灯效果
	 */
	private void ImageRun() {
		try {
			// 图片处理
			llAdvertiseBoard = (LinearLayout) kongcvLayout
					.findViewById(R.id.llAdvertiseBoard);
			inflater = LayoutInflater.from(this.getActivity());
			List data = new ArrayList<String>();
			data = (List) Data.getData("url");
			if (data != null && data.size() > 0) {
				llAdvertiseBoard.addView(new Advertisements(homeActivity, true,
						inflater, 3000).initView(data));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getCacheThread(String jsonStr, String img,String mode) {
		Message msg = mHandler.obtainMessage();
		if ("0".equals(img)) {
			Community info = getInfo(jsonStr, img,mode);
			msg.what = 0;
			msg.obj = info;
		} else {
			Community info = getInfo(jsonStr, img,mode);
			msg.what = 1;
			msg.obj = info;
		}
		mHandler.sendMessage(msg);
	}
	
	public Community getInfo(String jsonStr, String img,String mode) {
		try {
			JSONObject demoJson = new JSONObject(jsonStr);
			JSONArray numberList = demoJson.getJSONArray("result");
			List<String> objectIdList = new ArrayList<String>();// objectId
			List<String> hire_fieldList = new ArrayList<String>();// 添加的字段
			List<String> methodList = new ArrayList<String>();
			List<String> urlList = new ArrayList<String>();
			List<Integer> hire_typeList=new ArrayList<Integer>();
			Community community = new Community();
			for (int i = 0; i < numberList.length(); i++) {
				String method = numberList.getJSONObject(i).getString("method");
				String field = numberList.getJSONObject(i).getString("field");
				int hire_type = numberList.getJSONObject(i).getInt("hire_type");
				hire_typeList.add(hire_type);//判断
				if(img.equals("0")){	
					if(mode.equals("community")){
						methodList.add(method);
						community.setMethod(methodList);
						hire_fieldList.add(field);
						String objectId = numberList.getJSONObject(i).getString(
								"objectId");
						objectIdList.add(objectId);
						JSONObject picture = numberList.getJSONObject(i)
								.getJSONObject("picture_community");
						String url = picture.getString("url");
						urlList.add(url);
					}else{
						methodList.add(method);
						hire_fieldList.add(field);
						String objectId = numberList.getJSONObject(i).getString(
								"objectId");
						objectIdList.add(objectId);
						JSONObject picture = numberList.getJSONObject(i)
								.getJSONObject("picture_curb");
						String url = picture.getString("url");
						urlList.add(url);
					}
				} else {
					if(mode.equals("curb")){
						methodList.add(method);
						hire_fieldList.add(field);
						String objectId = numberList.getJSONObject(i).getString(
								"objectId");
						objectIdList.add(objectId);
						JSONObject picture = numberList.getJSONObject(i)
								.getJSONObject("picture_curb");
						String url = picture.getString("url");
						urlList.add(url);
					}else{
						methodList.add(method);
						community.setMethod(methodList);
						hire_fieldList.add(field);
						String objectId = numberList.getJSONObject(i).getString(
								"objectId");
						objectIdList.add(objectId);
						JSONObject picture = numberList.getJSONObject(i)
								.getJSONObject("picture_community");
						String url = picture.getString("url");
						urlList.add(url);
					}
				}
			}
			community.setHire_type(hire_typeList);
			community.setHire_field(hire_fieldList);
			community.setMethod(methodList);
			community.setObjectId(hire_fieldList);
			community.setUrl(urlList);
			community.setObjectId(objectIdList);
			community.setMode(mode);
			return community;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取道路和社区照片
	 */
	public void btnImage() {
		data = (List<ModeAndObjId>) Data.getData("objectIddoReadBtn");
		List<String> urlList=new ArrayList<String>();
		/*if(data==null){
			loadJson();
		}else{
			for(int i=0;i<data.size();i++){
				urlList.add(data.get(i).url);
			}
			Message msg = new Message();
			msg.what = 2;
			msg.obj = urlList;
			mHandler.sendMessage(msg);
		}*/
		if(data!=null){
			for(int i=0;i<data.size();i++){
				urlList.add(data.get(i).url);
			}
			Message msg = new Message();
			msg.what = 2;
			msg.obj = urlList;
			mHandler.sendMessage(msg);
		}
	}
	/*private ArrayList<JsonObjectRequest> mRequestQueue;
	private void loadJson() {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();  
		params.put("{}", null);  
		RequestQueue requestQueue = Volley.newRequestQueue(homeActivity);
		Request<JSONObject> request = new NormalPostRequest(Information.KONGCV_GET_PARK_TYPE,
		    new Response.Listener<JSONObject>() {
		        @Override
		        public void onResponse(JSONObject response) {
		            Log.v("空车位", "response -> " + response.toString());
		            Log.v("空车位", "response -> " + response.toString());
		            Log.v("空车位", "response -> " + response.toString());
		            Log.d(TAG, "response -> " + response.toString());
		            Log.d(TAG, "response -> " + response.toString());
		            Log.d(TAG, "response -> " + response.toString());
		        }
		    }, new Response.ErrorListener() {
		        @Override
		        public void onErrorResponse(VolleyError error) {
		            Log.e(TAG, error.getMessage(), error);
		        }
		    }, params);
		requestQueue.add(request);
	}
	
	private void btnImgCache() {
		// TODO Auto-generated method stub
		try {
			JSONObject obj = new JSONObject();
			obj.put("{}", null);
			String jsonStr = PostCLientUtils
					.doHttpsPost(Information.KONGCV_GET_PARK_TYPE,
							JsonStrUtils.JsonStr(obj));
			mCache.put("btnImgCache", jsonStr,ACacheUtils.TIME_DAY);
			doBtnImg(jsonStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void doBtnImg(String jsonStr) {
		// TODO Auto-generated method stub
		try {
			Map<Integer, String> map = new HashMap<Integer, String>();
			List<String> list = new ArrayList<String>();
			List<String> modeList=new ArrayList<String>();
			
			JSONObject demoJson = new JSONObject(jsonStr);
			JSONArray numberList = demoJson.getJSONArray("result");
			for (int i = 0; i < numberList.length(); i++) {
				String name = numberList.getJSONObject(i).getString("name");
				modeList.add(name);
	
				JSONObject object = numberList.getJSONObject(i).getJSONObject(
						"picture_small");
				String url = object.getString("url");
				// 使用另一种方式传递数据
				String objectId = numberList.getJSONObject(i).getString(
						"objectId");
				list.add(objectId);
				map.put(i, url);// 道路和社区用到了
			}
			CurbOrComm comm=new CurbOrComm(modeList, map);
			Data.putData("objectId", list);
			Message msg = new Message();
			msg.what = 2;
			msg.obj = comm;
			mHandler.sendMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	/*class CurbOrComm {
		private List<String> mList;
		private Map<Integer,String> map;
		public CurbOrComm(List<String> list,Map<Integer,String> map){
			this.mList=list;
			this.map=map;
		}
	}*/
	private void getImage(String value, ImageView img) {
		// TODO Auto-generated method stub
		ImageListener listener = ImageLoader.getImageListener(img,
				0, 0);//
		imageLoader.get(value, listener);
	}
	/**
	 * 点击社区和道路出现类型图片
	 */
	private boolean whichClick=true;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image1:
			if(CommunityCurb0!=null){
				List urlList0 = CommunityCurb0.getUrl();
				gridAdapter=new KCVGridAdapter(homeActivity, urlList0, imageLoader);
				gridView.setAdapter(gridAdapter);
				gridAdapter.notifyDataSetChanged();
				whichClick=true;
			}
			break;
		case R.id.image2:
			if(CommunityCurb1!=null){
				List urlList1 = CommunityCurb1.getUrl();
				gridAdapter=new KCVGridAdapter(homeActivity, urlList1, imageLoader);
				gridView.setAdapter(gridAdapter);
				gridAdapter.notifyDataSetChanged();
				whichClick=false;
			}
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * 类型图片点击实现跳转到搜索页
	 */
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
		// TODO Auto-generated method stub
		view.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(homeActivity, SearchActivity.class);
				Bundle bundle = new Bundle();
				Community community;
				if (whichClick) {//判断是点击的是哪一个   传递的是哪一个
					community=CommunityCurb0;
					if(community.getMode().equals("curb")){
						List<Integer> hire_type = community.getHire_type();
						if(hire_type.get(position)==2){
							bundle.putInt("hire_type", hire_type.get(position));
						}
						bundle.putSerializable("curb", community);//实体类
					}else{
						bundle.putSerializable("community", community);//实体类     
					}
					List<String> objectId = community.getObjectId();
					String communityId = objectId.get(position);
					List<String> hire_field = community.getHire_field();
					String field = hire_field.get(position);
					Log.e("if whichClick field", field+":");
					Log.e("if whichClick mode", community.getMode()+":");
					
					bundle.putString("objectId", communityId);//车位id
					bundle.putString("mode", community.getMode());//道路
					bundle.putString("hire_field", field);//hire_field字段
				} else {
					community=CommunityCurb1;
					if(community.getMode().equals("curb")){
						List<Integer> hire_type = community.getHire_type();
						if(hire_type.get(position)==2){
							bundle.putInt("hire_type", hire_type.get(position));
						}
						bundle.putSerializable("curb", community);//实体类
					}else{
						bundle.putSerializable("community", community);//实体类     
					}
					List<String> objectId = community.getObjectId();
					String communityId = objectId.get(position);
					List<String> hire_field = community.getHire_field();
					String field = hire_field.get(position);
					Log.e("else whichClick field", field+":");
					Log.e("else whichClick mode", community.getMode()+":");
					
					bundle.putString("objectId", communityId);//车位id
					bundle.putString("mode", community.getMode());//道路
					bundle.putString("hire_field", field);//hire_field字段
				}
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}, 800);
	}
}
