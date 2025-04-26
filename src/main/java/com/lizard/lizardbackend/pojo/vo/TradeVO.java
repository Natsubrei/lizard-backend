package com.lizard.lizardbackend.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 交易记录VO
 */
@Data
public class TradeVO {
    /**
     * 交易记录id
     */
    private Long tradeId;

    /**
     * 付款用户id
     */
    private Long payerId;

    /**
     * 收款用户id
     */
    private Long payeeId;

    /**
     * 帖子id
     */
    private Long postId;

    /**
     * 交易状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}