/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.draw;

import java.util.List;

import com.efida.sports.entity.DrawActivity;
import com.efida.sports.entity.DrawPrize;
import com.efida.sports.entity.DrawPrizeRecord;

/**
 * 
 * 抽奖相关服务
 * @author wang yi
 * @desc 
 * @version $Id: DrawprizeService.java, v 0.1 2018年10月18日 上午10:53:05 wang yi Exp $
 */
public interface DrawprizeService {

    /**
     * 获取所有奖品
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param activeCode 活动编号
     * @return
     */
    public List<DrawPrize> getAndCacheAllPrize(String activeCode);

    /**
     * 根据活动抽奖
     * @title
     * @methodName
     * @author wangyi
     * @description
      * @param activeCode 活动编号
     * @param registerCode 用户编号
     * @return
     */
    public DrawPrize luckDrawPrize(String activeCode, String registerCode);

    /**
     * 增加新记录数据
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @return
     */
    public DrawPrizeRecord addNewViewRecord(DrawPrizeRecord dr);

    /**
     * 检查活动是否允许参加
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param activeCode
     * @return 是否过期
     */
    public boolean checkActivityEnable(String activeCode);

    /**
     * 获取活动信息
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param activeCode
     * @return
     */
    public DrawActivity getActivityByCode(String activeCode);

    /**
     * 检查用户是否允许抽奖
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @return 是否允许抽奖
     */
    public void checkIsEnableDraw(String activeCode, String registerCode);

    /**
     * 获取用户参与记录
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param activeCode
     * @param registerCode
     * @return
     */
    DrawPrizeRecord getPrizeRecord(String activeCode, String registerCode);

    /**
     * 放入缓存中奖信息
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param activeCode
     * @param resultPizes
     * @return
     */
    List<DrawPrize> putAllPrize2Cache(String activeCode, List<DrawPrize> resultPizes);

}
