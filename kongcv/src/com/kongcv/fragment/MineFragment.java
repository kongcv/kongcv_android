package com.kongcv.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.json.JSONObject;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kongcv.R;
import com.kongcv.ImageRun.GetImage;
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
	private View view;
	private CircleImageView mFace;

	private ACacheUtils mCache;
	private String user_name;
	// 自定义的弹出框类
	MinePopu menuWindow;
	private String url;

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

		mHandler.sendEmptyMessage(0);
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
			case 0:
				if (mCache.getAsString("USER") != null) {
//					Bundle arguments = getArguments();
//					String userUrl = arguments.getString("userUrl");
//					String userName = arguments.getString("userName");
//					if (userUrl != null && userName != null) {
//						uName.setText(userName);
//						updateUserImage(userUrl);
//					}
					updateUser();
				}
				break;
			case 1:
				String username = (String) msg.obj;
				uName.setText(username);
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

//		private void updateUserImage(String userUrl) {
//			ReadType readType = new ReadType();
//			readType.execute(userUrl);
//		}
//	};
//
//	class ReadType extends PreReadTask<String, Void, Void> {
//		@Override
//		protected Void doInBackground(String... params) {
//			bit = GetImage.getHttpBitmap(params[0]);
//
//			Message msg = mHandler.obtainMessage();
//			msg.what = 1;
//			msg.obj = bit;
//			mHandler.sendMessage(msg);
//			return null;
//		}
//	}
	private void updateUser() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject object = new JSONObject();
					object.put("mobilePhoneNumber", mCache.getAsString("USER"));
					object.put("user_id", mCache.getAsString("user_id"));
					String jsoStr = PostCLientUtils.doHttpsPost2(Information.KONGCV_GET_USERINFO, JsonStrUtils.JsonStr(object), mCache.getAsString("sessionToken"));
					JSONObject obj = new JSONObject(jsoStr);
					String username = obj.getJSONObject("result").getString("username");
					if (obj.getJSONObject("result").has("image")) {
						url = obj.getJSONObject("result").getJSONObject("image").getString("url");
						bit = GetImage.getHttpBitmap(url);
					} else {
						url = "";
						bit = null;
					}
					Message msg = mHandler.obtainMessage();
					msg.what = 1;
					msg.obj = username;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onResume() {
		super.onResume();
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
		case 0:
			if (data != null) {
				user_name = data.getStringExtra("user_name");
				uName.setText(user_name);
			}
			break;
		case 10:
			if (data != null) {
				user_name = data.getStringExtra("nick");
				if(user_name!=null){
				uName.setText(user_name);
				}else{
					uName.setText("登录");
				}
				Bitmap bit = data.getParcelableExtra("bit");
				if (bit != null) {
					mFace.setImageBitmap(bit);
				}else{
					mFace.setImageResource(R.drawable.defaulto);
				}
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
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
			 * if(mCache.getAsString("USER")!=null &&
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
						//	startActivity(intent);
						startActivityForResult(intent, 0);
					}
				}, 2000);
				mHandler.sendEmptyMessage(1);
			}
			break;

		case R.id.tv_logn:
			// setDialog();
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
						//	startActivity(intent);
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
	 * 修改用户名称
	 */
	private void setDialog() {
		final Dialog mDialog = new Dialog(getActivity(), R.style.MyDialog);
		mDialog.setContentView(R.layout.mydialog_user);
		TextView oldUser = (TextView) mDialog.findViewById(R.id.old_name);
		oldUser.setText("13717950391");
		EditText newUser = (EditText) mDialog.findViewById(R.id.new_name);
		Button selectCancel = (Button) mDialog.findViewById(R.id.select_cancel);
		selectCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}

	/**
	 * 修改用户名称 仿ios 修改2
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
