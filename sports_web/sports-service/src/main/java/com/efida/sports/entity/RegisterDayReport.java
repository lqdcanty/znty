package com.efida.sports.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 
 * </p>
 *
 * @author zoutao
 * @since 2018-08-29
 */
public class RegisterDayReport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long              id;
    /**
     * 报表唯一编号
     */
    private String            reportCode;
    /**
     * 注册来源（dmp,android,ios,pc,weichat）
     */
    private String            regTerminal;
    /**
     * 渠道编号
     */
    private String            channelCode;
    /**
     * 渠道名称
     */
    private String            channelName;
    /**
     * 报表生成时间（精确到秒）
     */
    private Date              reportTime;
    /**
     * 报表日期（精确到天）
     */
    private Date              reportDay;
    /**
     * 总用户数
     */
    private Long              totalRegister;
    /**
     * 新增用户数
     */
    private Long              newRegister;
    /**
     * 登录人数
     */
    private Long              loginRegister;
    /**
     * 周活跃用户数    
     */
    private Long              weekActiveRegister;
    /**
     * 月活跃用户
     */
    private Long              monthActiveRegister;
    /**
     * 访问量
     */
    private Long              accessCount;

    /**
     * 创建时间
     */
    private Date              gmtCreate;
    /**
     * 修改时间
     */
    private Date              gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public String getRegTerminal() {
        return regTerminal;
    }

    public void setRegTerminal(String regTerminal) {
        this.regTerminal = regTerminal;
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

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public Date getReportDay() {
        return reportDay;
    }

    public void setReportDay(Date reportDay) {
        this.reportDay = reportDay;
    }

    public Long getTotalRegister() {
        return totalRegister;
    }

    public void setTotalRegister(Long totalRegister) {
        this.totalRegister = totalRegister;
    }

    public Long getNewRegister() {
        return newRegister;
    }

    public void setNewRegister(Long newRegister) {
        this.newRegister = newRegister;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Long getLoginRegister() {
        return loginRegister;
    }

    public void setLoginRegister(Long loginRegister) {
        this.loginRegister = loginRegister;
    }

    public Long getWeekActiveRegister() {
        return weekActiveRegister;
    }

    public void setWeekActiveRegister(Long weekActiveRegister) {
        this.weekActiveRegister = weekActiveRegister;
    }

    public Long getMonthActiveRegister() {
        return monthActiveRegister;
    }

    public void setMonthActiveRegister(Long monthActiveRegister) {
        this.monthActiveRegister = monthActiveRegister;
    }

    public Long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(Long accessCount) {
        this.accessCount = accessCount;
    }

    @Override
    public String toString() {
        return "RegisterDayReport [id=" + id + ", reportCode=" + reportCode + ", regTerminal="
               + regTerminal + ", channelCode=" + channelCode + ", channelName=" + channelName
               + ", reportTime=" + reportTime + ", reportDay=" + reportDay + ", totalRegister="
               + totalRegister + ", newRegister=" + newRegister + ", loginRegister=" + loginRegister
               + ", weekActiveRegister=" + weekActiveRegister + ", monthActiveRegister="
               + monthActiveRegister + ", accessCount=" + accessCount + ", gmtCreate=" + gmtCreate
               + ", gmtModified=" + gmtModified + "]";
    }

}
