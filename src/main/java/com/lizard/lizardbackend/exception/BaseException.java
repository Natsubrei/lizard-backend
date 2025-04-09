package com.lizard.lizardbackend.exception;

/**
 * 业务异常基础类
 */
public class BaseException extends RuntimeException {
    BaseException(String msg) {
        super(msg);
    }
}
