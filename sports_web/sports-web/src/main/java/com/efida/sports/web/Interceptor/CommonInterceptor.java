package com.efida.sports.web.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author zoutao
 * @version $Id: CommonInterceptor.java, v 0.1 2017年10月15日 上午10:38:53  Exp $
 */
public class CommonInterceptor implements HandlerInterceptor {
    /** 
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */

    @Value("${spring.static-path}")
    public String staticPath;
    @Value("${social-domain}")
    public String socialDomain;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse, Object o) throws Exception {
        Boolean isWeicat = false;
        String ua = httpServletRequest.getHeader("user-agent").toLowerCase();
        if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器  
            isWeicat = true;
        }
        String path = httpServletRequest.getContextPath();
        String scheme = httpServletRequest.getScheme();
        String serverName = httpServletRequest.getServerName();
        int port = httpServletRequest.getServerPort();
        String basePath = scheme + "://" + serverName + ":" + port + path;
        httpServletRequest.setAttribute("ctx", basePath);
        httpServletRequest.setAttribute("cssV", "201807090101");
        httpServletRequest.setAttribute("isWeicat", isWeicat);
        httpServletRequest.setAttribute("staticPath", staticPath);
        httpServletRequest.setAttribute("socialDomain", socialDomain);
        //        httpServletRequest.getSession().setAttribute("ctx", basePath);
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