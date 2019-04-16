/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.efida.sports.exception.ServiceException;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.MsgService;
import com.efida.sports.util.JsonResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author zoutao
 * @version $Id: VerifyCodeController.java, v 0.1 2018年1月24日 下午4:45:16 zoutao Exp $
 */
@RestController()
@Api(value = "VerifyCodeApi", tags = { "发送验证码接口" })
@RequestMapping(value = "/api/verify", produces = "application/json")
public class VerifyCodeController {
    private static Logger log      = LoggerFactory.getLogger(VerifyCodeController.class);
    @Autowired
    private CacheService  cacheService;

    public final String   SEND_KEY = "api_mobile_";

    @Autowired
    private MsgService    msgService;

    @ApiOperation(value = "发送验证码接口", notes = "用于发送短信验证码")
    @ApiImplicitParams({ @ApiImplicitParam(name = "phoneNum", value = "电话号码", required = true, dataType = "String", paramType = "query") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "send", method = { RequestMethod.GET })
    public Map<String, Object> sendSMSVerifyCode(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 @RequestParam(value = "phoneNum", required = true) String phoneNum) {
        log.info("*******************发送验证码****************** phoneNum="+phoneNum);
        try {

            String key = SEND_KEY + phoneNum;
            String sendCodeStatus = cacheService.get(key);
            if (StringUtils.isNotBlank(sendCodeStatus)) {
                return JsonResultUtil.getSuccessResult();
            }
            boolean sendVerifyCode = msgService.sendVerifyCode(phoneNum);
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
