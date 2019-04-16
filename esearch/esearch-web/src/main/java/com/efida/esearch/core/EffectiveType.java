package com.efida.esearch.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public enum EffectiveType {
    e_none("e_none","无"),
    e_null("e_null","不为NULL"),
    e_empty("e_empty","不为空窜"),
    e_value("e_value","条件值");

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

    EffectiveType(String type, String comment) {
        this.type=type;
        this.comment=comment;
    }
    public static JSONObject toJson(String type){
        JSONArray jsonArray = new JSONArray();
        for (EffectiveType e : EffectiveType.values()) {
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
        for (EffectiveType e : EffectiveType.values()) {
            jsonArray.add(toJson(e.getType()));
        }
        return jsonArray;
    }
}
