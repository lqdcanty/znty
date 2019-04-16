/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.easy.ucenter.facade.model.RegisterModel;

/**
 * 
 * @author zoutao
 * @version $Id: LoginAdapterService.java, v 0.1 2018年10月10日 下午3:12:32 zoutao Exp $
 * 用户中心适配程序
 */
public interface UcenterAdapterService {
    /**
     * 获取h5登录信息
     * 
     * @param session
     * @return
     */
    public AuthToken getH5AuthToken(HttpSession session);

    /**
     * 获取app登录用户信息
     * @param token
     * @return
     */
    public RegisterModel getAppAuthRegister(String token);

    /**
     * 
     * 获取pc登录用户信息
     * @param session
     * @return
     */
    public AuthToken getPCAuthToken(HttpSession session);

    /**
     * 获取pc端cookie获取登录信息
     * 
     * @param token
     * @return
     */
    AuthToken getAuthTokenByCookieToken(String token);

    /**
     * 退出登录
     * 
     * @param session
     * @param response
     */
    public void pcQuitLogin(HttpSession session, HttpServletResponse response);

    /**
     * 刷新session
     * 
     * @param session
     * @param authToken
     */

    void refreshPcSesssion(HttpSession session, String cookieToken, AuthToken authToken);

}
