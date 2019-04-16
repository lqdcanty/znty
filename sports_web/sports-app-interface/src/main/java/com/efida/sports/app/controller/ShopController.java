package com.efida.sports.app.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sports.app.vo.GoodsOrderVo;
import com.efida.sports.app.vo.GoodsVo;
import com.efida.sports.app.vo.UserAddressVo;
import com.efida.sports.config.AlipayConfig;
import com.efida.sports.config.WeichatConfig;
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
import com.efida.sports.service.dubbo.intergration.UcenterLoginFacadeClient;
import com.efida.sports.util.HttpUtils;
import com.efida.sports.util.JsonResultUtil;
import com.efida.sports.util.ServletUtil;
import com.efida.sports.util.XMLUtil;
import com.efida.sports.util.weichat.WeichatUtil;

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
@RequestMapping("/api/shop")
public class ShopController {

    private static Logger            log          = LoggerFactory.getLogger(ShopController.class);

    @Autowired
    private IUserAddressService      userAddressService;

    @Autowired
    private IGoodsOrderService       goodsOrderService;

    @Autowired
    private IGoodsService            goodsService;

    @Autowired
    private IPayOrderService         payOrderService;

    @Autowired
    private WeichatConfig            weichatConfig;

    @Autowired
    private UcenterLoginFacadeClient loginServiceClient;

    @Autowired
    private AlipayConfig             alipayConfig;

