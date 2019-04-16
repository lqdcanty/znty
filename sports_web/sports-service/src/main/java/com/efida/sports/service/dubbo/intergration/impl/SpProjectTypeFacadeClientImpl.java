/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.intergration.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.efida.sport.admin.facade.SpProjectTypeFacade;
import com.efida.sport.admin.facade.exception.BusinessException;
import com.efida.sport.admin.facade.model.SpProjectTypeModel;
import com.efida.sport.admin.facade.result.SportAdminResult;
import com.efida.sports.service.dubbo.intergration.SpProjectTypeFacadeClient;

/**
 * 
 * @author zoutao
 * @version $Id: SpProjectTypeFacadeClient.java, v 0.1 2018年5月23日 下午6:16:50 zoutao Exp $
 */
@Service("spProjectTypeFacadeClient")
public class SpProjectTypeFacadeClientImpl implements SpProjectTypeFacadeClient {

    @Reference
    private SpProjectTypeFacade      spProjectTypeFacade;
    private static Logger            log             = LoggerFactory
        .getLogger(SpProjectTypeFacadeClientImpl.class);

    private long                     lastRefreshTime = 0;
    private List<SpProjectTypeModel> enableProjects  = null;

    @Override
    public List<SpProjectTypeModel> getProjectTypes() {
        if (System.currentTimeMillis() - lastRefreshTime < 5000) {
            return this.enableProjects;
        }
        SportAdminResult<List<SpProjectTypeModel>> rs = spProjectTypeFacade.getEnableSpProjects();
        if (log.isDebugEnabled()) {
            log.debug("查询所有有效项目分类,返回结果：" + JSON.toJSONString(rs));
        }
        if (!rs.isSuccess()) {
            throw new BusinessException(rs.getErrorMsg());
        }
        this.enableProjects = rs.getResultObj();
        lastRefreshTime = System.currentTimeMillis();
        return rs.getResultObj();
    }

    @Override
    public List<SpProjectTypeModel> getAllProjectTypes() {

        return getProjectTypes();
    }

    @Override
    public SpProjectTypeModel getSpProjectByCode(String gameCode) {

        SportAdminResult<SpProjectTypeModel> rs = spProjectTypeFacade.getSpProjectByCode(gameCode);
        if (log.isDebugEnabled()) {
            log.debug("查询项目类型: " + gameCode + "  ,返回结果为:" + JSON.toJSONString(rs));
        }
        if (!rs.isSuccess()) {
            throw new BusinessException(rs.getErrorMsg());
        }
        return rs.getResultObj();

    }
}
