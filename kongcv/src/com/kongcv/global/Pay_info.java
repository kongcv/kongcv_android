package com.kongcv.global;

import java.io.Serializable;

public class Pay_info implements Serializable{
	private static final long serialVersionUID = 22339797L;
	private String mode;
	private String device_type;
	private String device_token;
	private String phone;
	private String subject;//产品名称 先传车位地址
	private String pay_type;
	private String open_id;
	
	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	/**
	 * @return the device_type
	 */
	public String getDevice_type() {
		return device_type;
	}
	/**
	 * @param device_type the device_type to set
	 */
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the pay_type
	 */
	public String getPay_type() {
		return pay_type;
	}
	/**
	 * @param pay_type the pay_type to set
	 */
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	/**
	 * @return the open_id
	 */
	public String getOpen_id() {
		return open_id;
	}
	/**
	 * @param open_id the open_id to set
	 */
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	/**
	 * @return the device_token
	 */
	public String getDevice_token() {
		return device_token;
	}
	/**
	 * @param device_token the device_token to set
	 */
	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Pay_info [mode=" + mode + ", device_type=" + device_type
				+ ", device_token=" + device_token + ", phone=" + phone
				+ ", subject=" + subject + ", pay_type=" + pay_type
				+ ", open_id=" + open_id + "]";
	}
	
}
