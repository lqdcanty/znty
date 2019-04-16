package com.efida.sport.facade.service;

import java.util.Map;

import com.efida.sport.facade.model.GoodsModel;
import com.efida.sport.facade.model.GoodsOrderModel;
import com.efida.sport.facade.model.PagingResult;

/**
 * 电子奖章
 * 
 * @author yanglei
 * @version $Id: ShopFacadeService.java, v 0.1 2018年10月12日 下午2:57:32 yanglei Exp $
 */
public interface ShopFacadeService {

    /**
     * 保存商品信息
     * 
     * @param goods
     */
    void saveGoods(GoodsModel goods);

    /**
     * 获取商品详情
     * 
     * @param id
     * @return
     */
    GoodsModel getOneGoodsById(long id);

    /**
     * 查询商品列表 分页查询
     * 
     * @param params
     * @return
     */
    PagingResult<GoodsModel> getGoodsListByPage(Map<String, Object> params);

    /**
     * 查询订单列表
     * 
     * @param params
     * @return
     */
    PagingResult<GoodsOrderModel> getGoodsOrderListByPage(Map<String, Object> params);

    /**
     * 发货
     * 
     * @param id
     */
    void sendGoodsOrder(int id);

    /**
     * 自动完成收货
     */
    void goodsOrderAutoCompleted();

}
