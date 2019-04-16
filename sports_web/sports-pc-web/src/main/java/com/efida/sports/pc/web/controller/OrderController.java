/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.pc.web.controller;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.plugins.Page;
import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sport.admin.facade.enums.MatchRegEums;
import com.efida.sport.admin.facade.model.SpAthletesEnrollModel;
import com.efida.sport.admin.facade.model.SpMatchAllInfoModel;
import com.efida.sports.config.WeichatConfig;
import com.efida.sports.constants.Constants;
import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.entity.LeaderInfo;
import com.efida.sports.entity.PayOrder;
import com.efida.sports.entity.Player;
import com.efida.sports.entity.SiteOrderDto;
import com.efida.sports.enums.ErrorCodeEnum;
import com.efida.sports.enums.IdCardTypeEnum;
import com.efida.sports.enums.OrderStatusEnum;
import com.efida.sports.enums.SexEnum;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.model.WeichatUnifiedModel;
import com.efida.sports.pc.web.config.AliPayConfig;
import com.efida.sports.pc.web.enums.MatchItemTypeEums;
import com.efida.sports.pc.web.enums.OrderCurentStatusEnum;
import com.efida.sports.pc.web.vo.GroupItemVo;
import com.efida.sports.pc.web.vo.LeaderInfoVo;
import com.efida.sports.pc.web.vo.PayOrderVo;
import com.efida.sports.pc.web.vo.PlayerPropertyVo;
import com.efida.sports.pc.web.vo.PlayerVo;
import com.efida.sports.pc.web.vo.SiteVo;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.IApplyInfoService;
import com.efida.sports.service.IPayOrderService;
import com.efida.sports.service.IPlayerService;
import com.efida.sports.service.MsgService;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.service.dubbo.intergration.SpAthletesFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpMatchFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;
import com.efida.sports.util.AmountUtils;
import com.efida.sports.util.DateUtil;
import com.efida.sports.util.GenerateQrCodeUtil;
import com.efida.sports.util.HttpUtils;
import com.efida.sports.util.JsonResultUtil;
import com.efida.sports.util.RegexUtils;
import com.efida.sports.util.ServletUtil;
import com.efida.sports.util.XMLUtil;
import com.efida.sports.util.weichat.WeichatUtil;

/**
 * 
 * @author 
 * @version $Id: OrderController.java, v 0.1 2018年5月22日 下午3:54:59 Exp $
 */
