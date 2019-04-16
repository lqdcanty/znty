/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.intergration.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.efida.sport.admin.facade.SpMatchFacade;
import com.efida.sport.admin.facade.model.SpMatchAllInfoModel;
import com.efida.sport.admin.facade.model.SpMatchGroupAndItemInfoModel;
import com.efida.sport.admin.facade.model.SpMatchGroupModel;
import com.efida.sport.admin.facade.model.SpMatchInfoModel;
import com.efida.sport.admin.facade.model.SpMatchModel;
import com.efida.sport.admin.facade.model.SpPlayingAreaModel;
import com.efida.sport.admin.facade.model.open.MatchCollectionModel;
import com.efida.sport.admin.facade.model.open.MatchDetailModel;
import com.efida.sport.admin.facade.model.open.MatchGroupItemModel;
import com.efida.sport.admin.facade.model.page.SportsAdminPageResult;
import com.efida.sport.admin.facade.result.SportAdminResult;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.dubbo.intergration.SpMatchFacadeClient;

/**
 * 
 * @author zoutao
 * @version $Id: SpMatchFacadeClientImpl.java, v 0.1 2018年5月23日 下午6:24:35 zoutao Exp $
 */
@Service
public class SpMatchFacadeClientImpl implements SpMatchFacadeClient {
    @Reference
    private SpMatchFacade spMatchFacade;
    private static Logger log = LoggerFactory.getLogger(SpMatchFacadeClientImpl.class);

    @Override
    public List<SpMatchModel> getMatchs(String gameCode) {
        SportAdminResult<List<SpMatchModel>> rs = spMatchFacade.getSpMatchsWithGameCode(gameCode);
        if (log.isDebugEnabled()) {
            log.debug("查询 赛事列表,项目分类编号: " + gameCode + "  ,返回结果为:" + JSON.toJSONString(rs));
        }
        if (!rs.isSuccess()) {
            throw new BusinessException(rs.getErrorMsg());
        }
        return rs.getResultObj();
    }

    @Override
    public List<SpPlayingAreaModel> getSpPlayingAreas(String matchCode) {
        SportAdminResult<List<SpPlayingAreaModel>> rs = spMatchFacade.getSpPlayingAreas(matchCode);
        if (log.isDebugEnabled()) {
            log.debug("查询赛场列表: " + matchCode + "  ,返回结果为:" + JSON.toJSONString(rs));
        }
        if (!rs.isSuccess()) {
            throw new BusinessException(rs.getErrorMsg());
        }
        return rs.getResultObj();
    }

    @Override
    public List<SpMatchGroupAndItemInfoModel> getMatchTypeAndGroupInfo(String fileidCode) {
        SportAdminResult<List<SpMatchGroupAndItemInfoModel>> rs = spMatchFacade
            .getMatchTypeAndGroupInfo(fileidCode);
        if (log.isDebugEnabled()) {
            log.debug("查询比赛组和比赛项数据 ,赛场编号: " + fileidCode + "  ,返回结果为:" + JSON.toJSONString(rs));
        }
        if (!rs.isSuccess()) {
            throw new BusinessException(rs.getErrorMsg());
        }
        return rs.getResultObj();
    }

    @Override
    public SpMatchInfoModel getMatchDetail(String code) {
        SportAdminResult<SpMatchInfoModel> rs = spMatchFacade.getEnableSpMathchInfo(code);
        if (log.isDebugEnabled()) {
            log.debug("查询赛事详情 ,赛事Code: " + code + "  ,返回结果为:" + JSON.toJSONString(rs));
        }
        if (!rs.isSuccess()) {
            throw new BusinessException(rs.getErrorMsg());
        }
        return rs.getResultObj() == null ? new SpMatchInfoModel() : rs.getResultObj();

    }

    @Override
    public SpMatchAllInfoModel getItemDetail(String fileidCode, String itemCode) {
        //满足现状的情况，兼容老数据
        if (StringUtils.isBlank(fileidCode) || StringUtils.isBlank(itemCode)) {
            return null;
        }
        long startTime = System.currentTimeMillis(); //获取开始时间
        SportAdminResult<SpMatchAllInfoModel> rs = spMatchFacade.getSpMatchAllInfoModel(fileidCode,
            itemCode);
        //        if (log.isDebugEnabled()) {
        log.info("查询比赛项详细信息，fileidCode: " + fileidCode + " ,itemCode:" + itemCode + " ,返回结果为:"
                 + JSON.toJSONString(rs));
        //        }
        long endTime = System.currentTimeMillis(); //获取结束时间
        log.info("查询比赛项详细信息花费时间：{}", (endTime - startTime) / 1000 + "秒");
        if (!rs.isSuccess()) {
            throw new BusinessException(rs.getErrorMsg());
        }

        return rs.getResultObj();

    }

