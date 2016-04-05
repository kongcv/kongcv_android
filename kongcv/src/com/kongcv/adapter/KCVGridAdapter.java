package com.kongcv.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.kongcv.R;
import com.kongcv.utils.AndroidUtil;
import com.kongcv.utils.BaseViewHolder;

public class KCVGridAdapter extends BaseAdapter {
	
	
	private List<Object> mList;
	private Context mContext;
	private ImageLoader imageLoader;
	public KCVGridAdapter(Context context,List<Object> list,
			ImageLoader imageLoader) {
		super();
		this.mContext = context;
		this.mList=list;
		this.imageLoader = imageLoader;
		allCount= mList.size();
		lastCount= allCount % 4;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size()+(lastCount>0?4-lastCount:0);
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	private int allCount=0;
	private int lastCount=0;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.grid_item, 
					parent, false);
		}
		NetworkImageView view = BaseViewHolder.get(convertView, R.id.gridview_net_image);
		LayoutParams params = view.getLayoutParams();
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		params.width=width/3;
		if(AndroidUtil.pictureOrT(mContext)){
			Log.d("falg", "AndroidUtil.pictureOrT(mContext)");
			params.height=width*100/450;
		}else{
			Log.d("else falg", "AndroidUtil.pictureOrT(mContext)");
			params.height=width/4;
		}
		view.setLayoutParams(params);
		if(position<allCount){
			view.setImageUrl((String) mList.get(position), imageLoader);
		}else{
			view.setImageResource(R.drawable.gridview_background);
			view.setOnClickListener(null);
		}
		return convertView;
	}
	
	
}



















