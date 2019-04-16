/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.vo;

import java.util.Date;

import com.efida.sport.admin.facade.model.SpGroupItemModel;
import com.efida.sports.util.AmountUtils;
import com.efida.sports.util.DateUtil;

/**
 * 
 * @author zoutao
 * @version $Id: EventVo.java, v 0.1 2018年5月25日 下午7:18:18 zoutao Exp $
 */
public class EventVo {

    /**
     * id
     */
    private Long    id;

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
     * 开始时间
     */
    private String  startTimeStr;

    /**
     * 比赛结束时间
     */
    private Date    endTime;
    /**
     * 结束时间
     */
    private String  endTimeStr;

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
    /**
     * 项目类型
     */
    private String  evnetType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getPersonLimit() {
        return personLimit;
    }

    public void setPersonLimit(int personLimit) {
        this.personLimit = personLimit;
    }

    public Boolean getCanApply() {
        return canApply;
    }

    public void setCanApply(Boolean canApply) {
        this.canApply = canApply;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getEvnetType() {
        return evnetType;
    }

    public void setEvnetType(String evnetType) {
        this.evnetType = evnetType;
    }

    public static EventVo getVo(SpGroupItemModel item, Long total) {
        EventVo vo = new EventVo();
        if (item == null) {
            return null;
        }
        vo.setEndTime(item.getEndTime());
        vo.setStartTime(item.getStartTime());
        vo.setEntryFeeStr(AmountUtils.changeF2Y(Long.valueOf(item.getEntryFee())));
        vo.setEntryFee(item.getEntryFee());
        vo.setItemCode(item.getItemCode());
        vo.setItemName(item.getItemName());
        vo.setEvnetType(item.getItemType());
        vo.setIsValid("1".equals(item.getStatus()));
        vo.setStartTimeStr(DateUtil.formatDay(item.getStartTime()));
        vo.setEndTimeStr(DateUtil.formatDay(item.getEndTime()));
        int personNum = item.getPersonLimit();
        Boolean status = "1".equals(item.getStatus());
        if (item.getEndTime() != null) {
            status = status && new Date().before(item.getEndTime());
        } else {
            status = false;
        }
        if (0 == personNum) {
            vo.setSurplusQuota("无人员限制");
            vo.setCanApply(status);
        } else {
            long num = Long.valueOf(personNum).longValue() - total;
            vo.setSurplusQuota((num > 0 ? num : 0) + "");
            vo.setCanApply(status && num > 0);
        }
        return vo;
    }

}
