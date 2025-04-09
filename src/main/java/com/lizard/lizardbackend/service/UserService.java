package com.lizard.lizardbackend.service;

public interface UserService {
    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param confirmPassword 确认密码
     * @return 注册成功则返回用户id，注册失败则返回null
     */
    Long register(String username, String password, String confirmPassword);
}
