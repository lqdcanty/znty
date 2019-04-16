package com.efida.esearch.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.efida.esearch.core.EFieldProperty.*;

public enum EDataType {
    T_BINARY("binary", "字节类型", "该类型的字段把值当做经过 base64 编码的字符串，默认不存储，且不可搜索",
            new EFieldProperty[]{
//                    P_BOOST,
                    P_DOC_VALUES,
                    P_STORE
            },
            new EFieldProperty[]{}),
    T_BOOLEAN("boolean", "是否", "true 和 false",
            new EFieldProperty[]{
//                    P_BOOST,
                    P_DOC_VALUES,
                    P_INDEX,
                    P_STORE
            },
            new EFieldProperty[]{}),
    T_IP("ip", "IP类型", "192.168.11.23/32",
            new EFieldProperty[]{
//                    P_BOOST,
                    P_DOC_VALUES,
                    P_INDEX,
                    P_STORE
            },
            new EFieldProperty[]{P_NULL_VALUE,}),
    T_KEYWORD("keyword", "单词语", "查询通过Filter方式，不会计算权重得分",
            new EFieldProperty[]{
                    P_INDEX,
                    P_DOC_VALUES,
                    P_STORE,
                    P_IGNORE_ABOVE,
//                    P_SIMILARITY,
//            P_BOOST,
//            P_INDEX_OPTIONS,
            }, new EFieldProperty[]{
            P_EAGER_GLOBAL_ORDINALS,
//            p_fields
            P_NORMS,
//            p_normalizer

    }),
    T_DATE("date", "日期", "支持各种系统格式，建议系统默认格式 format:yyyy-MM-dd HH:mm:ss 对应类型设置",
            new EFieldProperty[]{
                    P_INDEX,
                    P_STORE,
                    P_FORMAT
//                    P_BOOST,
            },
            new EFieldProperty[]{
                    P_DOC_VALUES,
                    P_IGNORE_MALFORMED,
                    P_NULL_VALUE,
            }),
    T_BYTE("byte", "字节小整形", "有符号8-bit integer，-128 ~ 127",
            new EFieldProperty[]{
                    P_INDEX,
                    P_STORE,
//                    P_BOOST,
            },
            new EFieldProperty[]{
                    P_NULL_VALUE,
                    P_DOC_VALUES,
            }),
    T_DOUBLE("double", "双精度浮点数", "64-bit IEEE 754 浮点数",
            new EFieldProperty[]{
                    P_INDEX,
                    P_STORE,
//                    P_BOOST,
            },
            new EFieldProperty[]{
                    P_DOC_VALUES,
                    P_NULL_VALUE,
            }),
    T_FLOAT("float", "浮点数", "32-bit IEEE 754 浮点数",
            new EFieldProperty[]{
                    P_INDEX,
                    P_STORE,
//                    P_BOOST,
            },
            new EFieldProperty[]{
                    P_NULL_VALUE,
                    P_DOC_VALUES,
            }),
    T_HALF_FLOAT("half_float", "小浮点数", "16-bit IEEE 754 浮点数",
            new EFieldProperty[]{
                    P_INDEX,
                    P_STORE,
//                    P_BOOST,
            },
            new EFieldProperty[]{
                    P_NULL_VALUE,
                    P_DOC_VALUES,
            }),
    T_INTEGER("integer", "整数", "有符号32-bit integer，-2^31 ~ 2^31 - 1",
            new EFieldProperty[]{
                    P_INDEX,
                    P_STORE,
//                    P_BOOST,
            },
            new EFieldProperty[]{
                    P_NULL_VALUE,
                    P_DOC_VALUES,
            }),
    T_LONG("long", "长整型", "有符号64-bit integer：-2^63 ~ 2^63 - 1",
            new EFieldProperty[]{
                    P_INDEX,
                    P_STORE,
//                    P_BOOST,
            },
            new EFieldProperty[]{
                    P_DOC_VALUES,
                    P_NULL_VALUE,
            }),
    T_SHORT("short", "短整型", "有符号16-bit integer，-32768 ~ 32767",
            new EFieldProperty[]{
                    P_INDEX,
                    P_NULL_VALUE,
                    P_STORE,
//                    P_BOOST,
            },
            new EFieldProperty[]{
                    P_DOC_VALUES,
            }),
    T_TEXT("text", "文本", "可以通过拆词插件拆词匹配计算权重得分查询",
            new EFieldProperty[]{
                    P_INDEX,
                    P_ANALYZER,
                    P_STORE,
                    P_SEARCH_ANALYZER,
                    P_SIMILARITY,
//            P_BOOST,
//            P_FIELDDATA_FREQUENCY_FILTER,P_FIELDS,
//            P_INDEX_OPTIONS,
//            P_POSITION_INCREMENT_GAP,
//            P_TERM_VECTOR
            }, new EFieldProperty[]{
                    P_EAGER_GLOBAL_ORDINALS,
                    P_FIELDDATA,
                    P_INDEX_PREFIXES,
                    P_INDEX_PHRASES,
                    P_NORMS,
//                    P_SEARCH_QUOTE_ANALYZER,

    }),
//    T_DATE_RANGE("date_range", "日期范围类型", "64-bit 无符号整数，时间戳（单位：毫秒）存储：{gte:1,lte:10}",
//            new EFieldProperty[]{
//                    P_INDEX,
//                    P_STORE
//            },
//            new EFieldProperty[]{
//                    P_COERCE,
//            }),
//    T_DOUBLE_RANGE("double_range", "双精度范围类型", "64-bit IEEE 754 浮点数 存储：{gte:1,lte:10}",
//            new EFieldProperty[]{
//                    P_INDEX,
//                    P_STORE
//            },
//            new EFieldProperty[]{
//                    P_COERCE,
//            }),
//    T_FLOAT_RANGE("float_range", "浮点数范围类型", "32-bit IEEE 754 浮点数 存储：{gte:1,lte:10}",
//            new EFieldProperty[]{
//                    P_INDEX,
//                    P_STORE
//            },
//            new EFieldProperty[]{
//                    P_COERCE,
//            }),
//    T_INTEGER_RANGE("integer_range", "整数范围类型", "有符号32-bit integer，-2^31 ~ 2^31 - 1 存储：{gte:1,lte:10}",
//            new EFieldProperty[]{
//                    P_INDEX,
//                    P_STORE
//            },
//            new EFieldProperty[]{
//                    P_COERCE,
//            }),
    /**
     *
     *  存储 {gte:1,lte:20}
     * 搜索 "term" : {"age": 15} 可以搜索该值；搜索 "range": {"age": {"gte":11, "lte": 15}} 也可以搜索到
     *  range范围类型特殊配置:
     *  INTERSECTS：默认配置，存在交集就返回
     *  WITHIN：字段值需要完全包含在搜索值之内   字段范围A 搜索范围B   B完全包含A
     *  CONTAINS：与WITHIN相反，只搜索字段值包含搜索值的文档  字段范围A 搜索范围B   A完全包含B
     */

//    T_IP_RANGE("ip_range", "IP范围类型", "192.168.0.1/21 192.168.0.7/32",
//            new EFieldProperty[]{
//                    P_INDEX,
//                    P_STORE
//            },
//            new EFieldProperty[]{
//                    P_COERCE,
//            }),
//
//    T_LONG_RANGE("long_range", "长整型范围类型", "有符号64-bit integer：-2^63 ~ 2^63 - 1  存储：{gte:1,lte:10}",
//            new EFieldProperty[]{
//                    P_INDEX,
//                    P_STORE
//            },
//            new EFieldProperty[]{
//                    P_COERCE,
//            }),
    ;

