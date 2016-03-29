/**
  * Copyright 2016 aTool.org 
  */
package com.kongcv.global;
/**
 * Auto-generated: 2016-01-11 12:7:18
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class Result {

    private ParkCommunity parkCommunity;
    private String comment;
    private double grade;
    private String user;
    private String objectid;
    private String createdAt;
    private String updatedat;
    public void setParkCommunity(ParkCommunity parkCommunity) {
         this.parkCommunity = parkCommunity;
     }
     public ParkCommunity getParkCommunity() {
         return parkCommunity;
     }

    public void setComment(String comment) {
         this.comment = comment;
     }
     public String getComment() {
         return comment;
     }

    public void setGrade(double grade) {
         this.grade = grade;
     }
     public double getGrade() {
         return grade;
     }

    public void setUser(String user) {
         this.user = user;
     }
     public String getUser() {
         return user;
     }

    public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public void setObjectid(String objectid) {
         this.objectid = objectid;
     }
     public String getObjectid() {
         return objectid;
     }

    public void setUpdatedat(String updatedat) {
         this.updatedat = updatedat;
     }
     public String getUpdatedat() {
         return updatedat;
     }

}