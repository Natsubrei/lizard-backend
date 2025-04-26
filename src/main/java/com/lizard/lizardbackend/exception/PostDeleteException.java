package com.lizard.lizardbackend.exception;

/**
 * 帖子中用户Id和当前用户Id不匹配异常
 */
public class PostDeleteException extends BaseException{
    public PostDeleteException(String msg) { super(msg); }
}
