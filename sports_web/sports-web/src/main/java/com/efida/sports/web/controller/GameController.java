/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sport.admin.facade.model.MatchAreaModel;
import com.efida.sport.admin.facade.model.SpAthletesEnrollModel;
import com.efida.sport.admin.facade.model.SpGroupItemModel;
import com.efida.sport.admin.facade.model.SpMatchGroupAndItemInfoModel;
import com.efida.sport.admin.facade.model.SpMatchInfoModel;
import com.efida.sport.admin.facade.model.SpMatchModel;
import com.efida.sport.admin.facade.model.SpPlayingAreaModel;
import com.efida.sport.admin.facade.model.SpProjectTypeModel;
import com.efida.sport.admin.facade.model.page.SportsAdminPageResult;
import com.efida.sport.facade.enums.RegTerminalEnum;
import com.efida.sports.config.WeichatConfig;
import com.efida.sports.constants.Constants;
import com.efida.sports.entity.PayOrder;
import com.efida.sports.enums.EventTypeEnums;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.IApplyInfoService;
import com.efida.sports.service.IPayOrderService;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.service.dubbo.intergration.SpAthletesFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpMatchAreaFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpMatchFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpProjectTypeFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;
import com.efida.sports.util.CommonUtil;
import com.efida.sports.util.JsonResultUtil;
import com.efida.sports.util.ServletUtil;
import com.efida.sports.util.logo.LogoUtil;
import com.efida.sports.util.weichat.WeichatUtil;
import com.efida.sports.web.vo.EventVo;
import com.efida.sports.web.vo.GameVo;
import com.efida.sports.web.vo.MatchDetailVo;
import com.efida.sports.web.vo.MatchGroupVo;
import com.efida.sports.web.vo.MatchVo;
import com.efida.sports.web.vo.PayOrderVo;
import com.efida.sports.web.vo.PlayerPropertyVo;
import com.efida.sports.web.vo.SiteVo;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author zoutao
 * @version $Id: GameController.java, v 0.1 2018年5月22日 下午3:14:01 zoutao Exp $
 */
@RequestMapping("game")
@Controller
public class GameController {

    @Autowired
    private SpProjectTypeFacadeClient   projectTypeFacadeClient;
    @Autowired
    private SpMatchFacadeClient         spMatchFacadeClient;

    @Autowired
    private IApplyInfoService           applyInfoService;
    @Autowired
    private WeichatConfig               weichatConfig;
    @Autowired
    private CacheService                cacheService;
    @Autowired
    private SpMatchAreaFacadeClient     spMatchAreaFacadeClient;

    @Autowired
    private SpAthletesFacadeClient      spAthletesFacadeClient;
    @Autowired
    private IPayOrderService            payOrderService;

    @Value("${ucenter-domain}")
    public String                       ucenterDomain;
    @Value("${apply-domain}")
    public String                       applyDomain;
    @Autowired
    private UcenterAdapterService       ucenterAdapterService;
    @Autowired
    private UcenterRegisterFacadeClient registerFacadeClient;

    private static Logger               log = LoggerFactory.getLogger(GameController.class);

    /**
     * 跳转到赛事分类页面
     * 
     * @param model
     * @return
     */
    @RequestMapping("type")
    public String types(Model model, String scope, HttpSession session) {
        List<SpProjectTypeModel> projectTypes = null;
        try {
            projectTypes = projectTypeFacadeClient.getProjectTypes();
        } catch (Exception e) {
            log.error("", e);
        }
        model.addAttribute("games", GameVo.getVos(projectTypes));
        model.addAttribute("logos", LogoUtil.getCompanyList());
        return "pages/index";
    }

    /**
     * 获取到赛事列表
     * 
     * @param code
     * @param model
     * @return
     */
    @RequestMapping("match/{code}")
    public String matchs(@PathVariable(value = "code") String code, Model model) {

        List<SpMatchModel> matchModes = null;
        try {
            matchModes = spMatchFacadeClient.getMatchs(code);
        } catch (Exception e) {
            log.error("", e);
        }
        model.addAttribute("matchs", MatchVo.getVos(matchModes));
        return "pages/match_list";

    }

