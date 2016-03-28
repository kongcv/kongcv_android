package com.kongcv.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongcv.R;
import com.kongcv.activity.SearchActivity;

public class SearchAdapter extends BaseAdapter {
	
	private Context mContext;
	
	
	private List<Map<String, Object>> dataList;
	
	public SearchAdapter(Context context) {
		super();
		this.mContext = context;
	}
	
	
	
	// 写一个方法用于传递参数
	public void setDataSource(List<Map<String, Object>> dataList) {
		if (null != dataList) {
			this.dataList = dataList;
			this.notifyDataSetChanged();
		}
	}
	
	// 重写myadapter
	public int getCount() {// 返回数组长度
	
		return dataList == null ? 0 : dataList.size();
	}
	
	public Object getItem(int position) {
	
		return (null != dataList && !dataList.isEmpty()) ? null : dataList
				.get(position);
	}
	
	public long getItemId(int position) {
	
		return dataList == null ? 0 : position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
	
		ViewHolder holder;
	
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.listview_item, parent, false);
	
			holder.ivDaoH = (ImageView) convertView
					.findViewById(R.id.iv_daoh);
			holder.txtFind = (TextView) convertView
					.findViewById(R.id.txtfind);
			holder.txtCity = (TextView) convertView
					.findViewById(R.id.search_tv_city);
			holder.ivCome = (ImageView) convertView
					.findViewById(R.id.iv_come);
			convertView.setTag(holder);// 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 给控件设置数据
		holder.ivDaoH.setImageResource((Integer) dataList.get(position)
				.get(SearchActivity.KEY[0]));
		holder.txtFind.setText((CharSequence) dataList.get(position).get(
				SearchActivity.KEY[1]));
		holder.ivCome.setImageResource((Integer) dataList.get(position)
				.get(SearchActivity.KEY[2]));
		holder.txtCity.setText((CharSequence) dataList.get(position).get(
				SearchActivity.KEY[3]));
		return convertView;
	}
	class ViewHolder {
		public ImageView ivCome, ivDaoH;// 两个图片
		public TextView txtFind,txtCity;
	}
}
	
	
	
	
	
	
	