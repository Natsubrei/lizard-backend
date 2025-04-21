package com.lizard.lizardbackend.service.impl;

import com.lizard.lizardbackend.constant.MessageConstant;
import com.lizard.lizardbackend.constant.UserConstant;
import com.lizard.lizardbackend.exception.LoginFailException;
import com.lizard.lizardbackend.exception.RegisterFailException;
import com.lizard.lizardbackend.exception.UpdateFailException;
import com.lizard.lizardbackend.mapper.UserMapper;
import com.lizard.lizardbackend.pojo.entity.User;
import com.lizard.lizardbackend.service.UserService;
import com.lizard.lizardbackend.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

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

        // 生成默认昵称
        String nickname = UserConstant.DEFAULT_NICKNAME_PREFIX + UUID.randomUUID().toString().substring(0, 6);

        // 创建新用户
        User newUser = User.builder()
                .username(username)
                .password(encryptPwd)
                .nickname(nickname)
                .build();

        // 将新用户信息存入数据库
        userMapper.insert(newUser);

        // 返回新用户的id
        return newUser.getId();
    }

    @Override
    public Long login(String username, String password) {
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

        return user.getId();
    }

    @Override
    public User getById(Long userId) {
        // 检查id是否为空
        if (userId == null) {
            return null;
        }

        // 查询用户并返回结果
        return userMapper.getById(userId);
    }

    @Override
    public void updateInfo(Long userId, String nickname, String phone, MultipartFile file) {
        // 检查各个字符是否为空
        if (StringUtils.isAllBlank(nickname, phone) && file.isEmpty()) {
            // 若全为空则之间返回
            return;
        }

        // 检查昵称格式
        if (StringUtils.isNotBlank(nickname) && !nickname.matches(UserConstant.NICKNAME_PATTERN)) {
            throw new UpdateFailException(MessageConstant.NICKNAME_FORMAT_ERROR);
        }

        // 检查手机号格式
        if (StringUtils.isNotBlank(phone) && !phone.matches(UserConstant.PHONE_PATTERN)) {
            throw new UpdateFailException(MessageConstant.PHONE_FORMAT_ERROR);
        }

        String avatar = null;
        if (!file.isEmpty()) {
            try {
                // 文件不为空则尝试上传到OSS
                avatar = AliOssUtil.upload(file.getBytes(), file.getOriginalFilename(), UserConstant.AVATAR_DIRECTORY);
            } catch (IOException e) {
                throw new UpdateFailException(MessageConstant.FILE_PROCESS_ERROR);
            }
        }

        User user = User.builder()
                .id(userId)
                .nickname(nickname)
                .phone(phone)
                .avatar(avatar)
                .build();

        userMapper.update(user);
    }
}
