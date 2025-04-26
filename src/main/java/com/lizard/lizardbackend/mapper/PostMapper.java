package com.lizard.lizardbackend.mapper;

import com.lizard.lizardbackend.pojo.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PostMapper {
    void insert(Post newPost);

    void update(Post newPost);

    @Select("SELECT * FROM post WHERE id = #{postId}")
    Post getUserId(long postId);
}
