package com.efida.esearch.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public enum LogicType {
    must("must","与关系(And)"),
    should("should","或关系(Or)"),
    must_not("must_not","非关系(!)");

    private String type;
    private String comment;

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

    LogicType(String type, String comment) {
        this.type=type;
        this.comment=comment;
    }
    public static JSONObject toJson(String type){
        JSONArray jsonArray = new JSONArray();
        for (LogicType e : LogicType.values()) {
            if(e.getType().equals(type)) {
                JSONObject object = new JSONObject();
                object.put("type", e.getType());
                object.put("comment", e.getComment());
                return object;
            }
        }
        return new JSONObject();
    }

    public static JSONArray toJson(){
        JSONArray jsonArray = new JSONArray();
        for (LogicType e : LogicType.values()) {
            jsonArray.add(toJson(e.getType()));
        }
        return jsonArray;
    }
}
