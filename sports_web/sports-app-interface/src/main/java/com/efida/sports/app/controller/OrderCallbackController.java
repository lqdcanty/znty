/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alipay.api.internal.util.AlipaySignature;
import com.efida.sports.config.AlipayConfig;
import com.efida.sports.config.WeichatConfig;
import com.efida.sports.enums.PayGateWayEnum;
import com.efida.sports.service.IPayOrderService;
import com.efida.sports.util.XMLUtil;
import com.efida.sports.util.weichat.WeichatUtil;

import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author zoutao
 * @version $Id: OrderCallbackController.java, v 0.1 2018年6月14日 下午8:42:31 zoutao Exp $
 */
@Controller
@RequestMapping("order")
@ApiIgnore
public class OrderCallbackController {
    private static Logger    log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IPayOrderService orderService;

    @Autowired
    private WeichatConfig    weichatConfig;

    @Autowired
    private AlipayConfig     alipayConfig;

    /**
     * 授权回调地址 用户支付成功后 支付宝会直接访问这个地址 然后后台处理具体的业务逻辑
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "alipay/pay/callback", method = RequestMethod.POST)
    @ResponseBody
    public String notifyUrl(String trade_status, String out_trade_no, String trade_no,
                            HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("支付宝授权回调地址被调用,调用参数：trade_status" + trade_status + " ,out_trade_no:" + out_trade_no
                 + ",trade_no" + trade_no);
        if (StringUtils.isBlank(out_trade_no)) {
            log.error("out_trade_no 不能为空");
            return "fail";
        }
        if (StringUtils.isBlank(trade_no)) {
            log.error("trade_no 不能为空");
            return "fail";
        }
        if (StringUtils.isBlank(trade_status)) {
            log.error("trade_status 不能为空");
            return "fail";
        }
        request.setCharacterEncoding("utf-8");
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                    : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        try {
            if (AlipaySignature.rsaCheckV1(params, alipayConfig.publicKey, "UTF-8",
                alipayConfig.encryptionMethod)) {
                log.error("支付宝签名校验失败 params:" + params.toString());
                return "fail";
            }
            if("TRADE_SUCCESS".equals(trade_status)) {
            		orderService.completePaySuccess(out_trade_no, trade_no, PayGateWayEnum.ALIPAY);
            }
            log.info("支付宝授权回调地址被调用");
        } catch (Exception e) {
            log.error("", e);
            return "fail";
        }
        return "success";
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
                orderService.completePaySuccess(out_trade_no, transaction_id,
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

}
