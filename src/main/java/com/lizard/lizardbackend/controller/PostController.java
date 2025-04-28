package com.lizard.lizardbackend.controller;

import com.lizard.lizardbackend.constant.UserConstant;
import com.lizard.lizardbackend.pojo.dto.PostCreateDTO;
import com.lizard.lizardbackend.pojo.dto.ImageAddDTO;
import com.lizard.lizardbackend.pojo.vo.PostVO;
import com.lizard.lizardbackend.result.PageResult;
import com.lizard.lizardbackend.result.Result;
import com.lizard.lizardbackend.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 帖子相关接口
 */
@Slf4j
@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    /**
     * 创建帖子
     * @param postCreateDTO 帖子创建DTO
     * @param request http请求
     * @return 创建成功返回帖子id，创建失败返回null
     */
    @PostMapping("/create")
    public Result<Long> createPost(@ModelAttribute PostCreateDTO postCreateDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(UserConstant.USER_ID);
        String title = postCreateDTO.getTitle();
        String content = postCreateDTO.getContent();
        Integer type = postCreateDTO.getType();
        Integer price = postCreateDTO.getPrice();
        MultipartFile file = postCreateDTO.getFile();

        log.info("创建新帖子：{}，{}，{}，{}, {}", userId, title, content, type, price);
        Long postId = postService.createPost(userId, title, content, type, price, file);

        return Result.success(postId);
    }

    /**
     * 向帖子中添加图片
     * @param imageAddDTO 图片添加DTO
     * @return 添加成功返回Result
     */
    @PostMapping("/image")
    public Result<?> addImageToPost(@ModelAttribute ImageAddDTO imageAddDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(UserConstant.USER_ID);
        Long postId = imageAddDTO.getPostId();
        MultipartFile file = imageAddDTO.getFile();

        log.info("插入图片：{}", postId);
        postService.addImageToPost(postId, userId, file);

        return Result.success();
    }

    /**
     * 根据帖子id删除帖子
     * @param postId 帖子id
     * @return 删除成功返回Result
     */
    @DeleteMapping("/{postId}")
    public Result<?> deletePost(@PathVariable Long postId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(UserConstant.USER_ID);

        log.info("删除帖子：{}, {}", postId, userId);
        postService.deleteById(postId, userId);

        return Result.success();
    }

    /**
     * 根据帖子id查询帖子详情
     * @param postId 帖子id
     * @return 帖子详情
     */
    @GetMapping("/{postId}")
    public Result<PostVO> queryPost(@PathVariable Long postId) {
        log.info("查询帖子：{}", postId);
        PostVO postVO = postService.queryById(postId);
        return Result.success(postVO);
    }

    /**
     * 根据用户id查询帖子
     * @param userId 用户id
     * @param pageNum 分页查询页号
     * @param pageSize 分页查询每页大小
     * @return 分页查询结果
     */
    @GetMapping("/list")
    public Result<PageResult> listByUserId(Long userId, Integer pageNum, Integer pageSize) {
        log.info("根据用户id查询帖子：{}, {}, {}", userId, pageNum, pageSize);
        PageResult pageResult = postService.pageQueryByUserId(userId, pageNum, pageSize);
        return Result.success(pageResult);
    }

    /**
     * 根据帖子标签查询帖子
     * @param type 帖子标签
     * @param pageNum 分页查询页号
     * @param pageSize 分页查询每页大小
     * @return 分页查询结果
     */
    @GetMapping("/type")
    public Result<PageResult> listByType(Integer type, Integer pageNum, Integer pageSize) {
        log.info("根据帖子类型查询帖子：{}, {}, {}", type, pageNum, pageSize);
        PageResult pageResult = postService.pageQueryByType(type, pageNum, pageSize);
        return Result.success(pageResult);
    }

    /**
     * 根据时间顺序查询帖子
     * @param pageNum 分页查询页号
     * @param pageSize 分页查询每页大小
     * @return 分页查询结果
     */
    @GetMapping("time")
    public Result<PageResult> listByTime(Integer pageNum, Integer pageSize) {
        log.info("根据时间顺序查询帖子：{}， {}", pageNum, pageSize);
        PageResult pageResult = postService.pageQueryByTime(pageNum, pageSize);
        return Result.success(pageResult);
    }

    /**
     * 根据关键词查询提欸子
     * @param word 关键词
     * @param pageNum 分页查询页号
     * @param pageSize 分页查询每页大小
     * @return 分页查询结果
     */
    @GetMapping("search")
    public Result<PageResult> listBySearch(String word, Integer pageNum, Integer pageSize) {
        log.info("根据关键词搜索帖子：{}, {}, {}", word, pageNum, pageSize);
        PageResult pageResult = postService.pageQueryByWord(word, pageNum, pageSize);
        return Result.success(pageResult);
    }
}