    /**
     * 跳转到赛事详情页面
     * 
     * @param model
     * @return
     */
    @RequestMapping("detail/{code}")
    public String detail(@PathVariable(value = "code") String code, Model model) {

        log.info("===================================game/detail===code=" + code);
        SpMatchInfoModel matchDetail = null;

        try {
            matchDetail = spMatchFacadeClient.getMatchDetail(code);
        } catch (Exception e) {
            log.error("", e);
        }
        model.addAttribute("match", MatchDetailVo.getVo(matchDetail));
        return "pages/match_introduce";
    }

    /**
     * 跳转到填写运动员信息页面
     * 
     * @param code
     * @param model
     * @return
     * @throws UnsupportedEncodingException 
     */
    @SuppressWarnings({ "deprecation", "deprecation" })
    @RequestMapping("player")
    public String palyer(String matchCode, String siteCode, String itemIds, Model model,
                         HttpSession session,
                         HttpServletRequest request) throws UnsupportedEncodingException {
        AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
        String uri = applyDomain + "/game/player?itemIds=" + itemIds + "&matchCode=" + matchCode
                     + "&siteCode=" + siteCode;
        uri = URLEncoder.encode(uri, "UTF-8");
        if (auth == null) {
            return "redirect:" + ucenterDomain + "?login_redirect=" + uri;
        }
        RegisterModel register = registerFacadeClient
            .getRegsiterByRegisterCode(auth.getRegisterCode());
        if (StringUtils.isBlank(register.getAccount())) {
            return "redirect:" + ucenterDomain + "/user/h5/phone?binding_redirect=" + uri;
        }
        String ip = ServletUtil.getIpAddr(request);
        String referer = ServletUtil.getReferer(request);
        List<String> ids = new ArrayList<String>();
        if (itemIds.indexOf(",") > 0) {
            String[] split = itemIds.split(",");
            if (split != null && split.length > 0) {
                for (String code : split) {
                    ids.add(code);
                }
            }
        } else {
            ids.add(itemIds);
        }

        String ua = request.getHeader("user-agent").toLowerCase();

        //创建订单 
        PayOrder order = payOrderService.createWaitComplete(ids, siteCode,
            ServletUtil.getIpAddr(request), auth.getRegisterCode(), RegTerminalEnum.WEICHAT);
        model.addAttribute("orderCode", order.getOrderCode());
        model.addAttribute("order", PayOrderVo.getVo(order));
        String applyType = order.getApplyType();
        SpAthletesEnrollModel athietes = null;
        SpMatchInfoModel matchMode = null;
        try {
            matchMode = spMatchFacadeClient.getMatchDetail(matchCode);
            athietes = spAthletesFacadeClient.getAthietes(matchCode);
        } catch (Exception e) {
            log.error("", e);
        }
        List<PlayerPropertyVo> vos = PlayerPropertyVo.getVos(athietes);
        model.addAttribute("pros", vos);
        model.addAttribute("itemIds", itemIds);
        model.addAttribute("matchCode", matchCode);
        model.addAttribute("siteCode", siteCode);
        model.addAttribute("init", true);
        model.addAttribute("isEdit", "1");
        session.setAttribute(order.getOrderCode() + "isEdit", "1");
        model.addAttribute("account", auth.getRegister().getAccount());
        model.addAttribute("match", MatchDetailVo.getVo(matchMode));
        if (EventTypeEnums.group.getCode().equals(applyType)) {
            model.addAttribute("isAccount", "1");
            return "pages/team_enroll_form";
        }
        return "pages/entyr_from";
    }

