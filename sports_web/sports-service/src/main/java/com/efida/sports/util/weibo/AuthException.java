package com.efida.sports.util.weibo;

/**
 * 
 * @author lijiaqi
 * @date 2015年8月19日
 */
public class AuthException extends Exception {

    public static final int   REPEATREGISTER   = 1;

    public static final int   UNSETPASSWORD    = 2;

    /**
     * 
     */
    private static final long serialVersionUID = -9161272804560739233L;

    private int               authCodeReason   = 0;

    public AuthException(int authCodeReason, String msg) {
        super(msg);
        this.authCodeReason = authCodeReason;
    }

    public AuthException(String msg) {
        super(msg);
    }

    public AuthException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public int getAuthCodeReason() {
        return authCodeReason;
    }

    public void setAuthCodeReason(int authCodeReason) {
        this.authCodeReason = authCodeReason;
    }

}
