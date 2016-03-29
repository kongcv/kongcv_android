package com.kongcv.utils;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyStore;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.kongcv.SSL.MyInitHttpClient;
import com.kongcv.SSL.SSLSocketFactoryImp;
import com.kongcv.global.Information;

public class PostCLientUtils {
	public static synchronized String doHttpsPost(String serverURL, String jsonStr)throws Exception {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));

		SSLSocketFactory sf = SSLSocketFactory.getSocketFactory();
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);
			sf = new SSLSocketFactoryImp(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			// 允许所有主机的验证
		} catch (Exception e) {
			Log.e("erro", "SSLSocketFactory Error");
		}

		schemeRegistry.register(new Scheme("https", sf, 443));
        // 参数
        HttpParams httpParameters = new BasicHttpParams();
        // 设置连接超时
        HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
        // 设置socket超时
        HttpConnectionParams.setSoTimeout(httpParameters, 5000);
        // 获取HttpClient对象 （认证）
        HttpClient hc = MyInitHttpClient.initHttpClient(httpParameters);
        HttpPost post = new HttpPost(serverURL);
        
        post.setHeader("X-LC-Id", Information.APP_ID);
        post.setHeader("X-LC-Key", Information.APP_KEY);
        
    //    post.setHeader("X-AVOSCloud-Session-Token", "x4l0rxcin5zc9h36o4veyadkf");
        post.setHeader("Content-Type", "application/json;charset=utf-8");
        // 接受数据类型
        post.addHeader("Accept", "application/json");
        // 请求报文
        StringEntity entity = new StringEntity(jsonStr, "UTF-8");
        post.setEntity(entity);
        post.setParams(httpParameters);
        HttpResponse response = null;
        try {
            response = hc.execute(post);
        } catch (UnknownHostException e) {
            throw new Exception("Unable to access " + e.getLocalizedMessage());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        int sCode = response.getStatusLine().getStatusCode();
        if (sCode == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity());
        } else
            throw new Exception("StatusCode is " + sCode);
    }
	
	public static synchronized String doHttpsPost2(String serverURL, String jsonStr,String sessionToken)throws Exception {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));

		SSLSocketFactory sf = SSLSocketFactory.getSocketFactory();
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);
			sf = new SSLSocketFactoryImp(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			// 允许所有主机的验证
		} catch (Exception e) {
			Log.e("erro", "SSLSocketFactory Error");
		}

		schemeRegistry.register(new Scheme("https", sf, 443));
        // 参数
        HttpParams httpParameters = new BasicHttpParams();
        // 设置连接超时
        HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
        // 设置socket超时
        HttpConnectionParams.setSoTimeout(httpParameters, 3000);
        // 获取HttpClient对象 （认证）
        HttpClient hc = MyInitHttpClient.initHttpClient(httpParameters);
        HttpPost post = new HttpPost(serverURL);
        
        post.setHeader("X-LC-Id", Information.APP_ID);
        post.setHeader("X-LC-Key", Information.APP_KEY);
   
        post.setHeader("X-AVOSCloud-Session-Token", sessionToken);
  //      post.setHeader("X-AVOSCloud-Session-Token", "x4l0rxcin5zc9h36o4veyadkf");
        post.setHeader("Content-Type", "application/json;charset=utf-8");
        // 接受数据类型
        post.addHeader("Accept", "application/json");
        // 请求报文
        StringEntity entity = new StringEntity(jsonStr, "UTF-8");
        post.setEntity(entity);
        post.setParams(httpParameters);
        HttpResponse response = null;
        try {
            response = hc.execute(post);
        } catch (UnknownHostException e) {
            throw new Exception("Unable to access " + e.getLocalizedMessage());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        int sCode = response.getStatusLine().getStatusCode();
        if (sCode == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity());
        } else
            throw new Exception("StatusCode is " + sCode);
    }
	
	public static synchronized String doHttpsPost4(String serverURL, String jsonStr,String sessionToken)throws Exception {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));

		SSLSocketFactory sf = SSLSocketFactory.getSocketFactory();
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);
			sf = new SSLSocketFactoryImp(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			// 允许所有主机的验证
		} catch (Exception e) {
			Log.e("erro", "SSLSocketFactory Error");
		}

		schemeRegistry.register(new Scheme("https", sf, 443));
        // 参数
        HttpParams httpParameters = new BasicHttpParams();
        // 设置连接超时
        HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
        // 设置socket超时
        HttpConnectionParams.setSoTimeout(httpParameters, 3000);
        // 获取HttpClient对象 （认证）
        HttpClient hc = MyInitHttpClient.initHttpClient(httpParameters);
        HttpPost post = new HttpPost(serverURL);
        
        post.setHeader("X-LC-Id", Information.APP_ID);
        post.setHeader("X-LC-Key", Information.APP_KEY);
        post.setHeader("X-AVOSCloud-Session-Token", sessionToken);
  //      post.setHeader("X-AVOSCloud-Session-Token", "x4l0rxcin5zc9h36o4veyadkf");
        post.setHeader("x-kongcv-key-signatures","80b5b4e822f60d0254d885a7c20f7a87");//支付请求
        post.setHeader("Content-Type", "application/json;charset=utf-8");
        // 接受数据类型
        post.addHeader("Accept", "application/json");
        // 请求报文
        StringEntity entity = new StringEntity(jsonStr, "UTF-8");
        post.setEntity(entity);
        post.setParams(httpParameters);
        HttpResponse response = null;
        try {
            response = hc.execute(post);
        } catch (UnknownHostException e) {
            throw new Exception("Unable to access " + e.getLocalizedMessage());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        int sCode = response.getStatusLine().getStatusCode();
        if (sCode == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity());
        } else
            throw new Exception("StatusCode is " + sCode);
    }
	
}
