package com.efida.sports.service.dubbo.facade;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.efida.sport.facade.model.PagingResult;
import com.efida.sport.facade.model.SmtPayOrderDetailModel;
import com.efida.sport.facade.model.SmtPayOrderModel;
import com.efida.sport.facade.service.FinancialReportFacadeService;
import com.efida.sports.entity.FinancialStatisticsReport;
import com.efida.sports.service.IFinancialStatisticsReportService;
import com.efida.sports.service.dubbo.facade.cover.FinancialReportCover;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
@Service
public class FinancialReportFacadeServiceImpl implements FinancialReportFacadeService {

    @Autowired
    private IFinancialStatisticsReportService reportService;

    @Override
    public PagingResult<SmtPayOrderModel> getFinancialReportListByPage(Map<String, Object> params) {
        int page = Integer.valueOf(params.get("page").toString());
        int limit = Integer.valueOf(params.get("limit").toString());
        Page<FinancialStatisticsReport> pagePayOrder = new Page<FinancialStatisticsReport>(page, limit);
        List<FinancialStatisticsReport> payOrderList = reportService.selectFinancialStatistics(pagePayOrder,params);
        pagePayOrder.setRecords(payOrderList);
        PagingResult<SmtPayOrderModel> pagingResult = new PagingResult<SmtPayOrderModel>(
                FinancialReportCover.convertVOList(pagePayOrder.getRecords()),
                pagePayOrder.getTotal(), page, limit);
        return pagingResult;
    }
    @Override
    public List<SmtPayOrderModel> getFinancialReportList(Map<String, Object> params) {

        List<FinancialStatisticsReport> payOrderList = reportService.selectFinancialStatistics(params);

        return FinancialReportCover.convertVOList(payOrderList);
    }
    @Override
    public SmtPayOrderModel getFinancialReportCount(Map<String, Object> params) {

        FinancialStatisticsReport payOrder = reportService.selectFinancialStatisticsCount(params);
        return FinancialReportCover.convertVO(payOrder);
    }

    @Override
    public PagingResult<SmtPayOrderDetailModel> getFinancialReportDetail(Map<String, Object> params) {
        int page = Integer.valueOf(params.get("page").toString());
        int limit = Integer.valueOf(params.get("limit").toString());
        Page<FinancialStatisticsReport> pagePayOrder = new Page<FinancialStatisticsReport>(page, limit);
        List<FinancialStatisticsReport> payOrderList = reportService.selectFinancialStatisticsDetail(pagePayOrder, params);
        pagePayOrder.setRecords(payOrderList);
        PagingResult<SmtPayOrderDetailModel> pagingResult = new PagingResult<SmtPayOrderDetailModel>(
                FinancialReportCover.convertDetailVOList(pagePayOrder.getRecords()),
                pagePayOrder.getTotal(), page, limit);
        return pagingResult;
    }
}
