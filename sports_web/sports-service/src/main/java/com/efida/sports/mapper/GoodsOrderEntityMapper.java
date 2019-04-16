package com.efida.sports.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.efida.sports.entity.GoodsOrder;

public interface GoodsOrderEntityMapper extends BaseMapper<GoodsOrder> {

    void updateOrderPayStatus(@Param("payOrderCode") String payOrderCode,
                              @Param("payTime") Date payTime,
                              @Param("orderStatus") String orderStatus,
                              @Param("payWayCode") String payWayCode,
                              @Param("payWayName") String payWayName);

    List<GoodsOrder> selectGoodsOrderList(Page<GoodsOrder> goodsOrder, Map<String, Object> params);

    void updateOrderByOptTime(Map<String, Object> params);

    /**
     * 奖章统计
     *
     * @param startTime
     * @param endTime
     * @param isRepeat
     * @return
     */
    List<Map<String, Object>> countMedalReceive(@Param("startTime") Date startTime,
                                                @Param("endTime") Date endTime,
                                                @Param("isRepeat") String isRepeat);

}