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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.efida.esearch.exception.EbusinessException;
import com.efida.esearch.mapper.AppDocFieldsMapper;
import com.efida.esearch.model.AppDocFields;

@Service
@CacheConfig(cacheNames = "appDocTpl") // 本类内方法指定使用缓存时，默认的名称就是appDocTpl
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class AppDocFieldService {

    private static final float Object = 0;

    private Logger             logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppDocFieldsMapper appDocFieldsMapper;

    /**
     * 获取字段
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param tplCode
     * @return
     */
    public List<AppDocFields> getDocFields(String appId, String tplCode) {
        return appDocFieldsMapper.selectByAppIdAndDocCode(appId, tplCode);
    }

    /**
     * 根据ID删除
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param id
     * @return
     */
    public boolean delByid(Long id) {
        appDocFieldsMapper.deleteByPrimaryKey(id);
        return true;
    }

    /**
     * 添加字段名
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param app
     * @return
     */
    public AppDocFields addDocField(AppDocFields app) {
        AppDocFields docField = getDocField(app.getAppId(), app.getDocCode(), app.getFieldName());
        if (docField != null) {
            throw new EbusinessException("字段名称已使用");
        }
        app.setGmtCreate(new Date());
        assertDocField(app);
        appDocFieldsMapper.insert(app);
        return app;
    }

    public AppDocFields getDocField(String appId, String docCode, String fieldName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appId", appId);
        params.put("docCode", docCode);
        params.put("fieldName", fieldName);
        return appDocFieldsMapper.selectOneByParams(params);
    }

    public void assertDocField(AppDocFields appDocTpl) {
        if (appDocTpl == null) {
            return;
        }
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<AppDocFields>> set = validator.validate(appDocTpl);
        for (ConstraintViolation<AppDocFields> constraintViolation : set) {
            throw new EbusinessException(constraintViolation.getMessage());
        }
    }

    /**
     * 更新字段
     * @title
     * @methodName
     * @author wangyi
     * @param id 
     * @description
     * @return
     */
    public AppDocFields updateDocField(Long id, AppDocFields docField) {
        AppDocFields docsField = getDocFieldById(id);
        if (docsField == null) {
            throw new EbusinessException("数据不存在");
        }
        docField.setId(docsField.getId());
        docField.setGmtModified(new Date());
        assertDocField(docField);
        appDocFieldsMapper.updateByPrimaryKeySelective(docField);
        return docField;
    }

    /**
     * 获取字段
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param id
     * @return
     */
    private AppDocFields getDocFieldById(Long id) {
        return appDocFieldsMapper.selectByPrimaryKey(id);
    }

    /**
     * 创建一个默认字段
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param appId
     * @param docCode
     * @param docName 
     * @return
     */
    public AppDocFields createNewDefault(String appId, String docCode, String docName) {
        AppDocFields appDocFields = new AppDocFields();
        appDocFields.setAppId(appId);
        appDocFields.setDocCode(docCode);
        appDocFields.setDocName(docName);
        appDocFields.setFieldName("doc_source_id");
        appDocFields.setFieldComment(" 业务数据对应唯一标识");
        appDocFields.setFieldType("keyword");
        appDocFields.setExtendJsonInfo(
            "{'index':'true','null_value':'0','store':'false','doc_values':'true'}");
        appDocFields.setGmtCreate(new Date());
        return appDocFields;
    }

}
