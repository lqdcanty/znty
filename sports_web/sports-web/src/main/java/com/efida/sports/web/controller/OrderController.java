/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sport.admin.facade.model.FromTypeModel;
import com.efida.sport.admin.facade.model.SpAthletesEnrollModel;
import com.efida.sport.admin.facade.model.SpMatchInfoModel;
import com.efida.sport.facade.enums.RegTerminalEnum;
import com.efida.sports.config.PlayerProConfig;
import com.efida.sports.config.WeichatConfig;
import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.entity.LeaderInfo;
import com.efida.sports.entity.PayOrder;
import com.efida.sports.entity.Player;
import com.efida.sports.enums.ErrorCodeEnum;
import com.efida.sports.enums.EventTypeEnums;
import com.efida.sports.enums.OrderStatusEnum;
import com.efida.sports.enums.PayGateWayEnum;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.model.WeichatUnifiedModel;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.IPayOrderService;
import com.efida.sports.service.IPlayerService;
import com.efida.sports.service.MsgService;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.service.dubbo.intergration.SpAthletesFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpMatchFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;
import com.efida.sports.util.DateUtil;
import com.efida.sports.util.HttpUtils;
import com.efida.sports.util.JsonResultUtil;
import com.efida.sports.util.ServletUtil;
import com.efida.sports.util.XMLUtil;
import com.efida.sports.util.weichat.WeichatUtil;
import com.efida.sports.web.vo.MatchDetailVo;
import com.efida.sports.web.vo.PayOrderDetailVo;
import com.efida.sports.web.vo.PayOrderVo;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author zoutao
 * @version $Id: OrderController.java, v 0.1 2018年5月22日 下午3:54:59 zoutao Exp $
 */
