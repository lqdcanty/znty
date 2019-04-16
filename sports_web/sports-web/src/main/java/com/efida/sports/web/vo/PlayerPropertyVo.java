/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.vo;

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
    private String  cnname;
    /**
     * 类型
     */
    private String  type;
    /**
     * 是否必填
     */
    private Boolean isRequired;
    /**
     * 是否展示
     */
    private Boolean isShow;

    private Boolean isCustom = false;

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

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getCnname() {
        return cnname;
    }

    public void setCnname(String cnname) {
        this.cnname = cnname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean isCustom) {
        this.isCustom = isCustom;
    }

    public static PlayerPropertyVo getVo(FromTypeModel model) {
        if (model == null) {
            return null;
        }
        PlayerPropertyVo vo = new PlayerPropertyVo();
        vo.setIsRequired(model.getIsRequired());
        vo.setIsShow(model.getIsRequired() ? true : model.getIsShow());
        vo.setCnname(model.getName());
        vo.setAttributeName(model.getAttribute());
        vo.setType(model.getType());
        if (model.getIsCustom() != null) {
            vo.setCustom(model.getIsCustom());
        }
        return vo;
    }

    public static List<PlayerPropertyVo> getVos(SpAthletesEnrollModel athiete) {
        List<PlayerPropertyVo> list = new ArrayList<PlayerPropertyVo>();
        if (athiete == null || athiete.getEnrollForm() == null
            || athiete.getEnrollForm().size() < 1) {
            return list;
        }
        for (FromTypeModel model : athiete.getEnrollForm()) {
            //如果属性包含证件编号。跳过，为避免后台配置了证件类型为必填项目，证件号码不是必填项
            if (model.getAttribute().equals("playerCerNo")) {
                continue;
            }
            PlayerPropertyVo vo = getVo(model);
            if (vo != null) {
                list.add(vo);
            }
            //如果证件号码为必填项  则任务证件号码也为必填项
            if (model.getAttribute().equals("playerCerType") && model.getIsRequired()) {
                PlayerPropertyVo playerCerNum = new PlayerPropertyVo();
                playerCerNum.setAttributeName("playerCerNo");
                playerCerNum.setCnname("证件号码");
                playerCerNum.setIsRequired(true);
                playerCerNum.setIsShow(true);
                list.add(playerCerNum);
            }
        }
        return list;
    }

    public static Map<String, PlayerPropertyVo> getMap(SpAthletesEnrollModel athiete) {
        Map<String, PlayerPropertyVo> map = new HashMap<String, PlayerPropertyVo>();
        if (athiete == null || athiete.getEnrollForm() == null
            || athiete.getEnrollForm().size() < 1) {
            return map;
        }
        for (FromTypeModel model : athiete.getEnrollForm()) {
            PlayerPropertyVo vo = getVo(model);
            if (vo != null && vo.getIsShow()) {
                map.put(vo.getAttributeName(), getVo(model));
            }
        }
        return map;
    }

}
