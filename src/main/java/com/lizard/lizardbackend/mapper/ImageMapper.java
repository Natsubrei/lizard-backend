package com.lizard.lizardbackend.mapper;

import com.lizard.lizardbackend.pojo.entity.Image;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageMapper {
    @Insert("INSERT INTO image (id, post_id, url) VALUES (#{id}, #{postId}, #{url})")
    void insert(Image newImage);
}
