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
import com.efida.sport.dmp.facade.ScoreCertFacade;
import com.efida.sport.dmp.facade.exception.BusinessException;
import com.efida.sport.dmp.facade.model.ScoreCertModel;
import com.efida.sport.dmp.facade.result.DefaultResult;
import com.efida.sport.dmp.facade.result.DmpPageResult;
import com.efida.sports.service.dubbo.intergration.ScoreCertFacadeClient;

/**
 * 
 * @author zoutao
 * @version $Id: ScoreCertFacadeClientImpl.java, v 0.1 2018年8月6日 下午5:30:27 zoutao Exp $
 */
@Service
public class ScoreCertFacadeClientImpl implements ScoreCertFacadeClient {

    private static Logger   logger = LoggerFactory.getLogger(ScoreCertFacadeClientImpl.class);

    @Reference
    private ScoreCertFacade scoreCertFacade;

    @Override
    public DmpPageResult<ScoreCertModel> getRegisterPageScoreCert(String registerCode,
                                                                  int currentPage, int pageSize) {

        DefaultResult<DmpPageResult<ScoreCertModel>> dr = scoreCertFacade
            .getRegisterPageScoreCert(registerCode, currentPage, pageSize);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();

    }

    @Override
    public ScoreCertModel getRegisterNewestScoreCert(String registerCode) {
        DefaultResult<ScoreCertModel> dr = scoreCertFacade.getRegisterNewestScoreCert(registerCode);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public Boolean viewScoreCert(String certCode) {
        DefaultResult<Boolean> dr = scoreCertFacade.viewScoreCert(certCode);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public List<ScoreCertModel> getRegisterAllScoreCert(String registerCode) {
        DefaultResult<List<ScoreCertModel>> dr = scoreCertFacade
            .getRegisterAllScoreCert(registerCode);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

}
