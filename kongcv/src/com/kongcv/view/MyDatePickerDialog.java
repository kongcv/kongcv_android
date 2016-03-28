package com.kongcv.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

public class MyDatePickerDialog extends DatePickerDialog {
        public MyDatePickerDialog(Context context,
                int mydialog, OnDateSetListener callBack, int year, int monthOfYear,
                int dayOfMonth) {
        	super(context, mydialog, callBack, year, monthOfYear, dayOfMonth);
        }
}
