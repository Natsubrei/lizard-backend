package com.lizard.lizardbackend.constant;

/**
 * 用户相关常量类
 */
public interface UserConstant {
    /**
     * 用户名格式，4~16位字母数字下划线
     */
    String USERNAME_PATTERN = "^\\w{4,16}$";

    /**
     * 密码格式，8~32位字母数字下划线
     */
    String PASSWORD_PATTERN = "^\\S{8,32}$";

    /**
     * 默认昵称前缀
     */
    String DEFAULT_NICKNAME_PREFIX = "西易用户_";

    /**
     * 用户id常量
     */
    String USER_ID = "USER_ID";
}
