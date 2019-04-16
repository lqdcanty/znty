/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * 
 * @author zoutao
 * @version $Id: SportsAppApplication.java, v 0.1 2018年6月12日 下午12:45:39 zoutao Exp $
 */
@SpringBootApplication
@MapperScan(basePackages = { "com.efida.sports.*.mapper" })
@EnableAutoConfiguration
public class SportsAppApplication {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootServletInitializer.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SportsAppApplication.class, args);
    }

}
