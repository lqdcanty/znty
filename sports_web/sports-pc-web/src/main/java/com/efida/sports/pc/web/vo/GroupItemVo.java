package com.efida.sports.pc.web.vo;

import java.util.Date;

/**
 * 
 * 
 * @author zengbo
 * @version $Id: GroupItemVo.java, v 0.1 2018年6月24日 上午11:51:18 zengbo Exp $
 */
public class GroupItemVo {

    /**
     * 比赛组编号
     */
    private String groupCode;

    /**
     * 比赛组名
     */
    private String groupName;

    /**
     * 比赛项
     */
    private String  itemCode;

    /**
     * 比赛项名称
     */
    private String  itemName;

    /**
     * 比赛开始时间
     */
    private Date    startTime;

    /**
     * 比赛结束时间
     */
    private Date    endTime;

    /**
     * 报名费(以分为单位)
     */
    private int     entryFee;

    /**
     * 报名费 
     */
    private String  entryFeeStr;

    /**
     * 剩余名额
     */
    private String  surplusQuota;
    /**
     * 是否可以报名
     */
    private Boolean canApply;

    /**
     * 参赛名额限制(0表示无人员限制)
     */
    private int     personLimit;
    /**
     *是否有效
     */
    private Boolean isValid;

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public int getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(int entryFee) {
        this.entryFee = entryFee;
    }

    public String getEntryFeeStr() {
        return entryFeeStr;
    }

    public void setEntryFeeStr(String entryFeeStr) {
        this.entryFeeStr = entryFeeStr;
    }

    public String getSurplusQuota() {
        return surplusQuota;
    }

    public void setSurplusQuota(String surplusQuota) {
        this.surplusQuota = surplusQuota;
    }

    public Boolean getCanApply() {
        return canApply;
    }

    public void setCanApply(Boolean canApply) {
        this.canApply = canApply;
    }

    public int getPersonLimit() {
        return personLimit;
    }

    public void setPersonLimit(int personLimit) {
        this.personLimit = personLimit;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

}
