package com.efida.sports.exception;

import org.apache.log4j.Logger;

/**
 * 业务参数验证导致异常，希望让用户看到的错误提示信息使用该异常 
 * 
 * @author zhiyang
 * @version $Id: BusinessException.java, v 0.1 2015年12月8日 下午4:29:12 zhiyang Exp $
 */
public class BusinessException extends RuntimeException {

    /**
     * Logger
     */
    Logger                    logger           = Logger.getLogger(BusinessException.class);
    /**
     * 
     */
    private static final long serialVersionUID = 775477636706597359L;

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public BusinessException() {
        super();
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        logger.debug("message :" + message);
    }

    public BusinessException(String message) {
        super(message);
        logger.debug("message :" + message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

}
