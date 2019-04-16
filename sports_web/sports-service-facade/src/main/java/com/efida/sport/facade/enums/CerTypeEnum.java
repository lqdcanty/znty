/**
 * 
 */
package com.efida.sport.facade.enums;


/**
 * 证件类型
 * 
 * @author zengbo
 * @version $Id: IdCardTypeEnum.java, v 0.1 2018年7月4日 下午2:21:00 zengbo Exp $
 */
public enum CerTypeEnum {
                             /**
                             * 身份证
                             */
                            ID_CARD("1", "身份证"),
                             /**
                             * 护照
                             */
                            PASSPORT("2", "护照"),
                             /**
                              * 驾照
                              */
                            DRIVING_LICENSE("3", "驾照"),
                            /**
                             * 军官证
                             */
                            CERTIFICATE_OFFICERS("4", "军官证");


    private String code;
    private String cname;

    private CerTypeEnum(String code, String cname) {
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
        if (code==null || "".equals(code)) {
            return "";
        }
        CerTypeEnum[] values = CerTypeEnum.values();
        for (CerTypeEnum em : values) {
            if (em.getCode().equalsIgnoreCase(code)) {
                return em.getCname();
            }
        }
        return "";
    }

}
