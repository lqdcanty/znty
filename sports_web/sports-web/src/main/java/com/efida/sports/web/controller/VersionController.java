package com.efida.sports.web.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.efida.sport.admin.facade.model.SpAppVersionModel;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.dubbo.intergration.SpAppVersionFacadeClient;
import com.efida.sports.util.JsonResultUtil;
import com.efida.sports.web.vo.AppVersionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by wnagyan on 2018/8/30.
 */
@RequestMapping("version")
@Controller
public class VersionController {
    private static Logger log = LoggerFactory.getLogger(VersionController.class);

    @Autowired
    private SpAppVersionFacadeClient spAppVersionFacadeClient;


    @ResponseBody
    @RequestMapping(value = "info", method = { RequestMethod.GET })
    public Map<String, Object> versionInfo(@RequestParam(value = "platform", required = true) String platform) {
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();//
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
}
