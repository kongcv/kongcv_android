package com.kongcv.UI.AsyncImageLoader;

import android.graphics.drawable.Drawable;

/**
 * 作为一个tag保存一些例如位置，地址，头像等信息
 * 
 */
public class TagInfo {
	 	String url;  
	    int position;  
	    Drawable drawable;  
	      
	    public String getUrl() {  
	        return url;  
	    }  
	    public void setUrl(String url) {  
	        this.url = url;  
	    }  
	    public int getPosition() {  
	        return position;  
	    }  
	    public void setPosition(int position) {  
	        this.position = position;  
	    }  
	    public Drawable getDrawable() {  
	        return drawable;  
	    }  
	    public void setDrawable(Drawable drawable) {  
	        this.drawable = drawable;  
	    }  
	  
}  
