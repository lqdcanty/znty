/**
 * 
 */
package com.efida.sport.facade.service;


import com.efida.sport.facade.model.PagingResult;
import com.efida.sport.facade.model.SmtPayOrderDetailModel;
import com.efida.sport.facade.model.SmtPayOrderModel;

import java.util.List;
import java.util.Map;

/**
 * @author antony
 *  结算报表统计接口
 */
public interface PayOrderFacadeService {

	public PagingResult<SmtPayOrderModel> getSmtPayOrderModel(Map<String, Object> params);

	public List<SmtPayOrderModel> getAllSmtPayOrderModel(Map<String, Object> params);

	public SmtPayOrderModel getSettlementPayOrderCount(Map<String, Object> params);

	public PagingResult<SmtPayOrderDetailModel> getSmtPayOrderDetail(Map<String, Object> params);

}
