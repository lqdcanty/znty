/**
 * evake.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.esearch.web.api;

import java.util.List;

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

import com.alibaba.fastjson.JSONArray;
import com.efida.esearch.core.EDataType;
import com.efida.esearch.exception.EbusinessException;
import com.efida.esearch.model.AppDocFields;
import com.efida.esearch.service.AppDocFieldService;
import com.efida.esearch.web.vo.ResultWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 字段管理Api
 * @author wang yi
 * @desc 
 * @version $Id: TemplateApiController.java, v 0.1 2018年9月30日 下午2:00:55 wang yi Exp $
 */
@RestController
@RequestMapping(value = "template/field", produces = "application/json")
@Api(value = "索引字段管理接口", tags = "搜索服务-索引字段管理")
public class TemplateFieldApiController {

    @Autowired
    private AppDocFieldService docFieldService;

    private Logger             logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(value = "索引字段列表", notes = "用于索引字段列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "应用ID", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "docCode", value = "数据表编码", required = false, dataType = "String", paramType = "query") })
    @RequestMapping(value = "list", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<List<AppDocFields>> getTplList(@RequestParam(required = true) String appId,
                                                        @RequestParam(required = false) String docCode,
                                                        HttpServletRequest request) {
        ResultWrapper<List<AppDocFields>> resultWrapper = new ResultWrapper<List<AppDocFields>>();
        try {
            List<AppDocFields> fields = docFieldService.getDocFields(appId, docCode);
            resultWrapper.setData(fields);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "索引字段添加", notes = "用于索引字段添加")
    @ApiImplicitParams({})
    @RequestMapping(value = "add", method = { RequestMethod.POST })
    @ResponseBody
    public ResultWrapper<AppDocFields> addAppInfo(@RequestBody AppDocFields app,
                                                  HttpServletRequest request) {
        ResultWrapper<AppDocFields> resultWrapper = new ResultWrapper<AppDocFields>();
        try {
            AppDocFields docField = docFieldService.addDocField(app);
            resultWrapper.setData(docField);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "索引字段修改", notes = "用于索引字段修改")
    @ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "字段ID", required = false, dataType = "String", paramType = "query"), })
    @RequestMapping(value = "up", method = { RequestMethod.POST })
    @ResponseBody
    public ResultWrapper<AppDocFields> addAppInfo(@RequestParam(required = true) Long id,
                                                  @RequestBody AppDocFields app,
                                                  HttpServletRequest request) {
        ResultWrapper<AppDocFields> resultWrapper = new ResultWrapper<AppDocFields>();
        try {
            AppDocFields docField = docFieldService.updateDocField(id, app);
            resultWrapper.setData(docField);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "删除索引字段", notes = "用于删除索引字段")
    @ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "字段ID", required = false, dataType = "int", paramType = "query"), })
    @RequestMapping(value = "del", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<Boolean> delTpl(@RequestParam(required = true) Long id,
                                         HttpServletRequest request) {
        ResultWrapper<Boolean> resultWrapper = new ResultWrapper<Boolean>();
        try {
            boolean fla = docFieldService.delByid(id);
            resultWrapper.setData(fla);
        } catch (EbusinessException ex) {
            resultWrapper.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }

    @ApiOperation(value = "获取字段类型数据", notes = "获取字段类型数据")
    @RequestMapping(value = "filed/alltype", method = { RequestMethod.GET })
    @ResponseBody
    public ResultWrapper<JSONArray> getAllFiledType(HttpServletRequest request) {
        ResultWrapper<JSONArray> resultWrapper = new ResultWrapper<JSONArray>();
        try {
            resultWrapper.setData(EDataType.toJson());
        } catch (Exception ex) {
            logger.error("系统错误 ", ex);
            resultWrapper.setErrorMsg("系统错误");
        }
        return resultWrapper;
    }
}
