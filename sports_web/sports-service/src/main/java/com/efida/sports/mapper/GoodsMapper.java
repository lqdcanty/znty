package com.efida.sports.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.efida.sports.entity.Goods;

public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 查询商品列表
     * 
     * @param goods
     * @param params
     * @return
     */
    List<Goods> selectGoods(Page<Goods> goods, Map<String, Object> params);
}