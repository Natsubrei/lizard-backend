package com.lizard.lizardbackend.service.impl;

import com.lizard.lizardbackend.constant.MessageConstant;
import com.lizard.lizardbackend.constant.UserConstant;
import com.lizard.lizardbackend.exception.LoginFailException;
import com.lizard.lizardbackend.exception.RegisterFailException;
import com.lizard.lizardbackend.mapper.UserMapper;
import com.lizard.lizardbackend.pojo.entity.User;
import com.lizard.lizardbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param confirmPassword 确认密码
     * @return 注册成功则返回用户id，注册失败则返回null
     */
    @Override
    public Long register(String username, String password, String confirmPassword) {
        // 检查各字段是否为空
        if (StringUtils.isAnyBlank(username, password, confirmPassword)) {
            throw new RegisterFailException(MessageConstant.USER_OR_PASSWORD_IS_NULL);
        }

        // 检查两次输入密码是否一致
        if (!confirmPassword.equals(password)) {
            throw new RegisterFailException(MessageConstant.PASSWORDS_NOT_CONSISTENT);
        }

        // 检查用户名格式是否正确
        if (!username.matches(UserConstant.USERNAME_PATTERN)) {
            throw new RegisterFailException(MessageConstant.USERNAME_FORMAT_ERROR);
        }

        // 检查密码格式是否正确
        if (!password.matches(UserConstant.PASSWORD_PATTERN)) {
            throw new RegisterFailException(MessageConstant.PASSWORD_FORMAT_ERROR);
        }

        // 检查用户是否存在
        User user = userMapper.getByUsername(username);
        if (user != null) {
            throw new RegisterFailException(MessageConstant.USERNAME_EXISTS);
        }

        // 对密码进行加密，用户名作为Salt，防止密码重复导致加密密码一致
        String encryptPwd = DigestUtils.md5DigestAsHex((password + username).getBytes(StandardCharsets.UTF_8));

        // 创建新用户
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(encryptPwd);

        // 将新用户信息存入数据库
        userMapper.insert(newUser);

        // 返回新用户的id
        return newUser.getId();
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 用户id
     */
    @Override
    public User login(String username, String password) {
        // 检查各字段是否为空
        if (StringUtils.isAnyBlank(username, password)) {
            throw new LoginFailException(MessageConstant.USER_OR_PASSWORD_IS_NULL);
        }

        // 检查用户名格式是否正确
        if (!username.matches(UserConstant.USERNAME_PATTERN)) {
            throw new LoginFailException(MessageConstant.USERNAME_FORMAT_ERROR);
        }

        // 检查密码格式是否正确
        if (!password.matches(UserConstant.PASSWORD_PATTERN)) {
            throw new LoginFailException(MessageConstant.PASSWORD_FORMAT_ERROR);
        }

        // 检查用户是否存在
        User user = userMapper.getByUsername(username);
        if (user == null) {
            throw new LoginFailException(MessageConstant.USERNAME_OR_PASSWORD_ERROR);
        }

        // 检查密码是否正确
        String encryptPwd = DigestUtils.md5DigestAsHex((password + username).getBytes(StandardCharsets.UTF_8));
        if (!encryptPwd.equals(user.getPassword())) {
            throw new LoginFailException(MessageConstant.USERNAME_OR_PASSWORD_ERROR);
        }

        return user;
    }
}
