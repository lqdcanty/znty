package com.efida.sports.pc.web.vo;

import java.util.List;

import com.efida.sport.admin.facade.model.SpMatchModel;
import com.efida.sport.dmp.facade.model.ScoreMatchModel;

/**
 * 成绩VO
 * 
 * @author zengbo
 * @version $Id: AchievementVo.java, v 0.1 2018年7月28日 下午1:14:55 zengbo Exp $
 */
public class AchievementVo {

    private String                       matchCode;

    private String                       matchName;

    /**赛事列表*/
    private List<AchievementMatchListVo> matchList;

    /** 成绩列表对个人中特殊处理 */
    private List<AchievementListVo>      achievements;

    private List<ScoreVo>                scores;

    public static AchievementVo getAchievementVo(SpMatchModel model) {
        AchievementVo vo = new AchievementVo();
        if (model == null) {
            return vo;
        }
        vo.setMatchCode(model.getMatchCode());
        vo.setMatchName(model.getMatchName());
        return vo;
    }

    public static AchievementVo getAchievementVo(ScoreMatchModel model) {
        AchievementVo vo = new AchievementVo();
        if (model == null) {
            return vo;
        }
        vo.setMatchCode(model.getMatchCode());
        vo.setMatchName(model.getMatchName());
        return vo;
    }

    public static AchievementVo getAchievementVo() {
        return null;
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

    public List<AchievementMatchListVo> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<AchievementMatchListVo> matchList) {
        this.matchList = matchList;
    }

    public List<AchievementListVo> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<AchievementListVo> achievements) {
        this.achievements = achievements;
    }

    public List<ScoreVo> getScores() {
        return scores;
    }

    public void setScores(List<ScoreVo> scores) {
        this.scores = scores;
    }

}
