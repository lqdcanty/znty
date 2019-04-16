/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.efida.sport.admin.facade.enums.MatchChapterEums;
import com.efida.sport.admin.facade.model.MatchChapterModel;
import com.efida.sport.admin.facade.model.SpMatchOtherModel;

/**
 * 
 * @author zoutao
 * @version $Id: ChapterVo.java, v 0.1 2018年5月29日 下午8:02:02 zoutao Exp $
 */
public class ChapterVo {

    private String             type;
    private String             name;
    private String             content;

    private List<EquipmentsVo> equipmentsVos;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<EquipmentsVo> getEquipmentsVos() {
        return equipmentsVos;
    }

    public void setEquipmentsVos(List<EquipmentsVo> equipmentsVos) {
        this.equipmentsVos = equipmentsVos;
    }

    public static List<ChapterVo> getVos(List<SpMatchOtherModel> models) {

        List<ChapterVo> vos = new ArrayList<ChapterVo>();
        if (models == null || models.size() < 1) {
            return vos;
        }
        for (SpMatchOtherModel spMatchOtherModel : models) {
            ChapterVo vo = getVo(spMatchOtherModel);
            if (vo != null) {
                vos.add(vo);
            }
        }
        return vos;
    }

    public static ChapterVo getVo(SpMatchOtherModel model) {
        if (model == null) {
            return null;
        }
        List<MatchChapterModel> matchOters = model.getMatchOters();
        ChapterVo vo = new ChapterVo();
        String mdoelType = model.getType();
        vo.setName(model.getChapterName());
        vo.setType(mdoelType);

        if (MatchChapterEums.DESC.getCode().equals(mdoelType)) {
            //如果集合为空为脏数据
            if (matchOters == null || matchOters.size() < 1
                || StringUtils.isEmpty(matchOters.get(0).getRichText())) {
                return null;
            }
            vo.setContent(matchOters.get(0).getRichText());
            return vo;
        }

        List<EquipmentsVo> equipmentsVos = EquipmentsVo.geEquipmentsVos(matchOters);
        vo.setEquipmentsVos(equipmentsVos);
        return vo;
    }

}
