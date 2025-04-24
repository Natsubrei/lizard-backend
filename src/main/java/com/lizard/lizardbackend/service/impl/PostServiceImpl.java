package com.lizard.lizardbackend.service.impl;
import com.lizard.lizardbackend.constant.MessageConstant;
import com.lizard.lizardbackend.constant.PostConstant;
import com.lizard.lizardbackend.exception.PostCreateException;
import com.lizard.lizardbackend.exception.UpdateFailException;
import com.lizard.lizardbackend.mapper.ImageMapper;
import com.lizard.lizardbackend.mapper.PostMapper;
import com.lizard.lizardbackend.mapper.UserMapper;
import com.lizard.lizardbackend.pojo.entity.Image;
import com.lizard.lizardbackend.pojo.entity.User;
import com.lizard.lizardbackend.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import com.lizard.lizardbackend.pojo.entity.Post;
import com.lizard.lizardbackend.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final ImageMapper imageMapper;

    public PostServiceImpl(PostMapper postMapper, UserMapper userMapper, ImageMapper imageMapper) {
        this.postMapper = postMapper;
        this.userMapper = userMapper;
        this.imageMapper = imageMapper;
    }

    @Override
    public Long createPost(Long userId, String title, String content, Integer type, Integer price, MultipartFile file) {
        // 通过用户id获取用户名
        User user = userMapper.getById(userId);
        String username = user.getUsername();

        Post newPost = Post.builder()
                        .userId(userId)
                        .username(username)
                        .title(title)
                        .content(content)
                        .type(type)
                        .price(price)
                        .build();

        postMapper.insert(newPost);

        String image = null;
        if (!file.isEmpty()) {
            try {
                // 文件不为空则尝试上传到OSS
                image = AliOssUtil.upload(file.getBytes(), file.getOriginalFilename(), PostConstant.IMAGE_DIRECTOR);
            } catch (IOException e) {
                throw new PostCreateException(MessageConstant.FILE_PROCESS_ERROR);
            }
        }

        Image newImage = Image.builder()
                .postId(newPost.getId())
                .url(image)
                .build();

        imageMapper.insert(newImage);

        return newPost.getId();
    }
}
