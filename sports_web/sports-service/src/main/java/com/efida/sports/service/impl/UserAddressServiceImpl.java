package com.efida.sports.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.efida.sports.entity.UserAddress;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.mapper.UserAddressEntityMapper;
import com.efida.sports.service.IUserAddressService;
import com.efida.sports.util.SeqWorkerUtil;

@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressEntityMapper, UserAddress>
		implements IUserAddressService {

	private static Logger log = LoggerFactory.getLogger(UserAddressServiceImpl.class);

	@Autowired
	private UserAddressEntityMapper userAddressMapper;

	/**
	 * 查询地址列表数据
	 */
	@Override
	public Page<UserAddress> selectPageAddress(int currentPage, int pageSize, String registerCode) {
		Page<UserAddress> page = new Page<UserAddress>(currentPage, pageSize, "create_time", false);
		Wrapper<UserAddress> wrapper = new EntityWrapper<UserAddress>();
		wrapper.eq("register_code", registerCode);
		Page<UserAddress> selectPage = selectPage(page, wrapper);
		List<UserAddress> records = selectPage.getRecords();
		if (CollectionUtils.isEmpty(records)) {
			return selectPage;
		}
		return selectPage;
	}

	@Override
	public UserAddress getAddressDetail(String addressCode) {
		return this.getAddressByCode(addressCode);
	}

	@Override
	public UserAddress getAddressByCode(String addressCode) {
		Wrapper<UserAddress> wrapper = new EntityWrapper<UserAddress>();
		wrapper.eq("address_code", addressCode);
		UserAddress address = selectOne(wrapper);
		return address;
	}

	@Transactional
	@Override
	public String saveOrUpdateAddress(UserAddress userAddress) {
		this.checkAddressNull(userAddress);
		String addressCode = userAddress.getAddressCode();
		boolean isDefault = userAddress.getIsdefault();
		if (isDefault) {
			// 如果设置默认 则修改其他地址状态
			this.updateAllStatusByUser(userAddress.getRegisterCode(), 0);
		}
		Date now = new Date();
		if (StringUtils.isEmpty(addressCode)) {
			// 新增
			userAddress.setAddressCode(SeqWorkerUtil.generateTimeSequence());
			userAddress.setCreateTime(now);
			userAddress.setGmtCreate(now);
			this.insert(userAddress);
		} else {
			// 保存
			UserAddress address = this.getAddressByCode(addressCode);
			if (address == null)
				throw new BusinessException("收件地址不存在");
			address.setRealname(userAddress.getRealname());
			address.setMobile(userAddress.getMobile());
			address.setProvince(userAddress.getProvince());
			address.setCity(userAddress.getCity());
			address.setArea(userAddress.getArea());
			address.setAddress(userAddress.getAddress());
			address.setIsdefault(userAddress.getIsdefault());
			address.setGmtModified(now);
			this.updateAllColumnById(address);
		}
		return userAddress.getAddressCode();
	}

	private void updateAllStatusByUser(String registerCode, int isDefault) {
		this.userAddressMapper.updateAllStatusByUser(registerCode, isDefault);
	}

	/**
	 * 非空校验
	 * 
	 * @param userAddress
	 */
	private void checkAddressNull(UserAddress userAddress) {
		String realname = userAddress.getRealname();
		if (StringUtils.isEmpty(realname)) {
			throw new BusinessException("收件人姓名不能为空");
		}
		String mobile = userAddress.getMobile();
		if (StringUtils.isEmpty(mobile)) {
			throw new BusinessException("联系方式不能为空");
		}
		String province = userAddress.getProvince();
		if (StringUtils.isEmpty(province)) {
			throw new BusinessException("省份不能为空");
		}
		String city = userAddress.getCity();
		if (StringUtils.isEmpty(city)) {
			throw new BusinessException("城市不能为空");
		}
		String area = userAddress.getArea();
		if (StringUtils.isEmpty(area)) {
			throw new BusinessException("城区不能为空");
		}
		String address = userAddress.getAddress();
		if (StringUtils.isEmpty(address)) {
			throw new BusinessException("详细地址不能为空");
		}
	}

	@Override
	public UserAddress getDefaultAddress(String registerCode) {
		Wrapper<UserAddress> wrapper = new EntityWrapper<UserAddress>();
		wrapper.eq("register_code", registerCode);
		wrapper.eq("isdefault", 1);
		UserAddress address = selectOne(wrapper);
		return address;
	}
}
