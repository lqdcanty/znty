/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sport.facade.model;

import java.io.Serializable;
import java.util.Date;

import com.efida.sport.facade.enums.RegTerminalEnum;

/**
 * 
 * @author zoutao
 * @version $Id: TerminaStatisticsModel.java, v 0.1 2018年9月12日 下午3:09:24 zoutao Exp $
 * 终端统计对象
 */

public class TerminaStatisticsModel implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;
    /**
     * 终端类型
     * @see RegTerminalEnum
     */
    private String            terminaType;
    /**
     * 终端名称
     */
    private String            terminaName;
    /**
    * 
    */
    private Date              date;
    /**
    * 格式化后时间
    */
    private String            dateStr;
    /**
    * 注册总数
    */
    private long              regCount;
    /**
    * 活跃用户数
    */
    private long              activeCount;

    public String getTerminaType() {
        return terminaType;
    }

    public void setTerminaType(String terminaType) {
        this.terminaType = terminaType;
    }

    public String getTerminaName() {
        return terminaName;
    }

    public void setTerminaName(String terminaName) {
        this.terminaName = terminaName;
    }

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

    public long getRegCount() {
        return regCount;
    }

    public void setRegCount(long regCount) {
        this.regCount = regCount;
    }

    public long getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(long activeCount) {
        this.activeCount = activeCount;
    }

}
