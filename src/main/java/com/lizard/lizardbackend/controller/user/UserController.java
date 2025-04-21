package com.lizard.lizardbackend.controller.user;

import com.lizard.lizardbackend.constant.UserConstant;
import com.lizard.lizardbackend.pojo.dto.UserLoginDTO;
import com.lizard.lizardbackend.pojo.dto.UserPwdUpdateDTO;
import com.lizard.lizardbackend.pojo.dto.UserRegisterDTO;
import com.lizard.lizardbackend.pojo.dto.UserInfoUpdateDTO;
import com.lizard.lizardbackend.pojo.entity.User;
import com.lizard.lizardbackend.pojo.vo.UserVO;
import com.lizard.lizardbackend.result.Result;
import com.lizard.lizardbackend.service.UserService;
import com.lizard.lizardbackend.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户相关接口
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

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
     * @return 用户id
     */
    @PostMapping("/login")
    public Result<Long> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response) {
        log.info("用户登录：{}", userLoginDTO);

        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        Long userId = userService.login(username, password);

        // 登录成功则生成JWT字符串，并将其添加至响应头部返回给前端
        String token = JwtUtil.createToken(userId.toString());
        response.addHeader("token", token);

        return Result.success(userId);
    }

    /**
     * 查询当前登录用户信息
     * @param request http请求
     * @return 脱敏后的用户信息
     */
    @GetMapping("/current")
    public Result<UserVO> current(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(UserConstant.USER_ID);

        User user = userService.getById(userId);
        log.info("当前用户为：{}", user.getUsername());

        // 用户信息脱敏
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        return Result.success(userVO);
    }

    /**
     * 修改用户信息
     * @param userUpdateDTO 用户信息更新DTO
     * @param request http请求
     * @return 修改成功则返回成功Result
     */
    @PutMapping("/info")
    public Result<?> updateInfo(@ModelAttribute UserInfoUpdateDTO userUpdateDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(UserConstant.USER_ID);

        String nickname = userUpdateDTO.getNickname();
        String phone = userUpdateDTO.getPhone();
        MultipartFile file = userUpdateDTO.getFile();

        log.info("用户信息更新：{}，{}，{}，{}", userId, nickname, phone, file.getName());
        userService.updateInfo(userId, nickname, phone, file);

        return Result.success();
    }

    /**
     * 修改用户密码
     * @param userPwdUpdateDTO 用户密码更新DTO
     * @param request http请求
     * @return 修改成功则返回成功Result
     */
    @PutMapping("/password")
    public Result<?> updatePassword(@RequestBody UserPwdUpdateDTO userPwdUpdateDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(UserConstant.USER_ID);

        String oldPassword = userPwdUpdateDTO.getOldPassword();
        String newPassword = userPwdUpdateDTO.getNewPassword();
        String confirmNewPassword = userPwdUpdateDTO.getConfirmNewPassword();

        log.info("用户修改密码：{}，{}", userId, userPwdUpdateDTO);
        userService.updatePassword(userId, oldPassword, newPassword, confirmNewPassword);

        return Result.success();
    }
}
