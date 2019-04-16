/**
 * 
 */
package com.efida.sport.facade.model;

import java.io.Serializable;

/**
 * @author antony
 */
public class SmtPayOrderModel implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 日期
	 */
	private String            orderDate;
	/**
	 * 订单总数
	 */
	private String            ordersTotal;
	/**
	 * 订单总金额
	 */
	private String            moneyTotal;
	/**
	 * 支付订单总数
	 */
	private String              payOrdersTotal;
	/**
	 * 支付订单总金额
	 */
	private String              payMoneyTotal;

	/**
	 * 退单总金额
	 */
	private String  refundOrdersTotal = "0";

	/**
	 * 退单总金额
	 */
	private String  refundMoneyTotal = "0.00";

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrdersTotal() {
		return ordersTotal;
	}

	public void setOrdersTotal(String ordersTotal) {
		this.ordersTotal = ordersTotal;
	}

	public String getMoneyTotal() {
		return moneyTotal;
	}

	public void setMoneyTotal(String moneyTotal) {
		this.moneyTotal = moneyTotal;
	}

	public String getPayOrdersTotal() {
		return payOrdersTotal;
	}

	public void setPayOrdersTotal(String payOrdersTotal) {
		this.payOrdersTotal = payOrdersTotal;
	}

	public String getPayMoneyTotal() {
		return payMoneyTotal;
	}

	public void setPayMoneyTotal(String payMoneyTotal) {
		this.payMoneyTotal = payMoneyTotal;
	}

	public String getRefundOrdersTotal() {
		return refundOrdersTotal;
	}

	public void setRefundOrdersTotal(String refundOrdersTotal) {
		this.refundOrdersTotal = refundOrdersTotal;
	}

	public String getRefundMoneyTotal() {
		return refundMoneyTotal;
	}

	public void setRefundMoneyTotal(String refundMoneyTotal) {
		this.refundMoneyTotal = refundMoneyTotal;
	}
}
