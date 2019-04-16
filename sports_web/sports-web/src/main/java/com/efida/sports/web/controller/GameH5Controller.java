package com.efida.sports.web.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.efida.sport.dmp.facade.model.CompetitionModel;
import com.efida.sport.dmp.facade.model.FieldInfoModel;
import com.efida.sport.dmp.facade.model.ScoreMatchModel;
import com.efida.sport.dmp.facade.result.DmpPageResult;
import com.efida.sports.service.dubbo.intergration.ScoreFacadeClient;
import com.hazelcast.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Version;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.efida.sport.admin.facade.model.SpMatchModel;
import com.efida.sport.admin.facade.model.SpProjectTypeModel;
import com.efida.sport.admin.facade.model.page.SportsAdminPageResult;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.dubbo.intergration.SpMatchFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpProjectTypeFacadeClient;
import com.efida.sports.util.JsonResultUtil;
import com.efida.sports.web.vo.GameVo;
import com.efida.sports.web.vo.MatchVo;

@RequestMapping("project")
@Controller
public class GameH5Controller {

    @Autowired
    private SpMatchFacadeClient       spMatchFacadeClient;

    @Autowired
    private SpProjectTypeFacadeClient projectTypeFacadeClient;

    @Autowired
    private ScoreFacadeClient scoreFacadeClient;

    private static Logger             log = LoggerFactory.getLogger(GameH5Controller.class);

    /**
     * 跳转更多项目
     *
     * @param model
     * @return
     */
    @RequestMapping("more")
    public String more(Model model, String scope) {
        log.info("**************************************/more");

        List<SpProjectTypeModel> projectTypes = null;
        try {
            projectTypes = projectTypeFacadeClient.getProjectTypes();
        } catch (Exception e) {
            log.error("", e);
        }
        model.addAttribute("games", GameVo.getVos(projectTypes));
        return "pages/more_project";
    }

    /**
     * 赛事搜索接口(有/无成绩)
     *
     * @param keyword
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "match/search", method = { RequestMethod.GET })
    public Map<String, Object> searchMatchList(@RequestParam(value = "keyword", required = false) String keyword,
                                               @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        log.info("**************************************match/search      keyword="+keyword);

        try {

            SportsAdminPageResult<SpMatchModel> page = spMatchFacadeClient.searchMatchs(keyword,
                currentPage, pageSize);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("totalRow", page.getAllRow());
            map.put("totalPage", page.getTotalPage());
            map.put("currentPage", page.getCurrentPage());
            map.put("pageSize", page.getPageSize());
            map.put("list", MatchVo.getVos(page.getList()));
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("搜索失败");
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
    @ResponseBody
    @RequestMapping(value = "search/matchs", method = { RequestMethod.GET })
    public Map<String, Object> searchMatchs(@RequestParam(value = "keyword", required = false) String keyword,
                                               @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        log.info("**************************************match/search      keyword="+keyword);

        try {

            DmpPageResult<ScoreMatchModel> page = scoreFacadeClient.queryHasScoreMatchByName(keyword,currentPage,pageSize);
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
            return JsonResultUtil.getServerErrorResult("搜索失败");
        }

    }

    /**
     * 获取项目列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "types", method = { RequestMethod.GET })
    public Map<String, Object> types() {
        log.info("************************获取项目列表***********************");

        try {
            List<SpProjectTypeModel> allTypes = projectTypeFacadeClient.getAllProjectTypes();
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("types", GameVo.getVos(allTypes));
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取项目分类失败,请稍后重试！！！");
        }
    }

    /**
     * 获取赛事列表
     *
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "matchs", method = { RequestMethod.GET })
    public Map<String, Object> matchs(@RequestParam(value = "code", required = true) String code,
                                      @RequestParam(value = "currentPage") String currentPage,
                                      @RequestParam(value = "pageSize") String pageSize) {

        log.info("**************************************matchs      code="+code);
        log.info("**************************************matchs      currentPage="+currentPage);
        log.info("**************************************matchs      pageSize="+pageSize);
        DmpPageResult<ScoreMatchModel> pageMatch = null;
        try {
            if(StringUtil.isNullOrEmpty(code)){
                return JsonResultUtil.getServerErrorResult("赛事code为空！");
            }
            if ("all".equals(code)) {
                pageMatch  = scoreFacadeClient.queryHasScoreMatch(null,null, Integer.valueOf(currentPage),
                    Integer.valueOf(pageSize));
            } else {
                pageMatch = scoreFacadeClient.queryHasScoreMatch(code,null, Integer.valueOf(currentPage),
                    Integer.valueOf(pageSize));
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("matchs", pageMatch.getList());
            map.put("totalPage", pageMatch.getTotalPage());
            map.put("total", pageMatch.getAllRow());
            map.put("currentPage", pageMatch.getCurrentPage());
            map.put("pageSize", pageSize);
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("赛事列表查询失败");
        }
    }
    /**
     * 获取赛场列表
     *
     * @param matchCode
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "match/sites", method = { RequestMethod.GET })
    public Map<String, Object> sites(@RequestParam(value = "matchCode", required = true) String matchCode) {
        log.info("**************************************match/sites      matchCode="+matchCode);

        try {
            if(StringUtil.isNullOrEmpty(matchCode)){
                return JsonResultUtil.getServerErrorResult("赛事matchCode为空！");
            }

            List< Map<String,Object>> resultList = new ArrayList<>();
            List<CompetitionModel> competitionModels= scoreFacadeClient.queryCompetitionByMatch(matchCode,null);
            for(CompetitionModel competitionModel:competitionModels){
                CompetitionModel competition = scoreFacadeClient.getCompetitionInfo(competitionModel.getCompetitionCode());
                if(competition != null){
                    Map<String,Object> map = new HashMap<>();
                    map.put("area",competition.getRelationAreas());
                    map.put("event",competition.getRelationEvents());
                    map.put("group",competition.getRelationGroups());
                    map.put("competitionCode",competition.getCompetitionCode());
                    resultList.add(map);
                }
            }
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("resultList",resultList);
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

    @ResponseBody
    @RequestMapping(value = "match/sites/all", method = { RequestMethod.GET })
    public Map<String, Object> sitesAll(@RequestParam(value = "matchCode", required = true) String matchCode) {
        log.info("**************************************match/sites      matchCode="+matchCode);

        try {
            if(StringUtil.isNullOrEmpty(matchCode)){
                return JsonResultUtil.getServerErrorResult("赛事matchCode为空！");
            }
            List<CompetitionModel> competitionModels= scoreFacadeClient.queryCompetitionByMatch(matchCode,null);
            List< List<FieldInfoModel> > list = new ArrayList<>();
            List<FieldInfoModel> resultList = new ArrayList<>();
            for(CompetitionModel competitionModel:competitionModels){
                FieldInfoModel fieldInfoModel = new FieldInfoModel();
                fieldInfoModel.setCompetitionCode(competitionModel.getCompetitionCode());
                fieldInfoModel.setEvent(competitionModel.getCompetitionName());
                resultList.add(fieldInfoModel);
            }
            list.add(resultList);
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("resultList",list);
            return JsonResultUtil.getSuccessResult(resultMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取分赛场失败,请稍后重试！！！");
        }
    }

}
