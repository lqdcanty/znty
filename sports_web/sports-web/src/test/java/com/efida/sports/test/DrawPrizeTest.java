package com.efida.sports.test;

import com.alibaba.fastjson.JSON;
import com.efida.sports.entity.DrawPrize;
import com.efida.sports.service.draw.DrawprizeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DrawPrizeTest extends BaseTest {

    @Autowired
    private DrawprizeService drpService;

//    @Test
    public void drawPrize() {
        int treadSize = 10;
        //创建一个可重用固定线程数的线程池
        ExecutorService pool = Executors.newFixedThreadPool(treadSize);
        //启动数据
        for (int s = 0; s < 1000; s++) {
            DrawpTestTread hander = new DrawpTestTread("AD201810181114",
                Math.round(Math.random() * 1000) + "", drpService);
            pool.execute(hander);
        }
        pool.shutdown();
        while (!pool.isTerminated()) {
        }
        List<DrawPrize> prizes = drpService.getAndCacheAllPrize("AD201810181114");
        System.err.println(JSON.toJSONString(prizes));
    }
}