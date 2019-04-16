/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efida.sport.admin.facade.model.FromTypeModel;
import com.efida.sport.admin.facade.model.SpAthletesEnrollModel;
import com.efida.sports.entity.Player;
import com.efida.sports.enums.IdCardTypeEnum;
import com.efida.sports.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author zoutao
 * @version $Id: PlayerVO.java, v 0.1 2018年6月13日 下午7:46:21 zoutao Exp $
 */
public class PlayerVo implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 值
     */
    private String            val;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<ParameterVo> getParams() {
        return params;
    }

    public void setParams(List<ParameterVo> params) {
        this.params = params;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public static List<PlayerVo> getVos(Player player,
                                        SpAthletesEnrollModel athiete) throws Exception {
        List<PlayerVo> list = new ArrayList<PlayerVo>();
        Map<String, String> playerMap = objectToMap(player);
        if (athiete == null || athiete.getEnrollForm() == null
            || athiete.getEnrollForm().size() < 1) {
            return list;
        }

        List<FromTypeModel> enrollForm = athiete.getEnrollForm();
        for (FromTypeModel model : enrollForm) {
            //如果属性包含证件编号。跳过，为避免后台配置了证件类型为必填项目，证件号码不是必填项
            if (model.getAttribute().equals("playerCerNo")) {
                continue;
            }
            PlayerVo vo = new PlayerVo();
            PlayerPropertyVo propertyVo = PlayerPropertyVo.getVo(model);
            vo.setAttributeName(propertyVo.getAttributeName());
            vo.setCnname(propertyVo.getCnname());
            vo.setIsRequired(propertyVo.getIsRequired());
            vo.setIsShow(propertyVo.getIsRequired() ? true : propertyVo.getIsShow());
            String key = model.getAttribute();
            if (playerMap.containsKey(key)) {
                String value = playerMap.get(key);
                if ("playerBirth".equals(key) && StringUtils.isNotBlank(value)) {
                    value = DateUtil.formatDay(player.getPlayerBirth());
                }
                vo.setVal(value);
            } else {
                if (player != null && StringUtils.isNotBlank(player.getExtPro())) {
                    String extPro = player.getExtPro();
                    JSONObject parseObject = JSON.parseObject(extPro);
                    if (parseObject.containsKey(key)) {
                        vo.setVal(parseObject.getString(key));
                    }
                }
            }
            list.add(vo);
            //如果证件号码为必填项  则任务证件号码也为必填项
            if (model.getAttribute().equals("playerCerType") && model.getIsRequired()) {
                PlayerVo playerCerNum = new PlayerVo();
                playerCerNum.setAttributeName("playerCerNo");
                playerCerNum.setCnname("证件号码");
                playerCerNum.setIsRequired(true);
                playerCerNum.setIsShow(true);
                if (player != null) {
                    playerCerNum.setVal(player.getPlayerCerNo());
                }
                list.add(playerCerNum);
            }

        }

        if (player != null && StringUtils.isNotBlank(player.getAttOne())) {
            String playerCerType = player.getPlayerCerType();
            PlayerVo vo = new PlayerVo();
            vo.setAttributeName("attOne");
            if ("1".equals(playerCerType)) {
                vo.setCnname("身份证正面");
            } else {
                vo.setCnname(IdCardTypeEnum.getDescByCode(playerCerType));
            }
            vo.setIsRequired(false);
            vo.setIsShow(true);
            vo.setVal(player.getAttOne());
            list.add(vo);
        }
        if (player != null && StringUtils.isNotBlank(player.getAttTwo())) {
            String playerCerType = player.getPlayerCerType();
            PlayerVo vo = new PlayerVo();
            vo.setAttributeName("attTwo");
            if ("1".equals(playerCerType)) {
                vo.setCnname("身份证反面");
            } else {
                vo.setCnname(IdCardTypeEnum.getDescByCode(playerCerType));
            }
            vo.setIsRequired(false);
            vo.setIsShow(true);
            vo.setVal(player.getAttTwo());
            list.add(vo);
        }

        if (player != null && StringUtils.isNotBlank(player.getAttUrl())) {
            PlayerVo vo = new PlayerVo();
            vo.setAttributeName("attUrl");
            vo.setCnname("健康证");
            vo.setIsRequired(false);
            vo.setIsShow(true);
            vo.setVal(player.getAttUrl());
            list.add(vo);
        }

        return list;
    }

    /**
     * 将报名信息转换成map
     * 
     * @param athiete
     * @return
     */
    private static Map<String, PlayerPropertyVo> getFromTypeMap(SpAthletesEnrollModel athiete) {
        HashMap<String, PlayerPropertyVo> map = new HashMap<String, PlayerPropertyVo>();
        if (athiete == null || athiete.getEnrollForm() == null
            || athiete.getEnrollForm().size() < 1) {
            return map;
        }
        List<FromTypeModel> enrollForm = athiete.getEnrollForm();
        for (FromTypeModel model : enrollForm) {
            map.put(model.getAttribute(), PlayerPropertyVo.getVo(model));
        }
        return map;
    }

    public static Map<String, String> objectToMap(Object obj) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        if (obj == null) {
            return map;
        }
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter != null && getter.invoke(obj) != null ? getter.invoke(obj) : "";
            map.put(key, String.valueOf(value));
        }
        return map;
    }

}
