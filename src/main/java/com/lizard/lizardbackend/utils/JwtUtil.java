package com.lizard.lizardbackend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.lizard.lizardbackend.constant.MessageConstant;
import com.lizard.lizardbackend.exception.UtilRuntimeException;

import java.util.Date;

/**
 * JWT工具类
 */
public class JwtUtil {
    /**
     * JWT密钥
     */
    private static final String SECRET_KEY = "Lizard";

    /**
     * JWT有效时间（ms）
     */
    private static final Long EXPIRATION_TIME = 1000L * 60 * 60 * 2;

    /**
     * JWT对应请求头的Key
     */
    public static final String AUTH_HEADER_KEY = "Authorization";

    /**
     * 创建JWT
     * @param content 携带的用户信息
     * @return JWT字符串
     */
    public static String createToken(String content) {
        return JWT.create()
                .withSubject(content)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    /**
     * 验证JWT
     * @param token JWT字符串
     * @return 携带的用户信息
     */
    public static String verifyToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC512(SECRET_KEY)).build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new UtilRuntimeException(MessageConstant.LOGIN_EXPIRED);
        } catch (IllegalArgumentException e) {
            throw new UtilRuntimeException(MessageConstant.TOKEN_VERIFICATION_FAILED);
        }
    }
}
