package com.efida.sports.pc.web.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.efida.sport.admin.facade.enums.MatchRegEums;
import com.efida.sport.admin.facade.model.SpMatchModel;
import com.efida.sports.util.DateUtil;

/**
 * 相关赛事VO
 * 
 * @author zengbo
 * @version $Id: MatchCorrelationVo.java, v 0.1 2018年7月28日 下午12:01:36 zengbo Exp $
 */
public class MatchCorrelationVo {

    private String  matchName;

    private String  matchCode;

    private String  startTime;

    private String  endTime;

    private Boolean canApply;

    private String  applyStatusDesc;

    private String  applyStatus;

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

    public static List<MatchCorrelationVo> getMatchCorrelationVoList(List<SpMatchModel> models) {
        List<MatchCorrelationVo> volist = new ArrayList<MatchCorrelationVo>();
        if (models == null) {
            return volist;
        }
        for (SpMatchModel model : models) {
            MatchCorrelationVo vo = getMatchCorrelationVo(model);
            if (vo != null) {
                volist.add(vo);
            }
        }
        return volist;
    }

    public static MatchCorrelationVo getMatchCorrelationVo(SpMatchModel model) {
        MatchCorrelationVo vo = new MatchCorrelationVo();
        if (model == null) {
            return vo;
        }
        vo.setMatchName(model.getMatchName());
        vo.setMatchCode(model.getMatchCode());
        vo.setStartTime(DateUtil.formatWeb(model.getStartTime()));
        vo.setEndTime(DateUtil.formatWeb(model.getEndTime()));

        String regStatus = model.getRegStatus();
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

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
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

    public String getMatchCode() {
        return matchCode;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
    }

}
