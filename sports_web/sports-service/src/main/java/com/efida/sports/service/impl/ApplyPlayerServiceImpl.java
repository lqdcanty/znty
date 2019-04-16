package com.efida.sports.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.efida.sports.entity.ApplyPlayer;
import com.efida.sports.mapper.ApplyPlayerMapper;
import com.efida.sports.service.IApplyPlayerService;
import com.efida.sports.service.IPlayerService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zoutao
 * @since 2018-07-24
 */
@Service
public class ApplyPlayerServiceImpl extends ServiceImpl<ApplyPlayerMapper, ApplyPlayer>
                                    implements IApplyPlayerService {

    @Autowired
    private IPlayerService playerService;

    @Override
    public List<String> selectPlayerCodeByApplyCode(String applyCode) {
        ArrayList<String> list = new ArrayList<String>();
        Wrapper<ApplyPlayer> wrapper = new EntityWrapper<ApplyPlayer>();
        wrapper.eq("apply_code", applyCode);
        List<ApplyPlayer> selectList = selectList(wrapper);
        if (selectList != null && selectList.size() > 0) {
            for (ApplyPlayer applyPlayer : selectList) {
                list.add(applyPlayer.getPlayerCode());
            }
        }
        return list;
    }

    @Override
    public void createApplyPlayer(ApplyPlayer applyPlayer) {
        Wrapper<ApplyPlayer> wrapper = new EntityWrapper<ApplyPlayer>();
        wrapper.eq("apply_code", applyPlayer.getApplyCode());
        wrapper.eq("player_code", applyPlayer.getPlayerCode());
        List<ApplyPlayer> selectList = selectList(wrapper);
        if (selectList != null && selectList.size() > 0) {
            return;
        }
        insert(applyPlayer);
    }

    @Override
    public Integer getTotalTermNum(String applyCode) {
        Wrapper<ApplyPlayer> wrapper = new EntityWrapper<ApplyPlayer>();
        wrapper.eq("apply_code", applyCode);
        return selectCount(wrapper);
    }

    @Override
    public void deleteByPlayerCode(String playerCode) {
        Wrapper<ApplyPlayer> wrapper = new EntityWrapper<ApplyPlayer>();
        wrapper.eq("player_code", playerCode);
        delete(wrapper);
    }

    @Override
    public void deleteByApplyCode(String applyCode) {
        Wrapper<ApplyPlayer> wrapper = new EntityWrapper<ApplyPlayer>();
        wrapper.eq("apply_code", applyCode);
        delete(wrapper);
    }

}
