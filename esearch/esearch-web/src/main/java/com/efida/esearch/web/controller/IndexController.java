/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.efida.esearch.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author wang yi
 * @desc 
 * @version $Id: IndexController.java, v 0.1 2018年10月25日 下午4:12:39 wang yi Exp $
 */
@Controller("indexController")
@RequestMapping("")
public class IndexController {

    public static final String INDEX_PAGE = "view/pages/index";

    @RequestMapping("/")
    public String index() {
        return INDEX_PAGE;
    }

}
