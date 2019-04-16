package com.efida.sports.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.efida.sports.entity.FinancialStatisticsReport;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IFinancialStatisticsReportService extends IService<FinancialStatisticsReport> {

    boolean updateReportByOrderCode(FinancialStatisticsReport entity);

    int selectReportCountByOrderCode(String orderCode);
    /*
        按时间查询订单
     */
    List<FinancialStatisticsReport> getPayOrderListByOrderOrPayTime(Date startTimeByOrder,Date endTimeByOrder,Date startTimeByPay,Date endTimeByPay,String orderStatus);

    /*
        财务统计分页查询
     */
    List<FinancialStatisticsReport> selectFinancialStatistics(Page<FinancialStatisticsReport> pageReport,
                                                              Map<String, Object> params);

    List<FinancialStatisticsReport> selectFinancialStatistics(Map<String, Object> params);
    /*
        财务统计总计
     */
    FinancialStatisticsReport selectFinancialStatisticsCount(Map<String, Object> params);

    /*
        财务统计详情
     */
    List<FinancialStatisticsReport> selectFinancialStatisticsDetail(Page<FinancialStatisticsReport> pageReport,
                                                                    Map<String, Object> params);
}
