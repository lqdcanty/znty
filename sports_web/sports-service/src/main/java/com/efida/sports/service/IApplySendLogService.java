package com.efida.sports.service;

import com.baomidou.mybatisplus.service.IService;
import com.efida.sports.entity.ApplySendLog;

/**
 * 发送日志服务
 * 
 * @author zengbo
 * @version $Id: IApplySendLogService.java, v 0.1 2018年7月5日 下午3:42:16 zengbo Exp $
 */
public interface IApplySendLogService extends IService<ApplySendLog> {

    /**
     * 保存发送日志
     * 
     * @param log
     * @return
     */
    public ApplySendLog saveSendLog(ApplySendLog log);

}
