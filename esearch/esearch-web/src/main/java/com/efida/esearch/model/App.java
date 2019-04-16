package com.efida.esearch.model;

import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.efida.esearch.utils.DateUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "应用信息")
public class App {
    private Long    id;

    @ApiModelProperty(value = "应用ID")
    @NotEmpty(message = "应用ID不能为空")
    @Size(min = 0, max = 32, message = "应用ID需在{min}和{max}个字符之间")
    private String  appId;

    @ApiModelProperty(value = "序列号")
    @NotEmpty(message = "序列号不能为空")
    @Size(min = 0, max = 64, message = "序列号需在{min}和{max}个字符之间")
    private String  appKey;

    @ApiModelProperty(value = "密钥")
    @NotEmpty(message = "密钥不能为空")
    @Size(min = 0, max = 64, message = "密钥需在{min}和{max}个字符之间")
    private String  appSecret;

    @ApiModelProperty(value = "应用名")
    @NotEmpty(message = "应用名不能为空")
    @Size(min = 0, max = 64, message = "应用名需在{min}和{max}个字符之间")
    private String  appName;

    @ApiModelProperty(value = "申请时间(备注:添加时前端不传)")
    private Date    applyTime;

    @ApiModelProperty(value = "申请用户(备注:添加时前端不传)")
    private String  applyUserId;

    @ApiModelProperty(value = "申请用户名(备注:添加时前端不传)")
    private String  applyUserName;

    @ApiModelProperty(value = "审核通过时间(备注:添加时前端不传)")
    private Date    auditPassTime;

    @ApiModelProperty(value = "申请描述")
    @Size(max = 255, message = "申请描述最大{max}个字符")
    private String  applyDesc;

    @ApiModelProperty(value = "审批描述(备注:添加时前端不传)")
    private String  auditDesc;

    @ApiModelProperty(value = "wait_audit:待审批;reject:驳回 pass:审批通过 (备注:添加时前端不传)")
    private String  auditStatus;

    @ApiModelProperty(value = "是否锁定。true 锁定 false 未锁定")
    private Boolean isLock;

    @ApiModelProperty(value = "创建时间(备注:添加时前端不传)")
    private Date    gmtCreate;

    private Date    gmtModified;

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

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey == null ? null : appKey.trim();
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret == null ? null : appSecret.trim();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public String getApplyTimeStr() {
        return DateUtil.formatDate(this.applyTime);
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
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

    public Date getAuditPassTime() {
        return auditPassTime;
    }

    public String getAuditPassTimeStr() {
        return DateUtil.formatDate(this.auditPassTime);
    }

    public void setAuditPassTime(Date auditPassTime) {
        this.auditPassTime = auditPassTime;
    }

    public String getApplyDesc() {
        return applyDesc;
    }

    public void setApplyDesc(String applyDesc) {
        this.applyDesc = applyDesc == null ? null : applyDesc.trim();
    }

    public String getAuditDesc() {
        return auditDesc;
    }

    public void setAuditDesc(String auditDesc) {
        this.auditDesc = auditDesc == null ? null : auditDesc.trim();
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus == null ? null : auditStatus.trim();
    }

    public Boolean getIsLock() {
        return isLock;
    }

    public void setIsLock(Boolean isLock) {
        this.isLock = isLock;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtCreateStr() {
        return DateUtil.formatDate(this.gmtCreate);
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public String getGmtModifiedStr() {
        return DateUtil.formatDate(this.gmtModified);
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}