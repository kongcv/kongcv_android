package com.kongcv.global;

import android.graphics.Bitmap;

public class CardTypeBean {
	private String bank;
	private Bitmap bitmap;
	private String url;
	private String card;
	private String name;
	private String money;
	
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap2) {
		this.bitmap = bitmap2;
	}
	
	@Override
	public String toString() {
		return "CardTypeBean [bank=" + bank + ", bitmap=" + bitmap + "]";
	}

}
