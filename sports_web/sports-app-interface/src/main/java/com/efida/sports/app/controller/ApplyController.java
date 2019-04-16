/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sport.admin.facade.model.SpAthletesEnrollModel;
import com.efida.sports.app.vo.PlayerVo;
import com.efida.sports.config.PlayerProConfig;
import com.efida.sports.entity.LeaderInfo;
import com.efida.sports.entity.Player;
import com.efida.sports.enums.ErrorCodeEnum;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.IPayOrderService;
import com.efida.sports.service.IPlayerService;
import com.efida.sports.service.dubbo.intergration.SpAthletesFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterLoginFacadeClient;
import com.efida.sports.util.DateUtil;
import com.efida.sports.util.JsonResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * Created by wangyan on 2018/7/25.
 */
@RestController()
@Api(value = "applyApi", tags = { "团体报名相关接口" })
@RequestMapping(value = "/api/apply", produces = "application/json")
public class ApplyController {

    @Autowired
    private CacheService             cacheService;
    @Autowired
    private IPayOrderService         orderService;
    @Autowired
    private IPlayerService           playerService;

    @Autowired
    private SpAthletesFacadeClient   spAthletesFacadeClient;
    @Autowired
    private UcenterLoginFacadeClient loginServiceClient;

    private static Logger            log = LoggerFactory.getLogger(OrderController.class);

    @ApiOperation(value = "添加团队成员", notes = "添加团队成员")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "matchCode", value = "赛事编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "orderCode", value = "订单编号", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerCode", value = "运动员编号", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerPhone", value = "运动员电话号码", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerName", value = "运动员名称", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "sex", value = "性别(m:男,f:女)", required = true, dataType = "String", paramType = "form"),
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
                         @ApiImplicitParam(name = "imgUrl", value = "头像地址", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query") })
    @RequestMapping(value = "add/term", method = { RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> createTteamMembers(@RequestParam(value = "matchCode", required = true) String matchCode,
                                                  @RequestParam(value = "orderCode", required = true) String orderCode,
                                                  @RequestParam(value = "playerCode", required = false) String playerCode,
                                                  @RequestParam(value = "playerPhone", required = true) String playerPhone,
                                                  @RequestParam(value = "playerName", required = true) String playerName,
                                                  @RequestParam(value = "sex", required = true) String sex,
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
                                                  @RequestParam(value = "playerWorkuUnit", required = false) String playerWorkuUnit,
                                                  @RequestParam(value = "playerEmergencyName", required = false) String playerEmergencyName,
                                                  @RequestParam(value = "playerEmergencyPhone", required = false) String playerEmergencyPhone,
                                                  @RequestParam(value = "attOne", required = false) String attOne,
                                                  @RequestParam(value = "attTwo", required = false) String attTwo,
                                                  @RequestParam(value = "assettoId", required = false) String assettoId,
                                                  @RequestParam(value = "attUrl", required = false) String attUrl,
                                                  @RequestParam(value = "imgUrl", required = false) String imgUrl,
                                                  @RequestParam(value = "token", required = true) String token,
                                                  HttpServletRequest request) {
        try {
            log.info("******************************************add/term   matchCode=" + matchCode);
            log.info("******************************************add/term   orderCode=" + orderCode);
            log.info(
                "******************************************add/term   playerPhone=" + playerPhone);
            log.info(
                "******************************************add/term  playerName=" + playerName);
            log.info("******************************************add/term   sex=" + sex);
            log.info("******************************************add/term  token=" + token);
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            //Register register = loginServiceClient.getRegisterByToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            if (StringUtils.isBlank(matchCode)) {
                return JsonResultUtil.getServerErrorResult("添加团队成员失败，赛事编码为空。");
            }
            Player player = new Player();
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
                if (playerPro.contains(paraName) || "orderCode".equals(paraName)) {
                    continue;
                }
                extProMap.put(paraName, request.getParameter(paraName));
            }
            player.setPlayerCode(playerCode);
            player.setExtProMap(extProMap);
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
            player.setPlayerWorkUnit(playerWorkuUnit);
            player.setAttOne(attOne);
            player.setAttTwo(attTwo);
            player.setAttUrl(attUrl);
            player.setAssettoId(assettoId);
            playerService.checkPlayer(player);
            log.info("******************************************add/term  验证运动员属性");
            //            String cacheVerifyCode = cacheService.get(Constants.VERIFY_CODE_KEY + playerPhone);
            //            if (!verifyCode.equals(cacheVerifyCode)) {
            //                throw new BusinessException("验证码错误，请重新输入");
            //            }
            Player newPlayer = orderService.createTeamMembers(player, orderCode,
                register.getRegisterCode());
            log.info("******************************************add/term  添加团队成员");
            SpAthletesEnrollModel athietes = spAthletesFacadeClient.getAthietes(matchCode);

            log.info("******************************************add/term  通过赛事编号获取指定赛事运动员报名模板信息");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("players", PlayerVo.getVos(newPlayer, athietes));
            map.put("playerCode", newPlayer.getPlayerCode());
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
                         @ApiImplicitParam(name = "verifyCode", value = "验证码", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "leaderName", value = "领队名称", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "leaderPhone", value = "领队电话号码", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query") })
    @RequestMapping(value = "save/leader", method = { RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> saveLeader(@RequestParam(value = "orderCode", required = true) String orderCode,
                                          @RequestParam(value = "teamName", required = false) String teamName,
                                          @RequestParam(value = "verifyCode", required = false) String verifyCode,
                                          @RequestParam(value = "leaderName", required = false) String leaderName,
                                          @RequestParam(value = "leaderPhone", required = false) String leaderPhone,
                                          @RequestParam(value = "token", required = true) String token,
                                          HttpServletRequest request) {
        log.info("******************************************save/leader  orderCode=" + orderCode);
        log.info("******************************************save/leader   token=" + token);
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            log.info("******************************************RegisterCode="
                     + register.getRegisterCode());
            LeaderInfo leader = new LeaderInfo();
            leader.setTeamName(teamName);
            leader.setLeaderName(leaderName);
            leader.setLeaderPhone(leaderPhone);
            orderService.saveLearnerInfo(leader, register.getRegisterCode(), orderCode);
            log.info("******************************************保存领队信息成功");
            return JsonResultUtil.getSuccessResult();
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("保存领队信息失败");
        }
    }

    @ApiOperation(value = "删除团队成员", notes = "删除团队成员")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "playerCode", value = "运动员编号", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query") })
    @RequestMapping(value = "del/team", method = { RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> del(@RequestParam(value = "playerCode", required = true) String playerCode,
                                   @RequestParam(value = "token", required = true) String token) {
        log.info("******************************************del/team  playerCode=" + playerCode);
        log.info("******************************************del/team   token=" + token);
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            log.info("******************************************RegisterCode="
                     + register.getRegisterCode());
            playerService.deletTeamMembers(playerCode);
            log.info("******************************************删除团队成员成功");
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
