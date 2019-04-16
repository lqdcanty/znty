package com.efida.sport.facade.enums;

/**
 * 性别
 * 
 * @author Administrator
 * 
 */
public enum SexEnum {

                     male("m", "男"),

                     female("f", "女"),

                     unknown("o", "未知");

    private String code;
    private String desc;

    private SexEnum(String code, String desc) {
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
    	if(code==null||"".equals(code)){
    	    return "";
    	}
        for (SexEnum type : SexEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }
}