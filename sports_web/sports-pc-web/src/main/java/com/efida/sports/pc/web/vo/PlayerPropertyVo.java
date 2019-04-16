/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.pc.web.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.efida.sport.admin.facade.model.FromTypeModel;
import com.efida.sport.admin.facade.model.SpAthletesEnrollModel;

/**
 * 
 * @author zoutao
 * @version $Id: PlayerPropertyVo.java, v 0.1 2018年5月28日 下午2:49:05 zoutao Exp $
 */
public class PlayerPropertyVo {

    /**
    * 属性名称
    */
    private String  attributeName;
    /**
     * 中文名称
     */
    private String  name;
    /**
     * 类型
     */
    private String  type;

    private Boolean isRequired;

    private Boolean isShow;

    private Boolean isCustom;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public static PlayerPropertyVo getVo(FromTypeModel model) {
        if (model == null) {
            return null;
        }
        PlayerPropertyVo vo = new PlayerPropertyVo();
        vo.setIsRequired(model.getIsRequired());
        vo.setIsShow(model.getIsShow());
        vo.setName(model.getName());
        vo.setAttributeName(model.getAttribute());
        vo.setType(model.getType());
        vo.setIsCustom(model.getIsCustom());
        return vo;
    }

    public static Map<String, Object> getMap(SpAthletesEnrollModel athiete) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (athiete == null || athiete.getEnrollForm() == null
            || athiete.getEnrollForm().size() < 1) {
            return map;
        }
        List<PlayerPropertyVo> expand = new ArrayList<PlayerPropertyVo>();
        for (FromTypeModel model : athiete.getEnrollForm()) {
            PlayerPropertyVo vo = getVo(model);
            if (vo == null) {
                continue;
            }
            if (vo.getIsCustom() == null || !vo.getIsCustom()) {
                map.put(model.getAttribute(), getVo(model));
            } else {
                expand.add(getVo(model));
            }
        }
        map.put("expands", expand);
        return map;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(Boolean isCustom) {
        this.isCustom = isCustom;
    }

}
