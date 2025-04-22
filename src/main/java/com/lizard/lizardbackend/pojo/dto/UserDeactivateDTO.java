package com.lizard.lizardbackend.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注销账号DTO
 */
@Data
public class UserDeactivateDTO implements Serializable {
    /**
     * 用户密码
     */
    private String password;
}
