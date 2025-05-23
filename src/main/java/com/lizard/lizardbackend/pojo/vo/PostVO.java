package com.lizard.lizardbackend.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 帖子详情VO
 */
@Data
public class PostVO implements Serializable {
    /**
     * 帖子id
     */
    private Long id;

    /**
     * 发布者id
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
     * 帖子图片URL数组
     */
    private List<String> imageUrls;

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
     * 创建时间
     */
    private LocalDateTime createTime;
}
