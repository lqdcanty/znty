package com.efida.sports.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sport.admin.facade.model.SpProjectTypeModel;
import com.efida.sport.dmp.facade.model.CompetitionModel;
import com.efida.sport.dmp.facade.model.FieldInfoModel;
import com.efida.sport.dmp.facade.model.RankModel;
import com.efida.sport.dmp.facade.model.ScoreMatchModel;
import com.efida.sport.dmp.facade.model.ScoreModel;
import com.efida.sport.dmp.facade.result.DmpPageResult;
import com.efida.sports.app.converter.ScoreConverter;
import com.efida.sports.app.vo.CompetitionVo;
import com.efida.sports.app.vo.GameVo;
import com.efida.sports.enums.ErrorCodeEnum;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.dubbo.intergration.ScoreFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpProjectTypeFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterLoginFacadeClient;
import com.efida.sports.util.JsonResultUtil;
import com.hazelcast.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Created by wangyan on 2018/8/28.
 */
@RestController()
@Api(value = "scoreApi", tags = { "比赛成绩相关接口" })
@RequestMapping(value = "/api/score", produces = "application/json")
public class ScoreController {

    private static Logger             log = LoggerFactory.getLogger(ScoreController.class);

    @Autowired
    private ScoreFacadeClient         scoreFacadeClient;

    @Autowired
    private SpProjectTypeFacadeClient projectTypeFacadeClient;

    @Autowired
    private UcenterLoginFacadeClient  loginServiceClient;

