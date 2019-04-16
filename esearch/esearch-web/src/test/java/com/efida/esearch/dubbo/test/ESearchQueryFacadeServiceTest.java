package com.efida.esearch.dubbo.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efida.esearch.dubbo.ESearchQueryFacadeService;
import com.efida.esearch.facade.constains.ESortable;
import com.efida.esearch.facade.model.ESPageModel;
import com.efida.esearch.facade.model.RequestContext;
import com.efida.esearch.facade.model.SyncDataResultModel;
import com.efida.esearch.facade.results.ESearchResult;
import com.efida.esearch.test.BaseTest;
import com.efida.esearch.utils.SecretTools;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ESearchQueryFacadeServiceTest extends BaseTest {

    private @Autowired
    ESearchQueryFacadeService esearchQueryFacadeService;

    @Test
    public void appKey(){
        System.out.println(SecretTools.generateAppKey());
    }
    @Test
    public void appSecret(){
        System.out.println(SecretTools.generateAppSecret("znty_sof","4d59fdd4f7f24dbab64795563476281d"));
    }

    @Test
    public void queryByTemplate() {
        RequestContext requestContext=getRequestContext();
        requestContext.setTplCode("queryby_keyword_type");

        Map<String,String> params=new HashMap<>();
        params.put("keyword","");
        params.put("type","");

       ESearchResult<ESPageModel> result= esearchQueryFacadeService.queryByTemplate(requestContext,params,1,10, ESortable.EASC,"create_time");
        if(result.isSuccess()){
            ESPageModel pageResult=  result.getResultObj();
            System.out.println("总条数:"+pageResult.getAllRow());
            System.out.println("总页数:"+pageResult.getTotalPage());
            List<String> datas=pageResult.getList();
            for (String s : datas) {
                System.out.println("当前页数据:"+s);
            }

        }else{
            System.out.println("查询结果异常:"+result.getErrorMsg()+"=="+result.getErrorInfo());
        }
    }

    @Test
    public void queryByES() {
        RequestContext requestContext=getRequestContext();
        requestContext.setTplCode("article_search");

        JSONObject queryJosn= JSON.parseObject("{\"size\":10,\"query\":{\"bool\":{\"must\":[{\"bool\":{\"should\":[{\"match\":{\"content\":\"哈哈\"}},{\"match\":{\"title\":\"哈哈\"}}]}}]}},\"from\":0}");

        ESearchResult<ESPageModel> result= esearchQueryFacadeService.queryByES(requestContext,queryJosn);
        if(result.isSuccess()){
            ESPageModel pageResult=  result.getResultObj();
            System.out.println("总条数:"+pageResult.getAllRow());
            System.out.println("总页数:"+pageResult.getTotalPage());
            List<String> datas=pageResult.getList();
            for (String s : datas) {
                System.out.println("当前页数据:"+s);
            }

        }else{
            System.out.println("查询结果异常:"+result.getErrorMsg());
        }
    }

    @Test
    public void synchroOneData() {
        RequestContext requestContext=getRequestContext();
        requestContext.setTplCode("article");

          /*"doc_source_id":"1",
                "register":"fbrid0001",
                "register_name":"发表人",
                "title":"这个标题很神奇",
                "content":"这篇文章是说有那个啥哈哈哈啥",
                "comment":"这是一个奇怪的评论",
                "createDate":"2018-09-11 12:12:12",
                "type":"i don't no!",
                "delFlag":false*/

//        {doc_source_id=5eeff523a5fa475caed0afd3beb5a05e, register_name=null, comment=, title=null, type=dynamic, delFlag=false, content=世界这么大我想去看看, register=2791618185512960, createDate=2018-10-09 14:12:55}

//        {"doc_source_id":"676ccc1211f54004a98dbe30f7a32d14","register_name":null,"comment":"","title":"给个电话","type":"article","delFlag":"false","content":"<p dir="ltr">比较符合img src="c="http://devfdfs.zntyydh.com/group1/M00/00/03/CkAillu7IOuAbZppAAYrOldNTz8387.jpg">比较">比较符合img src="c="http://devfdfs.zntyydh.com/group1/M00/00/03/CkAillu7IOuAbZppAAYrOldNTz8387.jpg"></"></p > ","register":"2791618185512960","createDate":"2018-10-09 21:54:01"}

        Map<String,String> param=new HashMap<>();
        param.put("doc_source_id","1");
        param.put("editor","张三");
        param.put("content","演示测试单条内容");
        param.put("title",null);
//        param.put("createDate","2018-09-11 我要报错");
        param.put("create_time","2018-10-09 14:12:55");
        param.put("type","dynamic");

        ESearchResult<String> result= esearchQueryFacadeService.synchroOneData(requestContext,"1",param);
        if(result.isSuccess()){
            System.out.println("ID为:"+result.getResultObj());

        }else{
            System.out.println("保存结果异常:"+result.getErrorMsg());
        }
    }

    @Test
    public void deleteByDataId() {
        RequestContext requestContext=getRequestContext();
        requestContext.setTplCode("article");
        ESearchResult<String> result= esearchQueryFacadeService.deleteByDataId(requestContext,"8");
        if(result.isSuccess()){
            System.out.println("删除数据的ID为:"+result.getResultObj());
        }else{
            System.out.println("删除数据异常:"+result.getErrorMsg());
        }
    }

    @Test
    public void synchroMultiData() {
        RequestContext requestContext=getRequestContext();
        requestContext.setTplCode("article");
        List<Map<String,String>> datas=new ArrayList<>();
        Map<String,String> param=new HashMap<>();
        param.put("doc_source_id","2");
        param.put("editor","李四");
        param.put("content","文化传媒");
        param.put("title","北京新文化传媒公司");
//        param.put("createDate","2018-09-11 我要报错");
        param.put("create_time","2018-10-08 14:12:55");
        param.put("type","dynamic");
        datas.add(param);
         param=new HashMap<>();
        param.put("doc_source_id","3");
        param.put("editor","王五");
        param.put("content","报错的世界");
        param.put("title","错吧，不是错");
        param.put("create_time","2018-09-7 我要报错");
        param.put("type","dynamic");
        datas.add(param);

        param=new HashMap<>();
        param.put("doc_source_id","4");
        param.put("editor","金庸");
        param.put("content","武侠世界");
        param.put("title","中国文化博大精深");
//        param.put("createDate","2018-09-11 我要报错");
        param.put("create_time","2018-10-06 14:12:55");
        param.put("type","dynamic");
        datas.add(param);

        ESearchResult<List<SyncDataResultModel>> result= esearchQueryFacadeService.synchroMultiData(requestContext,datas);
        if(result.isSuccess()){
            List<SyncDataResultModel> resultModels=result.getResultObj();
            for (SyncDataResultModel resultModel : resultModels) {
                System.out.println(String.format("数据ID：%s 保存状态：%s 信息：%s",resultModel.getDataId(),resultModel.getStatus(),resultModel.getErrorMsg()));
            }
        }else{
            System.out.println("批量保存数据异常:"+result.getErrorMsg());
        }
    }

    private RequestContext getRequestContext(){
        RequestContext requestContext=new RequestContext();
        requestContext.setAppId("app123");
        requestContext.setAppKey("eb6dbb0074b64e5896dd68bbb8f6f7d8");
        requestContext.setSign("672ef74872cfc0e485b14a0b9454ac9e");
        return requestContext;
    }
}