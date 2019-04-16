/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author zoutao
 * @version $Id: SMSConfig.java, v 0.1 2018年5月24日 下午3:54:43 zoutao Exp $
 */
@Configuration
public class SMSConfig {

    @Value("${sms.uid}")
    public String uid;
    @Value("${sms.pwd}")
    public String pwd;

}
