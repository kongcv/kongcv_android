package com.kongcv.global;

import java.io.Serializable;

public class JpushBean implements Serializable{	private static final long serialVersionUID = 1401634710L;	private String address;	private String park_id;	private double price;
	private String own_mobile;	private String own_device_type;	private String hire_method_id;
	private String hire_method_field;	private String message_id;	private String hire_start;	private String own_device_token;	private String hire_end;	private String push_type;	private String mode;
	public String getAddress() {		return this.address;	}
	public void setAddress(String address) {		this.address = address;	}
	public String getPark_id() {		return this.park_id;	}
	public void setPark_id(String park_id) {		this.park_id = park_id;	}
	
	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getOwn_mobile() {		return this.own_mobile;	}
	public void setOwn_mobile(String own_mobile) {		this.own_mobile = own_mobile;	}
	public String getOwn_device_type() {		return this.own_device_type;	}
	public void setOwn_device_type(String own_device_type) {		this.own_device_type = own_device_type;	}
	public String getHire_method_id() {		return this.hire_method_id;	}
	public void setHire_method_id(String hire_method_id) {		this.hire_method_id = hire_method_id;	}
	public String getMessage_id() {		return this.message_id;	}
	public void setMessage_id(String message_id) {		this.message_id = message_id;	}
	public String getHire_start() {		return this.hire_start;	}
	public void setHire_start(String hire_start) {		this.hire_start = hire_start;	}
	public String getOwn_device_token() {		return this.own_device_token;	}
	public void setOwn_device_token(String own_device_token) {		this.own_device_token = own_device_token;	}
	public String getHire_end() {		return this.hire_end;	}
	public void setHire_end(String hire_end) {		this.hire_end = hire_end;	}
	public String getPush_type() {		return this.push_type;	}
	public void setPush_type(String push_type) {		this.push_type = push_type;	}
	public String getMode() {		return this.mode;	}
	public void setMode(String mode) {		this.mode = mode;	}
		public String getHire_method_field() {
		return hire_method_field;
	}

	public void setHire_method_field(String hire_method_field) {
		this.hire_method_field = hire_method_field;
	}

}
