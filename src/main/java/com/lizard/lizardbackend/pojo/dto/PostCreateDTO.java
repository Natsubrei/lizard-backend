package com.lizard.lizardbackend.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 帖子创建DTO
 */
@Data
public class PostCreateDTO implements Serializable {
    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 帖子标签
     */
    private Integer type;

    /**
     * 预估价格
     */
    private Integer price;

    /**
     * 帖子图片文件
     */
    private MultipartFile file;
}
