package com.lizard.lizardbackend.constant;

/**
 * 交易相关常量类
 */
public interface TradeConstant {
    /**
     * 交易记录id常量
     */
    String TRADE_ID = "TRADE_ID";

    /**
     * 交易状态 - 进行中
     */
    int STATUS_IN_PROGRESS = 0;

    /**
     * 交易状态 - 已完成
     */
    int STATUS_COMPLETED = 1;

    /**
     * 交易状态 - 已取消
     */
    int STATUS_CANCELLED = 2;

    /**
     * 删除类型 - 逻辑删除
     */
    int DELETE_TYPE_LOGICAL = 0;

    /**
     * 删除类型 - 物理删除
     */
    int DELETE_TYPE_PHYSICAL = 1;
}