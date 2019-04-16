/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sport.facade.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author zoutao
 * @version $Id: RegisterStatisticsModel.java, v 0.1 2018年8月29日 下午7:28:34 zoutao Exp $
 */
public class RegisterStatisticsModel implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    /**
     * 总用户数
     */
    private long              totalRegister;
    /**
     * 七日平均活跃用户数
     */
    private double            sevenAvgActive;
    /**
     * 七日平均活跃用户数
     */
    private String            sevenAvgActiveStr;

    /**
     * 三十日总活跃用户数
     */
    private long              thirtyActiveRegitsers;
    /**
     * 七日平均新增用户数
     */
    private double            sevenAvgNewRegister;
    /**
     * 七日留存率
     */
    private String            sevenRetention;
    /**
    * 七日总活跃用户
    */
    private long              sevenActiveRegitsers;

    public long getTotalRegister() {
        return totalRegister;
    }

    public void setTotalRegister(long totalRegister) {
        this.totalRegister = totalRegister;
    }

    public double getSevenAvgActive() {
        return sevenAvgActive;
    }

    public void setSevenAvgActive(double sevenAvgActive) {
        sevenAvgActiveStr = "";
        long dev = (long) sevenAvgActive;
        if (dev == sevenAvgActive) {
            this.sevenAvgActiveStr = dev + "";
        } else {
            this.sevenAvgActiveStr = new BigDecimal(sevenAvgActive)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
        }

        this.sevenAvgActive = sevenAvgActive;
    }

    public long getThirtyActiveRegitsers() {
        return thirtyActiveRegitsers;
    }

    public void setThirtyActiveRegitsers(long thirtyActiveRegitsers) {
        this.thirtyActiveRegitsers = thirtyActiveRegitsers;
    }

    public double getSevenAvgNewRegister() {
        return sevenAvgNewRegister;
    }

    public void setSevenAvgNewRegister(double sevenAvgNewRegister) {
        this.sevenAvgNewRegister = sevenAvgNewRegister;
    }

    public String getSevenRetention() {
        return sevenRetention;
    }

    public void setSevenRetention(String sevenRetention) {
        this.sevenRetention = sevenRetention;
    }

    public long getSevenActiveRegitsers() {
        return sevenActiveRegitsers;
    }

    public void setSevenActiveRegitsers(long sevenActiveRegitsers) {
        this.sevenActiveRegitsers = sevenActiveRegitsers;
    }

    public String getSevenAvgActiveStr() {
        return sevenAvgActiveStr;
    }

    public void setSevenAvgActiveStr(String sevenAvgActiveStr) {
        this.sevenAvgActiveStr = sevenAvgActiveStr;
    }

}
