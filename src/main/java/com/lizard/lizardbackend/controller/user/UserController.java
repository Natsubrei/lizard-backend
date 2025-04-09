package com.lizard.lizardbackend.controller.user;

import com.lizard.lizardbackend.pojo.dto.UserRegisterDTO;
import com.lizard.lizardbackend.result.Result;
import com.lizard.lizardbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 用户相关接口
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册
     * @param userRegisterDTO 用户注册DTO
     * @return 注册成功则返回用户id，注册失败则返回null
     */
    @PostMapping("/register")
    public Result<Long> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册：{}", userRegisterDTO);

        String username = userRegisterDTO.getUsername();
        String password = userRegisterDTO.getPassword();
        String confirmPassword = userRegisterDTO.getConfirmPassword();

        Long userId = userService.register(username, password, confirmPassword);

        return Result.success(userId);
    }
}
