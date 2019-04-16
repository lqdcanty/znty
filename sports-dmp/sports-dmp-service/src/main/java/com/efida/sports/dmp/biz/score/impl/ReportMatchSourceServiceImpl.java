package com.efida.sports.dmp.biz.score.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efida.sport.admin.facade.model.SpMatchModel;
import com.efida.sports.dmp.biz.score.ReportMatchFinishService;
import com.efida.sports.dmp.biz.score.ReportMatchSourceService;
import com.efida.sports.dmp.dao.entity.ReportMatchSourceEntity;
import com.efida.sports.dmp.dao.mapper.ReportMatchSourceEntityMapper;
import com.efida.sports.dmp.service.dubbo.intergration.SpMatchFacadeClient;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.evake.auth.usermodel.PagingResult;

/**
 * 
 * 
 * @author zengbo
 * @version $Id: ReportMatchSourceServiceImpl.java, v 0.1 2018年8月30日 上午9:51:10 zengbo Exp $
 */
@Service("reportMatchSourceService")
public class ReportMatchSourceServiceImpl implements ReportMatchSourceService {

    @Autowired
    private ReportMatchSourceEntityMapper reportMatchSourceEntityMapper;

    @Autowired
    private ReportMatchFinishService      reportMatchFinishService;

    @Autowired
    private SpMatchFacadeClient           spMatchFacadeClient;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return reportMatchSourceEntityMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ReportMatchSourceEntity record) {
        return reportMatchSourceEntityMapper.insert(record);
    }

    @Override
    public int insertSelective(ReportMatchSourceEntity record) {
        return reportMatchSourceEntityMapper.insertSelective(record);
    }

    @Override
    public ReportMatchSourceEntity selectByPrimaryKey(Integer id) {
        return reportMatchSourceEntityMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ReportMatchSourceEntity record) {
        return reportMatchSourceEntityMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(ReportMatchSourceEntity record) {
        return reportMatchSourceEntityMapper.updateByPrimaryKey(record);
    }

    @Override
    public void saveReportMatchSourceList(List<ReportMatchSourceEntity> records) {
        if (records != null && records.size() > 0) {
            for (ReportMatchSourceEntity entity : records) {
                entity.setGmtModified(new Date());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("matchCode", entity.getMatchCode());
                map.put("applyTimeDay", entity.getApplyTimeDay());
                List<ReportMatchSourceEntity> result = reportMatchSourceEntityMapper
                    .queryMatchEnrollByCode(map);
                if (result != null && result.size() > 0) {
                    ReportMatchSourceEntity oldEntity = result.get(0);
                    if (entity.getOrganizerCount() == oldEntity.getOrganizerCount()
                        && entity.getOfficialCount() == oldEntity.getOfficialCount()) {
                        continue;
                    }
                    entity.setId(oldEntity.getId());
                    this.updateByPrimaryKeySelective(entity);
                    continue;
                }
                entity.setGmtCreate(new Date());
                this.insertSelective(entity);
            }
        }
    }

    @Override
    public PagingResult<ReportMatchSourceEntity> getPageSourceEntity(String likeParams,
                                                                     String inParams,
                                                                     int pageNumber, int pageSize) {

        if (StringUtils.isBlank(inParams) && StringUtils.isBlank(likeParams)) {
            return new PagingResult<ReportMatchSourceEntity>(
                new ArrayList<ReportMatchSourceEntity>(), 0, pageNumber, pageSize);
        }
        PageHelper.startPage(pageNumber, pageSize);
        Map<String, Object> queryParams = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(likeParams)) {
            queryParams.put("likeParams", likeParams);
        }
        if (StringUtils.isNotBlank(inParams)) {
            queryParams.put("inParams", strparamsConvertList(inParams));
        }
        //        List<ReportMatchSourceEntity> matchs = reportMatchSourceEntityMapper
        //            .countMatchEnrollByDetailParams(queryParams);

        List<Map<String, Object>> results = reportMatchSourceEntityMapper
            .queryCountMatchEnrollByParams(queryParams);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(results);
        List<ReportMatchSourceEntity> matchSource = mapToList(pageInfo.getList());

        return new PagingResult<ReportMatchSourceEntity>(matchSource, pageInfo.getTotal(),
            pageNumber, pageSize);
    }

    public List<ReportMatchSourceEntity> mapToList(List<Map<String, Object>> list) {
        List<ReportMatchSourceEntity> result = new ArrayList<ReportMatchSourceEntity>();
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map : list) {
                ReportMatchSourceEntity entity = new ReportMatchSourceEntity();
                String matchCode = (String) map.get("matchCode");
                String matchName = (String) map.get("matchName");
                String unitCode = (String) map.get("unitCode");
                String unitName = (String) map.get("unitName");
                BigDecimal enrollTotalCount = (BigDecimal) map.get("enrollTotalCount");
                BigDecimal organizerCount = (BigDecimal) map.get("organizerCount");
                BigDecimal officialCount = (BigDecimal) map.get("officialCount");
                entity.setMatchCode(matchCode);
                entity.setMatchName(matchName);
                entity.setUnitCode(unitCode);
                entity.setUnitName(unitName);
                entity.setEnrollTotalCount(enrollTotalCount.intValue());
                entity.setOrganizerCount(organizerCount.intValue());
                entity.setOfficialCount(officialCount.intValue());
                result.add(entity);
            }
        }
        return result;
    }

    public List<String> strparamsConvertList(String strs) {
        List<String> params = new ArrayList<String>();
        if (StringUtils.isBlank(strs)) {
            return params;
        }
        if (strs.indexOf(",") > 0) {
            String[] param = strs.split(",");
            for (int i = 0; i < param.length; i++) {
                params.add(param[i]);
            }
        } else {
            params.add(strs);
        }

        return params;
    }

    @Override
    public List<Map<String, Object>> officialEnrollCount(String likeParams, String inParams) {
        if (StringUtils.isBlank(inParams) && StringUtils.isBlank(likeParams)) {
            return new ArrayList<Map<String, Object>>();
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(likeParams)) {
            map.put("likeParams", likeParams);
        }
        if (StringUtils.isNotBlank(inParams)) {
            List<String> params = strparamsConvertList(inParams);
            map.put("inParams", params);
        }
        List<Map<String, Object>> results = reportMatchSourceEntityMapper.officialEnrollCount(map);
        if (results != null && results.size() > 0) {
            for (Map<String, Object> result : results) {
                if (result != null && result.containsKey("matchCode")) {
                    String matchCode = (String) result.get("matchCode");
                    String matchName = (String) result.get("matchName");
                    if (StringUtils.isBlank(matchName)) {
                        matchName = getMatchName(matchCode);
                    }
                    int finshCount = reportMatchFinishService.officialMatchFinishCount(matchCode);
                    result.put("finishCount", finshCount);
                    result.put("matchName", matchName);
                    list.add(result);
                } else {
                    result.put("finishCount", 0);
                }
            }
        }
        return results;
    }

    @Override
    public PagingResult<ReportMatchSourceEntity> officialEnrollByParams(String likeParams,
                                                                        String inParams,
                                                                        int pageNumber,
                                                                        int pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        Map<String, Object> queryParams = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(likeParams)) {
            queryParams.put("likeParams", likeParams);
        }
        if (StringUtils.isNotBlank(inParams)) {
            queryParams.put("inParams", strparamsConvertList(inParams));
        }
        // #{start} , #{limit}

        List<ReportMatchSourceEntity> matchs = reportMatchSourceEntityMapper
            .officialEnrollByParams(queryParams);
        PageInfo<ReportMatchSourceEntity> pageInfo = new PageInfo<ReportMatchSourceEntity>(matchs);
        return new PagingResult<ReportMatchSourceEntity>(pageInfo.getList(), pageInfo.getTotal(),
            pageNumber, pageSize);
    }

    @Override
    public List<Map<String, Object>> queryMatchNameByParams(String matchName) {
        List<Map<String, Object>> names = new ArrayList<Map<String, Object>>();
        Map<String, Object> queryParams = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(matchName)) {
            queryParams.put("matchName", matchName);
        }

        List<String> matchCodes = new ArrayList<String>();
        List<Map<String, Object>> results = reportMatchSourceEntityMapper
            .queryMatchNameByParams(queryParams);
        if (results != null && results.size() > 0) {
            for (Map<String, Object> result : results) {
                String matchCode = (String) result.get("matchCode");
                if (StringUtils.isNotBlank(matchCode)) {
                    matchCodes.add(matchCode);
                }
            }
        }
        List<SpMatchModel> models = spMatchFacadeClient.getMatchsByMatchCodes(matchCodes);
        if (models != null && models.size() > 0) {
            for (SpMatchModel model : models) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("matchName", model.getMatchName());
                map.put("matchCode", model.getMatchCode());
                names.add(map);
            }
        }
        return names;
    }

    @Override
    public List<SpMatchModel> queryMatchName(String matchName) {
        List<SpMatchModel> list = new ArrayList<SpMatchModel>();
        Map<String, Object> queryParams = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(matchName)) {
            queryParams.put("matchName", matchName);
        }

        List<String> matchCodes = new ArrayList<String>();
        List<Map<String, Object>> results = reportMatchSourceEntityMapper
            .queryMatchNameByParams(queryParams);
        if (results != null && results.size() > 0) {
            for (Map<String, Object> result : results) {
                String matchCode = (String) result.get("matchCode");
                String matchNames = (String) result.get("matchName");
                if (StringUtils.isNotBlank(matchCode) && StringUtils.isNotBlank(matchNames)) {
                    SpMatchModel model = new SpMatchModel();
                    model.setMatchCode(matchCode);
                    model.setMatchName(matchNames);
                    list.add(model);
                }
            }
        }
        return list;
        //        return spMatchFacadeClient.getMatchsByMatchCodes(matchCodes);
    }

    @Override
    public List<String> matchEnrollTop5(String type) {
        List<Map<String, Object>> top5 = null;
        if ("1".equals(type)) {//官方
            //            Map<String, Object> map = new HashMap<String, Object>();
            //            map.put("likeParams", null);
            //            map.put("inParams", null);
            //            map.put("default", "default");
            //            List<Map<String, Object>> results = reportMatchSourceEntityMapper
            //                .officialEnrollCount(map);
            //            return getMatchCode(results);
            top5 = reportMatchSourceEntityMapper.officialEnrollTop5();
        } else {

            top5 = reportMatchSourceEntityMapper.matchEnrollTop5();
        }
        return getMatchCode(top5);
    }

    public List<String> getMatchCode(List<Map<String, Object>> results) {
        List<String> array = new ArrayList<String>();
        if (results != null && results.size() > 0) {
            for (Map<String, Object> map : results) {
                String matchCode = (String) map.get("matchCode");
                array.add(matchCode);
            }
        }
        return array;
    }

    @Override
    public List<ReportMatchSourceEntity> getPageSourceEntityByExport(String likeParams,
                                                                     String inParams) {

        if (StringUtils.isBlank(likeParams) && StringUtils.isBlank(inParams)) {
            return new ArrayList<ReportMatchSourceEntity>();
        }
        Map<String, Object> queryParams = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(likeParams)) {
            queryParams.put("likeParams", likeParams);
        }
        if (StringUtils.isNotBlank(inParams)) {
            queryParams.put("inParams", strparamsConvertList(inParams));
        }

        List<Map<String, Object>> results = reportMatchSourceEntityMapper
            .queryCountMatchEnrollByParams(queryParams);
        List<ReportMatchSourceEntity> matchSource = mapToList(results);
        return matchSource;
    }

    @Override
    public List<ReportMatchSourceEntity> officialEnrollByEceport(String likeParams,
                                                                 String inParams) {
        if (StringUtils.isBlank(inParams) && StringUtils.isBlank(likeParams)) {
            return new ArrayList<ReportMatchSourceEntity>();
        }
        List<ReportMatchSourceEntity> results = new ArrayList<ReportMatchSourceEntity>();
        Map<String, Object> queryParams = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(likeParams)) {
            queryParams.put("likeParams", likeParams);
        }
        if (StringUtils.isNotBlank(inParams)) {
            queryParams.put("inParams", strparamsConvertList(inParams));
        }

        List<Map<String, Object>> matchs = reportMatchSourceEntityMapper
            .officialEnrollByDetailParams(queryParams);

        for (Map<String, Object> map : matchs) {
            ReportMatchSourceEntity entity = new ReportMatchSourceEntity();
            String matchName = (String) map.get("matchName");
            String matchCode = (String) map.get("matchCode");
            BigDecimal enrollTotalCount = (BigDecimal) map.get("officialCount");
            if (StringUtils.isBlank(matchName)) {
                matchName = getMatchName(matchCode);
            }
            entity.setMatchName(matchName);
            entity.setMatchCode(matchCode);
            entity.setOfficialCount(enrollTotalCount.intValue());
            results.add(entity);
        }
        return results;
    }

    @Override
    public PagingResult<ReportMatchSourceEntity> officialEnrollByDetailParams(String likeParams,
                                                                              String inParams,
                                                                              int pageNumber,
                                                                              int pageSize) {
        if (StringUtils.isBlank(inParams) && StringUtils.isBlank(likeParams)) {
            return new PagingResult<ReportMatchSourceEntity>(
                new ArrayList<ReportMatchSourceEntity>(), 0, pageNumber, pageSize);
        }
        PageHelper.startPage(pageNumber, pageSize);
        Map<String, Object> queryParams = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(likeParams)) {
            queryParams.put("likeParams", likeParams);
        }
        if (StringUtils.isNotBlank(inParams)) {
            queryParams.put("inParams", strparamsConvertList(inParams));
        }
        // #{start} , #{limit}

        List<Map<String, Object>> matchs = reportMatchSourceEntityMapper
            .officialEnrollByDetailParams(queryParams);

        //        List<ReportMatchSourceEntity> matchs = reportMatchSourceEntityMapper
        //            .officialEnrollByParams(queryParams);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(matchs);

        List<ReportMatchSourceEntity> results = new ArrayList<ReportMatchSourceEntity>();
        for (Map<String, Object> map : pageInfo.getList()) {
            ReportMatchSourceEntity entity = new ReportMatchSourceEntity();
            String matchName = (String) map.get("matchName");
            String matchCode = (String) map.get("matchCode");
            BigDecimal enrollTotalCount = (BigDecimal) map.get("officialCount");
            entity.setMatchName(matchName);
            entity.setMatchCode(matchCode);
            entity.setOfficialCount(enrollTotalCount.intValue());
            results.add(entity);
        }
        return new PagingResult<ReportMatchSourceEntity>(results, pageInfo.getTotal(), pageNumber,
            pageSize);
    }

    public String getMatchName(String matchCode) {
        Map<String, Object> queryParams = new HashMap<String, Object>();
        List<Map<String, Object>> results = reportMatchSourceEntityMapper
            .queryMatchNameByParams(queryParams);
        if (results != null && results.size() > 0) {
            for (Map<String, Object> result : results) {
                String code = (String) result.get("matchCode");
                if (matchCode.equals(code)) {
                    return code;
                }
            }
        }
        return "";
    }

}
