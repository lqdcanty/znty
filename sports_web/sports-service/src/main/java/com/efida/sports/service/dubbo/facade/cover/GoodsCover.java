package com.efida.sports.service.dubbo.facade.cover;

import java.util.ArrayList;
import java.util.List;

import com.efida.sport.facade.model.GoodsModel;
import com.efida.sports.entity.Goods;

/**
 * 
 * 商品模型转换
 * @author yanglei
 * @version $Id: GoodsCover.java, v 0.1 2018年10月12日 下午3:21:46 yanglei Exp $
 */
public class GoodsCover {

    public static List<GoodsModel> convertVOList(List<Goods> goodsList) {
        List<GoodsModel> vos = new ArrayList<GoodsModel>();
        if (goodsList == null || goodsList.size() < 1) {
            return vos;
        }
        for (Goods goods : goodsList) {
            GoodsModel vo = convertVO(goods);
            if (vo != null) {
                vos.add(vo);
            }
        }
        return vos;
    }

    public static GoodsModel convertVO(Goods goods) {
        if (goods == null) {
            return null;
        }
        GoodsModel vo = new GoodsModel();
        vo.setId(goods.getId());
        vo.setGoodsCode(goods.getGoodsCode());
        vo.setTitle(goods.getTitle());
        vo.setGoodsPic(goods.getGoodsPic());
        vo.setProductPrice(goods.getProductPrice());
        vo.setExtraMoney(goods.getExtraMoney());
        vo.setDescription(goods.getDescription());
        vo.setCreateTime(goods.getCreateTime());
        return vo;
    }
}
