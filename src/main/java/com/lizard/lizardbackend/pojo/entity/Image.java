package com.lizard.lizardbackend.pojo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 帖子图片类
 */
@Data
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
}
