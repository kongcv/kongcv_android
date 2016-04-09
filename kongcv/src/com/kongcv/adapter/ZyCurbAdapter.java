package com.kongcv.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kongcv.R;
import com.kongcv.global.ZyCommityAdapterBean;
import com.kongcv.utils.BaseViewHolder;

public class ZyCurbAdapter extends BaseAdapter {

	private Context context;
	private List<ZyCommityAdapterBean> mList;

	public ZyCurbAdapter(Context context, List<ZyCommityAdapterBean> mList) {
		this.context = context;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList.size();

	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	TextView tv_username, tv_start, tv_end, tv_order, tv_price, tv_state,
			tv_method;
	private int state;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.comm_order_item, null);
		}
		tv_username = BaseViewHolder.get(convertView, R.id.tv_username);
		tv_start = BaseViewHolder.get(convertView, R.id.tv_start);
		tv_end = BaseViewHolder.get(convertView, R.id.tv_end);
		tv_order = BaseViewHolder.get(convertView, R.id.tv_order);
		tv_price = BaseViewHolder.get(convertView, R.id.tv_price);
		tv_state = BaseViewHolder.get(convertView, R.id.tv_state);
		tv_method = BaseViewHolder.get(convertView, R.id.tv_method);

		if(mList.size()>position){
			tv_username.setText(mList.get(position).getAddress() == null ? ""
					: mList.get(position).getAddress());
			
			tv_start.setText(mList.get(position).getHire_start() == null ? ""
					: mList.get(position).getHire_start());
			tv_end.setText(mList.get(position).getHire_end() == null ? "" : mList
					.get(position).getHire_end());
			tv_order.setText(mList.get(position).getObjectId() == null ? "" : mList
					.get(position).getObjectId());
			tv_price.setText(mList.get(position).getPrice() + "");
			state = mList.get(position).getTrade_state();
			// 1代表已完成,0代表未完成
			if (state == 1) {
				tv_state.setText("已完成");
				tv_state.setTextColor(Color.parseColor("#76d25a"));
			} else {
				tv_state.setText("未完成");
				tv_state.setTextColor(Color.parseColor("#FF692A"));
			}
			tv_method.setText(mList.get(position).getMethod());
		}
		return convertView;
	}

}
