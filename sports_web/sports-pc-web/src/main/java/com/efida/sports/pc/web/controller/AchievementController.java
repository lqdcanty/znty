package com.efida.sports.pc.web.controller;

import java.net.URLEncoder;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sport.admin.facade.model.SpMatchInfoModel;
import com.efida.sport.admin.facade.model.SpMatchModel;
import com.efida.sport.admin.facade.model.SpPlayingAreaModel;
import com.efida.sport.admin.facade.model.SpProjectTypeModel;
import com.efida.sport.dmp.facade.model.CompetitionModel;
import com.efida.sport.dmp.facade.model.RankModel;
import com.efida.sport.dmp.facade.model.ScoreGridData;
import com.efida.sport.dmp.facade.model.ScoreMatchModel;
import com.efida.sport.dmp.facade.model.ScoreModel;
import com.efida.sport.dmp.facade.result.DmpPageResult;
import com.efida.sports.enums.ErrorCodeEnum;
import com.efida.sports.pc.web.vo.AchievementListVo;
import com.efida.sports.pc.web.vo.AchievementMatchListVo;
import com.efida.sports.pc.web.vo.AchievementVo;
import com.efida.sports.pc.web.vo.MatchCorrelationVo;
import com.efida.sports.pc.web.vo.MatchRelevantVo;
import com.efida.sports.pc.web.vo.ScoreMatchModelVo;
import com.efida.sports.pc.web.vo.ScoreVo;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.service.dubbo.intergration.ScoreFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpMatchFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpProjectTypeFacadeClient;
import com.efida.sports.util.DateUtil;
import com.efida.sports.util.JsonResultUtil;

/**
 * 成绩查询接口
 * 
 * @author zengbo
 * @version $Id: AchievementController.java, v 0.1 2018年7月28日 上午10:58:17 zengbo Exp $
 */
@Controller
@RequestMapping("achievement")
public class AchievementController {

    private static Logger             log = LoggerFactory.getLogger(AchievementController.class);

    @Autowired
    private SpMatchFacadeClient       spMatchFacadeClient;

    @Autowired
    private ScoreFacadeClient         scoreFacadeClient;

    @Autowired
    private UcenterAdapterService     ucenterAdapterService;

    @Autowired
    private SpProjectTypeFacadeClient projectTypeFacadeClient;

    @Value("${ucenter-domain}")
    public String                     ucenterDomain;
    @Value("${apply-domain}")
    public String                     applyDomain;

