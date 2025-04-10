package com.lizard.lizardbackend.interceptor;

import com.lizard.lizardbackend.constant.UserConstant;
import com.lizard.lizardbackend.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT令牌校验拦截器
 */
@Slf4j
@Component
public class JwtUserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        // 判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            // 当前拦截到的不是动态方法，直接放行
            return true;
        }

        // 从请求头中获取令牌
        String token = request.getHeader(JwtUtil.AUTH_HEADER_KEY);

        try {
            // JWT校验
            log.info("JWT校验：{}", token);
            String content = JwtUtil.verifyToken(token);

            // 获取用户id
            Long userId = Long.valueOf(content);
            log.info("当前用户id：{}", userId);

            // 设置attribute
            request.setAttribute(UserConstant.USER_ID, userId);
            return true;
        } catch (Exception e) {
            // 响应状态码401（未授权）
            response.setStatus(401);
            return false;
        }
    }
}
