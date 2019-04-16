/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.vo;

import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.entity.PayOrder;
import com.efida.sports.enums.OrderStatusEnum;
import com.efida.sports.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import com.efida.sports.enums.EventTypeEnums;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author zoutao
 * @version $Id: PayOrderVo.java, v 0.1 2018年6月13日 下午3:04:47 zoutao Exp $
 */
public class PayOrderVo implements Serializable {

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
     * 赛事名称
     */
    private String            matchName;
    /**
     * 项目分类名称
     */
    private String            gameName;
    /**
     * 订单创建时间
     */
    private String            orderTime;
    /**
     * 比赛项目名称
     */
    private String            itemName;

    /**
     * 赛事图片
     */
    private String            matchImg;

    /**
     * 比赛项类型
     * @see EventTypeEnums
     */
    private String            eventType;


    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getMatchImg() {
        return matchImg;
    }

    public void setMatchImg(String matchImg) {
        this.matchImg = matchImg;
    }

    public static List<PayOrderVo> getVos(List<PayOrder> list) {
        List<PayOrderVo> vos = new ArrayList<PayOrderVo>();
        if (list == null || list.size() < 1) {
            return vos;
        }
        for (PayOrder payOrder : list) {
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
        List<ApplyInfo> applyInfos = payOrder.getApplyInfos();
        String itemName = "";
        for (ApplyInfo applyInfo : applyInfos) {
            if (StringUtils.isNotBlank(applyInfo.getMatchGroupName())) {
                itemName += applyInfo.getMatchGroupName() + "_" + applyInfo.getEventName() + "/";
            } else {
                itemName += applyInfo.getEventName() + "/";
            }
        }
        PayOrderVo vo = new PayOrderVo();

        //有部分脏数据
        if (applyInfos != null && applyInfos.size() > 0) {
            ApplyInfo applyInfo = applyInfos.get(0);
            String gameName = applyInfo.getGameName();
            String matchName = applyInfo.getMatchName();
            String matchImg = applyInfo.getPoster();
            String eventType = applyInfo.getEventType();
            vo.setEventType(eventType);
            vo.setMatchImg(matchImg);
            vo.setGameName(gameName);
            vo.setMatchName(matchName);
        }

        vo.setItemName(getFormatItemName(itemName));
        vo.setOrderStatus(payOrder.getOrderStatus());
        vo.setOrderStatusDesc(OrderStatusEnum.getDescByCode(payOrder.getOrderStatus()));
        if (OrderStatusEnum.WAIT_PAY.getCode().equals(payOrder.getOrderStatus())
            && new Date().after(payOrder.getExpireTime())) {
            vo.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
            vo.setOrderStatusDesc(OrderStatusEnum.CANCEL.getCname());
        }
        vo.setOrderTime(DateUtil.format(payOrder.getOrderTime()));
        vo.setOrderCode(payOrder.getOrderCode());
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
