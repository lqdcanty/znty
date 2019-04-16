package com.efida.esearch.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务异常
 * @author wang yi
 * @desc 
 * @version $Id: EbusinessException.java, v 0.1 2018年9月27日 下午4:32:00 wang yi Exp $
 */
public class EbusinessException extends RuntimeException {

    /**  */
    private static final long serialVersionUID = 2588742087808966160L;
    /**
     * Logger
     */
    private Logger            logger           = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public EbusinessException() {
        super();
    }

    public EbusinessException(String message, Throwable cause) {
        super(message, cause);
        logger.debug("message :" + message);
    }

    public EbusinessException(String message) {
        super(message);
        logger.debug("message :" + message);
    }

    public EbusinessException(Throwable cause) {
        super(cause);
    }

}
