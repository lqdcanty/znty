package com.efida.sports.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
            //为当前包路径
            .apis(RequestHandlerSelectors.basePackage("com.efida.sports.web.controller"))
            .paths(PathSelectors.any()).build();

    }

    //构建 api文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            //页面标题
            .title("首届全国智能体育运动会数据交换平台接口规范")
            //创建人
            .contact(
                new Contact("邹涛", "http://api.sport.api/swagger-ui.html", "zoutao@efida.com.cn"))
            //版本号
            .version("1.0")
            //描述
            .description("数据交换平台适用于“智体运动会”，利用该平台可以建立自上而下的数据交换体系，统一数据交换标准，统一交换渠道，实现数据中心级联和数据采集。"
                         + "数据交换平台由管理控制层与传输层组成，管理控制层提供平台管理、接收管理、发送管理等管理功能和数据接收服务、数据发送服务、数据抽取服务等一系列服务功能；传输层提供基于HTTP协议的传输通道。")
            .build();
    }
}
