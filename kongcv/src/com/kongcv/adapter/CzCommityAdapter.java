package com.kongcv.adapter;

import java.util.List;

import android.R.dimen;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongcv.R;
import com.kongcv.ImageRun.GetImage;
import com.kongcv.activity.HomeActivity;
import com.kongcv.global.ZyCommityAdapterBean;
import com.kongcv.utils.AndroidUtil;
import com.kongcv.utils.BaseViewHolder;
import com.kongcv.utils.Data;

public class CzCommityAdapter extends BaseAdapter implements OnClickListener {

	private int state = 1;
	private Context mContext;
	private String phoneNumber;
	private ImageView iv_bohao, iv_dingwei;
	private List<ZyCommityAdapterBean> mList;
	private TextView tv_username, tv_start, tv_end, tv_order, tv_price,
			tv_state, tv_method;

	public CzCommityAdapter(Context context, List<ZyCommityAdapterBean> list) {
		mContext = context;
		mList = list;
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
	private Drawable d;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.comm_order_item, null);
		}
		tv_username = BaseViewHolder.get(convertView, R.id.tv_username);
		tv_start = BaseViewHolder.get(convertView, R.id.tv_start);
		tv_end = BaseViewHolder.get(convertView, R.id.tv_end);
		tv_order = BaseViewHolder.get(convertView, R.id.tv_order);
		tv_price = BaseViewHolder.get(convertView, R.id.tv_price);
		tv_state = BaseViewHolder.get(convertView, R.id.tv_state);
		tv_method = BaseViewHolder.get(convertView, R.id.tv_method);
		iv_bohao = BaseViewHolder.get(convertView, R.id.iv_bohao);
		iv_dingwei = BaseViewHolder.get(convertView, R.id.iv_dingwei);
	//	iv_bohao.setVisibility(View.VISIBLE);
		iv_bohao.setOnClickListener(this);
		iv_bohao.setTag(position);

		tv_username.setText(mList.get(position).getUsername() == null ? ""
				: mList.get(position).getUsername());
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
		phoneNumber = mList.get(position).getMobilePhoneNumber();
		tv_method.setText(mList.get(position).getMethod() == null ? "" : mList
				.get(position).getMethod());
		
		if (mList.get(position).getBitmap() != null) {
			if(AndroidUtil.pictureOrT(mContext)){
				d = GetImage.resizeImage(mList.get(position).getBitmap(),
						160, 160);
			}else{
				d = GetImage.resizeImage(mList.get(position).getBitmap(),
						80, 80);
			}
			iv_dingwei.setTag(d);
			if (iv_dingwei.getTag() != null && iv_dingwei.getTag().equals(d)) {
				iv_dingwei.setImageDrawable(d);
			}
		}else{
			Resources res=mContext.getResources();
			Bitmap bmp=BitmapFactory.decodeResource(res, R.drawable.logo);
			if(AndroidUtil.pictureOrT(mContext)){
				d = GetImage.resizeImage(bmp,
						160, 160);
			}else{
				d = GetImage.resizeImage(bmp,
						80, 80);
			}
			iv_dingwei.setImageDrawable(d);
		}
		return convertView;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_bohao:
			phoneNumber = mList.get((int) v.getTag()).getMobilePhoneNumber();
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ phoneNumber));
			mContext.startActivity(intent);
			break;
		default:
			break;
		}
	}
}
