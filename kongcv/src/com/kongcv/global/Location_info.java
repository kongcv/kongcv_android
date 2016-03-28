package com.kongcv.global;

import java.io.Serializable;

public class Location_info implements Serializable{
	private static final long serialVersionUID = 1848425001L;
	private double longitude;
	private double latitude;

	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

}
