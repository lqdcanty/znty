package com.efida.sports.service.dubbo.facade;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.efida.sport.facade.model.PagingResult;
import com.efida.sport.facade.model.SmtPayOrderDetailModel;
import com.efida.sport.facade.model.SmtPayOrderModel;
import com.efida.sport.facade.service.PayOrderFacadeService;
import com.efida.sports.entity.PayOrder;
import com.efida.sports.service.IPayOrderService;
import com.efida.sports.service.dubbo.facade.cover.PayOrderCover;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 支付统计报表查询
 * @author antony
 */
@Service
public class PayOrderFacadeServiceImpl implements PayOrderFacadeService {
	
	@Autowired
	private IPayOrderService payOrderService;

	@Override
	public PagingResult<SmtPayOrderModel> getSmtPayOrderModel(Map<String, Object> params) {
		int page = Integer.valueOf(params.get("page").toString());
		int limit = Integer.valueOf(params.get("limit").toString());
		Page<PayOrder> pagePayOrder = new Page<PayOrder>(page, limit);
		List<PayOrder> payOrderList = payOrderService.selectSettlementPayOrderList(pagePayOrder, params);
		pagePayOrder.setRecords(payOrderList);
		PagingResult<SmtPayOrderModel> pagingResult = new PagingResult<SmtPayOrderModel>(
				PayOrderCover.convertVOList(pagePayOrder.getRecords()),
				pagePayOrder.getTotal(), page, limit);
		return pagingResult;
	}
	@Override
	public List<SmtPayOrderModel> getAllSmtPayOrderModel(Map<String, Object> params) {

		List<PayOrder> payOrderList = payOrderService.selectSettlementPayOrderList(params);

		return PayOrderCover.convertVOList(payOrderList);
	}
	@Override
	public SmtPayOrderModel getSettlementPayOrderCount(Map<String, Object> params) {

		PayOrder payOrder = payOrderService.selectSettlementPayOrderCount(params);
		return PayOrderCover.convertVO(payOrder);
	}

	@Override
	public PagingResult<SmtPayOrderDetailModel> getSmtPayOrderDetail(Map<String, Object> params) {
		int page = Integer.valueOf(params.get("page").toString());
		int limit = Integer.valueOf(params.get("limit").toString());
		Page<PayOrder> pagePayOrder = new Page<PayOrder>(page, limit);
		List<PayOrder> payOrderList = payOrderService.selectSettlementPayOrderDetail(pagePayOrder, params);
		pagePayOrder.setRecords(payOrderList);
		PagingResult<SmtPayOrderDetailModel> pagingResult = new PagingResult<SmtPayOrderDetailModel>(
				PayOrderCover.convertDetailVOList(pagePayOrder.getRecords()),
				pagePayOrder.getTotal(), page, limit);
		return pagingResult;
	}
}
