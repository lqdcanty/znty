package com.efida.sports.enums;

/**
 * 性别
 * 
 * @author Administrator
 * 
 */
public enum GenderEnum {

                        male("1", "男"),

                        female("2", "女"),

                        unknown("0", "未知");

    private String code;
    private String desc;

    private GenderEnum(String code, String desc) {
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
        for (GenderEnum type : GenderEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }
}