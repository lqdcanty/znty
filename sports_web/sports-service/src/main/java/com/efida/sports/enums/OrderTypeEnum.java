/**
 * 
 */
package com.efida.sports.enums;

/**
 * @author Administrator
 *
 */
public enum OrderTypeEnum {

                           /**
                            * 报名
                            */
                           APPLY("apply", "报名"),

                           /**
                            * 商城
                            */
                           mall("mall", "商城");

    private String code;
    private String cname;

    private OrderTypeEnum(String code, String cname) {
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

}
