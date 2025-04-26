package com.lizard.lizardbackend.pojo.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 帖子实体类
 */
@Data
@Builder
public class Post implements Serializable {
    /**
     * 帖子id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 发布者用户名
     */
    private String username;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 正文
     */
    private String content;

    /**
     * 正文预览
     */
    private String contentBrief;

    /**
     * 交易类型
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 预估价格
     */
    private Integer price;

    /**
     * 是否被删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
