/**
  * Copyright 2016 aTool.org 
  */
package com.kongcv.global;
import java.util.Date;
import java.util.List;
/**
 * Auto-generated: 2016-01-07 10:18:55
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class PublishBean {

    private String user_id;
    private String worker_id;
    private String address;
    private String park_detail;
    private String park_description;
    private LocationInfo location_info;
    private String hire_start;
    private String hire_end;
    private List<String> no_hire;
    private String tail_num;
    private String city;
    private boolean normal;
    
    private double park_area;
    private double park_height;
    private String gate_card;
    private List<String> hire_method_id;
    private List<String> hire_price;
    private List<String> hire_time;
    private List<String> hire_field;
    private int park_struct;
    private int personal;
    private String mode;
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return user_id;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.user_id = userId;
	}
	/**
	 * @return the workerId
	 */
	public String getWorkerId() {
		return worker_id;
	}
	/**
	 * @param workerId the workerId to set
	 */
	public void setWorkerId(String workerId) {
		this.worker_id = workerId;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the parkDetail
	 */
	public String getParkDetail() {
		return park_detail;
	}
	/**
	 * @param parkDetail the parkDetail to set
	 */
	public void setParkDetail(String parkDetail) {
		this.park_detail = parkDetail;
	}
	/**
	 * @return the parkDescription
	 */
	public String getParkDescription() {
		return park_description;
	}
	/**
	 * @param parkDescription the parkDescription to set
	 */
	public void setParkDescription(String parkDescription) {
		this.park_description = parkDescription;
	}
	/**
	 * @return the locationInfo
	 */
	public LocationInfo getLocationInfo() {
		return location_info;
	}
	/**
	 * @param locationInfo the locationInfo to set
	 */
	public void setLocationInfo(LocationInfo locationInfo) {
		this.location_info = locationInfo;
	}
	/**
	 * @return the hireStart
	 */
	public String getHireStart() {
		return hire_start;
	}
	/**
	 * @param hireStart the hireStart to set
	 */
	public void setHireStart(String hireStart) {
		this.hire_start = hireStart;
	}
	/**
	 * @return the hireEnd
	 */
	public String getHireEnd() {
		return hire_end;
	}
	/**
	 * @param hireEnd the hireEnd to set
	 */
	public void setHireEnd(String hireEnd) {
		this.hire_end = hireEnd;
	}
	/**
	 * @return the noHire
	 */
	public List<String> getNoHire() {
		return no_hire;
	}
	/**
	 * @param list the noHire to set
	 */
	public void setNoHire(List<String> list) {
		this.no_hire = list;
	}
	/**
	 * @return the tailNum
	 */
	public String getTailNum() {
		return tail_num;
	}
	/**
	 * @param tailNum the tailNum to set
	 */
	public void setTailNum(String tailNum) {
		this.tail_num = tailNum;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the normal
	 */
	public boolean isNormal() {
		return normal;
	}
	/**
	 * @param normal the normal to set
	 */
	public void setNormal(boolean normal) {
		this.normal = normal;
	}
	/**
	 * @return the parkArea
	 */
	public double getParkArea() {
		return park_area;
	}
	/**
	 * @param parkArea the parkArea to set
	 */
	public void setParkArea(double parkArea) {
		this.park_area = parkArea;
	}
	/**
	 * @return the parkHeight
	 */
	public double getParkHeight() {
		return park_height;
	}
	/**
	 * @param parkHeight the parkHeight to set
	 */
	public void setParkHeight(double parkHeight) {
		this.park_height = parkHeight;
	}
	/**
	 * @return the gateCard
	 */
	public String getGateCard() {
		return gate_card;
	}
	/**
	 * @param gateCard the gateCard to set
	 */
	public void setGateCard(String gateCard) {
		this.gate_card = gateCard;
	}
	/**
	 * @return the hireMethodId
	 */
	public List<String> getHireMethodId() {
		return hire_method_id;
	}
	/**
	 * @param hireMethodId the hireMethodId to set
	 */
	public void setHireMethodId(List<String> hireMethodId) {
		this.hire_method_id = hireMethodId;
	}
	/**
	 * @return the hirePrice
	 */
	public List<String> getHirePrice() {
		return hire_price;
	}
	/**
	 * @param hirePrice the hirePrice to set
	 */
	public void setHirePrice(List<String> hirePrice) {
		this.hire_price = hirePrice;
	}
	/**
	 * @return the hireTime
	 */
	public List<String> getHireTime() {
		return hire_time ;
	}
	/**
	 * @param hireTime the hireTime to set
	 */
	public void setHireTime(List<String> hireTime) {
		this.hire_time  = hireTime;
	}
	
	/**
	 * @return the park_struct
	 */
	public int getPark_struct() {
		return park_struct;
	}
	/**
	 * @param park_struct the park_struct to set
	 */
	public void setPark_struct(int park_struct) {
		this.park_struct = park_struct;
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
	public List<String> getHire_field() {
		return hire_field;
	}
	public void setHire_field(List<String> hire_field) {
		this.hire_field = hire_field;
	}
	/**
	 * @return the personal
	 */
	public int getPersonal() {
		return personal;
	}
	/**
	 * @param personal the personal to set
	 */
	public void setPersonal(int personal) {
		this.personal = personal;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PublishBean [user_id=" + user_id + ", worker_id=" + worker_id
				+ ", address=" + address + ", park_detail=" + park_detail
				+ ", park_description=" + park_description + ", location_info="
				+ location_info + ", hire_start=" + hire_start + ", hire_end="
				+ hire_end + ", no_hire=" + no_hire + ", tail_num=" + tail_num
				+ ", city=" + city + ", normal=" + normal + ", park_area="
				+ park_area + ", park_height=" + park_height + ", gate_card="
				+ gate_card + ", hire_method_id=" + hire_method_id
				+ ", hire_price=" + hire_price + ", hire_time=" + hire_time
				+ ", hire_field=" + hire_field + ", park_struct=" + park_struct
				+ ", personal=" + personal + ", mode=" + mode + "]";
	}

}





