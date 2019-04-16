package com.efida.esearch.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.efida.esearch.model.AppDocFields;

public interface AppDocFieldsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AppDocFields record);

    int insertSelective(AppDocFields record);

    AppDocFields selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AppDocFields record);

    int updateByPrimaryKey(AppDocFields record);

    List<AppDocFields> selectByAppIdAndDocCode(@Param("appId") String appId,
                                               @Param("docCode") String docCode);

    AppDocFields selectOneByParams(Map<String, Object> params);
}