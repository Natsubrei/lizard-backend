package com.lizard.lizardbackend.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 图片添加DTO
 */
@Data
public class ImageAddDTO implements Serializable {
    /**
     * 帖子id
     */
    private Long postId;

    /**
     * 帖子图片文件
     */
    private MultipartFile file;
}
