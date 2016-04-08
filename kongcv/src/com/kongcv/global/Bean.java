package com.kongcv.global;

import java.io.Serializable;
import java.util.List;

import com.amap.api.maps.model.LatLng;

public class Bean implements Serializable{	private static final long serialVersionUID = 959214212L;	private List<String> addressList;	private List<String> objList;
	private List<LatLng> latLngList;
	private List<String> park_id;
	private List<Integer> restList;//是否出租
	private List<String> priceList;
	/**
	 * @return the addressList
	 */
	public List<String> getAddressList() {
		return addressList;
	}
	/**
	 * @param addressList the addressList to set
	 */
	public void setAddressList(List<String> addressList) {
		this.addressList = addressList;
	}
	/**
	 * @return the objList
	 */
	public List<String> getObjList() {
		return objList;
	}
	/**
	 * @param objList the objList to set
	 */
	public void setObjList(List<String> objList) {
		this.objList = objList;
	}
	/**
	 * @return the latLngList
	 */
	public List<LatLng> getLatLngList() {
		return latLngList;
	}
	/**
	 * @param latLngList the latLngList to set
	 */
	public void setLatLngList(List<LatLng> latLngList) {
		this.latLngList = latLngList;
	}
	/**
	 * @return the restList
	 */
	public List<Integer> getRestList() {
		return restList;
	}
	/**
	 * @param restList the restList to set
	 */
	public void setRestList(List<Integer> restList) {
		this.restList = restList;
	}
	
	/*public List<Integer> getPriceList() {
		return priceList;
	}
	public void setPriceList(List<Integer> priceList) {
		this.priceList = priceList;
	}*/
	/**
	 * @return the priceList
	 */
	public List<String> getPriceList() {
		return priceList;
	}
	/**
	 * @param priceList the priceList to set
	 */
	public void setPriceList(List<String> priceList) {
		this.priceList = priceList;
	}
	/**
	 * @return the park_id
	 */
	public List<String> getPark_id() {
		return park_id;
	}
	/**
	 * @param park_id the park_id to set
	 */
	public void setPark_id(List<String> park_id) {
		this.park_id = park_id;
	}
	
		}