@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private IPayOrderService            orderServce;

    @Autowired
    private WeichatConfig               weichatConfig;

    @Autowired
    private AliPayConfig                alipayConfig;

    @Autowired
    private CacheService                cacheService;

    @Value("${ucenter-domain}")
    public String                       ucenterDomain;
    @Value("${apply-domain}")
    public String                       applyDomain;

    //    @Autowired
    //    private IPayOrderService       iPayOrderService;

    @Autowired
    private SpAthletesFacadeClient      spAthletesFacadeClient;

    @Autowired
    private SpMatchFacadeClient         spMatchFacadeClient;

    @Autowired
    private IApplyInfoService           applyInfoService;

    @Autowired
    private MsgService                  msgService;

    @Autowired
    private IPlayerService              iPlayerService;

    private static Logger               log          = LoggerFactory
        .getLogger(OrderController.class);

    private AlipayClient                alipayClient = null;

    @Autowired
    private UcenterAdapterService       ucenterAdapterService;
    @Autowired
    private UcenterRegisterFacadeClient registerFacadeClient;

    /**
     * 生成初始化订单
     * 
     * @param eventdCodes
     * @param siteCode
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("apply/init")
    @ResponseBody
    public Map<String, Object> createOderPay(@RequestBody List<SiteOrderDto> sites, Model model,
                                             HttpServletRequest request,
                                             HttpServletResponse response, HttpSession session) {
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            if (authToken == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登陆");
            }
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            String ip = ServletUtil.getIpAddress(request);
            if (sites == null || sites.size() <= 0) {
                return JsonResultUtil.getServerErrorResult("请选择赛事信息");
            }
            PayOrder payOrder = orderServce.createWaitComplete(sites, ip,
                authToken.getRegisterCode());
            //            weichatUnified(payOrder, response);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderCode", payOrder.getOrderCode());
            map.put("matchCode", sites.get(0).getMatchCode());
            return JsonResultUtil.getSuccessResult("创建成功", map);
        } catch (Exception e) {
            log.error("创建初始化订单失败", e);
            return JsonResultUtil.getServerErrorResult("订单保存失败");
        }
    }

    /**
     * 团体报名
     * 
     * @param orderCode
     * @param session
     * @return
     */
    @RequestMapping(value = "enrollGroup/{orderCode}/{matchCode}/{groupLimit}", produces = "application/json; charset=utf-8")
    public String playerGroupEnroll(@PathVariable(value = "orderCode") String orderCode,
                                    @PathVariable(value = "matchCode") String matchCode,
                                    @PathVariable(value = "groupLimit") int groupLimit, Model model,
                                    HttpSession session) {
        PayOrder payOrder = null;
        RegisterModel register = null;
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            String uri = applyDomain + "/order/order/enrollGroup/" + orderCode + "/" + matchCode;
            uri = URLEncoder.encode(uri, "UTF-8");
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
            payOrder = orderServce.getOrderByCode(orderCode);
        } catch (Exception e) {
            log.error("", e);
        }
        model.addAttribute("orderCode", payOrder.getOrderCode());
        model.addAttribute("matchCode", matchCode);
        model.addAttribute("groupLimit", groupLimit);
        model.addAttribute("register", register);
        //        model.addAttribute("player", PlayerVo.getPlayerVoList(payOrder.getPlayers()));
        //model.addAttribute("leader", LeaderInfoVo.getLeaderInfoVo(payOrder.getLeaderInfo()));
        return "pages/group_enroll";
        //        return "pages/group_enroll_perfect";
    }

    /**
     * 查询动态属性
     * 
     * @param request
     * @param model
     * @param session
     * @param orderCode
     * @param player
     * @return
     */
    @RequestMapping("query/dynamicAttr")
    @ResponseBody
    public Map<String, Object> queryDynamicAttr(@RequestParam(value = "matchCode", required = false) String matchCode,
                                                HttpServletRequest request, Model model,
                                                HttpSession session) {
        SpAthletesEnrollModel athietes = null;
        try {
            athietes = spAthletesFacadeClient.getAthietes(matchCode);
            //特殊处理
            Map<String, Object> map = PlayerPropertyVo.getMap(athietes);
            map.remove("imgUrl");
            PlayerPropertyVo phone = (PlayerPropertyVo) map.get("playerEmergencyPhone");
            if (phone == null) {
                PlayerPropertyVo vo = new PlayerPropertyVo();
                vo.setName("紧急联系电话");
                vo.setAttributeName("playerEmergencyPhone");
                vo.setIsShow(false);
                vo.setIsRequired(false);
                vo.setIsCustom(false);
                map.put("playerEmergencyPhone", vo);
            }
            PlayerPropertyVo name = (PlayerPropertyVo) map.get("playerEmergencyName");
            if (name == null) {
                PlayerPropertyVo vo = new PlayerPropertyVo();
                vo.setName("紧急联系人");
                vo.setAttributeName("playerEmergencyName");
                vo.setIsShow(false);
                vo.setIsRequired(false);
                vo.setIsCustom(false);
                map.put("playerEmergencyName", vo);
            }
            PlayerPropertyVo unit = (PlayerPropertyVo) map.get("playerWorkUnit");
            if (unit == null) {
                PlayerPropertyVo vo = new PlayerPropertyVo();
                vo.setName("工作单位");
                vo.setAttributeName("playerWorkUnit");
                vo.setIsShow(false);
                vo.setIsRequired(false);
                vo.setIsCustom(false);
                map.put("playerWorkUnit", vo);
            }
            model.addAttribute("pro", map);
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("动态属性查询失败");
        }
    }

    /**
     * 查询运动员信息
     * 
     * @param request
     * @param model
     * @param session
     * @param orderCode
     * @param player
     * @return
     */
    @RequestMapping("query/player")
    @ResponseBody
    public Map<String, Object> queryPlayer(@RequestParam(value = "playerCode", required = false) String playerCode,
                                           @RequestParam(value = "matchCode", required = false) String matchCode,
                                           HttpServletRequest request, Model model,
                                           HttpSession session) {
        RegisterModel register = null;
        Map<String, Object> map = new HashMap<String, Object>();
        SpAthletesEnrollModel athietes = null;
        try {
            athietes = spAthletesFacadeClient.getAthietes(matchCode);
            map.put("pro", PlayerPropertyVo.getMap(athietes));
            register = ucenterAdapterService.getPCAuthToken(session).getRegister();
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登陆");
            }
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            Player player = iPlayerService.getPlayerByPlayerCode(playerCode);
            PlayerVo vo = PlayerVo.getPlayerVo(player);
            if (StringUtils.isBlank(vo.getPlayerBirthStr())) {
                Date date = vo.getPlayerBirth();
                log.info("出生日期：" + DateUtil.formatWeb(date));
                vo.setPlayerBirthStr(DateUtil.formatWeb(date));
            }
            map.put("player", PlayerVo.getPlayerVo(player));
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("报名失败，请稍后重试！！");
        }
    }

    /**
     * 删除运动员信息
     * 
     * @param request
     * @param model
     * @param session
     * @param orderCode
     * @param player
     * @return
     */
    @RequestMapping("player/delete")
    @ResponseBody
    public Map<String, Object> deletePlayer(@RequestParam(value = "playerCode", required = false) String playerCode,
                                            HttpServletRequest request, Model model,
                                            HttpSession session) {
        RegisterModel register = null;
        try {
            register = ucenterAdapterService.getPCAuthToken(session).getRegister();
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登陆");
            }
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            if (StringUtils.isBlank(playerCode)) {
                log.error("playerCode不能为空");
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "删除失败");
            }
            iPlayerService.deletTeamMembers(playerCode);
            return JsonResultUtil.getSuccessResult("删除成功");
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("删除失败，请稍后重试！！");
        }
    }

    /**
     * 保存团队成员
     * 
     * @param request
     * @param model
     * @param session
     * @param orderCode
     * @param player
     * @return
     */
    @RequestMapping("save/team/member")
    @ResponseBody
    public Map<String, Object> saveTeamMember(Player player, String orderCode,
                                              HttpServletRequest request, Model model,
                                              HttpSession session) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            if (authToken == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登陆");
            }
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            PayOrder payOrder = orderServce.getOrderDetail(orderCode);
            int groupLimit = payOrder.getPersonGroup();
            if (groupLimit != 0 && payOrder.getPlayers().size() + 1 > groupLimit) {
                return JsonResultUtil.getServerErrorResult("运动员名额已满");
            }
            if (StringUtils.isNotBlank(player.getPlayerBirthStr())) {
                player.setPlayerBirth(DateUtil.strToDate1(player.getPlayerBirthStr()));
            }
            iPlayerService.checkPlayer(player);
            //            String verifyCode = player.getVerifyCode();
            //            String cacheVerifyCode = cacheService
            //                .get(Constants.VERIFY_CODE_KEY + player.getPlayerPhone());
            //            if (!verifyCode.equals(cacheVerifyCode)) {
            //                return JsonResultUtil.getFailResult(ErrorCodeEnum.VERIFY_CODE.getCode(), "验证码输入错误");
            //            }
            String cardType = player.getPlayerCerType();
            String playerCerNo = player.getPlayerCerNo();
            if (IdCardTypeEnum.ID_CARD.getCode().equals(cardType)
                && StringUtils.isNotBlank(playerCerNo)) {
                boolean bool = RegexUtils.checkIdCard(playerCerNo);
                if (!bool) {
                    return JsonResultUtil.getFailResult(ErrorCodeEnum.ID_CARD_ERROR.getCode(),
                        "身份证号码格式错误");
                }
            }
            if (IdCardTypeEnum.PASSPORT.getCode().equals(cardType)
                && StringUtils.isNotBlank(playerCerNo)) {
                boolean bool = RegexUtils.checkPassport(playerCerNo);
                if (!bool) {
                    return JsonResultUtil.getFailResult(ErrorCodeEnum.ID_CARD_ERROR.getCode(),
                        "护照号码格式错误");
                }
            }
            if (IdCardTypeEnum.CERTIFICATE_OFFICERS.getCode().equals(cardType)
                && StringUtils.isNotBlank(playerCerNo)) {
                boolean bool = RegexUtils.checkCertificateOfficers(playerCerNo);
                if (!bool) {
                    return JsonResultUtil.getFailResult(ErrorCodeEnum.ID_CARD_ERROR.getCode(),
                        "军官证格式错误");
                }
            }
            //            if (StringUtils.isNotBlank(player.getAssettoId())) {
            //                boolean bool = RegexUtils.checkAssettoId(player.getAssettoId());
            //                if (!bool) {
            //                    return JsonResultUtil.getFailResult(ErrorCodeEnum.ID_CARD_ERROR.getCode(),
            //                        "神力科莎游戏昵称格式错误");
            //                }
            //            }
            Map<String, Object> expmap = expConvert(player.getExtPro());
            player.setExtProMap(expmap);
            player.setRegisterCode(authToken.getRegisterCode());
            Player newplayer = orderServce.createTeamMembers(player, orderCode,
                authToken.getRegisterCode());
            map.put("player", PlayerVo.getPlayerVo(newplayer));
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("报名失败，请稍后重试！！");
        }
    }

    /**
     * 保存团队报名信息
     * 
     * @param request
     * @param model
     * @param session
     * @param orderCode
     * @param player
     * @return
     */
    @RequestMapping("save/group")
    @ResponseBody
    public Map<String, Object> saveGroup(LeaderInfoVo leader, String orderCode,
                                         HttpServletRequest request, Model model,
                                         HttpSession session) {
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            if (authToken == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登陆");
            }
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            LeaderInfo leaderInfo = LeaderInfoVo.getLeaderInfo(leader);
            orderServce.saveLearnerInfo(leaderInfo, authToken.getRegisterCode(), orderCode);
            return JsonResultUtil.getSuccessResult("保存成功");
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("保存失败，请稍后重试！！");
        }
    }

    /**
     * 提交团队报名信息
     * 
     * @param request
     * @param model
     * @param session
     * @param orderCode
     * @param player
     * @return
     */
    @RequestMapping("commit/group")
    @ResponseBody
    public Map<String, Object> commitGroup(LeaderInfoVo leader, String orderCode, String type,
                                           HttpServletRequest request, Model model,
                                           HttpSession session) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            if (authToken == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登陆");
            }
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            String verifyCode = leader.getVerifyCode();
            boolean checkVerifyCode = msgService.checkVerifyCode(leader.getLeaderPhone(),
                verifyCode);
            if (!checkVerifyCode) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.VERIFY_CODE.getCode(), "验证码输入错误");
            }
            LeaderInfo leaderInfo = LeaderInfoVo.getLeaderInfo(leader);
            orderServce.completeTeamApply(leaderInfo, orderCode, authToken.getRegisterCode());
            PayOrder oldOrder = orderServce.checkOrderCanPay(orderCode);
            if (oldOrder.getOrderAmount() == 0) {
                orderServce.completePaySuccess(orderCode, null, null);
                PayOrder payOrder = orderServce.getOrderDetail(orderCode);
                sendSuccessMessage(authToken.getRegister(), payOrder);
                map.put("url", "/order/success/" + orderCode);
            } else {
                if ("alipay".equals(type)) {
                    map.put("url", "/order/alipay/" + orderCode);
                } else if ("wechatpay".equals(type)) {
                    map.put("url", "/order/weichatpay/" + orderCode);
                }
            }
            return JsonResultUtil.getSuccessResult("报名成功", map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("报名失败，请稍后重试！！");
        }
    }

    /**
     * 查询团队报名信息
     * 
     * @param request
     * @param model
     * @param session
     * @param orderCode
     * @param player
     * @return
     */
    @RequestMapping("query/team")
    @ResponseBody
    public Map<String, Object> queryTeam(String orderCode, HttpServletRequest request, Model model,
                                         HttpSession session) {
        RegisterModel register = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            register = ucenterAdapterService.getPCAuthToken(session).getRegister();
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登陆");
            }
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            PayOrder order = orderServce.getOrderDetail(orderCode);
            LeaderInfo leader = order.getLeaderInfo();
            List<Player> players = order.getPlayers();
            map.put("leader", LeaderInfoVo.getLeaderInfoVo(leader));
            map.put("players", PlayerVo.getPlayerVoList(players));
            return JsonResultUtil.getSuccessResult("团队信息查询成功", map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("报名失败，请稍后重试！！");
        }
    }

    /**
     * 运动员报名
     * 
     * @param orderCode
     * @param session
     * @return
     */
    @RequestMapping(value = "enroll/{orderCode}/{matchCode}", produces = "application/json; charset=utf-8")
    public String playerEnroll(@PathVariable(value = "orderCode") String orderCode,
                               @PathVariable(value = "matchCode") String matchCode, Model model,
                               HttpSession session) {
        PayOrder payOrder = null;
        RegisterModel register = null;
        SpAthletesEnrollModel athietes = null;
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            if (authToken == null) {

            }
            register = registerFacadeClient.getRegsiterByRegisterCode(authToken.getRegisterCode());
            if (register == null) {

            }
            if (StringUtils.isBlank(register.getAccount())) {
                model.addAttribute("registerCode", register.getRegisterCode());
                model.addAttribute("url", "/order/enroll/" + orderCode + "/" + matchCode);
                return "pages/bind_login";
            }
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            payOrder = orderServce.getOrderByCode(orderCode);
            athietes = spAthletesFacadeClient.getAthietes(matchCode);
        } catch (Exception e) {
            log.error("", e);
        }
        Map<String, Object> map = PlayerPropertyVo.getMap(athietes);
        model.addAttribute("pro", map);
        model.addAttribute("orderCode", payOrder.getOrderCode());
        model.addAttribute("register", register);
        return "pages/player_enroll";
    }

    /**
     * 提交运动员信息
     * 
     * @param request
     * @param model
     * @param session
     * @param orderCode
     * @param player
     * @return
     */
    @RequestMapping("enroll/commit")
    @ResponseBody
    public Map<String, Object> enrollCommit(HttpServletRequest request, Model model,
                                            HttpSession session, Player player, String orderCode,
                                            String type) {
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            if (StringUtils.isNotBlank(player.getPlayerBirthStr())) {
                Date date = DateUtil.strToDate1(player.getPlayerBirthStr());
                player.setPlayerBirth(date);
            }
            iPlayerService.checkPlayer(player);
            String verifyCode = player.getVerifyCode();
            boolean checkVerifyCode = msgService.checkVerifyCode(player.getPlayerPhone(),
                verifyCode);
            if (!checkVerifyCode) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.VERIFY_CODE.getCode(), "验证码输入错误");
            }
            String cardType = player.getPlayerCerType();
            String playerCerNo = player.getPlayerCerNo();
            if (IdCardTypeEnum.ID_CARD.getCode().equals(cardType)
                && StringUtils.isNotBlank(playerCerNo)) {
                boolean bool = RegexUtils.checkIdCard(playerCerNo);
                if (!bool) {
                    return JsonResultUtil.getFailResult(ErrorCodeEnum.ID_CARD_ERROR.getCode(),
                        "身份证号码格式错误");
                }
            }
            if (IdCardTypeEnum.PASSPORT.getCode().equals(cardType)
                && StringUtils.isNotBlank(playerCerNo)) {
                boolean bool = RegexUtils.checkPassport(playerCerNo);
                if (!bool) {
                    return JsonResultUtil.getFailResult(ErrorCodeEnum.ID_CARD_ERROR.getCode(),
                        "护照号码格式错误");
                }
            }
            if (IdCardTypeEnum.CERTIFICATE_OFFICERS.getCode().equals(cardType)
                && StringUtils.isNotBlank(playerCerNo)) {
                boolean bool = RegexUtils.checkCertificateOfficers(playerCerNo);
                if (!bool) {
                    return JsonResultUtil.getFailResult(ErrorCodeEnum.ID_CARD_ERROR.getCode(),
                        "军官证格式错误");
                }
            }
            //            if (StringUtils.isNotBlank(player.getAssettoId())) {
            //                boolean bool = RegexUtils.checkAssettoId(player.getAssettoId());
            //                if (!bool) {
            //                    return JsonResultUtil.getFailResult(ErrorCodeEnum.ID_CARD_ERROR.getCode(),
            //                        "神力科莎游戏昵称格式错误");
            //                }
            //            }

            PayOrder oldOrder = orderServce.checkOrderCanPay(orderCode);
            Map<String, Object> map = new HashMap<String, Object>();

            Map<String, Object> expmap = expConvert(player.getExtPro());
            player.setExtProMap(expmap);
            orderServce.completeOrder(orderCode, authToken.getRegisterCode(), player);
            if (oldOrder.getOrderAmount() == 0) {
                orderServce.completePaySuccess(orderCode, null, null);
                PayOrder payOrder = orderServce.getOrderDetail(orderCode);
                sendSuccessMessage(authToken.getRegister(), payOrder);
                map.put("url", "/order/success/" + orderCode);
            } else {
                if ("alipay".equals(type)) {
                    map.put("url", "/order/alipay/" + orderCode);
                } else if ("wechatpay".equals(type)) {
                    map.put("url", "/order/weichatpay/" + orderCode);
                }
            }
            map.put("orderCode", orderCode);
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("报名失败，请稍后重试！！");
        }
    }

    public Map<String, Object> expConvert(String exps) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(exps)) {
            return map;
        }
        JSONArray arrays = JSONArray.parseArray(exps);
        for (int i = 0; i < arrays.size(); i++) {
            JSONObject obj = arrays.getJSONObject(i);
            Set<String> entrySet = obj.keySet();
            Iterator<String> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next().toString();
                String value = obj.getString(key);
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * 微信支付
     * 
     * @param orderCode
     * @param response
     */
    @RequestMapping(value = "weichatpay/{orderCode}", produces = "application/json; charset=utf-8")
    public String wechatPay(@PathVariable(value = "orderCode") String orderCode,
                            HttpSession session, Model model, HttpServletResponse response) {
        //获取当前用户
        RegisterModel register = ucenterAdapterService.getPCAuthToken(session).getRegister();
        if (register == null) {
            return "redirect:/match/type";
        }
        PayOrder payOrder = orderServce.getOrderDetail(orderCode);
        payOrder.setOrderAmountStr(AmountUtils.changeF2Y(payOrder.getOrderAmount()));

        String status = getOrderStatus(payOrder);
        if (OrderCurentStatusEnum.WAIT_PAY.getCode().equals(status)) {
            Integer plusTime = (int) (payOrder.getExpireTime().getTime() - new Date().getTime())
                               / 1000;
            payOrder.setOverplusTime(plusTime);
        }
        if (payOrder.getOverplusTime() == null) {
            payOrder.setOverplusTime(0);
        }
        //        weichatUnified(payOrder, response);
        model.addAttribute("payOrder", payOrder);
        if (OrderStatusEnum.SUCCESS.getCode().equals(payOrder.getOrderStatus())
            || payOrder.getOrderAmount() == 0) {
            //            sendSuccessMessage(auth, payOrder);
            return "pages/pay_success";
        }
        return "pages/code_pay";
    }

    /**
     * 微信支付
     * 
     * @param orderCode
     * @param response
     */
    @RequestMapping(value = "wechatPayCode/{orderCode}", produces = "application/json; charset=utf-8")
    public void wechatPayCode(@PathVariable(value = "orderCode") String orderCode,
                              HttpSession session, Model model, HttpServletResponse response,
                              HttpServletRequest request) {
        PayOrder payOrder = orderServce.getOrderDetail(orderCode);
        String ip = ServletUtil.getIpAddress(request);
        weichatUnified(payOrder, response, ip);
    }

    /**
     * 微信支付
     * 
     * @param orderCode
     * @param response
     */
    @RequestMapping(value = "wechat/check/{orderCode}", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map<String, Object> wechatCheckStatus(@PathVariable(value = "orderCode") String orderCode,
                                                 HttpSession session, Model model,
                                                 HttpServletResponse response) {
        try {
            PayOrder payOrder = orderServce.getOrderDetail(orderCode);
            Map<String, Object> map = new HashMap<>();
            map.put("orderStatus", payOrder.getOrderStatus());
            return JsonResultUtil.getSuccessResult("订单状态查询成功", map);
        } catch (Exception e) {
            log.error("订单查询失败", e);
            return JsonResultUtil.getServerErrorResult("订单状态查询失败");
        }

    }

    /**
     * 阿里支付
     * 
     * @param orderCode
     * @param response
     */
    @RequestMapping(value = "alipay/{orderCode}", produces = "application/json; charset=utf-8")
    public void aliOrderPay(@PathVariable(value = "orderCode") String orderCode,
                            HttpServletResponse response) {
        try {
            PayOrder payOrder = orderServce.checkOrderCanPay(orderCode);
            initClient();
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

            AlipayTradePagePayModel model = new AlipayTradePagePayModel();
            model.setBody("全国智能体育报名费");
            model.setSubject("报名费");
            model.setOutTradeNo(payOrder.getOrderCode());
            model.setTimeoutExpress(alipayConfig.overTime);
            model.setTotalAmount("0.01");//AmountUtils.changeF2Y(order.getOrderAmount())
            model.setProductCode("FAST_INSTANT_TRADE_PAY");
            //            model.setProductCode("QUICK_MSECURITY_PAY");
            alipayRequest.setBizModel(model);
            alipayRequest.setNotifyUrl(alipayConfig.notifyUrl);// 在公共参数中设置回跳和通知地址

            String body = alipayClient.pageExecute(alipayRequest).getBody();
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(body);
            response.getWriter().flush();
        } catch (BusinessException e) {
            log.error("阿里支付失败", e.getMessage());
        } catch (Exception e) {
            log.error("扫码支付失败", e);
        }
    }

    private void initClient() {
        if (alipayClient == null) {
            alipayClient = new DefaultAlipayClient(alipayConfig.aLiUrl, alipayConfig.appId,
                alipayConfig.privateKey, "json", "utf-8", alipayConfig.publicKey,
                alipayConfig.encryptionMethod);
        }
    }

    /**
     * 保存运动员信息
     * 
     * @param request
     * @param model
     * @param session
     * @param orderCode
     * @param player
     * @return
     */
    @RequestMapping("enroll/save")
    @ResponseBody
    public Map<String, Object> enrollSave(HttpServletRequest request, Model model,
                                          HttpSession session, Player player, String orderCode) {
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            //            checkaddApply(player);
            if (StringUtils.isNotBlank(player.getPlayerBirthStr())) {
                Date date = DateUtil.strToDate1(player.getPlayerBirthStr());
                player.setPlayerBirth(date);
            }
            Map<String, Object> expmap = expConvert(player.getExtPro());
            player.setExtProMap(expmap);
            PayOrder order = orderServce.savePlayer(orderCode, authToken.getRegisterCode(), player);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("order", order);
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("报名失败，请稍后重试！！");
        }
    }

    /**
     * 运动员报名详细
     * 
     * @param orderCode
     * @param session
     * @return
     */
    @RequestMapping(value = "enroll/detail/{orderCode}/{matchCode}", produces = "application/json; charset=utf-8")
    public String playerEnrollDetail(@PathVariable(value = "orderCode") String orderCode,
                                     @PathVariable(value = "matchCode") String matchCode,
                                     Model model, HttpSession session) {
        PayOrder payOrder = null;
        RegisterModel register = null;
        SpAthletesEnrollModel athietes = null;
        String itemType = "";
        SpMatchAllInfoModel spmodel = null;
        String unitCode = "";
        try {
            register = ucenterAdapterService.getPCAuthToken(session).getRegister();
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            payOrder = orderServce.getOrderDetail(orderCode);
            payOrder.setOrderAmountStr(AmountUtils.changeF2Y(payOrder.getOrderAmount()));
            athietes = spAthletesFacadeClient.getAthietes(matchCode);
        } catch (Exception e) {
            log.error("", e);
        }
        Map<String, Object> map = PlayerPropertyVo.getMap(athietes);
        List<Player> list = payOrder.getPlayers();
        List<ApplyInfo> infos = payOrder.getApplyInfos();
        List<SiteVo> sites = new ArrayList<SiteVo>();
        if (infos != null && infos.size() > 0) {
            unitCode = infos.get(0).getUnitCode();
            List<String> siteCodes = getSiteCode(infos);
            for (String siteCode : siteCodes) {
                SiteVo siteVo = new SiteVo();
                List<GroupItemVo> groupVo = new ArrayList<GroupItemVo>();
                for (ApplyInfo info : infos) {
                    if (siteCode.equals(info.getSiteCode())) {
                        spmodel = spMatchFacadeClient.getItemDetail(info.getSiteCode(),
                            info.getGroupEventCode());
                        if (spmodel != null) {
                            if (spmodel.getPayAreaInfo() != null) {
                                if (StringUtils.isBlank(siteVo.getFieldName())) {
                                    siteVo.setFieldName(spmodel.getPayAreaInfo().getFieldName());
                                }
                                if (StringUtils.isBlank(siteVo.getFieldAddress())) {
                                    siteVo.setFieldAddress(
                                        spmodel.getPayAreaInfo().getFieldAddress());
                                }
                                if (siteVo.getStartTime() == null) {
                                    siteVo.setStartTime(spmodel.getPayAreaInfo().getStartTime());
                                }
                                if (siteVo.getEndTime() == null) {
                                    siteVo.setEndTime(spmodel.getPayAreaInfo().getEndTime());
                                }
                            }
                            GroupItemVo group = new GroupItemVo();
                            if (spmodel.getGroupInfo() != null) {
                                group.setGroupName(spmodel.getGroupInfo().getGroupName());
                                group.setGroupCode(spmodel.getGroupInfo().getGroupCode());
                            }
                            if (spmodel.getGroupItemInfo() != null) {
                                group.setItemCode(spmodel.getGroupItemInfo().getItemCode());
                                group.setItemName(spmodel.getGroupItemInfo().getItemName());
                                group.setStartTime(spmodel.getGroupItemInfo().getStartTime());
                                group.setEndTime(spmodel.getGroupItemInfo().getEndTime());
                                group.setEntryFee(spmodel.getGroupItemInfo().getEntryFee());
                                group.setEntryFeeStr(AmountUtils.changeF2Y(
                                    Long.valueOf(spmodel.getGroupItemInfo().getEntryFee())));
                                Long total = applyInfoService.getApplyCountByEventCode(
                                    spmodel.getGroupItemInfo().getItemCode());
                                int personNum = spmodel.getGroupItemInfo().getPersonLimit();
                                if (0 == personNum) {
                                    group.setSurplusQuota("无人员限制");
                                } else {
                                    long num = Long.valueOf(personNum).longValue() - total;
                                    group.setSurplusQuota((num > 0 ? num : 0) + "");
                                }
                                group.setPersonLimit(personNum);
                            }
                            groupVo.add(group);
                        }
                    }
                    siteVo.setItems(groupVo);
                }
                sites.add(siteVo);
            }
        }
        String status = getOrderStatus(payOrder);
        if (infos != null && infos.size() > 0) {
            itemType = infos.get(0).getEventType();
        }

        if (OrderStatusEnum.WAIT_PAY.getCode().equals(payOrder.getOrderStatus())) {
            payOrder.setOverplusTime(0);
            if (spmodel != null) {
                boolean bool = DateUtil.compareDate(spmodel.getMatchInfo().getEndTime(),
                    new Date());
                if (bool) {
                    boolean bl = DateUtil.compareDate(payOrder.getExpireTime(), new Date());
                    if (bl) {
                        Integer plusTime = (int) (payOrder.getExpireTime().getTime()
                                                  - new Date().getTime())
                                           / 1000;
                        payOrder.setOverplusTime(plusTime);
                    }
                }
            }
        }
        //赛事成功，同时为lantianlvye
        if (OrderStatusEnum.SUCCESS.getCode().equals(payOrder.getOrderStatus())
            && "lantianlvye".equals(unitCode)) {
            List<PlayerPropertyVo> expands = (List<PlayerPropertyVo>) map.get("expands");
            if (expands != null) {
                PlayerPropertyVo vo = new PlayerPropertyVo();
                vo.setAttributeName("参赛验证码");
                vo.setName("参赛验证码");
                vo.setIsShow(true);
                vo.setIsRequired(true);
                vo.setIsCustom(true);
                expands.add(vo);
                map.put("expands", expands);
            }
        }

        Date orderTime = payOrder.getOrderTime();
        if (OrderCurentStatusEnum.WAIT_PERFECT.getCode().equals(status)) {
            return "redirect:/order/apply/edit?orderCode=" + orderCode + "&matchCode=" + matchCode
                   + "&editStatus=1";
        }
        payOrder.setOrderTimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderTime));
        model.addAttribute("pro", map);
        model.addAttribute("register", register);
        model.addAttribute("payOrder", payOrder);
        model.addAttribute("sites", sites);
        model.addAttribute("status", status);
        if (MatchItemTypeEums.PERSONAL.getCode().equals(itemType)) {
            Player player = null;
            if (list != null && list.size() > 0) {
                player = list.get(0);
                log.info("个人信息 ：" + JSON.toJSONString(player));
                player.setSexStr(SexEnum.getDescByCode(player.getSex()));
            }
            model.addAttribute("leader", "");
            model.addAttribute("player", PlayerVo.getPlayerVo(player));
            return "pages/entry_form";
        }
        model.addAttribute("leader", LeaderInfoVo.getLeaderInfoVo(payOrder.getLeaderInfo()));
        model.addAttribute("players", PlayerVo.getPlayerVoList(payOrder.getPlayers()));
        return "pages/group_enroll_detail";

    }

    public String getOrderStatus(PayOrder payOrder) {
        String status = "";
        Date matchDate = null;
        Date regTime = null;
        String regStatus = "";
        try {
            List<ApplyInfo> infos = payOrder.getApplyInfos();
            if (infos != null && infos.size() > 0) {
                log.info(JSON.toJSONString(infos));
                SpMatchAllInfoModel spmodel = spMatchFacadeClient
                    .getItemDetail(infos.get(0).getSiteCode(), infos.get(0).getGroupEventCode());
                if (spmodel != null) {
                    matchDate = spmodel.getMatchInfo().getEndTime();
                    regTime = spmodel.getMatchInfo().getRegTime();
                    regStatus = spmodel.getMatchInfo().getRegStatus();
                }
            }
            if (DateUtil.compareDate(new Date(), regTime)) {
                return OrderCurentStatusEnum.ENROLL_END.getCode();
            }
            if (MatchRegEums.pause.getCode().equals(regStatus)) {
                return OrderCurentStatusEnum.MATCH_PAUSE.getCode();
            }
            if (OrderStatusEnum.WAIT_COMPLETE.getCode().equals(payOrder.getOrderStatus())) {
                boolean bool = DateUtil.compareDate(new Date(), matchDate);
                if (bool) {//已结束
                    status = OrderCurentStatusEnum.WAIT_END.getCode();
                } else {
                    status = OrderCurentStatusEnum.WAIT_PERFECT.getCode();
                }
            } else if (OrderStatusEnum.WAIT_PAY.getCode().equals(payOrder.getOrderStatus())) {
                boolean bool = DateUtil.compareDate(new Date(), matchDate);
                if (bool) {
                    status = OrderCurentStatusEnum.WAIT_PAY_END.getCode();
                } else {
                    boolean bl = DateUtil.compareDate(payOrder.getExpireTime(), new Date());
                    if (bl) {
                        status = OrderCurentStatusEnum.WAIT_PAY.getCode();
                    } else {
                        status = OrderCurentStatusEnum.WAIT_PAY_OVERTIME.getCode();
                    }
                }
            } else if (OrderStatusEnum.SUCCESS.getCode().equals(payOrder.getOrderStatus())) {
                status = OrderCurentStatusEnum.SUCCESS.getCode();
            } else if (OrderStatusEnum.FAIL.getCode().equals(payOrder.getOrderStatus())) {
                status = OrderCurentStatusEnum.FAIL.getCode();
            } else if (OrderStatusEnum.CANCEL.getCode().equals(payOrder.getOrderStatus())) {
                status = OrderCurentStatusEnum.ABANDONED.getCode();
            }
        } catch (Exception e) {
            log.error("获取订单状态失败", e);
        }
        return status;
    }

    //set去重
    public static List<String> getSiteCode(List<ApplyInfo> infos) {
        Set set = new HashSet();
        List<String> listNew = new ArrayList<>();
        for (ApplyInfo info : infos) {
            set.add(info.getSiteCode());
        }
        listNew.addAll(set);
        return listNew;
    }

    /**
     * 订单查询
     * 
     * @param orderStatus
     * @param currentPage
     * @param pageSize
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "query", produces = "application/json; charset=utf-8")
    public String orderQuery(@RequestParam(value = "orderStatus", required = false) String orderStatus,
                             @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             HttpSession session, Model model) {
        RegisterModel register = null;
        Page<PayOrder> page = null;
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            if (authToken == null) {
                String uri = applyDomain + "/order/query?page=1";
                uri = URLEncoder.encode(uri, "UTF-8");
                return "redirect:" + ucenterDomain + "?login_redirect=" + uri;
            }
            register = authToken.getRegister();
        } catch (Exception e) {
            log.error("", e);
        }
        model.addAttribute("register", register);
        return "pages/registration";
    }

    /**
     * 订单查询
     * 
     * @param orderStatus
     * @param currentPage
     * @param pageSize
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "query/page", produces = "application/json; charset=utf-8")
    @ResponseBody
    public Map<String, Object> orderQueryPage(@RequestParam(value = "orderStatus", required = false) String orderStatus,
                                              @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                              HttpSession session) {
        Page<PayOrder> page = null;
        try {
            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            OrderStatusEnum status = OrderStatusEnum.getEnumByCode(orderStatus);
            page = orderServce.selectPagePageOrderByStatus(currentPage, pageSize,
                authToken.getRegisterCode(), status);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("register", authToken.getRegister());
            map.put("page", page);
            return JsonResultUtil.getSuccessResult("查询成功", map);
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("订单查询失败");
        }
    }

    private void weichatUnified(PayOrder payOrde, HttpServletResponse response, String ip) {
        WeichatUnifiedModel unified = new WeichatUnifiedModel();
        SortedMap<String, Object> params = new TreeMap<String, Object>();
        params.put("appid", weichatConfig.appId);//公众号appid
        params.put("mch_id", weichatConfig.mchId); //商户号
        //        params.put("openid", openId);
        params.put("nonce_str", WeichatUtil.createNoncestr()); //随机字符串
        String outTradeNo = payOrde.getOrderCode();
        unified.setOrderSeq(outTradeNo);
        params.put("out_trade_no", outTradeNo);//商户订单号   本系统自动生成的订单号
        params.put("total_fee", payOrde.getOrderAmount() + "");//总金额
        params.put("spbill_create_ip", ip); //终端IP
        params.put("notify_url", weichatConfig.notifyUrl); //通知地址
        params.put("trade_type", "NATIVE"); //交易类型
        params.put("body", payOrde.getRemark()); //说明
        String sign = WeichatUtil.getSign(params, weichatConfig.weiChatPayKey);
        params.put("sign", sign);//签名
        String requestXml = WeichatUtil.getRequestXml(params);
        log.info("微信统一下单请求参数：" + requestXml);
        String resXml = HttpUtils.executePost(weichatConfig.unifiedorder, requestXml);
        log.info("微信统一下单返回参数参数：" + resXml);
        Map<Object, Object> resultMap;
        try {
            resultMap = XMLUtil.doXMLParse(resXml);
            //result_code 和return_code都为SUCCESS的时候有返回
            if ("SUCCESS".equals(resultMap.get("return_code"))
                && "SUCCESS".equals(resultMap.get("result_code"))) {
                GenerateQrCodeUtil.encodeQrcode((String) resultMap.get("code_url"), response);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        log.info("微信统一下单返回页面参数：" + JSON.toJSONString(unified));
    }

    @RequestMapping("confirm/{orderCode}")
    public String confirmOrder(@PathVariable(value = "orderCode") String orderCode, Model model) {
        PayOrder order = orderServce.getOrderDetail(orderCode);
        model.addAttribute("order", PayOrderVo.getVo(order));
        return "pages/pay_entyr";
    }

    @RequestMapping("success/{orderCode}")
    public String orderSuccess(@PathVariable(value = "orderCode") String orderCode, Model model,
                               HttpSession session) {
        PayOrder order = orderServce.getOrderDetail(orderCode);
        RegisterModel register = null;
        if (order == null) {
            return "redirect:/match/type";
        }
        try {
            register = ucenterAdapterService.getPCAuthToken(session).getRegister();
        } catch (Exception e) {
            log.error("", e);
        }
        model.addAttribute("register", register);
        if (OrderStatusEnum.SUCCESS.getCode().equals(order.getOrderStatus())) {
            model.addAttribute("order", PayOrderVo.getVo(order));
            //            sendSuccessMessage(register, order);
            return "pages/pay_success";
        }
        return "redirect:/order/apply/unified?orderCode=" + orderCode;
    }

    @RequestMapping("apply/unified")
    public String orderApplyUnified(String orderCode, Model model, HttpSession session,
                                    HttpServletResponse response, HttpServletRequest request) {
        //获取当前用户
        RegisterModel register = ucenterAdapterService.getPCAuthToken(session).getRegister();
        if (register == null) {
            return "redirect:/match/type";
        }
        PayOrder payOrder = orderServce.getOrderDetail(orderCode);
        payOrder.setOrderAmountStr(AmountUtils.changeF2Y(payOrder.getOrderAmount()));

        String status = getOrderStatus(payOrder);
        if (OrderCurentStatusEnum.WAIT_PAY.getCode().equals(status)) {
            Integer plusTime = (int) (payOrder.getExpireTime().getTime() - new Date().getTime())
                               / 1000;
            payOrder.setOverplusTime(plusTime);
        }
        String ip = ServletUtil.getIpAddress(request);
        weichatUnified(payOrder, response, ip);

        model.addAttribute("payOrder", payOrder);
        if (OrderStatusEnum.SUCCESS.getCode().equals(payOrder.getOrderStatus())
            || payOrder.getOrderAmount() == 0) {
            return "pages/pay_success";
        }
        return "pages/code_pay";
    }

    /**
     * 待完善
     * 
     * @param orderCode
     * @param editStatus
     * @param model
     * @param session
     * @param response
     * @return
     */
    @RequestMapping("apply/edit")
    public String orderApplyEdit(@RequestParam(value = "orderCode", required = false) String orderCode,
                                 @RequestParam(value = "editStatus", required = false) String editStatus,
                                 @RequestParam(value = "matchCode", required = false) String matchCode,
                                 Model model, HttpSession session, HttpServletResponse response) {

        //获取当前用户
        RegisterModel register = null;
        PayOrder payOrder = null;
        SpAthletesEnrollModel athietes = null;
        String itemType = "";
        try {
            register = ucenterAdapterService.getPCAuthToken(session).getRegister();
            //            register = iRegisterService.selectRegisterByCode(register.getRegisterCode());
            payOrder = orderServce.getOrderDetail(orderCode);
            athietes = spAthletesFacadeClient
                .getAthietes(payOrder.getApplyInfos().get(0).getMatchCode());
        } catch (Exception e) {
            log.error("", e);
        }
        Map<String, Object> map = PlayerPropertyVo.getMap(athietes);
        model.addAttribute("pro", map);

        String status = getOrderStatus(payOrder);
        if (!OrderCurentStatusEnum.WAIT_PERFECT.getCode().equals(status)
            && StringUtils.isBlank(editStatus)) {
            return "redirect:/order/enroll/detail/" + orderCode + "/"
                   + payOrder.getApplyInfos().get(0).getMatchCode();
        }
        List<ApplyInfo> applyinfos = payOrder.getApplyInfos();
        if (applyinfos != null && applyinfos.size() > 0) {
            itemType = applyinfos.get(0).getEventType();
        }
        model.addAttribute("register", register);
        model.addAttribute("payOrder", payOrder);
        model.addAttribute("orderCode", payOrder.getOrderCode());
        if (MatchItemTypeEums.PERSONAL.getCode().equals(itemType)) {
            List<Player> playerList = payOrder.getPlayers();
            if (playerList != null && playerList.size() > 0) {
                model.addAttribute("player", PlayerVo.getPlayerVo(payOrder.getPlayers().get(0)));
            } else {
                model.addAttribute("player", new PlayerVo());
            }
            return "pages/player_enroll_perfect";
        }
        model.addAttribute("matchCode", matchCode);
        model.addAttribute("player", PlayerVo.getPlayerVoList(payOrder.getPlayers()));
        model.addAttribute("groupLimit", payOrder.getPersonGroup());
        return "pages/group_enroll_perfect";
    }

    /**
     * 发送成功消息
     * @param register
     * @param payOrder
     */
    @Async
    public void sendSuccessMessage(RegisterModel register, PayOrder payOrder) {
        try {
            List<ApplyInfo> infos = payOrder.getApplyInfos();
            log.info("订单信息: " + JSON.toJSONString(infos));
            log.info("用户信息: " + JSON.toJSONString(register));
            if (infos != null && infos.size() > 0) {
                String value = "";
                try {
                    value = cacheService.get(Constants.MESSAGE_VERIFY_CODE + register.getAccount()
                                             + "_" + payOrder.getOrderCode());
                } catch (Exception e) {
                    log.error("", e);
                }
                if (infos != null && infos.size() > 0 && !Constants.MESSAGE_TREU.equals(value)) {
                    for (ApplyInfo info : infos) {

                        SpMatchAllInfoModel spmodel = spMatchFacadeClient
                            .getItemDetail(info.getSiteCode(), info.getEventCode());
                        List<Player> players = iPlayerService
                            .selectPlayerByApplyInfoCode(info.getApplyCode());
                        for (Player player : players) {
                            String partCode = "";
                            if ("lantianlvye".equals(info.getUnitCode())) {
                                String extPro = player.getExtPro();
                                if (StringUtils.isNotBlank(extPro)) {
                                    JSONObject obj = JSON.parseObject(extPro);
                                    partCode = (String) obj.get("参赛验证码");
                                }
                            }

                            if (StringUtils.isNotBlank(player.getPlayerPhone())) {
                                msgService.sendSuccessMessage(info.getMatchName(),
                                    info.getMatchGroupName(), player.getPlayerPhone(),
                                    spmodel.getGroupItemInfo().getItemName(),
                                    spmodel.getGroupItemInfo().getStartTime(),
                                    spmodel.getPayAreaInfo().getFieldAddress(),
                                    spmodel.getGroupItemInfo().getEndTime(), partCode);
                                cacheService
                                    .put(Constants.MESSAGE_VERIFY_CODE + player.getPlayerPhone()
                                         + "_" + payOrder.getOrderCode(),
                                        Constants.MESSAGE_TREU, 1000 * 200);
                            }
                        }
                        //                        Player play = iPlayerService.getPlayerByPlayerCode(info.getPlayerCode());
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

}
