package com.efida.sport.facade.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 商品模型
 * @author yanglei
 * @version $Id: GoodsModel.java, v 0.1 2018年10月12日 下午2:38:58 yanglei Exp $
 */
public class GoodsModel implements Serializable {

    /**  */
    private static final long serialVersionUID = 2239614124801548703L;

    private long              id;
    private String            goodsCode;
    private String            title;
    private String            goodsPic;
    private String            description;
    private int               productPrice;
    private int               extraMoney;
    private Date              createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

}
