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
import com.kongcv.global.ZyCommityAdapterBean;
import com.kongcv.global.OrderCommityBean.ResultEntity;
import com.kongcv.global.OrderCommityBean.ResultEntity.HireEndEntity;
import com.kongcv.global.OrderCommityBean.ResultEntity.HireStartEntity;
import com.kongcv.global.OrderCommityBean.ResultEntity.UserList;
import com.kongcv.utils.BaseViewHolder;

public class CzCommityAdapter extends BaseAdapter implements OnClickListener{
	/*private Context context;
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
	}*/
	private Context mContext;
	private List<ZyCommityAdapterBean> mList;
	public CzCommityAdapter(Context context,List<ZyCommityAdapterBean> list){
		mContext=context;
		mList=list;
	}
	@Override
	public int getCount() {
		//return resultBean.size();
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

	/*ViewHolder holder;
	class ViewHolder {
		TextView tv_username, tv_start, tv_end, tv_order, tv_price, tv_state,
				tv_method;
		ImageView iv_bohao, iv_dingwei;
	}*/
	
	private int state=1;
	private String phoneNumber;
	private ImageView iv_bohao, iv_dingwei;
	private TextView tv_username, tv_start, tv_end, tv_order, tv_price, tv_state,tv_method;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.order_item, null);
		}
		tv_username=BaseViewHolder.get(convertView, R.id.tv_username);
		tv_start=BaseViewHolder.get(convertView, R.id.tv_start);
		tv_end=BaseViewHolder.get(convertView, R.id.tv_end);
		tv_order=BaseViewHolder.get(convertView, R.id.tv_order);
		tv_price=BaseViewHolder.get(convertView, R.id.tv_price);
		tv_state=BaseViewHolder.get(convertView, R.id.tv_state);
		tv_method=BaseViewHolder.get(convertView, R.id.tv_method);
		iv_bohao=BaseViewHolder.get(convertView, R.id.iv_bohao);
		iv_dingwei=BaseViewHolder.get(convertView, R.id.iv_dingwei);
		iv_bohao.setOnClickListener(this);
		iv_bohao.setTag(position);
		
		iv_bohao.setVisibility(View.VISIBLE);
		tv_username.setText(mList.get(position).getUsername()==null?"":mList.get(position).getUsername());
		tv_start.setText(mList.get(position).getHire_start()==null?"":mList.get(position).getHire_start());
		tv_end.setText(mList.get(position).getHire_end()==null?"":mList.get(position).getHire_end());
		tv_order.setText(mList.get(position).getObjectId()==null?"":mList.get(position).getObjectId());
		tv_price.setText(mList.get(position).getPrice()+"");
		state = mList.get(position).getTrade_state();
		// 1代表已完成,0代表未完成
		if (state == 1) {
			tv_state.setText("已完成");
			tv_state.setTextColor(Color.parseColor("#76d25a"));
		} else {
			tv_state.setText("未完成");
			tv_state.setTextColor(Color.parseColor("#FF692A"));
		}
		phoneNumber=mList.get(position).getMobilePhoneNumber();
		tv_method.setText(mList.get(position).getMethod()==null?"":mList.get(position).getMethod());
		if(mList.get(position).getBitmap()!=null){
			Drawable d=GetImage.resizeImage(mList.get(position).getBitmap(),160, 160);
		    iv_dingwei.setImageDrawable(d);
		}
		/*if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.order_item, null);
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
		}*/
		/*holder.iv_bohao.setVisibility(View.VISIBLE);
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
		}*/
		/* holder.tv_method.setText(resultBean.get(position).getMethod());
	     Bitmap bitmap=	userBeans.get(position).getBitMap();*/
	     /*Drawable d=GetImage.resizeImage(bitmap,160, 160);
	     holder.iv_dingwei.setImageDrawable(d);*/
/*		final String phoneNumber = userBeans.get(position).getMobilePhoneNumber();
		holder.iv_bohao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ phoneNumber));
				context.startActivity(intent);
			}
		});
		Log.v("method", resultBean.get(position).getMethod());*/
		return convertView;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_bohao:
			phoneNumber=mList.get((int) v.getTag()).getMobilePhoneNumber();
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ phoneNumber));
			mContext.startActivity(intent);
			break;
		default:
			break;
		}
	}
}
