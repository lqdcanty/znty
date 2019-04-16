/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.esearch.enmus;

/**
 *应用审核状态
 * @author wang yi
 * @desc 
 * @version $Id: AppAuditEnmu.java, v 0.1 2018年10月8日 下午3:07:36 wang yi Exp $
 */
public enum DocTplAuditEnmu {

                             WAIT_AUDIT("wait_audit", "待审批"), REJECT("reject", "驳回"), PASS("pass",
                                                                                           "审批通过");

    /**  */
    private String code;
    /**  */
    private String desc;

    /**
     * 
     * @param code
     * @return
     */
    public static String getDescByCode(String code) {
        DocTplAuditEnmu[] cardStatus = DocTplAuditEnmu.values();
        for (DocTplAuditEnmu status : cardStatus) {
            if (status.getCode().equals(code)) {
                return status.getDesc();
            }
        }
        return null;
    }

    /**
     * @param code
     * @return
     */
    public static DocTplAuditEnmu getEnumByCode(String code) {
        DocTplAuditEnmu[] cardStatus = DocTplAuditEnmu.values();
        for (DocTplAuditEnmu status : cardStatus) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * @param code
     * @param desc
     */
    private DocTplAuditEnmu(String code, String desc) {
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

}
