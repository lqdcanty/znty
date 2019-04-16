/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.util.weichat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.efida.sports.util.CommonUtil;

/**
 * 微信登录util
 * @author zoutao
 * @version $Id: WeichatAuthUtil.java, v 0.1 2018年2月24日 下午3:52:32 zoutao Exp $
 */
public class WeichatAuthUtil {
    private static Logger log = LoggerFactory.getLogger(WeichatAuthUtil.class);

    public static WeiChatToken getAccessToken(String appId, String appSecret, String code) {
        log.info("获得用户登录的AccessToken");
        WeiChatToken token = null;
        // 拼接请求地址
        String requestUrl = String.format(
            "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
            appId, appSecret, code);
        log.info("请求地址:" + requestUrl);
        // 获取网页授权凭证
        JSONObject jsonObject = CommonUtil.httpGet(requestUrl);
        if (null != jsonObject) {
            try {
                token = new WeiChatToken();
                token.setAccessToken(jsonObject.getString("access_token"));
                token.setExpiresIn(jsonObject.getInteger("expires_in"));
                token.setRefreshToken(jsonObject.getString("refresh_token"));
                token.setOpenId(jsonObject.getString("openid"));
                token.setScope(jsonObject.getString("scope"));
            } catch (Exception e) {
                token = null;
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                log.error("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return token;
    }
}
