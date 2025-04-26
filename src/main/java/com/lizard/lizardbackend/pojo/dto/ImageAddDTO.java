package com.lizard.lizardbackend.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class ImageAddDTO implements Serializable {
    /**
     * 图片Id
     */
    private Long id;

    /**
     * 帖子Id
     */
    private Long postId;

    /**
     * 帖子图片文件
     */
    private MultipartFile file;
}
