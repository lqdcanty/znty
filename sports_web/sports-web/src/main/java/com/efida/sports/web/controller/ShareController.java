/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efida.sports.config.WeichatConfig;
import com.efida.sports.constants.Constants;
import com.efida.sports.service.CacheService;
import com.efida.sports.util.CommonUtil;
import com.efida.sports.util.JsonResultUtil;
import com.efida.sports.util.weichat.WeichatUtil;

/**
 * 
 * @author zoutao
 * @version $Id: ShareController.java, v 0.1 2018年6月1日 下午4:13:57 zoutao Exp $
 */
@Controller
public class ShareController {

    private static Logger log = LoggerFactory.getLogger(ShareController.class);

    @Autowired
    private CacheService  cacheService;
    @Autowired
    private WeichatConfig weichatConfig;

    private String getAccessToken() {
        String accessToken = null;
        try {

            accessToken = cacheService.get(Constants.ACCESS_TOKEN_KEY);
        } catch (Exception e) {
            log.error("从redis中获取accessToken错误,错误原因:{}", accessToken);
        }
        //如果存在直接返回
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }
        // 拼接请求地址
        String requestUrl = String.format(
            "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
            weichatConfig.appId, weichatConfig.appSecret);
        log.info("请求地址:" + requestUrl);
        JSONObject jsonObject = CommonUtil.httpGet(requestUrl);
        log.info("获取access_token返回结果" + jsonObject);
        accessToken = jsonObject.getString("access_token");
        if (StringUtils.isNotBlank(accessToken)) {
            try {
                cacheService.put(Constants.ACCESS_TOKEN_KEY, accessToken, 7000 * 1000);
            } catch (Exception e) {
                log.info("将accessToken放入redis中错误,错误原因:{}" + accessToken);
            }
        }
        return accessToken;
    }

    private String getJsApiTicket() {
        String accessToken = getAccessToken();
        String ticket = null;
        try {
            ticket = cacheService.get(Constants.JSAPI_TICKET_KEY);
        } catch (Exception e) {
            log.error("从redis中获取ticket失败,失败原因：{}", e);
        }
        if (StringUtils.isNotBlank(ticket)) {
            return ticket;
        }
        // 拼接请求地址
        String requestUrl = String.format(
            "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi",
            accessToken);
        log.info("getJsApiTicket.requestUrl====>" + requestUrl);
        JSONObject jsonObject = CommonUtil.httpGet(requestUrl);
        log.info("获取jsapi_ticket返回结构" + jsonObject);
        ticket = jsonObject.getString("ticket");
        if (StringUtils.isNotBlank(ticket)) {
            try {
                cacheService.put(Constants.JSAPI_TICKET_KEY, ticket, 7000 * 1000);
            } catch (Exception e) {
                log.error("将redis加入ticket错误,错误原因:{}", e);
            }
        }
        return ticket;
    }

    @RequestMapping("share/info")
    @ResponseBody
    public Map<String, Object> getShareInfo(HttpServletRequest request, String shareUrl) {
        String jsApiTicket = getJsApiTicket();
        SortedMap<Object, Object> map = new TreeMap<Object, Object>();
        String noncestr = WeichatUtil.createNoncestr();
        String timestamp = String.valueOf(new Date().getTime() / 1000);
        map.put("jsapi_ticket", jsApiTicket);
        map.put("noncestr", noncestr);
        map.put("timestamp", timestamp);
        map.put("url", shareUrl);
        String signature = WeichatUtil.createSha1Sign(map);
        log.info("签名参数：" + JSON.toJSONString(map) + " ,签名：" + signature);
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("jsApiTicket", jsApiTicket);
        retMap.put("noncestr", noncestr);
        retMap.put("timestamp", timestamp);
        retMap.put("appId", weichatConfig.appId);
        retMap.put("signature", signature);
        return JsonResultUtil.getSuccessResult(retMap);

    }

}
