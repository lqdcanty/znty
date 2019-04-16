package com.efida.sport.facade.service;

import com.efida.sport.facade.model.PagingResult;
import com.efida.sport.facade.model.SmtPayOrderDetailModel;
import com.efida.sport.facade.model.SmtPayOrderModel;

import java.util.List;
import java.util.Map;

public interface FinancialReportFacadeService {

    PagingResult<SmtPayOrderModel> getFinancialReportListByPage(Map<String, Object> params);

    List<SmtPayOrderModel> getFinancialReportList(Map<String, Object> params);

    SmtPayOrderModel getFinancialReportCount(Map<String, Object> params);

    PagingResult<SmtPayOrderDetailModel> getFinancialReportDetail(Map<String, Object> params);
}
