package com.efida.sports.dmp.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "报名分布情况")
public class ApplyAreaVo {
    @ApiModelProperty(value = "赛场名称")
    private String fileName;
    @ApiModelProperty(value = "报名次数")
    private long   applyCount;
    @ApiModelProperty(value = "经度")
    private String lon;
    @ApiModelProperty(value = "纬度")
    private String lat;
    @ApiModelProperty(value = "省")
    private String province;
    @ApiModelProperty(value = "市")
    private String city;
    @ApiModelProperty(value = "区县")
    private String area;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(long applyCount) {
        this.applyCount = applyCount;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

}
