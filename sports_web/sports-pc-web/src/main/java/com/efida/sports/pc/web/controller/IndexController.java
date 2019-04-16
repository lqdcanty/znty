package com.efida.sports.pc.web.controller;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.sports.service.UcenterAdapterService;

/**
 * 域名直接访问首页
 * 
 * @author zengbo
 * @version $Id: IndexController.java, v 0.1 2018年7月9日 上午11:37:39 zengbo Exp $
 */
@RequestMapping("/")
@Controller
public class IndexController {

    private Logger                logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UcenterAdapterService ucenterAdapterService;
    @Value("${ucenter-domain}")
    public String                 ucenterDomain;
    @Value("${apply-domain}")
    public String                 applyDomain;

    /**
     * 跳转到赛事列表页面
     * 
     * @param request
     * @return
     */
    @RequestMapping("")
    public String index(HttpServletRequest request, HttpSession session, Model model) {
        AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
        if (authToken != null) {
            model.addAttribute("register", authToken.getRegister());
        }
        return "pages/index";
    }

    @RequestMapping("static/{path}")
    public String orderSuccess(@PathVariable(value = "path") String path, HttpSession session,
                               Model model, HttpServletRequest request) {
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            if (path.indexOf("goodsInfo") > 0) {
                return "pages/" + path;
            }
            String num = (String) request.getAttribute("num");
            String uri = applyDomain + "/static/" + path + "?num=" + num;
            uri = URLEncoder.encode(uri, "UTF-8");
            if (authToken == null) {
                return "redirect:" + ucenterDomain + "?login_redirect=" + uri;
            }
        } catch (Exception e) {
            logger.error("用戶信息獲取失敗", e);
        }
        return "pages/" + path;
    }

}
