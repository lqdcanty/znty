/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.pc.web.vo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.efida.sport.admin.facade.model.SpMatchModel;

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
    private Date   regTime;

    /**
     * 赛事开始时间
     */
    private Date   startTime;
    /**
     *赛事结束时间 
     * 
     */
    private Date   endTime;

    /**
     * 赛事介绍
     */
    private String introduce;

    /**
     * 賽事狀態
     */
    private String regStatus;

    /**
     * 报名截止时间
     */
    private String regTimeStr;

    /**
     * 赛事开始时间
     */
    private String startTimeStr;
    /**
     *赛事结束时间 
     * 
     */
    private String endTimeStr;

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

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getRegTimeStr() {
        return regTimeStr;
    }

    public void setRegTimeStr(String regTimeStr) {
        this.regTimeStr = regTimeStr;
    }

    public String getRegStatus() {
        return regStatus;
    }

    public void setRegStatus(String regStatus) {
        this.regStatus = regStatus;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
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
        vo.setRegTime(model.getRegTime());
        vo.setStartTime(model.getStartTime());
        vo.setEndTime(model.getEndTime());
        vo.setIntroduce(model.getIntroduce());
        Date regTimes = model.getRegTime();
        vo.setRegTimeStr(new SimpleDateFormat("yyyy-MM-dd").format(regTimes));
        vo.setStartTimeStr(new SimpleDateFormat("yyyy-MM-dd").format(model.getStartTime()));
        vo.setEndTimeStr(new SimpleDateFormat("yyyy-MM-dd").format(model.getEndTime()));
        vo.setRegStatus(model.getRegStatus());
        Date eTime = model.getEndTime();
        if (eTime.before(new Date())) {
            //比赛结束
            vo.setRegStatus("end");
        }
        Date regTime = model.getRegTime();
        if (regTime.before(new Date())) {
            //报名截止
            vo.setRegStatus("enroll_end");
        }
        return vo;
    }

}
