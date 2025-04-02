package com.lizard.lizardbackend.result;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T> 数据类型
 */
public class Result<T> implements Serializable {
    /**
     * 编码，1-成功，0-失败
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 携带的数据
     */
    private T data;

    /**
     * 创建成功Result
     * @return 成功Result
     * @param <T> 数据类型
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = 1;
        return result;
    }

    /**
     * 创建携带数据的成功Result
     * @param data 携带的数据
     * @return 携带数据的成功Result
     * @param <T> 数据类型
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 1;
        result.data = data;
        return result;
    }

    /**
     * 创建携带错误信息的错误Result
     * @param msg 错误信息
     * @return 携带错误信息的错误Result
     * @param <T> 数据类型
     */
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.code = 0;
        result.msg = msg;
        return result;
    }
}
