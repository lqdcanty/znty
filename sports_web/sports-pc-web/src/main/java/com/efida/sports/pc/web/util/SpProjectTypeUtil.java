package com.efida.sports.pc.web.util;

import java.util.List;

import com.efida.sport.admin.facade.model.SpProjectTypeModel;

/**
 * 
 * 
 * @author zengbo
 * @version $Id: SpProjectTypeUtil.java, v 0.1 2018年8月8日 下午5:44:33 zengbo Exp $
 */
public class SpProjectTypeUtil {

    /**
     * 根据gamecode获取游戏名称
     * 
     * @param list
     * @param gameCode
     * @return
     */
    public static String getProjectNameByType(List<SpProjectTypeModel> list, String gameCode) {
        String name = "";
        if (list != null && list.size() > 0) {
            for (SpProjectTypeModel model : list) {
                if (gameCode.equals(model.getGameCode())) {
                    return model.getGameName();
                }
            }
        }
        return name;
    }

}
