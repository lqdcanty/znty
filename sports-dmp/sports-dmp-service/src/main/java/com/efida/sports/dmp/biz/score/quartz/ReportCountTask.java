/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.dmp.biz.score.quartz;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.efida.sports.dmp.biz.score.OpenApplyinfoCountService;
import com.efida.sports.dmp.biz.score.OpenScoreCountService;
import com.efida.sports.dmp.biz.score.ReportMatchFinishService;
import com.efida.sports.dmp.biz.score.ReportMatchSourceService;
import com.efida.sports.dmp.biz.score.ReportUnitEnrollService;
import com.efida.sports.dmp.biz.score.ReportUnitFinishService;
import com.efida.sports.dmp.dao.entity.ReportMatchFinishEntity;
import com.efida.sports.dmp.dao.entity.ReportMatchSourceEntity;
import com.efida.sports.dmp.dao.entity.ReportUnitEnrollEntity;
import com.efida.sports.dmp.dao.entity.ReportUnitFinishEntity;
import com.efida.sports.dmp.utils.DateUtil;

import cn.evake.auth.service.common.CacheService;

/**
 * 报表统计定时任务
 * 
 * @author zengbo
 * @version $Id: ReportCountTask.java, v 0.1 2018年8月29日 下午3:15:17 zengbo Exp $
 */
@Component
public class ReportCountTask {

    //
    private Logger                    logger                  = LoggerFactory
        .getLogger(ReportCountTask.class);

    private final String              REPORT_ENROLL_TASK_LOCK = "report_enroll_task_lock";

    private final String              REPORT_UNIT_TASK_LOCK   = "report_unit_task_lock";

    private final String              DAY_UNIT_TASK_LOCK      = "day_unit_task_lock";

    @Autowired
    private OpenApplyinfoCountService openApplyinfoCountService;

    @Autowired
    private OpenScoreCountService     openScoreCountService;

    @Autowired
    private ReportUnitEnrollService   reportUnitEnrollService;

    @Autowired
    private ReportUnitFinishService   reportUnitFinishService;

    @Autowired
    private ReportMatchSourceService  reportMatchSourceService;

    @Autowired
    private ReportMatchFinishService  reportMatchFinishService;

    @Autowired
    private CacheService              cacheSevcice;

    /**
     * 定时每隔2小时生成报名统计数据
     */
    @Scheduled(cron = "* * */2 * * ?")
    public void timingCreateApplyinfoDataTask() {
        try {
            String status = cacheSevcice.get(REPORT_ENROLL_TASK_LOCK);
            if (!StringUtils.isEmpty(status)) {
                return;
            }
            cacheSevcice.put(REPORT_ENROLL_TASK_LOCK, UUID.randomUUID().toString(), 2000);
            List<String> applydates = openApplyinfoCountService.queryUnitEnrollDate();
            for (String applydate : applydates) {
                List<ReportUnitEnrollEntity> unitList = openApplyinfoCountService
                    .queryUnitEnrollCount(applydate);
                reportUnitEnrollService.saveReportUnitList(unitList);
                List<ReportMatchSourceEntity> matchList = openApplyinfoCountService
                    .queryMatchEnrollCount(applydate);
                reportMatchSourceService.saveReportMatchSourceList(matchList);
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            cacheSevcice.remove(REPORT_ENROLL_TASK_LOCK);
        }
    }

    /**
     * 定时每隔2小时生成成绩统计数据
     */
    @Scheduled(cron = "* * */2 * * ?")
    public void timingCreateScoreDataTask() {
        try {
            String status = cacheSevcice.get(REPORT_UNIT_TASK_LOCK);
            if (!StringUtils.isEmpty(status)) {
                return;
            }
            cacheSevcice.put(REPORT_UNIT_TASK_LOCK, UUID.randomUUID().toString(), 2000);
            List<String> partdates = openScoreCountService.queryUnitScoreDate();
            for (String partdate : partdates) {
                List<ReportUnitFinishEntity> unitRecords = openScoreCountService
                    .queryUnitScoreCount(partdate);
                reportUnitFinishService.saveReportUnitFinishList(unitRecords);
                List<ReportMatchFinishEntity> matchRecords = openScoreCountService
                    .queryMatchScoreCount(partdate);
                reportMatchFinishService.saveReportMatchFinishList(matchRecords);
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            cacheSevcice.remove(REPORT_UNIT_TASK_LOCK);
        }
    }

    /**
     * 每5分钟更新一次当日数据
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void timingUpdateDayDataTask() {
        try {
            String status = cacheSevcice.get(DAY_UNIT_TASK_LOCK);
            if (!StringUtils.isEmpty(status)) {
                return;
            }
            cacheSevcice.put(DAY_UNIT_TASK_LOCK, UUID.randomUUID().toString(), 2000);
            String date = DateUtil.formatDate(new Date());
            List<ReportUnitEnrollEntity> unitList = openApplyinfoCountService
                .queryUnitEnrollCount(date);
            reportUnitEnrollService.saveReportUnitList(unitList);
            List<ReportMatchSourceEntity> matchList = openApplyinfoCountService
                .queryMatchEnrollCount(date);
            reportMatchSourceService.saveReportMatchSourceList(matchList);
            List<ReportUnitFinishEntity> unitRecords = openScoreCountService
                .queryUnitScoreCount(date);
            reportUnitFinishService.saveReportUnitFinishList(unitRecords);
            List<ReportMatchFinishEntity> matchRecords = openScoreCountService
                .queryMatchScoreCount(date);
            reportMatchFinishService.saveReportMatchFinishList(matchRecords);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            cacheSevcice.remove(DAY_UNIT_TASK_LOCK);
        }
    }

}
