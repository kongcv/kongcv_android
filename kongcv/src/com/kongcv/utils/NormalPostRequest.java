package com.kongcv.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.kongcv.global.Information;

public class NormalPostRequest extends Request<JSONObject> {
	
	private Listener<JSONObject> mListener;
	
	private Map<String, String> mMap;
	public NormalPostRequest(String url, Listener<JSONObject> listener,
			ErrorListener errorListener, Map<String, String> map) {
		super(Request.Method.POST, url, errorListener);
		mListener = listener;
		mMap = map;
	}
	// mMap是已经按照前面的方式,设置了参数的实例
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mMap;
	}
	
	/*private Map<String, String> mMap;
	public NormalPostRequest(String url, Listener<JSONObject> listener,
			ErrorListener errorListener, Map<String, String> map) {
		super(Request.Method.POST, url, errorListener);
		mListener = listener;
		mMap = map;
	}
	public Map<String, String> getmMap() {
		return mMap;
	}
	*/
	
	// 此处因为response返回值需要json数据,和JsonObjectRequest类一样即可
	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		String jsonString=null;
		try {
			jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			Log.v("JSONException>>>>>", jsonString+"::::");
			Log.v("JSONException>>>>>", jsonString+"::::");
			return Response.error(new ParseError(je));
		}
	}
	

	@Override
	protected void deliverResponse(JSONObject response) {
		mListener.onResponse(response);
	}
	
	/**
	 * 设置属性参数
	 */
	@Override
	protected Map<String, String> getPostParams() throws AuthFailureError {
		Map<String, String> params = new HashMap<String, String>();
		params.put("Content-Type", "application/json;charset=utf-8");
		params.put("Accept", "application/json");
		return params;
	}
	/**
	 * 添加请求头信息
	 */
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		// TODO Auto-generated method stub
		Map<String, String> headers = new HashMap<String, String>();  
	    headers.put("X-LC-Id", Information.APP_ID); 
	    headers.put("X-LC-Key", Information.APP_KEY);    
		return headers;  
	}
}
















