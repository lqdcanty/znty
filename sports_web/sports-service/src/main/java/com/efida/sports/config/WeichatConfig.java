/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author zoutao
 * @version $Id: WeichatPayConfig.java, v 0.1 2018年5月3日 下午4:28:27 zoutao Exp $
 */
@Configuration
public class WeichatConfig {
    @Value("${weichat.pay.mchId}")
    public String mchId;
    @Value("${weichat.appId}")
    public String appId;

    @Value("${weichat.pay.apiKey}")
    public String weiChatPayKey;
    @Value("${weichat.pay.unifiedorder}")
    public String unifiedorder;
    @Value("${weichat.pay.notifyUrl}")
    public String notifyUrl;

    @Value("${weichat.redirectUri}")
    public String redirectUri;

    @Value("${weichat.appSecret}")
    public String appSecret;

}
