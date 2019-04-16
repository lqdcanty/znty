package com.efida.esearch.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.efida.esearch.enmus.DocTplAuditEnmu;
import com.efida.esearch.exception.EbusinessException;
import com.efida.esearch.mapper.AppDocFieldsMapper;
import com.efida.esearch.mapper.AppDocTplMapper;
import com.efida.esearch.model.AppDocFields;
import com.efida.esearch.model.AppDocTpl;
import com.efida.esearch.model.AppDocTplDto;
import com.efida.esearch.model.PagingResult;
import com.efida.esearch.utils.DiyBeanUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@CacheConfig(cacheNames = "appDocTpl") // 本类内方法指定使用缓存时，默认的名称就是appDocTpl
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class AppDocTplService {

    private Logger             logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppDocFieldsMapper appDocFieldsMapper;

    @Autowired
    private AppDocTplMapper    appDocTplMapper;

    /**
     * 使用前，请确保 类 添加 CacheConfig 注解并且 cacheNames是全局唯一，保证数据不冲突，所以一般以实体名+Cache
     * CachePut(key="#p0") --将数据存入到缓存，一般用于修改和保存方法，修改方法需要将修改后最新数据查询进行方法返回
     * Cacheable(key="#p1") --先查询缓存，再执行方法,一般用于查询方法
     * CacheEvict(key="#p0.id") --删除缓存名称为 类CacheConfig cacheNames 对应缓存块中 指定的id对应的缓存
     * CacheEvict(allEntries = true) 删除缓存名称为 类CacheConfig cacheNames 对应缓存块中 所有缓存
     * #p0 #p1 #p0.id 是方法参数  p0标识方法第一个参数   p1 方法第二个参数   pn.xxx 第n个参数的 xxx 属性 注意一般是该实体的唯一标识，如果是符合组件还待查找方法
     * key="#p0#p1"
     */

    /**
     * 根据应用和表(文档)编码获取表(文档)字段
     * @param appId
     * @param docCode
     * @return
     */
    public List<AppDocFields> getAllFeild(String appId, String docCode) {
        return appDocFieldsMapper.selectByAppIdAndDocCode(appId, docCode);
    }

    /**
     * 根据应用ID和模板编码获取 模板信息
     * @param appId
     * @param docTplCode
     * @return
     */
    public AppDocTpl getAppDocTpl(String appId, String docTplCode) {
        AppDocTpl docTpl = appDocTplMapper.selectByAppIdAndDocTplCode(appId, docTplCode);
        return docTpl;
    }

    /**
     * 根据应用ID和 和 文档类型名称获取 自定义表结构字段 生成ES mapping 结构 和数据保存模板，该方法只是生成不会保存
     * @param appId
     * @param docCode
     * @return
     */
    public AppDocTpl generateDocTpl(String appId, String docCode) {
        List<AppDocFields> docFields = getAllFeild(appId, docCode);
        JSONObject esMappingJson = new JSONObject();
        JSONObject properties = new JSONObject();
        JSONObject fieldConf = new JSONObject();
        JSONObject esDataJson = new JSONObject();
        for (AppDocFields docField : docFields) {
            if (StringUtils.isNotBlank(docField.getExtendJsonInfo())) {
                fieldConf = JSONObject.parseObject(docField.getExtendJsonInfo());
                if(StringUtils.isNotBlank(fieldConf.getString("index_prefixes"))){
                    String[] startAndMin=fieldConf.getString("index_prefixes").split("-");
                   JSONObject index_prefixes=new JSONObject();
                    index_prefixes.put("min_chars",startAndMin[0]);
                    index_prefixes.put("max_chars",startAndMin[1]);
                    fieldConf.put("index_prefixes",index_prefixes);
                }
                fieldConf.put("type", docField.getFieldType());
            } else {
                fieldConf = new JSONObject();
                fieldConf.put("type", docField.getFieldType());
            }



            properties.put(docField.getFieldName(), fieldConf);
            esDataJson.put(docField.getFieldName(), "$" + docField.getFieldName()+ "");
        }
        esMappingJson.put("properties", properties);

        AppDocTpl docTpl = new AppDocTpl();
        docTpl.setMappingTplContent(esMappingJson.toJSONString());
        docTpl.setDataTplContent(esDataJson.toJSONString());
        return docTpl;
    }

    /**
     * 根据应用ID 和 文档类型名称 保存 生成的 ES  数据存储结构模板字符窜 以及 数据保存模板
      * @param docTpl
     */
    public void saveDocMappingTplAndDataTpl(AppDocTpl docTpl) {
        AppDocTpl genresult = generateDocTpl(docTpl.getAppId(), docTpl.getDocCode());
        AppDocTpl old = getAppDocTpl(docTpl.getAppId(), docTpl.getTplCode());

        docTpl.setMappingTplContent(genresult.getMappingTplContent());
        docTpl.setDataTplContent(genresult.getDataTplContent());
        if (old == null) {
            appDocTplMapper.insertSelective(docTpl);
        } else {
            DiyBeanUtils.copyNotNullProperties(docTpl, old);
            appDocTplMapper.updateByPrimaryKeySelective(old);
        }

    }

    /**
     * 获取应用模板
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param appId
     * @param keyword
     * @param status
     * @return
     */
    public PagingResult<AppDocTplDto> getPagesAppTemplates(String appId, String keyword,
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
        List<AppDocTplDto> appTempales = appDocTplMapper.selectAppTemplatesParams(params);
        PageInfo<AppDocTplDto> pageInfo = new PageInfo<AppDocTplDto>(appTempales);
        return new PagingResult<AppDocTplDto>(pageInfo.getList(), pageInfo.getTotal(), pageNumber,
            pageSize);
    }

    /**
     * 锁定模板
     * @title
     * @methodName
     * @author wangyi
     * @param isLock
    * @param tplCode
     * @description
     * @return
     */
    public boolean lockTemplate(String appId, String tplCode, boolean isLock) {
        AppDocTpl docTpl = getAppDocTpl(appId, tplCode);
        if (docTpl == null) {
            throw new EbusinessException("编号为:" + tplCode + "的模板不存在");
        }
        AppDocTpl appDocTpl = new AppDocTpl();
        appDocTpl.setId(docTpl.getId());
        appDocTpl.setIsLock(isLock);
        appDocTplMapper.updateByPrimaryKeySelective(appDocTpl);
        return true;
    }

    /**
     * 删除一个模板
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @return
     */
    public boolean delDocTpl(String appId, String tplCode) {
        AppDocTpl docTpl = getAppDocTpl(appId, tplCode);
        if (docTpl == null) {
            throw new EbusinessException("编号为:" + tplCode + "的模板不存在");
        }
        //
        if (DocTplAuditEnmu.PASS.getCode().equals(docTpl.getAuditStatus())) {
        throw new EbusinessException("审批通过模板不允许删除");
    }
    int re = appDocTplMapper.deleteByPrimaryKey(docTpl.getId());
        if (re > 0) {
        return true;
    }
        return false;
}

    /**
     * 创建一个模板
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param appDocTpl
     * @return
     */
    public AppDocTpl createDocTpl(AppDocTpl appDocTpl) {
        assertDocInfo(appDocTpl);
        AppDocTpl docTpl = getAppDocTpl(appDocTpl.getAppId(), appDocTpl.getTplCode());
        AppDocTpl tpl=generateDocTpl(appDocTpl.getAppId(),appDocTpl.getTplCode());
        if(tpl!=null && docTpl!=null){
            docTpl.setDataTplContent(tpl.getDataTplContent());
            docTpl.setMappingTplContent(tpl.getMappingTplContent());
        }
        if (docTpl != null) {
            throw new EbusinessException("模板编号:" + appDocTpl.getTplCode() + "已被占用");
        }
        appDocTplMapper.insert(appDocTpl);
        return appDocTpl;
    }

    /**
     * 校验存储模板
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param appDocTpl
     */
    public void assertDocInfo(AppDocTpl appDocTpl) {
        if (appDocTpl == null) {
            return;
        }
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<AppDocTpl>> set = validator.validate(appDocTpl);
        for (ConstraintViolation<AppDocTpl> constraintViolation : set) {
            throw new EbusinessException(constraintViolation.getMessage());
        }
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
    public AppDocTpl upDocTpl(AppDocTpl appDocTpl) {
        AppDocTpl docTpl = getAppDocTpl(appDocTpl.getAppId(), appDocTpl.getTplCode());
        if (docTpl == null) {
            throw new EbusinessException("模板编号:" + appDocTpl.getTplCode() + "数据不存在");
        }
        if (DocTplAuditEnmu.PASS.getCode().equals(docTpl.getAuditStatus())) {
            throw new EbusinessException("已通过模板不可修改哦");
        }
        AppDocTpl appDocTpl2 = new AppDocTpl();
        appDocTpl2.setId(docTpl.getId());
        appDocTpl2.setTplName(appDocTpl.getTplName());
        appDocTpl2.setForecastDataSize(appDocTpl.getForecastDataSize());
        appDocTpl2.setTplDesc(appDocTpl.getTplDesc());
        AppDocTpl tpl=generateDocTpl(appDocTpl.getAppId(),appDocTpl.getTplCode());
        if(tpl!=null){
            appDocTpl2.setDataTplContent(tpl.getDataTplContent());
            appDocTpl2.setMappingTplContent(tpl.getMappingTplContent());
        }

        appDocTplMapper.updateByPrimaryKeySelective(appDocTpl2);
        return appDocTpl2;
    }

    /**
     * 修改模板审核状态
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param tplCode
     * @param auditStatus
     * @return
     */
    public boolean auditDocTpl(String appId, String tplCode, String auditStatus) {
        AppDocTpl docTpl = getAppDocTpl(appId, tplCode);
        if (docTpl == null) {
            throw new EbusinessException("模板编号:" + tplCode + "数据不存在");
        }
        AppDocTpl appDocTpl2 = new AppDocTpl();
        appDocTpl2.setId(docTpl.getId());
        appDocTpl2.setAuditStatus(auditStatus);
        if (DocTplAuditEnmu.PASS.getCode().equals(auditStatus)) {
            appDocTpl2.setAuditPassTime(new Date());
        }
        appDocTplMapper.updateByPrimaryKeySelective(appDocTpl2);
        return true;
    }

    /**
     * 重新提交申請
     * @title
     * @methodName
     * @author wangyi
     * @param tplCode 
     * @description
     * @param tpl_code
     * @return
     */
    public boolean replayAuditDocTpl(String appId, String tplCode) {
        AppDocTpl docTpl = getAppDocTpl(appId, tplCode);
        if (docTpl == null) {
            throw new EbusinessException("模板编号:" + tplCode + "数据不存在");
        }
        if (!DocTplAuditEnmu.REJECT.getCode().equals(docTpl.getAuditStatus())) {
            throw new EbusinessException("驳回模板后才可再次申请哦");
        }
        return auditDocTpl(appId, tplCode, DocTplAuditEnmu.WAIT_AUDIT.getCode());
    }

    /**
     * 检查模板编号是否可使用
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param appId
     * @param tplCode
     * @return
     */
    public Boolean checkDocTplCode(String appId, String tplCode) {
        AppDocTpl appDocTpl = getAppDocTpl(appId, tplCode);
        if (appDocTpl == null) {
            return false;
        }
        return true;
    }

}
