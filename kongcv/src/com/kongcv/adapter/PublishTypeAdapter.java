package com.kongcv.adapter;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kongcv.R;
import com.kongcv.global.TypeBean;
import com.kongcv.view.SwipeLayout;
import com.kongcv.view.SwipeLayout.OnSwipeListener;



public class PublishTypeAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<SwipeLayout> openedItems;
	private ArrayList<TypeBean> items;
	UpdateList list;
	public interface  UpdateList {
		public void deteleList(ArrayList<TypeBean> items);
	}
	public PublishTypeAdapter(Context context, ArrayList<TypeBean> item) {
		super();
		this.context=context;
		this.items=item;
	}
	public PublishTypeAdapter(Context context, ArrayList<TypeBean> item,UpdateList updateList) {
		super();
		this.context=context;
		this.items=item;
		openedItems = new ArrayList<SwipeLayout>();
//		this.list=updateList;
	}
	@Override
	public int getCount() {
		return items==null?0:items.size();
	}

	@Override
	public Object getItem(int position) {
		return items == null ? 0 : position;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView=View.inflate(context, R.layout.public_type_item, null);
			holder.tv_type=(TextView) convertView.findViewById(R.id.type_tv_type);
			holder.tv_date=(TextView) convertView.findViewById(R.id.type_tv_date);
			holder.tv_price=(TextView) convertView.findViewById(R.id.type_tv_price);
			
			holder.mCancel=(TextView) convertView.findViewById(R.id.cancel);
			holder.mDelete=(TextView) convertView.findViewById(R.id.Delete);
		    convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
        final SwipeLayout sl = (SwipeLayout)convertView;
		sl.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onClose(SwipeLayout layout) {
				openedItems.remove(layout);
			}

			@Override
			public void onOpen(SwipeLayout layout) {
				openedItems.add(layout);
			}

			@Override
			public void onStartOpen(SwipeLayout layout) {
				// 关闭所有已经打开的条目
				for (int i = 0; i < openedItems.size(); i++) {
					openedItems.get(i).close(true);
				}
				openedItems.clear();
			}

			@Override
			public void onStartClose(SwipeLayout layout) {
			}
			
		});
		
		holder.mCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openedItems.get(position).close(true);
			}
		});
		
		holder.mDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				items.remove(position);//只是在列表中删除了这个选项 但是发布集合中的数据仍然存在/
		//		list.deteleList(items);//删除
				openedItems.remove(sl);
				// 只需要重新设置一下adapter  
				notifyDataSetChanged();
			}
		});
		for(int i=0;i<items.size();i++){
			Log.e(i+"::items!!!", items.get(i).getDate()+":::::");
		}
		holder.tv_type.setText(items.get(position).getMethod());
		holder.tv_price.setText(items.get(position).getDate()=="0"?null:items.get(position).getDate());
		holder.tv_date.setText(items.get(position).getPrice()==null?"0":items.get(position).getPrice());
		return convertView;
	}
	public class  ViewHolder{
		public TextView tv_type;
		public TextView tv_date;
		public TextView tv_price;
		public TextView mCancel,mDelete;
	}


}

