/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.esearch.service.intergration.auth;

import cn.evake.auth.dubbo.open.model.OpenUserModel;

/**
 * 开放用户接口
 * @author wang yi
 * @desc 
 * @version $Id: OpenUserFacade.java, v 0.1 2018年8月2日 下午2:46:58 wang yi Exp $
 */
public interface EUserService {

    /**
     * 获取用户信息
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param uuid 用户UID (必填)
     * @return
     */
    OpenUserModel getUserInfo(String uuid);
}
