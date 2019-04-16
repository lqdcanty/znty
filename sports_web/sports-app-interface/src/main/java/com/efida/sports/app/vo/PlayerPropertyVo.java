/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.vo;

import java.util.ArrayList;
import java.util.List;

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
    private String            attributeName;
    /**
     * 中文名称
     */
    private String            cnname;
    /**
     * 类型
     */
    private String            type;
    /**
     * 是否必填
     */
    private Boolean           isRequired;
    /**
     * 是否展示
     */
    private Boolean           isShow;
    /**
     *对应的参数名称
     */
    private List<ParameterVo> params;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ParameterVo> getParams() {
        return params;
    }

    public void setParams(List<ParameterVo> params) {
        this.params = params;
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
        if (model.getAttribute().equals("playerPhone")) {
            vo.setIsRequired(true);
            vo.setIsShow(true);
        }
        if (model.getAttribute().equals("playerName")) {
            vo.setIsRequired(true);
            vo.setIsShow(true);
        }
        if (model.getAttribute().equals("sex")) {
            vo.setIsRequired(true);
            vo.setIsShow(true);
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
            if (model.getAttribute().equals("playerCerType") ) {
                PlayerPropertyVo playerCerNum = new PlayerPropertyVo();
                playerCerNum.setAttributeName("playerCerNo");
                playerCerNum.setCnname("证件号码");
                if(model.getIsRequired()){
                    playerCerNum.setIsRequired(true);
                    playerCerNum.setIsShow(true);
                }else {
                    playerCerNum.setIsRequired(false);
                    playerCerNum.setIsShow(false);
                }
                list.add(playerCerNum);
            }
        }
        return list;
    }

}
