/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.biz.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efida.easy.ucenter.facade.enums.TerminalEnum;
import com.efida.sport.facade.enums.RegTerminalEnum;
import com.efida.sports.biz.GudongEnrollService;
import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.entity.PayOrder;
import com.efida.sports.entity.Player;
import com.efida.sports.enums.ApplyStatusEnum;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.GudongMatchService;
import com.efida.sports.service.IApplyInfoService;
import com.efida.sports.service.IPayOrderService;
import com.efida.sports.service.IPlayerService;
import com.efida.sports.util.HttpUtils;
import com.efida.sports.util.MD5Utils;
import com.efida.sports.util.emoji.DateUtil;

/**
 * 
 * 只为咕咚定制页面服务
 * 
 * @author lizhiyang
 * @version $Id: GudongEnrollServiceImpl.java, v 0.1 2018年9月6日 上午8:20:01 lizhiyang Exp $
 */
@Service("gudongEnrollService")
public class GudongEnrollServiceImpl implements GudongEnrollService {

    private static Logger      log = LoggerFactory.getLogger(GudongEnrollServiceImpl.class);

    @Autowired
    private IPayOrderService   iPayOrderService;

    @Autowired
    private IApplyInfoService  iApplyInfoService;

    @Autowired
    private IPlayerService     playerService;

    @Autowired
    private GudongMatchService gudongMatchService;

    @Override
    public String createAppyInfo(ApplyInfo apply, Player player, RegTerminalEnum regterminal,
                                 String ip) {

        apply.setChannelCode(RegTerminalEnum.apph5.getCode());
        String str = iPayOrderService.createApply(apply, player, TerminalEnum.apph5, ip);

        return str;
    }

    @Override
    public void notifyGudong(String orderCode) {

        PayOrder order = iPayOrderService.getOrderDetail(orderCode);
        if (order == null) {
            log.info("咕咚报名成功订单号{}不存在", orderCode);
            return;
        }
        if (order.getApplyInfos() == null || order.getApplyInfos().size() <= 0) {
            log.info("咕咚报名成功订单号{}不存在相关报名信息", orderCode);
            return;
        }
        if (order.getPlayers() == null || order.getPlayers().size() <= 0) {
            log.info("咕咚报名成功订单号{}不存在相关运动员信息", orderCode);
            return;
        }
        ApplyInfo applyInfo = order.getApplyInfos().get(0);

        notifyGudong(applyInfo);
    }

