package top.meyok.user.constant;

/**
 * @author meyok@mbulletin.meyok.top
 * @date 2022/8/29 5:38
 * 用户状态枚举
 */
public enum AccountStatusEnum {

    /**
     * 未注册
     */
    UNREGISTERED,
    /**
     * 注册但未激活
     */
    REGISTERED_BUT_NONACTIVATED,
    /**
     * 注册且激活
     */
    REGISTERED_AND_NACTIVATED,
    /**
     * 冻结中
     */
    BLOCKED,
    /**
     * 账户出错
     */
    ERROR


}