    /**
     * 成绩赛事列表初始化
     * 
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "match/init", produces = "application/json; charset=utf-8")
    public String matchInit(HttpSession session, Model model) {
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            RegisterModel register = null;
            if (authToken != null) {
                register = authToken.getRegister();
            }
            model.addAttribute("register", register);
            //            if (register == null) {
            //                model.addAttribute("url", "/achievement/match/init");
            //                return "pages/login";
            //            }
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
        } catch (Exception e) {
            log.error("", e);
        }
        return "pages/achievement_match_list";
    }

    /**
     * 賽事成绩列表查詢
     * 
     * @param gameCode
     * @param currentPage
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping(value = "match/page", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map<String, Object> matchQueryPage(@RequestParam(value = "gameCode") String gameCode,
                                              @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                              HttpSession session) {
        RegisterModel register = null;
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            if (authToken != null) {
                register = authToken.getRegister();
            }
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            if ("all".equals(gameCode)) {
                gameCode = "";
            }
            DmpPageResult<ScoreMatchModel> result = scoreFacadeClient.queryHasScoreMatch(gameCode,
                null, currentPage, pageSize);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("register", register);
            map.put("list", ScoreMatchModelVo.getScoreMatchModelVoList(result.getList()));
            map.put("total", result.getAllRow());
            map.put("totalPage", result.getTotalPage());
            map.put("curentPage", result.getCurrentPage());
            map.put("pageSize", result.getPageSize());
            return JsonResultUtil.getSuccessResult("成绩赛事列表查询成功", map);
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("成绩赛事列表查询失败");
        }
    }

    /**
     * 我的成绩查询列表初始化
     * 
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "init/page", produces = "application/json; charset=utf-8")
    public String orderQuery(HttpSession session, Model model) {
        RegisterModel register = null;
        try {

            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            String uri = applyDomain + "/achievement/init/page";
            uri = URLEncoder.encode(uri, "UTF-8");
            if (authToken == null) {
                return "redirect:" + ucenterDomain + "?login_redirect=" + uri;
            }
            register = authToken.getRegister();
        } catch (Exception e) {
            log.error("", e);
        }
        model.addAttribute("register", register);
        return "pages/achievement_list";
    }

    /**
     * 我的賽事成绩列表查詢
     * 
     * @param orderStatus
     * @param currentPage
     * @param pageSize
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "query/page", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map<String, Object> achievementQueryPage(@RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                    HttpSession session) {
        List<AchievementVo> achievementVoList = new ArrayList<AchievementVo>();
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            RegisterModel register = authToken.getRegister();
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            DmpPageResult<ScoreMatchModel> result = scoreFacadeClient.queryHasScoreMatch(null,
                register.getAccount(), currentPage, pageSize);
            List<ScoreMatchModel> list = result.getList();
            if (list != null && list.size() > 0) {
                for (ScoreMatchModel match : list) {
                    AchievementVo achievementVo = AchievementVo.getAchievementVo(match);
                    List<CompetitionModel> competions = scoreFacadeClient
                        .queryCompetitionByMatch(match.getMatchCode(), register.getAccount());
                    for (CompetitionModel model : competions) {
                        List<RankModel> ranks = scoreFacadeClient.getMyRank(register.getAccount(),
                            null, model.getCompetitionCode());
                        List<AchievementListVo> ranklist = AchievementListVo
                            .getAchievementList(ranks);
                        for (AchievementListVo vo : ranklist) {
                            vo.setCompetitionName(model.getCompetitionName());
                        }
                        achievementVo.setAchievements(ranklist);
                    }
                    achievementVoList.add(achievementVo);
                }
            }
            Map<String, Object> map = new HashMap<String, Object>();
            //            map.put("register", register);
            map.put("achievements", achievementVoList);
            map.put("total", result.getAllRow());
            map.put("totalPage", result.getTotalPage());
            map.put("curentPage", result.getCurrentPage());
            map.put("pageSize", result.getPageSize());
            return JsonResultUtil.getSuccessResult("赛事成绩查询成功", map);
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("赛事成绩查询失败");
        }
    }

    /**
     * 成绩详情初始化
     * 
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "detail/init", produces = "application/json; charset=utf-8")
    public String detailInit(@RequestParam(value = "matchCode", required = true) String matchCode,
                             @RequestParam(value = "competitionCode", required = false) String competitionCode,
                             HttpSession session, Model model) {
        RegisterModel register = null;
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            String uri = applyDomain + "/achievement/detail/init?matchCode=" + matchCode
                         + "&competitionCode=" + matchCode;
            uri = URLEncoder.encode(uri, "UTF-8");
            if (authToken == null) {
                return "redirect:" + ucenterDomain + "?login_redirect=" + uri;
            }
            register = authToken.getRegister();
        } catch (Exception e) {
            log.error("", e);
        }
        model.addAttribute("register", register);
        model.addAttribute("matchCode", matchCode);
        model.addAttribute("competitionCode", competitionCode);
        return "pages/achievement_detail";
    }

    /**
     * 查询赛事成绩信息
     * 
     * @param matchCode
     * @param session
     * @return
     */
    @RequestMapping(value = "match/detail", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map<String, Object> matchAchievementDetail(@RequestParam(value = "matchCode", required = true) String matchCode,
                                                      @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                                      HttpSession session) {
        RegisterModel register = null;
        SpMatchInfoModel matchDetail = null;
        List<SpPlayingAreaModel> areas = null;
        List<SpMatchModel> models = null;
        List<AchievementMatchListVo> volist = null;
        List<CompetitionModel> competions = null;
        List<String> dates = null;
        List<AchievementMatchListVo> matchCompetions = new ArrayList<AchievementMatchListVo>();
        Map<String, Object> map = new HashMap<String, Object>();
        String gameName = "";
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            matchDetail = spMatchFacadeClient.getMatchDetail(matchCode);
            areas = spMatchFacadeClient.getPlayingAreas(matchCode);
            String gameCode = matchDetail.getGameCode();
            models = spMatchFacadeClient.getMatchs(gameCode);
            //获取项目名称
            SpProjectTypeModel type = projectTypeFacadeClient.getSpProjectByCode(gameCode);
            if (type != null) {
                gameName = type.getGameName();
            }
            //查询成绩信息
            SpMatchModel model = spMatchFacadeClient.getEnableSpMatch(matchCode);
            AchievementVo achievement = AchievementVo.getAchievementVo(model);

            competions = scoreFacadeClient.queryCompetitionByMatch(matchCode, null);
            volist = AchievementMatchListVo.getScoreMatchModelListVo(competions);

            for (AchievementMatchListVo vo : volist) {
                //比赛项
                AchievementMatchListVo matchCompetion = new AchievementMatchListVo();
                matchCompetion.setCompetitionCode(vo.getCompetitionCode());
                matchCompetion.setCompetitionName(vo.getCompetitionName());
                matchCompetions.add(matchCompetion);
                DmpPageResult<RankModel> dmpRank = scoreFacadeClient.queryRankByCompetition(
                    vo.getCompetitionCode(), null, null, currentPage, pageSize);

                if (currentPage == 1 && authToken != null) {
                    register = authToken.getRegister();
                    List<RankModel> myRanks = scoreFacadeClient.getMyRank(register.getAccount(),
                        null, vo.getCompetitionCode());
                    vo.setMyAchievements(AchievementListVo.getAchievementList(myRanks));
                }
                List<RankModel> ranks = dmpRank.getList();
                List<AchievementListVo> ranklist = AchievementListVo.getAchievementList(ranks);
                vo.setAchievements(ranklist);
                vo.setTotal(dmpRank.getAllRow());
                vo.setPages(dmpRank.getTotalPage());
                vo.setCurrent(dmpRank.getCurrentPage());

            }
            achievement.setMatchList(matchCompetions);
            //赛事信息
            map.put("relevant", MatchRelevantVo.getMatchRelevantVo(matchDetail, areas));
            //相关赛事
            map.put("correlations", MatchCorrelationVo.getMatchCorrelationVoList(models));
            //賽事比賽項信息
            map.put("achievement", achievement);
            //比赛项及成绩信息
            map.put("competions", volist);
            map.put("gameName", gameName);
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("查询失败");
        }
    }

