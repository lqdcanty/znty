package com.efida.sports.util.weibo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 
 * @author lijiaqi
 * @date 2014年12月9日
 * @version 1.0.0
 * @description desc
 */
public class UrlEnCodeAndDecodeUtil {

	
	/**
	 * 编码
	 * @param str
	 * @return
	 */
	public static String encodeurl(String str){
		if (str == null) {
			return null;
		}
		String result = null;
		try {
			result = URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	/**
	 * 解码
	 * @param str
	 * @return
	 */
	public static String decodeurl(String str){
		if (str == null) {
			return null;
		}
		String result = null;
		try {
			result = URLDecoder.decode(str, "utf-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}
}
