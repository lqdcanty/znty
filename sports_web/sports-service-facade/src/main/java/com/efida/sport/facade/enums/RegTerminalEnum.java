/**
 * 
 */
package com.efida.sport.facade.enums;

/**
 * @author Administrator
 * 注册来源 
 */
public enum RegTerminalEnum {
                             /**
                              * pc
                              */
                             PC("pc", "pc"),
                             /**
                              * 安卓
                              */
                             ANDROID("android", "android"),
                             /**
                              * ios
                              */
                             IOS("ios", "ios"),
                             /**
                              * dmp
                              */
                             DMP("dmp", "dmp"),
                            /**
                             *
                             */
                            apph5("apph5", "APP内嵌H5"),
                             /**
                               * 微信
                               */
                             WEICHAT("weichat", "微信公众号");

    private String code;
    private String cname;

    private RegTerminalEnum(String code, String cname) {
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
        RegTerminalEnum[] values = RegTerminalEnum.values();
        for (RegTerminalEnum em : values) {
            if (em.getCode().equalsIgnoreCase(code)) {
                return em.getCname();
            }
        }
        return "";
    }

}
