package com.efida.sports.service.dubbo.facade;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.efida.sport.dmp.facade.exception.BusinessException;
import com.efida.sport.facade.model.GoodsModel;
import com.efida.sport.facade.model.GoodsOrderModel;
import com.efida.sport.facade.model.PagingResult;
import com.efida.sport.facade.service.ShopFacadeService;
import com.efida.sports.entity.Goods;
import com.efida.sports.entity.GoodsOrder;
import com.efida.sports.service.IGoodsOrderService;
import com.efida.sports.service.IGoodsService;
import com.efida.sports.service.dubbo.facade.cover.GoodsCover;
import com.efida.sports.service.dubbo.facade.cover.GoodsOrderCover;

/**
 * 电子奖章
 * 
 * @author yanglei
 * @version $Id: ShopFacadeServiceImpl.java, v 0.1 2018年10月12日 下午3:00:14 yanglei Exp $
 */
@Service
public class ShopFacadeServiceImpl implements ShopFacadeService {

    private static Logger      logger = LoggerFactory.getLogger(ShopFacadeServiceImpl.class);

    @Autowired
    private IGoodsService      goodsService;

    @Autowired
    private IGoodsOrderService orderService;

    @Override
    public void saveGoods(GoodsModel gm) {
        long id = gm.getId();
        Goods goods = this.goodsService.selectById(id);
        if (goods == null)
            return;
        goods.setDescription(gm.getDescription());
        goods.setExtraMoney(gm.getExtraMoney());
        goods.setGoodsPic(gm.getGoodsPic());
        goods.setProductPrice(gm.getProductPrice());
        goods.setTitle(gm.getTitle());
        this.goodsService.updateById(goods);
    }

    @Override
    public GoodsModel getOneGoodsById(long id) {
        Goods goods = this.goodsService.selectById(id);
        if (goods == null)
            throw new BusinessException("商品不存在");
        return GoodsCover.convertVO(goods);
    }

    @Override
    public PagingResult<GoodsModel> getGoodsListByPage(Map<String, Object> params) {
        int page = Integer.valueOf(params.get("page").toString());
        int limit = Integer.valueOf(params.get("limit").toString());
        Page<Goods> goods = new Page<Goods>(page, limit);
        List<Goods> goodsList = goodsService.selectGoods(goods, params);
        goods.setRecords(goodsList);
        PagingResult<GoodsModel> pagingResult = new PagingResult<GoodsModel>(
            GoodsCover.convertVOList(goods.getRecords()), goods.getTotal(), page, limit);
        return pagingResult;
    }

    @Override
    public PagingResult<GoodsOrderModel> getGoodsOrderListByPage(Map<String, Object> params) {
        int page = Integer.valueOf(params.get("page").toString());
        int limit = Integer.valueOf(params.get("limit").toString());
        Page<GoodsOrder> goods = new Page<GoodsOrder>(page, limit);
        List<GoodsOrder> goodsList = orderService.selectGoodsOrderList(goods, params);
        goods.setRecords(goodsList);
        PagingResult<GoodsOrderModel> pagingResult = new PagingResult<GoodsOrderModel>(
            GoodsOrderCover.convertVOList(goods.getRecords()), goods.getTotal(), page, limit);
        return pagingResult;

    }

    @Override
    public void sendGoodsOrder(int id) {
        GoodsOrder order = this.orderService.selectById(id);
        if (order == null)
            throw new BusinessException("订单不存在");
        String orderStatus = order.getOrderStatus();
        if ("wait_pay".equals(orderStatus)) {
            throw new BusinessException("订单未支付");
        }
        /**
         * 支付完成的订单才能进行发货操作
         */
        if ("success".equals(orderStatus)) {
            order.setOptTime(new Date());
            order.setOrderStatus("shipped");//已发货
            this.orderService.updateById(order);
        }
    }

    @Override
    public void goodsOrderAutoCompleted() {
        this.orderService.autoCompleted();
    }

}
