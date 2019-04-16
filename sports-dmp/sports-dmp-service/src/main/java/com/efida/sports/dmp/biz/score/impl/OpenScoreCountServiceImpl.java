package com.efida.sports.dmp.biz.score.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efida.sports.dmp.biz.score.OpenScoreCountService;
import com.efida.sports.dmp.dao.entity.OpenUnitEntity;
import com.efida.sports.dmp.dao.entity.ReportMatchFinishEntity;
import com.efida.sports.dmp.dao.entity.ReportUnitFinishEntity;
import com.efida.sports.dmp.dao.mapper.OpenScoreEntityMapper;
import com.efida.sports.dmp.dao.mapper.OpenUnitEntityMapper;
import com.efida.sports.dmp.utils.DateUtil;

/**
 * 
 * 
 * @author zengbo
 * @version $Id: OpenScoreCountServiceImpl.java, v 0.1 2018年8月29日 下午9:00:19 zengbo Exp $
 */
@Service("openScoreCountService")
public class OpenScoreCountServiceImpl implements OpenScoreCountService {

    //接口数据
    private static final String   OFFICIAL = "official";

    @Autowired
    private OpenScoreEntityMapper openScoreEntityMapper;

    @Autowired
    private OpenUnitEntityMapper  OpenUnitEntityMapper;

    @Override
    public List<String> queryUnitScoreDate() {
        return openScoreEntityMapper.queryUnitScoreDate();
    }

    @Override
    public List<ReportUnitFinishEntity> queryUnitScoreCount(String date) {
        List<ReportUnitFinishEntity> list = new ArrayList<ReportUnitFinishEntity>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("partTime", date);
        List<Map<String, Object>> scoreMaps = openScoreEntityMapper.queryUnitScoreByCount(map);
        List<OpenUnitEntity> units = OpenUnitEntityMapper.queryLikeUnitName(null, null);
        if (scoreMaps != null && scoreMaps.size() > 0) {
            for (Map<String, Object> scoreMap : scoreMaps) {
                ReportUnitFinishEntity entity = new ReportUnitFinishEntity();
                int finishCont = 0;
                String unitCode = StringUtils.defaultString((String) scoreMap.get("unit_code"), "");
                String unitName = OpenApplyinfoCountServiceImpl.getUnitName(units, unitCode);
                if (scoreMap.containsKey("counts") && scoreMap.get("counts") != null) {
                    Long conut = (Long) scoreMap.get("counts");
                    finishCont = conut.intValue();
                }
                Date partTime = (Date) scoreMap.get("part_time");
                String day = new SimpleDateFormat("yyyy-MM-dd").format(partTime);
                entity.setUnitCode(unitCode);
                entity.setUnitName(unitName);
                entity.setFinishCont(finishCont);
                entity.setPartTime(partTime);
                entity.setPartTimeDay(DateUtil.strToDate(day));
                list.add(entity);
            }
        }
        return list;
    }

    @Override
    public List<ReportMatchFinishEntity> queryMatchScoreCount(String date) {
        List<ReportMatchFinishEntity> results = new ArrayList<ReportMatchFinishEntity>();
        Map<String, Object> organizerParams = new HashMap<String, Object>();
        organizerParams.put("partTime", date);
        //        organizerParams.put("enrollSource", 2);
        //        Map<String, Object> officialParams = new HashMap<String, Object>();
        //        officialParams.put("partTime", date);
        //        officialParams.put("enrollSource", 1);
        List<Map<String, Object>> organizermaps = openScoreEntityMapper
            .queryMatchScoreByCount(organizerParams);
        //        List<Map<String, Object>> officialmaps = openScoreEntityMapper
        //            .queryMatchScoreByCount(officialParams);
        List<OpenUnitEntity> units = OpenUnitEntityMapper.queryLikeUnitName(null, null);
        List<ReportMatchFinishEntity> organizers = this.getOrganizerCount(organizermaps, units,
            null);
        List<ReportMatchFinishEntity> officials = this.getOrganizerCount(organizermaps, units,
            OFFICIAL);
        for (ReportMatchFinishEntity organizer : organizers) {
            if (organizer == null) {
                continue;
            }
            organizer.setFinishTotalCount(organizer.getOrganizerCount());
            ReportMatchFinishEntity official = this
                .getReportMatchSourceEntity(organizer.getMatchCode(), officials);
            if (official != null) {
                organizer.setFinishCount(official.getFinishCount());
                organizer
                    .setFinishTotalCount(organizer.getOrganizerCount() + official.getFinishCount());
                if (StringUtils.isBlank(organizer.getMatchName())) {
                    organizer.setMatchName(official.getMatchName());
                }
            }
            results.add(organizer);
        }
        for (ReportMatchFinishEntity official : officials) {
            ReportMatchFinishEntity organizer = this
                .getReportMatchSourceEntity(official.getMatchCode(), results);
            if (organizer == null) {
                official.setFinishTotalCount(official.getFinishCount());
                results.add(official);
            }
        }
        return results;
    }

