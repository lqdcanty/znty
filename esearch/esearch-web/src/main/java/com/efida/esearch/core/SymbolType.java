package com.efida.esearch.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public enum  SymbolType {
//    match 分析字段评分匹配，其他字段精确匹配
//    term 精确匹配
//    terms 多个匹配条件 变量必须是数组变量
//    range 范围条件  gt,gte,lt,lte
//    exists 是否包含某个字段
//    missing


    /**
     * "multi_match":{
     *             "query":"chm",
     *             "fields":["author","title"]
     *         }
     */
//    multi_match("multi_match","多字段匹配",null),

    /**
     * "query_string":{
     *             "query":"Elasticsearch OR 张三",
     *             "fields":["author", "title"]
     *         }
     */
//    query_string("query_string","语法查询 例如 : A or B 会查询 包含A 或者 B的数据 添加 \"fields\":[\"author\", \"title\"] 可以多字段查询",null),
    match("match","文本分析索引字段评分匹配，其他索引字段精确匹配",true,null),
    match_phrase("match_phrase","所有词和顺序都需要满足",true,null),
    term("term","文本分析索引字段精确匹配",true,null),
//    terms("terms","多个匹配条件 变量必须是数组变量",true,null),
    range("range","范围条件  gt,gte,lt,lte",true,new String[]{"gt","gte","lt","lte"}),
    //“field”: “title”
    exists("exists","是否包含某个字段",false,null),
    //“field”: “title”
    missing("missing","是否不包含某个字段",false,null),
    regexp("regexp","正则查询，例如:E[0-9].+，拆词字段针对每个词",true,null),
    wildcard("wildcard","通配符查询例如 ?xxx,*xxx ?任意字符，*任意多个字符，拆词字段针对每个词",true,null);

    /**
     * aggs 聚合查询 处理逻辑先外层后内层
     * {
     *   "fields": [
     *     "show_status"
     *   ],
     *   "size": 0,
     *   "aggs": {
     *     "_result": {
     *       "terms": {
     *         "field": "province_id",
     *         "size": 100
     *       },
     *       "aggs": {
     *         "result": {
     *           "sum": {
     *             "field": "amount"
     *           }
     *         }
     *       }
     *     }
     *   }
     * }！
     */

    private String type;
    private String comment;
    private String[] properties;
    private boolean isHaveVal;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String[] getProperties() {
        return properties;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }

    public boolean isHaveVal() {
        return isHaveVal;
    }

    public void setHaveVal(boolean haveVal) {
        isHaveVal = haveVal;
    }

    SymbolType(String type, String comment, boolean isHaveVal,String[] properties) {
        this.type=type;
        this.comment=comment;
        this.properties=properties;
        this.isHaveVal=isHaveVal;
    }
    public static JSONObject toJson(String type){
        JSONArray jsonArray = new JSONArray();
        for (SymbolType e : SymbolType.values()) {
            if(e.getType().equals(type)) {
                JSONObject object = new JSONObject();
                object.put("type", e.getType());
                object.put("comment", e.getComment());
                object.put("properties", e.getProperties());
                object.put("isHaveVal", e.isHaveVal());
                return object;
            }
        }
        return new JSONObject();
    }

    public static JSONArray toJson(){
        JSONArray jsonArray = new JSONArray();
        for (SymbolType e : SymbolType.values()) {
            jsonArray.add(toJson(e.getType()));
        }
        return jsonArray;
    }
}
