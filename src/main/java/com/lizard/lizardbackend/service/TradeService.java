package com.lizard.lizardbackend.service;

public interface TradeService {
    /**
     * 创建交易记录
     * @param userId 用户id
     * @param payerId 付款方id
     * @param payeeId 收款方id
     * @param postId 帖子id
     * @return 交易记录id
     */
    Long createTrade(Long userId, Long payerId, Long payeeId, Long postId);

    /**
     * 删除交易记录
     * @param userId 用户id
     * @param tradeId 交易记录id
     */
    void deleteTrade(Long userId, Long tradeId);

    /**
     * 取消交易
     * @param userId 用户id
     * @param tradeId 交易记录id
     */
    void cancelTrade(Long userId, Long tradeId);

    /**
     * 收款方建立交易
     */
    void establishTrade(Long userId, Long tradeId);

    /**
     * 付款方确定交易
     */
    void successTrade(Long userId,Long tradeId);
}