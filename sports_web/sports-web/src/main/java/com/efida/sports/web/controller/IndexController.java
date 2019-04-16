/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.efida.sports.entity.PayOrder;
import com.efida.sports.enums.OrderStatusEnum;
import com.efida.sports.web.vo.PayOrderVo;

/**
 * 
 * @author zoutao
 * @version $Id: IndexController.java, v 0.1 2018年5月11日 上午11:04:17 zoutao Exp $
 */

@Controller
public class IndexController {
	@RequestMapping("static/{path}")
    public String orderSuccess(@PathVariable(value = "path") String path) {

        return "pages/" + path;

    }
	
	
	@RequestMapping("/")
	 public String index() {
        return "redirect:/game/type";

    }


}
