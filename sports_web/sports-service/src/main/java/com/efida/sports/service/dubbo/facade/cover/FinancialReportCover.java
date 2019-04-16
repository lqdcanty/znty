package com.efida.sports.service.dubbo.facade.cover;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.efida.sport.facade.model.SmtPayOrderDetailModel;
import com.efida.sport.facade.model.SmtPayOrderModel;
import com.efida.sports.entity.FinancialStatisticsReport;
import com.efida.sports.util.DateUtil;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FinancialReportCover {
    public static SmtPayOrderModel convertVO(FinancialStatisticsReport source){
        SmtPayOrderModel target = new SmtPayOrderModel();
        if(null != source){
            BeanUtils.copyProperties(source, target);
            target.setOrderDate(DateUtil.formatDay(source.getReportDate()));
        }
        return target;
    }

    public static List<SmtPayOrderModel> convertVOList(List<FinancialStatisticsReport> PayOrderList){
        List<SmtPayOrderModel> payOrderModelList = new ArrayList<SmtPayOrderModel>();
        if(CollectionUtils.isNotEmpty(PayOrderList))  {
            for(FinancialStatisticsReport entity : PayOrderList){
                payOrderModelList.add(convertVO(entity));
            }
        }
        return payOrderModelList;
    }

    public static SmtPayOrderDetailModel convertDetailVO(FinancialStatisticsReport source){
        SmtPayOrderDetailModel target = new SmtPayOrderDetailModel();
        if(null != source){
            BeanUtils.copyProperties(source, target);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(null!=source.getPayTime()){
            target.setPayTime(formatter.format(source.getPayTime()));
        }
        if(null!=source.getOrderTime()){
            target.setGmtCreate(formatter.format(source.getOrderTime()));
        }
        return target;
    }

    public static List<SmtPayOrderDetailModel> convertDetailVOList(List<FinancialStatisticsReport> PayOrderList){
        List<SmtPayOrderDetailModel> payOrderModelList = new ArrayList<SmtPayOrderDetailModel>();
        if(CollectionUtils.isNotEmpty(PayOrderList))  {
            for(FinancialStatisticsReport entity : PayOrderList){
                payOrderModelList.add(convertDetailVO(entity));
            }
        }
        return payOrderModelList;
    }
}
