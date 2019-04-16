package com.efida.sports.model;

import com.efida.sports.entity.ApplyInfo;

import java.util.Date;

/**
 * 报名成功返回消息实体
 * Created by wangyan on 2018/7/24.
 */
public class MessageInfoModel {
    /**
     * 报名信息唯一编号
     */
    private String            applyCode;

    /**
     * 订单唯一编号
     */
    private String            payOrderCode;

    /**
     * 报名时间
     */
    private Date applyTime;

    /**
     * 报名状态(待付款,成功)
     */
    private String            applyStatus;


    /**
     * 赛事名称(例如：XXX创办跳绳比赛)
     */
    private String            matchName;


    /**
     * 赛事海报
     */
    private String            poster;

    /**
     * 比赛项类型
     */
    private String            eventType;

    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public String getPayOrderCode() {
        return payOrderCode;
    }

    public void setPayOrderCode(String payOrderCode) {
        this.payOrderCode = payOrderCode;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public static MessageInfoModel getVo(ApplyInfo info) {
        if (info == null) {
            return null;
        }
        MessageInfoModel vo = new MessageInfoModel();
        vo.setMatchName(info.getMatchName());
        vo.setApplyCode(info.getApplyCode());
        vo.setApplyStatus(info.getApplyStatus());
        vo.setApplyTime(info.getApplyTime());
        vo.setPayOrderCode(info.getPayOrderCode());
        vo.setPoster(info.getPoster());
        vo.setEventType(info.getEventType());
        return vo;
    }

}
