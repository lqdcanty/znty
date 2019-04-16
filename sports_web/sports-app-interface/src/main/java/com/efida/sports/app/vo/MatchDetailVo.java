/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.vo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.efida.sport.admin.facade.enums.MatchRegEums;
import com.efida.sport.admin.facade.model.SpMatchInfoModel;
import com.efida.sports.util.DateUtil;

/**
 * 
 * @author zoutao
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
    private String          startTime;
    /**
     *赛事结束时间 
     */
    private String          endTime;

    /**
     * 简介
     */
    private String          introduce;

    /**
     * 赛事海报(详情页面海报应该取后台设置的banner)
     */
    private String          poster;

    /**
     * 其他章节
     */
    private List<ChapterVo> chapters;

    /**
     * 奖品
     */
    List<AwardsVo>          awards;

    private Boolean         canApply;

    private String          applyStatusDesc = "报名";

    private String          applyStatus     = "5";
    /**
     * 客服电话
     */
    private String          customerService;

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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public static MatchDetailVo getVo(SpMatchInfoModel model) {
        if (model == null) {
            return null;
        }
        MatchDetailVo vo = new MatchDetailVo();
        vo.setAwards(AwardsVo.getVos(model.getAwards()));
        vo.setIntroduce(model.getIntroduce());
        vo.setEndTime(DateUtil.format(model.getEndTime()));
        vo.setMatchName(model.getMatchName());
        vo.setStartTime(DateUtil.format(model.getStartTime()));
        vo.setMatchCode(model.getMatchCode());
        vo.setCustomerService(model.getCustomerService());
        vo.setChapters(ChapterVo.getVos(model.getChapters()));
        String regStatus = model.getRegStatus();
        vo.setPoster(StringUtils.isNotBlank(model.getAppBanner()) ? model.getAppBanner()
            : model.getPoster());
        if (MatchRegEums.pause.getCode().equals(regStatus)) {
            vo.setCanApply(false);
            vo.setApplyStatusDesc("暂停报名");
            vo.setApplyStatus("4");
            return vo;
        }
        Date eTime = model.getEndTime();
        if (eTime.before(new Date())) {
            vo.setCanApply(false);
            vo.setApplyStatusDesc("比赛已结束");
            vo.setApplyStatus("1");
            return vo;
        }
        Date regTime = model.getRegTime();
        if (regTime.before(new Date())) {
            vo.setCanApply(false);
            vo.setApplyStatusDesc("报名已截止");
            vo.setApplyStatus("2");
            return vo;
        }
        vo.setCanApply(true);
        return vo;
    }
}
