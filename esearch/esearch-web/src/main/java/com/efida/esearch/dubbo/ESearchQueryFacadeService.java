package com.efida.esearch.dubbo;

import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.*;
import java.util.Map.Entry;

import com.efida.esearch.facade.constains.ESortable;
import com.efida.esearch.model.AppDocFields;
import com.efida.esearch.utils.VelocityUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efida.esearch.core.ESClient;
import com.efida.esearch.facade.constains.ResultMsgCode;
import com.efida.esearch.facade.model.ESPageModel;
import com.efida.esearch.facade.model.RequestContext;
import com.efida.esearch.facade.model.SyncDataResultModel;
import com.efida.esearch.facade.results.ESearchResult;
import com.efida.esearch.facade.service.ESearchFacade;
import com.efida.esearch.model.AppDocTpl;
import com.efida.esearch.model.AppQueryTpl;
import com.efida.esearch.service.AppDocTplService;
import com.efida.esearch.service.AppQueryTplService;
import com.efida.esearch.service.AppService;
import com.efida.esearch.utils.SecretTools;

@Service
public class ESearchQueryFacadeService implements ESearchFacade {
    private Logger                        log            = LoggerFactory
        .getLogger(ESearchQueryFacadeService.class);
    private @Autowired ESClient           esClient;

    private @Autowired AppService         appService;

    private @Autowired AppDocTplService   appDocTplService;

    private @Autowired AppQueryTplService appQueryTplService;

    private VelocityEngine                velocityEngine = null;

    /**
     * 我们可以在查询条件中使用AND/OR/NOT操作符，这就是布尔查询(Bool Query)。布尔查询可以接受一个must参数(等价于AND)，一个must_not参数(等价于NOT)，以及一个should参数(等价于OR)
     * 查询模板定义举例:
     * 单字段匹配
     * "query": {
     *         "match": {
     *            "title": "我是中国人"
     *         }
     *     },
     *    "highlight": {
     *       "fields": {
     *          "title": {}
     *       }
     *    }
     * 多字段匹配
     *  "query": {
     *     "multi_match": {
     *       "type":     "most_fields",
     *       "query":    "最新",
     *       "fields": [ "title", "title.cn", "title.en" ]
     *     }
     *   }
     *  多条件查询
     *   "query": {
     *         "bool": {
     *                 "must": {
     *                     "bool" : {
     *                         "should": [
     *                             { "match": { "about": "music" }},
     *                             { "match": { "about": "climb" }} ]
     *                     }
     *                 },
     *                 "must": {
     *                     "match": { "first_nale": "John" }
     *                 },
     *                 "must_not": {
     *                     "match": {"last_name": "Smith" }
     *                 }
     *             }
     *     }
     *"query":{
     *       "bool": {
     *         "must":[
     *           {
     *             "match":{
     *               "content": "啥"
     *             }
     *           },
     *           {
     *             "range": {
     *               "createDate": {
     *                 "gte": "2018-09-04 00:00:00",
     *                 "lte": "2018-09-12 00:00:00"
     *               }
     *             }
     *           }
     *           ]
     *       }
     *     }
     */
    /**
     * 
     * @see com.efida.esearch.facade.service.ESearchFacade#(com.efida.esearch.facade.model.RequestContext, java.util.Map, long, int)
     */
    @Override
    public ESearchResult<ESPageModel> queryByTemplate(RequestContext request,
                                                      Map<String, String> param, long page,
                                                      int pageSize, ESortable eSortable,String sortField) {
        //"query":{"bool":{"must":[{"bool":{"should":[{"match":{"content":"${keyword}"}},{"match":{"title": "${keyword}"}}]}},{"range":{"createDate":{"gte":"${startTime}","lte":"${endTime}"}}}]}}}

        ESearchResult<ESPageModel> result = new ESearchResult<>();
        if (!checkAppInfo(result, request)) {
            return result;
        }
        if (pageSize > 2000) {
            result.setSuccess(false);
            result.setErrorMsg(ResultMsgCode.paramerror.getCode());
            result.setErrorMsg(ResultMsgCode.syserror.getDesc() + ":" + "每页显示条数大于最大显示2000条");
            return result;
        }
        AppQueryTpl queryTpl = appQueryTplService.getAppQueryTplByCode(request.getTplCode());
        if (queryTpl == null || StringUtils.isBlank(queryTpl.getTplContent())) {
            result.setSuccess(false);
            result.setErrorMsg(ResultMsgCode.paramerror.getCode());
            result.setErrorMsg(ResultMsgCode.paramerror.getDesc() + ":" + "提交的查询模板编码不存在，或者模板内容为空");
            return result;
        }

        //索引名称通过 appID和模板编码设置,不是查询编码，所以换掉查询编码
        request.setTplCode(queryTpl.getDocTplCode());
        String indexName = getIndexName(request);
        try {
            List<String> paramNames=new ArrayList<>();
            if(queryTpl.getParams()!=null){
                paramNames=Arrays.asList( queryTpl.getParams().split(","));
            }
           //replaceparam2(queryTpl.getTplContent(), param)
            String tplContent =  VelocityUtil.replaceTplByVelocity(queryTpl.getTplContent(),paramNames,param);
            JSONObject boolObject= JSONObject.parseObject(tplContent);
            JSONObject queryObject = new JSONObject();
            queryObject.put("query",boolObject);
            queryObject.put("from", ESPageModel.countOffset(page, pageSize));
            queryObject.put("size", pageSize);
            queryObject.put("sort",esClient.getSortConditionJson(sortField,eSortable.getSortType()));
            log.info("查询语句：" + queryObject.toJSONString());
            String resultStr = esClient.query(indexName, queryObject.toJSONString());
            JSONObject resultObj = JSON.parseObject(resultStr);
            result.setSuccess(true);
            result.setResultObj(buildPageDataByEsSearchResult(resultObj, page, pageSize));
        } catch (Exception e) {
            log.error("模板查询异常", e);
            result.setSuccess(false);
            result.setErrorCode(ResultMsgCode.syserror.getCode());
            result.setErrorMsg(ResultMsgCode.syserror.getDesc() + ":" + e.getMessage());
        }
        return result;
    }

