package com.lizard.lizardbackend.constant;

/**
 * 用户相关常量类
 */
public interface UserConstant {
    /**
     * 用户名格式，4~16位字母、数字、下划线
     */
    String USERNAME_PATTERN = "^\\w{4,16}$";

    /**
     * 密码格式，8~32位字母、数字、下划线
     */
    String PASSWORD_PATTERN = "^\\w{8,32}$";

    /**
     * 昵称格式，2~12位中文字符、字母、数字、下划线
     */
    String NICKNAME_PATTERN = "^[\\u4e00-\\u9fa5\\w]{2,12}$";

    /**
     * 手机号格式，11位中国大陆手机号码
     */
    String PHONE_PATTERN = "^1[3456789]\\d{9}$";

    /**
     * 默认昵称前缀
     */
    String DEFAULT_NICKNAME_PREFIX = "西易用户_";

    /**
     * 用户id常量
     */
    String USER_ID = "USER_ID";

    /**
     * 用户头像存放目录
     */
    String AVATAR_DIRECTORY = "avatar";
}
