/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.esearch.facade.model;

import java.io.Serializable;

/**
 * 请求上下文信息。 
 * 包括安全验证信息及 模板信息 。
 * @author Lenovo
 * @version $Id: RequestContext.java, v 0.1 2018年9月25日 下午6:26:36 Lenovo Exp $
 */
public class RequestContext implements Serializable{

    /**
     * 调用者appId。 如 sports-dmp, sports-admin, sports-web
     */
    private String appId;
    /**
     * 模板编号
     */
    String tplCode;
    /**
     * 
     */
    private String appKey;
    /**
     * 随机字符串
     */
    private String timestamp;
    /**
     * md5(appKey+appSecrect+timestamp) 大写32bit Md5. 
     * appSecrect为appKey对应密钥
     */
    private String sign;
    /**
     * Getter method for property <tt>appId</tt>.
     * 
     * @return property value of appId
     */
    public String getAppId() {
        return appId;
    }
    /**
     * Setter method for property <tt>appId</tt>.
     * 
     * @param appId value to be assigned to property appId
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    /**
     * Getter method for property <tt>queryTplCode</tt>.
     * 
     * @return property value of queryTplCode
     */
    public String getTplCode() {
        return tplCode;
    }
    /**
     * Setter method for property <tt>queryTplCode</tt>.
     * 
     * @param tplCode value to be assigned to property queryTplCode
     */
    public void setTplCode(String tplCode) {
        this.tplCode = tplCode;
    }
    /**
     * Getter method for property <tt>appKey</tt>.
     * 
     * @return property value of appKey
     */
    public String getAppKey() {
        return appKey;
    }
    /**
     * Setter method for property <tt>appKey</tt>.
     * 
     * @param appKey value to be assigned to property appKey
     */
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    /**
     * Getter method for property <tt>timestamp</tt>.
     * 
     * @return property value of timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }
    /**
     * Setter method for property <tt>timestamp</tt>.
     * 
     * @param timestamp value to be assigned to property timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    /**
     * Getter method for property <tt>sign</tt>.
     * 
     * @return property value of sign
     */
    public String getSign() {
        return sign;
    }
    /**
     * Setter method for property <tt>sign</tt>.
     * 
     * @param sign value to be assigned to property sign
     */
    public void setSign(String sign) {
        this.sign = sign;
    }
    
}
