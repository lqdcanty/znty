/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * 
 * @author zoutao
 * @version $Id: RedisSessionConfig.java, v 0.1 2018年5月23日 下午2:32:08 zoutao Exp $
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 7200)
public class RedisSessionConfig {
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        //cookie名字
        defaultCookieSerializer.setCookieName("sessionId");
        //域
        defaultCookieSerializer.setDomainName("zntyydh.com");
        //存储路径
        defaultCookieSerializer.setCookiePath("/");
        return defaultCookieSerializer;
    }
}
