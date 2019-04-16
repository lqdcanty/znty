package com.efida.sports.util;

import com.efida.sports.biz.GudongEnrollService;

/**
 * Created by wangyan on 2018/9/11.
 */
public class GudongSynchronousDataUtil {

    private static GudongEnrollService gudongEnrollService = SpringContextHolder.getBean(GudongEnrollService.class);

    public static void notifyGudong(String orderCode){
        gudongEnrollService.notifyGudong(orderCode);
    }

}
