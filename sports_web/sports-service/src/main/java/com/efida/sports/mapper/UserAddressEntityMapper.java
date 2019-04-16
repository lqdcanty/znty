package com.efida.sports.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.efida.sports.entity.UserAddress;

/**
 * 
 * @author yanglei
 */
public interface UserAddressEntityMapper extends BaseMapper<UserAddress> {

	void updateAllStatusByUser(@Param("registerCode") String registerCode, @Param("isDefault") int isDefault);

}