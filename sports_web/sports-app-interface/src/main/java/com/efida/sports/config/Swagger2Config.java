package com.efida.sports.config;

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
            .apis(RequestHandlerSelectors.basePackage("com.efida.sports.app.controller"))
            .paths(PathSelectors.any()).build();

    }

    //构建 api文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            //页面标题
            .title("全国智能体育运动会移动端API接口规范")
            //创建人
            .contact(
                new Contact("邹涛", "http://api.sport.api/swagger-ui.html", "zoutao@efida.com.cn"))
            //版本号
            .version("1.0").
            //描述
            description("全国智能体育运动会移动端API接口规范").build();
    }
}