    EDataType(String type, String typeName, String comment, EFieldProperty[] usefullProperties, EFieldProperty[] extendProperties) {
        this.type=type;
        this.typeName=typeName;
        this.comment=comment;
        this.usefullProperties=usefullProperties;
        this.extendProperties=extendProperties;
    }

    private String type;
    private String typeName;
    private String comment;
    private EFieldProperty[] usefullProperties;
    private EFieldProperty[] extendProperties;

    public static JSONObject toJson(String type){
        JSONArray jsonArray = new JSONArray();

        for (EDataType e : EDataType.values()) {
            if(e.getType().equals(type)) {
                JSONObject object = new JSONObject();
                object.put("type", e.getType());
                object.put("typeName", e.getTypeName());
                object.put("comment", e.getComment());
                EFieldProperty[] usefullProperties = e.getUsefullProperties();
                JSONArray fileds = new JSONArray();
                if (usefullProperties != null) {
                    for (EFieldProperty fieldProperty : usefullProperties) {
                        fileds.add(EFieldProperty.toJson(fieldProperty.getProperty()));
                    }
                    object.put("userfullProperties", fileds);
                } else {
                    object.put("userfullProperties", fileds);
                }
                EFieldProperty[] extendProperties = e.getExtendProperties();
                fileds = new JSONArray();
                if (extendProperties != null) {
                    for (EFieldProperty fieldProperty : extendProperties) {
                        fileds.add(EFieldProperty.toJson(fieldProperty.getProperty()));
                    }
                    object.put("extendProperties", fileds);
                } else {
                    object.put("extendProperties", fileds);
                }
                object.put("type", e.getType());
                return object;
            }
        }
        return new JSONObject();
    }

    public static JSONArray toJson(){
        JSONArray jsonArray = new JSONArray();
        for (EDataType e : EDataType.values()) {
            jsonArray.add(toJson(e.getType()));
        }
        return jsonArray;
    }

    public String getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public EFieldProperty[] getUsefullProperties() {
        return usefullProperties;
    }

    public void setUsefullProperties(EFieldProperty[] usefullProperties) {
        this.usefullProperties = usefullProperties;
    }

    public EFieldProperty[] getExtendProperties() {
        return extendProperties;
    }

    public void setExtendProperties(EFieldProperty[] extendProperties) {
        this.extendProperties = extendProperties;
    }

    public void setType(String type) {
        this.type = type;
    }
}
