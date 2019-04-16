package com.efida.esearch.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.efida.esearch.model.AppQueryTpl;

public interface AppQueryTplMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AppQueryTpl record);

    int insertSelective(AppQueryTpl record);

    AppQueryTpl selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AppQueryTpl record);

    int updateByPrimaryKeyWithBLOBs(AppQueryTpl record);

    int updateByPrimaryKey(AppQueryTpl record);

    AppQueryTpl selectAppQueryTplByCode(String appQueryCode);

    List<AppQueryTpl> getQueryTemplates(Map<String, Object> params);

    AppQueryTpl selectByCode(@Param(value = "tplCode") String tplCode);

    AppQueryTpl selectAppQueryTplByAappIndexTplCode(@Param(value = "appId") String appId,@Param(value = "docTplCode") String docTplCode,@Param(value = "tplCode") String tplCode );
}