package com.kongcv.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongcv.R;
import com.kongcv.global.InfoBean;

public class InfoNotifyAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<InfoBean> mList;

	public InfoNotifyAdapter(Context context, ArrayList<InfoBean> mList) {
		this.context = context;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.mine_info_item, null);
			holder = new ViewHolder();
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_address);
			holder.tv_starttime = (TextView) convertView
					.findViewById(R.id.tv_starttime);
			holder.tv_choice = (TextView) convertView
					.findViewById(R.id.tv_choice);
			holder.iv_address = (ImageView) convertView
					.findViewById(R.id.iv_address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_address.setText(mList.get(position).getAddress());
		holder.tv_starttime.setText(mList.get(position).getHire_start());
		holder.tv_choice.setText(mList.get(position).getState());
		if (mList.get(position).getMode().equals("community")) {
			holder.iv_address.setImageResource(R.drawable.bg_shequ_info);
		} else {
			holder.iv_address.setImageResource(R.drawable.bg_daolu_info);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_starttime, tv_address,tv_choice;
		ImageView iv_address;

	}

}