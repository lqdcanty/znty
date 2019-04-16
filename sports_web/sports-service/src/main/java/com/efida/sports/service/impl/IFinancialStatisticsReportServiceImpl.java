package com.efida.sports.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.efida.sports.entity.FinancialStatisticsReport;
import com.efida.sports.mapper.FinancialStatisticsReportMapper;
import com.efida.sports.mapper.PayOrderMapper;
import com.efida.sports.service.IFinancialStatisticsReportService;
import com.efida.sports.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IFinancialStatisticsReportServiceImpl extends
        ServiceImpl<FinancialStatisticsReportMapper, FinancialStatisticsReport>
        implements IFinancialStatisticsReportService {
    private static Logger log = LoggerFactory.getLogger(IFinancialStatisticsReportServiceImpl.class);

    @Autowired
    private FinancialStatisticsReportMapper reportMapper;

    @Override
    public boolean updateReportByOrderCode(FinancialStatisticsReport entity) {
        if(StringUtils.isBlank(entity.getOrderCode())){
            log.info("updateReportByOrderCode：orderCode为空，修改报表订单支付相关状态失败");
            return false;
        }
        reportMapper.updateReportByOrderCode(entity);
        return true;
    }
    @Override
    public int selectReportCountByOrderCode(String orderCode){
        Wrapper<FinancialStatisticsReport> wrapper = new EntityWrapper<FinancialStatisticsReport>();
        wrapper.eq("order_code", orderCode);
        return selectCount(wrapper);
    }
    @Override
   public List<FinancialStatisticsReport> getPayOrderListByOrderOrPayTime(Date startTimeByOrder,Date endTimeByOrder,Date startTimeByPay,Date endTimeByPay,String orderStatus){
        Map<String, Object> params = new HashMap<>();
        params.put("startTimeByOrder",startTimeByOrder);
        params.put("endTimeByOrder",endTimeByOrder);
        params.put("startTimeByPay",startTimeByPay);
        params.put("endTimeByPay",endTimeByPay);
        params.put("orderStatus",orderStatus);
        return reportMapper.selectPayOrderListByOrderOrPayTime(params);
    }

    @Override
    public List<FinancialStatisticsReport> selectFinancialStatistics(Page<FinancialStatisticsReport> pageReport,
                                                              Map<String, Object> params){
       return reportMapper.selectFinancialStatistics(pageReport,params);
    }

    @Override
    public List<FinancialStatisticsReport> selectFinancialStatistics(Map<String, Object> params){

        return reportMapper.selectFinancialStatistics(params);
    }

    @Override
    public FinancialStatisticsReport selectFinancialStatisticsCount(Map<String, Object> params){
        return reportMapper.selectFinancialStatisticsCount(params);
    }

    @Override
    public List<FinancialStatisticsReport> selectFinancialStatisticsDetail(Page<FinancialStatisticsReport> pageReport,
                                                                           Map<String, Object> params){
        return reportMapper.selectFinancialStatisticsDetail(pageReport,params);
    }
}
