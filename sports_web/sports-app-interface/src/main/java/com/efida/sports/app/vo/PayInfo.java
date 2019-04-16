/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.vo;

import java.io.Serializable;
import java.util.List;

import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.entity.PayOrder;
import com.efida.sports.enums.OrderStatusEnum;
import com.efida.sports.util.AmountUtils;
import com.efida.sports.util.DateUtil;

/**
 * 
 * @author zoutao
 * @version $Id: PayInfo.java, v 0.1 2018年6月26日 下午7:14:18 zoutao Exp $
 */
public class PayInfo implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;
    /**
     * 订单编号
     */
    private String            orderCode;

    /**
     * 订单状态
     */
    private String            orderStatus;
    /**
     *状态描述
     */
    private String            orderStatusDesc;

    /**
     * 订单创建时间
     */
    private String            orderTime;
    /**
     * 备注
     */
    private String            remark;

    /**
     * 订单金额
     */
    private Long              orderAmount;
    /**
     * 订单金额 转换后
     */
    private String            orderAmountStr;

    private String            expiresTime;

    /**
     * 报名信息
     */
    private List<ApplyVo>     applys;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusDesc() {
        return orderStatusDesc;
    }

    public void setOrderStatusDesc(String orderStatusDesc) {
        this.orderStatusDesc = orderStatusDesc;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public List<ApplyVo> getApplys() {
        return applys;
    }

    public void setApplys(List<ApplyVo> applys) {
        this.applys = applys;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderAmountStr() {
        return orderAmountStr;
    }

    public void setOrderAmountStr(String orderAmountStr) {
        this.orderAmountStr = orderAmountStr;
    }

    public String getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(String expiresTime) {
        this.expiresTime = expiresTime;
    }

    public static PayInfo getPayOrderVo(PayOrder payOrder) {
        if (payOrder == null) {
            return null;
        }
        PayInfo vo = new PayInfo();
        List<ApplyInfo> applyInfos = payOrder.getApplyInfos();
        vo.setApplys(ApplyVo.getVos(applyInfos));
        vo.setOrderStatus(payOrder.getOrderStatus());
        vo.setOrderStatusDesc(OrderStatusEnum.getDescByCode(payOrder.getOrderStatus()));
        vo.setOrderTime(DateUtil.format(payOrder.getOrderTime()));
        vo.setExpiresTime(DateUtil.format(payOrder.getExpireTime()));
        vo.setOrderCode(payOrder.getOrderCode());
        vo.setRemark(payOrder.getRemark());
        vo.setOrderAmount(payOrder.getOrderAmount());
        vo.setOrderAmountStr(AmountUtils.changeF2Y(payOrder.getOrderAmount()));

        return vo;
    }

}
