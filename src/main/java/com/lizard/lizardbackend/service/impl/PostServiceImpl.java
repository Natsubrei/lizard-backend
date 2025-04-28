package com.lizard.lizardbackend.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lizard.lizardbackend.constant.MessageConstant;
import com.lizard.lizardbackend.constant.PostConstant;
import com.lizard.lizardbackend.exception.*;
import com.lizard.lizardbackend.mapper.ImageMapper;
import com.lizard.lizardbackend.mapper.PostMapper;
import com.lizard.lizardbackend.mapper.UserMapper;
import com.lizard.lizardbackend.pojo.entity.Image;
import com.lizard.lizardbackend.pojo.entity.User;
import com.lizard.lizardbackend.pojo.vo.PostQueryVO;
import com.lizard.lizardbackend.pojo.vo.PostVO;
import com.lizard.lizardbackend.result.PageResult;
import com.lizard.lizardbackend.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import com.lizard.lizardbackend.pojo.entity.Post;
import com.lizard.lizardbackend.service.PostService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

import static com.lizard.lizardbackend.constant.PostConstant.*;

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

        // 检查交易类型是否合法
        if (!Objects.equals(type, STATUS_BUY) && !Objects.equals(type, STATUS_SELL) && !Objects.equals(type, STATUS_RENT)
                && !Objects.equals(type, STATUS_LEND)) {
            throw new PostCreateException(MessageConstant.TYPE_ERROR);
        }

        // 通过用户id获取用户名
        User user = userMapper.getById(userId);
        String username = user.getUsername();

        // 截取正文前面一部分作为预览
        String contentBrief = content.substring(0, Math.min(64, content.length()));

        Post newPost = Post.builder()
                .userId(userId)
                .username(username)
                .title(title)
                .content(content)
                .contentBrief(contentBrief)
                .type(type)
                .price(price)
                .build();

        postMapper.insert(newPost);

        String image = null;
        if (!file.isEmpty()) {
            try {
                // 文件不为空则尝试上传到OSS
                image = AliOssUtil.upload(file.getBytes(), file.getOriginalFilename(), PostConstant.IMAGE_DIRECTORY);
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
        // 检查文件是否为空
        if (file == null || file.isEmpty() ){
           throw new ImageAddException(MessageConstant.ALL_FIELDS_REQUIRED) ;
        }

        Post post = postMapper.getByPostId(postId);

        // 检查帖子是否存在
        if (post == null || post.getIsDeleted() == 1) {
            throw new PostCreateException(MessageConstant.POST_NOT_EXISTS);
        }

        // 检查用户帖子是否属于当前用户
        if (!Objects.equals(post.getUserId(), userId)) {
            throw new PostCreateException(MessageConstant.POST_OWNER_MISMATCH_ERROR);
        }

        String image = null;
        if (!file.isEmpty()) {
            try {
                // 文件不为空则尝试上传到OSS
                image = AliOssUtil.upload(file.getBytes(), file.getOriginalFilename(), PostConstant.IMAGE_DIRECTORY);
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
    public void deleteById(Long postId, Long userId) {
        Post post = postMapper.getByPostId(postId);

        // 检查帖子是否存在
        if (post == null || post.getIsDeleted() == 1) {
            throw new PostDeleteException(MessageConstant.POST_NOT_EXISTS);
        }

        // 检查用户帖子是否属于当前用户
        if (!Objects.equals(post.getUserId(), userId)) {
            throw new PostDeleteException(MessageConstant.POST_OWNER_MISMATCH_ERROR);
        }

        postMapper.delete(postId);
    }

    @Override
    public PostVO queryById(Long postId) {
        Post post = postMapper.getByPostId(postId);

        // 检查帖子是否存在
        if (post == null) {
            throw new PostQueryException(MessageConstant.POST_NOT_EXISTS);
        }

        PostVO postVO = new PostVO();
        BeanUtils.copyProperties(post, postVO);

        return postVO;
    }

    @Override
    public PageResult pageQueryByUserId(Long userId, Integer pageNum, Integer pageSize) {
        // 使用PageHelper进行分页查询
        PageHelper.startPage(pageNum, pageSize);
        Page<PostQueryVO> page = postMapper.pageQueryByUserId(userId);

        // 查询失败则抛出异常
        if (page == null) {
            throw new PostQueryException(MessageConstant.PAGE_QUERY_ERROR);
        }

        // 返回帖子总数以及此次查询的结果
        return new PageResult(page.getTotal(), page);
    }

    @Override
    public PageResult pageQueryByType(Integer type, Integer pageNum, Integer pageSize) {
        // 使用PageHelper进行分页查询
        PageHelper.startPage(pageNum, pageSize);
        Page<PostQueryVO> page = postMapper.pageQueryByType(type);

        // 查询失败则抛出异常
        if (page == null) {
            throw new PostQueryException(MessageConstant.PAGE_QUERY_ERROR);
        }

        //返回帖子总数以及此次查询结果
        return new PageResult(page.getTotal(), page);
    }

    @Override
    public PageResult pageQueryByTime(Integer pageNum, Integer pageSize) {
        // 使用PageHelper进行分页查询
        PageHelper.startPage(pageNum, pageSize);
        Page<PostQueryVO> page = postMapper.pageQueryByTime();

        //查询失败抛出异常
        if(page == null) {
            throw new PostQueryException(MessageConstant.PAGE_QUERY_ERROR);
        }

        //返回帖子总数以及此次查询结果
        return new PageResult(page.getTotal(), page);
    }
}
