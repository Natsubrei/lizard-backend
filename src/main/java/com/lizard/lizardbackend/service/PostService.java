package com.lizard.lizardbackend.service;

import com.lizard.lizardbackend.pojo.vo.PostVO;
import com.lizard.lizardbackend.result.PageResult;
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
     * 给帖子添加图片
     * @param postId 帖子id
     * @param userId 用户id
     * @param file 图片文件
     */
    void addImageToPost(Long postId, Long userId, MultipartFile file);

    /**
     * 根据帖子id删除帖子
     * @param postId 帖子id
     * @param userId 用户id
     */
    void deleteById(Long postId, Long userId);

    /**
     * 根据帖子id查询帖子详情
     * @param postId 帖子id
     * @return 帖子详情
     */
    PostVO queryById(Long postId);

    /**
     * 根据用户id查询帖子
     * @param userId 用户id
     * @param pageNum 分页查询页号
     * @param pageSize 分页查询每页大小
     * @return 分页查询结果
     */
    PageResult pageQueryByUserId(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 根据帖子标签查询帖子
     * @param type 帖子标签
     * @param pageNum 分页查询页号
     * @param pageSize 分页查询每页大小
     * @return 分页查询结果
     */
    PageResult pageQueryByType(Integer type, Integer pageNum, Integer pageSize);

    /**
     * 根据时间查询帖子
     * @param pageNum 分页查询页号
     * @param pageSize 分页查询每页大小
     * @return 分页查询结果
     */
    PageResult pageQueryByTime(Integer pageNum, Integer pageSize);

    /**
     * 根据关键词查询帖子
     * @param word 关键词
     * @param pageNum 分页查询页号
     * @param pageSize 分页查询每页大小
     * @return 分页查询结果
     */
    PageResult pageQueryByWord(String word, Integer pageNum, Integer pageSize);
}
