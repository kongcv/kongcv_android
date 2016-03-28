package com.kongcv.global;

import java.io.Serializable;

public class ConsentBean implements Serializable{	private static final long serialVersionUID = 94715198L;	private String address;	private String park_id;	private long price;	private String own_mobile;	private String own_device_type;	private String hire_method_id;	private String message_id;	private String hire_start;	private String own_device_token;	private String hire_end;	private String push_type;	private String mode;
	public String getAddress() {		return this.address;	}
	public void setAddress(String address) {		this.address = address;	}
	public String getPark_id() {		return this.park_id;	}
	public void setPark_id(String park_id) {		this.park_id = park_id;	}
	public long getPrice() {		return this.price;	}
	public void setPrice(long price) {		this.price = price;	}
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

	public String toString() {
		return "ConsentBean [address = " + address + ", park_id = " + park_id + ", price = " + price + ", own_mobile = " + own_mobile + ", own_device_type = " + own_device_type + ", hire_method_id = " + hire_method_id + ", message_id = " + message_id + ", hire_start = " + hire_start + ", own_device_token = " + own_device_token + ", hire_end = " + hire_end + ", push_type = " + push_type + ", mode = " + mode + "]";	}
}
