package com.kongcv.global;

import java.util.ArrayList;
public class JpushBeanAndInfoBean {
	public JpushBean jpushBean;
	public ArrayList<InfoBean> infoList;
	public JpushBeanAndInfoBean(JpushBean jpushBean,ArrayList<InfoBean> mList){
		this.jpushBean=jpushBean;
		this.infoList=mList;
	}
}