    @Override
    public List<MatchDetailModel> queryMatches(String matchCode, String matchStatus,
                                               Date beginTimeMin, Date beginTimeMax,
                                               Date endTimeMin, Date endTimeMax, int pageNumber,
                                               int pageSize) {
        SportAdminResult<List<MatchDetailModel>> rs = spMatchFacade.queryMatches(matchCode,
            matchStatus, beginTimeMin, beginTimeMax, endTimeMin, endTimeMax, pageNumber, pageSize);
        if (log.isDebugEnabled()) {
            log.debug(
                "查询 赛事信息, matchCode=" + matchCode + "&matchStatus=" + matchStatus + "&beginTimeMin="
                      + beginTimeMin + "&beginTimeMax=" + beginTimeMax + "&endTimeMin=" + endTimeMin
                      + "&endTimeMax=" + endTimeMax + "," + "  ,返回结果为:" + JSON.toJSONString(rs));

        }
        if (!rs.isSuccess()) {
            throw new BusinessException(rs.getErrorMsg());
        }
        return rs.getResultObj();
    }

    @Override
    public SportsAdminPageResult<SpMatchModel> searchMatchs(String keyword, int pageNumber,
                                                            int pageSize) {
        SportAdminResult<SportsAdminPageResult<SpMatchModel>> dr = spMatchFacade
            .getEnableSpMatchByKeyWord(keyword, pageNumber, pageSize);
        if (log.isDebugEnabled()) {
            log.debug("搜索赛事列表信息, keyword=" + keyword + "&pageNumber=" + pageNumber + "&pageSize="
                      + pageSize + "& ,返回结果为:" + JSON.toJSONString(dr));

        }
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public SportsAdminPageResult<SpMatchModel> getPageMatchs(String gameCode, int pageNumber,
                                                             int pageSize) {
        SportAdminResult<SportsAdminPageResult<SpMatchModel>> dr = spMatchFacade
            .getPageEnableSpMatchs(gameCode, pageNumber, pageSize);
        if (log.isDebugEnabled()) {
            log.debug("根据项目编号获取赛事列表, keyword=" + gameCode + "&pageNumber=" + pageNumber
                      + "&pageSize=" + pageSize + "& ,返回结果为:" + JSON.toJSONString(dr));
        }
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj();
    }

    @Override
    public SpMatchModel getEnableSpMatch(String matchCode) {

        SportAdminResult<SpMatchModel> dr = spMatchFacade.getEnableSpMatch(matchCode);
        if (log.isDebugEnabled()) {
            log.debug("赛事信息" + JSON.toJSONString(dr));
        }
        if (!dr.isSuccess()) {
            throw new BusinessException(dr.getErrorMsg());
        }
        return dr.getResultObj() == null ? new SpMatchInfoModel() : dr.getResultObj();
    }

    @Override
    public List<MatchGroupItemModel> getMatchGroupItemList(String matchCode) {
        SportAdminResult<List<MatchGroupItemModel>> rs = spMatchFacade
            .getSpMatchGroupItems(matchCode);
        if (log.isDebugEnabled()) {
            log.debug("查询 赛区-分组-项,返回结果为:" + JSON.toJSONString(rs));

        }
        if (!rs.isSuccess()) {
            throw new BusinessException(rs.getErrorMsg());
        }
        return rs.getResultObj();
    }

    @Override
    public SpMatchGroupModel getSpMatchGroupModel(String areaGroupCode) {
        SportAdminResult<SpMatchGroupModel> rs = spMatchFacade.getSpMatchGroupModel(areaGroupCode);
        if (log.isDebugEnabled()) {
            log.debug("查询 赛区-分组-项,返回结果为:" + JSON.toJSONString(rs));

        }
        if (!rs.isSuccess()) {
            throw new BusinessException(rs.getErrorMsg());
        }
        return rs.getResultObj();
    }

    @Override
    public List<MatchCollectionModel> getSpMatchCollections(String matchCode) {
        SportAdminResult<List<MatchCollectionModel>> rs = spMatchFacade
            .getSpMatchCollections(matchCode);
        if (log.isDebugEnabled()) {
            log.debug("赛事集,返回结果为:" + JSON.toJSONString(rs));

        }
        if (!rs.isSuccess()) {
            throw new BusinessException(rs.getErrorMsg());
        }
        log.info("赛事集,返回结果为:" + JSON.toJSONString(rs.getResultObj()));
        return rs.getResultObj();
    }

    @Override
    public List<SpPlayingAreaModel> getPlayingAreas(String matchCode) {
        SportAdminResult<List<SpPlayingAreaModel>> rs = spMatchFacade.getPlayingAreas(matchCode);
        if (log.isDebugEnabled()) {
            log.debug("分赛场,返回结果为:" + JSON.toJSONString(rs));
        }
        if (!rs.isSuccess()) {
            throw new BusinessException(rs.getErrorMsg());
        }
        return rs.getResultObj();
    }

    @Override
    public String getFieldAddress(String siteCode) {
        SportAdminResult<SpPlayingAreaModel> dr = spMatchFacade.getSpPlayingArea(siteCode);
        if (dr.isSuccess() && dr.getResultObj() != null) {
            return dr.getResultObj().getFieldAddress();
        }
        return "";
    }

}
