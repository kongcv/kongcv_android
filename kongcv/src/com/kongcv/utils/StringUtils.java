package com.kongcv.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.kongcv.fragment.PublishFragment;

import android.text.TextUtils;


public class StringUtils {

    /**
     * will trim the string
     * 
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (null == s) return true;
        if (s.length() == 0) return true;
        if (s.trim().length() == 0) return true;
        return false;
    }
    
    public static String emptyStringIfNull(String s) {
        return (null == s) ? "" : s;
    }

    public static String humanReadableByteCount(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "KMGTPE".charAt(exp-1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
    
    public static String fixedLengthString(String s, int expectedLength) {
        int l = s.length();
        if (l >= expectedLength) {
            return s;
            //return s.substring(0, expectedLength);
        }
        for (int i = 0; i < expectedLength - l; i++) {
            s = s + " ";
        }
        return s;
    }
    
    public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
    
    public static String toMD5(String source) {
        if (null == source || "".equals(source)) return null;
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(source.getBytes());
            return toHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String toHex(byte[] buf) {
        if (buf == null) return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }
    
    private final static String HEX = "0123456789ABCDEF";
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    /**
	 * 验证手机号码和验证码位数
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNo(String mobiles) {
		String telRegex = "[1][358]\\d{9}";
		return TextUtils.isEmpty(mobiles) ? false : mobiles.matches(telRegex);
	}

	public static boolean isCodeNo(String code) {
		String telRegex = "^\\d{6,}$";
		return TextUtils.isEmpty(code) ? false : code.matches(telRegex);
	}
	
	/**
	 * JAVA判断字符串数组中是否包含某字符串元素
	 * @param substring
	 *            某字符串
	 * @param source
	 *            源字符串数组
	 * @return 包含则返回true，否则返回false
	 */
	public static String isIn(String substring, String[] days) {
		if (days == null || days.length == 0) {
			return null;
		}
		for (int i = 0; i < days.length; i++) {
			String aSource = days[i];
			if (aSource.equals(substring)) {
				return i + 1 + "";
			}
		}
		return null;
	}
	
	/**
	 * 数组转字符串
	 * @param stringList
	 * @return
	 */
	public static String listToString(List<String> stringList){
        if (stringList==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
           /* int i = Integer.parseInt(string)-1;
            string=PublishFragment.days[i];		
            result.append(string);  替换*/
            result.append(string);
        }
        return result.toString();
    }
	
	/**
	 * 繁体字
	 */
	public static List<String> isNumber(String s){
		 List<String> ss = new ArrayList<String>();
		    for(String sss:s.replaceAll("[^0-9]", ",").split(",")){
		        if (sss.length()>0){
		        	ss.add(sss);
		        }
		    }
		    return ss;
	}
	public static boolean isNum(String ss) {
		String str = ss.replaceAll("\\D", "");
		int parseInt = Integer.parseInt(str);
		if (parseInt != 0) {
			return true;
		} else {
			return false;
		}
	}
}
