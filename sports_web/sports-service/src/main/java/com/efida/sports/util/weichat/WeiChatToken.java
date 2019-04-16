/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.util.weichat;

import java.io.Serializable;

/**
 * 
 * @author zoutao
 * @version $Id: Token.java, v 0.1 2018年1月19日 下午4:19:28 zoutao Exp $
 */
public class WeiChatToken implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;
    // 接口访问凭证
    private String            accessToken;
    // 凭证有效期，单位：秒
    private int               expiresIn;
    // 用于刷新凭证
    private String            refreshToken;
    // 用户标识
    private String            openId;
    // 用户授权作用域
    private String            scope;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

}