    public ReportMatchFinishEntity getReportMatchSourceEntity(String matchCode,
                                                              List<ReportMatchFinishEntity> list) {
        if (list == null || list.size() < 0) {
            return null;
        }
        for (ReportMatchFinishEntity entity : list) {
            if (entity != null && matchCode.equals(entity.getMatchCode())) {
                return entity;
            }
        }
        return null;
    }

    /**
     * 获取承办方完赛数据
     * 
     * @param applyinfos
     * @return
     */
    public List<ReportMatchFinishEntity> getOrganizerCount(List<Map<String, Object>> applyinfos,
                                                           List<OpenUnitEntity> units,
                                                           String type) {
        List<ReportMatchFinishEntity> list = new ArrayList<ReportMatchFinishEntity>();
        if (applyinfos != null && applyinfos.size() > 0) {
            for (Map<String, Object> matchMap : applyinfos) {
                ReportMatchFinishEntity entity = scoremapCovertEntity(matchMap, units, type);
                if (entity != null) {
                    list.add(entity);
                }
            }
        }
        return list;
    }

    /**
     * 获取官网完赛数据
     * 
     * @param applyinfos
     * @return
     */
    //    public List<ReportMatchFinishEntity> getOfficialCount(List<Map<String, Object>> applyinfos,
    //                                                          List<OpenUnitEntity> units, String type) {
    //        List<ReportMatchFinishEntity> list = new ArrayList<ReportMatchFinishEntity>();
    //        if (applyinfos != null && applyinfos.size() > 0) {
    //            for (Map<String, Object> matchMap : applyinfos) {
    //                ReportMatchFinishEntity entity = scoremapCovertEntity(matchMap, units, type);
    //                if (entity != null) {
    //                    list.add(entity);
    //                }
    //            }
    //        }
    //        return list;
    //    }

    /**
     * 统计数据转换为对象
     * 
     * @param matchMap
     * @param type
     * @return
     */
    public ReportMatchFinishEntity scoremapCovertEntity(Map<String, Object> matchMap,
                                                        List<OpenUnitEntity> units, String type) {
        ReportMatchFinishEntity entity = new ReportMatchFinishEntity();
        int finishCount = 0;
        int enrollSource = 0;
        String matchCode = StringUtils.defaultString((String) matchMap.get("match_code"), "");
        String matchName = StringUtils.defaultString((String) matchMap.get("match_name"), "");
        String unitCode = StringUtils.defaultString((String) matchMap.get("unit_code"), "");
        String unitName = OpenApplyinfoCountServiceImpl.getUnitName(units, unitCode);
        if (matchMap.containsKey("enroll_source") && matchMap.get("enroll_source") != null) {
            enrollSource = (int) matchMap.get("enroll_source");
        }
        Date partTime = (Date) matchMap.get("part_time");
        String day = new SimpleDateFormat("yyyy-MM-dd").format(partTime);
        if (matchMap.containsKey("counts") && matchMap.get("counts") != null) {
            Long conut = (Long) matchMap.get("counts");
            finishCount = conut.intValue();
        }
        if (OFFICIAL.equals(type)) {
            if (enrollSource == 1) {
                entity.setFinishCount(finishCount);
                entity.setOrganizerCount(0);
            } else {
                return null;
            }
        } else {
            if (enrollSource == 1) {
                return null;
            }
            entity.setOrganizerCount(finishCount);
            entity.setFinishCount(0);
        }
        entity.setMatchCode(matchCode);
        entity.setMatchName(matchName);
        entity.setUnitCode(unitCode);
        entity.setUnitName(unitName);
        entity.setPartTime(partTime);
        entity.setPartTimeDay(DateUtil.strToDate(day));
        return entity;
    }

}
