package com.efida.sports.service.dubbo.intergration.impl;

import java.util.Date;
import java.util.List;

import com.efida.sport.dmp.facade.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.efida.sport.admin.facade.exception.BusinessException;
import com.efida.sport.dmp.facade.ScoreFacade;
import com.efida.sport.dmp.facade.result.DefaultResult;
import com.efida.sport.dmp.facade.result.DmpPageResult;
import com.efida.sports.service.dubbo.intergration.ScoreFacadeClient;

/**
 * 
 * 
 * @author zengbo
 * @version $Id: ScoreFacadeClientImpl.java, v 0.1 2018年7月28日 下午4:30:11 zengbo Exp $
 */
@Service("scoreFacadeClient")
public class ScoreFacadeClientImpl implements ScoreFacadeClient {

    /**  */
    private static Logger logger = LoggerFactory.getLogger(ScoreFacadeClientImpl.class);

    /**  */
    @Reference
    private ScoreFacade   scoreFacade;

    @Override
    public DmpPageResult<ScoreMatchModel> queryHasScoreMatch(String gameCode, String phoneNum,
                                                             int currentPage, int pageSize) {
        DefaultResult<DmpPageResult<ScoreMatchModel>> rs = scoreFacade.queryHasScoreMatch(gameCode,
            phoneNum, currentPage, pageSize);
        if (!rs.isSuccess()) {
            logger.error("query ScoreMatchModel failed", rs.getErrorInfo());
            throw new BusinessException(rs.getErrorMsg());
        }
        return rs.getResultObj();
    }

    @Override
    public List<CompetitionModel> queryCompetitionByMatch(String matchCode, String phoneNum) {
        DefaultResult<List<CompetitionModel>> result = scoreFacade
            .queryCompetitionByMatch(matchCode, phoneNum);
        if (!result.isSuccess()) {
            logger.error("query queryCompetitionByMatch failed", result.getErrorInfo());
            throw new BusinessException(result.getErrorMsg());
        }
        return result.getResultObj();
    }

    @Override
    public DmpPageResult<RankModel> queryRankByCompetition(String competitionCode,
                                                           Date competitionDate, String phoneNum,
                                                           int currentPage, int pageSize) {
        DefaultResult<DmpPageResult<RankModel>> rs = scoreFacade.queryRankByCompetition(
            competitionCode, competitionDate, phoneNum, currentPage, pageSize);
        if (!rs.isSuccess()) {
            logger.error("query RankModel failed", rs.getErrorInfo());
            throw new BusinessException(rs.getErrorMsg());
        }
        return rs.getResultObj();
    }

    @Override
    public List<Date> queryCompetitionDate(String competitionCode, String phoneNum) {
        DefaultResult<List<Date>> result = scoreFacade.queryCompetitionDate(competitionCode,
            phoneNum);
        if (!result.isSuccess()) {
            logger.error("query queryCompetitionDate failed", result.getErrorInfo());
            throw new BusinessException(result.getErrorMsg());
        }
        return result.getResultObj();
    }

    @Override
    public List<RankModel> getMyRank(String phoneNum, Date competitionDate,
                                     String competitionCode) {
        DefaultResult<List<RankModel>> result = scoreFacade.getMyRank(phoneNum, competitionDate,
            competitionCode);
        if (!result.isSuccess()) {
            logger.error("query getMyRank failed", result.getErrorInfo());
            throw new BusinessException(result.getErrorMsg());
        }
        return result.getResultObj();
    }

    @Override
    public List<SoreExtPro> getRankExtPro(String rankCode, String competitionCode) {
        DefaultResult<List<SoreExtPro>> result = scoreFacade.getRankExtPro(rankCode,
            competitionCode);
        if (!result.isSuccess()) {
            logger.error("query getRankExtPro failed", result.getErrorInfo());
            throw new BusinessException(result.getErrorMsg());
        }
        return result.getResultObj();
    }

    @Override
    public ScoreGridData getScoreDetail(String rankCode, String competitionCode) {
        DefaultResult<ScoreGridData> result = scoreFacade.getScoreGridData(rankCode,
            competitionCode);
        if (!result.isSuccess()) {
            logger.error("query getRankExtPro failed", result.getErrorInfo());
            throw new BusinessException(result.getErrorMsg());
        }
        return result.getResultObj();
    }

