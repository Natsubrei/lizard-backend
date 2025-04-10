package com.lizard.lizardbackend.service;

import com.lizard.lizardbackend.pojo.entity.User;

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
}
