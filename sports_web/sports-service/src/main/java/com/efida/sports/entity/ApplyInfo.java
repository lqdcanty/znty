package com.efida.sports.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.efida.sports.enums.EventTypeEnums;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zoutao
 * @since 2018-05-18
 */
public class ApplyInfo implements Serializable {

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
     * 订单唯一编号
     */
    private String            payOrderCode;
    /**
     * 用户编号
     */
    private String            registerCode;

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

    /**
     * 是否删除(1:是,0:否)
     */
    private Integer           isDelet;
    /**
     * 过期时间
     */
    private Date              expireTime;

    /**
     * 渠道代码
     */
    private String            channelCode;

    private Byte              sync;

    /**
     * 发送状态
     */
    private String            sendStatus;

    /**
     * 比赛项类型
     * @see EventTypeEnums
     */
    private String            eventType;
    /**
     * 参赛人数
     */
    private Integer           registrationNum;
    /**
     * 下一次发送时间
     */
    private Date              nextSendTime;
    /**
     * 外部幂等id
     */
    private String            idempotentId;
    /**
     *  发送次数
     */
    private Integer           sentTotal;

    /**
     * 创建时间
     */
    private Date              gmtCreate;
    /**
     * 修改时间
     */
    private Date              gmtModified;

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }

    @TableField(exist = false)
    private String  poster;

    @TableField(exist = false)
    private String  status;
    /**
     * 详细地址
     */
    @TableField(exist = false)
    private String  address;

    @TableField(exist = false)
    private String  statusCode;

    /**
     * 是否有未读消息(1:是,0:否)
     */
    private Integer isRead;

    /**
     * 直接同步到承办方状态：1已经同步 0 未同步成功
     */
    private Integer syncUnit;

    /**
     * 下次同步到承办方的时间(失败时指定下次时间)
     */
    private Date syncUnitNextTime;


    /**
     * 同步次数
     */
    private Integer syncUnitTotal;


    /**
     * 同步状态描述
     */
    private String syncUnitStatus;

    /**
     * 咕咚透传字段
     */
    private String externalData;


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

    /**
     * Getter method for property <tt>applyTime</tt>.
     * 
     * @return property value of applyTime
     */
    public Date getApplyTime() {
        return applyTime;
    }

    /**
     * Setter method for property <tt>applyTime</tt>.
     * 
     * @param applyTime value to be assigned to property applyTime
     */
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

    public Integer getIsDelet() {
        return isDelet;
    }

    public void setIsDelet(Integer isDelet) {
        this.isDelet = isDelet;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(Date eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    /**
     * Getter method for property <tt>channelCode</tt>.
     * 
     * @return property value of channelCode
     */
    public String getChannelCode() {
        return channelCode;
    }

    /**
     * Setter method for property <tt>channelCode</tt>.
     * 
     * @param channelCode value to be assigned to property channelCode
     */
    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter method for property <tt>sync</tt>.
     * 
     * @return property value of sync
     */
    public Byte getSync() {
        return sync;
    }

    /**
     * Setter method for property <tt>sync</tt>.
     * 
     * @param sync value to be assigned to property sync
     */
    public void setSync(Byte sync) {
        this.sync = sync;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getGroupEventCode() {
        return groupEventCode;
    }

    public void setGroupEventCode(String groupEventCode) {
        this.groupEventCode = groupEventCode;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getRegistrationNum() {
        return registrationNum;
    }

    public void setRegistrationNum(Integer registrationNum) {
        this.registrationNum = registrationNum;
    }

    public Date getNextSendTime() {
        return nextSendTime;
    }

    public void setNextSendTime(Date nextSendTime) {
        this.nextSendTime = nextSendTime;
    }

    public String getIdempotentId() {
        return idempotentId;
    }

    public void setIdempotentId(String idempotentId) {
        this.idempotentId = idempotentId;
    }

    public Integer getSentTotal() {
        return sentTotal;
    }

    public void setSentTotal(Integer sentTotal) {
        this.sentTotal = sentTotal;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public Integer getSyncUnit() {
        return syncUnit;
    }

    public void setSyncUnit(Integer syncUnit) {
        this.syncUnit = syncUnit;
    }

    public Date getSyncUnitNextTime() {
        return syncUnitNextTime;
    }

    public void setSyncUnitNextTime(Date syncUnitNextTime) {
        this.syncUnitNextTime = syncUnitNextTime;
    }

    public Integer getSyncUnitTotal() {
        return syncUnitTotal;
    }

    public void setSyncUnitTotal(Integer syncUnitTotal) {
        this.syncUnitTotal = syncUnitTotal;
    }

    public String getSyncUnitStatus() {
        return syncUnitStatus;
    }

    public void setSyncUnitStatus(String syncUnitStatus) {
        this.syncUnitStatus = syncUnitStatus;
    }

    public String getExternalData() {
        return externalData;
    }

    public void setExternalData(String externalData) {
        this.externalData = externalData;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ApplyInfo [id=" + id + ", applyCode=" + applyCode + ", payOrderCode=" + payOrderCode
               + ", applyAmount=" + applyAmount + ", applyTime=" + applyTime + ", applyStatus="
               + applyStatus + ", unitCode=" + unitCode + ", gameCode=" + gameCode + ", gameName="
               + gameName + ", matchCode=" + matchCode + ", matchName=" + matchName + ", siteCode="
               + siteCode + ", siteName=" + siteName + ", matchGroupName=" + matchGroupName
               + ", matchGroupCode=" + matchGroupCode + ", eventCode=" + eventCode
               + ", groupEventCode=" + groupEventCode + ", eventName=" + eventName
               + ", eventStartTime=" + eventStartTime + ", eventEndTime=" + eventEndTime
               + ", isDelet=" + isDelet + ", expireTime=" + expireTime + ", channelCode="
               + channelCode + ", sync=" + sync + ", sendStatus=" + sendStatus + ", eventType="
               + eventType + ", registrationNum=" + registrationNum + ", nextSendTime="
               + nextSendTime + ", idempotentId=" + idempotentId + ", sentTotal=" + sentTotal
               + ", gmtCreate=" + gmtCreate + ", gmtModified=" + gmtModified + ", poster=" + poster
               + ", status=" + status + ", address=" + address + ", getId()=" + getId() + ",isRead="
               + isRead + ",syncUnit=" + syncUnit + ",syncUnitNextTime=" + syncUnitNextTime + ",syncUnitTotal="
               + syncUnitTotal + ",syncUnitStatus=" + syncUnitStatus + ",externalData=" + externalData
               + ", getApplyCode()=" + getApplyCode() + ", getPayOrderCode()="
               + getPayOrderCode() + ", getApplyAmount()=" + getApplyAmount() + ", getApplyTime()="
               + getApplyTime() + ", getApplyStatus()=" + getApplyStatus() + ", getUnitCode()="
               + getUnitCode() + ", getGameCode()=" + getGameCode() + ", getGameName()="
               + getGameName() + ", getMatchCode()=" + getMatchCode() + ", getMatchName()="
               + getMatchName() + ", getMatchGroupName()=" + getMatchGroupName()
               + ", getMatchGroupCode()=" + getMatchGroupCode() + ", getEventCode()="
               + getEventCode() + ", getEventName()=" + getEventName() + ", getEventStartTime()="
               + getEventStartTime() + ", getIsDelet()=" + getIsDelet() + ", getExpireTime()="
               + getExpireTime() + ", getEventEndTime()=" + getEventEndTime()
               + ", getChannelCode()=" + getChannelCode() + ", getGmtCreate()=" + getGmtCreate()
               + ", getGmtModified()=" + getGmtModified() + ", getSiteCode()=" + getSiteCode()
               + ", getSiteName()=" + getSiteName() + ", getPoster()=" + getPoster()
               + ", getStatus()=" + getStatus() + ", getAddress()=" + getAddress() + ", getSync()="
               + getSync() + ", getSendStatus()=" + getSendStatus() + ", getGroupEventCode()="
               + getGroupEventCode() + ", getEventType()=" + getEventType()
               + ", getRegistrationNum()=" + getRegistrationNum() + ", getNextSendTime()="
               + getNextSendTime() + ", getIdempotentId()=" + getIdempotentId()
               + ", getSentTotal()=" + getSentTotal() + ", getClass()=" + getClass()
               + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
    }

}
