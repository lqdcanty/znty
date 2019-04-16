/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.efida.sports.entity.Goods;

/**
 * 
 * @author yanglei
 */
public class GoodsVo {

	private String goodsCode;
	private String title;
	private String goodsPic;
	private String thumb;
	private String description;
	private int productPrice;
	private int extraMoney;

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGoodsPic() {
		return goodsPic;
	}

	public void setGoodsPic(String goodsPic) {
		this.goodsPic = goodsPic;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	public int getExtraMoney() {
		return extraMoney;
	}

	public void setExtraMoney(int extraMoney) {
		this.extraMoney = extraMoney;
	}

	public static List<GoodsVo> getVos(List<Goods> goodsList) {
		List<GoodsVo> vos = new ArrayList<GoodsVo>();
		if (goodsList == null || goodsList.size() < 1) {
			return vos;
		}
		for (Goods goods : goodsList) {
			GoodsVo vo = getVo(goods);
			if (vo != null) {
				vos.add(vo);
			}
		}
		return vos;
	}

	public static GoodsVo getVo(Goods goods) {
		if (goods == null) {
			return null;
		}
		GoodsVo vo = new GoodsVo();
		vo.setGoodsCode(goods.getGoodsCode());
		vo.setTitle(goods.getTitle());
		vo.setGoodsPic(goods.getGoodsPic());
		vo.setProductPrice(goods.getProductPrice());
		vo.setExtraMoney(goods.getExtraMoney());
		vo.setThumb(goods.getThumb());
		vo.setDescription(goods.getDescription());
		return vo;
	}
}
