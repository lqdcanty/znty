/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.intergration.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.efida.easy.ucenter.facade.enums.TerminalEnum;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.easy.ucenter.facade.result.UcenterDefaultResult;
import com.efida.easy.ucenter.facade.result.UcenterPageResult;
import com.efida.easy.ucenter.facade.service.UcenterRegisterFacadeService;
import com.efida.sport.dmp.facade.exception.BusinessException;
import com.efida.sport.facade.enums.GenderEnum;
import com.efida.sport.facade.enums.PlatformEnum;
import com.efida.sport.facade.enums.RegTerminalEnum;
import com.efida.sports.service.MsgService;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;

/**
 * 
 * @author zoutao
 * @version $Id: RegisterFacadeClientImpl.java, v 0.1 2018年10月10日 下午3:22:06 zoutao Exp $
 */
@Service
public class UcenterRegisterFacadeClientImpl implements UcenterRegisterFacadeClient {
    @Reference
    private UcenterRegisterFacadeService registerFacadeService;
    @Autowired
    private MsgService                   msgService;

    @Override
    public RegisterModel getRegsiterByRegisterCode(String registerCode) {
        UcenterDefaultResult<RegisterModel> dr = registerFacadeService
            .getRegisterByCode(registerCode);

        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public RegisterModel bindPhone(RegisterModel register, String phoneNum, String verifyCode) {
        boolean checkVerifyCode = msgService.checkVerifyCode(phoneNum, verifyCode);
        if (!checkVerifyCode) {
            throw new BusinessException("验证码错误");
        }
        UcenterDefaultResult<RegisterModel> dr = registerFacadeService
            .bindPhone(register.getRegisterCode(), phoneNum);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public RegisterModel updateRegsiterInfo(RegisterModel register) {
        UcenterDefaultResult<RegisterModel> dr = registerFacadeService.updateRegsiterInfo(register);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public RegisterModel createRegsiter(RegisterModel register) {
        UcenterDefaultResult<RegisterModel> dr = registerFacadeService.createRegsiter(register);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();

    }

    @Override
    public RegisterModel getRegsiterByAccount(String phoneNum) {
        UcenterDefaultResult<RegisterModel> dr = registerFacadeService
            .getRegsiterByAccount(phoneNum);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public RegisterModel getFirstRegRegister() {
        UcenterDefaultResult<RegisterModel> dr = registerFacadeService.getFirstRegRegister();
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public List<Map<String, Object>> getRegisterGroupByRegTerminal(Date startTime, Date endTime) {
        UcenterDefaultResult<List<Map<String, Object>>> dr = registerFacadeService
            .getRegisterGroupByRegTerminal(startTime, endTime);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public Long getTotalRegisterByRegTerminal(String regTerminal, Date endTime) {

        UcenterDefaultResult<Long> dr = registerFacadeService
            .getTotalRegisterByRegTerminal(regTerminal, endTime);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public List<Map<String, Object>> getRegisterGroupByChannelCode(Date startTime, Date endTime) {

        UcenterDefaultResult<List<Map<String, Object>>> dr = registerFacadeService
            .getRegisterGroupByChannelCode(startTime, endTime);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public Long getTotalRegisterByRegChannel(String channel, Date endTime) {

        UcenterDefaultResult<Long> dr = registerFacadeService.getTotalRegisterByRegChannel(channel,
            endTime);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public long getTotalRegister() {

        UcenterDefaultResult<Long> dr = registerFacadeService.getTotalRegister();
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public RegisterModel getOrInsertRegisterByAccout(String phoneNum) {
        UcenterDefaultResult<RegisterModel> dr = registerFacadeService
            .getOrInsertRegisterByAccout(phoneNum);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public UcenterPageResult<RegisterModel> queryRegisterModel(String nickName, String realName,
                                                               String phoneNum,
                                                               PlatformEnum platform,
                                                               GenderEnum gender,
                                                               RegTerminalEnum regTerm, String sort,
                                                               boolean isAsc, int currentPage,
                                                               int pageSize) {
        com.efida.easy.ucenter.facade.enums.PlatformEnum plat = null;
        if (platform != null) {
            plat = com.efida.easy.ucenter.facade.enums.PlatformEnum
                .getEnumByCode(platform.getCode());
        }
        com.efida.easy.ucenter.facade.enums.GenderEnum genderEnum = null;
        if (gender != null) {
            genderEnum = com.efida.easy.ucenter.facade.enums.GenderEnum
                .getEnumByCode(gender.getCode());
        }
        TerminalEnum term = null;
        if (regTerm != null) {
            term = TerminalEnum.getEnumByCode(regTerm.getCode());
        }
        UcenterDefaultResult<UcenterPageResult<RegisterModel>> dr = registerFacadeService
            .queryRegisterModel(nickName, realName, phoneNum, plat, genderEnum, term, sort, isAsc,
                currentPage, pageSize);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

}
