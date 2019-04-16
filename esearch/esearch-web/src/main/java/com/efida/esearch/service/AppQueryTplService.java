package com.efida.esearch.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efida.esearch.core.LogicType;
import com.efida.esearch.core.SymbolType;
import com.efida.esearch.enmus.DocTplAuditEnmu;
import com.efida.esearch.enmus.QueryTplAuditEnmu;
import com.efida.esearch.exception.EbusinessException;
import com.efida.esearch.mapper.AppQueryTplDetailMapper;
import com.efida.esearch.mapper.AppQueryTplMapper;
import com.efida.esearch.model.AppQueryTpl;
import com.efida.esearch.model.PagingResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@CacheConfig(cacheNames = "appQueryTpl") // 本类内方法指定使用缓存时，默认的名称就是appQueryTpl
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class AppQueryTplService {
    private Logger            log = LoggerFactory
        .getLogger(AppQueryTplService.class.getSimpleName());
    @Autowired
    private AppQueryTplMapper appQueryTplMapper;

    @Autowired
    AppQueryTplDetailMapper   appQueryTplDetailMapper;

    /**
     * 使用前，请确保 类 添加 CacheConfig 注解并且 cacheNames是全局唯一，保证数据不冲突，所以一般以实体名+Cache
     * CachePut(key="#p0") --将数据存入到缓存，一般用于修改和保存方法，修改方法需要将修改后最新数据查询进行方法返回
     * Cacheable(key="#p1") --先查询缓存，再执行方法,一般用于查询方法
     * CacheEvict(key="#p0.id") --删除缓存名称为 类CacheConfig cacheNames 对应缓存块中 指定的id对应的缓存
     * CacheEvict(allEntries = true) 删除缓存名称为 类CacheConfig cacheNames 对应缓存块中 所有缓存
     *
     * #p0 #p1 #p0.id 是方法参数  p0标识方法第一个参数   p1 方法第二个参数   pn.xxx 第n个参数的 xxx 属性 注意一般是该实体的唯一标识，如果是符合组件还待查找方法
     * key="#p0#p1"
     */

    public AppQueryTpl getAppQueryTplByAppDocTplCode(String appId, String docTplCode,
                                                     String tplCode) {
        AppQueryTpl appQueryTpl = appQueryTplMapper.selectAppQueryTplByAappIndexTplCode(appId,
            docTplCode, tplCode);
        return appQueryTpl;
    }

    public AppQueryTpl getAppQueryTplByCode(String tplCode) {
        return appQueryTplMapper.selectByCode(tplCode);
    }

    /**
     * 查询应用查询模板
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param appId
     * @param keyword
     * @param status
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PagingResult<AppQueryTpl> getPagesQueryTemplates(String appId, String keyword,
                                                            String status, int pageNumber,
                                                            int pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(status)) {
            if (status.equals("true")) {
                params.put("isLock", true);
            }
            if (status.equals("false")) {
                params.put("isLock", false);
            }
            if (!(status.equals("false") || status.equals("true"))) {
                params.put("auditStatus", status);
            }
        }
        params.put("keyword", keyword);
        params.put("appId", appId);
        List<AppQueryTpl> appinfos = appQueryTplMapper.getQueryTemplates(params);
        PageInfo<AppQueryTpl> pageInfo = new PageInfo<AppQueryTpl>(appinfos);
        return new PagingResult<AppQueryTpl>(pageInfo.getList(), pageInfo.getTotal(), pageNumber,
            pageSize);
    }

    /**
     * 创建一个查询模板
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param appDocTpl
     * @return
     */
    public AppQueryTpl createDocTpl(AppQueryTpl appDocTpl) {
        assertDocInfo(appDocTpl);
        AppQueryTpl queryTpl = getQueryTplByCode(appDocTpl.getTplCode());
        if (queryTpl != null) {
            throw new EbusinessException("模板编号:" + appDocTpl.getTplCode() + "已被占用");
        }
        appQueryTplMapper.insert(appDocTpl);
        return appDocTpl;
    }

    /**
     * 校验查询模板
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param appDocTpl
     */
    public void assertDocInfo(AppQueryTpl appDocTpl) {
        if (appDocTpl == null) {
            return;
        }
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<AppQueryTpl>> set = validator.validate(appDocTpl);
        for (ConstraintViolation<AppQueryTpl> constraintViolation : set) {
            throw new EbusinessException(constraintViolation.getMessage());
        }
    }

    /**
     * 获取一个查询模板通过编号
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param tplCode
     * @return
     */
    private AppQueryTpl getQueryTplByCode(String tplCode) {
        return appQueryTplMapper.selectByCode(tplCode);
    }

    /**
     * 更新模板
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param appDocTpl
     * @return
     */
    public AppQueryTpl upDocTpl(AppQueryTpl appDocTpl) {
        AppQueryTpl docTpl = getQueryTplByCode(appDocTpl.getTplCode());
        if (docTpl == null) {
            throw new EbusinessException("模板编号:" + appDocTpl.getTplCode() + "数据不存在");
        }
        AppQueryTpl appDocTpl2 = new AppQueryTpl();
        appDocTpl2.setId(docTpl.getId());
        appDocTpl2.setTplCode(appDocTpl.getTplCode());
        appDocTpl2.setTplName(appDocTpl.getTplName());
        appDocTpl2.setDocTplCode(appDocTpl.getDocTplCode());
        appDocTpl2.setTplDesc(appDocTpl.getTplDesc());
        appDocTpl2.setTplContent(appDocTpl.getTplContent());
        appDocTpl2.setParams(appDocTpl.getParams());
        appDocTpl2.setConditionJson(appDocTpl.getConditionJson());
        appQueryTplMapper.updateByPrimaryKeySelective(appDocTpl2);
        return appDocTpl;
    }

    /**
     * 锁定查询模板
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param isLock
     * @return
     */
    public boolean lockTemplate(String tplCode, Boolean isLock) {
        AppQueryTpl docTpl = getQueryTplByCode(tplCode);
        if (docTpl == null) {
            throw new EbusinessException("编号为:" + tplCode + "的模板不存在");
        }
        AppQueryTpl appDocTpl = new AppQueryTpl();
        appDocTpl.setId(docTpl.getId());
        appDocTpl.setIsLock(isLock);
        appQueryTplMapper.updateByPrimaryKeySelective(appDocTpl);
        return true;
    }

    /**
     * 删除查询模板
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param tplCode
     * @return
     */
    public boolean delDocTpl(String tplCode) {
        AppQueryTpl docTpl = getQueryTplByCode(tplCode);
        if (docTpl == null) {
            throw new EbusinessException("编号为:" + tplCode + "数据不存在");
        }
        if (QueryTplAuditEnmu.PASS.getCode().equals(docTpl.getAuditStatus())) {
            throw new EbusinessException("已通过审核模板不允许删除");
        }
        appQueryTplMapper.deleteByPrimaryKey(docTpl.getId());
        return true;
    }

    /**
     * 审核查询模板
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param auditStatus
     * @return
     */
    public boolean auditDocTpl(String tplCode, String auditStatus) {
        AppQueryTpl docTpl = getQueryTplByCode(tplCode);
        if (docTpl == null) {
            throw new EbusinessException("模板编号:" + tplCode + "数据不存在");
        }
        AppQueryTpl appDocTpl2 = new AppQueryTpl();
        appDocTpl2.setId(docTpl.getId());
        appDocTpl2.setAuditStatus(auditStatus);
        if (DocTplAuditEnmu.PASS.getCode().equals(auditStatus)) {
            appDocTpl2.setAuditPassTime(new Date());
        }
        appQueryTplMapper.updateByPrimaryKeySelective(appDocTpl2);
        return true;
    }

    /**
     * 申请查询模板
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param tplCode
     * @return
     */
    public boolean replayAuditDocTpl(String tplCode) {
        return auditDocTpl(tplCode, DocTplAuditEnmu.WAIT_AUDIT.getCode());
    }
    private String simpleConditionAnalize(JSONObject condition) {
        String tempStr = null;
        JSONObject effectJson = condition.getJSONObject("effect");
        String fieldName = condition.getString("fieldName");
        String symbol = condition.getString("symbol");
        String value = condition.getString("value");
        String variable = condition.getString("variable");
        if (symbol.equals(SymbolType.range.getType())) {
            // {"bool": {"must": [{ "range": {"content": {"gte": "","lte": null }}}]} }
            JSONArray selected_vars = condition.getJSONArray("selected_vars");
            String proTemp = null;
            List<String> effects = new ArrayList<>();
            JSONObject pro = selected_vars.getJSONObject(0);
            String pro_var = pro.getString("_var");
            if (pro.getIntValue("val_method") == 0) {
                proTemp = "\"" + pro_var + "\":" + "\"" + pro.getString("value") + "\"";
                //如果有两个，将第二个拼接
                if (selected_vars.size() > 1) {
                    //有两个,处理第二个，由于有逗号，所以必须分开处理....
                    pro = selected_vars.getJSONObject(1);
                    pro_var = pro.getString("_var");
                    if (pro.getIntValue("val_method") == 0) {
                        proTemp += ",\"" + pro_var + "\":" + "\"" + pro.getString("value") + "\"";
                    } else {
                        effectJson = pro.getJSONObject("effect");
                        String effectStr = effect(effectJson, pro.getString("variable"));
                        if (effectStr != null) {
                            effects.add(effectStr);
                            proTemp += effectCondition(effectStr) + ",\"" + pro_var + "\":" + "\"$"
                                       + pro.getString("variable") + "\"#end";
                        } else {
                            proTemp += ",\"" + pro_var + "\":" + "\"" + pro.getString("value")
                                       + "\"";
                        }
                    }
                }
            } else {
                if (selected_vars.size() > 1) {
                    String proTemp2 = null;
                    JSONObject pro2 = selected_vars.getJSONObject(1);
                    String pro_var2 = pro2.getString("_var");
                    if (pro2.getIntValue("val_method") == 0) {
                        proTemp2 = "\"" + pro_var2 + "\":" + "\"" + pro2.getString("value") + "\"";
                        effectJson = pro.getJSONObject("effect");
                        String effectStr = effect(effectJson, pro.getString("variable"));
                        if (effectStr != null) {
                            proTemp = proTemp2 + effectCondition(effectStr) + ",\"" + pro_var
                                      + "\":" + "\"$" + pro.getString("variable") + "\"#end";
                            effects.add(effectStr);
                        } else {
                            proTemp = proTemp2 + ",\"" + pro_var + "\":" + "\"$"
                                      + pro.getString("variable") + "\"#end";
                        }
                    } else {
                        //这一步，肯定两个都是 变量都需要判断是否有生效条件
                        effectJson = pro.getJSONObject("effect");
                        JSONObject effectJson2 = pro2.getJSONObject("effect");
                        String effectStr = effect(effectJson, pro.getString("variable"));
                        String effectStr2 = effect(effectJson2, pro2.getString("variable"));
                        if (effectStr != null) {
                            effects.add(effectStr);
                            if (effectStr2 != null) {
                                effects.add(effectStr2);
                                //两个都有生效条件,判断两个条件都为TRUE，或者其中一个为T 另外一个为 F
                                proTemp = "#if(" + effectStr + " && " + effectStr2 + ") " + "\""
                                          + pro_var + "\":" + "\"$" + pro.getString("variable")
                                          + "\"" + ",\"" + pro_var2 + "\":" + "\"$"
                                          + pro2.getString("variable") + "\"";
                                proTemp += "#elseif(" + effectStr + " && !(" + effectStr2 + "))"
                                           + "\"" + pro_var + "\":" + "\"$"
                                           + pro.getString("variable") + "\"";
                                proTemp += "#elseif(!(" + effectStr + ") && " + effectStr2 + ")"
                                           + "\"" + pro_var2 + "\":" + "\"$"
                                           + pro2.getString("variable") + "\"#end";
                                //最后一种情况在最外层就会过滤掉
                            } else {
                                //第二个没有生效条件,第一个有
                                proTemp = "\"" + pro_var2 + "\":" + "\"$"
                                          + pro2.getString("variable") + "\""
                                          + effectCondition(effectStr) + ",\"" + pro_var + "\":"
                                          + "\"$" + pro.getString("variable") + "\"#end";
                            }
                        } else {
                            //第一个没有生效条件肯等存在，就看第二个是否生效，判断后添加逗号
                            proTemp = "\"" + pro_var + "\":" + "\"$" + pro.getString("variable")
                                      + "\"" + effectCondition(effectStr2) + ",\"" + pro_var2
                                      + "\":" + "\"$" + pro2.getString("variable") + "\"#end";
                        }
                    }
                } else {
                    effectJson = pro.getJSONObject("effect");
                    String effectStr = effect(effectJson, pro.getString("variable"));
                    if (effectStr != null) {
                        proTemp = effectCondition(effectStr) + "\"" + pro_var + "\":" + "\"$"
                                  + pro.getString("variable") + "\"#end";
                        effects.add(effectStr);
                    } else {
                        proTemp = "\"" + pro_var + "\":" + "\"$" + pro.getString("variable")
                                  + "\"#end";
                    }
                }
            }
            if (effects.size() == 1) {
                tempStr = "{ \"bool\":{" + "#if(" + effects.get(0) + " ) \"must\":[{\"range\":{\""
                          + fieldName + "\":{" + proTemp + "}}}]#end}}";
            } else if (effects.size() == 2) {
                tempStr = "{ \"bool\":{" + "#if(" + effects.get(0) + " && " + effects.get(1)
                          + " ) \"must\":[{\"range\":{\"" + fieldName + "\":{" + proTemp
                          + "}}}]#end}}";
            } else {
                tempStr = "{ \"bool\":{" + "\"must\":[{\"range\":{\"" + fieldName + "\":{" + proTemp
                          + "}}}]}}";
            }

        } else if (symbol.equals(SymbolType.exists.getType())
                   || symbol.equals(SymbolType.missing.getType())) {
            tempStr = "{ \"bool\":{\"must\":[{\"" + symbol + "\":{\"field\":\"$" + fieldName + "\"}}]}}";
        } else {
            JSONObject termContent = new JSONObject();
            if (condition.getIntValue("val_method") == 0) {
                tempStr = "{ \"bool\":{\"must\":[{\"" + symbol + "\":{\"" + fieldName + "\":\""
                          + value + "\"}}]}}";
            } else {
                if (effectJson != null && effectJson.size() > 0) {
                    boolean isRelyVar = effectJson.getBooleanValue("isRelyVar");
                    if (isRelyVar) {
                        String effectStr = effect(effectJson, variable);
                        if (effectStr != null) {
                            tempStr = "{ \"bool\":{" + effectCondition(effectStr) + "\"must\":[{\""
                                      + symbol + "\":{\"" + fieldName + "\":\"$" + variable
                                      + "\"}}]#end}}";
                        }
                    }
                }
                if (tempStr == null) {
                    tempStr = "{ \"bool\":{\"must\":[{\"" + symbol + "\":{\"" + fieldName + "\":$"
                              + variable + "}}]}}";
                    ;
                }
            }
        }
        return tempStr;
    }

    private String effectCondition(String condition) {
        return "#if(" + condition + ")";
    }

    private String effect(JSONObject effect, String rely_variable) {
        String tempStr = null;
        if (effect != null && effect.size() > 0) {
            boolean isRelyVar = effect.getBooleanValue("isRelyVar");
            String effectKey = effect.getString("effectKey");
            if (StringUtils.isNotBlank(effect.getString("variable"))) {
                rely_variable = effect.getString("variable");
            }
            if (isRelyVar) {
                if (effectKey.equals("e_empty")) {
                    tempStr = "\"$!" + rely_variable + "\" != \"\"";
                } else {
                    tempStr = "\"$" + rely_variable + "\" == \"" + effect.getString("value") + "\"";
                }
            }
        }
        return tempStr;
    }

    //一定返回 bool: xxx格式
    public String complexConditionAnalize(JSONArray conditionObjList) {
        JSONArray mustConditions = new JSONArray();
        JSONArray mustNotConditions = new JSONArray();
        JSONArray shouldConditions = new JSONArray();
        JSONObject temp;
        temp = conditionObjList.getJSONObject(0);
        mustConditions.add(temp);
        if (conditionObjList.size() > 1) {
            for (int i = 1; i < conditionObjList.size(); i++) {
                temp = conditionObjList.getJSONObject(i);
                if (temp.getString("logic").equals(LogicType.must.getType())) {
                    mustConditions.add(temp);
                } else if (temp.getString("logic").equals(LogicType.must_not.getType())) {
                    mustNotConditions.add(temp);
                } else if (temp.getString("logic").equals(LogicType.should.getType())) {
                    shouldConditions.add(temp);
                }
            }
        }
        //或的查询范围大，所以共同存在的情况，将或的关系作为整体bool处理？还是将其他关系结果一起作为或处理？
        //must_not 理解为不等于，和 must 同级处理，同一个bool
        //目前处理整体作为或处理
        String queryStr = "";
        String must = dealLogic(mustConditions, "must");
        String must_not = dealLogic(mustNotConditions, "must_not");
        String should = dealLogic(shouldConditions, "should");
        //由于must_not 是最后过滤，不是条件判断，所以做最后处理

        //为什么用should,如果should存在和其他肯定是或 的关系，不存在就是and关系
        if (should != null) {
            queryStr = "{\"bool\": {\"should\": [";
            queryStr += should;
            if (must != null)
                queryStr += "," + must;
            if (must_not != null)
                queryStr += "," + must_not;
        } else {
            queryStr = "{\"bool\": {\"must\": [";
            if (must != null) {
                queryStr += must;
                if (must_not != null)
                    queryStr += "," + must_not;
            } else {
                if (must_not != null)
                    queryStr += must_not;
            }
        }
        queryStr += "]}}";
        return queryStr;
    }

    private String dealLogic(JSONArray contitions, String logic) {
        String logicStr = null;
        if (contitions.size() > 0) {
            String tempStr = null;
            JSONObject temp;
            //所有must 只需要  "bool": {"must": [
            for (int i = 0; i < contitions.size(); i++) {
                temp = contitions.getJSONObject(i);
                if (temp.getBooleanValue("is_simple_condition")
                    || temp.getJSONArray("conditionObjList") == null
                    || temp.getJSONArray("conditionObjList").size() == 0) {
                    //简单条件
                    // {"bool": { }}
                    String simpleCondition = simpleConditionAnalize(temp);
                    if (contitions.size() == 1)
                        return simpleCondition;
                    tempStr = (tempStr == null ? "" : tempStr + ",");
                    tempStr += simpleCondition;
                } else {
                    //主界面没有生效条件，  子条件可能有生效条件
                    JSONObject effectJson = temp.getJSONObject("effect");
                    String estr = effect(effectJson, effectJson.getString("variable"));
                    log.info("temp---------conditions:"+temp.toJSONString());
                    log.info("temp---------conditions=----------------:"+temp.getJSONArray("conditionObjList"));
                    //这里只会存在处理主界面的子条件
                    String complexCondition = complexConditionAnalize(
                        temp.getJSONArray("conditionObjList"));
                    if (estr != null) {
                        //有生效条件一定要加一层 boolean must 不然 会存在must体为空
                        tempStr = (tempStr == null ? "" : tempStr + ",");
                        tempStr += "{\"bool\": {" + effectCondition(estr) + "\"must\": ["
                                   + complexCondition + "]#end}}";
                    } else {
                        tempStr = (tempStr == null ? "" : tempStr + ",");
                        tempStr += complexCondition;
                    }
                }
            }
            logicStr = "{\"bool\": {\"" + logic + "\": [";
            logicStr += tempStr;
            logicStr += "]}}";
        }
        return logicStr;
    }

}
