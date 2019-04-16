package com.efida.esearch;

import com.efida.esearch.utils.VelocityUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.efida.esearch.sysconfig.GlobalInterceptor;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author tangp
 * @desc 
 * @version $Id: DmpStartApplication.java, v 0.1 2018年9月18日 $
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
@MapperScan(basePackages = { "com.efida.esearch.mapper" })
public class EsearchStartApplication extends WebMvcConfigurationSupport {
    public static void main(String[] args) {

        SpringApplication.run(EsearchStartApplication.class, args);
    }

    public void addInterceptors(InterceptorRegistry registry) {



        super.addInterceptors(registry);
        registry.addInterceptor(new GlobalInterceptor()).addPathPatterns("/**");
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/");
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }
}
