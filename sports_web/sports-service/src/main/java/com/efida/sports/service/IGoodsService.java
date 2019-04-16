package com.efida.sports.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.efida.sports.entity.Goods;

public interface IGoodsService extends IService<Goods> {

    Goods getGoodsByCode(String goodsCode);

    Goods getGoodsByMatchCode(String matchCode);

    List<Goods> selectGoods(Page<Goods> goods, Map<String, Object> params);

}
