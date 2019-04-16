package com.efida.sports.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.efida.sports.constants.Constants;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.GudongMatchService;
import com.efida.sports.util.JsonResultUtil;

/**
 * Created by wnagyan on 2018/7/18.
 */
@Controller
public class AboutH5Controller {

    @Autowired
    private CacheService       cacheService;

    @Autowired
    private GudongMatchService gudongMatchService;

    @Value("${ucenter-domain}")
    public String              ucenterDomain;

    @RequestMapping("about")
    public String about() {
        return "pages/about";
    }

    @RequestMapping("custom/service")
    public String customService() {
        return "pages/service";
    }

    @RequestMapping("ucenter")
    public String ucenter(HttpSession session) {
        return "redirect:" + ucenterDomain;

    }

    @RequestMapping("ucenter/ucenterChild")
    public String ucenterChild() {
        return "pages/ucenterChild";
    }

    @RequestMapping("session/save")
    @ResponseBody
    public Map<String, Object> saveSession(String sessionName, String sessionValue,
                                           HttpSession session) {
        try {
            session.setAttribute(sessionName, sessionValue);
            return JsonResultUtil.getSuccessResult();
        } catch (Exception e) {
            return JsonResultUtil.getServerErrorResult("保存session信息失败");
        }
    }

    @RequestMapping("session/remove")
    @ResponseBody
    public Map<String, Object> removeSession(String sessionName, HttpSession session) {
        try {
            session.removeAttribute(sessionName);
            return JsonResultUtil.getSuccessResult();
        } catch (Exception e) {
            return JsonResultUtil.getServerErrorResult("删除session信息失败");
        }
    }

    @RequestMapping("gudongMatch/save")
    @ResponseBody
    public Map<String, Object> saveGudongmatch(String sessionName, HttpSession session) {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("data", gudongMatchService.getAllDataMap());
            return JsonResultUtil.getSuccessResult(result);
        } catch (Exception e) {
            return JsonResultUtil.getServerErrorResult("获取咕咚对应赛事信息失败");
        }
    }

    @RequestMapping("gudongMatch/remove")
    @ResponseBody
    public Map<String, Object> removeGudongmatch(String sessionName, HttpSession session) {
        try {
            cacheService.remove(Constants.GUDONG_MATCH_CODE);
            return JsonResultUtil.getSuccessResult();
        } catch (Exception e) {
            return JsonResultUtil.getServerErrorResult("删除咕咚对应赛事信息失败");
        }
    }

}
