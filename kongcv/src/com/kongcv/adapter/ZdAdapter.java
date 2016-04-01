package com.kongcv.adapter;

import java.util.List;

import com.kongcv.R;
import com.kongcv.adapter.ZyCommityAdapter.ViewHolder;
import com.kongcv.global.CheckBean;
import com.kongcv.global.OrderCommityBean.ResultEntity;
import com.kongcv.global.OrderCommityBean.ResultEntity.HireEndEntity;
import com.kongcv.global.OrderCommityBean.ResultEntity.HireStartEntity;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ZdAdapter extends BaseAdapter {
	private Context context;
	private List<CheckBean> checkList;

	public ZdAdapter(Context context, List<CheckBean> checkList) {
		this.context = context;
		this.checkList = checkList;
	}

	@Override
	public int getCount() {
		return checkList.size();

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
			convertView = View.inflate(context, R.layout.check_item, null);
			holder = new ViewHolder();
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_year = (TextView) convertView.findViewById(R.id.tv_year);
			holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_time.setText(checkList.get(position).getTime());
		holder.tv_year.setText(checkList.get(position).getYear());
		holder.tv_money.setText(checkList.get(position).getMoney());
		holder.tv_address.setText(checkList.get(position).getAddress());
		return convertView;
	}

	class ViewHolder {
		TextView tv_time, tv_year, tv_money, tv_address;

	}

}
