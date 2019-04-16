package com.efida.sports.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.efida.sports.entity.DrawPrize;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zoutao
 * @since 2018-10-17
 */
public interface DrawPrizeMapper extends BaseMapper<DrawPrize> {

    /**
     * 锁定奖品
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param id
     */
    void lockPrize(Integer id);

    /**
     * 减少奖品数量
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param id
     */
    void upPrizeRemainSize(Integer id);

}
