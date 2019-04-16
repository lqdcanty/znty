package com.efida.sports.pc.web.controller;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sport.admin.facade.model.MatchAreaModel;
import com.efida.sport.admin.facade.model.SpGroupItemModel;
import com.efida.sport.admin.facade.model.SpMatchGroupAndItemInfoModel;
import com.efida.sport.admin.facade.model.SpMatchInfoModel;
import com.efida.sport.admin.facade.model.SpMatchModel;
import com.efida.sport.admin.facade.model.SpPlayingAreaModel;
import com.efida.sport.admin.facade.model.SpProjectTypeModel;
import com.efida.sport.admin.facade.model.open.MatchCollectionModel;
import com.efida.sport.admin.facade.model.page.SportsAdminPageResult;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.pc.web.vo.EventVo;
import com.efida.sports.pc.web.vo.GameVo;
import com.efida.sports.pc.web.vo.MatchCorrelationVo;
import com.efida.sports.pc.web.vo.MatchDetailVo;
import com.efida.sports.pc.web.vo.MatchGroupVo;
import com.efida.sports.pc.web.vo.MatchRelevantVo;
import com.efida.sports.pc.web.vo.MatchVo;
import com.efida.sports.pc.web.vo.NewSiteVo;
import com.efida.sports.service.IApplyInfoService;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.service.dubbo.intergration.SpAthletesFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpMatchAreaFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpMatchFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpProjectTypeFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;
import com.efida.sports.util.JsonResultUtil;

/**
 * 
 * 赛事信息 
 * @author zengbo
 * @version $Id: MatchController.java, v 0.1 2018年6月14日 下午5:38:23 zengbo Exp $
 */
@RequestMapping("match")
@Controller
public class MatchController {

    private static Logger               log = LoggerFactory.getLogger(MatchController.class);

    @Autowired
    private SpProjectTypeFacadeClient   projectTypeFacadeClient;

    @Autowired
    private SpMatchFacadeClient         spMatchFacadeClient;

    @Autowired
    private SpAthletesFacadeClient      spAthletesFacadeClient;

    @Autowired
    private IApplyInfoService           applyInfoService;

    @Autowired
    private SpMatchAreaFacadeClient     spMatchAreaFacadeClient;

    @Autowired
    private UcenterAdapterService       ucenterAdapterService;
    @Autowired
    private UcenterRegisterFacadeClient registerFacadeClient;

    @Value("${ucenter-domain}")
    public String                       ucenterDomain;
    @Value("${apply-domain}")
    public String                       applyDomain;

