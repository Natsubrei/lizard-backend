package com.lizard.lizardbackend.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册DTO
 */
@Data
public class UserRegisterDTO implements Serializable {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 确认密码，需要与输入的密码一致
     */
    private String confirmPassword;
}
