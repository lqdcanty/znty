package com.efida.sports.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.efida.sports.entity.FinancialStatisticsReport;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FinancialStatisticsReportMapper extends BaseMapper<FinancialStatisticsReport> {

    List<FinancialStatisticsReport> selectPayOrderListByOrderOrPayTime(Map<String, Object> params);

    void updateReportByOrderCode(FinancialStatisticsReport entity);

    /***
     * 支付统计报表查询
     * @param params
     * @return
     */
    List<FinancialStatisticsReport> selectFinancialStatistics(Page<FinancialStatisticsReport> pageReport, Map<String, Object> params);
    /***
     * 支付统计报表查询 Excel
     * @param params
     * @return
     */
    List<FinancialStatisticsReport> selectFinancialStatistics( Map<String, Object> params);
    /***
     * 支付统计总数查询
     * @param params
     * @return
     */
    FinancialStatisticsReport selectFinancialStatisticsCount(Map<String, Object> params);
    /***
     * 支付明细报表查询
     * @param params
     * @return
     */
    List<FinancialStatisticsReport> selectFinancialStatisticsDetail(Page<FinancialStatisticsReport> pageReport, Map<String, Object> params);

}
