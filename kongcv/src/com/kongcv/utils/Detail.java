package com.kongcv.utils;

public class Detail {
	
	// location地上地下
	private Double High,Area;
	private int CarAddress;
	private Boolean CarRight;							//门禁卡
	private String Address, TimeStart, TimeEnd, CarNum, CarCard, CarDescribe;

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getTimeStart() {
		return TimeStart;
	}

	public void setTimeStart(String timeStart) {
		TimeStart = timeStart;
	}

	public String getTimeEnd() {
		return TimeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		TimeEnd = timeEnd;
	}

	public Double getArea() {
		return Area;
	}

	public void setArea(Double area) {
		Area = area;
	}

	public Double getHigh() {
		return High;
	}

	public void setHigh(Double high) {
		High = high;
	}

	public String getCarNum() {
		return CarNum;
	}

	public void setCarNum(String carNum) {
		CarNum = carNum;
	}

	public Boolean getCarRight() {
		return CarRight;
	}

	public void setCarRight(Boolean carRight) {
		CarRight = carRight;
	}

	public int getCarAddress() {
		return CarAddress;
	}

	public void setCarAddress(int carAddress) {
		CarAddress = carAddress;
	}

	public String getCarCard() {
		return CarCard;
	}

	public void setCarCard(String carCard) {
		CarCard = carCard;
	}

	public String getCarDescribe() {
		return CarDescribe;
	}

	public void setCarDescribe(String carDescribe) {
		CarDescribe = carDescribe;
	}

	@Override
	public String toString() {
		return "DetailUtil [Address=" + Address + ", TimeStart=" + TimeStart
				+ ", TimeEnd=" + TimeEnd + ", Area=" + Area + ", High=" + High
				+ ", CarNum=" + CarNum + ", CarRight=" + CarRight
				+ ", CarAddress=" + CarAddress + ", CarCard=" + CarCard
				+ ", CarDescribe=" + CarDescribe + "]";
	}

	
}
