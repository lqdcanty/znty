package com.efida.sports.pc.web.vo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.efida.sport.dmp.facade.model.RankModel;
import com.efida.sport.dmp.facade.model.SoreExtPro;

/**
 * 成绩列表
 * 
 * @author zengbo
 * @version $Id: AchievementListVo.java, v 0.1 2018年7月28日 下午1:18:17 zengbo Exp $
 */
public class AchievementListVo {

    /**
     * 比赛编号
     */
    private String           competitionCode;

    /**
     * 比赛项名称
     */
    private String           competitionName;

    /**
     * 排名编号
     */
    private String           rankCode;
    /**
     * 运动员名称
     */
    private String           playerName;
    /**
    * 运动员编号
    */
    private String           playerCode;
    /**
     * 比赛时间
     */
    private Date             competitionDate;
    /**
     * 排名
     */
    private int              rank;
    /**
     * 运动员头像  可能为空
     */
    private String           playerHeaderImg;
    /**
     * 显示成绩
     */
    private String           score;
    /**
     * 成绩扩展属性
     */
    private List<SoreExtPro> scoreExtPros;
    /**
     * 比赛时间
     */
    private String           competitionDateStr;

    private String           matchName;

    public static List<AchievementListVo> getAchievementList(List<RankModel> ranks) {
        List<AchievementListVo> list = new ArrayList<AchievementListVo>();
        if (ranks == null) {
            return list;
        }
        for (RankModel rank : ranks) {
            AchievementListVo vo = getAchievementListVo(rank);
            if (vo != null) {
                list.add(vo);
            }
        }
        return list;
    }

    public static AchievementListVo getAchievementListVo(RankModel model) {
        AchievementListVo vo = new AchievementListVo();
        if (model == null) {
            return vo;
        }
        vo.setPlayerName(model.getPlayerName());
        vo.setPlayerCode(model.getPlayerCode());
        vo.setCompetitionDate(model.getCompetitionDate());
        vo.setRank(model.getRank());
        vo.setPlayerHeaderImg(model.getPlayerHeaderImg());
        vo.setScore(model.getScore());
        vo.setRankCode(model.getRankCode());
        vo.setCompetitionCode(model.getCompetitionCode());
        vo.setCompetitionDateStr(
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(model.getCompetitionDate()));
        vo.setMatchName(model.getMatchName());
        return vo;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerCode() {
        return playerCode;
    }

    public void setPlayerCode(String playerCode) {
        this.playerCode = playerCode;
    }

    public Date getCompetitionDate() {
        return competitionDate;
    }

    public void setCompetitionDate(Date competitionDate) {
        this.competitionDate = competitionDate;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getPlayerHeaderImg() {
        return playerHeaderImg;
    }

    public void setPlayerHeaderImg(String playerHeaderImg) {
        this.playerHeaderImg = playerHeaderImg;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<SoreExtPro> getScoreExtPros() {
        return scoreExtPros;
    }

    public void setScoreExtPros(List<SoreExtPro> scoreExtPros) {
        this.scoreExtPros = scoreExtPros;
    }

    public String getCompetitionDateStr() {
        return competitionDateStr;
    }

    public void setCompetitionDateStr(String competitionDateStr) {
        this.competitionDateStr = competitionDateStr;
    }

    public String getCompetitionCode() {
        return competitionCode;
    }

    public void setCompetitionCode(String competitionCode) {
        this.competitionCode = competitionCode;
    }

    public String getRankCode() {
        return rankCode;
    }

    public void setRankCode(String rankCode) {
        this.rankCode = rankCode;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

}