    /**
     * 跳转更多项目
     */
    @ApiOperation(value = "获取项目信息列表", notes = "比赛成绩获取项目所有项目分类调用")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "types", method = { RequestMethod.GET })
    public Map<String, Object> more() {
        try {
            List<SpProjectTypeModel> projectTypes = projectTypeFacadeClient.getProjectTypes();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("types", GameVo.getVos(projectTypes));
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取项目分类失败");
        }
    }

    /**
     * 赛事搜索接口(有成绩)
     *
     * @param keyword
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "赛事搜索接口", notes = "通过关键字分页搜索赛事列表")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "keyword", value = "关键字", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "currentPage", value = "当前页数(不传默认取第一页)", required = false, dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "每页大小(不传默认为10)", required = false, dataType = "int", paramType = "query") })
    @RequestMapping(value = "match/search", method = { RequestMethod.GET })
    public Map<String, Object> searchMatchs(@RequestParam(value = "keyword", required = false) String keyword,
                                            @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        log.info("**************************************match/search      keyword=" + keyword);
        try {
            DmpPageResult<ScoreMatchModel> page = scoreFacadeClient
                .queryHasScoreMatchByName(keyword, currentPage, pageSize);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("totalRow", page.getAllRow());
            map.put("totalPage", page.getTotalPage());
            map.put("currentPage", page.getCurrentPage());
            map.put("pageSize", page.getPageSize());
            map.put("list", page.getList());
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("通过关键字分页搜索赛事列表失败");
        }
    }

    /**
     * 获取赛事列表
     *
     * @param code
     * @return
     */
    @ApiOperation(value = "获取赛事列表", notes = "根据项目分类编号，获取赛事列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "code", value = "分类编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "currentPage", value = "当前页数(不传默认取第一页)", required = false, dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "每页大小(不传默认为10)", required = false, dataType = "int", paramType = "query") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "matchs", method = { RequestMethod.GET })
    public Map<String, Object> matchs(@RequestParam(value = "code", required = true) String code,
                                      @RequestParam(value = "currentPage") int currentPage,
                                      @RequestParam(value = "pageSize") int pageSize) {
        log.info("**************************************matchs      code=" + code);
        log.info("**************************************matchs      currentPage=" + currentPage);
        log.info("**************************************matchs      pageSize=" + pageSize);
        try {
            DmpPageResult<ScoreMatchModel> pageMatch = null;
            if (StringUtil.isNullOrEmpty(code)) {
                return JsonResultUtil.getServerErrorResult("赛事code为空！");
            }
            if ("all".equals(code)) {
                pageMatch = scoreFacadeClient.queryHasScoreMatch(null, null, currentPage, pageSize);
            } else {
                pageMatch = scoreFacadeClient.queryHasScoreMatch(code, null, currentPage, pageSize);
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("matchs", pageMatch.getList());
            map.put("totalPage", pageMatch.getTotalPage());
            map.put("total", pageMatch.getAllRow());
            map.put("currentPage", pageMatch.getCurrentPage());
            map.put("pageSize", pageSize);
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("根据项目分类编号，获取赛事列表失败");
        }
    }

    /**
     * 获取赛场列表
     *
     * @param matchCode
     * @return
     */
    @ApiOperation(value = "获取赛场列表(废弃)", notes = "根据赛事编号，获取分赛场列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "matchCode", value = "赛事编号", required = true, dataType = "String", paramType = "query") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "match/sites", method = { RequestMethod.GET })
    public Map<String, Object> sites(@RequestParam(value = "matchCode", required = true) String matchCode) {
        log.info("**************************************match/sites      matchCode=" + matchCode);

        try {
            if (StringUtil.isNullOrEmpty(matchCode)) {
                return JsonResultUtil.getServerErrorResult("赛事matchCode为空！");
            }
            List<CompetitionVo> resultList = new ArrayList<>();
            List<CompetitionModel> competitionModels = scoreFacadeClient
                .queryCompetitionByMatch(matchCode, null);
            for (CompetitionModel competitionModel : competitionModels) {
                CompetitionModel competition = scoreFacadeClient
                    .getCompetitionInfo(competitionModel.getCompetitionCode());
                if (competition != null) {
                    Map<String, String> map = new HashMap<>();
                    map.put("area", competition.getRelationAreas().size() > 0
                        ? competition.getRelationAreas().get(0).getName() + "" : null);
                    map.put("event", competition.getRelationEvents().size() > 0
                        ? competition.getRelationEvents().get(0).getName() + "" : null);
                    map.put("group", competition.getRelationGroups().size() > 0
                        ? competition.getRelationGroups().get(0).getName() + "" : null);
                    map.put("competitionCode", competition.getCompetitionCode());
                    resultList.add(CompetitionVo.getCompetitionVo(map));
                }
            }
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("sites", resultList);
            return JsonResultUtil.getSuccessResult(resultMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取分赛场失败,请稍后重试！！！");
        }
    }

    /**
     * 获取赛场列表(有成绩)
     *
     * @param matchCode
     * @return
     */
    @ApiOperation(value = "获取有成绩的赛场列表", notes = "根据赛事编号，获取有成绩的分赛场列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "matchCode", value = "赛事编号", required = true, dataType = "String", paramType = "query") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ResponseBody
    @RequestMapping(value = "match/all/sites", method = { RequestMethod.GET })
    public Map<String, Object> getSites(@RequestParam(value = "matchCode", required = true) String matchCode) {
        log.info("**************************************match/sites      matchCode=" + matchCode);

        try {
            if (StringUtil.isNullOrEmpty(matchCode)) {
                return JsonResultUtil.getServerErrorResult("赛事matchCode为空！");
            }
            List<CompetitionModel> competitionModels = scoreFacadeClient
                .queryCompetitionByMatch(matchCode, null);
            List<FieldInfoModel> resultList = new ArrayList<>();
            for (CompetitionModel competitionModel : competitionModels) {
                FieldInfoModel fieldInfoModel = new FieldInfoModel();
                fieldInfoModel.setCompetitionCode(competitionModel.getCompetitionCode());
                fieldInfoModel.setArea(competitionModel.getCompetitionName());
                fieldInfoModel.setGroup("");
                resultList.add(fieldInfoModel);
            }
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("resultList", resultList);
            return JsonResultUtil.getSuccessResult(resultMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取分赛场失败,请稍后重试！！！");
        }
    }

    /**
     * 比赛成绩列表-排名
     *
     * @return
     */
    @ApiOperation(value = "获取比赛成绩列表", notes = "根据比赛编号，获取比赛成绩列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "competitionCode", value = "比赛编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "competitionDate", value = "比赛时间", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "currentPage", value = "当前页数(不传默认取第一页)", required = false, dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "每页大小(不传默认为10)", required = false, dataType = "int", paramType = "query") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
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
            List<RankModel> list = new ArrayList<>();
            long allRow = 0l;
            int totalPage = 0;
            DmpPageResult<RankModel> rank = scoreFacadeClient
                .queryRankByCompetition(competitionCode, null, null, currentPage, pageSize);
            if (rank != null) {
                list = rank.getList();
                allRow = rank.getAllRow();
                currentPage = rank.getCurrentPage();
                pageSize = rank.getPageSize();
                totalPage = rank.getTotalPage();
            }

            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("list", list);
            hashMap.put("currentPage", currentPage);
            hashMap.put("pageSize", pageSize);
            hashMap.put("allRow", allRow);
            hashMap.put("totalPage", totalPage);
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取比赛成绩列表失败");
        }
    }

    /**
     * 我的成绩-排名
     *
     * @return
     */
    @ApiOperation(value = "获取我的成绩", notes = "根据比赛编号，获取我的成绩")
    @ApiImplicitParams({ @ApiImplicitParam(name = "competitionCode", value = "比赛编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "competitionDate", value = "比赛时间", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "myRank", method = { RequestMethod.GET })
    public Map<String, Object> myRank(@RequestParam(value = "competitionCode", required = true) String competitionCode,
                                      @RequestParam(value = "competitionDate", required = false) String competitionDate,
                                      @RequestParam(value = "token", required = true) String token) {
        log.info("*****************************score/myRank********competitionCode="
                 + competitionCode + " token=" + token);

        if (StringUtil.isNullOrEmpty(competitionCode)) {
            return JsonResultUtil.getServerErrorResult("competitionCode为空！");
        }
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            if (StringUtils.isBlank(register.getAccount())) {
                hashMap.put("rank", null);
                return JsonResultUtil.getSuccessResult(hashMap);
            }
            List<RankModel> myRank = scoreFacadeClient.getMyRank(register.getAccount(), null,
                competitionCode);

            RankModel rankModel = null;
            if (myRank != null && myRank.size() > 0) {
                rankModel = myRank.get(0);
                rankModel.setPlayerHeaderImg(register.getHeadimgUrl());
            }
            hashMap.put("rank", rankModel);
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取我的成绩失败");
        }
    }

    /**
     * 我的成绩（all）
     *
     * @return
     */
    @ApiOperation(value = "获取我的成绩列表", notes = "获取我的成绩列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "currentPage", value = "当前页数(不传默认取第一页)", required = false, dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "每页大小(不传默认为10)", required = false, dataType = "int", paramType = "query") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "myRankList", method = { RequestMethod.GET })
    public Map<String, Object> myRankList(@RequestParam(value = "token", required = true) String token,
                                          @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

        log.info("*****************************score/myRankList********currentPage=" + currentPage
                 + " pageSize=" + pageSize);
        log.info("*****************************score/myRankList********token=" + token);
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            if (StringUtils.isBlank(register.getAccount())) {
                hashMap.put("list", new ArrayList<>());
                return JsonResultUtil.getSuccessResult(hashMap);
            }
            List<Map<String, Object>> resultList = new ArrayList<>();
            //赛事
            DmpPageResult<ScoreMatchModel> data = scoreFacadeClient.queryHasScoreMatch(null,
                register.getAccount(), currentPage, pageSize);
            if (data == null) {
                hashMap.put("list", resultList);
                hashMap.put("pageSize", pageSize);
                hashMap.put("currentPage", currentPage);
                hashMap.put("allRow", data.getAllRow());
                hashMap.put("totalPage", data.getTotalPage());
                return JsonResultUtil.getSuccessResult(hashMap);
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
                    competitionMap.put("score", score.getScoreDesc());
                    competitionMap.put("date", date);
                    competitionMap.put("competition", scoreMap);
                    competitionMsgList.add(competitionMap);
                }
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("comList", competitionMsgList);
                resultMap.put("match", matchList.get(i));
                resultList.add(resultMap);
            }
            hashMap.put("list", resultList);
            hashMap.put("pageSize", data.getPageSize());
            hashMap.put("currentPage", data.getCurrentPage());
            hashMap.put("allRow", data.getAllRow());
            hashMap.put("totalPage", data.getTotalPage());
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
    @ApiOperation(value = "获取比赛列表", notes = "根据赛事编号获取比赛列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "matchCode", value = "赛事编号", required = true, dataType = "String", paramType = "query") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "competitionList", method = { RequestMethod.GET })
    public Map<String, Object> competitionList(@RequestParam(value = "matchCode", required = true) String matchCode) {

        log.info(
            "*****************************score/competitionList********matchCode=" + matchCode);

        if (StringUtil.isNullOrEmpty(matchCode)) {
            return JsonResultUtil.getServerErrorResult("matchCode为空！");
        }
        try {
            List<CompetitionModel> competitionModels = scoreFacadeClient
                .queryCompetitionByMatch(matchCode, "");
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("list", competitionModels);
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
    @ApiOperation(value = "获取我的成绩详情", notes = "获取用户某个比赛下所有成绩列表(我的成绩详情)")
    @ApiImplicitParams({ @ApiImplicitParam(name = "competitionCode", value = "比赛编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "getRegisterScores", method = { RequestMethod.GET })
    public Map<String, Object> getRegisterScores(@RequestParam(value = "competitionCode", required = true) String competitionCode,
                                                 @RequestParam(value = "token", required = true) String token) {

        log.info("*****************************getRegisterScores********competitionCode="
                 + competitionCode);
        log.info("*****************************getRegisterScores********token=" + token);
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            if (StringUtils.isBlank(register.getAccount())) {
                hashMap.put("scoreList", new ArrayList<>());
                hashMap.put("bestScore", null);
                hashMap.put("headimgUrl", register.getHeadimgUrl());
                hashMap.put("phone", null);
                return JsonResultUtil.getSuccessResult(hashMap);
            }
            ScoreModel bestScore = scoreFacadeClient.getMyBestScore(register.getAccount(), null,
                competitionCode);
            List<ScoreModel> scoreModels = scoreFacadeClient
                .getRegisterScores(register.getAccount(), null, competitionCode);
            String scoreDesc = "";
            if (bestScore != null) {
                scoreDesc = bestScore.getScoreDesc();
                bestScore.setCompetitionCode(competitionCode);
            }
            hashMap.put("scoreList",
                ScoreConverter.converterScoreList(scoreModels, competitionCode, scoreDesc));
            hashMap.put("bestScore", bestScore);
            hashMap.put("headimgUrl", register.getHeadimgUrl());
            hashMap.put("phone", register.getAccount());
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取用户该比赛的成绩列表失败");
        }
    }

    /**
     * 获取用户某一个成绩的详细信息
     *
     * @param competitionCode
     * @return
     */
    @ApiOperation(value = "获取用户某一个成绩的详细信息", notes = "获取用户某一个成绩的详细信息")
    @ApiImplicitParams({ @ApiImplicitParam(name = "competitionCode", value = "比赛编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "scoreDesc", value = "比赛编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
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
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            if (StringUtils.isBlank(register.getAccount())) {
                hashMap.put("detail", new HashMap<String, Object>());
                return JsonResultUtil.getSuccessResult(hashMap);
            }
            List<ScoreModel> scoreModels = scoreFacadeClient
                .getRegisterScores(register.getAccount(), null, competitionCode);
            ScoreModel scoreModel = null;
            if (scoreModels != null) {
                for (ScoreModel score : scoreModels) {
                    if (score.getScoreDesc().equals(scoreDesc)) {
                        scoreModel = score;
                        break;
                    }
                }
            }

            hashMap.put("detail", ScoreConverter.getMyScoreInfo(scoreModel));
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取用户某一个成绩的详细信息失败");
        }
    }

}
