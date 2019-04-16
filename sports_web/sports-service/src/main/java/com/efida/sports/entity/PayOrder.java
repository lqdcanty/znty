package com.efida.sports.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 
 * </p>
 *
 * @author zoutao
 * @since 2018-05-18
 */
public class PayOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long              id;
    /**
     * 订单号
     */
    private String            orderCode;

    /**
     * 创建用户唯一标识
     */
    private String            registerCode;
    /**
     * 状态(待支付,成功,失败,废弃)
     */
    private String            orderStatus;

    /**
     * 订单类型
     */
    private String            orderType;
    /**
     * 订单金额(扣除折扣，代金券等购买商品实际应付金额)
     */
    private Long              orderAmount;
    /**
     * 创建时间
     */
    private Date              orderTime;
    /**
     * ip
     */
    private String            orderIp;
    /**
     * 从哪个页面链接过来
     */
    private String            orderRefererUrl;
    /**
     * 页面回调url
     */
    private String            orderReturnUrl;
    /**
     * 后台异步通知url
     */
    private String            notifyUrl;
    /**
     * 订单有效期(单位分钟)
     */
    private Integer           orderPeriod;
    /**
     * 取消原因
     */
    private String            cancelReason;
    /**
     * 支付网关code
     */
    private String            payWayCode;
    /**
     * 支付网关名称
     */
    private String            payWayName;
    /**
     * 备注
     */
    private String            remark;
    /**
     * 交易流水号
     */
    private String            trxNo;
    /**
     * 订单到期时间
     */
    private Date              expireTime;
    /**
     * 支付时间
     */
    private Date              payTime;
    /**
     * 创建时间
     */
    private Date              gmtCreate;
    /**
     * 修改时间
     */
    private Date              gmtModified;

    @TableField(exist = false)
    private List<ApplyInfo>   applyInfos;

    @TableField(exist = false)
    private int               applyInfoSize;

    @TableField(exist = false)
    private List<Player>      players;

    @TableField(exist = false)
    private String            orderAmountStr;

    @TableField(exist = false)
    private Integer           overplusTime;

    @TableField(exist = false)
    private String            orderTimeStr;

    @TableField(exist = false)
    private LeaderInfo        leaderInfo;

    /**
     * 支付统计报表-日期
     */
    @TableField(exist = false)
    private String            orderDate;
    /**
     * 支付统计报表-订单总数
     */
    @TableField(exist = false)
    private String            ordersTotal;
    /**
     * 支付统计报表-订单总金额
     */
    @TableField(exist = false)
    private String            moneyTotal;
    /**
     * 支付统计报表-支付订单总数
     */
    @TableField(exist = false)
    private String            payOrdersTotal;
    /**
     * 支付统计报表-支付订单总金额
     */
    @TableField(exist = false)
    private String            payMoneyTotal;

    /**
     * 支付详情报表
     */
    @TableField(exist = false)
    String gameName;
    /**
     * 支付详情报表
     */
    @TableField(exist = false)
    String matchName;
    /**
     * 支付详情报表
     */
    @TableField(exist = false)
    String nickName;

    /**
     * 支付详情报表
     */
    @TableField(exist = false)
    String payMoney;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderIp() {
        return orderIp;
    }

    public void setOrderIp(String orderIp) {
        this.orderIp = orderIp;
    }

    public String getOrderRefererUrl() {
        return orderRefererUrl;
    }

    public void setOrderRefererUrl(String orderRefererUrl) {
        this.orderRefererUrl = orderRefererUrl;
    }

    public String getOrderReturnUrl() {
        return orderReturnUrl;
    }

    public void setOrderReturnUrl(String orderReturnUrl) {
        this.orderReturnUrl = orderReturnUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Integer getOrderPeriod() {
        return orderPeriod;
    }

    public void setOrderPeriod(Integer orderPeriod) {
        this.orderPeriod = orderPeriod;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTrxNo() {
        return trxNo;
    }

    public void setTrxNo(String trxNo) {
        this.trxNo = trxNo;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
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

    public List<ApplyInfo> getApplyInfos() {
        return applyInfos;
    }

    public void setApplyInfos(List<ApplyInfo> applyInfos) {
        this.applyInfos = applyInfos;
    }

    public String getApplyType() {
        List<ApplyInfo> infos = this.applyInfos;
        if (infos == null || infos.size() < 1) {
            return "";
        }
        return infos.iterator().next().getEventType();
    }

    public int getPersonGroup() {
        List<ApplyInfo> infos = this.applyInfos;
        if (infos == null || infos.size() < 1) {
            return 0;
        }
        return infos.iterator().next().getRegistrationNum();
    }

    public int getApplyInfoSize() {
        return applyInfoSize;
    }

    public void setApplyInfoSize(int applyInfoSize) {
        this.applyInfoSize = applyInfoSize;
    }

    public String getOrderAmountStr() {
        return orderAmountStr;
    }

    public void setOrderAmountStr(String orderAmountStr) {
        this.orderAmountStr = orderAmountStr;
    }

    public Integer getOverplusTime() {
        return overplusTime;
    }

    public void setOverplusTime(Integer overplusTime) {
        this.overplusTime = overplusTime;
    }

    public String getOrderTimeStr() {
        return orderTimeStr;
    }

    public void setOrderTimeStr(String orderTimeStr) {
        this.orderTimeStr = orderTimeStr;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public LeaderInfo getLeaderInfo() {
        return leaderInfo;
    }

    public void setLeaderInfo(LeaderInfo leaderInfo) {
        this.leaderInfo = leaderInfo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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

    @Override
    public String toString() {
        return "PayOrder [id=" + id + ", orderCode=" + orderCode + ", registerCode=" + registerCode
               + ", orderStatus=" + orderStatus + ", orderAmount=" + orderAmount + ", orderTime="
               + orderTime + ", orderIp=" + orderIp + ", orderRefererUrl=" + orderRefererUrl
               + ", orderReturnUrl=" + orderReturnUrl + ", notifyUrl=" + notifyUrl
               + ", orderPeriod=" + orderPeriod + ", cancelReason=" + cancelReason + ", payWayCode="
               + payWayCode + ", payWayName=" + payWayName + ", remark=" + remark + ", trxNo="
               + trxNo + ", expireTime=" + expireTime + ", payTime=" + payTime + ", gmtCreate="
               + gmtCreate + ", gmtModified=" + gmtModified + ", applyInfos=" + applyInfos
               + ", applyInfoSize=" + applyInfoSize + ", players=" + players + ", orderAmountStr="
               + orderAmountStr + ", overplusTime=" + overplusTime + ", orderTimeStr="
               + orderTimeStr + "]";
    }

}
