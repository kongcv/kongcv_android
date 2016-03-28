package com.kongcv.adapter;

import java.util.List;

import com.kongcv.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CheckAdapter extends BaseAdapter {
	private Context context;
	private List<String> mList;
	private List<String> timeList;
	public CheckAdapter(Context context,List<String> mList,List<String> timeList){
		this.context=context;
		this.mList=mList;
		this.timeList=timeList;
		
	}

	@Override
	public int getCount() {
		return mList.size();
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
	            convertView = View.inflate(context, R.layout.checkhistory_item, null);
	            holder = new ViewHolder();
	            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
	            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
	            convertView.setTag(holder);
	        }else{
	            holder = (ViewHolder) convertView.getTag();
	        }
	        holder.tv_money.setText("¥"+mList.get(position));
	        holder.tv_time.setText(timeList.get(position));
		    return convertView;
	}
	class ViewHolder{
		TextView tv_money,tv_time;
		
	}
}