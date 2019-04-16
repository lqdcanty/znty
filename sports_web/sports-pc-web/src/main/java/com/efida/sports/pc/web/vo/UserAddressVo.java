/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.pc.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.efida.sports.entity.UserAddress;

/**
 * 
 * @author yanglei
 */
public class UserAddressVo {

	/**
	 * 地址编号
	 */
	private String addressCode;
	/**
	 * 收件人
	 */
	private String realname;
	/**
	 * 联系方式
	 */
	private String mobile;
	/**
	 * 省份
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 地区
	 */
	private String area;
	/**
	 * 详细地址
	 */
	private String address;
	/**
	 * 是否默认
	 */
	private boolean isdefault;

	public String getAddressCode() {
		return addressCode;
	}

	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isIsdefault() {
		return isdefault;
	}

	public void setIsdefault(boolean isdefault) {
		this.isdefault = isdefault;
	}

	public static List<UserAddressVo> getVos(List<UserAddress> addressList) {
		List<UserAddressVo> vos = new ArrayList<UserAddressVo>();
		if (addressList == null || addressList.size() < 1) {
			return vos;
		}
		for (UserAddress address : addressList) {
			UserAddressVo vo = getVo(address);
			if (vo != null) {
				vos.add(vo);
			}
		}
		return vos;
	}

	public static UserAddressVo getVo(UserAddress address) {
		if (address == null) {
			return null;
		}
		UserAddressVo vo = new UserAddressVo();
		vo.setAddressCode(address.getAddressCode());
		vo.setRealname(address.getRealname());
		vo.setMobile(address.getMobile());
		vo.setProvince(address.getProvince());
		vo.setCity(address.getCity());
		vo.setArea(address.getArea());
		vo.setAddress(address.getAddress());
		vo.setIsdefault(address.getIsdefault());
		return vo;
	}
}
