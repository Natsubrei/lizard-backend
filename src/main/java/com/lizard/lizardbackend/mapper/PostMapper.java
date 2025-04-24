package com.lizard.lizardbackend.mapper;

import com.lizard.lizardbackend.pojo.entity.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {
    void insert( Post newPost );
}
