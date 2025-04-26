package com.lizard.lizardbackend.pojo.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 交易记录实体类
 */
@Data
@Builder
public class Trade implements Serializable {
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
     * 是否被删除
     */
    private Integer isDeleted;

    /**
     * 付款方是否删除
     */
    private Integer payerDeleted;

    /**
     * 收款方是否删除
     */
    private Integer payeeDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}