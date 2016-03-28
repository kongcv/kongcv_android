package com.kongcv.view;

import com.kongcv.R;
import com.kongcv.utils.ToastUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ToggleButton;

/**
 * 自定义开关
 */
public class MySwitch extends View {

	private Paint mPaint;// 画笔
	private Bitmap mBitmapBg;// 开关背景图片
	private Bitmap mBitmapSlide;// 滑块图片
	private int MAX_LEFT;// 滑块最大左边距

	private int mSlideLeft;// 滑块左边距

	private boolean isOpen;// true表示开关已打开, false表示已关闭

	// 代码初始化
	public MySwitch(Context context) {
		super(context);
		init();
	}

	// 布局文件初始化,带属性和样式
	public MySwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	// 布局文件初始化, 带属性
	public MySwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

		// 自定义属性
		isOpen = attrs.getAttributeBooleanValue(
				"http://schemas.android.com/apk/res/com.kongcv", "isChecked",
				false);// 获取当前选中状态

		int slideId = attrs.getAttributeResourceValue(
				"http://schemas.android.com/apk/res/com.kongcv",
				"slideDrawable", 0);// 获取滑块图片

		if (slideId > 0) {
			mBitmapSlide = BitmapFactory
					.decodeResource(getResources(), slideId);// 加载滑块图片
		}

		// 根据当前选中状态,更新界面展示
		if (isOpen) {
			mSlideLeft = MAX_LEFT;
		} else {
			mSlideLeft = 0;
		}
		invalidate();
	}

	/**
	 * 初始化
	 */
	private void init() {
		// 初始化画笔对象
		mPaint = new Paint();
		mPaint.setColor(Color.RED);

		// 初始化图片对象
		mBitmapBg = BitmapFactory.decodeResource(getResources(),
				R.drawable.switch_background);
		mBitmapSlide = BitmapFactory.decodeResource(getResources(),
				R.drawable.slide_button);

		// 计算滑块最大左边距
		MAX_LEFT = mBitmapBg.getWidth() - mBitmapSlide.getWidth();

		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isClick) {
					if (isOpen) {
						isOpen = false;
						mSlideLeft = 0;
					} else {
						isOpen = true;
						mSlideLeft = MAX_LEFT;
					}

					if (mListener != null) {
						mListener.onCheckedChanged(MySwitch.this, isOpen);
					}
					invalidate();// 刷新view, 会重新调用onDraw方法
				}
			}
		});
	}

	private OnCheckedChangeListener mListener;

	/**
	 * 开关切换事件的回调接口
	 */
	public interface OnCheckedChangeListener {
		public void onCheckedChanged(MySwitch view, boolean isChecked);
	}

	/**
	 * 设置开关切换的事件监听
	 */
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mListener = listener;
	}
	
	public void setChecked(boolean ischecked) {
			isOpen=ischecked;
			if (isOpen) {
				isOpen = false;
				mSlideLeft = 0;
			} else {
				isOpen = true;
				mSlideLeft = MAX_LEFT;
			}

			if (mListener != null) {
				mListener.onCheckedChanged(MySwitch.this, isOpen);
			}
			invalidate();// 刷新view, 会重新调用onDraw方法
	}

	int startX;// 起始x坐标
	int mMoveX;// 水平移动距离
	boolean isClick;// 表示当前是否可以响应点击事件

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int endX = (int) event.getX();
			int dx = endX - startX;

			mMoveX += Math.abs(dx);// 计算移动距离

			mSlideLeft += dx;// 根据x坐标移动偏移量,计算最新的左边距值

			// 修正移动位置,避免过于偏左
			if (mSlideLeft < 0) {
				mSlideLeft = 0;
			}

			// 修正移动位置,避免过于偏右
			if (mSlideLeft > MAX_LEFT) {
				mSlideLeft = MAX_LEFT;
			}

			// 刷新view
			invalidate();
			// 重新初始化起始点坐标
			startX = (int) event.getX();
			break;

		case MotionEvent.ACTION_UP:
			if (mMoveX >= 5) {// 如果移动距离大于等于5个像素,认为触发了移动事件,此时不允许响应单击事件
				isClick = false;
			} else {
				isClick = true;
			}

			mMoveX = 0;// 移动距离归零
			if (!isClick) {
				// 手指抬起后,根据当前滑块位置,确定开关状态
				if (mSlideLeft > MAX_LEFT / 2) {// 打开状态
					mSlideLeft = MAX_LEFT;
					isOpen = true;
				} else {// 关闭状态
					mSlideLeft = 0;
					isOpen = false;
				}

				invalidate();// 刷新

				// 开关切换事件的回调方法
				if (mListener != null) {
					mListener.onCheckedChanged(MySwitch.this, isOpen);
				}
			}

			break;

		default:
			break;
		}

		return super.onTouchEvent(event);// 此处改成父类方法, 可以同时响应onClick和onTouchEvent
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		System.out.println("onMeasure");
		setMeasuredDimension(mBitmapBg.getWidth(), mBitmapBg.getHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// canvas.drawRect(0, 0, 200, 200, mPaint);//绘制矩形
		canvas.drawBitmap(mBitmapBg, 0, 0, mPaint);// 绘制图片
		canvas.drawBitmap(mBitmapSlide, mSlideLeft, 0, mPaint);
	}
}
