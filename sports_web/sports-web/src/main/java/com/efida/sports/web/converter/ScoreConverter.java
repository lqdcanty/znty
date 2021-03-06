package com.efida.sports.web.converter;

import com.efida.sport.dmp.facade.model.ScoreHeader;
import com.efida.sport.dmp.facade.model.ScoreModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreConverter {

    public static List<Map<String,Object>> convertHeader(List<ScoreHeader> scoreHeaders, List<List<String>> dataList ) {
        if(scoreHeaders == null || dataList == null){
            return null;
        }
        List<Map<String,Object>> resultList = new ArrayList<>();
        //去掉不需要显示字段
        for (int i = 0; i < scoreHeaders.size(); i++) {
            if(scoreHeaders.get(i).isH5IsShow()){
                Map<String,Object> result = new HashMap<>();
                result.put("key",scoreHeaders.get(i).getHeader());
                result.put("val",convertList(dataList,i));
                resultList.add(result);
            }
        }
        return resultList;
    }
    //[[1,2,3],[4,5,6],[7,8,9]]
    public static List<String> convertList(List<List<String>> dataList, int page){
        List<String> retList = new ArrayList<>();
        for (int i = 0;i<dataList.size();i++){
            retList.add(dataList.get(i).get(page));
        }
        return retList;
    }

    public static Map<String, Object>  getMyScoreInfo( ScoreModel scoreModel){
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("matchName", scoreModel.getMatchName());
        result.put("siteName", scoreModel.getSiteName());
        result.put("groupName", scoreModel.getGroupName());
        result.put("eventName", scoreModel.getEventName());
        return  result;
    }
    public static  List<Map<String, Object>>  converterScoreList( List<ScoreModel> scoreModels) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ScoreModel score : scoreModels) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("time", score.getPartTime());
            hashMap.put("timeStr", score.getPartTimeStr());
            hashMap.put("score", score.getScoreDesc());
            hashMap.put("competitionCode", score.getCompetitionCode());
            list.add(hashMap);
        }
        return list;
    }
}
