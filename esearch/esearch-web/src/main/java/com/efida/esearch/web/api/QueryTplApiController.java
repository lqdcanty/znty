/**
 * evake.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.esearch.web.api;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efida.esearch.core.EffectiveType;
import com.efida.esearch.core.LogicType;
import com.efida.esearch.core.SymbolType;
import com.efida.esearch.enmus.AppAuditEnmu;
import com.efida.esearch.enmus.DocTplAuditEnmu;
import com.efida.esearch.exception.EbusinessException;
import com.efida.esearch.model.App;
import com.efida.esearch.model.AppDocTpl;
import com.efida.esearch.model.AppQueryTpl;
import com.efida.esearch.model.PagingResult;
import com.efida.esearch.service.AppDocTplService;
import com.efida.esearch.service.AppQueryTplService;
import com.efida.esearch.service.AppService;
import com.efida.esearch.utils.VelocityUtil;
import com.efida.esearch.web.vo.ResultWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 模板管理Api
 * @author wang yi
 * @desc 
 * @version $Id: QueryTplApiController.java, v 0.1 2018年9月30日 下午2:00:55 wang yi Exp $
 */
@RestController
@RequestMapping(value = "template/query", produces = "application/json")
@Api(value = "查询模板管理接口", tags = "搜索服务-查询模板管理")
public class QueryTplApiController {

    @Autowired
    private AppQueryTplService docQueryTplService;

    @Autowired
    private AppDocTplService   docTplService;

    @Autowired
    private AppService         appService;

