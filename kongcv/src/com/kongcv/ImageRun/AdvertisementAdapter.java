package com.kongcv.ImageRun;

import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.kongcv.R;
import com.kongcv.utils.ConfigCacheUtil;

/**
 * 广告轮播adapter
 */
public class AdvertisementAdapter extends PagerAdapter {

	private Context context;
	private List<View> views;
//	JSONArray advertiseArray;

	public AdvertisementAdapter() {
		super();
	}

	private List<String> advertiseArray;
	/*public AdvertisementAdapter(Context context, List<View> views,
			JSONArray advertiseArray) {
		this.context = context;
		this.views = views;
		this.advertiseArray = advertiseArray;
	}*/
	public AdvertisementAdapter(Context context, List<View> views,
			List<String> advertiseArray) {
		this.context = context;
		this.views = views;
		this.advertiseArray = advertiseArray;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(views.get(position));
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(views.get(position), 0);
		final int POSITION = position;
		View view = views.get(position);
		try {
			/*String head_img = advertiseArray.optJSONObject(position).optString(
					"head_img");*/
			
			String head_img = advertiseArray.get(position);
			ImageView ivAdvertise = (ImageView) view
					.findViewById(R.id.ivAdvertise);
			
			// 默认加载断网的时候加载本地图片
			 ImageLoaderUtil.getImage(context, ivAdvertise, head_img,
			 				R.drawable.erroro, R.drawable.erroro,0,0);
			 
			ivAdvertise.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 点击跳转页面
					Toast.makeText(context, "第" + POSITION + "个广告图片被点击", 0)
							.show();
					// 模拟加载地图
					/*Uri uri = Uri.parse("geo:38.899533,-77.036476");
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					context.startActivity(it);*/
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}
	public Bitmap getBitmapFromRes(int resId) {
		Resources res = context.getResources();
		return BitmapFactory.decodeResource(res, resId);

	}
}

	