package com.kongcv.global;

import android.graphics.Bitmap;

public class ZyCommityAdapterBean {

	private String hire_start;
	private String hire_end;
	private double price;
	private String objectId;//订单号
	private String method;//租用方式
	private String username;
	private String image;
	private String mobilePhoneNumber;
	private int trade_state;//订单状态
	private Bitmap bitmap;
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getHire_start() {
		return hire_start;
	}
	public void setHire_start(String hire_start) {
		this.hire_start = hire_start;
	}
	public String getHire_end() {
		return hire_end;
	}
	public void setHire_end(String hire_end) {
		this.hire_end = hire_end;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}
	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}
	public int getTrade_state() {
		return trade_state;
	}
	public void setTrade_state(int trade_state) {
		this.trade_state = trade_state;
	}
	@Override
	public String toString() {
		return "ZyCommityAdapterBean [hire_start=" + hire_start + ", hire_end="
				+ hire_end + ", price=" + price + ", objectId=" + objectId
				+ ", method=" + method + ", username=" + username + ", image="
				+ image + ", mobilePhoneNumber=" + mobilePhoneNumber
				+ ", trade_state=" + trade_state + "]";
	}
	
}
