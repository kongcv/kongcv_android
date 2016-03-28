package com.kongcv.global;

import java.io.Serializable;

public class PayBean implements Serializable{	private static final long serialVersionUID = 22339797L;//	private long amount;
	private double amount;	private String pay_info;	private String order_no;	private String subject;	private String channel;	private String open_id;
	/*public long getAmount() {		return this.amount;	}
	public void setAmount(long amount) {		this.amount = amount;	}*/
	public double getAmount() {
		return this.amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getPay_info() {		return this.pay_info;	}
	public void setPay_info(String pay_info) {		this.pay_info = pay_info;	}
	public String getOrder_no() {		return this.order_no;	}
	public void setOrder_no(String order_no) {		this.order_no = order_no;	}
	public String getSubject() {		return this.subject;	}
	public void setSubject(String subject) {		this.subject = subject;	}
	public String getChannel() {		return this.channel;	}
	public void setChannel(String channel) {		this.channel = channel;	}
	public String getOpen_id() {		return this.open_id;	}
	public void setOpen_id(String open_id) {		this.open_id = open_id;	}
	
}

