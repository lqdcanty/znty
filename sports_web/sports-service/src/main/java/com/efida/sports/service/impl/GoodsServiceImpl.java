package com.efida.sports.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.efida.sports.entity.Goods;
import com.efida.sports.mapper.GoodsMapper;
import com.efida.sports.service.IGoodsService;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    private static Logger log = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Autowired
    private GoodsMapper   goodsMapper;

    @Override
    public Goods getGoodsByCode(String goodsCode) {
        Wrapper<Goods> wrapper = new EntityWrapper<Goods>();
        wrapper.eq("goods_code", goodsCode);
        Goods goods = selectOne(wrapper);
        return goods;
    }

    @Override
    public Goods getGoodsByMatchCode(String matchCode) {
        Wrapper<Goods> wrapper = new EntityWrapper<Goods>();
        wrapper.eq("match_code", matchCode);
        Goods goods = selectOne(wrapper);
        return goods;
    }

    @Override
    public List<Goods> selectGoods(Page<Goods> goods, Map<String, Object> params) {
        return goodsMapper.selectGoods(goods, params);
    }

}
