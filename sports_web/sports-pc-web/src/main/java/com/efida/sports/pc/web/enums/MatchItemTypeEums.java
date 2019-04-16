/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.pc.web.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 比赛项类型枚举
 * 
 * @author zengbo
 * @version $Id: MatchItemTypeEums.java, v 0.1 2018年7月24日 下午3:16:24 zengbo Exp $
 */
public enum MatchItemTypeEums {
                               /**
                                * 个人
                                */
                               PERSONAL("personal", "个人"),
                               /**
                                * 团体
                                */
                               GROUP("group", "团体");

    /**  */
    private String code;
    /**  */
    private String desc;

    /**
     * @param code
     * @param desc
     */
    private MatchItemTypeEums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Getter method for property <tt>code</tt>.
     * 
     * @return property value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter method for property <tt>code</tt>.
     * 
     * @param code value to be assigned to property code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Getter method for property <tt>desc</tt>.
     * 
     * @return property value of desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Setter method for property <tt>desc</tt>.
     * 
     * @param desc value to be assigned to property desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 
     * @param code
     * @return
     */
    /**
     * 
     * @param code
     * @return
     */
    public static MatchItemTypeEums getEnumByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            MatchItemTypeEums[] cardStatus = MatchItemTypeEums.values();
            for (MatchItemTypeEums status : cardStatus) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
        }
        return null;
    }

}
