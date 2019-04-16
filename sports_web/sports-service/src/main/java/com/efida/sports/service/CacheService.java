/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service;

/**
 * 
 * @author zoutao
 * @version $Id: CacheService.java, v 0.1 2018年1月21日 下午2:59:27 zoutao Exp $
 */
public interface CacheService {

    /**
     * 
     * 根据key 获取redis的值
     * 返回String
     * @param k
     * @return
     */
    public String get(final String k);

    /**
     * @title 获取Object
     * @methodName
     * @description
     * @param k
     * @return
     */
    public Object getObj(final String k);

    /**
     * 
     * @title 移除数据
     * @methodName
     * @description
     * @param k
     * @return
     */
    public void remove(final String k);

    /***
     * @title
     * @methodName
     * @description
     * @param k
     * @param v
     * @param expireTime timeunit millseconds -1 will never remove 
     */
    public boolean put(final String k, final String v, int expireTime);

    /**
     * @title
     * @methodName
     * @description
     * @param k
     * @param v
     * @param expireTime
     */
    public boolean putObj(final String k, final Object v, int expireTime);

    /***
     * @title default expired time 60s
     * @methodName
     * @description
     * @param k
     * @param v
     */
    public void put(final String k, final String v);

}
