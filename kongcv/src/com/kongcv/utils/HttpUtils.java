package com.kongcv.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * 此工具类是用来获取json数据连接和数据解析的
 * @author kcw001
 *
 */
public class HttpUtils {
	
	//获取json数据连接
    public static String getJsonContent(String urlStr)
    {
        try
        {// 获取HttpURLConnection连接对象
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = (HttpURLConnection) url
                    .openConnection();
            // 设置连接属性
            httpConn.setConnectTimeout(6000);
            httpConn.setDoInput(true);
            httpConn.setRequestMethod("GET");
            // 获取相应码
            int respCode = httpConn.getResponseCode();
            if (respCode == 200)
            {
                return ConvertStream2Json(httpConn.getInputStream());
            }
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "网络连接失败";
    }

   //输入流的方式返回json字符串
    private static String ConvertStream2Json(InputStream inputStream)
    {
        String jsonStr = "";
        // ByteArrayOutputStream相当于内存输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        // 将输入流转移到内存输出流中
        try
        {
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1)
            {
                out.write(buffer, 0, len);
            }
            // 将内存流转换为字符串
            jsonStr = new String(out.toByteArray());
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonStr;
    }
    
    //以post方式获取Http服务器 上的数据，获取数据时，附加http头等验证信息
    public void HttpPostData() { 
		try {
		    HttpClient httpclient = new DefaultHttpClient();
		    String uri="";//待传的值
		    HttpPost httppost = new HttpPost(uri);
		    //添加http头信息
		    httppost.addHeader("Authorization", "your token"); //认证token
		    httppost.addHeader("Content-Type", "application/json");
		    httppost.addHeader("User-Agent", "imgfornote");
		    //http post的json数据格式：  {"name": "your name","parentId": "id_of_parent"}
		    JSONObject obj = new JSONObject();
		    obj.put("name", "your name");
		    obj.put("parentId", "your parentid");
		    httppost.setEntity(new StringEntity(obj.toString()));  
		    HttpResponse response;
		    response = httpclient.execute(httppost);
		    //检验状态码，如果成功接收数据
		    int code = response.getStatusLine().getStatusCode();
		    if (code == 200) {
		        String rev = EntityUtils.toString(response.getEntity());//返回json格式： {"id": "27JpL~j4vsL0LX00E00005","version": "abc"}      
		        obj = new JSONObject(rev);
		        String id = obj.getString("id");
		        String version = obj.getString("version");
		    }
		    } catch (ClientProtocolException e) {  
		    } catch (IOException e) {  
		    } catch (Exception e) {
		    }
		}
  //以get方式获取Http服务器 上的数据，获取数据时，附加http头等验证信息
    public void HttpGetData() {  
        try {  
            HttpClient httpclient = new DefaultHttpClient();  
            String uri = "http://www.yourweb.com";   
            HttpGet get = new HttpGet(uri);  
            //添加http头信息    
            get.addHeader("Authorization", "your token ");  
            get.addHeader("Content-Type", "application/json");  
            get.addHeader("User-Agent","your agent");
            JSONObject obj = new JSONObject();
            HttpResponse response;  
            response = httpclient.execute(get);  
            int code = response.getStatusLine().getStatusCode();  
            //检验状态码，如果成功接收数据   
            if (code == 200) {  
                //返回json格式： {"id": "27JpL~j4vsL0LX00E00005","version": "abc"}           
                String rev = EntityUtils.toString(response.getEntity());          
                obj = new JSONObject(rev);    
                String id = obj.getString("id");    
                String version = obj.getString("version");    
            }  
        } catch (Exception e) {  
        	
        }  
    }  
    
    
    /**
	 * 判断是否有网络连接
	 * @param context
	 * @return
	 */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
 // 判断WIFI网络是否可用的方法
 	public boolean isOpenWifi(Context context) {
 		ConnectivityManager connManager = (ConnectivityManager) context
 				.getSystemService(Context.CONNECTIVITY_SERVICE);
 		NetworkInfo mWifi = connManager
 				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
 		return mWifi.isConnected();
 	}
   
}
