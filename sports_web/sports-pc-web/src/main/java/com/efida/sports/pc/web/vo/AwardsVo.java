/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.pc.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.efida.sport.admin.facade.enums.AwardsEums;
import com.efida.sport.admin.facade.model.SpMatchRewardModel;

/**
 * 
 * @author zoutao
 * @version $Id: AwardsVo.java, v 0.1 2018年5月25日 下午4:07:15 zoutao Exp $
 */
public class AwardsVo {

    /**
     * 奖项名称
     */
    private String awardName;

    /**
     * 奖品图片
     */
    private String prizeImg;

    /**
     *奖品名称 
     */
    private String prizeName;

    /**
     * 奖品类型
     * @see AwardsEums
     */
    private String awardType;

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public String getPrizeImg() {
        return prizeImg;
    }

    public void setPrizeImg(String prizeImg) {
        this.prizeImg = prizeImg;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getAwardType() {
        return awardType;
    }

    public void setAwardType(String awardType) {
        this.awardType = awardType;
    }

    public static List<AwardsVo> getVos(List<SpMatchRewardModel> models) {
        List<AwardsVo> vos = new ArrayList<AwardsVo>();
        if (models == null || models.size() < 1) {
            return vos;
        }
        for (SpMatchRewardModel model : models) {
            AwardsVo vo = getVo(model);
            if (vo != null) {
                vos.add(vo);
            }

        }
        return vos;

    }

    private static AwardsVo getVo(SpMatchRewardModel model) {
        if (model == null) {
            return null;
        }
        AwardsVo vo = new AwardsVo();
        vo.setAwardName(model.getAwardName());
        vo.setPrizeImg(model.getPrizeImg());
        vo.setPrizeName(model.getPrizeName());
        vo.setAwardType(model.getAwardType());

        return vo;
    }

}
