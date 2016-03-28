package com.kongcv.global;

import java.io.Serializable;

public class CommentBean implements Serializable{	private static final long serialVersionUID = 1689555682L;	private String user_id;	private String comment;	private String park_id;	private long grade;	private String mode;
	public String getUser_id() {		return this.user_id;	}
	public void setUser_id(String user_id) {		this.user_id = user_id;	}
	public String getComment() {		return this.comment;	}
	public void setComment(String comment) {		this.comment = comment;	}
	public String getPark_id() {		return this.park_id;	}
	public void setPark_id(String park_id) {		this.park_id = park_id;	}
	public long getGrade() {		return this.grade;	}
	public void setGrade(long grade) {		this.grade = grade;	}
	public String getMode() {		return this.mode;	}
	public void setMode(String mode) {		this.mode = mode;	}

	public String toString() {
		return "CommentBean [user_id = " + user_id + ", comment = " + comment + ", park_id = " + park_id + ", grade = " + grade + ", mode = " + mode + "]";	}
}
