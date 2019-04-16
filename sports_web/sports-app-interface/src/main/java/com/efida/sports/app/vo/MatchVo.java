/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.vo;

import java.util.ArrayList;
import java.util.List;

import com.efida.sport.admin.facade.model.SpMatchModel;
import com.efida.sports.util.DateUtil;

/**
 * 
 * @author zoutao
 * @version $Id: MatchVo.java, v 0.1 2018年5月23日 下午7:00:09 zoutao Exp $
 */
public class MatchVo {

    /**
     * 项目编号
     */
    private String gameCode;

    /**
     * 赛事编号
     */
    private String matchCode;

    /**
     * 赛事名称
     */
    private String matchName;

    /**
     * 赛事海报
     */
    private String poster;

    /**
     * 报名截止时间
     */
    private String regTime;

    /**
     * 赛事开始时间
     */
    private String startTime;
    /**
     *赛事结束时间 
     * 
     */
    private String endTime;

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getMatchCode() {
        return matchCode;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public static List<MatchVo> getVos(List<SpMatchModel> models) {
        List<MatchVo> vos = new ArrayList<MatchVo>();

        if (models == null || models.size() < 1) {
            return vos;
        }

        for (SpMatchModel spMatchModel : models) {
            MatchVo vo = getVo(spMatchModel);
            if (vo != null) {
                vos.add(vo);
            }
        }
        return vos;
    }

    public static MatchVo getVo(SpMatchModel model) {
        if (model == null) {
            return null;
        }
        MatchVo vo = new MatchVo();
        vo.setGameCode(model.getGameCode());
        vo.setMatchCode(model.getMatchCode());
        vo.setMatchName(model.getMatchName());
        vo.setPoster(model.getPoster());
        vo.setRegTime(DateUtil.format(model.getRegTime()));
        vo.setStartTime(DateUtil.format(model.getStartTime()));
        vo.setEndTime(DateUtil.format(model.getEndTime()));
        return vo;
    }

}
