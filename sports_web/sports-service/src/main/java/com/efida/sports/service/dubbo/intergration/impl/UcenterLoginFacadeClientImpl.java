/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.intergration.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.efida.easy.ucenter.facade.enums.TerminalEnum;
import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.easy.ucenter.facade.model.UserModel;
import com.efida.easy.ucenter.facade.result.UcenterDefaultResult;
import com.efida.easy.ucenter.facade.service.UcenterLoginFacadeService;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.dubbo.intergration.UcenterLoginFacadeClient;

/**
 * 
 * @author zoutao
 * @version $Id: UcenterLoginFacadeClientImpl.java, v 0.1 2018年10月10日 下午6:12:41 zoutao Exp $
 */
@Service
public class UcenterLoginFacadeClientImpl implements UcenterLoginFacadeClient {

    @Reference
    private UcenterLoginFacadeService loginService;

    @Override
    public RegisterModel getRegisterByAppToken(String token) {
        UcenterDefaultResult<RegisterModel> dr = loginService.getRegisterByAppToken(token);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public void addAppLoginLog(String token, String appVersion, TerminalEnum regTerminal) {
        try {
            loginService.addAppLoginLog(token, appVersion, regTerminal);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public RegisterModel verifyCodeLogin(String phoneNum, String verifyCode,
                                         TerminalEnum regTerminal, String appVersion) {
        UcenterDefaultResult<RegisterModel> dr = loginService.verifyCodeLogin(phoneNum, verifyCode,
            regTerminal, appVersion);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public RegisterModel thirdUserLogin(UserModel user, TerminalEnum terminal, String appVersion) {
        user.setTerminal(terminal);
        UcenterDefaultResult<RegisterModel> dr = loginService.thirdUserLogin(user, appVersion);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public AuthToken getAuthTokenByCookieToken(String token) {
        UcenterDefaultResult<AuthToken> dr = loginService.getAuthTokenByCookieToken(token);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public long activeRegitsers(int days) {

        UcenterDefaultResult<Long> dr = loginService.activeRegitsers(days);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj() == null ? 0 : dr.getResultObj();
    }

    @Override
    public long getRetentionRegisters(int days) {
        UcenterDefaultResult<Long> dr = loginService.getRetentionRegisters(days);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj() == null ? 0 : dr.getResultObj();
    }

    @Override
    public long getWeekActiveRegister(Date reportDate, String regTerminal) {
        UcenterDefaultResult<Long> dr = loginService.getWeekActiveRegister(reportDate, regTerminal);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj() == null ? 0 : dr.getResultObj();
    }

    @Override
    public long getMonthActiveRegister(Date reportDate, String regTerminal) {
        UcenterDefaultResult<Long> dr = loginService.getMonthActiveRegister(reportDate,
            regTerminal);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj() == null ? 0 : dr.getResultObj();
    }

    @Override
    public long getLoginRegister(Date reportDate, String regTerminal) {
        UcenterDefaultResult<Long> dr = loginService.getLoginRegister(reportDate, regTerminal);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj() == null ? 0 : dr.getResultObj();
    }

    @Override
    public void refreshPcCookieToken(String cookieToken, AuthToken authToken) {
        loginService.refreshPcCookieToken(cookieToken, authToken);
    }

    @Override
    public void refreshAppToken(String token, RegisterModel register) {
        loginService.refreshAppToken(token, register);
    }

}
