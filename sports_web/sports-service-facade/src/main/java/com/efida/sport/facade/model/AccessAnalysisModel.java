/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sport.facade.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author zoutao
 * @version $Id: AccessAnalysisModel.java, v 0.1 2018年9月11日 下午9:19:30 zoutao Exp $
 * 访问分析
 */
public class AccessAnalysisModel implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    private Date              date;

    private String            dateStr;

    private long              accessCount;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(long accessCount) {
        this.accessCount = accessCount;
    }

}
