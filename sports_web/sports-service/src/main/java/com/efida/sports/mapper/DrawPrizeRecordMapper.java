package com.efida.sports.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.efida.sports.entity.DrawPrizeRecord;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zoutao
 * @since 2018-10-17
 */
public interface DrawPrizeRecordMapper extends BaseMapper<DrawPrizeRecord> {

    /**
     * 修改中间信息
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param drawPrizeRecord
     */
    int upOverDrawInfo(DrawPrizeRecord drawPrizeRecord);

}