    @RequestMapping(value = "competition/ranks", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map<String, Object> competition(@RequestParam(value = "competitionCode", required = true) String competition,
                                           @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                           HttpSession session) {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            DmpPageResult<RankModel> dmpRank = scoreFacadeClient.queryRankByCompetition(competition,
                null, null, currentPage, pageSize);
            List<RankModel> ranks = dmpRank.getList();
            List<AchievementListVo> ranklist = AchievementListVo.getAchievementList(ranks);
            //比赛项及成绩信息
            map.put("achievements", ranklist);
            map.put("total", dmpRank.getAllRow());
            map.put("pages", dmpRank.getTotalPage());
            map.put("current", dmpRank.getCurrentPage());
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("查询失败");
        }
    }

    /**
     * 查询赛事相关信息
     * 
     * @param matchCode
     * @param session
     * @return
     */
    @RequestMapping(value = "match/correlation", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map<String, Object> matchCorrelation(@RequestParam(value = "matchCode", required = true) String matchCode,
                                                HttpSession session) {
        RegisterModel register = null;
        SpMatchInfoModel matchDetail = null;
        List<SpPlayingAreaModel> areas = null;
        List<SpMatchModel> models = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            matchDetail = spMatchFacadeClient.getMatchDetail(matchCode);
            register = ucenterAdapterService.getPCAuthToken(session).getRegister();
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            areas = spMatchFacadeClient.getSpPlayingAreas(matchCode);
            String gameCode = matchDetail.getGameCode();
            models = spMatchFacadeClient.getMatchs(gameCode);
            map.put("register", register);
            map.put("relevant", MatchRelevantVo.getMatchRelevantVo(matchDetail, areas));
            map.put("correlations", MatchCorrelationVo.getMatchCorrelationVoList(models));
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("查询失败");
        }
    }

