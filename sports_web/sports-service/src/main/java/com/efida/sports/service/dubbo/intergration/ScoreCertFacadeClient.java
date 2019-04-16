/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.intergration;

import java.util.List;

import com.efida.sport.dmp.facade.model.ScoreCertModel;
import com.efida.sport.dmp.facade.result.DmpPageResult;

/**
 * 
 * @author zoutao
 * @version $Id: ScoreCertFacadeClient.java, v 0.1 2018年8月6日 下午5:30:09 zoutao Exp $
 */
public interface ScoreCertFacadeClient {
    /**
     * 获取用户的证书
     * 获取已查看和未查看的证书
     *  分页查询
     * @param registerCode
     * @param currentPage
     * @param pageSize
     * @return
     */
    public DmpPageResult<ScoreCertModel> getRegisterPageScoreCert(String registerCode,
                                                                  int currentPage, int pageSize);

    /**
     * 获取用户最新未查看的证书
     * 
     * @param registerCode
     * @return
     */
    public ScoreCertModel getRegisterNewestScoreCert(String registerCode);

    /**
     * 
     * 查看证书（标识该证书我已查看）
     * @param certCode
     * @return
     */
    public Boolean viewScoreCert(String certCode);

    /**
     * 获取用户所有证书
     * 
     * @param registerCode
     * @return
     */
    List<ScoreCertModel> getRegisterAllScoreCert(String registerCode);
}
