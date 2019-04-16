package com.efida.esearch.core;

import com.alibaba.fastjson.JSONObject;

public enum EFieldProperty {
                            //    P_BOOST("boost",null,1,"权重值",false),  
                            P_INDEX("index", new Object[] { true, false }, true,
                                    "是否索引存储,_source中存储源数据",
                                    false), P_STORE("store", new Object[] { true, false }, false,
                                                    "默认不存储,如果为True在查询结果中将独立这一个属性数据，默认_source中默认有存储",
                                                    false), P_NULL_VALUE("null_value", new Object[] {0}, 0,
                                                                         "空属性填充，请按照字段类型和格式输入，否则模板审核会不通过",
                                                                         true), P_DOC_VALUES("doc_values",
                                                                                             new Object[] { true,
                                                                                                            false },
                                                                                             true,
                                                                                             "序列化数据到磁盘，false不能被用于聚合、排序以及脚本操作，存在数据大内存崩溃的风险，true查询效率有一定影响，但是安全。",
                                                                                             false), P_COERCE("coerce",
                                                                                                              new Object[] { true,
                                                                                                                             false },
                                                                                                              true,
                                                                                                              "强制尝试清除脏值以适应字段的数据类型",
                                                                                                              false),

                            P_INDEX_PREFIXES("index_prefixes", new Object[] { "1-5", "2-5", "3-5" },
                                             "1-5", "索引拆词数",
                                             false), P_INDEX_PHRASES("index_phrases",
                                                                     new Object[] { true, false },
                                                                     false, "最大索引精确快速匹配",
                                                                     false), P_SEARCH_ANALYZER("search_analyzer",
                                                                                               new Object[] { "ik_max_word",
                                                                                                              "ik_smart","standard" },
                                                                                               "ik_max_word",
                                                                                               "搜索字符拆词",
                                                                                               false),

                            //正常情况  需要先配置 analysis 的每个 analyzer ，然后再字段中引用配置的 analyzer 名称，当然目前都可以直接配置系统自带或者插件安装分词器
                            P_ANALYZER("analyzer", new Object[] { "ik_max_word", "ik_smart","standard" },
                                       "ik_max_word", "索引文本拆词",
                                       true), P_ENABLED("enabled", new Object[] { true, false },
                                                        true, "是否创建索引,默认true创建，false不创建",
                                                        false), P_EAGER_GLOBAL_ORDINALS("eager_global_ordinals",
                                                                                        new Object[] { true,
                                                                                                       false },
                                                                                        false,
                                                                                        "全局序数，针对text和keyword字段设置术语编号用于提升查询性能",
                                                                                        false), P_FIELDDATA("fielddata",
                                                                                                            new Object[] { true,
                                                                                                                           false },
                                                                                                            false,
                                                                                                            "ES 另外一种索引，正序索引,哪些文档中包含哪些词语,加载内存判断，慎重使用容易内存溢出",
                                                                                                            false), P_SEARCH_QUOTE_ANALYZER("search_quote_analyzer",
                                                                                                                                            new Object[] { true,
                                                                                                                                                           false },
                                                                                                                                            false,
                                                                                                                                            "是否禁用停止词",
                                                                                                                                            false), P_SIMILARITY("similarity",
                                                                                                                                                                 new Object[] { "classic",
                                                                                                                                                                                "BM25",
                                                                                                                                                                                "boolean" },
                                                                                                                                                                 "BM25",
                                                                                                                                                                 "相似度算法",
                                                                                                                                                                 false), P_IGNORE_ABOVE("ignore_above",
                                                                                                                                                                                        new Object[] { 100,
                                                                                                                                                                                                       200,
                                                                                                                                                                                                       400,
                                                                                                                                                                                                       800,
                                                                                                                                                                                                       10000 },
                                                                                                                                                                                        10000,
                                                                                                                                                                                        "超过长度不做索引,不能精确匹配",
                                                                                                                                                                                        false), P_IGNORE_MALFORMED("ignore_malformed",
                                                                                                                                                                                                                   new Object[] { true,
                                                                                                                                                                                                                                  false },
                                                                                                                                                                                                                   false,
                                                                                                                                                                                                                   "忽略字段值异常类型，插入不会报错",
                                                                                                                                                                                                                   false), P_NORMS("norms",
                                                                                                                                                                                                                                   new Object[] { true,
                                                                                                                                                                                                                                                  false },
                                                                                                                                                                                                                                   false,
                                                                                                                                                                                                                                   "用于标准化文档，以便查询时计算文档的相关性。建议不开启,默认true,特别消耗磁盘大小",
                                                                                                                                                                                                                                   false),
                            //    P_INDEX_OPTIONS("index_options",new Object[]{"positions","docs","offsets","freqs"},"positions","索引类型字符窜类型默认:positions,其他docs",false),
                            P_FORMAT("format",
                                     new Object[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss",
                                                    "epoch_millis", "epoch_second" },
                                     "yyyy-MM-dd HH:mm:ss", "日期格式化", false),
    //    P_COPY_TO("copy_to",null,null,"将该字段值复制到目标字段值中",false),//该属性用户可以定义结构导入数据前做，所以不开放
    //    P_TERM_VECTOR("term_vector",new Object[]{"no","yes","with_positions","with_offsets","with_positions_offsets"},"no","词向量存储，不存储，存储，储存偏移量，存储词索引",false),
    //    P_PROPERTIES("properties",null,null,"复合结构，一般不使用,关系型建议冗余模式做",false),
    //  P_POSITION_INCREMENT_GAP("position_increment_gap",,new Object[]{true,false},false,"文本字段存数组类型将会将数组所有元素窜起来做索引",false),

    //    arabic_normalization, asciifolding, bengali_normalization, cjk_width, decimal_digit, elision, german_normalization, hindi_normalization, indic_normalization, lowercase, persian_normalization, scandinavian_folding, serbian_normalization, sorani_normalization, uppercase
    //P_NORMALIZER("norms",new Object[]{"arabic_normalization","asciifolding"},"asciifolding","规化器类似于分析器beta版本还不稳定,不建议使用",false),
    ;

    private String  property;
    private Object  values;
    private Object  defaultVal;
    private String  comment;
    private boolean onIndexTrue;;

    public static EFieldProperty defaultVal(EFieldProperty obj, Object val) {
        obj.setDefaultVal(val);
        return obj;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Object getValues() {
        return values;
    }

    public void setValues(Object values) {
        this.values = values;
    }

    public Object getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(Object defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isOnIndexTrue() {
        return onIndexTrue;
    }

    public void setOnIndexTrue(boolean onIndexTrue) {
        this.onIndexTrue = onIndexTrue;
    }

    EFieldProperty(String property, Object values, Object defaultVal, String comment,
                   boolean onIndexTrue) {
        this.property = property;
        this.values = values;
        this.defaultVal = defaultVal;
        this.comment = comment;
        this.onIndexTrue = onIndexTrue;
    }

    public static JSONObject toJson(String property) {
        for (EFieldProperty e : EFieldProperty.values()) {
            if (e.getProperty().equals(property)) {
                JSONObject object = new JSONObject();
                object.put("property", e.getProperty());
                object.put("values", e.getValues());
                object.put("defaultVal", e.getDefaultVal());
                object.put("comment", e.getComment());
                object.put("onIndexTrue", e.isOnIndexTrue());
                return object;
            }
        }
        return new JSONObject();
    }
}
