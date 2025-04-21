package com.lizard.lizardbackend.mapper;

import com.lizard.lizardbackend.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE username = #{username}")
    User getByUsername(String username);

    void insert(User newUser);

    @Select("SELECT * FROM user WHERE id = #{userId}")
    User getById(Long userId);

    void update(User user);
}
