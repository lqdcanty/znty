/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sport.admin.facade.model.SpAthletesEnrollModel;
import com.efida.sports.config.PlayerProConfig;
import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.entity.LeaderInfo;
import com.efida.sports.entity.PayOrder;
import com.efida.sports.entity.Player;
import com.efida.sports.enums.ErrorCodeEnum;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.IPayOrderService;
import com.efida.sports.service.IPlayerService;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.service.dubbo.intergration.SpAthletesFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;
import com.efida.sports.util.DateUtil;
import com.efida.sports.util.JsonResultUtil;
import com.efida.sports.util.SeqWorkerUtil;
import com.efida.sports.web.vo.PlayerVo;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author zoutao
 * @version $Id: ApplyController.java, v 0.1 2018年7月24日 下午10:55:14 zoutao Exp $
 */
@RequestMapping("apply")
@Controller
public class ApplyController {

    @Autowired
    private CacheService                cacheService;
    @Autowired
    private UcenterRegisterFacadeClient registerFacadeClient;
    @Autowired
    private UcenterAdapterService       ucenterAdapterService;
    @Autowired
    private IPayOrderService            orderService;
    @Autowired
    private IPlayerService              playerService;
    @Autowired
    private SpAthletesFacadeClient      spAthletesFacadeClient;

    private static Logger               log = LoggerFactory.getLogger(ApplyController.class);

