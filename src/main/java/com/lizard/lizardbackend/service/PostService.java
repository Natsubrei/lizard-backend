package com.lizard.lizardbackend.service;

import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    /**
     * 创建帖子
     * @param userId 用户id
     * @param title 帖子标题
     * @param content 帖子内容
     * @param type 帖子类型
     * @param price 商品价格
     * @return 帖子唯一标识符帖子id
     */
    Long createPost(Long userId, String title, String content, Integer type, Integer price, MultipartFile file);

    /**
     * 向帖子中添加图片
     * @param postId 帖子id
     */
    void addImageToPost(Long postId, Long userId, MultipartFile file);

    /**
     * 根据帖子id删除帖子
     * @param postId 帖子id
     * @param userId 用户id
     */
    void deletePost(Long postId, Long userId);
}
