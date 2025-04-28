package com.lizard.lizardbackend.constant;

/**
 * 交易相关常量类
 */
public interface TradeConstant {
    /**
     * 交易状态 - 待交易
     */
    int STATUS_IN_PROGRESS = 0;

    /**
     * 交易状态 - 交易建立
     */
    int STATUS_ESTABLISHED = 1;

    /**
     * 交易状态 - 交易成功
     */
    int STATUS_SUCCESS = 2;

    /**
     * 交易状态 - 交易失败
     */
    int STATUS_FAILURE = 3;}