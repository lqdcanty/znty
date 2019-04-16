package com.efida.sports.pc.web.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.efida.sport.dmp.facade.model.ScoreMatchModel;
import com.efida.sports.util.DateUtil;

/**
 * 
 * 
 * @author zengbo
 * @version $Id: ScoreMatchModelVo.java, v 0.1 2018年7月28日 下午5:05:19 zengbo Exp $
 */
public class ScoreMatchModelVo {

    /**  */
    private static final long serialVersionUID = 1L;
    /**
     * 赛事编号
     */
    private String            matchCode;
    /**
     * 赛事名称
     */
    private String            matchName;
    /**
     * 赛事图片
     */
    private String            matchImg;
    /**
     * 开始时间
     */
    private Date              startTime;
    /**
     * 结束时间
     */
    private Date              endTime;

    /**
     * 开始时间
     */
    private String            startTimeStr;
    /**
     * 结束时间
     */
    private String            endTimeStr;

    public static List<ScoreMatchModelVo> getScoreMatchModelVoList(List<ScoreMatchModel> models) {
        List<ScoreMatchModelVo> volist = new ArrayList<ScoreMatchModelVo>();
        if (models == null) {
            return volist;
        }
        for (ScoreMatchModel model : models) {
            ScoreMatchModelVo vo = getScoreMatchModelVo(model);
            if (vo != null) {
                volist.add(vo);
            }
        }
        return volist;
    }

    public static ScoreMatchModelVo getScoreMatchModelVo(ScoreMatchModel model) {
        ScoreMatchModelVo vo = new ScoreMatchModelVo();
        if (model == null) {
            return vo;
        }
        vo.setMatchCode(model.getMatchCode());
        vo.setMatchImg(model.getMatchImg());
        vo.setMatchName(model.getMatchName());
        vo.setEndTime(model.getEndTime());
        vo.setStartTime(model.getStartTime());
        vo.setStartTimeStr(DateUtil.formatWeb(model.getStartTime()));
        vo.setEndTimeStr(DateUtil.formatWeb(model.getEndTime()));
        return vo;
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

    public String getMatchImg() {
        return matchImg;
    }

    public void setMatchImg(String matchImg) {
        this.matchImg = matchImg;
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

}
