package com.lizard.lizardbackend.mapper;

import com.lizard.lizardbackend.pojo.entity.Image;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ImageMapper {
    /**
     * 插入一条新图片记录
     * @param newImage 图片对象
     */
    @Insert("INSERT INTO image (id, post_id, url) VALUES (#{id}, #{postId}, #{url})")
    void insert(Image newImage);

    /**
     * 根据帖子id将其关联的所有图片标记为已删除（逻辑删除）
     * @param postId 帖子id
     */
    @Update("UPDATE image SET is_deleted = 1 WHERE post_id = #{postId}")
    void deleteByPostId(Long postId);

    /**
     * 根据帖子id查询其所有图片URL列表
     * @param postId 帖子id
     * @return 该帖子关联的所有图片URL
     */
    @Select("SELECT url FROM image WHERE post_id = #{postId}")
    List<String> getByPostId(Long postId);
}
