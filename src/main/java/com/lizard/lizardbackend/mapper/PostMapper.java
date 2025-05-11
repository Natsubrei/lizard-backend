package com.lizard.lizardbackend.mapper;

import com.github.pagehelper.Page;
import com.lizard.lizardbackend.pojo.entity.Post;
import com.lizard.lizardbackend.pojo.vo.PostQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PostMapper {
    /**
     * 插入一条新的帖子记录
     * @param newPost 帖子对象
     */
    void insert(Post newPost);

    /**
     * 根据帖子id逻辑删除该帖子
     * @param postId 帖子id
     */
    @Update("UPDATE post SET is_deleted = 1 WHERE id = #{postId}")
    void delete(Long postId);

    /**
     * 根据帖子id设置其状态为已交易（status = 1）
     * @param postId 帖子id
     */
    @Update("UPDATE post SET status = 1 WHERE id = #{postId}")
    void traded(Long postId);

    /**
     * 根据帖子id获取帖子详情信息
     * @param postId 帖子id
     * @return 匹配的帖子对象
     */
    @Select("SELECT * FROM post WHERE id = #{postId}")
    Post getByPostId(Long postId);

    /**
     * 分页查询
     * @param post 包含查询条件的帖子对象
     * @return 分页查询结果
     */
    Page<PostQueryVO> pageQuery(Post post);

    /**
     * 根据关键词分页模糊查询帖子（在title和content中匹配），只查询未删除且未交易的帖子
     * @param word 关键词
     * @return 包含关键词的帖子分页查询结果
     */
    @Select("SELECT * FROM post WHERE status = 0 AND is_deleted = 0 " +
            "AND (title LIKE CONCAT ('%', #{word}, '%') OR content LIKE CONCAT('%', #{word}, '%')) " +
            "ORDER BY create_time DESC")
    Page<PostQueryVO> pageQueryByWord(String word);
}
