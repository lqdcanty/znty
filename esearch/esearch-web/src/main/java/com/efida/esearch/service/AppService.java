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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.efida.esearch.core.ESClient;
import com.efida.esearch.enmus.AppAuditEnmu;
import com.efida.esearch.exception.EbusinessException;
import com.efida.esearch.mapper.AppMapper;
import com.efida.esearch.model.App;
import com.efida.esearch.model.AppDocTpl;
import com.efida.esearch.model.PagingResult;
import com.efida.esearch.utils.SecretTools;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@CacheConfig(cacheNames = "appCache") // 本类内方法指定使用缓存时，默认的名称就是appCache
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class AppService {
    private Logger           log = LoggerFactory.getLogger(AppService.class.getSimpleName());
    @Autowired
    private ESClient         esClient;

    @Autowired
    private AppMapper        appMapper;

    @Autowired
    private AppDocTplService appDocTplService;

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

    /**
     * 根据应用ID 和文档类型名称 在ES中创建对应的 mapping 文档存储结构
     * @param appId
     * @param docTplCode
     */
    public void createDocMapping(String appId, String docTplCode) {
        AppDocTpl docTpl = appDocTplService.getAppDocTpl(appId, docTplCode);
        log.info("mapping存储的前:{}", docTpl.getMappingTplContent());
        JSONObject properties = JSONObject.parseObject(docTpl.getMappingTplContent());
        log.info("mapping存储的最终json格式:{}", properties.toJSONString());
        esClient.createMapping(appId + "_" + docTplCode, properties);
    }

    public PagingResult<App> getAppInfos(String appId, String keyword, String status,
                                         int pageNumber, int pageSize) {
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
        List<App> appinfos = appMapper.selectAppByParams(params);
        PageInfo<App> pageInfo = new PageInfo<App>(appinfos);
        return new PagingResult<App>(pageInfo.getList(), pageInfo.getTotal(), pageNumber, pageSize);
    }

    /**
     * 创建一个应用
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param app
     * @return
     */
    public App createAppInfo(App app) {
        String appKey = SecretTools.generateAppKey();
        app.setAppKey(appKey);
        app.setAppSecret(SecretTools.generateAppSecret(app.getAppId(), appKey));
        assertAppInfo(app);
        App appinfo = getAppByAppid(app.getAppId());
        if (appinfo != null) {
            throw new EbusinessException("应用ID:" + app.getAppId() + "已被占用");
        }
        appMapper.insert(app);
        return app;
    }

    /**
     * 断言应用信息
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param appinfo
     */
    public void assertAppInfo(App appinfo) {
        if (appinfo == null) {
            return;
        }
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<App>> set = validator.validate(appinfo);
        for (ConstraintViolation<App> constraintViolation : set) {
            throw new EbusinessException(constraintViolation.getMessage());
        }
    }

    /**
     * 锁定一个应用
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param appId
     * @param isLock
     * @return
     */
    public boolean lockAppInfo(String appId, boolean isLock) {
        App appinfo = getAppByAppid(appId);
        if (appinfo == null) {
            throw new EbusinessException("应用ID:" + appId + "不存在");
        }
        try {
            App app = new App();
            app.setId(appinfo.getId());
            app.setIsLock(isLock);
            app.setGmtModified(new Date());
            appMapper.updateByPrimaryKeySelective(app);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
        return true;
    }

    /**
     * 获取应用信息
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param appId 应用ID
     * @return
     */
    public App getAppByAppid(String appId) {
        App app = appMapper.selectAppByAppId(appId);
        return app;
    }

    /**
     * 审核应用
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param appId 应用ID
     * @param auditStatus 审批状态
     * @param auditDesc 审批描述
     * @return
     */
    public boolean auditAppInfo(String appId, String auditStatus, String auditDesc) {
        App appByAppid = getAppByAppid(appId);
        if (appByAppid == null) {
            throw new EbusinessException("应用ID:" + appId + "不存在");
        }
        if (StringUtils.isNotBlank(auditDesc) && auditDesc.length() > 64) {
            throw new EbusinessException("描述最多64个字符");
        }
        App app = new App();
        app.setId(appByAppid.getId());
        app.setAuditStatus(auditStatus);
        app.setGmtModified(new Date());
        if (AppAuditEnmu.PASS.getCode().equals(auditStatus)) {
            app.setAuditPassTime(new Date());
        }
        appMapper.updateByPrimaryKeySelective(app);
        return true;
    }

    /**
     * 修改应用信息
     * @title
     * @methodName
     * @author wangyi
     * @param appId 
     * @description
     * @param app
     * @return
     */
    public App upAppInfo(String appId, App app) {
        App app2 = getAppByAppid(appId);
        if (app2 == null) {
            throw new EbusinessException("应用ID:" + appId + "不存在");
        }
        //不更新核心数据
        app2.setAppKey(null);
        app2.setAppSecret(null);
        app2.setAppName(app.getAppName());
        app2.setIsLock(app.getIsLock());
        assertAppInfo(app2);
        appMapper.updateByPrimaryKeySelective(app2);
        return getAppByAppid(appId);
    }

}
