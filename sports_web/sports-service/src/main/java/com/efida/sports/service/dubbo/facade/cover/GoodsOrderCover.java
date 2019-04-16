package com.efida.sports.service.dubbo.facade.cover;

import java.util.ArrayList;
import java.util.List;

import com.efida.sport.facade.model.GoodsOrderModel;
import com.efida.sports.entity.GoodsOrder;

/**
 * 
 * 
 * @author yanglei
 * @version $Id: GoodsOrderCover.java, v 0.1 2018年10月12日 下午6:47:50 yanglei Exp $
 */
public class GoodsOrderCover {

    public static List<GoodsOrderModel> convertVOList(List<GoodsOrder> orderList) {
        List<GoodsOrderModel> vos = new ArrayList<GoodsOrderModel>();
        if (orderList == null || orderList.size() < 1) {
            return vos;
        }
        for (GoodsOrder order : orderList) {
            GoodsOrderModel vo = convertVO(order);
            if (vo != null) {
                vos.add(vo);
            }
        }
        return vos;
    }

    public static GoodsOrderModel convertVO(GoodsOrder order) {
        if (order == null) {
            return null;
        }
        GoodsOrderModel vo = new GoodsOrderModel();
        vo.setOrderCode(order.getOrderCode());
        vo.setRealname(order.getRealname());
        vo.setMobile(order.getMobile());
        vo.setProvince(order.getProvince());
        vo.setCity(order.getCity());
        vo.setArea(order.getArea());
        vo.setAddress(order.getAddress());
        vo.setId(order.getId());
        vo.setOrderTime(order.getOrderTime());
        vo.setOptTime(order.getOptTime());
        vo.setOrderStatus(order.getOrderStatus());
        vo.setRemark(order.getRemark());
        vo.setLoginPhone(order.getLoginPhone());
        vo.setGoodsNum(order.getGoodsNum());
        return vo;
    }
}