    // 实例化客户端
    private AlipayClient             alipayClient = null;

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
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "currentPage", value = "当前页数(不传默认取第一页)", required = false, dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "每页大小(不传默认为10)", required = false, dataType = "int", paramType = "query") })
    @RequestMapping(value = "/user/address/list", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> addressList(HttpSession session,
                                           @RequestParam(value = "token", required = true) String token,
                                           @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            String registerCode = register.getRegisterCode();
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
    @RequestMapping(value = "/user/address", method = { RequestMethod.GET })
    public Map<String, Object> addressDetails(HttpSession session,
                                              @RequestParam(value = "token", required = true) String token,
                                              @RequestParam(value = "addressCode", required = false) String addressCode,
                                              Model model) {
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            String registerCode = register.getRegisterCode();
            UserAddress address = null;
            if (!StringUtils.isEmpty(addressCode))
                address = userAddressService.getAddressDetail(addressCode);
            else
                address = userAddressService.getDefaultAddress(registerCode);
            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, Object> defaultResult = new HashMap<String, Object>();
            UserAddressVo vo = UserAddressVo.getVo(address);
            map.put("address", vo == null ? defaultResult : vo);
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
    @ApiImplicitParams({ @ApiImplicitParam(name = "addressCode", value = "地址编号", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "realname", value = "收件人", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "mobile", value = "联系方式", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "province", value = "省份", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "city", value = "城市", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "area", value = "地区", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "isdefault", value = "默认", required = true, dataType = "int", paramType = "form"),
                         @ApiImplicitParam(name = "address", value = "详细地址", required = true, dataType = "String", paramType = "form") })
    @RequestMapping(value = "/user/address/edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addressEdit(@RequestParam(value = "addressCode", required = false) String addressCode,
                                           @RequestParam(value = "token", required = true) String token,
                                           @RequestParam(value = "realname", required = true) String realname,
                                           @RequestParam(value = "mobile", required = true) String mobile,
                                           @RequestParam(value = "province", required = true) String province,
                                           @RequestParam(value = "city", required = true) String city,
                                           @RequestParam(value = "area", required = true) String area,
                                           @RequestParam(value = "address", required = true) String address,
                                           @RequestParam(value = "isdefault", required = true) int isdefault,
                                           HttpServletRequest request, HttpSession session) {
        log.info("*******************************user/address/edit     addressCode=" + addressCode);
        log.info("*******************************user/address/edit     token=" + token);
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            String registerCode = register.getRegisterCode();

            UserAddress userAddress = new UserAddress();
            userAddress.setRegisterCode(registerCode);
            userAddress.setAddressCode(addressCode);
            userAddress.setRealname(realname);
            userAddress.setMobile(mobile);
            userAddress.setProvince(province);
            userAddress.setCity(city);
            userAddress.setArea(area);
            userAddress.setAddress(address);
            userAddress.setIsdefault(false);
            if (isdefault == 1) {
                userAddress.setIsdefault(true);
            }
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
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "orderStatus", value = "订单状态(WAIT_PAY:待支付,已成功：SUCCESS)", required = false, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "currentPage", value = "当前页数(不传默认取第一页)", required = false, dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "每页大小(不传默认为10)", required = false, dataType = "int", paramType = "query") })
    @RequestMapping(value = "/user/order/list", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> getOrderList(HttpSession session,
                                            @RequestParam(value = "token", required = true) String token,
                                            @RequestParam(value = "orderStatus", required = false) OrderStatusEnum orderStatus,
                                            @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

        // 获取当前用户
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            String registerCode = register.getRegisterCode();
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
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "num", value = "数量", required = true, dataType = "int", paramType = "form"),
                         @ApiImplicitParam(name = "addressCode", value = "收件地址编号", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "remark", value = "备注", required = false, dataType = "String", paramType = "form") })
    @RequestMapping(value = "/user/order/create", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> orderCreate(HttpServletRequest request, Model model,
                                           HttpSession session,
                                           @RequestParam(value = "token", required = true) String token,
                                           @RequestParam(value = "goodsCode", required = true) String goodsCode,
                                           @RequestParam(value = "num", required = true) int num,
                                           @RequestParam(value = "addressCode", required = true) String addressCode,
                                           @RequestParam(value = "remark", required = false) String remark) {
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            String registerCode = register.getRegisterCode();
            String ip = ServletUtil.getIpAddr(request);
            String referer = ServletUtil.getReferer(request);
            GoodsOrder order = goodsOrderService.addOrder(goodsCode, num, addressCode, registerCode,
                ip, referer, remark);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("orderCode", order.getOrderCode());
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
    @RequestMapping("/goods/{goodsCode}")
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
     * 微信支付统一下单
     * 
     */

    @ApiOperation(value = "微信支付统一下单", notes = "使用微信支付调用")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderCode", value = "订单编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query") })
    @RequestMapping(value = "wechat/pay", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> weichatUnified(@RequestParam(value = "orderCode", required = true) String orderCode,
                                              @RequestParam(value = "token", required = false) String token) {
        try {
            PayOrder payOrder = payOrderService.checkOrderCanPay(orderCode);
            if (payOrder == null)
                throw new BusinessException("订单不存在");
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

    private WeichatUnifiedModel weichatUnified(PayOrder payOrde) {
        WeichatUnifiedModel unified = new WeichatUnifiedModel();
        SortedMap<String, Object> params = new TreeMap<String, Object>();
        params.put("appid", weichatConfig.appId);// 公众号appid
        params.put("mch_id", weichatConfig.mchId); // 商户号
        params.put("nonce_str", WeichatUtil.createNoncestr()); // 随机字符串
        String outTradeNo = payOrde.getOrderCode();
        unified.setOrderSeq(outTradeNo);
        params.put("out_trade_no", outTradeNo);// 商户订单号 本系统自动生成的订单号
        params.put("total_fee", payOrde.getOrderAmount() + "");// 总金额
        params.put("spbill_create_ip", payOrde.getOrderIp()); // 终端IP
        params.put("notify_url", weichatConfig.notifyUrl); // 通知地址
        params.put("trade_type", "APP"); // 交易类型
        params.put("body", payOrde.getRemark()); // 说明
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
                String timeStamp = String.valueOf(new Date().getTime() / 1000);// 当前时间戳
                String prepayId = resultMap.get("prepay_id").toString();// 统一下单返回的预支付id
                String nonceStr = WeichatUtil.buildRandom(20) + "";// 不长于32位的随机字符串
                SortedMap<String, Object> signMap = new TreeMap<String, Object>();// 自然升序map
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
    @RequestMapping(value = "/user/check/login", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> checkLogin(@RequestParam(value = "token", required = true) String token,
                                          HttpServletRequest request, Model model,
                                          HttpSession session) {
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
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

    /**
     * 支付宝支付统一下单
     */
    @ApiOperation(value = "支付宝支付统一下单", notes = "支付宝支付统一下单")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "orderCode", value = "订单编号", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "query") })
    @RequestMapping(value = "/alipay/unified", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> alipayUnified(@RequestParam(value = "orderCode", required = true) String orderCode,
                                             @RequestParam(value = "token", required = true) String token) {

        try {
            PayOrder order = payOrderService.checkOrderCanPay(orderCode);
            if (order == null)
                throw new BusinessException("订单不存在");
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

        model.setBody(order.getRemark());
        model.setSubject(subject);
        model.setOutTradeNo(order.getOrderCode());
        model.setTimeoutExpress(alipayConfig.overTime);
        double totalAmount = ((double) order.getOrderAmount()) / 100;// 支付时以元为单位
        model.setTotalAmount(format1(totalAmount));
        model.setProductCode("QUICK_MSECURITY_PAY");
        alipayRequest.setBizModel(model);
        alipayRequest.setNotifyUrl(alipayConfig.notifyUrl);// 在公共参数中设置回跳和通知地址
        AlipayTradeAppPayResponse sdkExecute = alipayClient.sdkExecute(alipayRequest);
        Map<String, Object> map = new HashMap<>();
        map.put("sign", sdkExecute.getBody());
        return map;
    }

    public static String format1(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.toString();
    }

    private void initClient() {
        if (alipayClient == null) {
            alipayClient = new DefaultAlipayClient(alipayConfig.aLiUrl, alipayConfig.appId,
                alipayConfig.privateKey, "json", "utf-8", alipayConfig.publicKey,
                alipayConfig.encryptionMethod);
        }
    }

}
