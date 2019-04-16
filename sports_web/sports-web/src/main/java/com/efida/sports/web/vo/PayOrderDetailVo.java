/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.vo;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.efida.sport.admin.facade.model.SpAthletesEnrollModel;
import com.efida.sports.entity.PayOrder;
import com.efida.sports.enums.OrderStatusEnum;
import com.efida.sports.util.AmountUtils;

/**
 * 
 * @author zoutao
 * @version $Id: PayOrderVo.java, v 0.1 2018年5月22日 下午5:18:46 zoutao Exp $
 */
public class PayOrderDetailVo {

    private static Logger  log        = LoggerFactory.getLogger(PayOrderDetailVo.class);

    /**
     * 订单编号
     */
    private String         orderCode;
    /**
     * 报名信息
     */
    private List<ApplyVo>  applys;
    /**
     * 团队成员信息
     */
    private List<TeamVo>   teams;

    /**
    * 领队信息
    */
    private LeaderInfoVo   leader;

    private List<PlayerVo> playerVos;

    /**
     * 订单金额
     */
    private Long           orderAmount;
    /**
     * 订单金额 转换后
     */
    private String         orderAmountStr;
    /**
     * 订单创建时间
     */
    private Date           orderTime;
    /**
     * 备注
     */
    private String         remark;

    /**
     * 订单状态
     */
    private String         orderStatus;
    /**
     *状态描述
     */
    private String         orderStatusDesc;
    /**
     * 团队人数
     */
    private String         personGroup;

    private String         eventType;

    private boolean        canAddTeam = true;
    /**
     * 人员描述
     */
    private String         teamRemark;
    /**
     * 需要多少人报名
     */
    private String         registrationNum;

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

    public LeaderInfoVo getLeader() {
        return leader;
    }

    public void setLeader(LeaderInfoVo leader) {
        this.leader = leader;
    }

    public String getPersonGroup() {
        return personGroup;
    }

    public void setPersonGroup(String personGroup) {
        this.personGroup = personGroup;
    }

    public List<TeamVo> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamVo> teams) {
        this.teams = teams;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public List<PlayerVo> getPlayerVos() {
        return playerVos;
    }

    public void setPlayerVos(List<PlayerVo> playerVos) {
        this.playerVos = playerVos;
    }

    public boolean isCanAddTeam() {
        return canAddTeam;
    }

    public void setCanAddTeam(boolean canAddTeam) {
        this.canAddTeam = canAddTeam;
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

    public static PayOrderDetailVo getVo(PayOrder payOrder,
                                         SpAthletesEnrollModel athiete) throws Exception {
        log.info("订单详情转换,player：{} , athiete: {}", JSON.toJSONString(payOrder),
            JSON.toJSONString(athiete));
        PayOrderDetailVo vo = new PayOrderDetailVo();
        vo.setApplys(ApplyVo.getVos(payOrder.getApplyInfos()));
        vo.setOrderAmount(payOrder.getOrderAmount());
        vo.setOrderAmountStr(AmountUtils.changeF2Y(payOrder.getOrderAmount()));
        vo.setOrderCode(payOrder.getOrderCode());
        vo.setOrderTime(payOrder.getOrderTime());
        vo.setRemark(payOrder.getRemark());
        vo.setOrderStatus(payOrder.getOrderStatus());
        vo.setOrderStatusDesc(OrderStatusEnum.getDescByCode(payOrder.getOrderStatus()));
        vo.setLeader(LeaderInfoVo.getVO(payOrder.getLeaderInfo()));
        vo.setPersonGroup(payOrder.getPersonGroup() + "");
        vo.setEventType(payOrder.getApplyType());
        vo.setTeams(TeamVo.getVos(payOrder.getPlayers(), athiete));
        if (payOrder.getPersonGroup() != 0 && payOrder.getPlayers() != null) {
            vo.setCanAddTeam(payOrder.getPlayers().size() < payOrder.getPersonGroup());
        }

        vo.setRegistrationNum(payOrder.getPersonGroup() + "");
        if ("0".equals(vo.getRegistrationNum())) {
            vo.setTeamRemark("至少2人");
        } else {
            int playerNum = payOrder.getPlayers() == null ? 0 : payOrder.getPlayers().size();
            vo.setTeamRemark(playerNum + "/" + vo.getRegistrationNum() + "人");
        }
        return vo;

    }

}
