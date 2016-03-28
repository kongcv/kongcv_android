package com.kongcv.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongcv.R;
import com.kongcv.fragment.CommunityDetailFragment;

public class PhoneNumberAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> mDataList=new ArrayList<String>();
	public PhoneNumberAdapter(Context context,List<String> dataList) {
		super();
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mDataList=dataList;
	}
	
	public int getCount() {// 返回数组长度

		return mDataList == null ? 0 : mDataList.size();
	}

	public Object getItem(int position) {

		return (null != mDataList && !mDataList.isEmpty()) ? null : mDataList
				.get(position);
	}

	public long getItemId(int position) {

		return mDataList == null ? 0 : position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.detail_lv_phone,
						parent, false);
			holder.tvPhone = (TextView) convertView
					.findViewById(R.id.detail_phone_number);
			holder.imgPhone = (ImageView) convertView.findViewById(R.id.detail_phone);
			convertView.setTag(holder);// 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 给控件设置数据
		holder.tvPhone.setText((CharSequence) mDataList.get(position));
		holder.imgPhone.setImageResource(R.drawable.img_detail_phone);
		holder.imgPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 // 直接连接打电话  
				 Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://"+mDataList.get(position)));    
				 mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tvPhone;
		ImageView imgPhone;
	}
}
























