package com.efida.sports.quartz;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.entity.FinancialStatisticsReport;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.IApplyInfoService;
import com.efida.sports.service.IFinancialStatisticsReportService;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;
import com.efida.sports.util.DateUtil;
import com.efida.sports.util.SeqWorkerUtil;

@Component
public class ReportTask {

    private Logger                            logger                            = LoggerFactory
        .getLogger(ReportTask.class);

    private final String                      CACHE_FINANCIALREPORT_SYNC_STATUS = "sports_cache_financialreport_task";

    @Autowired
    private CacheService                      cacheSevcice;
    /**
     * 订单状态
     */
    public final static String                ORDER_STATUS                      = "success";
    /**
     * 生成和支付订单为0
     */
    public final static int                   REPORT_IS_EMPTY                   = 2;
    /**
     * 订单有效时间
     */
    public final static long                  ORDER_ACTIVE_TIME                 = 1000 * 60 * 10;

    @Autowired
    private IFinancialStatisticsReportService IFinancialStatisticsReportService;

    @Autowired
    private IApplyInfoService                 applyInfoService;

    @Autowired
    private UcenterRegisterFacadeClient       ucenterRegisterFacadeClient;

    @Value("${runSycTask}")
    private Boolean                           runSycTask;

    /**
     * 定时导入数据到财务统计表
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void saveReport() {
        logger.info("*************save*FinancialStatisticsReport*start*************");
        String status = cacheSevcice.get(CACHE_FINANCIALREPORT_SYNC_STATUS);
        if (StringUtils.isNotBlank(status)) {
            return;
        }

        cacheSevcice.put(CACHE_FINANCIALREPORT_SYNC_STATUS, SeqWorkerUtil.buildSingleId(),
            1000 * 3600 * 23);
        try {
            Date startTime = DateUtil.getDayAgoTime(1);
            Date endTime = new Date(startTime.getTime() + 1000 * 3600 * 24 - 1000);

            //在前一天  00:00:00-23:59:59  时间段内，生成的所有订单
            List<FinancialStatisticsReport> orderlist = IFinancialStatisticsReportService
                .getPayOrderListByOrderOrPayTime(startTime, endTime, null, null, null);

            //在前一天  00:00:00-23:59:59  时间段内支付,且在在前一天00:00:00之前生成的的所有订单(订单只有10分钟支付时间)
            List<FinancialStatisticsReport> paylist = IFinancialStatisticsReportService
                .getPayOrderListByOrderOrPayTime(new Date(startTime.getTime() - ORDER_ACTIVE_TIME),
                    startTime, startTime, new Date(startTime.getTime() + ORDER_ACTIVE_TIME),
                    ORDER_STATUS);

            int orderlistSize = orderlist.size();
            int paylistSize = paylist.size();

            if (orderlistSize > 0) {
                //保存财务统计表
                saveList(orderlist, endTime);
            }
            if (paylistSize > 0) {
                //隔天支付修改状态
                updateList(paylist);
            }

            //如果生成订单和支付订单 都为空  保存一条空数据，用于查询时展示
            if (orderlistSize == 0 && paylistSize == 0) {
                FinancialStatisticsReport financialStatisticsReport = new FinancialStatisticsReport();
                financialStatisticsReport.setOrderCode("null" + System.currentTimeMillis());
                financialStatisticsReport.setOrderType("apply");
                financialStatisticsReport.setReportDate(endTime);
                financialStatisticsReport.setIsDelete(REPORT_IS_EMPTY);
                IFinancialStatisticsReportService.insert(financialStatisticsReport);
                return;
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            cacheSevcice.remove(CACHE_FINANCIALREPORT_SYNC_STATUS);
        }

        logger.info("*************save*FinancialStatisticsReport*end*************");
    }

    /**
     * 遍历ReportList
     */
    private void saveList(List<FinancialStatisticsReport> list, Date date) {
        for (FinancialStatisticsReport report : list) {
            //保存
            saveOne(report, date);
        }
    }

    /**
     * 保存财务统计表
     */
    private void saveOne(FinancialStatisticsReport report, Date date) {
        //根据订单code获取报名信息
        List<ApplyInfo> applyInfos = applyInfoService
            .getApplyInfoByOrderCode(report.getOrderCode());
        if (applyInfos == null || applyInfos.size() == 0)
            return;
        ApplyInfo applyInfo = applyInfos.get(0);
        if (report.getOrderCode().equals(applyInfo.getPayOrderCode())) {
            RegisterModel register = ucenterRegisterFacadeClient
                .getRegsiterByRegisterCode(applyInfo.getRegisterCode());
            if (register != null) {
                report.setNickName(register.getNickName());
            }
            report.setReportDate(date);
            report.setRegisterCode(applyInfo.getRegisterCode());
            report.setUnitCode(applyInfo.getUnitCode());
            report.setGameCode(applyInfo.getGameCode());
            report.setGameName(applyInfo.getGameName());
            report.setMatchCode(applyInfo.getMatchCode());
            report.setMatchName(applyInfo.getMatchName());
            if (IFinancialStatisticsReportService
                .selectReportCountByOrderCode(applyInfo.getPayOrderCode()) > 0)
                return;
            IFinancialStatisticsReportService.insert(report);
        }
    }

    /**
     * 隔天支付修改状态
     */
    private void updateList(List<FinancialStatisticsReport> list) {
        for (FinancialStatisticsReport report : list) {
            if (IFinancialStatisticsReportService
                .selectReportCountByOrderCode(report.getOrderCode()) == 0) {
                saveOne(report, new Date(System.currentTimeMillis()));
            }
            IFinancialStatisticsReportService.updateReportByOrderCode(report);
        }
    }
}
