/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.intergration.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.efida.sport.admin.facade.exception.BusinessException;
import com.efida.sport.dmp.facade.OpenEnrollFacade;
import com.efida.sport.dmp.facade.model.EnrollxInfoDto;
import com.efida.sport.dmp.facade.result.DefaultResult;
import com.efida.sports.service.dubbo.intergration.OpenEnrollFacadeClient;

/**
 * 
 * @author zhiyang
 * @version $Id: OpenEnrollFacadeClientImpl.java, v 0.1 2018年7月5日 下午9:45:59 zhiyang Exp $
 */
@Service("openEnrollFacadeClient")
public class OpenEnrollFacadeClientImpl implements OpenEnrollFacadeClient {
    /**
     * 
     */
    private static Logger    logger = LoggerFactory.getLogger(OpenEnrollFacadeClient.class);

    @Reference
    private OpenEnrollFacade openEnrollFacade;

    /**
     * 
     * @see com.efida.sports.service.dubbo.intergration.OpenEnrollFacadeClient#submitEnrollInfo(java.lang.String, com.efida.sport.dmp.facade.model.EnrollInfoDto)
     */
    @Override
    public Boolean submitEnrollInfo(String unitCode, EnrollxInfoDto enrollInfo) {

        DefaultResult<Boolean> rs = openEnrollFacade.submitEnrollInfo(unitCode, enrollInfo);
        if (!rs.isSuccess()) {
            logger.error("submit enrollInfo failed, applayCode" + enrollInfo.getIdempotentId()
                         + ", leaderPhone:" + enrollInfo.getLeaderPhone(),
                rs.getErrorInfo());
            throw new BusinessException(rs.getErrorMsg());
        }

        return rs.getResultObj();
    }

}
