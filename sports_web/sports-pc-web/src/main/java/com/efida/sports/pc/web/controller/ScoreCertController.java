/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.pc.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sport.dmp.facade.model.ScoreCertModel;
import com.efida.sport.dmp.facade.result.DmpPageResult;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.service.dubbo.intergration.ScoreCertFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;
import com.efida.sports.util.JsonResultUtil;

/**
 * 
 * @author zoutao
 * @version $Id: ScoreCertController.java, v 0.1 2018年8月6日 下午5:36:37 zoutao Exp $
 */
@Controller
@RequestMapping("cert")
public class ScoreCertController {
    private static Logger               log = LoggerFactory.getLogger(MatchController.class);

    @Autowired
    private ScoreCertFacadeClient       scoreCertFacadeClient;

    @Autowired
    private UcenterAdapterService       ucenterAdapterService;
    @Autowired
    private UcenterRegisterFacadeClient registerFacadeClient;

    @Value("${ucenter-domain}")
    public String                       ucenterDomain;
    @Value("${apply-domain}")
    public String                       applyDomain;

    @RequestMapping("init")
    public String certPage(HttpSession session, Model model) throws UnsupportedEncodingException {
        RegisterModel register = null;
        String uri = applyDomain + "/cert/init";
        uri = URLEncoder.encode(uri, "UTF-8");
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
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
            return "redirect:" + ucenterDomain + "?login_redirect=" + uri;
        }
        model.addAttribute("register", register);
        return "pages/medal_page";
    }

    @RequestMapping("view/cert")
    @ResponseBody
    public Map<String, Object> viewCertPic(HttpSession session,
                                           @RequestParam(value = "certCode", required = true) String certCode) {
        try {
            scoreCertFacadeClient.viewScoreCert(certCode);
            return JsonResultUtil.getSuccessResult();
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("修改查看证书状态失败");
        }
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> certList(HttpSession session, Model model,
                                        @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            DmpPageResult<ScoreCertModel> page = scoreCertFacadeClient
                .getRegisterPageScoreCert(authToken.getRegisterCode(), currentPage, pageSize);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("register", authToken.getRegister());
            map.put("list", page.getList());
            map.put("total", page.getAllRow());
            map.put("current", page.getCurrentPage());
            map.put("pages", page.getTotalPage());
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("证书列表查询失败");
        }
    }

    @RequestMapping("new/cert")
    @ResponseBody
    public Map<String, Object> newestScoreCert(HttpSession session, Model model) {
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            ScoreCertModel scoreCert = scoreCertFacadeClient
                .getRegisterNewestScoreCert(authToken.getRegisterCode());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("register", authToken.getRegister());
            map.put("cert", scoreCert);
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取证书");
        }
    }

}
