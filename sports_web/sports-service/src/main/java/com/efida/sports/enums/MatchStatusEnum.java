package com.efida.sports.enums;

/**
 * 赛事状态枚举
 * 
 * @author zengbo
 * @version $Id: MatchStatusEnum.java, v 0.1 2018年6月22日 下午4:20:27 zengbo Exp $
 */
public enum MatchStatusEnum {

                             PAUSE("pause", "暂停报名"),

                             GOIND("going", "报名中"),

                             ABORT("abort", "报名已截止"),

                             END("end", "已结束");

    private String code;
    private String desc;

    private MatchStatusEnum(String code, String desc) {
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
        for (MatchStatusEnum type : MatchStatusEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }
}