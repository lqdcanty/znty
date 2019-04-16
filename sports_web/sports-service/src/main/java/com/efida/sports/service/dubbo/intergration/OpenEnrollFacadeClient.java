/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.intergration;

import com.efida.sport.dmp.facade.model.EnrollxInfoDto;

/**
 * 
 * @author zhiyang
 * @version $Id: OpenEnrollFacadeClient.java, v 0.1 2018年7月5日 下午9:45:23 zhiyang Exp $
 */
public interface OpenEnrollFacadeClient {

    /**
     * 提交报名数据到dmp平台
     * 
     * @param unitCode
     * @param enrollInfo
     * @return
     */
    public Boolean submitEnrollInfo(String unitCode, EnrollxInfoDto enrollInfo);

}