    @Override
    public void notifyGudong(ApplyInfo applyInfo) {

        if (applyInfo == null) {
            return;
        }
        if (!(applyInfo.getUnitCode().equalsIgnoreCase("gudong")
              && applyInfo.getApplyStatus().equalsIgnoreCase(ApplyStatusEnum.SUCCESS.getCode()))) {
            log.info("只同步咕咚报名成功订单,applyCode:{}", applyInfo.getApplyCode());
            return;
        }
        if (applyInfo.getSyncUnit() != null && applyInfo.getSyncUnit() > 0) {
            return;
        }

        String applyCode = applyInfo.getApplyCode();
        Map<String, String> gudongMap = gudongMatchService.getAllDataMap();
        String url = "https://cop.codoon.com/mini_program/sign_online"; //咕咚同步地址

        String match_id = ""; //咕咚赛事id
        String group_id = ""; //咕咚对应该赛事分组id
        String user_name = ""; //姓名
        String phone = ""; //手机号
        String gender = ""; //性别 1:男 0:女
        String external_data = ""; //透传字段

        int unit = 0;
        int syncUnitTotal = 1;
        if (applyInfo.getSyncUnitTotal() != null) {
            syncUnitTotal = syncUnitTotal + 1;
        }

        long nextTime = syncUnitTotal * 30L;
        if (nextTime > 300) {
            nextTime = 300L;
        }
        String unitStatus = "";
        try {

            List<Player> players = playerService
                .selectPlayerByApplyInfoCode(applyInfo.getApplyCode());
            if (players == null || players.size() < 1) {
                return;
            }
            Player player = players.get(0);
            if (StringUtils.isNotBlank(applyInfo.getExternalData())) {
                external_data = applyInfo.getExternalData();
            }
            if (StringUtils.isNotBlank(applyInfo.getMatchCode())) {
                match_id = gudongMap.get(applyInfo.getMatchCode());
            }
            if (StringUtils.isNotBlank(applyInfo.getGroupEventCode())) {
                group_id = gudongMap.get(applyInfo.getGroupEventCode());
            }
            if (StringUtils.isNotBlank(player.getPlayerName())) {
                user_name = player.getPlayerName();
            }
            if (StringUtils.isNotBlank(player.getPlayerPhone())) {
                phone = player.getPlayerPhone();
            }
            if (StringUtils.isNotBlank(player.getSex())) {
                if (player.getSex().equalsIgnoreCase("m")) {
                    gender = "1";
                }
                if (player.getSex().equalsIgnoreCase("f")) {
                    gender = "0";
                }
            }

            if (StringUtils.isEmpty(group_id)) {
                unit = 2;
                throw new BusinessException("咕咚分组编号group_id为空! ");
            }
            if (StringUtils.isEmpty(match_id)) {
                unit = 2;
                throw new BusinessException("咕咚分组编号match_id为空! ");
            }

            String appClientId = "zntyydh";
            String secrectKey = "zntyydh";
            String timestamp = DateUtil.format(new Date(), DateUtil.LONG_FORMAT);
            url += "?";
            url += "clientId=" + appClientId;
            url += "&timestamp=" + timestamp;
            url += "&orderCode=" + applyCode;
            String signData = appClientId + secrectKey + timestamp + match_id + group_id + user_name
                              + phone + gender;
            String sign = MD5Utils.MD5Encode(signData).toUpperCase();
            url += "&sign=" + sign;

            JSONObject json = new JSONObject();
            json.put("match_id", Integer.parseInt(match_id));
            json.put("group_id", Integer.parseInt(group_id));
            json.put("user_name", user_name);
            json.put("phone_number", phone);
            json.put("gender", Integer.parseInt(gender));
            json.put("external_data", external_data);
            json.put("orderCode", applyInfo.getApplyCode());

            String data = json.toJSONString();
            log.info("咕咚报名成功,applyCode:{}, 同步data信息：{}", applyCode, data);
            String result = HttpUtils.executePostJSON(url, data);
            log.info("咕咚报名成功订单,applyCode:{}同步返回信息：{}", applyCode, result);

            if (StringUtils.isNotBlank(result)) {
                Map map = JSON.parseObject(result);
                if (map != null) {
                    if (map.get("status") != null
                        && map.get("status").toString().equalsIgnoreCase("ok")) {
                        unit = 1;
                        unitStatus = "同步成功";
                    } else {
                        if (map.get("description") != null
                            && StringUtils.isNotBlank(map.get("description").toString())) {
                            unitStatus = map.get("description").toString();
                        }
                    }
                }
                if (unitStatus.equalsIgnoreCase("已经报名了")) {
                    unit = 1;
                    unitStatus = "同步成功";
                }
            }
        } catch (BusinessException ex) {
            unitStatus = ex.getMessage();
            log.error("", ex);
        } catch (Exception ex) {
            unitStatus = "发生异常!" + ex.getMessage();
            log.error("", ex);
        }

        Date nextSyncTime = new Date(System.currentTimeMillis() + nextTime * 1000);
        if (unitStatus != null && unitStatus.length() > 24) {
            unitStatus = unitStatus.substring(0, 24);
        }
        applyInfo.setSyncUnitTotal(syncUnitTotal);
        applyInfo.setSyncUnit(unit);
        applyInfo.setSyncUnitStatus(unitStatus);
        applyInfo.setSyncUnitNextTime(nextSyncTime);
        iApplyInfoService.updateSynchronousData(applyInfo);
    }
}
