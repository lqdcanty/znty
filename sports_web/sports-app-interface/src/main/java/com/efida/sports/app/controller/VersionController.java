/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.efida.sport.admin.facade.model.SpAppVersionModel;
import com.efida.sports.app.vo.AppVersionVo;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.dubbo.intergration.SpAppVersionFacadeClient;
import com.efida.sports.util.JsonResultUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController()
@Api(value = "versionApi", tags = { "app版本检查接口" })
@RequestMapping(value = "/api", produces = "application/json")
public class VersionController {
    private static Logger log = LoggerFactory.getLogger(VersionController.class);

    @Autowired
    private SpAppVersionFacadeClient spAppVersionFacadeClient;

    @ApiOperation(value = "获取app版本号", notes = "获取app版本控制信息")
    @ApiImplicitParams({ @ApiImplicitParam(name = "platform", value = "app类型(ios,android)", required = true, dataType = "String", paramType = "query") })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "version/info", method = { RequestMethod.GET })
    public Map<String, Object> versionInfo(@RequestParam(value = "platform", required = true) String platform) {
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
//            if ("android".equals(platform)) {
//                AppVersionVo vo = new AppVersionVo();
//                vo.setDesc("1.登录后头像高亮显示修复；\n" +
//                        "2.我的成绩页返回时崩溃修复；\n" +
//                        "3.成绩卡分享时崩溃修复");
//                vo.setDownUrl("http://static.appapi.zntyydh.com/download/ztyd.apk");
//                vo.setIsForcedUpgrade(true);
//                vo.setVersion("2.1.1");
//                map.put("version", vo);
//            } else {
//                AppVersionVo vo = new AppVersionVo();
//                vo.setDesc("1.增加成绩系统\n" +
//                        "2.优化个人中心\n" +
//                        "3.优化帮助与反馈");
//                vo.setDownUrl("https://itunes.apple.com/cn/app/id1420328037?mt=8");
//                vo.setIsForcedUpgrade(false);
//                vo.setVersion("2.1.0");
//                map.put("version", vo);
//            }
            //动态获取测试完毕，等待上线-需要把以上数据录入生产表中在上线
            if(StringUtils.isBlank(platform)){
                return JsonResultUtil.getServerErrorResult("获取版本信息失败");
            }
            SpAppVersionModel appVersion = spAppVersionFacadeClient.getAppVersionByAppType(platform);
            if(null == appVersion){
                return JsonResultUtil.getServerErrorResult("获取版本信息失败");
            }
            map.put("version", getAppVersion(appVersion));
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取版本信息失败");
        }
    }

    public AppVersionVo getAppVersion(SpAppVersionModel appVersion){
        AppVersionVo vo = new AppVersionVo();
        vo.setDesc(appVersion.getAppDesc());
        if("android".equals(appVersion.getAppType())){
            vo.setDownUrl(appVersion.getAppdownUrl());
        }
        if("ios".equals(appVersion.getAppType())){
            vo.setDownUrl(appVersion.getDownUrl());
        }
        if(0 == appVersion.getIsForcedUpgrade()){
            vo.setIsForcedUpgrade(false);
        }
        if(1 == appVersion.getIsForcedUpgrade()){
            vo.setIsForcedUpgrade(true);
        }
        if(0 == appVersion.getStatus()){
            vo.setStatus(false);
        }
        if(1 == appVersion.getStatus()){
            vo.setStatus(true);
        }
        vo.setVersion(appVersion.getAppVersion());
        return vo;
    }

    /**
     * 检查域名
     */
    @ApiOperation(value = "检查域名", notes = "检查域名是否正确")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "check/domian", method = { RequestMethod.GET })
    public Map<String, Object> checkDomain() {
        return JsonResultUtil.getSuccessResult();
    }
}
