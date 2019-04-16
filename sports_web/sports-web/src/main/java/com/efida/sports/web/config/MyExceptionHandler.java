/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 
 * @author zoutao
 * @version $Id: MyExceptionHandler.java, v 0.1 2018年3月31日 下午12:24:34 zoutao Exp $
 */
@ControllerAdvice
public class MyExceptionHandler {
    private static Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public String MyExceptionHandler(Exception e) {
        log.error("", e);
        return "/pages/error";
    }
}
