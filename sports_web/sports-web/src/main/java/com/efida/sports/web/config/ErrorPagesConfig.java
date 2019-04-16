/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 
 * @author zoutao
 * @version $Id: ErrorPagesConfig.java, v 0.1 2018年3月31日 下午12:19:57 zoutao Exp $
 */
@Configuration
public class ErrorPagesConfig {
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                //状态码               错误页面的存储路径  
                ErrorPage errorPage400 = new ErrorPage(HttpStatus.BAD_REQUEST, "/pages/error");
                ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/pages/error");
                ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,
                    "/pages/error");
                container.addErrorPages(errorPage400, errorPage404, errorPage500);
            }
        };
    }
}
