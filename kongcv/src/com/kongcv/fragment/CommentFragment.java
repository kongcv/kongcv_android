package com.kongcv.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kongcv.R;
import com.kongcv.UI.AsyncImageLoader.PreReadTask;
import com.kongcv.activity.WriteCommentActivity;
import com.kongcv.adapter.CommentAdapter;
import com.kongcv.global.Information;
import com.kongcv.global.JsonRootBean;
import com.kongcv.global.Result;
import com.kongcv.global.User;
import com.kongcv.global.User.ImageUser;
import com.kongcv.utils.BitmapCache;
import com.kongcv.utils.GTMDateUtil;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.ToastUtil;
import com.kongcv.view.AMapListView;
import com.kongcv.view.AMapListView.AMapListViewListener;

/**
 * 评论fragment
 */
public class CommentFragment extends Fragment implements AMapListViewListener,
		OnRatingBarChangeListener, OnScrollListener, OnClickListener {
	private Button says;
	private View view;
	private AMapListView listView;// 评论数据
	private Handler mHandler = new Handler();;

	private List<Map<String, Object>> listData;
	private ImageView mUserImage;
	private RatingBar mRatingBar;
	private TextView mUser;
	private TextView txtFind;
	public static String[] KEY = new String[] { "userName", "myratingbar",
			"userImage", "tvTime", "tvSay" };
	private ImageLoader imageLoader;
	private RequestQueue mQueue;
	private CommentAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
			return view;
		}
		view = inflater.inflate(R.layout.comment_activity, null);
		mQueue = Volley.newRequestQueue(getActivity());
		imageLoader = new ImageLoader(mQueue, new BitmapCache());
		init();
		initDate();
		return view;
	}

	private void init() {
		txtFind = (TextView) view.findViewById(R.id.txtMs);
		mRatingBar = (RatingBar) view.findViewById(R.id.myratingbar);
		mUserImage = (ImageView) view.findViewById(R.id.userimge);
		mUser = (TextView) view.findViewById(R.id.tv_username);
		listView = (AMapListView) view.findViewById(R.id.iv_comment);
		listView.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
		listView.setOnScrollListener(this);

		listData = new ArrayList<Map<String, Object>>();
		listView.setAMapListViewListener(this);

		says = (Button) view.findViewById(R.id.says);
		says.setOnClickListener(this);
	}

	private String mode;
	private String park_id;
	private int i = 0;

	private void initDate() {
		Bundle arguments = getArguments();
		mode = arguments.getString("mode");
		park_id = arguments.getString("park_id");
		ReadInfo task2 = new ReadInfo();
		task2.execute(i);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ReadInfo task2 = new ReadInfo();
		task2.execute(i);
	}
	
	/**
	 * 获取评论数据
	 */
	class ReadInfo extends PreReadTask<Integer, Void, Void> {
		@Override
		protected Void doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			Integer skip = params[0];
			skip = skip * 10;
			readInfo(skip);
			return null;
		}
	}

	private void readInfo(Integer integer) {
		// TODO Auto-generated method stub
		try {
			JSONObject object = new JSONObject();
			object.put("park_id", park_id);
			object.put("skip", integer);
			object.put("limit", 10);
			object.put("mode", mode);// 需要传递过来
			String doHttpsPost = PostCLientUtils.doHttpsPost(
					Information.KONGCV_GET_COMMENT,
					JsonStrUtils.JsonStr(object));
			Gson gson = new Gson();
			JsonRootBean rootBean = gson.fromJson(doHttpsPost,
					JsonRootBean.class);
			List<Result> result = rootBean.getResult();

			Message msg = handler.obtainMessage();
			msg.what = 0;
			msg.obj = result;
			handler.sendMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				@SuppressWarnings("unchecked")
				List<Result> result = (List<Result>) msg.obj;
				if (result != null && result.size() > 0) {
					if (listData != null)
						listData.clear();
					getInfo(result);
				} else {
					mAdapter = new CommentAdapter(getActivity(), listData,
							imageLoader);
					listView.setAdapter(mAdapter);
					if (i != 0) {
						ToastUtil.show(getActivity(), "没有数据啦！");
					}
				}
				break;
			default:
				break;
			}
		}
	};

	private void getInfo(List<Result> result) {
		Gson gson = new Gson();
		HashMap<String, Object> map;
		String urlImage = null;
		for (int i = 0; i < result.size(); i++) {
			map = new HashMap<String, Object>();
			if (result.get(i).getUser() != null) {
				String user = result.get(i).getUser();
				User userBean = gson.fromJson(user, User.class);
				String username = userBean.getUsername();
				map.put("userName", username);
				ImageUser image = userBean.getImage();
				if (user.contains("url")) {
					urlImage = image.getUrl();
				}
			}
			String gtmToLocal = GTMDateUtil.GTMToLocal(result.get(i)
					.getCreatedAt(), true);
			map.put("tvTime", gtmToLocal);// 时间
			map.put("tvSay", result.get(i).getComment());// 评论

			map.put("myratingbar", (float) result.get(i).getGrade());// 星评论
			map.put("userImage", urlImage);// 图片
			listData.add(map);
		}
		mAdapter = new CommentAdapter(getActivity(), listData, imageLoader);
		listView.setAdapter(mAdapter);
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.says:// 发表评论
			Intent intent = new Intent(getActivity(),
					WriteCommentActivity.class);
			intent.putExtra("MODE", mode);
			intent.putExtra("park_id", park_id);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 刷新加载数据
	 */
	/** 停止刷新， */
	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("刚刚");
	}

	// 刷新
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				i = 0;
				ReadInfo task2 = new ReadInfo();
				task2.execute(i);
				onLoad();
			}
		}, 2000);
	}

	// 加载更多
	public void onLoadMore() {

		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				i++;
				ReadInfo task2 = new ReadInfo();
				task2.execute(i);
				onLoad();
			}
		}, 2000);
	}
}
