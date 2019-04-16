package com.efida.sports.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
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

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.sports.config.WeichatConfig;
import com.efida.sports.constants.Constants;
import com.efida.sports.entity.Goods;
import com.efida.sports.entity.GoodsOrder;
import com.efida.sports.entity.PayOrder;
import com.efida.sports.entity.UserAddress;
import com.efida.sports.enums.ErrorCodeEnum;
import com.efida.sports.enums.OrderStatusEnum;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.model.WeichatUnifiedModel;
import com.efida.sports.service.IGoodsOrderService;
import com.efida.sports.service.IGoodsService;
import com.efida.sports.service.IPayOrderService;
import com.efida.sports.service.IUserAddressService;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.util.HttpUtils;
import com.efida.sports.util.JsonResultUtil;
import com.efida.sports.util.ServletUtil;
import com.efida.sports.util.XMLUtil;
import com.efida.sports.util.weichat.WeichatUtil;
import com.efida.sports.web.vo.GoodsOrderVo;
import com.efida.sports.web.vo.GoodsVo;
import com.efida.sports.web.vo.UserAddressVo;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 商城功能
 * 
 * @author yanglei
 */
@Controller
@RequestMapping("shop")
public class ShopController {

    private static Logger         log = LoggerFactory.getLogger(ShopController.class);

    @Autowired
    private IUserAddressService   userAddressService;

    @Autowired
    private IGoodsOrderService    goodsOrderService;

    @Autowired
    private IGoodsService         goodsService;

    @Autowired
    private IPayOrderService      payOrderService;

    @Autowired
    private WeichatConfig         weichatConfig;

    @Autowired
    private UcenterAdapterService ucenterAdapterService;

    @Value("${ucenter-domain}")
    public String                 ucenterDomain;
    @Value("${apply-domain}")
    public String                 applyDomain;

    /**
     * 地址列表
     * 
     * @param session
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取地址列表", notes = "根据用户取用户地址列表")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "currentPage", value = "当前页数(不传默认取第一页)", required = false, dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "每页大小(不传默认为10)", required = false, dataType = "int", paramType = "query") })
    @RequestMapping(value = "/user/address/list", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> addressList(HttpSession session,
                                           @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        try {
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            String registerCode = auth.getRegisterCode();

            Page<UserAddress> page = this.userAddressService.selectPageAddress(currentPage,
                pageSize, registerCode);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("totalRow", page.getTotal());
            map.put("totalPage", page.getPages());
            map.put("currentPage", page.getCurrent());
            map.put("pageSize", page.getSize());
            map.put("list", UserAddressVo.getVos(page.getRecords()));
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取用户地址列表失败,请稍后重试！！！");
        }
    }

    /**
     * 地址详情
     * 
     * @param addressCode
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/user/address")
    public Map<String, Object> addressDetails(HttpSession session,
                                              @RequestParam(value = "addressCode", required = false) String addressCode,
                                              Model model) {
        try {
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            String registerCode = auth.getRegisterCode();
            UserAddress address = null;
            if (!StringUtils.isEmpty(addressCode))
                address = userAddressService.getAddressDetail(addressCode);
            else
                address = userAddressService.getDefaultAddress(registerCode);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("address", UserAddressVo.getVo(address));
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取地址详情失败,请稍后重试！！！");
        }
    }

    /**
     * 删除地址 TODO: 后期开发
     * 
     * @param addressCode
     * @param model
     * @return
     */
    @RequestMapping("/user/address/del/{addressCode}")
    public String delAddress(@PathVariable(value = "addressCode") String addressCode, Model model) {
        return "";
    }

