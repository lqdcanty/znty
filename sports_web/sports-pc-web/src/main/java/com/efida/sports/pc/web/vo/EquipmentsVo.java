/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.pc.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.efida.sport.admin.facade.model.MatchChapterModel;

/**
 * 
 * @author zoutao
 * @version $Id: EquipmentsVo.java, v 0.1 2018年5月29日 下午8:03:27 zoutao Exp $
 */
public class EquipmentsVo {

    /**
     * 名称
     */
    private String name;

    /**
     * url
     */
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static List<EquipmentsVo> geEquipmentsVos(List<MatchChapterModel> models) {
        List<EquipmentsVo> vos = new ArrayList<EquipmentsVo>();
        if (models == null || models.size() < 1) {
            return vos;
        }
        for (MatchChapterModel model : models) {
            if (model != null) {
                EquipmentsVo e = new EquipmentsVo();
                e.setName(model.getDesc());
                e.setUrl(model.getPic());
                vos.add(e);
            }
        }
        return vos;
    }

}
