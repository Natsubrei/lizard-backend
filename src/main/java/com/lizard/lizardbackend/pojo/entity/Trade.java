package com.lizard.lizardbackend.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 交易实体类
 */
@Data
public class Trade implements Serializable {
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
     * 创建时间
     */
    private LocalDateTime createTime;
}
