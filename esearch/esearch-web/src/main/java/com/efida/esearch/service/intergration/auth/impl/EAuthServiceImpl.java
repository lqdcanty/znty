/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.esearch.service.intergration.auth.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.efida.esearch.exception.EbusinessException;
import com.efida.esearch.service.intergration.auth.EAuthService;

import cn.evake.auth.dubbo.open.facade.OpenAuthFacade;
import cn.evake.auth.dubbo.open.model.OpenUserModel;
import cn.evake.auth.dubbo.open.result.OpenAuthResult;

/**
 * 
 * @author wang yi
 * @desc 
 * @version $Id: EAuthServiceImpl.java, v 0.1 2018年9月27日 下午3:17:19 wang yi Exp $
 */
@Service
public class EAuthServiceImpl implements EAuthService {

    @Reference
    private OpenAuthFacade authFacade;

    private Logger         logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public OpenUserModel getLoginUserInfo(String uuid, String authToken) {
        try {
            OpenAuthResult<OpenUserModel> loginUserInfo = authFacade.getLoginUserInfo(uuid,
                authToken);
            if (loginUserInfo.isSuccess()) {
                return loginUserInfo.getResultObj();
            }
        } catch (Exception e) {
            String errMsg = "获取登录用户信息错误";
            logger.error(errMsg, e);
            throw new EbusinessException(errMsg);
        }
        return null;
    }

    @Override
    public boolean checkIsLogin(String uuid, String authToken) {
        try {
            OpenAuthResult<Boolean> checkIsLogin = authFacade.checkIsLogin(uuid, authToken);
            if (checkIsLogin.isSuccess()) {
                return checkIsLogin.getResultObj();
            }
        } catch (Exception e) {
            String errMsg = "检查用户是否登录错误";
            logger.error(errMsg, e);
            throw new EbusinessException(errMsg);
        }
        return false;
    }

    @Override
    public boolean checkIsLimit(String uuid, String permissionCode) {
        try {
            OpenAuthResult<Boolean> checkIsLimit = authFacade.checkIsLimit(uuid, permissionCode);
            if (checkIsLimit.isSuccess()) {
                return checkIsLimit.getResultObj();
            }
        } catch (Exception e) {
            String errMsg = "检查用户授权错误";
            logger.error(errMsg, e);
            throw new EbusinessException(errMsg);
        }
        return false;
    }

}
