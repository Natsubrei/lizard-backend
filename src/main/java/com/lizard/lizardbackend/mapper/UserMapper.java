package com.lizard.lizardbackend.mapper;

import com.lizard.lizardbackend.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /**
     * 新增一条用户信息
     * @param newUser 用户对象
     */
    void insert(User newUser);

    /**
     * 更新用户信息
     * @param user 包含更新信息的用户对象
     */
    void update(User user);

    /**
     * 根据用户id查询用户
     * @param userId 用户id
     * @return 用户对象
     */
    @Select("SELECT * FROM user WHERE id = #{userId}")
    User getById(Long userId);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    @Select("SELECT * FROM user WHERE username = #{username}")
    User getByUsername(String username);
}