    /***
     * 渲染模板内容
     * 
     * @param tplContent
     * @param params
     * @return
     */
    private String renderTemplate(String tplContent, Map<String, String> params) {

        if (velocityEngine == null) {
            Properties properties = new Properties();
            properties.setProperty("resource.loader", "class");
            properties.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            properties.setProperty("input.encoding", "UTF-8");
            properties.setProperty("output.encoding", "UTF-8");
            properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
            properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
            velocityEngine = new VelocityEngine(properties);
        }

        VelocityContext context = new VelocityContext();
        if (params != null) {
            Set<Entry<String, String>> set = params.entrySet();
            Iterator<Entry<String, String>> iter = set.iterator();
            while (iter.hasNext()) {
                Entry<String, String> entry = iter.next();
                String key = entry.getKey();
                String value = entry.getValue();
                context.put(key, value);

            }
        }
        StringWriter sw = new StringWriter();
        velocityEngine.evaluate(context, sw, "logtag", tplContent);
        return sw.toString();
    }

    /**
     * 
     * 
     * @param request
     * @return
     */
    public ESearchResult<ESPageModel> queryByES(RequestContext request, JSONObject queryJson) {

        ESearchResult<ESPageModel> result = new ESearchResult<>();
        if (!checkAppInfo(result, request)) {
            return result;
        }
        if (queryJson == null) {
            result.setSuccess(false);
            result.setErrorMsg(ResultMsgCode.paramerror.getCode());
            result.setErrorMsg(ResultMsgCode.syserror.getDesc() + ":" + "提交的结构不能为空");
            return result;
        }
        String indexName = getIndexName(request);
        try {
            String resultStr = esClient.query(indexName, queryJson.toJSONString());
            JSONObject resultObj = JSON.parseObject(resultStr);
            result.setSuccess(true);
            result.setResultObj(buildPageDataByEsSearchResult(resultObj,
                queryJson.getInteger("from"), queryJson.getInteger("size")));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorCode(ResultMsgCode.syserror.getCode());
            result.setErrorMsg(ResultMsgCode.syserror.getDesc() + ":" + e.getMessage());
        }
        return result;
    }

