package com.efida.esearch.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.alibaba.fastjson.JSON;
import com.efida.esearch.utils.DateUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "应用模板字段对象")
public class AppDocFields {
    @ApiModelProperty(value = "id")
    private Long                id;

    @ApiModelProperty(value = "应用ID")
    @NotEmpty(message = "应用ID不能为空")
    @Size(min = 0, max = 32, message = "应用ID需在{min}和{max}个字符之间")
    private String              appId;

    @ApiModelProperty(value = "模板名")
    @NotEmpty(message = "模板名不能为空")
    @Size(min = 0, max = 24, message = "模板名需在{min}和{max}个字符之间")
    private String              docName;

    @ApiModelProperty(value = "数据表编码(模板数据中的数据表编码)")
    @NotEmpty(message = "数据表编码不能为空")
    @Size(min = 0, max = 32, message = "数据表编码需在{min}和{max}个字符之间")
    private String              docCode;

    @ApiModelProperty(value = "字段名")
    @NotEmpty(message = "字段名不能为空")
    @Size(min = 0, max = 24, message = "字段名需在{min}和{max}个字符之间")
    private String              fieldName;

    @ApiModelProperty(value = "字段描述")
    @NotEmpty(message = "字段描述不能为空")
    @Size(min = 0, max = 100, message = "字段描述需在{min}和{max}个字符之间")
    private String              fieldComment;

    @ApiModelProperty(value = "字段类型")
    @NotEmpty(message = "字段类型不能为空")
    @Size(min = 0, max = 100, message = "字段类型需在{min}和{max}个字符之间")
    private String              fieldType;

    private String              analyzeIndex;

    private String              analyzeSearch;

    private Integer             weight;

    private Boolean             isOrder;

    private Integer             orderNum;

    private String              extendJsonInfo;

    @ApiModelProperty(value = "创建时间")
    private Date                gmtCreate;

    @ApiModelProperty(value = "最后修改时间")
    private Date                gmtModified;

    //拓展字段
    private Map<String, Object> extendMapObj;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName == null ? null : docName.trim();
    }

    public String getDocCode() {
        return docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode == null ? null : docCode.trim();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    public String getFieldComment() {
        return fieldComment;
    }

    public void setFieldComment(String fieldComment) {
        this.fieldComment = fieldComment == null ? null : fieldComment.trim();
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType == null ? null : fieldType.trim();
    }

    public String getAnalyzeIndex() {
        return analyzeIndex;
    }

    public void setAnalyzeIndex(String analyzeIndex) {
        this.analyzeIndex = analyzeIndex == null ? null : analyzeIndex.trim();
    }

    public String getAnalyzeSearch() {
        return analyzeSearch;
    }

    public void setAnalyzeSearch(String analyzeSearch) {
        this.analyzeSearch = analyzeSearch == null ? null : analyzeSearch.trim();
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Boolean getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(Boolean isOrder) {
        this.isOrder = isOrder;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getExtendJsonInfo() {
        return extendJsonInfo;
    }

    public void setExtendJsonInfo(String extendJsonInfo) {
        this.extendJsonInfo = extendJsonInfo == null ? null : extendJsonInfo.trim();
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getExtendMapObj() {
        if (StringUtils.isNotBlank(this.extendJsonInfo)) {
            Map<String, Object> parseObject = new HashMap<String, Object>();
            try {
                parseObject = JSON.parseObject(extendJsonInfo, Map.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return parseObject;
        }
        return extendMapObj;
    }

    public void setExtendMapObj(Map<String, Object> extendMapObj) {
        if (extendMapObj != null) {
            this.extendJsonInfo = JSON.toJSONString(extendMapObj);
        } else {
            this.extendJsonInfo = "{}";
        }
        this.extendMapObj = extendMapObj;
    }

    public String getGmtCreateStr() {
        return DateUtil.formatDate(this.gmtCreate);
    }

    public String getGmtModifiedStr() {
        return DateUtil.formatDate(this.gmtModified);
    }

}