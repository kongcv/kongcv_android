package com.kongcv.global;

public class AboutUsBean {
	private String info;
	private String name;
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "AboutUsBean [info=" + info + ", name=" + name + "]";
	}
	

}
