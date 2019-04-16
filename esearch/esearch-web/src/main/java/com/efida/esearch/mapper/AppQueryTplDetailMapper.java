package com.efida.esearch.mapper;

import com.efida.esearch.model.AppQueryTplDetail;
import com.efida.esearch.model.AppQueryTplDetailKey;

import java.util.List;

public interface AppQueryTplDetailMapper {
    int deleteByPrimaryKey(AppQueryTplDetailKey key);

    int insert(AppQueryTplDetail record);

    int insertSelective(AppQueryTplDetail record);

    AppQueryTplDetail selectByPrimaryKey(AppQueryTplDetailKey key);

    int updateByPrimaryKeySelective(AppQueryTplDetail record);

    int updateByPrimaryKeyWithBLOBs(AppQueryTplDetail record);

    int updateByPrimaryKey(AppQueryTplDetail record);

    void saveQueryTplConditionsBatch(List<AppQueryTplDetail> tplDetails);
}