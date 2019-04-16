/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.impl;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efida.easy.ucenter.facade.constants.UcenterConstants;
import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.service.dubbo.intergration.UcenterLoginFacadeClient;
import com.efida.sports.util.CookiesUtil;

/**
 * 
 * @author zoutao
 * @version $Id: UcenterAdapterServiceImpl.java, v 0.1 2018年10月10日 下午3:16:49 zoutao Exp $
 */
@Service
public class UcenterAdapterServiceImpl implements UcenterAdapterService {

    @Autowired
    private UcenterLoginFacadeClient loginFacadeClient;

    @Override
    public AuthToken getH5AuthToken(HttpSession session) {
        AuthToken authToken = (AuthToken) session
            .getAttribute(UcenterConstants.UCENTER_H5_AUTH_TOKEN);
        return authToken;
    }

    @Override
    public RegisterModel getAppAuthRegister(String token) {
        RegisterModel register = loginFacadeClient.getRegisterByAppToken(token);
        return register;
    }

    @Override
    public AuthToken getPCAuthToken(HttpSession session) {
        AuthToken authToken = (AuthToken) session
            .getAttribute(UcenterConstants.UCENTER_PC_AUTH_TOKEN);
        return authToken;
    }

    @Override
    public AuthToken getAuthTokenByCookieToken(String token) {
        AuthToken authToken = loginFacadeClient.getAuthTokenByCookieToken(token);
        return authToken;
    }

    @Override
    public void pcQuitLogin(HttpSession session, HttpServletResponse response) {
        session.removeAttribute(UcenterConstants.UCENTER_PC_AUTH_TOKEN);
        CookiesUtil.setCookie(response, UcenterConstants.UCENTER_PC_COOKIE_KEY, "", "zntyydh.com",
            0);
    }

    @Override
    public void refreshPcSesssion(HttpSession session, String cookieToken, AuthToken authToken) {
        session.setAttribute(UcenterConstants.UCENTER_PC_AUTH_TOKEN, authToken);
        if (StringUtils.isNotBlank(cookieToken)) {
            loginFacadeClient.refreshPcCookieToken(cookieToken, authToken);
        }
    }

}
