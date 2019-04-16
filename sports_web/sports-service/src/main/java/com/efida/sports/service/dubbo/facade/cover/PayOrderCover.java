package com.efida.sports.service.dubbo.facade.cover;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.efida.sport.facade.model.SmtPayOrderDetailModel;
import com.efida.sport.facade.model.SmtPayOrderModel;
import com.efida.sports.entity.PayOrder;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 支付统计报表Bean转换
 * Created by antony on 2018/7/21.
 */
public class PayOrderCover {

    public static SmtPayOrderModel convertVO(PayOrder source){
        SmtPayOrderModel target = new SmtPayOrderModel();
        if(null != source){
            BeanUtils.copyProperties(source, target);
        }
        return target;
    }

    public static List<SmtPayOrderModel> convertVOList(List<PayOrder> PayOrderList){
        List<SmtPayOrderModel> payOrderModelList = new ArrayList<SmtPayOrderModel>();
        if(CollectionUtils.isNotEmpty(PayOrderList))  {
            for(PayOrder entity : PayOrderList){
                payOrderModelList.add(convertVO(entity));
            }
        }
        return payOrderModelList;
    }

    public static SmtPayOrderDetailModel convertDetailVO(PayOrder source){
        SmtPayOrderDetailModel target = new SmtPayOrderDetailModel();
        if(null != source){
            BeanUtils.copyProperties(source, target);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(null!=source.getPayTime()){
            target.setPayTime(formatter.format(source.getPayTime()));
        }
        if(null!=source.getGmtCreate()){
            target.setGmtCreate(formatter.format(source.getGmtCreate()));
        }
        return target;
    }

    public static List<SmtPayOrderDetailModel> convertDetailVOList(List<PayOrder> PayOrderList){
        List<SmtPayOrderDetailModel> payOrderModelList = new ArrayList<SmtPayOrderDetailModel>();
        if(CollectionUtils.isNotEmpty(PayOrderList))  {
            for(PayOrder entity : PayOrderList){
                payOrderModelList.add(convertDetailVO(entity));
            }
        }
        return payOrderModelList;
    }
}