    /**
     * 项目类型初始化
     * 
     * @param model
     * @param scope
     * @param session
     * @return
     */
    @RequestMapping("type")
    public String types(Model model, String scope, HttpSession session) {
        RegisterModel register = null;
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            register = authToken.getRegister();
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
        } catch (Exception e) {
            log.error("", e);
        }
        model.addAttribute("register", register);
        return "pages/index";
    }

    /**
     * 赛事列表查询
     * 
     * @param code 項目code
     * @param pageNumber
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping(value = "type/list", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map<String, Object> matchTypeList(HttpSession session) {
        List<SpProjectTypeModel> projectTypes = new ArrayList<SpProjectTypeModel>();
        try {
            List<SpProjectTypeModel> prj = projectTypeFacadeClient.getProjectTypes();
            SpProjectTypeModel spmodel = new SpProjectTypeModel();
            spmodel.setGameName("全部");
            spmodel.setGameCode("all");
            projectTypes.add(spmodel);
            if (!CollectionUtils.isEmpty(prj)) {
                projectTypes.addAll(prj);
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("types", GameVo.getVos(projectTypes));
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("赛事类型查询失败");
        }
    }

    /**
     * 赛事列表查询
     * 
     * @param code 項目code
     * @param pageNumber
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping(value = "list/{code}/{currentPage}/{pageSize}", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map<String, Object> matchList(@PathVariable(value = "code") String code,
                                         @PathVariable(value = "currentPage") String currentPage,
                                         @PathVariable(value = "pageSize") String pageSize,
                                         HttpSession session) {

        List<SpMatchModel> matchs = null;
        SportsAdminPageResult<SpMatchModel> pageMatch = null;
        try {
            if ("all".equals(code)) {
                pageMatch = spMatchFacadeClient.searchMatchs(null, Integer.valueOf(currentPage),
                    Integer.valueOf(pageSize));
            } else {
                pageMatch = spMatchFacadeClient.getPageMatchs(code, Integer.valueOf(currentPage),
                    Integer.valueOf(pageSize));
            }
            matchs = pageMatch.getList();
            //            matchs = spMatchFacadeClient.getMatchs(code);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("matchs", MatchVo.getVos(matchs));
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
     * 跳转到赛事详情页面
     * 
     * @param model
     * @return
     */
    @RequestMapping("detail/{code}")
    public String detail(@PathVariable(value = "code") String code, Model model,
                         HttpSession session) {
        SpMatchInfoModel matchDetail = null;
        List<SpPlayingAreaModel> areas = null;
        List<SpMatchModel> models = null;
        List<MatchCollectionModel> collections = null;
        String gameName = "";
        RegisterModel register = null;
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            String uri = applyDomain + "/match/detail/" + code;
            uri = URLEncoder.encode(uri, "UTF-8");

            if (authToken == null) {
                return "redirect:" + ucenterDomain + "?login_redirect=" + uri;
            }
            register = registerFacadeClient.getRegsiterByRegisterCode(authToken.getRegisterCode());
            if (register == null) {
                return "redirect:" + ucenterDomain + "?login_redirect=" + uri;
            }
            if (StringUtils.isBlank(register.getAccount())) {
                return "redirect:" + ucenterDomain + "/login/phone?binding_redirect=" + uri;
            }
            matchDetail = spMatchFacadeClient.getMatchDetail(code);
            collections = spMatchFacadeClient.getSpMatchCollections(matchDetail.getMatchCode());
            areas = spMatchFacadeClient.getPlayingAreas(code);
            String gameCode = matchDetail.getGameCode();
            models = spMatchFacadeClient.getMatchs(gameCode);
            SpProjectTypeModel type = projectTypeFacadeClient.getSpProjectByCode(gameCode);
            if (type != null) {
                gameName = type.getGameName();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        model.addAttribute("register", register);
        model.addAttribute("match", MatchDetailVo.getVo(matchDetail));
        model.addAttribute("relevant", MatchRelevantVo.getMatchRelevantVo(matchDetail, areas));
        model.addAttribute("correlations", MatchCorrelationVo.getMatchCorrelationVoList(models));
        model.addAttribute("collections", collections);
        model.addAttribute("gameName", gameName);
        return "pages/match_details";
    }

    /**
     * 跳转到比赛项页面
     * 
     * @param matchCode
     * @param model
     * @return
     */
    @RequestMapping("item/{matchCode}")
    public String apply(@PathVariable(value = "matchCode") String matchCode, Model model,
                        HttpSession session) {
        RegisterModel register = null;
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            String uri = applyDomain + "/match/item/" + matchCode;
            uri = URLEncoder.encode(uri, "UTF-8");
            if (authToken == null) {
                return "redirect:" + ucenterDomain + "?login_redirect=" + uri;
            }
            register = registerFacadeClient.getRegsiterByRegisterCode(authToken.getRegisterCode());
            if (register == null) {
                return "redirect:" + ucenterDomain + "?login_redirect=" + uri;
            }
            if (StringUtils.isBlank(register.getAccount())) {
                return "redirect:" + ucenterDomain + "/login/phone?binding_redirect=" + uri;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        model.addAttribute("register", register);
        return "pages/match_site";
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
    @RequestMapping(value = "area/list", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> areaList(@RequestParam(value = "matchCode", required = true) String matchCode,
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
    @RequestMapping(value = "sites/pages", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> sitesPages(@RequestParam(value = "matchCode", required = true) String matchCode,
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

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("totalRow", page.getAllRow());
            map.put("totalPage", page.getTotalPage());
            map.put("currentPage", page.getCurrentPage());
            map.put("pageSize", page.getPageSize());
            map.put("sites", NewSiteVo.getVos(page.getList()));
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

}
