package com.kongcv.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongcv.R;
import com.kongcv.ImageRun.GetImage;
import com.kongcv.global.OrderCommityBean.ResultEntity;
import com.kongcv.global.OrderCommityBean.ResultEntity.HireEndEntity;
import com.kongcv.global.OrderCommityBean.ResultEntity.HireStartEntity;
import com.kongcv.global.OrderCommityBean.ResultEntity.UserList;

public class CzCommityAdapter extends BaseAdapter {
	private Context context;
	private List<HireStartEntity> mList;
	private List<HireEndEntity> list;
	private List<ResultEntity> resultBean;
	private List<UserList> userBeans;

	public CzCommityAdapter(Context context, List<HireStartEntity> mList,
			List<HireEndEntity> list, List<ResultEntity> resultBean,
			List<UserList> userBeans) {
		this.context = context;
		this.mList = mList;
		this.list = list;
		this.resultBean = resultBean;
		this.userBeans = userBeans;
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
			holder.iv_bohao = (ImageView) convertView
					.findViewById(R.id.iv_bohao);
			holder.iv_dingwei = (ImageView) convertView
					.findViewById(R.id.iv_dingwei);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.iv_bohao.setVisibility(View.VISIBLE);
		holder.tv_username.setText(userBeans.get(position).getUsername());
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
	     Bitmap bitmap=	userBeans.get(position).getBitMap();
	     /*Drawable d=GetImage.resizeImage(bitmap,160, 160);
	     holder.iv_dingwei.setImageDrawable(d);*/
		final String phoneNumber = userBeans.get(position).getMobilePhoneNumber();
		holder.iv_bohao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ phoneNumber));
				context.startActivity(intent);
			}
		});
		Log.v("method", resultBean.get(position).getMethod());
		return convertView;
	}

	class ViewHolder {
		TextView tv_username, tv_start, tv_end, tv_order, tv_price, tv_state,
				tv_method;
		ImageView iv_bohao, iv_dingwei;

	}

}
