package com.lizard.lizardbackend.service.impl;

import com.lizard.lizardbackend.constant.MessageConstant;
import com.lizard.lizardbackend.constant.UserConstant;
import com.lizard.lizardbackend.exception.UserServiceException;
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
            throw new UserServiceException(MessageConstant.USER_OR_PASSWORD_IS_NULL);
        }

        // 检查两次输入密码是否一致
        if (!confirmPassword.equals(password)) {
            throw new UserServiceException(MessageConstant.PASSWORDS_NOT_CONSISTENT);
        }

        // 检查用户名格式是否正确
        if (!username.matches(UserConstant.USERNAME_PATTERN)) {
            throw new UserServiceException(MessageConstant.USERNAME_FORMAT_ERROR);
        }

        // 检查密码格式是否正确
        if (!password.matches(UserConstant.PASSWORD_PATTERN)) {
            throw new UserServiceException(MessageConstant.PASSWORD_FORMAT_ERROR);
        }

        // 检查用户是否存在
        User user = userMapper.getByUsername(username);
        if (user != null) {
            throw new UserServiceException(MessageConstant.USERNAME_EXISTS);
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
            throw new UserServiceException(MessageConstant.USER_OR_PASSWORD_IS_NULL);
        }

        // 检查用户名格式是否正确
        if (!username.matches(UserConstant.USERNAME_PATTERN)) {
            throw new UserServiceException(MessageConstant.USERNAME_FORMAT_ERROR);
        }

        // 检查密码格式是否正确
        if (!password.matches(UserConstant.PASSWORD_PATTERN)) {
            throw new UserServiceException(MessageConstant.PASSWORD_FORMAT_ERROR);
        }

        // 检查用户是否存在
        User user = userMapper.getByUsername(username);
        if (user == null || user.getIsDeleted() == 1) {
            throw new UserServiceException(MessageConstant.USERNAME_OR_PASSWORD_ERROR);
        }

        // 检查密码是否正确
        String encryptPwd = DigestUtils.md5DigestAsHex((password + username).getBytes(StandardCharsets.UTF_8));
        if (!encryptPwd.equals(user.getPassword())) {
            throw new UserServiceException(MessageConstant.USERNAME_OR_PASSWORD_ERROR);
        }

        return user.getId();
    }

    @Override
    public User getById(Long userId) {
        // 查询用户并返回结果
        return userMapper.getById(userId);
    }

    @Override
    public void updateInfo(Long userId, String nickname, String phone, MultipartFile file) {
        // 检查各字符是否为空
        if (StringUtils.isAllBlank(nickname, phone) && (file == null || file.isEmpty())) {
            // 若全为空则之间返回
            return;
        }

        // 检查昵称格式
        if (StringUtils.isNotBlank(nickname) && !nickname.matches(UserConstant.NICKNAME_PATTERN)) {
            throw new UserServiceException(MessageConstant.NICKNAME_FORMAT_ERROR);
        }

        // 检查手机号格式
        if (StringUtils.isNotBlank(phone) && !phone.matches(UserConstant.PHONE_PATTERN)) {
            throw new UserServiceException(MessageConstant.PHONE_FORMAT_ERROR);
        }

        String avatar = null;
        if (file != null && !file.isEmpty()) {
            try {
                // 文件不为空则尝试上传到OSS
                avatar = AliOssUtil.upload(file.getBytes(), file.getOriginalFilename(), UserConstant.AVATAR_DIRECTORY);
            } catch (IOException e) {
                throw new UserServiceException(MessageConstant.FILE_PROCESS_ERROR);
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

    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword, String confirmNewPassword) {
        // 检查各字段是否为空
        if (StringUtils.isAnyBlank(oldPassword, newPassword, confirmNewPassword)) {
            throw new UserServiceException(MessageConstant.ALL_FIELDS_REQUIRED);
        }

        // 检查新密码与旧密码是否一致
        if (newPassword.equals(oldPassword)) {
            throw new UserServiceException(MessageConstant.PASSWORD_DUPLICATION_NOT_ALLOWED);
        }

        // 检查两次输入的新密码是否一致
        if (!confirmNewPassword.equals(newPassword)) {
            throw new UserServiceException(MessageConstant.NEW_PASSWORDS_NOT_CONSISTENT);
        }

        // 检查旧密码格式是否正确
        if (!oldPassword.matches(UserConstant.PASSWORD_PATTERN)) {
            throw new UserServiceException(MessageConstant.OLD_PASSWORD_FORMAT_ERROR);
        }

        // 检查新密码格式是否正确
        if (!newPassword.matches(UserConstant.PASSWORD_PATTERN)) {
            throw new UserServiceException(MessageConstant.NEW_PASSWORD_FORMAT_ERROR);
        }

        // 通过用户id获取用户名
        User user = userMapper.getById(userId);
        String username = user.getUsername();

        // 检查旧密码是否正确
        String encryptPwd = DigestUtils.md5DigestAsHex((oldPassword + username).getBytes(StandardCharsets.UTF_8));
        if (!encryptPwd.equals(user.getPassword())) {
            throw new UserServiceException(MessageConstant.OLD_PASSWORD_INPUT_ERROR);
        }

        // 对新密码进行加密
        String newEncryptPwd = DigestUtils.md5DigestAsHex((newPassword + username).getBytes(StandardCharsets.UTF_8));

        User userUpdate = User.builder()
                .id(userId)
                .password(newEncryptPwd)
                .build();

        userMapper.update(userUpdate);
    }

    @Override
    public void deactivate(Long userId, String password) {
        // 检查密码格式是否正确
        if (!password.matches(UserConstant.PASSWORD_PATTERN)) {
            throw new UserServiceException(MessageConstant.PASSWORD_FORMAT_ERROR);
        }

        // 通过用户id获取用户名
        User user = userMapper.getById(userId);
        String username = user.getUsername();

        // 检查密码是否正确
        String encryptPwd = DigestUtils.md5DigestAsHex((password + username).getBytes(StandardCharsets.UTF_8));
        if (!encryptPwd.equals(user.getPassword())) {
            throw new UserServiceException(MessageConstant.PASSWORD_INPUT_ERROR);
        }

        // 逻辑删除字段置为1，代表删除
        User userUpdate = User.builder()
                .id(userId)
                .isDeleted(1)
                .build();

        userMapper.update(userUpdate);
    }
}
