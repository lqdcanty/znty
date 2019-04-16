/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.efida.sports.entity.DrawPrize;
import com.efida.sports.service.draw.DrawprizeService;

/**
 * 
 * @author wang yi
 * @desc 
 * @version $Id: DrawpTestTread.java, v 0.1 2018年10月18日 下午9:15:23 wang yi Exp $
 */
public class DrawpTestTread implements Runnable {

    private String           activeCode;

    private String           registerCode;

    private DrawprizeService drpService;

    private Logger           log = LoggerFactory.getLogger(this.getClass());

    public DrawpTestTread(String activeCode, String registerCode, DrawprizeService drpService) {
        this.activeCode = activeCode;
        this.registerCode = registerCode;
        this.drpService = drpService;
    }

    @Override
    public void run() {
        System.err.println("线程: " + Thread.currentThread().getName() + " 开始执行抽奖...");
        long startM = System.currentTimeMillis();
        DrawPrize luckDrawPrize = drpService.luckDrawPrize(activeCode, registerCode);
        long endM = System.currentTimeMillis();
        System.err.println("线程: " + Thread.currentThread().getName() + "花费:" + ((endM - startM))
                           + "ms " + " 获得奖:" + luckDrawPrize.getPrizeName());
    }

}
