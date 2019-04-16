/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sport.facade.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author zoutao
 * @version $Id: ChannelTrendAnalysisModel.java, v 0.1 2018年8月30日 下午12:48:02 zoutao Exp $
 * 渠道用户分析
 */
public class ChannelTrendAnalysisModel implements Serializable {
    /**  */
    private static final long        serialVersionUID = 1L;
    /**
    * 时间
    */
    private Date                     date;
    /**
     * 格式化后的时间
     */
    private String                   dateStr;
    /**
     * 用户趋势对象
     */
    List<RegisterTrendAnalysisModel> trendModels;

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

    public List<RegisterTrendAnalysisModel> getTrendModels() {
        return trendModels;
    }

    public void setTrendModels(List<RegisterTrendAnalysisModel> trendModels) {
        this.trendModels = trendModels;
    }

}
