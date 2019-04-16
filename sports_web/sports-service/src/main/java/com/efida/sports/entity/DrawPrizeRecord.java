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
 * @since 2018-10-17
 */
public class DrawPrizeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer           id;
    /**
     * 活动编号
     */
    private String            activityCode;
    /**
     * 活动唯一编号
     */
    private String            registerCode;
    /**
     * 微信用户唯一标识
     */
    private String            wxOpenId;
    /**
     * 用户手机号
     */
    private String            memberMobile;
    /**
     * 获得奖品编号
     */
    private String            prizeCode;
    /**
     * 获得奖品名称
     */
    private String            prizeName;
    /**
     * 生效状态 1有效 0无效
     */
    private Integer           status;
    /**
     * 是否已抽奖 1已抽奖0未抽奖
     */
    private Integer           isDraw;
    /**
     * 抽奖操作时间
     */
    private Date              operationTime;
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

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public String getPrizeCode() {
        return prizeCode;
    }

    public void setPrizeCode(String prizeCode) {
        this.prizeCode = prizeCode;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDraw() {
        return isDraw;
    }

    public void setIsDraw(Integer isDraw) {
        this.isDraw = isDraw;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
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
        return "DrawPrizeRecord{" + ", id=" + id + ", activityCode=" + activityCode
               + ", registerCode=" + registerCode + ", wxOpenId=" + wxOpenId + ", memberMobile="
               + memberMobile + ", prizeCode=" + prizeCode + ", prizeName=" + prizeName
               + ", status=" + status + ", isDraw=" + isDraw + ", operationTime=" + operationTime
               + ", gmtCreate=" + gmtCreate + ", gmtModified=" + gmtModified + "}";
    }
}
