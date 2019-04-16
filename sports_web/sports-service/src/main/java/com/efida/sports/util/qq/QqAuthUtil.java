package com.efida.sports.util.qq;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.efida.sports.util.HttpUtils;

public class QqAuthUtil {
	private static Logger log = LoggerFactory.getLogger(QqAuthUtil.class);
	private static final String QQ_OAUTH_URI = "https://graph.qq.com/oauth2.0/token";
	private static final String QQ_OPENID_URI = "https://graph.qq.com/oauth2.0/me";
	private static final String QQ_USER_INFO_URI = "https://graph.qq.com/user/get_user_info";

	/**
	 * 获取网页授权凭证
	 * 
	 * @param appId
	 *            公众账号的唯一标识
	 * @param appSecret
	 *            公众账号的密钥
	 * @param code
	 * @return WeixinAouth2Token
	 */
	public static QqOauth2Token getQqOauth2AccessToken(String appId, String appSecret, String code,
			String redirectUri) {
		log.info("获得QQ用户登录的AccessToken");
		QqOauth2Token qt = null;
		// 获取网页授权凭证
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("grant_type", "authorization_code");
		params.put("client_id", appId);
		params.put("client_secret", appSecret);
		params.put("code", code);
		params.put("redirect_uri", redirectUri);
		String resultStr = HttpUtils.executeGet(QQ_OAUTH_URI, params);
		Map<String, String> data = analysisRseultAccessToken(resultStr);
		if (null != data) {
			qt = new QqOauth2Token();
			qt.setAppId(appId);
			qt.setAccessToken(data.get("access_token"));
			qt.setExpiresIn(Integer.parseInt(data.get("expires_in")));
			qt.setRefreshToken(data.get("refresh_token"));
			qt.setOpenId(getOpenId(data.get("access_token")));
		}
		return qt;
	}

	/**
	 * 获取QQ用户OPENID
	 * 
	 * @param accessToken
	 */
	public static String getOpenId(String accessToken) {
		log.info("获得QQ用户的OpenId");
		// 拼接请求地址
		String openId = "";
		// 获取openId
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("access_token", accessToken);
		String resultStr = HttpUtils.executeGet(QQ_OPENID_URI, params);
		JSONObject jsonObject = str2JsonObject(resultStr);
		if (null != jsonObject) {
			try {
				openId = jsonObject.getString("openid");
			} catch (Exception e) {
				int errorCode = jsonObject.getInteger("error");
				String errorMsg = jsonObject.getString("error_description");
				log.error("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg, e);
			}
		}
		return openId;
	}

	/**
	 * 通过网页授权获取用户信息
	 * 
	 * @param accessToken
	 *            网页授权接口调用凭证
	 * @param openId
	 *            用户标识
	 * @return UserInfoModel
	 */
	public static JSONObject accessToken2UserInfo(QqOauth2Token token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("access_token", token.getAccessToken());
		params.put("oauth_consumer_key", token.getAppId());
		params.put("openid", token.getOpenId());
		String resultStr = HttpUtils.executeGet(QQ_USER_INFO_URI, params);
		return JSONObject.parseObject(resultStr);
	}

	/**
	 * 处理获取accessToken返回字符串
	 * 
	 * @param returnStr
	 * @return
	 */
	private static Map<String, String> analysisRseultAccessToken(String strResult) {
		Map<String, String> data = new HashMap<String, String>();
		JSONObject jsonObject = str2JsonObject(strResult);
		if (null != jsonObject) {
			int errorCode = jsonObject.getInteger("error");
			String errorMsg = jsonObject.getString("error_description");
			log.error("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg);
			return null;
		}
		String[] split = strResult.split("&");
		for (String str : split) {
			String[] str1 = str.split("=");
			data.put(str1[0], str1[1]);
		}
		return data;
	}

	/**
	 * 将字符串装换成json
	 * 
	 * @param strResult
	 * @return
	 */
	private static JSONObject str2JsonObject(String strResult) {
		if (strResult.startsWith("callback")) {
			String replace = strResult.replace("callback(", "");
			String jsonStr = replace.substring(0, replace.length() - 2);
			JSONObject jsonResult = JSONObject.parseObject(jsonStr);
			return jsonResult;
		}
		return null;
	}
}
