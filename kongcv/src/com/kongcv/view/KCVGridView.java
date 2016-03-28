package com.kongcv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.widget.GridView;

/**
 * kongcv fragment页面布局
 * @author kcw001
 */
public class KCVGridView extends GridView{

	public KCVGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public KCVGridView(Context context) {
		super(context);
	}

	public KCVGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		/*int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);*/
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
}
