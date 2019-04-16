package com.efida.esearch.model;


import java.util.Date;

public class AppDocTpl {
    private Long id;

    private String appId;

    private String tplCode;

    private String tplName;

    private String docCode;

    private String tplDesc;

    private Long forecastDataSize;

    private String applyUserId;

    private String applyUserName;

    private String auditStatus;

    private Date auditPassTime;

    private Boolean isLock;

    private String modifyUserId;

    private Date gmtCreate;

    private Date gmtModified;
    private String mappingTplContent;

    private String dataTplContent;

    public String getMappingTplContent() {
        return mappingTplContent;
    }

    public void setMappingTplContent(String mappingTplContent) {
        this.mappingTplContent = mappingTplContent == null ? null : mappingTplContent.trim();
    }

    public String getDataTplContent() {
        return dataTplContent;
    }

    public void setDataTplContent(String dataTplContent) {
        this.dataTplContent = dataTplContent == null ? null : dataTplContent.trim();
    }

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

    public String getDocCode() {
        return docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode == null ? null : docCode.trim();
    }

    public String getTplDesc() {
        return tplDesc;
    }

    public void setTplDesc(String tplDesc) {
        this.tplDesc = tplDesc == null ? null : tplDesc.trim();
    }

    public Long getForecastDataSize() {
        return forecastDataSize;
    }

    public void setForecastDataSize(Long forecastDataSize) {
        this.forecastDataSize = forecastDataSize;
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
}