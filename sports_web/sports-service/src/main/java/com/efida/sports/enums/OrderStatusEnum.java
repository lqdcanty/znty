/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author zoutao
 * @version $Id: TradeCategory.java, v 0.1 2018年2月3日 下午3:54:59 zoutao Exp $
 */
public enum OrderStatusEnum {

                             /**
                              * 待支付
                              */
                             WAIT_PAY("wait_pay", "待支付"),

                             /**处理中
                              */
                             WAIT_COMPLETE("wait_complete", "待完善"),
                             /**
                              * 成功
                              */
                             SUCCESS("success", "成功"),
                             /**
                              * 已发货 - 奖章订单使用
                              */
                             SHIPPED("shipped", "已发货"),
                             /**
                              * 已完成 - 奖章订单使用
                              */
                             COMPLETED("completed", "已完成"),
                             /**
                              * 失败
                              */
                             FAIL("fail", "失败"),
                             /**
                              * 废弃
                              */
                             CANCEL("cancel", "取消");

    private String code;
    private String cname;

    private OrderStatusEnum(String code, String cname) {
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
        OrderStatusEnum[] values = OrderStatusEnum.values();
        for (OrderStatusEnum em : values) {
            if (code.equals(em.getCode())) {
                return em.getCname();
            }
        }
        return "";
    }

    public static OrderStatusEnum getEnumByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        OrderStatusEnum[] values = OrderStatusEnum.values();
        for (OrderStatusEnum em : values) {
            if (code.equals(em.getCode())) {
                return em;
            }
        }
        return null;
    }

}
