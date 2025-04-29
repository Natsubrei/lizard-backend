package com.lizard.lizardbackend.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 帖子查询VO
 */
@Data
public class TradeQueryVO implements Serializable {
    /**
     * 交易记录id
     */
    private Long id;

    /**
     * 付款方id
     */
    private Long payerId;

    /**
     * 收款方id
     */
    private Long payeeId;

    /**
     * 帖子id
     */
    private Long postId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
