/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.model;

/**
 * 
 * @author zoutao
 * @version $Id: WeichatUnifiedModel.java, v 0.1 2018年3月28日 下午12:40:44 zoutao Exp $
 * 微信统一下单对象
 */
public class WeichatUnifiedModel {
    private String appId;
    private String nonceStr;
    private String prepayId;
    private String sign;
    private String timeStamp;
    private String orderSeq;
    private String packageStr;
    private String mwebUrl;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(String orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getPackageStr() {
        return packageStr;
    }

    public void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }

    public String getMwebUrl() {
        return mwebUrl;
    }

    public void setMwebUrl(String mwebUrl) {
        this.mwebUrl = mwebUrl;
    }
}
