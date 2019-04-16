/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.efida.sport.admin.facade.model.MatchChapterModel;

/**
 * 
 * @author zoutao
 * @version $Id: EquipmentsVo.java, v 0.1 2018年5月29日 下午8:03:27 zoutao Exp $
 */
public class CompositeVo {

    /**
     * 名称
     */
    private String name;

    /**
     * 附件地址
     */
    private String attaUrl;

    /**
     * 跳转地址
     * 
     */
    private String skipUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttaUrl() {
        return attaUrl;
    }

    public void setAttaUrl(String attaUrl) {
        this.attaUrl = attaUrl;
    }

    public String getSkipUrl() {
        return skipUrl;
    }

    public void setSkipUrl(String skipUrl) {
        this.skipUrl = skipUrl;
    }

    public static List<CompositeVo> geEquipmentsVos(List<MatchChapterModel> models) {
        List<CompositeVo> vos = new ArrayList<CompositeVo>();
        if (models == null || models.size() < 1) {
            return vos;
        }
        for (MatchChapterModel model : models) {
            if (model != null) {
                CompositeVo e = new CompositeVo();
                e.setName(model.getDesc());
                e.setAttaUrl(model.getPic());
                vos.add(e);
            }
        }
        return vos;
    }

    public static List<CompositeVo> geCompositeVos(List<MatchChapterModel> matchOters) {
        List<CompositeVo> vos = new ArrayList<CompositeVo>();
        if (matchOters == null || matchOters.size() < 1) {
            return vos;
        }
        for (MatchChapterModel model : matchOters) {
            if (model != null) {
                CompositeVo e = new CompositeVo();
                e.setName(model.getDesc());
                e.setAttaUrl(
                    StringUtils.isNotBlank(model.getPic()) ? model.getPic() : model.getVideo());
                vos.add(e);
            }
        }
        return vos;
    }

}
