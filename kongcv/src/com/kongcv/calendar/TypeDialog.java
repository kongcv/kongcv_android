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
import com.kongcv.fragment.PublishFragment;
import com.kongcv.global.Community;
import com.kongcv.global.TimeBean;
import com.kongcv.global.TypeBean;
import com.kongcv.utils.ACacheUtils;
import com.kongcv.utils.Data;
import com.kongcv.utils.StringUtils;
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

	TypeBean bean = new TypeBean();
	TimeBean timer = new TimeBean();
	ArrayList<TypeBean> items = new ArrayList<TypeBean>();
	ArrayList<TypeBean> typeBeans;
	ArrayList<String> onItemSelectedId = new ArrayList<String>();
	private List<String> list = new ArrayList<String>();
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
	private boolean flag = true;
	private boolean StartOrEnd = true;
	private String start;
	private String end;
	private int i = 0;
	
	public interface LeaveMyDialogListener {
		public void refreshUI(ArrayList<TypeBean> item);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_day:
			i = 0;
			flag = true;
			findViewById(R.id.tv_txt1).setVisibility(View.VISIBLE);
			findViewById(R.id.tv_txt2).setVisibility(View.INVISIBLE);
		//	mPicker.setVisibility(View.INVISIBLE);
			mPicker.setVisibility(View.GONE);
			adapterData(commMethod, spinner);
			break;
		case R.id.tv_time:
			i = 1;
			flag = false;
			findViewById(R.id.tv_txt2).setVisibility(View.VISIBLE);
			findViewById(R.id.tv_txt1).setVisibility(View.INVISIBLE);
			mPicker.setVisibility(View.VISIBLE);
			adapterData(curbMethod, spinner);
			break;
		case R.id.iv_submit://提交的时候
			typeBeans=PublishFragment.mydialog;
			for(int i=0;i<typeBeans.size();i++){
				System.out.println("typeBeans.get(i).getField():"+typeBeans.get(i).getField());
				System.out.println("typeBeans.get(i):"+typeBeans.get(i).toString());
			}
			System.out.println("bean.getData():"+bean.getDate());
			System.out.println("bean.getData():"+bean.getDate());
			System.out.println("bean.getData():"+bean.getDate());
			System.out.println("bean.getField():"+bean.getField());
			if(typeBeans!=null && typeBeans.size()>0){
				for(int i=0;i<typeBeans.size();i++){
						if ("".equals(price.getText().toString()) && tvPrice==null) {
							ToastUtil.show(context, "价格不能为0！");
							return;
						} else {
							if (StringUtils.isNumber(price.getText().toString()) == null) {
								ToastUtil.show(context, "价格不能为0！");
								return;
							} else {
								if (tvPrice == null) {
									ToastUtil.show(context, "价格不能为空！");
									return;
								} else {
									if (StringUtils.isNum(tvPrice)) {
										bean.setPrice(tvPrice + result);
										if (end != null && start != null) {
											String date = start + "--" + end;
											bean.setDate(date==null?"0":date);
										}
									} else {
										ToastUtil.show(context, "无效的价格数据！");
										return;
									}
								}
							}
						}
					//}
				}
			}else{
				if(price.getText().toString()==null && tvPrice==null && !StringUtils.isNum(tvPrice)) {
					ToastUtil.show(context, "价格不能为0！");
					return;
				} else {
					if (StringUtils.isNumber(price.getText().toString()) == null) {
						ToastUtil.show(context, "价格不能为0！");
						return;
					} else {
						if (tvPrice == null) {
							ToastUtil.show(context, "价格不能为空！");
							return;
						} else {
							if (StringUtils.isNum(tvPrice)) {
								bean.setPrice(tvPrice + result);
								if (end != null && start != null) {
									String date = end + "--" + start;
									bean.setDate(date==null?"0":date);
								}else{
									if(flag){
										bean.setDate("0");
									}else{
										ToastUtil.show(context, "未设置起始出租时间!");
										return;
									}
								}
							} else {
								ToastUtil.show(context, "无效的价格数据！");
								return;
							}
						}
					}
				}
			}
			if(bean.getDate()==null){
				bean.setDate("0");
			}
			items.add(bean);
			listener.refreshUI(items);
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

	private void timePicker() {
		TimePickerDialog time = new TimePickerDialog(context, R.style.MyDialog,
				new OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						// TODO Auto-generated method stub
						if (StartOrEnd) {
							start = hourOfDay + ":" + minute;
							mStart.setText(start);
						} else {
							end = hourOfDay + ":" + minute;
							mEnd.setText(end);
						}
					}
				}, 12, 30, true);
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
//		Community community = (Community) Data.getData("corc");
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
		mPicker.setVisibility(View.GONE);
	//	mPicker.setVisibility(View.INVISIBLE);
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
	private String tvPrice = null;

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
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
	}

}
