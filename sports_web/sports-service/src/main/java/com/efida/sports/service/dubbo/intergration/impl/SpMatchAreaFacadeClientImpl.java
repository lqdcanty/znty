/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.intergration.impl;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.efida.sport.admin.facade.SpMatchAreaFacade;
import com.efida.sport.admin.facade.model.MatchAreaModel;
import com.efida.sport.admin.facade.model.SpPlayingAreaModel;
import com.efida.sport.admin.facade.model.page.SportsAdminPageResult;
import com.efida.sport.admin.facade.result.SportAdminResult;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.dubbo.intergration.SpMatchAreaFacadeClient;

/**
 * 
 * @author zoutao
 * @version $Id: SpMatchAreaFacadeClientImpl.java, v 0.1 2018年6月29日 下午5:11:33 zoutao Exp $
 */
@Service
public class SpMatchAreaFacadeClientImpl implements SpMatchAreaFacadeClient {
    @Reference
    private SpMatchAreaFacade spMatchAreaFacade;

    @Override
    public List<MatchAreaModel> getMatchAreas(String matchCode, String areaType,
                                              String parentArea) {

        SportAdminResult<List<MatchAreaModel>> rs = spMatchAreaFacade.getMatchAreas(matchCode,
            areaType, parentArea);
        if (!rs.isSuccess()) {
            throw new BusinessException(rs.getErrorMsg());
        }
        return rs.getResultObj();
    }

    @Override
    public SportsAdminPageResult<SpPlayingAreaModel> getMatchSites(String matchCode,
                                                                   String areaType, String areaName,
                                                                   BigDecimal curLongitude,
                                                                   BigDecimal curLatitude, int page,
                                                                   int pageSize) {

        SportAdminResult<SportsAdminPageResult<SpPlayingAreaModel>> rs = spMatchAreaFacade
            .getPlayingAreas(matchCode, areaName, areaType, curLongitude, curLatitude, page,
                pageSize);
        if (!rs.isSuccess()) {
            throw new BusinessException(rs.getErrorMsg());
        }
        return rs.getResultObj();
    }
}
