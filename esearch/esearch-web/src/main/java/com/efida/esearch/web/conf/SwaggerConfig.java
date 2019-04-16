package com.efida.esearch.web.conf;

import org.springframework.beans.factory.annotation.Value;
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

/**
 * Springboot 加载到参数后自动加载该配置
 * @author wang yi
 * @desc 
 * @version $Id: SwaggerConfig.java, v 0.1 2018年6月15日 下午3:19:07 wang yi Exp $
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value(value = "${spring.profiles.active}")
    private String springProfiles;

    @Bean
    public Docket createRestApi() {
        if (springProfiles.equals("dev")) {
            return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.efida.esearch.web.api"))
                .paths(PathSelectors.ant("/**")).build();//URL过滤
        } else {
            return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.efida.esearch.web.api")).build();
        }
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("搜索服务接口规范").description("搜索管理平台开发接口规范。 ")
            .termsOfServiceUrl("http://www.efida.com.cn/")
            .contact(new Contact("王依", "", "wang765aaa@163.com")).version("1.0").build();
    }
}