package com.lizard.lizardbackend.service;

import com.lizard.lizardbackend.pojo.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param confirmPassword 确认密码
     * @return 注册成功则返回用户id，注册失败则返回null
     */
    Long register(String username, String password, String confirmPassword);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 用户id
     */
    Long login(String username, String password);

    /**
     * 根据id查询用户
     * @param userId 用户id
     * @return 用户信息
     */
    User getById(Long userId);

    /**
     * 修改用户信息
     * @param userId 用户id
     * @param nickname 用户昵称
     * @param phone 用户手机号
     * @param file 用户头像文件
     */
    void updateInfo(Long userId, String nickname, String phone, MultipartFile file);

    /**
     * 修改用户密码
     * @param userId 用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param confirmNewPassword 确认新密码
     */
    void updatePassword(Long userId, String oldPassword, String newPassword, String confirmNewPassword);

    /**
     * 注销账号
     * @param userId 用户id
     * @param password 用户密码
     */
    void deactivate(Long userId, String password);
}
