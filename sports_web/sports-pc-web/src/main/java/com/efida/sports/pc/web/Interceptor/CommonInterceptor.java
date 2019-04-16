package com.efida.sports.pc.web.Interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.efida.easy.ucenter.facade.constants.UcenterConstants;
import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.sports.constants.Constants;
import com.efida.sports.service.IRegisterAccessLogService;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.util.CookiesUtil;

/**
 * 
 * @author zoutao
 * @version $Id: CommonInterceptor.java, v 0.1 2017年10月15日 上午10:38:53  Exp $
 */
public class CommonInterceptor implements HandlerInterceptor {

    //    private static String resourcePath = "http://static.pc.zntyydh.com";
    //    private static String resourcePath = "http://zntyweb.efida.com.cn";
    //    private static String resourcePath = "http://localhost:9996";

    @Value("${spring.static-path}")
    public String                     resourcePath;

    @Value("${social-domain}")
    public String                     socialDomain;

    @Autowired
    private IRegisterAccessLogService accessLogService;

    @Autowired
    private UcenterAdapterService     ucenterAdapterService;

    /** 
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse, Object o) throws Exception {
        String path = httpServletRequest.getContextPath();
        String scheme = httpServletRequest.getScheme();
        String serverName = httpServletRequest.getServerName();
        int port = httpServletRequest.getServerPort();
        String basePath = scheme + "://" + serverName + ":" + port + path;
        httpServletRequest.setAttribute("ctx", basePath);
        httpServletRequest.setAttribute("stx", resourcePath);
        httpServletRequest.setAttribute("socialDomain", socialDomain);
        httpServletRequest.setAttribute("cssV", "20180606");
        httpServletRequest.setAttribute("isLogin", false);
        HttpSession session = httpServletRequest.getSession();
        AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
        accessLogService.addPCAccessLog(httpServletRequest);
        if (authToken != null) {
            httpServletRequest.setAttribute("isLogin", true);
            return true;
        }
        Cookie cookie = CookiesUtil.getCookieByName(httpServletRequest,
            UcenterConstants.UCENTER_PC_COOKIE_KEY);
        if (cookie != null && StringUtils.isNotBlank(cookie.getValue())) {
            String cookieToken = cookie.getValue();
            authToken = ucenterAdapterService.getAuthTokenByCookieToken(cookieToken);
            if (authToken != null) {
                httpServletRequest.setAttribute("isLogin", true);
                session.setAttribute(UcenterConstants.UCENTER_PC_AUTH_TOKEN, authToken);
                CookiesUtil.setCookie(httpServletResponse, UcenterConstants.UCNTER_COOKIE_TOKEN_KEY,
                    cookieToken, "zntyydh.com", Constants.COOKIE_TIME_OUT);
            }
        }
        return true;
    }

    /** 
     * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {

    }

    /** 
     * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse, Object o,
                                Exception e) throws Exception {

    }
}