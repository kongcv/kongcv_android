package com.kongcv.global;

import java.io.Serializable;
import java.util.List;

public class Community implements Serializable{	private static final long serialVersionUID = 959214212L;	private List<String> objectId;//objectId	private List<String> hire_field;//添加的字段
	private List<String> method;
	private List<String> url;
	private List<Integer> hire_type;
	private String mode;
	
	/**
	 * @return the hire_type
	 */
	public List<Integer> getHire_type() {
		return hire_type;
	}

	/**
	 * @param hire_type the hire_type to set
	 */
	public void setHire_type(List<Integer> hire_type) {
		this.hire_type = hire_type;
	}

	public List<String> getMethod() {
		return method;
	}

	public void setMethod(List<String> method) {
		this.method = method;
	}
	public List<String> getHire_field() {		return this.hire_field;	}
	public void setHire_field(List<String> hire_field) {		this.hire_field = hire_field;	}

	/**
	 * @return the objectId
	 */
	public List<String> getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId(List<String> objectId) {
		this.objectId = objectId;
	}

	/**
	 * @return the url
	 */
	public List<String> getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(List<String> url) {
		this.url = url;
	}

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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Community [objectId=" + objectId + ", hire_field=" + hire_field
				+ ", method=" + method + ", url=" + url + ", hire_type="
				+ hire_type + ", mode=" + mode + "]";
	}
}
