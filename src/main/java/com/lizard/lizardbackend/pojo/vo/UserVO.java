package com.lizard.lizardbackend.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录VO
 */
@Data
public class UserVO implements Serializable {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 手机号
     */
    private String phone;
}
