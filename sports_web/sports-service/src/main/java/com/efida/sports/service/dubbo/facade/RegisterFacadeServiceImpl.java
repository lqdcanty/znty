/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.facade;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.efida.easy.ucenter.facade.result.UcenterPageResult;
import com.efida.sport.admin.facade.exception.BusinessException;
import com.efida.sport.facade.enums.GenderEnum;
import com.efida.sport.facade.enums.PlatformEnum;
import com.efida.sport.facade.enums.RegTerminalEnum;
import com.efida.sport.facade.model.DefaultResult;
import com.efida.sport.facade.model.PagingResult;
import com.efida.sport.facade.model.RegisterModel;
import com.efida.sport.facade.service.RegisterFacadeService;
import com.efida.sports.service.dubbo.facade.cover.RegisterCover;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;

/**
 * 
 * @author zoutao
 * @version $Id: RegisterFacadeServiceImpl.java, v 0.1 2018年8月5日 下午2:00:05 zoutao Exp $
 */
@Service
public class RegisterFacadeServiceImpl implements RegisterFacadeService {

    @Autowired
    private UcenterRegisterFacadeClient ucenterRegisterFacadeClient;

    @Override
    public DefaultResult<RegisterModel> getOrInsertRegisterByPhone(String phoneNum) {

        DefaultResult<RegisterModel> result = new DefaultResult<RegisterModel>();
        try {
            if (StringUtils.isBlank(phoneNum)) {
                throw new BusinessException("电话号码不能为空");
            }
            com.efida.easy.ucenter.facade.model.RegisterModel register = ucenterRegisterFacadeClient
                .getOrInsertRegisterByAccout(phoneNum);
            result.setSuccess(true);
            result.setResultObj(RegisterCover.entity2model(register));
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setErrorCode(e.getMessage());
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorCode("获取用户失败");
        }

        return result;
    }

    @Override
    public DefaultResult<PagingResult<RegisterModel>> queryRegisterModel(String nickName,
                                                                         String realName,
                                                                         String phoneNum,
                                                                         PlatformEnum platform,
                                                                         GenderEnum gender,
                                                                         RegTerminalEnum regTerm,
                                                                         String sort, boolean isAsc,
                                                                         int currentPage,
                                                                         int PageSize) {
        DefaultResult<PagingResult<RegisterModel>> result = new DefaultResult<PagingResult<RegisterModel>>();
        try {
            UcenterPageResult<com.efida.easy.ucenter.facade.model.RegisterModel> page = ucenterRegisterFacadeClient
                .queryRegisterModel(nickName, realName, phoneNum, platform, gender, regTerm, sort,
                    isAsc, currentPage, PageSize);
            List<com.efida.easy.ucenter.facade.model.RegisterModel> records = page.getList();
            PagingResult<RegisterModel> pagingResult = new PagingResult<RegisterModel>(
                RegisterCover.ucenterModel2models(records), page.getAllRow(), currentPage,
                PageSize);
            result.setSuccess(true);
            result.setResultObj(pagingResult);
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setErrorCode(e.getMessage());
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorCode("获取用户列表失败");
        }

        return result;
    }

}
