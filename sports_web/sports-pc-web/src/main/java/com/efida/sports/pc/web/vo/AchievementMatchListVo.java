package com.efida.sports.pc.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.efida.sport.dmp.facade.model.CompetitionModel;

/**
 * 成绩VO
 * 
 * @author zengbo
 * @version $Id: AchievementVo.java, v 0.1 2018年7月28日 下午1:14:55 zengbo Exp $
 */
public class AchievementMatchListVo {

    private String                  competitionCode;

    private String                  competitionName;

    private String                  matchCode;

    private long                    total;

    private int                     pages;

    private int                     current;

    /** 成绩列表 */
    private List<AchievementListVo> achievements;
    /**
    * 我的成绩
    */
    private List<AchievementListVo> myAchievements;

    /** 日期*/
    private List<String>            dates;

    public static List<AchievementMatchListVo> getScoreMatchModelListVo(List<CompetitionModel> models) {
        List<AchievementMatchListVo> volist = new ArrayList<AchievementMatchListVo>();
        if (models == null) {
            return volist;
        }
        for (CompetitionModel model : models) {
            AchievementMatchListVo vo = getAchievementMatchListVo(model);
            if (vo != null) {
                volist.add(vo);
            }
        }
        return volist;
    }

    public static AchievementMatchListVo getAchievementMatchListVo(CompetitionModel model) {
        AchievementMatchListVo vo = new AchievementMatchListVo();
        if (model == null) {
            return vo;
        }
        vo.setCompetitionCode(model.getCompetitionCode());
        vo.setCompetitionName(model.getCompetitionName());
        return vo;
    }

    public String getCompetitionCode() {
        return competitionCode;
    }

    public void setCompetitionCode(String competitionCode) {
        this.competitionCode = competitionCode;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public List<AchievementListVo> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<AchievementListVo> achievements) {
        this.achievements = achievements;
    }

    public String getMatchCode() {
        return matchCode;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<AchievementListVo> getMyAchievements() {
        return myAchievements;
    }

    public void setMyAchievements(List<AchievementListVo> myAchievements) {
        this.myAchievements = myAchievements;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

}
