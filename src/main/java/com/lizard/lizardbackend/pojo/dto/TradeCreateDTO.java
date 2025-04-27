package com.lizard.lizardbackend.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建交易DTO
 */
@Data
public class TradeCreateDTO implements Serializable {
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
}