package com.kongcv.adapter;

import java.util.ArrayList;
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
import com.kongcv.global.MineCarmanagerBean;
import com.kongcv.global.CarBean.HireEndEntity;
import com.kongcv.global.CarBean.HireStartEntity;
import com.kongcv.global.Information;
import com.kongcv.utils.BaseViewHolder;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.ToastUtil;

public class CarManagerAdapter extends BaseAdapter implements OnClickListener {

	private Context context;
	private int park_hide;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 2:
				String state = (String) msg.obj;
				if ("ok".equals(state)) {
					ToastUtil.show(context, "OK!");
					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (park_hide == 0) {
								mListBean.get(index).setPark_hide(1);
							} else {
								mListBean.get(index).setPark_hide(0);
							}
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
	private ArrayList<MineCarmanagerBean> mListBean;
	public CarManagerAdapter(Context context,
			ArrayList<MineCarmanagerBean> beans) {
		this.context = context;
		this.mListBean = beans;
	}

	@Override
	public int getCount() {
		return mListBean.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private TextView tv_address, tv_start, tv_end, tv_ispush;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.carmanager_item, null);
		}
		tv_address = BaseViewHolder.get(convertView, R.id.tv_address);
		tv_start = BaseViewHolder.get(convertView, R.id.tv_start);
		tv_end = BaseViewHolder.get(convertView, R.id.tv_end);
		tv_ispush = BaseViewHolder.get(convertView, R.id.tv_ispush);
		tv_ispush.setOnClickListener(this);
		tv_ispush.setTag(position);

		tv_address.setText(mListBean.get(position).getAddress());
		tv_start.setText(mListBean.get(position).getHire_start() == null ? ""
				: mListBean.get(position).getHire_start());
		tv_end.setText(mListBean.get(position).getHire_end() == null ? ""
				: mListBean.get(position).getHire_end());
		park_hide = mListBean.get(position).getPark_hide();
		if (park_hide == 0) {
			tv_ispush.setText(showOrHide[0]);
			tv_ispush.setTextColor(Color.parseColor("#76d25a"));
		} else {
			tv_ispush.setText(showOrHide[1]);
			tv_ispush.setTextColor(Color.parseColor("#FF692A"));
		}
		return convertView;
	}

	private String[] showOrHide = new String[] { "屏蔽", "发布" };

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
			String doHttpsPost = PostCLientUtils
					.doHttpsPost(Information.KONGCV_PUT_PARK_HIDE,
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

	private int index = 0;
	private String park_id;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_ispush:
			index = (int) v.getTag();
			park_id = mListBean.get(index).getObjectId();
			park_hide = mListBean.get(index).getPark_hide();
			ReadType readType = new ReadType();
			readType.execute();
			break;
		default:
			break;
		}
	}
}
