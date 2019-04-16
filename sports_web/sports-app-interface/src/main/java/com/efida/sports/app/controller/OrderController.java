/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

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

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sport.admin.facade.model.SpAthletesEnrollModel;
import com.efida.sport.admin.facade.model.SpMatchInfoModel;
import com.efida.sport.facade.enums.RegTerminalEnum;
import com.efida.sports.app.vo.MatchDetailVo;
import com.efida.sports.app.vo.PayInfo;
import com.efida.sports.app.vo.PayOrderDetailVo;
import com.efida.sports.app.vo.PayOrderVo;
import com.efida.sports.config.AlipayConfig;
import com.efida.sports.config.PlayerProConfig;
import com.efida.sports.config.WeichatConfig;
import com.efida.sports.entity.LeaderInfo;
import com.efida.sports.entity.PayOrder;
import com.efida.sports.entity.Player;
import com.efida.sports.enums.ErrorCodeEnum;
import com.efida.sports.enums.OrderStatusEnum;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.model.WeichatUnifiedModel;
import com.efida.sports.service.IPayOrderService;
import com.efida.sports.service.MsgService;
import com.efida.sports.service.dubbo.intergration.SpAthletesFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpMatchFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterLoginFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;
import com.efida.sports.util.AmountUtils;
import com.efida.sports.util.DateUtil;
import com.efida.sports.util.HttpUtils;
import com.efida.sports.util.JsonResultUtil;
import com.efida.sports.util.ServletUtil;
import com.efida.sports.util.XMLUtil;
import com.efida.sports.util.weichat.WeichatUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author zoutao
 * @version $Id: OrderController.java, v 0.1 2018年6月12日 下午7:52:10 zoutao Exp $
 */
@RestController()
@Api(value = "orderApi", tags = { "订单操作接口" })
@RequestMapping(value = "/api", produces = "application/json")
public class OrderController {
    private static Logger               log          = LoggerFactory
        .getLogger(OrderController.class);

    @Autowired
    private IPayOrderService            orderService;

    @Autowired
    private WeichatConfig               weichatConfig;

    @Autowired
    private AlipayConfig                alipayConfig;
    // 实例化客户端
    private AlipayClient                alipayClient = null;
    @Autowired
    private SpAthletesFacadeClient      spAthletesFacadeClient;
    @Autowired
    private SpMatchFacadeClient         spMatchFacadeClient;

    @Autowired
    private MsgService                  msgService;

    @Autowired
    private UcenterLoginFacadeClient    loginServiceClient;

    @Autowired
    private UcenterRegisterFacadeClient regsiterFacadeClient;

