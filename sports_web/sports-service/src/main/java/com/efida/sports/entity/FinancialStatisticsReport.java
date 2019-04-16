package com.efida.sports.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

public class FinancialStatisticsReport implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long            id;
    /**
     * 报表时间
     */
    private Date            reportDate;
    /**
     * 订单唯一编号
     */
    private String          orderCode;
    /**
     * 承办方唯一标识
     */
    private String           unitCode;
    /**
     * 项目编号
     */
    private String           gameCode;
    /**
     * 项目名称(大类名称:跳绳等)
     */
    private String           gameName;
    /**
     * 赛事编号
     */
    private String           matchCode;
    /**
     * 赛事名称(例如：XXX创办跳绳比赛)
     */
    private String           matchName;
    /**
     * 用户编号
     */
    private String           registerCode;
    /**
     * 昵称
     */
    private String            nickName;
    /**
     * 订单金额
     */
    private String              payMoney;
    /**
     * 状态(待支付,成功,失败,废弃)
     */
    private String            orderStatus;
    /**
     * 订单类型
     */
    private String            orderType;
    /**
     * 支付网关code
     */
    private String            payWayCode;
    /**
     * 支付网关名称
     */
    private String            payWayName;
    /**
     * 订单时间
     */
    private Date              orderTime;
    /**
     * 支付时间
     */
    private Date              payTime;
    /**
     * 退单时间
     */
    private Date              orderRefundTime;
    /**
     * 创建时间
     */
    private Date              createDate;
    /**
     * 是否删除：0：未删除 1：已删除 2：没有订单
     */
    private int              isDelete;
    /**
     * 订单总数
     */
    @TableField(exist = false)
    private String            ordersTotal;
    /**
     * 订单总金额
     */
    @TableField(exist = false)
    private String            moneyTotal;
    /**
     * 支付订单总数
     */
    @TableField(exist = false)
    private String            payOrdersTotal;
    /**
     * 支付订单总金额
     */
    @TableField(exist = false)
    private String            payMoneyTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
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

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPayWayCode() {
        return payWayCode;
    }

    public void setPayWayCode(String payWayCode) {
        this.payWayCode = payWayCode;
    }

    public String getPayWayName() {
        return payWayName;
    }

    public void setPayWayName(String payWayName) {
        this.payWayName = payWayName;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getOrderRefundTime() {
        return orderRefundTime;
    }

    public void setOrderRefundTime(Date orderRefundTime) {
        this.orderRefundTime = orderRefundTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getOrdersTotal() {
        return ordersTotal;
    }

    public void setOrdersTotal(String ordersTotal) {
        this.ordersTotal = ordersTotal;
    }

    public String getMoneyTotal() {
        return moneyTotal;
    }

    public void setMoneyTotal(String moneyTotal) {
        this.moneyTotal = moneyTotal;
    }

    public String getPayOrdersTotal() {
        return payOrdersTotal;
    }

    public void setPayOrdersTotal(String payOrdersTotal) {
        this.payOrdersTotal = payOrdersTotal;
    }

    public String getPayMoneyTotal() {
        return payMoneyTotal;
    }

    public void setPayMoneyTotal(String payMoneyTotal) {
        this.payMoneyTotal = payMoneyTotal;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
}
