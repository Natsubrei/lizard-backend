package com.lizard.lizardbackend.pojo.dto;

import lombok.Data;

/**
 * 用户密码更新DTO
 */
@Data
public class UserPwdUpdateDTO {
    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 确认新密码
     */
    private String confirmNewPassword;
}
