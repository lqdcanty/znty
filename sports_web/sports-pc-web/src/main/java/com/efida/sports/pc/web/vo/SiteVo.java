/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.pc.web.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.efida.sport.admin.facade.model.SpPlayingAreaModel;

/**
 * 
 * @author 
 * @version $Id: SiteVo.java, v 0.1 2018年5月25日 下午5:04:58 zoutao Exp $
 */
public class SiteVo implements Serializable {

    /**  */
    private static final long  serialVersionUID = 1L;

    /**
     * 分赛场名称名称
     */
    private String             fieldName;
    /**
    * 赛场编号
    */
    private String             siteCode;

    /**
     * id
     */
    private Long               id;

    /**
     * 比赛时间
     */
    private Date               startTime;

    /**
     * 结束时间
     */
    private Date               endTime;

    /**
     * 赛场地址
     */
    private String             fieldAddress;

    /**
     * 比赛组
     */
    private List<MatchGroupVo> groups;

    private List<GroupItemVo>  items;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getFieldAddress() {
        return fieldAddress;
    }

    public void setFieldAddress(String fieldAddress) {
        this.fieldAddress = fieldAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public List<MatchGroupVo> getGroups() {
        return groups;
    }

    public void setGroups(List<MatchGroupVo> groups) {
        this.groups = groups;
    }

    public List<GroupItemVo> getItems() {
        return items;
    }

    public void setItems(List<GroupItemVo> items) {
        this.items = items;
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
        vo.setEndTime(model.getEndTime());
        vo.setFieldAddress(model.getFieldAddress());
        vo.setFieldName(model.getFieldName());
        vo.setStartTime(model.getStartTime());
        vo.setSiteCode(model.getFieldCode());
        return vo;
    }

    public static SiteVo getVo(SpPlayingAreaModel model, List<MatchGroupVo> groups) {
        if (model == null) {
            return null;
        }
        SiteVo vo = new SiteVo();
        vo.setEndTime(model.getEndTime());
        vo.setFieldAddress(model.getFieldAddress());
        vo.setFieldName(model.getFieldName());
        vo.setStartTime(model.getStartTime());
        vo.setSiteCode(model.getFieldCode());
        vo.setGroups(groups);
        return vo;
    }

}
