package com.efida.sports.app.converter;

import com.efida.sport.dmp.facade.model.ScoreHeader;
import com.efida.sport.dmp.facade.model.ScoreModel;
import org.apache.commons.lang3.StringUtils;

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
        if(scoreModel==null){
            return result;
        }
        result.put("matchName", scoreModel.getMatchName());
        result.put("siteName", scoreModel.getSiteName());
        result.put("groupName", scoreModel.getGroupName());
        result.put("eventName", scoreModel.getEventName());
        return  result;
    }
    public static  List<Map<String, Object>>  converterScoreList( List<ScoreModel> scoreModels,String competitionCode,String scoreDesc) {
        List<Map<String, Object>> list = new ArrayList<>();
        if( scoreModels == null || scoreModels.size() <= 0){
            return list;
        }
        for (ScoreModel score : scoreModels) {
            boolean result = false;
            if(StringUtils.isNotBlank(scoreDesc)){
                result=score.getScoreDesc().equals(scoreDesc);
            }
            if( !result ) {
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("time", score.getPartTime());
                hashMap.put("timeStr", score.getPartTimeStr());
                hashMap.put("score", score.getScoreDesc());
                hashMap.put("competitionCode", competitionCode);
                list.add(hashMap);
            }
        }
        return list;
    }
}
