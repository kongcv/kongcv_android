package com.kongcv.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.kongcv.MyApplication;
import com.kongcv.R;
import com.kongcv.fragment.CommentFragment;
import com.kongcv.upload.PictureUtil;
import com.kongcv.view.CircleImageView;

public class CommentAdapter extends BaseAdapter {

	private Context mContext;
	private List<Map<String, Object>> dataList;
	private ImageLoader imageLoader;
	public CommentAdapter(Context mContext, List<Map<String, Object>> dataList,ImageLoader imageLoader) {
		super();
		this.mContext = mContext;
		this.dataList = dataList;
		this.imageLoader = imageLoader;
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
			/*convertView = LayoutInflater.from(mContext).inflate(
					R.layout.comment_list_item, parent, false);*/
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.comment_item, parent, false);

			holder.tvUserName = (TextView) convertView.findViewById(R.id.txtMs);
			holder.mRatingBar = (RatingBar) convertView
					.findViewById(R.id.myratingbar);
			holder.userimge = (CircleImageView) convertView
					.findViewById(R.id.userimge);
			holder.tv_username = (TextView) convertView
					.findViewById(R.id.tv_username);
			holder.tv_says = (TextView) convertView.findViewById(R.id.tv_says);
			convertView.setTag(holder);// 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 给控件设置数据
		holder.tvUserName.setText((CharSequence) dataList.get(position).get(
				CommentFragment.KEY[0]));
		
		holder.mRatingBar.setNumStars(5);
		holder.mRatingBar.setRating((Float) dataList.get(position).
				get(CommentFragment.KEY[1]));
		
		if(dataList.get(position).get(
				CommentFragment.KEY[2])!=null){
			ImageListener listener = ImageLoader.getImageListener(holder.userimge,
					0, 0);
			imageLoader.get((String) dataList.get(position).get(
					CommentFragment.KEY[2]), listener);
		}else{
			holder.userimge.setImageResource(R.drawable.erroro);
		}
		
		
		holder.tv_username.setText((CharSequence) dataList.get(position).get(
				CommentFragment.KEY[4]));
		holder.tv_says.setText((CharSequence) dataList.get(position).get(
				CommentFragment.KEY[3]));
		return convertView;
	}

	class ViewHolder {
		public CircleImageView userimge;
		public TextView tvUserName, tv_username, tv_says;
		public RatingBar mRatingBar;
	}
}