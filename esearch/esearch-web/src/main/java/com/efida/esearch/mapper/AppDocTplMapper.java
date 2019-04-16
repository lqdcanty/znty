package com.efida.esearch.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.efida.esearch.model.AppDocTpl;
import com.efida.esearch.model.AppDocTplDto;

public interface AppDocTplMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AppDocTpl record);

    int insertSelective(AppDocTpl record);

    AppDocTpl selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AppDocTpl record);

    int updateByPrimaryKey(AppDocTpl record);

    AppDocTpl selectByAppIdAndDocTplCode(@Param("appId") String appId,
                                         @Param("docTplCode") String docTplCode);

    List<AppDocTplDto> selectAppTemplatesParams(Map<String, Object> params);

}