    private Logger             logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(value = "查询模板列表", notes = "用于赛事信息列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "keyword", value = "关键字(模板编号、模板名、模板描述)", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "status", value = "状态(wait_audit:待审批; pass:审批通过  true.锁定模板 停止使用 false.未锁定)", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "pageNumber", value = "页数", required = false, defaultValue = "1", dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "数量", required = false, defaultValue = "10", dataType = "int", paramType = "query"), })
    @RequestMapping(value = "list", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<PagingResult<AppQueryTpl>> getTplList(@RequestParam(required = false) String appId,
                                                               @RequestParam(required = false) String keyword,
                                                               @RequestParam(required = false) String status,
                                                               @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                                               @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                               HttpServletRequest request) {
        ResultWrapper<PagingResult<AppQueryTpl>> resultWrapper = new ResultWrapper<PagingResult<AppQueryTpl>>();
        try {
            PagingResult<AppQueryTpl> appTemplates = docQueryTplService
                .getPagesQueryTemplates(appId, keyword, status, pageNumber, pageSize);
            resultWrapper.setData(appTemplates);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "查询模板单独根据appId,docTplCode,tpl_code获取", notes = "用于修改查看查询模板")
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "docTplCode", value = "索引模板编码", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "tplCode", value = "查询模板编码", required = true, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "/tpl/info", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<AppQueryTpl> getTplInfo(@RequestParam(required = false) String appId,
                                                 @RequestParam(required = false) String docTplCode,
                                                 @RequestParam(required = false) String tplCode,
                                                 HttpServletRequest request) {
        ResultWrapper<AppQueryTpl> resultWrapper = new ResultWrapper<AppQueryTpl>();
        try {
            AppQueryTpl queryTpl = docQueryTplService.getAppQueryTplByAppDocTplCode(appId,
                docTplCode, tplCode);
            resultWrapper.setData(queryTpl);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "模板申请", notes = "用于模板申请")
    @ApiImplicitParams({})
    @RequestMapping(value = "apply", method = { RequestMethod.POST })
    @ResponseBody
    public ResultWrapper<JSONObject> addAppInfo(@RequestBody JSONObject app,
                                                HttpServletRequest request) {
        ResultWrapper<JSONObject> resultWrapper = new ResultWrapper<JSONObject>();
        try {
            //条件限定
            App appInfo = appService.getAppByAppid(app.getString("appId"));
            if (appInfo == null) {
                resultWrapper.setErrorMsg("应用不存在");
                return resultWrapper;
            }
            /*   if (appInfo.getIsLock()) {
                resultWrapper.setErrorMsg("锁定应用不能申请搜索模板");
                return resultWrapper;
            }*/
            if (!appInfo.getAuditStatus().equals(AppAuditEnmu.PASS.getCode())) {
                resultWrapper.setErrorMsg("未通过审核索引不能申请搜索模板");
                return resultWrapper;
            }
            //索引模板
            AppDocTpl appDocTpl = docTplService.getAppDocTpl(app.getString("appId"),
                app.getString("docTplCode"));
            if (appDocTpl == null) {
                resultWrapper.setErrorMsg("索引不存在");
                return resultWrapper;
            }
            /* if (appDocTpl.getIsLock()) {
                resultWrapper.setErrorMsg("锁定索引不能申请搜索模板");
                return resultWrapper;
            }*/
            if (!appDocTpl.getAuditStatus().equals(DocTplAuditEnmu.PASS.getCode())) {
                resultWrapper.setErrorMsg("未通过审核索引不能申请搜索模板");
                return resultWrapper;
            }
            AppQueryTpl appQueryTpl = new AppQueryTpl();
            appQueryTpl.setId(app.getLong("id"));
            appQueryTpl.setTplDesc(app.getString("tplDesc"));
            appQueryTpl.setTplName(app.getString("tplName"));
            appQueryTpl.setTplCode(app.getString("tplCode"));
            appQueryTpl.setDocTplCode(app.getString("docTplCode"));
            appQueryTpl.setAppId(app.getString("appId"));
            appQueryTpl.setParams(app.getString("params"));
            appQueryTpl.setConditionJson(app.getJSONObject("condition").toJSONString());
            appQueryTpl.setTplContent(docQueryTplService
                .complexConditionAnalize(app.getJSONObject("condition").getJSONArray("condition")));
            appQueryTpl.setAuditStatus(AppAuditEnmu.WAIT_AUDIT.getCode());
            appQueryTpl.setIsLock(true);
            appQueryTpl.setGmtCreate(new Date());
            appQueryTpl.setGmtModified(new Date());
            appQueryTpl = docQueryTplService.createDocTpl(appQueryTpl);
            resultWrapper.setData(app);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "模板修改", notes = "用于模板修改")
    @ApiImplicitParams({ @ApiImplicitParam(name = "tpl_code", value = "模板编号", required = false, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "up", method = { RequestMethod.POST })
    @ResponseBody
    public ResultWrapper<AppQueryTpl> addAppInfo(@RequestParam(required = true) String tpl_code,
                                                 @RequestBody JSONObject app,
                                                 HttpServletRequest request) {
        ResultWrapper<AppQueryTpl> resultWrapper = new ResultWrapper<AppQueryTpl>();
        try {
            AppQueryTpl appQueryTpl = new AppQueryTpl();
            appQueryTpl.setTplDesc(app.getString("tplDesc"));
            appQueryTpl.setTplName(app.getString("tplName"));
            appQueryTpl.setTplCode(app.getString("tplCode"));
            appQueryTpl.setDocTplCode(app.getString("docTplCode"));
            appQueryTpl.setParams(app.getString("params"));
            appQueryTpl.setConditionJson(app.getJSONObject("condition").toJSONString());
            appQueryTpl.setTplContent(docQueryTplService
                .complexConditionAnalize(app.getJSONObject("condition").getJSONArray("condition")));
            appQueryTpl.setGmtModified(new Date());
            appQueryTpl.setAuditStatus(AppAuditEnmu.WAIT_AUDIT.getCode());
            appQueryTpl = docQueryTplService.upDocTpl(appQueryTpl);
            resultWrapper.setData(appQueryTpl);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "锁定模板", notes = "用于锁定模板")
    @ApiImplicitParams({ @ApiImplicitParam(name = "tpl_code", value = "模板编号", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "isLock", value = "状态(  true.锁定  false.未锁定)", required = false, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "lock", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<Boolean> lockTpl(@RequestParam(required = true) String tpl_code,
                                          @RequestParam(required = true) Boolean isLock,
                                          HttpServletRequest request) {
        ResultWrapper<Boolean> resultWrapper = new ResultWrapper<Boolean>();
        try {
            boolean fla = docQueryTplService.lockTemplate(tpl_code, isLock);
            resultWrapper.setData(fla);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "删除模板", notes = "用于删除模板")
    @ApiImplicitParams({ @ApiImplicitParam(name = "tpl_code", value = "模板编号", required = false, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "del", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<Boolean> delTpl(@RequestParam(required = true) String tpl_code,
                                         HttpServletRequest request) {
        ResultWrapper<Boolean> resultWrapper = new ResultWrapper<Boolean>();
        try {
            boolean fla = docQueryTplService.delDocTpl(tpl_code);
            resultWrapper.setData(fla);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "审核模板", notes = "用于审核模板")
    @ApiImplicitParams({ @ApiImplicitParam(name = "tpl_code", value = "模板编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "auditStatus", value = "reject:驳回 pass:审批通过", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "auditDesc", value = "审批描述", required = true, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "audit", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<Boolean> auditDocTplInfo(@RequestParam(required = true) String tpl_code,
                                                  @RequestParam(required = true) String auditStatus,
                                                  @RequestParam(required = false) String auditDesc,
                                                  HttpServletRequest request) {
        ResultWrapper<Boolean> resultWrapper = new ResultWrapper<Boolean>();
        try {
            boolean fla = docQueryTplService.auditDocTpl(tpl_code, auditStatus);
            resultWrapper.setData(fla);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "提交申请模板", notes = "用于提交申请模板")
    @ApiImplicitParams({ @ApiImplicitParam(name = "tpl_code", value = "模板编号", required = true, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "replay/apply", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<Boolean> auditTplInfo(@RequestParam(required = true) String tpl_code,
                                               HttpServletRequest request) {
        ResultWrapper<Boolean> resultWrapper = new ResultWrapper<Boolean>();
        try {
            boolean fla = docQueryTplService.replayAuditDocTpl(tpl_code);
            resultWrapper.setData(fla);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "查询模板逻辑关系和符号获取接口", notes = "查询模板创建")
    @RequestMapping(value = "sys/logic_symbol", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<JSONObject> logic_symbol(HttpServletRequest request) {
        ResultWrapper<JSONObject> resultWrapper = new ResultWrapper<JSONObject>();
        try {
            JSONObject logic_symbol = new JSONObject();
            logic_symbol.put("logic", LogicType.toJson());
            logic_symbol.put("symbol", SymbolType.toJson());
            logic_symbol.put("effect", EffectiveType.toJson());
            resultWrapper.setData(logic_symbol);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "预览查询模板结构", notes = "预览查询模板结构")
    @ApiImplicitParams({ @ApiImplicitParam(name = "dataJson", value = "查询模板封装json对象 ,包含 queryConfigJson  paramValues,   queryConfigJson 包含:conditions,params", required = true, dataType = "String", paramType = "query") })
    @RequestMapping(value = "view/query/tpl", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public ResultWrapper<String> viewQueryTpl(HttpServletRequest request,
                                              @RequestBody(required = true) JSONObject dataJson) {
        ResultWrapper<String> resultWrapper = new ResultWrapper<String>();
        try {
            JSONObject queryConfigJson = dataJson.getJSONObject("queryConfigJson");
            JSONObject pValues = dataJson.getJSONObject("paramValues");
            logger.info("pValues:" + pValues.toJSONString());
            JSONArray conditions = queryConfigJson.getJSONArray("conditions");
            String[] pNames = queryConfigJson.getString("params").split(",");
            List<String> paramNames = Arrays.asList(pNames);
            logger.info("pNames:" + pNames);
            Map<String, String> params = JSONObject.parseObject(pValues.toJSONString(), Map.class);
            logger.info("conditions:" + conditions.toJSONString());
            String boolStr = docQueryTplService.complexConditionAnalize(conditions);
            StringBuffer queryStr = new StringBuffer();

            queryStr.append("{\"query\":{\"bool\":{\"should\":[");
            queryStr.append(boolStr);
            queryStr.append("]}}}");
            logger.info("生成的模板语句:" + queryStr);
            String tplStr = VelocityUtil.replaceTplByVelocity(queryStr.toString(), paramNames,
                params);
            resultWrapper.setData(tplStr);
            return resultWrapper;
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {

            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

}
