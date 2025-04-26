package com.lizard.lizardbackend.service.impl;

import com.lizard.lizardbackend.constant.MessageConstant;
import com.lizard.lizardbackend.constant.PostConstant;
import com.lizard.lizardbackend.exception.ImageAddException;
import com.lizard.lizardbackend.exception.PostCreateException;
import com.lizard.lizardbackend.exception.PostDeleteException;
import com.lizard.lizardbackend.mapper.ImageMapper;
import com.lizard.lizardbackend.mapper.PostMapper;
import com.lizard.lizardbackend.mapper.UserMapper;
import com.lizard.lizardbackend.pojo.entity.Image;
import com.lizard.lizardbackend.pojo.entity.User;
import com.lizard.lizardbackend.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import com.lizard.lizardbackend.pojo.entity.Post;
import com.lizard.lizardbackend.service.PostService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

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
        // 检查各字段是否为空
        if (StringUtils.isAnyBlank(title, content) || type == null || file == null || file.isEmpty()) {
            throw new PostCreateException(MessageConstant.ALL_FIELDS_REQUIRED);
        }

        // 检查标题长度是否合法
        if (title.length() > PostConstant.TITLE_MAX_LENGTH) {
            throw new PostCreateException(MessageConstant.TITLE_LENGTH_EXCEED_ERROR);
        }

        // 检查正文长度是否合法
        if (content.length() > PostConstant.CONTENT_MAX_LENGTH) {
            throw new PostCreateException(MessageConstant.CONTENT_LENGTH_EXCEED_ERROR);
        }

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

    @Override
    public void addImageToPost(Long postId, Long userId, MultipartFile file) {
        //检查文件是否存在
        if (file == null || file.isEmpty() ){
           throw new ImageAddException(MessageConstant.ALL_FIELDS_REQUIRED) ;
        }

        //检查用户Id和帖子是否对应
        if (!Objects.equals(postId, userId)){
            throw new ImageAddException(MessageConstant.USERID_MISMATCH_ERROR);
        }

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
                .postId(postId)
                .url(image)
                .build();

        imageMapper.insert(newImage);
    }

    @Override
    public void deletePost(Long postId, Long userId) {
        Post post = postMapper.getUserId(postId);
        //检查用户Id是否对应
        if (!Objects.equals(post.getUserId(), userId)){
            throw new PostDeleteException(MessageConstant.USERID_MISMATCH_ERROR);
        }

        Post newPost = Post.builder()
                .id(postId)
                .userId(userId)
                .build();

        postMapper.update(newPost);
    }
}