    /**
     * 根据ES查询结果构建公用高可读性的分页对象
     * @param esSearchResult
     * @return
     */
    private ESPageModel<String> buildPageDataByEsSearchResult(JSONObject esSearchResult, long page,
                                                              int pageSize) {
        ESPageModel<String> pageModel = new ESPageModel<>();
        if (esSearchResult == null)
            return pageModel;
        JSONObject dataObj = esSearchResult.getJSONObject("hits");
        if (dataObj == null)
            return pageModel;
        long total = dataObj.getLong("total");
        JSONArray dataList = dataObj.getJSONArray("hits");
        List<String> pageDataList = new ArrayList<>();
        //        List<String> pageDataList=new EFieldProperty[];
        if (dataList != null && !dataList.isEmpty()) {
            for (int i = 0; i < dataList.size(); i++) {
                //如果有高亮显示需求，需要全文档返回，去掉.getJSONObject("_source")
                pageDataList.add(dataList.getJSONObject(i).getJSONObject("_source").toJSONString());
            }
        }
        return new ESPageModel(pageDataList, total, page, pageSize);
    }

    /**
     * 
     * @see com.efida.esearch.facade.service.ESearchFacade#synchroOneData(com.efida.esearch.facade.model.RequestContext, java.lang.String, java.util.Map)
     */
    @Override
    public ESearchResult<String> synchroOneData(RequestContext request, String dataId,
                                                Map<String, String> param) {
        ESearchResult<String> result = new ESearchResult<>();
        if (!checkAppInfo(result, request)) {
            return result;
        }
        /*"doc_source_id":"1",
                "register":"fbrid0001",
                "register_name":"发表人",
                "title":"这个标题很神奇",
                "content":"这篇文章是说有那个啥哈哈哈啥",
                "comment":"这是一个奇怪的评论",
                "createDate":"2018-09-11 12:12:12",
                "type":"i don't no!",
                "delFlag":false*/
        //查询数据库字段，进行数据类型校验,先关闭该版本的校验，后续界面完成
        //        List<AppDocFields> list=appService..getAllFeild(request.getAppId(),request.getQueryTplCode());
        String docId = dataId;
        result.setResultObj(docId);
        if (StringUtils.isBlank(docId)) {
            result.setErrorMsg("数据标识字段data_source_id不允许为空");
            result.setSuccess(false);
            result.setResultObj(docId);
            return result;
        }
        //        for (AppDocFields appDocFields : list) {
        //            if(StringUtils.isBlank(param.get(appDocFields.getFieldType()))){
        //                resultModel=new SyncDataResultModel();
        //                resultModel.setDataId(docId);
        //                resultModel.setErrorMsg("数据字段"+appDocFields.getFieldType()+"不允许为空");
        //                resultModel.setStatus(false);
        //                break;
        //            }
        //        }
        //
        //        String tplContent=replaceparam(queryTpl.getTplContent(),param);
        //        JSONObject queryObject=JSONObject.parseObject(tplContent);
        //        queryObject.put("from",ESPageModel.countOffset(page,pageSize));
        //        queryObject.put("size",pageSize);
        //        String resultStr = esClient.query(indexName,  queryObject.toJSONString());

        try {
            String indexName = getIndexName(request);
            AppDocTpl docTpl = appDocTplService.getAppDocTpl(request.getAppId(),
                request.getTplCode());
            if(docTpl==null || StringUtils.isBlank(docTpl.getMappingTplContent()) || StringUtils.isBlank(docTpl.getDataTplContent())){
                log.info("同步数据==》模板内容不存在，请检查模板编号");
                throw  new Exception("同步数据==》模板内容不存在，请检查模板编号");
            }
            if (!esClient.checkIndexExits(indexName)) {
                log.info("没有获取到索引，需要新建，索引名为：" + indexName);
                esClient.createIndex(indexName);
                esClient.createMapping(indexName,
                    JSONObject.parseObject(docTpl.getMappingTplContent()));
            }
            log.info("保存数据模板内容：" + docTpl.getDataTplContent());

            List<AppDocFields> allFields= appDocTplService.getAllFeild(request.getAppId(),docTpl.getTplCode());

            String tplContent = VelocityUtil.replaceTplByVelocity(docTpl.getDataTplContent(),syncDataParams(allFields), param);
            log.info("保存数据查看：" + tplContent);
            JSONObject dataJson = JSON.parseObject(tplContent);

            esClient.upinsertDocument(indexName, docId, dataJson);

        } catch (Exception e) {
            log.error("保存单条数据异常：", e);
            result.setResultObj(docId);
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
            return result;
        }

        return result;
    }

