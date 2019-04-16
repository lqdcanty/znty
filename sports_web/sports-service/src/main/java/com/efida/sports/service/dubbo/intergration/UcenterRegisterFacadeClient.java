/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.intergration;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.easy.ucenter.facade.result.UcenterPageResult;
import com.efida.sport.facade.enums.GenderEnum;
import com.efida.sport.facade.enums.PlatformEnum;
import com.efida.sport.facade.enums.RegTerminalEnum;

public interface UcenterRegisterFacadeClient {

    RegisterModel getRegsiterByRegisterCode(String registerCode);

    /**
     * 用户绑定手机号
     * 
     * @param register
     * @param phoneNum
     * @param verifyCode
     * @return
     */
    RegisterModel bindPhone(RegisterModel register, String phoneNum, String verifyCode);

    /**
     * 修改用户基本信息
     * 
     * @param register
     * @return
     */
    RegisterModel updateRegsiterInfo(RegisterModel register);

    /**
     *创建用户
     * 
     * @param register
     * @return 
     */
    RegisterModel createRegsiter(RegisterModel register);

    /**
     * 根据手机号查询用户
     * 
     * @param phoneNum
     * @return
     */
    RegisterModel getRegsiterByAccount(String phoneNum);

    /**
     * 获取第一个注册用户
     * 
     * @return
     */
    RegisterModel getFirstRegRegister();

    /**
       * 根据开始时间和结束时间按RegTerminal分组统计注册人数
       * @param startTime >=
       * @param endTime   <=
       * @return
    */
    List<Map<String, Object>> getRegisterGroupByRegTerminal(Date startTime, Date endTime);

    /**
     * 根据注册类型和结束时间查询总用户数
     * 
     * @param regTerminal
     * @param endTime
     * @return
     */
    Long getTotalRegisterByRegTerminal(String regTerminal, Date endTime);

    /**
     * 根据渠道编号分组统计用户数
     * 
     * @param startTime >=
     * @param endTime   <=
     * @return
     */
    List<Map<String, Object>> getRegisterGroupByChannelCode(Date startTime, Date endTime);

    /**
     * 根据渠道编号和结束时间获取总用户数
     * 
     * @param channel
     * @param endTime
     * @return
     */
    Long getTotalRegisterByRegChannel(String channel, Date endTime);

    /**
     * 获取总用户数
     * 
     * @return
     */
    long getTotalRegister();

    /**
    * 根据手机号查询用户  用户不存在 创建一个用户
    * 
    * @param phoneNum
    * @return
    */
    RegisterModel getOrInsertRegisterByAccout(String phoneNum);

    /**
     * 分页查询
     * 
     * @param nickName
     * @param realName
     * @param phoneNum
     * @param platform
     * @param gender
     * @param regTerm
     * @param sort
     * @param isAsc
     * @param currentPage
     * @param pageSize
     * @return
     */

    UcenterPageResult<com.efida.easy.ucenter.facade.model.RegisterModel> queryRegisterModel(String nickName,
                                                                                            String realName,
                                                                                            String phoneNum,
                                                                                            PlatformEnum platform,
                                                                                            GenderEnum gender,
                                                                                            RegTerminalEnum regTerm,
                                                                                            String sort,
                                                                                            boolean isAsc,
                                                                                            int currentPage,
                                                                                            int pageSize);

}
