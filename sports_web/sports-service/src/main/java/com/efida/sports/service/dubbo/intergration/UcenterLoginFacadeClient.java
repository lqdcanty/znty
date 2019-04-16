/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.intergration;

import java.util.Date;

import com.efida.easy.ucenter.facade.enums.TerminalEnum;
import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.easy.ucenter.facade.model.UserModel;

/**
 * 
 * @author zoutao
 * @version $Id: UcenterLoginFacadeClient.java, v 0.1 2018年10月10日 下午6:11:45 zoutao Exp $
 */
public interface UcenterLoginFacadeClient {

    RegisterModel getRegisterByAppToken(String token);

    /**
     * 添加用户登录日志
     * 
     * @param token
     * @param appVersion
     * @param regTerminal
     */
    void addAppLoginLog(String token, String appVersion, TerminalEnum regTerminal);

    /**
     * 验证码登录
     * 
     * @param phoneNum
     * @param verifyCode
     * @param regTerminal
     * @param appVersion
     * @return
     */
    RegisterModel verifyCodeLogin(String phoneNum, String verifyCode, TerminalEnum regTerminal,
                                  String appVersion);

    /**
     * 第三登录接口
     * 
     * @param user
     * @param regTerminal
     * @param appVersion
     * @return
     */
    RegisterModel thirdUserLogin(UserModel user, TerminalEnum regTerminal, String appVersion);

    /**
     * 根据cookie中存放的token获取用户AuthToken
     * 
     * @param token
     * @return
     */
    AuthToken getAuthTokenByCookieToken(String token);

    /**
     *  查询几天内总活跃用户数
     * 
     * @param days
     * @return
     */
    long activeRegitsers(int days);

    /**
     * 查询几天内留存得用户总数
     * 
     * @param days
     * @return
     */
    long getRetentionRegisters(int days);

    /**
     * 获取周活跃用户数
     * 规则：当天登录的用户 且之前7天也登录过的用户
     * 算法: 查询当天登录用户所有编号
     *       查询之前一周内所有登录的用户编号
     * @param startTime
     * @param endTime
     * @return
     */
    long getWeekActiveRegister(Date reportDate, String regTerminal);

    /**
     * 获取月活跃用户数
     * 规则：当天登录的用户 且之前30天也登录过的用户
     * 算法: 查询当天登录用户所有编号
     *       查询之前30天内所有登录的用户编号
     */
    long getMonthActiveRegister(Date reportDate, String regTerminal);

    /**
     * 获取登录用户数
     * 
     * @param startTime
     * @param endTime
     * @return
     */
    long getLoginRegister(Date reportDate, String regTerminal);

    /**
     * 刷新cookie中token对应的值
     * 
     * @param cookieToken
     * @param authToken
     */
    void refreshPcCookieToken(String cookieToken, AuthToken authToken);

    /**
     *刷新apptoken
     * 
     * @param token
     * @param register
     */
    void refreshAppToken(String token, RegisterModel register);

}
