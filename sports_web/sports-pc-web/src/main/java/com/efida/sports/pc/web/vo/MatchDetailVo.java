/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.pc.web.vo;

import java.util.Date;
import java.util.List;

import com.efida.sport.admin.facade.model.SpMatchInfoModel;

/**
 * 
 * @author 
 * @version $Id: MatchDetailVo.java, v 0.1 2018年5月25日 下午3:35:49 zoutao Exp $
 */
public class MatchDetailVo {

    /**
     * 赛事名称
     */
    private String          matchName;
    /**
     * 赛事编号
     */
    private String          matchCode;

    /**
     * 赛事开始时间
     */
    private Date            startTime;
    /**
     *赛事结束时间 
     */
    private Date            endTime;

    /**
     * 简介
     */
    private String          introduce;

    /**
     * 其他章节
     */
    private List<ChapterVo> chapters;

    private String          regStatus;

    /**
     * 奖品
     */
    List<AwardsVo>          awards;

    private Boolean         canApply;

    private String          applyStatusDesc;

    private String          applyStatus;

    private String          customerService;

    private String          appBanner;

    private String          pcPanner;

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getMatchCode() {
        return matchCode;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
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

    public List<AwardsVo> getAwards() {
        return awards;
    }

    public void setAwards(List<AwardsVo> list) {
        this.awards = list;
    }

    public List<ChapterVo> getChapters() {
        return chapters;
    }

    public void setChapters(List<ChapterVo> chapters) {
        this.chapters = chapters;
    }

    public Boolean getCanApply() {
        return canApply;
    }

    public void setCanApply(Boolean canApply) {
        this.canApply = canApply;
    }

    public String getApplyStatusDesc() {
        return applyStatusDesc;
    }

    public void setApplyStatusDesc(String applyStatusDesc) {
        this.applyStatusDesc = applyStatusDesc;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getCustomerService() {
        return customerService;
    }

    public void setCustomerService(String customerService) {
        this.customerService = customerService;
    }

    public String getRegStatus() {
        return regStatus;
    }

    public void setRegStatus(String regStatus) {
        this.regStatus = regStatus;
    }

    public String getAppBanner() {
        return appBanner;
    }

    public void setAppBanner(String appBanner) {
        this.appBanner = appBanner;
    }

    public String getPcPanner() {
        return pcPanner;
    }

    public void setPcPanner(String pcPanner) {
        this.pcPanner = pcPanner;
    }

    public static MatchDetailVo getVo(SpMatchInfoModel model) {
        if (model == null) {
            return null;
        }
        MatchDetailVo vo = new MatchDetailVo();
        vo.setAwards(AwardsVo.getVos(model.getAwards()));
        vo.setIntroduce(model.getIntroduce());
        vo.setEndTime(model.getEndTime());
        vo.setMatchName(model.getMatchName());
        vo.setStartTime(model.getStartTime());
        vo.setMatchCode(model.getMatchCode());
        vo.setRegStatus(model.getRegStatus());
        vo.setCustomerService(model.getCustomerService());
        vo.setChapters(ChapterVo.getVos(model.getChapters()));
        vo.setAppBanner(model.getAppBanner());
        vo.setPcPanner(model.getPcPanner());
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
        vo.setCanApply(true);
        return vo;
    }
}
