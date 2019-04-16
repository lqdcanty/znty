package com.efida.sports.service;

import com.baomidou.mybatisplus.service.IService;
import com.efida.sports.entity.GudongMatch;

import java.util.List;
import java.util.Map;

/**
 * Created by wangyan on 2018/9/11.
 */
public interface GudongMatchService extends IService<GudongMatch> {

    /**
     * 获取全部对应赛事信息
     * @return
     */
    List<GudongMatch> getAllData();

    /**
     * 获取全部对应赛事信息
     * @return
     */
    Map<String,String> getAllDataMap();

}
