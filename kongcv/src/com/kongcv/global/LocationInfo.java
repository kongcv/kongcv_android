/**
  * Copyright 2016 aTool.org 
  */
package com.kongcv.global;

import java.io.Serializable;

/**
 * Auto-generated: 2016-01-07 10:18:55
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class LocationInfo implements Serializable{

	private static final long serialVersionUID = 3593573094445487940L;
	private String __type;
    private double latitude;
    private double longitude;
    public void set_type(String _type) {
         this.__type = _type;
     }
     public String get_type() {
         return __type;
     }

    public void setLatitude(double latitude) {
         this.latitude = latitude;
     }
     public double getLatitude() {
         return latitude;
     }

    public void setLongitude(double longitude) {
         this.longitude = longitude;
     }
     public double getLongitude() {
         return longitude;
     }

}