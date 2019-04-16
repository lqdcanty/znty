package com.efida.sports.web.Interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author zoutao
 * @version $Id: CommonInterceptorConfig.java, v 0.1 2017年10月15日 上午10:39:47  Exp $
 */
@Configuration
public class CommonInterceptorConfig extends WebMvcConfigurerAdapter {
    /** 
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry)
     */
    @Bean
    CommonInterceptor localInterceptor() {
        return new CommonInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localInterceptor()).addPathPatterns("/**");
    }

}