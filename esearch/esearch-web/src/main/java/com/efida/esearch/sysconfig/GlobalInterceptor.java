package com.efida.esearch.sysconfig;
/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */

import com.efida.esearch.utils.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author lijiaqi
 * @version $Id: GlobalInterceptor.java, v 0.1 2016年6月13日 上午11:04:59 lijiaqi Exp $
 */
public class GlobalInterceptor extends HandlerInterceptorAdapter {

    private Logger log = LoggerFactory.getLogger("GlobalInterceptor");

    @PostConstruct
    private void init() {
        log.info("GlobalInterceptor init success");
    }

    /** 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

        if (modelAndView == null) {
            return;
        }
        if (modelAndView.getViewName().startsWith("redirect:")) {
            return;
        }
        final String ctx = ServletUtil.getCtx(request);
        final String ctxassets = ctx + "assets/";
        HttpSession session = request.getSession();
        modelAndView.addObject("ctx", ctx);
        modelAndView.addObject("assets", ctxassets);
        modelAndView.addObject("session", session);
        super.postHandle(request, response, handler, modelAndView);
    }

    private void addDefaultValueIfExists(ModelAndView modelAndView, final String key,
                                         Object defVal) {
        Object extendKeywordObj = modelAndView.getModel().get(key);
        if (extendKeywordObj == null) {
            modelAndView.addObject(key, defVal);
        }
    }


}
