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
     * 帖子标题
     */
    private String title;

    /**
     * 帖子第一张图片URL
     */
    private String imageUrl;

    /**
     * 交易类型
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
