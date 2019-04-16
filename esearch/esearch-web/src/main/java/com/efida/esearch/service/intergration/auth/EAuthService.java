/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.esearch.service.intergration.auth;

import cn.evake.auth.dubbo.open.model.OpenUserModel;

/**
 * 
 * 开放鉴权接口
 * @author wang yi
 * @desc 
 * @version $Id: OpenAuthFacade.java, v 0.1 2018年8月2日 下午2:46:37 wang yi Exp $
 */
public interface EAuthService {

    /**
     * 获取登录用户信息
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param uuid  用户UID (必填)
     * @param authToken 用户token (必填)
     * @return
     */
    OpenUserModel getLoginUserInfo(String uuid, String authToken);

    /**
     * 检查用户是否登录
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param uuid 用户UID (必填)
     * @param authToken 用户token (必填)
     * @return
     */
    boolean checkIsLogin(String uuid, String authToken);

    /**
     * 检查登录用户授权码是否可使用
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param uuid 用户UID (必填)
     * @param permissionCode 访问授权码 (必填)
     * @return
     */
    boolean checkIsLimit(String uuid, String permissionCode);

}
