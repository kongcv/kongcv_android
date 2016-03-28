package com.kongcv.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 缓存工具类
 * 
 * @author kcw001
 * 
 */
public class ConfigCacheUtil {

	/**
	 * 保存图片到本地,这个是把图片压缩成字节流然后保存到本地，所以本地的图片是无法显示的
	 * 
	 */
	public static void saveBitmap(Bitmap mBitmap, String imageURL, Context cxt) {

		String bitmapName = imageURL.substring(imageURL.lastIndexOf("/") + 1); // 传入一个远程图片的url，然后取最后的图片名字

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try {
			fos = cxt.openFileOutput(bitmapName, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(byteArray);
		} catch (Exception e) {
			e.printStackTrace();
			// 这里是保存文件产生异常
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// fos流关闭异常
					e.printStackTrace();
				}
			}
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					// oos流关闭异常
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 读取本地私有文件夹的图片
	 */
	public static Bitmap getBitmap(String fileName, Context cxt) {
		String bitmapName = fileName.substring(fileName.lastIndexOf("/") + 1);
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = cxt.openFileInput(bitmapName);
			ois = new ObjectInputStream(fis);
			byte[] byteArray = (byte[]) ois.readObject();
			Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
					byteArray.length);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			// 这里是读取文件产生异常
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// fis流关闭异常
					e.printStackTrace();
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					// ois流关闭异常
					e.printStackTrace();
				}
			}
		}
		// 读取产生异常，返回null
		return null;
	}

	// 通过这种方式保存在本地的图片，是可以看到的
	public static void saveBitmap2(Bitmap mBitmap, String imageURL, Context cxt) {

		String bitmapName = imageURL.substring(imageURL.lastIndexOf("/") + 1);

		FileOutputStream fos = null;

		try {
			fos = cxt.openFileOutput(bitmapName, Context.MODE_PRIVATE);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			// 这里是保存文件产生异常
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// fos流关闭异常
					e.printStackTrace();
				}
			}
		}
	}
	
	//获取图片
	public static Bitmap getBitmap2(String fileName, Context cxt) {
		String bitmapName = fileName.substring(fileName.lastIndexOf("/") + 1);
		FileInputStream fis = null;
		try {
			fis = cxt.openFileInput(bitmapName);
			byte[] b = new byte[fis.available()];
			fis.read(b);
			fis.close();
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			// 这里是读取文件产生异常
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// fis流关闭异常
					e.printStackTrace();
				}
			}
		}
		// 读取产生异常，返回null
		return null;
	}

	/**
	 * 判断本地的私有文件夹里面是否存在当前名字的文件
	 */
	public static boolean isFileExist(String fileName, Context cxt) {
		String bitmapName = fileName.substring(fileName.lastIndexOf("/") + 1);
		List<String> nameLst = Arrays.asList(cxt.fileList());
		if (nameLst.contains(bitmapName)) {
			return true;
		} else {
			return false;
		}

	}
}
