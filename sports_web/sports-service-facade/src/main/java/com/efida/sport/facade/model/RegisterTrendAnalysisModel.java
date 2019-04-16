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
 * @version $Id: RegisterTrendAnalysisModel.java, v 0.1 2018年8月29日 下午8:01:04 zoutao Exp $
 */
public class RegisterTrendAnalysisModel implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;
    /**
     * 时间
     */
    private Date              date;
    /**
     * 格式化后得时间
     */
    private String            dateStr;
    /**
     * 新增用户数
     */
    private long              newRegister;
    /**
     * 总用户数
     */
    private long              totalRregister;

    /**
     * 渠道编号
     */

    private String            channelCode;
    /**
     * 渠道名称
     */
    private String            channelName;

    /**
     * 占比
     */
    private String            proportion;

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

    public long getNewRegister() {
        return newRegister;
    }

    public void setNewRegister(long newRegister) {
        this.newRegister = newRegister;
    }

    public long getTotalRregister() {
        return totalRregister;
    }

    public void setTotalRregister(long totalRregister) {
        this.totalRregister = totalRregister;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

}
