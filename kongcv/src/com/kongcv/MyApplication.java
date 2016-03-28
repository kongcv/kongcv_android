package com.kongcv;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import cn.jpush.android.api.JPushInterface;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.kongcv.ImageRun.VolleySingleton;
import com.kongcv.utils.ConfigCacheUtil;

/**
 * 配置ImageLoader
 */
public class MyApplication extends Application {

	ImageLoader mImageLoader;
	RequestQueue mRequestQueue;
	public static String TAG;
	private static MyApplication myApplication;
	private List<Activity> activityList = new LinkedList<Activity>();

	public static MyApplication getInstance() {
		if (null == myApplication) {
			myApplication = new MyApplication();
		}
		return myApplication;
	}

	// 添加Activity 到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity 并finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		TAG = this.getClass().getSimpleName();
		myApplication = this;
		mRequestQueue = VolleySingleton.getVolleySingleton(
				this.getApplicationContext()).getRequestQueue();
		initJPush();
		getImageLoader();
	}

	/**
	 * 初始化极光推送
	 */
	private void initJPush() {
		// TODO Auto-generated method stub
		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
	}

	public ImageLoader getImageLoader() {
		if (mImageLoader == null) {
			// 这个是ImageLoader 的缓存，每次新启动应用，都会走这里
			final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(
					20);
			ImageCache imageCache = new ImageCache() {
				@Override
				public void putBitmap(String key, Bitmap value) {
					mImageCache.put(key, value);
					// 保存到本地
					ConfigCacheUtil.saveBitmap2(value, key,
							getApplicationContext());
				}

				@Override
				public Bitmap getBitmap(String key) {
					return mImageCache.get(key);
				}
			};
			mImageLoader = new ImageLoader(mRequestQueue, imageCache);
		}
		return mImageLoader;
	}
}
