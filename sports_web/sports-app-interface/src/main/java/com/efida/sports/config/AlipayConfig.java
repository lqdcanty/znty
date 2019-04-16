package com.efida.sports.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig {
    /**
     * 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/
     * pidAndKey.htm
     */
    @Value("${ALiPay.partner}")
    public String partner;
    //支付成功返回地址
    @Value("${ALiPay.returnUrl}")
    public String returnUrl;
    // 回调地址
    @Value("${ALiPay.notifyUrl}")
    public String notifyUrl;
    // 支付宝公钥
    @Value("${ALiPay.publicKey}")
    public String publicKey;
    // 支付宝私钥
    @Value("${ALiPay.privateKey}")
    public String privateKey;

    // 支付宝appId
    @Value("${ALiPay.appId}")
    public String appId;
    // 加密方式
    public String encryptionMethod = "RSA2";
    // 超时时间
    public String overTime         = "30m";

    // 支付宝网关
    public String aLiUrl           = "https://openapi.alipay.com/gateway.do";

}
