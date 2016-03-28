package com.kongcv.global;

import java.io.Serializable;

public class RegistionBean implements Serializable{	private static final long serialVersionUID = 77009512L;	private String license_plate;	private String device_type;	private String user_name;	private String device_token;	private String mobilePhoneNumber;
	public String getLicense_plate() {		return this.license_plate;	}
	public void setLicense_plate(String license_plate) {		this.license_plate = license_plate;	}
	public String getDevice_type() {		return this.device_type;	}
	public void setDevice_type(String device_type) {		this.device_type = device_type;	}
	public String getUser_name() {		return this.user_name;	}
	public void setUser_name(String user_name) {		this.user_name = user_name;	}
	public String getDevice_token() {		return this.device_token;	}
	public void setDevice_token(String device_token) {		this.device_token = device_token;	}
	public String getMobilePhoneNumber() {		return this.mobilePhoneNumber;	}
	public void setMobilePhoneNumber(String mobilePhoneNumber) {		this.mobilePhoneNumber = mobilePhoneNumber;	}

	public String toString() {
		return "RegistionBean [license_plate = " + license_plate + ", device_type = " + device_type + ", user_name = " + user_name + ", device_token = " + device_token + ", mobilePhoneNumber = " + mobilePhoneNumber + "]";	}
}
