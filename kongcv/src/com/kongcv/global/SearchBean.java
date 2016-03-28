package com.kongcv.global;

import java.io.Serializable;

public class SearchBean implements Serializable{	private static final long serialVersionUID = 1848425001L;	private String address;	private String sort;	private int skip;	private String hire_field;	private String hire_method_id;	private Location_info location_info;	private int limit;	private String mode;
	public String getAddress() {		return this.address;	}
	public void setAddress(String address) {		this.address = address;	}
	public String getSort() {		return this.sort;	}
	public void setSort(String sort) {		this.sort = sort;	}
	public long getSkip() {		return this.skip;	}
	public void setSkip(int skip) {		this.skip = skip;	}
	public String getHire_field() {		return this.hire_field;	}
	public void setHire_field(String hire_field) {		this.hire_field = hire_field;	}
	public String getHire_method_id() {		return this.hire_method_id;	}
	public void setHire_method_id(String hire_method_id) {		this.hire_method_id = hire_method_id;	}
	public Location_info getLocation_info() {		return this.location_info;	}
	public void setLocation_info(Location_info location_info) {		this.location_info = location_info;	}
	public long getLimit() {		return this.limit;	}
	public void setLimit(int limit) {		this.limit = limit;	}
	public String getMode() {		return this.mode;	}
	public void setMode(String mode) {		this.mode = mode;	}
}

