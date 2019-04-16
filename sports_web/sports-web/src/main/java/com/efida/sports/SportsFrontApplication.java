/**
 * 
 */
package com.efida.sports;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zoutao
 *
 */
@SpringBootApplication
@MapperScan(basePackages = { "com.efida.sports.*.mapper" })
@EnableAutoConfiguration
@EnableScheduling
public class SportsFrontApplication extends SpringBootServletInitializer {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootServletInitializer.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SportsFrontApplication.class, args);
    }
}
