package com.lizard.lizardbackend.pojo.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 帖子图片类
 */
@Data
@Builder
public class Image implements Serializable {
    /**
     * 图片id
     */
    private Long id;

    /**
     * 帖子id
     */
    private Long postId;

    /**
     * 图片URL
     */
    private String url;

    /**
     * 是否被删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
