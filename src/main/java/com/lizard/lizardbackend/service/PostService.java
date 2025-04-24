package com.lizard.lizardbackend.service;

import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    /**
     * @param userId 用户Id
     * @param title 帖子标题
     * @param content 帖子内容
     * @param type 帖子类型
     * @param price 商品价格
     * @return 帖子唯一标识符帖子Id
     */
    Long createPost(Long userId, String title, String content, Integer type, Integer price, MultipartFile file);
}
