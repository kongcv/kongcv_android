package com.kongcv.adapter;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.kongcv.R;
import com.kongcv.global.CardTypeBean;
import com.kongcv.utils.BaseViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CreditTypeAdapter extends BaseAdapter {
	private Context context;
	private int[] imgIds=new int[]{R.drawable.bg_yuan,R.drawable.icon_shezhi,R.drawable.icon_qianbao,R.drawable.icon_cheweiguanli,R.drawable.icon_zhangdan,R.drawable.bg_yuan,R.drawable.bg_yuan,R.drawable.bg_yuan,R.drawable.bg_yuan,R.drawable.bg_yuan};
	private List<String> list;
	private List<String> picList;
	private List<CardTypeBean> cList;
	private ImageLoader imageLoader;
	public CreditTypeAdapter(Context context ,List<CardTypeBean> cList,ImageLoader imageLoader){
		this.context = context;
		this.cList=cList;
		this.imageLoader = imageLoader;
	}

	@Override
	public int getCount() {
		return cList.size();
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
		 if(convertView==null){
				convertView=LayoutInflater.from(context).inflate(R.layout.mine_credit_type, 
						parent, false);
		 }
		 	TextView tv_check = BaseViewHolder.get(convertView, R.id.tv_check);
		 	NetworkImageView iv_credittype = BaseViewHolder.get(convertView, R.id.iv_credittype);
		 	iv_credittype.setImageUrl((String) cList.get(position).getUrl(), imageLoader);
		 	tv_check.setText(cList.get(position).getBank());
		    return convertView;
	}
	class ViewHolder {
		TextView tv_check;
		ImageView iv_credittype;
		RelativeLayout rl_type;
	}

}
