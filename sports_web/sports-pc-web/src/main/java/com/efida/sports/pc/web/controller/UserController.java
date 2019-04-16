package com.efida.sports.pc.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.efida.easy.ucenter.facade.constants.UcenterConstants;
import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.exception.ServiceException;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.MsgService;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;
import com.efida.sports.util.CookiesUtil;
import com.efida.sports.util.JsonResultUtil;

/**
 * 用户controller
 * 
 * @author zengbo
 * @version $Id: UserController.java, v 0.1 2018年6月13日 下午5:13:04 zengbo Exp $
 */
@RequestMapping("user")
@Controller
public class UserController {

    private static Logger               log      = LoggerFactory.getLogger(UserController.class);

    public final String                 SEND_KEY = "pc_send_mobile_";

    @Autowired
    private CacheService                cacheService;

    @Autowired
    private UcenterAdapterService       ucenterAdapterService;

    @Autowired
    private UcenterRegisterFacadeClient registerFacadeClient;
    @Autowired
    private MsgService                  msgService;

    @Value("${ucenter-domain}")
    public String                       ucenterDomain;
    @Value("${apply-domain}")
    public String                       applyDomain;

    /**
     * 登录初始化
     * 
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping("login/index")
    public String userLoginIndx() {
        return "redirect:" + ucenterDomain;
    }

    /**
     * 退出登录
     * 
     * @param session
     * @return
     */
    @RequestMapping("quit/login")
    public void quitLogin(HttpSession session, HttpServletResponse response) {
        try {
            AuthToken pcAuthToken = ucenterAdapterService.getPCAuthToken(session);
            if (pcAuthToken != null) {
                ucenterAdapterService.pcQuitLogin(session, response);
            }
            response.sendRedirect("/match/type");
        } catch (IOException e) {
            log.error("退出登录失败", e);
        }
    }

    /**
     * 操作失败
     * 
     * @param session
     * @param response
     * @return
     */
    @RequestMapping("error")
    public String error(HttpSession session, HttpServletResponse response) {
        return "pages/500";
    }

    /**
     * 下载页
     * 
     * @return
     */
    @RequestMapping("upload/page")
    public String uploadPage() {
        return "pages/uploadPage";
    }

    /**
     * 关于我们
     * 
     * @return
     */
    @RequestMapping("about/us")
    public String aboutUs(HttpSession session, Model model) {
        RegisterModel register = null;
        AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
        if (authToken != null) {
            register = authToken.getRegister();
        }
        model.addAttribute("register", register);
        return "pages/about_us";
    }

    /**
     * 在线客服
     * 
     * @return
     */
    @RequestMapping("customer/service")
    public String customerService(HttpSession session, Model model) {
        RegisterModel register = null;
        AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
        if (authToken != null) {
            register = authToken.getRegister();
        }
        model.addAttribute("register", register);
        return "pages/customer_service";
    }

    /**
     * 用户头像修改初始化
     * 
     * @return
     */
    @RequestMapping("head/init")
    public String headInit(Model model, HttpSession session) {
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            if (authToken == null) {
                String uri = applyDomain + "user/head/init";
                uri = URLEncoder.encode(uri, "UTF-8");
                return "redirect:" + ucenterDomain + "?login_redirect=" + uri;
            }
            RegisterModel register = registerFacadeClient
                .getRegsiterByRegisterCode(authToken.getRegisterCode());
            model.addAttribute("register", register);
        } catch (Exception e) {
            log.error("头像修改初始化失败", e);
        }
        return "pages/user_head_update";
    }

    /**
     * 发送验证码
     * 
     * @param request
     * @param response
     * @param playerPhone
     * @return
     */
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

    /**
     * 修改用户头像
     * 
     * @param imgUrl
     * @param session
     * @return
     */
    @RequestMapping("update/head")
    @ResponseBody
    public Map<String, Object> updateHead(String headimgUrl, String nickName, HttpSession session,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        try {
            if (StringUtils.isNotBlank(nickName) && nickName.length() >= 32) {
                return JsonResultUtil.getServerErrorResult("昵称长度不能超过32位");
            }
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            RegisterModel register = authToken.getRegister();
            if (headimgUrl.equals(register.getHeadimgUrl())
                && nickName.equals(register.getNickName())) {
                return JsonResultUtil.getSuccessResult("修改成功");
            }
            register.setHeadimgUrl(headimgUrl);
            register.setNickName(nickName);
            register = registerFacadeClient.updateRegsiterInfo(register);
            authToken.setRegister(register);
            Cookie cookie = CookiesUtil.getCookieByName(request,
                UcenterConstants.UCENTER_PC_COOKIE_KEY);
            String cookieToken = null;
            if (cookie != null) {
                cookieToken = cookie.getValue();
            }
            ucenterAdapterService.refreshPcSesssion(session, cookieToken, authToken);
            request.setAttribute("register", register);
            return JsonResultUtil.getSuccessResult("修改成功");
        } catch (BusinessException e) {
            log.error("登录失败", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("登录失败", e);
            return JsonResultUtil.getServerErrorResult("修改失败");
        }
    }

    /**
     * 绑定手机号
     * 
     * @param registerCode
     * @param telphone
     * @param verifyCode
     * @param url
     * @param session
     * @return
     */
    @RequestMapping("bind/telphone")
    @ResponseBody
    public Map<String, Object> bindTelphone(String registerCode, String telphone, String verifyCode,
                                            String url, HttpSession session,
                                            HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            RegisterModel register = registerFacadeClient
                .getRegsiterByRegisterCode(authToken.getRegisterCode());
            if (register == null) {
                log.error("用户不存在");
                return JsonResultUtil.getServerErrorResult("手机号绑定失败");
            }
            boolean checkVerifyCode = msgService.checkVerifyCode(telphone, verifyCode);
            if (!checkVerifyCode) {
                return JsonResultUtil.getServerErrorResult("验证码错误");
            }
            register = registerFacadeClient.bindPhone(register, telphone, verifyCode);
            Cookie cookie = CookiesUtil.getCookieByName(request,
                UcenterConstants.UCENTER_PC_COOKIE_KEY);
            String cookieToken = null;
            if (cookie != null) {
                cookieToken = cookie.getValue();
            }
            ucenterAdapterService.refreshPcSesssion(session, cookieToken, authToken);
            map.put("register", register);
            map.put("url", url);
            return JsonResultUtil.getSuccessResult("手机号绑定成功", map);
        } catch (BusinessException e) {
            log.error("手机号绑定失败", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("手机号绑定失败", e);
            return JsonResultUtil.getServerErrorResult("手机号绑定失败");
        }
    }

}
