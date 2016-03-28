package com.kongcv.UI.AsyncImageLoader;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class MyAsynaTask extends AsyncTask<String, Integer, Bitmap>{  
    
    private ImageView imageView;  
    public MyAsynaTask(ImageView imageView) {  
        super();  
        this.imageView = imageView;  
    }  
    
    @Override  
    protected void onPreExecute() {  
        // TODO Auto-generated method stub   
         
        /* 
         * 改方法在执行实际的后台操作时被UI线程调用，可以在该方法中做一些准备工作，比如 
         *  Toast.makeText(context, "准备下载", Toast.LENGTH_LONG).show(); 
         */  
        super.onPreExecute();  
    }  
  
    @Override  
    protected Bitmap doInBackground(String... params) {//输入编变长的可变参数 和ＵＩ线程中的Asyna.execute()对应   
        // TODO Auto-generated method stub   
    /* 
     * 该方法在OnpreExecute执行以后马上执行，改方法执行在后台线程当中，负责耗时的计算，可以调用publishProcess方法来实时更新任务进度 
     */  
        Bitmap bitmap=null;  
        try {  
             URL url=new URL(params[0]);  
             HttpURLConnection connection=(HttpURLConnection) url.openConnection();  
             connection.setReadTimeout(60000);
             connection.setDoInput(true);  
             connection.connect();  
              
             InputStream inputStream=connection.getInputStream();  
             bitmap=BitmapFactory.decodeStream(inputStream);  
             
             inputStream.close();  
              
        } catch (Exception e) {  
            // TODO: handle exception   
        }  
        return bitmap;  
    }  
  
    @Override  
    protected void onProgressUpdate(Integer... values) {  
        // TODO Auto-generated method stub   
        /* 
         * 当publichProcess 呗调用以后，ＵＩ线程将调用这个有方法在界面上展示任务的情况，比如一个额进度条。这里是更新进度条 
         */  
        super.onProgressUpdate(values);  
    }  
  
    @Override  
    protected void onPostExecute(Bitmap result) {  
        // TODO Auto-generated method stub   
        /* 
         * 在doInbackground执行完成以后，onPostExecute将被调用，后台的结果将返回给UI线程，将获得图片显示出来 
         */  
    	imageView.setImageBitmap(result);  
        super.onPostExecute(result);  
    }  
}

