/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author zoutao
 * @version $Id: PayGateWayEnum.java, v 0.1 2018年2月8日 下午4:47:02 zoutao Exp $
 */
public enum PayGateWayEnum {
                            /**
                             * 支付宝
                             */
                            ALIPAY("alipay", "支付宝"),
                            /**
                             * 微信
                             */
                            WEICHAT_PAY("weichat_pay", "微信支付"),
                            /**
                             * 信用支付
                             */
                            CREDIT_PAY("credit_pay", "信用支付");

    private String code;
    private String cname;

    private PayGateWayEnum(String code, String cname) {
        this.code = code;
        this.cname = cname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public static String getDescByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        PayGateWayEnum[] values = PayGateWayEnum.values();
        for (PayGateWayEnum em : values) {
            if (em.getCode().equalsIgnoreCase(code)) {
                return em.getCname();
            }
        }
        return "";
    }

}
