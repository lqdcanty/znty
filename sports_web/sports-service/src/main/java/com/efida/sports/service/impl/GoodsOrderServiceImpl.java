package com.efida.sports.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.efida.sports.config.WeichatConfig;
import com.efida.sports.constants.Constants;
import com.efida.sports.entity.Goods;
import com.efida.sports.entity.GoodsOrder;
import com.efida.sports.entity.PayOrder;
import com.efida.sports.entity.UserAddress;
import com.efida.sports.enums.OrderStatusEnum;
import com.efida.sports.enums.OrderTypeEnum;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.mapper.GoodsMapper;
import com.efida.sports.mapper.GoodsOrderEntityMapper;
import com.efida.sports.service.IGoodsOrderService;
import com.efida.sports.service.IGoodsService;
import com.efida.sports.service.IPayOrderService;
import com.efida.sports.service.IUserAddressService;
import com.efida.sports.util.SeqWorkerUtil;
import com.efida.sports.util.emoji.DateUtil;

@Service
public class GoodsOrderServiceImpl extends ServiceImpl<GoodsOrderEntityMapper, GoodsOrder>
                                   implements IGoodsOrderService {

    private static Logger          log = LoggerFactory.getLogger(GoodsOrderServiceImpl.class);

    @Autowired
    private IGoodsService          goodsService;

    @Autowired
    private IUserAddressService    addressService;

    @Autowired
    private WeichatConfig          weichatConfig;

    @Autowired
    private IPayOrderService       payOrderService;

    @Autowired
    private GoodsOrderEntityMapper goodsOrderMapper;

    @Override
    public Page<GoodsOrder> selectPageOrderByStatus(int currentPage, int pageSize,
                                                    String registerCode,
                                                    OrderStatusEnum orderStatus) {
        Page<GoodsOrder> page = new Page<GoodsOrder>(currentPage, pageSize, "order_time", false);
        Wrapper<GoodsOrder> wrapper = new EntityWrapper<GoodsOrder>();
        wrapper.eq("register_code", registerCode);
        if (orderStatus != null) {
            wrapper.eq("order_status", orderStatus.getCode());
        }
        wrapper.orderBy("order_time", false);
        Page<GoodsOrder> selectPage = selectPage(page, wrapper);
        List<GoodsOrder> records = selectPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return selectPage;
        }
        return selectPage;
    }

    @Override
    public GoodsOrder getOrderDetail(String orderCode) {
        return this.getOrderByCode(orderCode);
    }

    @Override
    public GoodsOrder getOrderByCode(String orderCode) {
        Wrapper<GoodsOrder> wrapper = new EntityWrapper<GoodsOrder>();
        wrapper.eq("order_code", orderCode);
        GoodsOrder address = selectOne(wrapper);
        return address;
    }

    @Transactional
    @Override
    public GoodsOrder addOrder(String goodsCode, int num, String addressCode, String registerCode,
                               String ip, String referer, String remark) {
        // 商品
        Goods goods = this.goodsService.getGoodsByCode(goodsCode);
        if (goods == null)
            throw new BusinessException("商品不存在");
        // 收件地址
        UserAddress userAddress = this.addressService.getAddressByCode(addressCode);
        if (userAddress == null)
            throw new BusinessException("收件地址不存在");
        // 创建商城订单
        GoodsOrder goodsOrder = this.createGoodsOrder(goods, userAddress, num, remark);
        goodsOrder.setRegisterCode(registerCode);
        // 创建支付订单
        PayOrder order = this.createPayOrder(ip, referer);
        order.setRegisterCode(registerCode);
        order.setOrderAmount(goodsOrder.getOrderPrice().longValue());
        order.setRemark(goods.getTitle());
        goodsOrder.setPayOrderCode(order.getOrderCode());
        // 保存数据
        this.insert(goodsOrder);
        payOrderService.insert(order);
        return goodsOrder;
    }

    private PayOrder createPayOrder(String ip, String referer) {
        final String payOrderCode = SeqWorkerUtil.generateTimeSequence();
        Date time = Calendar.getInstance().getTime();
        Date expireTime = getExpireTime();
        PayOrder order = new PayOrder();
        order.setOrderCode(payOrderCode);
        order.setExpireTime(expireTime);
        order.setGmtCreate(time);
        order.setGmtModified(expireTime);
        order.setNotifyUrl(weichatConfig.notifyUrl);
        order.setOrderType(OrderTypeEnum.mall.getCode());
        order.setOrderIp(ip);
        order.setOrderRefererUrl(referer);
        order.setOrderTime(time);
        order.setOrderPeriod(Constants.APPLY_TIME_OUT);
        order.setOrderStatus(OrderStatusEnum.WAIT_PAY.getCode());
        return order;
    }

    /**
     * 创建商品订单
     * 
     * @param goods
     * @param userAddress
     * @return
     */
    private GoodsOrder createGoodsOrder(Goods goods, UserAddress userAddress, int goodsNum,
                                        String remark) {
        final String orderCode = SeqWorkerUtil.generateTimeSequence();
        GoodsOrder order = new GoodsOrder();
        order.setGoodsTitle(goods.getTitle());
        order.setGoodsPic(goods.getGoodsPic());
        order.setOrderCode(orderCode);
        order.setGoodsCode(goods.getGoodsCode());
        order.setGoodsPrice(goods.getProductPrice());
        order.setGoodsNum(goodsNum);
        order.setExtraMoney(goods.getExtraMoney());
        int orderPrice = goods.getProductPrice() * goodsNum + goods.getExtraMoney();
        order.setOrderPrice(orderPrice);
        order.setOrderStatus(OrderStatusEnum.WAIT_PAY.getCode());
        order.setRealname(userAddress.getRealname());
        order.setMobile(userAddress.getMobile());
        order.setProvince(userAddress.getProvince());
        order.setCity(userAddress.getCity());
        order.setArea(userAddress.getArea());
        order.setAddress(userAddress.getAddress());
        order.setRemark(remark);
        Date now = new Date();
        order.setOrderTime(now);
        order.setGmtCreate(now);
        return order;
    }

    private Date getExpireTime() {
        int applyTimeOut = Constants.APPLY_TIME_OUT;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, applyTimeOut);
        return calendar.getTime();
    }

    @Override
    public void paySuccess(String payOrderCode, Date payTime, String payWayCode,
                           String payWayName) {
        this.baseMapper.updateOrderPayStatus(payOrderCode, payTime,
            OrderStatusEnum.SUCCESS.getCode(), payWayCode, payWayName);
    }

    @Override
    public List<GoodsOrder> selectGoodsOrderList(Page<GoodsOrder> goodsOrder,
                                                 Map<String, Object> params) {
        return goodsOrderMapper.selectGoodsOrderList(goodsOrder, params);
    }

    @Override
    public void autoCompleted() {
        Map<String, Object> params = new HashMap<>();
        //7天以前的时间
        String week = DateUtil.beforLongDate(System.currentTimeMillis(), -7,
            DateUtil.LONG_WEB_FORMAT);
        params.put("optTime", week);
        params.put("orderStatusPre", "shipped");//已发货
        params.put("orderStatus", "completed");//已完成
        this.goodsOrderMapper.updateOrderByOptTime(params);
    }
}
