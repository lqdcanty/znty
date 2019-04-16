/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.draw.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.efida.sports.entity.DrawActivity;
import com.efida.sports.entity.DrawPrize;
import com.efida.sports.entity.DrawPrizeRecord;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.mapper.DrawActivityMapper;
import com.efida.sports.mapper.DrawPrizeMapper;
import com.efida.sports.mapper.DrawPrizeRecordMapper;
import com.efida.sports.model.Gift;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.draw.DrawprizeService;
import com.efida.sports.util.DateUtil;

/**
 * 抽奖活动实现
 * @author wang yi
 * @desc 
 * @version $Id: DrawprizeServiceImpl.java, v 0.1 2018年10月18日 下午2:20:40 wang yi Exp $
 */
@Service
public class DrawprizeServiceImpl implements DrawprizeService {

    @Autowired
    private DrawPrizeRecordMapper recordMapper;

    @Autowired
    private DrawActivityMapper    activityMapper;

    @Autowired
    private DrawPrizeMapper       prizeMapper;

    @Autowired
    private CacheService          cahceService;

    /**
     * 奖品缓存头
     */
    private final String          dwp_target   = "dwp_prizes";

    /**
     * 活动缓存头
     */
    private final String          aty_target   = "dwp_activity";

    /**
     * 奖品缓存时间 单位 ms
     */
    private final int             cacheDwpTime = 5 * 60 * 1000;

    /**
     * 缓存活动时间
     */
    private final int             cacheAtyTime = 1 * 60 * 1000;

    private Logger                log          = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("unchecked")
    @Override
    public synchronized List<DrawPrize> getAndCacheAllPrize(String activeCode) {
        List<DrawPrize> resultPizes = null;
        //加缓存
        resultPizes = (List<DrawPrize>) cahceService.getObj(getDwpTargetKey(activeCode));
        if (resultPizes != null) {
            return resultPizes;
        }
        resultPizes = getAllPrize(activeCode);
        putAllPrize2Cache(activeCode, resultPizes);
        return resultPizes;
    }

    @Override
    public synchronized List<DrawPrize> putAllPrize2Cache(String activeCode,
                                                          List<DrawPrize> resultPizes) {
        cahceService.putObj(getDwpTargetKey(activeCode), resultPizes, cacheDwpTime);
        return resultPizes;
    }

    private String getDwpTargetKey(String activeCode) {
        return dwp_target + "_" + activeCode;
    };

    /**
     * 获取所有奖品
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param activeCode
     * @return
     */
    private List<DrawPrize> getAllPrize(String activeCode) {
        EntityWrapper<DrawPrize> entityWrapper = new EntityWrapper<DrawPrize>();
        entityWrapper.eq("activity_code", activeCode);
        List<DrawPrize> resultPizes = prizeMapper.selectList(entityWrapper);
        return resultPizes;
    }

    @Override
    public synchronized DrawPrize luckDrawPrize(String activeCode, String registerCode) {
        DrawPrize resultPrize = getUnDrawPrize(getAndCacheAllPrize(activeCode));
        //setp1 是否中奖
        boolean isLuck = getIsLuck(activeCode, registerCode);
        if (!isLuck) {
            //写入中奖信息
            DrawPrizeRecord drawPrizeRecord = new DrawPrizeRecord();
            drawPrizeRecord.setActivityCode(activeCode);
            drawPrizeRecord.setRegisterCode(registerCode);
            drawPrizeRecord.setOperationTime(new Date());
            drawPrizeRecord.setIsDraw(1);
            drawPrizeRecord.setPrizeCode(resultPrize.getPrizeCode());
            drawPrizeRecord.setPrizeName(resultPrize.getPrizeName());
            recordMapper.upOverDrawInfo(drawPrizeRecord);
            return resultPrize;
        }
        return startLuckPrize(activeCode, registerCode);
    }

    @Transactional
    private DrawPrize startLuckPrize(String activeCode, String registerCode) {
        activityMapper.lockActivityByCode(activeCode);
        List<DrawPrize> allPrize = getAndCacheAllPrize(activeCode);
        DrawPrize resultPrize = getUnDrawPrize(allPrize);
        //setp2 获取该用户中奖的奖品
        List<DrawPrize> prize = getValiblePrize(allPrize);
        //动态计算中奖概率
        List<Gift> gifts = getDynamicGift(prize);
        Gift gift = luckGift(gifts);
        resultPrize = getPrizeByGift(gift, allPrize);
        if (resultPrize == null) {
            resultPrize = getUnDrawPrize(allPrize);
        }
        //有剩余奖品时候减少奖品数量
        if (resultPrize.getPrizeNumber() != -1 && resultPrize.getRemainNumber() > 0) {
            prizeMapper.upPrizeRemainSize(resultPrize.getId());
            for (DrawPrize prize1 : allPrize) {
                if (prize1.getPrizeCode().equals(resultPrize.getPrizeCode())) {
                    int remain = prize1.getRemainNumber() - 1;
                    if (remain < 1) {
                        remain = 0;
                    }
                    prize1.setRemainNumber(remain);
                }
            }
            //刷新奖品信息到缓存
            putAllPrize2Cache(activeCode, allPrize);
        }

        //写入中奖信息
        DrawPrizeRecord drawPrizeRecord = new DrawPrizeRecord();
        drawPrizeRecord.setActivityCode(activeCode);
        drawPrizeRecord.setRegisterCode(registerCode);
        drawPrizeRecord.setOperationTime(new Date());
        drawPrizeRecord.setIsDraw(1);
        drawPrizeRecord.setPrizeCode(resultPrize.getPrizeCode());
        drawPrizeRecord.setPrizeName(resultPrize.getPrizeName());
        recordMapper.upOverDrawInfo(drawPrizeRecord);
        return resultPrize;
    }

