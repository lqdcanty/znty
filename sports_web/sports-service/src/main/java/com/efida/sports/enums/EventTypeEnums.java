/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.enums;

/**
 * 
 * @author zoutao
 * @version $Id: EventTypeEnums.java, v 0.1 2018年7月24日 下午2:29:51 zoutao Exp $
 * 比赛项类型  个人 团体
 */
public enum EventTypeEnums {
                            /**
                             * 个人
                             */
                            personal("personal", "个人赛"),
                            /**
                             * 团体
                             */
                            group("group", "团体赛");

    private String code;
    private String desc;

    private EventTypeEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        for (EventTypeEnums type : EventTypeEnums.values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }
}