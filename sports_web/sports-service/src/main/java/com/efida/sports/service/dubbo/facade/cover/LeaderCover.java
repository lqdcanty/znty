/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.facade.cover;

import com.efida.sport.admin.facade.exception.BusinessException;
import com.efida.sport.facade.model.ApplyInfoModel;
import com.efida.sports.entity.LeaderInfo;
import com.efida.sports.util.SeqWorkerUtil;

/**
 * 
 * @author zoutao
 * @version $Id: LeaderCover.java, v 0.1 2018年7月31日 下午8:49:47 zoutao Exp $
 */
public class LeaderCover {

    public static LeaderInfo getLearnInfo(ApplyInfoModel model) {
        if (model == null) {
            throw new BusinessException("报名信息不能为空");
        }
        LeaderInfo info = new LeaderInfo();
        //        if (StringUtils.isBlank(model.getLeaderName())) {
        //            throw new BusinessException("领队名称不为为空");
        //        }
        //        if (StringUtils.isBlank(model.getLeaderPhone())) {
        //            throw new BusinessException("领队电话不为为空");
        //        }
        //        if (StringUtils.isBlank(model.getTeamName())) {
        //            throw new BusinessException("团队名称不能为空");
        //        }
        info.setLeaderCode(SeqWorkerUtil.generateTimeSequence());
        info.setLeaderName(model.getLeaderName());
        info.setLeaderPhone(model.getLeaderPhone());
        info.setTeamName(model.getTeamName());
        return info;
    }

}
