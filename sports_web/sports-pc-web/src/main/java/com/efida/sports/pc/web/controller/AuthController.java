package com.efida.sports.pc.web.controller;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.efida.sports.config.WeichatConfig;
import com.efida.sports.pc.web.config.QqConfig;
import com.efida.sports.pc.web.config.WeiboConfig;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.UcenterAdapterService;

/**
 * 第三方登录
 * 
 * @author zengbo
 * @version $Id: AuthController.java, v 0.1 2018年6月14日 上午10:09:41 zengbo Exp $
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private static Logger         log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private WeichatConfig         weichatConfig;

    @Autowired
    private WeiboConfig           weiboConfig;

    @Autowired
    private QqConfig              qqConfig;

    @Autowired
    private CacheService          cacheService;

    @Autowired
    private UcenterAdapterService ucenterAdapterService;

    /**----------------------------------------微博登录----------------------------------*/
    @GetMapping("weibo/auth")
    public String authWeibo() {
        String weiboAuthUrl = String.format(
            "https://api.weibo.com/oauth2/authorize?client_id=%s&scope=all&response_type=%s&redirect_uri=%s",
            weiboConfig.clientId, weiboConfig.code, weiboConfig.redirectUri);
        return "redirect:" + weiboAuthUrl;
    }

    //    @GetMapping("weibo/authcallback")
    //    public String authWeiboCallback(@RequestParam(value = "code", required = false) String code,
    //                                    HttpSession session, HttpServletRequest request,
    //                                    HttpServletResponse response, Model model) {
    //        log.info("start weibo callback");
    //        Users user = null;
    //        if (StringUtils.isBlank(code)) {
    //            log.error("微博登陆的Code为空,属于非法操作");
    //            return "pages/400";
    //        }
    //        try {
    //            // 根据微博回掉信息，构建用户对象
    //            AuthToken weiboLogin = userService.weiboLogin(code, weiboConfig.clientId,
    //                weiboConfig.clientSecret, weiboConfig.granttype, weiboConfig.redirectUri,
    //                RegTerminalEnum.PC, PlatformEnum.SINA);
    //            Register register = weiboLogin.getRegister();
    //            if (StringUtils.isBlank(register.getAccount())) {
    //                model.addAttribute("registerCode", register.getRegisterCode());
    //                return "pages/bind_login";
    //            }
    //            log.info("根据微博返回信息构建用户对象,用户基本属性：{}", JSON.toJSONString(weiboLogin));
    //            session.setAttribute("register", register);
    //            putLoginRegister2redis(register, response);
    //            model.addAttribute("register", register);
    //            // 获取用户的信息
    //            return "pages/index";
    //        } catch (Exception e) {
    //            log.error("微博授信获取信息失败", e);
    //            return "pages/400";
    //        }
    //    }

    //    private String putLoginRegister2redis(Register register, HttpServletResponse response) {
    //        String origin = register.getRegisterCode() + new Date().getTime();
    //        String token = MD5Utils.MD5Encode(origin);
    //        CookiesUtil.setCookie(response, Constants.COOKIE_TOKEN_KEY, token,
    //            Constants.COOKIE_TIME_OUT);
    //        cacheService.put(Constants.COOKIE_TOKEN_KEY + token, register.getRegisterCode(),
    //            1000 * 60 * 60 * 24 * 10);
    //        return token;
    //    }

    /** ----------------------------微信登录------------------------------ */
    /**
     * 微信登陆跳转地址
     * 
     * @return
     */
    @GetMapping("weixin/auth")
    public String authWeixin() {
        String weixinAuthUrl = String
            .format("https://open.weixin.qq.com/connect/qrconnect?" + "appid=%s&redirect_uri=%s&"
                    + "response_type=code&scope=snsapi_login&state=0#wechat_redirect",
                weichatConfig.appId, URLEncoder.encode(weichatConfig.redirectUri));
        return "redirect:" + weixinAuthUrl;

    }

    /**
     * 微信登录回调地址
     * 
     * @param code
     * @param session
     * @param response
     * @param attributes@GetMapping
     * @return
     */
    //    @GetMapping("weixin/authcallback")
    //    public String authweixinCallback(@RequestParam(value = "code", required = false) String code,
    //                                     HttpServletRequest request, HttpSession session,
    //                                     HttpServletResponse response, Model model) {
    //        log.info("start wechat callback");
    //        try {
    //            // 用户同意授权后，能获取到code
    //            if (StringUtils.isBlank(code)) {
    //                log.error("微信登陆的Code为空,属于非法操作");
    //                return "pages/400";
    //            }
    //            // 获取网页授权access_token,下面只得到了openId,还有 accessToken
    //            AuthToken weichatLogin = userService.weichatLogin(code, RegTerminalEnum.PC,
    //                PlatformEnum.WEICHAT);
    //            Register register = weichatLogin.getRegister();
    //            if (StringUtils.isBlank(register.getAccount())) {
    //                model.addAttribute("registerCode", register.getRegisterCode());
    //                return "pages/bind_login";
    //            }
    //            //            String ip = HttpsClientUtil.getIpAddr(request);
    //            // 获取用户的信息
    //            log.info("根据微信返回信息构建用户对象,用户基本属性：{}", JSON.toJSONString(weichatLogin.getRegister()));
    //            session.setAttribute("register", weichatLogin.getRegister());
    //            putLoginRegister2redis(weichatLogin.getRegister(), response);
    //            model.addAttribute("register", register);
    //            // 获取用户的信息
    //            return "pages/index";
    //        } catch (Exception e) {
    //            log.error("微信登录失败", e);
    //            return "pages/400";
    //        }
    //    }

    /**----------------------------------------qq登录----------------------------------*/
    /**
     * QQ第三方登录
     * 
     * @param session
     * @param request
     * @param attributes
     * @return
     */
    @GetMapping(value = { "qq/auth" })
    public String authQQ(HttpSession session, HttpServletRequest request,
                         RedirectAttributes attributes) {
        String qqAuthUrl = String.format(
            "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=%s&redirect_uri=%s&scope=get_user_info",
            qqConfig.clientId, URLEncoder.encode(qqConfig.redirectUri));
        return "redirect:" + qqAuthUrl;
    }

    /**
     * 第三方QQ登录回调接口
     * 
     * @param code
     * @param session
     * @param response
     * @param request
     * @param redirectAttributes
     * @return
     */
    //    @GetMapping("qq/authcallback")
    //    public String authQQCallback(@RequestParam(value = "code", required = false) String code,
    //                                 HttpSession session, HttpServletResponse response, Model model,
    //                                 HttpServletRequest request) {
    //        log.info("start QQ callback");
    //        if (StringUtils.isBlank(code)) {
    //            log.error("QQ登陆的Code为空,属于非法操作");
    //            return "pages/400";
    //        }
    //        try {
    //            request.setCharacterEncoding("utf-8");
    //            response.setCharacterEncoding("utf-8");
    //            response.setContentType("text/html; charset=utf-8");
    //            AuthToken qqLogin = userService.qqLogin(code, qqConfig.clientId, qqConfig.clientKey,
    //                qqConfig.redirectUri, RegTerminalEnum.PC, PlatformEnum.QQ);
    //            //            String ip = HttpsClientUtil.getIpAddr(request);
    //            // 获取用户的信息
    //            log.info("根据QQ返回信息构建用户对象,用户基本属性：{}", JSON.toJSONString(qqLogin.getRegister()));
    //            Register register = qqLogin.getRegister();
    //            if (StringUtils.isBlank(register.getAccount())) {
    //                model.addAttribute("registerCode", register.getRegisterCode());
    //                return "pages/bind_login";
    //            }
    //            session.setAttribute("register", register);
    //            putLoginRegister2redis(register, response);
    //            model.addAttribute("register", register);
    //            // 获取用户的信息
    //            return "pages/index";
    //        } catch (Exception e) {
    //            log.error("QQ登录失败", e);
    //            return "pages/400";
    //        }
    //    }

}
