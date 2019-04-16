package com.efida.sports.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.efida.sports.entity.ApplySendLog;
import com.efida.sports.mapper.ApplySendLogMapper;
import com.efida.sports.service.IApplySendLogService;

/**
 * 日志发送服务类
 * 
 * @author zengbo
 * @version $Id: ApplySendLogServiceImpl.java, v 0.1 2018年7月5日 下午3:44:45 zengbo Exp $
 */
@Service
public class ApplySendLogServiceImpl extends ServiceImpl<ApplySendLogMapper, ApplySendLog>
                                     implements IApplySendLogService {

    @Autowired
    private ApplySendLogMapper applySendLogMapper;

    @Override
    public ApplySendLog saveSendLog(ApplySendLog log) {
        log.setGmtCreate(new Date());
        log.setGmtModified(new Date());
        insert(log);
        return log;
    }

}
