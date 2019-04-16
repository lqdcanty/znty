/**
 * evake.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.esearch.web.api;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.efida.esearch.enmus.AppAuditEnmu;
import com.efida.esearch.exception.EbusinessException;
import com.efida.esearch.model.App;
import com.efida.esearch.model.PagingResult;
import com.efida.esearch.service.AppService;
import com.efida.esearch.web.vo.ResultWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 应用管理Api
 * @author wang yi
 * @desc 
 * @version $Id: AppApiController.java, v 0.1 2018年10月8日 上午11:06:17 wang yi Exp $
 */
@RestController
@RequestMapping(value = "app", produces = "application/json")
@Api(value = "应用管理接口", tags = "搜索服务-应用管理")
public class AppApiController {

    @Autowired
    private AppService appService;

    private Logger     logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(value = "应用列表", notes = "用于应用列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "keyword", value = "关键字(应用名、申请描述)", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "status", value = "状态(wait_audit:待审批;reject:驳回 pass:审批通过  true.锁定  false.未锁定)", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "pageNumber", value = "页数", required = false, defaultValue = "1", dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "数量", required = false, defaultValue = "10", dataType = "int", paramType = "query"), })
    @RequestMapping(value = "list", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<PagingResult<App>> getAppInfos(@RequestParam(required = false) String appId,
                                                        @RequestParam(required = false) String keyword,
                                                        @RequestParam(required = false) String status,
                                                        @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                                        @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                        HttpServletRequest request) {
        ResultWrapper<PagingResult<App>> resultWrapper = new ResultWrapper<PagingResult<App>>();
        try {
            PagingResult<App> appInfos = appService.getAppInfos(appId, keyword, status, pageNumber,
                pageSize);
            resultWrapper.setData(appInfos);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "应用列表(可使用的)", notes = "用于应用列表(可使用的)")
    @ApiImplicitParams({ @ApiImplicitParam(name = "keyword", value = "关键字(应用名、申请描述)", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "pageNumber", value = "页数", required = false, defaultValue = "1", dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "数量", required = false, defaultValue = "10", dataType = "int", paramType = "query"), })
    @RequestMapping(value = "list/avalible", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<PagingResult<App>> getValibleAppInfos(@RequestParam(required = false) String keyword,
                                                               @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                                               @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                               HttpServletRequest request) {
        ResultWrapper<PagingResult<App>> resultWrapper = new ResultWrapper<PagingResult<App>>();
        try {
            PagingResult<App> appInfos = appService.getAppInfos(null, keyword, "pass", pageNumber,
                pageSize);
            resultWrapper.setData(appInfos);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "应用申请", notes = "用于申请")
    @ApiImplicitParams({})
    @RequestMapping(value = "apply", method = { RequestMethod.POST })
    @ResponseBody
    public ResultWrapper<App> addAppInfo(@RequestBody App app, HttpServletRequest request) {
        ResultWrapper<App> resultWrapper = new ResultWrapper<App>();
        try {
            app.setGmtCreate(new Date());
            app.setApplyTime(new Date());
            app.setGmtModified(new Date());
            app.setIsLock(app.getIsLock() == null ? true : app.getIsLock());
            app.setAuditStatus(AppAuditEnmu.WAIT_AUDIT.getCode());
            App appinfo = appService.createAppInfo(app);
            resultWrapper.setData(appinfo);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "应用修改", notes = "用于申请修改")
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = false, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "up", method = { RequestMethod.POST })
    @ResponseBody
    public ResultWrapper<App> upAppInfo(@RequestParam(required = true) String appId,
                                        @RequestBody App app, HttpServletRequest request) {
        ResultWrapper<App> resultWrapper = new ResultWrapper<App>();
        try {
            app.setGmtModified(new Date());
            App appinfo = appService.upAppInfo(appId, app);
            resultWrapper.setData(appinfo);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "锁定应用", notes = "用于锁定应用")
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "isLock", value = "状态(  true.锁定  false.未锁定)", required = false, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "lock", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<Boolean> lockAppInfo(@RequestParam(required = true) String appId,
                                              @RequestParam(required = true) Boolean isLock,
                                              HttpServletRequest request) {
        ResultWrapper<Boolean> resultWrapper = new ResultWrapper<Boolean>();
        try {
            boolean status = appService.lockAppInfo(appId, isLock);
            resultWrapper.setData(status);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "审核应用", notes = "用于审核应用")
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "auditStatus", value = "reject:驳回 pass:审批通过", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "auditDesc", value = "审批描述", required = true, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "audit", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<Boolean> auditAppInfo(@RequestParam(required = true) String appId,
                                               @RequestParam(required = true) String auditStatus,
                                               @RequestParam(required = false) String auditDesc,
                                               HttpServletRequest request) {
        ResultWrapper<Boolean> resultWrapper = new ResultWrapper<Boolean>();
        try {
            boolean status = appService.auditAppInfo(appId, auditStatus, auditDesc);
            resultWrapper.setData(status);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "提交申请应用", notes = "用于提交申请应用")
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = true, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "replay/apply", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<Boolean> auditAppInfo(@RequestParam(required = true) String appId,
                                               HttpServletRequest request) {
        ResultWrapper<Boolean> resultWrapper = new ResultWrapper<Boolean>();
        try {
            boolean status = appService.auditAppInfo(appId, AppAuditEnmu.WAIT_AUDIT.getCode(),
                null);
            resultWrapper.setData(status);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

}
