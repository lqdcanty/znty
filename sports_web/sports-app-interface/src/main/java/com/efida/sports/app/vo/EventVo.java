/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.vo;

import com.efida.sport.admin.facade.model.SpGroupItemModel;
import com.efida.sports.util.AmountUtils;
import com.efida.sports.util.DateUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author zoutao
 * @version $Id: EventVo.java, v 0.1 2018年5月25日 下午7:18:18 zoutao Exp $
 */
public class EventVo implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    /**
     * 比赛项
     */
    private String            itemCode;

    /**
     * 比赛项名称
     */
    private String            itemName;

    /**
     * 比赛开始时间
     */
    private String            startTime;

    /**
     * 比赛结束时间
     */
    private String            endTime;

    /**
     * 报名费(以分为单位)
     */
    private int               entryFee;

    /**
     * 报名费 
     */
    private String            entryFeeStr;

    /**
     * 剩余名额
     */
    private String            surplusQuota;
    /**
     * 是否可以报名
     */
    private Boolean           canApply;

    /**
     * 参赛名额限制(0表示无人员限制)
     */
    private int               personLimit;

    /**
     * 比赛项类型
     */
    private String            itemType;

    /**
     * 团体报名人员限制
     */
    private int               groupLimit;

    /**
     *是否有效
     */
    private Boolean           isValid;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getGroupLimit() {
        return groupLimit;
    }

    public void setGroupLimit(int groupLimit) {
        this.groupLimit = groupLimit;
    }

    public static EventVo getVo(SpGroupItemModel item, Long total) {
        EventVo vo = new EventVo();
        if (item == null) {
            return null;
        }

        vo.setEndTime(DateUtil.format(item.getEndTime()));
        vo.setStartTime(DateUtil.format(item.getStartTime()));
        vo.setEntryFeeStr(AmountUtils.changeF2Y(Long.valueOf(item.getEntryFee())));
        vo.setEntryFee(item.getEntryFee());
        vo.setItemCode(item.getItemCode());
        vo.setItemName(item.getItemName());
        vo.setItemType(item.getItemType());
        vo.setGroupLimit(item.getGroupLimit());
        vo.setPersonLimit(item.getPersonLimit());
        Boolean status = "1".equals(item.getStatus());
        vo.setIsValid(status);
        int personNum = item.getPersonLimit();
          
        if (item.getEndTime() != null) {
            status = status && new Date().before(item.getEndTime());
        }else{
        	status=false;
        }
        if (0 == personNum) {
            vo.setSurplusQuota("无人员限制");
            vo.setCanApply(status);
        } else {
            long num = Long.valueOf(personNum).longValue() - total;
            vo.setSurplusQuota((num > 0 ? num : "无") + "");
            vo.setCanApply(status && num > 0);
        }
        return vo;

    }

}
