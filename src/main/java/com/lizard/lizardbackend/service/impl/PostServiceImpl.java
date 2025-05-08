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
import java.util.List;
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
            throw new PostServiceException(MessageConstant.ALL_FIELDS_REQUIRED);
        }

        // 检查标题长度是否合法
        if (title.length() > PostConstant.TITLE_MAX_LENGTH) {
            throw new PostServiceException(MessageConstant.TITLE_LENGTH_EXCEED_ERROR);
        }

        // 检查正文长度是否合法
        if (content.length() > PostConstant.CONTENT_MAX_LENGTH) {
            throw new PostServiceException(MessageConstant.CONTENT_LENGTH_EXCEED_ERROR);
        }

        // 检查交易类型是否合法
        if (!Objects.equals(type, PostConstant.TYPE_BUY) && !Objects.equals(type, PostConstant.TYPE_SELL)
                && !Objects.equals(type, PostConstant.TYPE_RENT) && !Objects.equals(type, PostConstant.TYPE_LEND)) {
            throw new PostServiceException(MessageConstant.TYPE_ERROR);
        }

        // 通过用户id获取用户名
        User user = userMapper.getById(userId);
        String username = user.getUsername();

        // 截取正文前面一部分作为预览
        String contentBrief = content.substring(0, Math.min(64, content.length()));

        // 上传帖子第一张图片
        String imageUrl = null;
        if (!file.isEmpty()) {
            try {
                // 文件不为空则尝试上传到OSS
                imageUrl = AliOssUtil.upload(file.getBytes(), file.getOriginalFilename(), PostConstant.IMAGE_DIRECTORY);
            } catch (IOException e) {
                throw new PostServiceException(MessageConstant.FILE_PROCESS_ERROR);
            }
        }

        Post newPost = Post.builder()
                .userId(userId)
                .username(username)
                .title(title)
                .content(content)
                .contentBrief(contentBrief)
                .imageUrl(imageUrl)
                .type(type)
                .price(price)
                .build();

        postMapper.insert(newPost);

        Image newImage = Image.builder()
                .postId(newPost.getId())
                .url(imageUrl)
                .build();

        imageMapper.insert(newImage);

        return newPost.getId();
    }

    @Override
    public void addImageToPost(Long postId, Long userId, MultipartFile file) {
        // 检查文件是否为空
        if (file == null || file.isEmpty() ){
           throw new PostServiceException(MessageConstant.ALL_FIELDS_REQUIRED) ;
        }

        Post post = postMapper.getByPostId(postId);

        // 检查帖子是否存在
        if (post == null || post.getIsDeleted() == 1) {
            throw new PostServiceException(MessageConstant.POST_NOT_EXISTS);
        }

        // 检查用户帖子是否属于当前用户
        if (!Objects.equals(post.getUserId(), userId)) {
            throw new PostServiceException(MessageConstant.POST_OWNER_MISMATCH_ERROR);
        }

        // 上传帖子图片
        String url = null;
        if (!file.isEmpty()) {
            try {
                // 文件不为空则尝试上传到OSS
                url = AliOssUtil.upload(file.getBytes(), file.getOriginalFilename(), PostConstant.IMAGE_DIRECTORY);
            } catch (IOException e) {
                throw new PostServiceException(MessageConstant.FILE_PROCESS_ERROR);
            }
        }

        Image newImage = Image.builder()
                .postId(postId)
                .url(url)
                .build();

        imageMapper.insert(newImage);
    }

    @Override
    public void deleteById(Long postId, Long userId) {
        Post post = postMapper.getByPostId(postId);

        // 检查帖子是否存在
        if (post == null || post.getIsDeleted() == 1) {
            throw new PostServiceException(MessageConstant.POST_NOT_EXISTS);
        }

        // 检查用户帖子是否属于当前用户
        if (!Objects.equals(post.getUserId(), userId)) {
            throw new PostServiceException(MessageConstant.POST_OWNER_MISMATCH_ERROR);
        }

        // 检测物品是否已经被交易
        if (Objects.equals(post.getStatus(), PostConstant.STATUS_HAS_TRADED)) {
            throw new PostServiceException(MessageConstant.ITEM_HAS_BEEN_TRADED);
        }

        postMapper.delete(postId);

        imageMapper.deleteByPostId(postId);
    }

    @Override
    public PostVO queryById(Long postId) {
        Post post = postMapper.getByPostId(postId);

        // 检查帖子是否存在
        if (post == null) {
            throw new PostServiceException(MessageConstant.POST_NOT_EXISTS);
        }

        PostVO postVO = new PostVO();
        BeanUtils.copyProperties(post, postVO);

        // 获取帖子对应的图片URL
        List<String> imageUrls = imageMapper.getByPostId(postId);
        postVO.setImageUrls(imageUrls);

        return postVO;
    }

    @Override
    public PageResult pageQueryByUserId(Long userId, Integer pageNum, Integer pageSize) {
        // 使用PageHelper进行分页查询
        PageHelper.startPage(pageNum, pageSize);

        Post post = Post.builder()
                .userId(userId)
                .isDeleted(0)
                .build();
        Page<PostQueryVO> page = postMapper.pageQuery(post);

        // 查询失败则抛出异常
        if (page == null) {
            throw new PostServiceException(MessageConstant.PAGE_QUERY_ERROR);
        }

        // 返回帖子总数以及此次查询的结果
        return new PageResult(page.getTotal(), page);
    }

    @Override
    public PageResult pageQueryByType(Integer type, Integer pageNum, Integer pageSize) {
        // 使用PageHelper进行分页查询
        PageHelper.startPage(pageNum, pageSize);

        Post post = Post.builder()
                .type(type)
                .status(PostConstant.STATUS_NOT_TRADED)
                .isDeleted(0)
                .build();
        Page<PostQueryVO> page = postMapper.pageQuery(post);

        // 查询失败则抛出异常
        if (page == null) {
            throw new PostServiceException(MessageConstant.PAGE_QUERY_ERROR);
        }

        // 返回帖子总数以及此次查询结果
        return new PageResult(page.getTotal(), page);
    }

    @Override
    public PageResult pageQueryByTime(Integer pageNum, Integer pageSize) {
        // 使用PageHelper进行分页查询
        PageHelper.startPage(pageNum, pageSize);

        Post post = Post.builder()
                .status(PostConstant.STATUS_NOT_TRADED)
                .isDeleted(0)
                .build();
        Page<PostQueryVO> page = postMapper.pageQuery(post);

        // 查询失败抛出异常
        if(page == null) {
            throw new PostServiceException(MessageConstant.PAGE_QUERY_ERROR);
        }

        // 返回帖子总数以及此次查询结果
        return new PageResult(page.getTotal(), page);
    }

    @Override
    public PageResult pageQueryByWord(String word, Integer pageNum, Integer pageSize) {
        // 使用PageHelper进行分页查询
        PageHelper.startPage(pageNum, pageSize);
        Page<PostQueryVO> page = postMapper.pageQueryByWord(word);

        // 查询失败抛出异常
        if(page == null) {
            throw new PostServiceException(MessageConstant.PAGE_QUERY_ERROR);
        }

        // 返回帖子总数以及此次查询结果
        return new PageResult(page.getTotal(), page);
    }
}
