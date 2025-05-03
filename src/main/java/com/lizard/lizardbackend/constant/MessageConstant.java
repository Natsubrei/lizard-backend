package com.lizard.lizardbackend.constant;

/**
 * 信息提示常量类
 */
public interface MessageConstant {
    String USER_OR_PASSWORD_IS_NULL = "用户名与密码均不能为空";
    String PASSWORDS_NOT_CONSISTENT = "两次密码输入不一致";
    String USERNAME_FORMAT_ERROR = "用户名格式错误";
    String PASSWORD_FORMAT_ERROR = "密码格式错误";
    String NICKNAME_FORMAT_ERROR = "昵称格式错误";
    String PHONE_FORMAT_ERROR = "手机号格式错误";
    String USERNAME_EXISTS = "用户名已存在";
    String USERNAME_OR_PASSWORD_ERROR = "用户名或密码错误";
    String FILE_PROCESS_ERROR = "文件处理异常";
    String ALL_FIELDS_REQUIRED = "各个字段不得为空";
    String PASSWORD_DUPLICATION_NOT_ALLOWED = "新密码不得与旧密码一致";
    String NEW_PASSWORDS_NOT_CONSISTENT = "两次输入的新密码不一致";
    String OLD_PASSWORD_FORMAT_ERROR = "旧密码格式错误";
    String NEW_PASSWORD_FORMAT_ERROR = "新密码格式错误";
    String OLD_PASSWORD_INPUT_ERROR = "旧密码输入错误";
    String PASSWORD_INPUT_ERROR = "密码输入错误";
    String LOGIN_EXPIRED = "登录已过期，请重新登录";
    String TOKEN_VERIFICATION_FAILED = "Token验证失败";
    String FILE_UPLOAD_FAILED = "文件上传失败";
    String FILE_NAME_FORMAT_ERROR = "文件名称格式错误";
    String TITLE_LENGTH_EXCEED_ERROR = "标题长度过长";
    String CONTENT_LENGTH_EXCEED_ERROR = "正文长度过长";
    String POST_OWNER_MISMATCH_ERROR = "帖子所属用户与当前用户不匹配";
    String POST_NOT_EXISTS = "帖子不存在";
    String PAGE_QUERY_ERROR = "分页查询错误";
    String TRADE_ERROR = "交易失败";
    String TRADING_USER_NOT_EXISTS = "交易用户不存在";
    String ITEM_HAS_BEEN_TRADED = "该物品已被交易";
    String TRADE_NOT_EXISTS = "交易记录不存在";
    String TRADE_EXISTS = "交易记录已存在";
    String TRADE_OWNER_MISMATCH_ERROR = "交易记录所属用户与当前用户不匹配";
    String TRADE_IN_PROGRESS = "交易进行中，无法删除记录";
    String TRADE_OVERED = "交易已结束，无法取消交易";
    String TRADE_PAY_MISMATCH_ERROR = "用户非交易双方，无权取消";
    String TYPE_ERROR = "交易类型不存在";
    String TRADE_OVER = "交易已完成，操作终止";
    String TRADE_ESTABLISH = "交易已建立，无法再次建立";
    String TRADE_PAYEE_MISMATCH_ERROR = "用户非收款方，无权建立交易";
    String TRADE_PAYER_MISMATCH_ERROR = "用户非付款方，无权建确定交易";
    String TRADE_NOY_ESTABLISH = "交易未建立，无法确定";
    String TRADE_DELETED_OVER = "交易已被某方删除，无法操作";
}
