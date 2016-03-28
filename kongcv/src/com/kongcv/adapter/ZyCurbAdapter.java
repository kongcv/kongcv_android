package com.kongcv.adapter;

import java.util.List;

import com.kongcv.R;
import com.kongcv.global.OrderCommityBean.ResultEntity;
import com.kongcv.global.OrderCommityBean.ResultEntity.HireEndEntity;
import com.kongcv.global.OrderCommityBean.ResultEntity.HireStartEntity;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ZyCurbAdapter extends BaseAdapter {
	private Context context;
	private List<HireStartEntity> mList;
	private List<HireEndEntity> list;
	private List<ResultEntity> resultBean;

	public ZyCurbAdapter(Context context, List<HireStartEntity> mList,
			List<HireEndEntity> list, List<ResultEntity> resultBean) {
		this.context = context;
		this.mList = mList;
		this.list = list;
		this.resultBean = resultBean;
	}

	@Override
	public int getCount() {
		return resultBean.size();

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
			convertView = View.inflate(context, R.layout.order_item, null);
			holder = new ViewHolder();
			holder.tv_username = (TextView) convertView
					.findViewById(R.id.tv_username);
			holder.tv_start = (TextView) convertView
					.findViewById(R.id.tv_start);
			holder.tv_end = (TextView) convertView.findViewById(R.id.tv_end);
			holder.tv_order = (TextView) convertView
					.findViewById(R.id.tv_order);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			holder.tv_state = (TextView) convertView
					.findViewById(R.id.tv_state);
			holder.tv_method = (TextView) convertView
					.findViewById(R.id.tv_method);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_username.setText(resultBean.get(position).getPark_curb());
		holder.tv_start.setText(mList.get(position).getIso());
		holder.tv_end.setText(list.get(position).getIso());
		holder.tv_order.setText(resultBean.get(position).getObjectId());
		holder.tv_price.setText(resultBean.get(position).getPrice() + "");

		int state = resultBean.get(position).getTrade_state();
		// 1代表已完成,0代表未完成
		if (state == 1) {
			holder.tv_state.setText("已完成");
			holder.tv_state.setTextColor(Color.parseColor("#76d25a"));
		} else {
			holder.tv_state.setText("未完成");
			holder.tv_state.setTextColor(Color.parseColor("#FF692A"));
		}
		holder.tv_method.setText(resultBean.get(position).getMethod());
		Log.v("Method", resultBean.get(position).getMethod() + "");
		return convertView;
	}

	class ViewHolder {
		TextView tv_username, tv_start, tv_end, tv_order, tv_price, tv_state,
				tv_method;
	}

}