    /**
     * 跳转到比赛项页面
     * 
     * @param matchCode
     * @param model
     * @return
     */
    @RequestMapping("item/{matchCode}")
    public String apply(@PathVariable(value = "matchCode") String matchCode,
                        HttpServletRequest request, Model model) {
        List<SpPlayingAreaModel> models = null;
        SpMatchInfoModel matchDetail = null;
        try {
            matchDetail = spMatchFacadeClient.getMatchDetail(matchCode);
            models = spMatchFacadeClient.getSpPlayingAreas(matchCode);
        } catch (Exception e) {
            log.error("", e);
        }
        String jsApiTicket = getJsApiTicket();
        SortedMap<Object, Object> map = new TreeMap<Object, Object>();
        String noncestr = WeichatUtil.createNoncestr();
        String timestamp = String.valueOf(new Date().getTime() / 1000);
        map.put("jsapi_ticket", jsApiTicket);
        map.put("noncestr", noncestr);
        map.put("timestamp", timestamp);
        map.put("url", request.getRequestURL().toString());
        log.info("签名参数：" + JSON.toJSONString(map));
        String signature = WeichatUtil.createSha1Sign(map);
        model.addAttribute("appId", weichatConfig.appId);
        model.addAttribute("timestamp", timestamp);
        model.addAttribute("noncestr", noncestr);
        model.addAttribute("signature", signature);
        model.addAttribute("sites", SiteVo.getVos(models));
        model.addAttribute("matchCode", matchCode);
        model.addAttribute("match", MatchDetailVo.getVo(matchDetail));
        return "pages/Competition_grouping";
    }

    private String getAccessToken() {
        String accessToken = null;
        try {
            accessToken = cacheService.get(Constants.ACCESS_TOKEN_KEY);
        } catch (Exception e) {
            log.error("从redis中获取accessToken错误,错误原因:{}", accessToken);
        }
        //如果存在直接返回
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }

        // 拼接请求地址
        String requestUrl = String.format(
            "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
            weichatConfig.appId, weichatConfig.appSecret);
        log.info("请求地址:" + requestUrl);
        JSONObject jsonObject = CommonUtil.httpGet(requestUrl);
        log.info("获取access_token返回结果" + jsonObject);
        accessToken = jsonObject.getString("access_token");
        if (StringUtils.isNotBlank(accessToken)) {
            try {
                cacheService.put(Constants.ACCESS_TOKEN_KEY, accessToken, 7000 * 1000);
            } catch (Exception e) {
                log.info("将accessToken放入redis中错误,错误原因:{}" + accessToken);
            }
        }
        return accessToken;
    }

    private String getJsApiTicket() {
        String accessToken = getAccessToken();
        String ticket = null;
        try {
            ticket = cacheService.get(Constants.JSAPI_TICKET_KEY);
        } catch (Exception e) {
            log.error("从redis中获取ticket失败,失败原因：{}", e);
        }
        if (StringUtils.isNotBlank(ticket)) {
            return ticket;
        }
        // 拼接请求地址
        String requestUrl = String.format(
            "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi",
            accessToken);
        log.info("getJsApiTicket.requestUrl====>" + requestUrl);
        JSONObject jsonObject = CommonUtil.httpGet(requestUrl);
        log.info("获取jsapi_ticket返回结构" + jsonObject);
        ticket = jsonObject.getString("ticket");
        if (StringUtils.isNotBlank(ticket)) {
            try {
                cacheService.put(Constants.JSAPI_TICKET_KEY, ticket, 7000 * 1000);
            } catch (Exception e) {
                log.error("将redis加入ticket错误,错误原因:{}", e);
            }
        }
        return ticket;
    }

    /**
     * 获取比赛分组信息和比赛项信息
     * 
     * @return
     */
    @RequestMapping("events")
    @ResponseBody
    public Map<String, Object> getGroupsAndEvents(String siteCode) {
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
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("未获取到相关数据");
        }
    }

    /**
     * 
     * 获取省市区接口
     * @param orderStatus
     * @param token
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取省市区接口", notes = "通过赛事编号获取省市区数据")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "matchCode", value = "赛事编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "areaType", value = "节点类型(province:省,city:市,area:区)", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "parentArea", value = "父节点名称(取省传null,取市传省名称,取区传市名称)", required = false, dataType = "String", paramType = "query") })
    @RequestMapping(value = "match/area", method = { RequestMethod.GET })
    @ResponseBody
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
     * @param orderStatus
     * @param token
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
    @ResponseBody
    public Map<String, Object> matchSites(@RequestParam(value = "matchCode", required = true) String matchCode,
                                          @RequestParam(value = "areaType", required = false) String areaType,
                                          @RequestParam(value = "areaName", required = false) String areaName,
                                          @RequestParam(value = "lon", required = false) String lon,
                                          @RequestParam(value = "lat", required = false) String lat,
                                          @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

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

}
