/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.quartz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efida.sport.admin.facade.model.SpMatchGroupModel;
import com.efida.sport.dmp.facade.model.EnrollxInfoDto;
import com.efida.sport.dmp.facade.model.PlayerInfoDto;
import com.efida.sport.facade.enums.RegTerminalEnum;
import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.entity.LeaderInfo;
import com.efida.sports.entity.Player;
import com.efida.sports.enums.EventTypeEnums;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.IApplyInfoService;
import com.efida.sports.service.ILeaderInfoService;
import com.efida.sports.service.IPlayerService;
import com.efida.sports.service.dubbo.intergration.OpenEnrollFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpMatchFacadeClient;
import com.efida.sports.util.DateUtil;
import com.efida.sports.util.SeqWorkerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 报名数据同步任务 
 * @author zhiyang
 * @version $Id: EnrollSycTask.java, v 0.1 2018年7月5日 下午8:11:55 zhiyang Exp $
 */
@Component
public class EnrollSycTask {

    private Logger                 logger               = LoggerFactory
        .getLogger(EnrollSycTask.class);
    private final String           LOCK_ENROLL_TASK_KEY = "lock_for_enroll_task";

    @Autowired
    private CacheService           cacheSevcice;

    @Autowired
    private IApplyInfoService      iApplayInfoService;

    @Autowired
    private IPlayerService         iPlayerService;

    @Autowired
    private ILeaderInfoService     iLeaderInfoService;
    @Autowired
    private OpenEnrollFacadeClient openEnrollFacadeClient;
    @Autowired
    private SpMatchFacadeClient    spMatchFacadeClient;

    @Value("${runSycTask}")
    private Boolean                runSycTask;

    /**
         * 定时将成功报名信息同步到dmp系统
         */
    @Scheduled(cron = "0/30 * *  * * ? ")
    public void timingSycEnrollInfo2Dmp() {
        if (!runSycTask) {
            return;
        }
        try {
            Thread.sleep((long) (5000 * Math.random()));
        } catch (InterruptedException e) {
            logger.error("", e);
        }
        String status = cacheSevcice.get(LOCK_ENROLL_TASK_KEY);
        if (!StringUtils.isEmpty(status)) {
            return;
        }
        cacheSevcice.put(LOCK_ENROLL_TASK_KEY, SeqWorkerUtil.buildSingleId(), 200000);
        int pageSize = 200;
        int current = 1;
        String unitCode = null;
        try {
            while (true) {
                List<ApplyInfo> items = iApplayInfoService.selectUnSycInfos(current, pageSize);
                if (CollectionUtils.isEmpty(items)) {
                    break;
                }
                for (ApplyInfo item : items) {
                    syncOneItem(item);
                }

                if (items.size() < pageSize) {
                    break;
                }
            }
        } catch (Exception ex) {
            logger.error("", ex);
        } finally {
            cacheSevcice.remove(LOCK_ENROLL_TASK_KEY);
        }

    }

    private void syncOneItem(ApplyInfo item) {

        String applyCode = item.getApplyCode();
        List<Player> players = iPlayerService.selectPlayerByApplyInfoCode(applyCode);
        int syncTotal = item.getSentTotal() == null ? 1 : item.getSentTotal() + 1;
        long syncInterval = syncTotal * 30;
        if (syncInterval > 30 * 60) {
            syncInterval = 1800;
        }
        long nextSendTime = System.currentTimeMillis() + syncInterval * 1000;

        try {
            if (CollectionUtils.isEmpty(players)) {
                logger.error("报名编号为：{}运动员不存在！！！", applyCode);
                item.setSync((byte) 0);
                item.setNextSendTime(new Date(nextSendTime));
                item.setSendStatus("运动员信息不存在");
                item.setSentTotal(syncTotal);
                iApplayInfoService.updateById(item);
                return;
            }
            EnrollxInfoDto dto = createEnrollInfo(item, players);
            if (item.getEventType().equals(EventTypeEnums.group.getCode())) {
                LeaderInfo leader = iLeaderInfoService.getLearnInfoByApplyCode(item.getApplyCode());
                if (leader != null) {
                    dto.setLeaderName(leader.getLeaderName());
                    dto.setLeaderPhone(leader.getLeaderPhone());
                    dto.setTeamName(leader.getTeamName());
                    dto.setRegistrationNum(item.getRegistrationNum());
                }
            }

            String unitCode = item.getUnitCode();
            if("gudong".equalsIgnoreCase(unitCode) && RegTerminalEnum.apph5.getCode().equalsIgnoreCase(item.getChannelCode()) ){
                dto.setSource(Byte.valueOf("1"));
            }
            boolean success = openEnrollFacadeClient.submitEnrollInfo(unitCode, dto);
            if (success) {
                item.setSync((byte) 1);
                item.setSendStatus("success");
            } else {
                item.setSync((byte) 0);
                item.setNextSendTime(new Date(nextSendTime));
                item.setSendStatus("dmp同步接口返回失败!");
            }

        } catch (Exception ex) {
            logger.error("syncOneItem failed, applyCode:" + applyCode, ex);
            item.setSync((byte) 0);
            String syncStatus = ex.getMessage();
            if (syncStatus != null && syncStatus.length() > 30) {
                syncStatus = syncStatus.substring(0, 30);
            }
            item.setNextSendTime(new Date(nextSendTime));
            item.setSendStatus(syncStatus);
        } finally {
            item.setSentTotal(syncTotal);
        }
        iApplayInfoService.updateById(item);

    }

