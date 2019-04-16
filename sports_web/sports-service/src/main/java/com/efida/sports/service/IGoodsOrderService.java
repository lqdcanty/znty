package com.efida.sports.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.efida.sports.entity.GoodsOrder;
import com.efida.sports.enums.OrderStatusEnum;

public interface IGoodsOrderService extends IService<GoodsOrder> {

    Page<GoodsOrder> selectPageOrderByStatus(int currentPage, int pageSize, String registerCode,
                                             OrderStatusEnum orderStatus);

    GoodsOrder getOrderDetail(String orderCode);

    GoodsOrder getOrderByCode(String orderCode);

    GoodsOrder addOrder(String goodsCode, int num, String addressCode, String registerCode,
                        String ip, String referer, String remark);

    public void paySuccess(String payOrderCode, Date payTime, String payWayCode, String payWayName);

    List<GoodsOrder> selectGoodsOrderList(Page<GoodsOrder> goods, Map<String, Object> params);

    void autoCompleted();
}
