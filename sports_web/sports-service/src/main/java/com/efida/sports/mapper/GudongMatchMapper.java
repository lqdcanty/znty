package com.efida.sports.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.efida.sports.entity.GudongMatch;

import java.util.List;

/**
 * Created by wangyan on 2018/9/11.
 */
public interface GudongMatchMapper extends BaseMapper<GudongMatch> {

    /**
     * 获取全部对应赛事信息
     * @return
     */
    List<GudongMatch> selectAllData();

}
