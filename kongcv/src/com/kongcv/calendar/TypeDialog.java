package com.kongcv.calendar;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kongcv.R;
import com.kongcv.global.Community;
import com.kongcv.global.TimeBean;
import com.kongcv.global.TypeBean;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.Data;
import com.kongcv.utils.ToastUtil;

/**
 * 
 * 出租类型 自定义dialog
 */
public class TypeDialog extends Dialog implements TextWatcher,
		android.view.View.OnClickListener, OnItemSelectedListener {

	private Context context;
	private Spinner spinner;
	private TextView mDay, mTime;
	private ArrayAdapter<String> adapter;
	private LeaveMyDialogListener listener;

	TimeBean timer = new TimeBean();
	ArrayList<String> onItemSelectedId = new ArrayList<String>();
	private ImageView mSubmit;
	private View mTxt1, mTxt2, mPicker;
	private ACacheUtils mCache;
	private TextView mStart, mEnd;
	private EditText price;

	private List<String> commMethod = new ArrayList<String>();
	private List<String> curbMethod = new ArrayList<String>();
	private List<String> commObjectId = new ArrayList<String>();
	private List<String> curbObjectId = new ArrayList<String>();
	private List<String> commField = new ArrayList<String>();
	private List<String> curbField = new ArrayList<String>();

	
	public interface LeaveMyDialogListener {
		public void refreshDate(TypeBean item);
	}
	public TypeDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public TypeDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public TypeDialog(Context context, int theme, LeaveMyDialogListener listener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.listener = listener;
	}

	private boolean flag = true, StartOrEnd = true;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_day:
			flag = true;
			findViewById(R.id.tv_txt1).setVisibility(View.VISIBLE);
			findViewById(R.id.tv_txt2).setVisibility(View.INVISIBLE);
			mPicker.setVisibility(View.INVISIBLE);
			adapterData(commMethod, spinner);
			break;
		case R.id.tv_time:
			flag = false;
			findViewById(R.id.tv_txt2).setVisibility(View.VISIBLE);
			findViewById(R.id.tv_txt1).setVisibility(View.INVISIBLE);
			mPicker.setVisibility(View.VISIBLE);
			adapterData(curbMethod, spinner);
			break;
		case R.id.iv_submit:// 提交的时候
			if (tvPrice != null) {
				bean.setPrice(tvPrice);
				if (flag) {
					if (bean.getField() != null && bean.getMethod() != null
							&& bean.getObjectId() != null) {
						listener.refreshDate(bean);
					} else {
						bean.setMethod(commMethod.get(0));
						bean.setObjectId(commObjectId.get(0));
						bean.setField(commField.get(0));
						listener.refreshDate(bean);
					}
				} else {
					if (startStr != null && endStr != null) {
						if (startStr.equals(endStr)) {
							ToastUtil.show(context, "起始时间相同!");
							return;
						} else {
							bean.setDate(startStr+":"+endStr);
							if (bean.getField() != null
									&& bean.getMethod() != null
									&& bean.getObjectId() != null) {
								listener.refreshDate(bean);
							} else {
								bean.setMethod(curbMethod.get(0));
								bean.setObjectId(curbObjectId.get(0));
								bean.setField(curbField.get(0));
								listener.refreshDate(bean);
							}
						}
					} else {
						ToastUtil.show(context, "请填写时间信息!");
						return;
					}
				}
			} else {
				ToastUtil.show(context, "请填写价格信息!");
				return;
			}
			dismiss();
			break;
		case R.id.type_et_start:
			StartOrEnd = true;
			timePicker();
			break;
		case R.id.type_et_end:
			StartOrEnd = false;
			timePicker();
			break;
		default:
			break;
		}
	}

	private String startStr = null, endStr = null;

	private void timePicker() {
		TimePickerDialog time = new TimePickerDialog(context, R.style.MyDialog,
				new OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						// TODO Auto-generated method stub
						if (StartOrEnd) {
							if(hourOfDay<10){
								if(minute<10){
									mStart.setText("0"+hourOfDay + ":0" + minute);
								}else{
									mStart.setText("0"+hourOfDay + ":" + minute);
								}
							}else{
								if(minute<10){
									mStart.setText(hourOfDay + ":0" + minute);
								}else{
									mStart.setText(hourOfDay + ":" + minute);
								}
							}
							startStr = mStart.getText().toString();
						} else {
							if(hourOfDay<10){
								if(minute<10){
									mEnd.setText("0"+hourOfDay + ":0" + minute);
								}else{
									mEnd.setText("0"+hourOfDay + ":" + minute);
								}
							}else{
								if(minute<10){
									mEnd.setText(hourOfDay + ":0" + minute);
								}else{
									mEnd.setText(hourOfDay + ":" + minute);
								}
							}
							endStr = mEnd.getText().toString();
						}
					}
				}, 12, 00, true);
		time.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mCache = ACacheUtils.get(context);
		initView();
	}

	private void initView() {
		mDay = (TextView) findViewById(R.id.tv_day);
		mTime = (TextView) findViewById(R.id.tv_time);
		spinner = (Spinner) findViewById(R.id.spinnerAdapter);
		mSubmit = (ImageView) findViewById(R.id.iv_submit);

		mStart = (TextView) findViewById(R.id.type_et_start);
		mEnd = (TextView) findViewById(R.id.type_et_end);

		mStart.setOnClickListener(this);
		mEnd.setOnClickListener(this);

		price = (EditText) findViewById(R.id.et_price);
		mTxt1 = findViewById(R.id.tv_txt1);
		mTxt2 = findViewById(R.id.tv_txt2);

		mSubmit.setOnClickListener(this);
		mDay.setOnClickListener(this);
		mTime.setOnClickListener(this);
		spinner.setOnItemSelectedListener(this);
		price.addTextChangedListener(this);

		Community community = (Community) Data.getData("community");
		if (community != null) {
			List<String> method = community.getMethod();
			List<String> objectId = community.getObjectId();
			List<String> hire_field = community.getHire_field();
			for (int i = 0; i < method.size(); i++) {
				if (i < 2) {
					commMethod.add(method.get(i));
					commObjectId.add(objectId.get(i));
					commField.add(hire_field.get(i));
				} else {
					curbMethod.add(method.get(i));
					curbObjectId.add(objectId.get(i));
					curbField.add(hire_field.get(i));
				}
			}
		}
		adapterData(commMethod, spinner);
		mPicker = findViewById(R.id.ll);
		mPicker.setVisibility(View.INVISIBLE);
	}

	/**
	 * 初始化数据
	 */
	private void adapterData(List<String> list, Spinner spinner) {
		// 车辆类型内如与arrayAdapter连接
		adapter = new ArrayAdapter<String>(getContext(),
				R.layout.spinner_activity, list);
		// 设置下拉列表的风格
		adapter.setDropDownViewResource(R.layout.spinner_activity);
		// 将adapter 添加到spinner中
		spinner.setAdapter(adapter);
	}

	private String object;
	private List data;
	private String result = null;

	private TypeBean bean;
	private String tvPrice = null;

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		bean = new TypeBean();
		if (flag) {
			result = commMethod.get(position).substring(
					commMethod.get(position).length() - 1);
			bean.setMethod(commMethod.get(position));
			bean.setObjectId(commObjectId.get(position));
			bean.setField(commField.get(position));
		} else {
			bean.setMethod(curbMethod.get(position));
			result = curbMethod.get(position).substring(
					curbMethod.get(position).length() - 1);
			bean.setMethod(curbMethod.get(position));
			bean.setObjectId(curbObjectId.get(position));
			bean.setField(curbField.get(position));
		}
		result = "/" + result;
		price.setHint(result);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
	}

	/**
	 * 输入价格侦听
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterTextChanged(Editable s) {
		tvPrice = s.toString();
		tvPrice=tvPrice+result;
	}

}
