package com.kongcv.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.kongcv.R;

public class LoadingProgressDialog extends ProgressDialog{  
  
    public LoadingProgressDialog(Context context) {  
        super(context);  
    }  
      
    public LoadingProgressDialog(Context context, int theme) {  
        super(context, theme);  
    }  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.dialog_loading);  
    }  
      
    public static LoadingProgressDialog show(Context ctx){  
    	LoadingProgressDialog d = new LoadingProgressDialog(ctx);  
        d.show();  
        return d;  
    }  
    public static LoadingProgressDialog dismiss(Context ctx){  
    	LoadingProgressDialog d = new LoadingProgressDialog(ctx);  
        d.dismiss();  
        return d;  
    }  
}  