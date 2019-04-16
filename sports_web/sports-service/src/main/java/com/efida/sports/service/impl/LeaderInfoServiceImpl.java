package com.efida.sports.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.efida.sports.entity.LeaderInfo;
import com.efida.sports.mapper.LeaderInfoMapper;
import com.efida.sports.service.ILeaderInfoService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zoutao
 * @since 2018-07-24
 */
@Service
public class LeaderInfoServiceImpl extends ServiceImpl<LeaderInfoMapper, LeaderInfo>
                                   implements ILeaderInfoService {

    @Override
    public LeaderInfo getLearnInfoByApplyCode(String applyCode) {
        Wrapper<LeaderInfo> wrapper = new EntityWrapper<LeaderInfo>();
        wrapper.eq("apply_info_code", applyCode);
        return selectOne(wrapper);
    }

    @Override
    public void deleteByApplyCode(String applyCode) {
        Wrapper<LeaderInfo> wrapper = new EntityWrapper<LeaderInfo>();
        wrapper.eq("apply_info_code", applyCode);
        delete(wrapper);
    }

}
