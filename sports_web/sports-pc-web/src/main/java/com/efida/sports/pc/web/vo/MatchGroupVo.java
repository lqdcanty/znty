/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.pc.web.vo;

import java.io.Serializable;
import java.util.List;

import com.efida.sport.admin.facade.model.SpMatchGroupAndItemInfoModel;

/**
 * 
 * @author 
 * @version $Id: MatchGroupVo.java, v 0.1 2018年5月25日 下午7:15:35 zoutao Exp $
 */
public class MatchGroupVo implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long   id;

    /**
     *排序
     */
    private int    sortIndex;

    /**
     * 比赛组编号
     */
    private String groupCode;

    /**
     * 比赛组名
     */
    private String groupName;

    List<EventVo>  events;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<EventVo> getEvents() {
        return events;
    }

    public void setEvents(List<EventVo> events) {
        this.events = events;
    }

    public static MatchGroupVo getVo(SpMatchGroupAndItemInfoModel model) {
        if (model == null) {
            return null;
        }
        MatchGroupVo vo = new MatchGroupVo();
        vo.setGroupName(model.getGroupName());
        vo.setSortIndex(model.getSortIndex());
        if (!model.getIsHasGroup()) {
            vo.setGroupName("项目名称");
        }
        return vo;

    }

}
