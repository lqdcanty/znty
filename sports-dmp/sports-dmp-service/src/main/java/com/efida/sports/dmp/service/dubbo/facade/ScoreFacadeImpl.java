/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.dmp.service.dubbo.facade;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.efida.sport.dmp.facade.model.*;
import com.efida.sports.dmp.service.dubbo.cover.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.efida.sport.admin.facade.model.SpMatchModel;
import com.efida.sport.admin.facade.model.open.MatchDetailModel;
import com.efida.sport.dmp.facade.ScoreFacade;
import com.efida.sport.dmp.facade.exception.BusinessException;
import com.efida.sport.dmp.facade.result.DefaultResult;
import com.efida.sport.dmp.facade.result.DmpPageResult;
import com.efida.sports.dmp.dao.entity.CompetitionEntity;
import com.efida.sports.dmp.dao.entity.OpenScoreEntity;
import com.efida.sports.dmp.dao.entity.OpenScoreRankEntity;
import com.efida.sports.dmp.service.dubbo.intergration.SpMatchFacadeClient;
import com.efida.sports.dmp.service.score.CompetitionService;
import com.efida.sports.dmp.service.score.ScoreRankService;

import cn.evake.auth.exception.AuthBusException;
import cn.evake.auth.usermodel.PagingResult;

/**
 * 
 * @author zoutao
 * @version $Id: ScoreFacadeImpl.java, v 0.1 2018年7月28日 下午1:10:59 zoutao Exp $
 */
@Service
public class ScoreFacadeImpl implements ScoreFacade {

    @Autowired
    private ScoreRankService    scoreRankService;
    @Autowired
    private SpMatchFacadeClient spMatchFacadeClient;
    @Autowired
    private CompetitionService  competitionService;
    private static Logger       log = LoggerFactory.getLogger(ScoreFacadeImpl.class);

