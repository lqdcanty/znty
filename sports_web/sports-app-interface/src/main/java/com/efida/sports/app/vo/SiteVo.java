/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.efida.sport.admin.facade.model.SpPlayingAreaModel;
import com.efida.sports.util.DateUtil;

/**
 * 
 * @author zoutao
 * @version $Id: SiteVo.java, v 0.1 2018年5月25日 下午5:04:58 zoutao Exp $
 */
public class SiteVo {

    /**
     * 分赛场名称名称
     */
    private String fieldName;
    /**
    * 赛场编号
    */
    private String siteCode;

    /**
     * 比赛时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 赛场地址
     */
    private String fieldAddress;

    /**
     * 赛场经度
     */
    private String lon;

    /**
     *  赛场纬度
     */
    private String lat;

    /**
     * 当前距离
     */
    private String distance;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFieldAddress() {
        return fieldAddress;
    }

    public void setFieldAddress(String fieldAddress) {
        this.fieldAddress = fieldAddress;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public static List<SiteVo> getVos(List<SpPlayingAreaModel> models) {
        List<SiteVo> list = new ArrayList<SiteVo>();
        if (models == null || models.size() < 1) {
            return list;

        }
        for (SpPlayingAreaModel model : models) {
            SiteVo vo = getVo(model);
            if (vo != null) {
                list.add(vo);
            }
        }
        return list;
    }

    private static SiteVo getVo(SpPlayingAreaModel model) {
        if (model == null) {
            return null;
        }
        SiteVo vo = new SiteVo();
        vo.setEndTime(DateUtil.format(model.getEndTime()));
        vo.setFieldAddress(model.getFieldAddress());
        vo.setFieldName(model.getFieldName());
        vo.setStartTime(DateUtil.format(model.getStartTime()));
        vo.setSiteCode(model.getFieldCode());
        if (model.getLatitude() != null) {
            vo.setLat(model.getLatitude().toString());
        }
        if (model.getLongitude() != null) {
            vo.setLon(model.getLongitude().toString());
        }
        if (model.getDistance() != null) {
            vo.setDistance(
                model.getDistance().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");
        }
        return vo;
    }
}
