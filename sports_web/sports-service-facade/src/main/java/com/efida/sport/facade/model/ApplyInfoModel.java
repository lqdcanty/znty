/**
 * 
 */
package com.efida.sport.facade.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class ApplyInfoModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 订单唯一编号
     */
    private String            payOrderCode;
    /**
     * 报名费用
     */
    private Long              applyAmount;

    /**
     * 报名时间
     */
    private Date              applyTime;
    /**
     * 报名状态(待付款,成功)
     */
    private String            applyStatus;
    /**
     * 承办方唯一标识
     */
    private String            unitCode;
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
     * 赛事分组名称(例如:男子组，女子组)
     */
    private String            matchGroupName;
    /**
     * 赛事分组编号
     */
    private String            matchGroupCode;
    /**
     * 比赛项目编号
     */
    private String            eventCode;
    /**
     * temp编号
     */
    private String            groupEventCode;

    /**
     * 比赛项目名称(例如：单摇)
     */
    private String            eventName;
    /**
     * 项目开始时间
     */
    private Date              eventStartTime;
    /**
     * 项目结束时间
     */
    private Date              eventEndTime;

    private List<PlayerModel> players;
    private String            leaderName;
    private String            leaderPhone;
    private String            teamName;
    private Integer           registrationNum;

    private String            eventType;

    /**
     * 唯一标识  32 位 applyCode
     */
    private String            idempotentId;

    public String getPayOrderCode() {
        return payOrderCode;
    }

    public void setPayOrderCode(String payOrderCode) {
        this.payOrderCode = payOrderCode;
    }

    public Long getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(Long applyAmount) {
        this.applyAmount = applyAmount;
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

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
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

    public String getMatchGroupName() {
        return matchGroupName;
    }

    public void setMatchGroupName(String matchGroupName) {
        this.matchGroupName = matchGroupName;
    }

    public String getMatchGroupCode() {
        return matchGroupCode;
    }

    public void setMatchGroupCode(String matchGroupCode) {
        this.matchGroupCode = matchGroupCode;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getGroupEventCode() {
        return groupEventCode;
    }

    public void setGroupEventCode(String groupEventCode) {
        this.groupEventCode = groupEventCode;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(Date eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public Date getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(Date eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public List<PlayerModel> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerModel> players) {
        this.players = players;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getLeaderPhone() {
        return leaderPhone;
    }

    public void setLeaderPhone(String leaderPhone) {
        this.leaderPhone = leaderPhone;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getRegistrationNum() {
        return registrationNum;
    }

    public void setRegistrationNum(Integer registrationNum) {
        this.registrationNum = registrationNum;
    }

    public String getIdempotentId() {
        return idempotentId;
    }

    public void setIdempotentId(String idempotentId) {
        this.idempotentId = idempotentId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

}
