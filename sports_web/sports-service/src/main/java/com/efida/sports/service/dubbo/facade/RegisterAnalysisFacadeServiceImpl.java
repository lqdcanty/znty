/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.facade;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.efida.sport.facade.model.AccessAnalysisModel;
import com.efida.sport.facade.model.ChannelTrendAnalysisModel;
import com.efida.sport.facade.model.DefaultResult;
import com.efida.sport.facade.model.PagingResult;
import com.efida.sport.facade.model.RegisterStatisticsModel;
import com.efida.sport.facade.model.RegisterTrendAnalysisModel;
import com.efida.sport.facade.model.TerminaStatisticsModel;
import com.efida.sport.facade.service.RegisterAnalysisFacadeService;
import com.efida.sports.service.IRegisterDayReportService;

/**
 * 
 * @author zoutao
 * @version $Id: RegisterAnalysisFacadeServiceImpl.java, v 0.1 2018年8月29日 下午8:09:32 zoutao Exp $
 */
@Service
public class RegisterAnalysisFacadeServiceImpl implements RegisterAnalysisFacadeService {

    @Autowired
    private IRegisterDayReportService reportService;

    private static Logger             logger = LoggerFactory
        .getLogger(RegisterAnalysisFacadeServiceImpl.class);

    @Override
    public DefaultResult<RegisterStatisticsModel> getRegisterStatistics() {
        DefaultResult<RegisterStatisticsModel> dr = new DefaultResult<RegisterStatisticsModel>();
        try {
            dr.setSuccess(true);
            dr.setResultObj(reportService.getRegisterStatistics());
        } catch (Exception e) {
            logger.error("", e);
            dr.setSuccess(false);
            dr.setErrorMsg("查询失败");
        }
        return dr;
    }

    @Override
    public DefaultResult<List<RegisterTrendAnalysisModel>> getRegisterGrowthTrend(Date startTime,
                                                                                  Date endTime) {
        DefaultResult<List<RegisterTrendAnalysisModel>> dr = new DefaultResult<List<RegisterTrendAnalysisModel>>();
        try {
            dr.setSuccess(true);
            dr.setResultObj(reportService.getRegisterGrowthTrend(startTime, endTime));
        } catch (Exception e) {
            logger.error("", e);
            dr.setSuccess(false);
            dr.setErrorMsg("查询失败");
        }
        return dr;
    }

    @Override
    public DefaultResult<PagingResult<RegisterTrendAnalysisModel>> getRegisterGrowthTrend(int currentPage,
                                                                                          int pageSize,
                                                                                          Date startTime,
                                                                                          Date endTime) {
        DefaultResult<PagingResult<RegisterTrendAnalysisModel>> dr = new DefaultResult<PagingResult<RegisterTrendAnalysisModel>>();
        try {
            dr.setSuccess(true);
            dr.setResultObj(
                reportService.getRegisterGrowthTrend(currentPage, pageSize, startTime, endTime));
        } catch (Exception e) {
            logger.error("", e);
            dr.setSuccess(false);
            dr.setErrorMsg("查询失败");
        }
        return dr;
    }

    @Override
    public DefaultResult<List<RegisterTrendAnalysisModel>> channelRegitserAnalysis(Date startTime,
                                                                                   Date endTime,
                                                                                   List<String> channelCodes) {
        DefaultResult<List<RegisterTrendAnalysisModel>> dr = new DefaultResult<List<RegisterTrendAnalysisModel>>();
        try {
            dr.setSuccess(true);
            List<RegisterTrendAnalysisModel> channelAnalysis = reportService
                .channelAnalysis(startTime, endTime, channelCodes);
            dr.setResultObj(channelAnalysis);
        } catch (Exception e) {
            logger.error("", e);
            dr.setSuccess(false);
            dr.setErrorMsg("查询失败");
        }
        return dr;
    }

    @Override
    public DefaultResult<List<ChannelTrendAnalysisModel>> channelTrendAnalysis(Date startTime,
                                                                               Date endTime,
                                                                               List<String> channelCodes) {

        DefaultResult<List<ChannelTrendAnalysisModel>> dr = new DefaultResult<List<ChannelTrendAnalysisModel>>();
        try {
            dr.setSuccess(true);
            List<ChannelTrendAnalysisModel> channelTrendAnalysis = reportService
                .channelTrendAnalysis(startTime, endTime, channelCodes);
            dr.setResultObj(channelTrendAnalysis);
        } catch (Exception e) {
            logger.error("", e);
            dr.setSuccess(false);
            dr.setErrorMsg("查询失败");
        }
        return dr;
    }

    @Override
    public DefaultResult<List<String>> getTopNumChannel(Date startTime, Date endTime, int topNum) {
        DefaultResult<List<String>> dr = new DefaultResult<List<String>>();
        try {
            dr.setSuccess(true);
            List<String> topNumChannel = reportService.getTopNumChannel(startTime, endTime, topNum);
            dr.setResultObj(topNumChannel);
        } catch (Exception e) {
            logger.error("", e);
            dr.setSuccess(false);
            dr.setErrorMsg("查询失败");
        }
        return dr;
    }

    @Override
    public DefaultResult<List<TerminaStatisticsModel>> getTerMinaStatisticsAnalysis(Date statiscsTime) {
        DefaultResult<List<TerminaStatisticsModel>> dr = new DefaultResult<List<TerminaStatisticsModel>>();
        try {
            dr.setSuccess(true);
            List<TerminaStatisticsModel> terMinaStatisticsAnalysis = reportService
                .getTerMinaStatisticsAnalysis(statiscsTime);
            dr.setResultObj(terMinaStatisticsAnalysis);
        } catch (Exception e) {
            logger.error("", e);
            dr.setSuccess(false);
            dr.setErrorMsg("查询失败");
        }
        return dr;

    }

    @Override
    public DefaultResult<List<AccessAnalysisModel>> getPCAccessAnalysis(Date startTime,
                                                                        Date endTime) {
        DefaultResult<List<AccessAnalysisModel>> dr = new DefaultResult<List<AccessAnalysisModel>>();
        try {
            dr.setSuccess(true);
            List<AccessAnalysisModel> accessAnalysis = reportService.getPCAccessAnalysis(startTime,
                endTime);
            dr.setResultObj(accessAnalysis);
        } catch (Exception e) {
            logger.error("", e);
            dr.setSuccess(false);
            dr.setErrorMsg("查询失败");
        }
        return dr;
    }

}
