package com.efida.esearch.facade.constains;

import java.io.Serializable;

public enum ResultMsgCode {

    success("success","成功"),noauth("noauth","未授权"),unrequest("unrequest","禁止访问"),paramerror("paramerror","参数异常"),syserror("syserror","服务器系统异常");
    private  String code;
    private  String desc;
    private ResultMsgCode(String code,String desc){
        this.code=code;
        this.desc=desc;
    }

    //根据code获取枚举
    public static ResultMsgCode getEnumByCode(String code){
        if(null == code){
            return null;
        }
        for(ResultMsgCode temp:ResultMsgCode.values()){
            if(temp.getCode().equals(code)){
                return temp;
            }
        }
        return null;
    }
    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}