    /**
     * 根据日期查询成绩
     * 
     * @param competitionCode
     * @param date
     * @param session
     * @return
     */
    @RequestMapping(value = "condition/query", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map<String, Object> queryCorrelationDetail(@PathVariable(value = "competitionCode", required = false) String competitionCode,
                                                      @PathVariable(value = "dateStr", required = false) String dateStr,
                                                      @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                      HttpSession session) {
        RegisterModel register = null;
        try {
            register = ucenterAdapterService.getPCAuthToken(session).getRegister();
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登陆");
            }
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("register", register);
            Date date = DateUtil.strToDate1(dateStr);
            DmpPageResult<RankModel> dmpresult = scoreFacadeClient
                .queryRankByCompetition(competitionCode, date, null, currentPage, pageSize);
            List<RankModel> ranks = dmpresult.getList();
            List<AchievementListVo> ranklist = AchievementListVo.getAchievementList(ranks);
            map.put("achievement", ranklist);
            return JsonResultUtil.getSuccessResult("成绩列表查询成功", map);
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("成绩列表查询失败");
        }
    }

    /**
     * 根据日期查询成绩
     * 
     * @param competitionCode
     * @param date
     * @param session
     * @return
     */
    @RequestMapping(value = "my/scores", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map<String, Object> queryMyScores(@RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                             HttpSession session) {

        RegisterModel register = null;
        List<AchievementVo> achievementVoList = new ArrayList<AchievementVo>();
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            register = authToken.getRegister();
            DmpPageResult<ScoreMatchModel> result = scoreFacadeClient.queryHasScoreMatch(null,
                register.getAccount(), currentPage, pageSize);

            List<ScoreMatchModel> list = result.getList();
            if (list != null && list.size() > 0) {
                for (ScoreMatchModel match : list) {
                    AchievementVo achievementVo = AchievementVo.getAchievementVo(match);
                    List<CompetitionModel> competions = scoreFacadeClient
                        .queryCompetitionByMatch(match.getMatchCode(), register.getAccount());
                    List<ScoreModel> scores = new ArrayList<ScoreModel>();
                    for (CompetitionModel model : competions) {
                        List<ScoreModel> registerScores = scoreFacadeClient
                            .getRegisterScoresWithRank(register.getAccount(), null,
                                model.getCompetitionCode());
                        if (registerScores != null && registerScores.size() > 0) {
                            scores.addAll(registerScores);
                        }
                    }
                    achievementVo.setScores(ScoreVo.getVos(scores));
                    achievementVoList.add(achievementVo);
                }
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("achievements", achievementVoList);
            map.put("total", result.getAllRow());
            map.put("totalPage", result.getTotalPage());
            map.put("curentPage", result.getCurrentPage());
            map.put("pageSize", result.getPageSize());
            return JsonResultUtil.getSuccessResult("赛事成绩查询成功", map);
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("赛事成绩查询失败");
        }

    }

    /**
     * 获取动态属性
     * 
     * @param competitionCode 比赛编号
     * @param session
     * @return
     */
    @RequestMapping(value = "extPros", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map<String, Object> achievementDetail(@RequestParam(value = "competitionCode", required = true) String competitionCode,
                                                 @RequestParam(value = "rankCode", required = true) String rankCode,
                                                 HttpSession session) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ScoreGridData scoreDetail = scoreFacadeClient.getScoreDetail(rankCode, competitionCode);
            map.put("scoreDetail", scoreDetail);
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("查询失败");
        }
    }

    public List<String> dateConverStr(List<Date> dates) {
        List<String> dateStr = new ArrayList<String>();
        if (dates != null && dates.size() > 0) {
            for (Date date : dates) {
                String str = DateUtil.formatWeb(date);
                dateStr.add(str);
            }
        }
        return dateStr;
    }

}
