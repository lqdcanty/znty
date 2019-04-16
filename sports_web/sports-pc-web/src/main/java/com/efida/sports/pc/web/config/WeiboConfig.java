package com.efida.sports.pc.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 微博配置信息
 * 
 * @author zengbo
 * @version $Id: WeiboConfig.java, v 0.1 2018年6月14日 上午11:12:00 zengbo Exp $
 */
@Configuration
public class WeiboConfig {

    @Value("${weibo.client_id}")
    public String clientId;
    @Value("${weibo.client_secret}")
    public String clientSecret;
    @Value("${weibo.redirect_uri}")
    public String redirectUri;
    @Value("${weibo.grant_type}")
    public String granttype;
    @Value("${weibo.code}")
    public String code;
    
}