    private EnrollxInfoDto createEnrollInfo(ApplyInfo applyInfo, List<Player> players) {

        EnrollxInfoDto enroll = new EnrollxInfoDto();
        setApplyInfo(enroll, applyInfo);
        List<PlayerInfoDto> playerDatas = getPlayerDtoInfo(applyInfo.getApplyCode(), players);
        enroll.setPlayerData(playerDatas);
        return enroll;
    }

    private List<PlayerInfoDto> getPlayerDtoInfo(String applyCode, List<Player> items) {

        List<PlayerInfoDto> playerData = new ArrayList<PlayerInfoDto>();
        int i = 1;
        for (Player item : items) {

            PlayerInfoDto op = new PlayerInfoDto();
            op.setApplyCode(applyCode);
            op.setPlayerCode(item.getPlayerCode());
            op.setPlayerPhone(item.getPlayerPhone());
            op.setPlayerName(item.getPlayerName());
            op.setSex(item.getSex());
            op.setImgUrl(item.getImgUrl());
            op.setEmail(item.getEmail());
            op.setPlayerHeight(item.getPlayerHeight());
            op.setPlayerWeight(item.getPlayerWeight());
            op.setPlayerBirth(getDate(item.getPlayerBirth()));
            op.setPlayerNationality(item.getPlayerNationality());
            op.setPlayerWorkUnit(item.getPlayerWorkUnit());
            op.setPlayerAddress(item.getPlayerAddress());

            if (item.getPlayerCerType() == null) {
                op.setPlayerCerType(null);
            } else {
                op.setPlayerCerType(Integer.parseInt(item.getPlayerCerType()));
            }
            op.setPlayerCerNo(item.getPlayerCerNo());
            op.setPlayerBloodType(item.getPlayerBloodType());

            op.setPlayerNation(item.getPlayerNation());
            op.setPlayerClothSize(item.getPlayerClothSize());
            op.setPlayerEmergencyName(item.getPlayerEmergencyName());
            op.setPlayerEmergencyPhone(item.getPlayerEmergencyPhone());
            op.setImgUrl(item.getImgUrl());
            JSONObject extProp = new JSONObject();
            String extPro = item.getExtPro();
            if (!StringUtils.isEmpty(extPro)) {
                JSONObject parseObject = JSON.parseObject(extPro);
                Set<String> keySet = parseObject.keySet();
                Iterator<String> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    {
                        String key = iterator.next();
                        extProp.put(key, parseObject.get(key));
                    }
                }
            }
            String assetId = item.getAssettoId();
            if (!StringUtils.isEmpty(assetId)) {
                extProp.put("assettoId", assetId);
            }
            if (extProp.keySet().size() > 0) {
                op.setExtProp(extProp);
            }
            op.setPlayerNo(i++);
            playerData.add(op);
        }

        return playerData;
    }

    private void setApplyInfo(EnrollxInfoDto enroll, ApplyInfo applyInfo) {

        enroll.setEntryFee(applyInfo.getApplyAmount() * 1.0 / 100);

        if (applyInfo.getApplyTime() != null) {
            enroll.setApplyTime(getDate(applyInfo.getApplyTime()));
        } else {
            enroll.setApplyTime(getDate(applyInfo.getGmtModified()));
        }
        double amount = applyInfo.getApplyAmount() * 1.0 / 100;
        enroll.setEntryFee(amount);

        enroll.setApplyCode(applyInfo.getApplyCode());
        enroll.setIdempotentId(applyInfo.getApplyCode());
        enroll.setGameCode(applyInfo.getGameCode());
        enroll.setGameName(applyInfo.getGameName());
        enroll.setMatchCode(applyInfo.getMatchCode());
        enroll.setMatchName(applyInfo.getMatchName());

        enroll.setFieldCode(applyInfo.getSiteCode());
        enroll.setFieldName(applyInfo.getSiteName());

        SpMatchGroupModel groupModel = spMatchFacadeClient
            .getSpMatchGroupModel(applyInfo.getMatchGroupCode());
        if (groupModel != null) {
            enroll.setMatchGroupCode(groupModel.getGroupCode());
        }
        enroll.setMatchGroupName(applyInfo.getMatchGroupName());

        enroll.setEventCode(applyInfo.getEventCode());
        enroll.setEventName(applyInfo.getEventName());

        enroll.setEventStartTime(applyInfo.getEventStartTime());
        enroll.setEventEndTime(applyInfo.getEventEndTime());
        enroll.setEventType(applyInfo.getEventType());
        enroll.setChannelCode(applyInfo.getChannelCode());
        return;

    }

    private String getDate(Date date) {
        if (date == null) {
            return "";
        }
        return DateUtil.format(date, DateUtil.LONG_WEB_FORMAT);
    }

}
