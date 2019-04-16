/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.intergration.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.efida.sport.admin.facade.SpAppVersionFacade;
import com.efida.sport.admin.facade.exception.BusinessException;
import com.efida.sport.admin.facade.model.SpAppVersionModel;
import com.efida.sport.dmp.facade.OpenEnrollFacade;
import com.efida.sport.dmp.facade.model.EnrollxInfoDto;
import com.efida.sport.dmp.facade.result.DefaultResult;
import com.efida.sports.service.dubbo.intergration.OpenEnrollFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpAppVersionFacadeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 
 * @author antony
 * @version $Id: SpAppVersionFacadeClientImpl.java, v 0.1 2018年08月24日 下午18:02:59 antony Exp $
 */
@Service("spAppVersionFacadeClient")
public class SpAppVersionFacadeClientImpl implements SpAppVersionFacadeClient {

    private static Logger logger = LoggerFactory.getLogger(OpenEnrollFacadeClient.class);

    @Reference
    private SpAppVersionFacade spAppVersionFacade;

    @Override
    public SpAppVersionModel getAppVersionByAppType(String appType) {
        return spAppVersionFacade.getAppVersionByType(appType);
    }

}
