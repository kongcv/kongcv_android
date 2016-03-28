package com.kongcv.global;

import java.io.Serializable;

public class CurbAndObjectId implements Serializable{

	private String ObjectId;
	private String mode;
	private String field;
	/**
	 * @return the objectId
	 */
	public String getObjectId() {
		return ObjectId;
	}
	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId(String objectId) {
		ObjectId = objectId;
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
	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}
	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}
	
}
