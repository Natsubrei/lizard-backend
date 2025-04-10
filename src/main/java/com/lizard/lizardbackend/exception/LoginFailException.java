package com.lizard.lizardbackend.exception;

/**
 * 注册失败异常类
 */
public class LoginFailException extends BaseException{
    public LoginFailException(String msg) {
        super(msg);
    }
}