    /**
     * 奖品转换
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param gift
     * @param allPrize
     * @return
     */
    private DrawPrize getPrizeByGift(Gift gift, List<DrawPrize> allPrize) {
        if (CollectionUtils.isEmpty(allPrize) || gift == null) {
            return null;
        }
        for (DrawPrize drawPrize : allPrize) {
            if (gift.getCode().equals(drawPrize.getPrizeCode())) {
                return drawPrize;
            }
        }
        return null;
    }

    private Gift luckGift(List<Gift> gifts) {
        if (gifts == null || gifts.size() < 1) {
            return null;
        }
        double rd = Math.random();
        for (Gift git : gifts) {
            if (rd >= git.getStartValue() && rd < git.getEndValue()) {
                return git;
            }
        }
        return null;
    }

    /**
     * 动态计算中奖概率
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param prize 奖品信息
     * @return
     */
    private List<Gift> getDynamicGift(List<DrawPrize> prize) {
        List<Gift> gifts = new ArrayList<Gift>();
        double totalGift = 0;
        for (DrawPrize drawPrize : prize) {
            Integer tprizeNumber = drawPrize.getRemainNumber();
            totalGift = totalGift + tprizeNumber;
        }
        double lastEndIndex = 0;
        double lastStartIndex = 0;
        for (DrawPrize drawPrize : prize) {
            if (drawPrize.getRemainNumber() < 1) {
                continue;
            }
            Gift gift = new Gift();
            gift.setCode(drawPrize.getPrizeCode());
            gift.setName(drawPrize.getPrizeName());
            gift.setProb(drawPrize.getRemainNumber() * 1.00 / totalGift);
            gift.setStartValue(lastStartIndex);
            lastEndIndex = lastStartIndex + gift.getProb();
            gift.setEndValue(lastEndIndex);
            lastStartIndex = lastEndIndex;
            gifts.add(gift);
        }
        return gifts;
    }

    /**
     * 获取可使用的非谢谢惠顾的奖品
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param dws
     * @return
     */
    private List<DrawPrize> getValiblePrize(List<DrawPrize> dws) {
        List<DrawPrize> results = new ArrayList<DrawPrize>();
        for (DrawPrize drawPrize : dws) {
            if (!drawPrize.getPrizeType().equalsIgnoreCase("-1")) {
                results.add(drawPrize);
            }
        }

        return results;
    }

    /**
    * 获取未中奖的特殊奖品
    * @title
    * @methodName
    * @author wangyi
    * @description
    * @param dw
    * @return
    */
    private DrawPrize getUnDrawPrize(List<DrawPrize> dw) {
        for (DrawPrize drawPrize : dw) {
            if (drawPrize.getPrizeType().equalsIgnoreCase("-1")) {
                return drawPrize;
            }
        }
        return null;
    }

    /**
     * 根据中间率计算用户是否中奖
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param activeCode
     * @param registerCode
     * @return
     */
    private boolean getIsLuck(String activeCode, String registerCode) {
        DrawActivity activity = getActivityByCode(activeCode);
        BigDecimal ratio = activity.getRatio();
        double rd = Math.random();
        if (rd < ratio.doubleValue()) {
            return true;
        }
        return false;
    }

    @Override
    public DrawPrizeRecord addNewViewRecord(DrawPrizeRecord dr) {
        recordMapper.insert(dr);
        return dr;
    }

    @Override
    public boolean checkActivityEnable(String activeCode) {
        DrawActivity acityModel = getActivityByCode(activeCode);
        if (acityModel.getStatus() == 0) {
            return false;
        }
        Date now = new Date();
        //当前时间大于开始时间  当前时间小于结束时间
        if (DateUtil.compareDate(now, acityModel.getStartTime())
            && DateUtil.compareDate(acityModel.getEndTime(), now)) {
            return true;
        }
        return false;
    }

    @Override
    public synchronized DrawActivity getActivityByCode(String activeCode) {
        DrawActivity drawActivity = null;
        /*      String cacheKey = aty_target + "_" + activeCode;
        //加缓存
        drawActivity = (DrawActivity) cahceService.getObj(cacheKey);
        if (drawActivity != null) {
            return drawActivity;
        }
           */
        DrawActivity drawActivitys = new DrawActivity();
        drawActivitys.setActivityCode(activeCode);
        drawActivity = activityMapper.selectOne(drawActivitys);
        /*cahceService.putObj(cacheKey, drawActivity, cacheAtyTime);*/
        return drawActivity;
    }

    @Override
    public void checkIsEnableDraw(String activeCode, String registerCode) {
        DrawActivity activity = getActivityByCode(activeCode);
        if (activity == null) {
            throw new BusinessException("系统繁忙,请稍后再试");
        }
        Date now = new Date();
        //当前时间小于开始时间
        if (DateUtil.compareDate(activity.getStartTime(), now)) {
            throw new BusinessException("抽奖活动未开始");
        }
        //当前时间大于结束时间
        if (DateUtil.compareDate(now, activity.getEndTime())) {
            throw new BusinessException("抽奖活动已结束");
        }
        DrawPrizeRecord dp = getPrizeRecord(activeCode, registerCode);
        if (dp != null && dp.getIsDraw() != 0) {
            throw new BusinessException("每个用户只能抽一次,上次抽奖结果为:" + dp.getPrizeName());
        }
    }

    @Override
    public DrawPrizeRecord getPrizeRecord(String activeCode, String registerCode) {
        DrawPrizeRecord drawPrizeRecord = new DrawPrizeRecord();
        drawPrizeRecord.setActivityCode(activeCode);
        drawPrizeRecord.setRegisterCode(registerCode);
        return recordMapper.selectOne(drawPrizeRecord);
    }

}
