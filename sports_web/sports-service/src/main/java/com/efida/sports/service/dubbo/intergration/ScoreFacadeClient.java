package com.efida.sports.service.dubbo.intergration;

import java.util.Date;
import java.util.List;

import com.efida.sport.dmp.facade.model.*;
import com.efida.sport.dmp.facade.result.DmpPageResult;

/**
 * 
 * 
 * @author zengbo
 * @version $Id: ScoreFacadeClient.java, v 0.1 2018年7月28日 下午4:29:22 zengbo Exp $
 */
public interface ScoreFacadeClient {

    /**
     * 根据项目获取有成绩的赛事列表
     * 
     * @param gameCode  项目编号 ,全部传空
     * @param phoneNum 电话号码  非必传，如果有值 查询自己关联的赛事
     * @param currentPage   当前页数
     * @param pageSzie      每页大小
     * @return
     */
    public DmpPageResult<ScoreMatchModel> queryHasScoreMatch(String gameCode, String phoneNum,
                                                             int currentPage, int pageSize);

    /**
     * 根据赛事编号获取比赛列表
     * 
     * @param matchCode 赛事编号  必传项
     * @param phoneNum  电话号码  非比传，如果有值 查询自己关联的比赛
     * @return
     */
    public List<CompetitionModel> queryCompetitionByMatch(String matchCode, String phoneNum);

    /**
     * 
     * 查询比赛下排名数据
     * @param competitionCode  比赛编号
     * @param competitionDate  比赛时间,非必填，为空查询所有的比赛
     * @param phoneNum 电话号码  非比传，如果有值 查询自己关联的赛事
     * @param currentPage      当前页数
     * @param pageSize         每页大小
     * @return
     */
    public DmpPageResult<RankModel> queryRankByCompetition(String competitionCode,
                                                           Date competitionDate, String phoneNum,
                                                           int currentPage, int pageSize);

    /**
     * 根据比赛编号获取所有比赛时间
     * 
     * @param competitionCode  比赛编号  按时间倒序
     * @param phoneNum 电话号码  非比传，如果有值 查询自己参加比赛的时间
     * @return
     */
    public List<Date> queryCompetitionDate(String competitionCode, String phoneNum);

    /**
     * 根据用户电话号码获取自己的排名
     * 
     * @param phoneNum 电话号码
     * @param competitionDate  比赛时间 为空传null
     * @param competitionCode  比赛编号
     * @return
     */
    public List<RankModel> getMyRank(String phoneNum, Date competitionDate, String competitionCode);

    /**
     * 根据排名编号和比赛编号获取成绩扩展属性
     * 
     * @param rankCode  排名编号
     * @param competitionCode  比赛编号
     * @return
     */
    @Deprecated
    List<SoreExtPro> getRankExtPro(String rankCode, String competitionCode);

    /**
     * 获取成绩详情
     * 
     * @param rankCode
     * @param competitionCode
     * @return
     */
    ScoreGridData getScoreDetail(String rankCode, String competitionCode);

    /**
     * 获取某个比赛下最好成绩
     * @param phoneNum
     * @param competitionDate
     * @param competitionCode
     * @return
     */
    ScoreModel getMyBestScore(String phoneNum, Date competitionDate, String competitionCode);

    /**
     * 
     * 分页获取 用户成绩列表
     * @param phoneNum  电话号码 必填
     * @param competitionDate  比赛时间 非必填
     * @param competitionCode  比赛编号  必填
     * @param currentPage      当前页数
     * @param pageSize         每页大小
     * @return
     */
    DmpPageResult<ScoreModel> getRegisterScores(String phoneNum, Date competitionDate,
                                                String competitionCode, int currentPage,
                                                int pageSize);

    /**
     * 获取用户某个比赛下所有成绩列表
     * 
     * @param phoneNum  电话号码 必填
     * @param competitionDate  比赛时间   非必填
     * @param competitionCode  比赛编号   必填
     * @return
     */
    List<ScoreModel> getRegisterScores(String phoneNum, Date competitionDate,
                                       String competitionCode);

    /**
     * 获取比赛相信信息
     * 
     * @param competitionCode
     * @return
     */
    CompetitionModel getCompetitionInfo(String competitionCode);

    /**
     * 根据比赛名字搜索有成绩的赛事列表
     *
     * @param keyword      关键词
     * @param currentPage   当前页数
     * @param pageSize      每页大小
     * @return
     */
    DmpPageResult<ScoreMatchModel> queryHasScoreMatchByName(String keyword, int currentPage,
                                                            int pageSize);

    /**
     * 获取个人成绩信息 及最好成绩排名信息
     * pc端新版本使用
     * 
     * @param phoneNum
     * @param competitionDate
     * @param competitionCode
     * @return
     */
    List<ScoreModel> getRegisterScoresWithRank(String phoneNum, Date competitionDate,
                                               String competitionCode);
    /**
     * 根据比赛名字搜索有成绩的赛事列表
     *
     * @param competitionCode  比赛编号
     * @return
     */
    List<FieldInfoModel> getHasScoreFieldInfo(String competitionCode);
}