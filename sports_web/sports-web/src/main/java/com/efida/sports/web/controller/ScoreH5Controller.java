package com.efida.sports.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sport.admin.facade.model.SpMatchInfoModel;
import com.efida.sport.admin.facade.model.open.MatchGroupItemModel;
import com.efida.sport.dmp.facade.model.CompetitionModel;
import com.efida.sport.dmp.facade.model.RankModel;
import com.efida.sport.dmp.facade.model.ScoreGridData;
import com.efida.sport.dmp.facade.model.ScoreHeader;
import com.efida.sport.dmp.facade.model.ScoreMatchModel;
import com.efida.sport.dmp.facade.model.ScoreModel;
import com.efida.sport.dmp.facade.result.DmpPageResult;
import com.efida.sports.enums.ErrorCodeEnum;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.service.dubbo.intergration.ScoreFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpMatchFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;
import com.efida.sports.util.JsonResultUtil;
import com.efida.sports.web.converter.ScoreConverter;
import com.efida.sports.web.util.FileUtils;
import com.hazelcast.util.StringUtil;

/**
 * Created by wnagyan on 2018/7/18.
 */
@RequestMapping("score")
@Controller
public class ScoreH5Controller {

    @RequestMapping("my")
    public String myScore() {
        return "pages/my_score";
    }

    @Autowired
    private ScoreFacadeClient           scoreFacadeClient;

    @Autowired
    private SpMatchFacadeClient         spMatchFacadeClient;

    @Autowired
    private UcenterAdapterService       ucenterAdapterService;
    @Autowired
    private UcenterRegisterFacadeClient registerFacadeClient;

    @Value("${ucenter-domain}")
    public String                       ucenterDomain;
    @Value("${apply-domain}")
    public String                       applyDomain;

    private static Logger               log = LoggerFactory.getLogger(ScoreH5Controller.class);

