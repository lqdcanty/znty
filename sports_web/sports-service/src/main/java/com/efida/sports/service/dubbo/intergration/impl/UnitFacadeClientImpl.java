/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.intergration.impl;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.efida.sport.dmp.facade.OpenUnitFacade;
import com.efida.sport.dmp.facade.exception.BusinessException;
import com.efida.sport.dmp.facade.model.UnitAccountDto;
import com.efida.sport.dmp.facade.result.DefaultResult;
import com.efida.sport.dmp.facade.result.DmpPageResult;
import com.efida.sports.service.dubbo.intergration.UnitFacadeClient;

/**
 * 
 * @author zoutao
 * @version $Id: UnitFacadeClientImpl.java, v 0.1 2018年8月29日 下午7:09:40 zoutao Exp $
 */
@Service
public class UnitFacadeClientImpl implements UnitFacadeClient {
    @Reference
    private OpenUnitFacade unitFacade;

    @Override
    public DmpPageResult<UnitAccountDto> getList(String keyword, int currentPage, int pageSzie) {
        DefaultResult<DmpPageResult<UnitAccountDto>> dr = unitFacade.getUnitAccounts(keyword,
            currentPage, pageSzie);
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

}
