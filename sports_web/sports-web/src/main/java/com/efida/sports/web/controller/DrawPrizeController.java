/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.sports.entity.DrawPrize;
import com.efida.sports.entity.DrawPrizeRecord;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.service.draw.DrawprizeService;
import com.efida.sports.util.JsonResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 抽奖活动Api
 * @author wang yi
 * @desc 
 * @version $Id: DrawPrizeController.java, v 0.1 2018年10月17日 下午6:48:03 wang yi Exp $
 */
@Controller
@Api(value = "drawPrize", tags = { "抽奖活动相关操作接口" })
@RequestMapping(value = "draw", produces = "application/json;charset=UTF-8")
public class DrawPrizeController {

    @Autowired
    private DrawprizeService      drpService;

    @Autowired
    private UcenterAdapterService ucenterAdapterService;

    @Value("${ucenter-domain}")
    public String                 ucenterDomain;
    @Value("${apply-domain}")
    public String                 applyDomain;

    private Logger                log = LoggerFactory.getLogger(this.getClass());

    /*@ApiOperation(value = "抽奖活动添加", notes = "抽奖活动添加")
    @ApiImplicitParams({})
    @ResponseBody
    @RequestMapping(value = "acvity/add", method = { RequestMethod.POST })
    public Map<String, Object> addActivity(@RequestBody DrawActivity acitive) {
    
        try {
            return JsonResultUtil.getSuccessResult(new HashMap<String, Object>());
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("操作失败");
        }
    }*/

    @ApiOperation(value = "参与抽奖", notes = "参与抽奖")
    @ApiImplicitParams({})
    @ResponseBody
    @RequestMapping(value = "prize", method = { RequestMethod.POST })
    public Map<String, Object> userDraw(@RequestParam(required = true) String activeCode,
                                        HttpSession session) {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            AuthToken auth = ucenterAdapterService.getPCAuthToken(session);
            if (auth == null) {
                throw new BusinessException("获取用户信息失败");
            }
            drpService.checkIsEnableDraw(activeCode, auth.getRegisterCode());
            DrawPrize luckDrawPrize = drpService.luckDrawPrize(activeCode, auth.getRegisterCode());
            if (luckDrawPrize == null) {
                throw new BusinessException("抽奖失败,请稍后重试");
            } else {
                result.put("status", true);
                result.put("prizeName", luckDrawPrize.getPrizeName());
                result.put("prizeType", Integer.parseInt(luckDrawPrize.getPrizeType()));
            }
            return JsonResultUtil.getSuccessResult(result);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("操作失败");
        }
    }

    @ApiOperation(value = "获取用户是否可抽奖", notes = "获取用户是否可抽奖")
    @ApiImplicitParams({})
    @ResponseBody
    @RequestMapping(value = "prize/status", method = { RequestMethod.POST })
    public Map<String, Object> userDrawStatus(@RequestParam(required = true) String activeCode,
                                              HttpSession session) {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            AuthToken auth = ucenterAdapterService.getPCAuthToken(session);
            if (auth == null) {
                throw new BusinessException("获取用户信息失败");
            }
            drpService.checkIsEnableDraw(activeCode, auth.getRegisterCode());
            result.put("status", true);
            result.put("reason", "");
            return JsonResultUtil.getSuccessResult(result);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("操作失败");
        }
    }

    @RequestMapping(value = "vacvity/{code}", method = { RequestMethod.GET })
    public String viewAcitity(@PathVariable(value = "code") String code, Model model,
                              HttpServletRequest request,
                              HttpSession session) throws UnsupportedEncodingException {
        request.setAttribute("activityCode", code);
        AuthToken auth = ucenterAdapterService.getPCAuthToken(session);
        String uri = applyDomain + "/draw/vacvity/" + code;
        uri = URLEncoder.encode(uri, "UTF-8");
        if (auth == null) {
            return "redirect:" + ucenterDomain + "?login_redirect=" + uri;
        }
        try {
            DrawPrizeRecord record = drpService.getPrizeRecord(code, auth.getRegisterCode());
            if (record == null) {
                //初始化记录
                DrawPrizeRecord drawPrizeRecord = new DrawPrizeRecord();
                drawPrizeRecord.setActivityCode(code);
                drawPrizeRecord.setRegisterCode(auth.getRegisterCode());
                drawPrizeRecord.setIsDraw(0);
                drawPrizeRecord.setGmtCreate(new Date());
                drpService.addNewViewRecord(drawPrizeRecord);
            }
            log.info("用户:{} 访问活动活动编号:{}", auth.getRegisterCode(), code);
        } catch (Exception e) {
            log.error("初始化记录信息失败", e);
        }
        return "pages/draw_index";
    }

}
