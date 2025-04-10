package com.lizard.lizardbackend.controller.user;

import com.lizard.lizardbackend.pojo.dto.UserLoginDTO;
import com.lizard.lizardbackend.pojo.dto.UserRegisterDTO;
import com.lizard.lizardbackend.pojo.entity.User;
import com.lizard.lizardbackend.pojo.vo.UserLoginVO;
import com.lizard.lizardbackend.result.Result;
import com.lizard.lizardbackend.service.UserService;
import com.lizard.lizardbackend.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    /**
     * 用户登录
     * @param userLoginDTO 用户登录DTO
     * @return 用户登录VO
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response) {
        log.info("用户登录：{}", userLoginDTO);

        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        User user = userService.login(username, password);

        // 登录成功则生成JWT字符串，并将其添加至响应头部返回给前端
        String token = JwtUtil.createToken(user.getId().toString());
        response.addHeader("token", token);

        // 用户信息脱敏
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, userLoginVO);

        return Result.success(userLoginVO);
    }
}
