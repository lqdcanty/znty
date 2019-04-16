package com.efida.sports.exception;

import com.efida.sports.enums.ErrorCodeEnum;

/**
 * 
 * @author lijiaqi
 * @date 2015年7月8日
 */
public class ErrorCodeException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 7050152196752715643L;

    private int               code;
    private String            msg;

    public ErrorCodeException(ErrorCodeEnum errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public ErrorCodeException(ErrorCodeEnum errorCode, Throwable t) {
        super(errorCode.getMsg(), t);
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public ErrorCodeException(int code, String msg, Throwable t) {
        super(msg, t);
        this.code = code;
        this.msg = msg;
    }

    public ErrorCodeException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
