package com.efida.sports.dmp.count;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.efida.sports.dmp.base.BaseTest;
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
import com.efida.sports.dmp.utils.NumberUtil;

/** 
 * 测试导出Excel数据 
 *  
 * @author jiaxingHuang 
 * @version 1.0 2014年8月20日 
 *  
 */
public class CountTest extends BaseTest {

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

    /**
     * 承办方报名数据
     * 
     * @throws Exception
     */
    @Test
    public void unittest() throws Exception {
        List<String> dates = openApplyinfoCountService.queryUnitEnrollDate();
        System.out.println(JSON.toJSONString(dates));
        for (String str : dates) {
            List<ReportUnitEnrollEntity> list = openApplyinfoCountService.queryUnitEnrollCount(str);
            System.out.println(JSON.toJSONString(list));
            //                reportUnitEnrollService.saveReportUnitList(list);
        }
    }

    @Test
    public void countUnitEnrollByCode() throws Exception {
        int s = reportUnitEnrollService.countUnitEnrollByCode("test", "2018-08-30");
        System.out.println(s);
    }

    /**
     * 赛事报名数据
     * 
     * @throws Exception
     */
    @Test
    public void matchtest() throws Exception {
        List<String> dates = openApplyinfoCountService.queryUnitEnrollDate();
        for (String str : dates) {
            List<ReportMatchSourceEntity> lists = openApplyinfoCountService
                .queryMatchEnrollCount(str);
            System.out.println(JSON.toJSONString(lists));
            //            reportMatchSourceService.saveReportMatchSourceList(lists);

        }
    }

    /**
     * 承办方完善人数统计
     * 
     * @throws Exception
     */
    @Test
    public void unitScoreTest() throws Exception {
        List<String> dates = openScoreCountService.queryUnitScoreDate();
        for (String str : dates) {
            List<ReportUnitFinishEntity> records = openScoreCountService.queryUnitScoreCount(str);
            System.out.println(JSON.toJSONString(records));
            //            reportUnitFinishService.saveReportUnitFinishList(records);
        }
    }

    /**
     * 赛事完善人数统计
     * 
     * @throws Exception
     */
    @Test
    public void matchScoreTest() throws Exception {
        List<String> dates = openScoreCountService.queryUnitScoreDate();
        for (String str : dates) {
            List<ReportMatchFinishEntity> records = openScoreCountService.queryMatchScoreCount(str);
            System.out.println(JSON.toJSONString(records));
            //            reportMatchFinishService.saveReportMatchFinishList(records);
        }
    }

    //    @Test
    //    public void test() {
    //        System.out
    //            .println("报名表开始时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    //        List<String> applydates = openApplyinfoCountService.queryUnitEnrollDate();
    //        for (String applydate : applydates) {
    //            System.out.println(
    //                "承办方报名表开始时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    //            List<ReportUnitEnrollEntity> list = openApplyinfoCountService
    //                .queryUnitEnrollCount(applydate);
    //            System.out.println(
    //                "承办方报名表结束时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    //            System.out.println(
    //                "赛事报名表开始时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    //            List<ReportMatchSourceEntity> lists = openApplyinfoCountService
    //                .queryMatchEnrollCount(applydate);
    //            System.out.println(
    //                "赛事报名表结束时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    //        }
    //        List<String> scoredates = openScoreCountService.queryUnitScoreDate();
    //        for (String scoredate : scoredates) {
    //            System.out.println(
    //                "承办方完赛开始时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    //            List<ReportUnitFinishEntity> unitList = openScoreCountService
    //                .queryUnitScoreCount(scoredate);
    //            System.out.println(
    //                "承办方完赛结束时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    //            System.out.println(
    //                "赛事完赛开始时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    //            List<ReportMatchFinishEntity> matchList = openScoreCountService
    //                .queryMatchScoreCount(scoredate);
    //            System.out.println(
    //                "赛事完赛结束时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    //        }
    //        System.out
    //            .println("结束时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    //    }

    /**
     * 总报名人数 百分比
     * 
     * @throws Exception
     */
    @Test
    public void testPercent() throws Exception {
        //        int enroll = reportUnitEnrollService.countTotalUnitEnroll(null, null);
        //        int finish = reportUnitFinishService.countTotalUnitFinish(null, null);

        int enroll = reportUnitEnrollService.countTotalUnitEnroll("2018-07-01", "2018-08-30");
        int finish = reportUnitFinishService.countTotalUnitFinish("2018-07-01", "2018-08-30");

        System.out.println(NumberUtil.percentageConvert(finish, enroll));
    }

    /**
     * 承办方报名人次 百分比
     * 
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        //        int enroll = reportUnitEnrollService.countTotalUnitEnroll(null, null);
        //        int finish = reportUnitFinishService.countTotalUnitFinish(null, null);

        int enroll = reportUnitEnrollService.countTotalUnitEnroll("2018-07-01", "2018-08-30");
        int finish = reportUnitFinishService.countTotalUnitFinish("2018-07-01", "2018-08-30");

        System.out.println(NumberUtil.percentageConvert(finish, enroll));
    }

}