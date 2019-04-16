/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.efida.easy.ucenter.facade.enums.GenderEnum;
import com.efida.easy.ucenter.facade.enums.PlatformEnum;
import com.efida.easy.ucenter.facade.enums.TerminalEnum;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.easy.ucenter.facade.model.UserModel;
import com.efida.sports.app.vo.RegisterVo;
import com.efida.sports.enums.ErrorCodeEnum;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.MsgService;
import com.efida.sports.service.dubbo.intergration.UcenterLoginFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;
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
 * @version $Id: LoginController.java, v 0.1 2018年6月12日 下午1:55:30 zoutao Exp $
 */
@RestController()
@Api(value = "logingApi", tags = { "登陆操作接口" })
@RequestMapping(value = "/api", produces = "application/json")
public class LoginController {

    private static Logger               log = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private UcenterLoginFacadeClient    loginServiceClient;

    @Autowired
    private UcenterRegisterFacadeClient regsiterFacadeClient;

    @Autowired
    private MsgService                  msgService;

    /**
     * 短信登录
     * @return
     */
    @ApiOperation(value = "记录用户登录日志", notes = "记录用户登录日志,app加载动画调用")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "验证码", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "appVersion", value = "app版本号", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "regTerminal", value = "注册来源(ANDROID,IOS)", required = true, dataType = "String", paramType = "form") })
    @RequestMapping(value = "login/log", method = { RequestMethod.POST })
    public Map<String, Object> loginLog(@RequestParam(value = "token", required = true) String token,
                                        @RequestParam(value = "appVersion", required = false) String appVersion,
                                        @RequestParam(value = "regTerminal", required = true) TerminalEnum regTerminal) {
        try {
            loginServiceClient.addAppLoginLog(token, appVersion, regTerminal);
        } catch (Exception e) {
            log.error("", e);
        }
        return JsonResultUtil.getSuccessResult();
    }

    /**
     * 短信登录
     * @return
     */
    @ApiOperation(value = "短信登录", notes = "短信登录接口(验证码登录时调用)")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "phoneNum", value = "电话号码", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "verifyCode", value = "验证码", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "appVersion", value = "app版本号", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "regTerminal", value = "注册来源(ANDROID,IOS)", required = true, dataType = "String", paramType = "form") })
    @RequestMapping(value = "user/login", method = { RequestMethod.POST })
    public Map<String, Object> userLogin(@RequestParam(value = "phoneNum", required = true) String phoneNum,
                                         @RequestParam(value = "verifyCode", required = true) String verifyCode,
                                         @RequestParam(value = "appVersion", required = false) String appVersion,
                                         @RequestParam(value = "regTerminal", required = true) TerminalEnum regTerminal) {
        try {

            RegisterModel register = loginServiceClient.verifyCodeLogin(phoneNum, verifyCode,
                regTerminal, appVersion);

            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("register", RegisterVo.getVo(register));
            hashMap.put("token", register.getToken());
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("登录失败,请稍后重试！！！");
        }
    }

    /**
     * 获取赛事列表
     * 
     * @return
     */
    @ApiOperation(value = "第三方登录", notes = "第三方登录接口(微信，微博，qq登录调用)")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "nickName", value = "电话号码", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "platform", value = "平台来源(WEICHAT:微信，QQ：腾讯qq,SINA：新浪微博)", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "appId", value = "appId", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "regTerminal", value = "注册来源(安卓：ANDROID,苹果：IOS)", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "openId", value = "openId", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "headimgUrl", value = "头像", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "gender", value = "性别(1:男,2:女,3:未知)", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "province", value = "省份", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "appVersion", value = "app版本号", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "city", value = "城市", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "country", value = "国家", required = false, dataType = "String", paramType = "form") })
    @RequestMapping(value = "third/login", method = { RequestMethod.POST })
    public Map<String, Object> thirdLogin(@RequestParam(value = "nickName", required = true) String nickName,
                                          @RequestParam(value = "platform", required = true) PlatformEnum platform,
                                          @RequestParam(value = "regTerminal", required = true) TerminalEnum regTerminal,
                                          @RequestParam(value = "appId", required = true) String appId,
                                          @RequestParam(value = "openId", required = false) String openId,
                                          @RequestParam(value = "headimgUrl", required = false) String headimgUrl,
                                          @RequestParam(value = "gender", required = false) String gender,
                                          @RequestParam(value = "city", required = false) String city,
                                          @RequestParam(value = "appVersion", required = false) String appVersion,
                                          @RequestParam(value = "province", required = false) String province,
                                          @RequestParam(value = "country", required = false) String country) {

        try {
            UserModel user = new UserModel();
            user.setAppId(appId);
            user.setCity(city);
            user.setOpenId(openId);
            user.setCountry(country);
            GenderEnum genderEnum = GenderEnum.getEnumByCode(gender);
            user.setGender(genderEnum);
            user.setHeadimgUrl(headimgUrl);
            user.setNickName(nickName);
            user.setPlatform(platform);
            user.setProvince(province);
            RegisterModel register = loginServiceClient.thirdUserLogin(user, regTerminal,
                appVersion);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("register", RegisterVo.getVo(register));
            hashMap.put("token", register.getToken());
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("登录失败,请稍后重试！！！");
        }
    }

    /**
     * 用户绑定电话号码
     * 
     * @return
     */
    @ApiOperation(value = "用户绑定电话号码", notes = "绑定电话号码")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "phoneNum", value = "电话号码", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "verifyCode", value = "验证码", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form") })
    @RequestMapping(value = "phone/bind", method = { RequestMethod.POST })
    public Map<String, Object> bindPhone(@RequestParam(value = "phoneNum", required = true) String phoneNum,
                                         @RequestParam(value = "verifyCode", required = true) String verifyCode,
                                         @RequestParam(value = "token", required = true) String token) {

        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            register = regsiterFacadeClient.bindPhone(register, phoneNum, verifyCode);
            //刷新token
            loginServiceClient.refreshAppToken(token, register);
            //清除50s内发送验证码限制。
            this.msgService.clearVerifyStatus(phoneNum);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("register", RegisterVo.getVo(register));
            hashMap.put("token", token);
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("绑定失败,请稍后重试！！！");
        }

    }
}
