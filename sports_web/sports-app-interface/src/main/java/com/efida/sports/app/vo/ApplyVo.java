/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.vo;

import java.util.ArrayList;
import java.util.List;

import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.util.AmountUtils;
import com.efida.sports.util.DateUtil;

/**
 * 
 * @author zoutao
 * @version $Id: ApplyVo.java, v 0.1 2018年5月22日 下午5:19:42 zoutao Exp $
 */
public class ApplyVo {
    /**
     * 报名编号
     */
    private String applyCode;
    /**
     * 比赛项目名称（赛事分类名称）
     */
    private String gameName;
    /**
     * 赛事名称
     */
    private String matchName;
    /**
     * 赛场名称
     */
    private String siteName;
    /**
     * 赛事分组名称
     */
    private String matchGroupName;
    /**
     * 比赛项名称
     */
    private String eventName;

    /**
     * 报名费用
     */
    private Long   applyAmount;
    /**
     * 报名费用  单位装换后
     */
    private String applyAmountStr;

    /**
     * 项目开始时间
     */
    private String eventStartTime;
    /**
     * 项目结束时间
     */
    private String eventEndTime;
    /**
     * 地址
     */
    private String address;

    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public String getMatchGroupName() {
        return matchGroupName;
    }

    public void setMatchGroupName(String matchGroupName) {
        this.matchGroupName = matchGroupName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(Long applyAmount) {
        this.applyAmount = applyAmount;
    }

    public String getApplyAmountStr() {
        return applyAmountStr;
    }

    public void setApplyAmountStr(String applyAmountStr) {
        this.applyAmountStr = applyAmountStr;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static List<ApplyVo> getVos(List<ApplyInfo> infos) {
        List<ApplyVo> vos = new ArrayList<ApplyVo>();
        if (infos == null || infos.size() < 1) {
            return vos;
        }
        for (ApplyInfo applyInfo : infos) {
            ApplyVo vo = getVo(applyInfo);
            if (vo == null) {
                continue;
            }
            vos.add(vo);
        }
        return vos;
    }

    public static ApplyVo getVo(ApplyInfo info) {
        if (info == null) {
            return null;
        }
        ApplyVo vo = new ApplyVo();
        vo.setApplyAmount(info.getApplyAmount());
        vo.setApplyAmountStr(AmountUtils.changeF2Y(info.getApplyAmount()));
        vo.setApplyCode(info.getApplyCode());
        vo.setEventName(info.getEventName());
        vo.setMatchGroupName(info.getMatchGroupName());
        vo.setEventStartTime(DateUtil.format(info.getEventStartTime()));
        vo.setEventEndTime(DateUtil.format(info.getEventEndTime()));
        vo.setSiteName(info.getSiteName());
        vo.setMatchName(info.getMatchName());
        vo.setGameName(info.getGameName());
        vo.setAddress(info.getAddress());
        return vo;

    }

}