    /**
     * 获取比赛相信信息
     * 
     * @param competitionCode
     * @return
     */
    @Override
    public CompetitionModel getCompetitionInfo(String competitionCode) {

        DefaultResult<CompetitionModel> result = scoreFacade.getCompetitionInfo(competitionCode);
        if (!result.isSuccess()) {
            logger.error("get competitionInfo failed", result.getErrorInfo());
            throw new BusinessException(result.getErrorMsg());
        }
        return result.getResultObj();

    }

    /**
     * 获取用户某个比赛下所有成绩列表
     * 
     * @param phoneNum  电话号码 必填
     * @param competitionDate  比赛时间   非必填
     * @param competitionCode  比赛编号   必填
     * @return
     */
    @Override
    public List<ScoreModel> getRegisterScores(String phoneNum, Date competitionDate,
                                              String competitionCode) {

        DefaultResult<List<ScoreModel>> result = scoreFacade.getRegisterScores(phoneNum,
            competitionDate, competitionCode);
        if (!result.isSuccess()) {
            logger.error("get register scores failed", result.getErrorInfo());
            throw new BusinessException(result.getErrorMsg());
        }
        return result.getResultObj();

    }

    @Override
    public List<ScoreModel> getRegisterScoresWithRank(String phoneNum, Date competitionDate,
                                                      String competitionCode) {

        DefaultResult<List<ScoreModel>> result = scoreFacade.getRegisterScoresWithRank(phoneNum,
            competitionDate, competitionCode);
        if (!result.isSuccess()) {
            logger.error("get register scores failed", result.getErrorInfo());
            throw new BusinessException(result.getErrorMsg());
        }
        return result.getResultObj();

    }

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
    @Override
    public DmpPageResult<ScoreModel> getRegisterScores(String phoneNum, Date competitionDate,
                                                       String competitionCode, int currentPage,
                                                       int pageSize) {

        DefaultResult<DmpPageResult<ScoreModel>> result = scoreFacade.getRegisterScores(phoneNum,
            competitionDate, competitionCode, currentPage, pageSize);
        if (!result.isSuccess()) {
            logger.error("get RegisterScores failed", result.getErrorInfo());
            throw new BusinessException(result.getErrorMsg());
        }
        return result.getResultObj();

    }

    /**
     * 获取某个比赛下最好成绩
     * @param phoneNum
     * @param competitionDate
     * @param competitionCode
     * @return
     */
    @Override
    public ScoreModel getMyBestScore(String phoneNum, Date competitionDate,
                                     String competitionCode) {

        DefaultResult<ScoreModel> result = scoreFacade.getMyBestScore(phoneNum, competitionDate,
            competitionCode);
        if (!result.isSuccess()) {
            logger.error("get register best score failed", result.getErrorInfo());
            throw new BusinessException(result.getErrorMsg());
        }
        return result.getResultObj();

    }

    /**
     * 根据比赛名字搜索有成绩的赛事列表
     *
     * @param keyword      关键词
     * @param currentPage   当前页数
     * @param pageSize      每页大小
     * @return
     */
    @Override
    public DmpPageResult<ScoreMatchModel> queryHasScoreMatchByName(String keyword, int currentPage,
                                                                   int pageSize) {
        DefaultResult<DmpPageResult<ScoreMatchModel>> rs = scoreFacade
            .queryHasScoreMatchByName(keyword, currentPage, pageSize);
        if (!rs.isSuccess()) {
            logger.error("query ScoreMatchModel failed", rs.getErrorInfo());
            throw new BusinessException(rs.getErrorMsg());
        }
        return rs.getResultObj();
    }

    /**
     * 根据比赛名字搜索有成绩的赛事列表
     *
     * @param competitionCode  比赛编号
     * @return
     */
    @Override
    public List<FieldInfoModel> getHasScoreFieldInfo(String competitionCode) {

        DefaultResult<List<FieldInfoModel>> rs = scoreFacade.getHasScoreFieldInfo(competitionCode);
        if (!rs.isSuccess()) {
            logger.error("query ScoreMatchModel failed", rs.getErrorInfo());
            throw new BusinessException(rs.getErrorMsg());
        }
        return rs.getResultObj();
    }

}
