package com.kongcv.adapter;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kongcv.R;
import com.kongcv.UI.AsyncImageLoader.PreReadTask;
import com.kongcv.global.CarBean;
import com.kongcv.global.CarBean.HireEndEntity;
import com.kongcv.global.CarBean.HireStartEntity;
import com.kongcv.global.Information;
import com.kongcv.utils.BaseViewHolder;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.ToastUtil;

public class CarManagerAdapter extends BaseAdapter implements OnClickListener{
	private Context context;
	private List<CarBean> mList;
	private List<HireStartEntity> startList;
	private List<HireEndEntity> endList;
	private int park_hide;
//	private ViewHolder holder;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 2:
				String state = (String) msg.obj;
				if ("ok".equals(state)) {
					/*if (park_hide == 0) {
						park_hide = 1;
						tv_ispush.setText("发布");
						tv_ispush.setTextColor(Color.parseColor("#FF692A"));
						Log.i("park_hide", park_hide + "");
					} else {
						park_hide = 0;
						Log.i("sss", "ssssss");
						tv_ispush.setText("屏蔽");
						tv_ispush.setTextColor(Color.parseColor("#76d25a"));
						Log.i("park_hide", park_hide + "");
					}*/
					 ToastUtil.show(context, "OK!");
					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(park_hide == 0){
								mList.get(index).setPark_hide(1);
							}else {
								mList.get(index).setPark_hide(0);
							}
							Log.d("handler park_hide", park_hide+">><<");
							notifyDataSetChanged();
						}
					});
				  } else {
					 ToastUtil.show(context, "error!");
				  }
				break;

			default:
				break;
			}
		};
	};
	public CarManagerAdapter(Context context, List<CarBean> mList,
			List<HireStartEntity> startList, List<HireEndEntity> endList) {
		this.context = context;
		this.mList = mList;
		this.startList = startList;
		this.endList = endList;
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

	private TextView tv_address,tv_start,tv_end,tv_ispush;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/*if (convertView == null) {
			convertView = View.inflate(context, R.layout.carmanager_item, null);
			holder = new ViewHolder(null, 0);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_address);
			holder.tv_start = (TextView) convertView
					.findViewById(R.id.tv_start);
			holder.tv_end = (TextView) convertView.findViewById(R.id.tv_end);
			holder.tv_ispush = (TextView) convertView
					.findViewById(R.id.tv_ispush);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(position<mList.size() && position<startList.size() && position<endList.size()){
			holder.tv_address.setText(mList.get(position).getAddress());
			holder.tv_start.setText(startList.get(position).getIso());
			holder.tv_end.setText(endList.get(position).getIso()==null?"":endList.get(position).getIso());
			if (park_hide == 0) {
				holder.tv_ispush.setText("屏蔽");
				holder.tv_ispush.setTextColor(Color.parseColor("#76d25a"));
			} else {
				holder.tv_ispush.setText("发布");
				holder.tv_ispush.setTextColor(Color.parseColor("#FF692A"));
			}
			holder.tv_ispush.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub  
					ReadType readType=new ReadType();
					readType.execute(new ViewHolder(mList.get(position).getObjectId(), 
							mList.get(position).getPark_hide()));
				}
			});
		}*/
		if(convertView == null){
			convertView = View.inflate(context, R.layout.carmanager_item, null);
		}
		tv_address = BaseViewHolder.get(convertView, R.id.tv_address);
		tv_start = BaseViewHolder.get(convertView, R.id.tv_start);
		tv_end = BaseViewHolder.get(convertView, R.id.tv_end);
		tv_ispush = BaseViewHolder.get(convertView, R.id.tv_ispush);
		tv_ispush.setOnClickListener(this);
		tv_ispush.setTag(position);
		if(position<mList.size() && position<startList.size() && position<endList.size()){
			tv_address.setText(mList.get(position).getAddress());
			tv_start.setText(startList.get(position).getIso());
			tv_end.setText(endList.get(position).getIso()==null?"":endList.get(position).getIso());
			park_hide=mList.get(position).getPark_hide();
			if (park_hide == 0) {
				tv_ispush.setText(showOrHide[0]);
				tv_ispush.setTextColor(Color.parseColor("#76d25a"));
			} else {
				tv_ispush.setText(showOrHide[1]);
				tv_ispush.setTextColor(Color.parseColor("#FF692A"));
			}
			/*holder.tv_ispush.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub  
					ReadType readType=new ReadType();
					readType.execute(new ViewHolder(mList.get(position).getObjectId(), 
							mList.get(position).getPark_hide()));
				}
			});*/
		}
		return convertView;
	}
	/*public class ViewHolder {
		public TextView tv_address, tv_start, tv_end, tv_ispush;
		private String park_id;
		private int num;
		public ViewHolder(String park_id,int num){
			this.park_id=park_id;
			this.num=num;
		}
	}*/
	/*class ReadType extends PreReadTask<ViewHolder, Void, Void> {
		@Override
		protected Void doInBackground(ViewHolder... params) {
			// TODO Auto-generated method stub
			doInfo(params[0].park_id,params[0].num);
			return null;
		}
	}*/
	private String[] showOrHide=new String[]{"屏蔽","发布"};
	class ReadType extends PreReadTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			doInfo();
			return null;
		}
	}
	private void doInfo() {
		// TODO Auto-generated method stub
		JSONObject obj = new JSONObject();
		try {
			obj.put("park_id", park_id);
			if (park_hide == 0) {
				obj.put("hide", 1);
			} else {
				obj.put("hide", 0);
			}
			obj.put("mode", "community");
			String doHttpsPost = PostCLientUtils.doHttpsPost(
					Information.KONGCV_PUT_PARK_HIDE,
					JsonStrUtils.JsonStr(obj));
			Log.i("doHttpsPost", doHttpsPost);
			JSONObject obj2 = new JSONObject(doHttpsPost);
			String str = obj2.getString("result");
			JSONObject objStr = new JSONObject(str);
			String state = objStr.getString("state");
			Message msg = Message.obtain();
			msg.what = 2;
			msg.obj = state;
			handler.sendMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int index=0;
	private String park_id;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_ispush:
			index=(int) v.getTag();
			park_id=mList.get(index).getObjectId();
			park_hide=mList.get(index).getPark_hide();
			
			Log.d("onClick index",index+"::");
			Log.d("onClick park_id",park_id+"::");
			Log.d("onClick park_hide",park_hide+"::");
			ReadType readType=new ReadType();
			readType.execute();
			break;
		default:
			break;
		}
	}
}
