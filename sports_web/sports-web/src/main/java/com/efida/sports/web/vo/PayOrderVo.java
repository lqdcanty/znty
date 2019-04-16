/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.entity.PayOrder;
import com.efida.sports.enums.OrderStatusEnum;
import com.efida.sports.util.AmountUtils;
import com.efida.sports.util.DateUtil;

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

    private String        orderTimeStr;
    /**
     * 备注
     */
    private String        remark;

    /**
     * 订单状态
     */
    private String        orderStatus;
    /**
     *状态描述
     */
    private String        orderStatusDesc;
    /**
     * 赛事名称
     */
    private String        matchName;
    /**
     * 项目分类名称
     */
    private String        gameName;
    /**
     * 比赛项目名称
     */
    private String        itemName;

    /**
     * 赛事图片
     */
    private String        matchImg;

    private Boolean       canAddTeam;

    private String        teamRemark;

    private String        registrationNum;

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

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getMatchImg() {
        return matchImg;
    }

    public void setMatchImg(String matchImg) {
        this.matchImg = matchImg;
    }

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

    public String getOrderTimeStr() {
        return orderTimeStr;
    }

    public void setOrderTimeStr(String orderTimeStr) {
        this.orderTimeStr = orderTimeStr;
    }

    public String getTeamRemark() {
        return teamRemark;
    }

    public void setTeamRemark(String teamRemark) {
        this.teamRemark = teamRemark;
    }

    public String getRegistrationNum() {
        return registrationNum;
    }

    public void setRegistrationNum(String registrationNum) {
        this.registrationNum = registrationNum;
    }

    public Boolean getCanAddTeam() {
        return canAddTeam;
    }

    public void setCanAddTeam(Boolean canAddTeam) {
        this.canAddTeam = canAddTeam;
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
        vo.setOrderTimeStr(DateUtil.format(payOrder.getOrderTime()));
        List<ApplyInfo> applyInfos = payOrder.getApplyInfos();
        String itemName = "";
        for (ApplyInfo applyInfo : applyInfos) {
            if (StringUtils.isNotBlank(applyInfo.getMatchGroupName())) {
                itemName += applyInfo.getMatchGroupName() + "_" + applyInfo.getEventName() + "/";
            } else {
                itemName += applyInfo.getEventName() + "/";
            }
        }
        ApplyInfo applyInfo = applyInfos.get(0);
        Integer registrationNum = applyInfo.getRegistrationNum();
        vo.setRegistrationNum(registrationNum == null ? "0" : registrationNum.toString());
        if ("0".equals(vo.getRegistrationNum())) {
            vo.setTeamRemark("至少2人");
            vo.setCanAddTeam(true);
        } else {
            int playerNum = payOrder.getPlayers() == null ? 0 : payOrder.getPlayers().size();
            vo.setCanAddTeam(playerNum < Integer.valueOf(vo.getRegistrationNum()));
            vo.setTeamRemark(playerNum + "/" + vo.getRegistrationNum() + "人");
        }
        String gameName = applyInfo.getGameName();
        String matchName = applyInfo.getMatchName();
        String matchImg = applyInfo.getPoster();
        vo.setMatchImg(matchImg);
        vo.setGameName(gameName);
        vo.setMatchName(matchName);
        vo.setItemName(getFormatItemName(itemName));
        vo.setOrderStatus(payOrder.getOrderStatus());
        vo.setRemark(payOrder.getRemark());
        vo.setOrderStatusDesc(OrderStatusEnum.getDescByCode(payOrder.getOrderStatus()));
        if (OrderStatusEnum.WAIT_PAY.getCode().equals(payOrder.getOrderStatus())
            && new Date().after(payOrder.getExpireTime())) {
            vo.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
            vo.setOrderStatusDesc(OrderStatusEnum.CANCEL.getCname());
        }
        return vo;
    }

    public static String getFormatItemName(String itemName) {
        if (StringUtils.isBlank(itemName)) {
            return "";
        }
        int length = itemName.length();
        if (length > 19) {
            itemName = itemName.substring(0, 16) + "...";
            return itemName;
        }
        itemName = itemName.substring(0, length - 1);
        return itemName;
    }

}
