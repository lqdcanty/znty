package com.efida.sports.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 发送日志
 * 
 * @author zengbo
 * @version $Id: ApplySendLog.java, v 0.1 2018年7月5日 下午3:34:50 zengbo Exp $
 */
public class ApplySendLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long              id;
    /**
     * 报名信息唯一编号
     */
    private String            applyCode;
    /**
     * 运动员唯一编号
     */
    private String            playerCode;

    /**
     * 订单唯一编号
     */
    private String            payOrderCode;
    /**
     * 项目编号
     */
    private String            gameCode;
    /**
     * 项目名称(大类名称:跳绳等)
     */
    private String            gameName;
    /**
     * 赛事编号
     */
    private String            matchCode;
    /**
     * 赛事名称(例如：XXX创办跳绳比赛)
     */
    private String            matchName;

    /**
     * 赛场编号
     */
    private String            siteCode;
    /**
     * 赛场名称 
     */
    private String            siteName;

    /**
     * 发送时间
     */
    private Date              sendTime;

    /**
     * 发送状态
     */
    private String            sendStatus;

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

    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public String getPlayerCode() {
        return playerCode;
    }

    public void setPlayerCode(String playerCode) {
        this.playerCode = playerCode;
    }

    public String getPayOrderCode() {
        return payOrderCode;
    }

    public void setPayOrderCode(String payOrderCode) {
        this.payOrderCode = payOrderCode;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getMatchCode() {
        return matchCode;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
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
        return "ApplyInfo{" + ", id=" + id + ", applyCode=" + applyCode + ", playerCode="
               + playerCode + ", payOrderCode=" + payOrderCode + ", gameCode="
               + gameCode + ", gameName=" + gameName + ", matchCode=" + matchCode + ", matchName="
               + matchName + ", gmtCreate=" + gmtCreate
               + ", gmtModified=" + gmtModified + "}";
    }
}