    @Override
    public DefaultResult<DmpPageResult<ScoreMatchModel>> queryHasScoreMatch(String gameCode,
                                                                            String phoneNum,
                                                                            int currentPage,
                                                                            int pageSize) {

        DefaultResult<DmpPageResult<ScoreMatchModel>> dr = new DefaultResult<DmpPageResult<ScoreMatchModel>>();
        dr.setSuccess(false);
        try {
            PagingResult<String> page = competitionService.queryMatchCodes(gameCode, phoneNum,
                currentPage, pageSize);
            if (page == null || page.getList() == null || page.getList().size() < 1) {
                DmpPageResult<ScoreMatchModel> retpage = new DmpPageResult<ScoreMatchModel>(
                    ScoreMatchCover.geModels(null), 0, currentPage, pageSize);
                dr.setSuccess(true);
                dr.setResultObj(retpage);
                return dr;
            }

            List<String> list = page.getList();
            List<SpMatchModel> matchs = spMatchFacadeClient.getMatchsByMatchCodes(list);
            DmpPageResult<ScoreMatchModel> retpage = new DmpPageResult<ScoreMatchModel>(
                ScoreMatchCover.geModels(matchs), page.getAllRow(), currentPage, pageSize);
            dr.setResultObj(retpage);
            dr.setSuccess(true);
        } catch (AuthBusException e) {
            log.error("", e);
            dr.setErrorMsg(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            dr.setErrorMsg("获取赛事列表失败");
        }
        return dr;
    }

    @Override
    public DefaultResult<List<CompetitionModel>> queryCompetitionByMatch(String matchCode,
                                                                         String phoneNum) {

        DefaultResult<List<CompetitionModel>> dr = new DefaultResult<List<CompetitionModel>>();
        dr.setSuccess(false);
        try {
            List<CompetitionEntity> competitions = competitionService.selectCompetitions(matchCode,
                phoneNum);
            dr.setSuccess(true);
            dr.setResultObj(CompetitionCover.entitys2models(competitions));
        } catch (BusinessException e) {
            log.error("", e);
            dr.setErrorMsg(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            dr.setErrorMsg("查询失败");
        }
        return dr;
    }

    @Override
    public DefaultResult<DmpPageResult<RankModel>> queryRankByCompetition(String competitionCode,
                                                                          Date competitionDate,
                                                                          String phoneNum,
                                                                          int currentPage,
                                                                          int pageSize) {

        DefaultResult<DmpPageResult<RankModel>> dr = new DefaultResult<DmpPageResult<RankModel>>();
        dr.setSuccess(false);
        try {

            PagingResult<OpenScoreRankEntity> page = competitionService.queryRankByCompetition(
                competitionCode, competitionDate, phoneNum, currentPage, pageSize);
            DmpPageResult<RankModel> retpage = new DmpPageResult<RankModel>(
                RankCover.entitys2models(page.getList(), competitionCode), page.getAllRow(),
                currentPage, pageSize);
            dr.setSuccess(true);
            dr.setResultObj(retpage);
            return dr;
        } catch (BusinessException e) {
            dr.setErrorMsg(e.getMessage());
            log.error("", e);
        } catch (Exception e) {
            dr.setErrorMsg("查询失败");
            log.error("", e);
        }
        return dr;
    }

    @Override
    public DefaultResult<List<Date>> queryCompetitionDate(String competitionCode, String phoneNum) {

        DefaultResult<List<Date>> dr = new DefaultResult<List<Date>>();
        dr.setSuccess(false);
        try {
            List<Date> dates = competitionService.queyCompetitionDate(competitionCode, phoneNum);
            dr.setSuccess(true);
            dr.setResultObj(dates);
            return dr;
        } catch (BusinessException e) {
            log.error("", e);
            dr.setErrorMsg(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            dr.setErrorMsg("查询失败");
        }
        return dr;
    }

    @Override
    public DefaultResult<List<RankModel>> getMyRank(String phoneNum, Date competitionDate,
                                                    String competitionCode) {

        DefaultResult<List<RankModel>> dr = new DefaultResult<List<RankModel>>();
        dr.setSuccess(false);
        try {
            List<OpenScoreRankEntity> myRanks = competitionService.getMyRanks(phoneNum,
                competitionDate, competitionCode);

            List<RankModel> mdoels = RankCover.entitys2models(myRanks, competitionCode);
            dr.setSuccess(true);
            dr.setResultObj(mdoels);
            return dr;
        } catch (BusinessException e) {
            dr.setErrorMsg(e.getMessage());
            log.error("", e);
        } catch (Exception e) {
            log.error("", e);
            dr.setErrorMsg("查询失败");
        }
        return dr;
    }

    @Override
    @Deprecated
    public DefaultResult<List<SoreExtPro>> getRankExtPro(String rankCode, String competitionCode) {
        DefaultResult<List<SoreExtPro>> dr = new DefaultResult<List<SoreExtPro>>();
        dr.setSuccess(false);
        try {
            List<SoreExtPro> pros = competitionService.getExtPro(rankCode, competitionCode);
            dr.setSuccess(true);
            dr.setResultObj(pros);
            return dr;
        } catch (BusinessException e) {
            dr.setErrorMsg(e.getMessage());
            log.error("", e);
        } catch (Exception e) {
            log.error("", e);
            dr.setErrorMsg("查询失败");
        }
        return dr;
    }

    @Override
    public DefaultResult<ScoreGridData> getScoreGridData(String rankCode, String competitionCode) {
        DefaultResult<ScoreGridData> dr = new DefaultResult<ScoreGridData>();
        dr.setSuccess(false);
        try {
            ScoreGridData pros = competitionService.getScoreGridData(rankCode, competitionCode);
            dr.setSuccess(true);
            dr.setResultObj(pros);
            return dr;
        } catch (BusinessException e) {
            dr.setErrorMsg(e.getMessage());
            log.error("", e);
        } catch (Exception e) {
            log.error("", e);
            dr.setErrorMsg(e.getMessage());
        }
        return dr;
    }

    @Override
    public DefaultResult<CompetitionModel> getCompetitionInfo(String competitionCode) {
        DefaultResult<CompetitionModel> defaultResult = new DefaultResult<CompetitionModel>();
        try {
            CompetitionEntity entity = competitionService.getCompetitionByCode(competitionCode);
            if (entity == null) {
                defaultResult.setResultObj(null);
                defaultResult.setSuccess(true);
                return defaultResult;
            }
            MatchDetailModel matchModel = spMatchFacadeClient
                .getMatchDetailModel(entity.getMatchCode());
            entity.setMatchDetail(matchModel);
            CompetitionModel competitionModel = CompetitionCover.entity2Model(entity);
            defaultResult.setResultObj(competitionModel);
            defaultResult.setSuccess(true);
            return defaultResult;
        } catch (BusinessException e) {
            defaultResult.setErrorMsg(e.getMessage());
            log.error("", e);
        } catch (Exception e) {
            defaultResult.setErrorMsg("查询失败");
        }
        defaultResult.setSuccess(false);
        return defaultResult;
    }

    @Override
    public DefaultResult<List<ScoreModel>> getRegisterScores(String phoneNum, Date competitionDate,
                                                             String competitionCode) {

        DefaultResult<List<ScoreModel>> dr = new DefaultResult<List<ScoreModel>>();
        dr.setSuccess(false);
        try {

            List<OpenScoreEntity> scores = competitionService.queryRegisterScores(phoneNum,
                competitionDate, competitionCode);
            List<ScoreModel> models = ScoreCover.scores2models(scores, competitionCode);
            dr.setSuccess(true);
            dr.setResultObj(models);
            return dr;
        } catch (BusinessException e) {
            dr.setErrorMsg(e.getMessage());
            log.error("", e);
        } catch (Exception e) {
            log.error("", e);
            dr.setErrorMsg("查询失败");
        }
        return dr;

    }

    @Override
    public DefaultResult<ScoreModel> getMyBestScore(String phoneNum, Date competitionDate,
                                                    String competitionCode) {

        DefaultResult<ScoreModel> dr = new DefaultResult<ScoreModel>();
        try {
            OpenScoreEntity entity = competitionService.getRegisterBestScore(phoneNum,
                competitionDate, competitionCode);
            dr.setResultObj(ScoreCover.score2model(entity, competitionCode));
            dr.setSuccess(true);
            return dr;
        } catch (BusinessException e) {
            dr.setErrorMsg(e.getMessage());
            log.error("", e);
        } catch (Exception e) {
            dr.setErrorMsg("查询失败");
        }
        return dr;
    }

    @Override
    public DefaultResult<DmpPageResult<ScoreModel>> getRegisterScores(String phoneNum,
                                                                      Date competitionDate,
                                                                      String competitionCode,
                                                                      int currentPage,
                                                                      int pageSize) {

        DefaultResult<DmpPageResult<ScoreModel>> dr = new DefaultResult<DmpPageResult<ScoreModel>>();
        dr.setSuccess(false);
        try {

            PagingResult<OpenScoreEntity> page = competitionService.queryRegisterScores(phoneNum,
                competitionDate, competitionCode, currentPage, pageSize);
            DmpPageResult<ScoreModel> retpage = new DmpPageResult<ScoreModel>(
                ScoreCover.scores2models(page.getList(), competitionCode), page.getAllRow(),
                currentPage, pageSize);
            dr.setSuccess(true);
            dr.setResultObj(retpage);
            return dr;
        } catch (BusinessException e) {
            dr.setErrorMsg(e.getMessage());
            log.error("", e);
        } catch (Exception e) {
            dr.setErrorMsg("查询失败");
            log.error("", e);
        }
        return dr;

    }

    @Override
    public DefaultResult<DmpPageResult<ScoreMatchModel>> queryHasScoreMatchByName(String keyword,
                                                                                  int currentPage,
                                                                                  int pageSize) {
        DefaultResult<DmpPageResult<ScoreMatchModel>> dr = new DefaultResult<DmpPageResult<ScoreMatchModel>>();
        dr.setSuccess(false);
        try {

            PagingResult<String> page = competitionService.queryMatchCodesByName(keyword, currentPage, pageSize);
            if (page == null || page.getList() == null || page.getList().size() < 1) {
                DmpPageResult<ScoreMatchModel> retpage = new DmpPageResult<ScoreMatchModel>(
                        ScoreMatchCover.geModels(null), 0, currentPage, pageSize);
                dr.setSuccess(true);
                dr.setResultObj(retpage);
                return dr;
            }
            List<String> list = page.getList();
            List<SpMatchModel> matchs = spMatchFacadeClient.getMatchsByMatchCodes(list);
            DmpPageResult<ScoreMatchModel> retpage = new DmpPageResult(
                ScoreMatchCover.geModels(matchs),  page.getAllRow(), currentPage, pageSize);
            dr.setResultObj(retpage);
            dr.setSuccess(true);
        } catch (AuthBusException e) {
            log.error("", e);
            dr.setErrorMsg(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            dr.setErrorMsg("获取赛事列表失败");
        }
        return dr;

    }

    /**
     * 
     * @see com.efida.sport.dmp.facade.ScoreFacade#getRegisterScoresWithRank(java.lang.String, java.util.Date, java.lang.String, int, int)
     */
    @Override
    public DefaultResult<List<ScoreModel>> getRegisterScoresWithRank(String phoneNum,
                                                                     Date competitionDate,
                                                                     String competitionCode) {

        DefaultResult<List<ScoreModel>> dr = new DefaultResult<List<ScoreModel>>();
        dr.setSuccess(false);
        try {
            //查询用户所有成绩
            List<OpenScoreEntity> scores = competitionService.queryRegisterScores(phoneNum,
                competitionDate, competitionCode);

            PagingResult<OpenScoreRankEntity> bestRankPage = competitionService
                .queryRankByCompetition(competitionCode, competitionDate, phoneNum, 1, 1);
            List<OpenScoreRankEntity> rankList = bestRankPage.getList();
            String ranking = "-";
            if (rankList != null && rankList.size() > 0) {
                OpenScoreRankEntity rankInfo = rankList.iterator().next();
                if (rankInfo.getScore() != null) {
                    ranking = rankInfo.getRanking() != null ? rankInfo.getRanking() + "" : "-";
                }
            }
            boolean isBigOk = false;
            //
            Double highScore = null;
            OpenScoreEntity best = null;

            if (!CollectionUtils.isEmpty(scores)) {
                OpenScoreEntity firstItem = scores.get(0);
                if ("big".equals(firstItem.getScoreType())) {
                    isBigOk = true;
                }
                for (OpenScoreEntity score : scores) {
                    if (score.getScore() == null) {
                        score.setScore(new BigDecimal("0"));
                    }
                    if (best == null) {
                        best = score;
                    } else {
                        if (isBigOk) {

                            if (score.getScore().doubleValue() > best.getScore().doubleValue()) {
                                best = score;
                            }
                        } else {
                            if (score.getScore().doubleValue() < best.getScore().doubleValue()) {
                                best = score;
                            }
                        }
                    }
                }
            }

            if (best != null) {
                highScore = best.getScore().doubleValue();
            }
            List<ScoreModel> models = ScoreCover.scores2models(scores, competitionCode);

            if (models != null && highScore != null) {
                for (ScoreModel sm : models) {
                    if (sm.getScore() != null && sm.getScore().doubleValue() == highScore) {
                        sm.setRanking(ranking);
                    }
                }
            }

            ScoreComparator scoreComp = new ScoreComparator();
            scoreComp.setBigOk(isBigOk);
            if (models != null && models.size() > 0) {
                Collections.sort(models, scoreComp);
            }

            dr.setSuccess(true);
            dr.setResultObj(models);
            return dr;
        } catch (BusinessException e) {
            dr.setErrorMsg(e.getMessage());
            log.error("", e);
        } catch (Exception e) {
            dr.setErrorMsg("查询失败");
            log.error("", e);
        }
        return dr;

    }
    @Override
    public DefaultResult<List<FieldInfoModel>> getHasScoreFieldInfo(String competitionCode){
        DefaultResult<List<FieldInfoModel>> defaultResult = new DefaultResult<List<FieldInfoModel>>();
        try {
            List<OpenScoreRankEntity>  scoreRankList  =  scoreRankService.selectCompetitionInfoByScore(competitionCode);
            List<FieldInfoModel> fieldInfoModels = FieldInfoCover.entitys2models(scoreRankList);
            defaultResult.setResultObj(fieldInfoModels);
            defaultResult.setSuccess(true);
            return defaultResult;
        } catch (BusinessException e) {
            defaultResult.setErrorMsg(e.getMessage());
            log.error("", e);
        } catch (Exception e) {
            defaultResult.setErrorMsg("查询失败");
        }
        defaultResult.setSuccess(false);
        return defaultResult;
    }
}
