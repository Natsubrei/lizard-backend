package com.lizard.lizardbackend.mapper;

import com.github.pagehelper.Page;
import com.lizard.lizardbackend.pojo.entity.Post;
import com.lizard.lizardbackend.pojo.vo.PostQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PostMapper {
    void insert(Post newPost);

    @Update("UPDATE post SET is_deleted = 1 WHERE id = #{postId}")
    void delete(Long postId);

    @Select("SELECT * FROM post WHERE id = #{postId}")
    Post getByPostId(long postId);

    @Select("SELECT * FROM post WHERE user_id = #{userId} AND is_deleted = 0 ORDER BY create_time DESC")
    Page<PostQueryVO> pageQueryByUserId(Long userId);

    @Select("SELECT * FROM post WHERE type = #{type} AND is_deleted = 0 ORDER BY create_time DESC")
    Page<PostQueryVO> pageQueryByType(Integer type);

    @Select("SELECT * FROM post WHERE is_deleted = 0 ORDER BY create_time DESC")
    Page<PostQueryVO> pageQueryByTime();

    @Select("SELECT * FROM post WHERE content LIKE CONCAT('%', #{word}, '%')")
    Page<PostQueryVO> pageQueryByWord(String word);
}
