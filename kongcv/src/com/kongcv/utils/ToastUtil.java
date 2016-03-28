package com.kongcv.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
public class ToastUtil {

	public static void show(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}

	public static void show(Context context, int info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 判断edittext是否null
	 */
	public static String checkEditText(EditText editText) {
		if (editText != null && editText.getText() != null
				&& !(editText.getText().toString().trim().equals(""))) {
			return editText.getText().toString().trim();
		} else {
			return "";
		}
	}
	/**
	 * 判断是否处于登录状态
	 */
	public static boolean isLogIn(String phoneNumber){
		if(phoneNumber==null){
			return false;
		}
		return true;
	}
	/**
	 * 动态计算listview的宽高
	 * @param mListPhone2
	 */
	public static void fixListViewHeight(ListView listView,int num) {
		// TODO Auto-generated method stub
		 // 如果没有设置数据适配器，则ListView没有子项，返回。  
        ListAdapter listAdapter = listView.getAdapter();  
        int totalHeight = 0;   
        if (listAdapter == null) {   
            return;   
        }   
        if(num==-1){
        	for (int i = 0, len = listAdapter.getCount(); i < len; i++) {     
                View listViewItem = listAdapter.getView(i , null, listView);  
                // 计算子项View 的宽高   
                listViewItem.measure(0, 0);    
                // 计算所有子项的高度和
                totalHeight += listViewItem.getMeasuredHeight();    
            }  
        }else{
        	for (int i = 0, len = num; i < len; i++) {     
                View listViewItem = listAdapter.getView(i , null, listView);  
                // 计算子项View 的宽高   
                listViewItem.measure(0, 0);    
                // 计算所有子项的高度和
                totalHeight += listViewItem.getMeasuredHeight();    
            } 
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();   
        // listView.getDividerHeight()获取子项间分隔符的高度   
        // params.height设置ListView完全显示需要的高度    
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
        listView.setLayoutParams(params);   
	}
	
}