@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private IPayOrderService            orderServce;

    @Autowired
    private WeichatConfig               weichatConfig;

    @Autowired
    private CacheService                cacheService;
    @Autowired
    private IPayOrderService            orderService;
    @Autowired
    private SpAthletesFacadeClient      spAthletesFacadeClient;
    @Autowired
    private SpMatchFacadeClient         spMatchFacadeClient;
    @Autowired
    private IPlayerService              playerService;
    @Autowired
    private MsgService                  msgService;
    @Autowired
    private UcenterAdapterService       ucenterAdapterService;
    @Autowired
    private UcenterRegisterFacadeClient registerFacadeClient;

    @Value("${ucenter-domain}")
    public String                       ucenterDomain;
    @Value("${apply-domain}")
    public String                       applyDomain;

    private static Logger               log = LoggerFactory.getLogger(OrderController.class);

    /**
     * 微信统一下单  生成预支付订单
     * 报名下单
     * @param totalFree  总金额
     * @param request  
     * @param model
     * @param orderSeq   订单编号,消费订单编号，充值订单方法内生成
     * @param session
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping("apply/unified")
    public String unifiedOrder(HttpServletRequest request, Model model, String orderCode,
                               String tradeType,
                               HttpSession session) throws UnsupportedEncodingException {
        AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
        String uri = "";
        if (StringUtils.isNotBlank(request.getParameter("jztype"))) {
            String payOrderCode = request.getParameter("payOrderCode");
            //获取当前用户
            if (auth == null) {
                if (StringUtils.isNotBlank(request.getParameter("orderCode"))) {
                    uri = applyDomain + "/order/apply/unified?orderCode="
                          + request.getParameter("orderCode") + "&goodsCode="
                          + request.getParameter("goodsCode") + "&address="
                          + request.getParameter("address") + "&tradeType=" + tradeType
                          + "&payOrderCode=" + payOrderCode;
                } else {
                    uri = applyDomain + "/order/apply/unified?waitPayCode="
                          + request.getParameter("waitPayCode") + "&tradeType=" + tradeType
                          + "&payOrderCode=" + payOrderCode;
                }
                uri = URLEncoder.encode(uri, "UTF-8");
                return "redirect:" + ucenterDomain + "?login_redirect=" + uri;
            }

            PayOrder payOrder = orderServce.getOrderDetail(payOrderCode);
            WeichatUnifiedModel weichatUnified = weichatUnified(payOrder, auth.getOpenId(),
                tradeType, request);
            model.addAttribute("appId", weichatUnified.getAppId());
            model.addAttribute("nonceStr", weichatUnified.getNonceStr());
            model.addAttribute("prepayId", weichatUnified.getPrepayId());
            model.addAttribute("paySign", weichatUnified.getSign());
            model.addAttribute("timeStamp", weichatUnified.getTimeStamp());
            model.addAttribute("orderCode", weichatUnified.getOrderSeq());
            model.addAttribute("orderStatus", payOrder.getOrderStatus());
            model.addAttribute("mwebUrl", weichatUnified.getMwebUrl());
            return "pages/payForMedal";
        }

        //获取当前用户
        if (auth == null) {
            uri = applyDomain + "/order/apply/unified?orderCode=" + orderCode + "&tradeType="
                  + tradeType;
            uri = URLEncoder.encode(uri, "UTF-8");
            return "redirect:" + ucenterDomain + "?login_redirect=" + uri;
        }
        PayOrder payOrder = orderServce.getOrderDetail(orderCode);

        WeichatUnifiedModel weichatUnified = weichatUnified(payOrder, auth.getOpenId(), tradeType,
            request);
        model.addAttribute("appId", weichatUnified.getAppId());
        model.addAttribute("nonceStr", weichatUnified.getNonceStr());
        model.addAttribute("prepayId", weichatUnified.getPrepayId());
        model.addAttribute("paySign", weichatUnified.getSign());
        model.addAttribute("timeStamp", weichatUnified.getTimeStamp());
        model.addAttribute("orderCode", weichatUnified.getOrderSeq());
        model.addAttribute("orderStatus", payOrder.getOrderStatus());
        model.addAttribute("order", PayOrderVo.getVo(payOrder));
        model.addAttribute("mwebUrl", weichatUnified.getMwebUrl());
        return "pages/confirm_entyr";
    }

    private WeichatUnifiedModel weichatUnified(PayOrder payOrde, String openId, String tradeType,
                                               HttpServletRequest request) {
        Map<String, String> tradeTypeMap = new HashMap<>();
        tradeTypeMap.put("internal", "JSAPI");
        tradeTypeMap.put("outside", "MWEB");
        WeichatUnifiedModel unified = new WeichatUnifiedModel();
        SortedMap<String, Object> params = new TreeMap<String, Object>();
        params.put("appid", weichatConfig.appId);//公众号appid
        params.put("body", payOrde.getRemark()); //说明
        params.put("mch_id", weichatConfig.mchId); //商户号
        params.put("nonce_str", WeichatUtil.createNoncestr()); //随机字符串
        params.put("notify_url", weichatConfig.notifyUrl); //通知地址
        if (!tradeType.equals("outside")) {
            params.put("openid", openId);
        }
        String outTradeNo = payOrde.getOrderCode();
        unified.setOrderSeq(outTradeNo);
        params.put("out_trade_no", outTradeNo + "_" + tradeType);//商户订单号   本系统自动生成的订单号
        if (tradeType.equals("outside")) {
            String url = weichatConfig.notifyUrl;
            if (url.split("/").length > 3) {
                url = url.split("/")[0] + "//" + url.split("/")[1] + url.split("/")[2];
            }
            String sceneInfo = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"" + url
                               + "\",\"wap_name\": \"znty\"}}";
            params.put("scene_info", sceneInfo); //场景信息-h5使用
        }
        String ip = ServletUtil.getIpAddress(request);
        log.info("发起微信支付spbill_create_ip-----------" + ip);
        params.put("spbill_create_ip", ip); //终端IP
        params.put("total_fee", payOrde.getOrderAmount() + "");//总金额
        params.put("trade_type", tradeTypeMap.get(tradeType)); //交易类型
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
                String timeStamp = WeichatUtil.getCurrTime();//当前时间戳
                String prepayId = "prepay_id=" + resultMap.get("prepay_id");//统一下单返回的预支付id
                String nonceStr = WeichatUtil.buildRandom(20) + "";//不长于32位的随机字符串
                String mwebUrl = resultMap.get("mweb_url") + "";
                SortedMap<String, Object> signMap = new TreeMap<String, Object>();//自然升序map
                signMap.put("appId", appId);
                signMap.put("package", prepayId);
                signMap.put("timeStamp", timeStamp);
                signMap.put("nonceStr", nonceStr);
                signMap.put("signType", "MD5");
                sign = WeichatUtil.getSign(signMap, weichatConfig.weiChatPayKey);
                unified.setAppId(appId);
                unified.setNonceStr(nonceStr);
                unified.setPrepayId(prepayId);
                unified.setSign(sign);
                unified.setTimeStamp(timeStamp);
                unified.setMwebUrl(mwebUrl);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        log.info("微信统一下单返回页面参数：" + JSON.toJSONString(unified));
        return unified;
    }

    /**
     * 
     * @param items  比赛项目编号
     * @param playerName  运动员名称 
     * @param gender      性别
     * @param email       邮箱
     * @param playerHeight 身高
     * @param playerWeight 体重
     * @param playerBirth  生日
     * @param playerNationality 国籍
     * @param playerAddress   地址
     * @param playerCerType  身份证类型
     * @param playerCerNo    身份证号码
     * @param playerBloodType  血型
     * @param playerNation    民族
     * @param playerClothSize  衣服尺寸
     * @param playerEmergencyName  紧急联系人
     * @param playerEmergencyPhone  经济联系号码
     * String playerName, GenderEnum gender, String email,
                                String playerHeight, String playerWeight, String playerBirth,
                                String playerNationality, String playerAddress,
                                String playerCerType, String playerCerNo, String playerBloodType,
                                String playerNation, String playerClothSize,
                                String playerEmergencyName, String playerEmergencyPhone
     * @return
     */
    @RequestMapping("add/apply")
    @ResponseBody
    public Map<String, Object> addApplyOrder(HttpServletRequest request, Model model,
                                             HttpSession session, String itemIds, String siteCode,
                                             Player player) {

        try {

            List<String> playerPro = PlayerProConfig.getInstance().getPlayerPro();
            Enumeration enu = request.getParameterNames();
            HashMap<String, Object> extProMap = new HashMap<String, Object>();
            while (enu.hasMoreElements()) {
                String paraName = (String) enu.nextElement();
                if (playerPro.contains(paraName) || "itemIds".equals(paraName)
                    || "siteCode".equals(paraName) || "verifyCode".equals(paraName)) {
                    continue;
                }
                extProMap.put(paraName, request.getParameter(paraName));
            }
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            String ip = ServletUtil.getIpAddr(request);
            String referer = ServletUtil.getReferer(request);
            List<String> ids = new ArrayList<String>();
            if (itemIds.indexOf(",") > 0) {
                String[] split = itemIds.split(",");
                if (split != null && split.length > 0) {
                    for (String code : split) {
                        ids.add(code);
                    }
                }
            } else {
                ids.add(itemIds);
            }
            checkaddApply(ids, player);
            player.setExtProMap(extProMap);
            PayOrder order = orderServce.addApplyOrder(ids, siteCode, player,
                auth.getRegisterCode(), ip, referer, RegTerminalEnum.WEICHAT);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("orderCode", order.getOrderCode());
            hashMap.put("url", "/order/confirm/" + order.getOrderCode());
            if (order.getOrderAmount() == 0) {
                orderServce.completePaySuccess(order.getOrderCode(), null, null);
                hashMap.put("url", "/order/detail/" + order.getOrderCode());
            }
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("报名失败，请稍后重试！！");
            // TODO: handle exception
        }
    }

    /**
     * 压力测试使用
     * 
     * @param request
     * @param model
     * @param session
     * @param itemIds
     * @param siteCode
     * @return
     */
    @RequestMapping("add/apply/test")
    @ResponseBody
    public Map<String, Object> addApplyTest(HttpServletRequest request, Model model,
                                            HttpSession session, String itemIds, String siteCode) {

        try {
            String secrect = request.getParameter("secrect");
            if (secrect == null || !secrect.equals("forpressuretest")) {
                return JsonResultUtil.getServerErrorResult("报名测试失败,压力测试需要提供正确的secrect参数!");
            }

            String registerCode = request.getParameter("registerCode");
            if (StringUtils.isEmpty(registerCode)) {
                return JsonResultUtil.getServerErrorResult("报名测试失败,压力测试需要提供账号编号!");
            }
            if (StringUtils.isEmpty(itemIds)) {
                return JsonResultUtil.getServerErrorResult("报名测试失败, 报名需要提供报名项对应编号itemIds!");
            }

            if (StringUtils.isEmpty(siteCode)) {
                return JsonResultUtil.getServerErrorResult("报名测试失败, 报名需要提供报名项对应赛场编号siteCode!");
            }
            String ip = ServletUtil.getIpAddr(request);
            String referer = ServletUtil.getReferer(request);
            List<String> ids = new ArrayList<String>();
            if (itemIds.indexOf(",") > 0) {
                String[] split = itemIds.split(",");
                if (split != null && split.length > 0) {
                    for (String code : split) {
                        ids.add(code);
                    }
                }
            } else {
                ids.add(itemIds);
            }
            Player player = new Player();
            player.setSex("M");
            player.setPlayerName("名称" + (int) (Math.random() * 100));
            player.setPlayerPhone("206" + (long) (Math.random() * 10000000));
            player.setVerifyCode("002234");

            checkaddApply(ids, player);
            PayOrder order = orderServce.addApplyOrder(ids, siteCode, player, registerCode, ip,
                referer, RegTerminalEnum.WEICHAT);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("orderCode", order.getOrderCode());
            hashMap.put("url", "/order/confirm/" + order.getOrderCode());

            orderServce.completePaySuccess(order.getOrderCode(), null, null);
            hashMap.put("url", "/order/detail/" + order.getOrderCode());
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("报名失败，请稍后重试！！");
            // TODO: handle exception
        }
    }

    private void checkaddApply(List<String> itemIds, Player player) {
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
        String playerPhone = player.getPlayerPhone();
        if (StringUtils.isEmpty(playerPhone)) {
            throw new BusinessException("运动员名电话号码不能为空");
        }
        boolean checkVerifyCode = msgService.checkVerifyCode(playerPhone, verifyCode);
        if (!checkVerifyCode) {
            throw new BusinessException("验证码错误，请重新输入");
        }
    }

    @RequestMapping("confirm/{orderCode}")
    public String confirmOrder(@PathVariable(value = "orderCode") String orderCode, Model model) {
        PayOrder order = orderServce.getOrderDetail(orderCode);
        model.addAttribute("order", PayOrderVo.getVo(order));
        return "pages/pay_entyr";

    }

    /**
     * 微信支付回调
     * @param request
     * @param response
     * @throws Exception
     */

    @SuppressWarnings("unchecked")
    @RequestMapping("weichat/notify")
    public void weixinPayNotify(HttpServletRequest request, HttpServletResponse response,
                                RedirectAttributes redirectAttributes) throws Exception {
        //读取参数  
        InputStream inputStream;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();
        //废话代码一大堆，直接IOUtils不好吗？
        log.info("weixin pay callback parameters：" + sb);
        //解析xml成map  
        Map<String, String> m = new HashMap<String, String>();
        m = XMLUtil.doXMLParse(sb.toString());
        //过滤空 设置 TreeMap  
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        Iterator<String> it = m.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = m.get(parameter);
            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }
        //判断签名是否正确  
        if (WeichatUtil.isTenpaySign("UTF-8", packageParams, weichatConfig.weiChatPayKey)) {
            String resXml = "";
            if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
                // 这里是支付成功  
                String mch_id = (String) packageParams.get("mch_id");
                String openid = (String) packageParams.get("openid");
                String is_subscribe = (String) packageParams.get("is_subscribe");
                String out_trade_no = (String) packageParams.get("out_trade_no");
                String total_fee = (String) packageParams.get("total_fee");
                String transaction_id = (String) packageParams.get("transaction_id");
                log.info("mch_id:" + mch_id);
                log.info("openid:" + openid);
                log.info("is_subscribe:" + is_subscribe);
                log.info("out_trade_no:" + out_trade_no);
                log.info("total_fee:" + total_fee);
                log.info("transaction_id:" + transaction_id);
                if (out_trade_no.indexOf("_") > -1) {
                    out_trade_no = out_trade_no.split("_")[0];
                }
                log.info("out_trade_no(截取后):" + out_trade_no);
                orderServce.completePaySuccess(out_trade_no, transaction_id,
                    PayGateWayEnum.WEICHAT_PAY);
                log.info("支付成功");
                //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.  
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                         + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            } else {
                log.info("支付失败,错误信息：" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                         + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
            //------------------------------  
            //------------------------------  
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
            log.info("通知微信.异步确认成功");
        } else {
            log.info("通知签名验证失败");
        }
    }

    @RequestMapping("detail/{orderCode}")
    public String orderSuccess(@PathVariable(value = "orderCode") String orderCode, Model model,
                               HttpSession session) {
        try {
            String isEdit = "0"; //是从团体报名页过来的为1
            if (session.getAttribute(orderCode + "isEdit") != null) {
                if (session.getAttribute(orderCode + "isEdit").toString().equalsIgnoreCase("1")) {
                    isEdit = "1";
                    model.addAttribute("init", true);
                }
            }
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            String uri = applyDomain + "/order/detail/" + orderCode;
            uri = URLEncoder.encode(uri, "UTF-8");
            if (auth == null) {
                return "redirect:" + ucenterDomain + "?login_redirect=" + uri;
            }
            RegisterModel register = registerFacadeClient
                .getRegsiterByRegisterCode(auth.getRegisterCode());
            if (StringUtils.isBlank(register.getAccount())) {
                return "redirect:" + ucenterDomain + "/user/h5/phone?binding_redirect=" + uri;
            }
            model.addAttribute("account", auth.getRegister().getAccount());
            PayOrder order = orderServce.getOrderDetail(orderCode);
            if (order == null) {
                return "redirect:/game/type";
            }
            ApplyInfo applyInfo = order.getApplyInfos().get(0);
            String matchCode = applyInfo.getMatchCode();
            SpMatchInfoModel matchDetail = spMatchFacadeClient.getMatchDetail(matchCode);
            MatchDetailVo matchVo = MatchDetailVo.getVo(matchDetail);
            SpAthletesEnrollModel athietes = null;
            try {
                athietes = spAthletesFacadeClient.getAthietes(matchCode);
                String unitCode = applyInfo.getUnitCode();
                if (OrderStatusEnum.SUCCESS.getCode().equals(order.getOrderStatus())
                    && "lantianlvye".equals(unitCode)) {
                    List<FromTypeModel> enrollForm = athietes.getEnrollForm();
                    FromTypeModel fromType = new FromTypeModel();
                    fromType.setIsCustom(true);
                    fromType.setIsShow(true);
                    fromType.setIsRequired(true);
                    fromType.setName("参赛验证码");
                    fromType.setAttribute("参赛验证码");
                    enrollForm.add(fromType);
                }
            } catch (Exception e) {
                log.error("", e);
            }
            model.addAttribute("order", PayOrderDetailVo.getVo(order, athietes));
            if (isEdit.equalsIgnoreCase("0")) {
                model.addAttribute("detail", true);
            }
            model.addAttribute("isEdit", isEdit);
            model.addAttribute("useCard", session.getAttribute(orderCode + "useCard"));
            model.addAttribute("match", matchVo);
            if (OrderStatusEnum.SUCCESS.getCode().equals(order.getOrderStatus())) {
                return "pages/enroll_success";
            }
            if (OrderStatusEnum.WAIT_COMPLETE.getCode().equals(order.getOrderStatus())) {
                if (EventTypeEnums.group.getCode().equals(applyInfo.getEventType())) {
                    return "pages/team_enroll_form";
                }
                return "pages/user_wait_perfect";
            }
            if (OrderStatusEnum.WAIT_PAY.getCode().equals(order.getOrderStatus())) {
                return "pages/user_wait_pay";
            }
        } catch (Exception e) {
            log.error("", e);
            // TODO: handle exception
        }
        return "redirect:/game/type";
    }

    /**
     * 修改订单跳转
     * @param orderCode
     * @param model
     * @return
     */

    @RequestMapping("modify/{orderCode}")
    public String modifyOrder(@PathVariable(value = "orderCode") String orderCode, Model model,
                              HttpSession session) {
        try {
            String uri = applyDomain + "/order/modify/" + orderCode;
            uri = URLEncoder.encode(uri, "UTF-8");
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return "redirect:" + ucenterDomain + "?login_redirect=" + uri;
            }
            RegisterModel register = registerFacadeClient
                .getRegsiterByRegisterCode(auth.getRegisterCode());
            if (StringUtils.isBlank(register.getAccount())) {
                return "redirect:" + ucenterDomain + "/user/h5/phone?binding_redirect=" + uri;
            }
            model.addAttribute("account", auth.getRegister().getAccount());
            PayOrder order = orderServce.getOrderDetail(orderCode);
            if (order == null) {
                return "redirect:/game/type";
            }
            ApplyInfo applyInfo = order.getApplyInfos().get(0);
            String matchCode = applyInfo.getMatchCode();
            SpAthletesEnrollModel athietes = null;
            SpMatchInfoModel matchDetail = spMatchFacadeClient.getMatchDetail(matchCode);
            MatchDetailVo matchVo = MatchDetailVo.getVo(matchDetail);
            try {
                athietes = spAthletesFacadeClient.getAthietes(matchCode);
            } catch (Exception e) {
                log.error("", e);
            }
            model.addAttribute("order", PayOrderDetailVo.getVo(order, athietes));
            model.addAttribute("match", matchVo);
            if (EventTypeEnums.group.getCode().equals(applyInfo.getEventType())) {
                //                model.addAttribute("isEdit", "1");
                model.addAttribute("init", true);
                return "pages/team_enroll_form";
            }
            return "pages/user_modify";

        } catch (Exception e) {
            log.error("", e);
        }
        return "redirect:/game/type";
    }

    @ApiOperation(value = "获取订单列表", notes = "根据订单状态获取用户订单列表(个人中心调用)")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderStatus", value = "订单状态(WAIT_PAY:待支付,待完善：WAIT_COMPLETE,已成功：SUCCESS)", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "currentPage", value = "当前页数(不传默认取第一页)", required = false, dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "每页大小(不传默认为10)", required = false, dataType = "int", paramType = "query") })
    @RequestMapping(value = "list", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> getOrderList(@RequestParam(value = "orderStatus", required = false) OrderStatusEnum orderStatus,
                                            HttpSession session,
                                            @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

        //获取当前用户
        try {
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            Page<PayOrder> page = orderServce.selectPagePageOrderByStatus(currentPage, pageSize,
                auth.getRegisterCode(), orderStatus);
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

    @ApiOperation(value = "完善订单", notes = "用户填写运动员信息提交报名和完善报名信息时候调用")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderCode", value = "订单编号", required = true, dataType = "String", paramType = "form"),
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
                         @ApiImplicitParam(name = "playerWorkUnit", value = "工作单位", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerEmergencyName", value = "紧急联系人", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "playerEmergencyPhone", value = "紧急联系人电话", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "attOne", value = "附件1(附件正面,驾照,护照)", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "attTwo", value = "附件1(附件反面)", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "assettoId", value = "神力科莎账户ID", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "attUrl", value = "健康证", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "imgUrl", value = "头像地址", required = false, dataType = "String", paramType = "form")

    })
    @RequestMapping(value = "complete/order", method = { RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> completeOrder(@RequestParam(value = "orderCode", required = true) String orderCode,
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
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            RegisterModel register = registerFacadeClient
                .getRegsiterByRegisterCode(auth.getRegisterCode());
            if (StringUtils.isBlank(register.getAccount())) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.UN_BIND_PHONE.getCode(),
                    "用户未绑定手机号");
            }
            Player player = new Player();
            log.info("提交保存运动员信息：playerWeight:{},playerHeight:{},playerBirth：{},附件1：{},附件2:{}",
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
                    || "isCard".equals(paraName)) {
                    continue;
                }
                extProMap.put(paraName, request.getParameter(paraName));
            }
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
            player.setPlayerWorkUnit(playerWorkUnit);
            player.setVerifyCode(verifyCode);
            player.setAttOne(attOne);
            player.setAttTwo(attTwo);
            player.setAttUrl(attUrl);
            player.setAssettoId(assettoId);
            playerService.checkPlayer(player);
            Boolean checkVerify = true;
            if (StringUtils.isNotBlank(isCard) && isCard.equalsIgnoreCase("1")) { //使用我的报名卡，不校验验证码
                checkVerify = false;
            }
            if (checkVerify) {
                boolean checkVerifyCode = msgService.checkVerifyCode(playerPhone, verifyCode);
                if (!checkVerifyCode) {
                    throw new BusinessException("验证码错误，请重新输入");
                }
            }
            PayOrder payOrder = orderService.completeOrder(orderCode, register.getRegisterCode(),
                player);
            String orderStaus = payOrder.getOrderStatus();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderCode", payOrder.getOrderCode());
            map.put("url", "/order/confirm/" + payOrder.getOrderCode());
            if (payOrder.getOrderAmount() == 0) {
                orderServce.completePaySuccess(payOrder.getOrderCode(), null, null);
                map.put("url", "/order/detail/" + payOrder.getOrderCode());
            }
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

    @ApiOperation(value = "取消订单接口", notes = "取消订单接口")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })

    @RequestMapping(value = "cancel/order")
    @ResponseBody
    public Map<String, Object> cancelOrder(@RequestParam(value = "orderCode", required = true) String orderCode,
                                           HttpSession session) {
        try {
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            PayOrder payOrder = orderService.cancelOrder(orderCode, auth.getRegisterCode());
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

    /**
     * 完善团队信息
     * 
     * @param orderCode
     * @param teamName
     * @param verifyCode
     * @param leaderName
     * @param leaderPhone
     * @param request
     * @param session
     * @return
     */

    @ApiOperation(value = "完善团队报名信息", notes = "完善团队报名信息")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderCode", value = "订单编号", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "teamName", value = "团队名称", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "verifyCode", value = "验证码", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "isCard", value = "是否使用我的报名卡(0:没使用,1:使用)", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "leaderName", value = "领队名称", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "leaderPhone", value = "领队电话号码", required = true, dataType = "String", paramType = "form") })
    @RequestMapping(value = "copmplete/group")
    @ResponseBody
    public Map<String, Object> copmpleteGroup(@RequestParam(value = "orderCode", required = true) String orderCode,
                                              @RequestParam(value = "teamName", required = true) String teamName,
                                              @RequestParam(value = "verifyCode", required = true) String verifyCode,
                                              @RequestParam(value = "isCard", required = false) String isCard,
                                              @RequestParam(value = "leaderName", required = true) String leaderName,
                                              @RequestParam(value = "leaderPhone", required = true) String leaderPhone,
                                              HttpServletRequest request, HttpSession session) {
        try {
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            RegisterModel register = registerFacadeClient
                .getRegsiterByRegisterCode(auth.getRegisterCode());
            if (StringUtils.isBlank(register.getAccount())) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.UN_BIND_PHONE.getCode(),
                    "用户未绑定手机号");
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
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderCode", order.getOrderCode());
            map.put("url", "/order/confirm/" + order.getOrderCode());
            if (order.getOrderAmount() == 0) {
                orderServce.completePaySuccess(order.getOrderCode(), null, null);
                map.put("url", "/order/detail/" + order.getOrderCode());
            }
            map.put("orderStaus", order.getOrderStatus());
            return JsonResultUtil.getSuccessResult(map);

        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("提交团队信息失败");
        }
    }

}
