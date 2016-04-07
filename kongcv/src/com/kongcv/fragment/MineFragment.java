package com.kongcv.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kongcv.R;
import com.kongcv.ImageRun.GetImage;
import com.kongcv.UI.AsyncImageLoader.PreReadTask;
import com.kongcv.activity.LogInActivity;
import com.kongcv.activity.MineCarmanagerActivity;
import com.kongcv.activity.MineInformationActivity;
import com.kongcv.activity.MineOrdermanagerActivity;
import com.kongcv.activity.MineSheZhiActivity;
import com.kongcv.activity.MineWalletActivity;
import com.kongcv.activity.NickNameActivity;
import com.kongcv.global.Information;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.FileUtil;
import com.kongcv.utils.JsonStrUtils;
import com.kongcv.utils.PostCLientUtils;
import com.kongcv.utils.ToastUtil;
import com.kongcv.view.CircleImageView;
import com.kongcv.view.MinePopu;

/*
 * 我的页面
 */
public class MineFragment extends Fragment implements OnClickListener {

	private RelativeLayout rl_wallet;// 钱包
	private RelativeLayout rl_carmanager;// 车位管理
	private RelativeLayout rl_ordermanager;// 订单管理
	private RelativeLayout rl_infonotify;// 消息通知
	private RelativeLayout rl_shezhi;// 设置

	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	private Bitmap bitmap, bit;
	private File tempFile;
	private View linearLayout;
	private TextView uName;
	private CircleImageView mFace;
	private ACacheUtils mCache;
	private String user_name;
	// 自定义的弹出框类
	MinePopu menuWindow;
	private String url;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCache = ACacheUtils.get(getActivity());
		updateUserInfo();
	}

	private void updateUserInfo() {
		ReadType readType = new ReadType();
		readType.execute();
	}

	class ReadType extends PreReadTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			updateUser();
			return null;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (linearLayout != null) {
			ViewGroup parent = (ViewGroup) linearLayout.getParent();
			if (parent != null) {
				parent.removeView(linearLayout);
			}
			return linearLayout;
		}
		linearLayout = inflater.inflate(R.layout.mine_activity, container,
				false);
		mCache = ACacheUtils.get(getActivity());
		initView();
		return linearLayout;
	}

	private void initView() {
		rl_wallet = (RelativeLayout) linearLayout.findViewById(R.id.rl_wallet);
		rl_carmanager = (RelativeLayout) linearLayout
				.findViewById(R.id.rl_carmanager);
		rl_ordermanager = (RelativeLayout) linearLayout
				.findViewById(R.id.rl_ordermanager);
		rl_infonotify = (RelativeLayout) linearLayout
				.findViewById(R.id.rl_infonotify);
		rl_infonotify.setOnClickListener(this);
		rl_wallet.setOnClickListener(this);
		rl_carmanager.setOnClickListener(this);
		rl_ordermanager.setOnClickListener(this);
		rl_shezhi = (RelativeLayout) linearLayout.findViewById(R.id.rl_shezhi);
		rl_shezhi.setOnClickListener(this);
		uName = (TextView) linearLayout.findViewById(R.id.tv_logn);
		uName.setOnClickListener(this);
		mFace = (CircleImageView) linearLayout.findViewById(R.id.iv_mine);
		mFace.setOnClickListener(this);
	}

	/**
	 * 更新用户信息
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				uName.setText(user_name);
				try {
					if (bit != null) {
						mFace.setImageBitmap(bit);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			default:
				break;
			}
		}
	};

	private void updateUser() {
		try {
			JSONObject object = new JSONObject();
			object.put("mobilePhoneNumber", mCache.getAsString("USER"));
			object.put("user_id", mCache.getAsString("user_id"));
			String jsoStr = PostCLientUtils.doHttpsPost2(
					Information.KONGCV_GET_USERINFO,
					JsonStrUtils.JsonStr(object),
					mCache.getAsString("sessionToken"));
			JSONObject obj = new JSONObject(jsoStr);
			Log.e("hahawwww","haha");
			if (obj.has("result")) {
				Log.e("haha","haha");
				JSONObject result = obj.getJSONObject("result");
				if (result != null) {
					user_name = obj.getJSONObject("result").getString(
							"username");
					Log.e("hahassseee","haha");
					if (obj.getJSONObject("result").has("image")) {
						Log.e("hahasswww","haha");
						url = obj.getJSONObject("result")
								.getJSONObject("image").getString("url");
						bit = GetImage.getHttpBitmap(url);
						mHandler.sendEmptyMessage(1);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		String PHOTO_FILE_NAME = mCache.getAsString("USER") + ".png";
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
				crop(uri);
			}
		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (FileUtil.hasSdcard()) {
				tempFile = new File(Environment.getExternalStorageDirectory(),
						PHOTO_FILE_NAME);
				crop(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(getActivity(), "未找到存储卡，无法存储照片！", 0).show();
			}
		} else if (requestCode == PHOTO_REQUEST_CUT) {
			try {
				bitmap = data.getParcelableExtra("data");
				this.mFace.setImageBitmap(bitmap);
				upload(mFace);// 上传
				boolean delete = tempFile.delete();
				System.out.println("delete = " + delete);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		switch (resultCode) {
		
		case 6:
			Log.e("sssss", "1233");
			Log.e("sssss", "1233");
			if (data != null) {
				user_name = data.getStringExtra("nick");
				Bitmap bit = data.getParcelableExtra("bit");
				String url=data.getStringExtra("urlsssss");
				Log.e("urlsswwww", url);
				if (user_name != null) {
					Log.e("user_nameuser_name", user_name);
					uName.setText(user_name);
				} 
				
				if (bit != null) {
					Log.e("sssss", "1233");
					mFace.setImageBitmap(bit);
				} else {
					mFace.setImageResource(R.drawable.defaulto);
				}
			}
			break;
		case 0:
			if (data != null) {
				user_name = data.getStringExtra("user_name");
				uName.setText(user_name);
			}
			break;

		default:
			break;
		}
	//	super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_infonotify:
			/*
			 * if(mCache.getAsString("USER")!=null &&
			 * mCache.getAsString("sessionToken")!=null){ Intent intentInfo =
			 * new Intent(getActivity(), MineInformationActivity.class);
			 * startActivity(intentInfo); }else { ToastUtil.show(getActivity(),
			 * "请先登录！"); mHandler.postAtTime(new Runnable() {
			 * 
			 * @Override public void run() { // TODO Auto-generated method stub
			 * Intent intent=new Intent(getActivity(),LogInActivity.class);
			 * startActivity(intent); } }, 2000); }
			 */
			Intent intentInfo = new Intent(getActivity(),
					MineInformationActivity.class);
			startActivity(intentInfo);
			break;
		case R.id.rl_wallet:
			/*
			 * if(mCache.getAsString("USER")!=null &&
			 * mCache.getAsString("sessionToken")!=null){ Intent intentWallet =
			 * new Intent(getActivity(), MineWalletActivity.class);
			 * startActivity(intentWallet); }else {
			 * ToastUtil.show(getActivity(), "请先登录！"); mHandler.postAtTime(new
			 * Runnable() {
			 * 
			 * @Override public void run() { // TODO Auto-generated method stub
			 * Intent intent=new Intent(getActivity(),LogInActivity.class);
			 * startActivity(intent); } }, 2000); }
			 */
			Intent intentWallet = new Intent(getActivity(),
					MineWalletActivity.class);
			startActivity(intentWallet);
			break;
		case R.id.rl_carmanager:
			/*
			 * if(mCache.getAsString("USER")!=null &&
			 * mCache.getAsString("sessionToken")!=null){ Intent intentCar = new
			 * Intent(getActivity(), MineCarmanagerActivity.class);
			 * startActivityForResult(intentCar, 0); }else {
			 * ToastUtil.show(getActivity(), "请先登录！"); mHandler.postAtTime(new
			 * Runnable() {
			 * 
			 * @Override public void run() { // TODO Auto-generated method stub
			 * Intent intent=new Intent(getActivity(),LogInActivity.class);
			 * startActivity(intent); } }, 2000); }
			 */
			Intent intentCar = new Intent(getActivity(),
					MineCarmanagerActivity.class);
			startActivityForResult(intentCar, 0);
			break;
		case R.id.rl_ordermanager:
			/*
			 * if(mCache.getAsString("USER")!=null &&
			 * mCache.getAsString("sessionToken")!=null){ Intent intentOrder =
			 * new Intent(getActivity(), MineOrdermanagerActivity.class);
			 * startActivity(intentOrder); }else { ToastUtil.show(getActivity(),
			 * "请先登录！"); mHandler.postAtTime(new Runnable() {
			 * 
			 * @Override public void run() { // TODO Auto-generated method stub
			 * Intent intent=new Intent(getActivity(),LogInActivity.class);
			 * startActivity(intent); } }, 2000); }
			 */
			Intent intentOrder = new Intent(getActivity(),
					MineOrdermanagerActivity.class);
			startActivity(intentOrder);
			break;
		case R.id.rl_shezhi:

			/*
			 * * if(mCache.getAsString("USER")!=null &&
			 * mCache.getAsString("sessionToken")!=null){ Intent intentSheZhi =
			 * new Intent(getActivity(), MineSheZhiActivity.class);
			 * startActivity(intentSheZhi); }else {
			 * ToastUtil.show(getActivity(), "请先登录！"); mHandler.postAtTime(new
			 * Runnable() {
			 * 
			 * @Override public void run() { // TODO Auto-generated method stub
			 * Intent intent=new Intent(getActivity(),LogInActivity.class);
			 * startActivity(intent); } }, 2000); }
			 */

			Intent intentSheZhi = new Intent(getActivity(),
					MineSheZhiActivity.class);
			startActivity(intentSheZhi);
			break;
		case R.id.iv_mine:
			if (mCache.getAsString("USER") != null
					&& mCache.getAsString("sessionToken") != null) {
				// 实例化SelectPicPopupWindow
				menuWindow = new MinePopu(getActivity(), itemsOnClick);
				// 显示窗口
				menuWindow.showAtLocation(
						getActivity().findViewById(R.id.main), Gravity.BOTTOM
								| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
			} else {
				ToastUtil.show(getActivity(), "请先登录！");
				mHandler.postAtTime(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent(getActivity(),
								LogInActivity.class);
						Log.e("iv_mine", "iv_mine");
						startActivityForResult(intent, 0);
					//	startActivity(intent);
					}
				}, 2000);
				//updateUserInfo();
				// mHandler.sendEmptyMessage(1);
			}
			break;

		case R.id.tv_logn:
			if (mCache.getAsString("USER") != null
					&& mCache.getAsString("sessionToken") != null) {
				setUserNickName();
			} else {
				ToastUtil.show(getActivity(), "请先登录！");
				mHandler.postAtTime(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent(getActivity(),
								LogInActivity.class);
						startActivityForResult(intent, 0);
					}
				}, 2000);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 修改用户名称 仿ios 修改
	 */
	private void setUserNickName() {
		Intent intent = new Intent(getActivity(), NickNameActivity.class);
		startActivityForResult(intent, 0);
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			String PHOTO_FILE_NAME = mCache.getAsString("USER") + ".png";
			switch (v.getId()) {
			case R.id.btn_take_photo:
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				// 判断存储卡是否可以用，可用进行存储
				if (FileUtil.hasSdcard()) {
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
							.fromFile(new File(Environment
									.getExternalStorageDirectory(),
									PHOTO_FILE_NAME)));
				}
				startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
				break;
			case R.id.btn_pick_photo:
				// 激活系统图库，选择一张图片
				Intent intent2 = new Intent(Intent.ACTION_PICK);
				intent2.setType("image/*");
				startActivityForResult(intent2, PHOTO_REQUEST_GALLERY);
				break;
			default:
				break;
			}

		}
	};

	/**
	 * 剪切图片
	 */
	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		// 图片格式
		intent.putExtra("outputFormat", "JPEG");//
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	/*
	 * 上传图片
	 */
	public void upload(View view) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
					out.flush();
					out.close();
					byte[] buffer = out.toByteArray();

					byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
					String photo = new String(encode);
					String PHOTO_FILE_NAME = mCache.getAsString("USER")
							+ ".png";
					JSONObject object = new JSONObject();
					object.put("user_id", mCache.getAsString("user_id"));
					object.put("file_base64", photo);
					object.put("file_name", PHOTO_FILE_NAME);
					object.put("image_id", "");// 旧的头像id，没有参数可以不传值或没有,
					String doHttpsPost = PostCLientUtils.doHttpsPost2(
							Information.KONGCV_UPLOAD_IMAGE,
							String.valueOf(object),
							mCache.getAsString("sessionToken"));
					Log.v("服务器返回的参数是：：：", doHttpsPost);
					JSONObject jsonObject = new JSONObject(doHttpsPost);
					String result = jsonObject.getString("result");
					JSONObject object2 = new JSONObject(result);
					String state = object2.getString("state");
					if (state.equals("ok")) {
						JSONObject imageAmcache = object2
								.getJSONObject("image");
						mCache.put("imageAmcache", imageAmcache);

						Looper.prepare();
						ToastUtil.show(getActivity(), "头像上传成功！");
						Looper.loop();
					} else {
						Looper.prepare();
						ToastUtil.show(getActivity(), "头像上传失败！");
						Looper.loop();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
