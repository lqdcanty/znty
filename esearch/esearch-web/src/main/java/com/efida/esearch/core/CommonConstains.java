package com.efida.esearch.core;

public class CommonConstains {

    public enum DataTypes {

        T_LONG("long","长整型","有符号64-bit integer：-2^63 ~ 2^63 - 1"),
        T_INTEGER("integer","整数","有符号32-bit integer，-2^31 ~ 2^31 - 1"),
        T_SHORT("short","短整型","有符号16-bit integer，-32768 ~ 32767"),
        T_KEYWORD("keyword","单词语","查询通过Filter方式，不会计算权重得分"),
        T_TEXT("text","文本","可以通过拆词插件拆词匹配计算权重得分查询"),
        T_DATE("date","日期","支持各种系统格式，建议系统默认格式 format:yyyy-MM-dd HH:mm:ss 对应类型设置"),
        T_IP("ip","IP类型","192.168.11.23/32"),
        T_BYTE("byte","字节小整形","有符号8-bit integer，-128 ~ 127"),
        T_DOUBLE("double","双精度浮点数","64-bit IEEE 754 浮点数"),
        T_FLOAT("float","浮点数","32-bit IEEE 754 浮点数"),
        T_HALF_FLOAT("half_float","小浮点数","16-bit IEEE 754 浮点数"),
        T_BOOLEAN("boolean","是否","true 和 false"),
        T_BINARY("binary","字节类型","该类型的字段把值当做经过 base64 编码的字符串，默认不存储，且不可搜索"),

        /**
         *
         *  存储 {gte:1,lte:20}
         * 搜索 "term" : {"age": 15} 可以搜索该值；搜索 "range": {"age": {"gte":11, "lte": 15}} 也可以搜索到
         *  range范围类型特殊配置:
         *  INTERSECTS：默认配置，存在交集就返回
         *  WITHIN：字段值需要完全包含在搜索值之内   字段范围A 搜索范围B   B完全包含A
         *  CONTAINS：与WITHIN相反，只搜索字段值包含搜索值的文档  字段范围A 搜索范围B   A完全包含B
         */
        T_INTEGER_RANGE("integer_range","整数范围类型","有符号32-bit integer，-2^31 ~ 2^31 - 1 存储：{gte:1,lte:10}"),
        T_FLOAT_RANGE("float_range","浮点数范围类型","32-bit IEEE 754 浮点数 存储：{gte:1,lte:10}"),
        T_LONG_RANGE("long_range","长整型范围类型","有符号64-bit integer：-2^63 ~ 2^63 - 1  存储：{gte:1,lte:10}"),
        T_DOUBLE_RANGE("double_range","双精度范围类型","64-bit IEEE 754 浮点数 存储：{gte:1,lte:10}"),
        T_DATE_RANGE("date_range","日期范围类型","64-bit 无符号整数，时间戳（单位：毫秒）存储：{gte:1,lte:10}"),
        T_IP_RANGE("ip_range","IP范围类型","192.168.0.1/21 192.168.0.7/32");

        DataTypes(String type,String typeName,String comment){

        }
    }
    /**
     * 公共属性：
     * properties :嵌套结构 当前版本不开放，过于复杂..
     * 以下直接归类
     *
     * 字符窜类型可有属性:
     * ignore_above
     * analyzer
     * boost
     * eager_global_ordinals
     * fielddata
     * fielddata_frequency_filter
     * fields
     * index
     * index_options 默认不提供界面配置
     * index_prefixes 拆词索引字符数 默认 0-5  {min_chars:1,max_char:10}
     * index_phrases 默认false 更大索引来精确快速匹配 2个字为一组作索引
     * norms  默认不提供界面配置 默认 true ，不利于用户理解
     * position_increment_gap 夸字段匹配用到的分数影响值 默认 100 ，具体作用还未理解
     * store
     * search_analyzer
     * search_quote_analyzer 禁用停止词.. 还未理解很复杂
     * similarity :相似度算法，评分算法选择，提供界面配置
     * term_vector
     *
     * keyword类型
     * boost
     * doc_values
     * eager_global_ordinals
     * fields
     * ignore_above
     * index
     * index_options
     * norms
     * null_value
     * store
     * similarity
     * normalizer 需要先配置 analysis ，然后使用 ，比较复杂后续在考虑 ....参考:https://m.imooc.com/article/72059
     *
     * 日期类型
     * format: 可选值太多，固定几个格式 直接使用表达式，不使用 es 内部类型名称（太多不太好理解） yyyy-MM-dd HH:mm:ss|| yyyy/MM/dd HH:mm:ss || yyyy-MM-dd|| yyyy-MM
     * boost
     * doc_values 类似磁盘缓存，提高查询效率，但是磁盘开销变大 默认 true
     * locale
     * ignore_malformed
     * index
     * null_value
     * store
     *
     * 数字类型
     * coerce:脏数据清理，自动转换
     * boost
     * doc_values 类似磁盘缓存，提高查询效率，但是磁盘开销变大 默认 true
     * index
     * store
     * ignore_malformed
     * null_values
     *
     * 浮点数--包含数字类型所有属性
     * scaling_factor 浮点数缩放值，对于缩放后端额值做索引，实际存储还是原来的值
     *
     * IP 类型
     * boost
     * doc_values
     * index
     * null_value
     * store
     *
     * 数组类型
     *
     * 范围类型
     * coerce
     * boost
     * index
     * store
     *
     * BOOLEAN 类型
     * boost
     * doc_values
     * index
     * null_value
     * store
     *
     * 对象类型，嵌套类型，坐标类型，token_count，join类型（关联字段，获取不同文档内容）--不常用，暂时不开发
     */




