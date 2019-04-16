package com.efida.sports.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.service.IService;
import com.efida.sports.entity.RegisterAccessLog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zoutao
 * @since 2018-09-11
 */
public interface IRegisterAccessLogService extends IService<RegisterAccessLog> {

    void addPCAccessLog(HttpServletRequest request);

    /**
    * 获取pc端访问量
    * 
    * 
    * @param reportDate
    * @return
    */
    long getPCAccessCount(Date reportDate);

}
