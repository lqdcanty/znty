/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.sports.config.WeichatConfig;
import com.efida.sports.constants.Constants;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.util.CommonUtil;

/**
 * 
 * @author zoutao
 * @version $Id: AuthController.java, v 0.1 2018年1月19日 下午3:37:25 zoutao Exp $
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private static Logger         log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private WeichatConfig         weichatConfig;

    @Autowired
    private CacheService          cacheService;

    @Value("${ucenter-domain}")
    public String                 ucenterDomain;
    @Autowired
    private UcenterAdapterService ucenterAdapterService;

    /**
     * 获取微信公众号accessToken
     * 
     * @return
     */
    public String getAccessToken() {
        String accessToken = getAccessToken4Cache(weichatConfig.appId, weichatConfig.appSecret);
        //如果存在直接返回
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }
        accessToken = getAccessToken4Weixin(weichatConfig.appId, weichatConfig.appSecret);
        return accessToken;
    }

    private String getAccessToken4Cache(String appId, String appSecrect) {

        String accessToken = null;
        try {
            accessToken = cacheService.get(Constants.ACCESS_TOKEN_KEY);
        } catch (Exception e) {
            log.error("从redis中获取accessToken错误, appId:{}", appId);
        }

        return accessToken;
    }

    /**
     * 同步执行
     * 
     * @param appId
     * @param appSecrect
     * @return
     */
    private synchronized String getAccessToken4Weixin(String appId, String appSecrect) {

        //2次检查，防止多次请求微信公众号接口
        String accessToken = getAccessToken4Cache(appId, appSecrect);
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }
        // 拼接请求地址
        String requestUrl = String.format(
            "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
            appId, appSecrect);
        log.info("请求地址:" + requestUrl);
        JSONObject jsonObject = CommonUtil.httpGet(requestUrl);
        log.info("获取access_token返回结果" + jsonObject);
        accessToken = jsonObject.getString("access_token");
        if (StringUtils.isBlank(accessToken)) {
            try {
                cacheService.put(Constants.ACCESS_TOKEN_KEY, accessToken, 7000 * 1000);
            } catch (Exception e) {
                log.info("将accessToken放入redis中错误,错误原因:{}" + accessToken);
            }
        }
        return accessToken;
    }

    /**
     * 微信登陆跳转地址
     * 
     * @return
     */
    @GetMapping("weixin/auth")
    public String authWeixin(HttpServletRequest request, Model model, HttpSession session) {
        return "redirect:" + ucenterDomain;
    }

    //    @GetMapping("weixin/token")
    //    @ResponseBody
    //    public String verificationToken(HttpServletRequest request) {
    //
    //        //微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp，nonce参数
    //        String signature = request.getParameter("signature");
    //        //时间戳
    //        String timestamp = request.getParameter("timestamp");
    //        //随机数
    //        String nonce = request.getParameter("nonce");
    //        //随机字符串
    //        String echostr = request.getParameter("echostr");
    //        if (WeichatTokenSignUtil.checkSignature(signature, timestamp, nonce)) {
    //            return echostr;
    //        }
    //        return "";
    //    }
    //
    //    @RequestMapping("weixin/authcallback")
    //    public String authweixinCallback(@RequestParam(value = "code", required = false) String code,
    //                                     HttpServletRequest request, HttpServletResponse response,
    //                                     HttpSession session, Model model) {
    //        try {
    //            AuthToken authToken = userService.weichatLogin(code, RegTerminalEnum.WEICHAT,
    //                PlatformEnum.WEICHAT);
    //            session.setAttribute(Constants.AUTH_TOKEN, authToken);
    //            if (session.getAttribute(Constants.AUTH_REDIRECT_URL) != null) {
    //                String retUrl = (String) session.getAttribute(Constants.AUTH_REDIRECT_URL);
    //                session.removeAttribute(Constants.AUTH_REDIRECT_URL);
    //                return retUrl;
    //            }
    //        } catch (Exception e) {
    //            log.error("", e);
    //        }
    //        return "redirect:/game/type";
    //    }
    //
    //    @RequestMapping("weixin/authcallback/test")
    //    public String mockweixinCallback(@RequestParam(value = "openId", required = false) String openId,
    //                                     @RequestParam(value = "secrect", required = false) String secrect,
    //                                     HttpServletRequest request, HttpServletResponse response,
    //                                     HttpSession session, Model model) {
    //        try {
    //            if (secrect == null || !secrect.equals("forpressuretest")) {
    //                return "redirect:/error";
    //            }
    //            if (openId.equals("random")) {
    //                openId = "open" + (int) (Math.random() * 1000000);
    //            }
    //            AuthToken authToken = userService.UserLogin4test(openId);
    //            session.setAttribute(Constants.AUTH_TOKEN, authToken);
    //            if (session.getAttribute(Constants.AUTH_REDIRECT_URL) != null) {
    //                String retUrl = (String) session.getAttribute(Constants.AUTH_REDIRECT_URL);
    //                session.removeAttribute(Constants.AUTH_REDIRECT_URL);
    //                return retUrl;
    //            }
    //        } catch (Exception e) {
    //            log.error("", e);
    //
    //            return "redirect:/error";
    //        }
    //        return "redirect:/game/type";
    //    }

    @RequestMapping("personal")
    public String personal(Model model, HttpSession session, HttpServletRequest request) {
        AuthToken authToken = ucenterAdapterService.getH5AuthToken(session);
        if (authToken == null) {
            return "redirect:" + ucenterDomain;
        }
        if (StringUtils.isBlank(authToken.getRegister().getAccount())) {
            return "redirect:" + ucenterDomain + "/user/h5/phone";
        }
        String number = request.getParameter("number");
        model.addAttribute("number", number);
        return "pages/user";
    }

    //    /**
    //     * 登录成功
    //     * @return
    //     */
    //    @RequestMapping(value = "phone/login/suc")
    //    public String bindLoginSuccess(HttpSession session) {
    //        AuthToken auth = (AuthToken) session.getAttribute(Constants.AUTH_TOKEN);
    //        if (auth == null) {
    //            return "redirect:/auth/weixin/auth";
    //        }
    //        if (session.getAttribute(Constants.AUTH_REDIRECT_URL) != null) {
    //            String retUrl = (String) session.getAttribute(Constants.AUTH_REDIRECT_URL);
    //            session.removeAttribute(Constants.AUTH_REDIRECT_URL);
    //            return retUrl;
    //        }
    //        return "redirect:/game/type";
    //    }
    //
    //    /**
    //     * 用户手机号登录
    //     * @return
    //     */
    //    @ApiOperation(value = "手机号登录", notes = "手机号登录")
    //    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
    //                            @ApiResponse(code = 500, message = "接口系统异常") })
    //    @ApiImplicitParams({ @ApiImplicitParam(name = "phoneNum", value = "电话号码", required = true, dataType = "String", paramType = "form"),
    //                         @ApiImplicitParam(name = "verifyCode", value = "验证码", required = true, dataType = "String", paramType = "form") })
    //    @ResponseBody
    //    @RequestMapping(value = "phone/login", method = { RequestMethod.POST })
    //    public Map<String, Object> bindLogin(@RequestParam(value = "phoneNum", required = true) String phoneNum,
    //                                         @RequestParam(value = "verifyCode", required = true) String verifyCode,
    //                                         HttpSession session) {
    //
    //        try {
    //            AuthToken authToken = new AuthToken();
    //            Register register = loginService.verifyCodeLogin(phoneNum, verifyCode,
    //                RegTerminalEnum.WEICHAT, null);
    //            //刷新session
    //            authToken.setRegister(register);
    //            authToken.setRegisterCode(register.getRegisterCode());
    //            session.removeAttribute(Constants.AUTH_TOKEN);
    //            session.setAttribute(Constants.AUTH_TOKEN, authToken);
    //            return JsonResultUtil.getSuccessResult();
    //        } catch (BusinessException e) {
    //            log.error("", e);
    //            return JsonResultUtil.getServerErrorResult(e.getMessage());
    //        } catch (Exception e) {
    //            log.error("", e);
    //            return JsonResultUtil.getServerErrorResult("绑定失败,请稍后重试！！！");
    //        }
    //    }
    //
    //    /**
    //     * 用户绑定电话号码
    //     * @return
    //     */
    //    @ApiOperation(value = "用户绑定电话号码", notes = "绑定电话号码")
    //    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
    //                            @ApiResponse(code = 500, message = "接口系统异常") })
    //    @ApiImplicitParams({ @ApiImplicitParam(name = "phoneNum", value = "电话号码", required = true, dataType = "String", paramType = "form"),
    //                         @ApiImplicitParam(name = "verifyCode", value = "验证码", required = true, dataType = "String", paramType = "form") })
    //    @ResponseBody
    //    @RequestMapping(value = "phone/bind", method = { RequestMethod.POST })
    //    public Map<String, Object> bindPhone(@RequestParam(value = "phoneNum", required = true) String phoneNum,
    //                                         @RequestParam(value = "verifyCode", required = true) String verifyCode,
    //                                         HttpSession session) {
    //
    //        try {
    //            AuthToken auth = (AuthToken) session.getAttribute(Constants.AUTH_TOKEN);
    //            if (auth == null) {
    //                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
    //            }
    //            Register register = auth.getRegister();
    //            if (register == null) {
    //                log.error("authToken中未找到对应账户信息, registerCode:" + auth.getRegisterCode());
    //                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "请重新登录");
    //            }
    //            register = this.registerService.selectByCode(register.getRegisterCode());
    //            if (register == null) {
    //                log.error("register中未找到对应账户信息, registerCode:" + auth.getRegisterCode());
    //                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
    //            }
    //            register = registerService.bindPhone(register, phoneNum, verifyCode);
    //            registerService.expireRegisterCache(register.getRegisterCode());
    //            //刷新session
    //            auth.setRegister(register);
    //            //清除50s内发送验证码限制。
    //            this.msgService.clearVerifyStatus(phoneNum);
    //            auth.setRegisterCode(register.getRegisterCode());
    //            session.removeAttribute(Constants.AUTH_TOKEN);
    //            session.setAttribute(Constants.AUTH_TOKEN, auth);
    //            return JsonResultUtil.getSuccessResult();
    //        } catch (BusinessException e) {
    //            log.error("", e);
    //            return JsonResultUtil.getServerErrorResult(e.getMessage());
    //        } catch (Exception e) {
    //            log.error("", e);
    //            return JsonResultUtil.getServerErrorResult("绑定失败,请稍后重试！！！");
    //        }
    //    }
    //
    //    /**
    //     * 用户绑定电话号码
    //     * @return
    //     */
    //
    //    @RequestMapping(value = "bind/success")
    //    public String bindSuccess(HttpSession session) {
    //
    //        Object attribute = session.getAttribute(Constants.BIND_PHONE_REDIRECT_URL);
    //        String url = "redirect:/ucenter";
    //        if (attribute != null) {
    //            url = String.valueOf(attribute);
    //        }
    //        return url;
    //    }

}
