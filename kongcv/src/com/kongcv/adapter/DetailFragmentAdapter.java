package com.kongcv.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kongcv.R;
import com.kongcv.fragment.CommunityDetailFragment;


public class DetailFragmentAdapter extends BaseAdapter {

		private Context mContext;
		private int index = -1;
		private List<Map<String, Object>> dataList;
		
		HashMap<String,Boolean> states=new HashMap<String,Boolean>();//用于记录每个被点击的状态
		
		public DetailFragmentAdapter(Context context) {
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
		public void setP(int p) {
			this.index=p;
		}
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
		
		
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.detail_listview, parent, false);

				holder.tvMethod = (TextView) convertView
						.findViewById(R.id.txt_method);
				holder.tvDate = (TextView) convertView.findViewById(R.id.txt_date);
				holder.tvPrice = (TextView) convertView
						.findViewById(R.id.txt_price);
				holder.mRadioButton = (RadioButton) convertView
						.findViewById(R.id.radioButton);

				convertView.setTag(holder);// 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 给控件设置数据
			holder.tvMethod.setText((CharSequence) dataList.get(position).get(
					CommunityDetailFragment.KEY[0]));
			holder.tvDate.setText((CharSequence) dataList.get(position).get(
					CommunityDetailFragment.KEY[1]));
			holder.tvPrice.setText((CharSequence) dataList.get(position).get(
					CommunityDetailFragment.KEY[2]));
			
			
			if(dataList.size()>1){
				if (index == position) {// 选中的条目和当前的条目是否相等
			    	   holder.mRadioButton.setChecked(true);
			       } else {
			    	   holder.mRadioButton.setChecked(false);
			      }  
			}else{
				holder.mRadioButton.setChecked(true);
			}
			return convertView;
		}
		public class ViewHolder {
			public TextView tvMethod, tvDate, tvPrice;
			public RadioButton mRadioButton;
		}

	}
















