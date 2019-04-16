package com.efida.sports.service.dubbo.intergration;

import com.efida.sport.admin.facade.model.SpAppVersionModel;
import com.efida.sport.admin.facade.model.SpProjectTypeModel;

import java.util.List;

public interface SpAppVersionFacadeClient {

    /**
     * 根据appType查询App版本
     * @param appType
     */
    public SpAppVersionModel getAppVersionByAppType(String appType);

}
