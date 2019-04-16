/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.efida.sports.app.converter;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * 
 * @author zoutao
 * @version $Id: WebConfigBeans.java, v 0.1 2017年5月16日 下午5:29:29 zoutao Exp $
 */
@Configuration
public class WebConfigBeans {
    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    /**
     * 增加字符串转日期的功能
     */
    @PostConstruct
    public void initEditableValidation() {
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter
            .getWebBindingInitializer();
        if (initializer.getConversionService() != null) {
            GenericConversionService genericConversionService = (GenericConversionService) initializer
                .getConversionService();
            genericConversionService.addConverter(new DateConverter());
        }

    }
}
