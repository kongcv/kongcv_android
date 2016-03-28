package com.kongcv.global;

public class TimeBean {

	private String Hour;
	private String Minute;
	/**
	 * @return the hour
	 */
	public String getHour() {
		return Hour;
	}
	/**
	 * @param hour the hour to set
	 */
	public void setHour(String hour) {
		Hour = hour;
	}
	/**
	 * @return the minute
	 */
	public String getMinute() {
		return Minute;
	}
	/**
	 * @param minute the minute to set
	 */
	public void setMinute(String minute) {
		Minute = minute;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TimeBean [Hour=" + Hour + ", Minute=" + Minute + "]";
	}
	
	
}
