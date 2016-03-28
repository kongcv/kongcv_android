package com.kongcv.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonStrUtils {

	/**
	 * 此类用于 转换json字符串
	 */
	public static String JsonStr(JSONObject obj) {
		String jsonStr = String.valueOf(obj);
		return jsonStr;
	}

	/**
	 * 生成json字符串
	 */
	public static String createJsonString(String key, Object value) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(key, value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
	/**
	 * str转化jsonobject
	 */
	public static JSONObject strToJson(String str){
		
		if(str!=null){
			try {
				JSONObject object=new JSONObject(str);
				return object;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
