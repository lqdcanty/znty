package cn.evake.auth.constants;

/**
 * 常用常量
 * @author Evance
 * @version $Id: Constants.java, v 0.1 2017年12月24日 上午12:23:43 Evance Exp $
 */
public class WebConstants {
    /**
     * 返回json相关
     */
    public static final String RESULTCODE   = "resultCode";

    public static final String RESULTMSG    = "resultMsg";

    public static final String RESULT       = "result";

    public static final String CODE         = "code";
    /**
     * 操作成功
     */
    public static final int    SUCCESS      = 0;

    /**
     * 操作失败
     */
    public static final int    FAILED       = 1;

    /**
     * 服务器错误异常
     */
    public static final int    SERVER_ERROR = 2;

    /**
     * 重复操作
     */
    public static final int    REPEAT       = 203;

    /**
     * 没有权限
     */
    public static final int    NO_AUTH      = 403;

}
