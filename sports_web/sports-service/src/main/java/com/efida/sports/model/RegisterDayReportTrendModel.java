/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.model;

import java.util.Date;

/**
 * 
 * @author zoutao
 * @version $Id: RegisterDayReportTrendModel.java, v 0.1 2018年8月29日 下午11:55:27 zoutao Exp $
 */
public class RegisterDayReportTrendModel {
    /**
     * 时间
     */
    private Date   date;
    /**
     * 新增用户数
     */
    private long   newRegister;
    /**
     * 总用户数
     */
    private long   totalRregister;

    /**
     * 渠道编号
     */

    private String channelCode;
    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 占比
     */
    private String proportion;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
