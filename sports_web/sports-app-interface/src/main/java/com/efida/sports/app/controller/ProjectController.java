/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.controller;

import com.alibaba.fastjson.JSON;
import com.efida.sport.admin.facade.model.*;
import com.efida.sport.admin.facade.model.page.SportsAdminPageResult;
import com.efida.sports.app.vo.*;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.dubbo.intergration.SpAthletesFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpMatchAreaFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpMatchFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpProjectTypeFacadeClient;
import com.efida.sports.service.impl.ApplyInfoServiceImpl;
import com.efida.sports.util.JsonResultUtil;
import com.efida.sports.util.logo.Logo;
import com.efida.sports.util.logo.LogoUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author zoutao
 * @version $Id: GameController.java, v 0.1 2018年6月12日 下午12:46:34 zoutao Exp $
 */
@RestController()
@Api(value = "projectApi", tags = { "比赛项目相关接口" })
@RequestMapping(value = "/api/project", produces = "application/json")
public class ProjectController {
    private static Logger             log = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private SpProjectTypeFacadeClient projectTypeFacadeClient;

    @Autowired
    private SpMatchFacadeClient       spMatchFacadeClient;
    @Autowired
    private SpAthletesFacadeClient    spAthletesFacadeClient;

    @Autowired
    private SpMatchAreaFacadeClient   spMatchAreaFacadeClient;

    @Autowired
    private ApplyInfoServiceImpl      applyInfoService;

