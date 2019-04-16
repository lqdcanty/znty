/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.pc.web.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.efida.sports.entity.PayOrder;
import com.efida.sports.util.AmountUtils;

/**
 * 
 * @author zoutao
 * @version $Id: PayOrderVo.java, v 0.1 2018年5月22日 下午5:18:46 zoutao Exp $
 */
public class PayOrderVo {
    /**
     * 订单编号
     */
    private String        orderCode;
    /**
     * 报名信息
     */
    private List<ApplyVo> applys;
    /**
     * 订单金额
     */
    private Long          orderAmount;
    /**
     * 订单金额 转换后
     */
    private String        orderAmountStr;
    /**
     * 订单创建时间
     */
    private Date          orderTime;
    /**
     * 备注
     */
    private String        remark;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public List<ApplyVo> getApplys() {
        return applys;
    }

    public void setApplys(List<ApplyVo> applys) {
        this.applys = applys;
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

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static List<PayOrderVo> getVos(List<PayOrder> orders) {
        List<PayOrderVo> vos = new ArrayList<PayOrderVo>();
        if (orders == null || orders.size() < 1) {
            return vos;
        }
        for (PayOrder payOrder : orders) {
            PayOrderVo vo = getVo(payOrder);
            if (vo != null) {
                vos.add(vo);
            }
        }
        return vos;
    }

    public static PayOrderVo getVo(PayOrder payOrder) {
        if (payOrder == null) {
            return null;
        }
        PayOrderVo vo = new PayOrderVo();
        vo.setApplys(ApplyVo.getVos(payOrder.getApplyInfos()));
        vo.setOrderAmount(payOrder.getOrderAmount());
        vo.setOrderAmountStr(AmountUtils.changeF2Y(payOrder.getOrderAmount()));
        vo.setOrderCode(payOrder.getOrderCode());
        vo.setOrderTime(payOrder.getOrderTime());
        vo.setRemark(payOrder.getRemark());
        return vo;

    }

}
