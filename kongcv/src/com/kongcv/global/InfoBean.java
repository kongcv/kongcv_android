package com.kongcv.global;

public class InfoBean {

	private String address;
	private String state;
	private String park_id;
	private String hire_method_field;
	private String hire_method_id;
	
	public String getPark_id() {
		return park_id;
	}
	public void setPark_id(String park_id) {
		this.park_id = park_id;
	}
	
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	private String hire_start;
	private String mode;
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public String getHire_start() {
		return hire_start;
	}
	public void setHire_start(String hire_start) {
		this.hire_start = hire_start;
	}
	/**
	 * @return the hire_method_field
	 */
	public String getHire_method_field() {
		return hire_method_field;
	}
	/**
	 * @param hire_method_field the hire_method_field to set
	 */
	public void setHire_method_field(String hire_method_field) {
		this.hire_method_field = hire_method_field;
	}
	/**
	 * @return the hire_method_id
	 */
	public String getHire_method_id() {
		return hire_method_id;
	}
	/**
	 * @param hire_method_id the hire_method_id to set
	 */
	public void setHire_method_id(String hire_method_id) {
		this.hire_method_id = hire_method_id;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InfoBean [address=" + address + ", state=" + state
				+ ", park_id=" + park_id + ", hire_method_field="
				+ hire_method_field + ", hire_method_id=" + hire_method_id
				+ ", hire_start=" + hire_start + ", mode=" + mode + "]";
	}
	
}



















