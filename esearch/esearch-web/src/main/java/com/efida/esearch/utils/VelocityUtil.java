package com.efida.esearch.utils;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.*;

public class VelocityUtil {
    private final static Logger logger            = LoggerFactory
            .getLogger(VelocityUtil.class);
    private static VelocityEngine engine = new VelocityEngine();
    public static String replaceTplByVelocity(String tplStr, List<String> paramNames,
                                       Map<String, String> params) {
        StringWriter writer = new StringWriter();
        String result=null;
        VelocityContext context = new VelocityContext();
        logger.info("velocity 前====:"+tplStr);
        if (paramNames!=null && paramNames.size() > 0) {
            if (params == null)
                params = new HashMap<>();
            String temp;
            for (int i = 0; i < paramNames.size(); i++) {
                temp = paramNames.get(i);
                if (!params.containsKey(temp)) {
                    //参数未提供，直接异常,这里如果做严格强校验，输出异常
                    context.put(temp, "");
                } else {
                    context.put( temp,dealReplaceValue(params.get(temp)));
                }
            }
            try {
                engine.evaluate(context, writer, "", tplStr);
                result=writer.toString();
                logger.info("velocity 后====:"+result);
                return result;
            } catch (Exception c) {
                return null;
            }
        } else {


            if(params.size()>0){
                Set<String> set =params.keySet();
                Iterator<String> iterator=set.iterator();
                String key="";
                while (iterator.hasNext()){
                    key=iterator.next();
                    context.put( key,dealReplaceValue(params.get(key)));
                }
                try {
                    engine.evaluate(context, writer, "", tplStr);
                    result=writer.toString();
                    logger.info("velocity 后====:"+result);
                    return result;
                } catch (Exception c) {
                    return null;
                }
            }else{
                return tplStr;
            }

        }
    }

    public static String dealReplaceValue(String source){
        if(source==null) return "";
        else return source.replaceAll("\"","\\\\\"");
    }
}
