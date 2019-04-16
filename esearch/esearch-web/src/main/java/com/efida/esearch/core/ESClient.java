/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.efida.esearch.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * ES 操作客户端工具封装
 * @author tangp
 * @desc 
 * @version $Id: ESClient.java, v 0.1 2018年9月18日 下午2:34:27 $
 */
@Component
@ConfigurationProperties(prefix = "es")
public class ESClient {
    private List<Map<String, String>> hosts;

    private List<Map<String, String>> headers;

    private HttpHost[]                httpHosts;

    private Integer                   maxRetryTimeoutMillis;

    private Header[]                  defaultHeaders;

    private static final String       defaultCharset = "utf-8";

    private Logger                    log            = LoggerFactory
        .getLogger(ESClient.class.getSimpleName());

    @PostConstruct
    private void init() {
        List<HttpHost> httpHostList = new ArrayList<HttpHost>();
        for (Map<String, String> map : hosts) {
            log.info("ES初始化IP和端口===>>{}:{}",map.get("host"),Integer.parseInt(map.get("port")));
            httpHostList
                .add(new HttpHost(map.get("host"), Integer.parseInt(map.get("port")), "http"));
        }
        httpHosts = new HttpHost[httpHostList.size()];
        httpHostList.toArray(httpHosts);
        if (maxRetryTimeoutMillis == null || maxRetryTimeoutMillis == 0) {
            //默认1000ms
            maxRetryTimeoutMillis = 60000;
        }
        if (headers != null && headers.size() > 0) {
            defaultHeaders = new Header[headers.size()];
            List<Header> defaultHeaderList = new ArrayList<Header>();
            for (Map<String, String> header : headers) {
                defaultHeaderList.add(new BasicHeader(header.get("name"), header.get("value")));
            }
            defaultHeaderList.toArray(defaultHeaders);
        }
        log.info("init esclient success");
    }

    /**
     * 创建索引库
     * @param indexName
     */
    public void createIndex(String indexName) {
        RestClient restClient = null;
        try {
            if(checkIndexExits(indexName)) return ;
            String resultStr = null;
            restClient = getClient();
            Request request=new Request("PUT","/" + indexName);

            Response resp= restClient.performRequest(request);
//            Response resp = restClient.performRequest("PUT", "/" + indexName);
            resultStr = EntityUtils.toString(resp.getEntity());
            if (StringUtils.isBlank(resultStr)) {
                throw new RuntimeException();
            }
            JSONObject resultObj = JSON.parseObject(resultStr);

            log.info("create index {} success  result:\n{} ", indexName, resultStr);
        } catch (Exception e) {
            log.error("es error details:"+e.getMessage());
            String[] errors=e.getMessage().split("\\n");
            if(errors.length>1){
                JSONObject errorInfo=JSON.parseObject(errors[1]).getJSONObject("error");
                if(errorInfo!=null)
                    throw new RuntimeException(
                            "error_type==>>" + errorInfo.getString("type")+";error_reason==>>"+errorInfo.getString("reason")+";error_source==>>"+errorInfo.getJSONObject("caused_by").getString("reason"));
            }
            throw new RuntimeException(e.getMessage());
        } finally {
            if (restClient != null) {
                try {
                    restClient.close();
                } catch (IOException e) {
                    log.error("close client encounter an exception", e);
                }
            }
        }
    }

    public JSONObject getSortConditionJson(String fieldName,String sort){
        JSONObject order=new JSONObject();
        if(StringUtils.isNotBlank(sort))
            order.put("order",sort);
        else
            order.put("order","desc");
        JSONObject sortFiled=new JSONObject();
        if(StringUtils.isNotBlank(fieldName))
            sortFiled.put(fieldName,order);
        else
            sortFiled.put("_score",order);
        return sortFiled;
    }

