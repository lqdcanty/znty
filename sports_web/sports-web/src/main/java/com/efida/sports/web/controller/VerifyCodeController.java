/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.efida.sports.exception.ServiceException;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.MsgService;
import com.efida.sports.util.JsonResultUtil;

/**
 * 
 * @author zoutao
 * @version $Id: VerifyCodeController.java, v 0.1 2018年1月24日 下午4:45:16 zoutao Exp $
 */
@Controller
@RequestMapping("verify")
public class VerifyCodeController {
    private static Logger log      = LoggerFactory.getLogger(VerifyCodeController.class);

    @Autowired
    private MsgService    msgService;
    @Autowired
    private CacheService  cacheService;

    private final String  SEND_KEY = "weichat_mobile_";

    @RequestMapping("send/sms/code")
    @ResponseBody
    public Map<String, Object> sendSMSVerifyCode(HttpServletRequest request,
                                                 HttpServletResponse response, String playerPhone) {
        try {
            String key = SEND_KEY + playerPhone;
            String sendCodeStatus = cacheService.get(key);
            if (StringUtils.isNotBlank(sendCodeStatus)) {
                return JsonResultUtil.getSuccessResult();
            }
            boolean sendVerifyCode = msgService.sendVerifyCode(playerPhone);
            if (!sendVerifyCode) {
                return JsonResultUtil.getServerErrorResult("发送失败,请稍后重试");
            }
            cacheService.put(key, new Date().getTime() / 100 + "", 1000 * 10);
        } catch (ServiceException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("发送失败,请稍后重试");
        }
        return JsonResultUtil.getSuccessResult();
    }

}
