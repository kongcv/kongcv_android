package com.kongcv.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kongcv.R;

/**
 * 自定义布局 绘制文字和园
 * @author kcw001
 */
public class CircleTextView extends View {

	private String mText = "";
	private OnClickListener onClickListener;
	private Paint mCirclePaint;
	private Paint mTextPaint;
	private int mViewWidth;
	private int mViewHight;

	public CircleTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public CircleTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public CircleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray t = context.obtainStyledAttributes(attrs,
				R.styleable.CircleTextView);
		mText = t.getString(R.styleable.CircleTextView_text);
		t.recycle();

		init();
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		super.setOnClickListener(l);
		this.onClickListener = l;
	}

	public void setmText(String mText) {
		this.mText = mText;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawCircle(mViewWidth / 2, mViewWidth / 2, mViewWidth / 2,
				mCirclePaint);
		canvas.drawText(mText,
				(mViewWidth - mTextPaint.measureText(mText)) / 2,
				(mViewHight + mTextPaint.measureText(mText) / 2) / 2,
				mTextPaint);
	}

	public void init() {
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		initPaint(false);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mViewWidth = w;
		mViewHight = h;
		mTextPaint.setTextSize(mViewWidth / 3);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			initPaint(true);
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (onClickListener != null)
				onClickListener.onClick(this);
			initPaint(false);
			break;
		}
		invalidate();
		return true;
	}

	private void initPaint(boolean isPress) {
		if (!isPress) {
			mCirclePaint.setStyle(Paint.Style.STROKE);
			mCirclePaint.setColor(Color.parseColor("#40000000"));
			mTextPaint.setColor(Color.parseColor("#80000000"));
		} else {
			mCirclePaint.setStyle(Paint.Style.FILL);
			mCirclePaint.setColor(Color.parseColor("#F24949"));
			mTextPaint.setColor(Color.parseColor("#fffffe"));
		}
	}
}