    /**
     * 检查索引库是否存在
     * @param indexName
     * @return
     * @throws Exception
     */
    public boolean checkIndexExits(String indexName) throws  Exception{

            RestClient restClient = getClient();
        Response resp=null;
        Request checkRequest = new Request("GET", "/_cat/indices");
            try{
                resp = restClient.performRequest(checkRequest);
            } catch (IOException e) {
                log.info("检查索引异常"+e.getMessage());
                return false;
            }
            String resultStr = null;
            if (resp!=null && resp.getEntity() != null) {
                resultStr = EntityUtils.toString(resp.getEntity());
                if (StringUtils.isBlank(resultStr)) {
                    throw new RuntimeException("请求无响应");
                }

                String[] allIndexInfo=resultStr.split("\\n");
                for (String s : allIndexInfo) {
                    String[] perIndex=s.split("\\s+");
                    String indexNameTemp=perIndex[2];

                    if(indexNameTemp.equals(indexName)){
                        return true;
                    }
                }
                return false;
            } else {
                return false;
            }
    }

    /**
     * 创建 ES索引库 map 结构
     * @param indexName
     * @param mapping
     */
    public void createMapping(String indexName, JSONObject mapping) {
        RestClient restClient = null;
        try {
            String resultStr = null;
            restClient = getClient();
            String endpoint = "/" + indexName+"/doc/_mapping";
            if(!checkIndexExits(indexName)) {
                log.info("没有获取到索引，需要新建，索引名为："+indexName);
                createIndex(indexName);
            }
            log.info("索引【{}】存在直接创建mapping：",indexName);
            Request request=new Request("PUT",endpoint);
            request.setJsonEntity(mapping.toJSONString());
            Response resp=restClient.performRequest(request);
                resultStr = EntityUtils.toString(resp.getEntity());
                if (StringUtils.isBlank(resultStr)) {
                    throw new RuntimeException();
                }
                log.info("es operate result:{}",resultStr);
                JSONObject resultObj = JSON.parseObject(resultStr);
                if (resultObj.getString("acknowledged") == null
                        || !resultObj.getString("acknowledged").equals("true")) {
                    throw new RuntimeException(
                            "create index " + indexName + " failed result:" + resultStr);
                }

            log.info("create index {}  success  result:\n{} ", indexName, resultStr);
        } catch (IOException e) {
            log.error("es error details:"+e.getMessage());
            String[] errors=e.getMessage().split("\\n");
            if(errors.length>1){
                JSONObject errorInfo=JSON.parseObject(errors[1]).getJSONObject("error");
                if(errorInfo!=null)
                    throw new RuntimeException(
                            "error_type==>>" + errorInfo.getString("type")+";error_reason==>>"+errorInfo.getString("reason")+";error_source==>>"+errorInfo.getJSONObject("caused_by").getString("reason"));
            }
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        } finally {
            if (restClient != null) {
                try {
                    restClient.close();
                } catch (IOException e) {
                    log.error("close client encounter an exception", e);
                }
            }
        }

    }

    /**
     * 指定索引库 并且指定文档ID进行添加或者修改
     * @param indexName
     * @param docId
     * @param saveDataJson 单条数据记录 json格式数据
     * @return
     */
    public JSONObject upinsertDocument(String indexName, String docId,
                                  JSONObject saveDataJson) {
        RestClient restClient = null;
        try {

            String resultStr = null;
            restClient = getClient();
            String endpoint = String.format("/%s/doc/%s", indexName,  docId);
            Request addDocRequest=new Request("POST",endpoint);
            addDocRequest.setJsonEntity(saveDataJson.toJSONString());
            Response resp = restClient.performRequest(addDocRequest);
            resultStr = EntityUtils.toString(resp.getEntity());
            if (StringUtils.isBlank(resultStr)) {
                throw new RuntimeException("result is blank");
            }
            JSONObject resultObj = JSON.parseObject(resultStr);
            log.info("index document index {} id {} success  result:\n{} ", indexName, docId, resultStr);
            return resultObj;
        } catch (Exception e) {
            log.error("es error details:"+e.getMessage());
            String[] errors=e.getMessage().split("\\n");
            if(errors.length>1){
                JSONObject errorInfo=JSON.parseObject(errors[1]).getJSONObject("error");
                if(errorInfo!=null)
                    throw new RuntimeException(
                            "error_type==>>" + errorInfo.getString("type")+";error_reason==>>"+errorInfo.getString("reason")+";error_source==>>"+errorInfo.getJSONObject("caused_by").getString("reason"));
            }
            throw new RuntimeException(e.getMessage());

        }finally {
            if (restClient != null) {
                try {
                    restClient.close();
                } catch (IOException e) {
                    log.error("close client encounter an exception", e);
                }
            }
        }
    }

