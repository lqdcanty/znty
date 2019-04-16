package com.efida.sports.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 
 * </p>
 *
 * @author zoutao
 * @since 2018-10-17
 */
public class DrawActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer           id;
    /**
     * 活动编号
     */
    private String            activityCode;
    /**
     * 活动名称
     */
    private String            activityName;
    /**
     * 活动描述
     */
    private String            activityDesc;
    /**
     * 活动开始时间
     */
    private Date              startTime;
    /**
     * 活动结束时间
     */
    private Date              endTime;
    /**
     * 获奖概率
     */
    private BigDecimal        ratio;
    /**
     * 状态 1可使用 0不可使用
     */
    private Integer           status;
    /**
     * 是否删除 1.已经删除 0 未删除
     */
    private Integer           isDel;

    /**
     * 是否每次刷新缓存
     */
    private Integer           IsRefresh;
    /**
     * 创建时间
     */
    private Date              gmtCreate;
    /**
     * 修改时间
     */
    private Date              gmtModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public Integer getIsRefresh() {
        return IsRefresh;
    }

    public void setIsRefresh(Integer isRefresh) {
        IsRefresh = isRefresh;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
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

    @Override
    public String toString() {
        return "DrawActivity{" + ", id=" + id + ", activityCode=" + activityCode + ", activityName="
               + activityName + ", activityDesc=" + activityDesc + ", startTime=" + startTime
               + ", endTime=" + endTime + ", ratio=" + ratio + ", status=" + status + ", isDel="
               + isDel + ", gmtCreate=" + gmtCreate + ", gmtModified=" + gmtModified + "}";
    }
}
