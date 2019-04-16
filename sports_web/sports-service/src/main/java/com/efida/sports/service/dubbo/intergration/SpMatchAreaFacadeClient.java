/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.intergration;

import java.math.BigDecimal;
import java.util.List;

import com.efida.sport.admin.facade.model.MatchAreaModel;
import com.efida.sport.admin.facade.model.SpPlayingAreaModel;
import com.efida.sport.admin.facade.model.page.SportsAdminPageResult;

/**
 * 
 * @author zoutao
 * @version $Id: SpMatchAreaFacadeClient.java, v 0.1 2018年6月29日 下午5:12:27 zoutao Exp $
 */
public interface SpMatchAreaFacadeClient {
    /**
     * 获取某个赛事的地区分布信息   
     * 
     * @param matchCode  赛事编号
     * @param areaType      @see AreaTypeEums  
     * @param parentArea  父节点名称  查询省   传null
     * @return
     */
    List<MatchAreaModel> getMatchAreas(String matchCode, String areaType, String parentArea);

    /**
     * 
     * 获取赛事列表
     * @param matchCode 赛事编号
     * @param areaType    @see AreaTypeEums 
     * @param parentArea   名称
     * @param curLongitude  经度
     * @param curLatitude   纬度
     * @param page       页数
     * @param pageSize   每页大小
     * @return
     */
    SportsAdminPageResult<SpPlayingAreaModel> getMatchSites(String matchCode, String areaType,
                                                            String parentArea,
                                                            BigDecimal curLongitude,
                                                            BigDecimal curLatitude, int page,
                                                            int pageSize);

}
