package com.efida.sports.pc.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * QQ配置信息
 * 
 * @author zengbo
 * @version $Id: QqConfig.java, v 0.1 2018年6月14日 上午11:15:09 zengbo Exp $
 */
@Configuration
public class QqConfig {

    @Value("${qq.client_id}")
    public String clientId;
    @Value("${qq.client_key}")
    public String clientKey;
    @Value("${qq.redirect_uri}")
    public String redirectUri;

}