    @ApiOperation(value = "添加团队成员", notes = "添加团队成员")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderCode", value = "订单编号", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerPhone", value = "运动员电话号码", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "seq", value = "唯一编号", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerCode", value = "运动员编号", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerName", value = "运动员名称", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "sex", value = "性别(m:男,f:女)", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "email", value = "邮箱", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerHeight", value = "身高(int类型)", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerWeight", value = "体重(Double类型)", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerBirth", value = "生日(yyyy-MM-dd)", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerNationality", value = "国籍", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerAddress", value = "地址", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerCerType", value = "证件类型", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerCerNo", value = "证件号码", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerBloodType", value = "血型", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerNation", value = "名族", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerClothSize", value = "衣服尺码", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerWorkuUnit", value = "工作单位", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerEmergencyName", value = "紧急联系人", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerEmergencyPhone", value = "紧急联系人电话", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "attOne", value = "附件1(附件正面,驾照,护照)", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "attTwo", value = "附件1(附件反面)", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "assettoId", value = "神力科莎账户ID", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "attUrl", value = "健康证", required = false, dataType = "String", paramType = "attUrl"),
                         @ApiImplicitParam(name = "imgUrl", value = "头像地址", required = false, dataType = "String", paramType = "form")

    })
    @RequestMapping(value = "save/team/member")
    @ResponseBody
    public Map<String, Object> createTteamMembers(@RequestParam(value = "orderCode", required = true) String orderCode,
                                                  @RequestParam(value = "playerPhone", required = true) String playerPhone,
                                                  @RequestParam(value = "playerCode", required = false) String playerCode,
                                                  @RequestParam(value = "seq", required = false) String seq,
                                                  @RequestParam(value = "playerName", required = false) String playerName,
                                                  @RequestParam(value = "sex", required = false) String sex,
                                                  @RequestParam(value = "email", required = false) String email,
                                                  @RequestParam(value = "playerHeight", required = false) String playerHeight,
                                                  @RequestParam(value = "playerWeight", required = false) String playerWeight,
                                                  @RequestParam(value = "playerBirth", required = false) String playerBirth,
                                                  @RequestParam(value = "playerNationality", required = false) String playerNationality,
                                                  @RequestParam(value = "playerAddress", required = false) String playerAddress,
                                                  @RequestParam(value = "playerCerType", required = false) String playerCerType,
                                                  @RequestParam(value = "playerCerNo", required = false) String playerCerNo,
                                                  @RequestParam(value = "playerBloodType", required = false) String playerBloodType,
                                                  @RequestParam(value = "playerNation", required = false) String playerNation,
                                                  @RequestParam(value = "playerClothSize", required = false) String playerClothSize,
                                                  @RequestParam(value = "playerWorkUnit", required = false) String playerWorkUnit,
                                                  @RequestParam(value = "playerEmergencyName", required = false) String playerEmergencyName,
                                                  @RequestParam(value = "playerEmergencyPhone", required = false) String playerEmergencyPhone,
                                                  @RequestParam(value = "attOne", required = false) String attOne,
                                                  @RequestParam(value = "attTwo", required = false) String attTwo,
                                                  @RequestParam(value = "assettoId", required = false) String assettoId,
                                                  @RequestParam(value = "attUrl", required = false) String attUrl,
                                                  @RequestParam(value = "imgUrl", required = false) String imgUrl,
                                                  HttpServletRequest request, HttpSession session) {
        try {
            log.info("**************************save/team/member**************************");
            String key = "sportsweb_group" + seq;
            String cacheVal = cacheService.get(key);
            if (StringUtils.isNotBlank(cacheVal)) {
                return JsonResultUtil.getErroCodeResult(1001, "重复提交");
            }
            cacheService.put(key, seq, 1000 * 10);
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            RegisterModel register = registerFacadeClient
                .getRegsiterByRegisterCode(auth.getRegisterCode());
            if (StringUtils.isBlank(register.getAccount())) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.UN_BIND_PHONE.getCode(),
                    "用户未绑定手机号");
            }
            Player player = new Player();
            log.info("提交保存团队成员信息：playerWeight:{},playerHeight:{},playerBirth：{},附件1：{},附件2:{}",
                playerWeight, playerHeight, playerBirth, attOne, attTwo);
            if (StringUtils.isNotBlank(playerWeight)) {
                if (!isDouble(playerWeight)) {
                    return JsonResultUtil.getServerErrorResult("体重填写错误");
                }
                player.setPlayerWeight(Double.parseDouble(playerWeight));
            }
            if (StringUtils.isNotBlank(playerBirth)) {
                player.setPlayerBirth(DateUtil.strToDate1(playerBirth));
            }
            if (StringUtils.isNotBlank(playerHeight)) {
                player.setPlayerHeight(Integer.parseInt(playerHeight));
            }
            List<String> playerPro = PlayerProConfig.getInstance().getPlayerPro();
            Enumeration enu = request.getParameterNames();
            HashMap<String, Object> extProMap = new HashMap<String, Object>();
            while (enu.hasMoreElements()) {
                String paraName = (String) enu.nextElement();
                if (playerPro.contains(paraName) || "orderCode".equals(paraName)
                    || "playerCode".equals(paraName) || "seq".equals(paraName)) {
                    continue;
                }
                extProMap.put(paraName, request.getParameter(paraName));
            }
            player.setExtProMap(extProMap);
            player.setPlayerCode(playerCode);
            player.setPlayerAddress(playerAddress);
            player.setEmail(email);
            player.setImgUrl(imgUrl);
            player.setSex(sex);
            player.setPlayerAddress(playerAddress);
            player.setPlayerBloodType(playerBloodType);
            player.setPlayerCerNo(playerCerNo);
            player.setPlayerCerType(playerCerType);
            player.setPlayerClothSize(playerClothSize);
            player.setPlayerEmergencyName(playerEmergencyName);
            player.setPlayerEmergencyPhone(playerEmergencyPhone);
            player.setPlayerName(playerName);
            player.setPlayerNation(playerNation);
            player.setPlayerNationality(playerNationality);
            player.setPlayerPhone(playerPhone);
            player.setPlayerWorkUnit(playerWorkUnit);
            player.setAttOne(attOne);
            player.setAttTwo(attTwo);
            player.setAttUrl(attUrl);
            player.setAssettoId(assettoId);
            playerService.checkPlayer(player);
            orderService.createTeamMembers(player, orderCode, register.getRegisterCode());
            Map<String, Object> map = new HashMap<String, Object>();
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("添加团队成员失败");
        }
    }

    @ApiOperation(value = "保存领队信息", notes = "保存领队信息")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderCode", value = "订单编号", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "teamName", value = "团队名称", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "leaderName", value = "领队名称", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "leaderPhone", value = "领队电话号码", required = false, dataType = "String", paramType = "form") })
    @RequestMapping(value = "save/leader")
    @ResponseBody
    public Map<String, Object> saveLeader(@RequestParam(value = "orderCode", required = true) String orderCode,
                                          @RequestParam(value = "teamName", required = false) String teamName,
                                          @RequestParam(value = "leaderName", required = false) String leaderName,
                                          @RequestParam(value = "leaderPhone", required = false) String leaderPhone,
                                          HttpServletRequest request, HttpSession session) {
        try {
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            LeaderInfo leader = new LeaderInfo();
            leader.setTeamName(teamName);
            leader.setLeaderName(leaderName);
            leader.setLeaderPhone(leaderPhone);
            orderService.saveLearnerInfo(leader, auth.getRegisterCode(), orderCode);
            return JsonResultUtil.getSuccessResult();
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("保存领队信息失败");
        }
    }

    /**
     * 添加团队成员
     * 
     * @param orderCode
     * @param model
     * @return
     */
    @RequestMapping("edit/team/{orderCode}")
    public String addTeam(@PathVariable(value = "orderCode") String orderCode, String playerCode,
                          Model model) {
        try {
            PayOrder order = orderService.getOrderDetail(orderCode);
            if (order == null) {
                return "redirect:/game/type";
            }
            Player player = null;
            if (StringUtils.isNotBlank(playerCode)) {
                player = playerService.getPlayerByPlayerCode(playerCode);

            }
            ApplyInfo applyInfo = order.getApplyInfos().get(0);
            String matchCode = applyInfo.getMatchCode();
            SpAthletesEnrollModel athietes = null;
            try {
                athietes = spAthletesFacadeClient.getAthietes(matchCode);
            } catch (Exception e) {
                log.error("", e);
            }
            model.addAttribute("players", PlayerVo.getVos(player, athietes));
            model.addAttribute("playerCode", playerCode);
            model.addAttribute("orderCode", order.getOrderCode());
            model.addAttribute("seq", SeqWorkerUtil.buildSingleId());
            return "pages/edit_team";
        } catch (Exception e) {
            log.error("", e);
        }
        return "redirect:/game/type";
    }

    /**
     * 获取成员详情
     * 
     * @param orderCode
     * @param model
     * @return
     */
    @RequestMapping("team/detail/{orderCode}/{playerCode}")
    public String teamDetail(@PathVariable(value = "playerCode") String playerCode,
                             @PathVariable(value = "orderCode") String orderCode, Model model) {
        try {
            Player player = playerService.getPlayerByPlayerCode(playerCode);
            PayOrder order = orderService.getOrderDetail(orderCode);
            if (order == null) {
                return "redirect:/game/type";
            }
            ApplyInfo applyInfo = order.getApplyInfos().get(0);
            String matchCode = applyInfo.getMatchCode();
            SpAthletesEnrollModel athietes = null;
            try {
                athietes = spAthletesFacadeClient.getAthietes(matchCode);
            } catch (Exception e) {
                log.error("", e);
            }
            model.addAttribute("players", PlayerVo.getVos(player, athietes));
            return "pages/team_detail";
        } catch (Exception e) {
            log.error("", e);
        }
        return "redirect:/game/type";
    }

    /**
     * 获取成员详情
     * 
     * @param orderCode
     * @param model
     * @return
     */
    @RequestMapping("modify/team/{orderCode}/{playerCode}")
    public String modifyTeam(@PathVariable(value = "playerCode") String playerCode,
                             @PathVariable(value = "orderCode") String orderCode, Model model) {
        try {
            Player player = playerService.getPlayerByPlayerCode(playerCode);
            PayOrder order = orderService.getOrderDetail(orderCode);
            if (order == null) {
                return "redirect:/game/type";
            }
            ApplyInfo applyInfo = order.getApplyInfos().get(0);
            String matchCode = applyInfo.getMatchCode();
            SpAthletesEnrollModel athietes = null;
            try {
                athietes = spAthletesFacadeClient.getAthietes(matchCode);
            } catch (Exception e) {
                log.error("", e);
            }
            model.addAttribute("players", PlayerVo.getVos(player, athietes));
            return "pages/modify_team";
        } catch (Exception e) {
            log.error("", e);
        }
        return "redirect:/game/type";
    }

    @RequestMapping(value = "del/team")
    @ResponseBody
    public Map<String, Object> del(@RequestParam(value = "playerCode", required = true) String playerCode,
                                   HttpServletRequest request, HttpSession session) {
        try {
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            RegisterModel register = registerFacadeClient
                .getRegsiterByRegisterCode(auth.getRegisterCode());
            if (StringUtils.isBlank(register.getAccount())) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.UN_BIND_PHONE.getCode(),
                    "用户未绑定手机号");
            }
            playerService.deletTeamMembers(playerCode);
            return JsonResultUtil.getSuccessResult();
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("删除团队成员失败");
        }
    }

    private Boolean isDouble(String str) {
        try {
            if (Double.parseDouble(str) > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
            // TODO: handle exception
        }
    }
}
