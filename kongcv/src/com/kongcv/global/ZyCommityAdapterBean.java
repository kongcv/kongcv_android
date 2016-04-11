package com.kongcv.global;

import java.io.Serializable;

import android.graphics.Bitmap;

public class ZyCommityAdapterBean implements Serializable{

	
	private static final long serialVersionUID = 9061047355702269342L;
	private String hire_start;
	private String hire_end;
	private double price;
	private double money;
	private String objectId;//订单号
	private String method;//租用方式
	private String username;
	private String image;
	private String mobilePhoneNumber;
	private int trade_state;//订单状态
	private int handsel_state;
	private Bitmap bitmap;
	
	private String park_id; //道边
	private String field;
	private String address;
	private String park_curb;
	private String device_type;
	private String device_token;
	private int check_state;
	
	
	
	
	
	public int getCheck_state() {
		return check_state;
	}
	public void setCheck_state(int check_state) {
		this.check_state = check_state;
	}
	public String getPark_id() {
		return park_id;
	}
	public void setPark_id(String park_id) {
		this.park_id = park_id;
	}
	
	private String mode;
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public String getDevice_token() {
		return device_token;
	}
	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}
	public int getHandsel_state() {
		return handsel_state;
	}
	public void setHandsel_state(int handsel_state) {
		this.handsel_state = handsel_state;
	}
	public String getPark_curb() {
		return park_curb;
	}
	public void setPark_curb(String park_curb) {
		this.park_curb = park_curb;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getParkId() {
		return park_id;
	}
	public void setParkId(String parkId) {
		this.park_id = parkId;
	}
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
