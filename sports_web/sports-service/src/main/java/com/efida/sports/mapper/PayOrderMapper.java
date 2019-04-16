package com.efida.sports.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.efida.sports.entity.PayOrder;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zoutao
 * @since 2018-05-18
 */
public interface PayOrderMapper extends BaseMapper<PayOrder> {

	void batchUpdateOrderCreater(@Param("oldRegisterCode") String oldRegiterCode,@Param("newRegisterCode") String newRegisterCode);

	/***
	 * 支付统计报表查询
	 * @param params
	 * @return
	 */
	List<PayOrder> selectSettlementPayOrderList(Page<PayOrder> pagePayOrder, Map<String, Object> params);

	/***
	 * 支付统计报表查询--all
	 * @param params
	 * @return
	 */
	List<PayOrder> selectSettlementPayOrderList(Map<String, Object> params);

	/***
	 * 支付统计总数查询
	 * @param params
	 * @return
	 */
	PayOrder selectSettlementPayOrderCount(Map<String, Object> params);

	/***
	 * 支付明细报表查询
	 * @param params
	 * @return
	 */
	List<PayOrder> selectSettlementPayOrderDetail(Page<PayOrder> pagePayOrder, Map<String, Object> params);

}
