package com.efida.sports.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

public class Goods implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column goods.id
	 *
	 * @mbggenerated
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column goods.goods_code
	 *
	 * @mbggenerated
	 */
	private String goodsCode;

	private String goodsPic;

	private String matchCode;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column goods.title
	 *
	 * @mbggenerated
	 */
	private String title;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column goods.product_price
	 *
	 * @mbggenerated
	 */
	private Integer productPrice;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column goods.extra_money
	 *
	 * @mbggenerated
	 */
	private Integer extraMoney;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column goods.create_time
	 *
	 * @mbggenerated
	 */
	private Date createTime;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column goods.gmt_create
	 *
	 * @mbggenerated
	 */
	private Date gmtCreate;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column goods.gmt_modified
	 *
	 * @mbggenerated
	 */
	private Date gmtModified;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column goods.thumb
	 *
	 * @mbggenerated
	 */
	private String thumb;

	/**
	 *
	 * This field was generated by MyBatis Generator. This field corresponds to the
	 * database column goods.description
	 *
	 * @mbggenerated
	 */
	private String description;

	public String getGoodsPic() {
		return goodsPic;
	}

	public void setGoodsPic(String goodsPic) {
		this.goodsPic = goodsPic;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column goods.thumb
	 *
	 * @return the value of goods.thumb
	 *
	 * @mbggenerated
	 */
	public String getThumb() {
		return thumb;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column goods.thumb
	 *
	 * @param thumb
	 *            the value for goods.thumb
	 *
	 * @mbggenerated
	 */
	public void setThumb(String thumb) {
		this.thumb = thumb == null ? null : thumb.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column goods.description
	 *
	 * @return the value of goods.description
	 *
	 * @mbggenerated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column goods.description
	 *
	 * @param description
	 *            the value for goods.description
	 *
	 * @mbggenerated
	 */
	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column goods.id
	 *
	 * @return the value of goods.id
	 *
	 * @mbggenerated
	 */
	public Long getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column goods.id
	 *
	 * @param id
	 *            the value for goods.id
	 *
	 * @mbggenerated
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column goods.goods_code
	 *
	 * @return the value of goods.goods_code
	 *
	 * @mbggenerated
	 */
	public String getGoodsCode() {
		return goodsCode;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column goods.goods_code
	 *
	 * @param goodsCode
	 *            the value for goods.goods_code
	 *
	 * @mbggenerated
	 */
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode == null ? null : goodsCode.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column goods.title
	 *
	 * @return the value of goods.title
	 *
	 * @mbggenerated
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column goods.title
	 *
	 * @param title
	 *            the value for goods.title
	 *
	 * @mbggenerated
	 */
	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column goods.product_price
	 *
	 * @return the value of goods.product_price
	 *
	 * @mbggenerated
	 */
	public Integer getProductPrice() {
		return productPrice;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column goods.product_price
	 *
	 * @param productPrice
	 *            the value for goods.product_price
	 *
	 * @mbggenerated
	 */
	public void setProductPrice(Integer productPrice) {
		this.productPrice = productPrice;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column goods.extra_money
	 *
	 * @return the value of goods.extra_money
	 *
	 * @mbggenerated
	 */
	public Integer getExtraMoney() {
		return extraMoney;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column goods.extra_money
	 *
	 * @param extraMoney
	 *            the value for goods.extra_money
	 *
	 * @mbggenerated
	 */
	public void setExtraMoney(Integer extraMoney) {
		this.extraMoney = extraMoney;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column goods.create_time
	 *
	 * @return the value of goods.create_time
	 *
	 * @mbggenerated
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column goods.create_time
	 *
	 * @param createTime
	 *            the value for goods.create_time
	 *
	 * @mbggenerated
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column goods.gmt_create
	 *
	 * @return the value of goods.gmt_create
	 *
	 * @mbggenerated
	 */
	public Date getGmtCreate() {
		return gmtCreate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column goods.gmt_create
	 *
	 * @param gmtCreate
	 *            the value for goods.gmt_create
	 *
	 * @mbggenerated
	 */
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value
	 * of the database column goods.gmt_modified
	 *
	 * @return the value of goods.gmt_modified
	 *
	 * @mbggenerated
	 */
	public Date getGmtModified() {
		return gmtModified;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of
	 * the database column goods.gmt_modified
	 *
	 * @param gmtModified
	 *            the value for goods.gmt_modified
	 *
	 * @mbggenerated
	 */
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public String getMatchCode() {
		return matchCode;
	}

	public void setMatchCode(String matchCode) {
		this.matchCode = matchCode;
	}

}