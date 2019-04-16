package com.efida.esearch.model;

import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import com.efida.esearch.utils.DateUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "应用模板对象")
public class AppDocTplDto {

    @ApiModelProperty(value = "序号")
    private Long    id;

    @ApiModelProperty(value = "应用ID")
    @NotEmpty(message = "应用ID不能为空")
    @Size(min = 0, max = 32, message = "应用ID需在{min}和{max}个字符之间")
    private String  appId;

    @ApiModelProperty(value = "所属应用")
    private String  appName;

    @ApiModelProperty(value = "模板编号")
    @NotEmpty(message = "模板编号不能为空")
    @Size(min = 0, max = 32, message = "模板编号需在{min}和{max}个字符之间")
    private String  tplCode;

    @ApiModelProperty(value = "模板名")
    @NotEmpty(message = "模板名不能为空")
    @Size(min = 0, max = 24, message = "模板名需在{min}和{max}个字符之间")
    private String  tplName;

    @ApiModelProperty(value = "数据表编码")
    @NotEmpty(message = "数据表编码不能为空")
    @Size(min = 0, max = 32, message = "数据表编码需在{min}和{max}个字符之间")
    private String  docCode;

    @ApiModelProperty(value = "模板说明")
    @NotEmpty(message = "模板说明不能为空")
    @Size(min = 0, max = 32, message = "数据表编码需在{min}和{max}个字符之间")
    private String  tplDesc;

    @ApiModelProperty(value = "预测数据规模")
    private Long    forecastDataSize;

    @ApiModelProperty(value = "申请用户ID")
    private String  applyUserId;

    @ApiModelProperty(value = "申请用户名称")
    private String  applyUserName;

    @ApiModelProperty(value = "wait_audit:待审批;reject:驳回; pass:审批通过")
    @NotEmpty(message = "审批状态不能为空")
    private String  auditStatus;

    @ApiModelProperty(value = "最近一次审批通过时间")
    private Date    auditPassTime;

    @ApiModelProperty(value = "锁定模板 true 停止使用 false.未锁定")
    private Boolean isLock;

    @ApiModelProperty(value = "修改者账号")
    private String  modifyUserId;

    @ApiModelProperty(value = "模板内容")
    private String  mappingTplContent;

    private String  dataTplContent;

    private Date    gmtCreate;

    private Date    gmtModified;

    public static AppDocTplDto coverToDto(AppDocTpl appDocTpl) {
        if (appDocTpl != null) {
            AppDocTplDto appDocTplDto = new AppDocTplDto();
            BeanUtils.copyProperties(appDocTpl, appDocTplDto);
            return appDocTplDto;
        }
        return null;
    }

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

    public String getAuditPassTimeStr() {
        return DateUtil.formatDate(auditPassTime);
    }

    public String getGmtCreateStr() {
        return DateUtil.formatDate(this.gmtCreate);
    }

    public String getGmtModifiedStr() {
        return DateUtil.formatDate(this.gmtModified);
    }

}