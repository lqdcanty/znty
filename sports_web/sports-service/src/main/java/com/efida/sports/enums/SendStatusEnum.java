package com.efida.sports.enums;

/**
 * 发送状态
 * 
 * @author zengbo
 * @version $Id: SendStatusEnum.java, v 0.1 2018年7月5日 下午4:16:33 zengbo Exp $
 */
public enum SendStatusEnum {

                            UNSENT("unsent", "未发送"),

                            SUCCESS("success", "成功"),

                            FAIL("fail", "失败");

    private String code;
    private String desc;

    private SendStatusEnum(String code, String desc) {
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
        for (SendStatusEnum type : SendStatusEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }
}