package com.kongcv.utils;

import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存取数据
 */
public class Data {  
	
    private static AbstractMap<String, Object> mData = new ConcurrentHashMap<String, Object>();  
    private Data() {  
    }  
    public static void putData(String key,Object obj) {  
        mData.put(key, obj);  
    }  
    public static Object getData(String key) {  
        return mData.get(key);  
    }  
}  