    //字符窜类型  可以忽略 超过长度的字符窜 ignore_above
    //插入文档时候是否忽略类型  ignore_malformed 默认 false
    //index  和 enable 几乎是一个意思.  enable 可以作为 doc配置,index 只能作为字段配置。true 和false 该字段是否会被索引和查询
    //null_values  指定一个值替换 显示的 xxx字段:null   例如：使用"NULL" 替换 null (xxx字段:"NULL")这样该字段为null时候也可以被索引 ，就可以查询为 "NULL" 的值

    /**
     *  index_options 索引操作:
     *  字符窜类型默认:positions
     *  其他字段默认:docs
     *
     *  docs:只有DOC编号被索引。这个的理解为：每个字段都有个唯一标识，对这个标识做索引。
     *  offsets:对文档号、术语频率、位置以及开始和结束字符偏移（将术语映射回原始字符串）进行索引。偏移量被统一的打火机使用以加速突出显示。
     *  freqs:DOC编号和术语频率被索引。术语频率被用来得分高于单个术语的重复项。
     *  positions: DOC编号、术语频率和术语位置（或订单）被索引。位置可以用于邻近或短语查询
     */

    //fields ：配置多个分词器 除了默认分词器意外的其他分词器,查询的时候也可以指定分词器进行查询，例如查中文或者查英文文章，需要分别使用不同的分词器
    //    //norms: 用于标准化文档，以便查询时计算文档的相关性。建议不开启,默认true,特别消耗磁盘大小
        //properties:嵌套结构

    //store 是否存储  false会被索引，但是不能查询原字段的值，通常 都默认 true ，false的时候如果可以索引查询不能查询原值，是因为默认存储在了 _source中

    /**
     *  analyzer: 用于setting中，目前不使用该模式，改用feilds自定义分词器，例如： 分词器名称 autocomplete
     *      "analyzer": {
     *         "autocomplete": {
     *             "type": "custom",
     *                     "tokenizer": "standard",
     *                     "filter": [
     *             "lowercase",
     *                     "autocomplete_filter"
     *           ]
     *         }
     *     }
     */


//    "similarity" : "BM25",#用于指定文档评分模型，参数有三个：
//            # BM25 ：ES和Lucene默认的评分模型
//    # classic ：TF/IDF评分
//    # boolean：布尔模型评分


    public enum Analyzer{
        ik_smart("ik_smart","IK中文粗粒度分词器"),
        ik_word_max("ik_smart","IK中文细粒度分词器"),
        whitespace ("whitespace","空格分词器"),
        english ("english","英文分词器"),

        //标准分析器将文本划分为单词边界，如Unicode文本分割算法所定义的。它删除了大多数标点符号、小写词，并支持删除停止词。
        standard("standard","英文标准分词器"),//默认分词器

        //简单分析器每当遇到一个不是字母的字符时，就把文本分成几个术语。它是所有情况的小写字母。
        simple("standard","简单分词器"),

        //停止分析器就像简单的分析器，但也支持移除停止字。
        stop("stop","截止分词器"),

        //关键字分析器是一个“noop”分析器，它接受给定的任何文本，并输出与单个术语完全相同的文本。
        keyword("keyword","词语分词器"),

        //模式分析器使用正则表达式将文本拆分成术语。它支持下套管和停止字。
        partten("partten","简单分词器"),

        //特定语言的分析器，如英语或法语。这个就不固定，例如上面的 english
//        language("language","简单分词器"),
        ;
        Analyzer(String code,String name){

        }
    }
}
