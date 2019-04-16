/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.impl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.efida.sports.service.CacheService;
import com.efida.sports.util.RedisUtil;

/**
 * 
 * @author zoutao
 * @version $Id: CacheServiceImpl.java, v 0.1 2018年1月21日 下午3:01:29 zoutao Exp $
 */
@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private Logger                        log = LoggerFactory.getLogger(getClass());

    @Override
    public String get(final String k) {
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] data = connection.get(RedisUtil.convert(k));
                if (data == null) {
                    return null;
                }
                return new String(data);
            }
        });
    }

    /** 
     * @see com.efida.DrawprizeService.module.cache.CacheService#getObj(java.lang.String)
     */
    @Override
    public Object getObj(final String k) {
        Object data = null;
        try {
            data = redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] data = connection.get(RedisUtil.convert(k));
                    if (data == null) {
                        return null;
                    }
                    try {
                        return RedisUtil.deSeialize(data);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Exception e) {
            log.error("get data from redis encounter an error ", e);
        }
        return data;
    }

    /** 
     * @see com.efida.common.udfs.DrawprizeService.cache.CacheService#remove(java.lang.String)
     */
    @Override
    public void remove(final String k) {
        redisTemplate.expire(k, -1, TimeUnit.MILLISECONDS);
    }

    /** 
     * @see com.efida.common.udfs.DrawprizeService.cache.CacheService#put(java.lang.String, java.lang.String, int)
     */
    @Override
    public boolean put(final String k, final String v, final int expireTime) {
        boolean res = false;
        try {
            res = redisTemplate.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.pSetEx(RedisUtil.convert(k), expireTime, RedisUtil.convert(v));
                    return true;
                }
            });
        } catch (Exception e) {
            log.error("save data to redis encounter an error ", e);
        }
        return res;
    }

    /** 
     * @see com.efida.DrawprizeService.module.cache.CacheService#putObj(java.lang.String, java.lang.String, int)
     */
    @Override
    public boolean putObj(final String k, final Object v, final int expireTime) {
        boolean res = false;
        try {

            res = redisTemplate.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    try {
                        connection.pSetEx(RedisUtil.convert(k), expireTime, RedisUtil.serialize(v));
                        return true;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        } catch (Exception e) {
            log.error("save data to redis encounter an error ", e);
        }
        return res;

    }

    /** 
     * @see com.efida.common.udfs.DrawprizeService.cache.CacheService#put(java.lang.String, java.lang.String)
     */
    @Override
    public void put(String k, String v) {
        put(k, v, 1000);
    }

}
