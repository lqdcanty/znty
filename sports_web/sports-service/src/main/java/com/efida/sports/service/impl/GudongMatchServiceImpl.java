package com.efida.sports.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.efida.sports.constants.Constants;
import com.efida.sports.entity.GudongMatch;
import com.efida.sports.mapper.GudongMatchMapper;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.GudongMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyan on 2018/9/11.
 */
@Service
public class GudongMatchServiceImpl  extends ServiceImpl<GudongMatchMapper, GudongMatch> implements GudongMatchService {

    @Autowired
    private GudongMatchMapper gudongMatchMapper;

    @Autowired
    private CacheService cacheService;


    @Override
    public List<GudongMatch> getAllData(){
        return  gudongMatchMapper.selectAllData();
    }

    @Override
    public Map<String,String> getAllDataMap(){
        Map<String,String> map = new HashMap<String,String>();
        if( cacheService.getObj(Constants.GUDONG_MATCH_CODE) == null){
            List<GudongMatch> list = getAllData();
            for (GudongMatch  gudongMatch: list){
                map.put(gudongMatch.getCode(),gudongMatch.getGudongCode());
            }
            if( map!=null && map.size() > 0){
                cacheService.putObj("GUDONG_MATCH_CODE",map,1000 * 60 * 60 * 24 );
            }
        }else{
            map = (Map<String, String>) cacheService.getObj(Constants.GUDONG_MATCH_CODE);
        }
        return  map;
    }

}
