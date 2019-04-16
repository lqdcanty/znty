///**
// * efida.com.cn Inc.
// * Copyright (c) 2004-2018 All Rights Reserved.
// */
//package com.efida.sports.test;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.alibaba.fastjson.JSON;
//import com.efida.sport.facade.model.ChannelTrendAnalysisModel;
//import com.efida.sport.facade.model.PagingResult;
//import com.efida.sport.facade.model.RegisterStatisticsModel;
//import com.efida.sport.facade.model.RegisterTrendAnalysisModel;
//import com.efida.sports.service.IRegisterDayReportService;
//
///**
// *
// * @author zoutao
// * @version $Id: UserTest.java, v 0.1 2018年2月5日 下午4:45:16 zoutao Exp $
// */
//public class UserTest extends BaseTest {
//
//    @Autowired
//    private IRegisterDayReportService reportService;
//
//    @Test
//    public void test1() {
//        reportService.batchCreateDailyReport();
//    }
//
//    /**
//     *
//        渠道用户分析 一段时间段类 各个渠道的新增用户数，总用户数，新增占比数据
//    Parameters:startTime 开始时间endTime 结束时间channelCodes 为空 去top5数据
//     */
//    @Test
//    public void channelAnalysis() {
//        Date startTime = daysAgoTime(100);
//        Date endTime = daysAgoTime(1);
//
//        List<String> channelCodes = new ArrayList<String>();
//        channelCodes.add("lantianlvye");
//        channelCodes.add("leizhansheji");
//        channelCodes.add("monisaiche");
//        List<RegisterTrendAnalysisModel> channelAnalysis = reportService.channelAnalysis(startTime,
//            endTime, null);
//        System.err.println(JSON.toJSONString(channelAnalysis));
//        System.out.println();
//    }
//
//    /**
//     *
//    
//    渠道用户趋势分析 一段时间段了各个渠道 用户新增数据
//    
//     */
//    @Test
//    public void channelTrendAnalysis() {
//        Date startTime = daysAgoTime(100);
//        Date endTime = daysAgoTime(1);
//
//        List<String> channelCodes = new ArrayList<String>();
//        channelCodes.add("lantianlvye");
//        channelCodes.add("leizhansheji");
//        channelCodes.add("monisaiche");
//        List<ChannelTrendAnalysisModel> channelTrendAnalysis = reportService
//            .channelTrendAnalysis(startTime, endTime, channelCodes);
//        System.err.println(JSON.toJSONString(channelTrendAnalysis));
//        System.out.println();
//    }
//
//    @Test
//    public void getRegisterGrowthTrend() {
//        Date startTime = daysAgoTime(100);
//        Date endTime = daysAgoTime(1);
//
//        List<String> channelCodes = new ArrayList<String>();
//
//        PagingResult<RegisterTrendAnalysisModel> registerGrowthTrend = reportService
//            .getRegisterGrowthTrend(2, 10, startTime, endTime);
//        System.err.println(JSON.toJSONString(registerGrowthTrend));
//        System.out.println();
//    }
//
//    @Test
//    public void getRegisterStatistics() {
//        RegisterStatisticsModel registerStatistics = reportService.getRegisterStatistics();
//        System.err.println(JSON.toJSONString(registerStatistics));
//        System.out.println();
//    }
//
//    private static Date daysAgoTime(int days) {
//        Calendar todayStart = Calendar.getInstance();
//        todayStart.set(Calendar.DATE, todayStart.get(Calendar.DATE) - days);
//        todayStart.set(Calendar.HOUR_OF_DAY, 0);
//        todayStart.set(Calendar.MINUTE, 0);
//        todayStart.set(Calendar.SECOND, 0);
//        todayStart.set(Calendar.MILLISECOND, 0);
//        Date time = todayStart.getTime();
//        return time;
//    }
//
//}