    /**
     * 获取首页banner
     */
    @ApiOperation(value = "获取banner", notes = "获取banner")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "banner", method = { RequestMethod.GET })
    public Map<String, Object> banner() {
        try {
            List<String> list = new ArrayList<String>();
            list.add("http://static.appapi.zntyydh.com/banner_ph.jpg");
            //list.add("http://static.appapi.zntyydh.com/3881533001502_.pic_hd.jpg");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", list);
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取banner列表失败");
        }
    }

    /**
    * 获取赛事列表
    * 
    * @return
    */
    @ApiOperation(value = "获取项目信息列表", notes = "首页获取项目所有项目分类调用")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "types", method = { RequestMethod.GET })
    public Map<String, Object> types() {
        log.info("*******************获取项目信息列表***********************");
        try {
            List<SpProjectTypeModel> allTypes = projectTypeFacadeClient.getAllProjectTypes();
            List<Logo> logos= LogoUtil.getCompanyList();
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("types", GameVo.getVos(allTypes));
            hashMap.put("logos", logos);
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
    @ApiOperation(value = "获取赛事列表", notes = "根据项目分类编号，获取赛事列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "code", value = "分类编号", required = true, dataType = "String", paramType = "query") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "matchs", method = { RequestMethod.GET })
    public Map<String, Object> matchs(@RequestParam(value = "code", required = true) String code) {
        log.info("******************************************/matchs   code="+code);
        try {
            List<SpMatchModel> matchModes = spMatchFacadeClient.getMatchs(code);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("matchs", MatchVo.getVos(matchModes));
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取赛事列表失败,请稍后重试！！！");
        }
    }

    /**
     * 获取赛事详情
     * 
     * @param matchCode
     * @return
     */
    @ApiOperation(value = "获取赛事详情", notes = "根据赛事编号，获取赛事详情")
    @ApiImplicitParams({ @ApiImplicitParam(name = "matchCode", value = "赛事编号", required = true, dataType = "String", paramType = "query") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "match/detail", method = { RequestMethod.GET })
    public Map<String, Object> detail(@RequestParam(value = "matchCode", required = true) String matchCode) {

        log.info("******************************************match/detail   matchCode="+matchCode);

        try {
            SpMatchInfoModel matchDetail = spMatchFacadeClient.getMatchDetail(matchCode);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("match", MatchDetailVo.getVo(matchDetail));
            //hashMap.put("shareUrl", "http://zntywx.efida.com.cn/game/detail/"+matchCode);
            hashMap.put("shareUrl", "http://wx.zntyydh.com/game/detail/"+matchCode);
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取赛事详情失败,请稍后重试！！！");
        }
    }

    /**
     * 获取赛场列表
     * 
     * @param matchCode
     * @return
     */
    @ApiOperation(value = "获取赛场列表", notes = "根据赛事编号，获取分赛场列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "matchCode", value = "赛事编号", required = true, dataType = "String", paramType = "query") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "match/sites", method = { RequestMethod.GET })
    public Map<String, Object> sites(@RequestParam(value = "matchCode", required = true) String matchCode) {

        log.info("******************************************match/sites   matchCode="+matchCode);
        try {
            List<SpPlayingAreaModel> models = spMatchFacadeClient.getSpPlayingAreas(matchCode);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("sites", SiteVo.getVos(models));
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取分赛场失败,请稍后重试！！！");
        }
    }

    /**
     * 获取比赛分组信息和比赛项信息
     * 
     * @return
     */
    @ApiOperation(value = "获取比赛组和比赛项数据", notes = "根据赛场编号，获取比赛组和比赛项数据")
    @ApiImplicitParams({ @ApiImplicitParam(name = "siteCode", value = "赛场编号", required = true, dataType = "String", paramType = "query") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "events", method = { RequestMethod.GET })
    public Map<String, Object> getGroupsAndEvents(@RequestParam(value = "siteCode", required = true) String siteCode) {

        log.info("******************************************events   siteCode="+siteCode);
        try {
            List<MatchGroupVo> groups = new ArrayList<MatchGroupVo>();
            List<SpMatchGroupAndItemInfoModel> items = spMatchFacadeClient
                .getMatchTypeAndGroupInfo(siteCode);
            Map<String, Object> retMap = new HashMap<String, Object>();
            if (items == null || items.size() < 1) {
                retMap.put("groups", groups);
                return JsonResultUtil.getSuccessResult(retMap);
            }
            for (SpMatchGroupAndItemInfoModel model : items) {
                List<SpGroupItemModel> groupItems = model.getGroupItems();
                if (!model.getIsHasGroup() && (groupItems == null || groupItems.size() < 1)) {
                    continue;
                }
                MatchGroupVo vo = MatchGroupVo.getVo(model);
                List<EventVo> events = new ArrayList<EventVo>();
                for (SpGroupItemModel item : groupItems) {
                    Long total = applyInfoService.getApplyCountByEventCode(item.getItemCode());
                    EventVo event = EventVo.getVo(item, total);
                    events.add(event);
                }
                vo.setEvents(events);
                groups.add(vo);
            }
            retMap.put("groups", groups);
            return JsonResultUtil.getSuccessResult(retMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("未获取到比赛项数据，请稍后重试！！！");
        }
    }

    /**
     * 获取运动员属性接口
     * 
     * @param matchCode
     * @return
     */
    @ApiOperation(value = "获取填写运动员属性接口", notes = "根据赛事编号获取某个赛事下运动员报名需要填写信息")
    @ApiImplicitParams({ @ApiImplicitParam(name = "matchCode", value = "赛事编号", required = true, dataType = "String", paramType = "query") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "player/property", method = { RequestMethod.GET })
    public Map<String, Object> getPlayerProperty(@RequestParam(value = "matchCode", required = true) String matchCode) {

        log.info("******************************************player/property   matchCode="+matchCode);
        try {
            SpAthletesEnrollModel athietes = spAthletesFacadeClient.getAthietes(matchCode);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("properties", PlayerPropertyVo.getVos(athietes));
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取运动员属性失败,请稍后重试");
        }
    }

    /**
     * 赛事搜索接口
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
    public Map<String, Object> searchMatchList(@RequestParam(value = "keyword", required = false) String keyword,
                                               @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

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
     * 
     * 获取省市区接口
     * @param matchCode
     * @param areaType
     * @param parentArea
     * @return
     */
    @ApiOperation(value = "获取省市区接口", notes = "通过赛事编号获取省市区数据")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "matchCode", value = "赛事编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "areaType", value = "节点类型(province:省,city:市,area:区)", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "parentArea", value = "父节点名称(取省传null,取市传省名称,取区传市名称)", required = false, dataType = "String", paramType = "query") })
    @RequestMapping(value = "match/area", method = { RequestMethod.GET })
    public Map<String, Object> matchArea(@RequestParam(value = "matchCode", required = true) String matchCode,
                                         @RequestParam(value = "areaType", required = true) String areaType,
                                         @RequestParam(value = "parentArea", required = false) String parentArea) {

        try {

            ArrayList<String> list = new ArrayList<String>();
            List<MatchAreaModel> matchAreas = spMatchAreaFacadeClient.getMatchAreas(matchCode,
                areaType, parentArea);
            if (matchAreas != null && matchAreas.size() > 0) {
                for (MatchAreaModel matchAreaModel : matchAreas) {
                    list.add(matchAreaModel.getAreaName());
                }
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", list);
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取区域列表失败");
        }
    }

    /**
     * 
     * 根据赛事编号和区域名称获取赛场信息
     * @param matchCode
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "根据赛事编号和区域名称获取赛场信息", notes = "根据赛事编号和区域名称获取赛场信息")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "matchCode", value = "赛事编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "areaType", value = "区域类型类型(province:省,city:市,area:区)", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "areaName", value = "区域名称", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "lon", value = "经度", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "lat", value = "纬度", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "currentPage", value = "当前页数", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "每页大小", required = false, dataType = "String", paramType = "query") })
    @RequestMapping(value = "pages/match/sites", method = { RequestMethod.GET })
    public Map<String, Object> matchSites(@RequestParam(value = "matchCode", required = true) String matchCode,
                                          @RequestParam(value = "areaType", required = false) String areaType,
                                          @RequestParam(value = "areaName", required = false) String areaName,
                                          @RequestParam(value = "lon", required = false) String lon,
                                          @RequestParam(value = "lat", required = false) String lat,
                                          @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

        log.info("******************************************pages/match/sites   matchCode="+matchCode);

        try {

            BigDecimal lonBig = null;
            BigDecimal latBig = null;
            if (StringUtils.isNotBlank(lon) && lon.indexOf("null") < 1) {
                lonBig = new BigDecimal(lon);
            }
            if (StringUtils.isNotBlank(lat) && lat.indexOf("null") < 1) {
                latBig = new BigDecimal(lat);
            }
            if (StringUtils.isNotBlank(areaName)) {
                if (areaName.indexOf("null") > 0 || "全部".equals(areaName))
                    areaName = null;
            }
            SportsAdminPageResult<SpPlayingAreaModel> page = spMatchAreaFacadeClient.getMatchSites(
                matchCode, areaType, areaName, lonBig, latBig, currentPage, pageSize);
            log.info("获取赛事下赛场信息，返回结果：{}", JSON.toJSONString(page));

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("totalRow", page.getAllRow());
            map.put("totalPage", page.getTotalPage());
            map.put("currentPage", page.getCurrentPage());
            map.put("pageSize", page.getPageSize());
            map.put("sites", SiteVo.getVos(page.getList()));
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取赛事场地失败");
        }
    }

    /**
     *  赛事搜索接口
     *
     * @param gameCode
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取赛事列表接口", notes = "通过项目编号分页获取赛事列表")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "gameCode", value = "分类编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "currentPage", value = "当前页数(不传默认取第一页)", required = false, dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "每页大小(不传默认为10)", required = false, dataType = "int", paramType = "query") })
    @RequestMapping(value = "pages/match", method = { RequestMethod.GET })
    public Map<String, Object> getOrderList(@RequestParam(value = "gameCode", required = true) String gameCode,
                                            @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

        log.info("******************************************pages/match   gameCode="+gameCode);

        try {
            SportsAdminPageResult<SpMatchModel> page = spMatchFacadeClient.getPageMatchs(gameCode,
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
            return JsonResultUtil.getServerErrorResult("获取赛事列表失败");
        }

    }

}
