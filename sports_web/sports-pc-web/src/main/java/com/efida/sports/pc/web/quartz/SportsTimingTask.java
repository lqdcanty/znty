/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.efida.sports.pc.web.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efida.sport.admin.facade.model.SpMatchAllInfoModel;
import com.efida.sports.constants.Constants;
import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.entity.ApplySendLog;
import com.efida.sports.entity.Player;
import com.efida.sports.enums.SendStatusEnum;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.IApplyInfoService;
import com.efida.sports.service.IApplySendLogService;
import com.efida.sports.service.IPayOrderService;
import com.efida.sports.service.IPlayerService;
import com.efida.sports.service.MsgService;
import com.efida.sports.service.dubbo.intergration.SpMatchFacadeClient;

/**
 * 时器任务
 * 
 * @author zengbo
 * @version $Id: SportsTimingTask.java, v 0.1 2018年7月5日 下午12:07:42 zengbo Exp $
 */
@Component
public class SportsTimingTask {

    private Logger               log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IPayOrderService     iPayOrderService;

    @Autowired
    private SpMatchFacadeClient  spMatchFacadeClient;

    @Autowired
    private CacheService         cacheService;

    @Autowired
    private MsgService           msgService;

    @Autowired
    private IApplyInfoService    iApplyInfoService;

    @Autowired
    private IPlayerService       iPlayerService;

    @Autowired
    private IApplySendLogService iApplySendLogService;

    /**
     * 每天9点定时发送短信
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void timingSendMessage() {
        log.info("短信发送定时器开始执行 ：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        this.excuteQuartz(null);
        log.info("短信发送定时器执行结束 ：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    /**
     * 每天1点重新发送失败短信
     */
    @Scheduled(cron = "0 0 13 * * ?")
    public void repeatSendMessage() {
        log.info("重新发生短信开始执行 ：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        this.excuteQuartz(SendStatusEnum.FAIL.getCode());
        log.info("重新发生短信执行结束 ：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    /**
     * 每隔1个小时获取一次比赛中报名的数据
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void hourlySendMessage() {
        log.info("比赛中报名短信开始执行 ：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        try {
            List<ApplyInfo> list = iApplyInfoService.queryGameingData();
            for (ApplyInfo info : list) {
                if (!SendStatusEnum.SUCCESS.getCode().equals(info.getSendStatus())) {
                    this.sendSuccessMessage(info, true);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }

        log.info("比赛中报名短信执行结束 ：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    /**
     * 执行定时任务
     */
    public void excuteQuartz(String sendStatus) {
        try {
            List<ApplyInfo> list = iApplyInfoService.selectApplyInfoByDate(sendStatus);
            log.info(JSON.toJSONString(list));
            if (list != null && list.size() > 0) {
                for (ApplyInfo info : list) {
                    if (!SendStatusEnum.SUCCESS.getCode().equals(info.getSendStatus())) {
                        this.sendSuccessMessage(info, false);
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 发送定时短信
     * @param register
     * @param payOrder
     */
    public void sendSuccessMessage(ApplyInfo info, Boolean blean) {
        try {
            Player player = iPlayerService.getPersonalPlayerByApplyCode(info.getApplyCode());
            //            Player player = iPlayerService.getPlayerByPlayerCode(info.getPlayerCode());
            String value = "";
            boolean bool = false;
            try {
                value = cacheService.get(Constants.MESSAGE_VERIFY_CODE + info.getApplyCode());
            } catch (Exception e) {
                log.error("", e);
            }
            if (info != null && !Constants.MESSAGE_TREU.equals(value)) {
                SpMatchAllInfoModel spmodel = spMatchFacadeClient.getItemDetail(info.getSiteCode(),
                    info.getEventCode());
                if (StringUtils.isNotBlank(player.getPlayerPhone())) {
                    /* new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(30000);
                            } catch (InterruptedException e) {
                            }
                        }
                    }.start();*/
                    String partCode = "";
                    if ("lantianlvye".equals(info.getUnitCode())) {
                        String extPro = player.getExtPro();
                        if (StringUtils.isNotBlank(extPro)) {
                            JSONObject obj = JSON.parseObject(extPro);
                            partCode = (String) obj.get("参赛验证码");
                        }
                    }

                    if (blean) {//比赛中报名
                        bool = msgService.flourlyMessage(info.getMatchName(),
                            info.getMatchGroupName(), player.getPlayerPhone(),
                            spmodel.getGroupItemInfo().getItemName(),
                            spmodel.getGroupItemInfo().getStartTime(),
                            spmodel.getPayAreaInfo().getFieldAddress(),
                            spmodel.getGroupItemInfo().getEndTime(), partCode);
                    } else {
                        bool = msgService.sendSuccessMessage(info.getMatchName(),
                            info.getMatchGroupName(), player.getPlayerPhone(),
                            spmodel.getGroupItemInfo().getItemName(),
                            spmodel.getGroupItemInfo().getStartTime(),
                            spmodel.getPayAreaInfo().getFieldAddress(),
                            spmodel.getGroupItemInfo().getEndTime(), partCode);
                    }
                    if (bool) {
                        iApplyInfoService.updateSendStatus(info.getApplyCode(),
                            SendStatusEnum.SUCCESS.getCode());
                        cacheService.put(Constants.MESSAGE_VERIFY_CODE + info.getApplyCode(),
                            Constants.MESSAGE_TREU, 1000 * 200);
                        this.saveSendLog(info, SendStatusEnum.SUCCESS.getCode());
                    } else {
                        iApplyInfoService.updateSendStatus(info.getApplyCode(),
                            SendStatusEnum.FAIL.getCode());
                        this.saveSendLog(info, SendStatusEnum.FAIL.getCode());
                    }

                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 保存发送日志
     * 
     * @param info
     * @param sendStatus
     */
    public void saveSendLog(ApplyInfo info, String sendStatus) {
        try {
            ApplySendLog log = new ApplySendLog();
            log.setApplyCode(info.getApplyCode());
            //            log.setPlayerCode(info.getPlayerCode());
            log.setPayOrderCode(info.getPayOrderCode());
            log.setGameCode(info.getGameCode());
            log.setGameName(info.getGameName());
            log.setMatchCode(info.getMatchCode());
            log.setMatchName(info.getMatchName());
            log.setSiteCode(info.getSiteCode());
            log.setSiteName(info.getSiteName());
            log.setSendTime(new Date());
            log.setSendStatus(sendStatus);
            iApplySendLogService.saveSendLog(log);
        } catch (Exception e) {
            log.error("", e);
        }
    }

}