    private List<String> syncDataParams(List<AppDocFields> allFields){
        List<String> result=new ArrayList<>();
        if(allFields!=null && allFields.size()>0){
            for (AppDocFields allField : allFields) {
                result.add(allField.getFieldName());
            }
        }
        return result;
    }

    /**
     * 
     * @see com.efida.esearch.facade.service.ESearchFacade#deleteByDataId(com.efida.esearch.facade.model.RequestContext, java.lang.String)
     */
    @Override
    public ESearchResult<String> deleteByDataId(RequestContext request, String dataId) {
        ESearchResult<String> result = new ESearchResult<>();
        if (!checkAppInfo(result, request)) {
            return result;
        }
        result.setSuccess(true);
        result.setResultObj(dataId);

        String indexName = getIndexName(request);
        try {
            esClient.deleteDocument(getIndexName(request), dataId);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorCode(ResultMsgCode.syserror.getCode());
            log.error("ES接口删除数据异常", e);
            if (e.getMessage().indexOf("404") > 0)
                result.setErrorMsg(String.format("数据id=%s不存在", dataId));
            else
                result.setErrorMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 该方法不提供给查询模板方法使用，查询模板的编码需要再次查找文档模板编码
     *      * @param request
     * @return
     */
    private String getIndexName(RequestContext request) {

        return request.getAppId() + "_" + request.getTplCode();

    }

    private String replaceparam2(String input, Map<String, String> param) throws Exception {
        JSONObject template = JSONObject.parseObject(input);
        Iterator<String> iterator = template.keySet().iterator();
        List<String> remvoeAllKeys = new ArrayList<>();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object temp = template.get(key);
            if (temp != null) {
                if (temp instanceof JSONObject) {
                    JSONObject child = template.getJSONObject(key);
                    JSONObject childJson = JSONObject
                        .parseObject(replaceparam2(child.toJSONString(), param));
                    if (childJson.keySet().size() != 0) {
                        template.put(key, childJson);
                    } else {
                        remvoeAllKeys.add(key);
                    }
                } else if (temp instanceof JSONArray) {
                    JSONArray childs = template.getJSONArray(key);
                    for (int i = 0; i < childs.size(); i++) {
                        //这里要判断是否直接是参数?
                        if (childs.get(i) != null) {
                            if (childs.get(i) instanceof JSONArray) {
                                //数据的情况，直接跳过，严格来说ES查询语句不支持嵌套数组([[],[]])的查询语句格式
                                continue;
                            } else if (childs.get(i) instanceof JSONObject) {
                                JSONObject child = childs.getJSONObject(i);
                                JSONObject childJson = JSONObject
                                    .parseObject(replaceparam2(child.toJSONString(), param));
                                if (childJson.keySet().size() != 0) {
                                    childs.set(i, childJson);
                                } else {
                                    childs.set(i, null);
                                }
                            } else {
                                if (childs.get(i).toString().indexOf("${") > -1) {
                                    String paramName = childs.get(i).toString().replace("${", "")
                                        .replace("}", "");
                                    childs.set(i, param.get(paramName));
                                }
                            }
                        }

                    }
                    JSONArray newarray = new JSONArray();
                    for (int j = 0; j < childs.size(); j++) {
                        if (childs.get(j) != null) {
                            newarray.add(childs.get(j));
                        }
                    }
                    if (newarray.size() > 0) {
                        template.put(key, newarray);
                    } else {
                        remvoeAllKeys.add(key);
                    }

                } else {
                    //这里如果 temp 是值类型，判断是否为空，然后要么有参数，要么没有参数，需要判断
                    if (temp.toString().indexOf("${") > -1) {
                        String paramName = temp.toString().replace("${", "").replace("}", "");
                        if (StringUtils.isBlank(param.get(paramName))) {
                            //删除key
                            remvoeAllKeys.add(key);
                            //                           template.remove(key);
                        } else {
                            template.put(key, param.get(paramName));
                        }

                    }

                }

            }
        }
        for (String remvoeKey : remvoeAllKeys) {
            template.remove(remvoeKey);
        }
        return template.toJSONString();
    }

    @Override
    public ESearchResult<List<SyncDataResultModel>> synchroMultiData(RequestContext request,
                                                                     List<Map<String, String>> params) {
        ESearchResult<List<SyncDataResultModel>> result = new ESearchResult<>();
        if (!checkAppInfo(result, request)) {
            return result;
        }
        try {
            String indexName = getIndexName(request);
            Map<String, SyncDataResultModel> allResult = new HashMap<>();
            AppDocTpl docTpl = appDocTplService.getAppDocTpl(request.getAppId(),
                request.getTplCode());
            if(docTpl==null || StringUtils.isBlank(docTpl.getMappingTplContent()) || StringUtils.isBlank(docTpl.getDataTplContent())){
                log.info("批量同步数据==》模板内容不存在，请检查模板编号");
                throw  new Exception("同步数据==》模板内容不存在，请检查模板编号");
            }
            if (!esClient.checkIndexExits(indexName)) {
                log.info("没有获取到索引，需要新建，索引名为：" + indexName);
                esClient.createIndex(indexName);
                esClient.createMapping(indexName,
                    JSONObject.parseObject(docTpl.getMappingTplContent()));
            }
            String tplContent = docTpl.getDataTplContent();

            List<AppDocFields> allFields= appDocTplService.getAllFeild(request.getAppId(),docTpl.getTplCode());
            List<String> paramNames=syncDataParams(allFields);
            List<JSONObject> dataList = new ArrayList<>();
            SyncDataResultModel resultModel;
            for (Map<String, String> param : params) {
                resultModel = new SyncDataResultModel();
                resultModel.setDataId(param.get("doc_source_id"));
                try {
                    JSONObject tplParam=JSONObject.parseObject(tplContent);
                    Set<String> set=tplParam.keySet();
                    String[] pnames=new String[set.size()];
//                    List<String> paramNames= Arrays.asList( tplParam.keySet().toArray(pnames));
                    ;
//                    JSONObject dataJson = JSON.parseObject(replaceparam2(tplContent, param));
                    dataList.add(JSON.parseObject(VelocityUtil.replaceTplByVelocity(tplContent,paramNames,param)));
                    resultModel.setStatus(true);
                } catch (Exception e) {
                    resultModel.setStatus(false);
                    resultModel.setErrorMsg(e.getMessage());
                }
                allResult.put(param.get("doc_source_id"), resultModel);
            }

            String esResult = esClient.bulkDocument(indexName, dataList);
            log.info("批量保存明细数据:" + esResult);
            JSONObject esResultJosn = JSON.parseObject(esResult);
            if (esResultJosn.getBooleanValue("errors")) {
                JSONArray errorDatas = esResultJosn.getJSONArray("items");
                JSONObject errorData;
                for (int i = 0; i < errorDatas.size(); i++) {
                    errorData = errorDatas.getJSONObject(i).getJSONObject("index");
                    JSONObject errorInfo = errorData.getJSONObject("error");
                    if (errorInfo != null) {
                        resultModel = allResult.get(errorData.getString("_id"));
                        resultModel.setStatus(false);
                        resultModel
                            .setErrorMsg(errorInfo.getJSONObject("caused_by").toJSONString());
                    }
                }
            }
            Collection<SyncDataResultModel> allResultList = allResult.values();
            result.setResultObj(new ArrayList<SyncDataResultModel>(allResultList));
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            log.error("批量保存异常：", e);
            result.setSuccess(false);
            result.setErrorCode(ResultMsgCode.syserror.getCode());
            result.setResultObj(null);
            result.setErrorMsg("批量保存系统异常：" + e.getMessage());
            return result;
        }
    }

    private boolean checkAppInfo(ESearchResult result, RequestContext request) {
        if (StringUtils.isBlank(request.getAppId()) || StringUtils.isBlank(request.getAppKey())
            || StringUtils.isBlank(request.getSign())) {
            result.setErrorMsg(ResultMsgCode.paramerror.getCode());
            result.setSuccess(false);
            result.setErrorMsg(String.format("%s%s%s%s%s", ResultMsgCode.paramerror.getDesc(), ":",
                request.getAppId(), request.getAppKey(), request.getSign()));
            return false;
        }
        if (SecretTools.validAppInfo(request.getAppId(), request.getAppKey(), request.getSign())) {
            return true;
        } else {
            result.setSuccess(false);
            result.setErrorMsg(String.format("%s%s%s%s%s", "应用key签名参数错误", ":", request.getAppId(),
                request.getAppKey(), request.getSign()));
            return false;
        }
    }

}
