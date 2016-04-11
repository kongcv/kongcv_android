package com.kongcv.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kongcv.R;
import com.kongcv.activity.NaviStartActivity;
import com.kongcv.activity.SearchActivity;
import com.kongcv.adapter.MyFragmentAdapter;
import com.kongcv.global.CurbAndObjectId;
import com.kongcv.global.DetailBean;
import com.kongcv.global.ZyCommityAdapterBean;
import com.kongcv.global.DetailBean.ResultEntity;
import com.kongcv.global.DetailBean.ResultEntity.LocationEntity;
import com.kongcv.global.Information;
import com.kongcv.utils.Data;
import com.kongcv.utils.NormalPostRequest;
import com.kongcv.utils.ToastUtil;

/**
 * 地图搜索点击item事件后 进入到此处 评论详情页
 */

public class DetailsFragment extends Fragment implements
		OnCheckedChangeListener, OnClickListener, OnPageChangeListener {

	private View view;
	private List<Fragment> mFragmentList;
	private ViewPager mViewPager;
	private RadioGroup mRadioGroup;
	private RadioButton mRadioButton1, mRadioButton2;
	private ImageView mBack, mDaoh, mImageView;
	private String mode,park_id;
	double price;
	private float mCurrentCheckedRadioLeft;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_details, null);
		initView();
		return view;
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		addFragment();// 添加道边详情页
		mViewPager = (ViewPager) view.findViewById(R.id.pager);
		mViewPager.setOnPageChangeListener(this);

		mRadioGroup = (RadioGroup) view.findViewById(R.id.radio);
		mRadioGroup.setOnCheckedChangeListener(this);
		mRadioButton1 = (RadioButton) view.findViewById(R.id.btn1);
		mRadioButton1.setChecked(true);
		mRadioButton2 = (RadioButton) view.findViewById(R.id.btn2);

		mBack = (ImageView) view.findViewById(R.id.iv_back);
		mBack.setOnClickListener(this);

		MyFragmentAdapter adapter = new MyFragmentAdapter(
				getChildFragmentManager(), mFragmentList);
		mViewPager.setAdapter(adapter);
		mDaoh = (ImageView) view.findViewById(R.id.details_daoh);
		mDaoh.setOnClickListener(this);
		mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();
		mImageView = (ImageView) view.findViewById(R.id.img1);
	}

	private String field,stringExtra=null,string=null;
	private String CurbMineReceiver=null;
	private ZyCommityAdapterBean mCommBean=null;
	private void addFragment() {
			Bundle extras = getArguments();
			if(extras!=null){
				mode = extras.getString("mode");
				park_id = extras.getString("park_id");
				price = extras.getDouble("price");
				field = extras.getString("field");
			}
			mFragmentList = new ArrayList<Fragment>();
			
			stringExtra = extras.getString("MineSendFragment");
			string = extras.getString("stringExtra");
			CurbMineReceiver = extras.getString("CurbMineReceiver");
			if(extras.containsKey("mCommBean"))
			mCommBean=(ZyCommityAdapterBean) extras.getSerializable("mCommBean");
			if (mode != null && park_id != null) {
				if (mode.equals("curb")) {
					CurbDetailFragment curbDetailFragment = new CurbDetailFragment();
					Bundle args = new Bundle();
					if (stringExtra != null) {
						args.putString("MineSendFragment", stringExtra);
					}
					if (string != null) {
						args.putString("stringExtra", string);
					}
					if (CurbMineReceiver != null) {
						args.putString("CurbMineReceiver", CurbMineReceiver);
					}
					if(mCommBean!=null){
						args.putSerializable("mCommBean", mCommBean);
					}
					args.putString("mode", mode);
					args.putString("park_id", park_id);
					args.putDouble("price", price);
					args.putString("field", field);
					curbDetailFragment.setArguments(args);
					mFragmentList.add(curbDetailFragment);
					CommentFragment commFragment = new CommentFragment();
					Bundle args2 = new Bundle();
					args2.putString("mode", mode);
					args2.putString("park_id", park_id);
					commFragment.setArguments(args2);
					mFragmentList.add(commFragment);
				} else {
					CommunityDetailFragment communityDetailFragment = new CommunityDetailFragment();
					Bundle args = new Bundle();
					if (stringExtra != null) {
						args.putString("MineSendFragment", stringExtra);
					}
					if (string != null) {
						args.putString("stringExtra", string);
					}
					if (CurbMineReceiver != null) {
						args.putString("CurbMineReceiver", CurbMineReceiver);
					}
					if(mCommBean!=null){
						args.putSerializable("mCommBean", mCommBean);
					}
					args.putString("mode", mode);
					args.putString("park_id", park_id);
					args.putDouble("price", price);
					args.putString("field", field);

					communityDetailFragment.setArguments(args);
					mFragmentList.add(communityDetailFragment);

					CommentFragment commFragment = new CommentFragment();
					Bundle args2 = new Bundle();
					args2.putString("mode", mode);
					args2.putString("park_id", park_id);
					commFragment.setArguments(args2);
					mFragmentList.add(commFragment);
				}
			} else {
				CurbAndObjectId bean = (CurbAndObjectId) Data
						.getData("CurbAndObjectId");
				mode = bean.getMode();
				park_id = bean.getObjectId();
				if (bean != null && mode.equals("curb")) {
					CurbDetailFragment fragment = new CurbDetailFragment();
					Bundle args = new Bundle();
					args.putString("mode", mode);
					args.putString("park_id", park_id);
					args.putDouble("price", price);

					args.putString("field", field);
					fragment.setArguments(args);

					mFragmentList.add(fragment);
					CommentFragment commFragment = new CommentFragment();
					Bundle args2 = new Bundle();
					args2.putString("mode", mode);
					args2.putString("park_id", park_id);
					commFragment.setArguments(args2);
					mFragmentList.add(commFragment);
				} else {
					CommunityDetailFragment communityDetailFragment = new CommunityDetailFragment();
					Bundle args = new Bundle();
					args.putString("mode", mode);
					args.putString("park_id", park_id);
					args.putDouble("price", price);

					args.putString("field", field);
					communityDetailFragment.setArguments(args);

					mFragmentList.add(communityDetailFragment);
					CommentFragment commFragment = new CommentFragment();
					Bundle args2 = new Bundle();
					args2.putString("mode", mode);
					args2.putString("park_id", park_id);
					commFragment.setArguments(args2);
					mFragmentList.add(commFragment);
				}
			}
	}
	private float getCurrentCheckedRadioLeft() {
		// TODO Auto-generated method stub
		if (mRadioButton1.isChecked()) {
			mDaoh.setVisibility(View.VISIBLE);
			return getResources().getDimension(R.dimen.rdo1);
		} else if (mRadioButton2.isChecked()) {
			mDaoh.setVisibility(View.INVISIBLE);
			return getResources().getDimension(R.dimen.rdo2);
		}
		return 0f;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.btn1:
			mDaoh.setVisibility(View.VISIBLE);
			imageTranslateAnimation(getResources().getDimension(R.dimen.rdo1));
			mViewPager.setCurrentItem(0);
			break;
		case R.id.btn2:
			mDaoh.setVisibility(View.INVISIBLE);
			imageTranslateAnimation(getResources().getDimension(R.dimen.rdo2));
			mViewPager.setCurrentItem(1);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:// 点击返回上一页
			getActivity().finish();
			break;
		case R.id.details_daoh:// 点击进入导航 闪过路线之后 导航 需传入当前坐标和item的经纬度值
			loadJson();
			break;
		default:
			break;
		}
	}
	private void loadJson() {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("park_id", park_id);
		params.put("mode", mode);
		RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
		Request<JSONObject> request = new NormalPostRequest(
				Information.KONGCV_GET_PARK_INFO,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						if (response.toString().equals("{}")) {
							Looper.prepare();
							ToastUtil.show(getActivity(), "没有数据！请重新搜索！");
							Looper.loop();
							Intent intent = new Intent(getActivity(),
									SearchActivity.class);
							startActivity(intent);
							getActivity().finish();
						} else {
							Gson gson = new Gson();
							DetailBean bean = gson.fromJson(
									response.toString(), DetailBean.class);
							ResultEntity result = bean.getResult();
							LocationEntity location = result.getLocation();
							double latitude = location.getLatitude();
							double longitude = location.getLongitude();
							Intent intent = new Intent(getActivity(),
									NaviStartActivity.class);
							intent.putExtra("latitude", latitude);
							intent.putExtra("longitude", longitude);
							startActivity(intent);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("空车位", error.getMessage(), error);
					}
				}, params);
		requestQueue.add(request);
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		((RadioButton) mRadioGroup.getChildAt(position)).setChecked(true);
	}

	private void imageTranslateAnimation(float f) {
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation = new TranslateAnimation(
				mCurrentCheckedRadioLeft, f, 0f, 0f);
		
		animationSet.addAnimation(translateAnimation);
		animationSet.setFillBefore(false);
		animationSet.setFillAfter(true);
		animationSet.setDuration(100);

		// mImageView.bringToFront();
		mImageView.startAnimation(animationSet);
	}

	// ==================
	public void onDetach() {
		super.onDetach();
		// 当fragment销毁时通过反射控制mChildFragmentManager也重置
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
