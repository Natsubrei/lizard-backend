package com.lizard.lizardbackend.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 用户修改个人信息DTO
 */
@Data
public class UserInfoUpdateDTO implements Serializable {
    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户头像文件
     */
    private MultipartFile file;
}
