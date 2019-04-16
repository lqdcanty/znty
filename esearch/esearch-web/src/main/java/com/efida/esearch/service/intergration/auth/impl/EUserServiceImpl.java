/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.esearch.service.intergration.auth.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.efida.esearch.exception.EbusinessException;
import com.efida.esearch.service.intergration.auth.EUserService;

import cn.evake.auth.dubbo.open.facade.OpenUserFacade;
import cn.evake.auth.dubbo.open.model.OpenUserModel;
import cn.evake.auth.dubbo.open.result.OpenAuthResult;

/**
 * 
 * @author wang yi
 * @desc 
 * @version $Id: EUserServiceImpl.java, v 0.1 2018年9月27日 下午3:19:04 wang yi Exp $
 */
@Service
public class EUserServiceImpl implements EUserService {

    /**
     * 是否开启鉴权
     */
    @Value(value = "${auth.webAuth}")
    private boolean        isAuth = false;

    @Reference
    private OpenUserFacade userFacade;

    private Logger         logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public OpenUserModel getUserInfo(String uuid) {
        try {
            OpenAuthResult<OpenUserModel> userInfo = userFacade.getUserInfo(uuid);
            if (userInfo.isSuccess()) {
                return userInfo.getResultObj();
            }
        } catch (Exception e) {
            String errMsg = "获取用户信息失败";
            logger.error(errMsg, e);
            throw new EbusinessException(errMsg);
        }
        return null;
    }

}
