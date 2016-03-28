package com.kongcv.ImageRun;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * 根据url下载图片
 * 
 * @author kcw
 * 
 */
public class GetImage {
	public static Bitmap getImage(String Url) throws Exception {
		URL url = new URL(Url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			InputStream inputStream = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			return bitmap;
		}
		return null;
	}

	public static Drawable resizeImage(Bitmap bitmap, int w, int h) {
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);

		return new BitmapDrawable(resizedBitmap);

	}
	/**
     * 根据URL获取Bitmap
     * */
    public static Bitmap getHttpBitmap(String url){
            Bitmap bitmap=null;
            URL myUrl;
            try {
                    myUrl=new URL(url);
                    HttpURLConnection conn=(HttpURLConnection)myUrl.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.connect();
                    InputStream is=conn.getInputStream();
                    bitmap=BitmapFactory.decodeStream(is);
                    //把bitmap转成圆形
                    bitmap=toRoundBitmap(bitmap);
                    is.close();
            } catch (MalformedURLException e) {
                    e.printStackTrace();
            } catch (IOException e) {
                    e.printStackTrace();
            }
            //返回圆形bitmap
            return bitmap;
    }
    /**
     * 把bitmap转成圆形
     * */
    public static Bitmap toRoundBitmap(Bitmap bitmap){
            int width=bitmap.getWidth();
            int height=bitmap.getHeight();
            int r=0;
            //取最短边做边长
            if(width<height){
                    r=width;
            }else{
                    r=height;
            }
            //构建一个bitmap
            Bitmap backgroundBm=Bitmap.createBitmap(width,height,Config.ARGB_8888);
            //new一个Canvas，在backgroundBmp上画图 
            Canvas canvas=new Canvas(backgroundBm);
            Paint p=new Paint();
            //设置边缘光滑，去掉锯齿 
            p.setAntiAlias(true);
            RectF rect=new RectF(0, 0, r, r);
            //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，  
            //且都等于r/2时，画出来的圆角矩形就是圆形 
            canvas.drawRoundRect(rect, r/2, r/2, p);
            //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
            p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            //canvas将bitmap画在backgroundBmp上
            canvas.drawBitmap(bitmap, null, rect, p);
            return backgroundBm;
    }

}
