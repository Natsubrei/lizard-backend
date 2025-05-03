package com.lizard.lizardbackend.mapper;

import com.lizard.lizardbackend.pojo.entity.Image;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ImageMapper {
    @Insert("INSERT INTO image (id, post_id, url) VALUES (#{id}, #{postId}, #{url})")
    void insert(Image newImage);

    @Select("SELECT url FROM image WHERE post_id = #{postId} AND is_deleted = 0")
    List<String> getByPostId(Long postId);

    @Update("UPDATE image SET is_deleted = 1 WHERE post_id = #{postId}")
    void deleteByPostId(Long postId);
}
