package com.efida.sports.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.efida.sports.entity.UserAddress;

public interface IUserAddressService extends IService<UserAddress> {

	Page<UserAddress> selectPageAddress(int currentPage, int pageSize, String registerCode);

	UserAddress getAddressDetail(String addressCode);

	UserAddress getAddressByCode(String addressCode);

	String saveOrUpdateAddress(UserAddress userAddress);

	UserAddress getDefaultAddress(String registerCode);

}
