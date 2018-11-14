package springboot.core.constant;

/**
 * redis中的常量
 */
public class RedisConstant {

    //redis 0号数据库存放 普通缓存
    public static final  Integer REDIS_COMMON_DB = 0;

    //redis 1号数据库存放 session缓存
    public static final  Integer REDIS_SESSION_DB = 1;

    //redis 2号数据库存放 1分钟内手机号是否发送过验证码倒计时标志
    public static final  Integer REDIS_PHONE_COUNT_DOWN_DB = 2;

    //redis 4号数据库存放 定时短信的信息
    public static final  Integer REDIS_CLOCK_PHONE_DB = 4;

    //redis 5号数据库存放 发送邮件后尚未激活的用户信息
    public static final Integer REDIS_EMAIL_USER_DB = 5;

    //session过期时间 1小时
    public static final Integer REDIS_SESSION_EXPIRE = 60*60;

    //手机验证码过期时间 1小时
    public static final Integer REDIS_PHONE_EXPIRE = 60*60;

    //发送邮件后尚未激活的用户信息存放24小时
    public static final Integer REDIS_EMAIL_EXPIRE = 24*60*60;





}
