package com.kongcv.global;

import java.util.ArrayList;
import java.util.List;

public class JpushBeanAndInfoBean {

	public ArrayList<JpushBean> jpushBean;
	public ArrayList<InfoBean> infoList;

	public JpushBeanAndInfoBean(ArrayList<JpushBean> jpushBean,
			ArrayList<InfoBean> mList) {
		this.jpushBean = jpushBean;
		this.infoList = mList;
	}
}