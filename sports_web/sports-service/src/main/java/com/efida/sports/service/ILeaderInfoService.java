package com.efida.sports.service;

import com.baomidou.mybatisplus.service.IService;
import com.efida.sports.entity.LeaderInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zoutao
 * @since 2018-07-24
 */
public interface ILeaderInfoService extends IService<LeaderInfo> {

    LeaderInfo getLearnInfoByApplyCode(String applyCode);

    void deleteByApplyCode(String applyCode);

}
