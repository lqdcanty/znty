/**
 * evake.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.esearch.web.api;

import java.util.Date;

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

import com.alibaba.fastjson.JSONObject;
import com.efida.esearch.enmus.AppAuditEnmu;
import com.efida.esearch.enmus.DocTplAuditEnmu;
import com.efida.esearch.exception.EbusinessException;
import com.efida.esearch.model.App;
import com.efida.esearch.model.AppDocTpl;
import com.efida.esearch.model.AppDocTplDto;
import com.efida.esearch.model.PagingResult;
import com.efida.esearch.service.AppDocFieldService;
import com.efida.esearch.service.AppDocTplService;
import com.efida.esearch.service.AppService;
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
 * @version $Id: TemplateApiController.java, v 0.1 2018年9月30日 下午2:00:55 wang yi Exp $
 */
@RestController
@RequestMapping(value = "template", produces = "application/json")
@Api(value = "索引模板管理接口", tags = "搜索服务-索引模板管理")
public class TemplateApiController {

    @Autowired
    private AppDocTplService   docTplService;

    @Autowired
    private AppDocFieldService docFieldService;

    @Autowired
    private AppService         appService;

    private Logger             logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(value = "索引模板列表", notes = "用于赛事信息列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "keyword", value = "关键字(模板编号、模板名、模板描述)", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "status", value = "状态(wait_audit:待审批; pass:审批通过  true.锁定模板 停止使用 false.未锁定)", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "pageNumber", value = "页数", required = false, defaultValue = "1", dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "数量", required = false, defaultValue = "10", dataType = "int", paramType = "query"), })
    @RequestMapping(value = "list", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<PagingResult<AppDocTplDto>> getTplList(@RequestParam(required = false) String appId,
                                                                @RequestParam(required = false) String keyword,
                                                                @RequestParam(required = false) String status,
                                                                @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                                                @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                                HttpServletRequest request) {
        ResultWrapper<PagingResult<AppDocTplDto>> resultWrapper = new ResultWrapper<PagingResult<AppDocTplDto>>();
        try {
            PagingResult<AppDocTplDto> appTemplates = docTplService.getPagesAppTemplates(appId,
                keyword, status, pageNumber, pageSize);
            resultWrapper.setData(appTemplates);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "索引模板列表(可使用的)", notes = "用于赛事信息列表(可使用的)")
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "keyword", value = "关键字(索引模板编号、索引模板名、索引模板描述)", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "pageNumber", value = "页数", required = false, defaultValue = "1", dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "数量", required = false, defaultValue = "10", dataType = "int", paramType = "query"), })
    @RequestMapping(value = "list/avalible", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<PagingResult<AppDocTplDto>> getAvalibleTplList(@RequestParam(required = false) String appId,
                                                                        @RequestParam(required = false) String keyword,
                                                                        @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                                                        @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                                        HttpServletRequest request) {
        ResultWrapper<PagingResult<AppDocTplDto>> resultWrapper = new ResultWrapper<PagingResult<AppDocTplDto>>();
        try {
            PagingResult<AppDocTplDto> appTemplates = docTplService.getPagesAppTemplates(appId,
                keyword, "pass", pageNumber, pageSize);
            resultWrapper.setData(appTemplates);
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
    public ResultWrapper<AppDocTplDto> addAppInfo(@RequestBody AppDocTplDto app,
                                                  HttpServletRequest request) {
        ResultWrapper<AppDocTplDto> resultWrapper = new ResultWrapper<AppDocTplDto>();
        try {
            app.setApplyUserId("test");
            app.setApplyUserName("test");
            app.setDocCode(app.getTplCode());
            app.setAuditStatus(AppAuditEnmu.WAIT_AUDIT.getCode());
            app.setIsLock(app.getIsLock() == null ? true : app.getIsLock());
            app.setGmtCreate(new Date());
            app.setGmtModified(new Date());
            App appInfo = appService.getAppByAppid(app.getAppId());
            if (appInfo == null) {
                resultWrapper.setErrorMsg("应用不存在");
                return resultWrapper;
            }
            /*      if (appInfo.getIsLock()) {
                resultWrapper.setErrorMsg("锁定应用不能申请索引");
                return resultWrapper;
            }*/
            if (!appInfo.getAuditStatus().equals(AppAuditEnmu.PASS.getCode())) {
                resultWrapper.setErrorMsg("未通过审核应用不能申请索引");
                return resultWrapper;
            }

            AppDocTpl appDocTpl = new AppDocTpl();
            BeanUtils.copyProperties(app, appDocTpl);
            appDocTpl = docTplService.createDocTpl(appDocTpl);
            //默认创建一个字段
            docFieldService.addDocField(docFieldService.createNewDefault(app.getAppId(),
                app.getDocCode(), appDocTpl.getTplName()));
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
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "tplCode", value = "模板编号", required = false, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "up", method = { RequestMethod.POST })
    @ResponseBody
    public ResultWrapper<AppDocTplDto> addAppInfo(@RequestParam(required = true) String appId,
                                                  @RequestParam(required = true) String tplCode,
                                                  @RequestBody AppDocTplDto app,
                                                  HttpServletRequest request) {
        ResultWrapper<AppDocTplDto> resultWrapper = new ResultWrapper<AppDocTplDto>();
        try {
            app.setAuditStatus(AppAuditEnmu.WAIT_AUDIT.getCode());
            app.setGmtModified(new Date());
            app.setAppId(appId);
            app.setTplCode(tplCode);
            AppDocTpl appDocTpl = new AppDocTpl();
            BeanUtils.copyProperties(app, appDocTpl);
            appDocTpl = docTplService.upDocTpl(appDocTpl);
            resultWrapper.setData(app);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "锁定模板", notes = "用于锁定模板")
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "tplCode", value = "模板编号", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "isLock", value = "状态(  true.锁定  false.未锁定)", required = false, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "lock", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<Boolean> lockTpl(@RequestParam(required = true) String appId,
                                          @RequestParam(required = true) String tplCode,
                                          @RequestParam(required = true) Boolean isLock,
                                          HttpServletRequest request) {
        ResultWrapper<Boolean> resultWrapper = new ResultWrapper<Boolean>();
        try {
            boolean fla = docTplService.lockTemplate(appId, tplCode, isLock);
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
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "tplCode", value = "模板编号", required = false, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "del", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<Boolean> delTpl(@RequestParam(required = true) String appId,
                                         @RequestParam(required = true) String tplCode,
                                         HttpServletRequest request) {
        ResultWrapper<Boolean> resultWrapper = new ResultWrapper<Boolean>();
        try {
            boolean fla = docTplService.delDocTpl(appId, tplCode);
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
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "tplCode", value = "模板编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "auditStatus", value = "reject:驳回 pass:审批通过", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "auditDesc", value = "审批描述", required = true, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "audit", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<Boolean> auditDocTplInfo(@RequestParam(required = true) String appId,
                                                  @RequestParam(required = true) String tplCode,
                                                  @RequestParam(required = true) String auditStatus,
                                                  @RequestParam(required = false) String auditDesc,
                                                  HttpServletRequest request) {
        ResultWrapper<Boolean> resultWrapper = new ResultWrapper<Boolean>();
        try {
            if (auditStatus.equals(DocTplAuditEnmu.PASS.getCode())) {
                appService.createDocMapping(appId, tplCode);
            }
            boolean fla = docTplService.auditDocTpl(appId, tplCode, auditStatus);
            resultWrapper.setData(fla);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("审核并创建索引失败", ex);
            resultWrapper.setErrorMsg("审核并创建索引失败");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "审核查询", notes = "用于查询模板信息")
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "tplCode", value = "模板编号", required = true, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "get", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<AppDocTplDto> getDocTplInfo(@RequestParam(required = true) String appId,
                                                     @RequestParam(required = true) String tplCode,
                                                     HttpServletRequest request) {
        ResultWrapper<AppDocTplDto> resultWrapper = new ResultWrapper<AppDocTplDto>();
        try {
            AppDocTpl appDocTpl = docTplService.getAppDocTpl(appId, tplCode);
            resultWrapper.setData(AppDocTplDto.coverToDto(appDocTpl));
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "提交申请模板", notes = "用于提交申请/修改模板")
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "tplCode", value = "模板编号", required = true, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "replay/apply", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<Boolean> applyTplInfo(@RequestParam(required = true) String appId,
                                               @RequestParam(required = true) String tplCode,
                                               HttpServletRequest request) {
        ResultWrapper<Boolean> resultWrapper = new ResultWrapper<Boolean>();
        try {
            boolean fla = docTplService.replayAuditDocTpl(appId, tplCode);
            resultWrapper.setData(fla);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "检查模板编号", notes = "用于检查模板编号是否被占用")
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "tplCode", value = "模板编号", required = true, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "code/check", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<Boolean> checkCode(@RequestParam(required = true) String appId,
                                            @RequestParam(required = true) String tplCode,
                                            HttpServletRequest request) {
        ResultWrapper<Boolean> resultWrapper = new ResultWrapper<Boolean>();
        try {
            resultWrapper.setData(docTplService.checkDocTplCode(appId, tplCode));
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "ES 索引存储结构模板预览", notes = "ES 索引存储结构模板预览")
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "tplCode", value = "模板编号", required = true, dataType = "String", paramType = "query") })
    @RequestMapping(value = "mapping/view", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<JSONObject> auditTplInfo(@RequestParam(required = true) String appId,
                                                  @RequestParam(required = true) String tplCode,
                                                  HttpServletRequest request) {
        ResultWrapper<JSONObject> resultWrapper = new ResultWrapper<JSONObject>();
        try {
            AppDocTpl tpl = docTplService.generateDocTpl(appId, tplCode);
            JSONObject result = new JSONObject();
            result.put("mappingTemplate", tpl.getMappingTplContent());
            result.put("saveDataTemplate", tpl.getDataTplContent());
            resultWrapper.setData(result);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

}