    /**
    * 获取订单详情
    * @return
    */
    @ApiOperation(value = "获取订单详细信息", notes = "根据订单编号获取订单详情")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderCode", value = "订单编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "query") })
    @RequestMapping(value = "order/detail", method = { RequestMethod.GET })
    public Map<String, Object> orderDetail(@RequestParam(value = "orderCode", required = true) String orderCode,
                                           @RequestParam(value = "token", required = true) String token) {

        log.info("******************************************order/detail  orderCode=" + orderCode);
        log.info("******************************************order/detail   token=" + token);
        try {
            PayOrder payOrder = orderService.getOrderDetail(orderCode);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            String matchCode = payOrder.getApplyInfos().get(0).getMatchCode();
            SpMatchInfoModel matchDetail = spMatchFacadeClient.getMatchDetail(matchCode);
            SpAthletesEnrollModel athietes = spAthletesFacadeClient.getAthietes(matchCode);
            MatchDetailVo vo = MatchDetailVo.getVo(matchDetail);
            hashMap.put("matchName", matchDetail.getMatchName());
            hashMap.put("matchCode", matchDetail.getMatchCode());
            hashMap.put("applyStatus", vo.getApplyStatus());
            hashMap.put("applyStatusDesc", vo.getApplyStatusDesc());
            hashMap.put("title", "2018首届全国智能体育大赛");
            hashMap.put("matchStartTime", DateUtil.format(matchDetail.getStartTime()));
            hashMap.put("matchEndTime", DateUtil.format(matchDetail.getEndTime()));
            hashMap.put("orderDetail", PayOrderDetailVo.getVo(payOrder, athietes));
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取订单详情失败,请稍后重试！！！");
        }
    }

    @ApiOperation(value = "获取订单列表", notes = "根据订单状态获取用户订单列表(个人中心调用)")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderStatus", value = "订单状态(WAIT_PAY:待支付,待完善：WAIT_COMPLETE,已成功：SUCCESS)", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "currentPage", value = "当前页数(不传默认取第一页)", required = false, dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "每页大小(不传默认为10)", required = false, dataType = "int", paramType = "query") })
    @RequestMapping(value = "order/list", method = { RequestMethod.GET })
    public Map<String, Object> getOrderList(@RequestParam(value = "orderStatus", required = false) OrderStatusEnum orderStatus,
                                            @RequestParam(value = "token", required = true) String token,
                                            @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        log.info("******************************************order/list   token=" + token);
        log.info(
            "******************************************order/list   orderStatus=" + orderStatus);
        log.info(
            "******************************************order/list   currentPage=" + currentPage);
        log.info("******************************************order/list   pageSize=" + pageSize);
        try {

            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            Page<PayOrder> page = orderService.selectPagePageOrderByStatus(currentPage, pageSize,
                register.getRegisterCode(), orderStatus);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("totalRow", page.getTotal());
            map.put("totalPage", page.getPages());
            map.put("currentPage", page.getCurrent());
            map.put("pageSize", page.getSize());
            map.put("list", PayOrderVo.getVos(page.getRecords()));
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取订单列表失败,请稍后重试！！！");
        }

    }

    @ApiOperation(value = "获取订单状态", notes = "根据订单编号获取订单状态（返回orderStatus，wait_pay:待支付,待完善：wait_complete,已成功：success）")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderCode", value = "订单编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query") })
    @RequestMapping(value = "order/status", method = { RequestMethod.GET })
    public Map<String, Object> getOrderStatus(@RequestParam(value = "orderCode", required = true) String orderCode,
                                              @RequestParam(value = "token", required = true) String token) {

        log.info("******************************************order/status   orderCode=" + orderCode);
        log.info("******************************************order/status   token=" + token);

        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            PayOrder payOrder = orderService.getOrderByCode(orderCode);
            if (payOrder == null) {
                return JsonResultUtil.getServerErrorResult("订单不存在");
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderStatus", payOrder.getOrderStatus());
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取订单状态失败");
        }
    }

    @ApiOperation(value = "付费报名订单接口", notes = "根据订单编号获取付费报名数据")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderCode", value = "订单编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query") })
    @RequestMapping(value = "order/pay", method = { RequestMethod.GET })
    public Map<String, Object> unifiedOrder(@RequestParam(value = "orderCode", required = true) String orderCode,
                                            @RequestParam(value = "token", required = true) String token) {
        try {
            log.info(
                "******************************************order/pay   orderCode=" + orderCode);
            log.info("******************************************order/pay   token=" + token);
            //            Register register = loginService.getRegisterByToken(token);
            //            if (register == null) {
            //                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            //            }
            PayOrder payOrder = orderService.getOrderDetail(orderCode);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("payinfo", PayInfo.getPayOrderVo(payOrder));
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取付费报名订单接口失败,请稍后重试！！！");
        }

    }

    @ApiOperation(value = "取消订单接口", notes = "取消订单接口")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderCode", value = "订单编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query") })
    @RequestMapping(value = "cancel/order", method = { RequestMethod.GET })
    public Map<String, Object> cancelOrder(@RequestParam(value = "orderCode", required = true) String orderCode,
                                           @RequestParam(value = "token", required = true) String token) {
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            PayOrder payOrder = orderService.cancelOrder(orderCode, register.getRegisterCode());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderCode", payOrder.getOrderCode());
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取付费报名订单接口失败,请稍后重试！！！");
        }

    }

    @ApiOperation(value = "创建订单", notes = "创建订单(选择比赛项目后点击下一步时候调用,前置条件：用户必须为登录状态下)")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "itemCodes", value = "比赛项编号(多个比赛项用,隔开)", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "siteCode", value = "赛场编号", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "terminal", value = "终端来源(ANDROID,IOS)", required = false, dataType = "String", paramType = "form") })
    @RequestMapping(value = "create/order", method = { RequestMethod.POST })
    public Map<String, Object> addOrder(HttpServletRequest request,
                                        @RequestParam(value = "itemCodes", required = true) String itemCodes,
                                        @RequestParam(value = "siteCode", required = true) String siteCode,
                                        @RequestParam(value = "token", required = true) String token,
                                        @RequestParam(value = "terminal", required = false) RegTerminalEnum terminal) {

        try {

            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            log.info("调用创建订单,itemCodes :{}, siteCode :{},registerCode:{}", itemCodes, siteCode,
                register.getRegisterCode());
            register = regsiterFacadeClient.getRegsiterByRegisterCode(register.getRegisterCode());
            if (StringUtils.isBlank(register.getAccount())) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.UN_BIND_PHONE.getCode(),
                    "用户未绑定手机号");
            }
            List<String> ids = new ArrayList<String>();
            Set<String> items = new HashSet<String>();
            if (itemCodes.indexOf(",") > 0) {
                String[] split = itemCodes.split(",");
                if (split != null && split.length > 0) {
                    for (String code : split) {
                        items.add(code);
                    }
                }
            } else {
                items.add(itemCodes);
            }
            ids.addAll(items);
            PayOrder order = orderService.createWaitComplete(ids, siteCode,
                ServletUtil.getIpAddr(request), register.getRegisterCode(), terminal);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderCode", order.getOrderCode());
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("创建订单失败");
        }
    }

    @ApiOperation(value = "完善团队报名信息", notes = "完善团队报名信息")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderCode", value = "订单编号", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "teamName", value = "团队名称", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "verifyCode", value = "验证码", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "leaderName", value = "领队名称", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "leaderPhone", value = "领队电话号码", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "isCard", value = "是否使用我的报名卡(0:没使用,1:使用)", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "appid", value = "渠道来源", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "openId", value = "用户唯一标识", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "sign", value = "签名", required = false, dataType = "String", paramType = "form") })
    @RequestMapping(value = "copmplete/group", method = { RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> copmpleteGroup(@RequestParam(value = "orderCode", required = true) String orderCode,
                                              @RequestParam(value = "teamName", required = true) String teamName,
                                              @RequestParam(value = "verifyCode", required = true) String verifyCode,
                                              @RequestParam(value = "leaderName", required = true) String leaderName,
                                              @RequestParam(value = "leaderPhone", required = true) String leaderPhone,
                                              @RequestParam(value = "token", required = true) String token,
                                              @RequestParam(value = "isCard", required = false) String isCard,
                                              @RequestParam(value = "appid", required = false) String appid,
                                              @RequestParam(value = "openId", required = false) String openId,
                                              @RequestParam(value = "sign", required = false) String sign) {
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            LeaderInfo leader = new LeaderInfo();
            leader.setTeamName(teamName);
            leader.setLeaderName(leaderName);
            leader.setLeaderPhone(leaderPhone);
            Boolean checkVerify = true; //默认校验验证码
            if (StringUtils.isNotBlank(isCard) && isCard.equalsIgnoreCase("1")) { //使用我的报名卡，不校验验证码
                checkVerify = false;
            }
            if (checkVerify) {
                boolean checkVerifyCode = msgService.checkVerifyCode(leaderPhone, verifyCode);
                if (!checkVerifyCode) {
                    throw new BusinessException("验证码错误，请重新输入");
                }
            }
            PayOrder order = orderService.completeTeamApply(leader, orderCode,
                register.getRegisterCode());
            String orderStaus = order.getOrderStatus();
            if (order.getOrderAmount() == 0L) {
                orderStaus = OrderStatusEnum.SUCCESS.getCode();
                orderService.completePaySuccess(order.getOrderCode(), null, null);
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderCode", order.getOrderCode());
            map.put("orderStaus", orderStaus);
            return JsonResultUtil.getSuccessResult(map);

        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("提交团队信息失败");
        }
    }

    @ApiOperation(value = "完善订单", notes = "用户填写运动员信息提交报名和完善报名信息时候调用")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderCode", value = "订单编号", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "isCard", value = "是否使用我的报名卡(0:没使用,1:使用)", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerPhone", value = "运动员电话号码", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "verifyCode", value = "验证码", required = true, dataType = "String", paramType = "form"),
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
                         @ApiImplicitParam(name = "attUrl", value = "健康证", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "imgUrl", value = "头像地址", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "appid", value = "渠道来源", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "openId", value = "用户唯一标识", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "sign", value = "签名", required = false, dataType = "String", paramType = "form") })
    @RequestMapping(value = "complete/order", method = { RequestMethod.POST })
    public Map<String, Object> completeOrder(@RequestParam(value = "orderCode", required = true) String orderCode,
                                             @RequestParam(value = "token", required = true) String token,
                                             @RequestParam(value = "isCard", required = false) String isCard,
                                             @RequestParam(value = "playerPhone", required = true) String playerPhone,
                                             @RequestParam(value = "verifyCode", required = true) String verifyCode,
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
                                             @RequestParam(value = "appid", required = false) String appid,
                                             @RequestParam(value = "openId", required = false) String openId,
                                             @RequestParam(value = "sign", required = false) String sign,
                                             HttpServletRequest request) {
        log.info(
            "******************************************complete/order  orderCode=" + orderCode);
        log.info("******************************************complete/order  token=" + token);
        log.info(
            "******************************************complete/order  playerPhone=" + playerPhone);
        log.info(
            "******************************************complete/order  verifyCode=" + verifyCode);
        log.info(
            "******************************************complete/order  playerName=" + playerName);
        log.info("******************************************complete/order  sex=" + sex);
        try {

            List<String> playerPro = PlayerProConfig.getInstance().getPlayerPro();
            Enumeration enu = request.getParameterNames();
            HashMap<String, Object> extProMap = new HashMap<String, Object>();
            while (enu.hasMoreElements()) {
                String paraName = (String) enu.nextElement();
                if (playerPro.contains(paraName) || "token".equals(paraName)
                    || "verifyCode".equals(paraName) || "orderCode".equals(paraName)
                    || "isCard".equals(paraName) || "t".equals(paraName)) {
                    continue;
                }
                extProMap.put(paraName, request.getParameter(paraName));
            }

            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            log.info("******************************************RegisterCode="
                     + register.getRegisterCode());
            register = regsiterFacadeClient.getRegsiterByRegisterCode(register.getRegisterCode());
            if (StringUtils.isBlank(register.getAccount())) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.UN_BIND_PHONE.getCode(),
                    "用户未绑定手机号");
            }
            log.info("******************************************account=" + register.getAccount());
            Player player = new Player();
            log.info("提交保存运动员信息：playerWeight:{},playerHeight:{},playerBirth：{},附件1：{},附件2:{}",
                playerWeight, playerHeight, playerBirth, attOne, attTwo);
            if (StringUtils.isNotBlank(playerWeight) && playerWeight.indexOf("null") < 0) {
                if (!isDouble(playerWeight)) {
                    return JsonResultUtil.getServerErrorResult("体重填写错误");
                }
                player.setPlayerWeight(Double.parseDouble(playerWeight));
            }
            if (StringUtils.isNotBlank(playerBirth) && playerBirth.indexOf("null") < 1) {
                player.setPlayerBirth(DateUtil.strToDate1(playerBirth));
            }
            if (StringUtils.isNotBlank(playerHeight) && playerHeight.indexOf("null") < 1) {
                player.setPlayerHeight(Integer.parseInt(playerHeight));
            }
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
            player.setVerifyCode(verifyCode);
            player.setAttOne(attOne);
            player.setAttTwo(attTwo);
            player.setAttUrl(attUrl);
            player.setAssettoId(assettoId);
            log.info("****************************校验验证码****************************");
            Boolean checkVerify = true; //默认校验验证码
            if (StringUtils.isNotBlank(isCard) && isCard.equalsIgnoreCase("1")) { //使用我的报名卡，不校验验证码
                checkVerify = false;
            }
            checkaddApply(player, checkVerify);
            player.setExtProMap(extProMap);
            PayOrder payOrder = orderService.completeOrder(orderCode, register.getRegisterCode(),
                player);
            String orderStaus = payOrder.getOrderStatus();
            if (payOrder.getOrderAmount() == 0L) {
                orderStaus = OrderStatusEnum.SUCCESS.getCode();
                orderService.completePaySuccess(payOrder.getOrderCode(), null, null);
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderCode", payOrder.getOrderCode());
            map.put("orderStaus", orderStaus);
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("提交运动员信息失败");
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

    @ApiOperation(value = "临时保存运动员信息", notes = "保存运动员信息(填写运动员基本属性后点保存时候调用)")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderCode", value = "订单编号", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
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
                         @ApiImplicitParam(name = "attOne", value = "附件1(附件正面,驾照,护照)", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "attTwo", value = "附件1(附件反面)", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "assettoId", value = "神力科莎账户ID", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "attUrl", value = "健康证", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "imgUrl", value = "头像地址", required = false, dataType = "String", paramType = "form")

    })
    @RequestMapping(value = "save/player", method = { RequestMethod.POST })
    public Map<String, Object> savePlayer(@RequestParam(value = "orderCode", required = true) String orderCode,
                                          @RequestParam(value = "token", required = true) String token,
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
                                          HttpServletRequest request) {
        log.info("******************************************save/player  orderCode=" + orderCode);
        log.info("******************************************save/player  token=" + token);
        log.info(
            "******************************************save/player  playerPhone=" + playerPhone);
        log.info("******************************************save/player  playerName=" + playerName);
        log.info("******************************************save/player  sex=" + sex);

        try {

            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            register = regsiterFacadeClient.getRegsiterByRegisterCode(register.getRegisterCode());
            if (StringUtils.isBlank(register.getAccount())) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.UN_BIND_PHONE.getCode(),
                    "用户未绑定手机号");
            }

            Player player = new Player();
            List<String> playerPro = PlayerProConfig.getInstance().getPlayerPro();
            Enumeration enu = request.getParameterNames();
            HashMap<String, Object> extProMap = new HashMap<String, Object>();
            while (enu.hasMoreElements()) {
                String paraName = (String) enu.nextElement();
                if (playerPro.contains(paraName) || "token".equals(paraName)
                    || "verifyCode".equals(paraName) || "orderCode".equals(paraName)
                    || "t".equals(paraName)) {
                    continue;
                }
                extProMap.put(paraName, request.getParameter(paraName));
            }
            player.setExtProMap(extProMap);
            log.info("临时保存运动员信息：playerWeight:{},playerHeight:{},playerBirth：{},附件1：{},附件2:{}",
                playerWeight, playerHeight, playerBirth, attOne, attTwo);
            if (StringUtils.isNotBlank(playerWeight) && playerWeight.indexOf("null") < 0) {
                if (!isDouble(playerWeight)) {
                    return JsonResultUtil.getServerErrorResult("体重填写错误");
                }
                player.setPlayerWeight(Double.parseDouble(playerWeight));
            }
            if (StringUtils.isNotBlank(playerBirth) && playerBirth.indexOf("null") < 1) {
                player.setPlayerBirth(DateUtil.strToDate1(playerBirth));
            }
            if (StringUtils.isNotBlank(playerHeight) && playerHeight.indexOf("null") < 1) {
                player.setPlayerHeight(Integer.parseInt(playerHeight));
            }
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
            player.setAssettoId(assettoId);
            player.setAttUrl(attUrl);
            PayOrder payOrder = orderService.savePlayer(orderCode, register.getRegisterCode(),
                player);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderCode", payOrder.getOrderCode());
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("保存运动员信息失败");
        }
    }

    private WeichatUnifiedModel weichatUnified(PayOrder payOrde) {
        WeichatUnifiedModel unified = new WeichatUnifiedModel();
        SortedMap<String, Object> params = new TreeMap<String, Object>();
        params.put("appid", weichatConfig.appId);//公众号appid
        params.put("mch_id", weichatConfig.mchId); //商户号
        params.put("nonce_str", WeichatUtil.createNoncestr()); //随机字符串
        String outTradeNo = payOrde.getOrderCode();
        unified.setOrderSeq(outTradeNo);
        params.put("out_trade_no", outTradeNo);//商户订单号   本系统自动生成的订单号
        params.put("total_fee", payOrde.getOrderAmount() + "");//总金额
        params.put("spbill_create_ip", payOrde.getOrderIp()); //终端IP
        params.put("notify_url", weichatConfig.notifyUrl); //通知地址
        params.put("trade_type", "APP"); //交易类型
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
                String appId = (String) resultMap.get("appid");//微信公众号AppId
                String timeStamp = String.valueOf(new Date().getTime() / 1000);//当前时间戳
                String prepayId = resultMap.get("prepay_id").toString();//统一下单返回的预支付id
                String nonceStr = WeichatUtil.buildRandom(20) + "";//不长于32位的随机字符串
                SortedMap<String, Object> signMap = new TreeMap<String, Object>();//自然升序map
                signMap.put("appid", appId);
                signMap.put("partnerid", resultMap.get("mch_id").toString());
                signMap.put("prepayid", prepayId);
                signMap.put("noncestr", nonceStr);
                signMap.put("timestamp", timeStamp);
                signMap.put("package", "Sign=WXPay");
                sign = WeichatUtil.getSign(signMap, weichatConfig.weiChatPayKey);
                unified.setAppId(appId);
                unified.setNonceStr(nonceStr);
                unified.setPrepayId(prepayId);
                unified.setPackageStr("Sign=WXPay");
                unified.setSign(sign);
                unified.setTimeStamp(timeStamp);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        log.info("微信统一下单返回页面参数：" + JSON.toJSONString(unified));
        return unified;
    }

    /**
     * 微信支付统一下单
    
     */

    @ApiOperation(value = "微信支付统一下单", notes = "使用微信支付调用")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderCode", value = "订单编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "query") })
    @RequestMapping(value = "weichat/unified", method = { RequestMethod.GET })
    public Map<String, Object> weichatUnified(@RequestParam(value = "orderCode", required = true) String orderCode,
                                              @RequestParam(value = "token", required = false) String token) {

        log.info(
            "******************************************weichat/unified  orderCode=" + orderCode);
        log.info("******************************************weichat/unified  token=" + token);
        try {
            PayOrder payOrder = orderService.checkOrderCanPay(orderCode);
            WeichatUnifiedModel weichatUnified = weichatUnified(payOrder);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("appId", weichatUnified.getAppId());
            map.put("nonceStr", weichatUnified.getNonceStr());
            map.put("timeStamp", weichatUnified.getTimeStamp());
            map.put("prepayId", weichatUnified.getPrepayId());
            map.put("partnerId", weichatConfig.mchId);
            map.put("sign", weichatUnified.getSign());
            map.put("package", weichatUnified.getPackageStr());
            map.put("orderCode", orderCode);
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("微信支付下单失败,请稍后重试！！！");
        }
    }

    /**
     * 支付宝支付统一下单
     */

    /**
     * 获取赛事列表
     * @return
     */
    @ApiOperation(value = "支付宝支付统一下单", notes = "支付宝支付统一下单")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderCode", value = "订单编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "query") })
    @RequestMapping(value = "alipay/unified", method = { RequestMethod.GET })
    public Map<String, Object> alipayUnified(@RequestParam(value = "orderCode", required = true) String orderCode,
                                             @RequestParam(value = "token", required = true) String token) {

        log.info("*****************************************alipay/unified  orderCode=" + orderCode);
        log.info("*****************************************alipay/unified  token=" + token);

        try {

            PayOrder order = orderService.checkOrderCanPay(orderCode);
            Map<String, Object> map = aliPayUnified(order);
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("支付宝下单失败");
        }
    }

    private Map<String, Object> aliPayUnified(PayOrder order) throws AlipayApiException {
        initClient();
        String subject = order.getRemark();
        AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();// 创建API对应的request
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        model.setBody("全国智能体育报名费");
        model.setSubject(subject);
        model.setOutTradeNo(order.getOrderCode());
        model.setTimeoutExpress(alipayConfig.overTime);
        model.setTotalAmount(AmountUtils.changeF2Y(order.getOrderAmount()));
        model.setProductCode("QUICK_MSECURITY_PAY");
        alipayRequest.setBizModel(model);
        alipayRequest.setNotifyUrl(alipayConfig.notifyUrl);// 在公共参数中设置回跳和通知地址
        AlipayTradeAppPayResponse sdkExecute = alipayClient.sdkExecute(alipayRequest);
        Map<String, Object> map = new HashMap<>();
        map.put("sign", sdkExecute.getBody());
        return map;
    }

    private static Map<String, Object> splitUrlQuery(String query) {
        Map<String, Object> result = new HashMap<String, Object>();
        String[] pairs = query.split("&");
        if (pairs != null && pairs.length > 0) {
            for (String pair : pairs) {
                String[] param = pair.split("=", 2);
                if (param != null && param.length == 2) {
                    result.put(param[0], param[1]);
                }
            }
        }
        return result;
    }

    private void checkaddApply(Player player, boolean checkVerify) {
        if (player == null) {
            throw new BusinessException("运动员不能为空");
        }
        String sex = player.getSex();
        if (StringUtils.isEmpty(sex)) {
            throw new BusinessException("运动员性别不能为空");
        }
        String verifyCode = player.getVerifyCode();
        if (StringUtils.isEmpty(verifyCode)) {
            throw new BusinessException("验证码不能为空");
        }
        String playerName = player.getPlayerName();
        if (StringUtils.isEmpty(playerName)) {
            throw new BusinessException("运动员名称不能为空");
        }
        if (playerName.length() > 24) {
            throw new BusinessException("运动员名称长度不能超过24位");
        }
        String playerPhone = player.getPlayerPhone();
        if (StringUtils.isEmpty(playerPhone)) {
            throw new BusinessException("运动员名电话号码不能为空");
        }
        if (checkVerify) {
            boolean checkVerifyCode = msgService.checkVerifyCode(playerPhone, verifyCode);
            if (!checkVerifyCode) {
                throw new BusinessException("验证码错误，请重新输入");
            }
        }

    }

    private void initClient() {
        if (alipayClient == null) {
            alipayClient = new DefaultAlipayClient(alipayConfig.aLiUrl, alipayConfig.appId,
                alipayConfig.privateKey, "json", "utf-8", alipayConfig.publicKey,
                alipayConfig.encryptionMethod);
        }
    }

}
