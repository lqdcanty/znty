/**
 *
 */
package com.efida.sport.facade.model;

import java.io.Serializable;

/**
 * @author antony
 */
public class SmtPayOrderDetailModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String orderCode;
    /**
     * 项目名称
     */
    private String gameName;
    /**
     * 赛事名称
     */
    private String matchName;
    /**
     * 用户编号
     */
    private String registerCode;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 订单金额
     */
    private String payMoney;
    /**
     * 状态
     */
    private String orderStatus;
    /**
     * 支付网关
     */
    private String payWayName;
    /**
     * 支付时间
     */
    private String payTime;
    /**
     * 创建时间
     */
    private String gmtCreate;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPayWayName() {
        return payWayName;
    }

    public void setPayWayName(String payWayName) {
        this.payWayName = payWayName;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