    /**
     * 添加/编辑收件地址
     * 
     * @param addressCode
     * @param realname
     * @param mobile
     * @param province
     * @param city
     * @param area
     * @param address
     * @param request
     * @param session
     * @return
     */
    @ApiOperation(value = "添加/编辑收件地址", notes = "添加/编辑收件地址")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "addressCode", value = "地址编号", dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "realname", value = "收件人", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "mobile", value = "联系方式", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "province", value = "省份", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "city", value = "城市", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "area", value = "地区", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "isdefault", value = "默认", required = true, dataType = "boolean", paramType = "form"),
                         @ApiImplicitParam(name = "address", value = "详细地址", required = true, dataType = "String", paramType = "form") })
    @RequestMapping(value = "/user/address/edit")
    @ResponseBody
    public Map<String, Object> addressEdit(@RequestParam(value = "addressCode") String addressCode,
                                           @RequestParam(value = "realname", required = true) String realname,
                                           @RequestParam(value = "mobile", required = true) String mobile,
                                           @RequestParam(value = "province", required = true) String province,
                                           @RequestParam(value = "city", required = true) String city,
                                           @RequestParam(value = "area", required = true) String area,
                                           @RequestParam(value = "address", required = true) String address,
                                           @RequestParam(value = "isdefault", required = true) boolean isdefault,
                                           HttpServletRequest request, HttpSession session) {
        try {
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            String registerCode = auth.getRegisterCode();

            UserAddress userAddress = new UserAddress();
            userAddress.setRegisterCode(registerCode);
            userAddress.setAddressCode(addressCode);
            userAddress.setRealname(realname);
            userAddress.setMobile(mobile);
            userAddress.setProvince(province);
            userAddress.setCity(city);
            userAddress.setArea(area);
            userAddress.setAddress(address);
            userAddress.setIsdefault(isdefault);
            addressCode = this.userAddressService.saveOrUpdateAddress(userAddress);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("addressCode", addressCode);
            // 更新完完成后跳转到地址列表页面
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("保存地址信息失败");
        }
    }

    /**
     * 获取订单列表
     * 
     * @param orderStatus
     * @param session
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取商品订单列表", notes = "根据订单状态获取用户商品订单列表")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderStatus", value = "订单状态(WAIT_PAY:待支付,已成功：SUCCESS)", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "currentPage", value = "当前页数(不传默认取第一页)", required = false, dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "每页大小(不传默认为10)", required = false, dataType = "int", paramType = "query") })
    @RequestMapping(value = "/user/order/list", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> getOrderList(HttpSession session,
                                            @RequestParam(value = "orderStatus", required = false) OrderStatusEnum orderStatus,
                                            @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

        // 获取当前用户
        try {
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            String registerCode = auth.getRegisterCode();
            Page<GoodsOrder> page = goodsOrderService.selectPageOrderByStatus(currentPage, pageSize,
                registerCode, orderStatus);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("totalRow", page.getTotal());
            map.put("totalPage", page.getPages());
            map.put("currentPage", page.getCurrent());
            map.put("pageSize", page.getSize());
            map.put("list", GoodsOrderVo.getVos(page.getRecords()));
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取订单列表失败,请稍后重试！！！");
        }

    }

    /**
     * 订单详情
     * 
     * @param orderCode
     * @param model
     * @return
     */
    @RequestMapping("/user/order/{orderCode}")
    @ResponseBody
    public Map<String, Object> orderDetail(@PathVariable(value = "orderCode") String orderCode,
                                           Model model) {
        try {
            GoodsOrder order = goodsOrderService.getOrderDetail(orderCode);
            if (order == null)
                throw new BusinessException("订单不存在");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("order", GoodsOrderVo.getVo(order));
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取订单详情失败,请稍后重试！！！");
        }

    }

    /**
     * 下单
     * 
     * @param request
     * @param model
     * @param session
     * @param goodsCode
     * @param num
     * @param addressCode
     * @return
     */
    @ApiOperation(value = "商品提交订单", notes = "商品提交订单")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "goodsCode", value = "商品编号", dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "num", value = "数量", required = true, dataType = "int", paramType = "form"),
                         @ApiImplicitParam(name = "addressCode", value = "收件地址编号", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "remark", value = "备注", required = false, dataType = "String", paramType = "form") })
    @RequestMapping("/user/order/create")
    @ResponseBody
    public Map<String, Object> orderCreate(HttpServletRequest request, Model model,
                                           HttpSession session,
                                           @RequestParam(value = "goodsCode", required = true) String goodsCode,
                                           @RequestParam(value = "num", required = true) int num,
                                           @RequestParam(value = "addressCode", required = true) String addressCode,
                                           @RequestParam(value = "remark", required = false) String remark) {
        try {
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            String registerCode = auth.getRegisterCode();
            String ip = ServletUtil.getIpAddr(request);
            String referer = ServletUtil.getReferer(request);
            GoodsOrder order = goodsOrderService.addOrder(goodsCode, num, addressCode, registerCode,
                ip, referer, remark);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("orderCode", order.getOrderCode());
            hashMap.put("payOrderCode", order.getPayOrderCode());
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("订单创建失败，请稍后重试！！");
        }
    }

    /**
     * 获取商品详情 下单页面使用
     * 
     * @param goodsCode
     * @return
     */
    @ApiOperation(value = "获取商品详情")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "/goods/{goodsCode}", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> goodsDetail(@PathVariable(value = "goodsCode") String goodsCode) {
        try {
            Goods goods = this.goodsService.getGoodsByCode(goodsCode);
            if (goods == null)
                throw new BusinessException("商品不存在");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("goods", GoodsVo.getVo(goods));
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取商品失败,请稍后重试！！！");
        }
    }

    /**
     * 获取赛事对应的商品信息
     * 
     * @param goodsCode
     * @return
     */
    @ApiOperation(value = "获取赛事对应的商品信息")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @RequestMapping(value = "/goods/match/{matchCode}", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> goodsByMatchCode(@PathVariable(value = "matchCode") String matchCode) {
        try {
            if (matchCode == null || matchCode.trim().length() == 0)
                throw new BusinessException("赛事编号不能为空");
            Goods goods = this.goodsService.getGoodsByMatchCode(matchCode);
            if (goods == null)
                throw new BusinessException("商品不存在");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("goods", GoodsVo.getVo(goods));
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取商品失败,请稍后重试！！！");
        }
    }

    /**
     * 微信统一下单 生成预支付订单
     * 
     * @return
     */
    @RequestMapping("/wechat/pay")
    public String unifiedOrder(HttpServletRequest request, Model model, String payOrderCode,
                               String tradeType, HttpSession session) {

        //获取当前用户
        AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
        if (auth == null) {
            if (StringUtils.isNotBlank(request.getParameter("orderCode"))) {
                session.setAttribute(
                    Constants.AUTH_REDIRECT_URL,
                    "redirect:/shop/wechat/pay?orderCode=" + request.getParameter("orderCode")
                                                 + "&goodsCode=" + request.getParameter("goodsCode")
                                                 + "&address=" + request.getParameter("address")
                                                 + "&tradeType=" + tradeType + "&payOrderCode="
                                                 + payOrderCode);
            } else {
                session.setAttribute(
                    Constants.AUTH_REDIRECT_URL,
                    "redirect:/shop/wechat/pay?waitPayCode=" + request.getParameter("waitPayCode")
                                                 + "&tradeType=" + tradeType + "&payOrderCode="
                                                 + payOrderCode);
            }

            return "redirect:/auth/weixin/auth";
        }

        PayOrder payOrder = payOrderService.getOrderDetail(payOrderCode);
        WeichatUnifiedModel weichatUnified = weichatUnified(payOrder, auth.getOpenId(), tradeType,
            request);

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

    private WeichatUnifiedModel weichatUnified(PayOrder payOrde, String openId, String tradeType,
                                               HttpServletRequest request) {
        Map<String, String> tradeTypeMap = new HashMap<>();
        tradeTypeMap.put("internal", "JSAPI");
        tradeTypeMap.put("outside", "MWEB");
        WeichatUnifiedModel unified = new WeichatUnifiedModel();
        SortedMap<String, Object> params = new TreeMap<String, Object>();
        params.put("appid", weichatConfig.appId);// 公众号appid
        params.put("body", payOrde.getRemark()); // 说明
        params.put("mch_id", weichatConfig.mchId); // 商户号
        params.put("nonce_str", WeichatUtil.createNoncestr()); // 随机字符串
        params.put("notify_url", weichatConfig.notifyUrl); // 通知地址
        if (!tradeType.equals("outside")) {
            params.put("openid", openId);
        }
        String outTradeNo = payOrde.getOrderCode();
        unified.setOrderSeq(outTradeNo);
        params.put("out_trade_no", outTradeNo + "_" + tradeType);// 商户订单号 本系统自动生成的订单号
        if (tradeType.equals("outside")) {
            String url = weichatConfig.notifyUrl;
            if (url.split("/").length > 3) {
                url = url.split("/")[0] + "//" + url.split("/")[1] + url.split("/")[2];
            }
            String sceneInfo = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"" + url
                               + "\",\"wap_name\": \"znty\"}}";
            params.put("scene_info", sceneInfo); // 场景信息-h5使用
        }

        params.put("spbill_create_ip", ServletUtil.getIpAddress(request)); // 终端IP
        params.put("total_fee", payOrde.getOrderAmount() + "");// 总金额
        params.put("trade_type", tradeTypeMap.get(tradeType)); // 交易类型
        String sign = WeichatUtil.getSign(params, weichatConfig.weiChatPayKey);
        params.put("sign", sign);// 签名
        String requestXml = WeichatUtil.getRequestXml(params);
        log.info("微信统一下单请求参数：" + requestXml);
        String resXml = HttpUtils.executePost(weichatConfig.unifiedorder, requestXml);
        log.info("微信统一下单返回参数参数：" + resXml);
        Map<Object, Object> resultMap;
        try {
            resultMap = XMLUtil.doXMLParse(resXml);
            // result_code 和return_code都为SUCCESS的时候有返回
            if ("SUCCESS".equals(resultMap.get("return_code"))
                && "SUCCESS".equals(resultMap.get("result_code"))) {
                String appId = (String) resultMap.get("appid");// 微信公众号AppId
                String timeStamp = WeichatUtil.getCurrTime();// 当前时间戳
                String prepayId = "prepay_id=" + resultMap.get("prepay_id");// 统一下单返回的预支付id
                String nonceStr = WeichatUtil.buildRandom(20) + "";// 不长于32位的随机字符串
                String mwebUrl = resultMap.get("mweb_url") + "";
                SortedMap<String, Object> signMap = new TreeMap<String, Object>();// 自然升序map
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
     * 检查用户登录状态
     * 
     * @param request
     * @param model
     * @param session
     * @param goodsCode
     * @param num
     * @param addressCode
     * @return
     */
    @RequestMapping("/user/check/login")
    @ResponseBody
    public Map<String, Object> checkLogin(HttpServletRequest request, Model model,
                                          HttpSession session) {
        try {
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("检查用户登录状态失败，请稍后重试！！");
        }
    }

}