    /**
     * 获取所有比赛日期
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDate", method = { RequestMethod.GET })
    public Map<String, Object> getDate(@RequestParam(value = "competitionCode", required = true) String competitionCode,
                                       @RequestParam(value = "token", required = false) String token) {
        try {
            List<Date> allDate = new ArrayList<>();
            allDate.add(new Date(System.currentTimeMillis()));
            allDate.add(new Date(System.currentTimeMillis() - 1000 * 3600 * 24));
            allDate.add(new Date(System.currentTimeMillis() - 1000 * 3600 * 48));
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("allDate", allDate);
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取比赛日期失败,请稍后重试！！！");
        }
    }

    /**
     * 比赛成绩列表-排名
     *
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "rankList", method = { RequestMethod.GET })
    public Map<String, Object> rankList(@RequestParam(value = "competitionCode", required = true) String competitionCode,
                                        @RequestParam(value = "competitionDate", required = false) String competitionDate,
                                        @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

        log.info("*****************************score/rankList********competitionCode="
                 + competitionCode + " currentPage=" + currentPage + " pageSize=" + pageSize);

        try {
            if (StringUtil.isNullOrEmpty(competitionCode)) {
                return JsonResultUtil.getServerErrorResult("competitionCode为空！");
            }
            DmpPageResult<RankModel> rank = scoreFacadeClient
                .queryRankByCompetition(competitionCode, null, null, currentPage, pageSize);
            if (rank == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "未获取到排名");
            }
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("rankList", rank.getList());
            hashMap.put("currentPage", rank.getCurrentPage());
            hashMap.put("pageSize", rank.getPageSize());
            hashMap.put("allRow", rank.getAllRow());
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取比赛成绩列表,请稍后重试！！！");
        }
    }

    /**
     * 我的成绩-排名
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "myRank", method = { RequestMethod.GET })
    public Map<String, Object> myRank(@RequestParam(value = "competitionCode", required = true) String competitionCode,
                                      @RequestParam(value = "competitionDate", required = false) String competitionDate,
                                      @RequestParam(value = "token", required = true) String token,
                                      HttpSession session) {
        log.info("*****************************score/myRank********competitionCode="
                 + competitionCode + " token=" + token);

        if (StringUtil.isNullOrEmpty(competitionCode)) {
            return JsonResultUtil.getServerErrorResult("competitionCode为空！");
        }
        /* register = new Register();
        register.setAccount("16601154599");*/
        try {
            RegisterModel register = ucenterAdapterService.getAppAuthRegister(token);
            if (register == null) {
                AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
                if (auth == null) {
                    return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(),
                        "用户未登录");
                }
                register = auth.getRegister();
            }
            register = registerFacadeClient.getRegsiterByRegisterCode(register.getRegisterCode());
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.SERVER_ERROR.getCode(), "用户未存在");
            }
            List<RankModel> myRank = scoreFacadeClient.getMyRank(register.getAccount(), null,
                competitionCode);
            if (myRank == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "未获取我的排名");
            }
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            RankModel rankModel = null;
            if (myRank.size() > 0) {
                rankModel = myRank.get(0);
                rankModel.setPlayerHeaderImg(register.getHeadimgUrl());
            }
            hashMap.put("myRank", rankModel);
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取我的成绩失败,请稍后重试！！！");
        }
    }

    /**
     * 我的成绩-详情(未使用)
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getMyScore", method = { RequestMethod.GET })
    public Map<String, Object> getMyScore(@RequestParam(value = "competitionCode", required = true) String competitionCode,
                                          @RequestParam(value = "matchCode", required = true) String matchCode,
                                          @RequestParam(value = "competitionDate", required = false) String competitionDate,
                                          @RequestParam(value = "token", required = true) String token,
                                          HttpSession session) {

        log.info("*****************************score/getMyScore********competitionCode="
                 + competitionCode + " matchCode=" + matchCode + " token=" + token);

        try {

            RegisterModel register = ucenterAdapterService.getAppAuthRegister(token);

            if (register == null) {
                AuthToken authToken = ucenterAdapterService.getH5AuthToken(session);
                if (authToken == null) {
                    return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(),
                        "用户未登录");
                }
                register = authToken.getRegister();
            }

            if (StringUtil.isNullOrEmpty(competitionCode)) {
                return JsonResultUtil.getServerErrorResult("competitionCode为空！");
            }
            SpMatchInfoModel spMatchInfoModel = spMatchFacadeClient.getMatchDetail(matchCode);
            if (spMatchInfoModel == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "赛事不存在");
            }
            List<RankModel> myRanks = scoreFacadeClient.getMyRank(register.getAccount(), null,
                competitionCode);
            if (myRanks == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "未获取排名");
            }
            List<MatchGroupItemModel> matchGroupItemModels = spMatchFacadeClient
                .getMatchGroupItemList(matchCode);
            MatchGroupItemModel matchGroupItemModel = null;
            if (matchGroupItemModels.size() > 0) {
                matchGroupItemModel = matchGroupItemModels.get(0);
            }
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            RankModel rankModel = null;
            ScoreGridData scoreGridData = null;
            if (myRanks.size() > 0) {
                rankModel = myRanks.get(0);
                scoreGridData = scoreFacadeClient.getScoreDetail(rankModel.getRankCode(),
                    rankModel.getCompetitionCode());

            }
            List<Map<String, Object>> score = null;
            if (scoreGridData != null) {
                List<ScoreHeader> scoreHeaders = scoreGridData.getHeader();
                List<List<String>> dataList = scoreGridData.getDetail().getData();
                score = ScoreConverter.convertHeader(scoreHeaders, dataList);
            }
            log.info("*****************************score/getMyScore********scoreGridData="
                     + scoreGridData);

            hashMap.put("soreExtPros", score);
            hashMap.put("myRank", rankModel);
            hashMap.put("matchName", spMatchInfoModel.getMatchName());
            hashMap.put("matchCode", spMatchInfoModel.getMatchCode());
            hashMap.put("matchEndTime", spMatchInfoModel.getEndTime());
            hashMap.put("matchStartTime", spMatchInfoModel.getStartTime());
            hashMap.put("matchGroup", matchGroupItemModel);
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取我的成绩失败,请稍后重试！！！");
        }
    }

    /**
     * 我的成绩（all）
     *
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "myRankList", method = { RequestMethod.GET })
    public Map<String, Object> myRankList(@RequestParam(value = "token", required = true) String token,
                                          @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                          @RequestParam(value = "pageSzie", required = false, defaultValue = "10") int pageSzie,
                                          HttpSession session) {

        log.info("*****************************score/myRankList********currentPage=" + currentPage
                 + " pageSzie=" + pageSzie);
        log.info("*****************************score/myRankList********token=" + token);

        try {
            RegisterModel register = ucenterAdapterService.getAppAuthRegister(token);

            if (register == null) {
                AuthToken authToken = ucenterAdapterService.getH5AuthToken(session);
                if (authToken == null) {
                    return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(),
                        "用户未登录");
                }
                register = authToken.getRegister();
            }
            List<Map<String, Object>> resultList = new ArrayList<>();
            //赛事
            DmpPageResult<ScoreMatchModel> data = scoreFacadeClient.queryHasScoreMatch(null,
                register.getAccount(), currentPage, pageSzie);
            if (data == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(),
                    "未能获取用户参加的赛事");
            }
            log.info("*****************************score/myRankList********data.list.toString="
                     + data.getList().toString());
            //赛事列表
            List<ScoreMatchModel> matchList = data.getList();
            for (int i = 0; i < matchList.size(); i++) {
                log.info("*****************************score/myRankList********i:----" + i);
                log.info(
                    "*****************************score/myRankList********matchList.get(i).getMatchCode():----"
                         + matchList.get(i).getMatchCode());
                log.info(
                    "*****************************score/myRankList********register.getAccount():----"
                         + register.getAccount());
                List<Map<String, Object>> competitionMsgList = new ArrayList<>();
                List<CompetitionModel> competitionModels = scoreFacadeClient
                    .queryCompetitionByMatch(matchList.get(i).getMatchCode(),
                        register.getAccount());
                if (competitionModels == null) {
                    return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(),
                        "未能获取赛事下的比赛");
                }
                log.info(
                    "*****************************score/myRankList********competitionModels.size="
                         + competitionModels.size());
                for (int k = 0; k < competitionModels.size(); k++) {
                    log.info("*****************************score/myRankList********k:----" + k);
                    CompetitionModel competition = competitionModels.get(k);
                    log.info(
                        "*****************************score/myRankList********competition.getCompetitionCode():----"
                             + competition.toString());
                    List<ScoreModel> scoreList = scoreFacadeClient.getRegisterScores(
                        register.getAccount(), null, competition.getCompetitionCode());
                    ScoreModel score = scoreFacadeClient.getMyBestScore(register.getAccount(), null,
                        competition.getCompetitionCode());
                    if (scoreList == null) {
                        continue;
                    }
                    if (score == null) {
                        continue;
                    }
                    Map<String, Object> scoreMap = new HashMap<>();
                    scoreMap.put("groupName", score.getGroupName());
                    scoreMap.put("eventName", score.getEventName());
                    scoreMap.put("areaName", score.getSiteName());
                    scoreMap.put("competitionName", competition.getCompetitionName());
                    scoreMap.put("competitionCode", competition.getCompetitionCode());
                    Map<String, Object> competitionMap = new HashMap<>();
                    Date date = null;
                    if (scoreList.size() > 0) {
                        date = scoreList.get(scoreList.size() - 1).getPartTime();
                    }
                    String scoreDesc = getScoreDesc(score);
                    competitionMap.put("score", scoreDesc);
                    competitionMap.put("date", date);
                    competitionMap.put("competition", scoreMap);
                    competitionMsgList.add(competitionMap);
                }
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("comList", competitionMsgList);
                resultMap.put("match", matchList.get(i));
                resultList.add(resultMap);
            }

            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("data", resultList);
            hashMap.put("pageSize", data.getPageSize());
            hashMap.put("currentPage", data.getCurrentPage());
            hashMap.put("allRow", data.getAllRow());
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取参赛列表失败,请稍后重试！！！");
        }
    }

    /**
     * 获取所有赛事下比赛
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "competitionList", method = { RequestMethod.GET })
    public Map<String, Object> competitionList(@RequestParam(value = "matchCode", required = true) String matchCode) {

        log.info(
            "*****************************score/competitionList********matchCode=" + matchCode);

        if (StringUtil.isNullOrEmpty(matchCode)) {
            return JsonResultUtil.getServerErrorResult("matchCode为空！");
        }
        String num = "";
        try {
            List<CompetitionModel> competitionModels = scoreFacadeClient
                .queryCompetitionByMatch(matchCode, num);
            if (competitionModels == null) {
                return JsonResultUtil.getServerErrorResult("未获取赛事下的比赛");
            }
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("data", competitionModels);
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取比赛列表失败,请稍后重试！！！");
        }
    }

    /**
     * 获取比赛详细信息
     *
     * @param competitionCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getCompetitionInfo", method = { RequestMethod.GET })
    public Map<String, Object> getCompetitionInfo(@RequestParam(value = "competitionCode", required = true) String competitionCode) {
        try {
            CompetitionModel competitionModel = scoreFacadeClient
                .getCompetitionInfo(competitionCode);
            if (competitionModel == null) {
                return JsonResultUtil.getServerErrorResult("未获取比赛的详细信息");
            }
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("data", competitionModel);
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取比赛列表失败,请稍后重试！！！");
        }
    }

    /**
     * 获取用户某个比赛下所有成绩列表(我的成绩详情)
     *
     * @param competitionCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getRegisterScores", method = { RequestMethod.GET })
    public Map<String, Object> getRegisterScores(@RequestParam(value = "competitionCode", required = true) String competitionCode,
                                                 @RequestParam(value = "token", required = true) String token,
                                                 HttpSession session) {

        log.info("*****************************getRegisterScores********competitionCode="
                 + competitionCode);
        log.info("*****************************getRegisterScores********token=" + token);
        try {
            RegisterModel register = ucenterAdapterService.getAppAuthRegister(token);

            if (register == null) {
                AuthToken authToken = ucenterAdapterService.getH5AuthToken(session);
                if (authToken == null) {
                    return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(),
                        "用户未登录");
                }
                register = authToken.getRegister();
            }
            log.info(
                "*****************************getRegisterScores********register.getRegisterCode()="
                     + register.getRegisterCode());
            RegisterModel user = registerFacadeClient
                .getRegsiterByRegisterCode(register.getRegisterCode());
            if (user == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.SERVER_ERROR.getCode(), "用户未存在");
            }
            ScoreModel bestScore = scoreFacadeClient.getMyBestScore(user.getAccount(), null,
                competitionCode);
            List<ScoreModel> scoreModels = scoreFacadeClient.getRegisterScores(user.getAccount(),
                null, competitionCode);
            if (bestScore == null) {
                return JsonResultUtil.getServerErrorResult("未获取到用户最好的成绩");
            }
            bestScore.setScoreDesc(getScoreDesc(bestScore));
            if (scoreModels == null || scoreModels.size() == 0) {
                return JsonResultUtil.getServerErrorResult("未获取到用户该比赛的成绩");
            }
            for (ScoreModel score : scoreModels) {
                score.setCompetitionCode(competitionCode);
            }
            for (ScoreModel score : scoreModels) {
                score.setScoreDesc(getScoreDesc(score));
                if (StringUtils.isEmpty(score.getScoreDesc())
                    || StringUtils.isEmpty(bestScore.getScoreDesc())) {
                    continue;
                }
                if (score.getScoreDesc().equals(bestScore.getScoreDesc())) {
                    scoreModels.remove(score);
                    break;
                }
            }

            bestScore.setCompetitionCode(competitionCode);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("scoreList", ScoreConverter.converterScoreList(scoreModels));
            hashMap.put("bestScore", bestScore);
            log.info("**********getRegisterScores***************************头像转base64---"
                     + user.getHeadimgUrl());
            if (StringUtils.isNotEmpty(user.getHeadimgUrl())) {
                String headimg = FileUtils.getBase64(user.getHeadimgUrl());
                log.info("**********getRegisterScores***************************headimg.length()="
                         + headimg.length());
                hashMap.put("headimgUrl", "data:image/png;base64," + headimg);
            }
            hashMap.put("phone", user.getAccount());
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取用户该比赛的成绩列表失败,请稍后重试！！！");
        }
    }

    private String getScoreDesc(ScoreModel score) {
        if (score == null) {
            return "";
        }
        if (StringUtils.isNotEmpty(score.getScoreDesc())) {
            return score.getScoreDesc();
        }
        String scoreDesc = "";
        if (score.getScore() != null) {
            double doubleValue = score.getScore().doubleValue();
            long dev = (long) doubleValue;
            if (dev == doubleValue) {
                scoreDesc += dev;
            } else {
                scoreDesc += score.getScore().doubleValue();
            }

        }
        if (score.getScoreUnit() != null) {
            scoreDesc += score.getScoreUnit();
        }
        return scoreDesc;

    }

    /**
     * 获取该用户比赛下的最好成绩
     *
     * @param competitionCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getMyBestScore", method = { RequestMethod.GET })
    public Map<String, Object> getMyBestScore(@RequestParam(value = "competitionCode", required = true) String competitionCode,
                                              @RequestParam(value = "token", required = true) String token,
                                              HttpSession session) {
        log.info("*****************************score/getMyBestScore********competitionCode="
                 + competitionCode);
        log.info("*****************************score/getMyBestScore********token=" + token);
        try {
            RegisterModel register = ucenterAdapterService.getAppAuthRegister(token);

            if (register == null) {
                AuthToken authToken = ucenterAdapterService.getH5AuthToken(session);
                if (authToken == null) {
                    return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(),
                        "用户未登录");
                }
                register = authToken.getRegister();
            }
            ScoreModel scoreModel = scoreFacadeClient.getMyBestScore(register.getAccount(), null,
                competitionCode);
            if (scoreModel == null) {
                return JsonResultUtil.getServerErrorResult("未获取到用户该比赛的最好成绩");
            }
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("data", scoreModel);
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取用户该比赛的最好成绩失败,请稍后重试！！！");
        }
    }

    /**
     * 获取用户某一个成绩的详细信息
     *
     * @param competitionCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getMyScoreInfo", method = { RequestMethod.GET })
    public Map<String, Object> getMyScoreInfo(@RequestParam(value = "competitionCode", required = true) String competitionCode,
                                              @RequestParam(value = "scoreDesc", required = true) String scoreDesc,
                                              @RequestParam(value = "token", required = true) String token,
                                              HttpSession session) {
        log.info("*****************************score/getMyScoreInfo********competitionCode="
                 + competitionCode);
        log.info("*****************************score/getMyScoreInfo********scoreDesc=" + scoreDesc);
        log.info("*****************************score/getMyScoreInfo********token=" + token);
        try {
            RegisterModel register = ucenterAdapterService.getAppAuthRegister(token);

            if (register == null) {
                AuthToken authToken = ucenterAdapterService.getH5AuthToken(session);
                if (authToken == null) {
                    return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(),
                        "用户未登录");
                }
                register = authToken.getRegister();
            }
            List<ScoreModel> scoreModels = scoreFacadeClient
                .getRegisterScores(register.getAccount(), null, competitionCode);
            if (scoreModels == null) {
                return JsonResultUtil.getServerErrorResult("未获取到用户该比赛的成绩信息");
            }
            ScoreModel scoreModel = null;
            for (ScoreModel score : scoreModels) {
                if (score.getScoreDesc().equals(scoreDesc)) {
                    scoreModel = score;
                    break;
                }
            }
            if (scoreModel == null) {
                return JsonResultUtil.getServerErrorResult("未获取到用户该比赛的成绩信息");
            }
            return JsonResultUtil.getSuccessResult(ScoreConverter.getMyScoreInfo(scoreModel));
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取用户该比赛的最好成绩失败,请稍后重试！！！");
        }
    }
}
