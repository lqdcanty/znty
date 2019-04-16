package com.efida.esearch.facade.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efida.esearch.facade.constains.ESortable;
import com.efida.esearch.facade.model.ESPageModel;
import com.efida.esearch.facade.model.RequestContext;
import com.efida.esearch.facade.model.SyncDataResultModel;
import com.efida.esearch.facade.results.ESearchResult;

import java.util.List;
import java.util.Map;

/**
 * esearch 开放dubbo查询接口
 * @author tping
 *
 */
public interface ESearchFacade {
    /**
     * 通过默认模板查询数据
     * @param request 请求上下文信息
     * @param param 请求模板中对应的参数信息
     * @param page 页数
     * @param pageSize  每页条数 限制 最大2000条
     * @return isSuccess() 成功还是失败 成功可以获取数据分页信息 {@link ESPageModel}
     */
    public ESearchResult<ESPageModel> queryByTemplate(RequestContext request, Map<String, String> param , long page, int pageSize, ESortable eSortable,String sortField);

    /**
     * 
     * 不推荐使用该方法。 
     * 
     * 原生 elasticsearch查询 支持json 格式。
     * 
     * elasticsearch查询 原生语法结构查询数据 es:版本6.4
     * 原生语法使用参考ES官网资料
     * @param request 请求上下文信息 
     * @param queryJson  查询对象
     * @return isSuccess() 成功还是失败 成功可以获取数据分页信息 {@link ESPageModel}
     */
//    public ESearchResult<ESPageModel> queryByES(RequestContext request,JSONObject queryJson);

    /**
     *  
     * 单条数据同步保存，根据已经定义的mapping中指定的 业务数据ID进行新增或者修改
                  同步返回处理结果 SyncDataResultModel 包含该条数据处理状态成功失败，业务数据标识，以及处理异常信息
      
     * 
     * @param request  请求上下文信息
     * @param dataId 数据唯一标识
     * @param param  请求存入数据信息
     * @return
     */
    public ESearchResult<String> synchroOneData(RequestContext request, String dataId, Map<String, String> param);

    /**
     * 批量数据同步保存，根据已经定义的mapping中指定的 业务数据ID进行新增或者修改
     * 
     * @param request 请求上下文信息
     * @param params 请求模板中对应的参数信息, param中必须包含_id参数
     * @param params
     * @return 同步返回处理结果 List<SyncDataResultModel> 包含所有传输数据处理状态成功还是失败，业务数据标识，以及处理异常信息
     */
    public ESearchResult<List<SyncDataResultModel>> synchroMultiData(RequestContext request, List<Map<String,String>> params);

    /**
     * 删除es中的数据
     * @param request 请求上下文信息
     * @param dataId  即将删除数据对应唯一标识
     * @return 处理结果 isSuccess() 成功还是失败
     */
    public ESearchResult<String> deleteByDataId(RequestContext request,String dataId);
}
