package com.efida.esearch.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import com.alibaba.fastjson.JSONObject;
import org.hibernate.validator.constraints.NotEmpty;

import com.efida.esearch.utils.DateUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "查询模板对象")
public class AppQueryTpl {

    @ApiModelProperty(value = "序号")
    private Long    id;

    @ApiModelProperty(value = "应用ID")
    @NotEmpty(message = "应用ID不能为空")
    @Size(min = 0, max = 32, message = "应用ID需在{min}和{max}个字符之间")
    private String  appId;

    @ApiModelProperty(value = "应用数据存储模板标识")
    @NotEmpty(message = "应用数据存储模板标识不能为空")
    @Size(min = 0, max = 32, message = "应用数据存储模板标识需在{min}和{max}个字符之间")
    private String  docTplCode;

    @ApiModelProperty(value = "模板编号")
    @NotEmpty(message = "模板编号不能为空")
    @Size(min = 0, max = 32, message = "模板编号需在{min}和{max}个字符之间")
    private String  tplCode;

    @ApiModelProperty(value = "模板名称")
    @NotEmpty(message = "模板名称不能为空")
    @Size(min = 0, max = 24, message = "模板名称需在{min}和{max}个字符之间")
    private String  tplName;

    @ApiModelProperty(value = "模板内容")
//    @NotEmpty(message = "模板内容不能为空")
    private String  tplContent;

    @ApiModelProperty(value = "模板描述")
    @Size(min = 0, max = 255, message = "模板描述需在{min}和{max}个字符之间")
    private String  tplDesc;

    @ApiModelProperty(value = "申请者账号")
    private String  applyUserId;

    @ApiModelProperty(value = "申请者姓名")
    private String  applyUserName;

    @ApiModelProperty(value = "审核状态 wait_audit:待审批;reject:驳回; pass:审批通过")
    @NotEmpty(message = "审核状态不能为空")
    private String  auditStatus;

    @ApiModelProperty(value = "最近一次审批通过时间")
    private Date    auditPassTime;

    @ApiModelProperty(value = "锁定状态 true.锁定模板 停止使用 false.未锁定")
    private Boolean isLock;

    @ApiModelProperty(value = "修改者账号")
    private String  modifyUserId;

    @ApiModelProperty(value = "创建时间")
    private Date    gmtCreate;

    @ApiModelProperty(value = "修改时间")
    private Date    gmtModified;

    @ApiModelProperty(value = "所有参数逗号分隔")
    private String params;

    @ApiModelProperty(value = "所有条件 模板界面所有条件对象")
    private String conditionJson;

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

    public String getDocTplCode() {
        return docTplCode;
    }

    public void setDocTplCode(String docTplCode) {
        this.docTplCode = docTplCode == null ? null : docTplCode.trim();
    }

    public String getTplCode() {
        return tplCode;
    }

    public void setTplCode(String tplCode) {
        this.tplCode = tplCode == null ? null : tplCode.trim();
    }

    public String getTplName() {
        return tplName;
    }

    public void setTplName(String tplName) {
        this.tplName = tplName == null ? null : tplName.trim();
    }

    public String getTplDesc() {
        return tplDesc;
    }

    public void setTplDesc(String tplDesc) {
        this.tplDesc = tplDesc == null ? null : tplDesc.trim();
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId == null ? null : applyUserId.trim();
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName == null ? null : applyUserName.trim();
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus == null ? null : auditStatus.trim();
    }

    public Date getAuditPassTime() {
        return auditPassTime;
    }

    public void setAuditPassTime(Date auditPassTime) {
        this.auditPassTime = auditPassTime;
    }

    public Boolean getIsLock() {
        return isLock;
    }

    public void setIsLock(Boolean isLock) {
        this.isLock = isLock;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId == null ? null : modifyUserId.trim();
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

    public String getTplContent() {
        return tplContent;
    }

    public void setTplContent(String tplContent) {
        this.tplContent = tplContent == null ? null : tplContent.trim();
    }

    public String getAuditPassTimeStr() {
        return DateUtil.formatDate(auditPassTime);
    }

    public String getGmtCreateStr() {
        return DateUtil.formatDate(this.gmtCreate);
    }

    public String getGmtModifiedStr() {
        return DateUtil.formatDate(this.gmtModified);
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getConditionJson() {
        return conditionJson;
    }

    public void setConditionJson(String conditionJson) {
        this.conditionJson = conditionJson;
    }
}