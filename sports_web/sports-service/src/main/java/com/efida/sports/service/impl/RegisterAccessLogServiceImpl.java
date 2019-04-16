package com.efida.sports.service.impl;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.sport.facade.enums.RegTerminalEnum;
import com.efida.sports.constants.Constants;
import com.efida.sports.entity.RegisterAccessLog;
import com.efida.sports.mapper.RegisterAccessLogMapper;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.IRegisterAccessLogService;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.util.DateUtil;
import com.efida.sports.util.SeqWorkerUtil;
import com.efida.sports.util.ServletUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zoutao
 * @since 2018-09-11
 */
@Service
public class RegisterAccessLogServiceImpl extends
                                          ServiceImpl<RegisterAccessLogMapper, RegisterAccessLog>
                                          implements IRegisterAccessLogService {

    private static Logger         log = LoggerFactory.getLogger(RegisterAccessLogServiceImpl.class);
    @Autowired
    private CacheService          cacheService;
    @Autowired
    private UcenterAdapterService ucenterAdapterService;

    @Override
    public void addPCAccessLog(HttpServletRequest request) {
        try {

            String accessLogCode = SeqWorkerUtil.buildSingleId();
            HttpSession session = request.getSession();
            Object object = session.getAttribute(Constants.ZTYD_PC_ACCESS_KEY);
            if (object == null) {
                session.setAttribute(Constants.ZTYD_PC_ACCESS_KEY, accessLogCode);
            }
            String ip = ServletUtil.getIpAddress(request);
            String cacheKey = Constants.ZTYD_PC_ACCESS_IP_KEY + ip;
            String cacheVal = cacheService.get(cacheKey);
            //如果已经存在访问了，不记录日志
            if (StringUtils.isNotBlank(cacheVal) && null != object) {
                return;
            }
            Date now = new Date();
            cacheService.put(cacheKey, ip, getCacheTimeOut());

            AuthToken authToken = ucenterAdapterService.getPCAuthToken(session);
            String path = request.getContextPath();
            String requestURI = request.getRequestURI();
            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int port = request.getServerPort();
            String accessEntry = scheme + "://" + serverName + ":" + port + path + requestURI;
            RegisterAccessLog accessLog = new RegisterAccessLog();
            accessLog.setAccessDay(now);
            accessLog.setAccessEntry(accessEntry);
            accessLog.setAccessLogCode(accessLogCode);
            accessLog.setAccessTime(now);
            accessLog.setGmtCreate(now);
            accessLog.setIp(ip);
            if (authToken != null) {
                accessLog.setRegisterCode(authToken.getRegisterCode());
            }
            accessLog.setAccessTerminal(RegTerminalEnum.PC.getCode());
            insert(accessLog);
        } catch (Exception e) {
            log.error("", e);
        }

    }

    private int getCacheTimeOut() {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        int millisecond = (int) (cal.getTimeInMillis() - System.currentTimeMillis());
        return millisecond;

    }

    @Override
    public long getPCAccessCount(Date reportDate) {
        Date startTime = DateUtil.getStartTime(reportDate);
        Date endTime = DateUtil.getEndTime(reportDate);
        Wrapper<RegisterAccessLog> wrapper = new EntityWrapper<RegisterAccessLog>();
        wrapper.ge("access_time", startTime);
        wrapper.le("access_time", endTime);
        return selectCount(wrapper);
    }

}