    /**
     * 原生ES _bulkDocument 数据导入
     * 由于访问权限很大，该访问暂时设置private 暂时不对外开放
     * @param allDataJosnStr
     * @return
     */
    private JSONObject bulkDocument(String allDataJosnStr) {
        RestClient restClient = null;
        try {
            String resultStr = null;
            restClient = getClient();
            final String endpoint = "/_bulk";

            Request bulkDocRequest=new Request("POST",endpoint);
            bulkDocRequest.setJsonEntity(allDataJosnStr);
            Response resp = restClient.performRequest(bulkDocRequest);
            resultStr = EntityUtils.toString(resp.getEntity());
            if (StringUtils.isBlank(resultStr)) {
                throw new RuntimeException();
            }
            log.info("批量处理结果信息：{}",resultStr);
            return JSON.parseObject(resultStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (restClient != null) {
                try {
                    restClient.close();
                } catch (IOException e) {
                    log.error("close client encounter an exception", e);
                }
            }
        }
    }

    /**
     * 异步批量存储数据
     * @param dataObjs
     */
    public void asyncBulkDocument(List<JSONObject> dataObjs,String uniqueKeyName) {
        RestClient restClient = null;
        try {
            restClient = getClient();
            final String endpoint = "/_bulk";
            String postData = buildIndexBulkData(dataObjs,uniqueKeyName);

            Request bulkDocRequest=new Request("POST",endpoint);
            bulkDocRequest.setJsonEntity(postData);
            restClient.performRequestAsync(bulkDocRequest, new ResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    try {
                        String result = EntityUtils.toString(response.getEntity());
                        JSONObject resultObj = JSON.parseObject(result);
                        if (resultObj.getBooleanValue("errors")) {
                            log.error("");
                        }
                    } catch (Exception e) {
                        log.error("", e);
                    }
                }
                @Override
                public void onFailure(Exception exception) {
                    log.error("req {} failed exception {}", endpoint, exception);
                }
            });

        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 同步批量存储数据,唯一字段值不存在将不会进行处理，直接跳过处理
     * @param indexName  ES索引库名称
     * @param saveDataList 数据表JSON格式数据
     * @return
     */
    public String bulkDocument(String indexName,  List<JSONObject> saveDataList) {
        RestClient restClient = null;
        try {
            String resultStr = null;
            restClient = getClient();
            String endpoint = "/" + indexName+"/doc";
            endpoint += "/_bulk";
            String buf = buildIndexBulkData(saveDataList,"doc_source_id");
            Request bulkDocRequest=new Request("POST",endpoint);
            bulkDocRequest.setJsonEntity(buf);
            Response resp = restClient.performRequest(bulkDocRequest);
            resultStr = EntityUtils.toString(resp.getEntity());
            if (StringUtils.isBlank(resultStr)) {
                throw new RuntimeException();
            }
            return resultStr;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (restClient != null) {
                try {
                    restClient.close();
                } catch (IOException e) {
                    log.error("close client encounter an exception", e);
                }
            }
        }
    }

    /**
     * 根据 docId,删除 指定索引库中某个表中 一条ES文档记录
     * @param indexName
     * @param docId
     * @return
     */
    public JSONObject deleteDocument(String indexName,  String docId) {
        RestClient restClient = null;
        try {
            String resultStr = null;
            restClient = getClient();
            final String endpoint = String.format("/%s/doc/%s", indexName,  docId);

            Request delDocRequest=new Request("DELETE",endpoint);
            Response resp = restClient.performRequest(delDocRequest);
            resultStr = EntityUtils.toString(resp.getEntity());
            if (StringUtils.isBlank(resultStr)) {
                throw new RuntimeException();
            }
            JSONObject resultObj = JSON.parseObject(resultStr);
            log.info("delete document index {} id {} success  result:\n{} ", indexName,
                    docId, resultStr);
            return resultObj;
        } catch (IOException e) {
            log.error("es error details:"+e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            if (restClient != null) {
                try {
                    restClient.close();
                } catch (IOException e) {
                    log.error("close client encounter an exception", e);
                }
            }
        }
    }

    public String query(String indexName, String queryStatment) {
        if (StringUtils.isBlank(indexName)) {
            throw new RuntimeException("index cannot be null or blank");
        }
        String endPoint = indexName + "/doc/_search";
        RestClient restClient = null;
        try {
            String resultStr = null;
            restClient = getClient();

            Request delDocRequest=new Request("POST",endPoint);
            delDocRequest.setJsonEntity(queryStatment);
            Response resp = restClient.performRequest(delDocRequest);
            resultStr = EntityUtils.toString(resp.getEntity());
            if (StringUtils.isBlank(resultStr)) {
                throw new RuntimeException();
            }
            JSONObject resultObj = JSON.parseObject(resultStr);
            log.info("query document index {} success  result:\n{} ", indexName,"查询结果就不全显示了");
            return resultStr;
        } catch (IOException e) {
            log.error("es error details:"+e.getMessage());
            String[] errors=e.getMessage().split("\\n");
            if(errors.length>1){
                JSONObject errorInfo=JSON.parseObject(errors[1]).getJSONObject("error");
                if(errorInfo!=null)
                    throw new RuntimeException(
                            "error_type==>>" + errorInfo.getString("type")+";error_reason==>>"+errorInfo.getString("reason")+";error_source==>>"+errorInfo.getJSONObject("caused_by").getString("reason"));
            }
            throw new RuntimeException(e.getMessage());
        } finally {
            if (restClient != null) {
                try {
                    restClient.close();
                } catch (IOException e) {
                    log.error("close client encounter an exception", e);
                }
            }
        }
    }

    /**
     * 该方法用于对批量操作保存文档进行_bulk 提交数据进行格式封装
     *
     * 考虑公用简化所以做如下使用说明：
     * 该方法只针对 默认 _index/_type/_bulk 路径做数据封装，meta 信息只会封装指定 _id  ,不开放批量数据中独立设置 _type 和 _index
     * 该方法只针对 index 做upsert 固化操作，新增和修改，删除请单独使用其他接口
     * @param dataObjs  数据表json格式数据
     * @param  uniqueKeyName 唯一标识字段名称
     * @return
     */
    private String buildIndexBulkData(List<JSONObject> dataObjs,String uniqueKeyName) {
        StringBuffer buf = new StringBuffer();
        JSONObject dataObj;
        JSONObject metaObj;
        JSONObject _id;
        String uniqueId;
        for (int i = 0; i < dataObjs.size(); i++) {
             dataObj = dataObjs.get(i);
             metaObj = new JSONObject();
             _id=new JSONObject();
             uniqueId=dataObj.getString(uniqueKeyName);
             if(StringUtils.isBlank(uniqueId)) continue;
            _id.put("_id",uniqueId);
            metaObj.put("index",_id);
            buf.append(metaObj.toJSONString()).append("\n");
            buf.append(dataObj.toJSONString()).append("\n");
        }
        return buf.toString();
    }

    /**
     * 创建ES 连接
     * @return
     */
    private RestClient getClient() {

       /* return  RestClient.builder(
                new HttpHost("127.0.0.1", 9200, "http")).build();*/
        return RestClient.builder(httpHosts).setMaxRetryTimeoutMillis(maxRetryTimeoutMillis)
            .setMaxRetryTimeoutMillis(maxRetryTimeoutMillis).setDefaultHeaders(defaultHeaders)
            .build();
    }

    /**
     * Getter method for property <tt>hosts</tt>.
     * 
     * @return property value of hosts
     */
    public List<Map<String, String>> getHosts() {
        return hosts;
    }

    /**
     * Setter method for property <tt>hosts</tt>.
     * 
     * @param hosts value to be assigned to property hosts
     */
    public void setHosts(List<Map<String, String>> hosts) {
        this.hosts = hosts;
    }

    /**
     * Getter method for property <tt>headers</tt>.
     * 
     * @return property value of headers
     */
    public List<Map<String, String>> getHeaders() {
        return headers;
    }

    /**
     * Setter method for property <tt>headers</tt>.
     * 
     * @param headers value to be assigned to property headers
     */
    public void setHeaders(List<Map<String, String>> headers) {
        this.headers = headers;
    }
}
