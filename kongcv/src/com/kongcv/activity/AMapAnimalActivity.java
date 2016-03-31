package com.kongcv.activity;

import com.kongcv.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AMapAnimalActivity extends Activity {

	private ImageView img;
	private int w;
	private int h;
	private ListView lv;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapanimal);
		w = getWindowManager().getDefaultDisplay().getWidth();
		h = getWindowManager().getDefaultDisplay().getHeight();
		lv = (ListView) findViewById(R.id.lv);
		img = (ImageView) findViewById(R.id.img);
		layout = (LinearLayout) findViewById(R.id.lin);

		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutParams layoutParams = layout.getLayoutParams();
				layout.setLayoutParams(layoutParams);
				img.setPivotX(0);
				img.setPivotY(0);
				img.animate().scaleX(10).setDuration(3000).start();
				img.animate().scaleY(8).setDuration(3000).start();
				lv.animate().translationY(500).setDuration(3000).start();
			}
		});

		lv.setAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView t = new TextView(AMapAnimalActivity.this);
				t.setBackgroundColor(Color.RED);
				t.setText("我是" + position);
				return t;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public int getCount() {
				return 50;
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LayoutParams layoutParams = layout.getLayoutParams();
				layoutParams.height = h;
				layout.setLayoutParams(layoutParams);
				img.setPivotX(0);
				img.setPivotY(0);
				img.animate().scaleX(10).setDuration(3000).start();
				img.animate().scaleY(8).setDuration(3000).start();
				lv.animate().translationY(500).setDuration(3000).start();
			}
		});
	}

}
