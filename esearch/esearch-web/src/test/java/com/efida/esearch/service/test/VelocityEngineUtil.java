/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.esearch.service.test;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

/**
 * 
 * @author Lenovo
 * @version $Id: VelocityEngineUtil.java, v 0.1 2018年10月17日 下午6:50:16 Lenovo Exp $
 */
public class VelocityEngineUtil {

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class",
            "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty("input.encoding", "UTF-8");
        properties.setProperty("output.encoding", "UTF-8");
        properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        VelocityEngine velocityEngine = new VelocityEngine(properties);
        VelocityContext context = new VelocityContext();
        context.put("keyword", "keyword关键词");
        context.put("type", "type-1");
        StringWriter sw = new StringWriter();

        String str = "{${keyword}}},{\"title\": \"${keyword}\"}}]}}\"${type}\"}}]}}}";
        velocityEngine.evaluate(context, sw, "logtag", str);
        System.out.println(sw.toString());

        str = "#if(!$keyword) keyword is null #else   keyword $keyword  is not null   #end";
        context.put("keyword", null);
        sw = new StringWriter();
        velocityEngine.evaluate(context, sw, "logtag", str);
        System.out.println("keyword is null:" + sw.toString());

        sw = new StringWriter();
        context.put("keyword", "关键词");
        velocityEngine.evaluate(context, sw, "logtag", str);
        System.out.println("keyword is not null :" + sw.toString());

    }
}
