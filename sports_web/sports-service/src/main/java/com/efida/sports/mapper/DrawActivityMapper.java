package com.efida.sports.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.efida.sports.entity.DrawActivity;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zoutao
 * @since 2018-10-17
 */
public interface DrawActivityMapper extends BaseMapper<DrawActivity> {

    void lockActivityByCode(@Param(value = "activeCode") String activeCode);

}
