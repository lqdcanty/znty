/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sport.facade.enums;

/**
 * 
 * @author zoutao
 * @version $Id: PlatformEnum.java, v 0.1 2018年6月14日 下午1:07:16 zoutao Exp $
 * 平台来源
 */
public enum PlatformEnum {
                          /**
                           * 微信
                           */
                          WEICHAT("weichat", "微信"),

                          /**
                          * 腾讯QQ
                          */
                          QQ("qq", "腾讯QQ"),

                          /**
                          * 微博
                          */
                          SINA("sina", "微博");

    private String code;
    private String desc;

    private PlatformEnum(String code, String desc) {
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
        PlatformEnum[] values = PlatformEnum.values();
        for (PlatformEnum em : values) {
            if (em.getCode().equalsIgnoreCase(code)) {
                return em.getDesc();
            }
        }
        return "";
    }

}
