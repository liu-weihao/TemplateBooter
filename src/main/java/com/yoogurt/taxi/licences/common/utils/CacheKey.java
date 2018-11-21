package com.yoogurt.taxi.licences.common.utils;

/**
 * Description:
 * Redis专用的常量类。
 * 命名有个基本的规范：
 * ************************************************************************
 * <p>
 * key name 一律使用大写，并添加必要的注释。
 * 修饰符：public static final。
 * 类型：{@link String}
 * </p>
 * -------------------------------------------------------------------------
 * <p>
 * 如果是用于临时作用的key，需要在key name前追加 T_，形如 {@code <T_CODE_KEY>};
 * 如果是相对持久的缓存作用的key，需要在key name前追加 P_，形如 {@code <P_CODE_KEY>};
 * 如果是定时过期的key，需要在key name前追加 E_，形如 {@code <E_CODE_KEY>}。
 * </p>
 * -------------------------------------------------------------------------
 * <p>
 * 如果key name后需拼接内容，使用#分隔，形如{@code <T_CODE_KEY#userId> }。
 * </p>
 * -------------------------------------------------------------------------
 * <p>
 * 末尾拼接 _KEY，表示这是一个key name。
 * 末尾拼接 _MAP，表示这是一个 map 对象。
 * </p>
 * **************************************************************************
 *
 * @author Eric Lau
 * @Date 2017/9/6.
 */
public final class CacheKey {

    /**
     * shiro授权缓存
     */
    public static final String SHIRO_AUTHORITY_MAP = "P_SHIRO_AUTHORITY_MAP";

    /**
     * 客户端登录成功，获取授权码。
     * 凭此授权码，获取系统颁发的token。
     * 成功生成token，并返回给客户端，将清除grant_code缓存。
     */
    public static final String GRANT_CODE_KEY = "E_GRANT_CODE#";

    /**
     * 缓存{@link com.yoogurt.taxi.common.bo.SessionUser} SessionUser对象。
     * 后面拼接userId.
     * 适用于Mobile和Web
     */
    public static final String SESSION_USER_KEY = "P_SESSION_USER#";

    /**
     * 缓存{@link com.yoogurt.taxi.common.bo.SessionUser} SessionUser对象。
     * 后面拼接userId.
     * 适用于WX
     */
    public static final String SESSION_USER_X_KEY = "P_SESSION_USER_X#";

    /**
     * 客户端通过授权码，获取到token，拼接userId。
     */
    public static final String TOKEN_KEY = "P_TOKEN#";

    /**
     * 用于缓存用户密码试错的次数，后面拼接username。
     */
    public static final String PASSWORD_RETRY_MAX_COUNT_KEY = "E_PASSWORD_RETRY_MAX_COUNT#";
    /**
     * 用于缓存用户激活试错的次数，后面拼接username。
     */
    public static final String ACTIVATE_RETRY_MAX_COUNT_KEY = "E_ACTIVATE_RETRY_MAX_COUNT_KEY#";

    /**
     * 用户缓存用户激活的步骤数，后面拼接userId
     */
    public static final String ACTIVATE_PROGRESS_STATUS_KEY = "E_ACTIVATE_PROGRESS_STATUS_KEY#";

    /**
     * 短信验证码，后面拼接手机号码
     */
    public static final String VERIFY_CODE_KEY = "E_VERIFY_CODE#";

    /**
     * 任务对象缓存的hash key
     */
    public static final String TASK_HASH_KEY = "T_TASK#";

    /**
     * 支付相关的缓存，以Map形式存储
     */
    public static final String PAY_MAP = "P_PAY_MAP";

    /**
     * 支付对象缓存的hash key
     */
    public static final String PAYMENT_HASH_KEY = "P_PAYMENT#";

    /**
     * 回调相关的缓存，以Map形式存储
     */
    public static final String NOTIFY_MAP = "P_NOTIFY_MAP";

    /**
     * 回调事件的缓存，以Map形式存储
     */
    public static final String EVENT_HASH_KEY = "P_EVENT#";

    /**
     * 退款相关的缓存，以Map形式存储
     */
    public static final String REFUND_MAP = "P_REFUND_MAP";

    /**
     * 退款对象缓存的hash key
     */
    public static final String REFUND_HASH_KEY = "P_REFUND#";

    /**
     * 车主导入相关的缓存，以map形式存储，{phoneNumber，password}, 仅供测试使用
     */
    public static final String PHONE_PASSWORD_HASH_MAP = "T_PHONE_PASSWORD__MAP#";

    public static final String USER_ACCOUNT_KEY = "@E_USER_ACCOUNT_KEY#";

    public static final String USER_TRADE_KEY = "@E_USER_TRADE_KEY#";

    public static final String FILE_UPLOAD_RESULT_KEY = "@E_FILE_UPLOAD_RESULT_KEY>";

    /**
     * 记录成功支付的结果，分享领券的活动以此为依据，支付一笔，只能领一张券
     */
    public static final String PAYMENT_MAP = "T_PAYMENT_MAP";

    /**
     * 通过活动页面新注册的用户
     */
    public static final String FRESH_MAN_MAP = "T_FRESH_MAN_MAP";

    /**
     * 消息相关阅读次数，计数器专用
     */
    public static final String MESSAGE_READ_TIMES_COUNT_KEY = "@E_notification_message_read_times_count_key#";

    /**
     * 消息相关签到次数，计数器专用
     */
    public static final String MESSAGE_ATTENDANCE_TIMES_COUNT_KEY = "@E_notification_message_attendance_times_count_key#";


    public static final String MESSAGE_READ_USER_COUNT_KEY = "@E_notification_message_read_user_count_key#";

}
