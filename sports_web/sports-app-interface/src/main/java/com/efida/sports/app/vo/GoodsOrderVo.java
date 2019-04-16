/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.vo;

import java.util.ArrayList;
import java.util.List;

import com.efida.sports.entity.GoodsOrder;
import com.efida.sports.util.DateUtil;

/**
 * 
 * @author yanglei
 */
public class GoodsOrderVo {

	private String orderCode;
	private String goodsCode;
	private String goodsTitle;
	private String goodsPic;
	private int goodsNum;
	private int goodsPrice;
	private int extraMoney;
	private int orderPrice;
	private String orderStatus;
	private String orderTime;
	private String payTime;
	private String realname;
	private String mobile;
	private String province;
	private String city;
	private String area;
	private String address;
	private String payOrderCode;
	private String payType;

	public String getPayOrderCode() {
		return payOrderCode;
	}

	public void setPayOrderCode(String payOrderCode) {
		this.payOrderCode = payOrderCode;
	}

	public int getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(int goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsTitle() {
		return goodsTitle;
	}

	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}

	public String getGoodsPic() {
		return goodsPic;
	}

	public void setGoodsPic(String goodsPic) {
		this.goodsPic = goodsPic;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public int getExtraMoney() {
		return extraMoney;
	}

	public void setExtraMoney(int extraMoney) {
		this.extraMoney = extraMoney;
	}

	public int getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(int orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	

	public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public static List<GoodsOrderVo> getVos(List<GoodsOrder> orderList) {
		List<GoodsOrderVo> vos = new ArrayList<GoodsOrderVo>();
		if (orderList == null || orderList.size() < 1) {
			return vos;
		}
		for (GoodsOrder order : orderList) {
			GoodsOrderVo vo = getVo(order);
			if (vo != null) {
				vos.add(vo);
			}
		}
		return vos;
	}

	public static GoodsOrderVo getVo(GoodsOrder order) {
		if (order == null) {
			return null;
		}
		GoodsOrderVo vo = new GoodsOrderVo();
		vo.setOrderCode(order.getOrderCode());
		vo.setGoodsCode(order.getGoodsCode());
		vo.setGoodsTitle(order.getGoodsTitle());
		vo.setGoodsPic(order.getGoodsPic());
		vo.setGoodsNum(order.getGoodsNum());
		vo.setGoodsPrice(order.getGoodsPrice());
		vo.setExtraMoney(order.getExtraMoney());
		vo.setOrderPrice(order.getOrderPrice());
		vo.setOrderStatus(order.getOrderStatus());
		vo.setOrderTime(DateUtil.format(order.getOrderTime()));
		vo.setPayTime(DateUtil.format(order.getPayTime()));
		vo.setRealname(order.getRealname());
		vo.setMobile(order.getMobile());
		vo.setProvince(order.getProvince());
		vo.setCity(order.getCity());
		vo.setArea(order.getArea());
		vo.setAddress(order.getAddress());
		vo.setPayOrderCode(order.getPayOrderCode());
		String payType = "";
        if ("weichat_pay".equals(order.getPayWayCode()))
            payType = "微信支付";
        else if ("alipay".equals(order.getPayWayCode()))
            payType = "支付宝支付";
        vo.setPayType(payType);
		return vo;
	}
}
